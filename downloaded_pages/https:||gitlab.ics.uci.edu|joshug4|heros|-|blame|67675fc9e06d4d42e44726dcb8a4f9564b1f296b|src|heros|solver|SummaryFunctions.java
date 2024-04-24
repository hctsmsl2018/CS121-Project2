



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

67675fc9e06d4d42e44726dcb8a4f9564b1f296b

















67675fc9e06d4d42e44726dcb8a4f9564b1f296b


Switch branch/tag










heros


src


heros


solver


SummaryFunctions.java



Find file
Normal viewHistoryPermalink






SummaryFunctions.java



2.55 KB









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




package heros.solver;

import heros.EdgeFunction;
import heros.SynchronizedBy;
import heros.ThreadSafe;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






16




17




18




19




20




21




22




23





import java.util.Collections;
import java.util.Map;


import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






24














initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




/**
 * A data structure to record summary functions in an indexed fashion, for fast retrieval.
 */
@ThreadSafe
public class SummaryFunctions<N,D,V> {
	
	@SynchronizedBy("consistent lock on this")
	protected Table<N,D,Table<N,D,EdgeFunction<V>>> table = HashBasedTable.create();
	
	/**
	 * Inserts a summary function.
	 * @param callSite The call site with which this function is associated.
	 * @param sourceVal The source value at the call site. 
	 * @param retSite The return site (in the caller) with which this function is associated.
	 * @param targetVal The target value at the return site.
	 * @param function The edge function used to compute V-type values from the source node to the target node.  
	 */
	public synchronized void insertFunction(N callSite,D sourceVal, N retSite, D targetVal, EdgeFunction<V> function) {
		assert callSite!=null;
		assert sourceVal!=null;
		assert retSite!=null;
		assert targetVal!=null;
		assert function!=null;
		
		Table<N, D, EdgeFunction<V>> targetAndTargetValToFunction = table.get(callSite,sourceVal);
		if(targetAndTargetValToFunction==null) {
			targetAndTargetValToFunction = HashBasedTable.create();
			table.put(callSite,sourceVal,targetAndTargetValToFunction);
		}
		targetAndTargetValToFunction.put(retSite, targetVal, function);
	}

	/**
	 * Retrieves all summary functions for a given call site, source value and
	 * return site (in the caller).
	 * The result contains a mapping from target value to associated edge function.
	 */
	public synchronized Map<D,EdgeFunction<V>> summariesFor(N callSite, D sourceVal, N returnSite) {
		assert callSite!=null;
		assert sourceVal!=null;
		assert returnSite!=null;

		Table<N, D, EdgeFunction<V>> res = table.get(callSite,sourceVal);
		if(res==null) return Collections.emptyMap();
		else {
			return res.row(returnSite);
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

67675fc9e06d4d42e44726dcb8a4f9564b1f296b

















67675fc9e06d4d42e44726dcb8a4f9564b1f296b


Switch branch/tag










heros


src


heros


solver


SummaryFunctions.java



Find file
Normal viewHistoryPermalink






SummaryFunctions.java



2.55 KB









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




package heros.solver;

import heros.EdgeFunction;
import heros.SynchronizedBy;
import heros.ThreadSafe;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






16




17




18




19




20




21




22




23





import java.util.Collections;
import java.util.Map;


import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






24














initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




/**
 * A data structure to record summary functions in an indexed fashion, for fast retrieval.
 */
@ThreadSafe
public class SummaryFunctions<N,D,V> {
	
	@SynchronizedBy("consistent lock on this")
	protected Table<N,D,Table<N,D,EdgeFunction<V>>> table = HashBasedTable.create();
	
	/**
	 * Inserts a summary function.
	 * @param callSite The call site with which this function is associated.
	 * @param sourceVal The source value at the call site. 
	 * @param retSite The return site (in the caller) with which this function is associated.
	 * @param targetVal The target value at the return site.
	 * @param function The edge function used to compute V-type values from the source node to the target node.  
	 */
	public synchronized void insertFunction(N callSite,D sourceVal, N retSite, D targetVal, EdgeFunction<V> function) {
		assert callSite!=null;
		assert sourceVal!=null;
		assert retSite!=null;
		assert targetVal!=null;
		assert function!=null;
		
		Table<N, D, EdgeFunction<V>> targetAndTargetValToFunction = table.get(callSite,sourceVal);
		if(targetAndTargetValToFunction==null) {
			targetAndTargetValToFunction = HashBasedTable.create();
			table.put(callSite,sourceVal,targetAndTargetValToFunction);
		}
		targetAndTargetValToFunction.put(retSite, targetVal, function);
	}

	/**
	 * Retrieves all summary functions for a given call site, source value and
	 * return site (in the caller).
	 * The result contains a mapping from target value to associated edge function.
	 */
	public synchronized Map<D,EdgeFunction<V>> summariesFor(N callSite, D sourceVal, N returnSite) {
		assert callSite!=null;
		assert sourceVal!=null;
		assert returnSite!=null;

		Table<N, D, EdgeFunction<V>> res = table.get(callSite,sourceVal);
		if(res==null) return Collections.emptyMap();
		else {
			return res.row(returnSite);
		}
	}
}











Open sidebar



Joshua Garcia heros

67675fc9e06d4d42e44726dcb8a4f9564b1f296b







Open sidebar



Joshua Garcia heros

67675fc9e06d4d42e44726dcb8a4f9564b1f296b




Open sidebar

Joshua Garcia heros

67675fc9e06d4d42e44726dcb8a4f9564b1f296b


Joshua Garciaherosheros
67675fc9e06d4d42e44726dcb8a4f9564b1f296b










67675fc9e06d4d42e44726dcb8a4f9564b1f296b


Switch branch/tag










heros


src


heros


solver


SummaryFunctions.java



Find file
Normal viewHistoryPermalink






SummaryFunctions.java



2.55 KB









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




package heros.solver;

import heros.EdgeFunction;
import heros.SynchronizedBy;
import heros.ThreadSafe;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






16




17




18




19




20




21




22




23





import java.util.Collections;
import java.util.Map;


import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






24














initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




/**
 * A data structure to record summary functions in an indexed fashion, for fast retrieval.
 */
@ThreadSafe
public class SummaryFunctions<N,D,V> {
	
	@SynchronizedBy("consistent lock on this")
	protected Table<N,D,Table<N,D,EdgeFunction<V>>> table = HashBasedTable.create();
	
	/**
	 * Inserts a summary function.
	 * @param callSite The call site with which this function is associated.
	 * @param sourceVal The source value at the call site. 
	 * @param retSite The return site (in the caller) with which this function is associated.
	 * @param targetVal The target value at the return site.
	 * @param function The edge function used to compute V-type values from the source node to the target node.  
	 */
	public synchronized void insertFunction(N callSite,D sourceVal, N retSite, D targetVal, EdgeFunction<V> function) {
		assert callSite!=null;
		assert sourceVal!=null;
		assert retSite!=null;
		assert targetVal!=null;
		assert function!=null;
		
		Table<N, D, EdgeFunction<V>> targetAndTargetValToFunction = table.get(callSite,sourceVal);
		if(targetAndTargetValToFunction==null) {
			targetAndTargetValToFunction = HashBasedTable.create();
			table.put(callSite,sourceVal,targetAndTargetValToFunction);
		}
		targetAndTargetValToFunction.put(retSite, targetVal, function);
	}

	/**
	 * Retrieves all summary functions for a given call site, source value and
	 * return site (in the caller).
	 * The result contains a mapping from target value to associated edge function.
	 */
	public synchronized Map<D,EdgeFunction<V>> summariesFor(N callSite, D sourceVal, N returnSite) {
		assert callSite!=null;
		assert sourceVal!=null;
		assert returnSite!=null;

		Table<N, D, EdgeFunction<V>> res = table.get(callSite,sourceVal);
		if(res==null) return Collections.emptyMap();
		else {
			return res.row(returnSite);
		}
	}
}














67675fc9e06d4d42e44726dcb8a4f9564b1f296b


Switch branch/tag










heros


src


heros


solver


SummaryFunctions.java



Find file
Normal viewHistoryPermalink






SummaryFunctions.java



2.55 KB









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




package heros.solver;

import heros.EdgeFunction;
import heros.SynchronizedBy;
import heros.ThreadSafe;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






16




17




18




19




20




21




22




23





import java.util.Collections;
import java.util.Map;


import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






24














initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




/**
 * A data structure to record summary functions in an indexed fashion, for fast retrieval.
 */
@ThreadSafe
public class SummaryFunctions<N,D,V> {
	
	@SynchronizedBy("consistent lock on this")
	protected Table<N,D,Table<N,D,EdgeFunction<V>>> table = HashBasedTable.create();
	
	/**
	 * Inserts a summary function.
	 * @param callSite The call site with which this function is associated.
	 * @param sourceVal The source value at the call site. 
	 * @param retSite The return site (in the caller) with which this function is associated.
	 * @param targetVal The target value at the return site.
	 * @param function The edge function used to compute V-type values from the source node to the target node.  
	 */
	public synchronized void insertFunction(N callSite,D sourceVal, N retSite, D targetVal, EdgeFunction<V> function) {
		assert callSite!=null;
		assert sourceVal!=null;
		assert retSite!=null;
		assert targetVal!=null;
		assert function!=null;
		
		Table<N, D, EdgeFunction<V>> targetAndTargetValToFunction = table.get(callSite,sourceVal);
		if(targetAndTargetValToFunction==null) {
			targetAndTargetValToFunction = HashBasedTable.create();
			table.put(callSite,sourceVal,targetAndTargetValToFunction);
		}
		targetAndTargetValToFunction.put(retSite, targetVal, function);
	}

	/**
	 * Retrieves all summary functions for a given call site, source value and
	 * return site (in the caller).
	 * The result contains a mapping from target value to associated edge function.
	 */
	public synchronized Map<D,EdgeFunction<V>> summariesFor(N callSite, D sourceVal, N returnSite) {
		assert callSite!=null;
		assert sourceVal!=null;
		assert returnSite!=null;

		Table<N, D, EdgeFunction<V>> res = table.get(callSite,sourceVal);
		if(res==null) return Collections.emptyMap();
		else {
			return res.row(returnSite);
		}
	}
}










67675fc9e06d4d42e44726dcb8a4f9564b1f296b


Switch branch/tag










heros


src


heros


solver


SummaryFunctions.java



Find file
Normal viewHistoryPermalink




67675fc9e06d4d42e44726dcb8a4f9564b1f296b


Switch branch/tag










heros


src


heros


solver


SummaryFunctions.java





67675fc9e06d4d42e44726dcb8a4f9564b1f296b


Switch branch/tag








67675fc9e06d4d42e44726dcb8a4f9564b1f296b


Switch branch/tag





67675fc9e06d4d42e44726dcb8a4f9564b1f296b

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src

heros

solver

SummaryFunctions.java
Find file
Normal viewHistoryPermalink




SummaryFunctions.java



2.55 KB









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




package heros.solver;

import heros.EdgeFunction;
import heros.SynchronizedBy;
import heros.ThreadSafe;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






16




17




18




19




20




21




22




23





import java.util.Collections;
import java.util.Map;


import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






24














initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




/**
 * A data structure to record summary functions in an indexed fashion, for fast retrieval.
 */
@ThreadSafe
public class SummaryFunctions<N,D,V> {
	
	@SynchronizedBy("consistent lock on this")
	protected Table<N,D,Table<N,D,EdgeFunction<V>>> table = HashBasedTable.create();
	
	/**
	 * Inserts a summary function.
	 * @param callSite The call site with which this function is associated.
	 * @param sourceVal The source value at the call site. 
	 * @param retSite The return site (in the caller) with which this function is associated.
	 * @param targetVal The target value at the return site.
	 * @param function The edge function used to compute V-type values from the source node to the target node.  
	 */
	public synchronized void insertFunction(N callSite,D sourceVal, N retSite, D targetVal, EdgeFunction<V> function) {
		assert callSite!=null;
		assert sourceVal!=null;
		assert retSite!=null;
		assert targetVal!=null;
		assert function!=null;
		
		Table<N, D, EdgeFunction<V>> targetAndTargetValToFunction = table.get(callSite,sourceVal);
		if(targetAndTargetValToFunction==null) {
			targetAndTargetValToFunction = HashBasedTable.create();
			table.put(callSite,sourceVal,targetAndTargetValToFunction);
		}
		targetAndTargetValToFunction.put(retSite, targetVal, function);
	}

	/**
	 * Retrieves all summary functions for a given call site, source value and
	 * return site (in the caller).
	 * The result contains a mapping from target value to associated edge function.
	 */
	public synchronized Map<D,EdgeFunction<V>> summariesFor(N callSite, D sourceVal, N returnSite) {
		assert callSite!=null;
		assert sourceVal!=null;
		assert returnSite!=null;

		Table<N, D, EdgeFunction<V>> res = table.get(callSite,sourceVal);
		if(res==null) return Collections.emptyMap();
		else {
			return res.row(returnSite);
		}
	}
}








SummaryFunctions.java



2.55 KB










SummaryFunctions.java



2.55 KB









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




package heros.solver;

import heros.EdgeFunction;
import heros.SynchronizedBy;
import heros.ThreadSafe;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






16




17




18




19




20




21




22




23





import java.util.Collections;
import java.util.Map;


import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;










renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






24














initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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




/**
 * A data structure to record summary functions in an indexed fashion, for fast retrieval.
 */
@ThreadSafe
public class SummaryFunctions<N,D,V> {
	
	@SynchronizedBy("consistent lock on this")
	protected Table<N,D,Table<N,D,EdgeFunction<V>>> table = HashBasedTable.create();
	
	/**
	 * Inserts a summary function.
	 * @param callSite The call site with which this function is associated.
	 * @param sourceVal The source value at the call site. 
	 * @param retSite The return site (in the caller) with which this function is associated.
	 * @param targetVal The target value at the return site.
	 * @param function The edge function used to compute V-type values from the source node to the target node.  
	 */
	public synchronized void insertFunction(N callSite,D sourceVal, N retSite, D targetVal, EdgeFunction<V> function) {
		assert callSite!=null;
		assert sourceVal!=null;
		assert retSite!=null;
		assert targetVal!=null;
		assert function!=null;
		
		Table<N, D, EdgeFunction<V>> targetAndTargetValToFunction = table.get(callSite,sourceVal);
		if(targetAndTargetValToFunction==null) {
			targetAndTargetValToFunction = HashBasedTable.create();
			table.put(callSite,sourceVal,targetAndTargetValToFunction);
		}
		targetAndTargetValToFunction.put(retSite, targetVal, function);
	}

	/**
	 * Retrieves all summary functions for a given call site, source value and
	 * return site (in the caller).
	 * The result contains a mapping from target value to associated edge function.
	 */
	public synchronized Map<D,EdgeFunction<V>> summariesFor(N callSite, D sourceVal, N returnSite) {
		assert callSite!=null;
		assert sourceVal!=null;
		assert returnSite!=null;

		Table<N, D, EdgeFunction<V>> res = table.get(callSite,sourceVal);
		if(res==null) return Collections.emptyMap();
		else {
			return res.row(returnSite);
		}
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

12

13

14

15
package heros.solver;packageheros.solver;import heros.EdgeFunction;importheros.EdgeFunction;import heros.SynchronizedBy;importheros.SynchronizedBy;import heros.ThreadSafe;importheros.ThreadSafe;



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

16

17

18

19

20

21

22

23
import java.util.Collections;importjava.util.Collections;import java.util.Map;importjava.util.Map;import com.google.common.collect.HashBasedTable;importcom.google.common.collect.HashBasedTable;import com.google.common.collect.Table;importcom.google.common.collect.Table;



renamed package


 

 


Eric Bodden
committed
Nov 28, 2012



renamed package


 

 

renamed package

 

Eric Bodden
committed
Nov 28, 2012

24




initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

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
/**/** * A data structure to record summary functions in an indexed fashion, for fast retrieval. * A data structure to record summary functions in an indexed fashion, for fast retrieval. */ */@ThreadSafe@ThreadSafepublic class SummaryFunctions<N,D,V> {publicclassSummaryFunctions<N,D,V>{		@SynchronizedBy("consistent lock on this")@SynchronizedBy("consistent lock on this")	protected Table<N,D,Table<N,D,EdgeFunction<V>>> table = HashBasedTable.create();protectedTable<N,D,Table<N,D,EdgeFunction<V>>>table=HashBasedTable.create();		/**/**	 * Inserts a summary function.	 * Inserts a summary function.	 * @param callSite The call site with which this function is associated.	 * @param callSite The call site with which this function is associated.	 * @param sourceVal The source value at the call site. 	 * @param sourceVal The source value at the call site. 	 * @param retSite The return site (in the caller) with which this function is associated.	 * @param retSite The return site (in the caller) with which this function is associated.	 * @param targetVal The target value at the return site.	 * @param targetVal The target value at the return site.	 * @param function The edge function used to compute V-type values from the source node to the target node.  	 * @param function The edge function used to compute V-type values from the source node to the target node.  	 */	 */	public synchronized void insertFunction(N callSite,D sourceVal, N retSite, D targetVal, EdgeFunction<V> function) {publicsynchronizedvoidinsertFunction(NcallSite,DsourceVal,NretSite,DtargetVal,EdgeFunction<V>function){		assert callSite!=null;assertcallSite!=null;		assert sourceVal!=null;assertsourceVal!=null;		assert retSite!=null;assertretSite!=null;		assert targetVal!=null;asserttargetVal!=null;		assert function!=null;assertfunction!=null;				Table<N, D, EdgeFunction<V>> targetAndTargetValToFunction = table.get(callSite,sourceVal);Table<N,D,EdgeFunction<V>>targetAndTargetValToFunction=table.get(callSite,sourceVal);		if(targetAndTargetValToFunction==null) {if(targetAndTargetValToFunction==null){			targetAndTargetValToFunction = HashBasedTable.create();targetAndTargetValToFunction=HashBasedTable.create();			table.put(callSite,sourceVal,targetAndTargetValToFunction);table.put(callSite,sourceVal,targetAndTargetValToFunction);		}}		targetAndTargetValToFunction.put(retSite, targetVal, function);targetAndTargetValToFunction.put(retSite,targetVal,function);	}}	/**/**	 * Retrieves all summary functions for a given call site, source value and	 * Retrieves all summary functions for a given call site, source value and	 * return site (in the caller).	 * return site (in the caller).	 * The result contains a mapping from target value to associated edge function.	 * The result contains a mapping from target value to associated edge function.	 */	 */	public synchronized Map<D,EdgeFunction<V>> summariesFor(N callSite, D sourceVal, N returnSite) {publicsynchronizedMap<D,EdgeFunction<V>>summariesFor(NcallSite,DsourceVal,NreturnSite){		assert callSite!=null;assertcallSite!=null;		assert sourceVal!=null;assertsourceVal!=null;		assert returnSite!=null;assertreturnSite!=null;		Table<N, D, EdgeFunction<V>> res = table.get(callSite,sourceVal);Table<N,D,EdgeFunction<V>>res=table.get(callSite,sourceVal);		if(res==null) return Collections.emptyMap();if(res==null)returnCollections.emptyMap();		else {else{			return res.row(returnSite);returnres.row(returnSite);		}}	}}}}





