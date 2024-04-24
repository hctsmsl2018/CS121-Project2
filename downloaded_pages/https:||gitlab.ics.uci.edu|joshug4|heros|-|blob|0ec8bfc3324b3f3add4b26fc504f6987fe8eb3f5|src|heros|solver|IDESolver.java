



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


















0ec8bfc3324b3f3add4b26fc504f6987fe8eb3f5


Switch branch/tag










heros


src


heros


solver


IDESolver.java



Find file
BlameHistoryPermalink










fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

·
0ec8bfc3





Steven Arzt authored Jun 16, 2013

fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return edge gets processed for **all callers** of the callee from which we return in cases where we haven't seen the respective call edge.

This however leads to spurious edges if we have multiple call sites for the same callee. Let us assume call site A calls foo() with jump function a and call site B calls it with b. If we now return from foo with something derived from a, we will not find an incoming edge for call site B. FollowReturnsPastSeeds thus made us propagate the value to both call sites which makes us loose context sensitivity.

Fix as follows: Only follow past seeds if we have seen no call edge into the callee at all. We can then assume that we're really running beyond the seeds of the analysis and have no other chance than propagating to all call sites.





0ec8bfc3























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


















0ec8bfc3324b3f3add4b26fc504f6987fe8eb3f5


Switch branch/tag










heros


src


heros


solver


IDESolver.java



Find file
BlameHistoryPermalink










fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

·
0ec8bfc3





Steven Arzt authored Jun 16, 2013

fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return edge gets processed for **all callers** of the callee from which we return in cases where we haven't seen the respective call edge.

This however leads to spurious edges if we have multiple call sites for the same callee. Let us assume call site A calls foo() with jump function a and call site B calls it with b. If we now return from foo with something derived from a, we will not find an incoming edge for call site B. FollowReturnsPastSeeds thus made us propagate the value to both call sites which makes us loose context sensitivity.

Fix as follows: Only follow past seeds if we have seen no call edge into the callee at all. We can then assume that we're really running beyond the seeds of the analysis and have no other chance than propagating to all call sites.





0ec8bfc3






















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











0ec8bfc3324b3f3add4b26fc504f6987fe8eb3f5


Switch branch/tag










heros


src


heros


solver


IDESolver.java



Find file
BlameHistoryPermalink










fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

·
0ec8bfc3





Steven Arzt authored Jun 16, 2013

fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return edge gets processed for **all callers** of the callee from which we return in cases where we haven't seen the respective call edge.

This however leads to spurious edges if we have multiple call sites for the same callee. Let us assume call site A calls foo() with jump function a and call site B calls it with b. If we now return from foo with something derived from a, we will not find an incoming edge for call site B. FollowReturnsPastSeeds thus made us propagate the value to both call sites which makes us loose context sensitivity.

Fix as follows: Only follow past seeds if we have seen no call edge into the callee at all. We can then assume that we're really running beyond the seeds of the analysis and have no other chance than propagating to all call sites.





0ec8bfc3

























0ec8bfc3324b3f3add4b26fc504f6987fe8eb3f5


Switch branch/tag










heros


src


heros


solver


IDESolver.java



Find file
BlameHistoryPermalink










fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

·
0ec8bfc3





Steven Arzt authored Jun 16, 2013

fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return edge gets processed for **all callers** of the callee from which we return in cases where we haven't seen the respective call edge.

This however leads to spurious edges if we have multiple call sites for the same callee. Let us assume call site A calls foo() with jump function a and call site B calls it with b. If we now return from foo with something derived from a, we will not find an incoming edge for call site B. FollowReturnsPastSeeds thus made us propagate the value to both call sites which makes us loose context sensitivity.

Fix as follows: Only follow past seeds if we have seen no call edge into the callee at all. We can then assume that we're really running beyond the seeds of the analysis and have no other chance than propagating to all call sites.





0ec8bfc3




















0ec8bfc3324b3f3add4b26fc504f6987fe8eb3f5


Switch branch/tag










heros


src


heros


solver


IDESolver.java



Find file
BlameHistoryPermalink




0ec8bfc3324b3f3add4b26fc504f6987fe8eb3f5


Switch branch/tag










heros


src


heros


solver


IDESolver.java





0ec8bfc3324b3f3add4b26fc504f6987fe8eb3f5


Switch branch/tag








0ec8bfc3324b3f3add4b26fc504f6987fe8eb3f5


Switch branch/tag





0ec8bfc3324b3f3add4b26fc504f6987fe8eb3f5

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








fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

·
0ec8bfc3





Steven Arzt authored Jun 16, 2013

fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return edge gets processed for **all callers** of the callee from which we return in cases where we haven't seen the respective call edge.

This however leads to spurious edges if we have multiple call sites for the same callee. Let us assume call site A calls foo() with jump function a and call site B calls it with b. If we now return from foo with something derived from a, we will not find an incoming edge for call site B. FollowReturnsPastSeeds thus made us propagate the value to both call sites which makes us loose context sensitivity.

Fix as follows: Only follow past seeds if we have seen no call edge into the callee at all. We can then assume that we're really running beyond the seeds of the analysis and have no other chance than propagating to all call sites.





0ec8bfc3

















fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

·
0ec8bfc3





Steven Arzt authored Jun 16, 2013

fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return edge gets processed for **all callers** of the callee from which we return in cases where we haven't seen the respective call edge.

This however leads to spurious edges if we have multiple call sites for the same callee. Let us assume call site A calls foo() with jump function a and call site B calls it with b. If we now return from foo with something derived from a, we will not find an incoming edge for call site B. FollowReturnsPastSeeds thus made us propagate the value to both call sites which makes us loose context sensitivity.

Fix as follows: Only follow past seeds if we have seen no call edge into the callee at all. We can then assume that we're really running beyond the seeds of the analysis and have no other chance than propagating to all call sites.





0ec8bfc3













fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

·
0ec8bfc3





Steven Arzt authored Jun 16, 2013

fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return edge gets processed for **all callers** of the callee from which we return in cases where we haven't seen the respective call edge.

This however leads to spurious edges if we have multiple call sites for the same callee. Let us assume call site A calls foo() with jump function a and call site B calls it with b. If we now return from foo with something derived from a, we will not find an incoming edge for call site B. FollowReturnsPastSeeds thus made us propagate the value to both call sites which makes us loose context sensitivity.

Fix as follows: Only follow past seeds if we have seen no call edge into the callee at all. We can then assume that we're really running beyond the seeds of the analysis and have no other chance than propagating to all call sites.





0ec8bfc3









fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

·
0ec8bfc3





Steven Arzt authored Jun 16, 2013

fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return edge gets processed for **all callers** of the callee from which we return in cases where we haven't seen the respective call edge.

This however leads to spurious edges if we have multiple call sites for the same callee. Let us assume call site A calls foo() with jump function a and call site B calls it with b. If we now return from foo with something derived from a, we will not find an incoming edge for call site B. FollowReturnsPastSeeds thus made us propagate the value to both call sites which makes us loose context sensitivity.

Fix as follows: Only follow past seeds if we have seen no call edge into the callee at all. We can then assume that we're really running beyond the seeds of the analysis and have no other chance than propagating to all call sites.





0ec8bfc3





fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

·
0ec8bfc3





Steven Arzt authored Jun 16, 2013

fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return edge gets processed for **all callers** of the callee from which we return in cases where we haven't seen the respective call edge.

This however leads to spurious edges if we have multiple call sites for the same callee. Let us assume call site A calls foo() with jump function a and call site B calls it with b. If we now return from foo with something derived from a, we will not find an incoming edge for call site B. FollowReturnsPastSeeds thus made us propagate the value to both call sites which makes us loose context sensitivity.

Fix as follows: Only follow past seeds if we have seen no call edge into the callee at all. We can then assume that we're really running beyond the seeds of the analysis and have no other chance than propagating to all call sites.

·
0ec8bfc3

Steven Arzt authored Jun 16, 2013




0ec8bfc3





0ec8bfc3



0ec8bfc3












