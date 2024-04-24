



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

f0141ddea32ad29669156ca5a894f82ab9ecb000

















f0141ddea32ad29669156ca5a894f82ab9ecb000


Switch branch/tag










heros


src


heros


solver


CountingThreadPoolExecutor.java



Find file
Normal viewHistoryPermalink






CountingThreadPoolExecutor.java



2.01 KB









Newer










Older









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






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
package heros.solver;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A {@link ThreadPoolExecutor} which keeps track of the number of spawned
 * tasks to allow clients to await their completion. 
 */
public class CountingThreadPoolExecutor extends ThreadPoolExecutor {
	
	protected final CountLatch numRunningTasks = new CountLatch(0);









added exception reporting for tasks in IDESolver


 

 


Eric Bodden
committed
Jun 27, 2013






24




	









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






25




	protected Throwable exception = null;









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






26




27




28




29




30




31




32





	public CountingThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	@Override









bugfix: must increment counter on task submission, not when task starts executing


 

 


Eric Bodden
committed
Jan 28, 2013






33




	public void execute(Runnable command) {









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






34




		numRunningTasks.increment();









bugfix: must increment counter on task submission, not when task starts executing


 

 


Eric Bodden
committed
Jan 28, 2013






35




		super.execute(command);









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






36




	}









bugfix: must increment counter on task submission, not when task starts executing


 

 


Eric Bodden
committed
Jan 28, 2013






37




	









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






38




39




40




	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		numRunningTasks.decrement();









shutdown now


 

 


Eric Bodden
committed
Jul 10, 2013






41




		if(t!=null) {









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






42




			exception = t;









shutdown now


 

 


Eric Bodden
committed
Jul 10, 2013






43




			shutdownNow();









Added counter resetting and thread interruption to fix #3


 

 


Marc-André Laverdière
committed
Sep 06, 2013






44




            numRunningTasks.resetAndInterrupt();









shutdown now


 

 


Eric Bodden
committed
Jul 10, 2013






45




		}









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






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




		super.afterExecute(r, t);
	}

	/**
	 * Awaits the completion of all spawned tasks.
	 */
	public void awaitCompletion() throws InterruptedException {
		numRunningTasks.awaitZero();
	}
	
	/**
	 * Awaits the completion of all spawned tasks.
	 */
	public void awaitCompletion(long timeout, TimeUnit unit) throws InterruptedException {
		numRunningTasks.awaitZero(timeout, unit);
	}









added exception reporting for tasks in IDESolver


 

 


Eric Bodden
committed
Jun 27, 2013






62




63




	
	/**









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






64




	 * Returns the exception thrown during task execution (if any).









added exception reporting for tasks in IDESolver


 

 


Eric Bodden
committed
Jun 27, 2013






65




	 */









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






66




67




	public Throwable getException() {
		return exception;









added exception reporting for tasks in IDESolver


 

 


Eric Bodden
committed
Jun 27, 2013






68




	}









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






69




70





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

f0141ddea32ad29669156ca5a894f82ab9ecb000

















f0141ddea32ad29669156ca5a894f82ab9ecb000


Switch branch/tag










heros


src


heros


solver


CountingThreadPoolExecutor.java



Find file
Normal viewHistoryPermalink






CountingThreadPoolExecutor.java



2.01 KB









Newer










Older









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






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
package heros.solver;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A {@link ThreadPoolExecutor} which keeps track of the number of spawned
 * tasks to allow clients to await their completion. 
 */
public class CountingThreadPoolExecutor extends ThreadPoolExecutor {
	
	protected final CountLatch numRunningTasks = new CountLatch(0);









added exception reporting for tasks in IDESolver


 

 


Eric Bodden
committed
Jun 27, 2013






24




	









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






25




	protected Throwable exception = null;









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






26




27




28




29




30




31




32





	public CountingThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	@Override









bugfix: must increment counter on task submission, not when task starts executing


 

 


Eric Bodden
committed
Jan 28, 2013






33




	public void execute(Runnable command) {









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






34




		numRunningTasks.increment();









bugfix: must increment counter on task submission, not when task starts executing


 

 


Eric Bodden
committed
Jan 28, 2013






35




		super.execute(command);









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






36




	}









bugfix: must increment counter on task submission, not when task starts executing


 

 


Eric Bodden
committed
Jan 28, 2013






37




	









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






38




39




40




	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		numRunningTasks.decrement();









shutdown now


 

 


Eric Bodden
committed
Jul 10, 2013






41




		if(t!=null) {









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






42




			exception = t;









shutdown now


 

 


Eric Bodden
committed
Jul 10, 2013






43




			shutdownNow();









Added counter resetting and thread interruption to fix #3


 

 


Marc-André Laverdière
committed
Sep 06, 2013






44




            numRunningTasks.resetAndInterrupt();









shutdown now


 

 


Eric Bodden
committed
Jul 10, 2013






45




		}









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






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




		super.afterExecute(r, t);
	}

	/**
	 * Awaits the completion of all spawned tasks.
	 */
	public void awaitCompletion() throws InterruptedException {
		numRunningTasks.awaitZero();
	}
	
	/**
	 * Awaits the completion of all spawned tasks.
	 */
	public void awaitCompletion(long timeout, TimeUnit unit) throws InterruptedException {
		numRunningTasks.awaitZero(timeout, unit);
	}









added exception reporting for tasks in IDESolver


 

 


Eric Bodden
committed
Jun 27, 2013






62




63




	
	/**









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






64




	 * Returns the exception thrown during task execution (if any).









added exception reporting for tasks in IDESolver


 

 


Eric Bodden
committed
Jun 27, 2013






65




	 */









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






66




67




	public Throwable getException() {
		return exception;









added exception reporting for tasks in IDESolver


 

 


Eric Bodden
committed
Jun 27, 2013






68




	}









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






69




70





}











Open sidebar



Joshua Garcia heros

f0141ddea32ad29669156ca5a894f82ab9ecb000







Open sidebar



Joshua Garcia heros

f0141ddea32ad29669156ca5a894f82ab9ecb000




Open sidebar

Joshua Garcia heros

f0141ddea32ad29669156ca5a894f82ab9ecb000


Joshua Garciaherosheros
f0141ddea32ad29669156ca5a894f82ab9ecb000










f0141ddea32ad29669156ca5a894f82ab9ecb000


Switch branch/tag










heros


src


heros


solver


CountingThreadPoolExecutor.java



Find file
Normal viewHistoryPermalink






CountingThreadPoolExecutor.java



2.01 KB









Newer










Older









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






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
package heros.solver;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A {@link ThreadPoolExecutor} which keeps track of the number of spawned
 * tasks to allow clients to await their completion. 
 */
public class CountingThreadPoolExecutor extends ThreadPoolExecutor {
	
	protected final CountLatch numRunningTasks = new CountLatch(0);









added exception reporting for tasks in IDESolver


 

 


Eric Bodden
committed
Jun 27, 2013






24




	









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






25




	protected Throwable exception = null;









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






26




27




28




29




30




31




32





	public CountingThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	@Override









bugfix: must increment counter on task submission, not when task starts executing


 

 


Eric Bodden
committed
Jan 28, 2013






33




	public void execute(Runnable command) {









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






34




		numRunningTasks.increment();









bugfix: must increment counter on task submission, not when task starts executing


 

 


Eric Bodden
committed
Jan 28, 2013






35




		super.execute(command);









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






36




	}









bugfix: must increment counter on task submission, not when task starts executing


 

 


Eric Bodden
committed
Jan 28, 2013






37




	









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






38




39




40




	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		numRunningTasks.decrement();









shutdown now


 

 


Eric Bodden
committed
Jul 10, 2013






41




		if(t!=null) {









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






42




			exception = t;









shutdown now


 

 


Eric Bodden
committed
Jul 10, 2013






43




			shutdownNow();









Added counter resetting and thread interruption to fix #3


 

 


Marc-André Laverdière
committed
Sep 06, 2013






44




            numRunningTasks.resetAndInterrupt();









shutdown now


 

 


Eric Bodden
committed
Jul 10, 2013






45




		}









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






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




		super.afterExecute(r, t);
	}

	/**
	 * Awaits the completion of all spawned tasks.
	 */
	public void awaitCompletion() throws InterruptedException {
		numRunningTasks.awaitZero();
	}
	
	/**
	 * Awaits the completion of all spawned tasks.
	 */
	public void awaitCompletion(long timeout, TimeUnit unit) throws InterruptedException {
		numRunningTasks.awaitZero(timeout, unit);
	}









added exception reporting for tasks in IDESolver


 

 


Eric Bodden
committed
Jun 27, 2013






62




63




	
	/**









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






64




	 * Returns the exception thrown during task execution (if any).









added exception reporting for tasks in IDESolver


 

 


Eric Bodden
committed
Jun 27, 2013






65




	 */









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






66




67




	public Throwable getException() {
		return exception;









added exception reporting for tasks in IDESolver


 

 


Eric Bodden
committed
Jun 27, 2013






68




	}









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






69




70





}














f0141ddea32ad29669156ca5a894f82ab9ecb000


Switch branch/tag










heros


src


heros


solver


CountingThreadPoolExecutor.java



Find file
Normal viewHistoryPermalink






CountingThreadPoolExecutor.java



2.01 KB









Newer










Older









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






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
package heros.solver;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A {@link ThreadPoolExecutor} which keeps track of the number of spawned
 * tasks to allow clients to await their completion. 
 */
public class CountingThreadPoolExecutor extends ThreadPoolExecutor {
	
	protected final CountLatch numRunningTasks = new CountLatch(0);









added exception reporting for tasks in IDESolver


 

 


Eric Bodden
committed
Jun 27, 2013






24




	









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






25




	protected Throwable exception = null;









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






26




27




28




29




30




31




32





	public CountingThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	@Override









bugfix: must increment counter on task submission, not when task starts executing


 

 


Eric Bodden
committed
Jan 28, 2013






33




	public void execute(Runnable command) {









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






34




		numRunningTasks.increment();









bugfix: must increment counter on task submission, not when task starts executing


 

 


Eric Bodden
committed
Jan 28, 2013






35




		super.execute(command);









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






36




	}









bugfix: must increment counter on task submission, not when task starts executing


 

 


Eric Bodden
committed
Jan 28, 2013






37




	









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






38




39




40




	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		numRunningTasks.decrement();









shutdown now


 

 


Eric Bodden
committed
Jul 10, 2013






41




		if(t!=null) {









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






42




			exception = t;









shutdown now


 

 


Eric Bodden
committed
Jul 10, 2013






43




			shutdownNow();









Added counter resetting and thread interruption to fix #3


 

 


Marc-André Laverdière
committed
Sep 06, 2013






44




            numRunningTasks.resetAndInterrupt();









shutdown now


 

 


Eric Bodden
committed
Jul 10, 2013






45




		}









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






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




		super.afterExecute(r, t);
	}

	/**
	 * Awaits the completion of all spawned tasks.
	 */
	public void awaitCompletion() throws InterruptedException {
		numRunningTasks.awaitZero();
	}
	
	/**
	 * Awaits the completion of all spawned tasks.
	 */
	public void awaitCompletion(long timeout, TimeUnit unit) throws InterruptedException {
		numRunningTasks.awaitZero(timeout, unit);
	}









added exception reporting for tasks in IDESolver


 

 


Eric Bodden
committed
Jun 27, 2013






62




63




	
	/**









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






64




	 * Returns the exception thrown during task execution (if any).









added exception reporting for tasks in IDESolver


 

 


Eric Bodden
committed
Jun 27, 2013






65




	 */









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






66




67




	public Throwable getException() {
		return exception;









added exception reporting for tasks in IDESolver


 

 


Eric Bodden
committed
Jun 27, 2013






68




	}









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






69




70





}










f0141ddea32ad29669156ca5a894f82ab9ecb000


Switch branch/tag










heros


src


heros


solver


CountingThreadPoolExecutor.java



Find file
Normal viewHistoryPermalink




f0141ddea32ad29669156ca5a894f82ab9ecb000


Switch branch/tag










heros


src


heros


solver


CountingThreadPoolExecutor.java





f0141ddea32ad29669156ca5a894f82ab9ecb000


Switch branch/tag








f0141ddea32ad29669156ca5a894f82ab9ecb000


Switch branch/tag





f0141ddea32ad29669156ca5a894f82ab9ecb000

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src

heros

solver

CountingThreadPoolExecutor.java
Find file
Normal viewHistoryPermalink




CountingThreadPoolExecutor.java



2.01 KB









Newer










Older









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






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
package heros.solver;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A {@link ThreadPoolExecutor} which keeps track of the number of spawned
 * tasks to allow clients to await their completion. 
 */
public class CountingThreadPoolExecutor extends ThreadPoolExecutor {
	
	protected final CountLatch numRunningTasks = new CountLatch(0);









added exception reporting for tasks in IDESolver


 

 


Eric Bodden
committed
Jun 27, 2013






24




	









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






25




	protected Throwable exception = null;









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






26




27




28




29




30




31




32





	public CountingThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	@Override









bugfix: must increment counter on task submission, not when task starts executing


 

 


Eric Bodden
committed
Jan 28, 2013






33




	public void execute(Runnable command) {









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






34




		numRunningTasks.increment();









bugfix: must increment counter on task submission, not when task starts executing


 

 


Eric Bodden
committed
Jan 28, 2013






35




		super.execute(command);









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






36




	}









bugfix: must increment counter on task submission, not when task starts executing


 

 


Eric Bodden
committed
Jan 28, 2013






37




	









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






38




39




40




	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		numRunningTasks.decrement();









shutdown now


 

 


Eric Bodden
committed
Jul 10, 2013






41




		if(t!=null) {









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






42




			exception = t;









shutdown now


 

 


Eric Bodden
committed
Jul 10, 2013






43




			shutdownNow();









Added counter resetting and thread interruption to fix #3


 

 


Marc-André Laverdière
committed
Sep 06, 2013






44




            numRunningTasks.resetAndInterrupt();









shutdown now


 

 


Eric Bodden
committed
Jul 10, 2013






45




		}









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






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




		super.afterExecute(r, t);
	}

	/**
	 * Awaits the completion of all spawned tasks.
	 */
	public void awaitCompletion() throws InterruptedException {
		numRunningTasks.awaitZero();
	}
	
	/**
	 * Awaits the completion of all spawned tasks.
	 */
	public void awaitCompletion(long timeout, TimeUnit unit) throws InterruptedException {
		numRunningTasks.awaitZero(timeout, unit);
	}









added exception reporting for tasks in IDESolver


 

 


Eric Bodden
committed
Jun 27, 2013






62




63




	
	/**









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






64




	 * Returns the exception thrown during task execution (if any).









added exception reporting for tasks in IDESolver


 

 


Eric Bodden
committed
Jun 27, 2013






65




	 */









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






66




67




	public Throwable getException() {
		return exception;









added exception reporting for tasks in IDESolver


 

 


Eric Bodden
committed
Jun 27, 2013






68




	}









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






69




70





}








CountingThreadPoolExecutor.java



2.01 KB










CountingThreadPoolExecutor.java



2.01 KB









Newer










Older
NewerOlder







further cleanups



 


Eric Bodden
committed
Jan 26, 2013






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
package heros.solver;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A {@link ThreadPoolExecutor} which keeps track of the number of spawned
 * tasks to allow clients to await their completion. 
 */
public class CountingThreadPoolExecutor extends ThreadPoolExecutor {
	
	protected final CountLatch numRunningTasks = new CountLatch(0);









added exception reporting for tasks in IDESolver


 

 


Eric Bodden
committed
Jun 27, 2013






24




	









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






25




	protected Throwable exception = null;









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






26




27




28




29




30




31




32





	public CountingThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	@Override









bugfix: must increment counter on task submission, not when task starts executing


 

 


Eric Bodden
committed
Jan 28, 2013






33




	public void execute(Runnable command) {









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






34




		numRunningTasks.increment();









bugfix: must increment counter on task submission, not when task starts executing


 

 


Eric Bodden
committed
Jan 28, 2013






35




		super.execute(command);









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






36




	}









bugfix: must increment counter on task submission, not when task starts executing


 

 


Eric Bodden
committed
Jan 28, 2013






37




	









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






38




39




40




	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		numRunningTasks.decrement();









shutdown now


 

 


Eric Bodden
committed
Jul 10, 2013






41




		if(t!=null) {









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






42




			exception = t;









shutdown now


 

 


Eric Bodden
committed
Jul 10, 2013






43




			shutdownNow();









Added counter resetting and thread interruption to fix #3


 

 


Marc-André Laverdière
committed
Sep 06, 2013






44




            numRunningTasks.resetAndInterrupt();









shutdown now


 

 


Eric Bodden
committed
Jul 10, 2013






45




		}









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






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




		super.afterExecute(r, t);
	}

	/**
	 * Awaits the completion of all spawned tasks.
	 */
	public void awaitCompletion() throws InterruptedException {
		numRunningTasks.awaitZero();
	}
	
	/**
	 * Awaits the completion of all spawned tasks.
	 */
	public void awaitCompletion(long timeout, TimeUnit unit) throws InterruptedException {
		numRunningTasks.awaitZero(timeout, unit);
	}









added exception reporting for tasks in IDESolver


 

 


Eric Bodden
committed
Jun 27, 2013






62




63




	
	/**









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






64




	 * Returns the exception thrown during task execution (if any).









added exception reporting for tasks in IDESolver


 

 


Eric Bodden
committed
Jun 27, 2013






65




	 */









simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013






66




67




	public Throwable getException() {
		return exception;









added exception reporting for tasks in IDESolver


 

 


Eric Bodden
committed
Jun 27, 2013






68




	}









further cleanups



 


Eric Bodden
committed
Jan 26, 2013






69




70





}







further cleanups



 


Eric Bodden
committed
Jan 26, 2013



further cleanups



 

further cleanups


Eric Bodden
committed
Jan 26, 2013

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
/*******************************************************************************/******************************************************************************* * Copyright (c) 2012 Eric Bodden. * Copyright (c) 2012 Eric Bodden. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Eric Bodden - initial API and implementation *     Eric Bodden - initial API and implementation ******************************************************************************/ ******************************************************************************/package heros.solver;packageheros.solver;import java.util.concurrent.BlockingQueue;importjava.util.concurrent.BlockingQueue;import java.util.concurrent.ThreadPoolExecutor;importjava.util.concurrent.ThreadPoolExecutor;import java.util.concurrent.TimeUnit;importjava.util.concurrent.TimeUnit;/**/** * A {@link ThreadPoolExecutor} which keeps track of the number of spawned * A {@link ThreadPoolExecutor} which keeps track of the number of spawned * tasks to allow clients to await their completion.  * tasks to allow clients to await their completion.  */ */public class CountingThreadPoolExecutor extends ThreadPoolExecutor {publicclassCountingThreadPoolExecutorextendsThreadPoolExecutor{		protected final CountLatch numRunningTasks = new CountLatch(0);protectedfinalCountLatchnumRunningTasks=newCountLatch(0);



added exception reporting for tasks in IDESolver


 

 


Eric Bodden
committed
Jun 27, 2013



added exception reporting for tasks in IDESolver


 

 

added exception reporting for tasks in IDESolver

 

Eric Bodden
committed
Jun 27, 2013

24
	



simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013



simplified exception handling


 

 

simplified exception handling

 

Eric Bodden
committed
Jul 11, 2013

25
	protected Throwable exception = null;protectedThrowableexception=null;



further cleanups



 


Eric Bodden
committed
Jan 26, 2013



further cleanups



 

further cleanups


Eric Bodden
committed
Jan 26, 2013

26

27

28

29

30

31

32
	public CountingThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,publicCountingThreadPoolExecutor(intcorePoolSize,intmaximumPoolSize,longkeepAliveTime,TimeUnitunit,			BlockingQueue<Runnable> workQueue) {BlockingQueue<Runnable>workQueue){		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);super(corePoolSize,maximumPoolSize,keepAliveTime,unit,workQueue);	}}	@Override@Override



bugfix: must increment counter on task submission, not when task starts executing


 

 


Eric Bodden
committed
Jan 28, 2013



bugfix: must increment counter on task submission, not when task starts executing


 

 

bugfix: must increment counter on task submission, not when task starts executing

 

Eric Bodden
committed
Jan 28, 2013

33
	public void execute(Runnable command) {publicvoidexecute(Runnablecommand){



further cleanups



 


Eric Bodden
committed
Jan 26, 2013



further cleanups



 

further cleanups


Eric Bodden
committed
Jan 26, 2013

34
		numRunningTasks.increment();numRunningTasks.increment();



bugfix: must increment counter on task submission, not when task starts executing


 

 


Eric Bodden
committed
Jan 28, 2013



bugfix: must increment counter on task submission, not when task starts executing


 

 

bugfix: must increment counter on task submission, not when task starts executing

 

Eric Bodden
committed
Jan 28, 2013

35
		super.execute(command);super.execute(command);



further cleanups



 


Eric Bodden
committed
Jan 26, 2013



further cleanups



 

further cleanups


Eric Bodden
committed
Jan 26, 2013

36
	}}



bugfix: must increment counter on task submission, not when task starts executing


 

 


Eric Bodden
committed
Jan 28, 2013



bugfix: must increment counter on task submission, not when task starts executing


 

 

bugfix: must increment counter on task submission, not when task starts executing

 

Eric Bodden
committed
Jan 28, 2013

37
	



further cleanups



 


Eric Bodden
committed
Jan 26, 2013



further cleanups



 

further cleanups


Eric Bodden
committed
Jan 26, 2013

38

39

40
	@Override@Override	protected void afterExecute(Runnable r, Throwable t) {protectedvoidafterExecute(Runnabler,Throwablet){		numRunningTasks.decrement();numRunningTasks.decrement();



shutdown now


 

 


Eric Bodden
committed
Jul 10, 2013



shutdown now


 

 

shutdown now

 

Eric Bodden
committed
Jul 10, 2013

41
		if(t!=null) {if(t!=null){



simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013



simplified exception handling


 

 

simplified exception handling

 

Eric Bodden
committed
Jul 11, 2013

42
			exception = t;exception=t;



shutdown now


 

 


Eric Bodden
committed
Jul 10, 2013



shutdown now


 

 

shutdown now

 

Eric Bodden
committed
Jul 10, 2013

43
			shutdownNow();shutdownNow();



Added counter resetting and thread interruption to fix #3


 

 


Marc-André Laverdière
committed
Sep 06, 2013



Added counter resetting and thread interruption to fix #3


 

 

Added counter resetting and thread interruption to fix #3

 

Marc-André Laverdière
committed
Sep 06, 2013

44
            numRunningTasks.resetAndInterrupt();numRunningTasks.resetAndInterrupt();



shutdown now


 

 


Eric Bodden
committed
Jul 10, 2013



shutdown now


 

 

shutdown now

 

Eric Bodden
committed
Jul 10, 2013

45
		}}



further cleanups



 


Eric Bodden
committed
Jan 26, 2013



further cleanups



 

further cleanups


Eric Bodden
committed
Jan 26, 2013

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
		super.afterExecute(r, t);super.afterExecute(r,t);	}}	/**/**	 * Awaits the completion of all spawned tasks.	 * Awaits the completion of all spawned tasks.	 */	 */	public void awaitCompletion() throws InterruptedException {publicvoidawaitCompletion()throwsInterruptedException{		numRunningTasks.awaitZero();numRunningTasks.awaitZero();	}}		/**/**	 * Awaits the completion of all spawned tasks.	 * Awaits the completion of all spawned tasks.	 */	 */	public void awaitCompletion(long timeout, TimeUnit unit) throws InterruptedException {publicvoidawaitCompletion(longtimeout,TimeUnitunit)throwsInterruptedException{		numRunningTasks.awaitZero(timeout, unit);numRunningTasks.awaitZero(timeout,unit);	}}



added exception reporting for tasks in IDESolver


 

 


Eric Bodden
committed
Jun 27, 2013



added exception reporting for tasks in IDESolver


 

 

added exception reporting for tasks in IDESolver

 

Eric Bodden
committed
Jun 27, 2013

62

63
		/**/**



simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013



simplified exception handling


 

 

simplified exception handling

 

Eric Bodden
committed
Jul 11, 2013

64
	 * Returns the exception thrown during task execution (if any).	 * Returns the exception thrown during task execution (if any).



added exception reporting for tasks in IDESolver


 

 


Eric Bodden
committed
Jun 27, 2013



added exception reporting for tasks in IDESolver


 

 

added exception reporting for tasks in IDESolver

 

Eric Bodden
committed
Jun 27, 2013

65
	 */	 */



simplified exception handling


 

 


Eric Bodden
committed
Jul 11, 2013



simplified exception handling


 

 

simplified exception handling

 

Eric Bodden
committed
Jul 11, 2013

66

67
	public Throwable getException() {publicThrowablegetException(){		return exception;returnexception;



added exception reporting for tasks in IDESolver


 

 


Eric Bodden
committed
Jun 27, 2013



added exception reporting for tasks in IDESolver


 

 

added exception reporting for tasks in IDESolver

 

Eric Bodden
committed
Jun 27, 2013

68
	}}



further cleanups



 


Eric Bodden
committed
Jan 26, 2013



further cleanups



 

further cleanups


Eric Bodden
committed
Jan 26, 2013

69

70
}}





