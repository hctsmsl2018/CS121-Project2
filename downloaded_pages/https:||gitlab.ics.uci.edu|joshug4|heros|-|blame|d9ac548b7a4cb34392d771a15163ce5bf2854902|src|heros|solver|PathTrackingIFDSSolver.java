



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

d9ac548b7a4cb34392d771a15163ce5bf2854902

















d9ac548b7a4cb34392d771a15163ce5bf2854902


Switch branch/tag










heros


src


heros


solver


PathTrackingIFDSSolver.java



Find file
Normal viewHistoryPermalink






PathTrackingIFDSSolver.java



3.86 KB









Newer










Older









added a PathTrackingIFDSSolver which implements the keeping track of paths;



 


Eric Bodden
committed
Oct 18, 2013






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
package heros.solver;

import heros.EdgeFunction;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * An {@link IFDSSolver} that tracks paths for reporting. To do so, it requires that data-flow abstractions implement the LinkedNode interface.
 * The solver implements a cache of data-flow facts for each statement and source value. If for the same statement and source value the same
 * target value is seen again (as determined through a cache hit), then the solver propagates the cached value but at the same time links
 * both target values with one another.
 *  
 * @author Eric Bodden
 */
public class PathTrackingIFDSSolver<N, D extends PathTrackingIFDSSolver.LinkedNode<D>, M, I extends InterproceduralCFG<N, M>> extends IFDSSolver<N, D, M, I> {

	/**
	 * A data-flow fact that can be linked with other equal facts.
	 * Equality and hash-code operations must <i>not</i> take the linking data structures into account!
	 */
	public static interface LinkedNode<D> {
		/**
		 * Links this node to a neighbor node, i.e., to an abstraction that would have been merged
		 * with this one of paths were not being tracked.
		 */
		public void addNeighbor(D originalAbstraction);
	}

	public PathTrackingIFDSSolver(IFDSTabulationProblem<N, D, M, I> ifdsProblem) {
		super(ifdsProblem);
	}

	private final Map<CacheEntry, LinkedNode<D>> cache = Maps.newHashMap();
	
	@Override
	protected void propagate(D sourceVal, N target, D targetVal, EdgeFunction<IFDSSolver.BinaryDomain> f, N relatedCallSite, boolean isUnbalancedReturn) {
		CacheEntry currentCacheEntry = new CacheEntry(target, sourceVal, targetVal);

		boolean propagate = false;
		synchronized (this) {
			if (cache.containsKey(currentCacheEntry)) {
				LinkedNode<D> existingTargetVal = cache.get(currentCacheEntry);
				if (existingTargetVal != targetVal)
					existingTargetVal.addNeighbor(targetVal);
			} else {
				cache.put(currentCacheEntry, targetVal);
				propagate = true;
			}
		}

		if (propagate)
			super.propagate(sourceVal, target, targetVal, f, relatedCallSite, isUnbalancedReturn);
		
	};
	
	
	private class CacheEntry {
		private N n;
		private D sourceVal;
		private D targetVal;

		public CacheEntry(N n, D sourceVal, D targetVal) {
			super();
			this.n = n;
			this.sourceVal = sourceVal;
			this.targetVal = targetVal;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((sourceVal == null) ? 0 : sourceVal.hashCode());
			result = prime * result + ((targetVal == null) ? 0 : targetVal.hashCode());
			result = prime * result + ((n == null) ? 0 : n.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings({ "unchecked" })
			CacheEntry other = (CacheEntry) obj;
			if (sourceVal == null) {
				if (other.sourceVal != null)
					return false;
			} else if (!sourceVal.equals(other.sourceVal))
				return false;
			if (targetVal == null) {
				if (other.targetVal != null)
					return false;
			} else if (!targetVal.equals(other.targetVal))
				return false;
			if (n == null) {
				if (other.n != null)
					return false;
			} else if (!n.equals(other.n))
				return false;
			return true;
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

d9ac548b7a4cb34392d771a15163ce5bf2854902

















d9ac548b7a4cb34392d771a15163ce5bf2854902


Switch branch/tag










heros


src


heros


solver


PathTrackingIFDSSolver.java



Find file
Normal viewHistoryPermalink






PathTrackingIFDSSolver.java



3.86 KB









Newer










Older









added a PathTrackingIFDSSolver which implements the keeping track of paths;



 


Eric Bodden
committed
Oct 18, 2013






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
package heros.solver;

import heros.EdgeFunction;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * An {@link IFDSSolver} that tracks paths for reporting. To do so, it requires that data-flow abstractions implement the LinkedNode interface.
 * The solver implements a cache of data-flow facts for each statement and source value. If for the same statement and source value the same
 * target value is seen again (as determined through a cache hit), then the solver propagates the cached value but at the same time links
 * both target values with one another.
 *  
 * @author Eric Bodden
 */
public class PathTrackingIFDSSolver<N, D extends PathTrackingIFDSSolver.LinkedNode<D>, M, I extends InterproceduralCFG<N, M>> extends IFDSSolver<N, D, M, I> {

	/**
	 * A data-flow fact that can be linked with other equal facts.
	 * Equality and hash-code operations must <i>not</i> take the linking data structures into account!
	 */
	public static interface LinkedNode<D> {
		/**
		 * Links this node to a neighbor node, i.e., to an abstraction that would have been merged
		 * with this one of paths were not being tracked.
		 */
		public void addNeighbor(D originalAbstraction);
	}

	public PathTrackingIFDSSolver(IFDSTabulationProblem<N, D, M, I> ifdsProblem) {
		super(ifdsProblem);
	}

	private final Map<CacheEntry, LinkedNode<D>> cache = Maps.newHashMap();
	
	@Override
	protected void propagate(D sourceVal, N target, D targetVal, EdgeFunction<IFDSSolver.BinaryDomain> f, N relatedCallSite, boolean isUnbalancedReturn) {
		CacheEntry currentCacheEntry = new CacheEntry(target, sourceVal, targetVal);

		boolean propagate = false;
		synchronized (this) {
			if (cache.containsKey(currentCacheEntry)) {
				LinkedNode<D> existingTargetVal = cache.get(currentCacheEntry);
				if (existingTargetVal != targetVal)
					existingTargetVal.addNeighbor(targetVal);
			} else {
				cache.put(currentCacheEntry, targetVal);
				propagate = true;
			}
		}

		if (propagate)
			super.propagate(sourceVal, target, targetVal, f, relatedCallSite, isUnbalancedReturn);
		
	};
	
	
	private class CacheEntry {
		private N n;
		private D sourceVal;
		private D targetVal;

		public CacheEntry(N n, D sourceVal, D targetVal) {
			super();
			this.n = n;
			this.sourceVal = sourceVal;
			this.targetVal = targetVal;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((sourceVal == null) ? 0 : sourceVal.hashCode());
			result = prime * result + ((targetVal == null) ? 0 : targetVal.hashCode());
			result = prime * result + ((n == null) ? 0 : n.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings({ "unchecked" })
			CacheEntry other = (CacheEntry) obj;
			if (sourceVal == null) {
				if (other.sourceVal != null)
					return false;
			} else if (!sourceVal.equals(other.sourceVal))
				return false;
			if (targetVal == null) {
				if (other.targetVal != null)
					return false;
			} else if (!targetVal.equals(other.targetVal))
				return false;
			if (n == null) {
				if (other.n != null)
					return false;
			} else if (!n.equals(other.n))
				return false;
			return true;
		}
	}	
	


}











Open sidebar



Joshua Garcia heros

d9ac548b7a4cb34392d771a15163ce5bf2854902







Open sidebar



Joshua Garcia heros

d9ac548b7a4cb34392d771a15163ce5bf2854902




Open sidebar

Joshua Garcia heros

d9ac548b7a4cb34392d771a15163ce5bf2854902


Joshua Garciaherosheros
d9ac548b7a4cb34392d771a15163ce5bf2854902










d9ac548b7a4cb34392d771a15163ce5bf2854902


Switch branch/tag










heros


src


heros


solver


PathTrackingIFDSSolver.java



Find file
Normal viewHistoryPermalink






PathTrackingIFDSSolver.java



3.86 KB









Newer










Older









added a PathTrackingIFDSSolver which implements the keeping track of paths;



 


Eric Bodden
committed
Oct 18, 2013






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
package heros.solver;

import heros.EdgeFunction;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * An {@link IFDSSolver} that tracks paths for reporting. To do so, it requires that data-flow abstractions implement the LinkedNode interface.
 * The solver implements a cache of data-flow facts for each statement and source value. If for the same statement and source value the same
 * target value is seen again (as determined through a cache hit), then the solver propagates the cached value but at the same time links
 * both target values with one another.
 *  
 * @author Eric Bodden
 */
public class PathTrackingIFDSSolver<N, D extends PathTrackingIFDSSolver.LinkedNode<D>, M, I extends InterproceduralCFG<N, M>> extends IFDSSolver<N, D, M, I> {

	/**
	 * A data-flow fact that can be linked with other equal facts.
	 * Equality and hash-code operations must <i>not</i> take the linking data structures into account!
	 */
	public static interface LinkedNode<D> {
		/**
		 * Links this node to a neighbor node, i.e., to an abstraction that would have been merged
		 * with this one of paths were not being tracked.
		 */
		public void addNeighbor(D originalAbstraction);
	}

	public PathTrackingIFDSSolver(IFDSTabulationProblem<N, D, M, I> ifdsProblem) {
		super(ifdsProblem);
	}

	private final Map<CacheEntry, LinkedNode<D>> cache = Maps.newHashMap();
	
	@Override
	protected void propagate(D sourceVal, N target, D targetVal, EdgeFunction<IFDSSolver.BinaryDomain> f, N relatedCallSite, boolean isUnbalancedReturn) {
		CacheEntry currentCacheEntry = new CacheEntry(target, sourceVal, targetVal);

		boolean propagate = false;
		synchronized (this) {
			if (cache.containsKey(currentCacheEntry)) {
				LinkedNode<D> existingTargetVal = cache.get(currentCacheEntry);
				if (existingTargetVal != targetVal)
					existingTargetVal.addNeighbor(targetVal);
			} else {
				cache.put(currentCacheEntry, targetVal);
				propagate = true;
			}
		}

		if (propagate)
			super.propagate(sourceVal, target, targetVal, f, relatedCallSite, isUnbalancedReturn);
		
	};
	
	
	private class CacheEntry {
		private N n;
		private D sourceVal;
		private D targetVal;

		public CacheEntry(N n, D sourceVal, D targetVal) {
			super();
			this.n = n;
			this.sourceVal = sourceVal;
			this.targetVal = targetVal;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((sourceVal == null) ? 0 : sourceVal.hashCode());
			result = prime * result + ((targetVal == null) ? 0 : targetVal.hashCode());
			result = prime * result + ((n == null) ? 0 : n.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings({ "unchecked" })
			CacheEntry other = (CacheEntry) obj;
			if (sourceVal == null) {
				if (other.sourceVal != null)
					return false;
			} else if (!sourceVal.equals(other.sourceVal))
				return false;
			if (targetVal == null) {
				if (other.targetVal != null)
					return false;
			} else if (!targetVal.equals(other.targetVal))
				return false;
			if (n == null) {
				if (other.n != null)
					return false;
			} else if (!n.equals(other.n))
				return false;
			return true;
		}
	}	
	


}














d9ac548b7a4cb34392d771a15163ce5bf2854902


Switch branch/tag










heros


src


heros


solver


PathTrackingIFDSSolver.java



Find file
Normal viewHistoryPermalink






PathTrackingIFDSSolver.java



3.86 KB









Newer










Older









added a PathTrackingIFDSSolver which implements the keeping track of paths;



 


Eric Bodden
committed
Oct 18, 2013






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
package heros.solver;

import heros.EdgeFunction;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * An {@link IFDSSolver} that tracks paths for reporting. To do so, it requires that data-flow abstractions implement the LinkedNode interface.
 * The solver implements a cache of data-flow facts for each statement and source value. If for the same statement and source value the same
 * target value is seen again (as determined through a cache hit), then the solver propagates the cached value but at the same time links
 * both target values with one another.
 *  
 * @author Eric Bodden
 */
public class PathTrackingIFDSSolver<N, D extends PathTrackingIFDSSolver.LinkedNode<D>, M, I extends InterproceduralCFG<N, M>> extends IFDSSolver<N, D, M, I> {

	/**
	 * A data-flow fact that can be linked with other equal facts.
	 * Equality and hash-code operations must <i>not</i> take the linking data structures into account!
	 */
	public static interface LinkedNode<D> {
		/**
		 * Links this node to a neighbor node, i.e., to an abstraction that would have been merged
		 * with this one of paths were not being tracked.
		 */
		public void addNeighbor(D originalAbstraction);
	}

	public PathTrackingIFDSSolver(IFDSTabulationProblem<N, D, M, I> ifdsProblem) {
		super(ifdsProblem);
	}

	private final Map<CacheEntry, LinkedNode<D>> cache = Maps.newHashMap();
	
	@Override
	protected void propagate(D sourceVal, N target, D targetVal, EdgeFunction<IFDSSolver.BinaryDomain> f, N relatedCallSite, boolean isUnbalancedReturn) {
		CacheEntry currentCacheEntry = new CacheEntry(target, sourceVal, targetVal);

		boolean propagate = false;
		synchronized (this) {
			if (cache.containsKey(currentCacheEntry)) {
				LinkedNode<D> existingTargetVal = cache.get(currentCacheEntry);
				if (existingTargetVal != targetVal)
					existingTargetVal.addNeighbor(targetVal);
			} else {
				cache.put(currentCacheEntry, targetVal);
				propagate = true;
			}
		}

		if (propagate)
			super.propagate(sourceVal, target, targetVal, f, relatedCallSite, isUnbalancedReturn);
		
	};
	
	
	private class CacheEntry {
		private N n;
		private D sourceVal;
		private D targetVal;

		public CacheEntry(N n, D sourceVal, D targetVal) {
			super();
			this.n = n;
			this.sourceVal = sourceVal;
			this.targetVal = targetVal;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((sourceVal == null) ? 0 : sourceVal.hashCode());
			result = prime * result + ((targetVal == null) ? 0 : targetVal.hashCode());
			result = prime * result + ((n == null) ? 0 : n.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings({ "unchecked" })
			CacheEntry other = (CacheEntry) obj;
			if (sourceVal == null) {
				if (other.sourceVal != null)
					return false;
			} else if (!sourceVal.equals(other.sourceVal))
				return false;
			if (targetVal == null) {
				if (other.targetVal != null)
					return false;
			} else if (!targetVal.equals(other.targetVal))
				return false;
			if (n == null) {
				if (other.n != null)
					return false;
			} else if (!n.equals(other.n))
				return false;
			return true;
		}
	}	
	


}










d9ac548b7a4cb34392d771a15163ce5bf2854902


Switch branch/tag










heros


src


heros


solver


PathTrackingIFDSSolver.java



Find file
Normal viewHistoryPermalink




d9ac548b7a4cb34392d771a15163ce5bf2854902


Switch branch/tag










heros


src


heros


solver


PathTrackingIFDSSolver.java





d9ac548b7a4cb34392d771a15163ce5bf2854902


Switch branch/tag








d9ac548b7a4cb34392d771a15163ce5bf2854902


Switch branch/tag





d9ac548b7a4cb34392d771a15163ce5bf2854902

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src

heros

solver

PathTrackingIFDSSolver.java
Find file
Normal viewHistoryPermalink




PathTrackingIFDSSolver.java



3.86 KB









Newer










Older









added a PathTrackingIFDSSolver which implements the keeping track of paths;



 


Eric Bodden
committed
Oct 18, 2013






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
package heros.solver;

import heros.EdgeFunction;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * An {@link IFDSSolver} that tracks paths for reporting. To do so, it requires that data-flow abstractions implement the LinkedNode interface.
 * The solver implements a cache of data-flow facts for each statement and source value. If for the same statement and source value the same
 * target value is seen again (as determined through a cache hit), then the solver propagates the cached value but at the same time links
 * both target values with one another.
 *  
 * @author Eric Bodden
 */
public class PathTrackingIFDSSolver<N, D extends PathTrackingIFDSSolver.LinkedNode<D>, M, I extends InterproceduralCFG<N, M>> extends IFDSSolver<N, D, M, I> {

	/**
	 * A data-flow fact that can be linked with other equal facts.
	 * Equality and hash-code operations must <i>not</i> take the linking data structures into account!
	 */
	public static interface LinkedNode<D> {
		/**
		 * Links this node to a neighbor node, i.e., to an abstraction that would have been merged
		 * with this one of paths were not being tracked.
		 */
		public void addNeighbor(D originalAbstraction);
	}

	public PathTrackingIFDSSolver(IFDSTabulationProblem<N, D, M, I> ifdsProblem) {
		super(ifdsProblem);
	}

	private final Map<CacheEntry, LinkedNode<D>> cache = Maps.newHashMap();
	
	@Override
	protected void propagate(D sourceVal, N target, D targetVal, EdgeFunction<IFDSSolver.BinaryDomain> f, N relatedCallSite, boolean isUnbalancedReturn) {
		CacheEntry currentCacheEntry = new CacheEntry(target, sourceVal, targetVal);

		boolean propagate = false;
		synchronized (this) {
			if (cache.containsKey(currentCacheEntry)) {
				LinkedNode<D> existingTargetVal = cache.get(currentCacheEntry);
				if (existingTargetVal != targetVal)
					existingTargetVal.addNeighbor(targetVal);
			} else {
				cache.put(currentCacheEntry, targetVal);
				propagate = true;
			}
		}

		if (propagate)
			super.propagate(sourceVal, target, targetVal, f, relatedCallSite, isUnbalancedReturn);
		
	};
	
	
	private class CacheEntry {
		private N n;
		private D sourceVal;
		private D targetVal;

		public CacheEntry(N n, D sourceVal, D targetVal) {
			super();
			this.n = n;
			this.sourceVal = sourceVal;
			this.targetVal = targetVal;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((sourceVal == null) ? 0 : sourceVal.hashCode());
			result = prime * result + ((targetVal == null) ? 0 : targetVal.hashCode());
			result = prime * result + ((n == null) ? 0 : n.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings({ "unchecked" })
			CacheEntry other = (CacheEntry) obj;
			if (sourceVal == null) {
				if (other.sourceVal != null)
					return false;
			} else if (!sourceVal.equals(other.sourceVal))
				return false;
			if (targetVal == null) {
				if (other.targetVal != null)
					return false;
			} else if (!targetVal.equals(other.targetVal))
				return false;
			if (n == null) {
				if (other.n != null)
					return false;
			} else if (!n.equals(other.n))
				return false;
			return true;
		}
	}	
	


}








PathTrackingIFDSSolver.java



3.86 KB










PathTrackingIFDSSolver.java



3.86 KB









Newer










Older
NewerOlder







added a PathTrackingIFDSSolver which implements the keeping track of paths;



 


Eric Bodden
committed
Oct 18, 2013






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
package heros.solver;

import heros.EdgeFunction;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * An {@link IFDSSolver} that tracks paths for reporting. To do so, it requires that data-flow abstractions implement the LinkedNode interface.
 * The solver implements a cache of data-flow facts for each statement and source value. If for the same statement and source value the same
 * target value is seen again (as determined through a cache hit), then the solver propagates the cached value but at the same time links
 * both target values with one another.
 *  
 * @author Eric Bodden
 */
public class PathTrackingIFDSSolver<N, D extends PathTrackingIFDSSolver.LinkedNode<D>, M, I extends InterproceduralCFG<N, M>> extends IFDSSolver<N, D, M, I> {

	/**
	 * A data-flow fact that can be linked with other equal facts.
	 * Equality and hash-code operations must <i>not</i> take the linking data structures into account!
	 */
	public static interface LinkedNode<D> {
		/**
		 * Links this node to a neighbor node, i.e., to an abstraction that would have been merged
		 * with this one of paths were not being tracked.
		 */
		public void addNeighbor(D originalAbstraction);
	}

	public PathTrackingIFDSSolver(IFDSTabulationProblem<N, D, M, I> ifdsProblem) {
		super(ifdsProblem);
	}

	private final Map<CacheEntry, LinkedNode<D>> cache = Maps.newHashMap();
	
	@Override
	protected void propagate(D sourceVal, N target, D targetVal, EdgeFunction<IFDSSolver.BinaryDomain> f, N relatedCallSite, boolean isUnbalancedReturn) {
		CacheEntry currentCacheEntry = new CacheEntry(target, sourceVal, targetVal);

		boolean propagate = false;
		synchronized (this) {
			if (cache.containsKey(currentCacheEntry)) {
				LinkedNode<D> existingTargetVal = cache.get(currentCacheEntry);
				if (existingTargetVal != targetVal)
					existingTargetVal.addNeighbor(targetVal);
			} else {
				cache.put(currentCacheEntry, targetVal);
				propagate = true;
			}
		}

		if (propagate)
			super.propagate(sourceVal, target, targetVal, f, relatedCallSite, isUnbalancedReturn);
		
	};
	
	
	private class CacheEntry {
		private N n;
		private D sourceVal;
		private D targetVal;

		public CacheEntry(N n, D sourceVal, D targetVal) {
			super();
			this.n = n;
			this.sourceVal = sourceVal;
			this.targetVal = targetVal;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((sourceVal == null) ? 0 : sourceVal.hashCode());
			result = prime * result + ((targetVal == null) ? 0 : targetVal.hashCode());
			result = prime * result + ((n == null) ? 0 : n.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings({ "unchecked" })
			CacheEntry other = (CacheEntry) obj;
			if (sourceVal == null) {
				if (other.sourceVal != null)
					return false;
			} else if (!sourceVal.equals(other.sourceVal))
				return false;
			if (targetVal == null) {
				if (other.targetVal != null)
					return false;
			} else if (!targetVal.equals(other.targetVal))
				return false;
			if (n == null) {
				if (other.n != null)
					return false;
			} else if (!n.equals(other.n))
				return false;
			return true;
		}
	}	
	


}







added a PathTrackingIFDSSolver which implements the keeping track of paths;



 


Eric Bodden
committed
Oct 18, 2013



added a PathTrackingIFDSSolver which implements the keeping track of paths;



 

added a PathTrackingIFDSSolver which implements the keeping track of paths;


Eric Bodden
committed
Oct 18, 2013

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
/*******************************************************************************/******************************************************************************* * Copyright (c) 2013 Eric Bodden. * Copyright (c) 2013 Eric Bodden. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Eric Bodden - initial API and implementation *     Eric Bodden - initial API and implementation ******************************************************************************/ ******************************************************************************/package heros.solver;packageheros.solver;import heros.EdgeFunction;importheros.EdgeFunction;import heros.IFDSTabulationProblem;importheros.IFDSTabulationProblem;import heros.InterproceduralCFG;importheros.InterproceduralCFG;import java.util.Map;importjava.util.Map;import com.google.common.collect.Maps;importcom.google.common.collect.Maps;/**/** * An {@link IFDSSolver} that tracks paths for reporting. To do so, it requires that data-flow abstractions implement the LinkedNode interface. * An {@link IFDSSolver} that tracks paths for reporting. To do so, it requires that data-flow abstractions implement the LinkedNode interface. * The solver implements a cache of data-flow facts for each statement and source value. If for the same statement and source value the same * The solver implements a cache of data-flow facts for each statement and source value. If for the same statement and source value the same * target value is seen again (as determined through a cache hit), then the solver propagates the cached value but at the same time links * target value is seen again (as determined through a cache hit), then the solver propagates the cached value but at the same time links * both target values with one another. * both target values with one another. *   *   * @author Eric Bodden * @author Eric Bodden */ */public class PathTrackingIFDSSolver<N, D extends PathTrackingIFDSSolver.LinkedNode<D>, M, I extends InterproceduralCFG<N, M>> extends IFDSSolver<N, D, M, I> {publicclassPathTrackingIFDSSolver<N,DextendsPathTrackingIFDSSolver.LinkedNode<D>,M,IextendsInterproceduralCFG<N,M>>extendsIFDSSolver<N,D,M,I>{	/**/**	 * A data-flow fact that can be linked with other equal facts.	 * A data-flow fact that can be linked with other equal facts.	 * Equality and hash-code operations must <i>not</i> take the linking data structures into account!	 * Equality and hash-code operations must <i>not</i> take the linking data structures into account!	 */	 */	public static interface LinkedNode<D> {publicstaticinterfaceLinkedNode<D>{		/**/**		 * Links this node to a neighbor node, i.e., to an abstraction that would have been merged		 * Links this node to a neighbor node, i.e., to an abstraction that would have been merged		 * with this one of paths were not being tracked.		 * with this one of paths were not being tracked.		 */		 */		public void addNeighbor(D originalAbstraction);publicvoidaddNeighbor(DoriginalAbstraction);	}}	public PathTrackingIFDSSolver(IFDSTabulationProblem<N, D, M, I> ifdsProblem) {publicPathTrackingIFDSSolver(IFDSTabulationProblem<N,D,M,I>ifdsProblem){		super(ifdsProblem);super(ifdsProblem);	}}	private final Map<CacheEntry, LinkedNode<D>> cache = Maps.newHashMap();privatefinalMap<CacheEntry,LinkedNode<D>>cache=Maps.newHashMap();		@Override@Override	protected void propagate(D sourceVal, N target, D targetVal, EdgeFunction<IFDSSolver.BinaryDomain> f, N relatedCallSite, boolean isUnbalancedReturn) {protectedvoidpropagate(DsourceVal,Ntarget,DtargetVal,EdgeFunction<IFDSSolver.BinaryDomain>f,NrelatedCallSite,booleanisUnbalancedReturn){		CacheEntry currentCacheEntry = new CacheEntry(target, sourceVal, targetVal);CacheEntrycurrentCacheEntry=newCacheEntry(target,sourceVal,targetVal);		boolean propagate = false;booleanpropagate=false;		synchronized (this) {synchronized(this){			if (cache.containsKey(currentCacheEntry)) {if(cache.containsKey(currentCacheEntry)){				LinkedNode<D> existingTargetVal = cache.get(currentCacheEntry);LinkedNode<D>existingTargetVal=cache.get(currentCacheEntry);				if (existingTargetVal != targetVal)if(existingTargetVal!=targetVal)					existingTargetVal.addNeighbor(targetVal);existingTargetVal.addNeighbor(targetVal);			} else {}else{				cache.put(currentCacheEntry, targetVal);cache.put(currentCacheEntry,targetVal);				propagate = true;propagate=true;			}}		}}		if (propagate)if(propagate)			super.propagate(sourceVal, target, targetVal, f, relatedCallSite, isUnbalancedReturn);super.propagate(sourceVal,target,targetVal,f,relatedCallSite,isUnbalancedReturn);			};};			private class CacheEntry {privateclassCacheEntry{		private N n;privateNn;		private D sourceVal;privateDsourceVal;		private D targetVal;privateDtargetVal;		public CacheEntry(N n, D sourceVal, D targetVal) {publicCacheEntry(Nn,DsourceVal,DtargetVal){			super();super();			this.n = n;this.n=n;			this.sourceVal = sourceVal;this.sourceVal=sourceVal;			this.targetVal = targetVal;this.targetVal=targetVal;		}}		@Override@Override		public int hashCode() {publicinthashCode(){			final int prime = 31;finalintprime=31;			int result = 1;intresult=1;			result = prime * result + ((sourceVal == null) ? 0 : sourceVal.hashCode());result=prime*result+((sourceVal==null)?0:sourceVal.hashCode());			result = prime * result + ((targetVal == null) ? 0 : targetVal.hashCode());result=prime*result+((targetVal==null)?0:targetVal.hashCode());			result = prime * result + ((n == null) ? 0 : n.hashCode());result=prime*result+((n==null)?0:n.hashCode());			return result;returnresult;		}}		@Override@Override		public boolean equals(Object obj) {publicbooleanequals(Objectobj){			if (this == obj)if(this==obj)				return true;returntrue;			if (obj == null)if(obj==null)				return false;returnfalse;			if (getClass() != obj.getClass())if(getClass()!=obj.getClass())				return false;returnfalse;			@SuppressWarnings({ "unchecked" })@SuppressWarnings({"unchecked"})			CacheEntry other = (CacheEntry) obj;CacheEntryother=(CacheEntry)obj;			if (sourceVal == null) {if(sourceVal==null){				if (other.sourceVal != null)if(other.sourceVal!=null)					return false;returnfalse;			} else if (!sourceVal.equals(other.sourceVal))}elseif(!sourceVal.equals(other.sourceVal))				return false;returnfalse;			if (targetVal == null) {if(targetVal==null){				if (other.targetVal != null)if(other.targetVal!=null)					return false;returnfalse;			} else if (!targetVal.equals(other.targetVal))}elseif(!targetVal.equals(other.targetVal))				return false;returnfalse;			if (n == null) {if(n==null){				if (other.n != null)if(other.n!=null)					return false;returnfalse;			} else if (!n.equals(other.n))}elseif(!n.equals(other.n))				return false;returnfalse;			return true;returntrue;		}}	}	}	}}





