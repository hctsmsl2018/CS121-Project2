



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


















5b67f79daa363c52fd3b7c8dff9c2626001c80c4


Switch branch/tag










heros


src


heros


solver


IDESolver.java



Find file
BlameHistoryPermalink










Fix for a long outstanding bug in the value computation of the IDE phase if...

·
5b67f79d





Eric Bodden authored Jan 25, 2015

Fix for a long outstanding bug in the value computation of the IDE phase if followReturnPastSeeds is used. Looking at the original IDE paper, one will see that in phase II the algorithm does the following:
1.) propagate values from all start nodes to call nodes and from call nodes to start nodes of callees (!!!)
2.) propagate from all start nodes to all non call-start nodes
In Heros, however, we do not propagate from *all* start nodes to call nodes but only from the initial seeds!
The problem is now that with followReturnPastSeeds=true we have an unbalanced problem, in which we propagate into callers that have no calls ever processed. In result, propagating from initial seeds to calls is not enough, as this will only enable value computation in callees, but not in callers.
The implemented fix memorizes all values propagated into callers in an unbalanced way as so-called "caller seeds" and then uses these also as seeds for value computation. This should fix the problem without having to compute values starting at any start node of any procedure of the program.





5b67f79d























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


















5b67f79daa363c52fd3b7c8dff9c2626001c80c4


Switch branch/tag










heros


src


heros


solver


IDESolver.java



Find file
BlameHistoryPermalink










Fix for a long outstanding bug in the value computation of the IDE phase if...

·
5b67f79d





Eric Bodden authored Jan 25, 2015

Fix for a long outstanding bug in the value computation of the IDE phase if followReturnPastSeeds is used. Looking at the original IDE paper, one will see that in phase II the algorithm does the following:
1.) propagate values from all start nodes to call nodes and from call nodes to start nodes of callees (!!!)
2.) propagate from all start nodes to all non call-start nodes
In Heros, however, we do not propagate from *all* start nodes to call nodes but only from the initial seeds!
The problem is now that with followReturnPastSeeds=true we have an unbalanced problem, in which we propagate into callers that have no calls ever processed. In result, propagating from initial seeds to calls is not enough, as this will only enable value computation in callees, but not in callers.
The implemented fix memorizes all values propagated into callers in an unbalanced way as so-called "caller seeds" and then uses these also as seeds for value computation. This should fix the problem without having to compute values starting at any start node of any procedure of the program.





5b67f79d






















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











5b67f79daa363c52fd3b7c8dff9c2626001c80c4


Switch branch/tag










heros


src


heros


solver


IDESolver.java



Find file
BlameHistoryPermalink










Fix for a long outstanding bug in the value computation of the IDE phase if...

·
5b67f79d





Eric Bodden authored Jan 25, 2015

Fix for a long outstanding bug in the value computation of the IDE phase if followReturnPastSeeds is used. Looking at the original IDE paper, one will see that in phase II the algorithm does the following:
1.) propagate values from all start nodes to call nodes and from call nodes to start nodes of callees (!!!)
2.) propagate from all start nodes to all non call-start nodes
In Heros, however, we do not propagate from *all* start nodes to call nodes but only from the initial seeds!
The problem is now that with followReturnPastSeeds=true we have an unbalanced problem, in which we propagate into callers that have no calls ever processed. In result, propagating from initial seeds to calls is not enough, as this will only enable value computation in callees, but not in callers.
The implemented fix memorizes all values propagated into callers in an unbalanced way as so-called "caller seeds" and then uses these also as seeds for value computation. This should fix the problem without having to compute values starting at any start node of any procedure of the program.





5b67f79d

























5b67f79daa363c52fd3b7c8dff9c2626001c80c4


Switch branch/tag










heros


src


heros


solver


IDESolver.java



Find file
BlameHistoryPermalink










Fix for a long outstanding bug in the value computation of the IDE phase if...

·
5b67f79d





Eric Bodden authored Jan 25, 2015

Fix for a long outstanding bug in the value computation of the IDE phase if followReturnPastSeeds is used. Looking at the original IDE paper, one will see that in phase II the algorithm does the following:
1.) propagate values from all start nodes to call nodes and from call nodes to start nodes of callees (!!!)
2.) propagate from all start nodes to all non call-start nodes
In Heros, however, we do not propagate from *all* start nodes to call nodes but only from the initial seeds!
The problem is now that with followReturnPastSeeds=true we have an unbalanced problem, in which we propagate into callers that have no calls ever processed. In result, propagating from initial seeds to calls is not enough, as this will only enable value computation in callees, but not in callers.
The implemented fix memorizes all values propagated into callers in an unbalanced way as so-called "caller seeds" and then uses these also as seeds for value computation. This should fix the problem without having to compute values starting at any start node of any procedure of the program.





5b67f79d




















5b67f79daa363c52fd3b7c8dff9c2626001c80c4


Switch branch/tag










heros


src


heros


solver


IDESolver.java



Find file
BlameHistoryPermalink




5b67f79daa363c52fd3b7c8dff9c2626001c80c4


Switch branch/tag










heros


src


heros


solver


IDESolver.java





5b67f79daa363c52fd3b7c8dff9c2626001c80c4


Switch branch/tag








5b67f79daa363c52fd3b7c8dff9c2626001c80c4


Switch branch/tag





5b67f79daa363c52fd3b7c8dff9c2626001c80c4

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src

heros

solver

IDESolver.java
Find file
BlameHistoryPermalink








Fix for a long outstanding bug in the value computation of the IDE phase if...

·
5b67f79d





Eric Bodden authored Jan 25, 2015

Fix for a long outstanding bug in the value computation of the IDE phase if followReturnPastSeeds is used. Looking at the original IDE paper, one will see that in phase II the algorithm does the following:
1.) propagate values from all start nodes to call nodes and from call nodes to start nodes of callees (!!!)
2.) propagate from all start nodes to all non call-start nodes
In Heros, however, we do not propagate from *all* start nodes to call nodes but only from the initial seeds!
The problem is now that with followReturnPastSeeds=true we have an unbalanced problem, in which we propagate into callers that have no calls ever processed. In result, propagating from initial seeds to calls is not enough, as this will only enable value computation in callees, but not in callers.
The implemented fix memorizes all values propagated into callers in an unbalanced way as so-called "caller seeds" and then uses these also as seeds for value computation. This should fix the problem without having to compute values starting at any start node of any procedure of the program.





5b67f79d

















Fix for a long outstanding bug in the value computation of the IDE phase if...

·
5b67f79d





Eric Bodden authored Jan 25, 2015

Fix for a long outstanding bug in the value computation of the IDE phase if followReturnPastSeeds is used. Looking at the original IDE paper, one will see that in phase II the algorithm does the following:
1.) propagate values from all start nodes to call nodes and from call nodes to start nodes of callees (!!!)
2.) propagate from all start nodes to all non call-start nodes
In Heros, however, we do not propagate from *all* start nodes to call nodes but only from the initial seeds!
The problem is now that with followReturnPastSeeds=true we have an unbalanced problem, in which we propagate into callers that have no calls ever processed. In result, propagating from initial seeds to calls is not enough, as this will only enable value computation in callees, but not in callers.
The implemented fix memorizes all values propagated into callers in an unbalanced way as so-called "caller seeds" and then uses these also as seeds for value computation. This should fix the problem without having to compute values starting at any start node of any procedure of the program.





5b67f79d













Fix for a long outstanding bug in the value computation of the IDE phase if...

·
5b67f79d





Eric Bodden authored Jan 25, 2015

Fix for a long outstanding bug in the value computation of the IDE phase if followReturnPastSeeds is used. Looking at the original IDE paper, one will see that in phase II the algorithm does the following:
1.) propagate values from all start nodes to call nodes and from call nodes to start nodes of callees (!!!)
2.) propagate from all start nodes to all non call-start nodes
In Heros, however, we do not propagate from *all* start nodes to call nodes but only from the initial seeds!
The problem is now that with followReturnPastSeeds=true we have an unbalanced problem, in which we propagate into callers that have no calls ever processed. In result, propagating from initial seeds to calls is not enough, as this will only enable value computation in callees, but not in callers.
The implemented fix memorizes all values propagated into callers in an unbalanced way as so-called "caller seeds" and then uses these also as seeds for value computation. This should fix the problem without having to compute values starting at any start node of any procedure of the program.





5b67f79d









Fix for a long outstanding bug in the value computation of the IDE phase if...

·
5b67f79d





Eric Bodden authored Jan 25, 2015

Fix for a long outstanding bug in the value computation of the IDE phase if followReturnPastSeeds is used. Looking at the original IDE paper, one will see that in phase II the algorithm does the following:
1.) propagate values from all start nodes to call nodes and from call nodes to start nodes of callees (!!!)
2.) propagate from all start nodes to all non call-start nodes
In Heros, however, we do not propagate from *all* start nodes to call nodes but only from the initial seeds!
The problem is now that with followReturnPastSeeds=true we have an unbalanced problem, in which we propagate into callers that have no calls ever processed. In result, propagating from initial seeds to calls is not enough, as this will only enable value computation in callees, but not in callers.
The implemented fix memorizes all values propagated into callers in an unbalanced way as so-called "caller seeds" and then uses these also as seeds for value computation. This should fix the problem without having to compute values starting at any start node of any procedure of the program.





5b67f79d





Fix for a long outstanding bug in the value computation of the IDE phase if...

·
5b67f79d





Eric Bodden authored Jan 25, 2015

Fix for a long outstanding bug in the value computation of the IDE phase if followReturnPastSeeds is used. Looking at the original IDE paper, one will see that in phase II the algorithm does the following:
1.) propagate values from all start nodes to call nodes and from call nodes to start nodes of callees (!!!)
2.) propagate from all start nodes to all non call-start nodes
In Heros, however, we do not propagate from *all* start nodes to call nodes but only from the initial seeds!
The problem is now that with followReturnPastSeeds=true we have an unbalanced problem, in which we propagate into callers that have no calls ever processed. In result, propagating from initial seeds to calls is not enough, as this will only enable value computation in callees, but not in callers.
The implemented fix memorizes all values propagated into callers in an unbalanced way as so-called "caller seeds" and then uses these also as seeds for value computation. This should fix the problem without having to compute values starting at any start node of any procedure of the program.

·
5b67f79d

Eric Bodden authored Jan 25, 2015




5b67f79d





5b67f79d



5b67f79d












