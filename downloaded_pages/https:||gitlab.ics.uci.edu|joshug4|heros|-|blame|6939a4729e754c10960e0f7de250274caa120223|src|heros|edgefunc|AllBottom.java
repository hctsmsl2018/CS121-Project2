



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

6939a4729e754c10960e0f7de250274caa120223

















6939a4729e754c10960e0f7de250274caa120223


Switch branch/tag










heros


src


heros


edgefunc


AllBottom.java



Find file
Normal viewHistoryPermalink






AllBottom.java



1.59 KB









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




package heros.edgefunc;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






12














renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






13




import heros.EdgeFunction;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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






public class AllBottom<V> implements EdgeFunction<V> {
	
	private final V bottomElement;

	public AllBottom(V bottomElement){
		this.bottomElement = bottomElement;
	} 

	public V computeTarget(V source) {
		return bottomElement;
	}

	public EdgeFunction<V> composeWith(EdgeFunction<V> secondFunction) {









fix: 'bottom(id)' should return 'bottom' not 'id'. Thanks to Johannes Späth who found the bug.


 

 


Alexandre Bartel
committed
Jan 23, 2015






29




30




		if (secondFunction instanceof EdgeIdentity)
			return this;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




		return secondFunction;
	}

	public EdgeFunction<V> joinWith(EdgeFunction<V> otherFunction) {
		if(otherFunction == this || otherFunction.equalTo(this)) return this;
		if(otherFunction instanceof AllTop) {
			return this;
		}
		if(otherFunction instanceof EdgeIdentity) {
			return this;
		}
		throw new IllegalStateException("unexpected edge function: "+otherFunction);
	}

	public boolean equalTo(EdgeFunction<V> other) {
		if(other instanceof AllBottom) {
			@SuppressWarnings("rawtypes")
			AllBottom allBottom = (AllBottom) other;
			return allBottom.bottomElement.equals(bottomElement);
		}		
		return false;
	}
	
	public String toString() {
		return "allbottom";
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

6939a4729e754c10960e0f7de250274caa120223

















6939a4729e754c10960e0f7de250274caa120223


Switch branch/tag










heros


src


heros


edgefunc


AllBottom.java



Find file
Normal viewHistoryPermalink






AllBottom.java



1.59 KB









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




package heros.edgefunc;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






12














renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






13




import heros.EdgeFunction;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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






public class AllBottom<V> implements EdgeFunction<V> {
	
	private final V bottomElement;

	public AllBottom(V bottomElement){
		this.bottomElement = bottomElement;
	} 

	public V computeTarget(V source) {
		return bottomElement;
	}

	public EdgeFunction<V> composeWith(EdgeFunction<V> secondFunction) {









fix: 'bottom(id)' should return 'bottom' not 'id'. Thanks to Johannes Späth who found the bug.


 

 


Alexandre Bartel
committed
Jan 23, 2015






29




30




		if (secondFunction instanceof EdgeIdentity)
			return this;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




		return secondFunction;
	}

	public EdgeFunction<V> joinWith(EdgeFunction<V> otherFunction) {
		if(otherFunction == this || otherFunction.equalTo(this)) return this;
		if(otherFunction instanceof AllTop) {
			return this;
		}
		if(otherFunction instanceof EdgeIdentity) {
			return this;
		}
		throw new IllegalStateException("unexpected edge function: "+otherFunction);
	}

	public boolean equalTo(EdgeFunction<V> other) {
		if(other instanceof AllBottom) {
			@SuppressWarnings("rawtypes")
			AllBottom allBottom = (AllBottom) other;
			return allBottom.bottomElement.equals(bottomElement);
		}		
		return false;
	}
	
	public String toString() {
		return "allbottom";
	}

}











Open sidebar



Joshua Garcia heros

6939a4729e754c10960e0f7de250274caa120223







Open sidebar



Joshua Garcia heros

6939a4729e754c10960e0f7de250274caa120223




Open sidebar

Joshua Garcia heros

6939a4729e754c10960e0f7de250274caa120223


Joshua Garciaherosheros
6939a4729e754c10960e0f7de250274caa120223










6939a4729e754c10960e0f7de250274caa120223


Switch branch/tag










heros


src


heros


edgefunc


AllBottom.java



Find file
Normal viewHistoryPermalink






AllBottom.java



1.59 KB









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




package heros.edgefunc;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






12














renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






13




import heros.EdgeFunction;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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






public class AllBottom<V> implements EdgeFunction<V> {
	
	private final V bottomElement;

	public AllBottom(V bottomElement){
		this.bottomElement = bottomElement;
	} 

	public V computeTarget(V source) {
		return bottomElement;
	}

	public EdgeFunction<V> composeWith(EdgeFunction<V> secondFunction) {









fix: 'bottom(id)' should return 'bottom' not 'id'. Thanks to Johannes Späth who found the bug.


 

 


Alexandre Bartel
committed
Jan 23, 2015






29




30




		if (secondFunction instanceof EdgeIdentity)
			return this;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




		return secondFunction;
	}

	public EdgeFunction<V> joinWith(EdgeFunction<V> otherFunction) {
		if(otherFunction == this || otherFunction.equalTo(this)) return this;
		if(otherFunction instanceof AllTop) {
			return this;
		}
		if(otherFunction instanceof EdgeIdentity) {
			return this;
		}
		throw new IllegalStateException("unexpected edge function: "+otherFunction);
	}

	public boolean equalTo(EdgeFunction<V> other) {
		if(other instanceof AllBottom) {
			@SuppressWarnings("rawtypes")
			AllBottom allBottom = (AllBottom) other;
			return allBottom.bottomElement.equals(bottomElement);
		}		
		return false;
	}
	
	public String toString() {
		return "allbottom";
	}

}














6939a4729e754c10960e0f7de250274caa120223


Switch branch/tag










heros


src


heros


edgefunc


AllBottom.java



Find file
Normal viewHistoryPermalink






AllBottom.java



1.59 KB









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




package heros.edgefunc;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






12














renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






13




import heros.EdgeFunction;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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






public class AllBottom<V> implements EdgeFunction<V> {
	
	private final V bottomElement;

	public AllBottom(V bottomElement){
		this.bottomElement = bottomElement;
	} 

	public V computeTarget(V source) {
		return bottomElement;
	}

	public EdgeFunction<V> composeWith(EdgeFunction<V> secondFunction) {









fix: 'bottom(id)' should return 'bottom' not 'id'. Thanks to Johannes Späth who found the bug.


 

 


Alexandre Bartel
committed
Jan 23, 2015






29




30




		if (secondFunction instanceof EdgeIdentity)
			return this;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




		return secondFunction;
	}

	public EdgeFunction<V> joinWith(EdgeFunction<V> otherFunction) {
		if(otherFunction == this || otherFunction.equalTo(this)) return this;
		if(otherFunction instanceof AllTop) {
			return this;
		}
		if(otherFunction instanceof EdgeIdentity) {
			return this;
		}
		throw new IllegalStateException("unexpected edge function: "+otherFunction);
	}

	public boolean equalTo(EdgeFunction<V> other) {
		if(other instanceof AllBottom) {
			@SuppressWarnings("rawtypes")
			AllBottom allBottom = (AllBottom) other;
			return allBottom.bottomElement.equals(bottomElement);
		}		
		return false;
	}
	
	public String toString() {
		return "allbottom";
	}

}










6939a4729e754c10960e0f7de250274caa120223


Switch branch/tag










heros


src


heros


edgefunc


AllBottom.java



Find file
Normal viewHistoryPermalink




6939a4729e754c10960e0f7de250274caa120223


Switch branch/tag










heros


src


heros


edgefunc


AllBottom.java





6939a4729e754c10960e0f7de250274caa120223


Switch branch/tag








6939a4729e754c10960e0f7de250274caa120223


Switch branch/tag





6939a4729e754c10960e0f7de250274caa120223

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src

heros

edgefunc

AllBottom.java
Find file
Normal viewHistoryPermalink




AllBottom.java



1.59 KB









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




package heros.edgefunc;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






12














renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






13




import heros.EdgeFunction;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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






public class AllBottom<V> implements EdgeFunction<V> {
	
	private final V bottomElement;

	public AllBottom(V bottomElement){
		this.bottomElement = bottomElement;
	} 

	public V computeTarget(V source) {
		return bottomElement;
	}

	public EdgeFunction<V> composeWith(EdgeFunction<V> secondFunction) {









fix: 'bottom(id)' should return 'bottom' not 'id'. Thanks to Johannes Späth who found the bug.


 

 


Alexandre Bartel
committed
Jan 23, 2015






29




30




		if (secondFunction instanceof EdgeIdentity)
			return this;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




		return secondFunction;
	}

	public EdgeFunction<V> joinWith(EdgeFunction<V> otherFunction) {
		if(otherFunction == this || otherFunction.equalTo(this)) return this;
		if(otherFunction instanceof AllTop) {
			return this;
		}
		if(otherFunction instanceof EdgeIdentity) {
			return this;
		}
		throw new IllegalStateException("unexpected edge function: "+otherFunction);
	}

	public boolean equalTo(EdgeFunction<V> other) {
		if(other instanceof AllBottom) {
			@SuppressWarnings("rawtypes")
			AllBottom allBottom = (AllBottom) other;
			return allBottom.bottomElement.equals(bottomElement);
		}		
		return false;
	}
	
	public String toString() {
		return "allbottom";
	}

}








AllBottom.java



1.59 KB










AllBottom.java



1.59 KB









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




package heros.edgefunc;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






12














renamed package


 

 


Eric Bodden
committed
Nov 29, 2012






13




import heros.EdgeFunction;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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






public class AllBottom<V> implements EdgeFunction<V> {
	
	private final V bottomElement;

	public AllBottom(V bottomElement){
		this.bottomElement = bottomElement;
	} 

	public V computeTarget(V source) {
		return bottomElement;
	}

	public EdgeFunction<V> composeWith(EdgeFunction<V> secondFunction) {









fix: 'bottom(id)' should return 'bottom' not 'id'. Thanks to Johannes Späth who found the bug.


 

 


Alexandre Bartel
committed
Jan 23, 2015






29




30




		if (secondFunction instanceof EdgeIdentity)
			return this;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




		return secondFunction;
	}

	public EdgeFunction<V> joinWith(EdgeFunction<V> otherFunction) {
		if(otherFunction == this || otherFunction.equalTo(this)) return this;
		if(otherFunction instanceof AllTop) {
			return this;
		}
		if(otherFunction instanceof EdgeIdentity) {
			return this;
		}
		throw new IllegalStateException("unexpected edge function: "+otherFunction);
	}

	public boolean equalTo(EdgeFunction<V> other) {
		if(other instanceof AllBottom) {
			@SuppressWarnings("rawtypes")
			AllBottom allBottom = (AllBottom) other;
			return allBottom.bottomElement.equals(bottomElement);
		}		
		return false;
	}
	
	public String toString() {
		return "allbottom";
	}

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

12




renamed package


 

 


Eric Bodden
committed
Nov 29, 2012



renamed package


 

 

renamed package

 

Eric Bodden
committed
Nov 29, 2012

13
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
public class AllBottom<V> implements EdgeFunction<V> {publicclassAllBottom<V>implementsEdgeFunction<V>{		private final V bottomElement;privatefinalVbottomElement;	public AllBottom(V bottomElement){publicAllBottom(VbottomElement){		this.bottomElement = bottomElement;this.bottomElement=bottomElement;	} }	public V computeTarget(V source) {publicVcomputeTarget(Vsource){		return bottomElement;returnbottomElement;	}}	public EdgeFunction<V> composeWith(EdgeFunction<V> secondFunction) {publicEdgeFunction<V>composeWith(EdgeFunction<V>secondFunction){



fix: 'bottom(id)' should return 'bottom' not 'id'. Thanks to Johannes Späth who found the bug.


 

 


Alexandre Bartel
committed
Jan 23, 2015



fix: 'bottom(id)' should return 'bottom' not 'id'. Thanks to Johannes Späth who found the bug.


 

 

fix: 'bottom(id)' should return 'bottom' not 'id'. Thanks to Johannes Späth who found the bug.

 

Alexandre Bartel
committed
Jan 23, 2015

29

30
		if (secondFunction instanceof EdgeIdentity)if(secondFunctioninstanceofEdgeIdentity)			return this;returnthis;



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

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
		return secondFunction;returnsecondFunction;	}}	public EdgeFunction<V> joinWith(EdgeFunction<V> otherFunction) {publicEdgeFunction<V>joinWith(EdgeFunction<V>otherFunction){		if(otherFunction == this || otherFunction.equalTo(this)) return this;if(otherFunction==this||otherFunction.equalTo(this))returnthis;		if(otherFunction instanceof AllTop) {if(otherFunctioninstanceofAllTop){			return this;returnthis;		}}		if(otherFunction instanceof EdgeIdentity) {if(otherFunctioninstanceofEdgeIdentity){			return this;returnthis;		}}		throw new IllegalStateException("unexpected edge function: "+otherFunction);thrownewIllegalStateException("unexpected edge function: "+otherFunction);	}}	public boolean equalTo(EdgeFunction<V> other) {publicbooleanequalTo(EdgeFunction<V>other){		if(other instanceof AllBottom) {if(otherinstanceofAllBottom){			@SuppressWarnings("rawtypes")@SuppressWarnings("rawtypes")			AllBottom allBottom = (AllBottom) other;AllBottomallBottom=(AllBottom)other;			return allBottom.bottomElement.equals(bottomElement);returnallBottom.bottomElement.equals(bottomElement);		}		}		return false;returnfalse;	}}		public String toString() {publicStringtoString(){		return "allbottom";return"allbottom";	}}}}





