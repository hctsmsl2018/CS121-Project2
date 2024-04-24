



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

b974b09ef56a83bbc26cf9960e46acbe7ebadd0e

















b974b09ef56a83bbc26cf9960e46acbe7ebadd0e


Switch branch/tag










heros


src


heros


alias


AccessPath.java



Find file
Normal viewHistoryPermalink






AccessPath.java



13.7 KB









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









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




13


14


15



import heros.alias.SubAccessPath.SetOfPossibleFieldAccesses;
import heros.alias.SubAccessPath.SpecificFieldAccess;
import heros.alias.Transition.MatchResult;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




16












cleaning code



Johannes Lerch
committed
Jan 07, 2015




17



import java.util.Arrays;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




18



import java.util.Collection;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




19



import java.util.HashSet;








subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015




20



import java.util.List;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




21


22



import java.util.Set;









remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




23



import com.google.common.base.Function;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




24



import com.google.common.base.Joiner;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




25



import com.google.common.collect.Lists;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




26


27


28



import com.google.common.collect.Sets;

@SuppressWarnings("unchecked")








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




29



public class AccessPath<T extends AccessPath.FieldRef<T>> {








cleaning code



Johannes Lerch
committed
Jan 07, 2015




30












merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




31


32


33


34


35



	public static interface FieldRef<F> {
		boolean shouldBeMergedWith(F fieldRef);
	}
	
	public static <T extends FieldRef<T>> AccessPath<T> empty() {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




36


37


38



		return new AccessPath<T>();
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




39



	private final SubAccessPath<T>[] accesses;








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




40



	private final Set<T> exclusions;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




41


42



	
	public AccessPath() {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




43



		accesses = new SubAccessPath[0];








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




44



		exclusions = Sets.newHashSet();








cleaning code



Johannes Lerch
committed
Jan 07, 2015




45


46



	}
	








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




47



	AccessPath(SubAccessPath<T>[] accesses, Set<T> exclusions) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




48


49



		this.accesses = accesses;
		this.exclusions = exclusions;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




50


51



	}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




52



	public boolean isAccessInExclusions(SubAccessPath<T>... fieldReferences) {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




53


54


55


56



		if(fieldReferences.length > 0) {
			for(T field : fieldReferences[0].elements()) {
				if(!exclusions.contains(field))
					return false;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




57


58



			}
			return true;








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




59



		}








cleaning code



Johannes Lerch
committed
Jan 07, 2015




60


61


62



		return false;
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




63



	public AccessPath<T> addFieldReference(SubAccessPath<T>... fieldReferences) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




64


65


66



		return addFieldReference(true, fieldReferences);
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




67



	AccessPath<T> addFieldReference(boolean merge, SubAccessPath<T>... fieldReferences) {








cleaning code



Johannes Lerch
committed
Jan 07, 2015




68



		if(isAccessInExclusions(fieldReferences))








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




69



			throw new IllegalArgumentException("FieldRef "+Arrays.toString(fieldReferences)+" cannot be added to "+toString());








cleaning code



Johannes Lerch
committed
Jan 07, 2015




70












regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




71


72


73



		if(merge) {
			for(int i=fieldReferences.length-1; i>=0; i--) {
				for(int j=0; j<accesses.length; j++) {








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




74



					if(accesses[j].shouldBeMerged(fieldReferences[i])) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




75


76


77


78


79


80



						// [..., {j-i}, ...]
						
						AccessPathBuilder builder = new AccessPathBuilder(j+fieldReferences.length-i);
						builder.keep(0, j);
						builder.merge(j, accesses.length).mergeWithLast(fieldReferences, 0, i);
						builder.append(fieldReferences, i+1, fieldReferences.length);








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




81



						builder.removeExclusions();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




82


83


84


85


86


87


88


89


90



						return builder.build();
					}
				}
			}
		}
		
		AccessPathBuilder builder = new AccessPathBuilder(accesses.length + fieldReferences.length);
		builder.keep(0, accesses.length);
		builder.append(fieldReferences, 0, fieldReferences.length);








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




91


92



		if(fieldReferences.length>0)
			builder.removeExclusions();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




93


94


95



		return builder.build();
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




96


97



	public AccessPath<T> addFieldReference(T... fieldReferences) {
		SubAccessPath<T>[] subPath = new SubAccessPath[fieldReferences.length];








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




98


99


100


101


102


103


104


105



		for(int i=0; i<fieldReferences.length; i++) {
			subPath[i] = new SpecificFieldAccess<>(fieldReferences[i]);
		}
		return addFieldReference(subPath);
	}
	
	private class AccessPathBuilder {
		








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




106



		private Set<T> newExclusions;








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




107



		private SubAccessPath<T>[] newAccesses;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




108


109


110


111


112


113


114



		private int currentIndex = 0;

		public AccessPathBuilder(int capacity) {
			newAccesses = new SubAccessPath[capacity];
			newExclusions = exclusions;
		}
		








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




115



		public AccessPath<T> build() {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




116


117


118



			return new AccessPath<>(newAccesses, newExclusions);
		}









remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




119


120



		public void removeExclusions() {
			newExclusions = Sets.newHashSet();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




121


122



		}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




123



		public void append(SubAccessPath<T>[] fieldReferences, int start, int endExcl) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




124


125


126


127



			for(int i=start; i<endExcl; i++) {
				newAccesses[currentIndex] = fieldReferences[i];
				currentIndex++;
				if(fieldReferences[i] instanceof SetOfPossibleFieldAccesses)








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




128



					removeExclusions();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




129


130


131


132



			}
			currentIndex+=endExcl-start;
		}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




133



		public void mergeWithLast(SubAccessPath<T>[] fieldReferences, int start, int endExcl) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




134


135


136


137



			newAccesses[currentIndex-1].merge(Arrays.copyOfRange(fieldReferences, start, endExcl));
		}

		public AccessPathBuilder merge(int srcIndex, int destIndexExcl) {








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




138



			Set<T> set = Sets.newHashSet();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




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



			for(int i=srcIndex; i<destIndexExcl; i++) {
				set.addAll(accesses[i].elements());
			}
			newAccesses[currentIndex] = new SubAccessPath.SetOfPossibleFieldAccesses<>(set);
			currentIndex++;
			return this;
		}

		public AccessPathBuilder keep(int srcIndex, int destIndexExcl) {
			System.arraycopy(accesses, srcIndex, newAccesses, currentIndex, destIndexExcl-srcIndex);
			currentIndex += destIndexExcl-srcIndex;
			return this;
		}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




153



		public void append(T fieldRef) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




154


155


156


157



			newAccesses[currentIndex] = new SubAccessPath.SpecificFieldAccess<>(fieldRef);
			currentIndex++;
		}
		








cleaning code



Johannes Lerch
committed
Jan 07, 2015




158


159



	}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




160



	public AccessPath<T> prepend(T fieldRef) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




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



		for(int j=0; j<accesses.length; j++) {
			if(accesses[j].contains(fieldRef)) {
				// [{0-j}, ...]
				
				AccessPathBuilder builder = new AccessPathBuilder(accesses.length-j);
				builder.merge(0, j+1);
				builder.keep(j+1, accesses.length);
				return builder.build();
			}
		}
		AccessPathBuilder builder = new AccessPathBuilder(accesses.length + 1);
		builder.append(fieldRef);
		builder.keep(0, accesses.length);
		return builder.build();








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




175


176



	}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




177



	public AccessPath<T> removeFirst(T field) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




178


179


180



		for(int i=0; i<accesses.length; i++) {
			if(accesses[i].contains(field)) {
				if(accesses[i] instanceof SpecificFieldAccess)








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




181



					return new AccessPath<T>(Arrays.copyOfRange(accesses, i+1, accesses.length), exclusions);








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




182


183


184


185


186


187


188


189


190


191



				else
					return this;
			}
			else if(accesses[i] instanceof SpecificFieldAccess)
				throw new IllegalStateException("Trying to remove "+field+" from "+this);
		}
		
		throw new IllegalStateException("Trying to remove "+field+" from "+this);
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




192



	public AccessPath<T> appendExcludedFieldReference(T... fieldReferences) {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




193


194


195



		HashSet<T> newExclusions = Sets.newHashSet(fieldReferences);
		newExclusions.addAll(exclusions);
		return new AccessPath<>(accesses, newExclusions);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




196



	}








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




197


198


199


200


201


202


203


204


205


206


207


208


209


210




	public static enum PrefixTestResult {
		GUARANTEED_PREFIX(2), POTENTIAL_PREFIX(1), NO_PREFIX(0);
		
		private int value;

		private PrefixTestResult(int value) {
			this.value = value;
		}
		
		public boolean atLeast(PrefixTestResult minimum) {
			return value >= minimum.value;
		}
	}








cleaning code



Johannes Lerch
committed
Jan 07, 2015




211



	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




212



	public PrefixTestResult isPrefixOf(AccessPath<T> accPath) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




213


214


215



		int currIndex = 0;
		int otherIndex = 0;
		PrefixTestResult result = PrefixTestResult.GUARANTEED_PREFIX;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




216



		








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




217


218



		int finalIndex = finalIndex();
		outer: while(currIndex < finalIndex) {








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




219


220



			Collection<Transition<T>> transitions = possibleTransitions(currIndex, true);
			Collection<Transition<T>> otherTransitions = accPath.possibleTransitions(otherIndex, true);








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




221












merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




222


223


224



			for(Transition<T> transition : transitions) {
				for(Transition<T> otherTransition : otherTransitions) {
					MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




225


226



					if(match.hasMatched()) {
						if(currIndex == transition.transitionToIndex() && otherIndex == otherTransition.transitionToIndex())








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




227



							continue;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




228


229


230


231


232


233


234


235


236


237



						
						currIndex = transition.transitionToIndex();
						otherIndex = otherTransition.transitionToIndex();
						if(!match.isGuaranteedMatch())
							result = PrefixTestResult.POTENTIAL_PREFIX;
						
						continue outer;
					}
				}
			}








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




238



			return PrefixTestResult.NO_PREFIX;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




239



		}








cleaning code



Johannes Lerch
committed
Jan 07, 2015




240



		








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




241


242


243


244



		return result;
	}
	
	private int finalIndex() {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




245


246



		if(!exclusions.isEmpty())
			return accesses.length + 1;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




247


248



		
		int finalIndex = 0;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




249



		for(int i=0; i<accesses.length; i++) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




250


251



			if(accesses[i] instanceof SpecificFieldAccess)
				finalIndex = i+1;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




252



		}








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




253


254


255



		return finalIndex;
	}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




256


257



	private Collection<Transition<T>> possibleTransitions(int index, boolean addExclusionTransitions) {
		Collection<Transition<T>> result = Lists.newLinkedList();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




258


259


260



		if(index < accesses.length) {
			if(accesses[index] instanceof SetOfPossibleFieldAccesses) {
				result.add(new Transition.SubAccessPathTransition<>(index, accesses[index]));








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




261



				result.addAll(possibleTransitions(index+1, addExclusionTransitions));








cleaning code



Johannes Lerch
committed
Jan 07, 2015




262



			}








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




263


264



			else
				result.add(new Transition.SubAccessPathTransition<>(index+1, accesses[index]));








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




265


266



		} else if(addExclusionTransitions && index - accesses.length == 0 && !exclusions.isEmpty()) {
			result.add(new Transition.ExclusionPathTransition<T>(index+1, exclusions));








cleaning code



Johannes Lerch
committed
Jan 07, 2015




267



		}








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




268



		return result;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




269


270



	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




271


272


273



	public SubAccessPath<T>[] getDeltaTo(AccessPath<T> accPath) {
		int currIndex = 0;
		int otherIndex = 0;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




274



		








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




275


276


277


278


279


280


281


282


283


284


285


286


287


288


289



		outer: while(true) {
			Collection<Transition<T>> transitions = possibleTransitions(currIndex, false);
			Collection<Transition<T>> otherTransitions = accPath.possibleTransitions(otherIndex, false);

			for(Transition<T> transition : transitions) {
				for(Transition<T> otherTransition : otherTransitions) {
					MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);
					if(match.hasMatched()) {
						if(currIndex == transition.transitionToIndex() && otherIndex == otherTransition.transitionToIndex())
							continue;
						
						currIndex = transition.transitionToIndex();
						otherIndex = otherTransition.transitionToIndex();
						continue outer;
					}








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




290


291



				}
			}








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




292



			break;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




293


294



		}
		








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




295



		return Arrays.copyOfRange(accPath.accesses, otherIndex, accPath.accesses.length);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




296


297



	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




298



	public AccessPath<T> mergeExcludedFieldReferences(AccessPath<T> accPath) {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




299


300


301



		HashSet<T> newExclusions = Sets.newHashSet(exclusions);
		newExclusions.addAll(accPath.exclusions);
		return new AccessPath<>(accesses, newExclusions);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




302


303



	}
	








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




304


305


306


307



	public boolean mayHaveEmptyAccessPath() {
		return finalIndex() == 0;
	}
	








cleaning code



Johannes Lerch
committed
Jan 07, 2015




308



	public boolean isEmpty() {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




309



		return exclusions.isEmpty() && accesses.length == 0;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




310


311


312


313


314


315


316



	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(accesses);








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




317



		result = prime * result + ((exclusions == null) ? 0 : exclusions.hashCode());








cleaning code



Johannes Lerch
committed
Jan 07, 2015




318


319


320


321


322


323


324


325


326


327


328


329


330


331



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








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




332


333


334


335



		if (exclusions == null) {
			if (other.exclusions != null)
				return false;
		} else if (!exclusions.equals(other.exclusions))








cleaning code



Johannes Lerch
committed
Jan 07, 2015




336


337


338


339


340


341


342



			return false;
		return true;
	}

	@Override
	public String toString() {
		String result = accesses.length > 0 ? "."+Joiner.on(".").join(accesses) : "";








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




343


344



		if(!exclusions.isEmpty())
			result += "^" + Joiner.on(",").join(exclusions);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




345


346


347



		return result;
	}
	








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




348


349


350


351



	public <U extends FieldRef<U>> AccessPath<U> map(Function<T, U> function) {
		SubAccessPath<U>[] newAccesses = new SubAccessPath[accesses.length];
		for(int i=0; i<accesses.length; i++) {
			newAccesses[i] = accesses[i].map(function);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




352



		}








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




353


354


355


356



		Set<U> newExclusions = Sets.newHashSet();
		for(T f : exclusions)
			newExclusions.add(function.apply(f));
		return new AccessPath<U>(newAccesses, newExclusions);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




357



	}








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




358



	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




359



	public AccessPath<T> removeAnyAccess() {








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




360



		if(accesses.length > 0)








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




361



			return new AccessPath<T>(new SubAccessPath[0], exclusions);








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




362


363


364


365


366


367



		else
			return this;
	}

	public boolean hasEmptyAccessPath() {
		return accesses.length == 0;








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




368



	}








subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015




369


370


371


372


373


374


375


376


377


378


379


380


381


382


383


384


385


386


387


388


389


390


391


392


393


394


395


396


397


398


399


400


401


402


403


404


405


406


407


408


409


410




	public boolean subsumes(AccessPath<T> accPath) {
		int currIndex = 0;
		int otherIndex = 0;
		
		
		outer: while(true) {
			Collection<Transition<T>> transitions = possibleTransitions(currIndex, false);
			Collection<Transition<T>> otherTransitions = accPath.possibleTransitions(otherIndex, false);

			if((currIndex >= accesses.length || (currIndex == accesses.length-1 && accesses[currIndex] instanceof SetOfPossibleFieldAccesses)) 
					&& otherIndex>=accPath.accesses.length-1) {
				if(transitions.isEmpty())
					return otherTransitions.isEmpty() && hasAtLeastTheSameExclusionsAs(accPath);
				for(Transition<T> transition : transitions) {
					for(Transition<T> otherTransition : otherTransitions) {
						MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);
						if(!match.hasMatched())
							return false;
					}	
				}
				return hasAtLeastTheSameExclusionsAs(accPath);
			}

			for(Transition<T> transition : transitions) {
				for(Transition<T> otherTransition : otherTransitions) {
					MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);
					if(match.hasMatched()) {
						if(otherIndex == otherTransition.transitionToIndex())
							continue;
						
						currIndex = transition.transitionToIndex();
						otherIndex = otherTransition.transitionToIndex();
						continue outer;
					}
				}
			}
			return false;
		}
	}
	
	private boolean hasAtLeastTheSameExclusionsAs(AccessPath<T> accPath) {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




411



		return accPath.exclusions.containsAll(exclusions);








subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015




412


413


414


415


416


417


418



	}

	public Collection<String> tokenize() {
		List<String> result = Lists.newLinkedList();
		for(SubAccessPath<T> s : accesses) {
			result.add(s.toString());
		}








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




419


420



		if(!exclusions.isEmpty())
			result.add("^"+Joiner.on(",").join(exclusions));








subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015




421


422



		return result;
	}








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




423


424


425


426




	public AccessPath<T> removeExclusions() {
		return new AccessPath<T>(accesses, Sets.<T>newHashSet());
	}








additional methods for AccessPath required by client FlowFunction

 


Johannes Lerch
committed
Feb 16, 2015




427


428


429


430


431


432


433


434


435


436


437


438


439


440


441


442


443


444


445


446




	public SubAccessPath<T> getFirstAccess() {
		return accesses[0];
	}

	public AccessPath<T> removeRepeatableFirstAccess(T field) {
		Collection<? extends T> elements = accesses[0].elements();
		if(!elements.contains(field))
			throw new IllegalArgumentException();
		
		if(elements.size() == 1) {
			return new AccessPath<>(Arrays.copyOfRange(accesses, 1, accesses.length), exclusions);
		}
		
		HashSet<T> newSet = Sets.newHashSet(elements);
		newSet.remove(field);
		SubAccessPath<T>[] newAccesses = Arrays.copyOf(accesses, accesses.length);
		newAccesses[0] = new SetOfPossibleFieldAccesses<>(newSet);
		return new AccessPath<T>(newAccesses, exclusions);
	}








cleaning code



Johannes Lerch
committed
Jan 07, 2015




447



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

b974b09ef56a83bbc26cf9960e46acbe7ebadd0e

















b974b09ef56a83bbc26cf9960e46acbe7ebadd0e


Switch branch/tag










heros


src


heros


alias


AccessPath.java



Find file
Normal viewHistoryPermalink






AccessPath.java



13.7 KB









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









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




13


14


15



import heros.alias.SubAccessPath.SetOfPossibleFieldAccesses;
import heros.alias.SubAccessPath.SpecificFieldAccess;
import heros.alias.Transition.MatchResult;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




16












cleaning code



Johannes Lerch
committed
Jan 07, 2015




17



import java.util.Arrays;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




18



import java.util.Collection;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




19



import java.util.HashSet;








subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015




20



import java.util.List;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




21


22



import java.util.Set;









remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




23



import com.google.common.base.Function;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




24



import com.google.common.base.Joiner;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




25



import com.google.common.collect.Lists;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




26


27


28



import com.google.common.collect.Sets;

@SuppressWarnings("unchecked")








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




29



public class AccessPath<T extends AccessPath.FieldRef<T>> {








cleaning code



Johannes Lerch
committed
Jan 07, 2015




30












merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




31


32


33


34


35



	public static interface FieldRef<F> {
		boolean shouldBeMergedWith(F fieldRef);
	}
	
	public static <T extends FieldRef<T>> AccessPath<T> empty() {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




36


37


38



		return new AccessPath<T>();
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




39



	private final SubAccessPath<T>[] accesses;








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




40



	private final Set<T> exclusions;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




41


42



	
	public AccessPath() {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




43



		accesses = new SubAccessPath[0];








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




44



		exclusions = Sets.newHashSet();








cleaning code



Johannes Lerch
committed
Jan 07, 2015




45


46



	}
	








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




47



	AccessPath(SubAccessPath<T>[] accesses, Set<T> exclusions) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




48


49



		this.accesses = accesses;
		this.exclusions = exclusions;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




50


51



	}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




52



	public boolean isAccessInExclusions(SubAccessPath<T>... fieldReferences) {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




53


54


55


56



		if(fieldReferences.length > 0) {
			for(T field : fieldReferences[0].elements()) {
				if(!exclusions.contains(field))
					return false;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




57


58



			}
			return true;








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




59



		}








cleaning code



Johannes Lerch
committed
Jan 07, 2015




60


61


62



		return false;
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




63



	public AccessPath<T> addFieldReference(SubAccessPath<T>... fieldReferences) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




64


65


66



		return addFieldReference(true, fieldReferences);
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




67



	AccessPath<T> addFieldReference(boolean merge, SubAccessPath<T>... fieldReferences) {








cleaning code



Johannes Lerch
committed
Jan 07, 2015




68



		if(isAccessInExclusions(fieldReferences))








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




69



			throw new IllegalArgumentException("FieldRef "+Arrays.toString(fieldReferences)+" cannot be added to "+toString());








cleaning code



Johannes Lerch
committed
Jan 07, 2015




70












regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




71


72


73



		if(merge) {
			for(int i=fieldReferences.length-1; i>=0; i--) {
				for(int j=0; j<accesses.length; j++) {








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




74



					if(accesses[j].shouldBeMerged(fieldReferences[i])) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




75


76


77


78


79


80



						// [..., {j-i}, ...]
						
						AccessPathBuilder builder = new AccessPathBuilder(j+fieldReferences.length-i);
						builder.keep(0, j);
						builder.merge(j, accesses.length).mergeWithLast(fieldReferences, 0, i);
						builder.append(fieldReferences, i+1, fieldReferences.length);








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




81



						builder.removeExclusions();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




82


83


84


85


86


87


88


89


90



						return builder.build();
					}
				}
			}
		}
		
		AccessPathBuilder builder = new AccessPathBuilder(accesses.length + fieldReferences.length);
		builder.keep(0, accesses.length);
		builder.append(fieldReferences, 0, fieldReferences.length);








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




91


92



		if(fieldReferences.length>0)
			builder.removeExclusions();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




93


94


95



		return builder.build();
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




96


97



	public AccessPath<T> addFieldReference(T... fieldReferences) {
		SubAccessPath<T>[] subPath = new SubAccessPath[fieldReferences.length];








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




98


99


100


101


102


103


104


105



		for(int i=0; i<fieldReferences.length; i++) {
			subPath[i] = new SpecificFieldAccess<>(fieldReferences[i]);
		}
		return addFieldReference(subPath);
	}
	
	private class AccessPathBuilder {
		








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




106



		private Set<T> newExclusions;








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




107



		private SubAccessPath<T>[] newAccesses;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




108


109


110


111


112


113


114



		private int currentIndex = 0;

		public AccessPathBuilder(int capacity) {
			newAccesses = new SubAccessPath[capacity];
			newExclusions = exclusions;
		}
		








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




115



		public AccessPath<T> build() {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




116


117


118



			return new AccessPath<>(newAccesses, newExclusions);
		}









remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




119


120



		public void removeExclusions() {
			newExclusions = Sets.newHashSet();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




121


122



		}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




123



		public void append(SubAccessPath<T>[] fieldReferences, int start, int endExcl) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




124


125


126


127



			for(int i=start; i<endExcl; i++) {
				newAccesses[currentIndex] = fieldReferences[i];
				currentIndex++;
				if(fieldReferences[i] instanceof SetOfPossibleFieldAccesses)








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




128



					removeExclusions();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




129


130


131


132



			}
			currentIndex+=endExcl-start;
		}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




133



		public void mergeWithLast(SubAccessPath<T>[] fieldReferences, int start, int endExcl) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




134


135


136


137



			newAccesses[currentIndex-1].merge(Arrays.copyOfRange(fieldReferences, start, endExcl));
		}

		public AccessPathBuilder merge(int srcIndex, int destIndexExcl) {








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




138



			Set<T> set = Sets.newHashSet();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




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



			for(int i=srcIndex; i<destIndexExcl; i++) {
				set.addAll(accesses[i].elements());
			}
			newAccesses[currentIndex] = new SubAccessPath.SetOfPossibleFieldAccesses<>(set);
			currentIndex++;
			return this;
		}

		public AccessPathBuilder keep(int srcIndex, int destIndexExcl) {
			System.arraycopy(accesses, srcIndex, newAccesses, currentIndex, destIndexExcl-srcIndex);
			currentIndex += destIndexExcl-srcIndex;
			return this;
		}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




153



		public void append(T fieldRef) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




154


155


156


157



			newAccesses[currentIndex] = new SubAccessPath.SpecificFieldAccess<>(fieldRef);
			currentIndex++;
		}
		








cleaning code



Johannes Lerch
committed
Jan 07, 2015




158


159



	}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




160



	public AccessPath<T> prepend(T fieldRef) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




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



		for(int j=0; j<accesses.length; j++) {
			if(accesses[j].contains(fieldRef)) {
				// [{0-j}, ...]
				
				AccessPathBuilder builder = new AccessPathBuilder(accesses.length-j);
				builder.merge(0, j+1);
				builder.keep(j+1, accesses.length);
				return builder.build();
			}
		}
		AccessPathBuilder builder = new AccessPathBuilder(accesses.length + 1);
		builder.append(fieldRef);
		builder.keep(0, accesses.length);
		return builder.build();








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




175


176



	}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




177



	public AccessPath<T> removeFirst(T field) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




178


179


180



		for(int i=0; i<accesses.length; i++) {
			if(accesses[i].contains(field)) {
				if(accesses[i] instanceof SpecificFieldAccess)








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




181



					return new AccessPath<T>(Arrays.copyOfRange(accesses, i+1, accesses.length), exclusions);








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




182


183


184


185


186


187


188


189


190


191



				else
					return this;
			}
			else if(accesses[i] instanceof SpecificFieldAccess)
				throw new IllegalStateException("Trying to remove "+field+" from "+this);
		}
		
		throw new IllegalStateException("Trying to remove "+field+" from "+this);
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




192



	public AccessPath<T> appendExcludedFieldReference(T... fieldReferences) {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




193


194


195



		HashSet<T> newExclusions = Sets.newHashSet(fieldReferences);
		newExclusions.addAll(exclusions);
		return new AccessPath<>(accesses, newExclusions);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




196



	}








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




197


198


199


200


201


202


203


204


205


206


207


208


209


210




	public static enum PrefixTestResult {
		GUARANTEED_PREFIX(2), POTENTIAL_PREFIX(1), NO_PREFIX(0);
		
		private int value;

		private PrefixTestResult(int value) {
			this.value = value;
		}
		
		public boolean atLeast(PrefixTestResult minimum) {
			return value >= minimum.value;
		}
	}








cleaning code



Johannes Lerch
committed
Jan 07, 2015




211



	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




212



	public PrefixTestResult isPrefixOf(AccessPath<T> accPath) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




213


214


215



		int currIndex = 0;
		int otherIndex = 0;
		PrefixTestResult result = PrefixTestResult.GUARANTEED_PREFIX;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




216



		








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




217


218



		int finalIndex = finalIndex();
		outer: while(currIndex < finalIndex) {








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




219


220



			Collection<Transition<T>> transitions = possibleTransitions(currIndex, true);
			Collection<Transition<T>> otherTransitions = accPath.possibleTransitions(otherIndex, true);








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




221












merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




222


223


224



			for(Transition<T> transition : transitions) {
				for(Transition<T> otherTransition : otherTransitions) {
					MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




225


226



					if(match.hasMatched()) {
						if(currIndex == transition.transitionToIndex() && otherIndex == otherTransition.transitionToIndex())








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




227



							continue;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




228


229


230


231


232


233


234


235


236


237



						
						currIndex = transition.transitionToIndex();
						otherIndex = otherTransition.transitionToIndex();
						if(!match.isGuaranteedMatch())
							result = PrefixTestResult.POTENTIAL_PREFIX;
						
						continue outer;
					}
				}
			}








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




238



			return PrefixTestResult.NO_PREFIX;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




239



		}








cleaning code



Johannes Lerch
committed
Jan 07, 2015




240



		








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




241


242


243


244



		return result;
	}
	
	private int finalIndex() {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




245


246



		if(!exclusions.isEmpty())
			return accesses.length + 1;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




247


248



		
		int finalIndex = 0;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




249



		for(int i=0; i<accesses.length; i++) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




250


251



			if(accesses[i] instanceof SpecificFieldAccess)
				finalIndex = i+1;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




252



		}








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




253


254


255



		return finalIndex;
	}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




256


257



	private Collection<Transition<T>> possibleTransitions(int index, boolean addExclusionTransitions) {
		Collection<Transition<T>> result = Lists.newLinkedList();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




258


259


260



		if(index < accesses.length) {
			if(accesses[index] instanceof SetOfPossibleFieldAccesses) {
				result.add(new Transition.SubAccessPathTransition<>(index, accesses[index]));








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




261



				result.addAll(possibleTransitions(index+1, addExclusionTransitions));








cleaning code



Johannes Lerch
committed
Jan 07, 2015




262



			}








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




263


264



			else
				result.add(new Transition.SubAccessPathTransition<>(index+1, accesses[index]));








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




265


266



		} else if(addExclusionTransitions && index - accesses.length == 0 && !exclusions.isEmpty()) {
			result.add(new Transition.ExclusionPathTransition<T>(index+1, exclusions));








cleaning code



Johannes Lerch
committed
Jan 07, 2015




267



		}








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




268



		return result;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




269


270



	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




271


272


273



	public SubAccessPath<T>[] getDeltaTo(AccessPath<T> accPath) {
		int currIndex = 0;
		int otherIndex = 0;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




274



		








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




275


276


277


278


279


280


281


282


283


284


285


286


287


288


289



		outer: while(true) {
			Collection<Transition<T>> transitions = possibleTransitions(currIndex, false);
			Collection<Transition<T>> otherTransitions = accPath.possibleTransitions(otherIndex, false);

			for(Transition<T> transition : transitions) {
				for(Transition<T> otherTransition : otherTransitions) {
					MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);
					if(match.hasMatched()) {
						if(currIndex == transition.transitionToIndex() && otherIndex == otherTransition.transitionToIndex())
							continue;
						
						currIndex = transition.transitionToIndex();
						otherIndex = otherTransition.transitionToIndex();
						continue outer;
					}








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




290


291



				}
			}








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




292



			break;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




293


294



		}
		








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




295



		return Arrays.copyOfRange(accPath.accesses, otherIndex, accPath.accesses.length);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




296


297



	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




298



	public AccessPath<T> mergeExcludedFieldReferences(AccessPath<T> accPath) {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




299


300


301



		HashSet<T> newExclusions = Sets.newHashSet(exclusions);
		newExclusions.addAll(accPath.exclusions);
		return new AccessPath<>(accesses, newExclusions);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




302


303



	}
	








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




304


305


306


307



	public boolean mayHaveEmptyAccessPath() {
		return finalIndex() == 0;
	}
	








cleaning code



Johannes Lerch
committed
Jan 07, 2015




308



	public boolean isEmpty() {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




309



		return exclusions.isEmpty() && accesses.length == 0;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




310


311


312


313


314


315


316



	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(accesses);








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




317



		result = prime * result + ((exclusions == null) ? 0 : exclusions.hashCode());








cleaning code



Johannes Lerch
committed
Jan 07, 2015




318


319


320


321


322


323


324


325


326


327


328


329


330


331



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








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




332


333


334


335



		if (exclusions == null) {
			if (other.exclusions != null)
				return false;
		} else if (!exclusions.equals(other.exclusions))








cleaning code



Johannes Lerch
committed
Jan 07, 2015




336


337


338


339


340


341


342



			return false;
		return true;
	}

	@Override
	public String toString() {
		String result = accesses.length > 0 ? "."+Joiner.on(".").join(accesses) : "";








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




343


344



		if(!exclusions.isEmpty())
			result += "^" + Joiner.on(",").join(exclusions);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




345


346


347



		return result;
	}
	








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




348


349


350


351



	public <U extends FieldRef<U>> AccessPath<U> map(Function<T, U> function) {
		SubAccessPath<U>[] newAccesses = new SubAccessPath[accesses.length];
		for(int i=0; i<accesses.length; i++) {
			newAccesses[i] = accesses[i].map(function);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




352



		}








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




353


354


355


356



		Set<U> newExclusions = Sets.newHashSet();
		for(T f : exclusions)
			newExclusions.add(function.apply(f));
		return new AccessPath<U>(newAccesses, newExclusions);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




357



	}








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




358



	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




359



	public AccessPath<T> removeAnyAccess() {








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




360



		if(accesses.length > 0)








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




361



			return new AccessPath<T>(new SubAccessPath[0], exclusions);








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




362


363


364


365


366


367



		else
			return this;
	}

	public boolean hasEmptyAccessPath() {
		return accesses.length == 0;








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




368



	}








subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015




369


370


371


372


373


374


375


376


377


378


379


380


381


382


383


384


385


386


387


388


389


390


391


392


393


394


395


396


397


398


399


400


401


402


403


404


405


406


407


408


409


410




	public boolean subsumes(AccessPath<T> accPath) {
		int currIndex = 0;
		int otherIndex = 0;
		
		
		outer: while(true) {
			Collection<Transition<T>> transitions = possibleTransitions(currIndex, false);
			Collection<Transition<T>> otherTransitions = accPath.possibleTransitions(otherIndex, false);

			if((currIndex >= accesses.length || (currIndex == accesses.length-1 && accesses[currIndex] instanceof SetOfPossibleFieldAccesses)) 
					&& otherIndex>=accPath.accesses.length-1) {
				if(transitions.isEmpty())
					return otherTransitions.isEmpty() && hasAtLeastTheSameExclusionsAs(accPath);
				for(Transition<T> transition : transitions) {
					for(Transition<T> otherTransition : otherTransitions) {
						MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);
						if(!match.hasMatched())
							return false;
					}	
				}
				return hasAtLeastTheSameExclusionsAs(accPath);
			}

			for(Transition<T> transition : transitions) {
				for(Transition<T> otherTransition : otherTransitions) {
					MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);
					if(match.hasMatched()) {
						if(otherIndex == otherTransition.transitionToIndex())
							continue;
						
						currIndex = transition.transitionToIndex();
						otherIndex = otherTransition.transitionToIndex();
						continue outer;
					}
				}
			}
			return false;
		}
	}
	
	private boolean hasAtLeastTheSameExclusionsAs(AccessPath<T> accPath) {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




411



		return accPath.exclusions.containsAll(exclusions);








subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015




412


413


414


415


416


417


418



	}

	public Collection<String> tokenize() {
		List<String> result = Lists.newLinkedList();
		for(SubAccessPath<T> s : accesses) {
			result.add(s.toString());
		}








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




419


420



		if(!exclusions.isEmpty())
			result.add("^"+Joiner.on(",").join(exclusions));








subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015




421


422



		return result;
	}








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




423


424


425


426




	public AccessPath<T> removeExclusions() {
		return new AccessPath<T>(accesses, Sets.<T>newHashSet());
	}








additional methods for AccessPath required by client FlowFunction

 


Johannes Lerch
committed
Feb 16, 2015




427


428


429


430


431


432


433


434


435


436


437


438


439


440


441


442


443


444


445


446




	public SubAccessPath<T> getFirstAccess() {
		return accesses[0];
	}

	public AccessPath<T> removeRepeatableFirstAccess(T field) {
		Collection<? extends T> elements = accesses[0].elements();
		if(!elements.contains(field))
			throw new IllegalArgumentException();
		
		if(elements.size() == 1) {
			return new AccessPath<>(Arrays.copyOfRange(accesses, 1, accesses.length), exclusions);
		}
		
		HashSet<T> newSet = Sets.newHashSet(elements);
		newSet.remove(field);
		SubAccessPath<T>[] newAccesses = Arrays.copyOf(accesses, accesses.length);
		newAccesses[0] = new SetOfPossibleFieldAccesses<>(newSet);
		return new AccessPath<T>(newAccesses, exclusions);
	}








cleaning code



Johannes Lerch
committed
Jan 07, 2015




447



}












Open sidebar



Joshua Garcia heros

b974b09ef56a83bbc26cf9960e46acbe7ebadd0e







Open sidebar



Joshua Garcia heros

b974b09ef56a83bbc26cf9960e46acbe7ebadd0e




Open sidebar

Joshua Garcia heros

b974b09ef56a83bbc26cf9960e46acbe7ebadd0e


Joshua Garciaherosheros
b974b09ef56a83bbc26cf9960e46acbe7ebadd0e










b974b09ef56a83bbc26cf9960e46acbe7ebadd0e


Switch branch/tag










heros


src


heros


alias


AccessPath.java



Find file
Normal viewHistoryPermalink






AccessPath.java



13.7 KB









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









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




13


14


15



import heros.alias.SubAccessPath.SetOfPossibleFieldAccesses;
import heros.alias.SubAccessPath.SpecificFieldAccess;
import heros.alias.Transition.MatchResult;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




16












cleaning code



Johannes Lerch
committed
Jan 07, 2015




17



import java.util.Arrays;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




18



import java.util.Collection;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




19



import java.util.HashSet;








subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015




20



import java.util.List;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




21


22



import java.util.Set;









remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




23



import com.google.common.base.Function;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




24



import com.google.common.base.Joiner;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




25



import com.google.common.collect.Lists;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




26


27


28



import com.google.common.collect.Sets;

@SuppressWarnings("unchecked")








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




29



public class AccessPath<T extends AccessPath.FieldRef<T>> {








cleaning code



Johannes Lerch
committed
Jan 07, 2015




30












merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




31


32


33


34


35



	public static interface FieldRef<F> {
		boolean shouldBeMergedWith(F fieldRef);
	}
	
	public static <T extends FieldRef<T>> AccessPath<T> empty() {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




36


37


38



		return new AccessPath<T>();
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




39



	private final SubAccessPath<T>[] accesses;








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




40



	private final Set<T> exclusions;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




41


42



	
	public AccessPath() {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




43



		accesses = new SubAccessPath[0];








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




44



		exclusions = Sets.newHashSet();








cleaning code



Johannes Lerch
committed
Jan 07, 2015




45


46



	}
	








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




47



	AccessPath(SubAccessPath<T>[] accesses, Set<T> exclusions) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




48


49



		this.accesses = accesses;
		this.exclusions = exclusions;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




50


51



	}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




52



	public boolean isAccessInExclusions(SubAccessPath<T>... fieldReferences) {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




53


54


55


56



		if(fieldReferences.length > 0) {
			for(T field : fieldReferences[0].elements()) {
				if(!exclusions.contains(field))
					return false;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




57


58



			}
			return true;








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




59



		}








cleaning code



Johannes Lerch
committed
Jan 07, 2015




60


61


62



		return false;
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




63



	public AccessPath<T> addFieldReference(SubAccessPath<T>... fieldReferences) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




64


65


66



		return addFieldReference(true, fieldReferences);
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




67



	AccessPath<T> addFieldReference(boolean merge, SubAccessPath<T>... fieldReferences) {








cleaning code



Johannes Lerch
committed
Jan 07, 2015




68



		if(isAccessInExclusions(fieldReferences))








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




69



			throw new IllegalArgumentException("FieldRef "+Arrays.toString(fieldReferences)+" cannot be added to "+toString());








cleaning code



Johannes Lerch
committed
Jan 07, 2015




70












regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




71


72


73



		if(merge) {
			for(int i=fieldReferences.length-1; i>=0; i--) {
				for(int j=0; j<accesses.length; j++) {








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




74



					if(accesses[j].shouldBeMerged(fieldReferences[i])) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




75


76


77


78


79


80



						// [..., {j-i}, ...]
						
						AccessPathBuilder builder = new AccessPathBuilder(j+fieldReferences.length-i);
						builder.keep(0, j);
						builder.merge(j, accesses.length).mergeWithLast(fieldReferences, 0, i);
						builder.append(fieldReferences, i+1, fieldReferences.length);








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




81



						builder.removeExclusions();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




82


83


84


85


86


87


88


89


90



						return builder.build();
					}
				}
			}
		}
		
		AccessPathBuilder builder = new AccessPathBuilder(accesses.length + fieldReferences.length);
		builder.keep(0, accesses.length);
		builder.append(fieldReferences, 0, fieldReferences.length);








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




91


92



		if(fieldReferences.length>0)
			builder.removeExclusions();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




93


94


95



		return builder.build();
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




96


97



	public AccessPath<T> addFieldReference(T... fieldReferences) {
		SubAccessPath<T>[] subPath = new SubAccessPath[fieldReferences.length];








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




98


99


100


101


102


103


104


105



		for(int i=0; i<fieldReferences.length; i++) {
			subPath[i] = new SpecificFieldAccess<>(fieldReferences[i]);
		}
		return addFieldReference(subPath);
	}
	
	private class AccessPathBuilder {
		








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




106



		private Set<T> newExclusions;








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




107



		private SubAccessPath<T>[] newAccesses;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




108


109


110


111


112


113


114



		private int currentIndex = 0;

		public AccessPathBuilder(int capacity) {
			newAccesses = new SubAccessPath[capacity];
			newExclusions = exclusions;
		}
		








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




115



		public AccessPath<T> build() {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




116


117


118



			return new AccessPath<>(newAccesses, newExclusions);
		}









remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




119


120



		public void removeExclusions() {
			newExclusions = Sets.newHashSet();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




121


122



		}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




123



		public void append(SubAccessPath<T>[] fieldReferences, int start, int endExcl) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




124


125


126


127



			for(int i=start; i<endExcl; i++) {
				newAccesses[currentIndex] = fieldReferences[i];
				currentIndex++;
				if(fieldReferences[i] instanceof SetOfPossibleFieldAccesses)








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




128



					removeExclusions();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




129


130


131


132



			}
			currentIndex+=endExcl-start;
		}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




133



		public void mergeWithLast(SubAccessPath<T>[] fieldReferences, int start, int endExcl) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




134


135


136


137



			newAccesses[currentIndex-1].merge(Arrays.copyOfRange(fieldReferences, start, endExcl));
		}

		public AccessPathBuilder merge(int srcIndex, int destIndexExcl) {








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




138



			Set<T> set = Sets.newHashSet();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




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



			for(int i=srcIndex; i<destIndexExcl; i++) {
				set.addAll(accesses[i].elements());
			}
			newAccesses[currentIndex] = new SubAccessPath.SetOfPossibleFieldAccesses<>(set);
			currentIndex++;
			return this;
		}

		public AccessPathBuilder keep(int srcIndex, int destIndexExcl) {
			System.arraycopy(accesses, srcIndex, newAccesses, currentIndex, destIndexExcl-srcIndex);
			currentIndex += destIndexExcl-srcIndex;
			return this;
		}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




153



		public void append(T fieldRef) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




154


155


156


157



			newAccesses[currentIndex] = new SubAccessPath.SpecificFieldAccess<>(fieldRef);
			currentIndex++;
		}
		








cleaning code



Johannes Lerch
committed
Jan 07, 2015




158


159



	}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




160



	public AccessPath<T> prepend(T fieldRef) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




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



		for(int j=0; j<accesses.length; j++) {
			if(accesses[j].contains(fieldRef)) {
				// [{0-j}, ...]
				
				AccessPathBuilder builder = new AccessPathBuilder(accesses.length-j);
				builder.merge(0, j+1);
				builder.keep(j+1, accesses.length);
				return builder.build();
			}
		}
		AccessPathBuilder builder = new AccessPathBuilder(accesses.length + 1);
		builder.append(fieldRef);
		builder.keep(0, accesses.length);
		return builder.build();








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




175


176



	}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




177



	public AccessPath<T> removeFirst(T field) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




178


179


180



		for(int i=0; i<accesses.length; i++) {
			if(accesses[i].contains(field)) {
				if(accesses[i] instanceof SpecificFieldAccess)








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




181



					return new AccessPath<T>(Arrays.copyOfRange(accesses, i+1, accesses.length), exclusions);








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




182


183


184


185


186


187


188


189


190


191



				else
					return this;
			}
			else if(accesses[i] instanceof SpecificFieldAccess)
				throw new IllegalStateException("Trying to remove "+field+" from "+this);
		}
		
		throw new IllegalStateException("Trying to remove "+field+" from "+this);
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




192



	public AccessPath<T> appendExcludedFieldReference(T... fieldReferences) {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




193


194


195



		HashSet<T> newExclusions = Sets.newHashSet(fieldReferences);
		newExclusions.addAll(exclusions);
		return new AccessPath<>(accesses, newExclusions);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




196



	}








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




197


198


199


200


201


202


203


204


205


206


207


208


209


210




	public static enum PrefixTestResult {
		GUARANTEED_PREFIX(2), POTENTIAL_PREFIX(1), NO_PREFIX(0);
		
		private int value;

		private PrefixTestResult(int value) {
			this.value = value;
		}
		
		public boolean atLeast(PrefixTestResult minimum) {
			return value >= minimum.value;
		}
	}








cleaning code



Johannes Lerch
committed
Jan 07, 2015




211



	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




212



	public PrefixTestResult isPrefixOf(AccessPath<T> accPath) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




213


214


215



		int currIndex = 0;
		int otherIndex = 0;
		PrefixTestResult result = PrefixTestResult.GUARANTEED_PREFIX;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




216



		








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




217


218



		int finalIndex = finalIndex();
		outer: while(currIndex < finalIndex) {








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




219


220



			Collection<Transition<T>> transitions = possibleTransitions(currIndex, true);
			Collection<Transition<T>> otherTransitions = accPath.possibleTransitions(otherIndex, true);








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




221












merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




222


223


224



			for(Transition<T> transition : transitions) {
				for(Transition<T> otherTransition : otherTransitions) {
					MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




225


226



					if(match.hasMatched()) {
						if(currIndex == transition.transitionToIndex() && otherIndex == otherTransition.transitionToIndex())








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




227



							continue;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




228


229


230


231


232


233


234


235


236


237



						
						currIndex = transition.transitionToIndex();
						otherIndex = otherTransition.transitionToIndex();
						if(!match.isGuaranteedMatch())
							result = PrefixTestResult.POTENTIAL_PREFIX;
						
						continue outer;
					}
				}
			}








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




238



			return PrefixTestResult.NO_PREFIX;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




239



		}








cleaning code



Johannes Lerch
committed
Jan 07, 2015




240



		








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




241


242


243


244



		return result;
	}
	
	private int finalIndex() {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




245


246



		if(!exclusions.isEmpty())
			return accesses.length + 1;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




247


248



		
		int finalIndex = 0;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




249



		for(int i=0; i<accesses.length; i++) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




250


251



			if(accesses[i] instanceof SpecificFieldAccess)
				finalIndex = i+1;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




252



		}








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




253


254


255



		return finalIndex;
	}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




256


257



	private Collection<Transition<T>> possibleTransitions(int index, boolean addExclusionTransitions) {
		Collection<Transition<T>> result = Lists.newLinkedList();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




258


259


260



		if(index < accesses.length) {
			if(accesses[index] instanceof SetOfPossibleFieldAccesses) {
				result.add(new Transition.SubAccessPathTransition<>(index, accesses[index]));








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




261



				result.addAll(possibleTransitions(index+1, addExclusionTransitions));








cleaning code



Johannes Lerch
committed
Jan 07, 2015




262



			}








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




263


264



			else
				result.add(new Transition.SubAccessPathTransition<>(index+1, accesses[index]));








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




265


266



		} else if(addExclusionTransitions && index - accesses.length == 0 && !exclusions.isEmpty()) {
			result.add(new Transition.ExclusionPathTransition<T>(index+1, exclusions));








cleaning code



Johannes Lerch
committed
Jan 07, 2015




267



		}








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




268



		return result;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




269


270



	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




271


272


273



	public SubAccessPath<T>[] getDeltaTo(AccessPath<T> accPath) {
		int currIndex = 0;
		int otherIndex = 0;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




274



		








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




275


276


277


278


279


280


281


282


283


284


285


286


287


288


289



		outer: while(true) {
			Collection<Transition<T>> transitions = possibleTransitions(currIndex, false);
			Collection<Transition<T>> otherTransitions = accPath.possibleTransitions(otherIndex, false);

			for(Transition<T> transition : transitions) {
				for(Transition<T> otherTransition : otherTransitions) {
					MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);
					if(match.hasMatched()) {
						if(currIndex == transition.transitionToIndex() && otherIndex == otherTransition.transitionToIndex())
							continue;
						
						currIndex = transition.transitionToIndex();
						otherIndex = otherTransition.transitionToIndex();
						continue outer;
					}








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




290


291



				}
			}








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




292



			break;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




293


294



		}
		








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




295



		return Arrays.copyOfRange(accPath.accesses, otherIndex, accPath.accesses.length);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




296


297



	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




298



	public AccessPath<T> mergeExcludedFieldReferences(AccessPath<T> accPath) {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




299


300


301



		HashSet<T> newExclusions = Sets.newHashSet(exclusions);
		newExclusions.addAll(accPath.exclusions);
		return new AccessPath<>(accesses, newExclusions);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




302


303



	}
	








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




304


305


306


307



	public boolean mayHaveEmptyAccessPath() {
		return finalIndex() == 0;
	}
	








cleaning code



Johannes Lerch
committed
Jan 07, 2015




308



	public boolean isEmpty() {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




309



		return exclusions.isEmpty() && accesses.length == 0;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




310


311


312


313


314


315


316



	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(accesses);








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




317



		result = prime * result + ((exclusions == null) ? 0 : exclusions.hashCode());








cleaning code



Johannes Lerch
committed
Jan 07, 2015




318


319


320


321


322


323


324


325


326


327


328


329


330


331



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








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




332


333


334


335



		if (exclusions == null) {
			if (other.exclusions != null)
				return false;
		} else if (!exclusions.equals(other.exclusions))








cleaning code



Johannes Lerch
committed
Jan 07, 2015




336


337


338


339


340


341


342



			return false;
		return true;
	}

	@Override
	public String toString() {
		String result = accesses.length > 0 ? "."+Joiner.on(".").join(accesses) : "";








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




343


344



		if(!exclusions.isEmpty())
			result += "^" + Joiner.on(",").join(exclusions);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




345


346


347



		return result;
	}
	








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




348


349


350


351



	public <U extends FieldRef<U>> AccessPath<U> map(Function<T, U> function) {
		SubAccessPath<U>[] newAccesses = new SubAccessPath[accesses.length];
		for(int i=0; i<accesses.length; i++) {
			newAccesses[i] = accesses[i].map(function);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




352



		}








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




353


354


355


356



		Set<U> newExclusions = Sets.newHashSet();
		for(T f : exclusions)
			newExclusions.add(function.apply(f));
		return new AccessPath<U>(newAccesses, newExclusions);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




357



	}








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




358



	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




359



	public AccessPath<T> removeAnyAccess() {








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




360



		if(accesses.length > 0)








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




361



			return new AccessPath<T>(new SubAccessPath[0], exclusions);








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




362


363


364


365


366


367



		else
			return this;
	}

	public boolean hasEmptyAccessPath() {
		return accesses.length == 0;








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




368



	}








subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015




369


370


371


372


373


374


375


376


377


378


379


380


381


382


383


384


385


386


387


388


389


390


391


392


393


394


395


396


397


398


399


400


401


402


403


404


405


406


407


408


409


410




	public boolean subsumes(AccessPath<T> accPath) {
		int currIndex = 0;
		int otherIndex = 0;
		
		
		outer: while(true) {
			Collection<Transition<T>> transitions = possibleTransitions(currIndex, false);
			Collection<Transition<T>> otherTransitions = accPath.possibleTransitions(otherIndex, false);

			if((currIndex >= accesses.length || (currIndex == accesses.length-1 && accesses[currIndex] instanceof SetOfPossibleFieldAccesses)) 
					&& otherIndex>=accPath.accesses.length-1) {
				if(transitions.isEmpty())
					return otherTransitions.isEmpty() && hasAtLeastTheSameExclusionsAs(accPath);
				for(Transition<T> transition : transitions) {
					for(Transition<T> otherTransition : otherTransitions) {
						MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);
						if(!match.hasMatched())
							return false;
					}	
				}
				return hasAtLeastTheSameExclusionsAs(accPath);
			}

			for(Transition<T> transition : transitions) {
				for(Transition<T> otherTransition : otherTransitions) {
					MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);
					if(match.hasMatched()) {
						if(otherIndex == otherTransition.transitionToIndex())
							continue;
						
						currIndex = transition.transitionToIndex();
						otherIndex = otherTransition.transitionToIndex();
						continue outer;
					}
				}
			}
			return false;
		}
	}
	
	private boolean hasAtLeastTheSameExclusionsAs(AccessPath<T> accPath) {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




411



		return accPath.exclusions.containsAll(exclusions);








subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015




412


413


414


415


416


417


418



	}

	public Collection<String> tokenize() {
		List<String> result = Lists.newLinkedList();
		for(SubAccessPath<T> s : accesses) {
			result.add(s.toString());
		}








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




419


420



		if(!exclusions.isEmpty())
			result.add("^"+Joiner.on(",").join(exclusions));








subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015




421


422



		return result;
	}








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




423


424


425


426




	public AccessPath<T> removeExclusions() {
		return new AccessPath<T>(accesses, Sets.<T>newHashSet());
	}








additional methods for AccessPath required by client FlowFunction

 


Johannes Lerch
committed
Feb 16, 2015




427


428


429


430


431


432


433


434


435


436


437


438


439


440


441


442


443


444


445


446




	public SubAccessPath<T> getFirstAccess() {
		return accesses[0];
	}

	public AccessPath<T> removeRepeatableFirstAccess(T field) {
		Collection<? extends T> elements = accesses[0].elements();
		if(!elements.contains(field))
			throw new IllegalArgumentException();
		
		if(elements.size() == 1) {
			return new AccessPath<>(Arrays.copyOfRange(accesses, 1, accesses.length), exclusions);
		}
		
		HashSet<T> newSet = Sets.newHashSet(elements);
		newSet.remove(field);
		SubAccessPath<T>[] newAccesses = Arrays.copyOf(accesses, accesses.length);
		newAccesses[0] = new SetOfPossibleFieldAccesses<>(newSet);
		return new AccessPath<T>(newAccesses, exclusions);
	}








cleaning code



Johannes Lerch
committed
Jan 07, 2015




447



}















b974b09ef56a83bbc26cf9960e46acbe7ebadd0e


Switch branch/tag










heros


src


heros


alias


AccessPath.java



Find file
Normal viewHistoryPermalink






AccessPath.java



13.7 KB









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









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




13


14


15



import heros.alias.SubAccessPath.SetOfPossibleFieldAccesses;
import heros.alias.SubAccessPath.SpecificFieldAccess;
import heros.alias.Transition.MatchResult;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




16












cleaning code



Johannes Lerch
committed
Jan 07, 2015




17



import java.util.Arrays;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




18



import java.util.Collection;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




19



import java.util.HashSet;








subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015




20



import java.util.List;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




21


22



import java.util.Set;









remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




23



import com.google.common.base.Function;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




24



import com.google.common.base.Joiner;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




25



import com.google.common.collect.Lists;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




26


27


28



import com.google.common.collect.Sets;

@SuppressWarnings("unchecked")








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




29



public class AccessPath<T extends AccessPath.FieldRef<T>> {








cleaning code



Johannes Lerch
committed
Jan 07, 2015




30












merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




31


32


33


34


35



	public static interface FieldRef<F> {
		boolean shouldBeMergedWith(F fieldRef);
	}
	
	public static <T extends FieldRef<T>> AccessPath<T> empty() {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




36


37


38



		return new AccessPath<T>();
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




39



	private final SubAccessPath<T>[] accesses;








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




40



	private final Set<T> exclusions;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




41


42



	
	public AccessPath() {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




43



		accesses = new SubAccessPath[0];








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




44



		exclusions = Sets.newHashSet();








cleaning code



Johannes Lerch
committed
Jan 07, 2015




45


46



	}
	








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




47



	AccessPath(SubAccessPath<T>[] accesses, Set<T> exclusions) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




48


49



		this.accesses = accesses;
		this.exclusions = exclusions;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




50


51



	}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




52



	public boolean isAccessInExclusions(SubAccessPath<T>... fieldReferences) {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




53


54


55


56



		if(fieldReferences.length > 0) {
			for(T field : fieldReferences[0].elements()) {
				if(!exclusions.contains(field))
					return false;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




57


58



			}
			return true;








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




59



		}








cleaning code



Johannes Lerch
committed
Jan 07, 2015




60


61


62



		return false;
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




63



	public AccessPath<T> addFieldReference(SubAccessPath<T>... fieldReferences) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




64


65


66



		return addFieldReference(true, fieldReferences);
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




67



	AccessPath<T> addFieldReference(boolean merge, SubAccessPath<T>... fieldReferences) {








cleaning code



Johannes Lerch
committed
Jan 07, 2015




68



		if(isAccessInExclusions(fieldReferences))








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




69



			throw new IllegalArgumentException("FieldRef "+Arrays.toString(fieldReferences)+" cannot be added to "+toString());








cleaning code



Johannes Lerch
committed
Jan 07, 2015




70












regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




71


72


73



		if(merge) {
			for(int i=fieldReferences.length-1; i>=0; i--) {
				for(int j=0; j<accesses.length; j++) {








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




74



					if(accesses[j].shouldBeMerged(fieldReferences[i])) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




75


76


77


78


79


80



						// [..., {j-i}, ...]
						
						AccessPathBuilder builder = new AccessPathBuilder(j+fieldReferences.length-i);
						builder.keep(0, j);
						builder.merge(j, accesses.length).mergeWithLast(fieldReferences, 0, i);
						builder.append(fieldReferences, i+1, fieldReferences.length);








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




81



						builder.removeExclusions();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




82


83


84


85


86


87


88


89


90



						return builder.build();
					}
				}
			}
		}
		
		AccessPathBuilder builder = new AccessPathBuilder(accesses.length + fieldReferences.length);
		builder.keep(0, accesses.length);
		builder.append(fieldReferences, 0, fieldReferences.length);








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




91


92



		if(fieldReferences.length>0)
			builder.removeExclusions();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




93


94


95



		return builder.build();
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




96


97



	public AccessPath<T> addFieldReference(T... fieldReferences) {
		SubAccessPath<T>[] subPath = new SubAccessPath[fieldReferences.length];








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




98


99


100


101


102


103


104


105



		for(int i=0; i<fieldReferences.length; i++) {
			subPath[i] = new SpecificFieldAccess<>(fieldReferences[i]);
		}
		return addFieldReference(subPath);
	}
	
	private class AccessPathBuilder {
		








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




106



		private Set<T> newExclusions;








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




107



		private SubAccessPath<T>[] newAccesses;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




108


109


110


111


112


113


114



		private int currentIndex = 0;

		public AccessPathBuilder(int capacity) {
			newAccesses = new SubAccessPath[capacity];
			newExclusions = exclusions;
		}
		








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




115



		public AccessPath<T> build() {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




116


117


118



			return new AccessPath<>(newAccesses, newExclusions);
		}









remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




119


120



		public void removeExclusions() {
			newExclusions = Sets.newHashSet();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




121


122



		}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




123



		public void append(SubAccessPath<T>[] fieldReferences, int start, int endExcl) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




124


125


126


127



			for(int i=start; i<endExcl; i++) {
				newAccesses[currentIndex] = fieldReferences[i];
				currentIndex++;
				if(fieldReferences[i] instanceof SetOfPossibleFieldAccesses)








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




128



					removeExclusions();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




129


130


131


132



			}
			currentIndex+=endExcl-start;
		}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




133



		public void mergeWithLast(SubAccessPath<T>[] fieldReferences, int start, int endExcl) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




134


135


136


137



			newAccesses[currentIndex-1].merge(Arrays.copyOfRange(fieldReferences, start, endExcl));
		}

		public AccessPathBuilder merge(int srcIndex, int destIndexExcl) {








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




138



			Set<T> set = Sets.newHashSet();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




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



			for(int i=srcIndex; i<destIndexExcl; i++) {
				set.addAll(accesses[i].elements());
			}
			newAccesses[currentIndex] = new SubAccessPath.SetOfPossibleFieldAccesses<>(set);
			currentIndex++;
			return this;
		}

		public AccessPathBuilder keep(int srcIndex, int destIndexExcl) {
			System.arraycopy(accesses, srcIndex, newAccesses, currentIndex, destIndexExcl-srcIndex);
			currentIndex += destIndexExcl-srcIndex;
			return this;
		}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




153



		public void append(T fieldRef) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




154


155


156


157



			newAccesses[currentIndex] = new SubAccessPath.SpecificFieldAccess<>(fieldRef);
			currentIndex++;
		}
		








cleaning code



Johannes Lerch
committed
Jan 07, 2015




158


159



	}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




160



	public AccessPath<T> prepend(T fieldRef) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




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



		for(int j=0; j<accesses.length; j++) {
			if(accesses[j].contains(fieldRef)) {
				// [{0-j}, ...]
				
				AccessPathBuilder builder = new AccessPathBuilder(accesses.length-j);
				builder.merge(0, j+1);
				builder.keep(j+1, accesses.length);
				return builder.build();
			}
		}
		AccessPathBuilder builder = new AccessPathBuilder(accesses.length + 1);
		builder.append(fieldRef);
		builder.keep(0, accesses.length);
		return builder.build();








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




175


176



	}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




177



	public AccessPath<T> removeFirst(T field) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




178


179


180



		for(int i=0; i<accesses.length; i++) {
			if(accesses[i].contains(field)) {
				if(accesses[i] instanceof SpecificFieldAccess)








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




181



					return new AccessPath<T>(Arrays.copyOfRange(accesses, i+1, accesses.length), exclusions);








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




182


183


184


185


186


187


188


189


190


191



				else
					return this;
			}
			else if(accesses[i] instanceof SpecificFieldAccess)
				throw new IllegalStateException("Trying to remove "+field+" from "+this);
		}
		
		throw new IllegalStateException("Trying to remove "+field+" from "+this);
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




192



	public AccessPath<T> appendExcludedFieldReference(T... fieldReferences) {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




193


194


195



		HashSet<T> newExclusions = Sets.newHashSet(fieldReferences);
		newExclusions.addAll(exclusions);
		return new AccessPath<>(accesses, newExclusions);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




196



	}








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




197


198


199


200


201


202


203


204


205


206


207


208


209


210




	public static enum PrefixTestResult {
		GUARANTEED_PREFIX(2), POTENTIAL_PREFIX(1), NO_PREFIX(0);
		
		private int value;

		private PrefixTestResult(int value) {
			this.value = value;
		}
		
		public boolean atLeast(PrefixTestResult minimum) {
			return value >= minimum.value;
		}
	}








cleaning code



Johannes Lerch
committed
Jan 07, 2015




211



	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




212



	public PrefixTestResult isPrefixOf(AccessPath<T> accPath) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




213


214


215



		int currIndex = 0;
		int otherIndex = 0;
		PrefixTestResult result = PrefixTestResult.GUARANTEED_PREFIX;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




216



		








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




217


218



		int finalIndex = finalIndex();
		outer: while(currIndex < finalIndex) {








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




219


220



			Collection<Transition<T>> transitions = possibleTransitions(currIndex, true);
			Collection<Transition<T>> otherTransitions = accPath.possibleTransitions(otherIndex, true);








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




221












merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




222


223


224



			for(Transition<T> transition : transitions) {
				for(Transition<T> otherTransition : otherTransitions) {
					MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




225


226



					if(match.hasMatched()) {
						if(currIndex == transition.transitionToIndex() && otherIndex == otherTransition.transitionToIndex())








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




227



							continue;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




228


229


230


231


232


233


234


235


236


237



						
						currIndex = transition.transitionToIndex();
						otherIndex = otherTransition.transitionToIndex();
						if(!match.isGuaranteedMatch())
							result = PrefixTestResult.POTENTIAL_PREFIX;
						
						continue outer;
					}
				}
			}








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




238



			return PrefixTestResult.NO_PREFIX;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




239



		}








cleaning code



Johannes Lerch
committed
Jan 07, 2015




240



		








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




241


242


243


244



		return result;
	}
	
	private int finalIndex() {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




245


246



		if(!exclusions.isEmpty())
			return accesses.length + 1;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




247


248



		
		int finalIndex = 0;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




249



		for(int i=0; i<accesses.length; i++) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




250


251



			if(accesses[i] instanceof SpecificFieldAccess)
				finalIndex = i+1;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




252



		}








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




253


254


255



		return finalIndex;
	}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




256


257



	private Collection<Transition<T>> possibleTransitions(int index, boolean addExclusionTransitions) {
		Collection<Transition<T>> result = Lists.newLinkedList();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




258


259


260



		if(index < accesses.length) {
			if(accesses[index] instanceof SetOfPossibleFieldAccesses) {
				result.add(new Transition.SubAccessPathTransition<>(index, accesses[index]));








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




261



				result.addAll(possibleTransitions(index+1, addExclusionTransitions));








cleaning code



Johannes Lerch
committed
Jan 07, 2015




262



			}








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




263


264



			else
				result.add(new Transition.SubAccessPathTransition<>(index+1, accesses[index]));








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




265


266



		} else if(addExclusionTransitions && index - accesses.length == 0 && !exclusions.isEmpty()) {
			result.add(new Transition.ExclusionPathTransition<T>(index+1, exclusions));








cleaning code



Johannes Lerch
committed
Jan 07, 2015




267



		}








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




268



		return result;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




269


270



	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




271


272


273



	public SubAccessPath<T>[] getDeltaTo(AccessPath<T> accPath) {
		int currIndex = 0;
		int otherIndex = 0;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




274



		








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




275


276


277


278


279


280


281


282


283


284


285


286


287


288


289



		outer: while(true) {
			Collection<Transition<T>> transitions = possibleTransitions(currIndex, false);
			Collection<Transition<T>> otherTransitions = accPath.possibleTransitions(otherIndex, false);

			for(Transition<T> transition : transitions) {
				for(Transition<T> otherTransition : otherTransitions) {
					MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);
					if(match.hasMatched()) {
						if(currIndex == transition.transitionToIndex() && otherIndex == otherTransition.transitionToIndex())
							continue;
						
						currIndex = transition.transitionToIndex();
						otherIndex = otherTransition.transitionToIndex();
						continue outer;
					}








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




290


291



				}
			}








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




292



			break;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




293


294



		}
		








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




295



		return Arrays.copyOfRange(accPath.accesses, otherIndex, accPath.accesses.length);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




296


297



	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




298



	public AccessPath<T> mergeExcludedFieldReferences(AccessPath<T> accPath) {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




299


300


301



		HashSet<T> newExclusions = Sets.newHashSet(exclusions);
		newExclusions.addAll(accPath.exclusions);
		return new AccessPath<>(accesses, newExclusions);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




302


303



	}
	








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




304


305


306


307



	public boolean mayHaveEmptyAccessPath() {
		return finalIndex() == 0;
	}
	








cleaning code



Johannes Lerch
committed
Jan 07, 2015




308



	public boolean isEmpty() {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




309



		return exclusions.isEmpty() && accesses.length == 0;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




310


311


312


313


314


315


316



	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(accesses);








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




317



		result = prime * result + ((exclusions == null) ? 0 : exclusions.hashCode());








cleaning code



Johannes Lerch
committed
Jan 07, 2015




318


319


320


321


322


323


324


325


326


327


328


329


330


331



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








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




332


333


334


335



		if (exclusions == null) {
			if (other.exclusions != null)
				return false;
		} else if (!exclusions.equals(other.exclusions))








cleaning code



Johannes Lerch
committed
Jan 07, 2015




336


337


338


339


340


341


342



			return false;
		return true;
	}

	@Override
	public String toString() {
		String result = accesses.length > 0 ? "."+Joiner.on(".").join(accesses) : "";








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




343


344



		if(!exclusions.isEmpty())
			result += "^" + Joiner.on(",").join(exclusions);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




345


346


347



		return result;
	}
	








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




348


349


350


351



	public <U extends FieldRef<U>> AccessPath<U> map(Function<T, U> function) {
		SubAccessPath<U>[] newAccesses = new SubAccessPath[accesses.length];
		for(int i=0; i<accesses.length; i++) {
			newAccesses[i] = accesses[i].map(function);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




352



		}








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




353


354


355


356



		Set<U> newExclusions = Sets.newHashSet();
		for(T f : exclusions)
			newExclusions.add(function.apply(f));
		return new AccessPath<U>(newAccesses, newExclusions);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




357



	}








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




358



	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




359



	public AccessPath<T> removeAnyAccess() {








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




360



		if(accesses.length > 0)








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




361



			return new AccessPath<T>(new SubAccessPath[0], exclusions);








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




362


363


364


365


366


367



		else
			return this;
	}

	public boolean hasEmptyAccessPath() {
		return accesses.length == 0;








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




368



	}








subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015




369


370


371


372


373


374


375


376


377


378


379


380


381


382


383


384


385


386


387


388


389


390


391


392


393


394


395


396


397


398


399


400


401


402


403


404


405


406


407


408


409


410




	public boolean subsumes(AccessPath<T> accPath) {
		int currIndex = 0;
		int otherIndex = 0;
		
		
		outer: while(true) {
			Collection<Transition<T>> transitions = possibleTransitions(currIndex, false);
			Collection<Transition<T>> otherTransitions = accPath.possibleTransitions(otherIndex, false);

			if((currIndex >= accesses.length || (currIndex == accesses.length-1 && accesses[currIndex] instanceof SetOfPossibleFieldAccesses)) 
					&& otherIndex>=accPath.accesses.length-1) {
				if(transitions.isEmpty())
					return otherTransitions.isEmpty() && hasAtLeastTheSameExclusionsAs(accPath);
				for(Transition<T> transition : transitions) {
					for(Transition<T> otherTransition : otherTransitions) {
						MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);
						if(!match.hasMatched())
							return false;
					}	
				}
				return hasAtLeastTheSameExclusionsAs(accPath);
			}

			for(Transition<T> transition : transitions) {
				for(Transition<T> otherTransition : otherTransitions) {
					MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);
					if(match.hasMatched()) {
						if(otherIndex == otherTransition.transitionToIndex())
							continue;
						
						currIndex = transition.transitionToIndex();
						otherIndex = otherTransition.transitionToIndex();
						continue outer;
					}
				}
			}
			return false;
		}
	}
	
	private boolean hasAtLeastTheSameExclusionsAs(AccessPath<T> accPath) {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




411



		return accPath.exclusions.containsAll(exclusions);








subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015




412


413


414


415


416


417


418



	}

	public Collection<String> tokenize() {
		List<String> result = Lists.newLinkedList();
		for(SubAccessPath<T> s : accesses) {
			result.add(s.toString());
		}








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




419


420



		if(!exclusions.isEmpty())
			result.add("^"+Joiner.on(",").join(exclusions));








subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015




421


422



		return result;
	}








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




423


424


425


426




	public AccessPath<T> removeExclusions() {
		return new AccessPath<T>(accesses, Sets.<T>newHashSet());
	}








additional methods for AccessPath required by client FlowFunction

 


Johannes Lerch
committed
Feb 16, 2015




427


428


429


430


431


432


433


434


435


436


437


438


439


440


441


442


443


444


445


446




	public SubAccessPath<T> getFirstAccess() {
		return accesses[0];
	}

	public AccessPath<T> removeRepeatableFirstAccess(T field) {
		Collection<? extends T> elements = accesses[0].elements();
		if(!elements.contains(field))
			throw new IllegalArgumentException();
		
		if(elements.size() == 1) {
			return new AccessPath<>(Arrays.copyOfRange(accesses, 1, accesses.length), exclusions);
		}
		
		HashSet<T> newSet = Sets.newHashSet(elements);
		newSet.remove(field);
		SubAccessPath<T>[] newAccesses = Arrays.copyOf(accesses, accesses.length);
		newAccesses[0] = new SetOfPossibleFieldAccesses<>(newSet);
		return new AccessPath<T>(newAccesses, exclusions);
	}








cleaning code



Johannes Lerch
committed
Jan 07, 2015




447



}











b974b09ef56a83bbc26cf9960e46acbe7ebadd0e


Switch branch/tag










heros


src


heros


alias


AccessPath.java



Find file
Normal viewHistoryPermalink




b974b09ef56a83bbc26cf9960e46acbe7ebadd0e


Switch branch/tag










heros


src


heros


alias


AccessPath.java





b974b09ef56a83bbc26cf9960e46acbe7ebadd0e


Switch branch/tag








b974b09ef56a83bbc26cf9960e46acbe7ebadd0e


Switch branch/tag





b974b09ef56a83bbc26cf9960e46acbe7ebadd0e

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



13.7 KB









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









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




13


14


15



import heros.alias.SubAccessPath.SetOfPossibleFieldAccesses;
import heros.alias.SubAccessPath.SpecificFieldAccess;
import heros.alias.Transition.MatchResult;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




16












cleaning code



Johannes Lerch
committed
Jan 07, 2015




17



import java.util.Arrays;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




18



import java.util.Collection;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




19



import java.util.HashSet;








subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015




20



import java.util.List;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




21


22



import java.util.Set;









remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




23



import com.google.common.base.Function;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




24



import com.google.common.base.Joiner;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




25



import com.google.common.collect.Lists;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




26


27


28



import com.google.common.collect.Sets;

@SuppressWarnings("unchecked")








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




29



public class AccessPath<T extends AccessPath.FieldRef<T>> {








cleaning code



Johannes Lerch
committed
Jan 07, 2015




30












merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




31


32


33


34


35



	public static interface FieldRef<F> {
		boolean shouldBeMergedWith(F fieldRef);
	}
	
	public static <T extends FieldRef<T>> AccessPath<T> empty() {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




36


37


38



		return new AccessPath<T>();
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




39



	private final SubAccessPath<T>[] accesses;








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




40



	private final Set<T> exclusions;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




41


42



	
	public AccessPath() {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




43



		accesses = new SubAccessPath[0];








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




44



		exclusions = Sets.newHashSet();








cleaning code



Johannes Lerch
committed
Jan 07, 2015




45


46



	}
	








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




47



	AccessPath(SubAccessPath<T>[] accesses, Set<T> exclusions) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




48


49



		this.accesses = accesses;
		this.exclusions = exclusions;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




50


51



	}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




52



	public boolean isAccessInExclusions(SubAccessPath<T>... fieldReferences) {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




53


54


55


56



		if(fieldReferences.length > 0) {
			for(T field : fieldReferences[0].elements()) {
				if(!exclusions.contains(field))
					return false;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




57


58



			}
			return true;








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




59



		}








cleaning code



Johannes Lerch
committed
Jan 07, 2015




60


61


62



		return false;
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




63



	public AccessPath<T> addFieldReference(SubAccessPath<T>... fieldReferences) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




64


65


66



		return addFieldReference(true, fieldReferences);
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




67



	AccessPath<T> addFieldReference(boolean merge, SubAccessPath<T>... fieldReferences) {








cleaning code



Johannes Lerch
committed
Jan 07, 2015




68



		if(isAccessInExclusions(fieldReferences))








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




69



			throw new IllegalArgumentException("FieldRef "+Arrays.toString(fieldReferences)+" cannot be added to "+toString());








cleaning code



Johannes Lerch
committed
Jan 07, 2015




70












regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




71


72


73



		if(merge) {
			for(int i=fieldReferences.length-1; i>=0; i--) {
				for(int j=0; j<accesses.length; j++) {








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




74



					if(accesses[j].shouldBeMerged(fieldReferences[i])) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




75


76


77


78


79


80



						// [..., {j-i}, ...]
						
						AccessPathBuilder builder = new AccessPathBuilder(j+fieldReferences.length-i);
						builder.keep(0, j);
						builder.merge(j, accesses.length).mergeWithLast(fieldReferences, 0, i);
						builder.append(fieldReferences, i+1, fieldReferences.length);








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




81



						builder.removeExclusions();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




82


83


84


85


86


87


88


89


90



						return builder.build();
					}
				}
			}
		}
		
		AccessPathBuilder builder = new AccessPathBuilder(accesses.length + fieldReferences.length);
		builder.keep(0, accesses.length);
		builder.append(fieldReferences, 0, fieldReferences.length);








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




91


92



		if(fieldReferences.length>0)
			builder.removeExclusions();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




93


94


95



		return builder.build();
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




96


97



	public AccessPath<T> addFieldReference(T... fieldReferences) {
		SubAccessPath<T>[] subPath = new SubAccessPath[fieldReferences.length];








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




98


99


100


101


102


103


104


105



		for(int i=0; i<fieldReferences.length; i++) {
			subPath[i] = new SpecificFieldAccess<>(fieldReferences[i]);
		}
		return addFieldReference(subPath);
	}
	
	private class AccessPathBuilder {
		








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




106



		private Set<T> newExclusions;








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




107



		private SubAccessPath<T>[] newAccesses;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




108


109


110


111


112


113


114



		private int currentIndex = 0;

		public AccessPathBuilder(int capacity) {
			newAccesses = new SubAccessPath[capacity];
			newExclusions = exclusions;
		}
		








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




115



		public AccessPath<T> build() {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




116


117


118



			return new AccessPath<>(newAccesses, newExclusions);
		}









remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




119


120



		public void removeExclusions() {
			newExclusions = Sets.newHashSet();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




121


122



		}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




123



		public void append(SubAccessPath<T>[] fieldReferences, int start, int endExcl) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




124


125


126


127



			for(int i=start; i<endExcl; i++) {
				newAccesses[currentIndex] = fieldReferences[i];
				currentIndex++;
				if(fieldReferences[i] instanceof SetOfPossibleFieldAccesses)








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




128



					removeExclusions();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




129


130


131


132



			}
			currentIndex+=endExcl-start;
		}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




133



		public void mergeWithLast(SubAccessPath<T>[] fieldReferences, int start, int endExcl) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




134


135


136


137



			newAccesses[currentIndex-1].merge(Arrays.copyOfRange(fieldReferences, start, endExcl));
		}

		public AccessPathBuilder merge(int srcIndex, int destIndexExcl) {








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




138



			Set<T> set = Sets.newHashSet();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




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



			for(int i=srcIndex; i<destIndexExcl; i++) {
				set.addAll(accesses[i].elements());
			}
			newAccesses[currentIndex] = new SubAccessPath.SetOfPossibleFieldAccesses<>(set);
			currentIndex++;
			return this;
		}

		public AccessPathBuilder keep(int srcIndex, int destIndexExcl) {
			System.arraycopy(accesses, srcIndex, newAccesses, currentIndex, destIndexExcl-srcIndex);
			currentIndex += destIndexExcl-srcIndex;
			return this;
		}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




153



		public void append(T fieldRef) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




154


155


156


157



			newAccesses[currentIndex] = new SubAccessPath.SpecificFieldAccess<>(fieldRef);
			currentIndex++;
		}
		








cleaning code



Johannes Lerch
committed
Jan 07, 2015




158


159



	}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




160



	public AccessPath<T> prepend(T fieldRef) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




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



		for(int j=0; j<accesses.length; j++) {
			if(accesses[j].contains(fieldRef)) {
				// [{0-j}, ...]
				
				AccessPathBuilder builder = new AccessPathBuilder(accesses.length-j);
				builder.merge(0, j+1);
				builder.keep(j+1, accesses.length);
				return builder.build();
			}
		}
		AccessPathBuilder builder = new AccessPathBuilder(accesses.length + 1);
		builder.append(fieldRef);
		builder.keep(0, accesses.length);
		return builder.build();








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




175


176



	}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




177



	public AccessPath<T> removeFirst(T field) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




178


179


180



		for(int i=0; i<accesses.length; i++) {
			if(accesses[i].contains(field)) {
				if(accesses[i] instanceof SpecificFieldAccess)








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




181



					return new AccessPath<T>(Arrays.copyOfRange(accesses, i+1, accesses.length), exclusions);








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




182


183


184


185


186


187


188


189


190


191



				else
					return this;
			}
			else if(accesses[i] instanceof SpecificFieldAccess)
				throw new IllegalStateException("Trying to remove "+field+" from "+this);
		}
		
		throw new IllegalStateException("Trying to remove "+field+" from "+this);
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




192



	public AccessPath<T> appendExcludedFieldReference(T... fieldReferences) {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




193


194


195



		HashSet<T> newExclusions = Sets.newHashSet(fieldReferences);
		newExclusions.addAll(exclusions);
		return new AccessPath<>(accesses, newExclusions);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




196



	}








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




197


198


199


200


201


202


203


204


205


206


207


208


209


210




	public static enum PrefixTestResult {
		GUARANTEED_PREFIX(2), POTENTIAL_PREFIX(1), NO_PREFIX(0);
		
		private int value;

		private PrefixTestResult(int value) {
			this.value = value;
		}
		
		public boolean atLeast(PrefixTestResult minimum) {
			return value >= minimum.value;
		}
	}








cleaning code



Johannes Lerch
committed
Jan 07, 2015




211



	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




212



	public PrefixTestResult isPrefixOf(AccessPath<T> accPath) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




213


214


215



		int currIndex = 0;
		int otherIndex = 0;
		PrefixTestResult result = PrefixTestResult.GUARANTEED_PREFIX;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




216



		








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




217


218



		int finalIndex = finalIndex();
		outer: while(currIndex < finalIndex) {








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




219


220



			Collection<Transition<T>> transitions = possibleTransitions(currIndex, true);
			Collection<Transition<T>> otherTransitions = accPath.possibleTransitions(otherIndex, true);








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




221












merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




222


223


224



			for(Transition<T> transition : transitions) {
				for(Transition<T> otherTransition : otherTransitions) {
					MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




225


226



					if(match.hasMatched()) {
						if(currIndex == transition.transitionToIndex() && otherIndex == otherTransition.transitionToIndex())








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




227



							continue;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




228


229


230


231


232


233


234


235


236


237



						
						currIndex = transition.transitionToIndex();
						otherIndex = otherTransition.transitionToIndex();
						if(!match.isGuaranteedMatch())
							result = PrefixTestResult.POTENTIAL_PREFIX;
						
						continue outer;
					}
				}
			}








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




238



			return PrefixTestResult.NO_PREFIX;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




239



		}








cleaning code



Johannes Lerch
committed
Jan 07, 2015




240



		








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




241


242


243


244



		return result;
	}
	
	private int finalIndex() {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




245


246



		if(!exclusions.isEmpty())
			return accesses.length + 1;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




247


248



		
		int finalIndex = 0;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




249



		for(int i=0; i<accesses.length; i++) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




250


251



			if(accesses[i] instanceof SpecificFieldAccess)
				finalIndex = i+1;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




252



		}








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




253


254


255



		return finalIndex;
	}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




256


257



	private Collection<Transition<T>> possibleTransitions(int index, boolean addExclusionTransitions) {
		Collection<Transition<T>> result = Lists.newLinkedList();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




258


259


260



		if(index < accesses.length) {
			if(accesses[index] instanceof SetOfPossibleFieldAccesses) {
				result.add(new Transition.SubAccessPathTransition<>(index, accesses[index]));








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




261



				result.addAll(possibleTransitions(index+1, addExclusionTransitions));








cleaning code



Johannes Lerch
committed
Jan 07, 2015




262



			}








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




263


264



			else
				result.add(new Transition.SubAccessPathTransition<>(index+1, accesses[index]));








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




265


266



		} else if(addExclusionTransitions && index - accesses.length == 0 && !exclusions.isEmpty()) {
			result.add(new Transition.ExclusionPathTransition<T>(index+1, exclusions));








cleaning code



Johannes Lerch
committed
Jan 07, 2015




267



		}








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




268



		return result;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




269


270



	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




271


272


273



	public SubAccessPath<T>[] getDeltaTo(AccessPath<T> accPath) {
		int currIndex = 0;
		int otherIndex = 0;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




274



		








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




275


276


277


278


279


280


281


282


283


284


285


286


287


288


289



		outer: while(true) {
			Collection<Transition<T>> transitions = possibleTransitions(currIndex, false);
			Collection<Transition<T>> otherTransitions = accPath.possibleTransitions(otherIndex, false);

			for(Transition<T> transition : transitions) {
				for(Transition<T> otherTransition : otherTransitions) {
					MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);
					if(match.hasMatched()) {
						if(currIndex == transition.transitionToIndex() && otherIndex == otherTransition.transitionToIndex())
							continue;
						
						currIndex = transition.transitionToIndex();
						otherIndex = otherTransition.transitionToIndex();
						continue outer;
					}








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




290


291



				}
			}








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




292



			break;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




293


294



		}
		








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




295



		return Arrays.copyOfRange(accPath.accesses, otherIndex, accPath.accesses.length);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




296


297



	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




298



	public AccessPath<T> mergeExcludedFieldReferences(AccessPath<T> accPath) {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




299


300


301



		HashSet<T> newExclusions = Sets.newHashSet(exclusions);
		newExclusions.addAll(accPath.exclusions);
		return new AccessPath<>(accesses, newExclusions);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




302


303



	}
	








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




304


305


306


307



	public boolean mayHaveEmptyAccessPath() {
		return finalIndex() == 0;
	}
	








cleaning code



Johannes Lerch
committed
Jan 07, 2015




308



	public boolean isEmpty() {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




309



		return exclusions.isEmpty() && accesses.length == 0;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




310


311


312


313


314


315


316



	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(accesses);








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




317



		result = prime * result + ((exclusions == null) ? 0 : exclusions.hashCode());








cleaning code



Johannes Lerch
committed
Jan 07, 2015




318


319


320


321


322


323


324


325


326


327


328


329


330


331



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








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




332


333


334


335



		if (exclusions == null) {
			if (other.exclusions != null)
				return false;
		} else if (!exclusions.equals(other.exclusions))








cleaning code



Johannes Lerch
committed
Jan 07, 2015




336


337


338


339


340


341


342



			return false;
		return true;
	}

	@Override
	public String toString() {
		String result = accesses.length > 0 ? "."+Joiner.on(".").join(accesses) : "";








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




343


344



		if(!exclusions.isEmpty())
			result += "^" + Joiner.on(",").join(exclusions);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




345


346


347



		return result;
	}
	








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




348


349


350


351



	public <U extends FieldRef<U>> AccessPath<U> map(Function<T, U> function) {
		SubAccessPath<U>[] newAccesses = new SubAccessPath[accesses.length];
		for(int i=0; i<accesses.length; i++) {
			newAccesses[i] = accesses[i].map(function);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




352



		}








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




353


354


355


356



		Set<U> newExclusions = Sets.newHashSet();
		for(T f : exclusions)
			newExclusions.add(function.apply(f));
		return new AccessPath<U>(newAccesses, newExclusions);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




357



	}








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




358



	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




359



	public AccessPath<T> removeAnyAccess() {








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




360



		if(accesses.length > 0)








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




361



			return new AccessPath<T>(new SubAccessPath[0], exclusions);








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




362


363


364


365


366


367



		else
			return this;
	}

	public boolean hasEmptyAccessPath() {
		return accesses.length == 0;








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




368



	}








subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015




369


370


371


372


373


374


375


376


377


378


379


380


381


382


383


384


385


386


387


388


389


390


391


392


393


394


395


396


397


398


399


400


401


402


403


404


405


406


407


408


409


410




	public boolean subsumes(AccessPath<T> accPath) {
		int currIndex = 0;
		int otherIndex = 0;
		
		
		outer: while(true) {
			Collection<Transition<T>> transitions = possibleTransitions(currIndex, false);
			Collection<Transition<T>> otherTransitions = accPath.possibleTransitions(otherIndex, false);

			if((currIndex >= accesses.length || (currIndex == accesses.length-1 && accesses[currIndex] instanceof SetOfPossibleFieldAccesses)) 
					&& otherIndex>=accPath.accesses.length-1) {
				if(transitions.isEmpty())
					return otherTransitions.isEmpty() && hasAtLeastTheSameExclusionsAs(accPath);
				for(Transition<T> transition : transitions) {
					for(Transition<T> otherTransition : otherTransitions) {
						MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);
						if(!match.hasMatched())
							return false;
					}	
				}
				return hasAtLeastTheSameExclusionsAs(accPath);
			}

			for(Transition<T> transition : transitions) {
				for(Transition<T> otherTransition : otherTransitions) {
					MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);
					if(match.hasMatched()) {
						if(otherIndex == otherTransition.transitionToIndex())
							continue;
						
						currIndex = transition.transitionToIndex();
						otherIndex = otherTransition.transitionToIndex();
						continue outer;
					}
				}
			}
			return false;
		}
	}
	
	private boolean hasAtLeastTheSameExclusionsAs(AccessPath<T> accPath) {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




411



		return accPath.exclusions.containsAll(exclusions);








subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015




412


413


414


415


416


417


418



	}

	public Collection<String> tokenize() {
		List<String> result = Lists.newLinkedList();
		for(SubAccessPath<T> s : accesses) {
			result.add(s.toString());
		}








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




419


420



		if(!exclusions.isEmpty())
			result.add("^"+Joiner.on(",").join(exclusions));








subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015




421


422



		return result;
	}








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




423


424


425


426




	public AccessPath<T> removeExclusions() {
		return new AccessPath<T>(accesses, Sets.<T>newHashSet());
	}








additional methods for AccessPath required by client FlowFunction

 


Johannes Lerch
committed
Feb 16, 2015




427


428


429


430


431


432


433


434


435


436


437


438


439


440


441


442


443


444


445


446




	public SubAccessPath<T> getFirstAccess() {
		return accesses[0];
	}

	public AccessPath<T> removeRepeatableFirstAccess(T field) {
		Collection<? extends T> elements = accesses[0].elements();
		if(!elements.contains(field))
			throw new IllegalArgumentException();
		
		if(elements.size() == 1) {
			return new AccessPath<>(Arrays.copyOfRange(accesses, 1, accesses.length), exclusions);
		}
		
		HashSet<T> newSet = Sets.newHashSet(elements);
		newSet.remove(field);
		SubAccessPath<T>[] newAccesses = Arrays.copyOf(accesses, accesses.length);
		newAccesses[0] = new SetOfPossibleFieldAccesses<>(newSet);
		return new AccessPath<T>(newAccesses, exclusions);
	}








cleaning code



Johannes Lerch
committed
Jan 07, 2015




447



}









AccessPath.java



13.7 KB










AccessPath.java



13.7 KB









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









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




13


14


15



import heros.alias.SubAccessPath.SetOfPossibleFieldAccesses;
import heros.alias.SubAccessPath.SpecificFieldAccess;
import heros.alias.Transition.MatchResult;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




16












cleaning code



Johannes Lerch
committed
Jan 07, 2015




17



import java.util.Arrays;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




18



import java.util.Collection;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




19



import java.util.HashSet;








subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015




20



import java.util.List;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




21


22



import java.util.Set;









remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




23



import com.google.common.base.Function;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




24



import com.google.common.base.Joiner;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




25



import com.google.common.collect.Lists;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




26


27


28



import com.google.common.collect.Sets;

@SuppressWarnings("unchecked")








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




29



public class AccessPath<T extends AccessPath.FieldRef<T>> {








cleaning code



Johannes Lerch
committed
Jan 07, 2015




30












merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




31


32


33


34


35



	public static interface FieldRef<F> {
		boolean shouldBeMergedWith(F fieldRef);
	}
	
	public static <T extends FieldRef<T>> AccessPath<T> empty() {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




36


37


38



		return new AccessPath<T>();
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




39



	private final SubAccessPath<T>[] accesses;








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




40



	private final Set<T> exclusions;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




41


42



	
	public AccessPath() {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




43



		accesses = new SubAccessPath[0];








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




44



		exclusions = Sets.newHashSet();








cleaning code



Johannes Lerch
committed
Jan 07, 2015




45


46



	}
	








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




47



	AccessPath(SubAccessPath<T>[] accesses, Set<T> exclusions) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




48


49



		this.accesses = accesses;
		this.exclusions = exclusions;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




50


51



	}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




52



	public boolean isAccessInExclusions(SubAccessPath<T>... fieldReferences) {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




53


54


55


56



		if(fieldReferences.length > 0) {
			for(T field : fieldReferences[0].elements()) {
				if(!exclusions.contains(field))
					return false;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




57


58



			}
			return true;








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




59



		}








cleaning code



Johannes Lerch
committed
Jan 07, 2015




60


61


62



		return false;
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




63



	public AccessPath<T> addFieldReference(SubAccessPath<T>... fieldReferences) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




64


65


66



		return addFieldReference(true, fieldReferences);
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




67



	AccessPath<T> addFieldReference(boolean merge, SubAccessPath<T>... fieldReferences) {








cleaning code



Johannes Lerch
committed
Jan 07, 2015




68



		if(isAccessInExclusions(fieldReferences))








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




69



			throw new IllegalArgumentException("FieldRef "+Arrays.toString(fieldReferences)+" cannot be added to "+toString());








cleaning code



Johannes Lerch
committed
Jan 07, 2015




70












regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




71


72


73



		if(merge) {
			for(int i=fieldReferences.length-1; i>=0; i--) {
				for(int j=0; j<accesses.length; j++) {








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




74



					if(accesses[j].shouldBeMerged(fieldReferences[i])) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




75


76


77


78


79


80



						// [..., {j-i}, ...]
						
						AccessPathBuilder builder = new AccessPathBuilder(j+fieldReferences.length-i);
						builder.keep(0, j);
						builder.merge(j, accesses.length).mergeWithLast(fieldReferences, 0, i);
						builder.append(fieldReferences, i+1, fieldReferences.length);








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




81



						builder.removeExclusions();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




82


83


84


85


86


87


88


89


90



						return builder.build();
					}
				}
			}
		}
		
		AccessPathBuilder builder = new AccessPathBuilder(accesses.length + fieldReferences.length);
		builder.keep(0, accesses.length);
		builder.append(fieldReferences, 0, fieldReferences.length);








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




91


92



		if(fieldReferences.length>0)
			builder.removeExclusions();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




93


94


95



		return builder.build();
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




96


97



	public AccessPath<T> addFieldReference(T... fieldReferences) {
		SubAccessPath<T>[] subPath = new SubAccessPath[fieldReferences.length];








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




98


99


100


101


102


103


104


105



		for(int i=0; i<fieldReferences.length; i++) {
			subPath[i] = new SpecificFieldAccess<>(fieldReferences[i]);
		}
		return addFieldReference(subPath);
	}
	
	private class AccessPathBuilder {
		








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




106



		private Set<T> newExclusions;








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




107



		private SubAccessPath<T>[] newAccesses;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




108


109


110


111


112


113


114



		private int currentIndex = 0;

		public AccessPathBuilder(int capacity) {
			newAccesses = new SubAccessPath[capacity];
			newExclusions = exclusions;
		}
		








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




115



		public AccessPath<T> build() {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




116


117


118



			return new AccessPath<>(newAccesses, newExclusions);
		}









remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




119


120



		public void removeExclusions() {
			newExclusions = Sets.newHashSet();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




121


122



		}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




123



		public void append(SubAccessPath<T>[] fieldReferences, int start, int endExcl) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




124


125


126


127



			for(int i=start; i<endExcl; i++) {
				newAccesses[currentIndex] = fieldReferences[i];
				currentIndex++;
				if(fieldReferences[i] instanceof SetOfPossibleFieldAccesses)








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




128



					removeExclusions();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




129


130


131


132



			}
			currentIndex+=endExcl-start;
		}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




133



		public void mergeWithLast(SubAccessPath<T>[] fieldReferences, int start, int endExcl) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




134


135


136


137



			newAccesses[currentIndex-1].merge(Arrays.copyOfRange(fieldReferences, start, endExcl));
		}

		public AccessPathBuilder merge(int srcIndex, int destIndexExcl) {








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




138



			Set<T> set = Sets.newHashSet();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




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



			for(int i=srcIndex; i<destIndexExcl; i++) {
				set.addAll(accesses[i].elements());
			}
			newAccesses[currentIndex] = new SubAccessPath.SetOfPossibleFieldAccesses<>(set);
			currentIndex++;
			return this;
		}

		public AccessPathBuilder keep(int srcIndex, int destIndexExcl) {
			System.arraycopy(accesses, srcIndex, newAccesses, currentIndex, destIndexExcl-srcIndex);
			currentIndex += destIndexExcl-srcIndex;
			return this;
		}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




153



		public void append(T fieldRef) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




154


155


156


157



			newAccesses[currentIndex] = new SubAccessPath.SpecificFieldAccess<>(fieldRef);
			currentIndex++;
		}
		








cleaning code



Johannes Lerch
committed
Jan 07, 2015




158


159



	}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




160



	public AccessPath<T> prepend(T fieldRef) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




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



		for(int j=0; j<accesses.length; j++) {
			if(accesses[j].contains(fieldRef)) {
				// [{0-j}, ...]
				
				AccessPathBuilder builder = new AccessPathBuilder(accesses.length-j);
				builder.merge(0, j+1);
				builder.keep(j+1, accesses.length);
				return builder.build();
			}
		}
		AccessPathBuilder builder = new AccessPathBuilder(accesses.length + 1);
		builder.append(fieldRef);
		builder.keep(0, accesses.length);
		return builder.build();








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




175


176



	}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




177



	public AccessPath<T> removeFirst(T field) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




178


179


180



		for(int i=0; i<accesses.length; i++) {
			if(accesses[i].contains(field)) {
				if(accesses[i] instanceof SpecificFieldAccess)








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




181



					return new AccessPath<T>(Arrays.copyOfRange(accesses, i+1, accesses.length), exclusions);








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




182


183


184


185


186


187


188


189


190


191



				else
					return this;
			}
			else if(accesses[i] instanceof SpecificFieldAccess)
				throw new IllegalStateException("Trying to remove "+field+" from "+this);
		}
		
		throw new IllegalStateException("Trying to remove "+field+" from "+this);
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




192



	public AccessPath<T> appendExcludedFieldReference(T... fieldReferences) {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




193


194


195



		HashSet<T> newExclusions = Sets.newHashSet(fieldReferences);
		newExclusions.addAll(exclusions);
		return new AccessPath<>(accesses, newExclusions);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




196



	}








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




197


198


199


200


201


202


203


204


205


206


207


208


209


210




	public static enum PrefixTestResult {
		GUARANTEED_PREFIX(2), POTENTIAL_PREFIX(1), NO_PREFIX(0);
		
		private int value;

		private PrefixTestResult(int value) {
			this.value = value;
		}
		
		public boolean atLeast(PrefixTestResult minimum) {
			return value >= minimum.value;
		}
	}








cleaning code



Johannes Lerch
committed
Jan 07, 2015




211



	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




212



	public PrefixTestResult isPrefixOf(AccessPath<T> accPath) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




213


214


215



		int currIndex = 0;
		int otherIndex = 0;
		PrefixTestResult result = PrefixTestResult.GUARANTEED_PREFIX;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




216



		








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




217


218



		int finalIndex = finalIndex();
		outer: while(currIndex < finalIndex) {








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




219


220



			Collection<Transition<T>> transitions = possibleTransitions(currIndex, true);
			Collection<Transition<T>> otherTransitions = accPath.possibleTransitions(otherIndex, true);








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




221












merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




222


223


224



			for(Transition<T> transition : transitions) {
				for(Transition<T> otherTransition : otherTransitions) {
					MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




225


226



					if(match.hasMatched()) {
						if(currIndex == transition.transitionToIndex() && otherIndex == otherTransition.transitionToIndex())








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




227



							continue;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




228


229


230


231


232


233


234


235


236


237



						
						currIndex = transition.transitionToIndex();
						otherIndex = otherTransition.transitionToIndex();
						if(!match.isGuaranteedMatch())
							result = PrefixTestResult.POTENTIAL_PREFIX;
						
						continue outer;
					}
				}
			}








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




238



			return PrefixTestResult.NO_PREFIX;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




239



		}








cleaning code



Johannes Lerch
committed
Jan 07, 2015




240



		








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




241


242


243


244



		return result;
	}
	
	private int finalIndex() {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




245


246



		if(!exclusions.isEmpty())
			return accesses.length + 1;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




247


248



		
		int finalIndex = 0;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




249



		for(int i=0; i<accesses.length; i++) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




250


251



			if(accesses[i] instanceof SpecificFieldAccess)
				finalIndex = i+1;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




252



		}








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




253


254


255



		return finalIndex;
	}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




256


257



	private Collection<Transition<T>> possibleTransitions(int index, boolean addExclusionTransitions) {
		Collection<Transition<T>> result = Lists.newLinkedList();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




258


259


260



		if(index < accesses.length) {
			if(accesses[index] instanceof SetOfPossibleFieldAccesses) {
				result.add(new Transition.SubAccessPathTransition<>(index, accesses[index]));








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




261



				result.addAll(possibleTransitions(index+1, addExclusionTransitions));








cleaning code



Johannes Lerch
committed
Jan 07, 2015




262



			}








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




263


264



			else
				result.add(new Transition.SubAccessPathTransition<>(index+1, accesses[index]));








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




265


266



		} else if(addExclusionTransitions && index - accesses.length == 0 && !exclusions.isEmpty()) {
			result.add(new Transition.ExclusionPathTransition<T>(index+1, exclusions));








cleaning code



Johannes Lerch
committed
Jan 07, 2015




267



		}








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




268



		return result;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




269


270



	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




271


272


273



	public SubAccessPath<T>[] getDeltaTo(AccessPath<T> accPath) {
		int currIndex = 0;
		int otherIndex = 0;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




274



		








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




275


276


277


278


279


280


281


282


283


284


285


286


287


288


289



		outer: while(true) {
			Collection<Transition<T>> transitions = possibleTransitions(currIndex, false);
			Collection<Transition<T>> otherTransitions = accPath.possibleTransitions(otherIndex, false);

			for(Transition<T> transition : transitions) {
				for(Transition<T> otherTransition : otherTransitions) {
					MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);
					if(match.hasMatched()) {
						if(currIndex == transition.transitionToIndex() && otherIndex == otherTransition.transitionToIndex())
							continue;
						
						currIndex = transition.transitionToIndex();
						otherIndex = otherTransition.transitionToIndex();
						continue outer;
					}








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




290


291



				}
			}








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




292



			break;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




293


294



		}
		








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




295



		return Arrays.copyOfRange(accPath.accesses, otherIndex, accPath.accesses.length);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




296


297



	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




298



	public AccessPath<T> mergeExcludedFieldReferences(AccessPath<T> accPath) {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




299


300


301



		HashSet<T> newExclusions = Sets.newHashSet(exclusions);
		newExclusions.addAll(accPath.exclusions);
		return new AccessPath<>(accesses, newExclusions);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




302


303



	}
	








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




304


305


306


307



	public boolean mayHaveEmptyAccessPath() {
		return finalIndex() == 0;
	}
	








cleaning code



Johannes Lerch
committed
Jan 07, 2015




308



	public boolean isEmpty() {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




309



		return exclusions.isEmpty() && accesses.length == 0;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




310


311


312


313


314


315


316



	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(accesses);








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




317



		result = prime * result + ((exclusions == null) ? 0 : exclusions.hashCode());








cleaning code



Johannes Lerch
committed
Jan 07, 2015




318


319


320


321


322


323


324


325


326


327


328


329


330


331



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








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




332


333


334


335



		if (exclusions == null) {
			if (other.exclusions != null)
				return false;
		} else if (!exclusions.equals(other.exclusions))








cleaning code



Johannes Lerch
committed
Jan 07, 2015




336


337


338


339


340


341


342



			return false;
		return true;
	}

	@Override
	public String toString() {
		String result = accesses.length > 0 ? "."+Joiner.on(".").join(accesses) : "";








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




343


344



		if(!exclusions.isEmpty())
			result += "^" + Joiner.on(",").join(exclusions);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




345


346


347



		return result;
	}
	








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




348


349


350


351



	public <U extends FieldRef<U>> AccessPath<U> map(Function<T, U> function) {
		SubAccessPath<U>[] newAccesses = new SubAccessPath[accesses.length];
		for(int i=0; i<accesses.length; i++) {
			newAccesses[i] = accesses[i].map(function);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




352



		}








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




353


354


355


356



		Set<U> newExclusions = Sets.newHashSet();
		for(T f : exclusions)
			newExclusions.add(function.apply(f));
		return new AccessPath<U>(newAccesses, newExclusions);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




357



	}








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




358



	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




359



	public AccessPath<T> removeAnyAccess() {








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




360



		if(accesses.length > 0)








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




361



			return new AccessPath<T>(new SubAccessPath[0], exclusions);








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




362


363


364


365


366


367



		else
			return this;
	}

	public boolean hasEmptyAccessPath() {
		return accesses.length == 0;








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




368



	}








subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015




369


370


371


372


373


374


375


376


377


378


379


380


381


382


383


384


385


386


387


388


389


390


391


392


393


394


395


396


397


398


399


400


401


402


403


404


405


406


407


408


409


410




	public boolean subsumes(AccessPath<T> accPath) {
		int currIndex = 0;
		int otherIndex = 0;
		
		
		outer: while(true) {
			Collection<Transition<T>> transitions = possibleTransitions(currIndex, false);
			Collection<Transition<T>> otherTransitions = accPath.possibleTransitions(otherIndex, false);

			if((currIndex >= accesses.length || (currIndex == accesses.length-1 && accesses[currIndex] instanceof SetOfPossibleFieldAccesses)) 
					&& otherIndex>=accPath.accesses.length-1) {
				if(transitions.isEmpty())
					return otherTransitions.isEmpty() && hasAtLeastTheSameExclusionsAs(accPath);
				for(Transition<T> transition : transitions) {
					for(Transition<T> otherTransition : otherTransitions) {
						MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);
						if(!match.hasMatched())
							return false;
					}	
				}
				return hasAtLeastTheSameExclusionsAs(accPath);
			}

			for(Transition<T> transition : transitions) {
				for(Transition<T> otherTransition : otherTransitions) {
					MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);
					if(match.hasMatched()) {
						if(otherIndex == otherTransition.transitionToIndex())
							continue;
						
						currIndex = transition.transitionToIndex();
						otherIndex = otherTransition.transitionToIndex();
						continue outer;
					}
				}
			}
			return false;
		}
	}
	
	private boolean hasAtLeastTheSameExclusionsAs(AccessPath<T> accPath) {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




411



		return accPath.exclusions.containsAll(exclusions);








subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015




412


413


414


415


416


417


418



	}

	public Collection<String> tokenize() {
		List<String> result = Lists.newLinkedList();
		for(SubAccessPath<T> s : accesses) {
			result.add(s.toString());
		}








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




419


420



		if(!exclusions.isEmpty())
			result.add("^"+Joiner.on(",").join(exclusions));








subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015




421


422



		return result;
	}








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




423


424


425


426




	public AccessPath<T> removeExclusions() {
		return new AccessPath<T>(accesses, Sets.<T>newHashSet());
	}








additional methods for AccessPath required by client FlowFunction

 


Johannes Lerch
committed
Feb 16, 2015




427


428


429


430


431


432


433


434


435


436


437


438


439


440


441


442


443


444


445


446




	public SubAccessPath<T> getFirstAccess() {
		return accesses[0];
	}

	public AccessPath<T> removeRepeatableFirstAccess(T field) {
		Collection<? extends T> elements = accesses[0].elements();
		if(!elements.contains(field))
			throw new IllegalArgumentException();
		
		if(elements.size() == 1) {
			return new AccessPath<>(Arrays.copyOfRange(accesses, 1, accesses.length), exclusions);
		}
		
		HashSet<T> newSet = Sets.newHashSet(elements);
		newSet.remove(field);
		SubAccessPath<T>[] newAccesses = Arrays.copyOf(accesses, accesses.length);
		newAccesses[0] = new SetOfPossibleFieldAccesses<>(newSet);
		return new AccessPath<T>(newAccesses, exclusions);
	}








cleaning code



Johannes Lerch
committed
Jan 07, 2015




447



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









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




13


14


15



import heros.alias.SubAccessPath.SetOfPossibleFieldAccesses;
import heros.alias.SubAccessPath.SpecificFieldAccess;
import heros.alias.Transition.MatchResult;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




16












cleaning code



Johannes Lerch
committed
Jan 07, 2015




17



import java.util.Arrays;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




18



import java.util.Collection;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




19



import java.util.HashSet;








subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015




20



import java.util.List;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




21


22



import java.util.Set;









remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




23



import com.google.common.base.Function;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




24



import com.google.common.base.Joiner;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




25



import com.google.common.collect.Lists;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




26


27


28



import com.google.common.collect.Sets;

@SuppressWarnings("unchecked")








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




29



public class AccessPath<T extends AccessPath.FieldRef<T>> {








cleaning code



Johannes Lerch
committed
Jan 07, 2015




30












merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




31


32


33


34


35



	public static interface FieldRef<F> {
		boolean shouldBeMergedWith(F fieldRef);
	}
	
	public static <T extends FieldRef<T>> AccessPath<T> empty() {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




36


37


38



		return new AccessPath<T>();
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




39



	private final SubAccessPath<T>[] accesses;








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




40



	private final Set<T> exclusions;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




41


42



	
	public AccessPath() {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




43



		accesses = new SubAccessPath[0];








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




44



		exclusions = Sets.newHashSet();








cleaning code



Johannes Lerch
committed
Jan 07, 2015




45


46



	}
	








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




47



	AccessPath(SubAccessPath<T>[] accesses, Set<T> exclusions) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




48


49



		this.accesses = accesses;
		this.exclusions = exclusions;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




50


51



	}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




52



	public boolean isAccessInExclusions(SubAccessPath<T>... fieldReferences) {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




53


54


55


56



		if(fieldReferences.length > 0) {
			for(T field : fieldReferences[0].elements()) {
				if(!exclusions.contains(field))
					return false;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




57


58



			}
			return true;








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




59



		}








cleaning code



Johannes Lerch
committed
Jan 07, 2015




60


61


62



		return false;
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




63



	public AccessPath<T> addFieldReference(SubAccessPath<T>... fieldReferences) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




64


65


66



		return addFieldReference(true, fieldReferences);
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




67



	AccessPath<T> addFieldReference(boolean merge, SubAccessPath<T>... fieldReferences) {








cleaning code



Johannes Lerch
committed
Jan 07, 2015




68



		if(isAccessInExclusions(fieldReferences))








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




69



			throw new IllegalArgumentException("FieldRef "+Arrays.toString(fieldReferences)+" cannot be added to "+toString());








cleaning code



Johannes Lerch
committed
Jan 07, 2015




70












regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




71


72


73



		if(merge) {
			for(int i=fieldReferences.length-1; i>=0; i--) {
				for(int j=0; j<accesses.length; j++) {








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




74



					if(accesses[j].shouldBeMerged(fieldReferences[i])) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




75


76


77


78


79


80



						// [..., {j-i}, ...]
						
						AccessPathBuilder builder = new AccessPathBuilder(j+fieldReferences.length-i);
						builder.keep(0, j);
						builder.merge(j, accesses.length).mergeWithLast(fieldReferences, 0, i);
						builder.append(fieldReferences, i+1, fieldReferences.length);








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




81



						builder.removeExclusions();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




82


83


84


85


86


87


88


89


90



						return builder.build();
					}
				}
			}
		}
		
		AccessPathBuilder builder = new AccessPathBuilder(accesses.length + fieldReferences.length);
		builder.keep(0, accesses.length);
		builder.append(fieldReferences, 0, fieldReferences.length);








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




91


92



		if(fieldReferences.length>0)
			builder.removeExclusions();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




93


94


95



		return builder.build();
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




96


97



	public AccessPath<T> addFieldReference(T... fieldReferences) {
		SubAccessPath<T>[] subPath = new SubAccessPath[fieldReferences.length];








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




98


99


100


101


102


103


104


105



		for(int i=0; i<fieldReferences.length; i++) {
			subPath[i] = new SpecificFieldAccess<>(fieldReferences[i]);
		}
		return addFieldReference(subPath);
	}
	
	private class AccessPathBuilder {
		








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




106



		private Set<T> newExclusions;








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




107



		private SubAccessPath<T>[] newAccesses;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




108


109


110


111


112


113


114



		private int currentIndex = 0;

		public AccessPathBuilder(int capacity) {
			newAccesses = new SubAccessPath[capacity];
			newExclusions = exclusions;
		}
		








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




115



		public AccessPath<T> build() {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




116


117


118



			return new AccessPath<>(newAccesses, newExclusions);
		}









remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




119


120



		public void removeExclusions() {
			newExclusions = Sets.newHashSet();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




121


122



		}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




123



		public void append(SubAccessPath<T>[] fieldReferences, int start, int endExcl) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




124


125


126


127



			for(int i=start; i<endExcl; i++) {
				newAccesses[currentIndex] = fieldReferences[i];
				currentIndex++;
				if(fieldReferences[i] instanceof SetOfPossibleFieldAccesses)








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




128



					removeExclusions();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




129


130


131


132



			}
			currentIndex+=endExcl-start;
		}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




133



		public void mergeWithLast(SubAccessPath<T>[] fieldReferences, int start, int endExcl) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




134


135


136


137



			newAccesses[currentIndex-1].merge(Arrays.copyOfRange(fieldReferences, start, endExcl));
		}

		public AccessPathBuilder merge(int srcIndex, int destIndexExcl) {








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




138



			Set<T> set = Sets.newHashSet();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




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



			for(int i=srcIndex; i<destIndexExcl; i++) {
				set.addAll(accesses[i].elements());
			}
			newAccesses[currentIndex] = new SubAccessPath.SetOfPossibleFieldAccesses<>(set);
			currentIndex++;
			return this;
		}

		public AccessPathBuilder keep(int srcIndex, int destIndexExcl) {
			System.arraycopy(accesses, srcIndex, newAccesses, currentIndex, destIndexExcl-srcIndex);
			currentIndex += destIndexExcl-srcIndex;
			return this;
		}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




153



		public void append(T fieldRef) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




154


155


156


157



			newAccesses[currentIndex] = new SubAccessPath.SpecificFieldAccess<>(fieldRef);
			currentIndex++;
		}
		








cleaning code



Johannes Lerch
committed
Jan 07, 2015




158


159



	}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




160



	public AccessPath<T> prepend(T fieldRef) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




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



		for(int j=0; j<accesses.length; j++) {
			if(accesses[j].contains(fieldRef)) {
				// [{0-j}, ...]
				
				AccessPathBuilder builder = new AccessPathBuilder(accesses.length-j);
				builder.merge(0, j+1);
				builder.keep(j+1, accesses.length);
				return builder.build();
			}
		}
		AccessPathBuilder builder = new AccessPathBuilder(accesses.length + 1);
		builder.append(fieldRef);
		builder.keep(0, accesses.length);
		return builder.build();








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




175


176



	}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




177



	public AccessPath<T> removeFirst(T field) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




178


179


180



		for(int i=0; i<accesses.length; i++) {
			if(accesses[i].contains(field)) {
				if(accesses[i] instanceof SpecificFieldAccess)








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




181



					return new AccessPath<T>(Arrays.copyOfRange(accesses, i+1, accesses.length), exclusions);








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




182


183


184


185


186


187


188


189


190


191



				else
					return this;
			}
			else if(accesses[i] instanceof SpecificFieldAccess)
				throw new IllegalStateException("Trying to remove "+field+" from "+this);
		}
		
		throw new IllegalStateException("Trying to remove "+field+" from "+this);
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




192



	public AccessPath<T> appendExcludedFieldReference(T... fieldReferences) {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




193


194


195



		HashSet<T> newExclusions = Sets.newHashSet(fieldReferences);
		newExclusions.addAll(exclusions);
		return new AccessPath<>(accesses, newExclusions);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




196



	}








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




197


198


199


200


201


202


203


204


205


206


207


208


209


210




	public static enum PrefixTestResult {
		GUARANTEED_PREFIX(2), POTENTIAL_PREFIX(1), NO_PREFIX(0);
		
		private int value;

		private PrefixTestResult(int value) {
			this.value = value;
		}
		
		public boolean atLeast(PrefixTestResult minimum) {
			return value >= minimum.value;
		}
	}








cleaning code



Johannes Lerch
committed
Jan 07, 2015




211



	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




212



	public PrefixTestResult isPrefixOf(AccessPath<T> accPath) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




213


214


215



		int currIndex = 0;
		int otherIndex = 0;
		PrefixTestResult result = PrefixTestResult.GUARANTEED_PREFIX;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




216



		








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




217


218



		int finalIndex = finalIndex();
		outer: while(currIndex < finalIndex) {








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




219


220



			Collection<Transition<T>> transitions = possibleTransitions(currIndex, true);
			Collection<Transition<T>> otherTransitions = accPath.possibleTransitions(otherIndex, true);








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




221












merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




222


223


224



			for(Transition<T> transition : transitions) {
				for(Transition<T> otherTransition : otherTransitions) {
					MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




225


226



					if(match.hasMatched()) {
						if(currIndex == transition.transitionToIndex() && otherIndex == otherTransition.transitionToIndex())








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




227



							continue;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




228


229


230


231


232


233


234


235


236


237



						
						currIndex = transition.transitionToIndex();
						otherIndex = otherTransition.transitionToIndex();
						if(!match.isGuaranteedMatch())
							result = PrefixTestResult.POTENTIAL_PREFIX;
						
						continue outer;
					}
				}
			}








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




238



			return PrefixTestResult.NO_PREFIX;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




239



		}








cleaning code



Johannes Lerch
committed
Jan 07, 2015




240



		








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




241


242


243


244



		return result;
	}
	
	private int finalIndex() {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




245


246



		if(!exclusions.isEmpty())
			return accesses.length + 1;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




247


248



		
		int finalIndex = 0;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




249



		for(int i=0; i<accesses.length; i++) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




250


251



			if(accesses[i] instanceof SpecificFieldAccess)
				finalIndex = i+1;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




252



		}








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




253


254


255



		return finalIndex;
	}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




256


257



	private Collection<Transition<T>> possibleTransitions(int index, boolean addExclusionTransitions) {
		Collection<Transition<T>> result = Lists.newLinkedList();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




258


259


260



		if(index < accesses.length) {
			if(accesses[index] instanceof SetOfPossibleFieldAccesses) {
				result.add(new Transition.SubAccessPathTransition<>(index, accesses[index]));








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




261



				result.addAll(possibleTransitions(index+1, addExclusionTransitions));








cleaning code



Johannes Lerch
committed
Jan 07, 2015




262



			}








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




263


264



			else
				result.add(new Transition.SubAccessPathTransition<>(index+1, accesses[index]));








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




265


266



		} else if(addExclusionTransitions && index - accesses.length == 0 && !exclusions.isEmpty()) {
			result.add(new Transition.ExclusionPathTransition<T>(index+1, exclusions));








cleaning code



Johannes Lerch
committed
Jan 07, 2015




267



		}








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




268



		return result;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




269


270



	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




271


272


273



	public SubAccessPath<T>[] getDeltaTo(AccessPath<T> accPath) {
		int currIndex = 0;
		int otherIndex = 0;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




274



		








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




275


276


277


278


279


280


281


282


283


284


285


286


287


288


289



		outer: while(true) {
			Collection<Transition<T>> transitions = possibleTransitions(currIndex, false);
			Collection<Transition<T>> otherTransitions = accPath.possibleTransitions(otherIndex, false);

			for(Transition<T> transition : transitions) {
				for(Transition<T> otherTransition : otherTransitions) {
					MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);
					if(match.hasMatched()) {
						if(currIndex == transition.transitionToIndex() && otherIndex == otherTransition.transitionToIndex())
							continue;
						
						currIndex = transition.transitionToIndex();
						otherIndex = otherTransition.transitionToIndex();
						continue outer;
					}








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




290


291



				}
			}








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




292



			break;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




293


294



		}
		








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




295



		return Arrays.copyOfRange(accPath.accesses, otherIndex, accPath.accesses.length);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




296


297



	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




298



	public AccessPath<T> mergeExcludedFieldReferences(AccessPath<T> accPath) {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




299


300


301



		HashSet<T> newExclusions = Sets.newHashSet(exclusions);
		newExclusions.addAll(accPath.exclusions);
		return new AccessPath<>(accesses, newExclusions);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




302


303



	}
	








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




304


305


306


307



	public boolean mayHaveEmptyAccessPath() {
		return finalIndex() == 0;
	}
	








cleaning code



Johannes Lerch
committed
Jan 07, 2015




308



	public boolean isEmpty() {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




309



		return exclusions.isEmpty() && accesses.length == 0;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




310


311


312


313


314


315


316



	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(accesses);








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




317



		result = prime * result + ((exclusions == null) ? 0 : exclusions.hashCode());








cleaning code



Johannes Lerch
committed
Jan 07, 2015




318


319


320


321


322


323


324


325


326


327


328


329


330


331



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








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




332


333


334


335



		if (exclusions == null) {
			if (other.exclusions != null)
				return false;
		} else if (!exclusions.equals(other.exclusions))








cleaning code



Johannes Lerch
committed
Jan 07, 2015




336


337


338


339


340


341


342



			return false;
		return true;
	}

	@Override
	public String toString() {
		String result = accesses.length > 0 ? "."+Joiner.on(".").join(accesses) : "";








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




343


344



		if(!exclusions.isEmpty())
			result += "^" + Joiner.on(",").join(exclusions);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




345


346


347



		return result;
	}
	








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




348


349


350


351



	public <U extends FieldRef<U>> AccessPath<U> map(Function<T, U> function) {
		SubAccessPath<U>[] newAccesses = new SubAccessPath[accesses.length];
		for(int i=0; i<accesses.length; i++) {
			newAccesses[i] = accesses[i].map(function);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




352



		}








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




353


354


355


356



		Set<U> newExclusions = Sets.newHashSet();
		for(T f : exclusions)
			newExclusions.add(function.apply(f));
		return new AccessPath<U>(newAccesses, newExclusions);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




357



	}








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




358



	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




359



	public AccessPath<T> removeAnyAccess() {








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




360



		if(accesses.length > 0)








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




361



			return new AccessPath<T>(new SubAccessPath[0], exclusions);








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




362


363


364


365


366


367



		else
			return this;
	}

	public boolean hasEmptyAccessPath() {
		return accesses.length == 0;








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




368



	}








subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015




369


370


371


372


373


374


375


376


377


378


379


380


381


382


383


384


385


386


387


388


389


390


391


392


393


394


395


396


397


398


399


400


401


402


403


404


405


406


407


408


409


410




	public boolean subsumes(AccessPath<T> accPath) {
		int currIndex = 0;
		int otherIndex = 0;
		
		
		outer: while(true) {
			Collection<Transition<T>> transitions = possibleTransitions(currIndex, false);
			Collection<Transition<T>> otherTransitions = accPath.possibleTransitions(otherIndex, false);

			if((currIndex >= accesses.length || (currIndex == accesses.length-1 && accesses[currIndex] instanceof SetOfPossibleFieldAccesses)) 
					&& otherIndex>=accPath.accesses.length-1) {
				if(transitions.isEmpty())
					return otherTransitions.isEmpty() && hasAtLeastTheSameExclusionsAs(accPath);
				for(Transition<T> transition : transitions) {
					for(Transition<T> otherTransition : otherTransitions) {
						MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);
						if(!match.hasMatched())
							return false;
					}	
				}
				return hasAtLeastTheSameExclusionsAs(accPath);
			}

			for(Transition<T> transition : transitions) {
				for(Transition<T> otherTransition : otherTransitions) {
					MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);
					if(match.hasMatched()) {
						if(otherIndex == otherTransition.transitionToIndex())
							continue;
						
						currIndex = transition.transitionToIndex();
						otherIndex = otherTransition.transitionToIndex();
						continue outer;
					}
				}
			}
			return false;
		}
	}
	
	private boolean hasAtLeastTheSameExclusionsAs(AccessPath<T> accPath) {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




411



		return accPath.exclusions.containsAll(exclusions);








subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015




412


413


414


415


416


417


418



	}

	public Collection<String> tokenize() {
		List<String> result = Lists.newLinkedList();
		for(SubAccessPath<T> s : accesses) {
			result.add(s.toString());
		}








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




419


420



		if(!exclusions.isEmpty())
			result.add("^"+Joiner.on(",").join(exclusions));








subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015




421


422



		return result;
	}








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




423


424


425


426




	public AccessPath<T> removeExclusions() {
		return new AccessPath<T>(accesses, Sets.<T>newHashSet());
	}








additional methods for AccessPath required by client FlowFunction

 


Johannes Lerch
committed
Feb 16, 2015




427


428


429


430


431


432


433


434


435


436


437


438


439


440


441


442


443


444


445


446




	public SubAccessPath<T> getFirstAccess() {
		return accesses[0];
	}

	public AccessPath<T> removeRepeatableFirstAccess(T field) {
		Collection<? extends T> elements = accesses[0].elements();
		if(!elements.contains(field))
			throw new IllegalArgumentException();
		
		if(elements.size() == 1) {
			return new AccessPath<>(Arrays.copyOfRange(accesses, 1, accesses.length), exclusions);
		}
		
		HashSet<T> newSet = Sets.newHashSet(elements);
		newSet.remove(field);
		SubAccessPath<T>[] newAccesses = Arrays.copyOf(accesses, accesses.length);
		newAccesses[0] = new SetOfPossibleFieldAccesses<>(newSet);
		return new AccessPath<T>(newAccesses, exclusions);
	}








cleaning code



Johannes Lerch
committed
Jan 07, 2015




447



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









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




13


14


15



import heros.alias.SubAccessPath.SetOfPossibleFieldAccesses;
import heros.alias.SubAccessPath.SpecificFieldAccess;
import heros.alias.Transition.MatchResult;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




16












cleaning code



Johannes Lerch
committed
Jan 07, 2015




17



import java.util.Arrays;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




18



import java.util.Collection;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




19



import java.util.HashSet;








subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015




20



import java.util.List;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




21


22



import java.util.Set;









remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




23



import com.google.common.base.Function;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




24



import com.google.common.base.Joiner;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




25



import com.google.common.collect.Lists;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




26


27


28



import com.google.common.collect.Sets;

@SuppressWarnings("unchecked")








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




29



public class AccessPath<T extends AccessPath.FieldRef<T>> {








cleaning code



Johannes Lerch
committed
Jan 07, 2015




30












merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




31


32


33


34


35



	public static interface FieldRef<F> {
		boolean shouldBeMergedWith(F fieldRef);
	}
	
	public static <T extends FieldRef<T>> AccessPath<T> empty() {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




36


37


38



		return new AccessPath<T>();
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




39



	private final SubAccessPath<T>[] accesses;








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




40



	private final Set<T> exclusions;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




41


42



	
	public AccessPath() {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




43



		accesses = new SubAccessPath[0];








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




44



		exclusions = Sets.newHashSet();








cleaning code



Johannes Lerch
committed
Jan 07, 2015




45


46



	}
	








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




47



	AccessPath(SubAccessPath<T>[] accesses, Set<T> exclusions) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




48


49



		this.accesses = accesses;
		this.exclusions = exclusions;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




50


51



	}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




52



	public boolean isAccessInExclusions(SubAccessPath<T>... fieldReferences) {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




53


54


55


56



		if(fieldReferences.length > 0) {
			for(T field : fieldReferences[0].elements()) {
				if(!exclusions.contains(field))
					return false;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




57


58



			}
			return true;








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




59



		}








cleaning code



Johannes Lerch
committed
Jan 07, 2015




60


61


62



		return false;
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




63



	public AccessPath<T> addFieldReference(SubAccessPath<T>... fieldReferences) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




64


65


66



		return addFieldReference(true, fieldReferences);
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




67



	AccessPath<T> addFieldReference(boolean merge, SubAccessPath<T>... fieldReferences) {








cleaning code



Johannes Lerch
committed
Jan 07, 2015




68



		if(isAccessInExclusions(fieldReferences))








k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




69



			throw new IllegalArgumentException("FieldRef "+Arrays.toString(fieldReferences)+" cannot be added to "+toString());








cleaning code



Johannes Lerch
committed
Jan 07, 2015




70












regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




71


72


73



		if(merge) {
			for(int i=fieldReferences.length-1; i>=0; i--) {
				for(int j=0; j<accesses.length; j++) {








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




74



					if(accesses[j].shouldBeMerged(fieldReferences[i])) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




75


76


77


78


79


80



						// [..., {j-i}, ...]
						
						AccessPathBuilder builder = new AccessPathBuilder(j+fieldReferences.length-i);
						builder.keep(0, j);
						builder.merge(j, accesses.length).mergeWithLast(fieldReferences, 0, i);
						builder.append(fieldReferences, i+1, fieldReferences.length);








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




81



						builder.removeExclusions();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




82


83


84


85


86


87


88


89


90



						return builder.build();
					}
				}
			}
		}
		
		AccessPathBuilder builder = new AccessPathBuilder(accesses.length + fieldReferences.length);
		builder.keep(0, accesses.length);
		builder.append(fieldReferences, 0, fieldReferences.length);








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




91


92



		if(fieldReferences.length>0)
			builder.removeExclusions();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




93


94


95



		return builder.build();
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




96


97



	public AccessPath<T> addFieldReference(T... fieldReferences) {
		SubAccessPath<T>[] subPath = new SubAccessPath[fieldReferences.length];








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




98


99


100


101


102


103


104


105



		for(int i=0; i<fieldReferences.length; i++) {
			subPath[i] = new SpecificFieldAccess<>(fieldReferences[i]);
		}
		return addFieldReference(subPath);
	}
	
	private class AccessPathBuilder {
		








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




106



		private Set<T> newExclusions;








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




107



		private SubAccessPath<T>[] newAccesses;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




108


109


110


111


112


113


114



		private int currentIndex = 0;

		public AccessPathBuilder(int capacity) {
			newAccesses = new SubAccessPath[capacity];
			newExclusions = exclusions;
		}
		








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




115



		public AccessPath<T> build() {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




116


117


118



			return new AccessPath<>(newAccesses, newExclusions);
		}









remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




119


120



		public void removeExclusions() {
			newExclusions = Sets.newHashSet();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




121


122



		}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




123



		public void append(SubAccessPath<T>[] fieldReferences, int start, int endExcl) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




124


125


126


127



			for(int i=start; i<endExcl; i++) {
				newAccesses[currentIndex] = fieldReferences[i];
				currentIndex++;
				if(fieldReferences[i] instanceof SetOfPossibleFieldAccesses)








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




128



					removeExclusions();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




129


130


131


132



			}
			currentIndex+=endExcl-start;
		}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




133



		public void mergeWithLast(SubAccessPath<T>[] fieldReferences, int start, int endExcl) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




134


135


136


137



			newAccesses[currentIndex-1].merge(Arrays.copyOfRange(fieldReferences, start, endExcl));
		}

		public AccessPathBuilder merge(int srcIndex, int destIndexExcl) {








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




138



			Set<T> set = Sets.newHashSet();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




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



			for(int i=srcIndex; i<destIndexExcl; i++) {
				set.addAll(accesses[i].elements());
			}
			newAccesses[currentIndex] = new SubAccessPath.SetOfPossibleFieldAccesses<>(set);
			currentIndex++;
			return this;
		}

		public AccessPathBuilder keep(int srcIndex, int destIndexExcl) {
			System.arraycopy(accesses, srcIndex, newAccesses, currentIndex, destIndexExcl-srcIndex);
			currentIndex += destIndexExcl-srcIndex;
			return this;
		}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




153



		public void append(T fieldRef) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




154


155


156


157



			newAccesses[currentIndex] = new SubAccessPath.SpecificFieldAccess<>(fieldRef);
			currentIndex++;
		}
		








cleaning code



Johannes Lerch
committed
Jan 07, 2015




158


159



	}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




160



	public AccessPath<T> prepend(T fieldRef) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




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



		for(int j=0; j<accesses.length; j++) {
			if(accesses[j].contains(fieldRef)) {
				// [{0-j}, ...]
				
				AccessPathBuilder builder = new AccessPathBuilder(accesses.length-j);
				builder.merge(0, j+1);
				builder.keep(j+1, accesses.length);
				return builder.build();
			}
		}
		AccessPathBuilder builder = new AccessPathBuilder(accesses.length + 1);
		builder.append(fieldRef);
		builder.keep(0, accesses.length);
		return builder.build();








Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




175


176



	}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




177



	public AccessPath<T> removeFirst(T field) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




178


179


180



		for(int i=0; i<accesses.length; i++) {
			if(accesses[i].contains(field)) {
				if(accesses[i] instanceof SpecificFieldAccess)








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




181



					return new AccessPath<T>(Arrays.copyOfRange(accesses, i+1, accesses.length), exclusions);








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




182


183


184


185


186


187


188


189


190


191



				else
					return this;
			}
			else if(accesses[i] instanceof SpecificFieldAccess)
				throw new IllegalStateException("Trying to remove "+field+" from "+this);
		}
		
		throw new IllegalStateException("Trying to remove "+field+" from "+this);
	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




192



	public AccessPath<T> appendExcludedFieldReference(T... fieldReferences) {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




193


194


195



		HashSet<T> newExclusions = Sets.newHashSet(fieldReferences);
		newExclusions.addAll(exclusions);
		return new AccessPath<>(accesses, newExclusions);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




196



	}








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




197


198


199


200


201


202


203


204


205


206


207


208


209


210




	public static enum PrefixTestResult {
		GUARANTEED_PREFIX(2), POTENTIAL_PREFIX(1), NO_PREFIX(0);
		
		private int value;

		private PrefixTestResult(int value) {
			this.value = value;
		}
		
		public boolean atLeast(PrefixTestResult minimum) {
			return value >= minimum.value;
		}
	}








cleaning code



Johannes Lerch
committed
Jan 07, 2015




211



	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




212



	public PrefixTestResult isPrefixOf(AccessPath<T> accPath) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




213


214


215



		int currIndex = 0;
		int otherIndex = 0;
		PrefixTestResult result = PrefixTestResult.GUARANTEED_PREFIX;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




216



		








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




217


218



		int finalIndex = finalIndex();
		outer: while(currIndex < finalIndex) {








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




219


220



			Collection<Transition<T>> transitions = possibleTransitions(currIndex, true);
			Collection<Transition<T>> otherTransitions = accPath.possibleTransitions(otherIndex, true);








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




221












merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




222


223


224



			for(Transition<T> transition : transitions) {
				for(Transition<T> otherTransition : otherTransitions) {
					MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




225


226



					if(match.hasMatched()) {
						if(currIndex == transition.transitionToIndex() && otherIndex == otherTransition.transitionToIndex())








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




227



							continue;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




228


229


230


231


232


233


234


235


236


237



						
						currIndex = transition.transitionToIndex();
						otherIndex = otherTransition.transitionToIndex();
						if(!match.isGuaranteedMatch())
							result = PrefixTestResult.POTENTIAL_PREFIX;
						
						continue outer;
					}
				}
			}








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




238



			return PrefixTestResult.NO_PREFIX;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




239



		}








cleaning code



Johannes Lerch
committed
Jan 07, 2015




240



		








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




241


242


243


244



		return result;
	}
	
	private int finalIndex() {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




245


246



		if(!exclusions.isEmpty())
			return accesses.length + 1;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




247


248



		
		int finalIndex = 0;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




249



		for(int i=0; i<accesses.length; i++) {








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




250


251



			if(accesses[i] instanceof SpecificFieldAccess)
				finalIndex = i+1;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




252



		}








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




253


254


255



		return finalIndex;
	}









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




256


257



	private Collection<Transition<T>> possibleTransitions(int index, boolean addExclusionTransitions) {
		Collection<Transition<T>> result = Lists.newLinkedList();








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




258


259


260



		if(index < accesses.length) {
			if(accesses[index] instanceof SetOfPossibleFieldAccesses) {
				result.add(new Transition.SubAccessPathTransition<>(index, accesses[index]));








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




261



				result.addAll(possibleTransitions(index+1, addExclusionTransitions));








cleaning code



Johannes Lerch
committed
Jan 07, 2015




262



			}








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




263


264



			else
				result.add(new Transition.SubAccessPathTransition<>(index+1, accesses[index]));








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




265


266



		} else if(addExclusionTransitions && index - accesses.length == 0 && !exclusions.isEmpty()) {
			result.add(new Transition.ExclusionPathTransition<T>(index+1, exclusions));








cleaning code



Johannes Lerch
committed
Jan 07, 2015




267



		}








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




268



		return result;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




269


270



	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




271


272


273



	public SubAccessPath<T>[] getDeltaTo(AccessPath<T> accPath) {
		int currIndex = 0;
		int otherIndex = 0;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




274



		








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




275


276


277


278


279


280


281


282


283


284


285


286


287


288


289



		outer: while(true) {
			Collection<Transition<T>> transitions = possibleTransitions(currIndex, false);
			Collection<Transition<T>> otherTransitions = accPath.possibleTransitions(otherIndex, false);

			for(Transition<T> transition : transitions) {
				for(Transition<T> otherTransition : otherTransitions) {
					MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);
					if(match.hasMatched()) {
						if(currIndex == transition.transitionToIndex() && otherIndex == otherTransition.transitionToIndex())
							continue;
						
						currIndex = transition.transitionToIndex();
						otherIndex = otherTransition.transitionToIndex();
						continue outer;
					}








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




290


291



				}
			}








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




292



			break;








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




293


294



		}
		








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




295



		return Arrays.copyOfRange(accPath.accesses, otherIndex, accPath.accesses.length);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




296


297



	}
	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




298



	public AccessPath<T> mergeExcludedFieldReferences(AccessPath<T> accPath) {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




299


300


301



		HashSet<T> newExclusions = Sets.newHashSet(exclusions);
		newExclusions.addAll(accPath.exclusions);
		return new AccessPath<>(accesses, newExclusions);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




302


303



	}
	








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




304


305


306


307



	public boolean mayHaveEmptyAccessPath() {
		return finalIndex() == 0;
	}
	








cleaning code



Johannes Lerch
committed
Jan 07, 2015




308



	public boolean isEmpty() {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




309



		return exclusions.isEmpty() && accesses.length == 0;








cleaning code



Johannes Lerch
committed
Jan 07, 2015




310


311


312


313


314


315


316



	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(accesses);








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




317



		result = prime * result + ((exclusions == null) ? 0 : exclusions.hashCode());








cleaning code



Johannes Lerch
committed
Jan 07, 2015




318


319


320


321


322


323


324


325


326


327


328


329


330


331



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








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




332


333


334


335



		if (exclusions == null) {
			if (other.exclusions != null)
				return false;
		} else if (!exclusions.equals(other.exclusions))








cleaning code



Johannes Lerch
committed
Jan 07, 2015




336


337


338


339


340


341


342



			return false;
		return true;
	}

	@Override
	public String toString() {
		String result = accesses.length > 0 ? "."+Joiner.on(".").join(accesses) : "";








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




343


344



		if(!exclusions.isEmpty())
			result += "^" + Joiner.on(",").join(exclusions);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




345


346


347



		return result;
	}
	








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




348


349


350


351



	public <U extends FieldRef<U>> AccessPath<U> map(Function<T, U> function) {
		SubAccessPath<U>[] newAccesses = new SubAccessPath[accesses.length];
		for(int i=0; i<accesses.length; i++) {
			newAccesses[i] = accesses[i].map(function);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




352



		}








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




353


354


355


356



		Set<U> newExclusions = Sets.newHashSet();
		for(T f : exclusions)
			newExclusions.add(function.apply(f));
		return new AccessPath<U>(newAccesses, newExclusions);








cleaning code



Johannes Lerch
committed
Jan 07, 2015




357



	}








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




358



	








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




359



	public AccessPath<T> removeAnyAccess() {








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




360



		if(accesses.length > 0)








merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




361



			return new AccessPath<T>(new SubAccessPath[0], exclusions);








regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




362


363


364


365


366


367



		else
			return this;
	}

	public boolean hasEmptyAccessPath() {
		return accesses.length == 0;








Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




368



	}








subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015




369


370


371


372


373


374


375


376


377


378


379


380


381


382


383


384


385


386


387


388


389


390


391


392


393


394


395


396


397


398


399


400


401


402


403


404


405


406


407


408


409


410




	public boolean subsumes(AccessPath<T> accPath) {
		int currIndex = 0;
		int otherIndex = 0;
		
		
		outer: while(true) {
			Collection<Transition<T>> transitions = possibleTransitions(currIndex, false);
			Collection<Transition<T>> otherTransitions = accPath.possibleTransitions(otherIndex, false);

			if((currIndex >= accesses.length || (currIndex == accesses.length-1 && accesses[currIndex] instanceof SetOfPossibleFieldAccesses)) 
					&& otherIndex>=accPath.accesses.length-1) {
				if(transitions.isEmpty())
					return otherTransitions.isEmpty() && hasAtLeastTheSameExclusionsAs(accPath);
				for(Transition<T> transition : transitions) {
					for(Transition<T> otherTransition : otherTransitions) {
						MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);
						if(!match.hasMatched())
							return false;
					}	
				}
				return hasAtLeastTheSameExclusionsAs(accPath);
			}

			for(Transition<T> transition : transitions) {
				for(Transition<T> otherTransition : otherTransitions) {
					MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);
					if(match.hasMatched()) {
						if(otherIndex == otherTransition.transitionToIndex())
							continue;
						
						currIndex = transition.transitionToIndex();
						otherIndex = otherTransition.transitionToIndex();
						continue outer;
					}
				}
			}
			return false;
		}
	}
	
	private boolean hasAtLeastTheSameExclusionsAs(AccessPath<T> accPath) {








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




411



		return accPath.exclusions.containsAll(exclusions);








subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015




412


413


414


415


416


417


418



	}

	public Collection<String> tokenize() {
		List<String> result = Lists.newLinkedList();
		for(SubAccessPath<T> s : accesses) {
			result.add(s.toString());
		}








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




419


420



		if(!exclusions.isEmpty())
			result.add("^"+Joiner.on(",").join(exclusions));








subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015




421


422



		return result;
	}








remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




423


424


425


426




	public AccessPath<T> removeExclusions() {
		return new AccessPath<T>(accesses, Sets.<T>newHashSet());
	}








additional methods for AccessPath required by client FlowFunction

 


Johannes Lerch
committed
Feb 16, 2015




427


428


429


430


431


432


433


434


435


436


437


438


439


440


441


442


443


444


445


446




	public SubAccessPath<T> getFirstAccess() {
		return accesses[0];
	}

	public AccessPath<T> removeRepeatableFirstAccess(T field) {
		Collection<? extends T> elements = accesses[0].elements();
		if(!elements.contains(field))
			throw new IllegalArgumentException();
		
		if(elements.size() == 1) {
			return new AccessPath<>(Arrays.copyOfRange(accesses, 1, accesses.length), exclusions);
		}
		
		HashSet<T> newSet = Sets.newHashSet(elements);
		newSet.remove(field);
		SubAccessPath<T>[] newAccesses = Arrays.copyOf(accesses, accesses.length);
		newAccesses[0] = new SetOfPossibleFieldAccesses<>(newSet);
		return new AccessPath<T>(newAccesses, exclusions);
	}








cleaning code



Johannes Lerch
committed
Jan 07, 2015




447



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


/*******************************************************************************/******************************************************************************* * Copyright (c) 2015 Johannes Lerch. * Copyright (c) 2015 Johannes Lerch. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Johannes Lerch - initial API and implementation *     Johannes Lerch - initial API and implementation ******************************************************************************/ ******************************************************************************/package heros.alias;packageheros.alias;




merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




13


14


15



import heros.alias.SubAccessPath.SetOfPossibleFieldAccesses;
import heros.alias.SubAccessPath.SpecificFieldAccess;
import heros.alias.Transition.MatchResult;






merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types

 

merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


13


14


15


import heros.alias.SubAccessPath.SetOfPossibleFieldAccesses;
import heros.alias.SubAccessPath.SpecificFieldAccess;
import heros.alias.Transition.MatchResult;

import heros.alias.SubAccessPath.SetOfPossibleFieldAccesses;importheros.alias.SubAccessPath.SetOfPossibleFieldAccesses;import heros.alias.SubAccessPath.SpecificFieldAccess;importheros.alias.SubAccessPath.SpecificFieldAccess;import heros.alias.Transition.MatchResult;importheros.alias.Transition.MatchResult;




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




16










regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


16









cleaning code



Johannes Lerch
committed
Jan 07, 2015




17



import java.util.Arrays;






cleaning code



Johannes Lerch
committed
Jan 07, 2015



cleaning code


cleaning code

Johannes Lerch
committed
Jan 07, 2015


17


import java.util.Arrays;

import java.util.Arrays;importjava.util.Arrays;




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




18



import java.util.Collection;






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


18


import java.util.Collection;

import java.util.Collection;importjava.util.Collection;




cleaning code



Johannes Lerch
committed
Jan 07, 2015




19



import java.util.HashSet;






cleaning code



Johannes Lerch
committed
Jan 07, 2015



cleaning code


cleaning code

Johannes Lerch
committed
Jan 07, 2015


19


import java.util.HashSet;

import java.util.HashSet;importjava.util.HashSet;




subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015




20



import java.util.List;






subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015



subumption + debugging

 

subumption + debugging

Johannes Lerch
committed
Feb 09, 2015


20


import java.util.List;

import java.util.List;importjava.util.List;




cleaning code



Johannes Lerch
committed
Jan 07, 2015




21


22



import java.util.Set;







cleaning code



Johannes Lerch
committed
Jan 07, 2015



cleaning code


cleaning code

Johannes Lerch
committed
Jan 07, 2015


21


22


import java.util.Set;


import java.util.Set;importjava.util.Set;




remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




23



import com.google.common.base.Function;






remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015



remove ability to nest exclusions

 

remove ability to nest exclusions

Johannes Lerch
committed
Feb 13, 2015


23


import com.google.common.base.Function;

import com.google.common.base.Function;importcom.google.common.base.Function;




cleaning code



Johannes Lerch
committed
Jan 07, 2015




24



import com.google.common.base.Joiner;






cleaning code



Johannes Lerch
committed
Jan 07, 2015



cleaning code


cleaning code

Johannes Lerch
committed
Jan 07, 2015


24


import com.google.common.base.Joiner;

import com.google.common.base.Joiner;importcom.google.common.base.Joiner;




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




25



import com.google.common.collect.Lists;






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


25


import com.google.common.collect.Lists;

import com.google.common.collect.Lists;importcom.google.common.collect.Lists;




cleaning code



Johannes Lerch
committed
Jan 07, 2015




26


27


28



import com.google.common.collect.Sets;

@SuppressWarnings("unchecked")






cleaning code



Johannes Lerch
committed
Jan 07, 2015



cleaning code


cleaning code

Johannes Lerch
committed
Jan 07, 2015


26


27


28


import com.google.common.collect.Sets;

@SuppressWarnings("unchecked")

import com.google.common.collect.Sets;importcom.google.common.collect.Sets;@SuppressWarnings("unchecked")@SuppressWarnings("unchecked")




merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




29



public class AccessPath<T extends AccessPath.FieldRef<T>> {






merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types

 

merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


29


public class AccessPath<T extends AccessPath.FieldRef<T>> {

public class AccessPath<T extends AccessPath.FieldRef<T>> {publicclassAccessPath<TextendsAccessPath.FieldRef<T>>{




cleaning code



Johannes Lerch
committed
Jan 07, 2015




30










cleaning code



Johannes Lerch
committed
Jan 07, 2015



cleaning code


cleaning code

Johannes Lerch
committed
Jan 07, 2015


30









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




31


32


33


34


35



	public static interface FieldRef<F> {
		boolean shouldBeMergedWith(F fieldRef);
	}
	
	public static <T extends FieldRef<T>> AccessPath<T> empty() {






merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types

 

merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


31


32


33


34


35


	public static interface FieldRef<F> {
		boolean shouldBeMergedWith(F fieldRef);
	}
	
	public static <T extends FieldRef<T>> AccessPath<T> empty() {

	public static interface FieldRef<F> {publicstaticinterfaceFieldRef<F>{		boolean shouldBeMergedWith(F fieldRef);booleanshouldBeMergedWith(FfieldRef);	}}		public static <T extends FieldRef<T>> AccessPath<T> empty() {publicstatic<TextendsFieldRef<T>>AccessPath<T>empty(){




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




36


37


38



		return new AccessPath<T>();
	}
	






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


36


37


38


		return new AccessPath<T>();
	}
	

		return new AccessPath<T>();returnnewAccessPath<T>();	}}	




merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




39



	private final SubAccessPath<T>[] accesses;






merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types

 

merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


39


	private final SubAccessPath<T>[] accesses;

	private final SubAccessPath<T>[] accesses;privatefinalSubAccessPath<T>[]accesses;




remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




40



	private final Set<T> exclusions;






remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015



remove ability to nest exclusions

 

remove ability to nest exclusions

Johannes Lerch
committed
Feb 13, 2015


40


	private final Set<T> exclusions;

	private final Set<T> exclusions;privatefinalSet<T>exclusions;




cleaning code



Johannes Lerch
committed
Jan 07, 2015




41


42



	
	public AccessPath() {






cleaning code



Johannes Lerch
committed
Jan 07, 2015



cleaning code


cleaning code

Johannes Lerch
committed
Jan 07, 2015


41


42


	
	public AccessPath() {

		public AccessPath() {publicAccessPath(){




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




43



		accesses = new SubAccessPath[0];






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


43


		accesses = new SubAccessPath[0];

		accesses = new SubAccessPath[0];accesses=newSubAccessPath[0];




remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




44



		exclusions = Sets.newHashSet();






remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015



remove ability to nest exclusions

 

remove ability to nest exclusions

Johannes Lerch
committed
Feb 13, 2015


44


		exclusions = Sets.newHashSet();

		exclusions = Sets.newHashSet();exclusions=Sets.newHashSet();




cleaning code



Johannes Lerch
committed
Jan 07, 2015




45


46



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


45


46


	}
	

	}}	




remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




47



	AccessPath(SubAccessPath<T>[] accesses, Set<T> exclusions) {






remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015



remove ability to nest exclusions

 

remove ability to nest exclusions

Johannes Lerch
committed
Feb 13, 2015


47


	AccessPath(SubAccessPath<T>[] accesses, Set<T> exclusions) {

	AccessPath(SubAccessPath<T>[] accesses, Set<T> exclusions) {AccessPath(SubAccessPath<T>[]accesses,Set<T>exclusions){




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




48


49



		this.accesses = accesses;
		this.exclusions = exclusions;






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


48


49


		this.accesses = accesses;
		this.exclusions = exclusions;

		this.accesses = accesses;this.accesses=accesses;		this.exclusions = exclusions;this.exclusions=exclusions;




cleaning code



Johannes Lerch
committed
Jan 07, 2015




50


51



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


50


51


	}


	}}




merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




52



	public boolean isAccessInExclusions(SubAccessPath<T>... fieldReferences) {






merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types

 

merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


52


	public boolean isAccessInExclusions(SubAccessPath<T>... fieldReferences) {

	public boolean isAccessInExclusions(SubAccessPath<T>... fieldReferences) {publicbooleanisAccessInExclusions(SubAccessPath<T>...fieldReferences){




remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




53


54


55


56



		if(fieldReferences.length > 0) {
			for(T field : fieldReferences[0].elements()) {
				if(!exclusions.contains(field))
					return false;






remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015



remove ability to nest exclusions

 

remove ability to nest exclusions

Johannes Lerch
committed
Feb 13, 2015


53


54


55


56


		if(fieldReferences.length > 0) {
			for(T field : fieldReferences[0].elements()) {
				if(!exclusions.contains(field))
					return false;

		if(fieldReferences.length > 0) {if(fieldReferences.length>0){			for(T field : fieldReferences[0].elements()) {for(Tfield:fieldReferences[0].elements()){				if(!exclusions.contains(field))if(!exclusions.contains(field))					return false;returnfalse;




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




57


58



			}
			return true;






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


57


58


			}
			return true;

			}}			return true;returntrue;




remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




59



		}






remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015



remove ability to nest exclusions

 

remove ability to nest exclusions

Johannes Lerch
committed
Feb 13, 2015


59


		}

		}}




cleaning code



Johannes Lerch
committed
Jan 07, 2015




60


61


62



		return false;
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


60


61


62


		return false;
	}
	

		return false;returnfalse;	}}	




merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




63



	public AccessPath<T> addFieldReference(SubAccessPath<T>... fieldReferences) {






merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types

 

merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


63


	public AccessPath<T> addFieldReference(SubAccessPath<T>... fieldReferences) {

	public AccessPath<T> addFieldReference(SubAccessPath<T>... fieldReferences) {publicAccessPath<T>addFieldReference(SubAccessPath<T>...fieldReferences){




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




64


65


66



		return addFieldReference(true, fieldReferences);
	}
	






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


64


65


66


		return addFieldReference(true, fieldReferences);
	}
	

		return addFieldReference(true, fieldReferences);returnaddFieldReference(true,fieldReferences);	}}	




merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




67



	AccessPath<T> addFieldReference(boolean merge, SubAccessPath<T>... fieldReferences) {






merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types

 

merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


67


	AccessPath<T> addFieldReference(boolean merge, SubAccessPath<T>... fieldReferences) {

	AccessPath<T> addFieldReference(boolean merge, SubAccessPath<T>... fieldReferences) {AccessPath<T>addFieldReference(booleanmerge,SubAccessPath<T>...fieldReferences){




cleaning code



Johannes Lerch
committed
Jan 07, 2015




68



		if(isAccessInExclusions(fieldReferences))






cleaning code



Johannes Lerch
committed
Jan 07, 2015



cleaning code


cleaning code

Johannes Lerch
committed
Jan 07, 2015


68


		if(isAccessInExclusions(fieldReferences))

		if(isAccessInExclusions(fieldReferences))if(isAccessInExclusions(fieldReferences))




k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015




69



			throw new IllegalArgumentException("FieldRef "+Arrays.toString(fieldReferences)+" cannot be added to "+toString());






k-limitting; fix in constraint propagation

 


Johannes Lerch
committed
Jan 14, 2015



k-limitting; fix in constraint propagation

 

k-limitting; fix in constraint propagation

Johannes Lerch
committed
Jan 14, 2015


69


			throw new IllegalArgumentException("FieldRef "+Arrays.toString(fieldReferences)+" cannot be added to "+toString());

			throw new IllegalArgumentException("FieldRef "+Arrays.toString(fieldReferences)+" cannot be added to "+toString());thrownewIllegalArgumentException("FieldRef "+Arrays.toString(fieldReferences)+" cannot be added to "+toString());




cleaning code



Johannes Lerch
committed
Jan 07, 2015




70










cleaning code



Johannes Lerch
committed
Jan 07, 2015



cleaning code


cleaning code

Johannes Lerch
committed
Jan 07, 2015


70









regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




71


72


73



		if(merge) {
			for(int i=fieldReferences.length-1; i>=0; i--) {
				for(int j=0; j<accesses.length; j++) {






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


71


72


73


		if(merge) {
			for(int i=fieldReferences.length-1; i>=0; i--) {
				for(int j=0; j<accesses.length; j++) {

		if(merge) {if(merge){			for(int i=fieldReferences.length-1; i>=0; i--) {for(inti=fieldReferences.length-1;i>=0;i--){				for(int j=0; j<accesses.length; j++) {for(intj=0;j<accesses.length;j++){




merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




74



					if(accesses[j].shouldBeMerged(fieldReferences[i])) {






merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types

 

merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


74


					if(accesses[j].shouldBeMerged(fieldReferences[i])) {

					if(accesses[j].shouldBeMerged(fieldReferences[i])) {if(accesses[j].shouldBeMerged(fieldReferences[i])){




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




75


76


77


78


79


80



						// [..., {j-i}, ...]
						
						AccessPathBuilder builder = new AccessPathBuilder(j+fieldReferences.length-i);
						builder.keep(0, j);
						builder.merge(j, accesses.length).mergeWithLast(fieldReferences, 0, i);
						builder.append(fieldReferences, i+1, fieldReferences.length);






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


75


76


77


78


79


80


						// [..., {j-i}, ...]
						
						AccessPathBuilder builder = new AccessPathBuilder(j+fieldReferences.length-i);
						builder.keep(0, j);
						builder.merge(j, accesses.length).mergeWithLast(fieldReferences, 0, i);
						builder.append(fieldReferences, i+1, fieldReferences.length);

						// [..., {j-i}, ...]// [..., {j-i}, ...]												AccessPathBuilder builder = new AccessPathBuilder(j+fieldReferences.length-i);AccessPathBuilderbuilder=newAccessPathBuilder(j+fieldReferences.length-i);						builder.keep(0, j);builder.keep(0,j);						builder.merge(j, accesses.length).mergeWithLast(fieldReferences, 0, i);builder.merge(j,accesses.length).mergeWithLast(fieldReferences,0,i);						builder.append(fieldReferences, i+1, fieldReferences.length);builder.append(fieldReferences,i+1,fieldReferences.length);




remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




81



						builder.removeExclusions();






remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015



remove ability to nest exclusions

 

remove ability to nest exclusions

Johannes Lerch
committed
Feb 13, 2015


81


						builder.removeExclusions();

						builder.removeExclusions();builder.removeExclusions();




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




82


83


84


85


86


87


88


89


90



						return builder.build();
					}
				}
			}
		}
		
		AccessPathBuilder builder = new AccessPathBuilder(accesses.length + fieldReferences.length);
		builder.keep(0, accesses.length);
		builder.append(fieldReferences, 0, fieldReferences.length);






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


82


83


84


85


86


87


88


89


90


						return builder.build();
					}
				}
			}
		}
		
		AccessPathBuilder builder = new AccessPathBuilder(accesses.length + fieldReferences.length);
		builder.keep(0, accesses.length);
		builder.append(fieldReferences, 0, fieldReferences.length);

						return builder.build();returnbuilder.build();					}}				}}			}}		}}				AccessPathBuilder builder = new AccessPathBuilder(accesses.length + fieldReferences.length);AccessPathBuilderbuilder=newAccessPathBuilder(accesses.length+fieldReferences.length);		builder.keep(0, accesses.length);builder.keep(0,accesses.length);		builder.append(fieldReferences, 0, fieldReferences.length);builder.append(fieldReferences,0,fieldReferences.length);




remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




91


92



		if(fieldReferences.length>0)
			builder.removeExclusions();






remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015



remove ability to nest exclusions

 

remove ability to nest exclusions

Johannes Lerch
committed
Feb 13, 2015


91


92


		if(fieldReferences.length>0)
			builder.removeExclusions();

		if(fieldReferences.length>0)if(fieldReferences.length>0)			builder.removeExclusions();builder.removeExclusions();




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




93


94


95



		return builder.build();
	}
	






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


93


94


95


		return builder.build();
	}
	

		return builder.build();returnbuilder.build();	}}	




merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




96


97



	public AccessPath<T> addFieldReference(T... fieldReferences) {
		SubAccessPath<T>[] subPath = new SubAccessPath[fieldReferences.length];






merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types

 

merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


96


97


	public AccessPath<T> addFieldReference(T... fieldReferences) {
		SubAccessPath<T>[] subPath = new SubAccessPath[fieldReferences.length];

	public AccessPath<T> addFieldReference(T... fieldReferences) {publicAccessPath<T>addFieldReference(T...fieldReferences){		SubAccessPath<T>[] subPath = new SubAccessPath[fieldReferences.length];SubAccessPath<T>[]subPath=newSubAccessPath[fieldReferences.length];




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




98


99


100


101


102


103


104


105



		for(int i=0; i<fieldReferences.length; i++) {
			subPath[i] = new SpecificFieldAccess<>(fieldReferences[i]);
		}
		return addFieldReference(subPath);
	}
	
	private class AccessPathBuilder {
		






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


98


99


100


101


102


103


104


105


		for(int i=0; i<fieldReferences.length; i++) {
			subPath[i] = new SpecificFieldAccess<>(fieldReferences[i]);
		}
		return addFieldReference(subPath);
	}
	
	private class AccessPathBuilder {
		

		for(int i=0; i<fieldReferences.length; i++) {for(inti=0;i<fieldReferences.length;i++){			subPath[i] = new SpecificFieldAccess<>(fieldReferences[i]);subPath[i]=newSpecificFieldAccess<>(fieldReferences[i]);		}}		return addFieldReference(subPath);returnaddFieldReference(subPath);	}}		private class AccessPathBuilder {privateclassAccessPathBuilder{		




remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




106



		private Set<T> newExclusions;






remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015



remove ability to nest exclusions

 

remove ability to nest exclusions

Johannes Lerch
committed
Feb 13, 2015


106


		private Set<T> newExclusions;

		private Set<T> newExclusions;privateSet<T>newExclusions;




merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




107



		private SubAccessPath<T>[] newAccesses;






merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types

 

merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


107


		private SubAccessPath<T>[] newAccesses;

		private SubAccessPath<T>[] newAccesses;privateSubAccessPath<T>[]newAccesses;




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




108


109


110


111


112


113


114



		private int currentIndex = 0;

		public AccessPathBuilder(int capacity) {
			newAccesses = new SubAccessPath[capacity];
			newExclusions = exclusions;
		}
		






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


108


109


110


111


112


113


114


		private int currentIndex = 0;

		public AccessPathBuilder(int capacity) {
			newAccesses = new SubAccessPath[capacity];
			newExclusions = exclusions;
		}
		

		private int currentIndex = 0;privateintcurrentIndex=0;		public AccessPathBuilder(int capacity) {publicAccessPathBuilder(intcapacity){			newAccesses = new SubAccessPath[capacity];newAccesses=newSubAccessPath[capacity];			newExclusions = exclusions;newExclusions=exclusions;		}}		




merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




115



		public AccessPath<T> build() {






merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types

 

merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


115


		public AccessPath<T> build() {

		public AccessPath<T> build() {publicAccessPath<T>build(){




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




116


117


118



			return new AccessPath<>(newAccesses, newExclusions);
		}







regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


116


117


118


			return new AccessPath<>(newAccesses, newExclusions);
		}


			return new AccessPath<>(newAccesses, newExclusions);returnnewAccessPath<>(newAccesses,newExclusions);		}}




remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




119


120



		public void removeExclusions() {
			newExclusions = Sets.newHashSet();






remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015



remove ability to nest exclusions

 

remove ability to nest exclusions

Johannes Lerch
committed
Feb 13, 2015


119


120


		public void removeExclusions() {
			newExclusions = Sets.newHashSet();

		public void removeExclusions() {publicvoidremoveExclusions(){			newExclusions = Sets.newHashSet();newExclusions=Sets.newHashSet();




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




121


122



		}







regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


121


122


		}


		}}




merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




123



		public void append(SubAccessPath<T>[] fieldReferences, int start, int endExcl) {






merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types

 

merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


123


		public void append(SubAccessPath<T>[] fieldReferences, int start, int endExcl) {

		public void append(SubAccessPath<T>[] fieldReferences, int start, int endExcl) {publicvoidappend(SubAccessPath<T>[]fieldReferences,intstart,intendExcl){




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




124


125


126


127



			for(int i=start; i<endExcl; i++) {
				newAccesses[currentIndex] = fieldReferences[i];
				currentIndex++;
				if(fieldReferences[i] instanceof SetOfPossibleFieldAccesses)






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


124


125


126


127


			for(int i=start; i<endExcl; i++) {
				newAccesses[currentIndex] = fieldReferences[i];
				currentIndex++;
				if(fieldReferences[i] instanceof SetOfPossibleFieldAccesses)

			for(int i=start; i<endExcl; i++) {for(inti=start;i<endExcl;i++){				newAccesses[currentIndex] = fieldReferences[i];newAccesses[currentIndex]=fieldReferences[i];				currentIndex++;currentIndex++;				if(fieldReferences[i] instanceof SetOfPossibleFieldAccesses)if(fieldReferences[i]instanceofSetOfPossibleFieldAccesses)




remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




128



					removeExclusions();






remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015



remove ability to nest exclusions

 

remove ability to nest exclusions

Johannes Lerch
committed
Feb 13, 2015


128


					removeExclusions();

					removeExclusions();removeExclusions();




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




129


130


131


132



			}
			currentIndex+=endExcl-start;
		}







regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


129


130


131


132


			}
			currentIndex+=endExcl-start;
		}


			}}			currentIndex+=endExcl-start;currentIndex+=endExcl-start;		}}




merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




133



		public void mergeWithLast(SubAccessPath<T>[] fieldReferences, int start, int endExcl) {






merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types

 

merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


133


		public void mergeWithLast(SubAccessPath<T>[] fieldReferences, int start, int endExcl) {

		public void mergeWithLast(SubAccessPath<T>[] fieldReferences, int start, int endExcl) {publicvoidmergeWithLast(SubAccessPath<T>[]fieldReferences,intstart,intendExcl){




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




134


135


136


137



			newAccesses[currentIndex-1].merge(Arrays.copyOfRange(fieldReferences, start, endExcl));
		}

		public AccessPathBuilder merge(int srcIndex, int destIndexExcl) {






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


134


135


136


137


			newAccesses[currentIndex-1].merge(Arrays.copyOfRange(fieldReferences, start, endExcl));
		}

		public AccessPathBuilder merge(int srcIndex, int destIndexExcl) {

			newAccesses[currentIndex-1].merge(Arrays.copyOfRange(fieldReferences, start, endExcl));newAccesses[currentIndex-1].merge(Arrays.copyOfRange(fieldReferences,start,endExcl));		}}		public AccessPathBuilder merge(int srcIndex, int destIndexExcl) {publicAccessPathBuildermerge(intsrcIndex,intdestIndexExcl){




merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




138



			Set<T> set = Sets.newHashSet();






merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types

 

merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


138


			Set<T> set = Sets.newHashSet();

			Set<T> set = Sets.newHashSet();Set<T>set=Sets.newHashSet();




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




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



			for(int i=srcIndex; i<destIndexExcl; i++) {
				set.addAll(accesses[i].elements());
			}
			newAccesses[currentIndex] = new SubAccessPath.SetOfPossibleFieldAccesses<>(set);
			currentIndex++;
			return this;
		}

		public AccessPathBuilder keep(int srcIndex, int destIndexExcl) {
			System.arraycopy(accesses, srcIndex, newAccesses, currentIndex, destIndexExcl-srcIndex);
			currentIndex += destIndexExcl-srcIndex;
			return this;
		}







regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


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


			for(int i=srcIndex; i<destIndexExcl; i++) {
				set.addAll(accesses[i].elements());
			}
			newAccesses[currentIndex] = new SubAccessPath.SetOfPossibleFieldAccesses<>(set);
			currentIndex++;
			return this;
		}

		public AccessPathBuilder keep(int srcIndex, int destIndexExcl) {
			System.arraycopy(accesses, srcIndex, newAccesses, currentIndex, destIndexExcl-srcIndex);
			currentIndex += destIndexExcl-srcIndex;
			return this;
		}


			for(int i=srcIndex; i<destIndexExcl; i++) {for(inti=srcIndex;i<destIndexExcl;i++){				set.addAll(accesses[i].elements());set.addAll(accesses[i].elements());			}}			newAccesses[currentIndex] = new SubAccessPath.SetOfPossibleFieldAccesses<>(set);newAccesses[currentIndex]=newSubAccessPath.SetOfPossibleFieldAccesses<>(set);			currentIndex++;currentIndex++;			return this;returnthis;		}}		public AccessPathBuilder keep(int srcIndex, int destIndexExcl) {publicAccessPathBuilderkeep(intsrcIndex,intdestIndexExcl){			System.arraycopy(accesses, srcIndex, newAccesses, currentIndex, destIndexExcl-srcIndex);System.arraycopy(accesses,srcIndex,newAccesses,currentIndex,destIndexExcl-srcIndex);			currentIndex += destIndexExcl-srcIndex;currentIndex+=destIndexExcl-srcIndex;			return this;returnthis;		}}




merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




153



		public void append(T fieldRef) {






merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types

 

merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


153


		public void append(T fieldRef) {

		public void append(T fieldRef) {publicvoidappend(TfieldRef){




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




154


155


156


157



			newAccesses[currentIndex] = new SubAccessPath.SpecificFieldAccess<>(fieldRef);
			currentIndex++;
		}
		






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


154


155


156


157


			newAccesses[currentIndex] = new SubAccessPath.SpecificFieldAccess<>(fieldRef);
			currentIndex++;
		}
		

			newAccesses[currentIndex] = new SubAccessPath.SpecificFieldAccess<>(fieldRef);newAccesses[currentIndex]=newSubAccessPath.SpecificFieldAccess<>(fieldRef);			currentIndex++;currentIndex++;		}}		




cleaning code



Johannes Lerch
committed
Jan 07, 2015




158


159



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


158


159


	}


	}}




merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




160



	public AccessPath<T> prepend(T fieldRef) {






merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types

 

merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


160


	public AccessPath<T> prepend(T fieldRef) {

	public AccessPath<T> prepend(T fieldRef) {publicAccessPath<T>prepend(TfieldRef){




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




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



		for(int j=0; j<accesses.length; j++) {
			if(accesses[j].contains(fieldRef)) {
				// [{0-j}, ...]
				
				AccessPathBuilder builder = new AccessPathBuilder(accesses.length-j);
				builder.merge(0, j+1);
				builder.keep(j+1, accesses.length);
				return builder.build();
			}
		}
		AccessPathBuilder builder = new AccessPathBuilder(accesses.length + 1);
		builder.append(fieldRef);
		builder.keep(0, accesses.length);
		return builder.build();






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


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


		for(int j=0; j<accesses.length; j++) {
			if(accesses[j].contains(fieldRef)) {
				// [{0-j}, ...]
				
				AccessPathBuilder builder = new AccessPathBuilder(accesses.length-j);
				builder.merge(0, j+1);
				builder.keep(j+1, accesses.length);
				return builder.build();
			}
		}
		AccessPathBuilder builder = new AccessPathBuilder(accesses.length + 1);
		builder.append(fieldRef);
		builder.keep(0, accesses.length);
		return builder.build();

		for(int j=0; j<accesses.length; j++) {for(intj=0;j<accesses.length;j++){			if(accesses[j].contains(fieldRef)) {if(accesses[j].contains(fieldRef)){				// [{0-j}, ...]// [{0-j}, ...]								AccessPathBuilder builder = new AccessPathBuilder(accesses.length-j);AccessPathBuilderbuilder=newAccessPathBuilder(accesses.length-j);				builder.merge(0, j+1);builder.merge(0,j+1);				builder.keep(j+1, accesses.length);builder.keep(j+1,accesses.length);				return builder.build();returnbuilder.build();			}}		}}		AccessPathBuilder builder = new AccessPathBuilder(accesses.length + 1);AccessPathBuilderbuilder=newAccessPathBuilder(accesses.length+1);		builder.append(fieldRef);builder.append(fieldRef);		builder.keep(0, accesses.length);builder.keep(0,accesses.length);		return builder.build();returnbuilder.build();




Bugfix, Helper functions, and renaming

 


Johannes Lerch
committed
Jan 08, 2015




175


176



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


175


176


	}


	}}




merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




177



	public AccessPath<T> removeFirst(T field) {






merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types

 

merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


177


	public AccessPath<T> removeFirst(T field) {

	public AccessPath<T> removeFirst(T field) {publicAccessPath<T>removeFirst(Tfield){




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




178


179


180



		for(int i=0; i<accesses.length; i++) {
			if(accesses[i].contains(field)) {
				if(accesses[i] instanceof SpecificFieldAccess)






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


178


179


180


		for(int i=0; i<accesses.length; i++) {
			if(accesses[i].contains(field)) {
				if(accesses[i] instanceof SpecificFieldAccess)

		for(int i=0; i<accesses.length; i++) {for(inti=0;i<accesses.length;i++){			if(accesses[i].contains(field)) {if(accesses[i].contains(field)){				if(accesses[i] instanceof SpecificFieldAccess)if(accesses[i]instanceofSpecificFieldAccess)




merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




181



					return new AccessPath<T>(Arrays.copyOfRange(accesses, i+1, accesses.length), exclusions);






merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types

 

merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


181


					return new AccessPath<T>(Arrays.copyOfRange(accesses, i+1, accesses.length), exclusions);

					return new AccessPath<T>(Arrays.copyOfRange(accesses, i+1, accesses.length), exclusions);returnnewAccessPath<T>(Arrays.copyOfRange(accesses,i+1,accesses.length),exclusions);




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




182


183


184


185


186


187


188


189


190


191



				else
					return this;
			}
			else if(accesses[i] instanceof SpecificFieldAccess)
				throw new IllegalStateException("Trying to remove "+field+" from "+this);
		}
		
		throw new IllegalStateException("Trying to remove "+field+" from "+this);
	}
	






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


182


183


184


185


186


187


188


189


190


191


				else
					return this;
			}
			else if(accesses[i] instanceof SpecificFieldAccess)
				throw new IllegalStateException("Trying to remove "+field+" from "+this);
		}
		
		throw new IllegalStateException("Trying to remove "+field+" from "+this);
	}
	

				elseelse					return this;returnthis;			}}			else if(accesses[i] instanceof SpecificFieldAccess)elseif(accesses[i]instanceofSpecificFieldAccess)				throw new IllegalStateException("Trying to remove "+field+" from "+this);thrownewIllegalStateException("Trying to remove "+field+" from "+this);		}}				throw new IllegalStateException("Trying to remove "+field+" from "+this);thrownewIllegalStateException("Trying to remove "+field+" from "+this);	}}	




merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




192



	public AccessPath<T> appendExcludedFieldReference(T... fieldReferences) {






merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types

 

merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


192


	public AccessPath<T> appendExcludedFieldReference(T... fieldReferences) {

	public AccessPath<T> appendExcludedFieldReference(T... fieldReferences) {publicAccessPath<T>appendExcludedFieldReference(T...fieldReferences){




remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




193


194


195



		HashSet<T> newExclusions = Sets.newHashSet(fieldReferences);
		newExclusions.addAll(exclusions);
		return new AccessPath<>(accesses, newExclusions);






remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015



remove ability to nest exclusions

 

remove ability to nest exclusions

Johannes Lerch
committed
Feb 13, 2015


193


194


195


		HashSet<T> newExclusions = Sets.newHashSet(fieldReferences);
		newExclusions.addAll(exclusions);
		return new AccessPath<>(accesses, newExclusions);

		HashSet<T> newExclusions = Sets.newHashSet(fieldReferences);HashSet<T>newExclusions=Sets.newHashSet(fieldReferences);		newExclusions.addAll(exclusions);newExclusions.addAll(exclusions);		return new AccessPath<>(accesses, newExclusions);returnnewAccessPath<>(accesses,newExclusions);




cleaning code



Johannes Lerch
committed
Jan 07, 2015




196



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


196


	}

	}}




Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




197


198


199


200


201


202


203


204


205


206


207


208


209


210




	public static enum PrefixTestResult {
		GUARANTEED_PREFIX(2), POTENTIAL_PREFIX(1), NO_PREFIX(0);
		
		private int value;

		private PrefixTestResult(int value) {
			this.value = value;
		}
		
		public boolean atLeast(PrefixTestResult minimum) {
			return value >= minimum.value;
		}
	}






Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015



Split prefix check into potential and guaranteed prefix

 

Split prefix check into potential and guaranteed prefix

Johannes Lerch
committed
Jan 22, 2015


197


198


199


200


201


202


203


204


205


206


207


208


209


210



	public static enum PrefixTestResult {
		GUARANTEED_PREFIX(2), POTENTIAL_PREFIX(1), NO_PREFIX(0);
		
		private int value;

		private PrefixTestResult(int value) {
			this.value = value;
		}
		
		public boolean atLeast(PrefixTestResult minimum) {
			return value >= minimum.value;
		}
	}

	public static enum PrefixTestResult {publicstaticenumPrefixTestResult{		GUARANTEED_PREFIX(2), POTENTIAL_PREFIX(1), NO_PREFIX(0);GUARANTEED_PREFIX(2),POTENTIAL_PREFIX(1),NO_PREFIX(0);				private int value;privateintvalue;		private PrefixTestResult(int value) {privatePrefixTestResult(intvalue){			this.value = value;this.value=value;		}}				public boolean atLeast(PrefixTestResult minimum) {publicbooleanatLeast(PrefixTestResultminimum){			return value >= minimum.value;returnvalue>=minimum.value;		}}	}}




cleaning code



Johannes Lerch
committed
Jan 07, 2015




211



	






cleaning code



Johannes Lerch
committed
Jan 07, 2015



cleaning code


cleaning code

Johannes Lerch
committed
Jan 07, 2015


211


	

	




merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




212



	public PrefixTestResult isPrefixOf(AccessPath<T> accPath) {






merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types

 

merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


212


	public PrefixTestResult isPrefixOf(AccessPath<T> accPath) {

	public PrefixTestResult isPrefixOf(AccessPath<T> accPath) {publicPrefixTestResultisPrefixOf(AccessPath<T>accPath){




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




213


214


215



		int currIndex = 0;
		int otherIndex = 0;
		PrefixTestResult result = PrefixTestResult.GUARANTEED_PREFIX;






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


213


214


215


		int currIndex = 0;
		int otherIndex = 0;
		PrefixTestResult result = PrefixTestResult.GUARANTEED_PREFIX;

		int currIndex = 0;intcurrIndex=0;		int otherIndex = 0;intotherIndex=0;		PrefixTestResult result = PrefixTestResult.GUARANTEED_PREFIX;PrefixTestResultresult=PrefixTestResult.GUARANTEED_PREFIX;




cleaning code



Johannes Lerch
committed
Jan 07, 2015




216



		






cleaning code



Johannes Lerch
committed
Jan 07, 2015



cleaning code


cleaning code

Johannes Lerch
committed
Jan 07, 2015


216


		

		




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




217


218



		int finalIndex = finalIndex();
		outer: while(currIndex < finalIndex) {






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


217


218


		int finalIndex = finalIndex();
		outer: while(currIndex < finalIndex) {

		int finalIndex = finalIndex();intfinalIndex=finalIndex();		outer: while(currIndex < finalIndex) {outer:while(currIndex<finalIndex){




merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




219


220



			Collection<Transition<T>> transitions = possibleTransitions(currIndex, true);
			Collection<Transition<T>> otherTransitions = accPath.possibleTransitions(otherIndex, true);






merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types

 

merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


219


220


			Collection<Transition<T>> transitions = possibleTransitions(currIndex, true);
			Collection<Transition<T>> otherTransitions = accPath.possibleTransitions(otherIndex, true);

			Collection<Transition<T>> transitions = possibleTransitions(currIndex, true);Collection<Transition<T>>transitions=possibleTransitions(currIndex,true);			Collection<Transition<T>> otherTransitions = accPath.possibleTransitions(otherIndex, true);Collection<Transition<T>>otherTransitions=accPath.possibleTransitions(otherIndex,true);




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




221










regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


221









merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




222


223


224



			for(Transition<T> transition : transitions) {
				for(Transition<T> otherTransition : otherTransitions) {
					MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);






merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types

 

merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


222


223


224


			for(Transition<T> transition : transitions) {
				for(Transition<T> otherTransition : otherTransitions) {
					MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);

			for(Transition<T> transition : transitions) {for(Transition<T>transition:transitions){				for(Transition<T> otherTransition : otherTransitions) {for(Transition<T>otherTransition:otherTransitions){					MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);MatchResult<Transition<T>>match=transition.isPrefixMatchOf(otherTransition);




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




225


226



					if(match.hasMatched()) {
						if(currIndex == transition.transitionToIndex() && otherIndex == otherTransition.transitionToIndex())






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


225


226


					if(match.hasMatched()) {
						if(currIndex == transition.transitionToIndex() && otherIndex == otherTransition.transitionToIndex())

					if(match.hasMatched()) {if(match.hasMatched()){						if(currIndex == transition.transitionToIndex() && otherIndex == otherTransition.transitionToIndex())if(currIndex==transition.transitionToIndex()&&otherIndex==otherTransition.transitionToIndex())




merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




227



							continue;






merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types

 

merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


227


							continue;

							continue;continue;




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




228


229


230


231


232


233


234


235


236


237



						
						currIndex = transition.transitionToIndex();
						otherIndex = otherTransition.transitionToIndex();
						if(!match.isGuaranteedMatch())
							result = PrefixTestResult.POTENTIAL_PREFIX;
						
						continue outer;
					}
				}
			}






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


228


229


230


231


232


233


234


235


236


237


						
						currIndex = transition.transitionToIndex();
						otherIndex = otherTransition.transitionToIndex();
						if(!match.isGuaranteedMatch())
							result = PrefixTestResult.POTENTIAL_PREFIX;
						
						continue outer;
					}
				}
			}

												currIndex = transition.transitionToIndex();currIndex=transition.transitionToIndex();						otherIndex = otherTransition.transitionToIndex();otherIndex=otherTransition.transitionToIndex();						if(!match.isGuaranteedMatch())if(!match.isGuaranteedMatch())							result = PrefixTestResult.POTENTIAL_PREFIX;result=PrefixTestResult.POTENTIAL_PREFIX;												continue outer;continueouter;					}}				}}			}}




Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




238



			return PrefixTestResult.NO_PREFIX;






Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015



Split prefix check into potential and guaranteed prefix

 

Split prefix check into potential and guaranteed prefix

Johannes Lerch
committed
Jan 22, 2015


238


			return PrefixTestResult.NO_PREFIX;

			return PrefixTestResult.NO_PREFIX;returnPrefixTestResult.NO_PREFIX;




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




239



		}






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


239


		}

		}}




cleaning code



Johannes Lerch
committed
Jan 07, 2015




240



		






cleaning code



Johannes Lerch
committed
Jan 07, 2015



cleaning code


cleaning code

Johannes Lerch
committed
Jan 07, 2015


240


		

		




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




241


242


243


244



		return result;
	}
	
	private int finalIndex() {






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


241


242


243


244


		return result;
	}
	
	private int finalIndex() {

		return result;returnresult;	}}		private int finalIndex() {privateintfinalIndex(){




remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




245


246



		if(!exclusions.isEmpty())
			return accesses.length + 1;






remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015



remove ability to nest exclusions

 

remove ability to nest exclusions

Johannes Lerch
committed
Feb 13, 2015


245


246


		if(!exclusions.isEmpty())
			return accesses.length + 1;

		if(!exclusions.isEmpty())if(!exclusions.isEmpty())			return accesses.length + 1;returnaccesses.length+1;




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




247


248



		
		int finalIndex = 0;






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


247


248


		
		int finalIndex = 0;

				int finalIndex = 0;intfinalIndex=0;




cleaning code



Johannes Lerch
committed
Jan 07, 2015




249



		for(int i=0; i<accesses.length; i++) {






cleaning code



Johannes Lerch
committed
Jan 07, 2015



cleaning code


cleaning code

Johannes Lerch
committed
Jan 07, 2015


249


		for(int i=0; i<accesses.length; i++) {

		for(int i=0; i<accesses.length; i++) {for(inti=0;i<accesses.length;i++){




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




250


251



			if(accesses[i] instanceof SpecificFieldAccess)
				finalIndex = i+1;






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


250


251


			if(accesses[i] instanceof SpecificFieldAccess)
				finalIndex = i+1;

			if(accesses[i] instanceof SpecificFieldAccess)if(accesses[i]instanceofSpecificFieldAccess)				finalIndex = i+1;finalIndex=i+1;




cleaning code



Johannes Lerch
committed
Jan 07, 2015




252



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


252


		}

		}}




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




253


254


255



		return finalIndex;
	}







regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


253


254


255


		return finalIndex;
	}


		return finalIndex;returnfinalIndex;	}}




merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




256


257



	private Collection<Transition<T>> possibleTransitions(int index, boolean addExclusionTransitions) {
		Collection<Transition<T>> result = Lists.newLinkedList();






merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types

 

merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


256


257


	private Collection<Transition<T>> possibleTransitions(int index, boolean addExclusionTransitions) {
		Collection<Transition<T>> result = Lists.newLinkedList();

	private Collection<Transition<T>> possibleTransitions(int index, boolean addExclusionTransitions) {privateCollection<Transition<T>>possibleTransitions(intindex,booleanaddExclusionTransitions){		Collection<Transition<T>> result = Lists.newLinkedList();Collection<Transition<T>>result=Lists.newLinkedList();




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




258


259


260



		if(index < accesses.length) {
			if(accesses[index] instanceof SetOfPossibleFieldAccesses) {
				result.add(new Transition.SubAccessPathTransition<>(index, accesses[index]));






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


258


259


260


		if(index < accesses.length) {
			if(accesses[index] instanceof SetOfPossibleFieldAccesses) {
				result.add(new Transition.SubAccessPathTransition<>(index, accesses[index]));

		if(index < accesses.length) {if(index<accesses.length){			if(accesses[index] instanceof SetOfPossibleFieldAccesses) {if(accesses[index]instanceofSetOfPossibleFieldAccesses){				result.add(new Transition.SubAccessPathTransition<>(index, accesses[index]));result.add(newTransition.SubAccessPathTransition<>(index,accesses[index]));




merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




261



				result.addAll(possibleTransitions(index+1, addExclusionTransitions));






merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types

 

merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


261


				result.addAll(possibleTransitions(index+1, addExclusionTransitions));

				result.addAll(possibleTransitions(index+1, addExclusionTransitions));result.addAll(possibleTransitions(index+1,addExclusionTransitions));




cleaning code



Johannes Lerch
committed
Jan 07, 2015




262



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


262


			}

			}}




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




263


264



			else
				result.add(new Transition.SubAccessPathTransition<>(index+1, accesses[index]));






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


263


264


			else
				result.add(new Transition.SubAccessPathTransition<>(index+1, accesses[index]));

			elseelse				result.add(new Transition.SubAccessPathTransition<>(index+1, accesses[index]));result.add(newTransition.SubAccessPathTransition<>(index+1,accesses[index]));




remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




265


266



		} else if(addExclusionTransitions && index - accesses.length == 0 && !exclusions.isEmpty()) {
			result.add(new Transition.ExclusionPathTransition<T>(index+1, exclusions));






remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015



remove ability to nest exclusions

 

remove ability to nest exclusions

Johannes Lerch
committed
Feb 13, 2015


265


266


		} else if(addExclusionTransitions && index - accesses.length == 0 && !exclusions.isEmpty()) {
			result.add(new Transition.ExclusionPathTransition<T>(index+1, exclusions));

		} else if(addExclusionTransitions && index - accesses.length == 0 && !exclusions.isEmpty()) {}elseif(addExclusionTransitions&&index-accesses.length==0&&!exclusions.isEmpty()){			result.add(new Transition.ExclusionPathTransition<T>(index+1, exclusions));result.add(newTransition.ExclusionPathTransition<T>(index+1,exclusions));




cleaning code



Johannes Lerch
committed
Jan 07, 2015




267



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


267


		}

		}}




Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




268



		return result;






Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015



Split prefix check into potential and guaranteed prefix

 

Split prefix check into potential and guaranteed prefix

Johannes Lerch
committed
Jan 22, 2015


268


		return result;

		return result;returnresult;




cleaning code



Johannes Lerch
committed
Jan 07, 2015




269


270



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


269


270


	}
	

	}}	




merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




271


272


273



	public SubAccessPath<T>[] getDeltaTo(AccessPath<T> accPath) {
		int currIndex = 0;
		int otherIndex = 0;






merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types

 

merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


271


272


273


	public SubAccessPath<T>[] getDeltaTo(AccessPath<T> accPath) {
		int currIndex = 0;
		int otherIndex = 0;

	public SubAccessPath<T>[] getDeltaTo(AccessPath<T> accPath) {publicSubAccessPath<T>[]getDeltaTo(AccessPath<T>accPath){		int currIndex = 0;intcurrIndex=0;		int otherIndex = 0;intotherIndex=0;




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




274



		






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


274


		

		




merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




275


276


277


278


279


280


281


282


283


284


285


286


287


288


289



		outer: while(true) {
			Collection<Transition<T>> transitions = possibleTransitions(currIndex, false);
			Collection<Transition<T>> otherTransitions = accPath.possibleTransitions(otherIndex, false);

			for(Transition<T> transition : transitions) {
				for(Transition<T> otherTransition : otherTransitions) {
					MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);
					if(match.hasMatched()) {
						if(currIndex == transition.transitionToIndex() && otherIndex == otherTransition.transitionToIndex())
							continue;
						
						currIndex = transition.transitionToIndex();
						otherIndex = otherTransition.transitionToIndex();
						continue outer;
					}






merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types

 

merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


275


276


277


278


279


280


281


282


283


284


285


286


287


288


289


		outer: while(true) {
			Collection<Transition<T>> transitions = possibleTransitions(currIndex, false);
			Collection<Transition<T>> otherTransitions = accPath.possibleTransitions(otherIndex, false);

			for(Transition<T> transition : transitions) {
				for(Transition<T> otherTransition : otherTransitions) {
					MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);
					if(match.hasMatched()) {
						if(currIndex == transition.transitionToIndex() && otherIndex == otherTransition.transitionToIndex())
							continue;
						
						currIndex = transition.transitionToIndex();
						otherIndex = otherTransition.transitionToIndex();
						continue outer;
					}

		outer: while(true) {outer:while(true){			Collection<Transition<T>> transitions = possibleTransitions(currIndex, false);Collection<Transition<T>>transitions=possibleTransitions(currIndex,false);			Collection<Transition<T>> otherTransitions = accPath.possibleTransitions(otherIndex, false);Collection<Transition<T>>otherTransitions=accPath.possibleTransitions(otherIndex,false);			for(Transition<T> transition : transitions) {for(Transition<T>transition:transitions){				for(Transition<T> otherTransition : otherTransitions) {for(Transition<T>otherTransition:otherTransitions){					MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);MatchResult<Transition<T>>match=transition.isPrefixMatchOf(otherTransition);					if(match.hasMatched()) {if(match.hasMatched()){						if(currIndex == transition.transitionToIndex() && otherIndex == otherTransition.transitionToIndex())if(currIndex==transition.transitionToIndex()&&otherIndex==otherTransition.transitionToIndex())							continue;continue;												currIndex = transition.transitionToIndex();currIndex=transition.transitionToIndex();						otherIndex = otherTransition.transitionToIndex();otherIndex=otherTransition.transitionToIndex();						continue outer;continueouter;					}}




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




290


291



				}
			}






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


290


291


				}
			}

				}}			}}




merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




292



			break;






merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types

 

merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


292


			break;

			break;break;




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




293


294



		}
		






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


293


294


		}
		

		}}		




merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




295



		return Arrays.copyOfRange(accPath.accesses, otherIndex, accPath.accesses.length);






merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types

 

merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


295


		return Arrays.copyOfRange(accPath.accesses, otherIndex, accPath.accesses.length);

		return Arrays.copyOfRange(accPath.accesses, otherIndex, accPath.accesses.length);returnArrays.copyOfRange(accPath.accesses,otherIndex,accPath.accesses.length);




cleaning code



Johannes Lerch
committed
Jan 07, 2015




296


297



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


296


297


	}
	

	}}	




merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




298



	public AccessPath<T> mergeExcludedFieldReferences(AccessPath<T> accPath) {






merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types

 

merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


298


	public AccessPath<T> mergeExcludedFieldReferences(AccessPath<T> accPath) {

	public AccessPath<T> mergeExcludedFieldReferences(AccessPath<T> accPath) {publicAccessPath<T>mergeExcludedFieldReferences(AccessPath<T>accPath){




remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




299


300


301



		HashSet<T> newExclusions = Sets.newHashSet(exclusions);
		newExclusions.addAll(accPath.exclusions);
		return new AccessPath<>(accesses, newExclusions);






remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015



remove ability to nest exclusions

 

remove ability to nest exclusions

Johannes Lerch
committed
Feb 13, 2015


299


300


301


		HashSet<T> newExclusions = Sets.newHashSet(exclusions);
		newExclusions.addAll(accPath.exclusions);
		return new AccessPath<>(accesses, newExclusions);

		HashSet<T> newExclusions = Sets.newHashSet(exclusions);HashSet<T>newExclusions=Sets.newHashSet(exclusions);		newExclusions.addAll(accPath.exclusions);newExclusions.addAll(accPath.exclusions);		return new AccessPath<>(accesses, newExclusions);returnnewAccessPath<>(accesses,newExclusions);




cleaning code



Johannes Lerch
committed
Jan 07, 2015




302


303



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


302


303


	}
	

	}}	




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




304


305


306


307



	public boolean mayHaveEmptyAccessPath() {
		return finalIndex() == 0;
	}
	






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


304


305


306


307


	public boolean mayHaveEmptyAccessPath() {
		return finalIndex() == 0;
	}
	

	public boolean mayHaveEmptyAccessPath() {publicbooleanmayHaveEmptyAccessPath(){		return finalIndex() == 0;returnfinalIndex()==0;	}}	




cleaning code



Johannes Lerch
committed
Jan 07, 2015




308



	public boolean isEmpty() {






cleaning code



Johannes Lerch
committed
Jan 07, 2015



cleaning code


cleaning code

Johannes Lerch
committed
Jan 07, 2015


308


	public boolean isEmpty() {

	public boolean isEmpty() {publicbooleanisEmpty(){




remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




309



		return exclusions.isEmpty() && accesses.length == 0;






remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015



remove ability to nest exclusions

 

remove ability to nest exclusions

Johannes Lerch
committed
Feb 13, 2015


309


		return exclusions.isEmpty() && accesses.length == 0;

		return exclusions.isEmpty() && accesses.length == 0;returnexclusions.isEmpty()&&accesses.length==0;




cleaning code



Johannes Lerch
committed
Jan 07, 2015




310


311


312


313


314


315


316



	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(accesses);






cleaning code



Johannes Lerch
committed
Jan 07, 2015



cleaning code


cleaning code

Johannes Lerch
committed
Jan 07, 2015


310


311


312


313


314


315


316


	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(accesses);

	}}		@Override@Override	public int hashCode() {publicinthashCode(){		final int prime = 31;finalintprime=31;		int result = 1;intresult=1;		result = prime * result + Arrays.hashCode(accesses);result=prime*result+Arrays.hashCode(accesses);




remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




317



		result = prime * result + ((exclusions == null) ? 0 : exclusions.hashCode());






remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015



remove ability to nest exclusions

 

remove ability to nest exclusions

Johannes Lerch
committed
Feb 13, 2015


317


		result = prime * result + ((exclusions == null) ? 0 : exclusions.hashCode());

		result = prime * result + ((exclusions == null) ? 0 : exclusions.hashCode());result=prime*result+((exclusions==null)?0:exclusions.hashCode());




cleaning code



Johannes Lerch
committed
Jan 07, 2015




318


319


320


321


322


323


324


325


326


327


328


329


330


331



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






cleaning code



Johannes Lerch
committed
Jan 07, 2015



cleaning code


cleaning code

Johannes Lerch
committed
Jan 07, 2015


318


319


320


321


322


323


324


325


326


327


328


329


330


331


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

		return result;returnresult;	}}	@Override@Override	public boolean equals(Object obj) {publicbooleanequals(Objectobj){		if (this == obj)if(this==obj)			return true;returntrue;		if (obj == null)if(obj==null)			return false;returnfalse;		if (!(obj instanceof AccessPath))if(!(objinstanceofAccessPath))			return false;returnfalse;		AccessPath other = (AccessPath) obj;AccessPathother=(AccessPath)obj;		if (!Arrays.equals(accesses, other.accesses))if(!Arrays.equals(accesses,other.accesses))			return false;returnfalse;




remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




332


333


334


335



		if (exclusions == null) {
			if (other.exclusions != null)
				return false;
		} else if (!exclusions.equals(other.exclusions))






remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015



remove ability to nest exclusions

 

remove ability to nest exclusions

Johannes Lerch
committed
Feb 13, 2015


332


333


334


335


		if (exclusions == null) {
			if (other.exclusions != null)
				return false;
		} else if (!exclusions.equals(other.exclusions))

		if (exclusions == null) {if(exclusions==null){			if (other.exclusions != null)if(other.exclusions!=null)				return false;returnfalse;		} else if (!exclusions.equals(other.exclusions))}elseif(!exclusions.equals(other.exclusions))




cleaning code



Johannes Lerch
committed
Jan 07, 2015




336


337


338


339


340


341


342



			return false;
		return true;
	}

	@Override
	public String toString() {
		String result = accesses.length > 0 ? "."+Joiner.on(".").join(accesses) : "";






cleaning code



Johannes Lerch
committed
Jan 07, 2015



cleaning code


cleaning code

Johannes Lerch
committed
Jan 07, 2015


336


337


338


339


340


341


342


			return false;
		return true;
	}

	@Override
	public String toString() {
		String result = accesses.length > 0 ? "."+Joiner.on(".").join(accesses) : "";

			return false;returnfalse;		return true;returntrue;	}}	@Override@Override	public String toString() {publicStringtoString(){		String result = accesses.length > 0 ? "."+Joiner.on(".").join(accesses) : "";Stringresult=accesses.length>0?"."+Joiner.on(".").join(accesses):"";




remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




343


344



		if(!exclusions.isEmpty())
			result += "^" + Joiner.on(",").join(exclusions);






remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015



remove ability to nest exclusions

 

remove ability to nest exclusions

Johannes Lerch
committed
Feb 13, 2015


343


344


		if(!exclusions.isEmpty())
			result += "^" + Joiner.on(",").join(exclusions);

		if(!exclusions.isEmpty())if(!exclusions.isEmpty())			result += "^" + Joiner.on(",").join(exclusions);result+="^"+Joiner.on(",").join(exclusions);




cleaning code



Johannes Lerch
committed
Jan 07, 2015




345


346


347



		return result;
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


345


346


347


		return result;
	}
	

		return result;returnresult;	}}	




remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




348


349


350


351



	public <U extends FieldRef<U>> AccessPath<U> map(Function<T, U> function) {
		SubAccessPath<U>[] newAccesses = new SubAccessPath[accesses.length];
		for(int i=0; i<accesses.length; i++) {
			newAccesses[i] = accesses[i].map(function);






remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015



remove ability to nest exclusions

 

remove ability to nest exclusions

Johannes Lerch
committed
Feb 13, 2015


348


349


350


351


	public <U extends FieldRef<U>> AccessPath<U> map(Function<T, U> function) {
		SubAccessPath<U>[] newAccesses = new SubAccessPath[accesses.length];
		for(int i=0; i<accesses.length; i++) {
			newAccesses[i] = accesses[i].map(function);

	public <U extends FieldRef<U>> AccessPath<U> map(Function<T, U> function) {public<UextendsFieldRef<U>>AccessPath<U>map(Function<T,U>function){		SubAccessPath<U>[] newAccesses = new SubAccessPath[accesses.length];SubAccessPath<U>[]newAccesses=newSubAccessPath[accesses.length];		for(int i=0; i<accesses.length; i++) {for(inti=0;i<accesses.length;i++){			newAccesses[i] = accesses[i].map(function);newAccesses[i]=accesses[i].map(function);




cleaning code



Johannes Lerch
committed
Jan 07, 2015




352



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


352


		}

		}}




remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




353


354


355


356



		Set<U> newExclusions = Sets.newHashSet();
		for(T f : exclusions)
			newExclusions.add(function.apply(f));
		return new AccessPath<U>(newAccesses, newExclusions);






remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015



remove ability to nest exclusions

 

remove ability to nest exclusions

Johannes Lerch
committed
Feb 13, 2015


353


354


355


356


		Set<U> newExclusions = Sets.newHashSet();
		for(T f : exclusions)
			newExclusions.add(function.apply(f));
		return new AccessPath<U>(newAccesses, newExclusions);

		Set<U> newExclusions = Sets.newHashSet();Set<U>newExclusions=Sets.newHashSet();		for(T f : exclusions)for(Tf:exclusions)			newExclusions.add(function.apply(f));newExclusions.add(function.apply(f));		return new AccessPath<U>(newAccesses, newExclusions);returnnewAccessPath<U>(newAccesses,newExclusions);




cleaning code



Johannes Lerch
committed
Jan 07, 2015




357



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


357


	}

	}}




remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




358



	






remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015



remove ability to nest exclusions

 

remove ability to nest exclusions

Johannes Lerch
committed
Feb 13, 2015


358


	

	




merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




359



	public AccessPath<T> removeAnyAccess() {






merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types

 

merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


359


	public AccessPath<T> removeAnyAccess() {

	public AccessPath<T> removeAnyAccess() {publicAccessPath<T>removeAnyAccess(){




Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




360



		if(accesses.length > 0)






Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015



Split prefix check into potential and guaranteed prefix

 

Split prefix check into potential and guaranteed prefix

Johannes Lerch
committed
Jan 22, 2015


360


		if(accesses.length > 0)

		if(accesses.length > 0)if(accesses.length>0)




merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015




361



			return new AccessPath<T>(new SubAccessPath[0], exclusions);






merge only on equal field types

 


Johannes Lerch
committed
Feb 05, 2015



merge only on equal field types

 

merge only on equal field types

Johannes Lerch
committed
Feb 05, 2015


361


			return new AccessPath<T>(new SubAccessPath[0], exclusions);

			return new AccessPath<T>(new SubAccessPath[0], exclusions);returnnewAccessPath<T>(newSubAccessPath[0],exclusions);




regexp access path

 


Johannes Lerch
committed
Feb 05, 2015




362


363


364


365


366


367



		else
			return this;
	}

	public boolean hasEmptyAccessPath() {
		return accesses.length == 0;






regexp access path

 


Johannes Lerch
committed
Feb 05, 2015



regexp access path

 

regexp access path

Johannes Lerch
committed
Feb 05, 2015


362


363


364


365


366


367


		else
			return this;
	}

	public boolean hasEmptyAccessPath() {
		return accesses.length == 0;

		elseelse			return this;returnthis;	}}	public boolean hasEmptyAccessPath() {publicbooleanhasEmptyAccessPath(){		return accesses.length == 0;returnaccesses.length==0;




Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015




368



	}






Split prefix check into potential and guaranteed prefix

 


Johannes Lerch
committed
Jan 22, 2015



Split prefix check into potential and guaranteed prefix

 

Split prefix check into potential and guaranteed prefix

Johannes Lerch
committed
Jan 22, 2015


368


	}

	}}




subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015




369


370


371


372


373


374


375


376


377


378


379


380


381


382


383


384


385


386


387


388


389


390


391


392


393


394


395


396


397


398


399


400


401


402


403


404


405


406


407


408


409


410




	public boolean subsumes(AccessPath<T> accPath) {
		int currIndex = 0;
		int otherIndex = 0;
		
		
		outer: while(true) {
			Collection<Transition<T>> transitions = possibleTransitions(currIndex, false);
			Collection<Transition<T>> otherTransitions = accPath.possibleTransitions(otherIndex, false);

			if((currIndex >= accesses.length || (currIndex == accesses.length-1 && accesses[currIndex] instanceof SetOfPossibleFieldAccesses)) 
					&& otherIndex>=accPath.accesses.length-1) {
				if(transitions.isEmpty())
					return otherTransitions.isEmpty() && hasAtLeastTheSameExclusionsAs(accPath);
				for(Transition<T> transition : transitions) {
					for(Transition<T> otherTransition : otherTransitions) {
						MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);
						if(!match.hasMatched())
							return false;
					}	
				}
				return hasAtLeastTheSameExclusionsAs(accPath);
			}

			for(Transition<T> transition : transitions) {
				for(Transition<T> otherTransition : otherTransitions) {
					MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);
					if(match.hasMatched()) {
						if(otherIndex == otherTransition.transitionToIndex())
							continue;
						
						currIndex = transition.transitionToIndex();
						otherIndex = otherTransition.transitionToIndex();
						continue outer;
					}
				}
			}
			return false;
		}
	}
	
	private boolean hasAtLeastTheSameExclusionsAs(AccessPath<T> accPath) {






subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015



subumption + debugging

 

subumption + debugging

Johannes Lerch
committed
Feb 09, 2015


369


370


371


372


373


374


375


376


377


378


379


380


381


382


383


384


385


386


387


388


389


390


391


392


393


394


395


396


397


398


399


400


401


402


403


404


405


406


407


408


409


410



	public boolean subsumes(AccessPath<T> accPath) {
		int currIndex = 0;
		int otherIndex = 0;
		
		
		outer: while(true) {
			Collection<Transition<T>> transitions = possibleTransitions(currIndex, false);
			Collection<Transition<T>> otherTransitions = accPath.possibleTransitions(otherIndex, false);

			if((currIndex >= accesses.length || (currIndex == accesses.length-1 && accesses[currIndex] instanceof SetOfPossibleFieldAccesses)) 
					&& otherIndex>=accPath.accesses.length-1) {
				if(transitions.isEmpty())
					return otherTransitions.isEmpty() && hasAtLeastTheSameExclusionsAs(accPath);
				for(Transition<T> transition : transitions) {
					for(Transition<T> otherTransition : otherTransitions) {
						MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);
						if(!match.hasMatched())
							return false;
					}	
				}
				return hasAtLeastTheSameExclusionsAs(accPath);
			}

			for(Transition<T> transition : transitions) {
				for(Transition<T> otherTransition : otherTransitions) {
					MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);
					if(match.hasMatched()) {
						if(otherIndex == otherTransition.transitionToIndex())
							continue;
						
						currIndex = transition.transitionToIndex();
						otherIndex = otherTransition.transitionToIndex();
						continue outer;
					}
				}
			}
			return false;
		}
	}
	
	private boolean hasAtLeastTheSameExclusionsAs(AccessPath<T> accPath) {

	public boolean subsumes(AccessPath<T> accPath) {publicbooleansubsumes(AccessPath<T>accPath){		int currIndex = 0;intcurrIndex=0;		int otherIndex = 0;intotherIndex=0;						outer: while(true) {outer:while(true){			Collection<Transition<T>> transitions = possibleTransitions(currIndex, false);Collection<Transition<T>>transitions=possibleTransitions(currIndex,false);			Collection<Transition<T>> otherTransitions = accPath.possibleTransitions(otherIndex, false);Collection<Transition<T>>otherTransitions=accPath.possibleTransitions(otherIndex,false);			if((currIndex >= accesses.length || (currIndex == accesses.length-1 && accesses[currIndex] instanceof SetOfPossibleFieldAccesses)) if((currIndex>=accesses.length||(currIndex==accesses.length-1&&accesses[currIndex]instanceofSetOfPossibleFieldAccesses))					&& otherIndex>=accPath.accesses.length-1) {&&otherIndex>=accPath.accesses.length-1){				if(transitions.isEmpty())if(transitions.isEmpty())					return otherTransitions.isEmpty() && hasAtLeastTheSameExclusionsAs(accPath);returnotherTransitions.isEmpty()&&hasAtLeastTheSameExclusionsAs(accPath);				for(Transition<T> transition : transitions) {for(Transition<T>transition:transitions){					for(Transition<T> otherTransition : otherTransitions) {for(Transition<T>otherTransition:otherTransitions){						MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);MatchResult<Transition<T>>match=transition.isPrefixMatchOf(otherTransition);						if(!match.hasMatched())if(!match.hasMatched())							return false;returnfalse;					}	}				}}				return hasAtLeastTheSameExclusionsAs(accPath);returnhasAtLeastTheSameExclusionsAs(accPath);			}}			for(Transition<T> transition : transitions) {for(Transition<T>transition:transitions){				for(Transition<T> otherTransition : otherTransitions) {for(Transition<T>otherTransition:otherTransitions){					MatchResult<Transition<T>> match = transition.isPrefixMatchOf(otherTransition);MatchResult<Transition<T>>match=transition.isPrefixMatchOf(otherTransition);					if(match.hasMatched()) {if(match.hasMatched()){						if(otherIndex == otherTransition.transitionToIndex())if(otherIndex==otherTransition.transitionToIndex())							continue;continue;												currIndex = transition.transitionToIndex();currIndex=transition.transitionToIndex();						otherIndex = otherTransition.transitionToIndex();otherIndex=otherTransition.transitionToIndex();						continue outer;continueouter;					}}				}}			}}			return false;returnfalse;		}}	}}		private boolean hasAtLeastTheSameExclusionsAs(AccessPath<T> accPath) {privatebooleanhasAtLeastTheSameExclusionsAs(AccessPath<T>accPath){




remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




411



		return accPath.exclusions.containsAll(exclusions);






remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015



remove ability to nest exclusions

 

remove ability to nest exclusions

Johannes Lerch
committed
Feb 13, 2015


411


		return accPath.exclusions.containsAll(exclusions);

		return accPath.exclusions.containsAll(exclusions);returnaccPath.exclusions.containsAll(exclusions);




subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015




412


413


414


415


416


417


418



	}

	public Collection<String> tokenize() {
		List<String> result = Lists.newLinkedList();
		for(SubAccessPath<T> s : accesses) {
			result.add(s.toString());
		}






subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015



subumption + debugging

 

subumption + debugging

Johannes Lerch
committed
Feb 09, 2015


412


413


414


415


416


417


418


	}

	public Collection<String> tokenize() {
		List<String> result = Lists.newLinkedList();
		for(SubAccessPath<T> s : accesses) {
			result.add(s.toString());
		}

	}}	public Collection<String> tokenize() {publicCollection<String>tokenize(){		List<String> result = Lists.newLinkedList();List<String>result=Lists.newLinkedList();		for(SubAccessPath<T> s : accesses) {for(SubAccessPath<T>s:accesses){			result.add(s.toString());result.add(s.toString());		}}




remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




419


420



		if(!exclusions.isEmpty())
			result.add("^"+Joiner.on(",").join(exclusions));






remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015



remove ability to nest exclusions

 

remove ability to nest exclusions

Johannes Lerch
committed
Feb 13, 2015


419


420


		if(!exclusions.isEmpty())
			result.add("^"+Joiner.on(",").join(exclusions));

		if(!exclusions.isEmpty())if(!exclusions.isEmpty())			result.add("^"+Joiner.on(",").join(exclusions));result.add("^"+Joiner.on(",").join(exclusions));




subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015




421


422



		return result;
	}






subumption + debugging

 


Johannes Lerch
committed
Feb 09, 2015



subumption + debugging

 

subumption + debugging

Johannes Lerch
committed
Feb 09, 2015


421


422


		return result;
	}

		return result;returnresult;	}}




remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015




423


424


425


426




	public AccessPath<T> removeExclusions() {
		return new AccessPath<T>(accesses, Sets.<T>newHashSet());
	}






remove ability to nest exclusions

 


Johannes Lerch
committed
Feb 13, 2015



remove ability to nest exclusions

 

remove ability to nest exclusions

Johannes Lerch
committed
Feb 13, 2015


423


424


425


426



	public AccessPath<T> removeExclusions() {
		return new AccessPath<T>(accesses, Sets.<T>newHashSet());
	}

	public AccessPath<T> removeExclusions() {publicAccessPath<T>removeExclusions(){		return new AccessPath<T>(accesses, Sets.<T>newHashSet());returnnewAccessPath<T>(accesses,Sets.<T>newHashSet());	}}




additional methods for AccessPath required by client FlowFunction

 


Johannes Lerch
committed
Feb 16, 2015




427


428


429


430


431


432


433


434


435


436


437


438


439


440


441


442


443


444


445


446




	public SubAccessPath<T> getFirstAccess() {
		return accesses[0];
	}

	public AccessPath<T> removeRepeatableFirstAccess(T field) {
		Collection<? extends T> elements = accesses[0].elements();
		if(!elements.contains(field))
			throw new IllegalArgumentException();
		
		if(elements.size() == 1) {
			return new AccessPath<>(Arrays.copyOfRange(accesses, 1, accesses.length), exclusions);
		}
		
		HashSet<T> newSet = Sets.newHashSet(elements);
		newSet.remove(field);
		SubAccessPath<T>[] newAccesses = Arrays.copyOf(accesses, accesses.length);
		newAccesses[0] = new SetOfPossibleFieldAccesses<>(newSet);
		return new AccessPath<T>(newAccesses, exclusions);
	}






additional methods for AccessPath required by client FlowFunction

 


Johannes Lerch
committed
Feb 16, 2015



additional methods for AccessPath required by client FlowFunction

 

additional methods for AccessPath required by client FlowFunction

Johannes Lerch
committed
Feb 16, 2015


427


428


429


430


431


432


433


434


435


436


437


438


439


440


441


442


443


444


445


446



	public SubAccessPath<T> getFirstAccess() {
		return accesses[0];
	}

	public AccessPath<T> removeRepeatableFirstAccess(T field) {
		Collection<? extends T> elements = accesses[0].elements();
		if(!elements.contains(field))
			throw new IllegalArgumentException();
		
		if(elements.size() == 1) {
			return new AccessPath<>(Arrays.copyOfRange(accesses, 1, accesses.length), exclusions);
		}
		
		HashSet<T> newSet = Sets.newHashSet(elements);
		newSet.remove(field);
		SubAccessPath<T>[] newAccesses = Arrays.copyOf(accesses, accesses.length);
		newAccesses[0] = new SetOfPossibleFieldAccesses<>(newSet);
		return new AccessPath<T>(newAccesses, exclusions);
	}

	public SubAccessPath<T> getFirstAccess() {publicSubAccessPath<T>getFirstAccess(){		return accesses[0];returnaccesses[0];	}}	public AccessPath<T> removeRepeatableFirstAccess(T field) {publicAccessPath<T>removeRepeatableFirstAccess(Tfield){		Collection<? extends T> elements = accesses[0].elements();Collection<?extendsT>elements=accesses[0].elements();		if(!elements.contains(field))if(!elements.contains(field))			throw new IllegalArgumentException();thrownewIllegalArgumentException();				if(elements.size() == 1) {if(elements.size()==1){			return new AccessPath<>(Arrays.copyOfRange(accesses, 1, accesses.length), exclusions);returnnewAccessPath<>(Arrays.copyOfRange(accesses,1,accesses.length),exclusions);		}}				HashSet<T> newSet = Sets.newHashSet(elements);HashSet<T>newSet=Sets.newHashSet(elements);		newSet.remove(field);newSet.remove(field);		SubAccessPath<T>[] newAccesses = Arrays.copyOf(accesses, accesses.length);SubAccessPath<T>[]newAccesses=Arrays.copyOf(accesses,accesses.length);		newAccesses[0] = new SetOfPossibleFieldAccesses<>(newSet);newAccesses[0]=newSetOfPossibleFieldAccesses<>(newSet);		return new AccessPath<T>(newAccesses, exclusions);returnnewAccessPath<T>(newAccesses,exclusions);	}}




cleaning code



Johannes Lerch
committed
Jan 07, 2015




447



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


447


}
}}





