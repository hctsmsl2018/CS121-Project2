



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

eda5559e1827af880a91488176d13076daee72da

















eda5559e1827af880a91488176d13076daee72da


Switch branch/tag










heros


src


heros


edgefunc


AllTop.java



Find file
Normal viewHistoryPermalink






AllTop.java



737 Bytes









Newer










Older









renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






1




package heros.edgefunc;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






2














renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






3




import heros.EdgeFunction;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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






public class AllTop<V> implements EdgeFunction<V> {
	
	private final V topElement; 

	public AllTop(V topElement){
		this.topElement = topElement;
	} 

	public V computeTarget(V source) {
		return topElement;
	}

	public EdgeFunction<V> composeWith(EdgeFunction<V> secondFunction) {
		return secondFunction;
	}

	public EdgeFunction<V> joinWith(EdgeFunction<V> otherFunction) {
		return otherFunction;
	}

	public boolean equalTo(EdgeFunction<V> other) {
		if(other instanceof AllTop) {
			@SuppressWarnings("rawtypes")
			AllTop allTop = (AllTop) other;
			return allTop.topElement.equals(topElement);
		}		
		return false;
	}

	public String toString() {
		return "alltop";
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

eda5559e1827af880a91488176d13076daee72da

















eda5559e1827af880a91488176d13076daee72da


Switch branch/tag










heros


src


heros


edgefunc


AllTop.java



Find file
Normal viewHistoryPermalink






AllTop.java



737 Bytes









Newer










Older









renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






1




package heros.edgefunc;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






2














renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






3




import heros.EdgeFunction;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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






public class AllTop<V> implements EdgeFunction<V> {
	
	private final V topElement; 

	public AllTop(V topElement){
		this.topElement = topElement;
	} 

	public V computeTarget(V source) {
		return topElement;
	}

	public EdgeFunction<V> composeWith(EdgeFunction<V> secondFunction) {
		return secondFunction;
	}

	public EdgeFunction<V> joinWith(EdgeFunction<V> otherFunction) {
		return otherFunction;
	}

	public boolean equalTo(EdgeFunction<V> other) {
		if(other instanceof AllTop) {
			@SuppressWarnings("rawtypes")
			AllTop allTop = (AllTop) other;
			return allTop.topElement.equals(topElement);
		}		
		return false;
	}

	public String toString() {
		return "alltop";
	}
	
}











Open sidebar



Joshua Garcia heros

eda5559e1827af880a91488176d13076daee72da







Open sidebar



Joshua Garcia heros

eda5559e1827af880a91488176d13076daee72da




Open sidebar

Joshua Garcia heros

eda5559e1827af880a91488176d13076daee72da


Joshua Garciaherosheros
eda5559e1827af880a91488176d13076daee72da










eda5559e1827af880a91488176d13076daee72da


Switch branch/tag










heros


src


heros


edgefunc


AllTop.java



Find file
Normal viewHistoryPermalink






AllTop.java



737 Bytes









Newer










Older









renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






1




package heros.edgefunc;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






2














renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






3




import heros.EdgeFunction;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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






public class AllTop<V> implements EdgeFunction<V> {
	
	private final V topElement; 

	public AllTop(V topElement){
		this.topElement = topElement;
	} 

	public V computeTarget(V source) {
		return topElement;
	}

	public EdgeFunction<V> composeWith(EdgeFunction<V> secondFunction) {
		return secondFunction;
	}

	public EdgeFunction<V> joinWith(EdgeFunction<V> otherFunction) {
		return otherFunction;
	}

	public boolean equalTo(EdgeFunction<V> other) {
		if(other instanceof AllTop) {
			@SuppressWarnings("rawtypes")
			AllTop allTop = (AllTop) other;
			return allTop.topElement.equals(topElement);
		}		
		return false;
	}

	public String toString() {
		return "alltop";
	}
	
}














eda5559e1827af880a91488176d13076daee72da


Switch branch/tag










heros


src


heros


edgefunc


AllTop.java



Find file
Normal viewHistoryPermalink






AllTop.java



737 Bytes









Newer










Older









renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






1




package heros.edgefunc;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






2














renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






3




import heros.EdgeFunction;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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






public class AllTop<V> implements EdgeFunction<V> {
	
	private final V topElement; 

	public AllTop(V topElement){
		this.topElement = topElement;
	} 

	public V computeTarget(V source) {
		return topElement;
	}

	public EdgeFunction<V> composeWith(EdgeFunction<V> secondFunction) {
		return secondFunction;
	}

	public EdgeFunction<V> joinWith(EdgeFunction<V> otherFunction) {
		return otherFunction;
	}

	public boolean equalTo(EdgeFunction<V> other) {
		if(other instanceof AllTop) {
			@SuppressWarnings("rawtypes")
			AllTop allTop = (AllTop) other;
			return allTop.topElement.equals(topElement);
		}		
		return false;
	}

	public String toString() {
		return "alltop";
	}
	
}










eda5559e1827af880a91488176d13076daee72da


Switch branch/tag










heros


src


heros


edgefunc


AllTop.java



Find file
Normal viewHistoryPermalink




eda5559e1827af880a91488176d13076daee72da


Switch branch/tag










heros


src


heros


edgefunc


AllTop.java





eda5559e1827af880a91488176d13076daee72da


Switch branch/tag








eda5559e1827af880a91488176d13076daee72da


Switch branch/tag





eda5559e1827af880a91488176d13076daee72da

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src

heros

edgefunc

AllTop.java
Find file
Normal viewHistoryPermalink




AllTop.java



737 Bytes









Newer










Older









renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






1




package heros.edgefunc;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






2














renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






3




import heros.EdgeFunction;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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






public class AllTop<V> implements EdgeFunction<V> {
	
	private final V topElement; 

	public AllTop(V topElement){
		this.topElement = topElement;
	} 

	public V computeTarget(V source) {
		return topElement;
	}

	public EdgeFunction<V> composeWith(EdgeFunction<V> secondFunction) {
		return secondFunction;
	}

	public EdgeFunction<V> joinWith(EdgeFunction<V> otherFunction) {
		return otherFunction;
	}

	public boolean equalTo(EdgeFunction<V> other) {
		if(other instanceof AllTop) {
			@SuppressWarnings("rawtypes")
			AllTop allTop = (AllTop) other;
			return allTop.topElement.equals(topElement);
		}		
		return false;
	}

	public String toString() {
		return "alltop";
	}
	
}








AllTop.java



737 Bytes










AllTop.java



737 Bytes









Newer










Older
NewerOlder







renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






1




package heros.edgefunc;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






2














renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






3




import heros.EdgeFunction;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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






public class AllTop<V> implements EdgeFunction<V> {
	
	private final V topElement; 

	public AllTop(V topElement){
		this.topElement = topElement;
	} 

	public V computeTarget(V source) {
		return topElement;
	}

	public EdgeFunction<V> composeWith(EdgeFunction<V> secondFunction) {
		return secondFunction;
	}

	public EdgeFunction<V> joinWith(EdgeFunction<V> otherFunction) {
		return otherFunction;
	}

	public boolean equalTo(EdgeFunction<V> other) {
		if(other instanceof AllTop) {
			@SuppressWarnings("rawtypes")
			AllTop allTop = (AllTop) other;
			return allTop.topElement.equals(topElement);
		}		
		return false;
	}

	public String toString() {
		return "alltop";
	}
	
}







renamed package


 

 


Eric Bodden
committed
Nov 29, 2012



renamed package


 

 

renamed package

 

Eric Bodden
committed
Nov 29, 2012

1
package heros.edgefunc;packageheros.edgefunc;



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

2




renamed package


 

 


Eric Bodden
committed
Nov 29, 2012



renamed package


 

 

renamed package

 

Eric Bodden
committed
Nov 29, 2012

3
import heros.EdgeFunction;importheros.EdgeFunction;



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

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
public class AllTop<V> implements EdgeFunction<V> {publicclassAllTop<V>implementsEdgeFunction<V>{		private final V topElement; privatefinalVtopElement;	public AllTop(V topElement){publicAllTop(VtopElement){		this.topElement = topElement;this.topElement=topElement;	} }	public V computeTarget(V source) {publicVcomputeTarget(Vsource){		return topElement;returntopElement;	}}	public EdgeFunction<V> composeWith(EdgeFunction<V> secondFunction) {publicEdgeFunction<V>composeWith(EdgeFunction<V>secondFunction){		return secondFunction;returnsecondFunction;	}}	public EdgeFunction<V> joinWith(EdgeFunction<V> otherFunction) {publicEdgeFunction<V>joinWith(EdgeFunction<V>otherFunction){		return otherFunction;returnotherFunction;	}}	public boolean equalTo(EdgeFunction<V> other) {publicbooleanequalTo(EdgeFunction<V>other){		if(other instanceof AllTop) {if(otherinstanceofAllTop){			@SuppressWarnings("rawtypes")@SuppressWarnings("rawtypes")			AllTop allTop = (AllTop) other;AllTopallTop=(AllTop)other;			return allTop.topElement.equals(topElement);returnallTop.topElement.equals(topElement);		}		}		return false;returnfalse;	}}	public String toString() {publicStringtoString(){		return "alltop";return"alltop";	}}	}}





