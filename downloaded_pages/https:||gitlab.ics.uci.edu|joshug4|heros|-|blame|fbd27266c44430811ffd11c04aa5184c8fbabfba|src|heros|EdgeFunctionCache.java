



GitLab


















Projects
Groups
Topics
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
Topics
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
Topics
Snippets



GitLab








GitLab









Projects
Groups
Topics
Snippets






Projects
Groups
Topics
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


Contributor statistics


Graph


Compare revisions







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


Artifacts


Schedules







Deployments




Deployments




Environments


Releases







Packages and registries




Packages and registries




Model experiments







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












Joshua Garcia heros

fbd27266c44430811ffd11c04aa5184c8fbabfba



















heros


src


heros


EdgeFunctionCache.java




Find file



Normal view


History


Permalink








EdgeFunctionCache.java



7.86 KiB









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



package heros;








initial checkin



Eric Bodden
committed
Nov 14, 2012




12


13


14


15




import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




16


17



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;








initial checkin



Eric Bodden
committed
Nov 14, 2012




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




public class EdgeFunctionCache<N, D, M, V> implements EdgeFunctions<N, D, M, V> {
	
	protected final EdgeFunctions<N, D, M, V> delegate;
	
	protected final LoadingCache<NDNDKey, EdgeFunction<V>> normalCache;
	
	protected final LoadingCache<CallKey, EdgeFunction<V>> callCache;

	protected final LoadingCache<ReturnKey, EdgeFunction<V>> returnCache;

	protected final LoadingCache<NDNDKey, EdgeFunction<V>> callToReturnCache;








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




30


31


32




    Logger logger = LoggerFactory.getLogger(getClass());









suppress warning

 


Eric Bodden
committed
Oct 28, 2013




33



	@SuppressWarnings("unchecked")








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


183


184


185


186


187


188


189


190


191


192


193


194


195


196


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


211


212


213


214


215


216


217


218


219


220


221


222


223


224


225


226


227


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


238


239


240


241


242


243


244


245


246


247


248


249


250


251


252


253


254


255


256


257


258


259


260


261


262


263


264


265


266


267


268


269


270


271



	public EdgeFunctionCache(final EdgeFunctions<N, D, M, V> delegate, @SuppressWarnings("rawtypes") CacheBuilder builder) {
		this.delegate = delegate;
		
		normalCache = builder.build(new CacheLoader<NDNDKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(NDNDKey key) throws Exception {
				return delegate.getNormalEdgeFunction(key.getN1(), key.getD1(), key.getN2(), key.getD2());
			}
		});
		
		callCache = builder.build(new CacheLoader<CallKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(CallKey key) throws Exception {
				return delegate.getCallEdgeFunction(key.getCallSite(), key.getD1(), key.getCalleeMethod(), key.getD2());
			}
		});
		
		returnCache = builder.build(new CacheLoader<ReturnKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(ReturnKey key) throws Exception {
				return delegate.getReturnEdgeFunction(key.getCallSite(), key.getCalleeMethod(), key.getExitStmt(), key.getD1(), key.getReturnSite(), key.getD2());
			}
		});
		
		callToReturnCache = builder.build(new CacheLoader<NDNDKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(NDNDKey key) throws Exception {
				return delegate.getCallToReturnEdgeFunction(key.getN1(), key.getD1(), key.getN2(), key.getD2());
			}
		});
	}

	public EdgeFunction<V> getNormalEdgeFunction(N curr, D currNode, N succ, D succNode) {
		return normalCache.getUnchecked(new NDNDKey(curr, currNode, succ, succNode));
	}

	public EdgeFunction<V> getCallEdgeFunction(N callStmt, D srcNode, M destinationMethod, D destNode) {
		return callCache.getUnchecked(new CallKey(callStmt, srcNode, destinationMethod, destNode));
	}

	public EdgeFunction<V> getReturnEdgeFunction(N callSite, M calleeMethod, N exitStmt, D exitNode, N returnSite, D retNode) {
		return returnCache.getUnchecked(new ReturnKey(callSite, calleeMethod, exitStmt, exitNode, returnSite, retNode));
	}

	public EdgeFunction<V> getCallToReturnEdgeFunction(N callSite, D callNode, N returnSite, D returnSideNode) {
		return callToReturnCache.getUnchecked(new NDNDKey(callSite, callNode, returnSite, returnSideNode));
	}


	private class NDNDKey {
		private final N n1, n2;
		private final D d1, d2;

		public NDNDKey(N n1, D d1, N n2, D d2) {
			this.n1 = n1;
			this.n2 = n2;
			this.d1 = d1;
			this.d2 = d2;
		}

		public N getN1() {
			return n1;
		}

		public D getD1() {
			return d1;
		}

		public N getN2() {
			return n2;
		}

		public D getD2() {
			return d2;
		}

		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((d1 == null) ? 0 : d1.hashCode());
			result = prime * result + ((d2 == null) ? 0 : d2.hashCode());
			result = prime * result + ((n1 == null) ? 0 : n1.hashCode());
			result = prime * result + ((n2 == null) ? 0 : n2.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("unchecked")
			NDNDKey other = (NDNDKey) obj;
			if (d1 == null) {
				if (other.d1 != null)
					return false;
			} else if (!d1.equals(other.d1))
				return false;
			if (d2 == null) {
				if (other.d2 != null)
					return false;
			} else if (!d2.equals(other.d2))
				return false;
			if (n1 == null) {
				if (other.n1 != null)
					return false;
			} else if (!n1.equals(other.n1))
				return false;
			if (n2 == null) {
				if (other.n2 != null)
					return false;
			} else if (!n2.equals(other.n2))
				return false;
			return true;
		}
	}
	
	private class CallKey {
		private final N callSite;
		private final M calleeMethod;
		private final D d1, d2;

		public CallKey(N callSite, D d1, M calleeMethod, D d2) {
			this.callSite = callSite;
			this.calleeMethod = calleeMethod;
			this.d1 = d1;
			this.d2 = d2;
		}

		public N getCallSite() {
			return callSite;
		}

		public D getD1() {
			return d1;
		}

		public M getCalleeMethod() {
			return calleeMethod;
		}

		public D getD2() {
			return d2;
		}

		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((d1 == null) ? 0 : d1.hashCode());
			result = prime * result + ((d2 == null) ? 0 : d2.hashCode());
			result = prime * result + ((callSite == null) ? 0 : callSite.hashCode());
			result = prime * result + ((calleeMethod == null) ? 0 : calleeMethod.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("unchecked")
			CallKey other = (CallKey) obj;
			if (d1 == null) {
				if (other.d1 != null)
					return false;
			} else if (!d1.equals(other.d1))
				return false;
			if (d2 == null) {
				if (other.d2 != null)
					return false;
			} else if (!d2.equals(other.d2))
				return false;
			if (callSite == null) {
				if (other.callSite != null)
					return false;
			} else if (!callSite.equals(other.callSite))
				return false;
			if (calleeMethod == null) {
				if (other.calleeMethod != null)
					return false;
			} else if (!calleeMethod.equals(other.calleeMethod))
				return false;
			return true;
		}
	}


	private class ReturnKey extends CallKey {
		
		private final N exitStmt, returnSite;

		public ReturnKey(N callSite, M calleeMethod, N exitStmt, D exitNode, N returnSite, D retNode) {
			super(callSite, exitNode, calleeMethod, retNode);
			this.exitStmt = exitStmt;
			this.returnSite = returnSite;
		}
		
		public N getExitStmt() {
			return exitStmt;
		}
		
		public N getReturnSite() {
			return returnSite;
		}

		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((exitStmt == null) ? 0 : exitStmt.hashCode());
			result = prime * result + ((returnSite == null) ? 0 : returnSite.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("unchecked")
			ReturnKey other = (ReturnKey) obj;
			if (exitStmt == null) {
				if (other.exitStmt != null)
					return false;
			} else if (!exitStmt.equals(other.exitStmt))
				return false;
			if (returnSite == null) {
				if (other.returnSite != null)
					return false;
			} else if (!returnSite.equals(other.returnSite))
				return false;
			return true;
		}
	}


	public void printStats() {








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




272


273


274


275


276


277



        logger.debug("Stats for edge-function cache:\n" +
                     "Normal:         {}\n"+
                     "Call:           {}\n"+
                     "Return:         {}\n"+
                     "Call-to-return: {}\n",
                normalCache.stats(), callCache.stats(),returnCache.stats(),callToReturnCache.stats());








initial checkin



Eric Bodden
committed
Nov 14, 2012




278


279


280



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


Contributor statistics


Graph


Compare revisions







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


Artifacts


Schedules







Deployments




Deployments




Environments


Releases







Packages and registries




Packages and registries




Model experiments







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


Contributor statistics


Graph


Compare revisions






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

Contributor statistics


Contributor statistics

Graph


Graph

Compare revisions


Compare revisions




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


Artifacts


Schedules






CI/CD


CI/CD




CI/CD


Pipelines


Pipelines

Jobs


Jobs

Artifacts


Artifacts

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




Packages and registries




Packages and registries




Model experiments






Packages and registries


Packages and registries




Packages and registries


Model experiments


Model experiments




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








Joshua Garcia heros

fbd27266c44430811ffd11c04aa5184c8fbabfba



















heros


src


heros


EdgeFunctionCache.java




Find file



Normal view


History


Permalink








EdgeFunctionCache.java



7.86 KiB









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



package heros;








initial checkin



Eric Bodden
committed
Nov 14, 2012




12


13


14


15




import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




16


17



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;








initial checkin



Eric Bodden
committed
Nov 14, 2012




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




public class EdgeFunctionCache<N, D, M, V> implements EdgeFunctions<N, D, M, V> {
	
	protected final EdgeFunctions<N, D, M, V> delegate;
	
	protected final LoadingCache<NDNDKey, EdgeFunction<V>> normalCache;
	
	protected final LoadingCache<CallKey, EdgeFunction<V>> callCache;

	protected final LoadingCache<ReturnKey, EdgeFunction<V>> returnCache;

	protected final LoadingCache<NDNDKey, EdgeFunction<V>> callToReturnCache;








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




30


31


32




    Logger logger = LoggerFactory.getLogger(getClass());









suppress warning

 


Eric Bodden
committed
Oct 28, 2013




33



	@SuppressWarnings("unchecked")








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


183


184


185


186


187


188


189


190


191


192


193


194


195


196


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


211


212


213


214


215


216


217


218


219


220


221


222


223


224


225


226


227


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


238


239


240


241


242


243


244


245


246


247


248


249


250


251


252


253


254


255


256


257


258


259


260


261


262


263


264


265


266


267


268


269


270


271



	public EdgeFunctionCache(final EdgeFunctions<N, D, M, V> delegate, @SuppressWarnings("rawtypes") CacheBuilder builder) {
		this.delegate = delegate;
		
		normalCache = builder.build(new CacheLoader<NDNDKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(NDNDKey key) throws Exception {
				return delegate.getNormalEdgeFunction(key.getN1(), key.getD1(), key.getN2(), key.getD2());
			}
		});
		
		callCache = builder.build(new CacheLoader<CallKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(CallKey key) throws Exception {
				return delegate.getCallEdgeFunction(key.getCallSite(), key.getD1(), key.getCalleeMethod(), key.getD2());
			}
		});
		
		returnCache = builder.build(new CacheLoader<ReturnKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(ReturnKey key) throws Exception {
				return delegate.getReturnEdgeFunction(key.getCallSite(), key.getCalleeMethod(), key.getExitStmt(), key.getD1(), key.getReturnSite(), key.getD2());
			}
		});
		
		callToReturnCache = builder.build(new CacheLoader<NDNDKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(NDNDKey key) throws Exception {
				return delegate.getCallToReturnEdgeFunction(key.getN1(), key.getD1(), key.getN2(), key.getD2());
			}
		});
	}

	public EdgeFunction<V> getNormalEdgeFunction(N curr, D currNode, N succ, D succNode) {
		return normalCache.getUnchecked(new NDNDKey(curr, currNode, succ, succNode));
	}

	public EdgeFunction<V> getCallEdgeFunction(N callStmt, D srcNode, M destinationMethod, D destNode) {
		return callCache.getUnchecked(new CallKey(callStmt, srcNode, destinationMethod, destNode));
	}

	public EdgeFunction<V> getReturnEdgeFunction(N callSite, M calleeMethod, N exitStmt, D exitNode, N returnSite, D retNode) {
		return returnCache.getUnchecked(new ReturnKey(callSite, calleeMethod, exitStmt, exitNode, returnSite, retNode));
	}

	public EdgeFunction<V> getCallToReturnEdgeFunction(N callSite, D callNode, N returnSite, D returnSideNode) {
		return callToReturnCache.getUnchecked(new NDNDKey(callSite, callNode, returnSite, returnSideNode));
	}


	private class NDNDKey {
		private final N n1, n2;
		private final D d1, d2;

		public NDNDKey(N n1, D d1, N n2, D d2) {
			this.n1 = n1;
			this.n2 = n2;
			this.d1 = d1;
			this.d2 = d2;
		}

		public N getN1() {
			return n1;
		}

		public D getD1() {
			return d1;
		}

		public N getN2() {
			return n2;
		}

		public D getD2() {
			return d2;
		}

		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((d1 == null) ? 0 : d1.hashCode());
			result = prime * result + ((d2 == null) ? 0 : d2.hashCode());
			result = prime * result + ((n1 == null) ? 0 : n1.hashCode());
			result = prime * result + ((n2 == null) ? 0 : n2.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("unchecked")
			NDNDKey other = (NDNDKey) obj;
			if (d1 == null) {
				if (other.d1 != null)
					return false;
			} else if (!d1.equals(other.d1))
				return false;
			if (d2 == null) {
				if (other.d2 != null)
					return false;
			} else if (!d2.equals(other.d2))
				return false;
			if (n1 == null) {
				if (other.n1 != null)
					return false;
			} else if (!n1.equals(other.n1))
				return false;
			if (n2 == null) {
				if (other.n2 != null)
					return false;
			} else if (!n2.equals(other.n2))
				return false;
			return true;
		}
	}
	
	private class CallKey {
		private final N callSite;
		private final M calleeMethod;
		private final D d1, d2;

		public CallKey(N callSite, D d1, M calleeMethod, D d2) {
			this.callSite = callSite;
			this.calleeMethod = calleeMethod;
			this.d1 = d1;
			this.d2 = d2;
		}

		public N getCallSite() {
			return callSite;
		}

		public D getD1() {
			return d1;
		}

		public M getCalleeMethod() {
			return calleeMethod;
		}

		public D getD2() {
			return d2;
		}

		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((d1 == null) ? 0 : d1.hashCode());
			result = prime * result + ((d2 == null) ? 0 : d2.hashCode());
			result = prime * result + ((callSite == null) ? 0 : callSite.hashCode());
			result = prime * result + ((calleeMethod == null) ? 0 : calleeMethod.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("unchecked")
			CallKey other = (CallKey) obj;
			if (d1 == null) {
				if (other.d1 != null)
					return false;
			} else if (!d1.equals(other.d1))
				return false;
			if (d2 == null) {
				if (other.d2 != null)
					return false;
			} else if (!d2.equals(other.d2))
				return false;
			if (callSite == null) {
				if (other.callSite != null)
					return false;
			} else if (!callSite.equals(other.callSite))
				return false;
			if (calleeMethod == null) {
				if (other.calleeMethod != null)
					return false;
			} else if (!calleeMethod.equals(other.calleeMethod))
				return false;
			return true;
		}
	}


	private class ReturnKey extends CallKey {
		
		private final N exitStmt, returnSite;

		public ReturnKey(N callSite, M calleeMethod, N exitStmt, D exitNode, N returnSite, D retNode) {
			super(callSite, exitNode, calleeMethod, retNode);
			this.exitStmt = exitStmt;
			this.returnSite = returnSite;
		}
		
		public N getExitStmt() {
			return exitStmt;
		}
		
		public N getReturnSite() {
			return returnSite;
		}

		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((exitStmt == null) ? 0 : exitStmt.hashCode());
			result = prime * result + ((returnSite == null) ? 0 : returnSite.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("unchecked")
			ReturnKey other = (ReturnKey) obj;
			if (exitStmt == null) {
				if (other.exitStmt != null)
					return false;
			} else if (!exitStmt.equals(other.exitStmt))
				return false;
			if (returnSite == null) {
				if (other.returnSite != null)
					return false;
			} else if (!returnSite.equals(other.returnSite))
				return false;
			return true;
		}
	}


	public void printStats() {








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




272


273


274


275


276


277



        logger.debug("Stats for edge-function cache:\n" +
                     "Normal:         {}\n"+
                     "Call:           {}\n"+
                     "Return:         {}\n"+
                     "Call-to-return: {}\n",
                normalCache.stats(), callCache.stats(),returnCache.stats(),callToReturnCache.stats());








initial checkin



Eric Bodden
committed
Nov 14, 2012




278


279


280



	}

}
















Joshua Garcia heros

fbd27266c44430811ffd11c04aa5184c8fbabfba












Joshua Garcia heros

fbd27266c44430811ffd11c04aa5184c8fbabfba










Joshua Garcia heros

fbd27266c44430811ffd11c04aa5184c8fbabfba




Joshua Garciaherosheros
fbd27266c44430811ffd11c04aa5184c8fbabfba












heros


src


heros


EdgeFunctionCache.java




Find file



Normal view


History


Permalink








EdgeFunctionCache.java



7.86 KiB









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



package heros;








initial checkin



Eric Bodden
committed
Nov 14, 2012




12


13


14


15




import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




16


17



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;








initial checkin



Eric Bodden
committed
Nov 14, 2012




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




public class EdgeFunctionCache<N, D, M, V> implements EdgeFunctions<N, D, M, V> {
	
	protected final EdgeFunctions<N, D, M, V> delegate;
	
	protected final LoadingCache<NDNDKey, EdgeFunction<V>> normalCache;
	
	protected final LoadingCache<CallKey, EdgeFunction<V>> callCache;

	protected final LoadingCache<ReturnKey, EdgeFunction<V>> returnCache;

	protected final LoadingCache<NDNDKey, EdgeFunction<V>> callToReturnCache;








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




30


31


32




    Logger logger = LoggerFactory.getLogger(getClass());









suppress warning

 


Eric Bodden
committed
Oct 28, 2013




33



	@SuppressWarnings("unchecked")








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


183


184


185


186


187


188


189


190


191


192


193


194


195


196


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


211


212


213


214


215


216


217


218


219


220


221


222


223


224


225


226


227


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


238


239


240


241


242


243


244


245


246


247


248


249


250


251


252


253


254


255


256


257


258


259


260


261


262


263


264


265


266


267


268


269


270


271



	public EdgeFunctionCache(final EdgeFunctions<N, D, M, V> delegate, @SuppressWarnings("rawtypes") CacheBuilder builder) {
		this.delegate = delegate;
		
		normalCache = builder.build(new CacheLoader<NDNDKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(NDNDKey key) throws Exception {
				return delegate.getNormalEdgeFunction(key.getN1(), key.getD1(), key.getN2(), key.getD2());
			}
		});
		
		callCache = builder.build(new CacheLoader<CallKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(CallKey key) throws Exception {
				return delegate.getCallEdgeFunction(key.getCallSite(), key.getD1(), key.getCalleeMethod(), key.getD2());
			}
		});
		
		returnCache = builder.build(new CacheLoader<ReturnKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(ReturnKey key) throws Exception {
				return delegate.getReturnEdgeFunction(key.getCallSite(), key.getCalleeMethod(), key.getExitStmt(), key.getD1(), key.getReturnSite(), key.getD2());
			}
		});
		
		callToReturnCache = builder.build(new CacheLoader<NDNDKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(NDNDKey key) throws Exception {
				return delegate.getCallToReturnEdgeFunction(key.getN1(), key.getD1(), key.getN2(), key.getD2());
			}
		});
	}

	public EdgeFunction<V> getNormalEdgeFunction(N curr, D currNode, N succ, D succNode) {
		return normalCache.getUnchecked(new NDNDKey(curr, currNode, succ, succNode));
	}

	public EdgeFunction<V> getCallEdgeFunction(N callStmt, D srcNode, M destinationMethod, D destNode) {
		return callCache.getUnchecked(new CallKey(callStmt, srcNode, destinationMethod, destNode));
	}

	public EdgeFunction<V> getReturnEdgeFunction(N callSite, M calleeMethod, N exitStmt, D exitNode, N returnSite, D retNode) {
		return returnCache.getUnchecked(new ReturnKey(callSite, calleeMethod, exitStmt, exitNode, returnSite, retNode));
	}

	public EdgeFunction<V> getCallToReturnEdgeFunction(N callSite, D callNode, N returnSite, D returnSideNode) {
		return callToReturnCache.getUnchecked(new NDNDKey(callSite, callNode, returnSite, returnSideNode));
	}


	private class NDNDKey {
		private final N n1, n2;
		private final D d1, d2;

		public NDNDKey(N n1, D d1, N n2, D d2) {
			this.n1 = n1;
			this.n2 = n2;
			this.d1 = d1;
			this.d2 = d2;
		}

		public N getN1() {
			return n1;
		}

		public D getD1() {
			return d1;
		}

		public N getN2() {
			return n2;
		}

		public D getD2() {
			return d2;
		}

		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((d1 == null) ? 0 : d1.hashCode());
			result = prime * result + ((d2 == null) ? 0 : d2.hashCode());
			result = prime * result + ((n1 == null) ? 0 : n1.hashCode());
			result = prime * result + ((n2 == null) ? 0 : n2.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("unchecked")
			NDNDKey other = (NDNDKey) obj;
			if (d1 == null) {
				if (other.d1 != null)
					return false;
			} else if (!d1.equals(other.d1))
				return false;
			if (d2 == null) {
				if (other.d2 != null)
					return false;
			} else if (!d2.equals(other.d2))
				return false;
			if (n1 == null) {
				if (other.n1 != null)
					return false;
			} else if (!n1.equals(other.n1))
				return false;
			if (n2 == null) {
				if (other.n2 != null)
					return false;
			} else if (!n2.equals(other.n2))
				return false;
			return true;
		}
	}
	
	private class CallKey {
		private final N callSite;
		private final M calleeMethod;
		private final D d1, d2;

		public CallKey(N callSite, D d1, M calleeMethod, D d2) {
			this.callSite = callSite;
			this.calleeMethod = calleeMethod;
			this.d1 = d1;
			this.d2 = d2;
		}

		public N getCallSite() {
			return callSite;
		}

		public D getD1() {
			return d1;
		}

		public M getCalleeMethod() {
			return calleeMethod;
		}

		public D getD2() {
			return d2;
		}

		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((d1 == null) ? 0 : d1.hashCode());
			result = prime * result + ((d2 == null) ? 0 : d2.hashCode());
			result = prime * result + ((callSite == null) ? 0 : callSite.hashCode());
			result = prime * result + ((calleeMethod == null) ? 0 : calleeMethod.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("unchecked")
			CallKey other = (CallKey) obj;
			if (d1 == null) {
				if (other.d1 != null)
					return false;
			} else if (!d1.equals(other.d1))
				return false;
			if (d2 == null) {
				if (other.d2 != null)
					return false;
			} else if (!d2.equals(other.d2))
				return false;
			if (callSite == null) {
				if (other.callSite != null)
					return false;
			} else if (!callSite.equals(other.callSite))
				return false;
			if (calleeMethod == null) {
				if (other.calleeMethod != null)
					return false;
			} else if (!calleeMethod.equals(other.calleeMethod))
				return false;
			return true;
		}
	}


	private class ReturnKey extends CallKey {
		
		private final N exitStmt, returnSite;

		public ReturnKey(N callSite, M calleeMethod, N exitStmt, D exitNode, N returnSite, D retNode) {
			super(callSite, exitNode, calleeMethod, retNode);
			this.exitStmt = exitStmt;
			this.returnSite = returnSite;
		}
		
		public N getExitStmt() {
			return exitStmt;
		}
		
		public N getReturnSite() {
			return returnSite;
		}

		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((exitStmt == null) ? 0 : exitStmt.hashCode());
			result = prime * result + ((returnSite == null) ? 0 : returnSite.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("unchecked")
			ReturnKey other = (ReturnKey) obj;
			if (exitStmt == null) {
				if (other.exitStmt != null)
					return false;
			} else if (!exitStmt.equals(other.exitStmt))
				return false;
			if (returnSite == null) {
				if (other.returnSite != null)
					return false;
			} else if (!returnSite.equals(other.returnSite))
				return false;
			return true;
		}
	}


	public void printStats() {








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




272


273


274


275


276


277



        logger.debug("Stats for edge-function cache:\n" +
                     "Normal:         {}\n"+
                     "Call:           {}\n"+
                     "Return:         {}\n"+
                     "Call-to-return: {}\n",
                normalCache.stats(), callCache.stats(),returnCache.stats(),callToReturnCache.stats());








initial checkin



Eric Bodden
committed
Nov 14, 2012




278


279


280



	}

}

















heros


src


heros


EdgeFunctionCache.java




Find file



Normal view


History


Permalink








EdgeFunctionCache.java



7.86 KiB









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



package heros;








initial checkin



Eric Bodden
committed
Nov 14, 2012




12


13


14


15




import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




16


17



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;








initial checkin



Eric Bodden
committed
Nov 14, 2012




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




public class EdgeFunctionCache<N, D, M, V> implements EdgeFunctions<N, D, M, V> {
	
	protected final EdgeFunctions<N, D, M, V> delegate;
	
	protected final LoadingCache<NDNDKey, EdgeFunction<V>> normalCache;
	
	protected final LoadingCache<CallKey, EdgeFunction<V>> callCache;

	protected final LoadingCache<ReturnKey, EdgeFunction<V>> returnCache;

	protected final LoadingCache<NDNDKey, EdgeFunction<V>> callToReturnCache;








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




30


31


32




    Logger logger = LoggerFactory.getLogger(getClass());









suppress warning

 


Eric Bodden
committed
Oct 28, 2013




33



	@SuppressWarnings("unchecked")








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


183


184


185


186


187


188


189


190


191


192


193


194


195


196


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


211


212


213


214


215


216


217


218


219


220


221


222


223


224


225


226


227


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


238


239


240


241


242


243


244


245


246


247


248


249


250


251


252


253


254


255


256


257


258


259


260


261


262


263


264


265


266


267


268


269


270


271



	public EdgeFunctionCache(final EdgeFunctions<N, D, M, V> delegate, @SuppressWarnings("rawtypes") CacheBuilder builder) {
		this.delegate = delegate;
		
		normalCache = builder.build(new CacheLoader<NDNDKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(NDNDKey key) throws Exception {
				return delegate.getNormalEdgeFunction(key.getN1(), key.getD1(), key.getN2(), key.getD2());
			}
		});
		
		callCache = builder.build(new CacheLoader<CallKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(CallKey key) throws Exception {
				return delegate.getCallEdgeFunction(key.getCallSite(), key.getD1(), key.getCalleeMethod(), key.getD2());
			}
		});
		
		returnCache = builder.build(new CacheLoader<ReturnKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(ReturnKey key) throws Exception {
				return delegate.getReturnEdgeFunction(key.getCallSite(), key.getCalleeMethod(), key.getExitStmt(), key.getD1(), key.getReturnSite(), key.getD2());
			}
		});
		
		callToReturnCache = builder.build(new CacheLoader<NDNDKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(NDNDKey key) throws Exception {
				return delegate.getCallToReturnEdgeFunction(key.getN1(), key.getD1(), key.getN2(), key.getD2());
			}
		});
	}

	public EdgeFunction<V> getNormalEdgeFunction(N curr, D currNode, N succ, D succNode) {
		return normalCache.getUnchecked(new NDNDKey(curr, currNode, succ, succNode));
	}

	public EdgeFunction<V> getCallEdgeFunction(N callStmt, D srcNode, M destinationMethod, D destNode) {
		return callCache.getUnchecked(new CallKey(callStmt, srcNode, destinationMethod, destNode));
	}

	public EdgeFunction<V> getReturnEdgeFunction(N callSite, M calleeMethod, N exitStmt, D exitNode, N returnSite, D retNode) {
		return returnCache.getUnchecked(new ReturnKey(callSite, calleeMethod, exitStmt, exitNode, returnSite, retNode));
	}

	public EdgeFunction<V> getCallToReturnEdgeFunction(N callSite, D callNode, N returnSite, D returnSideNode) {
		return callToReturnCache.getUnchecked(new NDNDKey(callSite, callNode, returnSite, returnSideNode));
	}


	private class NDNDKey {
		private final N n1, n2;
		private final D d1, d2;

		public NDNDKey(N n1, D d1, N n2, D d2) {
			this.n1 = n1;
			this.n2 = n2;
			this.d1 = d1;
			this.d2 = d2;
		}

		public N getN1() {
			return n1;
		}

		public D getD1() {
			return d1;
		}

		public N getN2() {
			return n2;
		}

		public D getD2() {
			return d2;
		}

		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((d1 == null) ? 0 : d1.hashCode());
			result = prime * result + ((d2 == null) ? 0 : d2.hashCode());
			result = prime * result + ((n1 == null) ? 0 : n1.hashCode());
			result = prime * result + ((n2 == null) ? 0 : n2.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("unchecked")
			NDNDKey other = (NDNDKey) obj;
			if (d1 == null) {
				if (other.d1 != null)
					return false;
			} else if (!d1.equals(other.d1))
				return false;
			if (d2 == null) {
				if (other.d2 != null)
					return false;
			} else if (!d2.equals(other.d2))
				return false;
			if (n1 == null) {
				if (other.n1 != null)
					return false;
			} else if (!n1.equals(other.n1))
				return false;
			if (n2 == null) {
				if (other.n2 != null)
					return false;
			} else if (!n2.equals(other.n2))
				return false;
			return true;
		}
	}
	
	private class CallKey {
		private final N callSite;
		private final M calleeMethod;
		private final D d1, d2;

		public CallKey(N callSite, D d1, M calleeMethod, D d2) {
			this.callSite = callSite;
			this.calleeMethod = calleeMethod;
			this.d1 = d1;
			this.d2 = d2;
		}

		public N getCallSite() {
			return callSite;
		}

		public D getD1() {
			return d1;
		}

		public M getCalleeMethod() {
			return calleeMethod;
		}

		public D getD2() {
			return d2;
		}

		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((d1 == null) ? 0 : d1.hashCode());
			result = prime * result + ((d2 == null) ? 0 : d2.hashCode());
			result = prime * result + ((callSite == null) ? 0 : callSite.hashCode());
			result = prime * result + ((calleeMethod == null) ? 0 : calleeMethod.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("unchecked")
			CallKey other = (CallKey) obj;
			if (d1 == null) {
				if (other.d1 != null)
					return false;
			} else if (!d1.equals(other.d1))
				return false;
			if (d2 == null) {
				if (other.d2 != null)
					return false;
			} else if (!d2.equals(other.d2))
				return false;
			if (callSite == null) {
				if (other.callSite != null)
					return false;
			} else if (!callSite.equals(other.callSite))
				return false;
			if (calleeMethod == null) {
				if (other.calleeMethod != null)
					return false;
			} else if (!calleeMethod.equals(other.calleeMethod))
				return false;
			return true;
		}
	}


	private class ReturnKey extends CallKey {
		
		private final N exitStmt, returnSite;

		public ReturnKey(N callSite, M calleeMethod, N exitStmt, D exitNode, N returnSite, D retNode) {
			super(callSite, exitNode, calleeMethod, retNode);
			this.exitStmt = exitStmt;
			this.returnSite = returnSite;
		}
		
		public N getExitStmt() {
			return exitStmt;
		}
		
		public N getReturnSite() {
			return returnSite;
		}

		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((exitStmt == null) ? 0 : exitStmt.hashCode());
			result = prime * result + ((returnSite == null) ? 0 : returnSite.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("unchecked")
			ReturnKey other = (ReturnKey) obj;
			if (exitStmt == null) {
				if (other.exitStmt != null)
					return false;
			} else if (!exitStmt.equals(other.exitStmt))
				return false;
			if (returnSite == null) {
				if (other.returnSite != null)
					return false;
			} else if (!returnSite.equals(other.returnSite))
				return false;
			return true;
		}
	}


	public void printStats() {








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




272


273


274


275


276


277



        logger.debug("Stats for edge-function cache:\n" +
                     "Normal:         {}\n"+
                     "Call:           {}\n"+
                     "Return:         {}\n"+
                     "Call-to-return: {}\n",
                normalCache.stats(), callCache.stats(),returnCache.stats(),callToReturnCache.stats());








initial checkin



Eric Bodden
committed
Nov 14, 2012




278


279


280



	}

}













heros


src


heros


EdgeFunctionCache.java




Find file



Normal view


History


Permalink








heros


src


heros


EdgeFunctionCache.java





heros

src

heros

EdgeFunctionCache.java

Find file



Normal view


History


Permalink


Find file


Normal view

History

Permalink





EdgeFunctionCache.java



7.86 KiB









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



package heros;








initial checkin



Eric Bodden
committed
Nov 14, 2012




12


13


14


15




import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




16


17



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;








initial checkin



Eric Bodden
committed
Nov 14, 2012




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




public class EdgeFunctionCache<N, D, M, V> implements EdgeFunctions<N, D, M, V> {
	
	protected final EdgeFunctions<N, D, M, V> delegate;
	
	protected final LoadingCache<NDNDKey, EdgeFunction<V>> normalCache;
	
	protected final LoadingCache<CallKey, EdgeFunction<V>> callCache;

	protected final LoadingCache<ReturnKey, EdgeFunction<V>> returnCache;

	protected final LoadingCache<NDNDKey, EdgeFunction<V>> callToReturnCache;








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




30


31


32




    Logger logger = LoggerFactory.getLogger(getClass());









suppress warning

 


Eric Bodden
committed
Oct 28, 2013




33



	@SuppressWarnings("unchecked")








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


183


184


185


186


187


188


189


190


191


192


193


194


195


196


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


211


212


213


214


215


216


217


218


219


220


221


222


223


224


225


226


227


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


238


239


240


241


242


243


244


245


246


247


248


249


250


251


252


253


254


255


256


257


258


259


260


261


262


263


264


265


266


267


268


269


270


271



	public EdgeFunctionCache(final EdgeFunctions<N, D, M, V> delegate, @SuppressWarnings("rawtypes") CacheBuilder builder) {
		this.delegate = delegate;
		
		normalCache = builder.build(new CacheLoader<NDNDKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(NDNDKey key) throws Exception {
				return delegate.getNormalEdgeFunction(key.getN1(), key.getD1(), key.getN2(), key.getD2());
			}
		});
		
		callCache = builder.build(new CacheLoader<CallKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(CallKey key) throws Exception {
				return delegate.getCallEdgeFunction(key.getCallSite(), key.getD1(), key.getCalleeMethod(), key.getD2());
			}
		});
		
		returnCache = builder.build(new CacheLoader<ReturnKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(ReturnKey key) throws Exception {
				return delegate.getReturnEdgeFunction(key.getCallSite(), key.getCalleeMethod(), key.getExitStmt(), key.getD1(), key.getReturnSite(), key.getD2());
			}
		});
		
		callToReturnCache = builder.build(new CacheLoader<NDNDKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(NDNDKey key) throws Exception {
				return delegate.getCallToReturnEdgeFunction(key.getN1(), key.getD1(), key.getN2(), key.getD2());
			}
		});
	}

	public EdgeFunction<V> getNormalEdgeFunction(N curr, D currNode, N succ, D succNode) {
		return normalCache.getUnchecked(new NDNDKey(curr, currNode, succ, succNode));
	}

	public EdgeFunction<V> getCallEdgeFunction(N callStmt, D srcNode, M destinationMethod, D destNode) {
		return callCache.getUnchecked(new CallKey(callStmt, srcNode, destinationMethod, destNode));
	}

	public EdgeFunction<V> getReturnEdgeFunction(N callSite, M calleeMethod, N exitStmt, D exitNode, N returnSite, D retNode) {
		return returnCache.getUnchecked(new ReturnKey(callSite, calleeMethod, exitStmt, exitNode, returnSite, retNode));
	}

	public EdgeFunction<V> getCallToReturnEdgeFunction(N callSite, D callNode, N returnSite, D returnSideNode) {
		return callToReturnCache.getUnchecked(new NDNDKey(callSite, callNode, returnSite, returnSideNode));
	}


	private class NDNDKey {
		private final N n1, n2;
		private final D d1, d2;

		public NDNDKey(N n1, D d1, N n2, D d2) {
			this.n1 = n1;
			this.n2 = n2;
			this.d1 = d1;
			this.d2 = d2;
		}

		public N getN1() {
			return n1;
		}

		public D getD1() {
			return d1;
		}

		public N getN2() {
			return n2;
		}

		public D getD2() {
			return d2;
		}

		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((d1 == null) ? 0 : d1.hashCode());
			result = prime * result + ((d2 == null) ? 0 : d2.hashCode());
			result = prime * result + ((n1 == null) ? 0 : n1.hashCode());
			result = prime * result + ((n2 == null) ? 0 : n2.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("unchecked")
			NDNDKey other = (NDNDKey) obj;
			if (d1 == null) {
				if (other.d1 != null)
					return false;
			} else if (!d1.equals(other.d1))
				return false;
			if (d2 == null) {
				if (other.d2 != null)
					return false;
			} else if (!d2.equals(other.d2))
				return false;
			if (n1 == null) {
				if (other.n1 != null)
					return false;
			} else if (!n1.equals(other.n1))
				return false;
			if (n2 == null) {
				if (other.n2 != null)
					return false;
			} else if (!n2.equals(other.n2))
				return false;
			return true;
		}
	}
	
	private class CallKey {
		private final N callSite;
		private final M calleeMethod;
		private final D d1, d2;

		public CallKey(N callSite, D d1, M calleeMethod, D d2) {
			this.callSite = callSite;
			this.calleeMethod = calleeMethod;
			this.d1 = d1;
			this.d2 = d2;
		}

		public N getCallSite() {
			return callSite;
		}

		public D getD1() {
			return d1;
		}

		public M getCalleeMethod() {
			return calleeMethod;
		}

		public D getD2() {
			return d2;
		}

		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((d1 == null) ? 0 : d1.hashCode());
			result = prime * result + ((d2 == null) ? 0 : d2.hashCode());
			result = prime * result + ((callSite == null) ? 0 : callSite.hashCode());
			result = prime * result + ((calleeMethod == null) ? 0 : calleeMethod.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("unchecked")
			CallKey other = (CallKey) obj;
			if (d1 == null) {
				if (other.d1 != null)
					return false;
			} else if (!d1.equals(other.d1))
				return false;
			if (d2 == null) {
				if (other.d2 != null)
					return false;
			} else if (!d2.equals(other.d2))
				return false;
			if (callSite == null) {
				if (other.callSite != null)
					return false;
			} else if (!callSite.equals(other.callSite))
				return false;
			if (calleeMethod == null) {
				if (other.calleeMethod != null)
					return false;
			} else if (!calleeMethod.equals(other.calleeMethod))
				return false;
			return true;
		}
	}


	private class ReturnKey extends CallKey {
		
		private final N exitStmt, returnSite;

		public ReturnKey(N callSite, M calleeMethod, N exitStmt, D exitNode, N returnSite, D retNode) {
			super(callSite, exitNode, calleeMethod, retNode);
			this.exitStmt = exitStmt;
			this.returnSite = returnSite;
		}
		
		public N getExitStmt() {
			return exitStmt;
		}
		
		public N getReturnSite() {
			return returnSite;
		}

		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((exitStmt == null) ? 0 : exitStmt.hashCode());
			result = prime * result + ((returnSite == null) ? 0 : returnSite.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("unchecked")
			ReturnKey other = (ReturnKey) obj;
			if (exitStmt == null) {
				if (other.exitStmt != null)
					return false;
			} else if (!exitStmt.equals(other.exitStmt))
				return false;
			if (returnSite == null) {
				if (other.returnSite != null)
					return false;
			} else if (!returnSite.equals(other.returnSite))
				return false;
			return true;
		}
	}


	public void printStats() {








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




272


273


274


275


276


277



        logger.debug("Stats for edge-function cache:\n" +
                     "Normal:         {}\n"+
                     "Call:           {}\n"+
                     "Return:         {}\n"+
                     "Call-to-return: {}\n",
                normalCache.stats(), callCache.stats(),returnCache.stats(),callToReturnCache.stats());








initial checkin



Eric Bodden
committed
Nov 14, 2012




278


279


280



	}

}









EdgeFunctionCache.java



7.86 KiB










EdgeFunctionCache.java



7.86 KiB









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



package heros;








initial checkin



Eric Bodden
committed
Nov 14, 2012




12


13


14


15




import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




16


17



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;








initial checkin



Eric Bodden
committed
Nov 14, 2012




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




public class EdgeFunctionCache<N, D, M, V> implements EdgeFunctions<N, D, M, V> {
	
	protected final EdgeFunctions<N, D, M, V> delegate;
	
	protected final LoadingCache<NDNDKey, EdgeFunction<V>> normalCache;
	
	protected final LoadingCache<CallKey, EdgeFunction<V>> callCache;

	protected final LoadingCache<ReturnKey, EdgeFunction<V>> returnCache;

	protected final LoadingCache<NDNDKey, EdgeFunction<V>> callToReturnCache;








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




30


31


32




    Logger logger = LoggerFactory.getLogger(getClass());









suppress warning

 


Eric Bodden
committed
Oct 28, 2013




33



	@SuppressWarnings("unchecked")








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


183


184


185


186


187


188


189


190


191


192


193


194


195


196


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


211


212


213


214


215


216


217


218


219


220


221


222


223


224


225


226


227


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


238


239


240


241


242


243


244


245


246


247


248


249


250


251


252


253


254


255


256


257


258


259


260


261


262


263


264


265


266


267


268


269


270


271



	public EdgeFunctionCache(final EdgeFunctions<N, D, M, V> delegate, @SuppressWarnings("rawtypes") CacheBuilder builder) {
		this.delegate = delegate;
		
		normalCache = builder.build(new CacheLoader<NDNDKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(NDNDKey key) throws Exception {
				return delegate.getNormalEdgeFunction(key.getN1(), key.getD1(), key.getN2(), key.getD2());
			}
		});
		
		callCache = builder.build(new CacheLoader<CallKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(CallKey key) throws Exception {
				return delegate.getCallEdgeFunction(key.getCallSite(), key.getD1(), key.getCalleeMethod(), key.getD2());
			}
		});
		
		returnCache = builder.build(new CacheLoader<ReturnKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(ReturnKey key) throws Exception {
				return delegate.getReturnEdgeFunction(key.getCallSite(), key.getCalleeMethod(), key.getExitStmt(), key.getD1(), key.getReturnSite(), key.getD2());
			}
		});
		
		callToReturnCache = builder.build(new CacheLoader<NDNDKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(NDNDKey key) throws Exception {
				return delegate.getCallToReturnEdgeFunction(key.getN1(), key.getD1(), key.getN2(), key.getD2());
			}
		});
	}

	public EdgeFunction<V> getNormalEdgeFunction(N curr, D currNode, N succ, D succNode) {
		return normalCache.getUnchecked(new NDNDKey(curr, currNode, succ, succNode));
	}

	public EdgeFunction<V> getCallEdgeFunction(N callStmt, D srcNode, M destinationMethod, D destNode) {
		return callCache.getUnchecked(new CallKey(callStmt, srcNode, destinationMethod, destNode));
	}

	public EdgeFunction<V> getReturnEdgeFunction(N callSite, M calleeMethod, N exitStmt, D exitNode, N returnSite, D retNode) {
		return returnCache.getUnchecked(new ReturnKey(callSite, calleeMethod, exitStmt, exitNode, returnSite, retNode));
	}

	public EdgeFunction<V> getCallToReturnEdgeFunction(N callSite, D callNode, N returnSite, D returnSideNode) {
		return callToReturnCache.getUnchecked(new NDNDKey(callSite, callNode, returnSite, returnSideNode));
	}


	private class NDNDKey {
		private final N n1, n2;
		private final D d1, d2;

		public NDNDKey(N n1, D d1, N n2, D d2) {
			this.n1 = n1;
			this.n2 = n2;
			this.d1 = d1;
			this.d2 = d2;
		}

		public N getN1() {
			return n1;
		}

		public D getD1() {
			return d1;
		}

		public N getN2() {
			return n2;
		}

		public D getD2() {
			return d2;
		}

		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((d1 == null) ? 0 : d1.hashCode());
			result = prime * result + ((d2 == null) ? 0 : d2.hashCode());
			result = prime * result + ((n1 == null) ? 0 : n1.hashCode());
			result = prime * result + ((n2 == null) ? 0 : n2.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("unchecked")
			NDNDKey other = (NDNDKey) obj;
			if (d1 == null) {
				if (other.d1 != null)
					return false;
			} else if (!d1.equals(other.d1))
				return false;
			if (d2 == null) {
				if (other.d2 != null)
					return false;
			} else if (!d2.equals(other.d2))
				return false;
			if (n1 == null) {
				if (other.n1 != null)
					return false;
			} else if (!n1.equals(other.n1))
				return false;
			if (n2 == null) {
				if (other.n2 != null)
					return false;
			} else if (!n2.equals(other.n2))
				return false;
			return true;
		}
	}
	
	private class CallKey {
		private final N callSite;
		private final M calleeMethod;
		private final D d1, d2;

		public CallKey(N callSite, D d1, M calleeMethod, D d2) {
			this.callSite = callSite;
			this.calleeMethod = calleeMethod;
			this.d1 = d1;
			this.d2 = d2;
		}

		public N getCallSite() {
			return callSite;
		}

		public D getD1() {
			return d1;
		}

		public M getCalleeMethod() {
			return calleeMethod;
		}

		public D getD2() {
			return d2;
		}

		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((d1 == null) ? 0 : d1.hashCode());
			result = prime * result + ((d2 == null) ? 0 : d2.hashCode());
			result = prime * result + ((callSite == null) ? 0 : callSite.hashCode());
			result = prime * result + ((calleeMethod == null) ? 0 : calleeMethod.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("unchecked")
			CallKey other = (CallKey) obj;
			if (d1 == null) {
				if (other.d1 != null)
					return false;
			} else if (!d1.equals(other.d1))
				return false;
			if (d2 == null) {
				if (other.d2 != null)
					return false;
			} else if (!d2.equals(other.d2))
				return false;
			if (callSite == null) {
				if (other.callSite != null)
					return false;
			} else if (!callSite.equals(other.callSite))
				return false;
			if (calleeMethod == null) {
				if (other.calleeMethod != null)
					return false;
			} else if (!calleeMethod.equals(other.calleeMethod))
				return false;
			return true;
		}
	}


	private class ReturnKey extends CallKey {
		
		private final N exitStmt, returnSite;

		public ReturnKey(N callSite, M calleeMethod, N exitStmt, D exitNode, N returnSite, D retNode) {
			super(callSite, exitNode, calleeMethod, retNode);
			this.exitStmt = exitStmt;
			this.returnSite = returnSite;
		}
		
		public N getExitStmt() {
			return exitStmt;
		}
		
		public N getReturnSite() {
			return returnSite;
		}

		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((exitStmt == null) ? 0 : exitStmt.hashCode());
			result = prime * result + ((returnSite == null) ? 0 : returnSite.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("unchecked")
			ReturnKey other = (ReturnKey) obj;
			if (exitStmt == null) {
				if (other.exitStmt != null)
					return false;
			} else if (!exitStmt.equals(other.exitStmt))
				return false;
			if (returnSite == null) {
				if (other.returnSite != null)
					return false;
			} else if (!returnSite.equals(other.returnSite))
				return false;
			return true;
		}
	}


	public void printStats() {








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




272


273


274


275


276


277



        logger.debug("Stats for edge-function cache:\n" +
                     "Normal:         {}\n"+
                     "Call:           {}\n"+
                     "Return:         {}\n"+
                     "Call-to-return: {}\n",
                normalCache.stats(), callCache.stats(),returnCache.stats(),callToReturnCache.stats());








initial checkin



Eric Bodden
committed
Nov 14, 2012




278


279


280



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



package heros;








initial checkin



Eric Bodden
committed
Nov 14, 2012




12


13


14


15




import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




16


17



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;








initial checkin



Eric Bodden
committed
Nov 14, 2012




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




public class EdgeFunctionCache<N, D, M, V> implements EdgeFunctions<N, D, M, V> {
	
	protected final EdgeFunctions<N, D, M, V> delegate;
	
	protected final LoadingCache<NDNDKey, EdgeFunction<V>> normalCache;
	
	protected final LoadingCache<CallKey, EdgeFunction<V>> callCache;

	protected final LoadingCache<ReturnKey, EdgeFunction<V>> returnCache;

	protected final LoadingCache<NDNDKey, EdgeFunction<V>> callToReturnCache;








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




30


31


32




    Logger logger = LoggerFactory.getLogger(getClass());









suppress warning

 


Eric Bodden
committed
Oct 28, 2013




33



	@SuppressWarnings("unchecked")








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


183


184


185


186


187


188


189


190


191


192


193


194


195


196


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


211


212


213


214


215


216


217


218


219


220


221


222


223


224


225


226


227


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


238


239


240


241


242


243


244


245


246


247


248


249


250


251


252


253


254


255


256


257


258


259


260


261


262


263


264


265


266


267


268


269


270


271



	public EdgeFunctionCache(final EdgeFunctions<N, D, M, V> delegate, @SuppressWarnings("rawtypes") CacheBuilder builder) {
		this.delegate = delegate;
		
		normalCache = builder.build(new CacheLoader<NDNDKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(NDNDKey key) throws Exception {
				return delegate.getNormalEdgeFunction(key.getN1(), key.getD1(), key.getN2(), key.getD2());
			}
		});
		
		callCache = builder.build(new CacheLoader<CallKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(CallKey key) throws Exception {
				return delegate.getCallEdgeFunction(key.getCallSite(), key.getD1(), key.getCalleeMethod(), key.getD2());
			}
		});
		
		returnCache = builder.build(new CacheLoader<ReturnKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(ReturnKey key) throws Exception {
				return delegate.getReturnEdgeFunction(key.getCallSite(), key.getCalleeMethod(), key.getExitStmt(), key.getD1(), key.getReturnSite(), key.getD2());
			}
		});
		
		callToReturnCache = builder.build(new CacheLoader<NDNDKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(NDNDKey key) throws Exception {
				return delegate.getCallToReturnEdgeFunction(key.getN1(), key.getD1(), key.getN2(), key.getD2());
			}
		});
	}

	public EdgeFunction<V> getNormalEdgeFunction(N curr, D currNode, N succ, D succNode) {
		return normalCache.getUnchecked(new NDNDKey(curr, currNode, succ, succNode));
	}

	public EdgeFunction<V> getCallEdgeFunction(N callStmt, D srcNode, M destinationMethod, D destNode) {
		return callCache.getUnchecked(new CallKey(callStmt, srcNode, destinationMethod, destNode));
	}

	public EdgeFunction<V> getReturnEdgeFunction(N callSite, M calleeMethod, N exitStmt, D exitNode, N returnSite, D retNode) {
		return returnCache.getUnchecked(new ReturnKey(callSite, calleeMethod, exitStmt, exitNode, returnSite, retNode));
	}

	public EdgeFunction<V> getCallToReturnEdgeFunction(N callSite, D callNode, N returnSite, D returnSideNode) {
		return callToReturnCache.getUnchecked(new NDNDKey(callSite, callNode, returnSite, returnSideNode));
	}


	private class NDNDKey {
		private final N n1, n2;
		private final D d1, d2;

		public NDNDKey(N n1, D d1, N n2, D d2) {
			this.n1 = n1;
			this.n2 = n2;
			this.d1 = d1;
			this.d2 = d2;
		}

		public N getN1() {
			return n1;
		}

		public D getD1() {
			return d1;
		}

		public N getN2() {
			return n2;
		}

		public D getD2() {
			return d2;
		}

		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((d1 == null) ? 0 : d1.hashCode());
			result = prime * result + ((d2 == null) ? 0 : d2.hashCode());
			result = prime * result + ((n1 == null) ? 0 : n1.hashCode());
			result = prime * result + ((n2 == null) ? 0 : n2.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("unchecked")
			NDNDKey other = (NDNDKey) obj;
			if (d1 == null) {
				if (other.d1 != null)
					return false;
			} else if (!d1.equals(other.d1))
				return false;
			if (d2 == null) {
				if (other.d2 != null)
					return false;
			} else if (!d2.equals(other.d2))
				return false;
			if (n1 == null) {
				if (other.n1 != null)
					return false;
			} else if (!n1.equals(other.n1))
				return false;
			if (n2 == null) {
				if (other.n2 != null)
					return false;
			} else if (!n2.equals(other.n2))
				return false;
			return true;
		}
	}
	
	private class CallKey {
		private final N callSite;
		private final M calleeMethod;
		private final D d1, d2;

		public CallKey(N callSite, D d1, M calleeMethod, D d2) {
			this.callSite = callSite;
			this.calleeMethod = calleeMethod;
			this.d1 = d1;
			this.d2 = d2;
		}

		public N getCallSite() {
			return callSite;
		}

		public D getD1() {
			return d1;
		}

		public M getCalleeMethod() {
			return calleeMethod;
		}

		public D getD2() {
			return d2;
		}

		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((d1 == null) ? 0 : d1.hashCode());
			result = prime * result + ((d2 == null) ? 0 : d2.hashCode());
			result = prime * result + ((callSite == null) ? 0 : callSite.hashCode());
			result = prime * result + ((calleeMethod == null) ? 0 : calleeMethod.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("unchecked")
			CallKey other = (CallKey) obj;
			if (d1 == null) {
				if (other.d1 != null)
					return false;
			} else if (!d1.equals(other.d1))
				return false;
			if (d2 == null) {
				if (other.d2 != null)
					return false;
			} else if (!d2.equals(other.d2))
				return false;
			if (callSite == null) {
				if (other.callSite != null)
					return false;
			} else if (!callSite.equals(other.callSite))
				return false;
			if (calleeMethod == null) {
				if (other.calleeMethod != null)
					return false;
			} else if (!calleeMethod.equals(other.calleeMethod))
				return false;
			return true;
		}
	}


	private class ReturnKey extends CallKey {
		
		private final N exitStmt, returnSite;

		public ReturnKey(N callSite, M calleeMethod, N exitStmt, D exitNode, N returnSite, D retNode) {
			super(callSite, exitNode, calleeMethod, retNode);
			this.exitStmt = exitStmt;
			this.returnSite = returnSite;
		}
		
		public N getExitStmt() {
			return exitStmt;
		}
		
		public N getReturnSite() {
			return returnSite;
		}

		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((exitStmt == null) ? 0 : exitStmt.hashCode());
			result = prime * result + ((returnSite == null) ? 0 : returnSite.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("unchecked")
			ReturnKey other = (ReturnKey) obj;
			if (exitStmt == null) {
				if (other.exitStmt != null)
					return false;
			} else if (!exitStmt.equals(other.exitStmt))
				return false;
			if (returnSite == null) {
				if (other.returnSite != null)
					return false;
			} else if (!returnSite.equals(other.returnSite))
				return false;
			return true;
		}
	}


	public void printStats() {








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




272


273


274


275


276


277



        logger.debug("Stats for edge-function cache:\n" +
                     "Normal:         {}\n"+
                     "Call:           {}\n"+
                     "Return:         {}\n"+
                     "Call-to-return: {}\n",
                normalCache.stats(), callCache.stats(),returnCache.stats(),callToReturnCache.stats());








initial checkin



Eric Bodden
committed
Nov 14, 2012




278


279


280



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



package heros;








initial checkin



Eric Bodden
committed
Nov 14, 2012




12


13


14


15




import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




16


17



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;








initial checkin



Eric Bodden
committed
Nov 14, 2012




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




public class EdgeFunctionCache<N, D, M, V> implements EdgeFunctions<N, D, M, V> {
	
	protected final EdgeFunctions<N, D, M, V> delegate;
	
	protected final LoadingCache<NDNDKey, EdgeFunction<V>> normalCache;
	
	protected final LoadingCache<CallKey, EdgeFunction<V>> callCache;

	protected final LoadingCache<ReturnKey, EdgeFunction<V>> returnCache;

	protected final LoadingCache<NDNDKey, EdgeFunction<V>> callToReturnCache;








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




30


31


32




    Logger logger = LoggerFactory.getLogger(getClass());









suppress warning

 


Eric Bodden
committed
Oct 28, 2013




33



	@SuppressWarnings("unchecked")








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


183


184


185


186


187


188


189


190


191


192


193


194


195


196


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


211


212


213


214


215


216


217


218


219


220


221


222


223


224


225


226


227


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


238


239


240


241


242


243


244


245


246


247


248


249


250


251


252


253


254


255


256


257


258


259


260


261


262


263


264


265


266


267


268


269


270


271



	public EdgeFunctionCache(final EdgeFunctions<N, D, M, V> delegate, @SuppressWarnings("rawtypes") CacheBuilder builder) {
		this.delegate = delegate;
		
		normalCache = builder.build(new CacheLoader<NDNDKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(NDNDKey key) throws Exception {
				return delegate.getNormalEdgeFunction(key.getN1(), key.getD1(), key.getN2(), key.getD2());
			}
		});
		
		callCache = builder.build(new CacheLoader<CallKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(CallKey key) throws Exception {
				return delegate.getCallEdgeFunction(key.getCallSite(), key.getD1(), key.getCalleeMethod(), key.getD2());
			}
		});
		
		returnCache = builder.build(new CacheLoader<ReturnKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(ReturnKey key) throws Exception {
				return delegate.getReturnEdgeFunction(key.getCallSite(), key.getCalleeMethod(), key.getExitStmt(), key.getD1(), key.getReturnSite(), key.getD2());
			}
		});
		
		callToReturnCache = builder.build(new CacheLoader<NDNDKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(NDNDKey key) throws Exception {
				return delegate.getCallToReturnEdgeFunction(key.getN1(), key.getD1(), key.getN2(), key.getD2());
			}
		});
	}

	public EdgeFunction<V> getNormalEdgeFunction(N curr, D currNode, N succ, D succNode) {
		return normalCache.getUnchecked(new NDNDKey(curr, currNode, succ, succNode));
	}

	public EdgeFunction<V> getCallEdgeFunction(N callStmt, D srcNode, M destinationMethod, D destNode) {
		return callCache.getUnchecked(new CallKey(callStmt, srcNode, destinationMethod, destNode));
	}

	public EdgeFunction<V> getReturnEdgeFunction(N callSite, M calleeMethod, N exitStmt, D exitNode, N returnSite, D retNode) {
		return returnCache.getUnchecked(new ReturnKey(callSite, calleeMethod, exitStmt, exitNode, returnSite, retNode));
	}

	public EdgeFunction<V> getCallToReturnEdgeFunction(N callSite, D callNode, N returnSite, D returnSideNode) {
		return callToReturnCache.getUnchecked(new NDNDKey(callSite, callNode, returnSite, returnSideNode));
	}


	private class NDNDKey {
		private final N n1, n2;
		private final D d1, d2;

		public NDNDKey(N n1, D d1, N n2, D d2) {
			this.n1 = n1;
			this.n2 = n2;
			this.d1 = d1;
			this.d2 = d2;
		}

		public N getN1() {
			return n1;
		}

		public D getD1() {
			return d1;
		}

		public N getN2() {
			return n2;
		}

		public D getD2() {
			return d2;
		}

		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((d1 == null) ? 0 : d1.hashCode());
			result = prime * result + ((d2 == null) ? 0 : d2.hashCode());
			result = prime * result + ((n1 == null) ? 0 : n1.hashCode());
			result = prime * result + ((n2 == null) ? 0 : n2.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("unchecked")
			NDNDKey other = (NDNDKey) obj;
			if (d1 == null) {
				if (other.d1 != null)
					return false;
			} else if (!d1.equals(other.d1))
				return false;
			if (d2 == null) {
				if (other.d2 != null)
					return false;
			} else if (!d2.equals(other.d2))
				return false;
			if (n1 == null) {
				if (other.n1 != null)
					return false;
			} else if (!n1.equals(other.n1))
				return false;
			if (n2 == null) {
				if (other.n2 != null)
					return false;
			} else if (!n2.equals(other.n2))
				return false;
			return true;
		}
	}
	
	private class CallKey {
		private final N callSite;
		private final M calleeMethod;
		private final D d1, d2;

		public CallKey(N callSite, D d1, M calleeMethod, D d2) {
			this.callSite = callSite;
			this.calleeMethod = calleeMethod;
			this.d1 = d1;
			this.d2 = d2;
		}

		public N getCallSite() {
			return callSite;
		}

		public D getD1() {
			return d1;
		}

		public M getCalleeMethod() {
			return calleeMethod;
		}

		public D getD2() {
			return d2;
		}

		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((d1 == null) ? 0 : d1.hashCode());
			result = prime * result + ((d2 == null) ? 0 : d2.hashCode());
			result = prime * result + ((callSite == null) ? 0 : callSite.hashCode());
			result = prime * result + ((calleeMethod == null) ? 0 : calleeMethod.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("unchecked")
			CallKey other = (CallKey) obj;
			if (d1 == null) {
				if (other.d1 != null)
					return false;
			} else if (!d1.equals(other.d1))
				return false;
			if (d2 == null) {
				if (other.d2 != null)
					return false;
			} else if (!d2.equals(other.d2))
				return false;
			if (callSite == null) {
				if (other.callSite != null)
					return false;
			} else if (!callSite.equals(other.callSite))
				return false;
			if (calleeMethod == null) {
				if (other.calleeMethod != null)
					return false;
			} else if (!calleeMethod.equals(other.calleeMethod))
				return false;
			return true;
		}
	}


	private class ReturnKey extends CallKey {
		
		private final N exitStmt, returnSite;

		public ReturnKey(N callSite, M calleeMethod, N exitStmt, D exitNode, N returnSite, D retNode) {
			super(callSite, exitNode, calleeMethod, retNode);
			this.exitStmt = exitStmt;
			this.returnSite = returnSite;
		}
		
		public N getExitStmt() {
			return exitStmt;
		}
		
		public N getReturnSite() {
			return returnSite;
		}

		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((exitStmt == null) ? 0 : exitStmt.hashCode());
			result = prime * result + ((returnSite == null) ? 0 : returnSite.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("unchecked")
			ReturnKey other = (ReturnKey) obj;
			if (exitStmt == null) {
				if (other.exitStmt != null)
					return false;
			} else if (!exitStmt.equals(other.exitStmt))
				return false;
			if (returnSite == null) {
				if (other.returnSite != null)
					return false;
			} else if (!returnSite.equals(other.returnSite))
				return false;
			return true;
		}
	}


	public void printStats() {








Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




272


273


274


275


276


277



        logger.debug("Stats for edge-function cache:\n" +
                     "Normal:         {}\n"+
                     "Call:           {}\n"+
                     "Return:         {}\n"+
                     "Call-to-return: {}\n",
                normalCache.stats(), callCache.stats(),returnCache.stats(),callToReturnCache.stats());








initial checkin



Eric Bodden
committed
Nov 14, 2012




278


279


280



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



package heros;






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


package heros;

package heros;packageheros;




initial checkin



Eric Bodden
committed
Nov 14, 2012




12


13


14


15




import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;






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


13


14


15



import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import com.google.common.cache.CacheBuilder;importcom.google.common.cache.CacheBuilder;import com.google.common.cache.CacheLoader;importcom.google.common.cache.CacheLoader;import com.google.common.cache.LoadingCache;importcom.google.common.cache.LoadingCache;




Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




16


17



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;






Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013



Ported to SLF4J Logging

 

Ported to SLF4J Logging

Marc-André Laverdière
committed
Oct 10, 2013


16


17


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;importorg.slf4j.Logger;import org.slf4j.LoggerFactory;importorg.slf4j.LoggerFactory;




initial checkin



Eric Bodden
committed
Nov 14, 2012




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




public class EdgeFunctionCache<N, D, M, V> implements EdgeFunctions<N, D, M, V> {
	
	protected final EdgeFunctions<N, D, M, V> delegate;
	
	protected final LoadingCache<NDNDKey, EdgeFunction<V>> normalCache;
	
	protected final LoadingCache<CallKey, EdgeFunction<V>> callCache;

	protected final LoadingCache<ReturnKey, EdgeFunction<V>> returnCache;

	protected final LoadingCache<NDNDKey, EdgeFunction<V>> callToReturnCache;






initial checkin



Eric Bodden
committed
Nov 14, 2012



initial checkin


initial checkin

Eric Bodden
committed
Nov 14, 2012


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



public class EdgeFunctionCache<N, D, M, V> implements EdgeFunctions<N, D, M, V> {
	
	protected final EdgeFunctions<N, D, M, V> delegate;
	
	protected final LoadingCache<NDNDKey, EdgeFunction<V>> normalCache;
	
	protected final LoadingCache<CallKey, EdgeFunction<V>> callCache;

	protected final LoadingCache<ReturnKey, EdgeFunction<V>> returnCache;

	protected final LoadingCache<NDNDKey, EdgeFunction<V>> callToReturnCache;

public class EdgeFunctionCache<N, D, M, V> implements EdgeFunctions<N, D, M, V> {publicclassEdgeFunctionCache<N,D,M,V>implementsEdgeFunctions<N,D,M,V>{		protected final EdgeFunctions<N, D, M, V> delegate;protectedfinalEdgeFunctions<N,D,M,V>delegate;		protected final LoadingCache<NDNDKey, EdgeFunction<V>> normalCache;protectedfinalLoadingCache<NDNDKey,EdgeFunction<V>>normalCache;		protected final LoadingCache<CallKey, EdgeFunction<V>> callCache;protectedfinalLoadingCache<CallKey,EdgeFunction<V>>callCache;	protected final LoadingCache<ReturnKey, EdgeFunction<V>> returnCache;protectedfinalLoadingCache<ReturnKey,EdgeFunction<V>>returnCache;	protected final LoadingCache<NDNDKey, EdgeFunction<V>> callToReturnCache;protectedfinalLoadingCache<NDNDKey,EdgeFunction<V>>callToReturnCache;




Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




30


31


32




    Logger logger = LoggerFactory.getLogger(getClass());







Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013



Ported to SLF4J Logging

 

Ported to SLF4J Logging

Marc-André Laverdière
committed
Oct 10, 2013


30


31


32



    Logger logger = LoggerFactory.getLogger(getClass());


    Logger logger = LoggerFactory.getLogger(getClass());Loggerlogger=LoggerFactory.getLogger(getClass());




suppress warning

 


Eric Bodden
committed
Oct 28, 2013




33



	@SuppressWarnings("unchecked")






suppress warning

 


Eric Bodden
committed
Oct 28, 2013



suppress warning

 

suppress warning

Eric Bodden
committed
Oct 28, 2013


33


	@SuppressWarnings("unchecked")

	@SuppressWarnings("unchecked")@SuppressWarnings("unchecked")




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


183


184


185


186


187


188


189


190


191


192


193


194


195


196


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


211


212


213


214


215


216


217


218


219


220


221


222


223


224


225


226


227


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


238


239


240


241


242


243


244


245


246


247


248


249


250


251


252


253


254


255


256


257


258


259


260


261


262


263


264


265


266


267


268


269


270


271



	public EdgeFunctionCache(final EdgeFunctions<N, D, M, V> delegate, @SuppressWarnings("rawtypes") CacheBuilder builder) {
		this.delegate = delegate;
		
		normalCache = builder.build(new CacheLoader<NDNDKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(NDNDKey key) throws Exception {
				return delegate.getNormalEdgeFunction(key.getN1(), key.getD1(), key.getN2(), key.getD2());
			}
		});
		
		callCache = builder.build(new CacheLoader<CallKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(CallKey key) throws Exception {
				return delegate.getCallEdgeFunction(key.getCallSite(), key.getD1(), key.getCalleeMethod(), key.getD2());
			}
		});
		
		returnCache = builder.build(new CacheLoader<ReturnKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(ReturnKey key) throws Exception {
				return delegate.getReturnEdgeFunction(key.getCallSite(), key.getCalleeMethod(), key.getExitStmt(), key.getD1(), key.getReturnSite(), key.getD2());
			}
		});
		
		callToReturnCache = builder.build(new CacheLoader<NDNDKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(NDNDKey key) throws Exception {
				return delegate.getCallToReturnEdgeFunction(key.getN1(), key.getD1(), key.getN2(), key.getD2());
			}
		});
	}

	public EdgeFunction<V> getNormalEdgeFunction(N curr, D currNode, N succ, D succNode) {
		return normalCache.getUnchecked(new NDNDKey(curr, currNode, succ, succNode));
	}

	public EdgeFunction<V> getCallEdgeFunction(N callStmt, D srcNode, M destinationMethod, D destNode) {
		return callCache.getUnchecked(new CallKey(callStmt, srcNode, destinationMethod, destNode));
	}

	public EdgeFunction<V> getReturnEdgeFunction(N callSite, M calleeMethod, N exitStmt, D exitNode, N returnSite, D retNode) {
		return returnCache.getUnchecked(new ReturnKey(callSite, calleeMethod, exitStmt, exitNode, returnSite, retNode));
	}

	public EdgeFunction<V> getCallToReturnEdgeFunction(N callSite, D callNode, N returnSite, D returnSideNode) {
		return callToReturnCache.getUnchecked(new NDNDKey(callSite, callNode, returnSite, returnSideNode));
	}


	private class NDNDKey {
		private final N n1, n2;
		private final D d1, d2;

		public NDNDKey(N n1, D d1, N n2, D d2) {
			this.n1 = n1;
			this.n2 = n2;
			this.d1 = d1;
			this.d2 = d2;
		}

		public N getN1() {
			return n1;
		}

		public D getD1() {
			return d1;
		}

		public N getN2() {
			return n2;
		}

		public D getD2() {
			return d2;
		}

		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((d1 == null) ? 0 : d1.hashCode());
			result = prime * result + ((d2 == null) ? 0 : d2.hashCode());
			result = prime * result + ((n1 == null) ? 0 : n1.hashCode());
			result = prime * result + ((n2 == null) ? 0 : n2.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("unchecked")
			NDNDKey other = (NDNDKey) obj;
			if (d1 == null) {
				if (other.d1 != null)
					return false;
			} else if (!d1.equals(other.d1))
				return false;
			if (d2 == null) {
				if (other.d2 != null)
					return false;
			} else if (!d2.equals(other.d2))
				return false;
			if (n1 == null) {
				if (other.n1 != null)
					return false;
			} else if (!n1.equals(other.n1))
				return false;
			if (n2 == null) {
				if (other.n2 != null)
					return false;
			} else if (!n2.equals(other.n2))
				return false;
			return true;
		}
	}
	
	private class CallKey {
		private final N callSite;
		private final M calleeMethod;
		private final D d1, d2;

		public CallKey(N callSite, D d1, M calleeMethod, D d2) {
			this.callSite = callSite;
			this.calleeMethod = calleeMethod;
			this.d1 = d1;
			this.d2 = d2;
		}

		public N getCallSite() {
			return callSite;
		}

		public D getD1() {
			return d1;
		}

		public M getCalleeMethod() {
			return calleeMethod;
		}

		public D getD2() {
			return d2;
		}

		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((d1 == null) ? 0 : d1.hashCode());
			result = prime * result + ((d2 == null) ? 0 : d2.hashCode());
			result = prime * result + ((callSite == null) ? 0 : callSite.hashCode());
			result = prime * result + ((calleeMethod == null) ? 0 : calleeMethod.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("unchecked")
			CallKey other = (CallKey) obj;
			if (d1 == null) {
				if (other.d1 != null)
					return false;
			} else if (!d1.equals(other.d1))
				return false;
			if (d2 == null) {
				if (other.d2 != null)
					return false;
			} else if (!d2.equals(other.d2))
				return false;
			if (callSite == null) {
				if (other.callSite != null)
					return false;
			} else if (!callSite.equals(other.callSite))
				return false;
			if (calleeMethod == null) {
				if (other.calleeMethod != null)
					return false;
			} else if (!calleeMethod.equals(other.calleeMethod))
				return false;
			return true;
		}
	}


	private class ReturnKey extends CallKey {
		
		private final N exitStmt, returnSite;

		public ReturnKey(N callSite, M calleeMethod, N exitStmt, D exitNode, N returnSite, D retNode) {
			super(callSite, exitNode, calleeMethod, retNode);
			this.exitStmt = exitStmt;
			this.returnSite = returnSite;
		}
		
		public N getExitStmt() {
			return exitStmt;
		}
		
		public N getReturnSite() {
			return returnSite;
		}

		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((exitStmt == null) ? 0 : exitStmt.hashCode());
			result = prime * result + ((returnSite == null) ? 0 : returnSite.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("unchecked")
			ReturnKey other = (ReturnKey) obj;
			if (exitStmt == null) {
				if (other.exitStmt != null)
					return false;
			} else if (!exitStmt.equals(other.exitStmt))
				return false;
			if (returnSite == null) {
				if (other.returnSite != null)
					return false;
			} else if (!returnSite.equals(other.returnSite))
				return false;
			return true;
		}
	}


	public void printStats() {






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


183


184


185


186


187


188


189


190


191


192


193


194


195


196


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


211


212


213


214


215


216


217


218


219


220


221


222


223


224


225


226


227


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


238


239


240


241


242


243


244


245


246


247


248


249


250


251


252


253


254


255


256


257


258


259


260


261


262


263


264


265


266


267


268


269


270


271


	public EdgeFunctionCache(final EdgeFunctions<N, D, M, V> delegate, @SuppressWarnings("rawtypes") CacheBuilder builder) {
		this.delegate = delegate;
		
		normalCache = builder.build(new CacheLoader<NDNDKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(NDNDKey key) throws Exception {
				return delegate.getNormalEdgeFunction(key.getN1(), key.getD1(), key.getN2(), key.getD2());
			}
		});
		
		callCache = builder.build(new CacheLoader<CallKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(CallKey key) throws Exception {
				return delegate.getCallEdgeFunction(key.getCallSite(), key.getD1(), key.getCalleeMethod(), key.getD2());
			}
		});
		
		returnCache = builder.build(new CacheLoader<ReturnKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(ReturnKey key) throws Exception {
				return delegate.getReturnEdgeFunction(key.getCallSite(), key.getCalleeMethod(), key.getExitStmt(), key.getD1(), key.getReturnSite(), key.getD2());
			}
		});
		
		callToReturnCache = builder.build(new CacheLoader<NDNDKey, EdgeFunction<V>>() {
			public EdgeFunction<V> load(NDNDKey key) throws Exception {
				return delegate.getCallToReturnEdgeFunction(key.getN1(), key.getD1(), key.getN2(), key.getD2());
			}
		});
	}

	public EdgeFunction<V> getNormalEdgeFunction(N curr, D currNode, N succ, D succNode) {
		return normalCache.getUnchecked(new NDNDKey(curr, currNode, succ, succNode));
	}

	public EdgeFunction<V> getCallEdgeFunction(N callStmt, D srcNode, M destinationMethod, D destNode) {
		return callCache.getUnchecked(new CallKey(callStmt, srcNode, destinationMethod, destNode));
	}

	public EdgeFunction<V> getReturnEdgeFunction(N callSite, M calleeMethod, N exitStmt, D exitNode, N returnSite, D retNode) {
		return returnCache.getUnchecked(new ReturnKey(callSite, calleeMethod, exitStmt, exitNode, returnSite, retNode));
	}

	public EdgeFunction<V> getCallToReturnEdgeFunction(N callSite, D callNode, N returnSite, D returnSideNode) {
		return callToReturnCache.getUnchecked(new NDNDKey(callSite, callNode, returnSite, returnSideNode));
	}


	private class NDNDKey {
		private final N n1, n2;
		private final D d1, d2;

		public NDNDKey(N n1, D d1, N n2, D d2) {
			this.n1 = n1;
			this.n2 = n2;
			this.d1 = d1;
			this.d2 = d2;
		}

		public N getN1() {
			return n1;
		}

		public D getD1() {
			return d1;
		}

		public N getN2() {
			return n2;
		}

		public D getD2() {
			return d2;
		}

		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((d1 == null) ? 0 : d1.hashCode());
			result = prime * result + ((d2 == null) ? 0 : d2.hashCode());
			result = prime * result + ((n1 == null) ? 0 : n1.hashCode());
			result = prime * result + ((n2 == null) ? 0 : n2.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("unchecked")
			NDNDKey other = (NDNDKey) obj;
			if (d1 == null) {
				if (other.d1 != null)
					return false;
			} else if (!d1.equals(other.d1))
				return false;
			if (d2 == null) {
				if (other.d2 != null)
					return false;
			} else if (!d2.equals(other.d2))
				return false;
			if (n1 == null) {
				if (other.n1 != null)
					return false;
			} else if (!n1.equals(other.n1))
				return false;
			if (n2 == null) {
				if (other.n2 != null)
					return false;
			} else if (!n2.equals(other.n2))
				return false;
			return true;
		}
	}
	
	private class CallKey {
		private final N callSite;
		private final M calleeMethod;
		private final D d1, d2;

		public CallKey(N callSite, D d1, M calleeMethod, D d2) {
			this.callSite = callSite;
			this.calleeMethod = calleeMethod;
			this.d1 = d1;
			this.d2 = d2;
		}

		public N getCallSite() {
			return callSite;
		}

		public D getD1() {
			return d1;
		}

		public M getCalleeMethod() {
			return calleeMethod;
		}

		public D getD2() {
			return d2;
		}

		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((d1 == null) ? 0 : d1.hashCode());
			result = prime * result + ((d2 == null) ? 0 : d2.hashCode());
			result = prime * result + ((callSite == null) ? 0 : callSite.hashCode());
			result = prime * result + ((calleeMethod == null) ? 0 : calleeMethod.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("unchecked")
			CallKey other = (CallKey) obj;
			if (d1 == null) {
				if (other.d1 != null)
					return false;
			} else if (!d1.equals(other.d1))
				return false;
			if (d2 == null) {
				if (other.d2 != null)
					return false;
			} else if (!d2.equals(other.d2))
				return false;
			if (callSite == null) {
				if (other.callSite != null)
					return false;
			} else if (!callSite.equals(other.callSite))
				return false;
			if (calleeMethod == null) {
				if (other.calleeMethod != null)
					return false;
			} else if (!calleeMethod.equals(other.calleeMethod))
				return false;
			return true;
		}
	}


	private class ReturnKey extends CallKey {
		
		private final N exitStmt, returnSite;

		public ReturnKey(N callSite, M calleeMethod, N exitStmt, D exitNode, N returnSite, D retNode) {
			super(callSite, exitNode, calleeMethod, retNode);
			this.exitStmt = exitStmt;
			this.returnSite = returnSite;
		}
		
		public N getExitStmt() {
			return exitStmt;
		}
		
		public N getReturnSite() {
			return returnSite;
		}

		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((exitStmt == null) ? 0 : exitStmt.hashCode());
			result = prime * result + ((returnSite == null) ? 0 : returnSite.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("unchecked")
			ReturnKey other = (ReturnKey) obj;
			if (exitStmt == null) {
				if (other.exitStmt != null)
					return false;
			} else if (!exitStmt.equals(other.exitStmt))
				return false;
			if (returnSite == null) {
				if (other.returnSite != null)
					return false;
			} else if (!returnSite.equals(other.returnSite))
				return false;
			return true;
		}
	}


	public void printStats() {

	public EdgeFunctionCache(final EdgeFunctions<N, D, M, V> delegate, @SuppressWarnings("rawtypes") CacheBuilder builder) {publicEdgeFunctionCache(finalEdgeFunctions<N,D,M,V>delegate,@SuppressWarnings("rawtypes")CacheBuilderbuilder){		this.delegate = delegate;this.delegate=delegate;				normalCache = builder.build(new CacheLoader<NDNDKey, EdgeFunction<V>>() {normalCache=builder.build(newCacheLoader<NDNDKey,EdgeFunction<V>>(){			public EdgeFunction<V> load(NDNDKey key) throws Exception {publicEdgeFunction<V>load(NDNDKeykey)throwsException{				return delegate.getNormalEdgeFunction(key.getN1(), key.getD1(), key.getN2(), key.getD2());returndelegate.getNormalEdgeFunction(key.getN1(),key.getD1(),key.getN2(),key.getD2());			}}		});});				callCache = builder.build(new CacheLoader<CallKey, EdgeFunction<V>>() {callCache=builder.build(newCacheLoader<CallKey,EdgeFunction<V>>(){			public EdgeFunction<V> load(CallKey key) throws Exception {publicEdgeFunction<V>load(CallKeykey)throwsException{				return delegate.getCallEdgeFunction(key.getCallSite(), key.getD1(), key.getCalleeMethod(), key.getD2());returndelegate.getCallEdgeFunction(key.getCallSite(),key.getD1(),key.getCalleeMethod(),key.getD2());			}}		});});				returnCache = builder.build(new CacheLoader<ReturnKey, EdgeFunction<V>>() {returnCache=builder.build(newCacheLoader<ReturnKey,EdgeFunction<V>>(){			public EdgeFunction<V> load(ReturnKey key) throws Exception {publicEdgeFunction<V>load(ReturnKeykey)throwsException{				return delegate.getReturnEdgeFunction(key.getCallSite(), key.getCalleeMethod(), key.getExitStmt(), key.getD1(), key.getReturnSite(), key.getD2());returndelegate.getReturnEdgeFunction(key.getCallSite(),key.getCalleeMethod(),key.getExitStmt(),key.getD1(),key.getReturnSite(),key.getD2());			}}		});});				callToReturnCache = builder.build(new CacheLoader<NDNDKey, EdgeFunction<V>>() {callToReturnCache=builder.build(newCacheLoader<NDNDKey,EdgeFunction<V>>(){			public EdgeFunction<V> load(NDNDKey key) throws Exception {publicEdgeFunction<V>load(NDNDKeykey)throwsException{				return delegate.getCallToReturnEdgeFunction(key.getN1(), key.getD1(), key.getN2(), key.getD2());returndelegate.getCallToReturnEdgeFunction(key.getN1(),key.getD1(),key.getN2(),key.getD2());			}}		});});	}}	public EdgeFunction<V> getNormalEdgeFunction(N curr, D currNode, N succ, D succNode) {publicEdgeFunction<V>getNormalEdgeFunction(Ncurr,DcurrNode,Nsucc,DsuccNode){		return normalCache.getUnchecked(new NDNDKey(curr, currNode, succ, succNode));returnnormalCache.getUnchecked(newNDNDKey(curr,currNode,succ,succNode));	}}	public EdgeFunction<V> getCallEdgeFunction(N callStmt, D srcNode, M destinationMethod, D destNode) {publicEdgeFunction<V>getCallEdgeFunction(NcallStmt,DsrcNode,MdestinationMethod,DdestNode){		return callCache.getUnchecked(new CallKey(callStmt, srcNode, destinationMethod, destNode));returncallCache.getUnchecked(newCallKey(callStmt,srcNode,destinationMethod,destNode));	}}	public EdgeFunction<V> getReturnEdgeFunction(N callSite, M calleeMethod, N exitStmt, D exitNode, N returnSite, D retNode) {publicEdgeFunction<V>getReturnEdgeFunction(NcallSite,McalleeMethod,NexitStmt,DexitNode,NreturnSite,DretNode){		return returnCache.getUnchecked(new ReturnKey(callSite, calleeMethod, exitStmt, exitNode, returnSite, retNode));returnreturnCache.getUnchecked(newReturnKey(callSite,calleeMethod,exitStmt,exitNode,returnSite,retNode));	}}	public EdgeFunction<V> getCallToReturnEdgeFunction(N callSite, D callNode, N returnSite, D returnSideNode) {publicEdgeFunction<V>getCallToReturnEdgeFunction(NcallSite,DcallNode,NreturnSite,DreturnSideNode){		return callToReturnCache.getUnchecked(new NDNDKey(callSite, callNode, returnSite, returnSideNode));returncallToReturnCache.getUnchecked(newNDNDKey(callSite,callNode,returnSite,returnSideNode));	}}	private class NDNDKey {privateclassNDNDKey{		private final N n1, n2;privatefinalNn1,n2;		private final D d1, d2;privatefinalDd1,d2;		public NDNDKey(N n1, D d1, N n2, D d2) {publicNDNDKey(Nn1,Dd1,Nn2,Dd2){			this.n1 = n1;this.n1=n1;			this.n2 = n2;this.n2=n2;			this.d1 = d1;this.d1=d1;			this.d2 = d2;this.d2=d2;		}}		public N getN1() {publicNgetN1(){			return n1;returnn1;		}}		public D getD1() {publicDgetD1(){			return d1;returnd1;		}}		public N getN2() {publicNgetN2(){			return n2;returnn2;		}}		public D getD2() {publicDgetD2(){			return d2;returnd2;		}}		public int hashCode() {publicinthashCode(){			final int prime = 31;finalintprime=31;			int result = 1;intresult=1;			result = prime * result + ((d1 == null) ? 0 : d1.hashCode());result=prime*result+((d1==null)?0:d1.hashCode());			result = prime * result + ((d2 == null) ? 0 : d2.hashCode());result=prime*result+((d2==null)?0:d2.hashCode());			result = prime * result + ((n1 == null) ? 0 : n1.hashCode());result=prime*result+((n1==null)?0:n1.hashCode());			result = prime * result + ((n2 == null) ? 0 : n2.hashCode());result=prime*result+((n2==null)?0:n2.hashCode());			return result;returnresult;		}}		public boolean equals(Object obj) {publicbooleanequals(Objectobj){			if (this == obj)if(this==obj)				return true;returntrue;			if (obj == null)if(obj==null)				return false;returnfalse;			if (getClass() != obj.getClass())if(getClass()!=obj.getClass())				return false;returnfalse;			@SuppressWarnings("unchecked")@SuppressWarnings("unchecked")			NDNDKey other = (NDNDKey) obj;NDNDKeyother=(NDNDKey)obj;			if (d1 == null) {if(d1==null){				if (other.d1 != null)if(other.d1!=null)					return false;returnfalse;			} else if (!d1.equals(other.d1))}elseif(!d1.equals(other.d1))				return false;returnfalse;			if (d2 == null) {if(d2==null){				if (other.d2 != null)if(other.d2!=null)					return false;returnfalse;			} else if (!d2.equals(other.d2))}elseif(!d2.equals(other.d2))				return false;returnfalse;			if (n1 == null) {if(n1==null){				if (other.n1 != null)if(other.n1!=null)					return false;returnfalse;			} else if (!n1.equals(other.n1))}elseif(!n1.equals(other.n1))				return false;returnfalse;			if (n2 == null) {if(n2==null){				if (other.n2 != null)if(other.n2!=null)					return false;returnfalse;			} else if (!n2.equals(other.n2))}elseif(!n2.equals(other.n2))				return false;returnfalse;			return true;returntrue;		}}	}}		private class CallKey {privateclassCallKey{		private final N callSite;privatefinalNcallSite;		private final M calleeMethod;privatefinalMcalleeMethod;		private final D d1, d2;privatefinalDd1,d2;		public CallKey(N callSite, D d1, M calleeMethod, D d2) {publicCallKey(NcallSite,Dd1,McalleeMethod,Dd2){			this.callSite = callSite;this.callSite=callSite;			this.calleeMethod = calleeMethod;this.calleeMethod=calleeMethod;			this.d1 = d1;this.d1=d1;			this.d2 = d2;this.d2=d2;		}}		public N getCallSite() {publicNgetCallSite(){			return callSite;returncallSite;		}}		public D getD1() {publicDgetD1(){			return d1;returnd1;		}}		public M getCalleeMethod() {publicMgetCalleeMethod(){			return calleeMethod;returncalleeMethod;		}}		public D getD2() {publicDgetD2(){			return d2;returnd2;		}}		public int hashCode() {publicinthashCode(){			final int prime = 31;finalintprime=31;			int result = 1;intresult=1;			result = prime * result + ((d1 == null) ? 0 : d1.hashCode());result=prime*result+((d1==null)?0:d1.hashCode());			result = prime * result + ((d2 == null) ? 0 : d2.hashCode());result=prime*result+((d2==null)?0:d2.hashCode());			result = prime * result + ((callSite == null) ? 0 : callSite.hashCode());result=prime*result+((callSite==null)?0:callSite.hashCode());			result = prime * result + ((calleeMethod == null) ? 0 : calleeMethod.hashCode());result=prime*result+((calleeMethod==null)?0:calleeMethod.hashCode());			return result;returnresult;		}}		public boolean equals(Object obj) {publicbooleanequals(Objectobj){			if (this == obj)if(this==obj)				return true;returntrue;			if (obj == null)if(obj==null)				return false;returnfalse;			if (getClass() != obj.getClass())if(getClass()!=obj.getClass())				return false;returnfalse;			@SuppressWarnings("unchecked")@SuppressWarnings("unchecked")			CallKey other = (CallKey) obj;CallKeyother=(CallKey)obj;			if (d1 == null) {if(d1==null){				if (other.d1 != null)if(other.d1!=null)					return false;returnfalse;			} else if (!d1.equals(other.d1))}elseif(!d1.equals(other.d1))				return false;returnfalse;			if (d2 == null) {if(d2==null){				if (other.d2 != null)if(other.d2!=null)					return false;returnfalse;			} else if (!d2.equals(other.d2))}elseif(!d2.equals(other.d2))				return false;returnfalse;			if (callSite == null) {if(callSite==null){				if (other.callSite != null)if(other.callSite!=null)					return false;returnfalse;			} else if (!callSite.equals(other.callSite))}elseif(!callSite.equals(other.callSite))				return false;returnfalse;			if (calleeMethod == null) {if(calleeMethod==null){				if (other.calleeMethod != null)if(other.calleeMethod!=null)					return false;returnfalse;			} else if (!calleeMethod.equals(other.calleeMethod))}elseif(!calleeMethod.equals(other.calleeMethod))				return false;returnfalse;			return true;returntrue;		}}	}}	private class ReturnKey extends CallKey {privateclassReturnKeyextendsCallKey{				private final N exitStmt, returnSite;privatefinalNexitStmt,returnSite;		public ReturnKey(N callSite, M calleeMethod, N exitStmt, D exitNode, N returnSite, D retNode) {publicReturnKey(NcallSite,McalleeMethod,NexitStmt,DexitNode,NreturnSite,DretNode){			super(callSite, exitNode, calleeMethod, retNode);super(callSite,exitNode,calleeMethod,retNode);			this.exitStmt = exitStmt;this.exitStmt=exitStmt;			this.returnSite = returnSite;this.returnSite=returnSite;		}}				public N getExitStmt() {publicNgetExitStmt(){			return exitStmt;returnexitStmt;		}}				public N getReturnSite() {publicNgetReturnSite(){			return returnSite;returnreturnSite;		}}		public int hashCode() {publicinthashCode(){			final int prime = 31;finalintprime=31;			int result = super.hashCode();intresult=super.hashCode();			result = prime * result + ((exitStmt == null) ? 0 : exitStmt.hashCode());result=prime*result+((exitStmt==null)?0:exitStmt.hashCode());			result = prime * result + ((returnSite == null) ? 0 : returnSite.hashCode());result=prime*result+((returnSite==null)?0:returnSite.hashCode());			return result;returnresult;		}}		public boolean equals(Object obj) {publicbooleanequals(Objectobj){			if (this == obj)if(this==obj)				return true;returntrue;			if (!super.equals(obj))if(!super.equals(obj))				return false;returnfalse;			if (getClass() != obj.getClass())if(getClass()!=obj.getClass())				return false;returnfalse;			@SuppressWarnings("unchecked")@SuppressWarnings("unchecked")			ReturnKey other = (ReturnKey) obj;ReturnKeyother=(ReturnKey)obj;			if (exitStmt == null) {if(exitStmt==null){				if (other.exitStmt != null)if(other.exitStmt!=null)					return false;returnfalse;			} else if (!exitStmt.equals(other.exitStmt))}elseif(!exitStmt.equals(other.exitStmt))				return false;returnfalse;			if (returnSite == null) {if(returnSite==null){				if (other.returnSite != null)if(other.returnSite!=null)					return false;returnfalse;			} else if (!returnSite.equals(other.returnSite))}elseif(!returnSite.equals(other.returnSite))				return false;returnfalse;			return true;returntrue;		}}	}}	public void printStats() {publicvoidprintStats(){




Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013




272


273


274


275


276


277



        logger.debug("Stats for edge-function cache:\n" +
                     "Normal:         {}\n"+
                     "Call:           {}\n"+
                     "Return:         {}\n"+
                     "Call-to-return: {}\n",
                normalCache.stats(), callCache.stats(),returnCache.stats(),callToReturnCache.stats());






Ported to SLF4J Logging

 


Marc-André Laverdière
committed
Oct 10, 2013



Ported to SLF4J Logging

 

Ported to SLF4J Logging

Marc-André Laverdière
committed
Oct 10, 2013


272


273


274


275


276


277


        logger.debug("Stats for edge-function cache:\n" +
                     "Normal:         {}\n"+
                     "Call:           {}\n"+
                     "Return:         {}\n"+
                     "Call-to-return: {}\n",
                normalCache.stats(), callCache.stats(),returnCache.stats(),callToReturnCache.stats());

        logger.debug("Stats for edge-function cache:\n" +logger.debug("Stats for edge-function cache:\n"+                     "Normal:         {}\n"+"Normal:         {}\n"+                     "Call:           {}\n"+"Call:           {}\n"+                     "Return:         {}\n"+"Return:         {}\n"+                     "Call-to-return: {}\n","Call-to-return: {}\n",                normalCache.stats(), callCache.stats(),returnCache.stats(),callToReturnCache.stats());normalCache.stats(),callCache.stats(),returnCache.stats(),callToReturnCache.stats());




initial checkin



Eric Bodden
committed
Nov 14, 2012




278


279


280



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


278


279


280


	}

}
	}}}}





