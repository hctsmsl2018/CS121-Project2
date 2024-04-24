



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

0353d246b7d684db4e17a5294d8f855759d3ba3a

















0353d246b7d684db4e17a5294d8f855759d3ba3a


Switch branch/tag










heros


src


heros


flowfunc


Compose.java



Find file
Normal viewHistoryPermalink






Compose.java



1.52 KB









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




13




package heros.flowfunc;

import heros.FlowFunction;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






14














new compose-method which ignores identity functions


 

 


Eric Bodden
committed
Dec 19, 2012






15




16




import java.util.ArrayList;
import java.util.List;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






17




18




19




20




import java.util.Set;

import com.google.common.collect.Sets;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






21














initial checkin



 


Eric Bodden
committed
Nov 14, 2012






22




23




24




25




26




27




28




/**
 * Represents the ordered composition of a set of flow functions.
 */
public class Compose<D> implements FlowFunction<D> {
	
	private final FlowFunction<D>[] funcs;










new compose-method which ignores identity functions


 

 


Eric Bodden
committed
Dec 19, 2012






29




	private Compose(FlowFunction<D>... funcs){









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




		this.funcs = funcs;
	} 

	public Set<D> computeTargets(D source) {
		Set<D> curr = Sets.newHashSet();
		curr.add(source);
		for (FlowFunction<D> func : funcs) {
			Set<D> next = Sets.newHashSet();
			for(D d: curr)
				next.addAll(func.computeTargets(d));
			curr = next;
		}
		return curr;
	}









new compose-method which ignores identity functions


 

 


Eric Bodden
committed
Dec 19, 2012






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





	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <D> FlowFunction<D> compose(FlowFunction<D>... funcs) {
		List<FlowFunction<D>> list = new ArrayList<FlowFunction<D>>();
		for (FlowFunction<D> f : funcs) {
			if(f!=Identity.v()) {
				list.add(f);
			}
		}
		return new Compose(list.toArray(new FlowFunction[list.size()]));
	}









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






55




	









license headers


 

 


Eric Bodden
committed
Nov 29, 2012






56




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

0353d246b7d684db4e17a5294d8f855759d3ba3a

















0353d246b7d684db4e17a5294d8f855759d3ba3a


Switch branch/tag










heros


src


heros


flowfunc


Compose.java



Find file
Normal viewHistoryPermalink






Compose.java



1.52 KB









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




13




package heros.flowfunc;

import heros.FlowFunction;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






14














new compose-method which ignores identity functions


 

 


Eric Bodden
committed
Dec 19, 2012






15




16




import java.util.ArrayList;
import java.util.List;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






17




18




19




20




import java.util.Set;

import com.google.common.collect.Sets;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






21














initial checkin



 


Eric Bodden
committed
Nov 14, 2012






22




23




24




25




26




27




28




/**
 * Represents the ordered composition of a set of flow functions.
 */
public class Compose<D> implements FlowFunction<D> {
	
	private final FlowFunction<D>[] funcs;










new compose-method which ignores identity functions


 

 


Eric Bodden
committed
Dec 19, 2012






29




	private Compose(FlowFunction<D>... funcs){









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




		this.funcs = funcs;
	} 

	public Set<D> computeTargets(D source) {
		Set<D> curr = Sets.newHashSet();
		curr.add(source);
		for (FlowFunction<D> func : funcs) {
			Set<D> next = Sets.newHashSet();
			for(D d: curr)
				next.addAll(func.computeTargets(d));
			curr = next;
		}
		return curr;
	}









new compose-method which ignores identity functions


 

 


Eric Bodden
committed
Dec 19, 2012






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





	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <D> FlowFunction<D> compose(FlowFunction<D>... funcs) {
		List<FlowFunction<D>> list = new ArrayList<FlowFunction<D>>();
		for (FlowFunction<D> f : funcs) {
			if(f!=Identity.v()) {
				list.add(f);
			}
		}
		return new Compose(list.toArray(new FlowFunction[list.size()]));
	}









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






55




	









license headers


 

 


Eric Bodden
committed
Nov 29, 2012






56




}











Open sidebar



Joshua Garcia heros

0353d246b7d684db4e17a5294d8f855759d3ba3a







Open sidebar



Joshua Garcia heros

0353d246b7d684db4e17a5294d8f855759d3ba3a




Open sidebar

Joshua Garcia heros

0353d246b7d684db4e17a5294d8f855759d3ba3a


Joshua Garciaherosheros
0353d246b7d684db4e17a5294d8f855759d3ba3a










0353d246b7d684db4e17a5294d8f855759d3ba3a


Switch branch/tag










heros


src


heros


flowfunc


Compose.java



Find file
Normal viewHistoryPermalink






Compose.java



1.52 KB









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




13




package heros.flowfunc;

import heros.FlowFunction;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






14














new compose-method which ignores identity functions


 

 


Eric Bodden
committed
Dec 19, 2012






15




16




import java.util.ArrayList;
import java.util.List;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






17




18




19




20




import java.util.Set;

import com.google.common.collect.Sets;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






21














initial checkin



 


Eric Bodden
committed
Nov 14, 2012






22




23




24




25




26




27




28




/**
 * Represents the ordered composition of a set of flow functions.
 */
public class Compose<D> implements FlowFunction<D> {
	
	private final FlowFunction<D>[] funcs;










new compose-method which ignores identity functions


 

 


Eric Bodden
committed
Dec 19, 2012






29




	private Compose(FlowFunction<D>... funcs){









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




		this.funcs = funcs;
	} 

	public Set<D> computeTargets(D source) {
		Set<D> curr = Sets.newHashSet();
		curr.add(source);
		for (FlowFunction<D> func : funcs) {
			Set<D> next = Sets.newHashSet();
			for(D d: curr)
				next.addAll(func.computeTargets(d));
			curr = next;
		}
		return curr;
	}









new compose-method which ignores identity functions


 

 


Eric Bodden
committed
Dec 19, 2012






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





	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <D> FlowFunction<D> compose(FlowFunction<D>... funcs) {
		List<FlowFunction<D>> list = new ArrayList<FlowFunction<D>>();
		for (FlowFunction<D> f : funcs) {
			if(f!=Identity.v()) {
				list.add(f);
			}
		}
		return new Compose(list.toArray(new FlowFunction[list.size()]));
	}









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






55




	









license headers


 

 


Eric Bodden
committed
Nov 29, 2012






56




}














0353d246b7d684db4e17a5294d8f855759d3ba3a


Switch branch/tag










heros


src


heros


flowfunc


Compose.java



Find file
Normal viewHistoryPermalink






Compose.java



1.52 KB









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




13




package heros.flowfunc;

import heros.FlowFunction;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






14














new compose-method which ignores identity functions


 

 


Eric Bodden
committed
Dec 19, 2012






15




16




import java.util.ArrayList;
import java.util.List;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






17




18




19




20




import java.util.Set;

import com.google.common.collect.Sets;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






21














initial checkin



 


Eric Bodden
committed
Nov 14, 2012






22




23




24




25




26




27




28




/**
 * Represents the ordered composition of a set of flow functions.
 */
public class Compose<D> implements FlowFunction<D> {
	
	private final FlowFunction<D>[] funcs;










new compose-method which ignores identity functions


 

 


Eric Bodden
committed
Dec 19, 2012






29




	private Compose(FlowFunction<D>... funcs){









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




		this.funcs = funcs;
	} 

	public Set<D> computeTargets(D source) {
		Set<D> curr = Sets.newHashSet();
		curr.add(source);
		for (FlowFunction<D> func : funcs) {
			Set<D> next = Sets.newHashSet();
			for(D d: curr)
				next.addAll(func.computeTargets(d));
			curr = next;
		}
		return curr;
	}









new compose-method which ignores identity functions


 

 


Eric Bodden
committed
Dec 19, 2012






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





	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <D> FlowFunction<D> compose(FlowFunction<D>... funcs) {
		List<FlowFunction<D>> list = new ArrayList<FlowFunction<D>>();
		for (FlowFunction<D> f : funcs) {
			if(f!=Identity.v()) {
				list.add(f);
			}
		}
		return new Compose(list.toArray(new FlowFunction[list.size()]));
	}









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






55




	









license headers


 

 


Eric Bodden
committed
Nov 29, 2012






56




}










0353d246b7d684db4e17a5294d8f855759d3ba3a


Switch branch/tag










heros


src


heros


flowfunc


Compose.java



Find file
Normal viewHistoryPermalink




0353d246b7d684db4e17a5294d8f855759d3ba3a


Switch branch/tag










heros


src


heros


flowfunc


Compose.java





0353d246b7d684db4e17a5294d8f855759d3ba3a


Switch branch/tag








0353d246b7d684db4e17a5294d8f855759d3ba3a


Switch branch/tag





0353d246b7d684db4e17a5294d8f855759d3ba3a

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src

heros

flowfunc

Compose.java
Find file
Normal viewHistoryPermalink




Compose.java



1.52 KB









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




13




package heros.flowfunc;

import heros.FlowFunction;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






14














new compose-method which ignores identity functions


 

 


Eric Bodden
committed
Dec 19, 2012






15




16




import java.util.ArrayList;
import java.util.List;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






17




18




19




20




import java.util.Set;

import com.google.common.collect.Sets;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






21














initial checkin



 


Eric Bodden
committed
Nov 14, 2012






22




23




24




25




26




27




28




/**
 * Represents the ordered composition of a set of flow functions.
 */
public class Compose<D> implements FlowFunction<D> {
	
	private final FlowFunction<D>[] funcs;










new compose-method which ignores identity functions


 

 


Eric Bodden
committed
Dec 19, 2012






29




	private Compose(FlowFunction<D>... funcs){









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




		this.funcs = funcs;
	} 

	public Set<D> computeTargets(D source) {
		Set<D> curr = Sets.newHashSet();
		curr.add(source);
		for (FlowFunction<D> func : funcs) {
			Set<D> next = Sets.newHashSet();
			for(D d: curr)
				next.addAll(func.computeTargets(d));
			curr = next;
		}
		return curr;
	}









new compose-method which ignores identity functions


 

 


Eric Bodden
committed
Dec 19, 2012






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





	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <D> FlowFunction<D> compose(FlowFunction<D>... funcs) {
		List<FlowFunction<D>> list = new ArrayList<FlowFunction<D>>();
		for (FlowFunction<D> f : funcs) {
			if(f!=Identity.v()) {
				list.add(f);
			}
		}
		return new Compose(list.toArray(new FlowFunction[list.size()]));
	}









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






55




	









license headers


 

 


Eric Bodden
committed
Nov 29, 2012






56




}








Compose.java



1.52 KB










Compose.java



1.52 KB









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




13




package heros.flowfunc;

import heros.FlowFunction;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






14














new compose-method which ignores identity functions


 

 


Eric Bodden
committed
Dec 19, 2012






15




16




import java.util.ArrayList;
import java.util.List;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






17




18




19




20




import java.util.Set;

import com.google.common.collect.Sets;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






21














initial checkin



 


Eric Bodden
committed
Nov 14, 2012






22




23




24




25




26




27




28




/**
 * Represents the ordered composition of a set of flow functions.
 */
public class Compose<D> implements FlowFunction<D> {
	
	private final FlowFunction<D>[] funcs;










new compose-method which ignores identity functions


 

 


Eric Bodden
committed
Dec 19, 2012






29




	private Compose(FlowFunction<D>... funcs){









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




		this.funcs = funcs;
	} 

	public Set<D> computeTargets(D source) {
		Set<D> curr = Sets.newHashSet();
		curr.add(source);
		for (FlowFunction<D> func : funcs) {
			Set<D> next = Sets.newHashSet();
			for(D d: curr)
				next.addAll(func.computeTargets(d));
			curr = next;
		}
		return curr;
	}









new compose-method which ignores identity functions


 

 


Eric Bodden
committed
Dec 19, 2012






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





	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <D> FlowFunction<D> compose(FlowFunction<D>... funcs) {
		List<FlowFunction<D>> list = new ArrayList<FlowFunction<D>>();
		for (FlowFunction<D> f : funcs) {
			if(f!=Identity.v()) {
				list.add(f);
			}
		}
		return new Compose(list.toArray(new FlowFunction[list.size()]));
	}









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






55




	









license headers


 

 


Eric Bodden
committed
Nov 29, 2012






56




}







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
/*******************************************************************************/******************************************************************************* * Copyright (c) 2012 Eric Bodden. * Copyright (c) 2012 Eric Bodden. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Eric Bodden - initial API and implementation *     Eric Bodden - initial API and implementation ******************************************************************************/ ******************************************************************************/



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

13
package heros.flowfunc;packageheros.flowfunc;import heros.FlowFunction;importheros.FlowFunction;



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

14




new compose-method which ignores identity functions


 

 


Eric Bodden
committed
Dec 19, 2012



new compose-method which ignores identity functions


 

 

new compose-method which ignores identity functions

 

Eric Bodden
committed
Dec 19, 2012

15

16
import java.util.ArrayList;importjava.util.ArrayList;import java.util.List;importjava.util.List;



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

17

18

19

20
import java.util.Set;importjava.util.Set;import com.google.common.collect.Sets;importcom.google.common.collect.Sets;



renamed package


 

 


Eric Bodden
committed
Nov 28, 2012



renamed package


 

 

renamed package

 

Eric Bodden
committed
Nov 28, 2012

21




initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

22

23

24

25

26

27

28
/**/** * Represents the ordered composition of a set of flow functions. * Represents the ordered composition of a set of flow functions. */ */public class Compose<D> implements FlowFunction<D> {publicclassCompose<D>implementsFlowFunction<D>{		private final FlowFunction<D>[] funcs;privatefinalFlowFunction<D>[]funcs;



new compose-method which ignores identity functions


 

 


Eric Bodden
committed
Dec 19, 2012



new compose-method which ignores identity functions


 

 

new compose-method which ignores identity functions

 

Eric Bodden
committed
Dec 19, 2012

29
	private Compose(FlowFunction<D>... funcs){privateCompose(FlowFunction<D>...funcs){



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

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
		this.funcs = funcs;this.funcs=funcs;	} }	public Set<D> computeTargets(D source) {publicSet<D>computeTargets(Dsource){		Set<D> curr = Sets.newHashSet();Set<D>curr=Sets.newHashSet();		curr.add(source);curr.add(source);		for (FlowFunction<D> func : funcs) {for(FlowFunction<D>func:funcs){			Set<D> next = Sets.newHashSet();Set<D>next=Sets.newHashSet();			for(D d: curr)for(Dd:curr)				next.addAll(func.computeTargets(d));next.addAll(func.computeTargets(d));			curr = next;curr=next;		}}		return curr;returncurr;	}}



new compose-method which ignores identity functions


 

 


Eric Bodden
committed
Dec 19, 2012



new compose-method which ignores identity functions


 

 

new compose-method which ignores identity functions

 

Eric Bodden
committed
Dec 19, 2012

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
	@SuppressWarnings({ "rawtypes", "unchecked" })@SuppressWarnings({"rawtypes","unchecked"})	public static <D> FlowFunction<D> compose(FlowFunction<D>... funcs) {publicstatic<D>FlowFunction<D>compose(FlowFunction<D>...funcs){		List<FlowFunction<D>> list = new ArrayList<FlowFunction<D>>();List<FlowFunction<D>>list=newArrayList<FlowFunction<D>>();		for (FlowFunction<D> f : funcs) {for(FlowFunction<D>f:funcs){			if(f!=Identity.v()) {if(f!=Identity.v()){				list.add(f);list.add(f);			}}		}}		return new Compose(list.toArray(new FlowFunction[list.size()]));returnnewCompose(list.toArray(newFlowFunction[list.size()]));	}}



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

55
	



license headers


 

 


Eric Bodden
committed
Nov 29, 2012



license headers


 

 

license headers

 

Eric Bodden
committed
Nov 29, 2012

56
}}





