



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

Repository


















b90d93333c0ac60f93cdbe88bca6cd88e8070f03


Switch branch/tag










heros


src


heros


solver


CountingThreadPoolExecutor.java



Find file
BlameHistoryPermalink










Better be careful with executors: If they are shutting down, no new tasks may...

·
3cad6e8e





Steven Arzt authored Oct 26, 2013

Better be careful with executors: If they are shutting down, no new tasks may be submitted - otherwise, we will just trigger another pointless exception. Secondly, if we increment the number of active executors and only afterwards actually submit the task, we have to make sure that the submission goes through - otherwise out counter is off by one! I now decrement it again if the executor rejects the new task.





3cad6e8e























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

Repository


















b90d93333c0ac60f93cdbe88bca6cd88e8070f03


Switch branch/tag










heros


src


heros


solver


CountingThreadPoolExecutor.java



Find file
BlameHistoryPermalink










Better be careful with executors: If they are shutting down, no new tasks may...

·
3cad6e8e





Steven Arzt authored Oct 26, 2013

Better be careful with executors: If they are shutting down, no new tasks may be submitted - otherwise, we will just trigger another pointless exception. Secondly, if we increment the number of active executors and only afterwards actually submit the task, we have to make sure that the submission goes through - otherwise out counter is off by one! I now decrement it again if the executor rejects the new task.





3cad6e8e






















Open sidebar



Joshua Garcia heros

Repository







Open sidebar



Joshua Garcia heros

Repository




Open sidebar

Joshua Garcia heros

Repository


Joshua Garciaherosheros
Repository











b90d93333c0ac60f93cdbe88bca6cd88e8070f03


Switch branch/tag










heros


src


heros


solver


CountingThreadPoolExecutor.java



Find file
BlameHistoryPermalink










Better be careful with executors: If they are shutting down, no new tasks may...

·
3cad6e8e





Steven Arzt authored Oct 26, 2013

Better be careful with executors: If they are shutting down, no new tasks may be submitted - otherwise, we will just trigger another pointless exception. Secondly, if we increment the number of active executors and only afterwards actually submit the task, we have to make sure that the submission goes through - otherwise out counter is off by one! I now decrement it again if the executor rejects the new task.





3cad6e8e

























b90d93333c0ac60f93cdbe88bca6cd88e8070f03


Switch branch/tag










heros


src


heros


solver


CountingThreadPoolExecutor.java



Find file
BlameHistoryPermalink










Better be careful with executors: If they are shutting down, no new tasks may...

·
3cad6e8e





Steven Arzt authored Oct 26, 2013

Better be careful with executors: If they are shutting down, no new tasks may be submitted - otherwise, we will just trigger another pointless exception. Secondly, if we increment the number of active executors and only afterwards actually submit the task, we have to make sure that the submission goes through - otherwise out counter is off by one! I now decrement it again if the executor rejects the new task.





3cad6e8e




















b90d93333c0ac60f93cdbe88bca6cd88e8070f03


Switch branch/tag










heros


src


heros


solver


CountingThreadPoolExecutor.java



Find file
BlameHistoryPermalink




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
BlameHistoryPermalink








Better be careful with executors: If they are shutting down, no new tasks may...

·
3cad6e8e





Steven Arzt authored Oct 26, 2013

Better be careful with executors: If they are shutting down, no new tasks may be submitted - otherwise, we will just trigger another pointless exception. Secondly, if we increment the number of active executors and only afterwards actually submit the task, we have to make sure that the submission goes through - otherwise out counter is off by one! I now decrement it again if the executor rejects the new task.





3cad6e8e

















Better be careful with executors: If they are shutting down, no new tasks may...

·
3cad6e8e





Steven Arzt authored Oct 26, 2013

Better be careful with executors: If they are shutting down, no new tasks may be submitted - otherwise, we will just trigger another pointless exception. Secondly, if we increment the number of active executors and only afterwards actually submit the task, we have to make sure that the submission goes through - otherwise out counter is off by one! I now decrement it again if the executor rejects the new task.





3cad6e8e













Better be careful with executors: If they are shutting down, no new tasks may...

·
3cad6e8e





Steven Arzt authored Oct 26, 2013

Better be careful with executors: If they are shutting down, no new tasks may be submitted - otherwise, we will just trigger another pointless exception. Secondly, if we increment the number of active executors and only afterwards actually submit the task, we have to make sure that the submission goes through - otherwise out counter is off by one! I now decrement it again if the executor rejects the new task.





3cad6e8e









Better be careful with executors: If they are shutting down, no new tasks may...

·
3cad6e8e





Steven Arzt authored Oct 26, 2013

Better be careful with executors: If they are shutting down, no new tasks may be submitted - otherwise, we will just trigger another pointless exception. Secondly, if we increment the number of active executors and only afterwards actually submit the task, we have to make sure that the submission goes through - otherwise out counter is off by one! I now decrement it again if the executor rejects the new task.





3cad6e8e





Better be careful with executors: If they are shutting down, no new tasks may...

·
3cad6e8e





Steven Arzt authored Oct 26, 2013

Better be careful with executors: If they are shutting down, no new tasks may be submitted - otherwise, we will just trigger another pointless exception. Secondly, if we increment the number of active executors and only afterwards actually submit the task, we have to make sure that the submission goes through - otherwise out counter is off by one! I now decrement it again if the executor rejects the new task.

·
3cad6e8e

Steven Arzt authored Oct 26, 2013




3cad6e8e





3cad6e8e



3cad6e8e












