



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


















25bbae8f27b3a296aa90fd93fa4a68fd6c5554c9


Switch branch/tag










heros


src


heros


solver


IDESolver.java



Find file
BlameHistoryPermalink










Refactored flow function computation to call FlowFunction.computeTargets in a...

·
25bbae8f





Steven Arzt authored Sep 18, 2013

Refactored flow function computation to call FlowFunction.computeTargets in a protected method (which can be overwritten by child classes) instead of directly in the solver logic.

The concrete use case was a return flow function which needed access to the context in which it was applied. With this refactoring, one can now simply subclass the solver, overwrite the new protected method and do one's problem-specific magic there.





25bbae8f























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


















25bbae8f27b3a296aa90fd93fa4a68fd6c5554c9


Switch branch/tag










heros


src


heros


solver


IDESolver.java



Find file
BlameHistoryPermalink










Refactored flow function computation to call FlowFunction.computeTargets in a...

·
25bbae8f





Steven Arzt authored Sep 18, 2013

Refactored flow function computation to call FlowFunction.computeTargets in a protected method (which can be overwritten by child classes) instead of directly in the solver logic.

The concrete use case was a return flow function which needed access to the context in which it was applied. With this refactoring, one can now simply subclass the solver, overwrite the new protected method and do one's problem-specific magic there.





25bbae8f






















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











25bbae8f27b3a296aa90fd93fa4a68fd6c5554c9


Switch branch/tag










heros


src


heros


solver


IDESolver.java



Find file
BlameHistoryPermalink










Refactored flow function computation to call FlowFunction.computeTargets in a...

·
25bbae8f





Steven Arzt authored Sep 18, 2013

Refactored flow function computation to call FlowFunction.computeTargets in a protected method (which can be overwritten by child classes) instead of directly in the solver logic.

The concrete use case was a return flow function which needed access to the context in which it was applied. With this refactoring, one can now simply subclass the solver, overwrite the new protected method and do one's problem-specific magic there.





25bbae8f

























25bbae8f27b3a296aa90fd93fa4a68fd6c5554c9


Switch branch/tag










heros


src


heros


solver


IDESolver.java



Find file
BlameHistoryPermalink










Refactored flow function computation to call FlowFunction.computeTargets in a...

·
25bbae8f





Steven Arzt authored Sep 18, 2013

Refactored flow function computation to call FlowFunction.computeTargets in a protected method (which can be overwritten by child classes) instead of directly in the solver logic.

The concrete use case was a return flow function which needed access to the context in which it was applied. With this refactoring, one can now simply subclass the solver, overwrite the new protected method and do one's problem-specific magic there.





25bbae8f




















25bbae8f27b3a296aa90fd93fa4a68fd6c5554c9


Switch branch/tag










heros


src


heros


solver


IDESolver.java



Find file
BlameHistoryPermalink




25bbae8f27b3a296aa90fd93fa4a68fd6c5554c9


Switch branch/tag










heros


src


heros


solver


IDESolver.java





25bbae8f27b3a296aa90fd93fa4a68fd6c5554c9


Switch branch/tag








25bbae8f27b3a296aa90fd93fa4a68fd6c5554c9


Switch branch/tag





25bbae8f27b3a296aa90fd93fa4a68fd6c5554c9

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








Refactored flow function computation to call FlowFunction.computeTargets in a...

·
25bbae8f





Steven Arzt authored Sep 18, 2013

Refactored flow function computation to call FlowFunction.computeTargets in a protected method (which can be overwritten by child classes) instead of directly in the solver logic.

The concrete use case was a return flow function which needed access to the context in which it was applied. With this refactoring, one can now simply subclass the solver, overwrite the new protected method and do one's problem-specific magic there.





25bbae8f

















Refactored flow function computation to call FlowFunction.computeTargets in a...

·
25bbae8f





Steven Arzt authored Sep 18, 2013

Refactored flow function computation to call FlowFunction.computeTargets in a protected method (which can be overwritten by child classes) instead of directly in the solver logic.

The concrete use case was a return flow function which needed access to the context in which it was applied. With this refactoring, one can now simply subclass the solver, overwrite the new protected method and do one's problem-specific magic there.





25bbae8f













Refactored flow function computation to call FlowFunction.computeTargets in a...

·
25bbae8f





Steven Arzt authored Sep 18, 2013

Refactored flow function computation to call FlowFunction.computeTargets in a protected method (which can be overwritten by child classes) instead of directly in the solver logic.

The concrete use case was a return flow function which needed access to the context in which it was applied. With this refactoring, one can now simply subclass the solver, overwrite the new protected method and do one's problem-specific magic there.





25bbae8f









Refactored flow function computation to call FlowFunction.computeTargets in a...

·
25bbae8f





Steven Arzt authored Sep 18, 2013

Refactored flow function computation to call FlowFunction.computeTargets in a protected method (which can be overwritten by child classes) instead of directly in the solver logic.

The concrete use case was a return flow function which needed access to the context in which it was applied. With this refactoring, one can now simply subclass the solver, overwrite the new protected method and do one's problem-specific magic there.





25bbae8f





Refactored flow function computation to call FlowFunction.computeTargets in a...

·
25bbae8f





Steven Arzt authored Sep 18, 2013

Refactored flow function computation to call FlowFunction.computeTargets in a protected method (which can be overwritten by child classes) instead of directly in the solver logic.

The concrete use case was a return flow function which needed access to the context in which it was applied. With this refactoring, one can now simply subclass the solver, overwrite the new protected method and do one's problem-specific magic there.

·
25bbae8f

Steven Arzt authored Sep 18, 2013




25bbae8f





25bbae8f



25bbae8f












