



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

52970dcc16b6d1f816b5b38489914eca8a1378aa

















52970dcc16b6d1f816b5b38489914eca8a1378aa


Switch branch/tag










heros


src


heros


flowfunc


Union.java



Find file
Normal viewHistoryPermalink






Union.java



1.5 KB









Newer










Older









added union flow function



 


Eric Bodden
committed
Oct 28, 2013






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
package heros.flowfunc;

import heros.FlowFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;


/**
 * Represents the union of a set of flow functions.
 */
public class Union<D> implements FlowFunction<D> {
	
	private final FlowFunction<D>[] funcs;

	private Union(FlowFunction<D>... funcs){
		this.funcs = funcs;
	} 

	public Set<D> computeTargets(D source) {
		Set<D> res = Sets.newHashSet();
		for (FlowFunction<D> func : funcs) {
			res.addAll(func.computeTargets(source));
		}
		return res;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <D> FlowFunction<D> union(FlowFunction<D>... funcs) {
		List<FlowFunction<D>> list = new ArrayList<FlowFunction<D>>();
		for (FlowFunction<D> f : funcs) {
			if(f!=Identity.v()) {
				list.add(f);
			}
		}
		if(list.size()==1) return list.get(0);
		else if(list.isEmpty()) return Identity.v();
		return new Union(list.toArray(new FlowFunction[list.size()]));
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

52970dcc16b6d1f816b5b38489914eca8a1378aa

















52970dcc16b6d1f816b5b38489914eca8a1378aa


Switch branch/tag










heros


src


heros


flowfunc


Union.java



Find file
Normal viewHistoryPermalink






Union.java



1.5 KB









Newer










Older









added union flow function



 


Eric Bodden
committed
Oct 28, 2013






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
package heros.flowfunc;

import heros.FlowFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;


/**
 * Represents the union of a set of flow functions.
 */
public class Union<D> implements FlowFunction<D> {
	
	private final FlowFunction<D>[] funcs;

	private Union(FlowFunction<D>... funcs){
		this.funcs = funcs;
	} 

	public Set<D> computeTargets(D source) {
		Set<D> res = Sets.newHashSet();
		for (FlowFunction<D> func : funcs) {
			res.addAll(func.computeTargets(source));
		}
		return res;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <D> FlowFunction<D> union(FlowFunction<D>... funcs) {
		List<FlowFunction<D>> list = new ArrayList<FlowFunction<D>>();
		for (FlowFunction<D> f : funcs) {
			if(f!=Identity.v()) {
				list.add(f);
			}
		}
		if(list.size()==1) return list.get(0);
		else if(list.isEmpty()) return Identity.v();
		return new Union(list.toArray(new FlowFunction[list.size()]));
	}
	
}











Open sidebar



Joshua Garcia heros

52970dcc16b6d1f816b5b38489914eca8a1378aa







Open sidebar



Joshua Garcia heros

52970dcc16b6d1f816b5b38489914eca8a1378aa




Open sidebar

Joshua Garcia heros

52970dcc16b6d1f816b5b38489914eca8a1378aa


Joshua Garciaherosheros
52970dcc16b6d1f816b5b38489914eca8a1378aa










52970dcc16b6d1f816b5b38489914eca8a1378aa


Switch branch/tag










heros


src


heros


flowfunc


Union.java



Find file
Normal viewHistoryPermalink






Union.java



1.5 KB









Newer










Older









added union flow function



 


Eric Bodden
committed
Oct 28, 2013






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
package heros.flowfunc;

import heros.FlowFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;


/**
 * Represents the union of a set of flow functions.
 */
public class Union<D> implements FlowFunction<D> {
	
	private final FlowFunction<D>[] funcs;

	private Union(FlowFunction<D>... funcs){
		this.funcs = funcs;
	} 

	public Set<D> computeTargets(D source) {
		Set<D> res = Sets.newHashSet();
		for (FlowFunction<D> func : funcs) {
			res.addAll(func.computeTargets(source));
		}
		return res;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <D> FlowFunction<D> union(FlowFunction<D>... funcs) {
		List<FlowFunction<D>> list = new ArrayList<FlowFunction<D>>();
		for (FlowFunction<D> f : funcs) {
			if(f!=Identity.v()) {
				list.add(f);
			}
		}
		if(list.size()==1) return list.get(0);
		else if(list.isEmpty()) return Identity.v();
		return new Union(list.toArray(new FlowFunction[list.size()]));
	}
	
}














52970dcc16b6d1f816b5b38489914eca8a1378aa


Switch branch/tag










heros


src


heros


flowfunc


Union.java



Find file
Normal viewHistoryPermalink






Union.java



1.5 KB









Newer










Older









added union flow function



 


Eric Bodden
committed
Oct 28, 2013






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
package heros.flowfunc;

import heros.FlowFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;


/**
 * Represents the union of a set of flow functions.
 */
public class Union<D> implements FlowFunction<D> {
	
	private final FlowFunction<D>[] funcs;

	private Union(FlowFunction<D>... funcs){
		this.funcs = funcs;
	} 

	public Set<D> computeTargets(D source) {
		Set<D> res = Sets.newHashSet();
		for (FlowFunction<D> func : funcs) {
			res.addAll(func.computeTargets(source));
		}
		return res;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <D> FlowFunction<D> union(FlowFunction<D>... funcs) {
		List<FlowFunction<D>> list = new ArrayList<FlowFunction<D>>();
		for (FlowFunction<D> f : funcs) {
			if(f!=Identity.v()) {
				list.add(f);
			}
		}
		if(list.size()==1) return list.get(0);
		else if(list.isEmpty()) return Identity.v();
		return new Union(list.toArray(new FlowFunction[list.size()]));
	}
	
}










52970dcc16b6d1f816b5b38489914eca8a1378aa


Switch branch/tag










heros


src


heros


flowfunc


Union.java



Find file
Normal viewHistoryPermalink




52970dcc16b6d1f816b5b38489914eca8a1378aa


Switch branch/tag










heros


src


heros


flowfunc


Union.java





52970dcc16b6d1f816b5b38489914eca8a1378aa


Switch branch/tag








52970dcc16b6d1f816b5b38489914eca8a1378aa


Switch branch/tag





52970dcc16b6d1f816b5b38489914eca8a1378aa

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src

heros

flowfunc

Union.java
Find file
Normal viewHistoryPermalink




Union.java



1.5 KB









Newer










Older









added union flow function



 


Eric Bodden
committed
Oct 28, 2013






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
package heros.flowfunc;

import heros.FlowFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;


/**
 * Represents the union of a set of flow functions.
 */
public class Union<D> implements FlowFunction<D> {
	
	private final FlowFunction<D>[] funcs;

	private Union(FlowFunction<D>... funcs){
		this.funcs = funcs;
	} 

	public Set<D> computeTargets(D source) {
		Set<D> res = Sets.newHashSet();
		for (FlowFunction<D> func : funcs) {
			res.addAll(func.computeTargets(source));
		}
		return res;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <D> FlowFunction<D> union(FlowFunction<D>... funcs) {
		List<FlowFunction<D>> list = new ArrayList<FlowFunction<D>>();
		for (FlowFunction<D> f : funcs) {
			if(f!=Identity.v()) {
				list.add(f);
			}
		}
		if(list.size()==1) return list.get(0);
		else if(list.isEmpty()) return Identity.v();
		return new Union(list.toArray(new FlowFunction[list.size()]));
	}
	
}








Union.java



1.5 KB










Union.java



1.5 KB









Newer










Older
NewerOlder







added union flow function



 


Eric Bodden
committed
Oct 28, 2013






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
package heros.flowfunc;

import heros.FlowFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;


/**
 * Represents the union of a set of flow functions.
 */
public class Union<D> implements FlowFunction<D> {
	
	private final FlowFunction<D>[] funcs;

	private Union(FlowFunction<D>... funcs){
		this.funcs = funcs;
	} 

	public Set<D> computeTargets(D source) {
		Set<D> res = Sets.newHashSet();
		for (FlowFunction<D> func : funcs) {
			res.addAll(func.computeTargets(source));
		}
		return res;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <D> FlowFunction<D> union(FlowFunction<D>... funcs) {
		List<FlowFunction<D>> list = new ArrayList<FlowFunction<D>>();
		for (FlowFunction<D> f : funcs) {
			if(f!=Identity.v()) {
				list.add(f);
			}
		}
		if(list.size()==1) return list.get(0);
		else if(list.isEmpty()) return Identity.v();
		return new Union(list.toArray(new FlowFunction[list.size()]));
	}
	
}







added union flow function



 


Eric Bodden
committed
Oct 28, 2013



added union flow function



 

added union flow function


Eric Bodden
committed
Oct 28, 2013

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
/*******************************************************************************/******************************************************************************* * Copyright (c) 2012 Eric Bodden. * Copyright (c) 2012 Eric Bodden. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Eric Bodden - initial API and implementation *     Eric Bodden - initial API and implementation ******************************************************************************/ ******************************************************************************/package heros.flowfunc;packageheros.flowfunc;import heros.FlowFunction;importheros.FlowFunction;import java.util.ArrayList;importjava.util.ArrayList;import java.util.List;importjava.util.List;import java.util.Set;importjava.util.Set;import com.google.common.collect.Sets;importcom.google.common.collect.Sets;/**/** * Represents the union of a set of flow functions. * Represents the union of a set of flow functions. */ */public class Union<D> implements FlowFunction<D> {publicclassUnion<D>implementsFlowFunction<D>{		private final FlowFunction<D>[] funcs;privatefinalFlowFunction<D>[]funcs;	private Union(FlowFunction<D>... funcs){privateUnion(FlowFunction<D>...funcs){		this.funcs = funcs;this.funcs=funcs;	} }	public Set<D> computeTargets(D source) {publicSet<D>computeTargets(Dsource){		Set<D> res = Sets.newHashSet();Set<D>res=Sets.newHashSet();		for (FlowFunction<D> func : funcs) {for(FlowFunction<D>func:funcs){			res.addAll(func.computeTargets(source));res.addAll(func.computeTargets(source));		}}		return res;returnres;	}}	@SuppressWarnings({ "rawtypes", "unchecked" })@SuppressWarnings({"rawtypes","unchecked"})	public static <D> FlowFunction<D> union(FlowFunction<D>... funcs) {publicstatic<D>FlowFunction<D>union(FlowFunction<D>...funcs){		List<FlowFunction<D>> list = new ArrayList<FlowFunction<D>>();List<FlowFunction<D>>list=newArrayList<FlowFunction<D>>();		for (FlowFunction<D> f : funcs) {for(FlowFunction<D>f:funcs){			if(f!=Identity.v()) {if(f!=Identity.v()){				list.add(f);list.add(f);			}}		}}		if(list.size()==1) return list.get(0);if(list.size()==1)returnlist.get(0);		else if(list.isEmpty()) return Identity.v();elseif(list.isEmpty())returnIdentity.v();		return new Union(list.toArray(new FlowFunction[list.size()]));returnnewUnion(list.toArray(newFlowFunction[list.size()]));	}}	}}





