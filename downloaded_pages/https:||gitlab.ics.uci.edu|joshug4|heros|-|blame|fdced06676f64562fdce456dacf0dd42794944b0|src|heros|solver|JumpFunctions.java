



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

fdced06676f64562fdce456dacf0dd42794944b0

















fdced06676f64562fdce456dacf0dd42794944b0


Switch branch/tag










heros


src


heros


solver


JumpFunctions.java



Find file
Normal viewHistoryPermalink






JumpFunctions.java



6.45 KB









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




14




15




16




package heros.solver;

import heros.DontSynchronize;
import heros.EdgeFunction;
import heros.SynchronizedBy;
import heros.ThreadSafe;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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





import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






29














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




/**
 * The IDE algorithm uses a list of jump functions. Instead of a list, we use a set of three
 * maps that are kept in sync. This allows for efficient indexing: the algorithm accesses
 * elements from the list through three different indices.
 */
@ThreadSafe
public class JumpFunctions<N,D,L> {
	
	//mapping from target node and value to a list of all source values and associated functions
	//where the list is implemented as a mapping from the source value to the function
	//we exclude empty default functions
	@SynchronizedBy("consistent lock on this")
	protected Table<N,D,Map<D,EdgeFunction<L>>> nonEmptyReverseLookup = HashBasedTable.create();
	
	//mapping from source value and target node to a list of all target values and associated functions
	//where the list is implemented as a mapping from the source value to the function
	//we exclude empty default functions 
	@SynchronizedBy("consistent lock on this")
	protected Table<D,N,Map<D,EdgeFunction<L>>> nonEmptyForwardLookup = HashBasedTable.create();

	//a mapping from target node to a list of triples consisting of source value,
	//target value and associated function; the triple is implemented by a table
	//we exclude empty default functions 
	@SynchronizedBy("consistent lock on this")
	protected Map<N,Table<D,D,EdgeFunction<L>>> nonEmptyLookupByTargetNode = new HashMap<N,Table<D,D,EdgeFunction<L>>>();

	@DontSynchronize("immutable")	
	private final EdgeFunction<L> allTop;
	
	public JumpFunctions(EdgeFunction<L> allTop) {
		this.allTop = allTop;
	}

	/**
	 * Records a jump function. The source statement is implicit.
	 * @see PathEdge
	 */
	public synchronized void addFunction(D sourceVal, N target, D targetVal, EdgeFunction<L> function) {
		assert sourceVal!=null;
		assert target!=null;
		assert targetVal!=null;
		assert function!=null;
		
		//we do not store the default function (all-top)
		if(function.equalTo(allTop)) return;
		
		Map<D,EdgeFunction<L>> sourceValToFunc = nonEmptyReverseLookup.get(target, targetVal);
		if(sourceValToFunc==null) {
			sourceValToFunc = new LinkedHashMap<D,EdgeFunction<L>>();
			nonEmptyReverseLookup.put(target,targetVal,sourceValToFunc);
		}
		sourceValToFunc.put(sourceVal, function);
		
		Map<D, EdgeFunction<L>> targetValToFunc = nonEmptyForwardLookup.get(sourceVal, target);
		if(targetValToFunc==null) {
			targetValToFunc = new LinkedHashMap<D,EdgeFunction<L>>();
			nonEmptyForwardLookup.put(sourceVal,target,targetValToFunc);
		}
		targetValToFunc.put(targetVal, function);

		Table<D,D,EdgeFunction<L>> table = nonEmptyLookupByTargetNode.get(target);
		if(table==null) {
			table = HashBasedTable.create();
			nonEmptyLookupByTargetNode.put(target,table);
		}
		table.put(sourceVal, targetVal, function);
	}
	
	/**
     * Returns, for a given target statement and value all associated
     * source values, and for each the associated edge function.
     * The return value is a mapping from source value to function.
	 */
	public synchronized Map<D,EdgeFunction<L>> reverseLookup(N target, D targetVal) {
		assert target!=null;
		assert targetVal!=null;
		Map<D,EdgeFunction<L>> res = nonEmptyReverseLookup.get(target,targetVal);
		if(res==null) return Collections.emptyMap();
		return res;
	}
	
	/**
	 * Returns, for a given source value and target statement all
	 * associated target values, and for each the associated edge function. 
     * The return value is a mapping from target value to function.
	 */
	public synchronized Map<D,EdgeFunction<L>> forwardLookup(D sourceVal, N target) {
		assert sourceVal!=null;
		assert target!=null;
		Map<D, EdgeFunction<L>> res = nonEmptyForwardLookup.get(sourceVal, target);
		if(res==null) return Collections.emptyMap();
		return res;
	}
	
	/**
	 * Returns for a given target statement all jump function records with this target.
	 * The return value is a set of records of the form (sourceVal,targetVal,edgeFunction).
	 */
	public synchronized Set<Cell<D,D,EdgeFunction<L>>> lookupByTarget(N target) {
		assert target!=null;
		Table<D, D, EdgeFunction<L>> table = nonEmptyLookupByTargetNode.get(target);
		if(table==null) return Collections.emptySet();
		Set<Cell<D, D, EdgeFunction<L>>> res = table.cellSet();
		if(res==null) return Collections.emptySet();
		return res;
	}
	









1) semantic fix: unbalanced returns are associated with a caller-side zero...


 

 


Steven Arzt
committed
Oct 10, 2013






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




	/**
	 * Removes a jump function. The source statement is implicit.
	 * @see PathEdge
	 * @return True if the function has actually been removed. False if it was not
	 * there anyway.
	 */
	public synchronized boolean removeFunction(D sourceVal, N target, D targetVal) {
		assert sourceVal!=null;
		assert target!=null;
		assert targetVal!=null;
		
		Map<D,EdgeFunction<L>> sourceValToFunc = nonEmptyReverseLookup.get(target, targetVal);
		if (sourceValToFunc == null)
			return false;
		if (sourceValToFunc.remove(sourceVal) == null)
			return false;
		if (sourceValToFunc.isEmpty())
			nonEmptyReverseLookup.remove(targetVal, targetVal);
		
		Map<D, EdgeFunction<L>> targetValToFunc = nonEmptyForwardLookup.get(sourceVal, target);
		if (targetValToFunc == null)
			return false;
		if (targetValToFunc.remove(targetVal) == null)
			return false;
		if (targetValToFunc.isEmpty())
			nonEmptyForwardLookup.remove(sourceVal, target);

		Table<D,D,EdgeFunction<L>> table = nonEmptyLookupByTargetNode.get(target);
		if (table == null)
			return false;
		if (table.remove(sourceVal, targetVal) == null)
			return false;
		if (table.isEmpty())
			nonEmptyLookupByTargetNode.remove(target);
		
		return true;
	}










added a function to clear the jump functions


 

 


Steven Arzt
committed
Oct 31, 2013






175




176




177




178




179




180




181




182




183




	/**
	 * Removes all jump functions
	 */
	public synchronized void clear() {
		this.nonEmptyForwardLookup.clear();
		this.nonEmptyLookupByTargetNode.clear();
		this.nonEmptyReverseLookup.clear();
	}










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






184




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

fdced06676f64562fdce456dacf0dd42794944b0

















fdced06676f64562fdce456dacf0dd42794944b0


Switch branch/tag










heros


src


heros


solver


JumpFunctions.java



Find file
Normal viewHistoryPermalink






JumpFunctions.java



6.45 KB









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




14




15




16




package heros.solver;

import heros.DontSynchronize;
import heros.EdgeFunction;
import heros.SynchronizedBy;
import heros.ThreadSafe;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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





import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






29














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




/**
 * The IDE algorithm uses a list of jump functions. Instead of a list, we use a set of three
 * maps that are kept in sync. This allows for efficient indexing: the algorithm accesses
 * elements from the list through three different indices.
 */
@ThreadSafe
public class JumpFunctions<N,D,L> {
	
	//mapping from target node and value to a list of all source values and associated functions
	//where the list is implemented as a mapping from the source value to the function
	//we exclude empty default functions
	@SynchronizedBy("consistent lock on this")
	protected Table<N,D,Map<D,EdgeFunction<L>>> nonEmptyReverseLookup = HashBasedTable.create();
	
	//mapping from source value and target node to a list of all target values and associated functions
	//where the list is implemented as a mapping from the source value to the function
	//we exclude empty default functions 
	@SynchronizedBy("consistent lock on this")
	protected Table<D,N,Map<D,EdgeFunction<L>>> nonEmptyForwardLookup = HashBasedTable.create();

	//a mapping from target node to a list of triples consisting of source value,
	//target value and associated function; the triple is implemented by a table
	//we exclude empty default functions 
	@SynchronizedBy("consistent lock on this")
	protected Map<N,Table<D,D,EdgeFunction<L>>> nonEmptyLookupByTargetNode = new HashMap<N,Table<D,D,EdgeFunction<L>>>();

	@DontSynchronize("immutable")	
	private final EdgeFunction<L> allTop;
	
	public JumpFunctions(EdgeFunction<L> allTop) {
		this.allTop = allTop;
	}

	/**
	 * Records a jump function. The source statement is implicit.
	 * @see PathEdge
	 */
	public synchronized void addFunction(D sourceVal, N target, D targetVal, EdgeFunction<L> function) {
		assert sourceVal!=null;
		assert target!=null;
		assert targetVal!=null;
		assert function!=null;
		
		//we do not store the default function (all-top)
		if(function.equalTo(allTop)) return;
		
		Map<D,EdgeFunction<L>> sourceValToFunc = nonEmptyReverseLookup.get(target, targetVal);
		if(sourceValToFunc==null) {
			sourceValToFunc = new LinkedHashMap<D,EdgeFunction<L>>();
			nonEmptyReverseLookup.put(target,targetVal,sourceValToFunc);
		}
		sourceValToFunc.put(sourceVal, function);
		
		Map<D, EdgeFunction<L>> targetValToFunc = nonEmptyForwardLookup.get(sourceVal, target);
		if(targetValToFunc==null) {
			targetValToFunc = new LinkedHashMap<D,EdgeFunction<L>>();
			nonEmptyForwardLookup.put(sourceVal,target,targetValToFunc);
		}
		targetValToFunc.put(targetVal, function);

		Table<D,D,EdgeFunction<L>> table = nonEmptyLookupByTargetNode.get(target);
		if(table==null) {
			table = HashBasedTable.create();
			nonEmptyLookupByTargetNode.put(target,table);
		}
		table.put(sourceVal, targetVal, function);
	}
	
	/**
     * Returns, for a given target statement and value all associated
     * source values, and for each the associated edge function.
     * The return value is a mapping from source value to function.
	 */
	public synchronized Map<D,EdgeFunction<L>> reverseLookup(N target, D targetVal) {
		assert target!=null;
		assert targetVal!=null;
		Map<D,EdgeFunction<L>> res = nonEmptyReverseLookup.get(target,targetVal);
		if(res==null) return Collections.emptyMap();
		return res;
	}
	
	/**
	 * Returns, for a given source value and target statement all
	 * associated target values, and for each the associated edge function. 
     * The return value is a mapping from target value to function.
	 */
	public synchronized Map<D,EdgeFunction<L>> forwardLookup(D sourceVal, N target) {
		assert sourceVal!=null;
		assert target!=null;
		Map<D, EdgeFunction<L>> res = nonEmptyForwardLookup.get(sourceVal, target);
		if(res==null) return Collections.emptyMap();
		return res;
	}
	
	/**
	 * Returns for a given target statement all jump function records with this target.
	 * The return value is a set of records of the form (sourceVal,targetVal,edgeFunction).
	 */
	public synchronized Set<Cell<D,D,EdgeFunction<L>>> lookupByTarget(N target) {
		assert target!=null;
		Table<D, D, EdgeFunction<L>> table = nonEmptyLookupByTargetNode.get(target);
		if(table==null) return Collections.emptySet();
		Set<Cell<D, D, EdgeFunction<L>>> res = table.cellSet();
		if(res==null) return Collections.emptySet();
		return res;
	}
	









1) semantic fix: unbalanced returns are associated with a caller-side zero...


 

 


Steven Arzt
committed
Oct 10, 2013






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




	/**
	 * Removes a jump function. The source statement is implicit.
	 * @see PathEdge
	 * @return True if the function has actually been removed. False if it was not
	 * there anyway.
	 */
	public synchronized boolean removeFunction(D sourceVal, N target, D targetVal) {
		assert sourceVal!=null;
		assert target!=null;
		assert targetVal!=null;
		
		Map<D,EdgeFunction<L>> sourceValToFunc = nonEmptyReverseLookup.get(target, targetVal);
		if (sourceValToFunc == null)
			return false;
		if (sourceValToFunc.remove(sourceVal) == null)
			return false;
		if (sourceValToFunc.isEmpty())
			nonEmptyReverseLookup.remove(targetVal, targetVal);
		
		Map<D, EdgeFunction<L>> targetValToFunc = nonEmptyForwardLookup.get(sourceVal, target);
		if (targetValToFunc == null)
			return false;
		if (targetValToFunc.remove(targetVal) == null)
			return false;
		if (targetValToFunc.isEmpty())
			nonEmptyForwardLookup.remove(sourceVal, target);

		Table<D,D,EdgeFunction<L>> table = nonEmptyLookupByTargetNode.get(target);
		if (table == null)
			return false;
		if (table.remove(sourceVal, targetVal) == null)
			return false;
		if (table.isEmpty())
			nonEmptyLookupByTargetNode.remove(target);
		
		return true;
	}










added a function to clear the jump functions


 

 


Steven Arzt
committed
Oct 31, 2013






175




176




177




178




179




180




181




182




183




	/**
	 * Removes all jump functions
	 */
	public synchronized void clear() {
		this.nonEmptyForwardLookup.clear();
		this.nonEmptyLookupByTargetNode.clear();
		this.nonEmptyReverseLookup.clear();
	}










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






184




}











Open sidebar



Joshua Garcia heros

fdced06676f64562fdce456dacf0dd42794944b0







Open sidebar



Joshua Garcia heros

fdced06676f64562fdce456dacf0dd42794944b0




Open sidebar

Joshua Garcia heros

fdced06676f64562fdce456dacf0dd42794944b0


Joshua Garciaherosheros
fdced06676f64562fdce456dacf0dd42794944b0










fdced06676f64562fdce456dacf0dd42794944b0


Switch branch/tag










heros


src


heros


solver


JumpFunctions.java



Find file
Normal viewHistoryPermalink






JumpFunctions.java



6.45 KB









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




14




15




16




package heros.solver;

import heros.DontSynchronize;
import heros.EdgeFunction;
import heros.SynchronizedBy;
import heros.ThreadSafe;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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





import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






29














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




/**
 * The IDE algorithm uses a list of jump functions. Instead of a list, we use a set of three
 * maps that are kept in sync. This allows for efficient indexing: the algorithm accesses
 * elements from the list through three different indices.
 */
@ThreadSafe
public class JumpFunctions<N,D,L> {
	
	//mapping from target node and value to a list of all source values and associated functions
	//where the list is implemented as a mapping from the source value to the function
	//we exclude empty default functions
	@SynchronizedBy("consistent lock on this")
	protected Table<N,D,Map<D,EdgeFunction<L>>> nonEmptyReverseLookup = HashBasedTable.create();
	
	//mapping from source value and target node to a list of all target values and associated functions
	//where the list is implemented as a mapping from the source value to the function
	//we exclude empty default functions 
	@SynchronizedBy("consistent lock on this")
	protected Table<D,N,Map<D,EdgeFunction<L>>> nonEmptyForwardLookup = HashBasedTable.create();

	//a mapping from target node to a list of triples consisting of source value,
	//target value and associated function; the triple is implemented by a table
	//we exclude empty default functions 
	@SynchronizedBy("consistent lock on this")
	protected Map<N,Table<D,D,EdgeFunction<L>>> nonEmptyLookupByTargetNode = new HashMap<N,Table<D,D,EdgeFunction<L>>>();

	@DontSynchronize("immutable")	
	private final EdgeFunction<L> allTop;
	
	public JumpFunctions(EdgeFunction<L> allTop) {
		this.allTop = allTop;
	}

	/**
	 * Records a jump function. The source statement is implicit.
	 * @see PathEdge
	 */
	public synchronized void addFunction(D sourceVal, N target, D targetVal, EdgeFunction<L> function) {
		assert sourceVal!=null;
		assert target!=null;
		assert targetVal!=null;
		assert function!=null;
		
		//we do not store the default function (all-top)
		if(function.equalTo(allTop)) return;
		
		Map<D,EdgeFunction<L>> sourceValToFunc = nonEmptyReverseLookup.get(target, targetVal);
		if(sourceValToFunc==null) {
			sourceValToFunc = new LinkedHashMap<D,EdgeFunction<L>>();
			nonEmptyReverseLookup.put(target,targetVal,sourceValToFunc);
		}
		sourceValToFunc.put(sourceVal, function);
		
		Map<D, EdgeFunction<L>> targetValToFunc = nonEmptyForwardLookup.get(sourceVal, target);
		if(targetValToFunc==null) {
			targetValToFunc = new LinkedHashMap<D,EdgeFunction<L>>();
			nonEmptyForwardLookup.put(sourceVal,target,targetValToFunc);
		}
		targetValToFunc.put(targetVal, function);

		Table<D,D,EdgeFunction<L>> table = nonEmptyLookupByTargetNode.get(target);
		if(table==null) {
			table = HashBasedTable.create();
			nonEmptyLookupByTargetNode.put(target,table);
		}
		table.put(sourceVal, targetVal, function);
	}
	
	/**
     * Returns, for a given target statement and value all associated
     * source values, and for each the associated edge function.
     * The return value is a mapping from source value to function.
	 */
	public synchronized Map<D,EdgeFunction<L>> reverseLookup(N target, D targetVal) {
		assert target!=null;
		assert targetVal!=null;
		Map<D,EdgeFunction<L>> res = nonEmptyReverseLookup.get(target,targetVal);
		if(res==null) return Collections.emptyMap();
		return res;
	}
	
	/**
	 * Returns, for a given source value and target statement all
	 * associated target values, and for each the associated edge function. 
     * The return value is a mapping from target value to function.
	 */
	public synchronized Map<D,EdgeFunction<L>> forwardLookup(D sourceVal, N target) {
		assert sourceVal!=null;
		assert target!=null;
		Map<D, EdgeFunction<L>> res = nonEmptyForwardLookup.get(sourceVal, target);
		if(res==null) return Collections.emptyMap();
		return res;
	}
	
	/**
	 * Returns for a given target statement all jump function records with this target.
	 * The return value is a set of records of the form (sourceVal,targetVal,edgeFunction).
	 */
	public synchronized Set<Cell<D,D,EdgeFunction<L>>> lookupByTarget(N target) {
		assert target!=null;
		Table<D, D, EdgeFunction<L>> table = nonEmptyLookupByTargetNode.get(target);
		if(table==null) return Collections.emptySet();
		Set<Cell<D, D, EdgeFunction<L>>> res = table.cellSet();
		if(res==null) return Collections.emptySet();
		return res;
	}
	









1) semantic fix: unbalanced returns are associated with a caller-side zero...


 

 


Steven Arzt
committed
Oct 10, 2013






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




	/**
	 * Removes a jump function. The source statement is implicit.
	 * @see PathEdge
	 * @return True if the function has actually been removed. False if it was not
	 * there anyway.
	 */
	public synchronized boolean removeFunction(D sourceVal, N target, D targetVal) {
		assert sourceVal!=null;
		assert target!=null;
		assert targetVal!=null;
		
		Map<D,EdgeFunction<L>> sourceValToFunc = nonEmptyReverseLookup.get(target, targetVal);
		if (sourceValToFunc == null)
			return false;
		if (sourceValToFunc.remove(sourceVal) == null)
			return false;
		if (sourceValToFunc.isEmpty())
			nonEmptyReverseLookup.remove(targetVal, targetVal);
		
		Map<D, EdgeFunction<L>> targetValToFunc = nonEmptyForwardLookup.get(sourceVal, target);
		if (targetValToFunc == null)
			return false;
		if (targetValToFunc.remove(targetVal) == null)
			return false;
		if (targetValToFunc.isEmpty())
			nonEmptyForwardLookup.remove(sourceVal, target);

		Table<D,D,EdgeFunction<L>> table = nonEmptyLookupByTargetNode.get(target);
		if (table == null)
			return false;
		if (table.remove(sourceVal, targetVal) == null)
			return false;
		if (table.isEmpty())
			nonEmptyLookupByTargetNode.remove(target);
		
		return true;
	}










added a function to clear the jump functions


 

 


Steven Arzt
committed
Oct 31, 2013






175




176




177




178




179




180




181




182




183




	/**
	 * Removes all jump functions
	 */
	public synchronized void clear() {
		this.nonEmptyForwardLookup.clear();
		this.nonEmptyLookupByTargetNode.clear();
		this.nonEmptyReverseLookup.clear();
	}










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






184




}














fdced06676f64562fdce456dacf0dd42794944b0


Switch branch/tag










heros


src


heros


solver


JumpFunctions.java



Find file
Normal viewHistoryPermalink






JumpFunctions.java



6.45 KB









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




14




15




16




package heros.solver;

import heros.DontSynchronize;
import heros.EdgeFunction;
import heros.SynchronizedBy;
import heros.ThreadSafe;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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





import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






29














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




/**
 * The IDE algorithm uses a list of jump functions. Instead of a list, we use a set of three
 * maps that are kept in sync. This allows for efficient indexing: the algorithm accesses
 * elements from the list through three different indices.
 */
@ThreadSafe
public class JumpFunctions<N,D,L> {
	
	//mapping from target node and value to a list of all source values and associated functions
	//where the list is implemented as a mapping from the source value to the function
	//we exclude empty default functions
	@SynchronizedBy("consistent lock on this")
	protected Table<N,D,Map<D,EdgeFunction<L>>> nonEmptyReverseLookup = HashBasedTable.create();
	
	//mapping from source value and target node to a list of all target values and associated functions
	//where the list is implemented as a mapping from the source value to the function
	//we exclude empty default functions 
	@SynchronizedBy("consistent lock on this")
	protected Table<D,N,Map<D,EdgeFunction<L>>> nonEmptyForwardLookup = HashBasedTable.create();

	//a mapping from target node to a list of triples consisting of source value,
	//target value and associated function; the triple is implemented by a table
	//we exclude empty default functions 
	@SynchronizedBy("consistent lock on this")
	protected Map<N,Table<D,D,EdgeFunction<L>>> nonEmptyLookupByTargetNode = new HashMap<N,Table<D,D,EdgeFunction<L>>>();

	@DontSynchronize("immutable")	
	private final EdgeFunction<L> allTop;
	
	public JumpFunctions(EdgeFunction<L> allTop) {
		this.allTop = allTop;
	}

	/**
	 * Records a jump function. The source statement is implicit.
	 * @see PathEdge
	 */
	public synchronized void addFunction(D sourceVal, N target, D targetVal, EdgeFunction<L> function) {
		assert sourceVal!=null;
		assert target!=null;
		assert targetVal!=null;
		assert function!=null;
		
		//we do not store the default function (all-top)
		if(function.equalTo(allTop)) return;
		
		Map<D,EdgeFunction<L>> sourceValToFunc = nonEmptyReverseLookup.get(target, targetVal);
		if(sourceValToFunc==null) {
			sourceValToFunc = new LinkedHashMap<D,EdgeFunction<L>>();
			nonEmptyReverseLookup.put(target,targetVal,sourceValToFunc);
		}
		sourceValToFunc.put(sourceVal, function);
		
		Map<D, EdgeFunction<L>> targetValToFunc = nonEmptyForwardLookup.get(sourceVal, target);
		if(targetValToFunc==null) {
			targetValToFunc = new LinkedHashMap<D,EdgeFunction<L>>();
			nonEmptyForwardLookup.put(sourceVal,target,targetValToFunc);
		}
		targetValToFunc.put(targetVal, function);

		Table<D,D,EdgeFunction<L>> table = nonEmptyLookupByTargetNode.get(target);
		if(table==null) {
			table = HashBasedTable.create();
			nonEmptyLookupByTargetNode.put(target,table);
		}
		table.put(sourceVal, targetVal, function);
	}
	
	/**
     * Returns, for a given target statement and value all associated
     * source values, and for each the associated edge function.
     * The return value is a mapping from source value to function.
	 */
	public synchronized Map<D,EdgeFunction<L>> reverseLookup(N target, D targetVal) {
		assert target!=null;
		assert targetVal!=null;
		Map<D,EdgeFunction<L>> res = nonEmptyReverseLookup.get(target,targetVal);
		if(res==null) return Collections.emptyMap();
		return res;
	}
	
	/**
	 * Returns, for a given source value and target statement all
	 * associated target values, and for each the associated edge function. 
     * The return value is a mapping from target value to function.
	 */
	public synchronized Map<D,EdgeFunction<L>> forwardLookup(D sourceVal, N target) {
		assert sourceVal!=null;
		assert target!=null;
		Map<D, EdgeFunction<L>> res = nonEmptyForwardLookup.get(sourceVal, target);
		if(res==null) return Collections.emptyMap();
		return res;
	}
	
	/**
	 * Returns for a given target statement all jump function records with this target.
	 * The return value is a set of records of the form (sourceVal,targetVal,edgeFunction).
	 */
	public synchronized Set<Cell<D,D,EdgeFunction<L>>> lookupByTarget(N target) {
		assert target!=null;
		Table<D, D, EdgeFunction<L>> table = nonEmptyLookupByTargetNode.get(target);
		if(table==null) return Collections.emptySet();
		Set<Cell<D, D, EdgeFunction<L>>> res = table.cellSet();
		if(res==null) return Collections.emptySet();
		return res;
	}
	









1) semantic fix: unbalanced returns are associated with a caller-side zero...


 

 


Steven Arzt
committed
Oct 10, 2013






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




	/**
	 * Removes a jump function. The source statement is implicit.
	 * @see PathEdge
	 * @return True if the function has actually been removed. False if it was not
	 * there anyway.
	 */
	public synchronized boolean removeFunction(D sourceVal, N target, D targetVal) {
		assert sourceVal!=null;
		assert target!=null;
		assert targetVal!=null;
		
		Map<D,EdgeFunction<L>> sourceValToFunc = nonEmptyReverseLookup.get(target, targetVal);
		if (sourceValToFunc == null)
			return false;
		if (sourceValToFunc.remove(sourceVal) == null)
			return false;
		if (sourceValToFunc.isEmpty())
			nonEmptyReverseLookup.remove(targetVal, targetVal);
		
		Map<D, EdgeFunction<L>> targetValToFunc = nonEmptyForwardLookup.get(sourceVal, target);
		if (targetValToFunc == null)
			return false;
		if (targetValToFunc.remove(targetVal) == null)
			return false;
		if (targetValToFunc.isEmpty())
			nonEmptyForwardLookup.remove(sourceVal, target);

		Table<D,D,EdgeFunction<L>> table = nonEmptyLookupByTargetNode.get(target);
		if (table == null)
			return false;
		if (table.remove(sourceVal, targetVal) == null)
			return false;
		if (table.isEmpty())
			nonEmptyLookupByTargetNode.remove(target);
		
		return true;
	}










added a function to clear the jump functions


 

 


Steven Arzt
committed
Oct 31, 2013






175




176




177




178




179




180




181




182




183




	/**
	 * Removes all jump functions
	 */
	public synchronized void clear() {
		this.nonEmptyForwardLookup.clear();
		this.nonEmptyLookupByTargetNode.clear();
		this.nonEmptyReverseLookup.clear();
	}










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






184




}










fdced06676f64562fdce456dacf0dd42794944b0


Switch branch/tag










heros


src


heros


solver


JumpFunctions.java



Find file
Normal viewHistoryPermalink




fdced06676f64562fdce456dacf0dd42794944b0


Switch branch/tag










heros


src


heros


solver


JumpFunctions.java





fdced06676f64562fdce456dacf0dd42794944b0


Switch branch/tag








fdced06676f64562fdce456dacf0dd42794944b0


Switch branch/tag





fdced06676f64562fdce456dacf0dd42794944b0

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src

heros

solver

JumpFunctions.java
Find file
Normal viewHistoryPermalink




JumpFunctions.java



6.45 KB









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




14




15




16




package heros.solver;

import heros.DontSynchronize;
import heros.EdgeFunction;
import heros.SynchronizedBy;
import heros.ThreadSafe;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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





import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






29














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




/**
 * The IDE algorithm uses a list of jump functions. Instead of a list, we use a set of three
 * maps that are kept in sync. This allows for efficient indexing: the algorithm accesses
 * elements from the list through three different indices.
 */
@ThreadSafe
public class JumpFunctions<N,D,L> {
	
	//mapping from target node and value to a list of all source values and associated functions
	//where the list is implemented as a mapping from the source value to the function
	//we exclude empty default functions
	@SynchronizedBy("consistent lock on this")
	protected Table<N,D,Map<D,EdgeFunction<L>>> nonEmptyReverseLookup = HashBasedTable.create();
	
	//mapping from source value and target node to a list of all target values and associated functions
	//where the list is implemented as a mapping from the source value to the function
	//we exclude empty default functions 
	@SynchronizedBy("consistent lock on this")
	protected Table<D,N,Map<D,EdgeFunction<L>>> nonEmptyForwardLookup = HashBasedTable.create();

	//a mapping from target node to a list of triples consisting of source value,
	//target value and associated function; the triple is implemented by a table
	//we exclude empty default functions 
	@SynchronizedBy("consistent lock on this")
	protected Map<N,Table<D,D,EdgeFunction<L>>> nonEmptyLookupByTargetNode = new HashMap<N,Table<D,D,EdgeFunction<L>>>();

	@DontSynchronize("immutable")	
	private final EdgeFunction<L> allTop;
	
	public JumpFunctions(EdgeFunction<L> allTop) {
		this.allTop = allTop;
	}

	/**
	 * Records a jump function. The source statement is implicit.
	 * @see PathEdge
	 */
	public synchronized void addFunction(D sourceVal, N target, D targetVal, EdgeFunction<L> function) {
		assert sourceVal!=null;
		assert target!=null;
		assert targetVal!=null;
		assert function!=null;
		
		//we do not store the default function (all-top)
		if(function.equalTo(allTop)) return;
		
		Map<D,EdgeFunction<L>> sourceValToFunc = nonEmptyReverseLookup.get(target, targetVal);
		if(sourceValToFunc==null) {
			sourceValToFunc = new LinkedHashMap<D,EdgeFunction<L>>();
			nonEmptyReverseLookup.put(target,targetVal,sourceValToFunc);
		}
		sourceValToFunc.put(sourceVal, function);
		
		Map<D, EdgeFunction<L>> targetValToFunc = nonEmptyForwardLookup.get(sourceVal, target);
		if(targetValToFunc==null) {
			targetValToFunc = new LinkedHashMap<D,EdgeFunction<L>>();
			nonEmptyForwardLookup.put(sourceVal,target,targetValToFunc);
		}
		targetValToFunc.put(targetVal, function);

		Table<D,D,EdgeFunction<L>> table = nonEmptyLookupByTargetNode.get(target);
		if(table==null) {
			table = HashBasedTable.create();
			nonEmptyLookupByTargetNode.put(target,table);
		}
		table.put(sourceVal, targetVal, function);
	}
	
	/**
     * Returns, for a given target statement and value all associated
     * source values, and for each the associated edge function.
     * The return value is a mapping from source value to function.
	 */
	public synchronized Map<D,EdgeFunction<L>> reverseLookup(N target, D targetVal) {
		assert target!=null;
		assert targetVal!=null;
		Map<D,EdgeFunction<L>> res = nonEmptyReverseLookup.get(target,targetVal);
		if(res==null) return Collections.emptyMap();
		return res;
	}
	
	/**
	 * Returns, for a given source value and target statement all
	 * associated target values, and for each the associated edge function. 
     * The return value is a mapping from target value to function.
	 */
	public synchronized Map<D,EdgeFunction<L>> forwardLookup(D sourceVal, N target) {
		assert sourceVal!=null;
		assert target!=null;
		Map<D, EdgeFunction<L>> res = nonEmptyForwardLookup.get(sourceVal, target);
		if(res==null) return Collections.emptyMap();
		return res;
	}
	
	/**
	 * Returns for a given target statement all jump function records with this target.
	 * The return value is a set of records of the form (sourceVal,targetVal,edgeFunction).
	 */
	public synchronized Set<Cell<D,D,EdgeFunction<L>>> lookupByTarget(N target) {
		assert target!=null;
		Table<D, D, EdgeFunction<L>> table = nonEmptyLookupByTargetNode.get(target);
		if(table==null) return Collections.emptySet();
		Set<Cell<D, D, EdgeFunction<L>>> res = table.cellSet();
		if(res==null) return Collections.emptySet();
		return res;
	}
	









1) semantic fix: unbalanced returns are associated with a caller-side zero...


 

 


Steven Arzt
committed
Oct 10, 2013






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




	/**
	 * Removes a jump function. The source statement is implicit.
	 * @see PathEdge
	 * @return True if the function has actually been removed. False if it was not
	 * there anyway.
	 */
	public synchronized boolean removeFunction(D sourceVal, N target, D targetVal) {
		assert sourceVal!=null;
		assert target!=null;
		assert targetVal!=null;
		
		Map<D,EdgeFunction<L>> sourceValToFunc = nonEmptyReverseLookup.get(target, targetVal);
		if (sourceValToFunc == null)
			return false;
		if (sourceValToFunc.remove(sourceVal) == null)
			return false;
		if (sourceValToFunc.isEmpty())
			nonEmptyReverseLookup.remove(targetVal, targetVal);
		
		Map<D, EdgeFunction<L>> targetValToFunc = nonEmptyForwardLookup.get(sourceVal, target);
		if (targetValToFunc == null)
			return false;
		if (targetValToFunc.remove(targetVal) == null)
			return false;
		if (targetValToFunc.isEmpty())
			nonEmptyForwardLookup.remove(sourceVal, target);

		Table<D,D,EdgeFunction<L>> table = nonEmptyLookupByTargetNode.get(target);
		if (table == null)
			return false;
		if (table.remove(sourceVal, targetVal) == null)
			return false;
		if (table.isEmpty())
			nonEmptyLookupByTargetNode.remove(target);
		
		return true;
	}










added a function to clear the jump functions


 

 


Steven Arzt
committed
Oct 31, 2013






175




176




177




178




179




180




181




182




183




	/**
	 * Removes all jump functions
	 */
	public synchronized void clear() {
		this.nonEmptyForwardLookup.clear();
		this.nonEmptyLookupByTargetNode.clear();
		this.nonEmptyReverseLookup.clear();
	}










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






184




}








JumpFunctions.java



6.45 KB










JumpFunctions.java



6.45 KB









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




14




15




16




package heros.solver;

import heros.DontSynchronize;
import heros.EdgeFunction;
import heros.SynchronizedBy;
import heros.ThreadSafe;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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





import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






29














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




/**
 * The IDE algorithm uses a list of jump functions. Instead of a list, we use a set of three
 * maps that are kept in sync. This allows for efficient indexing: the algorithm accesses
 * elements from the list through three different indices.
 */
@ThreadSafe
public class JumpFunctions<N,D,L> {
	
	//mapping from target node and value to a list of all source values and associated functions
	//where the list is implemented as a mapping from the source value to the function
	//we exclude empty default functions
	@SynchronizedBy("consistent lock on this")
	protected Table<N,D,Map<D,EdgeFunction<L>>> nonEmptyReverseLookup = HashBasedTable.create();
	
	//mapping from source value and target node to a list of all target values and associated functions
	//where the list is implemented as a mapping from the source value to the function
	//we exclude empty default functions 
	@SynchronizedBy("consistent lock on this")
	protected Table<D,N,Map<D,EdgeFunction<L>>> nonEmptyForwardLookup = HashBasedTable.create();

	//a mapping from target node to a list of triples consisting of source value,
	//target value and associated function; the triple is implemented by a table
	//we exclude empty default functions 
	@SynchronizedBy("consistent lock on this")
	protected Map<N,Table<D,D,EdgeFunction<L>>> nonEmptyLookupByTargetNode = new HashMap<N,Table<D,D,EdgeFunction<L>>>();

	@DontSynchronize("immutable")	
	private final EdgeFunction<L> allTop;
	
	public JumpFunctions(EdgeFunction<L> allTop) {
		this.allTop = allTop;
	}

	/**
	 * Records a jump function. The source statement is implicit.
	 * @see PathEdge
	 */
	public synchronized void addFunction(D sourceVal, N target, D targetVal, EdgeFunction<L> function) {
		assert sourceVal!=null;
		assert target!=null;
		assert targetVal!=null;
		assert function!=null;
		
		//we do not store the default function (all-top)
		if(function.equalTo(allTop)) return;
		
		Map<D,EdgeFunction<L>> sourceValToFunc = nonEmptyReverseLookup.get(target, targetVal);
		if(sourceValToFunc==null) {
			sourceValToFunc = new LinkedHashMap<D,EdgeFunction<L>>();
			nonEmptyReverseLookup.put(target,targetVal,sourceValToFunc);
		}
		sourceValToFunc.put(sourceVal, function);
		
		Map<D, EdgeFunction<L>> targetValToFunc = nonEmptyForwardLookup.get(sourceVal, target);
		if(targetValToFunc==null) {
			targetValToFunc = new LinkedHashMap<D,EdgeFunction<L>>();
			nonEmptyForwardLookup.put(sourceVal,target,targetValToFunc);
		}
		targetValToFunc.put(targetVal, function);

		Table<D,D,EdgeFunction<L>> table = nonEmptyLookupByTargetNode.get(target);
		if(table==null) {
			table = HashBasedTable.create();
			nonEmptyLookupByTargetNode.put(target,table);
		}
		table.put(sourceVal, targetVal, function);
	}
	
	/**
     * Returns, for a given target statement and value all associated
     * source values, and for each the associated edge function.
     * The return value is a mapping from source value to function.
	 */
	public synchronized Map<D,EdgeFunction<L>> reverseLookup(N target, D targetVal) {
		assert target!=null;
		assert targetVal!=null;
		Map<D,EdgeFunction<L>> res = nonEmptyReverseLookup.get(target,targetVal);
		if(res==null) return Collections.emptyMap();
		return res;
	}
	
	/**
	 * Returns, for a given source value and target statement all
	 * associated target values, and for each the associated edge function. 
     * The return value is a mapping from target value to function.
	 */
	public synchronized Map<D,EdgeFunction<L>> forwardLookup(D sourceVal, N target) {
		assert sourceVal!=null;
		assert target!=null;
		Map<D, EdgeFunction<L>> res = nonEmptyForwardLookup.get(sourceVal, target);
		if(res==null) return Collections.emptyMap();
		return res;
	}
	
	/**
	 * Returns for a given target statement all jump function records with this target.
	 * The return value is a set of records of the form (sourceVal,targetVal,edgeFunction).
	 */
	public synchronized Set<Cell<D,D,EdgeFunction<L>>> lookupByTarget(N target) {
		assert target!=null;
		Table<D, D, EdgeFunction<L>> table = nonEmptyLookupByTargetNode.get(target);
		if(table==null) return Collections.emptySet();
		Set<Cell<D, D, EdgeFunction<L>>> res = table.cellSet();
		if(res==null) return Collections.emptySet();
		return res;
	}
	









1) semantic fix: unbalanced returns are associated with a caller-side zero...


 

 


Steven Arzt
committed
Oct 10, 2013






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




	/**
	 * Removes a jump function. The source statement is implicit.
	 * @see PathEdge
	 * @return True if the function has actually been removed. False if it was not
	 * there anyway.
	 */
	public synchronized boolean removeFunction(D sourceVal, N target, D targetVal) {
		assert sourceVal!=null;
		assert target!=null;
		assert targetVal!=null;
		
		Map<D,EdgeFunction<L>> sourceValToFunc = nonEmptyReverseLookup.get(target, targetVal);
		if (sourceValToFunc == null)
			return false;
		if (sourceValToFunc.remove(sourceVal) == null)
			return false;
		if (sourceValToFunc.isEmpty())
			nonEmptyReverseLookup.remove(targetVal, targetVal);
		
		Map<D, EdgeFunction<L>> targetValToFunc = nonEmptyForwardLookup.get(sourceVal, target);
		if (targetValToFunc == null)
			return false;
		if (targetValToFunc.remove(targetVal) == null)
			return false;
		if (targetValToFunc.isEmpty())
			nonEmptyForwardLookup.remove(sourceVal, target);

		Table<D,D,EdgeFunction<L>> table = nonEmptyLookupByTargetNode.get(target);
		if (table == null)
			return false;
		if (table.remove(sourceVal, targetVal) == null)
			return false;
		if (table.isEmpty())
			nonEmptyLookupByTargetNode.remove(target);
		
		return true;
	}










added a function to clear the jump functions


 

 


Steven Arzt
committed
Oct 31, 2013






175




176




177




178




179




180




181




182




183




	/**
	 * Removes all jump functions
	 */
	public synchronized void clear() {
		this.nonEmptyForwardLookup.clear();
		this.nonEmptyLookupByTargetNode.clear();
		this.nonEmptyReverseLookup.clear();
	}










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






184




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

14

15

16
package heros.solver;packageheros.solver;import heros.DontSynchronize;importheros.DontSynchronize;import heros.EdgeFunction;importheros.EdgeFunction;import heros.SynchronizedBy;importheros.SynchronizedBy;import heros.ThreadSafe;importheros.ThreadSafe;



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

21

22

23

24

25

26

27

28
import java.util.Collections;importjava.util.Collections;import java.util.HashMap;importjava.util.HashMap;import java.util.LinkedHashMap;importjava.util.LinkedHashMap;import java.util.Map;importjava.util.Map;import java.util.Set;importjava.util.Set;import com.google.common.collect.HashBasedTable;importcom.google.common.collect.HashBasedTable;import com.google.common.collect.Table;importcom.google.common.collect.Table;import com.google.common.collect.Table.Cell;importcom.google.common.collect.Table.Cell;



renamed package


 

 


Eric Bodden
committed
Nov 28, 2012



renamed package


 

 

renamed package

 

Eric Bodden
committed
Nov 28, 2012

29




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
/**/** * The IDE algorithm uses a list of jump functions. Instead of a list, we use a set of three * The IDE algorithm uses a list of jump functions. Instead of a list, we use a set of three * maps that are kept in sync. This allows for efficient indexing: the algorithm accesses * maps that are kept in sync. This allows for efficient indexing: the algorithm accesses * elements from the list through three different indices. * elements from the list through three different indices. */ */@ThreadSafe@ThreadSafepublic class JumpFunctions<N,D,L> {publicclassJumpFunctions<N,D,L>{		//mapping from target node and value to a list of all source values and associated functions//mapping from target node and value to a list of all source values and associated functions	//where the list is implemented as a mapping from the source value to the function//where the list is implemented as a mapping from the source value to the function	//we exclude empty default functions//we exclude empty default functions	@SynchronizedBy("consistent lock on this")@SynchronizedBy("consistent lock on this")	protected Table<N,D,Map<D,EdgeFunction<L>>> nonEmptyReverseLookup = HashBasedTable.create();protectedTable<N,D,Map<D,EdgeFunction<L>>>nonEmptyReverseLookup=HashBasedTable.create();		//mapping from source value and target node to a list of all target values and associated functions//mapping from source value and target node to a list of all target values and associated functions	//where the list is implemented as a mapping from the source value to the function//where the list is implemented as a mapping from the source value to the function	//we exclude empty default functions //we exclude empty default functions 	@SynchronizedBy("consistent lock on this")@SynchronizedBy("consistent lock on this")	protected Table<D,N,Map<D,EdgeFunction<L>>> nonEmptyForwardLookup = HashBasedTable.create();protectedTable<D,N,Map<D,EdgeFunction<L>>>nonEmptyForwardLookup=HashBasedTable.create();	//a mapping from target node to a list of triples consisting of source value,//a mapping from target node to a list of triples consisting of source value,	//target value and associated function; the triple is implemented by a table//target value and associated function; the triple is implemented by a table	//we exclude empty default functions //we exclude empty default functions 	@SynchronizedBy("consistent lock on this")@SynchronizedBy("consistent lock on this")	protected Map<N,Table<D,D,EdgeFunction<L>>> nonEmptyLookupByTargetNode = new HashMap<N,Table<D,D,EdgeFunction<L>>>();protectedMap<N,Table<D,D,EdgeFunction<L>>>nonEmptyLookupByTargetNode=newHashMap<N,Table<D,D,EdgeFunction<L>>>();	@DontSynchronize("immutable")	@DontSynchronize("immutable")	private final EdgeFunction<L> allTop;privatefinalEdgeFunction<L>allTop;		public JumpFunctions(EdgeFunction<L> allTop) {publicJumpFunctions(EdgeFunction<L>allTop){		this.allTop = allTop;this.allTop=allTop;	}}	/**/**	 * Records a jump function. The source statement is implicit.	 * Records a jump function. The source statement is implicit.	 * @see PathEdge	 * @see PathEdge	 */	 */	public synchronized void addFunction(D sourceVal, N target, D targetVal, EdgeFunction<L> function) {publicsynchronizedvoidaddFunction(DsourceVal,Ntarget,DtargetVal,EdgeFunction<L>function){		assert sourceVal!=null;assertsourceVal!=null;		assert target!=null;asserttarget!=null;		assert targetVal!=null;asserttargetVal!=null;		assert function!=null;assertfunction!=null;				//we do not store the default function (all-top)//we do not store the default function (all-top)		if(function.equalTo(allTop)) return;if(function.equalTo(allTop))return;				Map<D,EdgeFunction<L>> sourceValToFunc = nonEmptyReverseLookup.get(target, targetVal);Map<D,EdgeFunction<L>>sourceValToFunc=nonEmptyReverseLookup.get(target,targetVal);		if(sourceValToFunc==null) {if(sourceValToFunc==null){			sourceValToFunc = new LinkedHashMap<D,EdgeFunction<L>>();sourceValToFunc=newLinkedHashMap<D,EdgeFunction<L>>();			nonEmptyReverseLookup.put(target,targetVal,sourceValToFunc);nonEmptyReverseLookup.put(target,targetVal,sourceValToFunc);		}}		sourceValToFunc.put(sourceVal, function);sourceValToFunc.put(sourceVal,function);				Map<D, EdgeFunction<L>> targetValToFunc = nonEmptyForwardLookup.get(sourceVal, target);Map<D,EdgeFunction<L>>targetValToFunc=nonEmptyForwardLookup.get(sourceVal,target);		if(targetValToFunc==null) {if(targetValToFunc==null){			targetValToFunc = new LinkedHashMap<D,EdgeFunction<L>>();targetValToFunc=newLinkedHashMap<D,EdgeFunction<L>>();			nonEmptyForwardLookup.put(sourceVal,target,targetValToFunc);nonEmptyForwardLookup.put(sourceVal,target,targetValToFunc);		}}		targetValToFunc.put(targetVal, function);targetValToFunc.put(targetVal,function);		Table<D,D,EdgeFunction<L>> table = nonEmptyLookupByTargetNode.get(target);Table<D,D,EdgeFunction<L>>table=nonEmptyLookupByTargetNode.get(target);		if(table==null) {if(table==null){			table = HashBasedTable.create();table=HashBasedTable.create();			nonEmptyLookupByTargetNode.put(target,table);nonEmptyLookupByTargetNode.put(target,table);		}}		table.put(sourceVal, targetVal, function);table.put(sourceVal,targetVal,function);	}}		/**/**     * Returns, for a given target statement and value all associated     * Returns, for a given target statement and value all associated     * source values, and for each the associated edge function.     * source values, and for each the associated edge function.     * The return value is a mapping from source value to function.     * The return value is a mapping from source value to function.	 */	 */	public synchronized Map<D,EdgeFunction<L>> reverseLookup(N target, D targetVal) {publicsynchronizedMap<D,EdgeFunction<L>>reverseLookup(Ntarget,DtargetVal){		assert target!=null;asserttarget!=null;		assert targetVal!=null;asserttargetVal!=null;		Map<D,EdgeFunction<L>> res = nonEmptyReverseLookup.get(target,targetVal);Map<D,EdgeFunction<L>>res=nonEmptyReverseLookup.get(target,targetVal);		if(res==null) return Collections.emptyMap();if(res==null)returnCollections.emptyMap();		return res;returnres;	}}		/**/**	 * Returns, for a given source value and target statement all	 * Returns, for a given source value and target statement all	 * associated target values, and for each the associated edge function. 	 * associated target values, and for each the associated edge function.      * The return value is a mapping from target value to function.     * The return value is a mapping from target value to function.	 */	 */	public synchronized Map<D,EdgeFunction<L>> forwardLookup(D sourceVal, N target) {publicsynchronizedMap<D,EdgeFunction<L>>forwardLookup(DsourceVal,Ntarget){		assert sourceVal!=null;assertsourceVal!=null;		assert target!=null;asserttarget!=null;		Map<D, EdgeFunction<L>> res = nonEmptyForwardLookup.get(sourceVal, target);Map<D,EdgeFunction<L>>res=nonEmptyForwardLookup.get(sourceVal,target);		if(res==null) return Collections.emptyMap();if(res==null)returnCollections.emptyMap();		return res;returnres;	}}		/**/**	 * Returns for a given target statement all jump function records with this target.	 * Returns for a given target statement all jump function records with this target.	 * The return value is a set of records of the form (sourceVal,targetVal,edgeFunction).	 * The return value is a set of records of the form (sourceVal,targetVal,edgeFunction).	 */	 */	public synchronized Set<Cell<D,D,EdgeFunction<L>>> lookupByTarget(N target) {publicsynchronizedSet<Cell<D,D,EdgeFunction<L>>>lookupByTarget(Ntarget){		assert target!=null;asserttarget!=null;		Table<D, D, EdgeFunction<L>> table = nonEmptyLookupByTargetNode.get(target);Table<D,D,EdgeFunction<L>>table=nonEmptyLookupByTargetNode.get(target);		if(table==null) return Collections.emptySet();if(table==null)returnCollections.emptySet();		Set<Cell<D, D, EdgeFunction<L>>> res = table.cellSet();Set<Cell<D,D,EdgeFunction<L>>>res=table.cellSet();		if(res==null) return Collections.emptySet();if(res==null)returnCollections.emptySet();		return res;returnres;	}}	



1) semantic fix: unbalanced returns are associated with a caller-side zero...


 

 


Steven Arzt
committed
Oct 10, 2013



1) semantic fix: unbalanced returns are associated with a caller-side zero...


 

 

1) semantic fix: unbalanced returns are associated with a caller-side zero...

 

Steven Arzt
committed
Oct 10, 2013

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
	/**/**	 * Removes a jump function. The source statement is implicit.	 * Removes a jump function. The source statement is implicit.	 * @see PathEdge	 * @see PathEdge	 * @return True if the function has actually been removed. False if it was not	 * @return True if the function has actually been removed. False if it was not	 * there anyway.	 * there anyway.	 */	 */	public synchronized boolean removeFunction(D sourceVal, N target, D targetVal) {publicsynchronizedbooleanremoveFunction(DsourceVal,Ntarget,DtargetVal){		assert sourceVal!=null;assertsourceVal!=null;		assert target!=null;asserttarget!=null;		assert targetVal!=null;asserttargetVal!=null;				Map<D,EdgeFunction<L>> sourceValToFunc = nonEmptyReverseLookup.get(target, targetVal);Map<D,EdgeFunction<L>>sourceValToFunc=nonEmptyReverseLookup.get(target,targetVal);		if (sourceValToFunc == null)if(sourceValToFunc==null)			return false;returnfalse;		if (sourceValToFunc.remove(sourceVal) == null)if(sourceValToFunc.remove(sourceVal)==null)			return false;returnfalse;		if (sourceValToFunc.isEmpty())if(sourceValToFunc.isEmpty())			nonEmptyReverseLookup.remove(targetVal, targetVal);nonEmptyReverseLookup.remove(targetVal,targetVal);				Map<D, EdgeFunction<L>> targetValToFunc = nonEmptyForwardLookup.get(sourceVal, target);Map<D,EdgeFunction<L>>targetValToFunc=nonEmptyForwardLookup.get(sourceVal,target);		if (targetValToFunc == null)if(targetValToFunc==null)			return false;returnfalse;		if (targetValToFunc.remove(targetVal) == null)if(targetValToFunc.remove(targetVal)==null)			return false;returnfalse;		if (targetValToFunc.isEmpty())if(targetValToFunc.isEmpty())			nonEmptyForwardLookup.remove(sourceVal, target);nonEmptyForwardLookup.remove(sourceVal,target);		Table<D,D,EdgeFunction<L>> table = nonEmptyLookupByTargetNode.get(target);Table<D,D,EdgeFunction<L>>table=nonEmptyLookupByTargetNode.get(target);		if (table == null)if(table==null)			return false;returnfalse;		if (table.remove(sourceVal, targetVal) == null)if(table.remove(sourceVal,targetVal)==null)			return false;returnfalse;		if (table.isEmpty())if(table.isEmpty())			nonEmptyLookupByTargetNode.remove(target);nonEmptyLookupByTargetNode.remove(target);				return true;returntrue;	}}



added a function to clear the jump functions


 

 


Steven Arzt
committed
Oct 31, 2013



added a function to clear the jump functions


 

 

added a function to clear the jump functions

 

Steven Arzt
committed
Oct 31, 2013

175

176

177

178

179

180

181

182

183
	/**/**	 * Removes all jump functions	 * Removes all jump functions	 */	 */	public synchronized void clear() {publicsynchronizedvoidclear(){		this.nonEmptyForwardLookup.clear();this.nonEmptyForwardLookup.clear();		this.nonEmptyLookupByTargetNode.clear();this.nonEmptyLookupByTargetNode.clear();		this.nonEmptyReverseLookup.clear();this.nonEmptyReverseLookup.clear();	}}



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

184
}}





