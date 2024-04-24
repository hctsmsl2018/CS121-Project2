



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

b90d93333c0ac60f93cdbe88bca6cd88e8070f03

















b90d93333c0ac60f93cdbe88bca6cd88e8070f03


Switch branch/tag










heros


src


heros


solver


CountingThreadPoolExecutor.java



Find file
Normal viewHistoryPermalink






CountingThreadPoolExecutor.java



2.44 KB









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








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




14



import java.util.concurrent.RejectedExecutionException;








further cleanups



Eric Bodden
committed
Jan 26, 2013




15


16


17



import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;









added logging for execution errors in worker threads

 


Steven Arzt
committed
Oct 26, 2013




18


19


20



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;









further cleanups



Eric Bodden
committed
Jan 26, 2013




21


22


23


24


25


26



/**
 * A {@link ThreadPoolExecutor} which keeps track of the number of spawned
 * tasks to allow clients to await their completion. 
 */
public class CountingThreadPoolExecutor extends ThreadPoolExecutor {
	








added logging for execution errors in worker threads

 


Steven Arzt
committed
Oct 26, 2013




27


28


29



    protected static final Logger logger = LoggerFactory.getLogger(IDESolver.class);

    protected final CountLatch numRunningTasks = new CountLatch(0);








added exception reporting for tasks in IDESolver

 


Eric Bodden
committed
Jun 27, 2013




30



	








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




31



	protected Throwable exception = null;








further cleanups



Eric Bodden
committed
Jan 26, 2013




32


33


34


35


36


37


38




	public CountingThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	@Override








bugfix: must increment counter on task submission, not when task starts executing

 


Eric Bodden
committed
Jan 28, 2013




39



	public void execute(Runnable command) {








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




40


41


42


43


44


45


46


47



		try {
			numRunningTasks.increment();
			super.execute(command);
		}
		catch (RejectedExecutionException ex) {
			// If we were unable to submit the task, we may not count it!
			numRunningTasks.decrement();
		}








further cleanups



Eric Bodden
committed
Jan 26, 2013




48



	}








bugfix: must increment counter on task submission, not when task starts executing

 


Eric Bodden
committed
Jan 28, 2013




49



	








further cleanups



Eric Bodden
committed
Jan 26, 2013




50


51


52



	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		numRunningTasks.decrement();








shutdown now

 


Eric Bodden
committed
Jul 10, 2013




53



		if(t!=null) {








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




54



			exception = t;








added logging for execution errors in worker threads

 


Steven Arzt
committed
Oct 26, 2013




55


56



			logger.error("Worker thread execution failed: " + t.getMessage(), t);
			








shutdown now

 


Eric Bodden
committed
Jul 10, 2013




57



			shutdownNow();








Added counter resetting and thread interruption to fix #3

 


Marc-André Laverdière
committed
Sep 06, 2013




58



            numRunningTasks.resetAndInterrupt();








shutdown now

 


Eric Bodden
committed
Jul 10, 2013




59



		}








further cleanups



Eric Bodden
committed
Jan 26, 2013




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




76


77



	
	/**








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




78



	 * Returns the exception thrown during task execution (if any).








added exception reporting for tasks in IDESolver

 


Eric Bodden
committed
Jun 27, 2013




79



	 */








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




80


81



	public Throwable getException() {
		return exception;








added exception reporting for tasks in IDESolver

 


Eric Bodden
committed
Jun 27, 2013




82



	}








further cleanups



Eric Bodden
committed
Jan 26, 2013




83


84




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

b90d93333c0ac60f93cdbe88bca6cd88e8070f03

















b90d93333c0ac60f93cdbe88bca6cd88e8070f03


Switch branch/tag










heros


src


heros


solver


CountingThreadPoolExecutor.java



Find file
Normal viewHistoryPermalink






CountingThreadPoolExecutor.java



2.44 KB









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








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




14



import java.util.concurrent.RejectedExecutionException;








further cleanups



Eric Bodden
committed
Jan 26, 2013




15


16


17



import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;









added logging for execution errors in worker threads

 


Steven Arzt
committed
Oct 26, 2013




18


19


20



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;









further cleanups



Eric Bodden
committed
Jan 26, 2013




21


22


23


24


25


26



/**
 * A {@link ThreadPoolExecutor} which keeps track of the number of spawned
 * tasks to allow clients to await their completion. 
 */
public class CountingThreadPoolExecutor extends ThreadPoolExecutor {
	








added logging for execution errors in worker threads

 


Steven Arzt
committed
Oct 26, 2013




27


28


29



    protected static final Logger logger = LoggerFactory.getLogger(IDESolver.class);

    protected final CountLatch numRunningTasks = new CountLatch(0);








added exception reporting for tasks in IDESolver

 


Eric Bodden
committed
Jun 27, 2013




30



	








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




31



	protected Throwable exception = null;








further cleanups



Eric Bodden
committed
Jan 26, 2013




32


33


34


35


36


37


38




	public CountingThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	@Override








bugfix: must increment counter on task submission, not when task starts executing

 


Eric Bodden
committed
Jan 28, 2013




39



	public void execute(Runnable command) {








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




40


41


42


43


44


45


46


47



		try {
			numRunningTasks.increment();
			super.execute(command);
		}
		catch (RejectedExecutionException ex) {
			// If we were unable to submit the task, we may not count it!
			numRunningTasks.decrement();
		}








further cleanups



Eric Bodden
committed
Jan 26, 2013




48



	}








bugfix: must increment counter on task submission, not when task starts executing

 


Eric Bodden
committed
Jan 28, 2013




49



	








further cleanups



Eric Bodden
committed
Jan 26, 2013




50


51


52



	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		numRunningTasks.decrement();








shutdown now

 


Eric Bodden
committed
Jul 10, 2013




53



		if(t!=null) {








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




54



			exception = t;








added logging for execution errors in worker threads

 


Steven Arzt
committed
Oct 26, 2013




55


56



			logger.error("Worker thread execution failed: " + t.getMessage(), t);
			








shutdown now

 


Eric Bodden
committed
Jul 10, 2013




57



			shutdownNow();








Added counter resetting and thread interruption to fix #3

 


Marc-André Laverdière
committed
Sep 06, 2013




58



            numRunningTasks.resetAndInterrupt();








shutdown now

 


Eric Bodden
committed
Jul 10, 2013




59



		}








further cleanups



Eric Bodden
committed
Jan 26, 2013




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




76


77



	
	/**








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




78



	 * Returns the exception thrown during task execution (if any).








added exception reporting for tasks in IDESolver

 


Eric Bodden
committed
Jun 27, 2013




79



	 */








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




80


81



	public Throwable getException() {
		return exception;








added exception reporting for tasks in IDESolver

 


Eric Bodden
committed
Jun 27, 2013




82



	}








further cleanups



Eric Bodden
committed
Jan 26, 2013




83


84




}












Open sidebar



Joshua Garcia heros

b90d93333c0ac60f93cdbe88bca6cd88e8070f03







Open sidebar



Joshua Garcia heros

b90d93333c0ac60f93cdbe88bca6cd88e8070f03




Open sidebar

Joshua Garcia heros

b90d93333c0ac60f93cdbe88bca6cd88e8070f03


Joshua Garciaherosheros
b90d93333c0ac60f93cdbe88bca6cd88e8070f03










b90d93333c0ac60f93cdbe88bca6cd88e8070f03


Switch branch/tag










heros


src


heros


solver


CountingThreadPoolExecutor.java



Find file
Normal viewHistoryPermalink






CountingThreadPoolExecutor.java



2.44 KB









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








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




14



import java.util.concurrent.RejectedExecutionException;








further cleanups



Eric Bodden
committed
Jan 26, 2013




15


16


17



import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;









added logging for execution errors in worker threads

 


Steven Arzt
committed
Oct 26, 2013




18


19


20



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;









further cleanups



Eric Bodden
committed
Jan 26, 2013




21


22


23


24


25


26



/**
 * A {@link ThreadPoolExecutor} which keeps track of the number of spawned
 * tasks to allow clients to await their completion. 
 */
public class CountingThreadPoolExecutor extends ThreadPoolExecutor {
	








added logging for execution errors in worker threads

 


Steven Arzt
committed
Oct 26, 2013




27


28


29



    protected static final Logger logger = LoggerFactory.getLogger(IDESolver.class);

    protected final CountLatch numRunningTasks = new CountLatch(0);








added exception reporting for tasks in IDESolver

 


Eric Bodden
committed
Jun 27, 2013




30



	








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




31



	protected Throwable exception = null;








further cleanups



Eric Bodden
committed
Jan 26, 2013




32


33


34


35


36


37


38




	public CountingThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	@Override








bugfix: must increment counter on task submission, not when task starts executing

 


Eric Bodden
committed
Jan 28, 2013




39



	public void execute(Runnable command) {








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




40


41


42


43


44


45


46


47



		try {
			numRunningTasks.increment();
			super.execute(command);
		}
		catch (RejectedExecutionException ex) {
			// If we were unable to submit the task, we may not count it!
			numRunningTasks.decrement();
		}








further cleanups



Eric Bodden
committed
Jan 26, 2013




48



	}








bugfix: must increment counter on task submission, not when task starts executing

 


Eric Bodden
committed
Jan 28, 2013




49



	








further cleanups



Eric Bodden
committed
Jan 26, 2013




50


51


52



	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		numRunningTasks.decrement();








shutdown now

 


Eric Bodden
committed
Jul 10, 2013




53



		if(t!=null) {








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




54



			exception = t;








added logging for execution errors in worker threads

 


Steven Arzt
committed
Oct 26, 2013




55


56



			logger.error("Worker thread execution failed: " + t.getMessage(), t);
			








shutdown now

 


Eric Bodden
committed
Jul 10, 2013




57



			shutdownNow();








Added counter resetting and thread interruption to fix #3

 


Marc-André Laverdière
committed
Sep 06, 2013




58



            numRunningTasks.resetAndInterrupt();








shutdown now

 


Eric Bodden
committed
Jul 10, 2013




59



		}








further cleanups



Eric Bodden
committed
Jan 26, 2013




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




76


77



	
	/**








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




78



	 * Returns the exception thrown during task execution (if any).








added exception reporting for tasks in IDESolver

 


Eric Bodden
committed
Jun 27, 2013




79



	 */








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




80


81



	public Throwable getException() {
		return exception;








added exception reporting for tasks in IDESolver

 


Eric Bodden
committed
Jun 27, 2013




82



	}








further cleanups



Eric Bodden
committed
Jan 26, 2013




83


84




}















b90d93333c0ac60f93cdbe88bca6cd88e8070f03


Switch branch/tag










heros


src


heros


solver


CountingThreadPoolExecutor.java



Find file
Normal viewHistoryPermalink






CountingThreadPoolExecutor.java



2.44 KB









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








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




14



import java.util.concurrent.RejectedExecutionException;








further cleanups



Eric Bodden
committed
Jan 26, 2013




15


16


17



import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;









added logging for execution errors in worker threads

 


Steven Arzt
committed
Oct 26, 2013




18


19


20



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;









further cleanups



Eric Bodden
committed
Jan 26, 2013




21


22


23


24


25


26



/**
 * A {@link ThreadPoolExecutor} which keeps track of the number of spawned
 * tasks to allow clients to await their completion. 
 */
public class CountingThreadPoolExecutor extends ThreadPoolExecutor {
	








added logging for execution errors in worker threads

 


Steven Arzt
committed
Oct 26, 2013




27


28


29



    protected static final Logger logger = LoggerFactory.getLogger(IDESolver.class);

    protected final CountLatch numRunningTasks = new CountLatch(0);








added exception reporting for tasks in IDESolver

 


Eric Bodden
committed
Jun 27, 2013




30



	








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




31



	protected Throwable exception = null;








further cleanups



Eric Bodden
committed
Jan 26, 2013




32


33


34


35


36


37


38




	public CountingThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	@Override








bugfix: must increment counter on task submission, not when task starts executing

 


Eric Bodden
committed
Jan 28, 2013




39



	public void execute(Runnable command) {








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




40


41


42


43


44


45


46


47



		try {
			numRunningTasks.increment();
			super.execute(command);
		}
		catch (RejectedExecutionException ex) {
			// If we were unable to submit the task, we may not count it!
			numRunningTasks.decrement();
		}








further cleanups



Eric Bodden
committed
Jan 26, 2013




48



	}








bugfix: must increment counter on task submission, not when task starts executing

 


Eric Bodden
committed
Jan 28, 2013




49



	








further cleanups



Eric Bodden
committed
Jan 26, 2013




50


51


52



	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		numRunningTasks.decrement();








shutdown now

 


Eric Bodden
committed
Jul 10, 2013




53



		if(t!=null) {








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




54



			exception = t;








added logging for execution errors in worker threads

 


Steven Arzt
committed
Oct 26, 2013




55


56



			logger.error("Worker thread execution failed: " + t.getMessage(), t);
			








shutdown now

 


Eric Bodden
committed
Jul 10, 2013




57



			shutdownNow();








Added counter resetting and thread interruption to fix #3

 


Marc-André Laverdière
committed
Sep 06, 2013




58



            numRunningTasks.resetAndInterrupt();








shutdown now

 


Eric Bodden
committed
Jul 10, 2013




59



		}








further cleanups



Eric Bodden
committed
Jan 26, 2013




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




76


77



	
	/**








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




78



	 * Returns the exception thrown during task execution (if any).








added exception reporting for tasks in IDESolver

 


Eric Bodden
committed
Jun 27, 2013




79



	 */








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




80


81



	public Throwable getException() {
		return exception;








added exception reporting for tasks in IDESolver

 


Eric Bodden
committed
Jun 27, 2013




82



	}








further cleanups



Eric Bodden
committed
Jan 26, 2013




83


84




}











b90d93333c0ac60f93cdbe88bca6cd88e8070f03


Switch branch/tag










heros


src


heros


solver


CountingThreadPoolExecutor.java



Find file
Normal viewHistoryPermalink




b90d93333c0ac60f93cdbe88bca6cd88e8070f03


Switch branch/tag










heros


src


heros


solver


CountingThreadPoolExecutor.java





b90d93333c0ac60f93cdbe88bca6cd88e8070f03


Switch branch/tag








b90d93333c0ac60f93cdbe88bca6cd88e8070f03


Switch branch/tag





b90d93333c0ac60f93cdbe88bca6cd88e8070f03

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



2.44 KB









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








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




14



import java.util.concurrent.RejectedExecutionException;








further cleanups



Eric Bodden
committed
Jan 26, 2013




15


16


17



import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;









added logging for execution errors in worker threads

 


Steven Arzt
committed
Oct 26, 2013




18


19


20



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;









further cleanups



Eric Bodden
committed
Jan 26, 2013




21


22


23


24


25


26



/**
 * A {@link ThreadPoolExecutor} which keeps track of the number of spawned
 * tasks to allow clients to await their completion. 
 */
public class CountingThreadPoolExecutor extends ThreadPoolExecutor {
	








added logging for execution errors in worker threads

 


Steven Arzt
committed
Oct 26, 2013




27


28


29



    protected static final Logger logger = LoggerFactory.getLogger(IDESolver.class);

    protected final CountLatch numRunningTasks = new CountLatch(0);








added exception reporting for tasks in IDESolver

 


Eric Bodden
committed
Jun 27, 2013




30



	








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




31



	protected Throwable exception = null;








further cleanups



Eric Bodden
committed
Jan 26, 2013




32


33


34


35


36


37


38




	public CountingThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	@Override








bugfix: must increment counter on task submission, not when task starts executing

 


Eric Bodden
committed
Jan 28, 2013




39



	public void execute(Runnable command) {








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




40


41


42


43


44


45


46


47



		try {
			numRunningTasks.increment();
			super.execute(command);
		}
		catch (RejectedExecutionException ex) {
			// If we were unable to submit the task, we may not count it!
			numRunningTasks.decrement();
		}








further cleanups



Eric Bodden
committed
Jan 26, 2013




48



	}








bugfix: must increment counter on task submission, not when task starts executing

 


Eric Bodden
committed
Jan 28, 2013




49



	








further cleanups



Eric Bodden
committed
Jan 26, 2013




50


51


52



	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		numRunningTasks.decrement();








shutdown now

 


Eric Bodden
committed
Jul 10, 2013




53



		if(t!=null) {








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




54



			exception = t;








added logging for execution errors in worker threads

 


Steven Arzt
committed
Oct 26, 2013




55


56



			logger.error("Worker thread execution failed: " + t.getMessage(), t);
			








shutdown now

 


Eric Bodden
committed
Jul 10, 2013




57



			shutdownNow();








Added counter resetting and thread interruption to fix #3

 


Marc-André Laverdière
committed
Sep 06, 2013




58



            numRunningTasks.resetAndInterrupt();








shutdown now

 


Eric Bodden
committed
Jul 10, 2013




59



		}








further cleanups



Eric Bodden
committed
Jan 26, 2013




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




76


77



	
	/**








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




78



	 * Returns the exception thrown during task execution (if any).








added exception reporting for tasks in IDESolver

 


Eric Bodden
committed
Jun 27, 2013




79



	 */








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




80


81



	public Throwable getException() {
		return exception;








added exception reporting for tasks in IDESolver

 


Eric Bodden
committed
Jun 27, 2013




82



	}








further cleanups



Eric Bodden
committed
Jan 26, 2013




83


84




}









CountingThreadPoolExecutor.java



2.44 KB










CountingThreadPoolExecutor.java



2.44 KB









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








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




14



import java.util.concurrent.RejectedExecutionException;








further cleanups



Eric Bodden
committed
Jan 26, 2013




15


16


17



import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;









added logging for execution errors in worker threads

 


Steven Arzt
committed
Oct 26, 2013




18


19


20



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;









further cleanups



Eric Bodden
committed
Jan 26, 2013




21


22


23


24


25


26



/**
 * A {@link ThreadPoolExecutor} which keeps track of the number of spawned
 * tasks to allow clients to await their completion. 
 */
public class CountingThreadPoolExecutor extends ThreadPoolExecutor {
	








added logging for execution errors in worker threads

 


Steven Arzt
committed
Oct 26, 2013




27


28


29



    protected static final Logger logger = LoggerFactory.getLogger(IDESolver.class);

    protected final CountLatch numRunningTasks = new CountLatch(0);








added exception reporting for tasks in IDESolver

 


Eric Bodden
committed
Jun 27, 2013




30



	








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




31



	protected Throwable exception = null;








further cleanups



Eric Bodden
committed
Jan 26, 2013




32


33


34


35


36


37


38




	public CountingThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	@Override








bugfix: must increment counter on task submission, not when task starts executing

 


Eric Bodden
committed
Jan 28, 2013




39



	public void execute(Runnable command) {








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




40


41


42


43


44


45


46


47



		try {
			numRunningTasks.increment();
			super.execute(command);
		}
		catch (RejectedExecutionException ex) {
			// If we were unable to submit the task, we may not count it!
			numRunningTasks.decrement();
		}








further cleanups



Eric Bodden
committed
Jan 26, 2013




48



	}








bugfix: must increment counter on task submission, not when task starts executing

 


Eric Bodden
committed
Jan 28, 2013




49



	








further cleanups



Eric Bodden
committed
Jan 26, 2013




50


51


52



	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		numRunningTasks.decrement();








shutdown now

 


Eric Bodden
committed
Jul 10, 2013




53



		if(t!=null) {








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




54



			exception = t;








added logging for execution errors in worker threads

 


Steven Arzt
committed
Oct 26, 2013




55


56



			logger.error("Worker thread execution failed: " + t.getMessage(), t);
			








shutdown now

 


Eric Bodden
committed
Jul 10, 2013




57



			shutdownNow();








Added counter resetting and thread interruption to fix #3

 


Marc-André Laverdière
committed
Sep 06, 2013




58



            numRunningTasks.resetAndInterrupt();








shutdown now

 


Eric Bodden
committed
Jul 10, 2013




59



		}








further cleanups



Eric Bodden
committed
Jan 26, 2013




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




76


77



	
	/**








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




78



	 * Returns the exception thrown during task execution (if any).








added exception reporting for tasks in IDESolver

 


Eric Bodden
committed
Jun 27, 2013




79



	 */








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




80


81



	public Throwable getException() {
		return exception;








added exception reporting for tasks in IDESolver

 


Eric Bodden
committed
Jun 27, 2013




82



	}








further cleanups



Eric Bodden
committed
Jan 26, 2013




83


84




}











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








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




14



import java.util.concurrent.RejectedExecutionException;








further cleanups



Eric Bodden
committed
Jan 26, 2013




15


16


17



import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;









added logging for execution errors in worker threads

 


Steven Arzt
committed
Oct 26, 2013




18


19


20



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;









further cleanups



Eric Bodden
committed
Jan 26, 2013




21


22


23


24


25


26



/**
 * A {@link ThreadPoolExecutor} which keeps track of the number of spawned
 * tasks to allow clients to await their completion. 
 */
public class CountingThreadPoolExecutor extends ThreadPoolExecutor {
	








added logging for execution errors in worker threads

 


Steven Arzt
committed
Oct 26, 2013




27


28


29



    protected static final Logger logger = LoggerFactory.getLogger(IDESolver.class);

    protected final CountLatch numRunningTasks = new CountLatch(0);








added exception reporting for tasks in IDESolver

 


Eric Bodden
committed
Jun 27, 2013




30



	








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




31



	protected Throwable exception = null;








further cleanups



Eric Bodden
committed
Jan 26, 2013




32


33


34


35


36


37


38




	public CountingThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	@Override








bugfix: must increment counter on task submission, not when task starts executing

 


Eric Bodden
committed
Jan 28, 2013




39



	public void execute(Runnable command) {








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




40


41


42


43


44


45


46


47



		try {
			numRunningTasks.increment();
			super.execute(command);
		}
		catch (RejectedExecutionException ex) {
			// If we were unable to submit the task, we may not count it!
			numRunningTasks.decrement();
		}








further cleanups



Eric Bodden
committed
Jan 26, 2013




48



	}








bugfix: must increment counter on task submission, not when task starts executing

 


Eric Bodden
committed
Jan 28, 2013




49



	








further cleanups



Eric Bodden
committed
Jan 26, 2013




50


51


52



	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		numRunningTasks.decrement();








shutdown now

 


Eric Bodden
committed
Jul 10, 2013




53



		if(t!=null) {








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




54



			exception = t;








added logging for execution errors in worker threads

 


Steven Arzt
committed
Oct 26, 2013




55


56



			logger.error("Worker thread execution failed: " + t.getMessage(), t);
			








shutdown now

 


Eric Bodden
committed
Jul 10, 2013




57



			shutdownNow();








Added counter resetting and thread interruption to fix #3

 


Marc-André Laverdière
committed
Sep 06, 2013




58



            numRunningTasks.resetAndInterrupt();








shutdown now

 


Eric Bodden
committed
Jul 10, 2013




59



		}








further cleanups



Eric Bodden
committed
Jan 26, 2013




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




76


77



	
	/**








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




78



	 * Returns the exception thrown during task execution (if any).








added exception reporting for tasks in IDESolver

 


Eric Bodden
committed
Jun 27, 2013




79



	 */








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




80


81



	public Throwable getException() {
		return exception;








added exception reporting for tasks in IDESolver

 


Eric Bodden
committed
Jun 27, 2013




82



	}








further cleanups



Eric Bodden
committed
Jan 26, 2013




83


84




}









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








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




14



import java.util.concurrent.RejectedExecutionException;








further cleanups



Eric Bodden
committed
Jan 26, 2013




15


16


17



import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;









added logging for execution errors in worker threads

 


Steven Arzt
committed
Oct 26, 2013




18


19


20



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;









further cleanups



Eric Bodden
committed
Jan 26, 2013




21


22


23


24


25


26



/**
 * A {@link ThreadPoolExecutor} which keeps track of the number of spawned
 * tasks to allow clients to await their completion. 
 */
public class CountingThreadPoolExecutor extends ThreadPoolExecutor {
	








added logging for execution errors in worker threads

 


Steven Arzt
committed
Oct 26, 2013




27


28


29



    protected static final Logger logger = LoggerFactory.getLogger(IDESolver.class);

    protected final CountLatch numRunningTasks = new CountLatch(0);








added exception reporting for tasks in IDESolver

 


Eric Bodden
committed
Jun 27, 2013




30



	








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




31



	protected Throwable exception = null;








further cleanups



Eric Bodden
committed
Jan 26, 2013




32


33


34


35


36


37


38




	public CountingThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	@Override








bugfix: must increment counter on task submission, not when task starts executing

 


Eric Bodden
committed
Jan 28, 2013




39



	public void execute(Runnable command) {








Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




40


41


42


43


44


45


46


47



		try {
			numRunningTasks.increment();
			super.execute(command);
		}
		catch (RejectedExecutionException ex) {
			// If we were unable to submit the task, we may not count it!
			numRunningTasks.decrement();
		}








further cleanups



Eric Bodden
committed
Jan 26, 2013




48



	}








bugfix: must increment counter on task submission, not when task starts executing

 


Eric Bodden
committed
Jan 28, 2013




49



	








further cleanups



Eric Bodden
committed
Jan 26, 2013




50


51


52



	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		numRunningTasks.decrement();








shutdown now

 


Eric Bodden
committed
Jul 10, 2013




53



		if(t!=null) {








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




54



			exception = t;








added logging for execution errors in worker threads

 


Steven Arzt
committed
Oct 26, 2013




55


56



			logger.error("Worker thread execution failed: " + t.getMessage(), t);
			








shutdown now

 


Eric Bodden
committed
Jul 10, 2013




57



			shutdownNow();








Added counter resetting and thread interruption to fix #3

 


Marc-André Laverdière
committed
Sep 06, 2013




58



            numRunningTasks.resetAndInterrupt();








shutdown now

 


Eric Bodden
committed
Jul 10, 2013




59



		}








further cleanups



Eric Bodden
committed
Jan 26, 2013




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




76


77



	
	/**








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




78



	 * Returns the exception thrown during task execution (if any).








added exception reporting for tasks in IDESolver

 


Eric Bodden
committed
Jun 27, 2013




79



	 */








simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




80


81



	public Throwable getException() {
		return exception;








added exception reporting for tasks in IDESolver

 


Eric Bodden
committed
Jun 27, 2013




82



	}








further cleanups



Eric Bodden
committed
Jan 26, 2013




83


84




}







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

/*******************************************************************************/******************************************************************************* * Copyright (c) 2012 Eric Bodden. * Copyright (c) 2012 Eric Bodden. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Eric Bodden - initial API and implementation *     Eric Bodden - initial API and implementation ******************************************************************************/ ******************************************************************************/package heros.solver;packageheros.solver;import java.util.concurrent.BlockingQueue;importjava.util.concurrent.BlockingQueue;




Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




14



import java.util.concurrent.RejectedExecutionException;






Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013



Better be careful with executors: If they are shutting down, no new tasks may...

 

Better be careful with executors: If they are shutting down, no new tasks may...

Steven Arzt
committed
Oct 26, 2013


14


import java.util.concurrent.RejectedExecutionException;

import java.util.concurrent.RejectedExecutionException;importjava.util.concurrent.RejectedExecutionException;




further cleanups



Eric Bodden
committed
Jan 26, 2013




15


16


17



import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;







further cleanups



Eric Bodden
committed
Jan 26, 2013



further cleanups


further cleanups

Eric Bodden
committed
Jan 26, 2013


15


16


17


import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


import java.util.concurrent.ThreadPoolExecutor;importjava.util.concurrent.ThreadPoolExecutor;import java.util.concurrent.TimeUnit;importjava.util.concurrent.TimeUnit;




added logging for execution errors in worker threads

 


Steven Arzt
committed
Oct 26, 2013




18


19


20



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;







added logging for execution errors in worker threads

 


Steven Arzt
committed
Oct 26, 2013



added logging for execution errors in worker threads

 

added logging for execution errors in worker threads

Steven Arzt
committed
Oct 26, 2013


18


19


20


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.slf4j.Logger;importorg.slf4j.Logger;import org.slf4j.LoggerFactory;importorg.slf4j.LoggerFactory;




further cleanups



Eric Bodden
committed
Jan 26, 2013




21


22


23


24


25


26



/**
 * A {@link ThreadPoolExecutor} which keeps track of the number of spawned
 * tasks to allow clients to await their completion. 
 */
public class CountingThreadPoolExecutor extends ThreadPoolExecutor {
	






further cleanups



Eric Bodden
committed
Jan 26, 2013



further cleanups


further cleanups

Eric Bodden
committed
Jan 26, 2013


21


22


23


24


25


26


/**
 * A {@link ThreadPoolExecutor} which keeps track of the number of spawned
 * tasks to allow clients to await their completion. 
 */
public class CountingThreadPoolExecutor extends ThreadPoolExecutor {
	

/**/** * A {@link ThreadPoolExecutor} which keeps track of the number of spawned * A {@link ThreadPoolExecutor} which keeps track of the number of spawned * tasks to allow clients to await their completion.  * tasks to allow clients to await their completion.  */ */public class CountingThreadPoolExecutor extends ThreadPoolExecutor {publicclassCountingThreadPoolExecutorextendsThreadPoolExecutor{	




added logging for execution errors in worker threads

 


Steven Arzt
committed
Oct 26, 2013




27


28


29



    protected static final Logger logger = LoggerFactory.getLogger(IDESolver.class);

    protected final CountLatch numRunningTasks = new CountLatch(0);






added logging for execution errors in worker threads

 


Steven Arzt
committed
Oct 26, 2013



added logging for execution errors in worker threads

 

added logging for execution errors in worker threads

Steven Arzt
committed
Oct 26, 2013


27


28


29


    protected static final Logger logger = LoggerFactory.getLogger(IDESolver.class);

    protected final CountLatch numRunningTasks = new CountLatch(0);

    protected static final Logger logger = LoggerFactory.getLogger(IDESolver.class);protectedstaticfinalLoggerlogger=LoggerFactory.getLogger(IDESolver.class);    protected final CountLatch numRunningTasks = new CountLatch(0);protectedfinalCountLatchnumRunningTasks=newCountLatch(0);




added exception reporting for tasks in IDESolver

 


Eric Bodden
committed
Jun 27, 2013




30



	






added exception reporting for tasks in IDESolver

 


Eric Bodden
committed
Jun 27, 2013



added exception reporting for tasks in IDESolver

 

added exception reporting for tasks in IDESolver

Eric Bodden
committed
Jun 27, 2013


30


	

	




simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




31



	protected Throwable exception = null;






simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013



simplified exception handling

 

simplified exception handling

Eric Bodden
committed
Jul 11, 2013


31


	protected Throwable exception = null;

	protected Throwable exception = null;protectedThrowableexception=null;




further cleanups



Eric Bodden
committed
Jan 26, 2013




32


33


34


35


36


37


38




	public CountingThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	@Override






further cleanups



Eric Bodden
committed
Jan 26, 2013



further cleanups


further cleanups

Eric Bodden
committed
Jan 26, 2013


32


33


34


35


36


37


38



	public CountingThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	@Override

	public CountingThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,publicCountingThreadPoolExecutor(intcorePoolSize,intmaximumPoolSize,longkeepAliveTime,TimeUnitunit,			BlockingQueue<Runnable> workQueue) {BlockingQueue<Runnable>workQueue){		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);super(corePoolSize,maximumPoolSize,keepAliveTime,unit,workQueue);	}}	@Override@Override




bugfix: must increment counter on task submission, not when task starts executing

 


Eric Bodden
committed
Jan 28, 2013




39



	public void execute(Runnable command) {






bugfix: must increment counter on task submission, not when task starts executing

 


Eric Bodden
committed
Jan 28, 2013



bugfix: must increment counter on task submission, not when task starts executing

 

bugfix: must increment counter on task submission, not when task starts executing

Eric Bodden
committed
Jan 28, 2013


39


	public void execute(Runnable command) {

	public void execute(Runnable command) {publicvoidexecute(Runnablecommand){




Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013




40


41


42


43


44


45


46


47



		try {
			numRunningTasks.increment();
			super.execute(command);
		}
		catch (RejectedExecutionException ex) {
			// If we were unable to submit the task, we may not count it!
			numRunningTasks.decrement();
		}






Better be careful with executors: If they are shutting down, no new tasks may...

 


Steven Arzt
committed
Oct 26, 2013



Better be careful with executors: If they are shutting down, no new tasks may...

 

Better be careful with executors: If they are shutting down, no new tasks may...

Steven Arzt
committed
Oct 26, 2013


40


41


42


43


44


45


46


47


		try {
			numRunningTasks.increment();
			super.execute(command);
		}
		catch (RejectedExecutionException ex) {
			// If we were unable to submit the task, we may not count it!
			numRunningTasks.decrement();
		}

		try {try{			numRunningTasks.increment();numRunningTasks.increment();			super.execute(command);super.execute(command);		}}		catch (RejectedExecutionException ex) {catch(RejectedExecutionExceptionex){			// If we were unable to submit the task, we may not count it!// If we were unable to submit the task, we may not count it!			numRunningTasks.decrement();numRunningTasks.decrement();		}}




further cleanups



Eric Bodden
committed
Jan 26, 2013




48



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


48


	}

	}}




bugfix: must increment counter on task submission, not when task starts executing

 


Eric Bodden
committed
Jan 28, 2013




49



	






bugfix: must increment counter on task submission, not when task starts executing

 


Eric Bodden
committed
Jan 28, 2013



bugfix: must increment counter on task submission, not when task starts executing

 

bugfix: must increment counter on task submission, not when task starts executing

Eric Bodden
committed
Jan 28, 2013


49


	

	




further cleanups



Eric Bodden
committed
Jan 26, 2013




50


51


52



	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		numRunningTasks.decrement();






further cleanups



Eric Bodden
committed
Jan 26, 2013



further cleanups


further cleanups

Eric Bodden
committed
Jan 26, 2013


50


51


52


	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		numRunningTasks.decrement();

	@Override@Override	protected void afterExecute(Runnable r, Throwable t) {protectedvoidafterExecute(Runnabler,Throwablet){		numRunningTasks.decrement();numRunningTasks.decrement();




shutdown now

 


Eric Bodden
committed
Jul 10, 2013




53



		if(t!=null) {






shutdown now

 


Eric Bodden
committed
Jul 10, 2013



shutdown now

 

shutdown now

Eric Bodden
committed
Jul 10, 2013


53


		if(t!=null) {

		if(t!=null) {if(t!=null){




simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




54



			exception = t;






simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013



simplified exception handling

 

simplified exception handling

Eric Bodden
committed
Jul 11, 2013


54


			exception = t;

			exception = t;exception=t;




added logging for execution errors in worker threads

 


Steven Arzt
committed
Oct 26, 2013




55


56



			logger.error("Worker thread execution failed: " + t.getMessage(), t);
			






added logging for execution errors in worker threads

 


Steven Arzt
committed
Oct 26, 2013



added logging for execution errors in worker threads

 

added logging for execution errors in worker threads

Steven Arzt
committed
Oct 26, 2013


55


56


			logger.error("Worker thread execution failed: " + t.getMessage(), t);
			

			logger.error("Worker thread execution failed: " + t.getMessage(), t);logger.error("Worker thread execution failed: "+t.getMessage(),t);			




shutdown now

 


Eric Bodden
committed
Jul 10, 2013




57



			shutdownNow();






shutdown now

 


Eric Bodden
committed
Jul 10, 2013



shutdown now

 

shutdown now

Eric Bodden
committed
Jul 10, 2013


57


			shutdownNow();

			shutdownNow();shutdownNow();




Added counter resetting and thread interruption to fix #3

 


Marc-André Laverdière
committed
Sep 06, 2013




58



            numRunningTasks.resetAndInterrupt();






Added counter resetting and thread interruption to fix #3

 


Marc-André Laverdière
committed
Sep 06, 2013



Added counter resetting and thread interruption to fix #3

 

Added counter resetting and thread interruption to fix #3

Marc-André Laverdière
committed
Sep 06, 2013


58


            numRunningTasks.resetAndInterrupt();

            numRunningTasks.resetAndInterrupt();numRunningTasks.resetAndInterrupt();




shutdown now

 


Eric Bodden
committed
Jul 10, 2013




59



		}






shutdown now

 


Eric Bodden
committed
Jul 10, 2013



shutdown now

 

shutdown now

Eric Bodden
committed
Jul 10, 2013


59


		}

		}}




further cleanups



Eric Bodden
committed
Jan 26, 2013




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






further cleanups



Eric Bodden
committed
Jan 26, 2013



further cleanups


further cleanups

Eric Bodden
committed
Jan 26, 2013


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

		super.afterExecute(r, t);super.afterExecute(r,t);	}}	/**/**	 * Awaits the completion of all spawned tasks.	 * Awaits the completion of all spawned tasks.	 */	 */	public void awaitCompletion() throws InterruptedException {publicvoidawaitCompletion()throwsInterruptedException{		numRunningTasks.awaitZero();numRunningTasks.awaitZero();	}}		/**/**	 * Awaits the completion of all spawned tasks.	 * Awaits the completion of all spawned tasks.	 */	 */	public void awaitCompletion(long timeout, TimeUnit unit) throws InterruptedException {publicvoidawaitCompletion(longtimeout,TimeUnitunit)throwsInterruptedException{		numRunningTasks.awaitZero(timeout, unit);numRunningTasks.awaitZero(timeout,unit);	}}




added exception reporting for tasks in IDESolver

 


Eric Bodden
committed
Jun 27, 2013




76


77



	
	/**






added exception reporting for tasks in IDESolver

 


Eric Bodden
committed
Jun 27, 2013



added exception reporting for tasks in IDESolver

 

added exception reporting for tasks in IDESolver

Eric Bodden
committed
Jun 27, 2013


76


77


	
	/**

		/**/**




simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




78



	 * Returns the exception thrown during task execution (if any).






simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013



simplified exception handling

 

simplified exception handling

Eric Bodden
committed
Jul 11, 2013


78


	 * Returns the exception thrown during task execution (if any).

	 * Returns the exception thrown during task execution (if any).	 * Returns the exception thrown during task execution (if any).




added exception reporting for tasks in IDESolver

 


Eric Bodden
committed
Jun 27, 2013




79



	 */






added exception reporting for tasks in IDESolver

 


Eric Bodden
committed
Jun 27, 2013



added exception reporting for tasks in IDESolver

 

added exception reporting for tasks in IDESolver

Eric Bodden
committed
Jun 27, 2013


79


	 */

	 */	 */




simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013




80


81



	public Throwable getException() {
		return exception;






simplified exception handling

 


Eric Bodden
committed
Jul 11, 2013



simplified exception handling

 

simplified exception handling

Eric Bodden
committed
Jul 11, 2013


80


81


	public Throwable getException() {
		return exception;

	public Throwable getException() {publicThrowablegetException(){		return exception;returnexception;




added exception reporting for tasks in IDESolver

 


Eric Bodden
committed
Jun 27, 2013




82



	}






added exception reporting for tasks in IDESolver

 


Eric Bodden
committed
Jun 27, 2013



added exception reporting for tasks in IDESolver

 

added exception reporting for tasks in IDESolver

Eric Bodden
committed
Jun 27, 2013


82


	}

	}}




further cleanups



Eric Bodden
committed
Jan 26, 2013




83


84




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


83


84



}
}}





