



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


















aa036d4b77ddac6dfefa6a9ecbfd1c905ed48812


Switch branch/tag









herossrcherossolverIDESolver.java
















28 Jan, 2013
3 commits









comments

·
aa036d4b


Eric Bodden authored Jan 28, 2013






aa036d4b













refactoring

·
b9930158


Eric Bodden authored Jan 28, 2013






b9930158













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










27 Jan, 2013
2 commits









Revert "optimized synchronization to work without busy loops"

·
9229bc5c





Eric Bodden authored Jan 27, 2013


This reverts commit 6f028b34.





9229bc5c













Revert "further cleanups"

·
f6eab2a7





Eric Bodden authored Jan 27, 2013


This reverts commit 5502e147.





f6eab2a7










26 Jan, 2013
3 commits









further cleanups

·
5502e147


Eric Bodden authored Jan 26, 2013






5502e147













optimized synchronization to work without busy loops

·
6f028b34


Eric Bodden authored Jan 26, 2013






6f028b34













minor cleanups

·
cdf8292c


Eric Bodden authored Jan 26, 2013






cdf8292c










25 Jan, 2013
1 commit









Fixed race condition in IDESolver and simplified the code

·
0a1e0ff5


Marc-André Laverdière authored Jan 25, 2013






0a1e0ff5










24 Jan, 2013
2 commits









replaced env variable by property

·
7afff058


Eric Bodden authored Jan 24, 2013






7afff058













added env flag HEROS_DEBUG

·
24fd6856


Eric Bodden authored Jan 24, 2013






24fd6856










22 Jan, 2013
1 commit









added possibility to switch off auto-zeroing of flow functions

·
0563038b


Eric Bodden authored Jan 22, 2013






0563038b










08 Jan, 2013
1 commit









fixing race condition found by Steven Arzt

·
2e63b6e7


Eric Bodden authored Jan 08, 2013






2e63b6e7










17 Dec, 2012
3 commits









debug flag

·
3d1f0a94


Eric Bodden authored Dec 17, 2012






3d1f0a94













bug fix against duplicate edges in unbalanced problems

·
8b8d8b3d


Eric Bodden authored Dec 17, 2012






8b8d8b3d













improved handling of unbalanced problems

·
60052739


Eric Bodden authored Dec 17, 2012






60052739










12 Dec, 2012
8 commits









making computation of unbalanced edges optional

·
15b0a59b


Eric Bodden authored Dec 12, 2012






15b0a59b













initial handling for unbalanced problems;

·
4f2738f5





Eric Bodden authored Dec 12, 2012

still need to make this optional, as otherwise we propagate too many useless flows by default





4f2738f5













undoing previous "fix"; as discussed with Steven, it is not required (see comment)

·
e86b976f


Eric Bodden authored Dec 12, 2012






e86b976f













comments and minor optimizations

·
62f80f72


Eric Bodden authored Dec 12, 2012






62f80f72













removed caller-side summary functions; instead now just use callee-side "endSummaries"

·
3458f830


Eric Bodden authored Dec 12, 2012






3458f830













fix for bug found in code review: if a summary function exists already we may...

·
67675fc9





Eric Bodden authored Dec 12, 2012

fix for bug found in code review: if a summary function exists already we may not just overwrite it;
instead join it with the new one!





67675fc9













implemented a small optimization in processExit: propagate intra-procedural...

·
2026bb15





Eric Bodden authored Dec 12, 2012

implemented a small optimization in processExit: propagate intra-procedural flows only if summary function was updated
also added implementation-level comments





2026bb15













reordered some methods

·
bcfdf777


Eric Bodden authored Dec 12, 2012






bcfdf777










11 Dec, 2012
1 commit









disabled debugging

·
0378270e


Eric Bodden authored Dec 11, 2012






0378270e










07 Dec, 2012
1 commit









made debug flag public

·
f2937905


Eric Bodden authored Dec 07, 2012






f2937905










29 Nov, 2012
4 commits









license headers

·
9a83422d


Eric Bodden authored Nov 29, 2012






9a83422d













renamed package

·
eda5559e


Eric Bodden authored Nov 29, 2012






eda5559e













moved Soot-specifiv code to soot

·
ae1aae21


Eric Bodden authored Nov 29, 2012






ae1aae21













added dumping code again for Soot/Jimple versions

·
f7c0f2f6


Eric Bodden authored Nov 29, 2012






f7c0f2f6










28 Nov, 2012
2 commits









moved dependencies on soot into separate package

·
0c5bf04d


Eric Bodden authored Nov 28, 2012






0c5bf04d













renamed package

·
92bb16ad


Eric Bodden authored Nov 28, 2012






92bb16ad










14 Nov, 2012
1 commit









initial checkin

·
d83b5de1


Eric Bodden authored Nov 14, 2012






d83b5de1



















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


















aa036d4b77ddac6dfefa6a9ecbfd1c905ed48812


Switch branch/tag









herossrcherossolverIDESolver.java
















28 Jan, 2013
3 commits









comments

·
aa036d4b


Eric Bodden authored Jan 28, 2013






aa036d4b













refactoring

·
b9930158


Eric Bodden authored Jan 28, 2013






b9930158













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










27 Jan, 2013
2 commits









Revert "optimized synchronization to work without busy loops"

·
9229bc5c





Eric Bodden authored Jan 27, 2013


This reverts commit 6f028b34.





9229bc5c













Revert "further cleanups"

·
f6eab2a7





Eric Bodden authored Jan 27, 2013


This reverts commit 5502e147.





f6eab2a7










26 Jan, 2013
3 commits









further cleanups

·
5502e147


Eric Bodden authored Jan 26, 2013






5502e147













optimized synchronization to work without busy loops

·
6f028b34


Eric Bodden authored Jan 26, 2013






6f028b34













minor cleanups

·
cdf8292c


Eric Bodden authored Jan 26, 2013






cdf8292c










25 Jan, 2013
1 commit









Fixed race condition in IDESolver and simplified the code

·
0a1e0ff5


Marc-André Laverdière authored Jan 25, 2013






0a1e0ff5










24 Jan, 2013
2 commits









replaced env variable by property

·
7afff058


Eric Bodden authored Jan 24, 2013






7afff058













added env flag HEROS_DEBUG

·
24fd6856


Eric Bodden authored Jan 24, 2013






24fd6856










22 Jan, 2013
1 commit









added possibility to switch off auto-zeroing of flow functions

·
0563038b


Eric Bodden authored Jan 22, 2013






0563038b










08 Jan, 2013
1 commit









fixing race condition found by Steven Arzt

·
2e63b6e7


Eric Bodden authored Jan 08, 2013






2e63b6e7










17 Dec, 2012
3 commits









debug flag

·
3d1f0a94


Eric Bodden authored Dec 17, 2012






3d1f0a94













bug fix against duplicate edges in unbalanced problems

·
8b8d8b3d


Eric Bodden authored Dec 17, 2012






8b8d8b3d













improved handling of unbalanced problems

·
60052739


Eric Bodden authored Dec 17, 2012






60052739










12 Dec, 2012
8 commits









making computation of unbalanced edges optional

·
15b0a59b


Eric Bodden authored Dec 12, 2012






15b0a59b













initial handling for unbalanced problems;

·
4f2738f5





Eric Bodden authored Dec 12, 2012

still need to make this optional, as otherwise we propagate too many useless flows by default





4f2738f5













undoing previous "fix"; as discussed with Steven, it is not required (see comment)

·
e86b976f


Eric Bodden authored Dec 12, 2012






e86b976f













comments and minor optimizations

·
62f80f72


Eric Bodden authored Dec 12, 2012






62f80f72













removed caller-side summary functions; instead now just use callee-side "endSummaries"

·
3458f830


Eric Bodden authored Dec 12, 2012






3458f830













fix for bug found in code review: if a summary function exists already we may...

·
67675fc9





Eric Bodden authored Dec 12, 2012

fix for bug found in code review: if a summary function exists already we may not just overwrite it;
instead join it with the new one!





67675fc9













implemented a small optimization in processExit: propagate intra-procedural...

·
2026bb15





Eric Bodden authored Dec 12, 2012

implemented a small optimization in processExit: propagate intra-procedural flows only if summary function was updated
also added implementation-level comments





2026bb15













reordered some methods

·
bcfdf777


Eric Bodden authored Dec 12, 2012






bcfdf777










11 Dec, 2012
1 commit









disabled debugging

·
0378270e


Eric Bodden authored Dec 11, 2012






0378270e










07 Dec, 2012
1 commit









made debug flag public

·
f2937905


Eric Bodden authored Dec 07, 2012






f2937905










29 Nov, 2012
4 commits









license headers

·
9a83422d


Eric Bodden authored Nov 29, 2012






9a83422d













renamed package

·
eda5559e


Eric Bodden authored Nov 29, 2012






eda5559e













moved Soot-specifiv code to soot

·
ae1aae21


Eric Bodden authored Nov 29, 2012






ae1aae21













added dumping code again for Soot/Jimple versions

·
f7c0f2f6


Eric Bodden authored Nov 29, 2012






f7c0f2f6










28 Nov, 2012
2 commits









moved dependencies on soot into separate package

·
0c5bf04d


Eric Bodden authored Nov 28, 2012






0c5bf04d













renamed package

·
92bb16ad


Eric Bodden authored Nov 28, 2012






92bb16ad










14 Nov, 2012
1 commit









initial checkin

·
d83b5de1


Eric Bodden authored Nov 14, 2012






d83b5de1


















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











aa036d4b77ddac6dfefa6a9ecbfd1c905ed48812


Switch branch/tag









herossrcherossolverIDESolver.java
















28 Jan, 2013
3 commits









comments

·
aa036d4b


Eric Bodden authored Jan 28, 2013






aa036d4b













refactoring

·
b9930158


Eric Bodden authored Jan 28, 2013






b9930158













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










27 Jan, 2013
2 commits









Revert "optimized synchronization to work without busy loops"

·
9229bc5c





Eric Bodden authored Jan 27, 2013


This reverts commit 6f028b34.





9229bc5c













Revert "further cleanups"

·
f6eab2a7





Eric Bodden authored Jan 27, 2013


This reverts commit 5502e147.





f6eab2a7










26 Jan, 2013
3 commits









further cleanups

·
5502e147


Eric Bodden authored Jan 26, 2013






5502e147













optimized synchronization to work without busy loops

·
6f028b34


Eric Bodden authored Jan 26, 2013






6f028b34













minor cleanups

·
cdf8292c


Eric Bodden authored Jan 26, 2013






cdf8292c










25 Jan, 2013
1 commit









Fixed race condition in IDESolver and simplified the code

·
0a1e0ff5


Marc-André Laverdière authored Jan 25, 2013






0a1e0ff5










24 Jan, 2013
2 commits









replaced env variable by property

·
7afff058


Eric Bodden authored Jan 24, 2013






7afff058













added env flag HEROS_DEBUG

·
24fd6856


Eric Bodden authored Jan 24, 2013






24fd6856










22 Jan, 2013
1 commit









added possibility to switch off auto-zeroing of flow functions

·
0563038b


Eric Bodden authored Jan 22, 2013






0563038b










08 Jan, 2013
1 commit









fixing race condition found by Steven Arzt

·
2e63b6e7


Eric Bodden authored Jan 08, 2013






2e63b6e7










17 Dec, 2012
3 commits









debug flag

·
3d1f0a94


Eric Bodden authored Dec 17, 2012






3d1f0a94













bug fix against duplicate edges in unbalanced problems

·
8b8d8b3d


Eric Bodden authored Dec 17, 2012






8b8d8b3d













improved handling of unbalanced problems

·
60052739


Eric Bodden authored Dec 17, 2012






60052739










12 Dec, 2012
8 commits









making computation of unbalanced edges optional

·
15b0a59b


Eric Bodden authored Dec 12, 2012






15b0a59b













initial handling for unbalanced problems;

·
4f2738f5





Eric Bodden authored Dec 12, 2012

still need to make this optional, as otherwise we propagate too many useless flows by default





4f2738f5













undoing previous "fix"; as discussed with Steven, it is not required (see comment)

·
e86b976f


Eric Bodden authored Dec 12, 2012






e86b976f













comments and minor optimizations

·
62f80f72


Eric Bodden authored Dec 12, 2012






62f80f72













removed caller-side summary functions; instead now just use callee-side "endSummaries"

·
3458f830


Eric Bodden authored Dec 12, 2012






3458f830













fix for bug found in code review: if a summary function exists already we may...

·
67675fc9





Eric Bodden authored Dec 12, 2012

fix for bug found in code review: if a summary function exists already we may not just overwrite it;
instead join it with the new one!





67675fc9













implemented a small optimization in processExit: propagate intra-procedural...

·
2026bb15





Eric Bodden authored Dec 12, 2012

implemented a small optimization in processExit: propagate intra-procedural flows only if summary function was updated
also added implementation-level comments





2026bb15













reordered some methods

·
bcfdf777


Eric Bodden authored Dec 12, 2012






bcfdf777










11 Dec, 2012
1 commit









disabled debugging

·
0378270e


Eric Bodden authored Dec 11, 2012






0378270e










07 Dec, 2012
1 commit









made debug flag public

·
f2937905


Eric Bodden authored Dec 07, 2012






f2937905










29 Nov, 2012
4 commits









license headers

·
9a83422d


Eric Bodden authored Nov 29, 2012






9a83422d













renamed package

·
eda5559e


Eric Bodden authored Nov 29, 2012






eda5559e













moved Soot-specifiv code to soot

·
ae1aae21


Eric Bodden authored Nov 29, 2012






ae1aae21













added dumping code again for Soot/Jimple versions

·
f7c0f2f6


Eric Bodden authored Nov 29, 2012






f7c0f2f6










28 Nov, 2012
2 commits









moved dependencies on soot into separate package

·
0c5bf04d


Eric Bodden authored Nov 28, 2012






0c5bf04d













renamed package

·
92bb16ad


Eric Bodden authored Nov 28, 2012






92bb16ad










14 Nov, 2012
1 commit









initial checkin

·
d83b5de1


Eric Bodden authored Nov 14, 2012






d83b5de1






















aa036d4b77ddac6dfefa6a9ecbfd1c905ed48812


Switch branch/tag









herossrcherossolverIDESolver.java
















28 Jan, 2013
3 commits









comments

·
aa036d4b


Eric Bodden authored Jan 28, 2013






aa036d4b













refactoring

·
b9930158


Eric Bodden authored Jan 28, 2013






b9930158













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










27 Jan, 2013
2 commits









Revert "optimized synchronization to work without busy loops"

·
9229bc5c





Eric Bodden authored Jan 27, 2013


This reverts commit 6f028b34.





9229bc5c













Revert "further cleanups"

·
f6eab2a7





Eric Bodden authored Jan 27, 2013


This reverts commit 5502e147.





f6eab2a7










26 Jan, 2013
3 commits









further cleanups

·
5502e147


Eric Bodden authored Jan 26, 2013






5502e147













optimized synchronization to work without busy loops

·
6f028b34


Eric Bodden authored Jan 26, 2013






6f028b34













minor cleanups

·
cdf8292c


Eric Bodden authored Jan 26, 2013






cdf8292c










25 Jan, 2013
1 commit









Fixed race condition in IDESolver and simplified the code

·
0a1e0ff5


Marc-André Laverdière authored Jan 25, 2013






0a1e0ff5










24 Jan, 2013
2 commits









replaced env variable by property

·
7afff058


Eric Bodden authored Jan 24, 2013






7afff058













added env flag HEROS_DEBUG

·
24fd6856


Eric Bodden authored Jan 24, 2013






24fd6856










22 Jan, 2013
1 commit









added possibility to switch off auto-zeroing of flow functions

·
0563038b


Eric Bodden authored Jan 22, 2013






0563038b










08 Jan, 2013
1 commit









fixing race condition found by Steven Arzt

·
2e63b6e7


Eric Bodden authored Jan 08, 2013






2e63b6e7










17 Dec, 2012
3 commits









debug flag

·
3d1f0a94


Eric Bodden authored Dec 17, 2012






3d1f0a94













bug fix against duplicate edges in unbalanced problems

·
8b8d8b3d


Eric Bodden authored Dec 17, 2012






8b8d8b3d













improved handling of unbalanced problems

·
60052739


Eric Bodden authored Dec 17, 2012






60052739










12 Dec, 2012
8 commits









making computation of unbalanced edges optional

·
15b0a59b


Eric Bodden authored Dec 12, 2012






15b0a59b













initial handling for unbalanced problems;

·
4f2738f5





Eric Bodden authored Dec 12, 2012

still need to make this optional, as otherwise we propagate too many useless flows by default





4f2738f5













undoing previous "fix"; as discussed with Steven, it is not required (see comment)

·
e86b976f


Eric Bodden authored Dec 12, 2012






e86b976f













comments and minor optimizations

·
62f80f72


Eric Bodden authored Dec 12, 2012






62f80f72













removed caller-side summary functions; instead now just use callee-side "endSummaries"

·
3458f830


Eric Bodden authored Dec 12, 2012






3458f830













fix for bug found in code review: if a summary function exists already we may...

·
67675fc9





Eric Bodden authored Dec 12, 2012

fix for bug found in code review: if a summary function exists already we may not just overwrite it;
instead join it with the new one!





67675fc9













implemented a small optimization in processExit: propagate intra-procedural...

·
2026bb15





Eric Bodden authored Dec 12, 2012

implemented a small optimization in processExit: propagate intra-procedural flows only if summary function was updated
also added implementation-level comments





2026bb15













reordered some methods

·
bcfdf777


Eric Bodden authored Dec 12, 2012






bcfdf777










11 Dec, 2012
1 commit









disabled debugging

·
0378270e


Eric Bodden authored Dec 11, 2012






0378270e










07 Dec, 2012
1 commit









made debug flag public

·
f2937905


Eric Bodden authored Dec 07, 2012






f2937905










29 Nov, 2012
4 commits









license headers

·
9a83422d


Eric Bodden authored Nov 29, 2012






9a83422d













renamed package

·
eda5559e


Eric Bodden authored Nov 29, 2012






eda5559e













moved Soot-specifiv code to soot

·
ae1aae21


Eric Bodden authored Nov 29, 2012






ae1aae21













added dumping code again for Soot/Jimple versions

·
f7c0f2f6


Eric Bodden authored Nov 29, 2012






f7c0f2f6










28 Nov, 2012
2 commits









moved dependencies on soot into separate package

·
0c5bf04d


Eric Bodden authored Nov 28, 2012






0c5bf04d













renamed package

·
92bb16ad


Eric Bodden authored Nov 28, 2012






92bb16ad










14 Nov, 2012
1 commit









initial checkin

·
d83b5de1


Eric Bodden authored Nov 14, 2012






d83b5de1


















aa036d4b77ddac6dfefa6a9ecbfd1c905ed48812


Switch branch/tag









herossrcherossolverIDESolver.java

















aa036d4b77ddac6dfefa6a9ecbfd1c905ed48812


Switch branch/tag









herossrcherossolverIDESolver.java















aa036d4b77ddac6dfefa6a9ecbfd1c905ed48812


Switch branch/tag









herossrcherossolverIDESolver.java




aa036d4b77ddac6dfefa6a9ecbfd1c905ed48812


Switch branch/tag








aa036d4b77ddac6dfefa6a9ecbfd1c905ed48812


Switch branch/tag





aa036d4b77ddac6dfefa6a9ecbfd1c905ed48812

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tagherossrcherossolverIDESolver.java













28 Jan, 2013
3 commits









comments

·
aa036d4b


Eric Bodden authored Jan 28, 2013






aa036d4b













refactoring

·
b9930158


Eric Bodden authored Jan 28, 2013






b9930158













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










27 Jan, 2013
2 commits









Revert "optimized synchronization to work without busy loops"

·
9229bc5c





Eric Bodden authored Jan 27, 2013


This reverts commit 6f028b34.





9229bc5c













Revert "further cleanups"

·
f6eab2a7





Eric Bodden authored Jan 27, 2013


This reverts commit 5502e147.





f6eab2a7










26 Jan, 2013
3 commits









further cleanups

·
5502e147


Eric Bodden authored Jan 26, 2013






5502e147













optimized synchronization to work without busy loops

·
6f028b34


Eric Bodden authored Jan 26, 2013






6f028b34













minor cleanups

·
cdf8292c


Eric Bodden authored Jan 26, 2013






cdf8292c










25 Jan, 2013
1 commit









Fixed race condition in IDESolver and simplified the code

·
0a1e0ff5


Marc-André Laverdière authored Jan 25, 2013






0a1e0ff5










24 Jan, 2013
2 commits









replaced env variable by property

·
7afff058


Eric Bodden authored Jan 24, 2013






7afff058













added env flag HEROS_DEBUG

·
24fd6856


Eric Bodden authored Jan 24, 2013






24fd6856










22 Jan, 2013
1 commit









added possibility to switch off auto-zeroing of flow functions

·
0563038b


Eric Bodden authored Jan 22, 2013






0563038b










08 Jan, 2013
1 commit









fixing race condition found by Steven Arzt

·
2e63b6e7


Eric Bodden authored Jan 08, 2013






2e63b6e7










17 Dec, 2012
3 commits









debug flag

·
3d1f0a94


Eric Bodden authored Dec 17, 2012






3d1f0a94













bug fix against duplicate edges in unbalanced problems

·
8b8d8b3d


Eric Bodden authored Dec 17, 2012






8b8d8b3d













improved handling of unbalanced problems

·
60052739


Eric Bodden authored Dec 17, 2012






60052739










12 Dec, 2012
8 commits









making computation of unbalanced edges optional

·
15b0a59b


Eric Bodden authored Dec 12, 2012






15b0a59b













initial handling for unbalanced problems;

·
4f2738f5





Eric Bodden authored Dec 12, 2012

still need to make this optional, as otherwise we propagate too many useless flows by default





4f2738f5













undoing previous "fix"; as discussed with Steven, it is not required (see comment)

·
e86b976f


Eric Bodden authored Dec 12, 2012






e86b976f













comments and minor optimizations

·
62f80f72


Eric Bodden authored Dec 12, 2012






62f80f72













removed caller-side summary functions; instead now just use callee-side "endSummaries"

·
3458f830


Eric Bodden authored Dec 12, 2012






3458f830













fix for bug found in code review: if a summary function exists already we may...

·
67675fc9





Eric Bodden authored Dec 12, 2012

fix for bug found in code review: if a summary function exists already we may not just overwrite it;
instead join it with the new one!





67675fc9













implemented a small optimization in processExit: propagate intra-procedural...

·
2026bb15





Eric Bodden authored Dec 12, 2012

implemented a small optimization in processExit: propagate intra-procedural flows only if summary function was updated
also added implementation-level comments





2026bb15













reordered some methods

·
bcfdf777


Eric Bodden authored Dec 12, 2012






bcfdf777










11 Dec, 2012
1 commit









disabled debugging

·
0378270e


Eric Bodden authored Dec 11, 2012






0378270e










07 Dec, 2012
1 commit









made debug flag public

·
f2937905


Eric Bodden authored Dec 07, 2012






f2937905










29 Nov, 2012
4 commits









license headers

·
9a83422d


Eric Bodden authored Nov 29, 2012






9a83422d













renamed package

·
eda5559e


Eric Bodden authored Nov 29, 2012






eda5559e













moved Soot-specifiv code to soot

·
ae1aae21


Eric Bodden authored Nov 29, 2012






ae1aae21













added dumping code again for Soot/Jimple versions

·
f7c0f2f6


Eric Bodden authored Nov 29, 2012






f7c0f2f6










28 Nov, 2012
2 commits









moved dependencies on soot into separate package

·
0c5bf04d


Eric Bodden authored Nov 28, 2012






0c5bf04d













renamed package

·
92bb16ad


Eric Bodden authored Nov 28, 2012






92bb16ad










14 Nov, 2012
1 commit









initial checkin

·
d83b5de1


Eric Bodden authored Nov 14, 2012






d83b5de1











28 Jan, 2013
3 commits
28 Jan, 20133 commits







comments

·
aa036d4b


Eric Bodden authored Jan 28, 2013






aa036d4b













refactoring

·
b9930158


Eric Bodden authored Jan 28, 2013






b9930158













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














comments

·
aa036d4b


Eric Bodden authored Jan 28, 2013






aa036d4b










comments

·
aa036d4b


Eric Bodden authored Jan 28, 2013






aa036d4b






comments

·
aa036d4b


Eric Bodden authored Jan 28, 2013


·
aa036d4b

Eric Bodden authored Jan 28, 2013




aa036d4b






aa036d4b




aa036d4b






refactoring

·
b9930158


Eric Bodden authored Jan 28, 2013






b9930158










refactoring

·
b9930158


Eric Bodden authored Jan 28, 2013






b9930158






refactoring

·
b9930158


Eric Bodden authored Jan 28, 2013


·
b9930158

Eric Bodden authored Jan 28, 2013




b9930158






b9930158




b9930158






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

27 Jan, 2013
2 commits
27 Jan, 20132 commits







Revert "optimized synchronization to work without busy loops"

·
9229bc5c





Eric Bodden authored Jan 27, 2013


This reverts commit 6f028b34.





9229bc5c













Revert "further cleanups"

·
f6eab2a7





Eric Bodden authored Jan 27, 2013


This reverts commit 5502e147.





f6eab2a7














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






Revert "further cleanups"

·
f6eab2a7





Eric Bodden authored Jan 27, 2013


This reverts commit 5502e147.





f6eab2a7










Revert "further cleanups"

·
f6eab2a7





Eric Bodden authored Jan 27, 2013


This reverts commit 5502e147.





f6eab2a7






Revert "further cleanups"

·
f6eab2a7





Eric Bodden authored Jan 27, 2013


This reverts commit 5502e147.

·
f6eab2a7

Eric Bodden authored Jan 27, 2013




f6eab2a7






f6eab2a7




f6eab2a7

26 Jan, 2013
3 commits
26 Jan, 20133 commits







further cleanups

·
5502e147


Eric Bodden authored Jan 26, 2013






5502e147













optimized synchronization to work without busy loops

·
6f028b34


Eric Bodden authored Jan 26, 2013






6f028b34













minor cleanups

·
cdf8292c


Eric Bodden authored Jan 26, 2013






cdf8292c














further cleanups

·
5502e147


Eric Bodden authored Jan 26, 2013






5502e147










further cleanups

·
5502e147


Eric Bodden authored Jan 26, 2013






5502e147






further cleanups

·
5502e147


Eric Bodden authored Jan 26, 2013


·
5502e147

Eric Bodden authored Jan 26, 2013




5502e147






5502e147




5502e147






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






minor cleanups

·
cdf8292c


Eric Bodden authored Jan 26, 2013






cdf8292c










minor cleanups

·
cdf8292c


Eric Bodden authored Jan 26, 2013






cdf8292c






minor cleanups

·
cdf8292c


Eric Bodden authored Jan 26, 2013


·
cdf8292c

Eric Bodden authored Jan 26, 2013




cdf8292c






cdf8292c




cdf8292c

25 Jan, 2013
1 commit
25 Jan, 20131 commit







Fixed race condition in IDESolver and simplified the code

·
0a1e0ff5


Marc-André Laverdière authored Jan 25, 2013






0a1e0ff5














Fixed race condition in IDESolver and simplified the code

·
0a1e0ff5


Marc-André Laverdière authored Jan 25, 2013






0a1e0ff5










Fixed race condition in IDESolver and simplified the code

·
0a1e0ff5


Marc-André Laverdière authored Jan 25, 2013






0a1e0ff5






Fixed race condition in IDESolver and simplified the code

·
0a1e0ff5


Marc-André Laverdière authored Jan 25, 2013


·
0a1e0ff5

Marc-André Laverdière authored Jan 25, 2013




0a1e0ff5






0a1e0ff5




0a1e0ff5

24 Jan, 2013
2 commits
24 Jan, 20132 commits







replaced env variable by property

·
7afff058


Eric Bodden authored Jan 24, 2013






7afff058













added env flag HEROS_DEBUG

·
24fd6856


Eric Bodden authored Jan 24, 2013






24fd6856














replaced env variable by property

·
7afff058


Eric Bodden authored Jan 24, 2013






7afff058










replaced env variable by property

·
7afff058


Eric Bodden authored Jan 24, 2013






7afff058






replaced env variable by property

·
7afff058


Eric Bodden authored Jan 24, 2013


·
7afff058

Eric Bodden authored Jan 24, 2013




7afff058






7afff058




7afff058






added env flag HEROS_DEBUG

·
24fd6856


Eric Bodden authored Jan 24, 2013






24fd6856










added env flag HEROS_DEBUG

·
24fd6856


Eric Bodden authored Jan 24, 2013






24fd6856






added env flag HEROS_DEBUG

·
24fd6856


Eric Bodden authored Jan 24, 2013


·
24fd6856

Eric Bodden authored Jan 24, 2013




24fd6856






24fd6856




24fd6856

22 Jan, 2013
1 commit
22 Jan, 20131 commit







added possibility to switch off auto-zeroing of flow functions

·
0563038b


Eric Bodden authored Jan 22, 2013






0563038b














added possibility to switch off auto-zeroing of flow functions

·
0563038b


Eric Bodden authored Jan 22, 2013






0563038b










added possibility to switch off auto-zeroing of flow functions

·
0563038b


Eric Bodden authored Jan 22, 2013






0563038b






added possibility to switch off auto-zeroing of flow functions

·
0563038b


Eric Bodden authored Jan 22, 2013


·
0563038b

Eric Bodden authored Jan 22, 2013




0563038b






0563038b




0563038b

08 Jan, 2013
1 commit
08 Jan, 20131 commit







fixing race condition found by Steven Arzt

·
2e63b6e7


Eric Bodden authored Jan 08, 2013






2e63b6e7














fixing race condition found by Steven Arzt

·
2e63b6e7


Eric Bodden authored Jan 08, 2013






2e63b6e7










fixing race condition found by Steven Arzt

·
2e63b6e7


Eric Bodden authored Jan 08, 2013






2e63b6e7






fixing race condition found by Steven Arzt

·
2e63b6e7


Eric Bodden authored Jan 08, 2013


·
2e63b6e7

Eric Bodden authored Jan 08, 2013




2e63b6e7






2e63b6e7




2e63b6e7

17 Dec, 2012
3 commits
17 Dec, 20123 commits







debug flag

·
3d1f0a94


Eric Bodden authored Dec 17, 2012






3d1f0a94













bug fix against duplicate edges in unbalanced problems

·
8b8d8b3d


Eric Bodden authored Dec 17, 2012






8b8d8b3d













improved handling of unbalanced problems

·
60052739


Eric Bodden authored Dec 17, 2012






60052739














debug flag

·
3d1f0a94


Eric Bodden authored Dec 17, 2012






3d1f0a94










debug flag

·
3d1f0a94


Eric Bodden authored Dec 17, 2012






3d1f0a94






debug flag

·
3d1f0a94


Eric Bodden authored Dec 17, 2012


·
3d1f0a94

Eric Bodden authored Dec 17, 2012




3d1f0a94






3d1f0a94




3d1f0a94






bug fix against duplicate edges in unbalanced problems

·
8b8d8b3d


Eric Bodden authored Dec 17, 2012






8b8d8b3d










bug fix against duplicate edges in unbalanced problems

·
8b8d8b3d


Eric Bodden authored Dec 17, 2012






8b8d8b3d






bug fix against duplicate edges in unbalanced problems

·
8b8d8b3d


Eric Bodden authored Dec 17, 2012


·
8b8d8b3d

Eric Bodden authored Dec 17, 2012




8b8d8b3d






8b8d8b3d




8b8d8b3d






improved handling of unbalanced problems

·
60052739


Eric Bodden authored Dec 17, 2012






60052739










improved handling of unbalanced problems

·
60052739


Eric Bodden authored Dec 17, 2012






60052739






improved handling of unbalanced problems

·
60052739


Eric Bodden authored Dec 17, 2012


·
60052739

Eric Bodden authored Dec 17, 2012




60052739






60052739




60052739

12 Dec, 2012
8 commits
12 Dec, 20128 commits







making computation of unbalanced edges optional

·
15b0a59b


Eric Bodden authored Dec 12, 2012






15b0a59b













initial handling for unbalanced problems;

·
4f2738f5





Eric Bodden authored Dec 12, 2012

still need to make this optional, as otherwise we propagate too many useless flows by default





4f2738f5













undoing previous "fix"; as discussed with Steven, it is not required (see comment)

·
e86b976f


Eric Bodden authored Dec 12, 2012






e86b976f













comments and minor optimizations

·
62f80f72


Eric Bodden authored Dec 12, 2012






62f80f72













removed caller-side summary functions; instead now just use callee-side "endSummaries"

·
3458f830


Eric Bodden authored Dec 12, 2012






3458f830













fix for bug found in code review: if a summary function exists already we may...

·
67675fc9





Eric Bodden authored Dec 12, 2012

fix for bug found in code review: if a summary function exists already we may not just overwrite it;
instead join it with the new one!





67675fc9













implemented a small optimization in processExit: propagate intra-procedural...

·
2026bb15





Eric Bodden authored Dec 12, 2012

implemented a small optimization in processExit: propagate intra-procedural flows only if summary function was updated
also added implementation-level comments





2026bb15













reordered some methods

·
bcfdf777


Eric Bodden authored Dec 12, 2012






bcfdf777














making computation of unbalanced edges optional

·
15b0a59b


Eric Bodden authored Dec 12, 2012






15b0a59b










making computation of unbalanced edges optional

·
15b0a59b


Eric Bodden authored Dec 12, 2012






15b0a59b






making computation of unbalanced edges optional

·
15b0a59b


Eric Bodden authored Dec 12, 2012


·
15b0a59b

Eric Bodden authored Dec 12, 2012




15b0a59b






15b0a59b




15b0a59b






initial handling for unbalanced problems;

·
4f2738f5





Eric Bodden authored Dec 12, 2012

still need to make this optional, as otherwise we propagate too many useless flows by default





4f2738f5










initial handling for unbalanced problems;

·
4f2738f5





Eric Bodden authored Dec 12, 2012

still need to make this optional, as otherwise we propagate too many useless flows by default





4f2738f5






initial handling for unbalanced problems;

·
4f2738f5





Eric Bodden authored Dec 12, 2012

still need to make this optional, as otherwise we propagate too many useless flows by default

·
4f2738f5

Eric Bodden authored Dec 12, 2012




4f2738f5






4f2738f5




4f2738f5






undoing previous "fix"; as discussed with Steven, it is not required (see comment)

·
e86b976f


Eric Bodden authored Dec 12, 2012






e86b976f










undoing previous "fix"; as discussed with Steven, it is not required (see comment)

·
e86b976f


Eric Bodden authored Dec 12, 2012






e86b976f






undoing previous "fix"; as discussed with Steven, it is not required (see comment)

·
e86b976f


Eric Bodden authored Dec 12, 2012


·
e86b976f

Eric Bodden authored Dec 12, 2012




e86b976f






e86b976f




e86b976f






comments and minor optimizations

·
62f80f72


Eric Bodden authored Dec 12, 2012






62f80f72










comments and minor optimizations

·
62f80f72


Eric Bodden authored Dec 12, 2012






62f80f72






comments and minor optimizations

·
62f80f72


Eric Bodden authored Dec 12, 2012


·
62f80f72

Eric Bodden authored Dec 12, 2012




62f80f72






62f80f72




62f80f72






removed caller-side summary functions; instead now just use callee-side "endSummaries"

·
3458f830


Eric Bodden authored Dec 12, 2012






3458f830










removed caller-side summary functions; instead now just use callee-side "endSummaries"

·
3458f830


Eric Bodden authored Dec 12, 2012






3458f830






removed caller-side summary functions; instead now just use callee-side "endSummaries"

·
3458f830


Eric Bodden authored Dec 12, 2012


·
3458f830

Eric Bodden authored Dec 12, 2012




3458f830






3458f830




3458f830






fix for bug found in code review: if a summary function exists already we may...

·
67675fc9





Eric Bodden authored Dec 12, 2012

fix for bug found in code review: if a summary function exists already we may not just overwrite it;
instead join it with the new one!





67675fc9










fix for bug found in code review: if a summary function exists already we may...

·
67675fc9





Eric Bodden authored Dec 12, 2012

fix for bug found in code review: if a summary function exists already we may not just overwrite it;
instead join it with the new one!





67675fc9






fix for bug found in code review: if a summary function exists already we may...

·
67675fc9





Eric Bodden authored Dec 12, 2012

fix for bug found in code review: if a summary function exists already we may not just overwrite it;
instead join it with the new one!

·
67675fc9

Eric Bodden authored Dec 12, 2012




67675fc9






67675fc9




67675fc9






implemented a small optimization in processExit: propagate intra-procedural...

·
2026bb15





Eric Bodden authored Dec 12, 2012

implemented a small optimization in processExit: propagate intra-procedural flows only if summary function was updated
also added implementation-level comments





2026bb15










implemented a small optimization in processExit: propagate intra-procedural...

·
2026bb15





Eric Bodden authored Dec 12, 2012

implemented a small optimization in processExit: propagate intra-procedural flows only if summary function was updated
also added implementation-level comments





2026bb15






implemented a small optimization in processExit: propagate intra-procedural...

·
2026bb15





Eric Bodden authored Dec 12, 2012

implemented a small optimization in processExit: propagate intra-procedural flows only if summary function was updated
also added implementation-level comments

·
2026bb15

Eric Bodden authored Dec 12, 2012




2026bb15






2026bb15




2026bb15






reordered some methods

·
bcfdf777


Eric Bodden authored Dec 12, 2012






bcfdf777










reordered some methods

·
bcfdf777


Eric Bodden authored Dec 12, 2012






bcfdf777






reordered some methods

·
bcfdf777


Eric Bodden authored Dec 12, 2012


·
bcfdf777

Eric Bodden authored Dec 12, 2012




bcfdf777






bcfdf777




bcfdf777

11 Dec, 2012
1 commit
11 Dec, 20121 commit







disabled debugging

·
0378270e


Eric Bodden authored Dec 11, 2012






0378270e














disabled debugging

·
0378270e


Eric Bodden authored Dec 11, 2012






0378270e










disabled debugging

·
0378270e


Eric Bodden authored Dec 11, 2012






0378270e






disabled debugging

·
0378270e


Eric Bodden authored Dec 11, 2012


·
0378270e

Eric Bodden authored Dec 11, 2012




0378270e






0378270e




0378270e

07 Dec, 2012
1 commit
07 Dec, 20121 commit







made debug flag public

·
f2937905


Eric Bodden authored Dec 07, 2012






f2937905














made debug flag public

·
f2937905


Eric Bodden authored Dec 07, 2012






f2937905










made debug flag public

·
f2937905


Eric Bodden authored Dec 07, 2012






f2937905






made debug flag public

·
f2937905


Eric Bodden authored Dec 07, 2012


·
f2937905

Eric Bodden authored Dec 07, 2012




f2937905






f2937905




f2937905

29 Nov, 2012
4 commits
29 Nov, 20124 commits







license headers

·
9a83422d


Eric Bodden authored Nov 29, 2012






9a83422d













renamed package

·
eda5559e


Eric Bodden authored Nov 29, 2012






eda5559e













moved Soot-specifiv code to soot

·
ae1aae21


Eric Bodden authored Nov 29, 2012






ae1aae21













added dumping code again for Soot/Jimple versions

·
f7c0f2f6


Eric Bodden authored Nov 29, 2012






f7c0f2f6














license headers

·
9a83422d


Eric Bodden authored Nov 29, 2012






9a83422d










license headers

·
9a83422d


Eric Bodden authored Nov 29, 2012






9a83422d






license headers

·
9a83422d


Eric Bodden authored Nov 29, 2012


·
9a83422d

Eric Bodden authored Nov 29, 2012




9a83422d






9a83422d




9a83422d






renamed package

·
eda5559e


Eric Bodden authored Nov 29, 2012






eda5559e










renamed package

·
eda5559e


Eric Bodden authored Nov 29, 2012






eda5559e






renamed package

·
eda5559e


Eric Bodden authored Nov 29, 2012


·
eda5559e

Eric Bodden authored Nov 29, 2012




eda5559e






eda5559e




eda5559e






moved Soot-specifiv code to soot

·
ae1aae21


Eric Bodden authored Nov 29, 2012






ae1aae21










moved Soot-specifiv code to soot

·
ae1aae21


Eric Bodden authored Nov 29, 2012






ae1aae21






moved Soot-specifiv code to soot

·
ae1aae21


Eric Bodden authored Nov 29, 2012


·
ae1aae21

Eric Bodden authored Nov 29, 2012




ae1aae21






ae1aae21




ae1aae21






added dumping code again for Soot/Jimple versions

·
f7c0f2f6


Eric Bodden authored Nov 29, 2012






f7c0f2f6










added dumping code again for Soot/Jimple versions

·
f7c0f2f6


Eric Bodden authored Nov 29, 2012






f7c0f2f6






added dumping code again for Soot/Jimple versions

·
f7c0f2f6


Eric Bodden authored Nov 29, 2012


·
f7c0f2f6

Eric Bodden authored Nov 29, 2012




f7c0f2f6






f7c0f2f6




f7c0f2f6

28 Nov, 2012
2 commits
28 Nov, 20122 commits







moved dependencies on soot into separate package

·
0c5bf04d


Eric Bodden authored Nov 28, 2012






0c5bf04d













renamed package

·
92bb16ad


Eric Bodden authored Nov 28, 2012






92bb16ad














moved dependencies on soot into separate package

·
0c5bf04d


Eric Bodden authored Nov 28, 2012






0c5bf04d










moved dependencies on soot into separate package

·
0c5bf04d


Eric Bodden authored Nov 28, 2012






0c5bf04d






moved dependencies on soot into separate package

·
0c5bf04d


Eric Bodden authored Nov 28, 2012


·
0c5bf04d

Eric Bodden authored Nov 28, 2012




0c5bf04d






0c5bf04d




0c5bf04d






renamed package

·
92bb16ad


Eric Bodden authored Nov 28, 2012






92bb16ad










renamed package

·
92bb16ad


Eric Bodden authored Nov 28, 2012






92bb16ad






renamed package

·
92bb16ad


Eric Bodden authored Nov 28, 2012


·
92bb16ad

Eric Bodden authored Nov 28, 2012




92bb16ad






92bb16ad




92bb16ad

14 Nov, 2012
1 commit
14 Nov, 20121 commit







initial checkin

·
d83b5de1


Eric Bodden authored Nov 14, 2012






d83b5de1














initial checkin

·
d83b5de1


Eric Bodden authored Nov 14, 2012






d83b5de1










initial checkin

·
d83b5de1


Eric Bodden authored Nov 14, 2012






d83b5de1






initial checkin

·
d83b5de1


Eric Bodden authored Nov 14, 2012


·
d83b5de1

Eric Bodden authored Nov 14, 2012




d83b5de1






d83b5de1




d83b5de1






