



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

c52d9928c59c397f1aafcfcefa2458bddb24d599

















c52d9928c59c397f1aafcfcefa2458bddb24d599


Switch branch/tag










heros


src


heros


TwoElementSet.java



Find file
Normal viewHistoryPermalink






TwoElementSet.java



1.62 KB









Newer










Older









added class TwoElementSet, which implements exactly what the name says; useful...



 


Eric Bodden
committed
Dec 24, 2013






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




/*******************************************************************************
 * Copyright (c) 2013 Eric Bodden.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation
 ******************************************************************************/
package heros;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An unmodifiable set holding exactly two elements. Particularly useful within flow functions.
 *
 * @param <E>
 * @see FlowFunction
 */
public class TwoElementSet<E> extends AbstractSet<E> {
	
	protected final E first, second;
	
	public TwoElementSet(E first, E second) {
		this.first = first;
		this.second = second;
	}	
	
	public static <E> TwoElementSet<E> twoElementSet(E first, E second) {
		return new TwoElementSet<E>(first, second);
	}
	
	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {		
			int elementsRead = 0;
			
			@Override
			public boolean hasNext() {
				return elementsRead<2;
			}

			@Override
			public E next() {
				switch(elementsRead) {
				case 0:
					elementsRead++;
					return first;
				case 1:
					elementsRead++;
					return second;
				default:
					throw new NoSuchElementException();	
				}
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public int size() {
		return 2;
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

c52d9928c59c397f1aafcfcefa2458bddb24d599

















c52d9928c59c397f1aafcfcefa2458bddb24d599


Switch branch/tag










heros


src


heros


TwoElementSet.java



Find file
Normal viewHistoryPermalink






TwoElementSet.java



1.62 KB









Newer










Older









added class TwoElementSet, which implements exactly what the name says; useful...



 


Eric Bodden
committed
Dec 24, 2013






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




/*******************************************************************************
 * Copyright (c) 2013 Eric Bodden.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation
 ******************************************************************************/
package heros;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An unmodifiable set holding exactly two elements. Particularly useful within flow functions.
 *
 * @param <E>
 * @see FlowFunction
 */
public class TwoElementSet<E> extends AbstractSet<E> {
	
	protected final E first, second;
	
	public TwoElementSet(E first, E second) {
		this.first = first;
		this.second = second;
	}	
	
	public static <E> TwoElementSet<E> twoElementSet(E first, E second) {
		return new TwoElementSet<E>(first, second);
	}
	
	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {		
			int elementsRead = 0;
			
			@Override
			public boolean hasNext() {
				return elementsRead<2;
			}

			@Override
			public E next() {
				switch(elementsRead) {
				case 0:
					elementsRead++;
					return first;
				case 1:
					elementsRead++;
					return second;
				default:
					throw new NoSuchElementException();	
				}
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public int size() {
		return 2;
	}	
}











Open sidebar



Joshua Garcia heros

c52d9928c59c397f1aafcfcefa2458bddb24d599







Open sidebar



Joshua Garcia heros

c52d9928c59c397f1aafcfcefa2458bddb24d599




Open sidebar

Joshua Garcia heros

c52d9928c59c397f1aafcfcefa2458bddb24d599


Joshua Garciaherosheros
c52d9928c59c397f1aafcfcefa2458bddb24d599










c52d9928c59c397f1aafcfcefa2458bddb24d599


Switch branch/tag










heros


src


heros


TwoElementSet.java



Find file
Normal viewHistoryPermalink






TwoElementSet.java



1.62 KB









Newer










Older









added class TwoElementSet, which implements exactly what the name says; useful...



 


Eric Bodden
committed
Dec 24, 2013






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




/*******************************************************************************
 * Copyright (c) 2013 Eric Bodden.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation
 ******************************************************************************/
package heros;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An unmodifiable set holding exactly two elements. Particularly useful within flow functions.
 *
 * @param <E>
 * @see FlowFunction
 */
public class TwoElementSet<E> extends AbstractSet<E> {
	
	protected final E first, second;
	
	public TwoElementSet(E first, E second) {
		this.first = first;
		this.second = second;
	}	
	
	public static <E> TwoElementSet<E> twoElementSet(E first, E second) {
		return new TwoElementSet<E>(first, second);
	}
	
	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {		
			int elementsRead = 0;
			
			@Override
			public boolean hasNext() {
				return elementsRead<2;
			}

			@Override
			public E next() {
				switch(elementsRead) {
				case 0:
					elementsRead++;
					return first;
				case 1:
					elementsRead++;
					return second;
				default:
					throw new NoSuchElementException();	
				}
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public int size() {
		return 2;
	}	
}














c52d9928c59c397f1aafcfcefa2458bddb24d599


Switch branch/tag










heros


src


heros


TwoElementSet.java



Find file
Normal viewHistoryPermalink






TwoElementSet.java



1.62 KB









Newer










Older









added class TwoElementSet, which implements exactly what the name says; useful...



 


Eric Bodden
committed
Dec 24, 2013






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




/*******************************************************************************
 * Copyright (c) 2013 Eric Bodden.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation
 ******************************************************************************/
package heros;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An unmodifiable set holding exactly two elements. Particularly useful within flow functions.
 *
 * @param <E>
 * @see FlowFunction
 */
public class TwoElementSet<E> extends AbstractSet<E> {
	
	protected final E first, second;
	
	public TwoElementSet(E first, E second) {
		this.first = first;
		this.second = second;
	}	
	
	public static <E> TwoElementSet<E> twoElementSet(E first, E second) {
		return new TwoElementSet<E>(first, second);
	}
	
	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {		
			int elementsRead = 0;
			
			@Override
			public boolean hasNext() {
				return elementsRead<2;
			}

			@Override
			public E next() {
				switch(elementsRead) {
				case 0:
					elementsRead++;
					return first;
				case 1:
					elementsRead++;
					return second;
				default:
					throw new NoSuchElementException();	
				}
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public int size() {
		return 2;
	}	
}










c52d9928c59c397f1aafcfcefa2458bddb24d599


Switch branch/tag










heros


src


heros


TwoElementSet.java



Find file
Normal viewHistoryPermalink




c52d9928c59c397f1aafcfcefa2458bddb24d599


Switch branch/tag










heros


src


heros


TwoElementSet.java





c52d9928c59c397f1aafcfcefa2458bddb24d599


Switch branch/tag








c52d9928c59c397f1aafcfcefa2458bddb24d599


Switch branch/tag





c52d9928c59c397f1aafcfcefa2458bddb24d599

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src

heros

TwoElementSet.java
Find file
Normal viewHistoryPermalink




TwoElementSet.java



1.62 KB









Newer










Older









added class TwoElementSet, which implements exactly what the name says; useful...



 


Eric Bodden
committed
Dec 24, 2013






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




/*******************************************************************************
 * Copyright (c) 2013 Eric Bodden.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation
 ******************************************************************************/
package heros;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An unmodifiable set holding exactly two elements. Particularly useful within flow functions.
 *
 * @param <E>
 * @see FlowFunction
 */
public class TwoElementSet<E> extends AbstractSet<E> {
	
	protected final E first, second;
	
	public TwoElementSet(E first, E second) {
		this.first = first;
		this.second = second;
	}	
	
	public static <E> TwoElementSet<E> twoElementSet(E first, E second) {
		return new TwoElementSet<E>(first, second);
	}
	
	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {		
			int elementsRead = 0;
			
			@Override
			public boolean hasNext() {
				return elementsRead<2;
			}

			@Override
			public E next() {
				switch(elementsRead) {
				case 0:
					elementsRead++;
					return first;
				case 1:
					elementsRead++;
					return second;
				default:
					throw new NoSuchElementException();	
				}
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public int size() {
		return 2;
	}	
}








TwoElementSet.java



1.62 KB










TwoElementSet.java



1.62 KB









Newer










Older
NewerOlder







added class TwoElementSet, which implements exactly what the name says; useful...



 


Eric Bodden
committed
Dec 24, 2013






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




/*******************************************************************************
 * Copyright (c) 2013 Eric Bodden.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Eric Bodden - initial API and implementation
 ******************************************************************************/
package heros;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An unmodifiable set holding exactly two elements. Particularly useful within flow functions.
 *
 * @param <E>
 * @see FlowFunction
 */
public class TwoElementSet<E> extends AbstractSet<E> {
	
	protected final E first, second;
	
	public TwoElementSet(E first, E second) {
		this.first = first;
		this.second = second;
	}	
	
	public static <E> TwoElementSet<E> twoElementSet(E first, E second) {
		return new TwoElementSet<E>(first, second);
	}
	
	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {		
			int elementsRead = 0;
			
			@Override
			public boolean hasNext() {
				return elementsRead<2;
			}

			@Override
			public E next() {
				switch(elementsRead) {
				case 0:
					elementsRead++;
					return first;
				case 1:
					elementsRead++;
					return second;
				default:
					throw new NoSuchElementException();	
				}
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public int size() {
		return 2;
	}	
}







added class TwoElementSet, which implements exactly what the name says; useful...



 


Eric Bodden
committed
Dec 24, 2013



added class TwoElementSet, which implements exactly what the name says; useful...



 

added class TwoElementSet, which implements exactly what the name says; useful...


Eric Bodden
committed
Dec 24, 2013

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
/*******************************************************************************/******************************************************************************* * Copyright (c) 2013 Eric Bodden. * Copyright (c) 2013 Eric Bodden. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Eric Bodden - initial API and implementation *     Eric Bodden - initial API and implementation ******************************************************************************/ ******************************************************************************/package heros;packageheros;import java.util.AbstractSet;importjava.util.AbstractSet;import java.util.Iterator;importjava.util.Iterator;import java.util.NoSuchElementException;importjava.util.NoSuchElementException;/**/** * An unmodifiable set holding exactly two elements. Particularly useful within flow functions. * An unmodifiable set holding exactly two elements. Particularly useful within flow functions. * * * @param <E> * @param <E> * @see FlowFunction * @see FlowFunction */ */public class TwoElementSet<E> extends AbstractSet<E> {publicclassTwoElementSet<E>extendsAbstractSet<E>{		protected final E first, second;protectedfinalEfirst,second;		public TwoElementSet(E first, E second) {publicTwoElementSet(Efirst,Esecond){		this.first = first;this.first=first;		this.second = second;this.second=second;	}	}		public static <E> TwoElementSet<E> twoElementSet(E first, E second) {publicstatic<E>TwoElementSet<E>twoElementSet(Efirst,Esecond){		return new TwoElementSet<E>(first, second);returnnewTwoElementSet<E>(first,second);	}}		@Override@Override	public Iterator<E> iterator() {publicIterator<E>iterator(){		return new Iterator<E>() {		returnnewIterator<E>(){			int elementsRead = 0;intelementsRead=0;						@Override@Override			public boolean hasNext() {publicbooleanhasNext(){				return elementsRead<2;returnelementsRead<2;			}}			@Override@Override			public E next() {publicEnext(){				switch(elementsRead) {switch(elementsRead){				case 0:case0:					elementsRead++;elementsRead++;					return first;returnfirst;				case 1:case1:					elementsRead++;elementsRead++;					return second;returnsecond;				default:default:					throw new NoSuchElementException();	thrownewNoSuchElementException();				}}			}}			@Override@Override			public void remove() {publicvoidremove(){				throw new UnsupportedOperationException();thrownewUnsupportedOperationException();			}}		};};	}}	@Override@Override	public int size() {publicintsize(){		return 2;return2;	}	}}}





