



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

b131fd75dc553a141444d3f8f99405593c8d3614

















b131fd75dc553a141444d3f8f99405593c8d3614


Switch branch/tag










heros


src


heros


alias


AccessPath.java



Find file
Normal viewHistoryPermalink






AccessPath.java



6.17 KB









Newer










Older









cleaning code



Johannes Lerch
committed
Jan 07, 2015




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
package heros.alias;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

@SuppressWarnings("unchecked")
public class AccessPath<FieldRef> {









Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




23


24



	private final FieldRef[] accesses;
	private final Set<FieldRef>[] exclusions;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




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



	
	public AccessPath() {
		accesses = (FieldRef[]) new Object[0];
		exclusions = new Set[0];
	}
	
	AccessPath(FieldRef[] accesses, Set<FieldRef>[] exclusions) {
		this.accesses = accesses;
		this.exclusions = exclusions;
	}

	public boolean hasExclusions() {
		return exclusions.length > 0;
	}
	
	public boolean isAccessInExclusions(FieldRef... fieldReferences) {
		for(int i=0; i<fieldReferences.length && i<exclusions.length; i++) {
			if(exclusions[i].contains(fieldReferences[i]))
				return true;
		}			
		return false;
	}
	
	public AccessPath<FieldRef> addFieldReference(FieldRef... fieldReferences) {
		if(isAccessInExclusions(fieldReferences))
			throw new IllegalArgumentException();

		FieldRef[] newAccesses = Arrays.copyOf(accesses, accesses.length+fieldReferences.length);
		System.arraycopy(fieldReferences, 0, newAccesses, accesses.length, fieldReferences.length);
		Set<FieldRef>[] newExclusionsArray = exclusions.length < fieldReferences.length ? exclusions : Arrays.copyOfRange(exclusions, fieldReferences.length, exclusions.length);			
		return new AccessPath<FieldRef>(newAccesses, newExclusionsArray);
	}

	public ExclusionSet getExclusions(int index) {
		return new ExclusionSet(index);
	}
	








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




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



	public AccessPath<FieldRef> append(AccessPath<FieldRef> accessPath) {
		if(exclusions.length > 0) 
			throw new IllegalStateException();
		
		FieldRef[] newAccesses = Arrays.copyOf(accesses, accesses.length + accessPath.accesses.length);
		System.arraycopy(accessPath.accesses, 0, newAccesses, accesses.length, accessPath.accesses.length);
		return new AccessPath<FieldRef>(newAccesses, accessPath.exclusions);
	}

	public AccessPath<FieldRef> removeFirstAccessIfAvailable() {
		if(accesses.length > 0)
			return new AccessPath<FieldRef>(Arrays.copyOfRange(accesses, 1, accesses.length), exclusions);
		else if(exclusions.length > 0)
			return new AccessPath<FieldRef>(accesses, Arrays.copyOfRange(exclusions, 1, exclusions.length));
		else
			return this;
	}

	public AccessPath<FieldRef> mergeExcludedFieldReference(FieldRef... fieldRef) {
		if(hasExclusions())
			return getExclusions(0).addExclusion(fieldRef);
		else
			return appendExcludedFieldReference(fieldRef);
	}
	








cleaning code



Johannes Lerch
committed
Jan 07, 2015




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


114


115


116


117


118


119


120


121


122


123


124


125


126


127


128


129


130


131


132


133


134


135


136


137


138


139


140


141


142


143


144


145


146


147


148


149


150


151


152


153


154


155


156


157


158


159


160


161


162


163


164


165


166


167


168


169


170


171


172


173


174


175


176


177


178


179


180


181


182



	public AccessPath<FieldRef> appendExcludedFieldReference(FieldRef... fieldReferences) {
		Set<FieldRef>[] newExclusionsArray = Arrays.copyOf(exclusions, exclusions.length+1);
		newExclusionsArray[exclusions.length] = Sets.newHashSet(fieldReferences);
		return new AccessPath<>(accesses, newExclusionsArray);
	}
	
	public boolean isPrefixOf(AccessPath<FieldRef> accessPath) {
		if(accesses.length > accessPath.accesses.length)
			return false;
		
		if(accesses.length + exclusions.length > accessPath.accesses.length + accessPath.exclusions.length)
			return false;
		
		for(int i=0; i<accesses.length; i++) {
			if(!accesses[i].equals(accessPath.accesses[i]))
				return false;
		}
		
		for(int i=0; i<exclusions.length; i++) {
			if(i+accesses.length < accessPath.accesses.length) {
				if(exclusions[i].contains(accessPath.accesses[i+accesses.length]))
					return false;
			}
			else {
				if(!exclusions[i].containsAll(accessPath.exclusions[i+accesses.length - accessPath.accesses.length]))
					return false;
			}
		}
		
		return true;
	}
	
	public FieldRef[] getDeltaTo(AccessPath<FieldRef> accPath) {
		if(isPrefixOf(accPath))
			return Arrays.copyOfRange(accPath.accesses, accesses.length, accPath.accesses.length);
		else
			throw new IllegalArgumentException("Given AccessPath must be a prefix of the current AccessPath");
	}
	
	public AccessPath<FieldRef> mergeExcludedFieldReferences(AccessPath<FieldRef> accPath) {
		Set<FieldRef>[] newExclusionArray = new Set[Math.max(exclusions.length,accPath.exclusions.length)];
		for(int i=0; i<newExclusionArray.length; i++) {
			newExclusionArray[i] = Sets.newHashSet();
			if(i<exclusions.length)
				newExclusionArray[i].addAll(exclusions[i]);
			if(i<accPath.exclusions.length)
				newExclusionArray[i].addAll(accPath.exclusions[i]);
		}
		return new AccessPath<>(accesses, newExclusionArray);
	}
	
	public boolean isEmpty() {
		return exclusions.length == 0 && accesses.length == 0;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(accesses);
		result = prime * result + Arrays.hashCode(exclusions);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AccessPath))
			return false;
		AccessPath other = (AccessPath) obj;
		if (!Arrays.equals(accesses, other.accesses))
			return false;
		if (!Arrays.equals(exclusions, other.exclusions))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String result = accesses.length > 0 ? "."+Joiner.on(".").join(accesses) : "";
		for(Set<FieldRef> exclusion : exclusions) {
			result += "^" + Joiner.on(",").join(exclusion);
		}
		return result;
	}
	
	public class ExclusionSet {
		private int index;
	
		private ExclusionSet(int index) {
			this.index = index;
		}
		








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




183



		public AccessPath<FieldRef> addExclusion(FieldRef... exclusion) {








cleaning code



Johannes Lerch
committed
Jan 07, 2015




184



			HashSet<FieldRef> newExclusions = Sets.newHashSet(exclusions[index]);








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




185


186



			for(FieldRef excl : exclusion)
				newExclusions.add(excl);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




187


188


189


190


191


192



			Set<FieldRef>[] newExclusionsArray = exclusions.clone();
			newExclusionsArray[index] = newExclusions;
			return new AccessPath<FieldRef>(accesses, newExclusionsArray);
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

b131fd75dc553a141444d3f8f99405593c8d3614

















b131fd75dc553a141444d3f8f99405593c8d3614


Switch branch/tag










heros


src


heros


alias


AccessPath.java



Find file
Normal viewHistoryPermalink






AccessPath.java



6.17 KB









Newer










Older









cleaning code



Johannes Lerch
committed
Jan 07, 2015




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
package heros.alias;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

@SuppressWarnings("unchecked")
public class AccessPath<FieldRef> {









Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




23


24



	private final FieldRef[] accesses;
	private final Set<FieldRef>[] exclusions;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




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



	
	public AccessPath() {
		accesses = (FieldRef[]) new Object[0];
		exclusions = new Set[0];
	}
	
	AccessPath(FieldRef[] accesses, Set<FieldRef>[] exclusions) {
		this.accesses = accesses;
		this.exclusions = exclusions;
	}

	public boolean hasExclusions() {
		return exclusions.length > 0;
	}
	
	public boolean isAccessInExclusions(FieldRef... fieldReferences) {
		for(int i=0; i<fieldReferences.length && i<exclusions.length; i++) {
			if(exclusions[i].contains(fieldReferences[i]))
				return true;
		}			
		return false;
	}
	
	public AccessPath<FieldRef> addFieldReference(FieldRef... fieldReferences) {
		if(isAccessInExclusions(fieldReferences))
			throw new IllegalArgumentException();

		FieldRef[] newAccesses = Arrays.copyOf(accesses, accesses.length+fieldReferences.length);
		System.arraycopy(fieldReferences, 0, newAccesses, accesses.length, fieldReferences.length);
		Set<FieldRef>[] newExclusionsArray = exclusions.length < fieldReferences.length ? exclusions : Arrays.copyOfRange(exclusions, fieldReferences.length, exclusions.length);			
		return new AccessPath<FieldRef>(newAccesses, newExclusionsArray);
	}

	public ExclusionSet getExclusions(int index) {
		return new ExclusionSet(index);
	}
	








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




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



	public AccessPath<FieldRef> append(AccessPath<FieldRef> accessPath) {
		if(exclusions.length > 0) 
			throw new IllegalStateException();
		
		FieldRef[] newAccesses = Arrays.copyOf(accesses, accesses.length + accessPath.accesses.length);
		System.arraycopy(accessPath.accesses, 0, newAccesses, accesses.length, accessPath.accesses.length);
		return new AccessPath<FieldRef>(newAccesses, accessPath.exclusions);
	}

	public AccessPath<FieldRef> removeFirstAccessIfAvailable() {
		if(accesses.length > 0)
			return new AccessPath<FieldRef>(Arrays.copyOfRange(accesses, 1, accesses.length), exclusions);
		else if(exclusions.length > 0)
			return new AccessPath<FieldRef>(accesses, Arrays.copyOfRange(exclusions, 1, exclusions.length));
		else
			return this;
	}

	public AccessPath<FieldRef> mergeExcludedFieldReference(FieldRef... fieldRef) {
		if(hasExclusions())
			return getExclusions(0).addExclusion(fieldRef);
		else
			return appendExcludedFieldReference(fieldRef);
	}
	








cleaning code



Johannes Lerch
committed
Jan 07, 2015




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


114


115


116


117


118


119


120


121


122


123


124


125


126


127


128


129


130


131


132


133


134


135


136


137


138


139


140


141


142


143


144


145


146


147


148


149


150


151


152


153


154


155


156


157


158


159


160


161


162


163


164


165


166


167


168


169


170


171


172


173


174


175


176


177


178


179


180


181


182



	public AccessPath<FieldRef> appendExcludedFieldReference(FieldRef... fieldReferences) {
		Set<FieldRef>[] newExclusionsArray = Arrays.copyOf(exclusions, exclusions.length+1);
		newExclusionsArray[exclusions.length] = Sets.newHashSet(fieldReferences);
		return new AccessPath<>(accesses, newExclusionsArray);
	}
	
	public boolean isPrefixOf(AccessPath<FieldRef> accessPath) {
		if(accesses.length > accessPath.accesses.length)
			return false;
		
		if(accesses.length + exclusions.length > accessPath.accesses.length + accessPath.exclusions.length)
			return false;
		
		for(int i=0; i<accesses.length; i++) {
			if(!accesses[i].equals(accessPath.accesses[i]))
				return false;
		}
		
		for(int i=0; i<exclusions.length; i++) {
			if(i+accesses.length < accessPath.accesses.length) {
				if(exclusions[i].contains(accessPath.accesses[i+accesses.length]))
					return false;
			}
			else {
				if(!exclusions[i].containsAll(accessPath.exclusions[i+accesses.length - accessPath.accesses.length]))
					return false;
			}
		}
		
		return true;
	}
	
	public FieldRef[] getDeltaTo(AccessPath<FieldRef> accPath) {
		if(isPrefixOf(accPath))
			return Arrays.copyOfRange(accPath.accesses, accesses.length, accPath.accesses.length);
		else
			throw new IllegalArgumentException("Given AccessPath must be a prefix of the current AccessPath");
	}
	
	public AccessPath<FieldRef> mergeExcludedFieldReferences(AccessPath<FieldRef> accPath) {
		Set<FieldRef>[] newExclusionArray = new Set[Math.max(exclusions.length,accPath.exclusions.length)];
		for(int i=0; i<newExclusionArray.length; i++) {
			newExclusionArray[i] = Sets.newHashSet();
			if(i<exclusions.length)
				newExclusionArray[i].addAll(exclusions[i]);
			if(i<accPath.exclusions.length)
				newExclusionArray[i].addAll(accPath.exclusions[i]);
		}
		return new AccessPath<>(accesses, newExclusionArray);
	}
	
	public boolean isEmpty() {
		return exclusions.length == 0 && accesses.length == 0;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(accesses);
		result = prime * result + Arrays.hashCode(exclusions);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AccessPath))
			return false;
		AccessPath other = (AccessPath) obj;
		if (!Arrays.equals(accesses, other.accesses))
			return false;
		if (!Arrays.equals(exclusions, other.exclusions))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String result = accesses.length > 0 ? "."+Joiner.on(".").join(accesses) : "";
		for(Set<FieldRef> exclusion : exclusions) {
			result += "^" + Joiner.on(",").join(exclusion);
		}
		return result;
	}
	
	public class ExclusionSet {
		private int index;
	
		private ExclusionSet(int index) {
			this.index = index;
		}
		








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




183



		public AccessPath<FieldRef> addExclusion(FieldRef... exclusion) {








cleaning code



Johannes Lerch
committed
Jan 07, 2015




184



			HashSet<FieldRef> newExclusions = Sets.newHashSet(exclusions[index]);








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




185


186



			for(FieldRef excl : exclusion)
				newExclusions.add(excl);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




187


188


189


190


191


192



			Set<FieldRef>[] newExclusionsArray = exclusions.clone();
			newExclusionsArray[index] = newExclusions;
			return new AccessPath<FieldRef>(accesses, newExclusionsArray);
		}
	}
}












Open sidebar



Joshua Garcia heros

b131fd75dc553a141444d3f8f99405593c8d3614







Open sidebar



Joshua Garcia heros

b131fd75dc553a141444d3f8f99405593c8d3614




Open sidebar

Joshua Garcia heros

b131fd75dc553a141444d3f8f99405593c8d3614


Joshua Garciaherosheros
b131fd75dc553a141444d3f8f99405593c8d3614










b131fd75dc553a141444d3f8f99405593c8d3614


Switch branch/tag










heros


src


heros


alias


AccessPath.java



Find file
Normal viewHistoryPermalink






AccessPath.java



6.17 KB









Newer










Older









cleaning code



Johannes Lerch
committed
Jan 07, 2015




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
package heros.alias;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

@SuppressWarnings("unchecked")
public class AccessPath<FieldRef> {









Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




23


24



	private final FieldRef[] accesses;
	private final Set<FieldRef>[] exclusions;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




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



	
	public AccessPath() {
		accesses = (FieldRef[]) new Object[0];
		exclusions = new Set[0];
	}
	
	AccessPath(FieldRef[] accesses, Set<FieldRef>[] exclusions) {
		this.accesses = accesses;
		this.exclusions = exclusions;
	}

	public boolean hasExclusions() {
		return exclusions.length > 0;
	}
	
	public boolean isAccessInExclusions(FieldRef... fieldReferences) {
		for(int i=0; i<fieldReferences.length && i<exclusions.length; i++) {
			if(exclusions[i].contains(fieldReferences[i]))
				return true;
		}			
		return false;
	}
	
	public AccessPath<FieldRef> addFieldReference(FieldRef... fieldReferences) {
		if(isAccessInExclusions(fieldReferences))
			throw new IllegalArgumentException();

		FieldRef[] newAccesses = Arrays.copyOf(accesses, accesses.length+fieldReferences.length);
		System.arraycopy(fieldReferences, 0, newAccesses, accesses.length, fieldReferences.length);
		Set<FieldRef>[] newExclusionsArray = exclusions.length < fieldReferences.length ? exclusions : Arrays.copyOfRange(exclusions, fieldReferences.length, exclusions.length);			
		return new AccessPath<FieldRef>(newAccesses, newExclusionsArray);
	}

	public ExclusionSet getExclusions(int index) {
		return new ExclusionSet(index);
	}
	








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




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



	public AccessPath<FieldRef> append(AccessPath<FieldRef> accessPath) {
		if(exclusions.length > 0) 
			throw new IllegalStateException();
		
		FieldRef[] newAccesses = Arrays.copyOf(accesses, accesses.length + accessPath.accesses.length);
		System.arraycopy(accessPath.accesses, 0, newAccesses, accesses.length, accessPath.accesses.length);
		return new AccessPath<FieldRef>(newAccesses, accessPath.exclusions);
	}

	public AccessPath<FieldRef> removeFirstAccessIfAvailable() {
		if(accesses.length > 0)
			return new AccessPath<FieldRef>(Arrays.copyOfRange(accesses, 1, accesses.length), exclusions);
		else if(exclusions.length > 0)
			return new AccessPath<FieldRef>(accesses, Arrays.copyOfRange(exclusions, 1, exclusions.length));
		else
			return this;
	}

	public AccessPath<FieldRef> mergeExcludedFieldReference(FieldRef... fieldRef) {
		if(hasExclusions())
			return getExclusions(0).addExclusion(fieldRef);
		else
			return appendExcludedFieldReference(fieldRef);
	}
	








cleaning code



Johannes Lerch
committed
Jan 07, 2015




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


114


115


116


117


118


119


120


121


122


123


124


125


126


127


128


129


130


131


132


133


134


135


136


137


138


139


140


141


142


143


144


145


146


147


148


149


150


151


152


153


154


155


156


157


158


159


160


161


162


163


164


165


166


167


168


169


170


171


172


173


174


175


176


177


178


179


180


181


182



	public AccessPath<FieldRef> appendExcludedFieldReference(FieldRef... fieldReferences) {
		Set<FieldRef>[] newExclusionsArray = Arrays.copyOf(exclusions, exclusions.length+1);
		newExclusionsArray[exclusions.length] = Sets.newHashSet(fieldReferences);
		return new AccessPath<>(accesses, newExclusionsArray);
	}
	
	public boolean isPrefixOf(AccessPath<FieldRef> accessPath) {
		if(accesses.length > accessPath.accesses.length)
			return false;
		
		if(accesses.length + exclusions.length > accessPath.accesses.length + accessPath.exclusions.length)
			return false;
		
		for(int i=0; i<accesses.length; i++) {
			if(!accesses[i].equals(accessPath.accesses[i]))
				return false;
		}
		
		for(int i=0; i<exclusions.length; i++) {
			if(i+accesses.length < accessPath.accesses.length) {
				if(exclusions[i].contains(accessPath.accesses[i+accesses.length]))
					return false;
			}
			else {
				if(!exclusions[i].containsAll(accessPath.exclusions[i+accesses.length - accessPath.accesses.length]))
					return false;
			}
		}
		
		return true;
	}
	
	public FieldRef[] getDeltaTo(AccessPath<FieldRef> accPath) {
		if(isPrefixOf(accPath))
			return Arrays.copyOfRange(accPath.accesses, accesses.length, accPath.accesses.length);
		else
			throw new IllegalArgumentException("Given AccessPath must be a prefix of the current AccessPath");
	}
	
	public AccessPath<FieldRef> mergeExcludedFieldReferences(AccessPath<FieldRef> accPath) {
		Set<FieldRef>[] newExclusionArray = new Set[Math.max(exclusions.length,accPath.exclusions.length)];
		for(int i=0; i<newExclusionArray.length; i++) {
			newExclusionArray[i] = Sets.newHashSet();
			if(i<exclusions.length)
				newExclusionArray[i].addAll(exclusions[i]);
			if(i<accPath.exclusions.length)
				newExclusionArray[i].addAll(accPath.exclusions[i]);
		}
		return new AccessPath<>(accesses, newExclusionArray);
	}
	
	public boolean isEmpty() {
		return exclusions.length == 0 && accesses.length == 0;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(accesses);
		result = prime * result + Arrays.hashCode(exclusions);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AccessPath))
			return false;
		AccessPath other = (AccessPath) obj;
		if (!Arrays.equals(accesses, other.accesses))
			return false;
		if (!Arrays.equals(exclusions, other.exclusions))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String result = accesses.length > 0 ? "."+Joiner.on(".").join(accesses) : "";
		for(Set<FieldRef> exclusion : exclusions) {
			result += "^" + Joiner.on(",").join(exclusion);
		}
		return result;
	}
	
	public class ExclusionSet {
		private int index;
	
		private ExclusionSet(int index) {
			this.index = index;
		}
		








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




183



		public AccessPath<FieldRef> addExclusion(FieldRef... exclusion) {








cleaning code



Johannes Lerch
committed
Jan 07, 2015




184



			HashSet<FieldRef> newExclusions = Sets.newHashSet(exclusions[index]);








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




185


186



			for(FieldRef excl : exclusion)
				newExclusions.add(excl);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




187


188


189


190


191


192



			Set<FieldRef>[] newExclusionsArray = exclusions.clone();
			newExclusionsArray[index] = newExclusions;
			return new AccessPath<FieldRef>(accesses, newExclusionsArray);
		}
	}
}















b131fd75dc553a141444d3f8f99405593c8d3614


Switch branch/tag










heros


src


heros


alias


AccessPath.java



Find file
Normal viewHistoryPermalink






AccessPath.java



6.17 KB









Newer










Older









cleaning code



Johannes Lerch
committed
Jan 07, 2015




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
package heros.alias;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

@SuppressWarnings("unchecked")
public class AccessPath<FieldRef> {









Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




23


24



	private final FieldRef[] accesses;
	private final Set<FieldRef>[] exclusions;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




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



	
	public AccessPath() {
		accesses = (FieldRef[]) new Object[0];
		exclusions = new Set[0];
	}
	
	AccessPath(FieldRef[] accesses, Set<FieldRef>[] exclusions) {
		this.accesses = accesses;
		this.exclusions = exclusions;
	}

	public boolean hasExclusions() {
		return exclusions.length > 0;
	}
	
	public boolean isAccessInExclusions(FieldRef... fieldReferences) {
		for(int i=0; i<fieldReferences.length && i<exclusions.length; i++) {
			if(exclusions[i].contains(fieldReferences[i]))
				return true;
		}			
		return false;
	}
	
	public AccessPath<FieldRef> addFieldReference(FieldRef... fieldReferences) {
		if(isAccessInExclusions(fieldReferences))
			throw new IllegalArgumentException();

		FieldRef[] newAccesses = Arrays.copyOf(accesses, accesses.length+fieldReferences.length);
		System.arraycopy(fieldReferences, 0, newAccesses, accesses.length, fieldReferences.length);
		Set<FieldRef>[] newExclusionsArray = exclusions.length < fieldReferences.length ? exclusions : Arrays.copyOfRange(exclusions, fieldReferences.length, exclusions.length);			
		return new AccessPath<FieldRef>(newAccesses, newExclusionsArray);
	}

	public ExclusionSet getExclusions(int index) {
		return new ExclusionSet(index);
	}
	








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




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



	public AccessPath<FieldRef> append(AccessPath<FieldRef> accessPath) {
		if(exclusions.length > 0) 
			throw new IllegalStateException();
		
		FieldRef[] newAccesses = Arrays.copyOf(accesses, accesses.length + accessPath.accesses.length);
		System.arraycopy(accessPath.accesses, 0, newAccesses, accesses.length, accessPath.accesses.length);
		return new AccessPath<FieldRef>(newAccesses, accessPath.exclusions);
	}

	public AccessPath<FieldRef> removeFirstAccessIfAvailable() {
		if(accesses.length > 0)
			return new AccessPath<FieldRef>(Arrays.copyOfRange(accesses, 1, accesses.length), exclusions);
		else if(exclusions.length > 0)
			return new AccessPath<FieldRef>(accesses, Arrays.copyOfRange(exclusions, 1, exclusions.length));
		else
			return this;
	}

	public AccessPath<FieldRef> mergeExcludedFieldReference(FieldRef... fieldRef) {
		if(hasExclusions())
			return getExclusions(0).addExclusion(fieldRef);
		else
			return appendExcludedFieldReference(fieldRef);
	}
	








cleaning code



Johannes Lerch
committed
Jan 07, 2015




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


114


115


116


117


118


119


120


121


122


123


124


125


126


127


128


129


130


131


132


133


134


135


136


137


138


139


140


141


142


143


144


145


146


147


148


149


150


151


152


153


154


155


156


157


158


159


160


161


162


163


164


165


166


167


168


169


170


171


172


173


174


175


176


177


178


179


180


181


182



	public AccessPath<FieldRef> appendExcludedFieldReference(FieldRef... fieldReferences) {
		Set<FieldRef>[] newExclusionsArray = Arrays.copyOf(exclusions, exclusions.length+1);
		newExclusionsArray[exclusions.length] = Sets.newHashSet(fieldReferences);
		return new AccessPath<>(accesses, newExclusionsArray);
	}
	
	public boolean isPrefixOf(AccessPath<FieldRef> accessPath) {
		if(accesses.length > accessPath.accesses.length)
			return false;
		
		if(accesses.length + exclusions.length > accessPath.accesses.length + accessPath.exclusions.length)
			return false;
		
		for(int i=0; i<accesses.length; i++) {
			if(!accesses[i].equals(accessPath.accesses[i]))
				return false;
		}
		
		for(int i=0; i<exclusions.length; i++) {
			if(i+accesses.length < accessPath.accesses.length) {
				if(exclusions[i].contains(accessPath.accesses[i+accesses.length]))
					return false;
			}
			else {
				if(!exclusions[i].containsAll(accessPath.exclusions[i+accesses.length - accessPath.accesses.length]))
					return false;
			}
		}
		
		return true;
	}
	
	public FieldRef[] getDeltaTo(AccessPath<FieldRef> accPath) {
		if(isPrefixOf(accPath))
			return Arrays.copyOfRange(accPath.accesses, accesses.length, accPath.accesses.length);
		else
			throw new IllegalArgumentException("Given AccessPath must be a prefix of the current AccessPath");
	}
	
	public AccessPath<FieldRef> mergeExcludedFieldReferences(AccessPath<FieldRef> accPath) {
		Set<FieldRef>[] newExclusionArray = new Set[Math.max(exclusions.length,accPath.exclusions.length)];
		for(int i=0; i<newExclusionArray.length; i++) {
			newExclusionArray[i] = Sets.newHashSet();
			if(i<exclusions.length)
				newExclusionArray[i].addAll(exclusions[i]);
			if(i<accPath.exclusions.length)
				newExclusionArray[i].addAll(accPath.exclusions[i]);
		}
		return new AccessPath<>(accesses, newExclusionArray);
	}
	
	public boolean isEmpty() {
		return exclusions.length == 0 && accesses.length == 0;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(accesses);
		result = prime * result + Arrays.hashCode(exclusions);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AccessPath))
			return false;
		AccessPath other = (AccessPath) obj;
		if (!Arrays.equals(accesses, other.accesses))
			return false;
		if (!Arrays.equals(exclusions, other.exclusions))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String result = accesses.length > 0 ? "."+Joiner.on(".").join(accesses) : "";
		for(Set<FieldRef> exclusion : exclusions) {
			result += "^" + Joiner.on(",").join(exclusion);
		}
		return result;
	}
	
	public class ExclusionSet {
		private int index;
	
		private ExclusionSet(int index) {
			this.index = index;
		}
		








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




183



		public AccessPath<FieldRef> addExclusion(FieldRef... exclusion) {








cleaning code



Johannes Lerch
committed
Jan 07, 2015




184



			HashSet<FieldRef> newExclusions = Sets.newHashSet(exclusions[index]);








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




185


186



			for(FieldRef excl : exclusion)
				newExclusions.add(excl);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




187


188


189


190


191


192



			Set<FieldRef>[] newExclusionsArray = exclusions.clone();
			newExclusionsArray[index] = newExclusions;
			return new AccessPath<FieldRef>(accesses, newExclusionsArray);
		}
	}
}











b131fd75dc553a141444d3f8f99405593c8d3614


Switch branch/tag










heros


src


heros


alias


AccessPath.java



Find file
Normal viewHistoryPermalink




b131fd75dc553a141444d3f8f99405593c8d3614


Switch branch/tag










heros


src


heros


alias


AccessPath.java





b131fd75dc553a141444d3f8f99405593c8d3614


Switch branch/tag








b131fd75dc553a141444d3f8f99405593c8d3614


Switch branch/tag





b131fd75dc553a141444d3f8f99405593c8d3614

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src

heros

alias

AccessPath.java
Find file
Normal viewHistoryPermalink




AccessPath.java



6.17 KB









Newer










Older









cleaning code



Johannes Lerch
committed
Jan 07, 2015




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
package heros.alias;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

@SuppressWarnings("unchecked")
public class AccessPath<FieldRef> {









Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




23


24



	private final FieldRef[] accesses;
	private final Set<FieldRef>[] exclusions;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




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



	
	public AccessPath() {
		accesses = (FieldRef[]) new Object[0];
		exclusions = new Set[0];
	}
	
	AccessPath(FieldRef[] accesses, Set<FieldRef>[] exclusions) {
		this.accesses = accesses;
		this.exclusions = exclusions;
	}

	public boolean hasExclusions() {
		return exclusions.length > 0;
	}
	
	public boolean isAccessInExclusions(FieldRef... fieldReferences) {
		for(int i=0; i<fieldReferences.length && i<exclusions.length; i++) {
			if(exclusions[i].contains(fieldReferences[i]))
				return true;
		}			
		return false;
	}
	
	public AccessPath<FieldRef> addFieldReference(FieldRef... fieldReferences) {
		if(isAccessInExclusions(fieldReferences))
			throw new IllegalArgumentException();

		FieldRef[] newAccesses = Arrays.copyOf(accesses, accesses.length+fieldReferences.length);
		System.arraycopy(fieldReferences, 0, newAccesses, accesses.length, fieldReferences.length);
		Set<FieldRef>[] newExclusionsArray = exclusions.length < fieldReferences.length ? exclusions : Arrays.copyOfRange(exclusions, fieldReferences.length, exclusions.length);			
		return new AccessPath<FieldRef>(newAccesses, newExclusionsArray);
	}

	public ExclusionSet getExclusions(int index) {
		return new ExclusionSet(index);
	}
	








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




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



	public AccessPath<FieldRef> append(AccessPath<FieldRef> accessPath) {
		if(exclusions.length > 0) 
			throw new IllegalStateException();
		
		FieldRef[] newAccesses = Arrays.copyOf(accesses, accesses.length + accessPath.accesses.length);
		System.arraycopy(accessPath.accesses, 0, newAccesses, accesses.length, accessPath.accesses.length);
		return new AccessPath<FieldRef>(newAccesses, accessPath.exclusions);
	}

	public AccessPath<FieldRef> removeFirstAccessIfAvailable() {
		if(accesses.length > 0)
			return new AccessPath<FieldRef>(Arrays.copyOfRange(accesses, 1, accesses.length), exclusions);
		else if(exclusions.length > 0)
			return new AccessPath<FieldRef>(accesses, Arrays.copyOfRange(exclusions, 1, exclusions.length));
		else
			return this;
	}

	public AccessPath<FieldRef> mergeExcludedFieldReference(FieldRef... fieldRef) {
		if(hasExclusions())
			return getExclusions(0).addExclusion(fieldRef);
		else
			return appendExcludedFieldReference(fieldRef);
	}
	








cleaning code



Johannes Lerch
committed
Jan 07, 2015




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


114


115


116


117


118


119


120


121


122


123


124


125


126


127


128


129


130


131


132


133


134


135


136


137


138


139


140


141


142


143


144


145


146


147


148


149


150


151


152


153


154


155


156


157


158


159


160


161


162


163


164


165


166


167


168


169


170


171


172


173


174


175


176


177


178


179


180


181


182



	public AccessPath<FieldRef> appendExcludedFieldReference(FieldRef... fieldReferences) {
		Set<FieldRef>[] newExclusionsArray = Arrays.copyOf(exclusions, exclusions.length+1);
		newExclusionsArray[exclusions.length] = Sets.newHashSet(fieldReferences);
		return new AccessPath<>(accesses, newExclusionsArray);
	}
	
	public boolean isPrefixOf(AccessPath<FieldRef> accessPath) {
		if(accesses.length > accessPath.accesses.length)
			return false;
		
		if(accesses.length + exclusions.length > accessPath.accesses.length + accessPath.exclusions.length)
			return false;
		
		for(int i=0; i<accesses.length; i++) {
			if(!accesses[i].equals(accessPath.accesses[i]))
				return false;
		}
		
		for(int i=0; i<exclusions.length; i++) {
			if(i+accesses.length < accessPath.accesses.length) {
				if(exclusions[i].contains(accessPath.accesses[i+accesses.length]))
					return false;
			}
			else {
				if(!exclusions[i].containsAll(accessPath.exclusions[i+accesses.length - accessPath.accesses.length]))
					return false;
			}
		}
		
		return true;
	}
	
	public FieldRef[] getDeltaTo(AccessPath<FieldRef> accPath) {
		if(isPrefixOf(accPath))
			return Arrays.copyOfRange(accPath.accesses, accesses.length, accPath.accesses.length);
		else
			throw new IllegalArgumentException("Given AccessPath must be a prefix of the current AccessPath");
	}
	
	public AccessPath<FieldRef> mergeExcludedFieldReferences(AccessPath<FieldRef> accPath) {
		Set<FieldRef>[] newExclusionArray = new Set[Math.max(exclusions.length,accPath.exclusions.length)];
		for(int i=0; i<newExclusionArray.length; i++) {
			newExclusionArray[i] = Sets.newHashSet();
			if(i<exclusions.length)
				newExclusionArray[i].addAll(exclusions[i]);
			if(i<accPath.exclusions.length)
				newExclusionArray[i].addAll(accPath.exclusions[i]);
		}
		return new AccessPath<>(accesses, newExclusionArray);
	}
	
	public boolean isEmpty() {
		return exclusions.length == 0 && accesses.length == 0;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(accesses);
		result = prime * result + Arrays.hashCode(exclusions);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AccessPath))
			return false;
		AccessPath other = (AccessPath) obj;
		if (!Arrays.equals(accesses, other.accesses))
			return false;
		if (!Arrays.equals(exclusions, other.exclusions))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String result = accesses.length > 0 ? "."+Joiner.on(".").join(accesses) : "";
		for(Set<FieldRef> exclusion : exclusions) {
			result += "^" + Joiner.on(",").join(exclusion);
		}
		return result;
	}
	
	public class ExclusionSet {
		private int index;
	
		private ExclusionSet(int index) {
			this.index = index;
		}
		








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




183



		public AccessPath<FieldRef> addExclusion(FieldRef... exclusion) {








cleaning code



Johannes Lerch
committed
Jan 07, 2015




184



			HashSet<FieldRef> newExclusions = Sets.newHashSet(exclusions[index]);








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




185


186



			for(FieldRef excl : exclusion)
				newExclusions.add(excl);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




187


188


189


190


191


192



			Set<FieldRef>[] newExclusionsArray = exclusions.clone();
			newExclusionsArray[index] = newExclusions;
			return new AccessPath<FieldRef>(accesses, newExclusionsArray);
		}
	}
}









AccessPath.java



6.17 KB










AccessPath.java



6.17 KB









Newer










Older
NewerOlder







cleaning code



Johannes Lerch
committed
Jan 07, 2015




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
package heros.alias;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

@SuppressWarnings("unchecked")
public class AccessPath<FieldRef> {









Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




23


24



	private final FieldRef[] accesses;
	private final Set<FieldRef>[] exclusions;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




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



	
	public AccessPath() {
		accesses = (FieldRef[]) new Object[0];
		exclusions = new Set[0];
	}
	
	AccessPath(FieldRef[] accesses, Set<FieldRef>[] exclusions) {
		this.accesses = accesses;
		this.exclusions = exclusions;
	}

	public boolean hasExclusions() {
		return exclusions.length > 0;
	}
	
	public boolean isAccessInExclusions(FieldRef... fieldReferences) {
		for(int i=0; i<fieldReferences.length && i<exclusions.length; i++) {
			if(exclusions[i].contains(fieldReferences[i]))
				return true;
		}			
		return false;
	}
	
	public AccessPath<FieldRef> addFieldReference(FieldRef... fieldReferences) {
		if(isAccessInExclusions(fieldReferences))
			throw new IllegalArgumentException();

		FieldRef[] newAccesses = Arrays.copyOf(accesses, accesses.length+fieldReferences.length);
		System.arraycopy(fieldReferences, 0, newAccesses, accesses.length, fieldReferences.length);
		Set<FieldRef>[] newExclusionsArray = exclusions.length < fieldReferences.length ? exclusions : Arrays.copyOfRange(exclusions, fieldReferences.length, exclusions.length);			
		return new AccessPath<FieldRef>(newAccesses, newExclusionsArray);
	}

	public ExclusionSet getExclusions(int index) {
		return new ExclusionSet(index);
	}
	








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




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



	public AccessPath<FieldRef> append(AccessPath<FieldRef> accessPath) {
		if(exclusions.length > 0) 
			throw new IllegalStateException();
		
		FieldRef[] newAccesses = Arrays.copyOf(accesses, accesses.length + accessPath.accesses.length);
		System.arraycopy(accessPath.accesses, 0, newAccesses, accesses.length, accessPath.accesses.length);
		return new AccessPath<FieldRef>(newAccesses, accessPath.exclusions);
	}

	public AccessPath<FieldRef> removeFirstAccessIfAvailable() {
		if(accesses.length > 0)
			return new AccessPath<FieldRef>(Arrays.copyOfRange(accesses, 1, accesses.length), exclusions);
		else if(exclusions.length > 0)
			return new AccessPath<FieldRef>(accesses, Arrays.copyOfRange(exclusions, 1, exclusions.length));
		else
			return this;
	}

	public AccessPath<FieldRef> mergeExcludedFieldReference(FieldRef... fieldRef) {
		if(hasExclusions())
			return getExclusions(0).addExclusion(fieldRef);
		else
			return appendExcludedFieldReference(fieldRef);
	}
	








cleaning code



Johannes Lerch
committed
Jan 07, 2015




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


114


115


116


117


118


119


120


121


122


123


124


125


126


127


128


129


130


131


132


133


134


135


136


137


138


139


140


141


142


143


144


145


146


147


148


149


150


151


152


153


154


155


156


157


158


159


160


161


162


163


164


165


166


167


168


169


170


171


172


173


174


175


176


177


178


179


180


181


182



	public AccessPath<FieldRef> appendExcludedFieldReference(FieldRef... fieldReferences) {
		Set<FieldRef>[] newExclusionsArray = Arrays.copyOf(exclusions, exclusions.length+1);
		newExclusionsArray[exclusions.length] = Sets.newHashSet(fieldReferences);
		return new AccessPath<>(accesses, newExclusionsArray);
	}
	
	public boolean isPrefixOf(AccessPath<FieldRef> accessPath) {
		if(accesses.length > accessPath.accesses.length)
			return false;
		
		if(accesses.length + exclusions.length > accessPath.accesses.length + accessPath.exclusions.length)
			return false;
		
		for(int i=0; i<accesses.length; i++) {
			if(!accesses[i].equals(accessPath.accesses[i]))
				return false;
		}
		
		for(int i=0; i<exclusions.length; i++) {
			if(i+accesses.length < accessPath.accesses.length) {
				if(exclusions[i].contains(accessPath.accesses[i+accesses.length]))
					return false;
			}
			else {
				if(!exclusions[i].containsAll(accessPath.exclusions[i+accesses.length - accessPath.accesses.length]))
					return false;
			}
		}
		
		return true;
	}
	
	public FieldRef[] getDeltaTo(AccessPath<FieldRef> accPath) {
		if(isPrefixOf(accPath))
			return Arrays.copyOfRange(accPath.accesses, accesses.length, accPath.accesses.length);
		else
			throw new IllegalArgumentException("Given AccessPath must be a prefix of the current AccessPath");
	}
	
	public AccessPath<FieldRef> mergeExcludedFieldReferences(AccessPath<FieldRef> accPath) {
		Set<FieldRef>[] newExclusionArray = new Set[Math.max(exclusions.length,accPath.exclusions.length)];
		for(int i=0; i<newExclusionArray.length; i++) {
			newExclusionArray[i] = Sets.newHashSet();
			if(i<exclusions.length)
				newExclusionArray[i].addAll(exclusions[i]);
			if(i<accPath.exclusions.length)
				newExclusionArray[i].addAll(accPath.exclusions[i]);
		}
		return new AccessPath<>(accesses, newExclusionArray);
	}
	
	public boolean isEmpty() {
		return exclusions.length == 0 && accesses.length == 0;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(accesses);
		result = prime * result + Arrays.hashCode(exclusions);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AccessPath))
			return false;
		AccessPath other = (AccessPath) obj;
		if (!Arrays.equals(accesses, other.accesses))
			return false;
		if (!Arrays.equals(exclusions, other.exclusions))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String result = accesses.length > 0 ? "."+Joiner.on(".").join(accesses) : "";
		for(Set<FieldRef> exclusion : exclusions) {
			result += "^" + Joiner.on(",").join(exclusion);
		}
		return result;
	}
	
	public class ExclusionSet {
		private int index;
	
		private ExclusionSet(int index) {
			this.index = index;
		}
		








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




183



		public AccessPath<FieldRef> addExclusion(FieldRef... exclusion) {








cleaning code



Johannes Lerch
committed
Jan 07, 2015




184



			HashSet<FieldRef> newExclusions = Sets.newHashSet(exclusions[index]);








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




185


186



			for(FieldRef excl : exclusion)
				newExclusions.add(excl);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




187


188


189


190


191


192



			Set<FieldRef>[] newExclusionsArray = exclusions.clone();
			newExclusionsArray[index] = newExclusions;
			return new AccessPath<FieldRef>(accesses, newExclusionsArray);
		}
	}
}











cleaning code



Johannes Lerch
committed
Jan 07, 2015




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
package heros.alias;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

@SuppressWarnings("unchecked")
public class AccessPath<FieldRef> {









Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




23


24



	private final FieldRef[] accesses;
	private final Set<FieldRef>[] exclusions;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




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



	
	public AccessPath() {
		accesses = (FieldRef[]) new Object[0];
		exclusions = new Set[0];
	}
	
	AccessPath(FieldRef[] accesses, Set<FieldRef>[] exclusions) {
		this.accesses = accesses;
		this.exclusions = exclusions;
	}

	public boolean hasExclusions() {
		return exclusions.length > 0;
	}
	
	public boolean isAccessInExclusions(FieldRef... fieldReferences) {
		for(int i=0; i<fieldReferences.length && i<exclusions.length; i++) {
			if(exclusions[i].contains(fieldReferences[i]))
				return true;
		}			
		return false;
	}
	
	public AccessPath<FieldRef> addFieldReference(FieldRef... fieldReferences) {
		if(isAccessInExclusions(fieldReferences))
			throw new IllegalArgumentException();

		FieldRef[] newAccesses = Arrays.copyOf(accesses, accesses.length+fieldReferences.length);
		System.arraycopy(fieldReferences, 0, newAccesses, accesses.length, fieldReferences.length);
		Set<FieldRef>[] newExclusionsArray = exclusions.length < fieldReferences.length ? exclusions : Arrays.copyOfRange(exclusions, fieldReferences.length, exclusions.length);			
		return new AccessPath<FieldRef>(newAccesses, newExclusionsArray);
	}

	public ExclusionSet getExclusions(int index) {
		return new ExclusionSet(index);
	}
	








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




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



	public AccessPath<FieldRef> append(AccessPath<FieldRef> accessPath) {
		if(exclusions.length > 0) 
			throw new IllegalStateException();
		
		FieldRef[] newAccesses = Arrays.copyOf(accesses, accesses.length + accessPath.accesses.length);
		System.arraycopy(accessPath.accesses, 0, newAccesses, accesses.length, accessPath.accesses.length);
		return new AccessPath<FieldRef>(newAccesses, accessPath.exclusions);
	}

	public AccessPath<FieldRef> removeFirstAccessIfAvailable() {
		if(accesses.length > 0)
			return new AccessPath<FieldRef>(Arrays.copyOfRange(accesses, 1, accesses.length), exclusions);
		else if(exclusions.length > 0)
			return new AccessPath<FieldRef>(accesses, Arrays.copyOfRange(exclusions, 1, exclusions.length));
		else
			return this;
	}

	public AccessPath<FieldRef> mergeExcludedFieldReference(FieldRef... fieldRef) {
		if(hasExclusions())
			return getExclusions(0).addExclusion(fieldRef);
		else
			return appendExcludedFieldReference(fieldRef);
	}
	








cleaning code



Johannes Lerch
committed
Jan 07, 2015




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


114


115


116


117


118


119


120


121


122


123


124


125


126


127


128


129


130


131


132


133


134


135


136


137


138


139


140


141


142


143


144


145


146


147


148


149


150


151


152


153


154


155


156


157


158


159


160


161


162


163


164


165


166


167


168


169


170


171


172


173


174


175


176


177


178


179


180


181


182



	public AccessPath<FieldRef> appendExcludedFieldReference(FieldRef... fieldReferences) {
		Set<FieldRef>[] newExclusionsArray = Arrays.copyOf(exclusions, exclusions.length+1);
		newExclusionsArray[exclusions.length] = Sets.newHashSet(fieldReferences);
		return new AccessPath<>(accesses, newExclusionsArray);
	}
	
	public boolean isPrefixOf(AccessPath<FieldRef> accessPath) {
		if(accesses.length > accessPath.accesses.length)
			return false;
		
		if(accesses.length + exclusions.length > accessPath.accesses.length + accessPath.exclusions.length)
			return false;
		
		for(int i=0; i<accesses.length; i++) {
			if(!accesses[i].equals(accessPath.accesses[i]))
				return false;
		}
		
		for(int i=0; i<exclusions.length; i++) {
			if(i+accesses.length < accessPath.accesses.length) {
				if(exclusions[i].contains(accessPath.accesses[i+accesses.length]))
					return false;
			}
			else {
				if(!exclusions[i].containsAll(accessPath.exclusions[i+accesses.length - accessPath.accesses.length]))
					return false;
			}
		}
		
		return true;
	}
	
	public FieldRef[] getDeltaTo(AccessPath<FieldRef> accPath) {
		if(isPrefixOf(accPath))
			return Arrays.copyOfRange(accPath.accesses, accesses.length, accPath.accesses.length);
		else
			throw new IllegalArgumentException("Given AccessPath must be a prefix of the current AccessPath");
	}
	
	public AccessPath<FieldRef> mergeExcludedFieldReferences(AccessPath<FieldRef> accPath) {
		Set<FieldRef>[] newExclusionArray = new Set[Math.max(exclusions.length,accPath.exclusions.length)];
		for(int i=0; i<newExclusionArray.length; i++) {
			newExclusionArray[i] = Sets.newHashSet();
			if(i<exclusions.length)
				newExclusionArray[i].addAll(exclusions[i]);
			if(i<accPath.exclusions.length)
				newExclusionArray[i].addAll(accPath.exclusions[i]);
		}
		return new AccessPath<>(accesses, newExclusionArray);
	}
	
	public boolean isEmpty() {
		return exclusions.length == 0 && accesses.length == 0;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(accesses);
		result = prime * result + Arrays.hashCode(exclusions);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AccessPath))
			return false;
		AccessPath other = (AccessPath) obj;
		if (!Arrays.equals(accesses, other.accesses))
			return false;
		if (!Arrays.equals(exclusions, other.exclusions))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String result = accesses.length > 0 ? "."+Joiner.on(".").join(accesses) : "";
		for(Set<FieldRef> exclusion : exclusions) {
			result += "^" + Joiner.on(",").join(exclusion);
		}
		return result;
	}
	
	public class ExclusionSet {
		private int index;
	
		private ExclusionSet(int index) {
			this.index = index;
		}
		








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




183



		public AccessPath<FieldRef> addExclusion(FieldRef... exclusion) {








cleaning code



Johannes Lerch
committed
Jan 07, 2015




184



			HashSet<FieldRef> newExclusions = Sets.newHashSet(exclusions[index]);








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




185


186



			for(FieldRef excl : exclusion)
				newExclusions.add(excl);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




187


188


189


190


191


192



			Set<FieldRef>[] newExclusionsArray = exclusions.clone();
			newExclusionsArray[index] = newExclusions;
			return new AccessPath<FieldRef>(accesses, newExclusionsArray);
		}
	}
}









cleaning code



Johannes Lerch
committed
Jan 07, 2015




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
package heros.alias;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

@SuppressWarnings("unchecked")
public class AccessPath<FieldRef> {









Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




23


24



	private final FieldRef[] accesses;
	private final Set<FieldRef>[] exclusions;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




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



	
	public AccessPath() {
		accesses = (FieldRef[]) new Object[0];
		exclusions = new Set[0];
	}
	
	AccessPath(FieldRef[] accesses, Set<FieldRef>[] exclusions) {
		this.accesses = accesses;
		this.exclusions = exclusions;
	}

	public boolean hasExclusions() {
		return exclusions.length > 0;
	}
	
	public boolean isAccessInExclusions(FieldRef... fieldReferences) {
		for(int i=0; i<fieldReferences.length && i<exclusions.length; i++) {
			if(exclusions[i].contains(fieldReferences[i]))
				return true;
		}			
		return false;
	}
	
	public AccessPath<FieldRef> addFieldReference(FieldRef... fieldReferences) {
		if(isAccessInExclusions(fieldReferences))
			throw new IllegalArgumentException();

		FieldRef[] newAccesses = Arrays.copyOf(accesses, accesses.length+fieldReferences.length);
		System.arraycopy(fieldReferences, 0, newAccesses, accesses.length, fieldReferences.length);
		Set<FieldRef>[] newExclusionsArray = exclusions.length < fieldReferences.length ? exclusions : Arrays.copyOfRange(exclusions, fieldReferences.length, exclusions.length);			
		return new AccessPath<FieldRef>(newAccesses, newExclusionsArray);
	}

	public ExclusionSet getExclusions(int index) {
		return new ExclusionSet(index);
	}
	








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




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



	public AccessPath<FieldRef> append(AccessPath<FieldRef> accessPath) {
		if(exclusions.length > 0) 
			throw new IllegalStateException();
		
		FieldRef[] newAccesses = Arrays.copyOf(accesses, accesses.length + accessPath.accesses.length);
		System.arraycopy(accessPath.accesses, 0, newAccesses, accesses.length, accessPath.accesses.length);
		return new AccessPath<FieldRef>(newAccesses, accessPath.exclusions);
	}

	public AccessPath<FieldRef> removeFirstAccessIfAvailable() {
		if(accesses.length > 0)
			return new AccessPath<FieldRef>(Arrays.copyOfRange(accesses, 1, accesses.length), exclusions);
		else if(exclusions.length > 0)
			return new AccessPath<FieldRef>(accesses, Arrays.copyOfRange(exclusions, 1, exclusions.length));
		else
			return this;
	}

	public AccessPath<FieldRef> mergeExcludedFieldReference(FieldRef... fieldRef) {
		if(hasExclusions())
			return getExclusions(0).addExclusion(fieldRef);
		else
			return appendExcludedFieldReference(fieldRef);
	}
	








cleaning code



Johannes Lerch
committed
Jan 07, 2015




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


114


115


116


117


118


119


120


121


122


123


124


125


126


127


128


129


130


131


132


133


134


135


136


137


138


139


140


141


142


143


144


145


146


147


148


149


150


151


152


153


154


155


156


157


158


159


160


161


162


163


164


165


166


167


168


169


170


171


172


173


174


175


176


177


178


179


180


181


182



	public AccessPath<FieldRef> appendExcludedFieldReference(FieldRef... fieldReferences) {
		Set<FieldRef>[] newExclusionsArray = Arrays.copyOf(exclusions, exclusions.length+1);
		newExclusionsArray[exclusions.length] = Sets.newHashSet(fieldReferences);
		return new AccessPath<>(accesses, newExclusionsArray);
	}
	
	public boolean isPrefixOf(AccessPath<FieldRef> accessPath) {
		if(accesses.length > accessPath.accesses.length)
			return false;
		
		if(accesses.length + exclusions.length > accessPath.accesses.length + accessPath.exclusions.length)
			return false;
		
		for(int i=0; i<accesses.length; i++) {
			if(!accesses[i].equals(accessPath.accesses[i]))
				return false;
		}
		
		for(int i=0; i<exclusions.length; i++) {
			if(i+accesses.length < accessPath.accesses.length) {
				if(exclusions[i].contains(accessPath.accesses[i+accesses.length]))
					return false;
			}
			else {
				if(!exclusions[i].containsAll(accessPath.exclusions[i+accesses.length - accessPath.accesses.length]))
					return false;
			}
		}
		
		return true;
	}
	
	public FieldRef[] getDeltaTo(AccessPath<FieldRef> accPath) {
		if(isPrefixOf(accPath))
			return Arrays.copyOfRange(accPath.accesses, accesses.length, accPath.accesses.length);
		else
			throw new IllegalArgumentException("Given AccessPath must be a prefix of the current AccessPath");
	}
	
	public AccessPath<FieldRef> mergeExcludedFieldReferences(AccessPath<FieldRef> accPath) {
		Set<FieldRef>[] newExclusionArray = new Set[Math.max(exclusions.length,accPath.exclusions.length)];
		for(int i=0; i<newExclusionArray.length; i++) {
			newExclusionArray[i] = Sets.newHashSet();
			if(i<exclusions.length)
				newExclusionArray[i].addAll(exclusions[i]);
			if(i<accPath.exclusions.length)
				newExclusionArray[i].addAll(accPath.exclusions[i]);
		}
		return new AccessPath<>(accesses, newExclusionArray);
	}
	
	public boolean isEmpty() {
		return exclusions.length == 0 && accesses.length == 0;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(accesses);
		result = prime * result + Arrays.hashCode(exclusions);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AccessPath))
			return false;
		AccessPath other = (AccessPath) obj;
		if (!Arrays.equals(accesses, other.accesses))
			return false;
		if (!Arrays.equals(exclusions, other.exclusions))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String result = accesses.length > 0 ? "."+Joiner.on(".").join(accesses) : "";
		for(Set<FieldRef> exclusion : exclusions) {
			result += "^" + Joiner.on(",").join(exclusion);
		}
		return result;
	}
	
	public class ExclusionSet {
		private int index;
	
		private ExclusionSet(int index) {
			this.index = index;
		}
		








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




183



		public AccessPath<FieldRef> addExclusion(FieldRef... exclusion) {








cleaning code



Johannes Lerch
committed
Jan 07, 2015




184



			HashSet<FieldRef> newExclusions = Sets.newHashSet(exclusions[index]);








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




185


186



			for(FieldRef excl : exclusion)
				newExclusions.add(excl);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




187


188


189


190


191


192



			Set<FieldRef>[] newExclusionsArray = exclusions.clone();
			newExclusionsArray[index] = newExclusions;
			return new AccessPath<FieldRef>(accesses, newExclusionsArray);
		}
	}
}







cleaning code



Johannes Lerch
committed
Jan 07, 2015




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
package heros.alias;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

@SuppressWarnings("unchecked")
public class AccessPath<FieldRef> {







cleaning code



Johannes Lerch
committed
Jan 07, 2015



cleaning code


cleaning code

Johannes Lerch
committed
Jan 07, 2015


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
package heros.alias;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

@SuppressWarnings("unchecked")
public class AccessPath<FieldRef> {


/*******************************************************************************/******************************************************************************* * Copyright (c) 2015 Johannes Lerch. * Copyright (c) 2015 Johannes Lerch. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Johannes Lerch - initial API and implementation *     Johannes Lerch - initial API and implementation ******************************************************************************/ ******************************************************************************/package heros.alias;packageheros.alias;import java.util.Arrays;importjava.util.Arrays;import java.util.HashSet;importjava.util.HashSet;import java.util.Set;importjava.util.Set;import com.google.common.base.Joiner;importcom.google.common.base.Joiner;import com.google.common.collect.Sets;importcom.google.common.collect.Sets;@SuppressWarnings("unchecked")@SuppressWarnings("unchecked")public class AccessPath<FieldRef> {publicclassAccessPath<FieldRef>{




Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




23


24



	private final FieldRef[] accesses;
	private final Set<FieldRef>[] exclusions;






Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015



Bugfix, Helper functions, and renaming

 

Bugfix, Helper functions, and renaming

Johannes Lerch
committed
Jan 08, 2015


23


24


	private final FieldRef[] accesses;
	private final Set<FieldRef>[] exclusions;

	private final FieldRef[] accesses;privatefinalFieldRef[]accesses;	private final Set<FieldRef>[] exclusions;privatefinalSet<FieldRef>[]exclusions;




cleaning code



Johannes Lerch
committed
Jan 07, 2015




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



	
	public AccessPath() {
		accesses = (FieldRef[]) new Object[0];
		exclusions = new Set[0];
	}
	
	AccessPath(FieldRef[] accesses, Set<FieldRef>[] exclusions) {
		this.accesses = accesses;
		this.exclusions = exclusions;
	}

	public boolean hasExclusions() {
		return exclusions.length > 0;
	}
	
	public boolean isAccessInExclusions(FieldRef... fieldReferences) {
		for(int i=0; i<fieldReferences.length && i<exclusions.length; i++) {
			if(exclusions[i].contains(fieldReferences[i]))
				return true;
		}			
		return false;
	}
	
	public AccessPath<FieldRef> addFieldReference(FieldRef... fieldReferences) {
		if(isAccessInExclusions(fieldReferences))
			throw new IllegalArgumentException();

		FieldRef[] newAccesses = Arrays.copyOf(accesses, accesses.length+fieldReferences.length);
		System.arraycopy(fieldReferences, 0, newAccesses, accesses.length, fieldReferences.length);
		Set<FieldRef>[] newExclusionsArray = exclusions.length < fieldReferences.length ? exclusions : Arrays.copyOfRange(exclusions, fieldReferences.length, exclusions.length);			
		return new AccessPath<FieldRef>(newAccesses, newExclusionsArray);
	}

	public ExclusionSet getExclusions(int index) {
		return new ExclusionSet(index);
	}
	






cleaning code



Johannes Lerch
committed
Jan 07, 2015



cleaning code


cleaning code

Johannes Lerch
committed
Jan 07, 2015


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


	
	public AccessPath() {
		accesses = (FieldRef[]) new Object[0];
		exclusions = new Set[0];
	}
	
	AccessPath(FieldRef[] accesses, Set<FieldRef>[] exclusions) {
		this.accesses = accesses;
		this.exclusions = exclusions;
	}

	public boolean hasExclusions() {
		return exclusions.length > 0;
	}
	
	public boolean isAccessInExclusions(FieldRef... fieldReferences) {
		for(int i=0; i<fieldReferences.length && i<exclusions.length; i++) {
			if(exclusions[i].contains(fieldReferences[i]))
				return true;
		}			
		return false;
	}
	
	public AccessPath<FieldRef> addFieldReference(FieldRef... fieldReferences) {
		if(isAccessInExclusions(fieldReferences))
			throw new IllegalArgumentException();

		FieldRef[] newAccesses = Arrays.copyOf(accesses, accesses.length+fieldReferences.length);
		System.arraycopy(fieldReferences, 0, newAccesses, accesses.length, fieldReferences.length);
		Set<FieldRef>[] newExclusionsArray = exclusions.length < fieldReferences.length ? exclusions : Arrays.copyOfRange(exclusions, fieldReferences.length, exclusions.length);			
		return new AccessPath<FieldRef>(newAccesses, newExclusionsArray);
	}

	public ExclusionSet getExclusions(int index) {
		return new ExclusionSet(index);
	}
	

		public AccessPath() {publicAccessPath(){		accesses = (FieldRef[]) new Object[0];accesses=(FieldRef[])newObject[0];		exclusions = new Set[0];exclusions=newSet[0];	}}		AccessPath(FieldRef[] accesses, Set<FieldRef>[] exclusions) {AccessPath(FieldRef[]accesses,Set<FieldRef>[]exclusions){		this.accesses = accesses;this.accesses=accesses;		this.exclusions = exclusions;this.exclusions=exclusions;	}}	public boolean hasExclusions() {publicbooleanhasExclusions(){		return exclusions.length > 0;returnexclusions.length>0;	}}		public boolean isAccessInExclusions(FieldRef... fieldReferences) {publicbooleanisAccessInExclusions(FieldRef...fieldReferences){		for(int i=0; i<fieldReferences.length && i<exclusions.length; i++) {for(inti=0;i<fieldReferences.length&&i<exclusions.length;i++){			if(exclusions[i].contains(fieldReferences[i]))if(exclusions[i].contains(fieldReferences[i]))				return true;returntrue;		}			}		return false;returnfalse;	}}		public AccessPath<FieldRef> addFieldReference(FieldRef... fieldReferences) {publicAccessPath<FieldRef>addFieldReference(FieldRef...fieldReferences){		if(isAccessInExclusions(fieldReferences))if(isAccessInExclusions(fieldReferences))			throw new IllegalArgumentException();thrownewIllegalArgumentException();		FieldRef[] newAccesses = Arrays.copyOf(accesses, accesses.length+fieldReferences.length);FieldRef[]newAccesses=Arrays.copyOf(accesses,accesses.length+fieldReferences.length);		System.arraycopy(fieldReferences, 0, newAccesses, accesses.length, fieldReferences.length);System.arraycopy(fieldReferences,0,newAccesses,accesses.length,fieldReferences.length);		Set<FieldRef>[] newExclusionsArray = exclusions.length < fieldReferences.length ? exclusions : Arrays.copyOfRange(exclusions, fieldReferences.length, exclusions.length);			Set<FieldRef>[]newExclusionsArray=exclusions.length<fieldReferences.length?exclusions:Arrays.copyOfRange(exclusions,fieldReferences.length,exclusions.length);		return new AccessPath<FieldRef>(newAccesses, newExclusionsArray);returnnewAccessPath<FieldRef>(newAccesses,newExclusionsArray);	}}	public ExclusionSet getExclusions(int index) {publicExclusionSetgetExclusions(intindex){		return new ExclusionSet(index);returnnewExclusionSet(index);	}}	




Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




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



	public AccessPath<FieldRef> append(AccessPath<FieldRef> accessPath) {
		if(exclusions.length > 0) 
			throw new IllegalStateException();
		
		FieldRef[] newAccesses = Arrays.copyOf(accesses, accesses.length + accessPath.accesses.length);
		System.arraycopy(accessPath.accesses, 0, newAccesses, accesses.length, accessPath.accesses.length);
		return new AccessPath<FieldRef>(newAccesses, accessPath.exclusions);
	}

	public AccessPath<FieldRef> removeFirstAccessIfAvailable() {
		if(accesses.length > 0)
			return new AccessPath<FieldRef>(Arrays.copyOfRange(accesses, 1, accesses.length), exclusions);
		else if(exclusions.length > 0)
			return new AccessPath<FieldRef>(accesses, Arrays.copyOfRange(exclusions, 1, exclusions.length));
		else
			return this;
	}

	public AccessPath<FieldRef> mergeExcludedFieldReference(FieldRef... fieldRef) {
		if(hasExclusions())
			return getExclusions(0).addExclusion(fieldRef);
		else
			return appendExcludedFieldReference(fieldRef);
	}
	






Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015



Bugfix, Helper functions, and renaming

 

Bugfix, Helper functions, and renaming

Johannes Lerch
committed
Jan 08, 2015


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


	public AccessPath<FieldRef> append(AccessPath<FieldRef> accessPath) {
		if(exclusions.length > 0) 
			throw new IllegalStateException();
		
		FieldRef[] newAccesses = Arrays.copyOf(accesses, accesses.length + accessPath.accesses.length);
		System.arraycopy(accessPath.accesses, 0, newAccesses, accesses.length, accessPath.accesses.length);
		return new AccessPath<FieldRef>(newAccesses, accessPath.exclusions);
	}

	public AccessPath<FieldRef> removeFirstAccessIfAvailable() {
		if(accesses.length > 0)
			return new AccessPath<FieldRef>(Arrays.copyOfRange(accesses, 1, accesses.length), exclusions);
		else if(exclusions.length > 0)
			return new AccessPath<FieldRef>(accesses, Arrays.copyOfRange(exclusions, 1, exclusions.length));
		else
			return this;
	}

	public AccessPath<FieldRef> mergeExcludedFieldReference(FieldRef... fieldRef) {
		if(hasExclusions())
			return getExclusions(0).addExclusion(fieldRef);
		else
			return appendExcludedFieldReference(fieldRef);
	}
	

	public AccessPath<FieldRef> append(AccessPath<FieldRef> accessPath) {publicAccessPath<FieldRef>append(AccessPath<FieldRef>accessPath){		if(exclusions.length > 0) if(exclusions.length>0)			throw new IllegalStateException();thrownewIllegalStateException();				FieldRef[] newAccesses = Arrays.copyOf(accesses, accesses.length + accessPath.accesses.length);FieldRef[]newAccesses=Arrays.copyOf(accesses,accesses.length+accessPath.accesses.length);		System.arraycopy(accessPath.accesses, 0, newAccesses, accesses.length, accessPath.accesses.length);System.arraycopy(accessPath.accesses,0,newAccesses,accesses.length,accessPath.accesses.length);		return new AccessPath<FieldRef>(newAccesses, accessPath.exclusions);returnnewAccessPath<FieldRef>(newAccesses,accessPath.exclusions);	}}	public AccessPath<FieldRef> removeFirstAccessIfAvailable() {publicAccessPath<FieldRef>removeFirstAccessIfAvailable(){		if(accesses.length > 0)if(accesses.length>0)			return new AccessPath<FieldRef>(Arrays.copyOfRange(accesses, 1, accesses.length), exclusions);returnnewAccessPath<FieldRef>(Arrays.copyOfRange(accesses,1,accesses.length),exclusions);		else if(exclusions.length > 0)elseif(exclusions.length>0)			return new AccessPath<FieldRef>(accesses, Arrays.copyOfRange(exclusions, 1, exclusions.length));returnnewAccessPath<FieldRef>(accesses,Arrays.copyOfRange(exclusions,1,exclusions.length));		elseelse			return this;returnthis;	}}	public AccessPath<FieldRef> mergeExcludedFieldReference(FieldRef... fieldRef) {publicAccessPath<FieldRef>mergeExcludedFieldReference(FieldRef...fieldRef){		if(hasExclusions())if(hasExclusions())			return getExclusions(0).addExclusion(fieldRef);returngetExclusions(0).addExclusion(fieldRef);		elseelse			return appendExcludedFieldReference(fieldRef);returnappendExcludedFieldReference(fieldRef);	}}	




cleaning code



Johannes Lerch
committed
Jan 07, 2015




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


114


115


116


117


118


119


120


121


122


123


124


125


126


127


128


129


130


131


132


133


134


135


136


137


138


139


140


141


142


143


144


145


146


147


148


149


150


151


152


153


154


155


156


157


158


159


160


161


162


163


164


165


166


167


168


169


170


171


172


173


174


175


176


177


178


179


180


181


182



	public AccessPath<FieldRef> appendExcludedFieldReference(FieldRef... fieldReferences) {
		Set<FieldRef>[] newExclusionsArray = Arrays.copyOf(exclusions, exclusions.length+1);
		newExclusionsArray[exclusions.length] = Sets.newHashSet(fieldReferences);
		return new AccessPath<>(accesses, newExclusionsArray);
	}
	
	public boolean isPrefixOf(AccessPath<FieldRef> accessPath) {
		if(accesses.length > accessPath.accesses.length)
			return false;
		
		if(accesses.length + exclusions.length > accessPath.accesses.length + accessPath.exclusions.length)
			return false;
		
		for(int i=0; i<accesses.length; i++) {
			if(!accesses[i].equals(accessPath.accesses[i]))
				return false;
		}
		
		for(int i=0; i<exclusions.length; i++) {
			if(i+accesses.length < accessPath.accesses.length) {
				if(exclusions[i].contains(accessPath.accesses[i+accesses.length]))
					return false;
			}
			else {
				if(!exclusions[i].containsAll(accessPath.exclusions[i+accesses.length - accessPath.accesses.length]))
					return false;
			}
		}
		
		return true;
	}
	
	public FieldRef[] getDeltaTo(AccessPath<FieldRef> accPath) {
		if(isPrefixOf(accPath))
			return Arrays.copyOfRange(accPath.accesses, accesses.length, accPath.accesses.length);
		else
			throw new IllegalArgumentException("Given AccessPath must be a prefix of the current AccessPath");
	}
	
	public AccessPath<FieldRef> mergeExcludedFieldReferences(AccessPath<FieldRef> accPath) {
		Set<FieldRef>[] newExclusionArray = new Set[Math.max(exclusions.length,accPath.exclusions.length)];
		for(int i=0; i<newExclusionArray.length; i++) {
			newExclusionArray[i] = Sets.newHashSet();
			if(i<exclusions.length)
				newExclusionArray[i].addAll(exclusions[i]);
			if(i<accPath.exclusions.length)
				newExclusionArray[i].addAll(accPath.exclusions[i]);
		}
		return new AccessPath<>(accesses, newExclusionArray);
	}
	
	public boolean isEmpty() {
		return exclusions.length == 0 && accesses.length == 0;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(accesses);
		result = prime * result + Arrays.hashCode(exclusions);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AccessPath))
			return false;
		AccessPath other = (AccessPath) obj;
		if (!Arrays.equals(accesses, other.accesses))
			return false;
		if (!Arrays.equals(exclusions, other.exclusions))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String result = accesses.length > 0 ? "."+Joiner.on(".").join(accesses) : "";
		for(Set<FieldRef> exclusion : exclusions) {
			result += "^" + Joiner.on(",").join(exclusion);
		}
		return result;
	}
	
	public class ExclusionSet {
		private int index;
	
		private ExclusionSet(int index) {
			this.index = index;
		}
		






cleaning code



Johannes Lerch
committed
Jan 07, 2015



cleaning code


cleaning code

Johannes Lerch
committed
Jan 07, 2015


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


114


115


116


117


118


119


120


121


122


123


124


125


126


127


128


129


130


131


132


133


134


135


136


137


138


139


140


141


142


143


144


145


146


147


148


149


150


151


152


153


154


155


156


157


158


159


160


161


162


163


164


165


166


167


168


169


170


171


172


173


174


175


176


177


178


179


180


181


182


	public AccessPath<FieldRef> appendExcludedFieldReference(FieldRef... fieldReferences) {
		Set<FieldRef>[] newExclusionsArray = Arrays.copyOf(exclusions, exclusions.length+1);
		newExclusionsArray[exclusions.length] = Sets.newHashSet(fieldReferences);
		return new AccessPath<>(accesses, newExclusionsArray);
	}
	
	public boolean isPrefixOf(AccessPath<FieldRef> accessPath) {
		if(accesses.length > accessPath.accesses.length)
			return false;
		
		if(accesses.length + exclusions.length > accessPath.accesses.length + accessPath.exclusions.length)
			return false;
		
		for(int i=0; i<accesses.length; i++) {
			if(!accesses[i].equals(accessPath.accesses[i]))
				return false;
		}
		
		for(int i=0; i<exclusions.length; i++) {
			if(i+accesses.length < accessPath.accesses.length) {
				if(exclusions[i].contains(accessPath.accesses[i+accesses.length]))
					return false;
			}
			else {
				if(!exclusions[i].containsAll(accessPath.exclusions[i+accesses.length - accessPath.accesses.length]))
					return false;
			}
		}
		
		return true;
	}
	
	public FieldRef[] getDeltaTo(AccessPath<FieldRef> accPath) {
		if(isPrefixOf(accPath))
			return Arrays.copyOfRange(accPath.accesses, accesses.length, accPath.accesses.length);
		else
			throw new IllegalArgumentException("Given AccessPath must be a prefix of the current AccessPath");
	}
	
	public AccessPath<FieldRef> mergeExcludedFieldReferences(AccessPath<FieldRef> accPath) {
		Set<FieldRef>[] newExclusionArray = new Set[Math.max(exclusions.length,accPath.exclusions.length)];
		for(int i=0; i<newExclusionArray.length; i++) {
			newExclusionArray[i] = Sets.newHashSet();
			if(i<exclusions.length)
				newExclusionArray[i].addAll(exclusions[i]);
			if(i<accPath.exclusions.length)
				newExclusionArray[i].addAll(accPath.exclusions[i]);
		}
		return new AccessPath<>(accesses, newExclusionArray);
	}
	
	public boolean isEmpty() {
		return exclusions.length == 0 && accesses.length == 0;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(accesses);
		result = prime * result + Arrays.hashCode(exclusions);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AccessPath))
			return false;
		AccessPath other = (AccessPath) obj;
		if (!Arrays.equals(accesses, other.accesses))
			return false;
		if (!Arrays.equals(exclusions, other.exclusions))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String result = accesses.length > 0 ? "."+Joiner.on(".").join(accesses) : "";
		for(Set<FieldRef> exclusion : exclusions) {
			result += "^" + Joiner.on(",").join(exclusion);
		}
		return result;
	}
	
	public class ExclusionSet {
		private int index;
	
		private ExclusionSet(int index) {
			this.index = index;
		}
		

	public AccessPath<FieldRef> appendExcludedFieldReference(FieldRef... fieldReferences) {publicAccessPath<FieldRef>appendExcludedFieldReference(FieldRef...fieldReferences){		Set<FieldRef>[] newExclusionsArray = Arrays.copyOf(exclusions, exclusions.length+1);Set<FieldRef>[]newExclusionsArray=Arrays.copyOf(exclusions,exclusions.length+1);		newExclusionsArray[exclusions.length] = Sets.newHashSet(fieldReferences);newExclusionsArray[exclusions.length]=Sets.newHashSet(fieldReferences);		return new AccessPath<>(accesses, newExclusionsArray);returnnewAccessPath<>(accesses,newExclusionsArray);	}}		public boolean isPrefixOf(AccessPath<FieldRef> accessPath) {publicbooleanisPrefixOf(AccessPath<FieldRef>accessPath){		if(accesses.length > accessPath.accesses.length)if(accesses.length>accessPath.accesses.length)			return false;returnfalse;				if(accesses.length + exclusions.length > accessPath.accesses.length + accessPath.exclusions.length)if(accesses.length+exclusions.length>accessPath.accesses.length+accessPath.exclusions.length)			return false;returnfalse;				for(int i=0; i<accesses.length; i++) {for(inti=0;i<accesses.length;i++){			if(!accesses[i].equals(accessPath.accesses[i]))if(!accesses[i].equals(accessPath.accesses[i]))				return false;returnfalse;		}}				for(int i=0; i<exclusions.length; i++) {for(inti=0;i<exclusions.length;i++){			if(i+accesses.length < accessPath.accesses.length) {if(i+accesses.length<accessPath.accesses.length){				if(exclusions[i].contains(accessPath.accesses[i+accesses.length]))if(exclusions[i].contains(accessPath.accesses[i+accesses.length]))					return false;returnfalse;			}}			else {else{				if(!exclusions[i].containsAll(accessPath.exclusions[i+accesses.length - accessPath.accesses.length]))if(!exclusions[i].containsAll(accessPath.exclusions[i+accesses.length-accessPath.accesses.length]))					return false;returnfalse;			}}		}}				return true;returntrue;	}}		public FieldRef[] getDeltaTo(AccessPath<FieldRef> accPath) {publicFieldRef[]getDeltaTo(AccessPath<FieldRef>accPath){		if(isPrefixOf(accPath))if(isPrefixOf(accPath))			return Arrays.copyOfRange(accPath.accesses, accesses.length, accPath.accesses.length);returnArrays.copyOfRange(accPath.accesses,accesses.length,accPath.accesses.length);		elseelse			throw new IllegalArgumentException("Given AccessPath must be a prefix of the current AccessPath");thrownewIllegalArgumentException("Given AccessPath must be a prefix of the current AccessPath");	}}		public AccessPath<FieldRef> mergeExcludedFieldReferences(AccessPath<FieldRef> accPath) {publicAccessPath<FieldRef>mergeExcludedFieldReferences(AccessPath<FieldRef>accPath){		Set<FieldRef>[] newExclusionArray = new Set[Math.max(exclusions.length,accPath.exclusions.length)];Set<FieldRef>[]newExclusionArray=newSet[Math.max(exclusions.length,accPath.exclusions.length)];		for(int i=0; i<newExclusionArray.length; i++) {for(inti=0;i<newExclusionArray.length;i++){			newExclusionArray[i] = Sets.newHashSet();newExclusionArray[i]=Sets.newHashSet();			if(i<exclusions.length)if(i<exclusions.length)				newExclusionArray[i].addAll(exclusions[i]);newExclusionArray[i].addAll(exclusions[i]);			if(i<accPath.exclusions.length)if(i<accPath.exclusions.length)				newExclusionArray[i].addAll(accPath.exclusions[i]);newExclusionArray[i].addAll(accPath.exclusions[i]);		}}		return new AccessPath<>(accesses, newExclusionArray);returnnewAccessPath<>(accesses,newExclusionArray);	}}		public boolean isEmpty() {publicbooleanisEmpty(){		return exclusions.length == 0 && accesses.length == 0;returnexclusions.length==0&&accesses.length==0;	}}		@Override@Override	public int hashCode() {publicinthashCode(){		final int prime = 31;finalintprime=31;		int result = 1;intresult=1;		result = prime * result + Arrays.hashCode(accesses);result=prime*result+Arrays.hashCode(accesses);		result = prime * result + Arrays.hashCode(exclusions);result=prime*result+Arrays.hashCode(exclusions);		return result;returnresult;	}}	@Override@Override	public boolean equals(Object obj) {publicbooleanequals(Objectobj){		if (this == obj)if(this==obj)			return true;returntrue;		if (obj == null)if(obj==null)			return false;returnfalse;		if (!(obj instanceof AccessPath))if(!(objinstanceofAccessPath))			return false;returnfalse;		AccessPath other = (AccessPath) obj;AccessPathother=(AccessPath)obj;		if (!Arrays.equals(accesses, other.accesses))if(!Arrays.equals(accesses,other.accesses))			return false;returnfalse;		if (!Arrays.equals(exclusions, other.exclusions))if(!Arrays.equals(exclusions,other.exclusions))			return false;returnfalse;		return true;returntrue;	}}	@Override@Override	public String toString() {publicStringtoString(){		String result = accesses.length > 0 ? "."+Joiner.on(".").join(accesses) : "";Stringresult=accesses.length>0?"."+Joiner.on(".").join(accesses):"";		for(Set<FieldRef> exclusion : exclusions) {for(Set<FieldRef>exclusion:exclusions){			result += "^" + Joiner.on(",").join(exclusion);result+="^"+Joiner.on(",").join(exclusion);		}}		return result;returnresult;	}}		public class ExclusionSet {publicclassExclusionSet{		private int index;privateintindex;			private ExclusionSet(int index) {privateExclusionSet(intindex){			this.index = index;this.index=index;		}}		




Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




183



		public AccessPath<FieldRef> addExclusion(FieldRef... exclusion) {






Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015



Bugfix, Helper functions, and renaming

 

Bugfix, Helper functions, and renaming

Johannes Lerch
committed
Jan 08, 2015


183


		public AccessPath<FieldRef> addExclusion(FieldRef... exclusion) {

		public AccessPath<FieldRef> addExclusion(FieldRef... exclusion) {publicAccessPath<FieldRef>addExclusion(FieldRef...exclusion){




cleaning code



Johannes Lerch
committed
Jan 07, 2015




184



			HashSet<FieldRef> newExclusions = Sets.newHashSet(exclusions[index]);






cleaning code



Johannes Lerch
committed
Jan 07, 2015



cleaning code


cleaning code

Johannes Lerch
committed
Jan 07, 2015


184


			HashSet<FieldRef> newExclusions = Sets.newHashSet(exclusions[index]);

			HashSet<FieldRef> newExclusions = Sets.newHashSet(exclusions[index]);HashSet<FieldRef>newExclusions=Sets.newHashSet(exclusions[index]);




Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




185


186



			for(FieldRef excl : exclusion)
				newExclusions.add(excl);






Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015



Bugfix, Helper functions, and renaming

 

Bugfix, Helper functions, and renaming

Johannes Lerch
committed
Jan 08, 2015


185


186


			for(FieldRef excl : exclusion)
				newExclusions.add(excl);

			for(FieldRef excl : exclusion)for(FieldRefexcl:exclusion)				newExclusions.add(excl);newExclusions.add(excl);




cleaning code



Johannes Lerch
committed
Jan 07, 2015




187


188


189


190


191


192



			Set<FieldRef>[] newExclusionsArray = exclusions.clone();
			newExclusionsArray[index] = newExclusions;
			return new AccessPath<FieldRef>(accesses, newExclusionsArray);
		}
	}
}





cleaning code



Johannes Lerch
committed
Jan 07, 2015



cleaning code


cleaning code

Johannes Lerch
committed
Jan 07, 2015


187


188


189


190


191


192


			Set<FieldRef>[] newExclusionsArray = exclusions.clone();
			newExclusionsArray[index] = newExclusions;
			return new AccessPath<FieldRef>(accesses, newExclusionsArray);
		}
	}
}
			Set<FieldRef>[] newExclusionsArray = exclusions.clone();Set<FieldRef>[]newExclusionsArray=exclusions.clone();			newExclusionsArray[index] = newExclusions;newExclusionsArray[index]=newExclusions;			return new AccessPath<FieldRef>(accesses, newExclusionsArray);returnnewAccessPath<FieldRef>(accesses,newExclusionsArray);		}}	}}}}





