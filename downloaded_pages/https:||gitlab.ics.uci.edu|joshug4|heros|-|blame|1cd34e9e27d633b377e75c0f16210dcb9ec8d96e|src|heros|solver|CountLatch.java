



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

1cd34e9e27d633b377e75c0f16210dcb9ec8d96e

















1cd34e9e27d633b377e75c0f16210dcb9ec8d96e


Switch branch/tag










heros


src


heros


solver


CountLatch.java



Find file
Normal viewHistoryPermalink






CountLatch.java



2.83 KB









Newer










Older









adding CountLatch



 


Eric Bodden
committed
Jan 28, 2013






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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * A synchronization aid similar to {@link CountDownLatch} but with the ability
 * to also count up. This is useful to wait until a variable number of tasks
 * have completed. {@link #awaitZero()} will block until the count reaches zero.
 */
public class CountLatch {

	@SuppressWarnings("serial")
	private static final class Sync extends AbstractQueuedSynchronizer {

		Sync(int count) {
			setState(count);
		}

		int getCount() {
			return getState();
		}










Added counter resetting and thread interruption to fix #3


 

 


Marc-André Laverdière
committed
Sep 06, 2013






35




36




37




38




        void reset() {
            setState(0);
        }










1) added some override annotations


 

 


Steven Arzt
committed
Oct 25, 2013






39




        @Override









adding CountLatch



 


Eric Bodden
committed
Jan 28, 2013






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




		protected int tryAcquireShared(int acquires) {
			return (getState() == 0) ? 1 : -1;
		}

		protected int acquireNonBlocking(int acquires) {
			// increment count
			for (;;) {
				int c = getState();
				int nextc = c + 1;
				if (compareAndSetState(c, nextc))
					return 1;
			}
		}










1) added some override annotations


 

 


Steven Arzt
committed
Oct 25, 2013






54




        @Override









adding CountLatch



 


Eric Bodden
committed
Jan 28, 2013






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




		protected boolean tryReleaseShared(int releases) {
			// Decrement count; signal when transition to zero
			for (;;) {
				int c = getState();
				if (c == 0)
					return false;
				int nextc = c - 1;
				if (compareAndSetState(c, nextc))
					return nextc == 0;
			}
		}
	}

	private final Sync sync;

	public CountLatch(int count) {
		this.sync = new Sync(count);
	}

	public void awaitZero() throws InterruptedException {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






75




		sync.acquireShared(1);









adding CountLatch



 


Eric Bodden
committed
Jan 28, 2013






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




	}

	public boolean awaitZero(long timeout, TimeUnit unit) throws InterruptedException {
		return sync.tryAcquireSharedNanos(1, unit.toNanos(timeout));
	}

	public void increment() {
		sync.acquireNonBlocking(1);
	}

	public void decrement() {
		sync.releaseShared(1);
	}










Added counter resetting and thread interruption to fix #3


 

 


Marc-André Laverdière
committed
Sep 06, 2013






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




    /**
     * Resets the counter to zero. But waiting threads won't be released somehow.
     * So this interrupts the threads so that they escape from their waiting state.
     */
    public void resetAndInterrupt(){
        sync.reset();
        for (int i = 0; i < 3; i++) //Because it is a best effort thing, do it three times and hope for the best.
            for (Thread t : sync.getQueuedThreads())
                t.interrupt();
        sync.reset(); //Just in case a thread would've incremented the counter again.
    }










adding CountLatch



 


Eric Bodden
committed
Jan 28, 2013






102




103




104




105




	public String toString() {
		return super.toString() + "[Count = " + sync.getCount() + "]";
	}










Revert "adding CountLatch"


 

 


Eric Bodden
committed
Jan 28, 2013






106




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

1cd34e9e27d633b377e75c0f16210dcb9ec8d96e

















1cd34e9e27d633b377e75c0f16210dcb9ec8d96e


Switch branch/tag










heros


src


heros


solver


CountLatch.java



Find file
Normal viewHistoryPermalink






CountLatch.java



2.83 KB









Newer










Older









adding CountLatch



 


Eric Bodden
committed
Jan 28, 2013






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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * A synchronization aid similar to {@link CountDownLatch} but with the ability
 * to also count up. This is useful to wait until a variable number of tasks
 * have completed. {@link #awaitZero()} will block until the count reaches zero.
 */
public class CountLatch {

	@SuppressWarnings("serial")
	private static final class Sync extends AbstractQueuedSynchronizer {

		Sync(int count) {
			setState(count);
		}

		int getCount() {
			return getState();
		}










Added counter resetting and thread interruption to fix #3


 

 


Marc-André Laverdière
committed
Sep 06, 2013






35




36




37




38




        void reset() {
            setState(0);
        }










1) added some override annotations


 

 


Steven Arzt
committed
Oct 25, 2013






39




        @Override









adding CountLatch



 


Eric Bodden
committed
Jan 28, 2013






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




		protected int tryAcquireShared(int acquires) {
			return (getState() == 0) ? 1 : -1;
		}

		protected int acquireNonBlocking(int acquires) {
			// increment count
			for (;;) {
				int c = getState();
				int nextc = c + 1;
				if (compareAndSetState(c, nextc))
					return 1;
			}
		}










1) added some override annotations


 

 


Steven Arzt
committed
Oct 25, 2013






54




        @Override









adding CountLatch



 


Eric Bodden
committed
Jan 28, 2013






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




		protected boolean tryReleaseShared(int releases) {
			// Decrement count; signal when transition to zero
			for (;;) {
				int c = getState();
				if (c == 0)
					return false;
				int nextc = c - 1;
				if (compareAndSetState(c, nextc))
					return nextc == 0;
			}
		}
	}

	private final Sync sync;

	public CountLatch(int count) {
		this.sync = new Sync(count);
	}

	public void awaitZero() throws InterruptedException {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






75




		sync.acquireShared(1);









adding CountLatch



 


Eric Bodden
committed
Jan 28, 2013






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




	}

	public boolean awaitZero(long timeout, TimeUnit unit) throws InterruptedException {
		return sync.tryAcquireSharedNanos(1, unit.toNanos(timeout));
	}

	public void increment() {
		sync.acquireNonBlocking(1);
	}

	public void decrement() {
		sync.releaseShared(1);
	}










Added counter resetting and thread interruption to fix #3


 

 


Marc-André Laverdière
committed
Sep 06, 2013






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




    /**
     * Resets the counter to zero. But waiting threads won't be released somehow.
     * So this interrupts the threads so that they escape from their waiting state.
     */
    public void resetAndInterrupt(){
        sync.reset();
        for (int i = 0; i < 3; i++) //Because it is a best effort thing, do it three times and hope for the best.
            for (Thread t : sync.getQueuedThreads())
                t.interrupt();
        sync.reset(); //Just in case a thread would've incremented the counter again.
    }










adding CountLatch



 


Eric Bodden
committed
Jan 28, 2013






102




103




104




105




	public String toString() {
		return super.toString() + "[Count = " + sync.getCount() + "]";
	}










Revert "adding CountLatch"


 

 


Eric Bodden
committed
Jan 28, 2013






106




}











Open sidebar



Joshua Garcia heros

1cd34e9e27d633b377e75c0f16210dcb9ec8d96e







Open sidebar



Joshua Garcia heros

1cd34e9e27d633b377e75c0f16210dcb9ec8d96e




Open sidebar

Joshua Garcia heros

1cd34e9e27d633b377e75c0f16210dcb9ec8d96e


Joshua Garciaherosheros
1cd34e9e27d633b377e75c0f16210dcb9ec8d96e










1cd34e9e27d633b377e75c0f16210dcb9ec8d96e


Switch branch/tag










heros


src


heros


solver


CountLatch.java



Find file
Normal viewHistoryPermalink






CountLatch.java



2.83 KB









Newer










Older









adding CountLatch



 


Eric Bodden
committed
Jan 28, 2013






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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * A synchronization aid similar to {@link CountDownLatch} but with the ability
 * to also count up. This is useful to wait until a variable number of tasks
 * have completed. {@link #awaitZero()} will block until the count reaches zero.
 */
public class CountLatch {

	@SuppressWarnings("serial")
	private static final class Sync extends AbstractQueuedSynchronizer {

		Sync(int count) {
			setState(count);
		}

		int getCount() {
			return getState();
		}










Added counter resetting and thread interruption to fix #3


 

 


Marc-André Laverdière
committed
Sep 06, 2013






35




36




37




38




        void reset() {
            setState(0);
        }










1) added some override annotations


 

 


Steven Arzt
committed
Oct 25, 2013






39




        @Override









adding CountLatch



 


Eric Bodden
committed
Jan 28, 2013






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




		protected int tryAcquireShared(int acquires) {
			return (getState() == 0) ? 1 : -1;
		}

		protected int acquireNonBlocking(int acquires) {
			// increment count
			for (;;) {
				int c = getState();
				int nextc = c + 1;
				if (compareAndSetState(c, nextc))
					return 1;
			}
		}










1) added some override annotations


 

 


Steven Arzt
committed
Oct 25, 2013






54




        @Override









adding CountLatch



 


Eric Bodden
committed
Jan 28, 2013






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




		protected boolean tryReleaseShared(int releases) {
			// Decrement count; signal when transition to zero
			for (;;) {
				int c = getState();
				if (c == 0)
					return false;
				int nextc = c - 1;
				if (compareAndSetState(c, nextc))
					return nextc == 0;
			}
		}
	}

	private final Sync sync;

	public CountLatch(int count) {
		this.sync = new Sync(count);
	}

	public void awaitZero() throws InterruptedException {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






75




		sync.acquireShared(1);









adding CountLatch



 


Eric Bodden
committed
Jan 28, 2013






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




	}

	public boolean awaitZero(long timeout, TimeUnit unit) throws InterruptedException {
		return sync.tryAcquireSharedNanos(1, unit.toNanos(timeout));
	}

	public void increment() {
		sync.acquireNonBlocking(1);
	}

	public void decrement() {
		sync.releaseShared(1);
	}










Added counter resetting and thread interruption to fix #3


 

 


Marc-André Laverdière
committed
Sep 06, 2013






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




    /**
     * Resets the counter to zero. But waiting threads won't be released somehow.
     * So this interrupts the threads so that they escape from their waiting state.
     */
    public void resetAndInterrupt(){
        sync.reset();
        for (int i = 0; i < 3; i++) //Because it is a best effort thing, do it three times and hope for the best.
            for (Thread t : sync.getQueuedThreads())
                t.interrupt();
        sync.reset(); //Just in case a thread would've incremented the counter again.
    }










adding CountLatch



 


Eric Bodden
committed
Jan 28, 2013






102




103




104




105




	public String toString() {
		return super.toString() + "[Count = " + sync.getCount() + "]";
	}










Revert "adding CountLatch"


 

 


Eric Bodden
committed
Jan 28, 2013






106




}














1cd34e9e27d633b377e75c0f16210dcb9ec8d96e


Switch branch/tag










heros


src


heros


solver


CountLatch.java



Find file
Normal viewHistoryPermalink






CountLatch.java



2.83 KB









Newer










Older









adding CountLatch



 


Eric Bodden
committed
Jan 28, 2013






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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * A synchronization aid similar to {@link CountDownLatch} but with the ability
 * to also count up. This is useful to wait until a variable number of tasks
 * have completed. {@link #awaitZero()} will block until the count reaches zero.
 */
public class CountLatch {

	@SuppressWarnings("serial")
	private static final class Sync extends AbstractQueuedSynchronizer {

		Sync(int count) {
			setState(count);
		}

		int getCount() {
			return getState();
		}










Added counter resetting and thread interruption to fix #3


 

 


Marc-André Laverdière
committed
Sep 06, 2013






35




36




37




38




        void reset() {
            setState(0);
        }










1) added some override annotations


 

 


Steven Arzt
committed
Oct 25, 2013






39




        @Override









adding CountLatch



 


Eric Bodden
committed
Jan 28, 2013






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




		protected int tryAcquireShared(int acquires) {
			return (getState() == 0) ? 1 : -1;
		}

		protected int acquireNonBlocking(int acquires) {
			// increment count
			for (;;) {
				int c = getState();
				int nextc = c + 1;
				if (compareAndSetState(c, nextc))
					return 1;
			}
		}










1) added some override annotations


 

 


Steven Arzt
committed
Oct 25, 2013






54




        @Override









adding CountLatch



 


Eric Bodden
committed
Jan 28, 2013






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




		protected boolean tryReleaseShared(int releases) {
			// Decrement count; signal when transition to zero
			for (;;) {
				int c = getState();
				if (c == 0)
					return false;
				int nextc = c - 1;
				if (compareAndSetState(c, nextc))
					return nextc == 0;
			}
		}
	}

	private final Sync sync;

	public CountLatch(int count) {
		this.sync = new Sync(count);
	}

	public void awaitZero() throws InterruptedException {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






75




		sync.acquireShared(1);









adding CountLatch



 


Eric Bodden
committed
Jan 28, 2013






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




	}

	public boolean awaitZero(long timeout, TimeUnit unit) throws InterruptedException {
		return sync.tryAcquireSharedNanos(1, unit.toNanos(timeout));
	}

	public void increment() {
		sync.acquireNonBlocking(1);
	}

	public void decrement() {
		sync.releaseShared(1);
	}










Added counter resetting and thread interruption to fix #3


 

 


Marc-André Laverdière
committed
Sep 06, 2013






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




    /**
     * Resets the counter to zero. But waiting threads won't be released somehow.
     * So this interrupts the threads so that they escape from their waiting state.
     */
    public void resetAndInterrupt(){
        sync.reset();
        for (int i = 0; i < 3; i++) //Because it is a best effort thing, do it three times and hope for the best.
            for (Thread t : sync.getQueuedThreads())
                t.interrupt();
        sync.reset(); //Just in case a thread would've incremented the counter again.
    }










adding CountLatch



 


Eric Bodden
committed
Jan 28, 2013






102




103




104




105




	public String toString() {
		return super.toString() + "[Count = " + sync.getCount() + "]";
	}










Revert "adding CountLatch"


 

 


Eric Bodden
committed
Jan 28, 2013






106




}










1cd34e9e27d633b377e75c0f16210dcb9ec8d96e


Switch branch/tag










heros


src


heros


solver


CountLatch.java



Find file
Normal viewHistoryPermalink




1cd34e9e27d633b377e75c0f16210dcb9ec8d96e


Switch branch/tag










heros


src


heros


solver


CountLatch.java





1cd34e9e27d633b377e75c0f16210dcb9ec8d96e


Switch branch/tag








1cd34e9e27d633b377e75c0f16210dcb9ec8d96e


Switch branch/tag





1cd34e9e27d633b377e75c0f16210dcb9ec8d96e

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src

heros

solver

CountLatch.java
Find file
Normal viewHistoryPermalink




CountLatch.java



2.83 KB









Newer










Older









adding CountLatch



 


Eric Bodden
committed
Jan 28, 2013






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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * A synchronization aid similar to {@link CountDownLatch} but with the ability
 * to also count up. This is useful to wait until a variable number of tasks
 * have completed. {@link #awaitZero()} will block until the count reaches zero.
 */
public class CountLatch {

	@SuppressWarnings("serial")
	private static final class Sync extends AbstractQueuedSynchronizer {

		Sync(int count) {
			setState(count);
		}

		int getCount() {
			return getState();
		}










Added counter resetting and thread interruption to fix #3


 

 


Marc-André Laverdière
committed
Sep 06, 2013






35




36




37




38




        void reset() {
            setState(0);
        }










1) added some override annotations


 

 


Steven Arzt
committed
Oct 25, 2013






39




        @Override









adding CountLatch



 


Eric Bodden
committed
Jan 28, 2013






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




		protected int tryAcquireShared(int acquires) {
			return (getState() == 0) ? 1 : -1;
		}

		protected int acquireNonBlocking(int acquires) {
			// increment count
			for (;;) {
				int c = getState();
				int nextc = c + 1;
				if (compareAndSetState(c, nextc))
					return 1;
			}
		}










1) added some override annotations


 

 


Steven Arzt
committed
Oct 25, 2013






54




        @Override









adding CountLatch



 


Eric Bodden
committed
Jan 28, 2013






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




		protected boolean tryReleaseShared(int releases) {
			// Decrement count; signal when transition to zero
			for (;;) {
				int c = getState();
				if (c == 0)
					return false;
				int nextc = c - 1;
				if (compareAndSetState(c, nextc))
					return nextc == 0;
			}
		}
	}

	private final Sync sync;

	public CountLatch(int count) {
		this.sync = new Sync(count);
	}

	public void awaitZero() throws InterruptedException {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






75




		sync.acquireShared(1);









adding CountLatch



 


Eric Bodden
committed
Jan 28, 2013






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




	}

	public boolean awaitZero(long timeout, TimeUnit unit) throws InterruptedException {
		return sync.tryAcquireSharedNanos(1, unit.toNanos(timeout));
	}

	public void increment() {
		sync.acquireNonBlocking(1);
	}

	public void decrement() {
		sync.releaseShared(1);
	}










Added counter resetting and thread interruption to fix #3


 

 


Marc-André Laverdière
committed
Sep 06, 2013






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




    /**
     * Resets the counter to zero. But waiting threads won't be released somehow.
     * So this interrupts the threads so that they escape from their waiting state.
     */
    public void resetAndInterrupt(){
        sync.reset();
        for (int i = 0; i < 3; i++) //Because it is a best effort thing, do it three times and hope for the best.
            for (Thread t : sync.getQueuedThreads())
                t.interrupt();
        sync.reset(); //Just in case a thread would've incremented the counter again.
    }










adding CountLatch



 


Eric Bodden
committed
Jan 28, 2013






102




103




104




105




	public String toString() {
		return super.toString() + "[Count = " + sync.getCount() + "]";
	}










Revert "adding CountLatch"


 

 


Eric Bodden
committed
Jan 28, 2013






106




}








CountLatch.java



2.83 KB










CountLatch.java



2.83 KB









Newer










Older
NewerOlder







adding CountLatch



 


Eric Bodden
committed
Jan 28, 2013






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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * A synchronization aid similar to {@link CountDownLatch} but with the ability
 * to also count up. This is useful to wait until a variable number of tasks
 * have completed. {@link #awaitZero()} will block until the count reaches zero.
 */
public class CountLatch {

	@SuppressWarnings("serial")
	private static final class Sync extends AbstractQueuedSynchronizer {

		Sync(int count) {
			setState(count);
		}

		int getCount() {
			return getState();
		}










Added counter resetting and thread interruption to fix #3


 

 


Marc-André Laverdière
committed
Sep 06, 2013






35




36




37




38




        void reset() {
            setState(0);
        }










1) added some override annotations


 

 


Steven Arzt
committed
Oct 25, 2013






39




        @Override









adding CountLatch



 


Eric Bodden
committed
Jan 28, 2013






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




		protected int tryAcquireShared(int acquires) {
			return (getState() == 0) ? 1 : -1;
		}

		protected int acquireNonBlocking(int acquires) {
			// increment count
			for (;;) {
				int c = getState();
				int nextc = c + 1;
				if (compareAndSetState(c, nextc))
					return 1;
			}
		}










1) added some override annotations


 

 


Steven Arzt
committed
Oct 25, 2013






54




        @Override









adding CountLatch



 


Eric Bodden
committed
Jan 28, 2013






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




		protected boolean tryReleaseShared(int releases) {
			// Decrement count; signal when transition to zero
			for (;;) {
				int c = getState();
				if (c == 0)
					return false;
				int nextc = c - 1;
				if (compareAndSetState(c, nextc))
					return nextc == 0;
			}
		}
	}

	private final Sync sync;

	public CountLatch(int count) {
		this.sync = new Sync(count);
	}

	public void awaitZero() throws InterruptedException {









cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015






75




		sync.acquireShared(1);









adding CountLatch



 


Eric Bodden
committed
Jan 28, 2013






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




	}

	public boolean awaitZero(long timeout, TimeUnit unit) throws InterruptedException {
		return sync.tryAcquireSharedNanos(1, unit.toNanos(timeout));
	}

	public void increment() {
		sync.acquireNonBlocking(1);
	}

	public void decrement() {
		sync.releaseShared(1);
	}










Added counter resetting and thread interruption to fix #3


 

 


Marc-André Laverdière
committed
Sep 06, 2013






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




    /**
     * Resets the counter to zero. But waiting threads won't be released somehow.
     * So this interrupts the threads so that they escape from their waiting state.
     */
    public void resetAndInterrupt(){
        sync.reset();
        for (int i = 0; i < 3; i++) //Because it is a best effort thing, do it three times and hope for the best.
            for (Thread t : sync.getQueuedThreads())
                t.interrupt();
        sync.reset(); //Just in case a thread would've incremented the counter again.
    }










adding CountLatch



 


Eric Bodden
committed
Jan 28, 2013






102




103




104




105




	public String toString() {
		return super.toString() + "[Count = " + sync.getCount() + "]";
	}










Revert "adding CountLatch"


 

 


Eric Bodden
committed
Jan 28, 2013






106




}







adding CountLatch



 


Eric Bodden
committed
Jan 28, 2013



adding CountLatch



 

adding CountLatch


Eric Bodden
committed
Jan 28, 2013

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
/*******************************************************************************/******************************************************************************* * Copyright (c) 2012 Eric Bodden. * Copyright (c) 2012 Eric Bodden. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Eric Bodden - initial API and implementation *     Eric Bodden - initial API and implementation ******************************************************************************/ ******************************************************************************/package heros.solver;packageheros.solver;import java.util.concurrent.CountDownLatch;importjava.util.concurrent.CountDownLatch;import java.util.concurrent.TimeUnit;importjava.util.concurrent.TimeUnit;import java.util.concurrent.locks.AbstractQueuedSynchronizer;importjava.util.concurrent.locks.AbstractQueuedSynchronizer;/**/** * A synchronization aid similar to {@link CountDownLatch} but with the ability * A synchronization aid similar to {@link CountDownLatch} but with the ability * to also count up. This is useful to wait until a variable number of tasks * to also count up. This is useful to wait until a variable number of tasks * have completed. {@link #awaitZero()} will block until the count reaches zero. * have completed. {@link #awaitZero()} will block until the count reaches zero. */ */public class CountLatch {publicclassCountLatch{	@SuppressWarnings("serial")@SuppressWarnings("serial")	private static final class Sync extends AbstractQueuedSynchronizer {privatestaticfinalclassSyncextendsAbstractQueuedSynchronizer{		Sync(int count) {Sync(intcount){			setState(count);setState(count);		}}		int getCount() {intgetCount(){			return getState();returngetState();		}}



Added counter resetting and thread interruption to fix #3


 

 


Marc-André Laverdière
committed
Sep 06, 2013



Added counter resetting and thread interruption to fix #3


 

 

Added counter resetting and thread interruption to fix #3

 

Marc-André Laverdière
committed
Sep 06, 2013

35

36

37

38
        void reset() {voidreset(){            setState(0);setState(0);        }}



1) added some override annotations


 

 


Steven Arzt
committed
Oct 25, 2013



1) added some override annotations


 

 

1) added some override annotations

 

Steven Arzt
committed
Oct 25, 2013

39
        @Override@Override



adding CountLatch



 


Eric Bodden
committed
Jan 28, 2013



adding CountLatch



 

adding CountLatch


Eric Bodden
committed
Jan 28, 2013

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
		protected int tryAcquireShared(int acquires) {protectedinttryAcquireShared(intacquires){			return (getState() == 0) ? 1 : -1;return(getState()==0)?1:-1;		}}		protected int acquireNonBlocking(int acquires) {protectedintacquireNonBlocking(intacquires){			// increment count// increment count			for (;;) {for(;;){				int c = getState();intc=getState();				int nextc = c + 1;intnextc=c+1;				if (compareAndSetState(c, nextc))if(compareAndSetState(c,nextc))					return 1;return1;			}}		}}



1) added some override annotations


 

 


Steven Arzt
committed
Oct 25, 2013



1) added some override annotations


 

 

1) added some override annotations

 

Steven Arzt
committed
Oct 25, 2013

54
        @Override@Override



adding CountLatch



 


Eric Bodden
committed
Jan 28, 2013



adding CountLatch



 

adding CountLatch


Eric Bodden
committed
Jan 28, 2013

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
		protected boolean tryReleaseShared(int releases) {protectedbooleantryReleaseShared(intreleases){			// Decrement count; signal when transition to zero// Decrement count; signal when transition to zero			for (;;) {for(;;){				int c = getState();intc=getState();				if (c == 0)if(c==0)					return false;returnfalse;				int nextc = c - 1;intnextc=c-1;				if (compareAndSetState(c, nextc))if(compareAndSetState(c,nextc))					return nextc == 0;returnnextc==0;			}}		}}	}}	private final Sync sync;privatefinalSyncsync;	public CountLatch(int count) {publicCountLatch(intcount){		this.sync = new Sync(count);this.sync=newSync(count);	}}	public void awaitZero() throws InterruptedException {publicvoidawaitZero()throwsInterruptedException{



cleaning code


 

 


Johannes Lerch
committed
Jan 07, 2015



cleaning code


 

 

cleaning code

 

Johannes Lerch
committed
Jan 07, 2015

75
		sync.acquireShared(1);sync.acquireShared(1);



adding CountLatch



 


Eric Bodden
committed
Jan 28, 2013



adding CountLatch



 

adding CountLatch


Eric Bodden
committed
Jan 28, 2013

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
	}}	public boolean awaitZero(long timeout, TimeUnit unit) throws InterruptedException {publicbooleanawaitZero(longtimeout,TimeUnitunit)throwsInterruptedException{		return sync.tryAcquireSharedNanos(1, unit.toNanos(timeout));returnsync.tryAcquireSharedNanos(1,unit.toNanos(timeout));	}}	public void increment() {publicvoidincrement(){		sync.acquireNonBlocking(1);sync.acquireNonBlocking(1);	}}	public void decrement() {publicvoiddecrement(){		sync.releaseShared(1);sync.releaseShared(1);	}}



Added counter resetting and thread interruption to fix #3


 

 


Marc-André Laverdière
committed
Sep 06, 2013



Added counter resetting and thread interruption to fix #3


 

 

Added counter resetting and thread interruption to fix #3

 

Marc-André Laverdière
committed
Sep 06, 2013

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
    /**/**     * Resets the counter to zero. But waiting threads won't be released somehow.     * Resets the counter to zero. But waiting threads won't be released somehow.     * So this interrupts the threads so that they escape from their waiting state.     * So this interrupts the threads so that they escape from their waiting state.     */     */    public void resetAndInterrupt(){publicvoidresetAndInterrupt(){        sync.reset();sync.reset();        for (int i = 0; i < 3; i++) //Because it is a best effort thing, do it three times and hope for the best.for(inti=0;i<3;i++)//Because it is a best effort thing, do it three times and hope for the best.            for (Thread t : sync.getQueuedThreads())for(Threadt:sync.getQueuedThreads())                t.interrupt();t.interrupt();        sync.reset(); //Just in case a thread would've incremented the counter again.sync.reset();//Just in case a thread would've incremented the counter again.    }}



adding CountLatch



 


Eric Bodden
committed
Jan 28, 2013



adding CountLatch



 

adding CountLatch


Eric Bodden
committed
Jan 28, 2013

102

103

104

105
	public String toString() {publicStringtoString(){		return super.toString() + "[Count = " + sync.getCount() + "]";returnsuper.toString()+"[Count = "+sync.getCount()+"]";	}}



Revert "adding CountLatch"


 

 


Eric Bodden
committed
Jan 28, 2013



Revert "adding CountLatch"


 

 

Revert "adding CountLatch"

 

Eric Bodden
committed
Jan 28, 2013

106
}}





