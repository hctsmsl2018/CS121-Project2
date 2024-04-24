



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


















2e040141a65d0ea58f1aac16b420e19d379db342


Switch branch/tag










heros


src


heros


solver


IDESolver.java



Find file
BlameHistoryPermalink










Final (?) fix for fix intended in 5b67f79d

·
2e040141





Eric Bodden authored Jan 26, 2015


It turns out that for value computation we only need to store the return sites to which we return in an unbalanced way. Then, at these sites, we *always* look up the edge function that goes to this site from the method's start point and the ZERO node! Previously, we did the look not using the ZERO node but using the original source of the path edge in the callee, which is wrong. Fortunately, the fix also makes for an easier implementation.





2e040141























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


















2e040141a65d0ea58f1aac16b420e19d379db342


Switch branch/tag










heros


src


heros


solver


IDESolver.java



Find file
BlameHistoryPermalink










Final (?) fix for fix intended in 5b67f79d

·
2e040141





Eric Bodden authored Jan 26, 2015


It turns out that for value computation we only need to store the return sites to which we return in an unbalanced way. Then, at these sites, we *always* look up the edge function that goes to this site from the method's start point and the ZERO node! Previously, we did the look not using the ZERO node but using the original source of the path edge in the callee, which is wrong. Fortunately, the fix also makes for an easier implementation.





2e040141






















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











2e040141a65d0ea58f1aac16b420e19d379db342


Switch branch/tag










heros


src


heros


solver


IDESolver.java



Find file
BlameHistoryPermalink










Final (?) fix for fix intended in 5b67f79d

·
2e040141





Eric Bodden authored Jan 26, 2015


It turns out that for value computation we only need to store the return sites to which we return in an unbalanced way. Then, at these sites, we *always* look up the edge function that goes to this site from the method's start point and the ZERO node! Previously, we did the look not using the ZERO node but using the original source of the path edge in the callee, which is wrong. Fortunately, the fix also makes for an easier implementation.





2e040141

























2e040141a65d0ea58f1aac16b420e19d379db342


Switch branch/tag










heros


src


heros


solver


IDESolver.java



Find file
BlameHistoryPermalink










Final (?) fix for fix intended in 5b67f79d

·
2e040141





Eric Bodden authored Jan 26, 2015


It turns out that for value computation we only need to store the return sites to which we return in an unbalanced way. Then, at these sites, we *always* look up the edge function that goes to this site from the method's start point and the ZERO node! Previously, we did the look not using the ZERO node but using the original source of the path edge in the callee, which is wrong. Fortunately, the fix also makes for an easier implementation.





2e040141




















2e040141a65d0ea58f1aac16b420e19d379db342


Switch branch/tag










heros


src


heros


solver


IDESolver.java



Find file
BlameHistoryPermalink




2e040141a65d0ea58f1aac16b420e19d379db342


Switch branch/tag










heros


src


heros


solver


IDESolver.java





2e040141a65d0ea58f1aac16b420e19d379db342


Switch branch/tag








2e040141a65d0ea58f1aac16b420e19d379db342


Switch branch/tag





2e040141a65d0ea58f1aac16b420e19d379db342

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








Final (?) fix for fix intended in 5b67f79d

·
2e040141





Eric Bodden authored Jan 26, 2015


It turns out that for value computation we only need to store the return sites to which we return in an unbalanced way. Then, at these sites, we *always* look up the edge function that goes to this site from the method's start point and the ZERO node! Previously, we did the look not using the ZERO node but using the original source of the path edge in the callee, which is wrong. Fortunately, the fix also makes for an easier implementation.





2e040141

















Final (?) fix for fix intended in 5b67f79d

·
2e040141





Eric Bodden authored Jan 26, 2015


It turns out that for value computation we only need to store the return sites to which we return in an unbalanced way. Then, at these sites, we *always* look up the edge function that goes to this site from the method's start point and the ZERO node! Previously, we did the look not using the ZERO node but using the original source of the path edge in the callee, which is wrong. Fortunately, the fix also makes for an easier implementation.





2e040141













Final (?) fix for fix intended in 5b67f79d

·
2e040141





Eric Bodden authored Jan 26, 2015


It turns out that for value computation we only need to store the return sites to which we return in an unbalanced way. Then, at these sites, we *always* look up the edge function that goes to this site from the method's start point and the ZERO node! Previously, we did the look not using the ZERO node but using the original source of the path edge in the callee, which is wrong. Fortunately, the fix also makes for an easier implementation.





2e040141









Final (?) fix for fix intended in 5b67f79d

·
2e040141





Eric Bodden authored Jan 26, 2015


It turns out that for value computation we only need to store the return sites to which we return in an unbalanced way. Then, at these sites, we *always* look up the edge function that goes to this site from the method's start point and the ZERO node! Previously, we did the look not using the ZERO node but using the original source of the path edge in the callee, which is wrong. Fortunately, the fix also makes for an easier implementation.





2e040141





Final (?) fix for fix intended in 5b67f79d

·
2e040141





Eric Bodden authored Jan 26, 2015


It turns out that for value computation we only need to store the return sites to which we return in an unbalanced way. Then, at these sites, we *always* look up the edge function that goes to this site from the method's start point and the ZERO node! Previously, we did the look not using the ZERO node but using the original source of the path edge in the callee, which is wrong. Fortunately, the fix also makes for an easier implementation.

·
2e040141

Eric Bodden authored Jan 26, 2015




2e040141





2e040141



2e040141












