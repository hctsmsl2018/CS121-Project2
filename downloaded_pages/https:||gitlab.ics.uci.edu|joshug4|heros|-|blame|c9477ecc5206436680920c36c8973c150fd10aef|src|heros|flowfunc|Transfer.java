



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

c9477ecc5206436680920c36c8973c150fd10aef

















c9477ecc5206436680920c36c8973c150fd10aef


Switch branch/tag










heros


src


heros


flowfunc


Transfer.java



Find file
Normal viewHistoryPermalink






Transfer.java



1.11 KB









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


12



package heros.flowfunc;









made Gen and Transfer use TwoElementSet

 


Eric Bodden
committed
Dec 24, 2013




13



import static heros.TwoElementSet.twoElementSet;








renamed package

 


Eric Bodden
committed
Nov 29, 2012




14



import heros.FlowFunction;








initial checkin



Eric Bodden
committed
Nov 14, 2012




15


16


17


18




import java.util.Collections;
import java.util.Set;









renamed package

 


Eric Bodden
committed
Nov 28, 2012




19












initial checkin



Eric Bodden
committed
Nov 14, 2012




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




public class Transfer<D> implements FlowFunction<D> {
	
	private final D toValue;
	private final D fromValue;
	
	public Transfer(D toValue, D fromValue){
		this.toValue = toValue;
		this.fromValue = fromValue;
	} 

	public Set<D> computeTargets(D source) {
		if(source==fromValue) {








made Gen and Transfer use TwoElementSet

 


Eric Bodden
committed
Dec 24, 2013




33



			return twoElementSet(source, toValue);








initial checkin



Eric Bodden
committed
Nov 14, 2012




34


35


36


37


38


39


40


41



		} else if(source==toValue) {
			return Collections.emptySet();
		} else {
			return Collections.singleton(source);
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

c9477ecc5206436680920c36c8973c150fd10aef

















c9477ecc5206436680920c36c8973c150fd10aef


Switch branch/tag










heros


src


heros


flowfunc


Transfer.java



Find file
Normal viewHistoryPermalink






Transfer.java



1.11 KB









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


12



package heros.flowfunc;









made Gen and Transfer use TwoElementSet

 


Eric Bodden
committed
Dec 24, 2013




13



import static heros.TwoElementSet.twoElementSet;








renamed package

 


Eric Bodden
committed
Nov 29, 2012




14



import heros.FlowFunction;








initial checkin



Eric Bodden
committed
Nov 14, 2012




15


16


17


18




import java.util.Collections;
import java.util.Set;









renamed package

 


Eric Bodden
committed
Nov 28, 2012




19












initial checkin



Eric Bodden
committed
Nov 14, 2012




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




public class Transfer<D> implements FlowFunction<D> {
	
	private final D toValue;
	private final D fromValue;
	
	public Transfer(D toValue, D fromValue){
		this.toValue = toValue;
		this.fromValue = fromValue;
	} 

	public Set<D> computeTargets(D source) {
		if(source==fromValue) {








made Gen and Transfer use TwoElementSet

 


Eric Bodden
committed
Dec 24, 2013




33



			return twoElementSet(source, toValue);








initial checkin



Eric Bodden
committed
Nov 14, 2012




34


35


36


37


38


39


40


41



		} else if(source==toValue) {
			return Collections.emptySet();
		} else {
			return Collections.singleton(source);
		}
	}
	
}












Open sidebar



Joshua Garcia heros

c9477ecc5206436680920c36c8973c150fd10aef







Open sidebar



Joshua Garcia heros

c9477ecc5206436680920c36c8973c150fd10aef




Open sidebar

Joshua Garcia heros

c9477ecc5206436680920c36c8973c150fd10aef


Joshua Garciaherosheros
c9477ecc5206436680920c36c8973c150fd10aef










c9477ecc5206436680920c36c8973c150fd10aef


Switch branch/tag










heros


src


heros


flowfunc


Transfer.java



Find file
Normal viewHistoryPermalink






Transfer.java



1.11 KB









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


12



package heros.flowfunc;









made Gen and Transfer use TwoElementSet

 


Eric Bodden
committed
Dec 24, 2013




13



import static heros.TwoElementSet.twoElementSet;








renamed package

 


Eric Bodden
committed
Nov 29, 2012




14



import heros.FlowFunction;








initial checkin



Eric Bodden
committed
Nov 14, 2012




15


16


17


18




import java.util.Collections;
import java.util.Set;









renamed package

 


Eric Bodden
committed
Nov 28, 2012




19












initial checkin



Eric Bodden
committed
Nov 14, 2012




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




public class Transfer<D> implements FlowFunction<D> {
	
	private final D toValue;
	private final D fromValue;
	
	public Transfer(D toValue, D fromValue){
		this.toValue = toValue;
		this.fromValue = fromValue;
	} 

	public Set<D> computeTargets(D source) {
		if(source==fromValue) {








made Gen and Transfer use TwoElementSet

 


Eric Bodden
committed
Dec 24, 2013




33



			return twoElementSet(source, toValue);








initial checkin



Eric Bodden
committed
Nov 14, 2012




34


35


36


37


38


39


40


41



		} else if(source==toValue) {
			return Collections.emptySet();
		} else {
			return Collections.singleton(source);
		}
	}
	
}















c9477ecc5206436680920c36c8973c150fd10aef


Switch branch/tag










heros


src


heros


flowfunc


Transfer.java



Find file
Normal viewHistoryPermalink






Transfer.java



1.11 KB









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


12



package heros.flowfunc;









made Gen and Transfer use TwoElementSet

 


Eric Bodden
committed
Dec 24, 2013




13



import static heros.TwoElementSet.twoElementSet;








renamed package

 


Eric Bodden
committed
Nov 29, 2012




14



import heros.FlowFunction;








initial checkin



Eric Bodden
committed
Nov 14, 2012




15


16


17


18




import java.util.Collections;
import java.util.Set;









renamed package

 


Eric Bodden
committed
Nov 28, 2012




19












initial checkin



Eric Bodden
committed
Nov 14, 2012




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




public class Transfer<D> implements FlowFunction<D> {
	
	private final D toValue;
	private final D fromValue;
	
	public Transfer(D toValue, D fromValue){
		this.toValue = toValue;
		this.fromValue = fromValue;
	} 

	public Set<D> computeTargets(D source) {
		if(source==fromValue) {








made Gen and Transfer use TwoElementSet

 


Eric Bodden
committed
Dec 24, 2013




33



			return twoElementSet(source, toValue);








initial checkin



Eric Bodden
committed
Nov 14, 2012




34


35


36


37


38


39


40


41



		} else if(source==toValue) {
			return Collections.emptySet();
		} else {
			return Collections.singleton(source);
		}
	}
	
}











c9477ecc5206436680920c36c8973c150fd10aef


Switch branch/tag










heros


src


heros


flowfunc


Transfer.java



Find file
Normal viewHistoryPermalink




c9477ecc5206436680920c36c8973c150fd10aef


Switch branch/tag










heros


src


heros


flowfunc


Transfer.java





c9477ecc5206436680920c36c8973c150fd10aef


Switch branch/tag








c9477ecc5206436680920c36c8973c150fd10aef


Switch branch/tag





c9477ecc5206436680920c36c8973c150fd10aef

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src

heros

flowfunc

Transfer.java
Find file
Normal viewHistoryPermalink




Transfer.java



1.11 KB









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


12



package heros.flowfunc;









made Gen and Transfer use TwoElementSet

 


Eric Bodden
committed
Dec 24, 2013




13



import static heros.TwoElementSet.twoElementSet;








renamed package

 


Eric Bodden
committed
Nov 29, 2012




14



import heros.FlowFunction;








initial checkin



Eric Bodden
committed
Nov 14, 2012




15


16


17


18




import java.util.Collections;
import java.util.Set;









renamed package

 


Eric Bodden
committed
Nov 28, 2012




19












initial checkin



Eric Bodden
committed
Nov 14, 2012




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




public class Transfer<D> implements FlowFunction<D> {
	
	private final D toValue;
	private final D fromValue;
	
	public Transfer(D toValue, D fromValue){
		this.toValue = toValue;
		this.fromValue = fromValue;
	} 

	public Set<D> computeTargets(D source) {
		if(source==fromValue) {








made Gen and Transfer use TwoElementSet

 


Eric Bodden
committed
Dec 24, 2013




33



			return twoElementSet(source, toValue);








initial checkin



Eric Bodden
committed
Nov 14, 2012




34


35


36


37


38


39


40


41



		} else if(source==toValue) {
			return Collections.emptySet();
		} else {
			return Collections.singleton(source);
		}
	}
	
}









Transfer.java



1.11 KB










Transfer.java



1.11 KB









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


12



package heros.flowfunc;









made Gen and Transfer use TwoElementSet

 


Eric Bodden
committed
Dec 24, 2013




13



import static heros.TwoElementSet.twoElementSet;








renamed package

 


Eric Bodden
committed
Nov 29, 2012




14



import heros.FlowFunction;








initial checkin



Eric Bodden
committed
Nov 14, 2012




15


16


17


18




import java.util.Collections;
import java.util.Set;









renamed package

 


Eric Bodden
committed
Nov 28, 2012




19












initial checkin



Eric Bodden
committed
Nov 14, 2012




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




public class Transfer<D> implements FlowFunction<D> {
	
	private final D toValue;
	private final D fromValue;
	
	public Transfer(D toValue, D fromValue){
		this.toValue = toValue;
		this.fromValue = fromValue;
	} 

	public Set<D> computeTargets(D source) {
		if(source==fromValue) {








made Gen and Transfer use TwoElementSet

 


Eric Bodden
committed
Dec 24, 2013




33



			return twoElementSet(source, toValue);








initial checkin



Eric Bodden
committed
Nov 14, 2012




34


35


36


37


38


39


40


41



		} else if(source==toValue) {
			return Collections.emptySet();
		} else {
			return Collections.singleton(source);
		}
	}
	
}











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


12



package heros.flowfunc;









made Gen and Transfer use TwoElementSet

 


Eric Bodden
committed
Dec 24, 2013




13



import static heros.TwoElementSet.twoElementSet;








renamed package

 


Eric Bodden
committed
Nov 29, 2012




14



import heros.FlowFunction;








initial checkin



Eric Bodden
committed
Nov 14, 2012




15


16


17


18




import java.util.Collections;
import java.util.Set;









renamed package

 


Eric Bodden
committed
Nov 28, 2012




19












initial checkin



Eric Bodden
committed
Nov 14, 2012




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




public class Transfer<D> implements FlowFunction<D> {
	
	private final D toValue;
	private final D fromValue;
	
	public Transfer(D toValue, D fromValue){
		this.toValue = toValue;
		this.fromValue = fromValue;
	} 

	public Set<D> computeTargets(D source) {
		if(source==fromValue) {








made Gen and Transfer use TwoElementSet

 


Eric Bodden
committed
Dec 24, 2013




33



			return twoElementSet(source, toValue);








initial checkin



Eric Bodden
committed
Nov 14, 2012




34


35


36


37


38


39


40


41



		} else if(source==toValue) {
			return Collections.emptySet();
		} else {
			return Collections.singleton(source);
		}
	}
	
}









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


12



package heros.flowfunc;









made Gen and Transfer use TwoElementSet

 


Eric Bodden
committed
Dec 24, 2013




13



import static heros.TwoElementSet.twoElementSet;








renamed package

 


Eric Bodden
committed
Nov 29, 2012




14



import heros.FlowFunction;








initial checkin



Eric Bodden
committed
Nov 14, 2012




15


16


17


18




import java.util.Collections;
import java.util.Set;









renamed package

 


Eric Bodden
committed
Nov 28, 2012




19












initial checkin



Eric Bodden
committed
Nov 14, 2012




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




public class Transfer<D> implements FlowFunction<D> {
	
	private final D toValue;
	private final D fromValue;
	
	public Transfer(D toValue, D fromValue){
		this.toValue = toValue;
		this.fromValue = fromValue;
	} 

	public Set<D> computeTargets(D source) {
		if(source==fromValue) {








made Gen and Transfer use TwoElementSet

 


Eric Bodden
committed
Dec 24, 2013




33



			return twoElementSet(source, toValue);








initial checkin



Eric Bodden
committed
Nov 14, 2012




34


35


36


37


38


39


40


41



		} else if(source==toValue) {
			return Collections.emptySet();
		} else {
			return Collections.singleton(source);
		}
	}
	
}







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

/*******************************************************************************/******************************************************************************* * Copyright (c) 2012 Eric Bodden. * Copyright (c) 2012 Eric Bodden. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Eric Bodden - initial API and implementation *     Eric Bodden - initial API and implementation ******************************************************************************/ ******************************************************************************/




renamed package

 


Eric Bodden
committed
Nov 29, 2012




11


12



package heros.flowfunc;







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


12


package heros.flowfunc;


package heros.flowfunc;packageheros.flowfunc;




made Gen and Transfer use TwoElementSet

 


Eric Bodden
committed
Dec 24, 2013




13



import static heros.TwoElementSet.twoElementSet;






made Gen and Transfer use TwoElementSet

 


Eric Bodden
committed
Dec 24, 2013



made Gen and Transfer use TwoElementSet

 

made Gen and Transfer use TwoElementSet

Eric Bodden
committed
Dec 24, 2013


13


import static heros.TwoElementSet.twoElementSet;

import static heros.TwoElementSet.twoElementSet;importstaticheros.TwoElementSet.twoElementSet;




renamed package

 


Eric Bodden
committed
Nov 29, 2012




14



import heros.FlowFunction;






renamed package

 


Eric Bodden
committed
Nov 29, 2012



renamed package

 

renamed package

Eric Bodden
committed
Nov 29, 2012


14


import heros.FlowFunction;

import heros.FlowFunction;importheros.FlowFunction;




initial checkin



Eric Bodden
committed
Nov 14, 2012




15


16


17


18




import java.util.Collections;
import java.util.Set;







initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


15


16


17


18



import java.util.Collections;
import java.util.Set;


import java.util.Collections;importjava.util.Collections;import java.util.Set;importjava.util.Set;




renamed package

 


Eric Bodden
committed
Nov 28, 2012




19










renamed package

 


Eric Bodden
committed
Nov 28, 2012



renamed package

 

renamed package

Eric Bodden
committed
Nov 28, 2012


19









initial checkin



Eric Bodden
committed
Nov 14, 2012




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




public class Transfer<D> implements FlowFunction<D> {
	
	private final D toValue;
	private final D fromValue;
	
	public Transfer(D toValue, D fromValue){
		this.toValue = toValue;
		this.fromValue = fromValue;
	} 

	public Set<D> computeTargets(D source) {
		if(source==fromValue) {






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


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



public class Transfer<D> implements FlowFunction<D> {
	
	private final D toValue;
	private final D fromValue;
	
	public Transfer(D toValue, D fromValue){
		this.toValue = toValue;
		this.fromValue = fromValue;
	} 

	public Set<D> computeTargets(D source) {
		if(source==fromValue) {

public class Transfer<D> implements FlowFunction<D> {publicclassTransfer<D>implementsFlowFunction<D>{		private final D toValue;privatefinalDtoValue;	private final D fromValue;privatefinalDfromValue;		public Transfer(D toValue, D fromValue){publicTransfer(DtoValue,DfromValue){		this.toValue = toValue;this.toValue=toValue;		this.fromValue = fromValue;this.fromValue=fromValue;	} }	public Set<D> computeTargets(D source) {publicSet<D>computeTargets(Dsource){		if(source==fromValue) {if(source==fromValue){




made Gen and Transfer use TwoElementSet

 


Eric Bodden
committed
Dec 24, 2013




33



			return twoElementSet(source, toValue);






made Gen and Transfer use TwoElementSet

 


Eric Bodden
committed
Dec 24, 2013



made Gen and Transfer use TwoElementSet

 

made Gen and Transfer use TwoElementSet

Eric Bodden
committed
Dec 24, 2013


33


			return twoElementSet(source, toValue);

			return twoElementSet(source, toValue);returntwoElementSet(source,toValue);




initial checkin



Eric Bodden
committed
Nov 14, 2012




34


35


36


37


38


39


40


41



		} else if(source==toValue) {
			return Collections.emptySet();
		} else {
			return Collections.singleton(source);
		}
	}
	
}





initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


34


35


36


37


38


39


40


41


		} else if(source==toValue) {
			return Collections.emptySet();
		} else {
			return Collections.singleton(source);
		}
	}
	
}
		} else if(source==toValue) {}elseif(source==toValue){			return Collections.emptySet();returnCollections.emptySet();		} else {}else{			return Collections.singleton(source);returnCollections.singleton(source);		}}	}}	}}





