



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

5268a4c6128ca689d49fa95e269216d27f055502

















5268a4c6128ca689d49fa95e269216d27f055502


Switch branch/tag










heros


test


heros


IFDSSolverTest.java



Find file
Normal viewHistoryPermalink






IFDSSolverTest.java



3.27 KB









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
package heros;

import org.junit.Before;
import org.junit.Test;

import static heros.TestHelper.*;

public class IFDSSolverTest {

	private TestHelper helper;

	@Before
	public void before() {
		helper = new TestHelper();
	}
	
	@Test
	public void test() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "x")),
				normalStmt("b").succ("c", flow("x", "x")),
				callSite("c").calls("foo", flow("x", "y")).retSite("f", flow("x", "x")));
		
		helper.method("foo",
				startPoints("d"),
				normalStmt("d").succ("e", flow("y", "y", "z")),
				exitStmt("e").returns(over("c"), to("f"), flow("z", "u"), flow("y")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummary() {
		helper.method("foo", 
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "x")).retSite("b", flow("0", "y")),
				callSite("b").calls("bar", flow("y", "x")).retSite("c", flow("y")),
				normalStmt("c").succ("c0", flow("w", "0")));
		
		helper.method("bar",
				startPoints("d"),
				normalStmt("d").succ("e", flow("x", "z")),
				exitStmt("e").returns(over("a"), to("b"), flow("z", "y"))
							  .returns(over("b"), to("c"), flow("z", "w")));
		
		helper.runSolver(false, "a");








test case for reusing a summary on caller side

 


Johannes Lerch
committed
Jun 25, 2014




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



	}
	
	@Test
	public void reuseSummaryForRecursiveCall() {
		helper.method("foo",
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "1")).retSite("b", flow("0")),
				normalStmt("b").succ("c", flow("2", "3")));
		
		helper.method("bar",
				startPoints("g"),
				normalStmt("g").succ("h", flow("1", "1")).succ("i", flow("1", "1")),
				callSite("i").calls("bar", flow("1", "1")).retSite("h", flow("1")),
				exitStmt("h").returns(over("a"), to("b"), flow("1"), flow("2" ,"2"))
							.returns(over("i"), to("h"), flow("1","2"), flow("2", "2")));
				
		helper.runSolver(false, "a");








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




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


105


106


107


108


109


110


111


112


113



	}
	
	@Test
	public void branch() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b1", flow("0", "x")).succ("b2", flow("0", "x")),
				normalStmt("b1").succ("c", flow("x", "x", "y")),
				normalStmt("b2").succ("c", flow("x", "x")),
				normalStmt("c").succ("d", flow("x", "z"), flow("y", "w")),
				normalStmt("d").succ("e", flow("z"), flow("w")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void unbalancedReturn() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				exitStmt("b").returns(over("x"),  to("y"), flow("1", "1")));
		
		helper.method("bar", 
				startPoints("unused"),
				normalStmt("y").succ("z", flow("1", "2")));
		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void artificalReturnEdgeForNoCallersCase() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				exitStmt("b").returns(null, null, flow("1", "1")));
		
		helper.runSolver(true, "a");
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

5268a4c6128ca689d49fa95e269216d27f055502

















5268a4c6128ca689d49fa95e269216d27f055502


Switch branch/tag










heros


test


heros


IFDSSolverTest.java



Find file
Normal viewHistoryPermalink






IFDSSolverTest.java



3.27 KB









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
package heros;

import org.junit.Before;
import org.junit.Test;

import static heros.TestHelper.*;

public class IFDSSolverTest {

	private TestHelper helper;

	@Before
	public void before() {
		helper = new TestHelper();
	}
	
	@Test
	public void test() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "x")),
				normalStmt("b").succ("c", flow("x", "x")),
				callSite("c").calls("foo", flow("x", "y")).retSite("f", flow("x", "x")));
		
		helper.method("foo",
				startPoints("d"),
				normalStmt("d").succ("e", flow("y", "y", "z")),
				exitStmt("e").returns(over("c"), to("f"), flow("z", "u"), flow("y")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummary() {
		helper.method("foo", 
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "x")).retSite("b", flow("0", "y")),
				callSite("b").calls("bar", flow("y", "x")).retSite("c", flow("y")),
				normalStmt("c").succ("c0", flow("w", "0")));
		
		helper.method("bar",
				startPoints("d"),
				normalStmt("d").succ("e", flow("x", "z")),
				exitStmt("e").returns(over("a"), to("b"), flow("z", "y"))
							  .returns(over("b"), to("c"), flow("z", "w")));
		
		helper.runSolver(false, "a");








test case for reusing a summary on caller side

 


Johannes Lerch
committed
Jun 25, 2014




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



	}
	
	@Test
	public void reuseSummaryForRecursiveCall() {
		helper.method("foo",
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "1")).retSite("b", flow("0")),
				normalStmt("b").succ("c", flow("2", "3")));
		
		helper.method("bar",
				startPoints("g"),
				normalStmt("g").succ("h", flow("1", "1")).succ("i", flow("1", "1")),
				callSite("i").calls("bar", flow("1", "1")).retSite("h", flow("1")),
				exitStmt("h").returns(over("a"), to("b"), flow("1"), flow("2" ,"2"))
							.returns(over("i"), to("h"), flow("1","2"), flow("2", "2")));
				
		helper.runSolver(false, "a");








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




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


105


106


107


108


109


110


111


112


113



	}
	
	@Test
	public void branch() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b1", flow("0", "x")).succ("b2", flow("0", "x")),
				normalStmt("b1").succ("c", flow("x", "x", "y")),
				normalStmt("b2").succ("c", flow("x", "x")),
				normalStmt("c").succ("d", flow("x", "z"), flow("y", "w")),
				normalStmt("d").succ("e", flow("z"), flow("w")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void unbalancedReturn() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				exitStmt("b").returns(over("x"),  to("y"), flow("1", "1")));
		
		helper.method("bar", 
				startPoints("unused"),
				normalStmt("y").succ("z", flow("1", "2")));
		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void artificalReturnEdgeForNoCallersCase() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				exitStmt("b").returns(null, null, flow("1", "1")));
		
		helper.runSolver(true, "a");
	}
}












Open sidebar



Joshua Garcia heros

5268a4c6128ca689d49fa95e269216d27f055502







Open sidebar



Joshua Garcia heros

5268a4c6128ca689d49fa95e269216d27f055502




Open sidebar

Joshua Garcia heros

5268a4c6128ca689d49fa95e269216d27f055502


Joshua Garciaherosheros
5268a4c6128ca689d49fa95e269216d27f055502










5268a4c6128ca689d49fa95e269216d27f055502


Switch branch/tag










heros


test


heros


IFDSSolverTest.java



Find file
Normal viewHistoryPermalink






IFDSSolverTest.java



3.27 KB









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
package heros;

import org.junit.Before;
import org.junit.Test;

import static heros.TestHelper.*;

public class IFDSSolverTest {

	private TestHelper helper;

	@Before
	public void before() {
		helper = new TestHelper();
	}
	
	@Test
	public void test() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "x")),
				normalStmt("b").succ("c", flow("x", "x")),
				callSite("c").calls("foo", flow("x", "y")).retSite("f", flow("x", "x")));
		
		helper.method("foo",
				startPoints("d"),
				normalStmt("d").succ("e", flow("y", "y", "z")),
				exitStmt("e").returns(over("c"), to("f"), flow("z", "u"), flow("y")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummary() {
		helper.method("foo", 
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "x")).retSite("b", flow("0", "y")),
				callSite("b").calls("bar", flow("y", "x")).retSite("c", flow("y")),
				normalStmt("c").succ("c0", flow("w", "0")));
		
		helper.method("bar",
				startPoints("d"),
				normalStmt("d").succ("e", flow("x", "z")),
				exitStmt("e").returns(over("a"), to("b"), flow("z", "y"))
							  .returns(over("b"), to("c"), flow("z", "w")));
		
		helper.runSolver(false, "a");








test case for reusing a summary on caller side

 


Johannes Lerch
committed
Jun 25, 2014




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



	}
	
	@Test
	public void reuseSummaryForRecursiveCall() {
		helper.method("foo",
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "1")).retSite("b", flow("0")),
				normalStmt("b").succ("c", flow("2", "3")));
		
		helper.method("bar",
				startPoints("g"),
				normalStmt("g").succ("h", flow("1", "1")).succ("i", flow("1", "1")),
				callSite("i").calls("bar", flow("1", "1")).retSite("h", flow("1")),
				exitStmt("h").returns(over("a"), to("b"), flow("1"), flow("2" ,"2"))
							.returns(over("i"), to("h"), flow("1","2"), flow("2", "2")));
				
		helper.runSolver(false, "a");








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




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


105


106


107


108


109


110


111


112


113



	}
	
	@Test
	public void branch() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b1", flow("0", "x")).succ("b2", flow("0", "x")),
				normalStmt("b1").succ("c", flow("x", "x", "y")),
				normalStmt("b2").succ("c", flow("x", "x")),
				normalStmt("c").succ("d", flow("x", "z"), flow("y", "w")),
				normalStmt("d").succ("e", flow("z"), flow("w")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void unbalancedReturn() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				exitStmt("b").returns(over("x"),  to("y"), flow("1", "1")));
		
		helper.method("bar", 
				startPoints("unused"),
				normalStmt("y").succ("z", flow("1", "2")));
		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void artificalReturnEdgeForNoCallersCase() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				exitStmt("b").returns(null, null, flow("1", "1")));
		
		helper.runSolver(true, "a");
	}
}















5268a4c6128ca689d49fa95e269216d27f055502


Switch branch/tag










heros


test


heros


IFDSSolverTest.java



Find file
Normal viewHistoryPermalink






IFDSSolverTest.java



3.27 KB









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
package heros;

import org.junit.Before;
import org.junit.Test;

import static heros.TestHelper.*;

public class IFDSSolverTest {

	private TestHelper helper;

	@Before
	public void before() {
		helper = new TestHelper();
	}
	
	@Test
	public void test() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "x")),
				normalStmt("b").succ("c", flow("x", "x")),
				callSite("c").calls("foo", flow("x", "y")).retSite("f", flow("x", "x")));
		
		helper.method("foo",
				startPoints("d"),
				normalStmt("d").succ("e", flow("y", "y", "z")),
				exitStmt("e").returns(over("c"), to("f"), flow("z", "u"), flow("y")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummary() {
		helper.method("foo", 
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "x")).retSite("b", flow("0", "y")),
				callSite("b").calls("bar", flow("y", "x")).retSite("c", flow("y")),
				normalStmt("c").succ("c0", flow("w", "0")));
		
		helper.method("bar",
				startPoints("d"),
				normalStmt("d").succ("e", flow("x", "z")),
				exitStmt("e").returns(over("a"), to("b"), flow("z", "y"))
							  .returns(over("b"), to("c"), flow("z", "w")));
		
		helper.runSolver(false, "a");








test case for reusing a summary on caller side

 


Johannes Lerch
committed
Jun 25, 2014




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



	}
	
	@Test
	public void reuseSummaryForRecursiveCall() {
		helper.method("foo",
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "1")).retSite("b", flow("0")),
				normalStmt("b").succ("c", flow("2", "3")));
		
		helper.method("bar",
				startPoints("g"),
				normalStmt("g").succ("h", flow("1", "1")).succ("i", flow("1", "1")),
				callSite("i").calls("bar", flow("1", "1")).retSite("h", flow("1")),
				exitStmt("h").returns(over("a"), to("b"), flow("1"), flow("2" ,"2"))
							.returns(over("i"), to("h"), flow("1","2"), flow("2", "2")));
				
		helper.runSolver(false, "a");








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




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


105


106


107


108


109


110


111


112


113



	}
	
	@Test
	public void branch() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b1", flow("0", "x")).succ("b2", flow("0", "x")),
				normalStmt("b1").succ("c", flow("x", "x", "y")),
				normalStmt("b2").succ("c", flow("x", "x")),
				normalStmt("c").succ("d", flow("x", "z"), flow("y", "w")),
				normalStmt("d").succ("e", flow("z"), flow("w")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void unbalancedReturn() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				exitStmt("b").returns(over("x"),  to("y"), flow("1", "1")));
		
		helper.method("bar", 
				startPoints("unused"),
				normalStmt("y").succ("z", flow("1", "2")));
		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void artificalReturnEdgeForNoCallersCase() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				exitStmt("b").returns(null, null, flow("1", "1")));
		
		helper.runSolver(true, "a");
	}
}











5268a4c6128ca689d49fa95e269216d27f055502


Switch branch/tag










heros


test


heros


IFDSSolverTest.java



Find file
Normal viewHistoryPermalink




5268a4c6128ca689d49fa95e269216d27f055502


Switch branch/tag










heros


test


heros


IFDSSolverTest.java





5268a4c6128ca689d49fa95e269216d27f055502


Switch branch/tag








5268a4c6128ca689d49fa95e269216d27f055502


Switch branch/tag





5268a4c6128ca689d49fa95e269216d27f055502

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

test

heros

IFDSSolverTest.java
Find file
Normal viewHistoryPermalink




IFDSSolverTest.java



3.27 KB









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
package heros;

import org.junit.Before;
import org.junit.Test;

import static heros.TestHelper.*;

public class IFDSSolverTest {

	private TestHelper helper;

	@Before
	public void before() {
		helper = new TestHelper();
	}
	
	@Test
	public void test() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "x")),
				normalStmt("b").succ("c", flow("x", "x")),
				callSite("c").calls("foo", flow("x", "y")).retSite("f", flow("x", "x")));
		
		helper.method("foo",
				startPoints("d"),
				normalStmt("d").succ("e", flow("y", "y", "z")),
				exitStmt("e").returns(over("c"), to("f"), flow("z", "u"), flow("y")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummary() {
		helper.method("foo", 
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "x")).retSite("b", flow("0", "y")),
				callSite("b").calls("bar", flow("y", "x")).retSite("c", flow("y")),
				normalStmt("c").succ("c0", flow("w", "0")));
		
		helper.method("bar",
				startPoints("d"),
				normalStmt("d").succ("e", flow("x", "z")),
				exitStmt("e").returns(over("a"), to("b"), flow("z", "y"))
							  .returns(over("b"), to("c"), flow("z", "w")));
		
		helper.runSolver(false, "a");








test case for reusing a summary on caller side

 


Johannes Lerch
committed
Jun 25, 2014




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



	}
	
	@Test
	public void reuseSummaryForRecursiveCall() {
		helper.method("foo",
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "1")).retSite("b", flow("0")),
				normalStmt("b").succ("c", flow("2", "3")));
		
		helper.method("bar",
				startPoints("g"),
				normalStmt("g").succ("h", flow("1", "1")).succ("i", flow("1", "1")),
				callSite("i").calls("bar", flow("1", "1")).retSite("h", flow("1")),
				exitStmt("h").returns(over("a"), to("b"), flow("1"), flow("2" ,"2"))
							.returns(over("i"), to("h"), flow("1","2"), flow("2", "2")));
				
		helper.runSolver(false, "a");








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




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


105


106


107


108


109


110


111


112


113



	}
	
	@Test
	public void branch() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b1", flow("0", "x")).succ("b2", flow("0", "x")),
				normalStmt("b1").succ("c", flow("x", "x", "y")),
				normalStmt("b2").succ("c", flow("x", "x")),
				normalStmt("c").succ("d", flow("x", "z"), flow("y", "w")),
				normalStmt("d").succ("e", flow("z"), flow("w")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void unbalancedReturn() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				exitStmt("b").returns(over("x"),  to("y"), flow("1", "1")));
		
		helper.method("bar", 
				startPoints("unused"),
				normalStmt("y").succ("z", flow("1", "2")));
		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void artificalReturnEdgeForNoCallersCase() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				exitStmt("b").returns(null, null, flow("1", "1")));
		
		helper.runSolver(true, "a");
	}
}









IFDSSolverTest.java



3.27 KB










IFDSSolverTest.java



3.27 KB









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
package heros;

import org.junit.Before;
import org.junit.Test;

import static heros.TestHelper.*;

public class IFDSSolverTest {

	private TestHelper helper;

	@Before
	public void before() {
		helper = new TestHelper();
	}
	
	@Test
	public void test() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "x")),
				normalStmt("b").succ("c", flow("x", "x")),
				callSite("c").calls("foo", flow("x", "y")).retSite("f", flow("x", "x")));
		
		helper.method("foo",
				startPoints("d"),
				normalStmt("d").succ("e", flow("y", "y", "z")),
				exitStmt("e").returns(over("c"), to("f"), flow("z", "u"), flow("y")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummary() {
		helper.method("foo", 
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "x")).retSite("b", flow("0", "y")),
				callSite("b").calls("bar", flow("y", "x")).retSite("c", flow("y")),
				normalStmt("c").succ("c0", flow("w", "0")));
		
		helper.method("bar",
				startPoints("d"),
				normalStmt("d").succ("e", flow("x", "z")),
				exitStmt("e").returns(over("a"), to("b"), flow("z", "y"))
							  .returns(over("b"), to("c"), flow("z", "w")));
		
		helper.runSolver(false, "a");








test case for reusing a summary on caller side

 


Johannes Lerch
committed
Jun 25, 2014




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



	}
	
	@Test
	public void reuseSummaryForRecursiveCall() {
		helper.method("foo",
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "1")).retSite("b", flow("0")),
				normalStmt("b").succ("c", flow("2", "3")));
		
		helper.method("bar",
				startPoints("g"),
				normalStmt("g").succ("h", flow("1", "1")).succ("i", flow("1", "1")),
				callSite("i").calls("bar", flow("1", "1")).retSite("h", flow("1")),
				exitStmt("h").returns(over("a"), to("b"), flow("1"), flow("2" ,"2"))
							.returns(over("i"), to("h"), flow("1","2"), flow("2", "2")));
				
		helper.runSolver(false, "a");








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




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


105


106


107


108


109


110


111


112


113



	}
	
	@Test
	public void branch() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b1", flow("0", "x")).succ("b2", flow("0", "x")),
				normalStmt("b1").succ("c", flow("x", "x", "y")),
				normalStmt("b2").succ("c", flow("x", "x")),
				normalStmt("c").succ("d", flow("x", "z"), flow("y", "w")),
				normalStmt("d").succ("e", flow("z"), flow("w")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void unbalancedReturn() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				exitStmt("b").returns(over("x"),  to("y"), flow("1", "1")));
		
		helper.method("bar", 
				startPoints("unused"),
				normalStmt("y").succ("z", flow("1", "2")));
		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void artificalReturnEdgeForNoCallersCase() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				exitStmt("b").returns(null, null, flow("1", "1")));
		
		helper.runSolver(true, "a");
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
package heros;

import org.junit.Before;
import org.junit.Test;

import static heros.TestHelper.*;

public class IFDSSolverTest {

	private TestHelper helper;

	@Before
	public void before() {
		helper = new TestHelper();
	}
	
	@Test
	public void test() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "x")),
				normalStmt("b").succ("c", flow("x", "x")),
				callSite("c").calls("foo", flow("x", "y")).retSite("f", flow("x", "x")));
		
		helper.method("foo",
				startPoints("d"),
				normalStmt("d").succ("e", flow("y", "y", "z")),
				exitStmt("e").returns(over("c"), to("f"), flow("z", "u"), flow("y")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummary() {
		helper.method("foo", 
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "x")).retSite("b", flow("0", "y")),
				callSite("b").calls("bar", flow("y", "x")).retSite("c", flow("y")),
				normalStmt("c").succ("c0", flow("w", "0")));
		
		helper.method("bar",
				startPoints("d"),
				normalStmt("d").succ("e", flow("x", "z")),
				exitStmt("e").returns(over("a"), to("b"), flow("z", "y"))
							  .returns(over("b"), to("c"), flow("z", "w")));
		
		helper.runSolver(false, "a");








test case for reusing a summary on caller side

 


Johannes Lerch
committed
Jun 25, 2014




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



	}
	
	@Test
	public void reuseSummaryForRecursiveCall() {
		helper.method("foo",
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "1")).retSite("b", flow("0")),
				normalStmt("b").succ("c", flow("2", "3")));
		
		helper.method("bar",
				startPoints("g"),
				normalStmt("g").succ("h", flow("1", "1")).succ("i", flow("1", "1")),
				callSite("i").calls("bar", flow("1", "1")).retSite("h", flow("1")),
				exitStmt("h").returns(over("a"), to("b"), flow("1"), flow("2" ,"2"))
							.returns(over("i"), to("h"), flow("1","2"), flow("2", "2")));
				
		helper.runSolver(false, "a");








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




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


105


106


107


108


109


110


111


112


113



	}
	
	@Test
	public void branch() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b1", flow("0", "x")).succ("b2", flow("0", "x")),
				normalStmt("b1").succ("c", flow("x", "x", "y")),
				normalStmt("b2").succ("c", flow("x", "x")),
				normalStmt("c").succ("d", flow("x", "z"), flow("y", "w")),
				normalStmt("d").succ("e", flow("z"), flow("w")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void unbalancedReturn() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				exitStmt("b").returns(over("x"),  to("y"), flow("1", "1")));
		
		helper.method("bar", 
				startPoints("unused"),
				normalStmt("y").succ("z", flow("1", "2")));
		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void artificalReturnEdgeForNoCallersCase() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				exitStmt("b").returns(null, null, flow("1", "1")));
		
		helper.runSolver(true, "a");
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
package heros;

import org.junit.Before;
import org.junit.Test;

import static heros.TestHelper.*;

public class IFDSSolverTest {

	private TestHelper helper;

	@Before
	public void before() {
		helper = new TestHelper();
	}
	
	@Test
	public void test() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "x")),
				normalStmt("b").succ("c", flow("x", "x")),
				callSite("c").calls("foo", flow("x", "y")).retSite("f", flow("x", "x")));
		
		helper.method("foo",
				startPoints("d"),
				normalStmt("d").succ("e", flow("y", "y", "z")),
				exitStmt("e").returns(over("c"), to("f"), flow("z", "u"), flow("y")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummary() {
		helper.method("foo", 
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "x")).retSite("b", flow("0", "y")),
				callSite("b").calls("bar", flow("y", "x")).retSite("c", flow("y")),
				normalStmt("c").succ("c0", flow("w", "0")));
		
		helper.method("bar",
				startPoints("d"),
				normalStmt("d").succ("e", flow("x", "z")),
				exitStmt("e").returns(over("a"), to("b"), flow("z", "y"))
							  .returns(over("b"), to("c"), flow("z", "w")));
		
		helper.runSolver(false, "a");








test case for reusing a summary on caller side

 


Johannes Lerch
committed
Jun 25, 2014




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



	}
	
	@Test
	public void reuseSummaryForRecursiveCall() {
		helper.method("foo",
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "1")).retSite("b", flow("0")),
				normalStmt("b").succ("c", flow("2", "3")));
		
		helper.method("bar",
				startPoints("g"),
				normalStmt("g").succ("h", flow("1", "1")).succ("i", flow("1", "1")),
				callSite("i").calls("bar", flow("1", "1")).retSite("h", flow("1")),
				exitStmt("h").returns(over("a"), to("b"), flow("1"), flow("2" ,"2"))
							.returns(over("i"), to("h"), flow("1","2"), flow("2", "2")));
				
		helper.runSolver(false, "a");








Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




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


105


106


107


108


109


110


111


112


113



	}
	
	@Test
	public void branch() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b1", flow("0", "x")).succ("b2", flow("0", "x")),
				normalStmt("b1").succ("c", flow("x", "x", "y")),
				normalStmt("b2").succ("c", flow("x", "x")),
				normalStmt("c").succ("d", flow("x", "z"), flow("y", "w")),
				normalStmt("d").succ("e", flow("z"), flow("w")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void unbalancedReturn() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				exitStmt("b").returns(over("x"),  to("y"), flow("1", "1")));
		
		helper.method("bar", 
				startPoints("unused"),
				normalStmt("y").succ("z", flow("1", "2")));
		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void artificalReturnEdgeForNoCallersCase() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				exitStmt("b").returns(null, null, flow("1", "1")));
		
		helper.runSolver(true, "a");
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
package heros;

import org.junit.Before;
import org.junit.Test;

import static heros.TestHelper.*;

public class IFDSSolverTest {

	private TestHelper helper;

	@Before
	public void before() {
		helper = new TestHelper();
	}
	
	@Test
	public void test() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "x")),
				normalStmt("b").succ("c", flow("x", "x")),
				callSite("c").calls("foo", flow("x", "y")).retSite("f", flow("x", "x")));
		
		helper.method("foo",
				startPoints("d"),
				normalStmt("d").succ("e", flow("y", "y", "z")),
				exitStmt("e").returns(over("c"), to("f"), flow("z", "u"), flow("y")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummary() {
		helper.method("foo", 
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "x")).retSite("b", flow("0", "y")),
				callSite("b").calls("bar", flow("y", "x")).retSite("c", flow("y")),
				normalStmt("c").succ("c0", flow("w", "0")));
		
		helper.method("bar",
				startPoints("d"),
				normalStmt("d").succ("e", flow("x", "z")),
				exitStmt("e").returns(over("a"), to("b"), flow("z", "y"))
							  .returns(over("b"), to("c"), flow("z", "w")));
		
		helper.runSolver(false, "a");






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
package heros;

import org.junit.Before;
import org.junit.Test;

import static heros.TestHelper.*;

public class IFDSSolverTest {

	private TestHelper helper;

	@Before
	public void before() {
		helper = new TestHelper();
	}
	
	@Test
	public void test() {
		helper.method("bar", 
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "x")),
				normalStmt("b").succ("c", flow("x", "x")),
				callSite("c").calls("foo", flow("x", "y")).retSite("f", flow("x", "x")));
		
		helper.method("foo",
				startPoints("d"),
				normalStmt("d").succ("e", flow("y", "y", "z")),
				exitStmt("e").returns(over("c"), to("f"), flow("z", "u"), flow("y")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void reuseSummary() {
		helper.method("foo", 
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "x")).retSite("b", flow("0", "y")),
				callSite("b").calls("bar", flow("y", "x")).retSite("c", flow("y")),
				normalStmt("c").succ("c0", flow("w", "0")));
		
		helper.method("bar",
				startPoints("d"),
				normalStmt("d").succ("e", flow("x", "z")),
				exitStmt("e").returns(over("a"), to("b"), flow("z", "y"))
							  .returns(over("b"), to("c"), flow("z", "w")));
		
		helper.runSolver(false, "a");

/*******************************************************************************/******************************************************************************* * Copyright (c) 2014 Johannes Lerch. * Copyright (c) 2014 Johannes Lerch. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Johannes Lerch - initial API and implementation *     Johannes Lerch - initial API and implementation ******************************************************************************/ ******************************************************************************/package heros;packageheros;import org.junit.Before;importorg.junit.Before;import org.junit.Test;importorg.junit.Test;import static heros.TestHelper.*;importstaticheros.TestHelper.*;public class IFDSSolverTest {publicclassIFDSSolverTest{	private TestHelper helper;privateTestHelperhelper;	@Before@Before	public void before() {publicvoidbefore(){		helper = new TestHelper();helper=newTestHelper();	}}		@Test@Test	public void test() {publicvoidtest(){		helper.method("bar", helper.method("bar",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b", flow("0", "x")),normalStmt("a").succ("b",flow("0","x")),				normalStmt("b").succ("c", flow("x", "x")),normalStmt("b").succ("c",flow("x","x")),				callSite("c").calls("foo", flow("x", "y")).retSite("f", flow("x", "x")));callSite("c").calls("foo",flow("x","y")).retSite("f",flow("x","x")));				helper.method("foo",helper.method("foo",				startPoints("d"),startPoints("d"),				normalStmt("d").succ("e", flow("y", "y", "z")),normalStmt("d").succ("e",flow("y","y","z")),				exitStmt("e").returns(over("c"), to("f"), flow("z", "u"), flow("y")));exitStmt("e").returns(over("c"),to("f"),flow("z","u"),flow("y")));				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void reuseSummary() {publicvoidreuseSummary(){		helper.method("foo", helper.method("foo",				startPoints("a"),startPoints("a"),				callSite("a").calls("bar", flow("0", "x")).retSite("b", flow("0", "y")),callSite("a").calls("bar",flow("0","x")).retSite("b",flow("0","y")),				callSite("b").calls("bar", flow("y", "x")).retSite("c", flow("y")),callSite("b").calls("bar",flow("y","x")).retSite("c",flow("y")),				normalStmt("c").succ("c0", flow("w", "0")));normalStmt("c").succ("c0",flow("w","0")));				helper.method("bar",helper.method("bar",				startPoints("d"),startPoints("d"),				normalStmt("d").succ("e", flow("x", "z")),normalStmt("d").succ("e",flow("x","z")),				exitStmt("e").returns(over("a"), to("b"), flow("z", "y"))exitStmt("e").returns(over("a"),to("b"),flow("z","y"))							  .returns(over("b"), to("c"), flow("z", "w")));.returns(over("b"),to("c"),flow("z","w")));				helper.runSolver(false, "a");helper.runSolver(false,"a");




test case for reusing a summary on caller side

 


Johannes Lerch
committed
Jun 25, 2014




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



	}
	
	@Test
	public void reuseSummaryForRecursiveCall() {
		helper.method("foo",
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "1")).retSite("b", flow("0")),
				normalStmt("b").succ("c", flow("2", "3")));
		
		helper.method("bar",
				startPoints("g"),
				normalStmt("g").succ("h", flow("1", "1")).succ("i", flow("1", "1")),
				callSite("i").calls("bar", flow("1", "1")).retSite("h", flow("1")),
				exitStmt("h").returns(over("a"), to("b"), flow("1"), flow("2" ,"2"))
							.returns(over("i"), to("h"), flow("1","2"), flow("2", "2")));
				
		helper.runSolver(false, "a");






test case for reusing a summary on caller side

 


Johannes Lerch
committed
Jun 25, 2014



test case for reusing a summary on caller side

 

test case for reusing a summary on caller side

Johannes Lerch
committed
Jun 25, 2014


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


	}
	
	@Test
	public void reuseSummaryForRecursiveCall() {
		helper.method("foo",
				startPoints("a"),
				callSite("a").calls("bar", flow("0", "1")).retSite("b", flow("0")),
				normalStmt("b").succ("c", flow("2", "3")));
		
		helper.method("bar",
				startPoints("g"),
				normalStmt("g").succ("h", flow("1", "1")).succ("i", flow("1", "1")),
				callSite("i").calls("bar", flow("1", "1")).retSite("h", flow("1")),
				exitStmt("h").returns(over("a"), to("b"), flow("1"), flow("2" ,"2"))
							.returns(over("i"), to("h"), flow("1","2"), flow("2", "2")));
				
		helper.runSolver(false, "a");

	}}		@Test@Test	public void reuseSummaryForRecursiveCall() {publicvoidreuseSummaryForRecursiveCall(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),				callSite("a").calls("bar", flow("0", "1")).retSite("b", flow("0")),callSite("a").calls("bar",flow("0","1")).retSite("b",flow("0")),				normalStmt("b").succ("c", flow("2", "3")));normalStmt("b").succ("c",flow("2","3")));				helper.method("bar",helper.method("bar",				startPoints("g"),startPoints("g"),				normalStmt("g").succ("h", flow("1", "1")).succ("i", flow("1", "1")),normalStmt("g").succ("h",flow("1","1")).succ("i",flow("1","1")),				callSite("i").calls("bar", flow("1", "1")).retSite("h", flow("1")),callSite("i").calls("bar",flow("1","1")).retSite("h",flow("1")),				exitStmt("h").returns(over("a"), to("b"), flow("1"), flow("2" ,"2"))exitStmt("h").returns(over("a"),to("b"),flow("1"),flow("2","2"))							.returns(over("i"), to("h"), flow("1","2"), flow("2", "2")));.returns(over("i"),to("h"),flow("1","2"),flow("2","2")));						helper.runSolver(false, "a");helper.runSolver(false,"a");




Tests for IDESolver



Johannes Lerch
committed
Jun 25, 2014




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


105


106


107


108


109


110


111


112


113



	}
	
	@Test
	public void branch() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b1", flow("0", "x")).succ("b2", flow("0", "x")),
				normalStmt("b1").succ("c", flow("x", "x", "y")),
				normalStmt("b2").succ("c", flow("x", "x")),
				normalStmt("c").succ("d", flow("x", "z"), flow("y", "w")),
				normalStmt("d").succ("e", flow("z"), flow("w")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void unbalancedReturn() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				exitStmt("b").returns(over("x"),  to("y"), flow("1", "1")));
		
		helper.method("bar", 
				startPoints("unused"),
				normalStmt("y").succ("z", flow("1", "2")));
		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void artificalReturnEdgeForNoCallersCase() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				exitStmt("b").returns(null, null, flow("1", "1")));
		
		helper.runSolver(true, "a");
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


105


106


107


108


109


110


111


112


113


	}
	
	@Test
	public void branch() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b1", flow("0", "x")).succ("b2", flow("0", "x")),
				normalStmt("b1").succ("c", flow("x", "x", "y")),
				normalStmt("b2").succ("c", flow("x", "x")),
				normalStmt("c").succ("d", flow("x", "z"), flow("y", "w")),
				normalStmt("d").succ("e", flow("z"), flow("w")));
		
		helper.runSolver(false, "a");
	}
	
	@Test
	public void unbalancedReturn() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				exitStmt("b").returns(over("x"),  to("y"), flow("1", "1")));
		
		helper.method("bar", 
				startPoints("unused"),
				normalStmt("y").succ("z", flow("1", "2")));
		
		helper.runSolver(true, "a");
	}
	
	@Test
	public void artificalReturnEdgeForNoCallersCase() {
		helper.method("foo",
				startPoints("a"),
				normalStmt("a").succ("b", flow("0", "1")),
				exitStmt("b").returns(null, null, flow("1", "1")));
		
		helper.runSolver(true, "a");
	}
}
	}}		@Test@Test	public void branch() {publicvoidbranch(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b1", flow("0", "x")).succ("b2", flow("0", "x")),normalStmt("a").succ("b1",flow("0","x")).succ("b2",flow("0","x")),				normalStmt("b1").succ("c", flow("x", "x", "y")),normalStmt("b1").succ("c",flow("x","x","y")),				normalStmt("b2").succ("c", flow("x", "x")),normalStmt("b2").succ("c",flow("x","x")),				normalStmt("c").succ("d", flow("x", "z"), flow("y", "w")),normalStmt("c").succ("d",flow("x","z"),flow("y","w")),				normalStmt("d").succ("e", flow("z"), flow("w")));normalStmt("d").succ("e",flow("z"),flow("w")));				helper.runSolver(false, "a");helper.runSolver(false,"a");	}}		@Test@Test	public void unbalancedReturn() {publicvoidunbalancedReturn(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b", flow("0", "1")),normalStmt("a").succ("b",flow("0","1")),				exitStmt("b").returns(over("x"),  to("y"), flow("1", "1")));exitStmt("b").returns(over("x"),to("y"),flow("1","1")));				helper.method("bar", helper.method("bar",				startPoints("unused"),startPoints("unused"),				normalStmt("y").succ("z", flow("1", "2")));normalStmt("y").succ("z",flow("1","2")));				helper.runSolver(true, "a");helper.runSolver(true,"a");	}}		@Test@Test	public void artificalReturnEdgeForNoCallersCase() {publicvoidartificalReturnEdgeForNoCallersCase(){		helper.method("foo",helper.method("foo",				startPoints("a"),startPoints("a"),				normalStmt("a").succ("b", flow("0", "1")),normalStmt("a").succ("b",flow("0","1")),				exitStmt("b").returns(null, null, flow("1", "1")));exitStmt("b").returns(null,null,flow("1","1")));				helper.runSolver(true, "a");helper.runSolver(true,"a");	}}}}





