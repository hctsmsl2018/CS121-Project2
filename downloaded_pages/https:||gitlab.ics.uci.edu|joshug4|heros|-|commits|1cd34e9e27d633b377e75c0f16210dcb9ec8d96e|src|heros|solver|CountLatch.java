



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

Commits


















1cd34e9e27d633b377e75c0f16210dcb9ec8d96e


Switch branch/tag









herossrcherossolverCountLatch.java
















07 Jan, 2015
1 commit









cleaning code

·
1cd34e9e


Johannes Lerch authored Jan 07, 2015






1cd34e9e










25 Oct, 2013
1 commit









1) added some override annotations

·
20810d2e





Steven Arzt authored Oct 25, 2013

2) improved thread safety by not only copying the incoming set, but also the sets it contains since they may as well be changed by other threads while we try to iterate over them





20810d2e










06 Sep, 2013
1 commit









Added counter resetting and thread interruption to fix #3

·
f0141dde


Marc-André Laverdière authored Sep 06, 2013






f0141dde










28 Jan, 2013
2 commits









Revert "adding CountLatch"

·
db8c1e4a





Eric Bodden authored Jan 28, 2013


This reverts commit 31c5f3dd.

Revert "bugfix: must increment counter on task submission, not when task starts executing"

This reverts commit c8267b07.

Revert "Revert "optimized synchronization to work without busy loops""

This reverts commit 9229bc5c.

Revert "Revert "further cleanups""

This reverts commit f6eab2a7.

bugfix: must increment counter on task submission, not when task starts executing





db8c1e4a













adding CountLatch

·
31c5f3dd


Eric Bodden authored Jan 28, 2013






31c5f3dd










27 Jan, 2013
1 commit









Revert "optimized synchronization to work without busy loops"

·
9229bc5c





Eric Bodden authored Jan 27, 2013


This reverts commit 6f028b34.





9229bc5c










26 Jan, 2013
1 commit









optimized synchronization to work without busy loops

·
6f028b34


Eric Bodden authored Jan 26, 2013






6f028b34



















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

Commits


















1cd34e9e27d633b377e75c0f16210dcb9ec8d96e


Switch branch/tag









herossrcherossolverCountLatch.java
















07 Jan, 2015
1 commit









cleaning code

·
1cd34e9e


Johannes Lerch authored Jan 07, 2015






1cd34e9e










25 Oct, 2013
1 commit









1) added some override annotations

·
20810d2e





Steven Arzt authored Oct 25, 2013

2) improved thread safety by not only copying the incoming set, but also the sets it contains since they may as well be changed by other threads while we try to iterate over them





20810d2e










06 Sep, 2013
1 commit









Added counter resetting and thread interruption to fix #3

·
f0141dde


Marc-André Laverdière authored Sep 06, 2013






f0141dde










28 Jan, 2013
2 commits









Revert "adding CountLatch"

·
db8c1e4a





Eric Bodden authored Jan 28, 2013


This reverts commit 31c5f3dd.

Revert "bugfix: must increment counter on task submission, not when task starts executing"

This reverts commit c8267b07.

Revert "Revert "optimized synchronization to work without busy loops""

This reverts commit 9229bc5c.

Revert "Revert "further cleanups""

This reverts commit f6eab2a7.

bugfix: must increment counter on task submission, not when task starts executing





db8c1e4a













adding CountLatch

·
31c5f3dd


Eric Bodden authored Jan 28, 2013






31c5f3dd










27 Jan, 2013
1 commit









Revert "optimized synchronization to work without busy loops"

·
9229bc5c





Eric Bodden authored Jan 27, 2013


This reverts commit 6f028b34.





9229bc5c










26 Jan, 2013
1 commit









optimized synchronization to work without busy loops

·
6f028b34


Eric Bodden authored Jan 26, 2013






6f028b34


















Open sidebar



Joshua Garcia heros

Commits







Open sidebar



Joshua Garcia heros

Commits




Open sidebar

Joshua Garcia heros

Commits


Joshua Garciaherosheros
Commits











1cd34e9e27d633b377e75c0f16210dcb9ec8d96e


Switch branch/tag









herossrcherossolverCountLatch.java
















07 Jan, 2015
1 commit









cleaning code

·
1cd34e9e


Johannes Lerch authored Jan 07, 2015






1cd34e9e










25 Oct, 2013
1 commit









1) added some override annotations

·
20810d2e





Steven Arzt authored Oct 25, 2013

2) improved thread safety by not only copying the incoming set, but also the sets it contains since they may as well be changed by other threads while we try to iterate over them





20810d2e










06 Sep, 2013
1 commit









Added counter resetting and thread interruption to fix #3

·
f0141dde


Marc-André Laverdière authored Sep 06, 2013






f0141dde










28 Jan, 2013
2 commits









Revert "adding CountLatch"

·
db8c1e4a





Eric Bodden authored Jan 28, 2013


This reverts commit 31c5f3dd.

Revert "bugfix: must increment counter on task submission, not when task starts executing"

This reverts commit c8267b07.

Revert "Revert "optimized synchronization to work without busy loops""

This reverts commit 9229bc5c.

Revert "Revert "further cleanups""

This reverts commit f6eab2a7.

bugfix: must increment counter on task submission, not when task starts executing





db8c1e4a













adding CountLatch

·
31c5f3dd


Eric Bodden authored Jan 28, 2013






31c5f3dd










27 Jan, 2013
1 commit









Revert "optimized synchronization to work without busy loops"

·
9229bc5c





Eric Bodden authored Jan 27, 2013


This reverts commit 6f028b34.





9229bc5c










26 Jan, 2013
1 commit









optimized synchronization to work without busy loops

·
6f028b34


Eric Bodden authored Jan 26, 2013






6f028b34






















1cd34e9e27d633b377e75c0f16210dcb9ec8d96e


Switch branch/tag









herossrcherossolverCountLatch.java
















07 Jan, 2015
1 commit









cleaning code

·
1cd34e9e


Johannes Lerch authored Jan 07, 2015






1cd34e9e










25 Oct, 2013
1 commit









1) added some override annotations

·
20810d2e





Steven Arzt authored Oct 25, 2013

2) improved thread safety by not only copying the incoming set, but also the sets it contains since they may as well be changed by other threads while we try to iterate over them





20810d2e










06 Sep, 2013
1 commit









Added counter resetting and thread interruption to fix #3

·
f0141dde


Marc-André Laverdière authored Sep 06, 2013






f0141dde










28 Jan, 2013
2 commits









Revert "adding CountLatch"

·
db8c1e4a





Eric Bodden authored Jan 28, 2013


This reverts commit 31c5f3dd.

Revert "bugfix: must increment counter on task submission, not when task starts executing"

This reverts commit c8267b07.

Revert "Revert "optimized synchronization to work without busy loops""

This reverts commit 9229bc5c.

Revert "Revert "further cleanups""

This reverts commit f6eab2a7.

bugfix: must increment counter on task submission, not when task starts executing





db8c1e4a













adding CountLatch

·
31c5f3dd


Eric Bodden authored Jan 28, 2013






31c5f3dd










27 Jan, 2013
1 commit









Revert "optimized synchronization to work without busy loops"

·
9229bc5c





Eric Bodden authored Jan 27, 2013


This reverts commit 6f028b34.





9229bc5c










26 Jan, 2013
1 commit









optimized synchronization to work without busy loops

·
6f028b34


Eric Bodden authored Jan 26, 2013






6f028b34


















1cd34e9e27d633b377e75c0f16210dcb9ec8d96e


Switch branch/tag









herossrcherossolverCountLatch.java

















1cd34e9e27d633b377e75c0f16210dcb9ec8d96e


Switch branch/tag









herossrcherossolverCountLatch.java















1cd34e9e27d633b377e75c0f16210dcb9ec8d96e


Switch branch/tag









herossrcherossolverCountLatch.java




1cd34e9e27d633b377e75c0f16210dcb9ec8d96e


Switch branch/tag








1cd34e9e27d633b377e75c0f16210dcb9ec8d96e


Switch branch/tag





1cd34e9e27d633b377e75c0f16210dcb9ec8d96e

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tagherossrcherossolverCountLatch.java













07 Jan, 2015
1 commit









cleaning code

·
1cd34e9e


Johannes Lerch authored Jan 07, 2015






1cd34e9e










25 Oct, 2013
1 commit









1) added some override annotations

·
20810d2e





Steven Arzt authored Oct 25, 2013

2) improved thread safety by not only copying the incoming set, but also the sets it contains since they may as well be changed by other threads while we try to iterate over them





20810d2e










06 Sep, 2013
1 commit









Added counter resetting and thread interruption to fix #3

·
f0141dde


Marc-André Laverdière authored Sep 06, 2013






f0141dde










28 Jan, 2013
2 commits









Revert "adding CountLatch"

·
db8c1e4a





Eric Bodden authored Jan 28, 2013


This reverts commit 31c5f3dd.

Revert "bugfix: must increment counter on task submission, not when task starts executing"

This reverts commit c8267b07.

Revert "Revert "optimized synchronization to work without busy loops""

This reverts commit 9229bc5c.

Revert "Revert "further cleanups""

This reverts commit f6eab2a7.

bugfix: must increment counter on task submission, not when task starts executing





db8c1e4a













adding CountLatch

·
31c5f3dd


Eric Bodden authored Jan 28, 2013






31c5f3dd










27 Jan, 2013
1 commit









Revert "optimized synchronization to work without busy loops"

·
9229bc5c





Eric Bodden authored Jan 27, 2013


This reverts commit 6f028b34.





9229bc5c










26 Jan, 2013
1 commit









optimized synchronization to work without busy loops

·
6f028b34


Eric Bodden authored Jan 26, 2013






6f028b34











07 Jan, 2015
1 commit
07 Jan, 20151 commit







cleaning code

·
1cd34e9e


Johannes Lerch authored Jan 07, 2015






1cd34e9e














cleaning code

·
1cd34e9e


Johannes Lerch authored Jan 07, 2015






1cd34e9e










cleaning code

·
1cd34e9e


Johannes Lerch authored Jan 07, 2015






1cd34e9e






cleaning code

·
1cd34e9e


Johannes Lerch authored Jan 07, 2015


·
1cd34e9e

Johannes Lerch authored Jan 07, 2015




1cd34e9e






1cd34e9e




1cd34e9e

25 Oct, 2013
1 commit
25 Oct, 20131 commit







1) added some override annotations

·
20810d2e





Steven Arzt authored Oct 25, 2013

2) improved thread safety by not only copying the incoming set, but also the sets it contains since they may as well be changed by other threads while we try to iterate over them





20810d2e














1) added some override annotations

·
20810d2e





Steven Arzt authored Oct 25, 2013

2) improved thread safety by not only copying the incoming set, but also the sets it contains since they may as well be changed by other threads while we try to iterate over them





20810d2e










1) added some override annotations

·
20810d2e





Steven Arzt authored Oct 25, 2013

2) improved thread safety by not only copying the incoming set, but also the sets it contains since they may as well be changed by other threads while we try to iterate over them





20810d2e






1) added some override annotations

·
20810d2e





Steven Arzt authored Oct 25, 2013

2) improved thread safety by not only copying the incoming set, but also the sets it contains since they may as well be changed by other threads while we try to iterate over them

·
20810d2e

Steven Arzt authored Oct 25, 2013




20810d2e






20810d2e




20810d2e

06 Sep, 2013
1 commit
06 Sep, 20131 commit







Added counter resetting and thread interruption to fix #3

·
f0141dde


Marc-André Laverdière authored Sep 06, 2013






f0141dde














Added counter resetting and thread interruption to fix #3

·
f0141dde


Marc-André Laverdière authored Sep 06, 2013






f0141dde










Added counter resetting and thread interruption to fix #3

·
f0141dde


Marc-André Laverdière authored Sep 06, 2013






f0141dde






Added counter resetting and thread interruption to fix #3

·
f0141dde


Marc-André Laverdière authored Sep 06, 2013


·
f0141dde

Marc-André Laverdière authored Sep 06, 2013




f0141dde






f0141dde




f0141dde

28 Jan, 2013
2 commits
28 Jan, 20132 commits







Revert "adding CountLatch"

·
db8c1e4a





Eric Bodden authored Jan 28, 2013


This reverts commit 31c5f3dd.

Revert "bugfix: must increment counter on task submission, not when task starts executing"

This reverts commit c8267b07.

Revert "Revert "optimized synchronization to work without busy loops""

This reverts commit 9229bc5c.

Revert "Revert "further cleanups""

This reverts commit f6eab2a7.

bugfix: must increment counter on task submission, not when task starts executing





db8c1e4a













adding CountLatch

·
31c5f3dd


Eric Bodden authored Jan 28, 2013






31c5f3dd














Revert "adding CountLatch"

·
db8c1e4a





Eric Bodden authored Jan 28, 2013


This reverts commit 31c5f3dd.

Revert "bugfix: must increment counter on task submission, not when task starts executing"

This reverts commit c8267b07.

Revert "Revert "optimized synchronization to work without busy loops""

This reverts commit 9229bc5c.

Revert "Revert "further cleanups""

This reverts commit f6eab2a7.

bugfix: must increment counter on task submission, not when task starts executing





db8c1e4a










Revert "adding CountLatch"

·
db8c1e4a





Eric Bodden authored Jan 28, 2013


This reverts commit 31c5f3dd.

Revert "bugfix: must increment counter on task submission, not when task starts executing"

This reverts commit c8267b07.

Revert "Revert "optimized synchronization to work without busy loops""

This reverts commit 9229bc5c.

Revert "Revert "further cleanups""

This reverts commit f6eab2a7.

bugfix: must increment counter on task submission, not when task starts executing





db8c1e4a






Revert "adding CountLatch"

·
db8c1e4a





Eric Bodden authored Jan 28, 2013


This reverts commit 31c5f3dd.

Revert "bugfix: must increment counter on task submission, not when task starts executing"

This reverts commit c8267b07.

Revert "Revert "optimized synchronization to work without busy loops""

This reverts commit 9229bc5c.

Revert "Revert "further cleanups""

This reverts commit f6eab2a7.

bugfix: must increment counter on task submission, not when task starts executing

·
db8c1e4a

Eric Bodden authored Jan 28, 2013




db8c1e4a






db8c1e4a




db8c1e4a






adding CountLatch

·
31c5f3dd


Eric Bodden authored Jan 28, 2013






31c5f3dd










adding CountLatch

·
31c5f3dd


Eric Bodden authored Jan 28, 2013






31c5f3dd






adding CountLatch

·
31c5f3dd


Eric Bodden authored Jan 28, 2013


·
31c5f3dd

Eric Bodden authored Jan 28, 2013




31c5f3dd






31c5f3dd




31c5f3dd

27 Jan, 2013
1 commit
27 Jan, 20131 commit







Revert "optimized synchronization to work without busy loops"

·
9229bc5c





Eric Bodden authored Jan 27, 2013


This reverts commit 6f028b34.





9229bc5c














Revert "optimized synchronization to work without busy loops"

·
9229bc5c





Eric Bodden authored Jan 27, 2013


This reverts commit 6f028b34.





9229bc5c










Revert "optimized synchronization to work without busy loops"

·
9229bc5c





Eric Bodden authored Jan 27, 2013


This reverts commit 6f028b34.





9229bc5c






Revert "optimized synchronization to work without busy loops"

·
9229bc5c





Eric Bodden authored Jan 27, 2013


This reverts commit 6f028b34.

·
9229bc5c

Eric Bodden authored Jan 27, 2013




9229bc5c






9229bc5c




9229bc5c

26 Jan, 2013
1 commit
26 Jan, 20131 commit







optimized synchronization to work without busy loops

·
6f028b34


Eric Bodden authored Jan 26, 2013






6f028b34














optimized synchronization to work without busy loops

·
6f028b34


Eric Bodden authored Jan 26, 2013






6f028b34










optimized synchronization to work without busy loops

·
6f028b34


Eric Bodden authored Jan 26, 2013






6f028b34






optimized synchronization to work without busy loops

·
6f028b34


Eric Bodden authored Jan 26, 2013


·
6f028b34

Eric Bodden authored Jan 26, 2013




6f028b34






6f028b34




6f028b34






