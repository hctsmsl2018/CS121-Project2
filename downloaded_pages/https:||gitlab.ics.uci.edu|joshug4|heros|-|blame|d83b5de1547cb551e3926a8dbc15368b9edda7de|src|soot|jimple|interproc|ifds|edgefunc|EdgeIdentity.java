



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

d83b5de1547cb551e3926a8dbc15368b9edda7de

















d83b5de1547cb551e3926a8dbc15368b9edda7de


Switch branch/tag










heros


..


edgefunc


EdgeIdentity.java



Find file
Normal viewHistoryPermalink






EdgeIdentity.java



1.16 KB









Newer










Older









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




package soot.jimple.interproc.ifds.edgefunc;

import soot.jimple.interproc.ifds.EdgeFunction;

/**
 * The identity function on graph edges
 * @param <V> The type of values to be computed along flow edges.
 */
public class EdgeIdentity<V> implements EdgeFunction<V> {
	
	@SuppressWarnings("rawtypes")
	private final static EdgeIdentity instance = new EdgeIdentity();
	
	private EdgeIdentity(){} //use v() instead

	public V computeTarget(V source) {
		return source;
	}

	public EdgeFunction<V> composeWith(EdgeFunction<V> secondFunction) {
		return secondFunction;
	}

	public EdgeFunction<V> joinWith(EdgeFunction<V> otherFunction) {
		if(otherFunction == this || otherFunction.equalTo(this)) return this;
		if(otherFunction instanceof AllBottom) {
			return otherFunction;
		}
		if(otherFunction instanceof AllTop) {
			return this;
		}
		//do not know how to join; hence ask other function to decide on this
		return otherFunction.joinWith(this);
	}
	
	public boolean equalTo(EdgeFunction<V> other) {
		//singleton
		return other==this;
	}

	@SuppressWarnings("unchecked")
	public static <A> EdgeIdentity<A> v() {
		return instance;
	}

	public String toString() {
		return "id";
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

d83b5de1547cb551e3926a8dbc15368b9edda7de

















d83b5de1547cb551e3926a8dbc15368b9edda7de


Switch branch/tag










heros


..


edgefunc


EdgeIdentity.java



Find file
Normal viewHistoryPermalink






EdgeIdentity.java



1.16 KB









Newer










Older









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




package soot.jimple.interproc.ifds.edgefunc;

import soot.jimple.interproc.ifds.EdgeFunction;

/**
 * The identity function on graph edges
 * @param <V> The type of values to be computed along flow edges.
 */
public class EdgeIdentity<V> implements EdgeFunction<V> {
	
	@SuppressWarnings("rawtypes")
	private final static EdgeIdentity instance = new EdgeIdentity();
	
	private EdgeIdentity(){} //use v() instead

	public V computeTarget(V source) {
		return source;
	}

	public EdgeFunction<V> composeWith(EdgeFunction<V> secondFunction) {
		return secondFunction;
	}

	public EdgeFunction<V> joinWith(EdgeFunction<V> otherFunction) {
		if(otherFunction == this || otherFunction.equalTo(this)) return this;
		if(otherFunction instanceof AllBottom) {
			return otherFunction;
		}
		if(otherFunction instanceof AllTop) {
			return this;
		}
		//do not know how to join; hence ask other function to decide on this
		return otherFunction.joinWith(this);
	}
	
	public boolean equalTo(EdgeFunction<V> other) {
		//singleton
		return other==this;
	}

	@SuppressWarnings("unchecked")
	public static <A> EdgeIdentity<A> v() {
		return instance;
	}

	public String toString() {
		return "id";
	}


}











Open sidebar



Joshua Garcia heros

d83b5de1547cb551e3926a8dbc15368b9edda7de







Open sidebar



Joshua Garcia heros

d83b5de1547cb551e3926a8dbc15368b9edda7de




Open sidebar

Joshua Garcia heros

d83b5de1547cb551e3926a8dbc15368b9edda7de


Joshua Garciaherosheros
d83b5de1547cb551e3926a8dbc15368b9edda7de










d83b5de1547cb551e3926a8dbc15368b9edda7de


Switch branch/tag










heros


..


edgefunc


EdgeIdentity.java



Find file
Normal viewHistoryPermalink






EdgeIdentity.java



1.16 KB









Newer










Older









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




package soot.jimple.interproc.ifds.edgefunc;

import soot.jimple.interproc.ifds.EdgeFunction;

/**
 * The identity function on graph edges
 * @param <V> The type of values to be computed along flow edges.
 */
public class EdgeIdentity<V> implements EdgeFunction<V> {
	
	@SuppressWarnings("rawtypes")
	private final static EdgeIdentity instance = new EdgeIdentity();
	
	private EdgeIdentity(){} //use v() instead

	public V computeTarget(V source) {
		return source;
	}

	public EdgeFunction<V> composeWith(EdgeFunction<V> secondFunction) {
		return secondFunction;
	}

	public EdgeFunction<V> joinWith(EdgeFunction<V> otherFunction) {
		if(otherFunction == this || otherFunction.equalTo(this)) return this;
		if(otherFunction instanceof AllBottom) {
			return otherFunction;
		}
		if(otherFunction instanceof AllTop) {
			return this;
		}
		//do not know how to join; hence ask other function to decide on this
		return otherFunction.joinWith(this);
	}
	
	public boolean equalTo(EdgeFunction<V> other) {
		//singleton
		return other==this;
	}

	@SuppressWarnings("unchecked")
	public static <A> EdgeIdentity<A> v() {
		return instance;
	}

	public String toString() {
		return "id";
	}


}














d83b5de1547cb551e3926a8dbc15368b9edda7de


Switch branch/tag










heros


..


edgefunc


EdgeIdentity.java



Find file
Normal viewHistoryPermalink






EdgeIdentity.java



1.16 KB









Newer










Older









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




package soot.jimple.interproc.ifds.edgefunc;

import soot.jimple.interproc.ifds.EdgeFunction;

/**
 * The identity function on graph edges
 * @param <V> The type of values to be computed along flow edges.
 */
public class EdgeIdentity<V> implements EdgeFunction<V> {
	
	@SuppressWarnings("rawtypes")
	private final static EdgeIdentity instance = new EdgeIdentity();
	
	private EdgeIdentity(){} //use v() instead

	public V computeTarget(V source) {
		return source;
	}

	public EdgeFunction<V> composeWith(EdgeFunction<V> secondFunction) {
		return secondFunction;
	}

	public EdgeFunction<V> joinWith(EdgeFunction<V> otherFunction) {
		if(otherFunction == this || otherFunction.equalTo(this)) return this;
		if(otherFunction instanceof AllBottom) {
			return otherFunction;
		}
		if(otherFunction instanceof AllTop) {
			return this;
		}
		//do not know how to join; hence ask other function to decide on this
		return otherFunction.joinWith(this);
	}
	
	public boolean equalTo(EdgeFunction<V> other) {
		//singleton
		return other==this;
	}

	@SuppressWarnings("unchecked")
	public static <A> EdgeIdentity<A> v() {
		return instance;
	}

	public String toString() {
		return "id";
	}


}










d83b5de1547cb551e3926a8dbc15368b9edda7de


Switch branch/tag










heros


..


edgefunc


EdgeIdentity.java



Find file
Normal viewHistoryPermalink




d83b5de1547cb551e3926a8dbc15368b9edda7de


Switch branch/tag










heros


..


edgefunc


EdgeIdentity.java





d83b5de1547cb551e3926a8dbc15368b9edda7de


Switch branch/tag








d83b5de1547cb551e3926a8dbc15368b9edda7de


Switch branch/tag





d83b5de1547cb551e3926a8dbc15368b9edda7de

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

..

edgefunc

EdgeIdentity.java
Find file
Normal viewHistoryPermalink




EdgeIdentity.java



1.16 KB









Newer










Older









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




package soot.jimple.interproc.ifds.edgefunc;

import soot.jimple.interproc.ifds.EdgeFunction;

/**
 * The identity function on graph edges
 * @param <V> The type of values to be computed along flow edges.
 */
public class EdgeIdentity<V> implements EdgeFunction<V> {
	
	@SuppressWarnings("rawtypes")
	private final static EdgeIdentity instance = new EdgeIdentity();
	
	private EdgeIdentity(){} //use v() instead

	public V computeTarget(V source) {
		return source;
	}

	public EdgeFunction<V> composeWith(EdgeFunction<V> secondFunction) {
		return secondFunction;
	}

	public EdgeFunction<V> joinWith(EdgeFunction<V> otherFunction) {
		if(otherFunction == this || otherFunction.equalTo(this)) return this;
		if(otherFunction instanceof AllBottom) {
			return otherFunction;
		}
		if(otherFunction instanceof AllTop) {
			return this;
		}
		//do not know how to join; hence ask other function to decide on this
		return otherFunction.joinWith(this);
	}
	
	public boolean equalTo(EdgeFunction<V> other) {
		//singleton
		return other==this;
	}

	@SuppressWarnings("unchecked")
	public static <A> EdgeIdentity<A> v() {
		return instance;
	}

	public String toString() {
		return "id";
	}


}








EdgeIdentity.java



1.16 KB










EdgeIdentity.java



1.16 KB









Newer










Older
NewerOlder







initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




package soot.jimple.interproc.ifds.edgefunc;

import soot.jimple.interproc.ifds.EdgeFunction;

/**
 * The identity function on graph edges
 * @param <V> The type of values to be computed along flow edges.
 */
public class EdgeIdentity<V> implements EdgeFunction<V> {
	
	@SuppressWarnings("rawtypes")
	private final static EdgeIdentity instance = new EdgeIdentity();
	
	private EdgeIdentity(){} //use v() instead

	public V computeTarget(V source) {
		return source;
	}

	public EdgeFunction<V> composeWith(EdgeFunction<V> secondFunction) {
		return secondFunction;
	}

	public EdgeFunction<V> joinWith(EdgeFunction<V> otherFunction) {
		if(otherFunction == this || otherFunction.equalTo(this)) return this;
		if(otherFunction instanceof AllBottom) {
			return otherFunction;
		}
		if(otherFunction instanceof AllTop) {
			return this;
		}
		//do not know how to join; hence ask other function to decide on this
		return otherFunction.joinWith(this);
	}
	
	public boolean equalTo(EdgeFunction<V> other) {
		//singleton
		return other==this;
	}

	@SuppressWarnings("unchecked")
	public static <A> EdgeIdentity<A> v() {
		return instance;
	}

	public String toString() {
		return "id";
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
package soot.jimple.interproc.ifds.edgefunc;packagesoot.jimple.interproc.ifds.edgefunc;import soot.jimple.interproc.ifds.EdgeFunction;importsoot.jimple.interproc.ifds.EdgeFunction;/**/** * The identity function on graph edges * The identity function on graph edges * @param <V> The type of values to be computed along flow edges. * @param <V> The type of values to be computed along flow edges. */ */public class EdgeIdentity<V> implements EdgeFunction<V> {publicclassEdgeIdentity<V>implementsEdgeFunction<V>{		@SuppressWarnings("rawtypes")@SuppressWarnings("rawtypes")	private final static EdgeIdentity instance = new EdgeIdentity();privatefinalstaticEdgeIdentityinstance=newEdgeIdentity();		private EdgeIdentity(){} //use v() insteadprivateEdgeIdentity(){}//use v() instead	public V computeTarget(V source) {publicVcomputeTarget(Vsource){		return source;returnsource;	}}	public EdgeFunction<V> composeWith(EdgeFunction<V> secondFunction) {publicEdgeFunction<V>composeWith(EdgeFunction<V>secondFunction){		return secondFunction;returnsecondFunction;	}}	public EdgeFunction<V> joinWith(EdgeFunction<V> otherFunction) {publicEdgeFunction<V>joinWith(EdgeFunction<V>otherFunction){		if(otherFunction == this || otherFunction.equalTo(this)) return this;if(otherFunction==this||otherFunction.equalTo(this))returnthis;		if(otherFunction instanceof AllBottom) {if(otherFunctioninstanceofAllBottom){			return otherFunction;returnotherFunction;		}}		if(otherFunction instanceof AllTop) {if(otherFunctioninstanceofAllTop){			return this;returnthis;		}}		//do not know how to join; hence ask other function to decide on this//do not know how to join; hence ask other function to decide on this		return otherFunction.joinWith(this);returnotherFunction.joinWith(this);	}}		public boolean equalTo(EdgeFunction<V> other) {publicbooleanequalTo(EdgeFunction<V>other){		//singleton//singleton		return other==this;returnother==this;	}}	@SuppressWarnings("unchecked")@SuppressWarnings("unchecked")	public static <A> EdgeIdentity<A> v() {publicstatic<A>EdgeIdentity<A>v(){		return instance;returninstance;	}}	public String toString() {publicStringtoString(){		return "id";return"id";	}}}}





