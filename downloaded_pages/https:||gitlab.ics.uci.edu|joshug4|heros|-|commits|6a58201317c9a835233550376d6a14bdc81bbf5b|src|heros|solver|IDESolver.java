



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

Commits



















herossrcherossolverIDESolver.java
















18 Oct, 2013
1 commit









Revert "first implementation of reduced summaries"

·
6a582013




Eric Bodden authored Oct 18, 2013


This reverts commit f161c043.





6a582013










16 Jul, 2013
1 commit









first implementation of reduced summaries

·
f161c043


Eric Bodden authored Jul 16, 2013






f161c043










11 Jul, 2013
1 commit









simplified exception handling

·
bdc8348c


Eric Bodden authored Jul 11, 2013






bdc8348c










10 Jul, 2013
1 commit









removing superfluous (hopefully?) call to scheduleEdgeProcessing

·
3f7647fe


Eric Bodden authored Jul 10, 2013






3f7647fe










09 Jul, 2013
1 commit









first version of fw-bw lockstep analysis that seems to be working; the trick...

·
1526d8d2




Eric Bodden authored Jul 09, 2013

first version of fw-bw lockstep analysis that seems to be working; the trick was apparently to distinguish balanced from unbalanced returns





1526d8d2










07 Jul, 2013
2 commits









further fix for followReturnPastSeeds:

·
e8034a41




Eric Bodden authored Jul 08, 2013

regarding Steven's earlier fix we now follow a different strategy…
we propagate upwards in an unbalanced way only such facts that originate from ZERO





e8034a41













improved and simplified handling of unbalanced problems:

·
44d17eee




Eric Bodden authored Jul 08, 2013

we now just propagate up the call stack in an unbalanced way if for the very flow fact we are looking at there
was no incoming flow observed earlier





44d17eee










06 Jul, 2013
7 commits









changed signature of "propagate" to include original call site for return and call flows

·
b8e2c3df




Eric Bodden authored Jul 06, 2013


modified bidi solver to changed attached source statement on return





b8e2c3df













removed superfluous type parameter from PathEdge

·
336790c7


Eric Bodden authored Jul 06, 2013






336790c7













make "propagate" protected

·
fef14535


Eric Bodden authored Jul 06, 2013






fef14535













removed unused import

·
734c0ca4


Eric Bodden authored Jul 06, 2013






734c0ca4













added support for debug name

·
68501d99


Eric Bodden authored Jul 06, 2013






68501d99













extracting method submitInitialSeeds to allow submission without having to wait

·
6a9d93c8


Eric Bodden authored Jul 06, 2013






6a9d93c8













made process* methods protected

·
56975c20


Eric Bodden authored Jul 06, 2013






56975c20










05 Jul, 2013
1 commit









changing initialization of analysis such that initialSeeds not is a mapping...

·
275f5783




Eric Bodden authored Jul 05, 2013

changing initialization of analysis such that initialSeeds not is a mapping from units to initial data-flow facts at these units

this is a breaking change, but the class DefaultSeeds can be used to easily convert a set of units (old format) into a default map that should work for current clients

the change was implemented to permit subtypes of IFDSTabulationProblem to overwrite facts at seeds in a convenient way





275f5783










04 Jul, 2013
3 commits









bugfix: when propagating inside out into a caller that was not yet processed...

·
17895853




Eric Bodden authored Jul 04, 2013

bugfix: when propagating inside out into a caller that was not yet processed we need to create a summary edge starting at ZERO, not at the callee's flow fact!





17895853













memory improvement: do not store implicit TOP values

·
987a7a4c


Steven Arzt authored Jul 04, 2013






987a7a4c













bugfix: when propagating inside out into a caller that was not yet processed...

·
3e3888da




Eric Bodden authored Jul 04, 2013

bugfix: when propagating inside out into a caller that was not yet processed we need to create a summary edge starting at ZERO, not at the callee's flow fact!





3e3888da










27 Jun, 2013
1 commit









added exception reporting for tasks in IDESolver

·
b95498c9


Eric Bodden authored Jun 27, 2013






b95498c9










16 Jun, 2013
1 commit









fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

·
0ec8bfc3




Steven Arzt authored Jun 16, 2013

fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return edge gets processed for **all callers** of the callee from which we return in cases where we haven't seen the respective call edge.

This however leads to spurious edges if we have multiple call sites for the same callee. Let us assume call site A calls foo() with jump function a and call site B calls it with b. If we now return from foo with something derived from a, we will not find an incoming edge for call site B. FollowReturnsPastSeeds thus made us propagate the value to both call sites which makes us loose context sensitivity.

Fix as follows: Only follow past seeds if we have seen no call edge into the callee at all. We can then assume that we're really running beyond the seeds of the analysis and have no other chance than propagating to all call sites.





0ec8bfc3










05 Jun, 2013
1 commit









for unbalanced problems on return statements we now use the return flow...

·
4ae358b3




Eric Bodden authored Jun 05, 2013

for unbalanced problems on return statements we now use the return flow function, not the normal flow function
the caller is then simply "null"
added some comments about this





4ae358b3










29 May, 2013
1 commit









adding synchronization on "val" due to possible race conditions (thanks to...

·
525c45fb




Eric Bodden authored May 29, 2013

adding synchronization on "val" due to possible race conditions (thanks to Damien Octeau for reporting this!)





525c45fb










25 Apr, 2013
1 commit









performance fix for return edges; if there were N start points (e.g. in a...

·
6a91d71e




Eric Bodden authored Apr 25, 2013

performance fix for return edges; if there were N start points (e.g. in a backwards analysis) and we did an unbalanced analysis, then the return-foe function was computed N times; now this is done only once





6a91d71e










15 Mar, 2013
1 commit









Bugfix: ProcessCall computed the new jump functions for a method call at a...

·
d564bec7




Steven Arzt authored Mar 15, 2013

Bugfix: ProcessCall computed the new jump functions for a method call at a specific call and return site pair, but then propagated the results to all return sites of this call. This lead to spurious results if there were multiple return sites for a call that had non-equal fact sets on their return edges.





d564bec7










11 Mar, 2013
1 commit









Replaced the duplicate call to the icfg by an access to cached structure we have anyway

·
5c0a40f6


Steven Arzt authored Mar 11, 2013






5c0a40f6










28 Feb, 2013
1 commit









removed stupig bug found by findbugs !!!

·
72286798


Eric Bodden authored Feb 28, 2013






72286798










20 Feb, 2013
1 commit









bugfix: were propagating incorrect values at return-flow edges

·
0645e494


Eric Bodden authored Feb 20, 2013






0645e494










14 Feb, 2013
1 commit









bug fix for value computation (need to treat initialSeeds just as method start nodes)

·
227b9337


Eric Bodden authored Feb 14, 2013






227b9337










30 Jan, 2013
3 commits









extracted method awaitCompletionComputeValuesAndShutdown()

·
446d709e


Eric Bodden authored Jan 30, 2013






446d709e













made scheduleEdgeProcessing protected such that it can be called from the outside

·
abe1050c


Eric Bodden authored Jan 30, 2013






abe1050c













Revert "removed superfluous call to scheduleEdgeProcessing"

·
2c7e04fd




Eric Bodden authored Jan 30, 2013


This reverts commit 1747df0c.

The change was wrong; scheduleEdgeProcessing does need to be called explicitly at initialization time!





2c7e04fd










29 Jan, 2013
6 commits









fixing shutdown sequence

·
e0e51e68


Eric Bodden authored Jan 29, 2013






e0e51e68













making executor exchangeable

·
18c72755


Eric Bodden authored Jan 29, 2013






18c72755













make computation of values optional

·
357e129e


Eric Bodden authored Jan 29, 2013






357e129e













number of threads is now configured through SolverConfiguration, a new super...

·
1e7167fe




Eric Bodden authored Jan 29, 2013

number of threads is now configured through SolverConfiguration, a new super interface of IFDSTabulationProblem





1e7167fe













indentation

·
aa7b4340


Eric Bodden authored Jan 29, 2013






aa7b4340













removed superfluous call to scheduleEdgeProcessing

·
1747df0c


Eric Bodden authored Jan 29, 2013






1747df0c










28 Jan, 2013
3 commits









refactoring: autoAddZero is now set in IFDSTabulationProblem

·
4b103811


Eric Bodden authored Jan 28, 2013






4b103811













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



















herossrcherossolverIDESolver.java
















18 Oct, 2013
1 commit









Revert "first implementation of reduced summaries"

·
6a582013




Eric Bodden authored Oct 18, 2013


This reverts commit f161c043.





6a582013










16 Jul, 2013
1 commit









first implementation of reduced summaries

·
f161c043


Eric Bodden authored Jul 16, 2013






f161c043










11 Jul, 2013
1 commit









simplified exception handling

·
bdc8348c


Eric Bodden authored Jul 11, 2013






bdc8348c










10 Jul, 2013
1 commit









removing superfluous (hopefully?) call to scheduleEdgeProcessing

·
3f7647fe


Eric Bodden authored Jul 10, 2013






3f7647fe










09 Jul, 2013
1 commit









first version of fw-bw lockstep analysis that seems to be working; the trick...

·
1526d8d2




Eric Bodden authored Jul 09, 2013

first version of fw-bw lockstep analysis that seems to be working; the trick was apparently to distinguish balanced from unbalanced returns





1526d8d2










07 Jul, 2013
2 commits









further fix for followReturnPastSeeds:

·
e8034a41




Eric Bodden authored Jul 08, 2013

regarding Steven's earlier fix we now follow a different strategy…
we propagate upwards in an unbalanced way only such facts that originate from ZERO





e8034a41













improved and simplified handling of unbalanced problems:

·
44d17eee




Eric Bodden authored Jul 08, 2013

we now just propagate up the call stack in an unbalanced way if for the very flow fact we are looking at there
was no incoming flow observed earlier





44d17eee










06 Jul, 2013
7 commits









changed signature of "propagate" to include original call site for return and call flows

·
b8e2c3df




Eric Bodden authored Jul 06, 2013


modified bidi solver to changed attached source statement on return





b8e2c3df













removed superfluous type parameter from PathEdge

·
336790c7


Eric Bodden authored Jul 06, 2013






336790c7













make "propagate" protected

·
fef14535


Eric Bodden authored Jul 06, 2013






fef14535













removed unused import

·
734c0ca4


Eric Bodden authored Jul 06, 2013






734c0ca4













added support for debug name

·
68501d99


Eric Bodden authored Jul 06, 2013






68501d99













extracting method submitInitialSeeds to allow submission without having to wait

·
6a9d93c8


Eric Bodden authored Jul 06, 2013






6a9d93c8













made process* methods protected

·
56975c20


Eric Bodden authored Jul 06, 2013






56975c20










05 Jul, 2013
1 commit









changing initialization of analysis such that initialSeeds not is a mapping...

·
275f5783




Eric Bodden authored Jul 05, 2013

changing initialization of analysis such that initialSeeds not is a mapping from units to initial data-flow facts at these units

this is a breaking change, but the class DefaultSeeds can be used to easily convert a set of units (old format) into a default map that should work for current clients

the change was implemented to permit subtypes of IFDSTabulationProblem to overwrite facts at seeds in a convenient way





275f5783










04 Jul, 2013
3 commits









bugfix: when propagating inside out into a caller that was not yet processed...

·
17895853




Eric Bodden authored Jul 04, 2013

bugfix: when propagating inside out into a caller that was not yet processed we need to create a summary edge starting at ZERO, not at the callee's flow fact!





17895853













memory improvement: do not store implicit TOP values

·
987a7a4c


Steven Arzt authored Jul 04, 2013






987a7a4c













bugfix: when propagating inside out into a caller that was not yet processed...

·
3e3888da




Eric Bodden authored Jul 04, 2013

bugfix: when propagating inside out into a caller that was not yet processed we need to create a summary edge starting at ZERO, not at the callee's flow fact!





3e3888da










27 Jun, 2013
1 commit









added exception reporting for tasks in IDESolver

·
b95498c9


Eric Bodden authored Jun 27, 2013






b95498c9










16 Jun, 2013
1 commit









fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

·
0ec8bfc3




Steven Arzt authored Jun 16, 2013

fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return edge gets processed for **all callers** of the callee from which we return in cases where we haven't seen the respective call edge.

This however leads to spurious edges if we have multiple call sites for the same callee. Let us assume call site A calls foo() with jump function a and call site B calls it with b. If we now return from foo with something derived from a, we will not find an incoming edge for call site B. FollowReturnsPastSeeds thus made us propagate the value to both call sites which makes us loose context sensitivity.

Fix as follows: Only follow past seeds if we have seen no call edge into the callee at all. We can then assume that we're really running beyond the seeds of the analysis and have no other chance than propagating to all call sites.





0ec8bfc3










05 Jun, 2013
1 commit









for unbalanced problems on return statements we now use the return flow...

·
4ae358b3




Eric Bodden authored Jun 05, 2013

for unbalanced problems on return statements we now use the return flow function, not the normal flow function
the caller is then simply "null"
added some comments about this





4ae358b3










29 May, 2013
1 commit









adding synchronization on "val" due to possible race conditions (thanks to...

·
525c45fb




Eric Bodden authored May 29, 2013

adding synchronization on "val" due to possible race conditions (thanks to Damien Octeau for reporting this!)





525c45fb










25 Apr, 2013
1 commit









performance fix for return edges; if there were N start points (e.g. in a...

·
6a91d71e




Eric Bodden authored Apr 25, 2013

performance fix for return edges; if there were N start points (e.g. in a backwards analysis) and we did an unbalanced analysis, then the return-foe function was computed N times; now this is done only once





6a91d71e










15 Mar, 2013
1 commit









Bugfix: ProcessCall computed the new jump functions for a method call at a...

·
d564bec7




Steven Arzt authored Mar 15, 2013

Bugfix: ProcessCall computed the new jump functions for a method call at a specific call and return site pair, but then propagated the results to all return sites of this call. This lead to spurious results if there were multiple return sites for a call that had non-equal fact sets on their return edges.





d564bec7










11 Mar, 2013
1 commit









Replaced the duplicate call to the icfg by an access to cached structure we have anyway

·
5c0a40f6


Steven Arzt authored Mar 11, 2013






5c0a40f6










28 Feb, 2013
1 commit









removed stupig bug found by findbugs !!!

·
72286798


Eric Bodden authored Feb 28, 2013






72286798










20 Feb, 2013
1 commit









bugfix: were propagating incorrect values at return-flow edges

·
0645e494


Eric Bodden authored Feb 20, 2013






0645e494










14 Feb, 2013
1 commit









bug fix for value computation (need to treat initialSeeds just as method start nodes)

·
227b9337


Eric Bodden authored Feb 14, 2013






227b9337










30 Jan, 2013
3 commits









extracted method awaitCompletionComputeValuesAndShutdown()

·
446d709e


Eric Bodden authored Jan 30, 2013






446d709e













made scheduleEdgeProcessing protected such that it can be called from the outside

·
abe1050c


Eric Bodden authored Jan 30, 2013






abe1050c













Revert "removed superfluous call to scheduleEdgeProcessing"

·
2c7e04fd




Eric Bodden authored Jan 30, 2013


This reverts commit 1747df0c.

The change was wrong; scheduleEdgeProcessing does need to be called explicitly at initialization time!





2c7e04fd










29 Jan, 2013
6 commits









fixing shutdown sequence

·
e0e51e68


Eric Bodden authored Jan 29, 2013






e0e51e68













making executor exchangeable

·
18c72755


Eric Bodden authored Jan 29, 2013






18c72755













make computation of values optional

·
357e129e


Eric Bodden authored Jan 29, 2013






357e129e













number of threads is now configured through SolverConfiguration, a new super...

·
1e7167fe




Eric Bodden authored Jan 29, 2013

number of threads is now configured through SolverConfiguration, a new super interface of IFDSTabulationProblem





1e7167fe













indentation

·
aa7b4340


Eric Bodden authored Jan 29, 2013






aa7b4340













removed superfluous call to scheduleEdgeProcessing

·
1747df0c


Eric Bodden authored Jan 29, 2013






1747df0c










28 Jan, 2013
3 commits









refactoring: autoAddZero is now set in IFDSTabulationProblem

·
4b103811


Eric Bodden authored Jan 28, 2013






4b103811













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












herossrcherossolverIDESolver.java
















18 Oct, 2013
1 commit









Revert "first implementation of reduced summaries"

·
6a582013




Eric Bodden authored Oct 18, 2013


This reverts commit f161c043.





6a582013










16 Jul, 2013
1 commit









first implementation of reduced summaries

·
f161c043


Eric Bodden authored Jul 16, 2013






f161c043










11 Jul, 2013
1 commit









simplified exception handling

·
bdc8348c


Eric Bodden authored Jul 11, 2013






bdc8348c










10 Jul, 2013
1 commit









removing superfluous (hopefully?) call to scheduleEdgeProcessing

·
3f7647fe


Eric Bodden authored Jul 10, 2013






3f7647fe










09 Jul, 2013
1 commit









first version of fw-bw lockstep analysis that seems to be working; the trick...

·
1526d8d2




Eric Bodden authored Jul 09, 2013

first version of fw-bw lockstep analysis that seems to be working; the trick was apparently to distinguish balanced from unbalanced returns





1526d8d2










07 Jul, 2013
2 commits









further fix for followReturnPastSeeds:

·
e8034a41




Eric Bodden authored Jul 08, 2013

regarding Steven's earlier fix we now follow a different strategy…
we propagate upwards in an unbalanced way only such facts that originate from ZERO





e8034a41













improved and simplified handling of unbalanced problems:

·
44d17eee




Eric Bodden authored Jul 08, 2013

we now just propagate up the call stack in an unbalanced way if for the very flow fact we are looking at there
was no incoming flow observed earlier





44d17eee










06 Jul, 2013
7 commits









changed signature of "propagate" to include original call site for return and call flows

·
b8e2c3df




Eric Bodden authored Jul 06, 2013


modified bidi solver to changed attached source statement on return





b8e2c3df













removed superfluous type parameter from PathEdge

·
336790c7


Eric Bodden authored Jul 06, 2013






336790c7













make "propagate" protected

·
fef14535


Eric Bodden authored Jul 06, 2013






fef14535













removed unused import

·
734c0ca4


Eric Bodden authored Jul 06, 2013






734c0ca4













added support for debug name

·
68501d99


Eric Bodden authored Jul 06, 2013






68501d99













extracting method submitInitialSeeds to allow submission without having to wait

·
6a9d93c8


Eric Bodden authored Jul 06, 2013






6a9d93c8













made process* methods protected

·
56975c20


Eric Bodden authored Jul 06, 2013






56975c20










05 Jul, 2013
1 commit









changing initialization of analysis such that initialSeeds not is a mapping...

·
275f5783




Eric Bodden authored Jul 05, 2013

changing initialization of analysis such that initialSeeds not is a mapping from units to initial data-flow facts at these units

this is a breaking change, but the class DefaultSeeds can be used to easily convert a set of units (old format) into a default map that should work for current clients

the change was implemented to permit subtypes of IFDSTabulationProblem to overwrite facts at seeds in a convenient way





275f5783










04 Jul, 2013
3 commits









bugfix: when propagating inside out into a caller that was not yet processed...

·
17895853




Eric Bodden authored Jul 04, 2013

bugfix: when propagating inside out into a caller that was not yet processed we need to create a summary edge starting at ZERO, not at the callee's flow fact!





17895853













memory improvement: do not store implicit TOP values

·
987a7a4c


Steven Arzt authored Jul 04, 2013






987a7a4c













bugfix: when propagating inside out into a caller that was not yet processed...

·
3e3888da




Eric Bodden authored Jul 04, 2013

bugfix: when propagating inside out into a caller that was not yet processed we need to create a summary edge starting at ZERO, not at the callee's flow fact!





3e3888da










27 Jun, 2013
1 commit









added exception reporting for tasks in IDESolver

·
b95498c9


Eric Bodden authored Jun 27, 2013






b95498c9










16 Jun, 2013
1 commit









fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

·
0ec8bfc3




Steven Arzt authored Jun 16, 2013

fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return edge gets processed for **all callers** of the callee from which we return in cases where we haven't seen the respective call edge.

This however leads to spurious edges if we have multiple call sites for the same callee. Let us assume call site A calls foo() with jump function a and call site B calls it with b. If we now return from foo with something derived from a, we will not find an incoming edge for call site B. FollowReturnsPastSeeds thus made us propagate the value to both call sites which makes us loose context sensitivity.

Fix as follows: Only follow past seeds if we have seen no call edge into the callee at all. We can then assume that we're really running beyond the seeds of the analysis and have no other chance than propagating to all call sites.





0ec8bfc3










05 Jun, 2013
1 commit









for unbalanced problems on return statements we now use the return flow...

·
4ae358b3




Eric Bodden authored Jun 05, 2013

for unbalanced problems on return statements we now use the return flow function, not the normal flow function
the caller is then simply "null"
added some comments about this





4ae358b3










29 May, 2013
1 commit









adding synchronization on "val" due to possible race conditions (thanks to...

·
525c45fb




Eric Bodden authored May 29, 2013

adding synchronization on "val" due to possible race conditions (thanks to Damien Octeau for reporting this!)





525c45fb










25 Apr, 2013
1 commit









performance fix for return edges; if there were N start points (e.g. in a...

·
6a91d71e




Eric Bodden authored Apr 25, 2013

performance fix for return edges; if there were N start points (e.g. in a backwards analysis) and we did an unbalanced analysis, then the return-foe function was computed N times; now this is done only once





6a91d71e










15 Mar, 2013
1 commit









Bugfix: ProcessCall computed the new jump functions for a method call at a...

·
d564bec7




Steven Arzt authored Mar 15, 2013

Bugfix: ProcessCall computed the new jump functions for a method call at a specific call and return site pair, but then propagated the results to all return sites of this call. This lead to spurious results if there were multiple return sites for a call that had non-equal fact sets on their return edges.





d564bec7










11 Mar, 2013
1 commit









Replaced the duplicate call to the icfg by an access to cached structure we have anyway

·
5c0a40f6


Steven Arzt authored Mar 11, 2013






5c0a40f6










28 Feb, 2013
1 commit









removed stupig bug found by findbugs !!!

·
72286798


Eric Bodden authored Feb 28, 2013






72286798










20 Feb, 2013
1 commit









bugfix: were propagating incorrect values at return-flow edges

·
0645e494


Eric Bodden authored Feb 20, 2013






0645e494










14 Feb, 2013
1 commit









bug fix for value computation (need to treat initialSeeds just as method start nodes)

·
227b9337


Eric Bodden authored Feb 14, 2013






227b9337










30 Jan, 2013
3 commits









extracted method awaitCompletionComputeValuesAndShutdown()

·
446d709e


Eric Bodden authored Jan 30, 2013






446d709e













made scheduleEdgeProcessing protected such that it can be called from the outside

·
abe1050c


Eric Bodden authored Jan 30, 2013






abe1050c













Revert "removed superfluous call to scheduleEdgeProcessing"

·
2c7e04fd




Eric Bodden authored Jan 30, 2013


This reverts commit 1747df0c.

The change was wrong; scheduleEdgeProcessing does need to be called explicitly at initialization time!





2c7e04fd










29 Jan, 2013
6 commits









fixing shutdown sequence

·
e0e51e68


Eric Bodden authored Jan 29, 2013






e0e51e68













making executor exchangeable

·
18c72755


Eric Bodden authored Jan 29, 2013






18c72755













make computation of values optional

·
357e129e


Eric Bodden authored Jan 29, 2013






357e129e













number of threads is now configured through SolverConfiguration, a new super...

·
1e7167fe




Eric Bodden authored Jan 29, 2013

number of threads is now configured through SolverConfiguration, a new super interface of IFDSTabulationProblem





1e7167fe













indentation

·
aa7b4340


Eric Bodden authored Jan 29, 2013






aa7b4340













removed superfluous call to scheduleEdgeProcessing

·
1747df0c


Eric Bodden authored Jan 29, 2013






1747df0c










28 Jan, 2013
3 commits









refactoring: autoAddZero is now set in IFDSTabulationProblem

·
4b103811


Eric Bodden authored Jan 28, 2013






4b103811













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























herossrcherossolverIDESolver.java
















18 Oct, 2013
1 commit









Revert "first implementation of reduced summaries"

·
6a582013




Eric Bodden authored Oct 18, 2013


This reverts commit f161c043.





6a582013










16 Jul, 2013
1 commit









first implementation of reduced summaries

·
f161c043


Eric Bodden authored Jul 16, 2013






f161c043










11 Jul, 2013
1 commit









simplified exception handling

·
bdc8348c


Eric Bodden authored Jul 11, 2013






bdc8348c










10 Jul, 2013
1 commit









removing superfluous (hopefully?) call to scheduleEdgeProcessing

·
3f7647fe


Eric Bodden authored Jul 10, 2013






3f7647fe










09 Jul, 2013
1 commit









first version of fw-bw lockstep analysis that seems to be working; the trick...

·
1526d8d2




Eric Bodden authored Jul 09, 2013

first version of fw-bw lockstep analysis that seems to be working; the trick was apparently to distinguish balanced from unbalanced returns





1526d8d2










07 Jul, 2013
2 commits









further fix for followReturnPastSeeds:

·
e8034a41




Eric Bodden authored Jul 08, 2013

regarding Steven's earlier fix we now follow a different strategy…
we propagate upwards in an unbalanced way only such facts that originate from ZERO





e8034a41













improved and simplified handling of unbalanced problems:

·
44d17eee




Eric Bodden authored Jul 08, 2013

we now just propagate up the call stack in an unbalanced way if for the very flow fact we are looking at there
was no incoming flow observed earlier





44d17eee










06 Jul, 2013
7 commits









changed signature of "propagate" to include original call site for return and call flows

·
b8e2c3df




Eric Bodden authored Jul 06, 2013


modified bidi solver to changed attached source statement on return





b8e2c3df













removed superfluous type parameter from PathEdge

·
336790c7


Eric Bodden authored Jul 06, 2013






336790c7













make "propagate" protected

·
fef14535


Eric Bodden authored Jul 06, 2013






fef14535













removed unused import

·
734c0ca4


Eric Bodden authored Jul 06, 2013






734c0ca4













added support for debug name

·
68501d99


Eric Bodden authored Jul 06, 2013






68501d99













extracting method submitInitialSeeds to allow submission without having to wait

·
6a9d93c8


Eric Bodden authored Jul 06, 2013






6a9d93c8













made process* methods protected

·
56975c20


Eric Bodden authored Jul 06, 2013






56975c20










05 Jul, 2013
1 commit









changing initialization of analysis such that initialSeeds not is a mapping...

·
275f5783




Eric Bodden authored Jul 05, 2013

changing initialization of analysis such that initialSeeds not is a mapping from units to initial data-flow facts at these units

this is a breaking change, but the class DefaultSeeds can be used to easily convert a set of units (old format) into a default map that should work for current clients

the change was implemented to permit subtypes of IFDSTabulationProblem to overwrite facts at seeds in a convenient way





275f5783










04 Jul, 2013
3 commits









bugfix: when propagating inside out into a caller that was not yet processed...

·
17895853




Eric Bodden authored Jul 04, 2013

bugfix: when propagating inside out into a caller that was not yet processed we need to create a summary edge starting at ZERO, not at the callee's flow fact!





17895853













memory improvement: do not store implicit TOP values

·
987a7a4c


Steven Arzt authored Jul 04, 2013






987a7a4c













bugfix: when propagating inside out into a caller that was not yet processed...

·
3e3888da




Eric Bodden authored Jul 04, 2013

bugfix: when propagating inside out into a caller that was not yet processed we need to create a summary edge starting at ZERO, not at the callee's flow fact!





3e3888da










27 Jun, 2013
1 commit









added exception reporting for tasks in IDESolver

·
b95498c9


Eric Bodden authored Jun 27, 2013






b95498c9










16 Jun, 2013
1 commit









fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

·
0ec8bfc3




Steven Arzt authored Jun 16, 2013

fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return edge gets processed for **all callers** of the callee from which we return in cases where we haven't seen the respective call edge.

This however leads to spurious edges if we have multiple call sites for the same callee. Let us assume call site A calls foo() with jump function a and call site B calls it with b. If we now return from foo with something derived from a, we will not find an incoming edge for call site B. FollowReturnsPastSeeds thus made us propagate the value to both call sites which makes us loose context sensitivity.

Fix as follows: Only follow past seeds if we have seen no call edge into the callee at all. We can then assume that we're really running beyond the seeds of the analysis and have no other chance than propagating to all call sites.





0ec8bfc3










05 Jun, 2013
1 commit









for unbalanced problems on return statements we now use the return flow...

·
4ae358b3




Eric Bodden authored Jun 05, 2013

for unbalanced problems on return statements we now use the return flow function, not the normal flow function
the caller is then simply "null"
added some comments about this





4ae358b3










29 May, 2013
1 commit









adding synchronization on "val" due to possible race conditions (thanks to...

·
525c45fb




Eric Bodden authored May 29, 2013

adding synchronization on "val" due to possible race conditions (thanks to Damien Octeau for reporting this!)





525c45fb










25 Apr, 2013
1 commit









performance fix for return edges; if there were N start points (e.g. in a...

·
6a91d71e




Eric Bodden authored Apr 25, 2013

performance fix for return edges; if there were N start points (e.g. in a backwards analysis) and we did an unbalanced analysis, then the return-foe function was computed N times; now this is done only once





6a91d71e










15 Mar, 2013
1 commit









Bugfix: ProcessCall computed the new jump functions for a method call at a...

·
d564bec7




Steven Arzt authored Mar 15, 2013

Bugfix: ProcessCall computed the new jump functions for a method call at a specific call and return site pair, but then propagated the results to all return sites of this call. This lead to spurious results if there were multiple return sites for a call that had non-equal fact sets on their return edges.





d564bec7










11 Mar, 2013
1 commit









Replaced the duplicate call to the icfg by an access to cached structure we have anyway

·
5c0a40f6


Steven Arzt authored Mar 11, 2013






5c0a40f6










28 Feb, 2013
1 commit









removed stupig bug found by findbugs !!!

·
72286798


Eric Bodden authored Feb 28, 2013






72286798










20 Feb, 2013
1 commit









bugfix: were propagating incorrect values at return-flow edges

·
0645e494


Eric Bodden authored Feb 20, 2013






0645e494










14 Feb, 2013
1 commit









bug fix for value computation (need to treat initialSeeds just as method start nodes)

·
227b9337


Eric Bodden authored Feb 14, 2013






227b9337










30 Jan, 2013
3 commits









extracted method awaitCompletionComputeValuesAndShutdown()

·
446d709e


Eric Bodden authored Jan 30, 2013






446d709e













made scheduleEdgeProcessing protected such that it can be called from the outside

·
abe1050c


Eric Bodden authored Jan 30, 2013






abe1050c













Revert "removed superfluous call to scheduleEdgeProcessing"

·
2c7e04fd




Eric Bodden authored Jan 30, 2013


This reverts commit 1747df0c.

The change was wrong; scheduleEdgeProcessing does need to be called explicitly at initialization time!





2c7e04fd










29 Jan, 2013
6 commits









fixing shutdown sequence

·
e0e51e68


Eric Bodden authored Jan 29, 2013






e0e51e68













making executor exchangeable

·
18c72755


Eric Bodden authored Jan 29, 2013






18c72755













make computation of values optional

·
357e129e


Eric Bodden authored Jan 29, 2013






357e129e













number of threads is now configured through SolverConfiguration, a new super...

·
1e7167fe




Eric Bodden authored Jan 29, 2013

number of threads is now configured through SolverConfiguration, a new super interface of IFDSTabulationProblem





1e7167fe













indentation

·
aa7b4340


Eric Bodden authored Jan 29, 2013






aa7b4340













removed superfluous call to scheduleEdgeProcessing

·
1747df0c


Eric Bodden authored Jan 29, 2013






1747df0c










28 Jan, 2013
3 commits









refactoring: autoAddZero is now set in IFDSTabulationProblem

·
4b103811


Eric Bodden authored Jan 28, 2013






4b103811













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



















herossrcherossolverIDESolver.java


















herossrcherossolverIDESolver.java
















herossrcherossolverIDESolver.java



herossrcherossolverIDESolver.java













18 Oct, 2013
1 commit









Revert "first implementation of reduced summaries"

·
6a582013




Eric Bodden authored Oct 18, 2013


This reverts commit f161c043.





6a582013










16 Jul, 2013
1 commit









first implementation of reduced summaries

·
f161c043


Eric Bodden authored Jul 16, 2013






f161c043










11 Jul, 2013
1 commit









simplified exception handling

·
bdc8348c


Eric Bodden authored Jul 11, 2013






bdc8348c










10 Jul, 2013
1 commit









removing superfluous (hopefully?) call to scheduleEdgeProcessing

·
3f7647fe


Eric Bodden authored Jul 10, 2013






3f7647fe










09 Jul, 2013
1 commit









first version of fw-bw lockstep analysis that seems to be working; the trick...

·
1526d8d2




Eric Bodden authored Jul 09, 2013

first version of fw-bw lockstep analysis that seems to be working; the trick was apparently to distinguish balanced from unbalanced returns





1526d8d2










07 Jul, 2013
2 commits









further fix for followReturnPastSeeds:

·
e8034a41




Eric Bodden authored Jul 08, 2013

regarding Steven's earlier fix we now follow a different strategy…
we propagate upwards in an unbalanced way only such facts that originate from ZERO





e8034a41













improved and simplified handling of unbalanced problems:

·
44d17eee




Eric Bodden authored Jul 08, 2013

we now just propagate up the call stack in an unbalanced way if for the very flow fact we are looking at there
was no incoming flow observed earlier





44d17eee










06 Jul, 2013
7 commits









changed signature of "propagate" to include original call site for return and call flows

·
b8e2c3df




Eric Bodden authored Jul 06, 2013


modified bidi solver to changed attached source statement on return





b8e2c3df













removed superfluous type parameter from PathEdge

·
336790c7


Eric Bodden authored Jul 06, 2013






336790c7













make "propagate" protected

·
fef14535


Eric Bodden authored Jul 06, 2013






fef14535













removed unused import

·
734c0ca4


Eric Bodden authored Jul 06, 2013






734c0ca4













added support for debug name

·
68501d99


Eric Bodden authored Jul 06, 2013






68501d99













extracting method submitInitialSeeds to allow submission without having to wait

·
6a9d93c8


Eric Bodden authored Jul 06, 2013






6a9d93c8













made process* methods protected

·
56975c20


Eric Bodden authored Jul 06, 2013






56975c20










05 Jul, 2013
1 commit









changing initialization of analysis such that initialSeeds not is a mapping...

·
275f5783




Eric Bodden authored Jul 05, 2013

changing initialization of analysis such that initialSeeds not is a mapping from units to initial data-flow facts at these units

this is a breaking change, but the class DefaultSeeds can be used to easily convert a set of units (old format) into a default map that should work for current clients

the change was implemented to permit subtypes of IFDSTabulationProblem to overwrite facts at seeds in a convenient way





275f5783










04 Jul, 2013
3 commits









bugfix: when propagating inside out into a caller that was not yet processed...

·
17895853




Eric Bodden authored Jul 04, 2013

bugfix: when propagating inside out into a caller that was not yet processed we need to create a summary edge starting at ZERO, not at the callee's flow fact!





17895853













memory improvement: do not store implicit TOP values

·
987a7a4c


Steven Arzt authored Jul 04, 2013






987a7a4c













bugfix: when propagating inside out into a caller that was not yet processed...

·
3e3888da




Eric Bodden authored Jul 04, 2013

bugfix: when propagating inside out into a caller that was not yet processed we need to create a summary edge starting at ZERO, not at the callee's flow fact!





3e3888da










27 Jun, 2013
1 commit









added exception reporting for tasks in IDESolver

·
b95498c9


Eric Bodden authored Jun 27, 2013






b95498c9










16 Jun, 2013
1 commit









fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return...

·
0ec8bfc3




Steven Arzt authored Jun 16, 2013

fixed the semantics of FollowReturnsPastSeeds: If this option is set, a return edge gets processed for **all callers** of the callee from which we return in cases where we haven't seen the respective call edge.

This however leads to spurious edges if we have multiple call sites for the same callee. Let us assume call site A calls foo() with jump function a and call site B calls it with b. If we now return from foo with something derived from a, we will not find an incoming edge for call site B. FollowReturnsPastSeeds thus made us propagate the value to both call sites which makes us loose context sensitivity.

Fix as follows: Only follow past seeds if we have seen no call edge into the callee at all. We can then assume that we're really running beyond the seeds of the analysis and have no other chance than propagating to all call sites.





0ec8bfc3










05 Jun, 2013
1 commit









for unbalanced problems on return statements we now use the return flow...

·
4ae358b3




Eric Bodden authored Jun 05, 2013

for unbalanced problems on return statements we now use the return flow function, not the normal flow function
the caller is then simply "null"
added some comments about this





4ae358b3










29 May, 2013
1 commit









adding synchronization on "val" due to possible race conditions (thanks to...

·
525c45fb




Eric Bodden authored May 29, 2013

adding synchronization on "val" due to possible race conditions (thanks to Damien Octeau for reporting this!)





525c45fb










25 Apr, 2013
1 commit









performance fix for return edges; if there were N start points (e.g. in a...

·
6a91d71e




Eric Bodden authored Apr 25, 2013

performance fix for return edges; if there were N start points (e.g. in a backwards analysis) and we did an unbalanced analysis, then the return-foe function was computed N times; now this is done only once





6a91d71e










15 Mar, 2013
1 commit









Bugfix: ProcessCall computed the new jump functions for a method call at a...

·
d564bec7




Steven Arzt authored Mar 15, 2013

Bugfix: ProcessCall computed the new jump functions for a method call at a specific call and return site pair, but then propagated the results to all return sites of this call. This lead to spurious results if there were multiple return sites for a call that had non-equal fact sets on their return edges.





d564bec7










11 Mar, 2013
1 commit









Replaced the duplicate call to the icfg by an access to cached structure we have anyway

·
5c0a40f6


Steven Arzt authored Mar 11, 2013






5c0a40f6










28 Feb, 2013
1 commit









removed stupig bug found by findbugs !!!

·
72286798


Eric Bodden authored Feb 28, 2013






72286798










20 Feb, 2013
1 commit









bugfix: were propagating incorrect values at return-flow edges

·
0645e494


Eric Bodden authored Feb 20, 2013






0645e494










14 Feb, 2013
1 commit









bug fix for value computation (need to treat initialSeeds just as method start nodes)

·
227b9337


Eric Bodden authored Feb 14, 2013






227b9337










30 Jan, 2013
3 commits









extracted method awaitCompletionComputeValuesAndShutdown()

·
446d709e


Eric Bodden authored Jan 30, 2013






446d709e













made scheduleEdgeProcessing protected such that it can be called from the outside

·
abe1050c


Eric Bodden authored Jan 30, 2013






abe1050c













Revert "removed superfluous call to scheduleEdgeProcessing"

·
2c7e04fd




Eric Bodden authored Jan 30, 2013


This reverts commit 1747df0c.

The change was wrong; scheduleEdgeProcessing does need to be called explicitly at initialization time!





2c7e04fd










29 Jan, 2013
6 commits









fixing shutdown sequence

·
e0e51e68


Eric Bodden authored Jan 29, 2013






e0e51e68













making executor exchangeable

·
18c72755


Eric Bodden authored Jan 29, 2013






18c72755













make computation of values optional

·
357e129e


Eric Bodden authored Jan 29, 2013






357e129e













number of threads is now configured through SolverConfiguration, a new super...

·
1e7167fe




Eric Bodden authored Jan 29, 2013

number of threads is now configured through SolverConfiguration, a new super interface of IFDSTabulationProblem





1e7167fe













indentation

·
aa7b4340


Eric Bodden authored Jan 29, 2013






aa7b4340













removed superfluous call to scheduleEdgeProcessing

·
1747df0c


Eric Bodden authored Jan 29, 2013






1747df0c










28 Jan, 2013
3 commits









refactoring: autoAddZero is now set in IFDSTabulationProblem

·
4b103811


Eric Bodden authored Jan 28, 2013






4b103811













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











18 Oct, 2013
1 commit
18 Oct, 20131 commit







Revert "first implementation of reduced summaries"

·
6a582013




Eric Bodden authored Oct 18, 2013


This reverts commit f161c043.





6a582013














Revert "first implementation of reduced summaries"

·
6a582013




Eric Bodden authored Oct 18, 2013


This reverts commit f161c043.





6a582013










Revert "first implementation of reduced summaries"

·
6a582013




Eric Bodden authored Oct 18, 2013


This reverts commit f161c043.





6a582013






Revert "first implementation of reduced summaries"

·
6a582013




Eric Bodden authored Oct 18, 2013


This reverts commit f161c043.

·
6a582013

Eric Bodden authored Oct 18, 2013




6a582013






6a582013




6a582013

16 Jul, 2013
1 commit
16 Jul, 20131 commit







first implementation of reduced summaries

·
f161c043


Eric Bodden authored Jul 16, 2013






f161c043














first implementation of reduced summaries

·
f161c043


Eric Bodden authored Jul 16, 2013






f161c043










first implementation of reduced summaries

·
f161c043


Eric Bodden authored Jul 16, 2013






f161c043






first implementation of reduced summaries

·
f161c043


Eric Bodden authored Jul 16, 2013


·
f161c043

Eric Bodden authored Jul 16, 2013




f161c043






f161c043




f161c043

11 Jul, 2013
1 commit
11 Jul, 20131 commit







simplified exception handling

·
bdc8348c


Eric Bodden authored Jul 11, 2013






bdc8348c














simplified exception handling

·
bdc8348c


Eric Bodden authored Jul 11, 2013






bdc8348c










simplified exception handling

·
bdc8348c


Eric Bodden authored Jul 11, 2013






bdc8348c






simplified exception handling

·
bdc8348c


Eric Bodden authored Jul 11, 2013


·
bdc8348c

Eric Bodden authored Jul 11, 2013




bdc8348c






bdc8348c




bdc8348c

10 Jul, 2013
1 commit
10 Jul, 20131 commit







removing superfluous (hopefully?) call to scheduleEdgeProcessing

·
3f7647fe


Eric Bodden authored Jul 10, 2013






3f7647fe














removing superfluous (hopefully?) call to scheduleEdgeProcessing

·
3f7647fe


Eric Bodden authored Jul 10, 2013






3f7647fe










removing superfluous (hopefully?) call to scheduleEdgeProcessing

·
3f7647fe


Eric Bodden authored Jul 10, 2013






3f7647fe






removing superfluous (hopefully?) call to scheduleEdgeProcessing

·
3f7647fe


Eric Bodden authored Jul 10, 2013


·
3f7647fe

Eric Bodden authored Jul 10, 2013




3f7647fe






3f7647fe




3f7647fe

09 Jul, 2013
1 commit
09 Jul, 20131 commit







first version of fw-bw lockstep analysis that seems to be working; the trick...

·
1526d8d2




Eric Bodden authored Jul 09, 2013

first version of fw-bw lockstep analysis that seems to be working; the trick was apparently to distinguish balanced from unbalanced returns





1526d8d2














first version of fw-bw lockstep analysis that seems to be working; the trick...

·
1526d8d2




Eric Bodden authored Jul 09, 2013

first version of fw-bw lockstep analysis that seems to be working; the trick was apparently to distinguish balanced from unbalanced returns





1526d8d2










first version of fw-bw lockstep analysis that seems to be working; the trick...

·
1526d8d2




Eric Bodden authored Jul 09, 2013

first version of fw-bw lockstep analysis that seems to be working; the trick was apparently to distinguish balanced from unbalanced returns





1526d8d2






first version of fw-bw lockstep analysis that seems to be working; the trick...

·
1526d8d2




Eric Bodden authored Jul 09, 2013

first version of fw-bw lockstep analysis that seems to be working; the trick was apparently to distinguish balanced from unbalanced returns

·
1526d8d2

Eric Bodden authored Jul 09, 2013




1526d8d2






1526d8d2




1526d8d2

07 Jul, 2013
2 commits
07 Jul, 20132 commits







further fix for followReturnPastSeeds:

·
e8034a41




Eric Bodden authored Jul 08, 2013

regarding Steven's earlier fix we now follow a different strategy…
we propagate upwards in an unbalanced way only such facts that originate from ZERO





e8034a41













improved and simplified handling of unbalanced problems:

·
44d17eee




Eric Bodden authored Jul 08, 2013

we now just propagate up the call stack in an unbalanced way if for the very flow fact we are looking at there
was no incoming flow observed earlier





44d17eee














further fix for followReturnPastSeeds:

·
e8034a41




Eric Bodden authored Jul 08, 2013

regarding Steven's earlier fix we now follow a different strategy…
we propagate upwards in an unbalanced way only such facts that originate from ZERO





e8034a41










further fix for followReturnPastSeeds:

·
e8034a41




Eric Bodden authored Jul 08, 2013

regarding Steven's earlier fix we now follow a different strategy…
we propagate upwards in an unbalanced way only such facts that originate from ZERO





e8034a41






further fix for followReturnPastSeeds:

·
e8034a41




Eric Bodden authored Jul 08, 2013

regarding Steven's earlier fix we now follow a different strategy…
we propagate upwards in an unbalanced way only such facts that originate from ZERO

·
e8034a41

Eric Bodden authored Jul 08, 2013




e8034a41






e8034a41




e8034a41






improved and simplified handling of unbalanced problems:

·
44d17eee




Eric Bodden authored Jul 08, 2013

we now just propagate up the call stack in an unbalanced way if for the very flow fact we are looking at there
was no incoming flow observed earlier





44d17eee










improved and simplified handling of unbalanced problems:

·
44d17eee




Eric Bodden authored Jul 08, 2013

we now just propagate up the call stack in an unbalanced way if for the very flow fact we are looking at there
was no incoming flow observed earlier





44d17eee






improved and simplified handling of unbalanced problems:

·
44d17eee




Eric Bodden authored Jul 08, 2013

we now just propagate up the call stack in an unbalanced way if for the very flow fact we are looking at there
was no incoming flow observed earlier

·
44d17eee

Eric Bodden authored Jul 08, 2013




44d17eee






44d17eee




44d17eee

06 Jul, 2013
7 commits
06 Jul, 20137 commits







changed signature of "propagate" to include original call site for return and call flows

·
b8e2c3df




Eric Bodden authored Jul 06, 2013


modified bidi solver to changed attached source statement on return





b8e2c3df













removed superfluous type parameter from PathEdge

·
336790c7


Eric Bodden authored Jul 06, 2013






336790c7













make "propagate" protected

·
fef14535


Eric Bodden authored Jul 06, 2013






fef14535













removed unused import

·
734c0ca4


Eric Bodden authored Jul 06, 2013






734c0ca4













added support for debug name

·
68501d99


Eric Bodden authored Jul 06, 2013






68501d99













extracting method submitInitialSeeds to allow submission without having to wait

·
6a9d93c8


Eric Bodden authored Jul 06, 2013






6a9d93c8













made process* methods protected

·
56975c20


Eric Bodden authored Jul 06, 2013






56975c20














changed signature of "propagate" to include original call site for return and call flows

·
b8e2c3df




Eric Bodden authored Jul 06, 2013


modified bidi solver to changed attached source statement on return





b8e2c3df










changed signature of "propagate" to include original call site for return and call flows

·
b8e2c3df




Eric Bodden authored Jul 06, 2013


modified bidi solver to changed attached source statement on return





b8e2c3df






changed signature of "propagate" to include original call site for return and call flows

·
b8e2c3df




Eric Bodden authored Jul 06, 2013


modified bidi solver to changed attached source statement on return

·
b8e2c3df

Eric Bodden authored Jul 06, 2013




b8e2c3df






b8e2c3df




b8e2c3df






removed superfluous type parameter from PathEdge

·
336790c7


Eric Bodden authored Jul 06, 2013






336790c7










removed superfluous type parameter from PathEdge

·
336790c7


Eric Bodden authored Jul 06, 2013






336790c7






removed superfluous type parameter from PathEdge

·
336790c7


Eric Bodden authored Jul 06, 2013


·
336790c7

Eric Bodden authored Jul 06, 2013




336790c7






336790c7




336790c7






make "propagate" protected

·
fef14535


Eric Bodden authored Jul 06, 2013






fef14535










make "propagate" protected

·
fef14535


Eric Bodden authored Jul 06, 2013






fef14535






make "propagate" protected

·
fef14535


Eric Bodden authored Jul 06, 2013


·
fef14535

Eric Bodden authored Jul 06, 2013




fef14535






fef14535




fef14535






removed unused import

·
734c0ca4


Eric Bodden authored Jul 06, 2013






734c0ca4










removed unused import

·
734c0ca4


Eric Bodden authored Jul 06, 2013






734c0ca4






removed unused import

·
734c0ca4


Eric Bodden authored Jul 06, 2013


·
734c0ca4

Eric Bodden authored Jul 06, 2013




734c0ca4






734c0ca4




734c0ca4






added support for debug name

·
68501d99


Eric Bodden authored Jul 06, 2013






68501d99










added support for debug name

·
68501d99


Eric Bodden authored Jul 06, 2013






68501d99






added support for debug name

·
68501d99


Eric Bodden authored Jul 06, 2013


·
68501d99

Eric Bodden authored Jul 06, 2013




68501d99






68501d99




68501d99






extracting method submitInitialSeeds to allow submission without having to wait

·
6a9d93c8


Eric Bodden authored Jul 06, 2013






6a9d93c8










extracting method submitInitialSeeds to allow submission without having to wait

·
6a9d93c8


Eric Bodden authored Jul 06, 2013






6a9d93c8






extracting method submitInitialSeeds to allow submission without having to wait

·
6a9d93c8


Eric Bodden authored Jul 06, 2013


·
6a9d93c8

Eric Bodden authored Jul 06, 2013




6a9d93c8






6a9d93c8




6a9d93c8






made process* methods protected

·
56975c20


Eric Bodden authored Jul 06, 2013






56975c20










made process* methods protected

·
56975c20


Eric Bodden authored Jul 06, 2013






56975c20






made process* methods protected

·
56975c20


Eric Bodden authored Jul 06, 2013


·
56975c20

Eric Bodden authored Jul 06, 2013




56975c20






56975c20




56975c20

05 Jul, 2013
1 commit
05 Jul, 20131 commit







changing initialization of analysis such that initialSeeds not is a mapping...

·
275f5783




Eric Bodden authored Jul 05, 2013

changing initialization of analysis such that initialSeeds not is a mapping from units to initial data-flow facts at these units

this is a breaking change, but the class DefaultSeeds can be used to easily convert a set of units (old format) into a default map that should work for current clients

the change was implemented to permit subtypes of IFDSTabulationProblem to overwrite facts at seeds in a convenient way





275f5783














changing initialization of analysis such that initialSeeds not is a mapping...

·
275f5783




Eric Bodden authored Jul 05, 2013

changing initialization of analysis such that initialSeeds not is a mapping from units to initial data-flow facts at these units

this is a breaking change, but the class DefaultSeeds can be used to easily convert a set of units (old format) into a default map that should work for current clients

the change was implemented to permit subtypes of IFDSTabulationProblem to overwrite facts at seeds in a convenient way





275f5783










changing initialization of analysis such that initialSeeds not is a mapping...

·
275f5783




Eric Bodden authored Jul 05, 2013

changing initialization of analysis such that initialSeeds not is a mapping from units to initial data-flow facts at these units

this is a breaking change, but the class DefaultSeeds can be used to easily convert a set of units (old format) into a default map that should work for current clients

the change was implemented to permit subtypes of IFDSTabulationProblem to overwrite facts at seeds in a convenient way





275f5783






changing initialization of analysis such that initialSeeds not is a mapping...

·
275f5783




Eric Bodden authored Jul 05, 2013

changing initialization of analysis such that initialSeeds not is a mapping from units to initial data-flow facts at these units

this is a breaking change, but the class DefaultSeeds can be used to easily convert a set of units (old format) into a default map that should work for current clients

the change was implemented to permit subtypes of IFDSTabulationProblem to overwrite facts at seeds in a convenient way

·
275f5783

Eric Bodden authored Jul 05, 2013




275f5783






275f5783




275f5783

04 Jul, 2013
3 commits
04 Jul, 20133 commits







bugfix: when propagating inside out into a caller that was not yet processed...

·
17895853




Eric Bodden authored Jul 04, 2013

bugfix: when propagating inside out into a caller that was not yet processed we need to create a summary edge starting at ZERO, not at the callee's flow fact!





17895853













memory improvement: do not store implicit TOP values

·
987a7a4c


Steven Arzt authored Jul 04, 2013






987a7a4c













bugfix: when propagating inside out into a caller that was not yet processed...

·
3e3888da




Eric Bodden authored Jul 04, 2013

bugfix: when propagating inside out into a caller that was not yet processed we need to create a summary edge starting at ZERO, not at the callee's flow fact!





3e3888da














bugfix: when propagating inside out into a caller that was not yet processed...

·
17895853




Eric Bodden authored Jul 04, 2013

bugfix: when propagating inside out into a caller that was not yet processed we need to create a summary edge starting at ZERO, not at the callee's flow fact!





17895853










bugfix: when propagating inside out into a caller that was not yet processed...

·
17895853




Eric Bodden authored Jul 04, 2013

bugfix: when propagating inside out into a caller that was not yet processed we need to create a summary edge starting at ZERO, not at the callee's flow fact!





17895853






bugfix: when propagating inside out into a caller that was not yet processed...

·
17895853




Eric Bodden authored Jul 04, 2013

bugfix: when propagating inside out into a caller that was not yet processed we need to create a summary edge starting at ZERO, not at the callee's flow fact!

·
17895853

Eric Bodden authored Jul 04, 2013




17895853






17895853




17895853






memory improvement: do not store implicit TOP values

·
987a7a4c


Steven Arzt authored Jul 04, 2013






987a7a4c










memory improvement: do not store implicit TOP values

·
987a7a4c


Steven Arzt authored Jul 04, 2013






987a7a4c






memory improvement: do not store implicit TOP values

·
987a7a4c


Steven Arzt authored Jul 04, 2013


·
987a7a4c

Steven Arzt authored Jul 04, 2013




987a7a4c






987a7a4c




987a7a4c






bugfix: when propagating inside out into a caller that was not yet processed...

·
3e3888da




Eric Bodden authored Jul 04, 2013

bugfix: when propagating inside out into a caller that was not yet processed we need to create a summary edge starting at ZERO, not at the callee's flow fact!





3e3888da










bugfix: when propagating inside out into a caller that was not yet processed...

·
3e3888da




Eric Bodden authored Jul 04, 2013

bugfix: when propagating inside out into a caller that was not yet processed we need to create a summary edge starting at ZERO, not at the callee's flow fact!





3e3888da






bugfix: when propagating inside out into a caller that was not yet processed...

·
3e3888da




Eric Bodden authored Jul 04, 2013

bugfix: when propagating inside out into a caller that was not yet processed we need to create a summary edge starting at ZERO, not at the callee's flow fact!

·
3e3888da

Eric Bodden authored Jul 04, 2013




3e3888da






3e3888da




3e3888da

27 Jun, 2013
1 commit
27 Jun, 20131 commit







added exception reporting for tasks in IDESolver

·
b95498c9


Eric Bodden authored Jun 27, 2013






b95498c9














added exception reporting for tasks in IDESolver

·
b95498c9


Eric Bodden authored Jun 27, 2013






b95498c9










added exception reporting for tasks in IDESolver

·
b95498c9


Eric Bodden authored Jun 27, 2013






b95498c9






added exception reporting for tasks in IDESolver

·
b95498c9


Eric Bodden authored Jun 27, 2013


·
b95498c9

Eric Bodden authored Jun 27, 2013




b95498c9






b95498c9




b95498c9

16 Jun, 2013
1 commit
16 Jun, 20131 commit







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

05 Jun, 2013
1 commit
05 Jun, 20131 commit







for unbalanced problems on return statements we now use the return flow...

·
4ae358b3




Eric Bodden authored Jun 05, 2013

for unbalanced problems on return statements we now use the return flow function, not the normal flow function
the caller is then simply "null"
added some comments about this





4ae358b3














for unbalanced problems on return statements we now use the return flow...

·
4ae358b3




Eric Bodden authored Jun 05, 2013

for unbalanced problems on return statements we now use the return flow function, not the normal flow function
the caller is then simply "null"
added some comments about this





4ae358b3










for unbalanced problems on return statements we now use the return flow...

·
4ae358b3




Eric Bodden authored Jun 05, 2013

for unbalanced problems on return statements we now use the return flow function, not the normal flow function
the caller is then simply "null"
added some comments about this





4ae358b3






for unbalanced problems on return statements we now use the return flow...

·
4ae358b3




Eric Bodden authored Jun 05, 2013

for unbalanced problems on return statements we now use the return flow function, not the normal flow function
the caller is then simply "null"
added some comments about this

·
4ae358b3

Eric Bodden authored Jun 05, 2013




4ae358b3






4ae358b3




4ae358b3

29 May, 2013
1 commit
29 May, 20131 commit







adding synchronization on "val" due to possible race conditions (thanks to...

·
525c45fb




Eric Bodden authored May 29, 2013

adding synchronization on "val" due to possible race conditions (thanks to Damien Octeau for reporting this!)





525c45fb














adding synchronization on "val" due to possible race conditions (thanks to...

·
525c45fb




Eric Bodden authored May 29, 2013

adding synchronization on "val" due to possible race conditions (thanks to Damien Octeau for reporting this!)





525c45fb










adding synchronization on "val" due to possible race conditions (thanks to...

·
525c45fb




Eric Bodden authored May 29, 2013

adding synchronization on "val" due to possible race conditions (thanks to Damien Octeau for reporting this!)





525c45fb






adding synchronization on "val" due to possible race conditions (thanks to...

·
525c45fb




Eric Bodden authored May 29, 2013

adding synchronization on "val" due to possible race conditions (thanks to Damien Octeau for reporting this!)

·
525c45fb

Eric Bodden authored May 29, 2013




525c45fb






525c45fb




525c45fb

25 Apr, 2013
1 commit
25 Apr, 20131 commit







performance fix for return edges; if there were N start points (e.g. in a...

·
6a91d71e




Eric Bodden authored Apr 25, 2013

performance fix for return edges; if there were N start points (e.g. in a backwards analysis) and we did an unbalanced analysis, then the return-foe function was computed N times; now this is done only once





6a91d71e














performance fix for return edges; if there were N start points (e.g. in a...

·
6a91d71e




Eric Bodden authored Apr 25, 2013

performance fix for return edges; if there were N start points (e.g. in a backwards analysis) and we did an unbalanced analysis, then the return-foe function was computed N times; now this is done only once





6a91d71e










performance fix for return edges; if there were N start points (e.g. in a...

·
6a91d71e




Eric Bodden authored Apr 25, 2013

performance fix for return edges; if there were N start points (e.g. in a backwards analysis) and we did an unbalanced analysis, then the return-foe function was computed N times; now this is done only once





6a91d71e






performance fix for return edges; if there were N start points (e.g. in a...

·
6a91d71e




Eric Bodden authored Apr 25, 2013

performance fix for return edges; if there were N start points (e.g. in a backwards analysis) and we did an unbalanced analysis, then the return-foe function was computed N times; now this is done only once

·
6a91d71e

Eric Bodden authored Apr 25, 2013




6a91d71e






6a91d71e




6a91d71e

15 Mar, 2013
1 commit
15 Mar, 20131 commit







Bugfix: ProcessCall computed the new jump functions for a method call at a...

·
d564bec7




Steven Arzt authored Mar 15, 2013

Bugfix: ProcessCall computed the new jump functions for a method call at a specific call and return site pair, but then propagated the results to all return sites of this call. This lead to spurious results if there were multiple return sites for a call that had non-equal fact sets on their return edges.





d564bec7














Bugfix: ProcessCall computed the new jump functions for a method call at a...

·
d564bec7




Steven Arzt authored Mar 15, 2013

Bugfix: ProcessCall computed the new jump functions for a method call at a specific call and return site pair, but then propagated the results to all return sites of this call. This lead to spurious results if there were multiple return sites for a call that had non-equal fact sets on their return edges.





d564bec7










Bugfix: ProcessCall computed the new jump functions for a method call at a...

·
d564bec7




Steven Arzt authored Mar 15, 2013

Bugfix: ProcessCall computed the new jump functions for a method call at a specific call and return site pair, but then propagated the results to all return sites of this call. This lead to spurious results if there were multiple return sites for a call that had non-equal fact sets on their return edges.





d564bec7






Bugfix: ProcessCall computed the new jump functions for a method call at a...

·
d564bec7




Steven Arzt authored Mar 15, 2013

Bugfix: ProcessCall computed the new jump functions for a method call at a specific call and return site pair, but then propagated the results to all return sites of this call. This lead to spurious results if there were multiple return sites for a call that had non-equal fact sets on their return edges.

·
d564bec7

Steven Arzt authored Mar 15, 2013




d564bec7






d564bec7




d564bec7

11 Mar, 2013
1 commit
11 Mar, 20131 commit







Replaced the duplicate call to the icfg by an access to cached structure we have anyway

·
5c0a40f6


Steven Arzt authored Mar 11, 2013






5c0a40f6














Replaced the duplicate call to the icfg by an access to cached structure we have anyway

·
5c0a40f6


Steven Arzt authored Mar 11, 2013






5c0a40f6










Replaced the duplicate call to the icfg by an access to cached structure we have anyway

·
5c0a40f6


Steven Arzt authored Mar 11, 2013






5c0a40f6






Replaced the duplicate call to the icfg by an access to cached structure we have anyway

·
5c0a40f6


Steven Arzt authored Mar 11, 2013


·
5c0a40f6

Steven Arzt authored Mar 11, 2013




5c0a40f6






5c0a40f6




5c0a40f6

28 Feb, 2013
1 commit
28 Feb, 20131 commit







removed stupig bug found by findbugs !!!

·
72286798


Eric Bodden authored Feb 28, 2013






72286798














removed stupig bug found by findbugs !!!

·
72286798


Eric Bodden authored Feb 28, 2013






72286798










removed stupig bug found by findbugs !!!

·
72286798


Eric Bodden authored Feb 28, 2013






72286798






removed stupig bug found by findbugs !!!

·
72286798


Eric Bodden authored Feb 28, 2013


·
72286798

Eric Bodden authored Feb 28, 2013




72286798






72286798




72286798

20 Feb, 2013
1 commit
20 Feb, 20131 commit







bugfix: were propagating incorrect values at return-flow edges

·
0645e494


Eric Bodden authored Feb 20, 2013






0645e494














bugfix: were propagating incorrect values at return-flow edges

·
0645e494


Eric Bodden authored Feb 20, 2013






0645e494










bugfix: were propagating incorrect values at return-flow edges

·
0645e494


Eric Bodden authored Feb 20, 2013






0645e494






bugfix: were propagating incorrect values at return-flow edges

·
0645e494


Eric Bodden authored Feb 20, 2013


·
0645e494

Eric Bodden authored Feb 20, 2013




0645e494






0645e494




0645e494

14 Feb, 2013
1 commit
14 Feb, 20131 commit







bug fix for value computation (need to treat initialSeeds just as method start nodes)

·
227b9337


Eric Bodden authored Feb 14, 2013






227b9337














bug fix for value computation (need to treat initialSeeds just as method start nodes)

·
227b9337


Eric Bodden authored Feb 14, 2013






227b9337










bug fix for value computation (need to treat initialSeeds just as method start nodes)

·
227b9337


Eric Bodden authored Feb 14, 2013






227b9337






bug fix for value computation (need to treat initialSeeds just as method start nodes)

·
227b9337


Eric Bodden authored Feb 14, 2013


·
227b9337

Eric Bodden authored Feb 14, 2013




227b9337






227b9337




227b9337

30 Jan, 2013
3 commits
30 Jan, 20133 commits







extracted method awaitCompletionComputeValuesAndShutdown()

·
446d709e


Eric Bodden authored Jan 30, 2013






446d709e













made scheduleEdgeProcessing protected such that it can be called from the outside

·
abe1050c


Eric Bodden authored Jan 30, 2013






abe1050c













Revert "removed superfluous call to scheduleEdgeProcessing"

·
2c7e04fd




Eric Bodden authored Jan 30, 2013


This reverts commit 1747df0c.

The change was wrong; scheduleEdgeProcessing does need to be called explicitly at initialization time!





2c7e04fd














extracted method awaitCompletionComputeValuesAndShutdown()

·
446d709e


Eric Bodden authored Jan 30, 2013






446d709e










extracted method awaitCompletionComputeValuesAndShutdown()

·
446d709e


Eric Bodden authored Jan 30, 2013






446d709e






extracted method awaitCompletionComputeValuesAndShutdown()

·
446d709e


Eric Bodden authored Jan 30, 2013


·
446d709e

Eric Bodden authored Jan 30, 2013




446d709e






446d709e




446d709e






made scheduleEdgeProcessing protected such that it can be called from the outside

·
abe1050c


Eric Bodden authored Jan 30, 2013






abe1050c










made scheduleEdgeProcessing protected such that it can be called from the outside

·
abe1050c


Eric Bodden authored Jan 30, 2013






abe1050c






made scheduleEdgeProcessing protected such that it can be called from the outside

·
abe1050c


Eric Bodden authored Jan 30, 2013


·
abe1050c

Eric Bodden authored Jan 30, 2013




abe1050c






abe1050c




abe1050c






Revert "removed superfluous call to scheduleEdgeProcessing"

·
2c7e04fd




Eric Bodden authored Jan 30, 2013


This reverts commit 1747df0c.

The change was wrong; scheduleEdgeProcessing does need to be called explicitly at initialization time!





2c7e04fd










Revert "removed superfluous call to scheduleEdgeProcessing"

·
2c7e04fd




Eric Bodden authored Jan 30, 2013


This reverts commit 1747df0c.

The change was wrong; scheduleEdgeProcessing does need to be called explicitly at initialization time!





2c7e04fd






Revert "removed superfluous call to scheduleEdgeProcessing"

·
2c7e04fd




Eric Bodden authored Jan 30, 2013


This reverts commit 1747df0c.

The change was wrong; scheduleEdgeProcessing does need to be called explicitly at initialization time!

·
2c7e04fd

Eric Bodden authored Jan 30, 2013




2c7e04fd






2c7e04fd




2c7e04fd

29 Jan, 2013
6 commits
29 Jan, 20136 commits







fixing shutdown sequence

·
e0e51e68


Eric Bodden authored Jan 29, 2013






e0e51e68













making executor exchangeable

·
18c72755


Eric Bodden authored Jan 29, 2013






18c72755













make computation of values optional

·
357e129e


Eric Bodden authored Jan 29, 2013






357e129e













number of threads is now configured through SolverConfiguration, a new super...

·
1e7167fe




Eric Bodden authored Jan 29, 2013

number of threads is now configured through SolverConfiguration, a new super interface of IFDSTabulationProblem





1e7167fe













indentation

·
aa7b4340


Eric Bodden authored Jan 29, 2013






aa7b4340













removed superfluous call to scheduleEdgeProcessing

·
1747df0c


Eric Bodden authored Jan 29, 2013






1747df0c














fixing shutdown sequence

·
e0e51e68


Eric Bodden authored Jan 29, 2013






e0e51e68










fixing shutdown sequence

·
e0e51e68


Eric Bodden authored Jan 29, 2013






e0e51e68






fixing shutdown sequence

·
e0e51e68


Eric Bodden authored Jan 29, 2013


·
e0e51e68

Eric Bodden authored Jan 29, 2013




e0e51e68






e0e51e68




e0e51e68






making executor exchangeable

·
18c72755


Eric Bodden authored Jan 29, 2013






18c72755










making executor exchangeable

·
18c72755


Eric Bodden authored Jan 29, 2013






18c72755






making executor exchangeable

·
18c72755


Eric Bodden authored Jan 29, 2013


·
18c72755

Eric Bodden authored Jan 29, 2013




18c72755






18c72755




18c72755






make computation of values optional

·
357e129e


Eric Bodden authored Jan 29, 2013






357e129e










make computation of values optional

·
357e129e


Eric Bodden authored Jan 29, 2013






357e129e






make computation of values optional

·
357e129e


Eric Bodden authored Jan 29, 2013


·
357e129e

Eric Bodden authored Jan 29, 2013




357e129e






357e129e




357e129e






number of threads is now configured through SolverConfiguration, a new super...

·
1e7167fe




Eric Bodden authored Jan 29, 2013

number of threads is now configured through SolverConfiguration, a new super interface of IFDSTabulationProblem





1e7167fe










number of threads is now configured through SolverConfiguration, a new super...

·
1e7167fe




Eric Bodden authored Jan 29, 2013

number of threads is now configured through SolverConfiguration, a new super interface of IFDSTabulationProblem





1e7167fe






number of threads is now configured through SolverConfiguration, a new super...

·
1e7167fe




Eric Bodden authored Jan 29, 2013

number of threads is now configured through SolverConfiguration, a new super interface of IFDSTabulationProblem

·
1e7167fe

Eric Bodden authored Jan 29, 2013




1e7167fe






1e7167fe




1e7167fe






indentation

·
aa7b4340


Eric Bodden authored Jan 29, 2013






aa7b4340










indentation

·
aa7b4340


Eric Bodden authored Jan 29, 2013






aa7b4340






indentation

·
aa7b4340


Eric Bodden authored Jan 29, 2013


·
aa7b4340

Eric Bodden authored Jan 29, 2013




aa7b4340






aa7b4340




aa7b4340






removed superfluous call to scheduleEdgeProcessing

·
1747df0c


Eric Bodden authored Jan 29, 2013






1747df0c










removed superfluous call to scheduleEdgeProcessing

·
1747df0c


Eric Bodden authored Jan 29, 2013






1747df0c






removed superfluous call to scheduleEdgeProcessing

·
1747df0c


Eric Bodden authored Jan 29, 2013


·
1747df0c

Eric Bodden authored Jan 29, 2013




1747df0c






1747df0c




1747df0c

28 Jan, 2013
3 commits
28 Jan, 20133 commits







refactoring: autoAddZero is now set in IFDSTabulationProblem

·
4b103811


Eric Bodden authored Jan 28, 2013






4b103811













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














refactoring: autoAddZero is now set in IFDSTabulationProblem

·
4b103811


Eric Bodden authored Jan 28, 2013






4b103811










refactoring: autoAddZero is now set in IFDSTabulationProblem

·
4b103811


Eric Bodden authored Jan 28, 2013






4b103811






refactoring: autoAddZero is now set in IFDSTabulationProblem

·
4b103811


Eric Bodden authored Jan 28, 2013


·
4b103811

Eric Bodden authored Jan 28, 2013




4b103811






4b103811




4b103811






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






