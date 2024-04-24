



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


haskell






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



Yu Liu haskell

7df36b18bc611e658501785db9a22d6fb013d2dd

















7df36b18bc611e658501785db9a22d6fb013d2dd


Switch branch/tag










haskell


cybert


test_cybert.hs



Find file
Normal viewHistoryPermalink






test_cybert.hs



337 Bytes









Newer










Older









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






1




2




3




4




5




6




import Cybert

main = do
    let c = cybert_entry{genesym=(Just "Sym"), mean = (Right [1,2,3])}
    let d = cybert_entry{genesym=(Just "Sym2"), mean = (Right [1,2,3])}
    exportCybert [c,d] "tested.txt"









hmm


 

 


darlliu
committed
May 23, 2013






7




8




    exportGeneSyms [c,d] "syms.txt"
    exportProbes [c,d] "probes.txt"









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






9




10




    some <- loadCybert "tested.txt"
    putStrLn (show some)












H


haskell






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


haskell


H
H
haskell




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



Yu Liu haskell

7df36b18bc611e658501785db9a22d6fb013d2dd

















7df36b18bc611e658501785db9a22d6fb013d2dd


Switch branch/tag










haskell


cybert


test_cybert.hs



Find file
Normal viewHistoryPermalink






test_cybert.hs



337 Bytes









Newer










Older









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






1




2




3




4




5




6




import Cybert

main = do
    let c = cybert_entry{genesym=(Just "Sym"), mean = (Right [1,2,3])}
    let d = cybert_entry{genesym=(Just "Sym2"), mean = (Right [1,2,3])}
    exportCybert [c,d] "tested.txt"









hmm


 

 


darlliu
committed
May 23, 2013






7




8




    exportGeneSyms [c,d] "syms.txt"
    exportProbes [c,d] "probes.txt"









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






9




10




    some <- loadCybert "tested.txt"
    putStrLn (show some)











Open sidebar



Yu Liu haskell

7df36b18bc611e658501785db9a22d6fb013d2dd







Open sidebar



Yu Liu haskell

7df36b18bc611e658501785db9a22d6fb013d2dd




Open sidebar

Yu Liu haskell

7df36b18bc611e658501785db9a22d6fb013d2dd


Yu Liuhaskellhaskell
7df36b18bc611e658501785db9a22d6fb013d2dd










7df36b18bc611e658501785db9a22d6fb013d2dd


Switch branch/tag










haskell


cybert


test_cybert.hs



Find file
Normal viewHistoryPermalink






test_cybert.hs



337 Bytes









Newer










Older









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






1




2




3




4




5




6




import Cybert

main = do
    let c = cybert_entry{genesym=(Just "Sym"), mean = (Right [1,2,3])}
    let d = cybert_entry{genesym=(Just "Sym2"), mean = (Right [1,2,3])}
    exportCybert [c,d] "tested.txt"









hmm


 

 


darlliu
committed
May 23, 2013






7




8




    exportGeneSyms [c,d] "syms.txt"
    exportProbes [c,d] "probes.txt"









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






9




10




    some <- loadCybert "tested.txt"
    putStrLn (show some)














7df36b18bc611e658501785db9a22d6fb013d2dd


Switch branch/tag










haskell


cybert


test_cybert.hs



Find file
Normal viewHistoryPermalink






test_cybert.hs



337 Bytes









Newer










Older









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






1




2




3




4




5




6




import Cybert

main = do
    let c = cybert_entry{genesym=(Just "Sym"), mean = (Right [1,2,3])}
    let d = cybert_entry{genesym=(Just "Sym2"), mean = (Right [1,2,3])}
    exportCybert [c,d] "tested.txt"









hmm


 

 


darlliu
committed
May 23, 2013






7




8




    exportGeneSyms [c,d] "syms.txt"
    exportProbes [c,d] "probes.txt"









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






9




10




    some <- loadCybert "tested.txt"
    putStrLn (show some)










7df36b18bc611e658501785db9a22d6fb013d2dd


Switch branch/tag










haskell


cybert


test_cybert.hs



Find file
Normal viewHistoryPermalink




7df36b18bc611e658501785db9a22d6fb013d2dd


Switch branch/tag










haskell


cybert


test_cybert.hs





7df36b18bc611e658501785db9a22d6fb013d2dd


Switch branch/tag








7df36b18bc611e658501785db9a22d6fb013d2dd


Switch branch/tag





7df36b18bc611e658501785db9a22d6fb013d2dd

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
haskell

cybert

test_cybert.hs
Find file
Normal viewHistoryPermalink




test_cybert.hs



337 Bytes









Newer










Older









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






1




2




3




4




5




6




import Cybert

main = do
    let c = cybert_entry{genesym=(Just "Sym"), mean = (Right [1,2,3])}
    let d = cybert_entry{genesym=(Just "Sym2"), mean = (Right [1,2,3])}
    exportCybert [c,d] "tested.txt"









hmm


 

 


darlliu
committed
May 23, 2013






7




8




    exportGeneSyms [c,d] "syms.txt"
    exportProbes [c,d] "probes.txt"









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






9




10




    some <- loadCybert "tested.txt"
    putStrLn (show some)








test_cybert.hs



337 Bytes










test_cybert.hs



337 Bytes









Newer










Older
NewerOlder







cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






1




2




3




4




5




6




import Cybert

main = do
    let c = cybert_entry{genesym=(Just "Sym"), mean = (Right [1,2,3])}
    let d = cybert_entry{genesym=(Just "Sym2"), mean = (Right [1,2,3])}
    exportCybert [c,d] "tested.txt"









hmm


 

 


darlliu
committed
May 23, 2013






7




8




    exportGeneSyms [c,d] "syms.txt"
    exportProbes [c,d] "probes.txt"









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






9




10




    some <- loadCybert "tested.txt"
    putStrLn (show some)







cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013



cybert exercise, now need to load line only



 

cybert exercise, now need to load line only


darlliu
committed
May 23, 2013

1

2

3

4

5

6
import CybertimportCybertmain = domain=do    let c = cybert_entry{genesym=(Just "Sym"), mean = (Right [1,2,3])}letc=cybert_entry{genesym=(Just"Sym"),mean=(Right[1,2,3])}    let d = cybert_entry{genesym=(Just "Sym2"), mean = (Right [1,2,3])}letd=cybert_entry{genesym=(Just"Sym2"),mean=(Right[1,2,3])}    exportCybert [c,d] "tested.txt"exportCybert[c,d]"tested.txt"



hmm


 

 


darlliu
committed
May 23, 2013



hmm


 

 

hmm

 

darlliu
committed
May 23, 2013

7

8
    exportGeneSyms [c,d] "syms.txt"exportGeneSyms[c,d]"syms.txt"    exportProbes [c,d] "probes.txt"exportProbes[c,d]"probes.txt"



cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013



cybert exercise, now need to load line only



 

cybert exercise, now need to load line only


darlliu
committed
May 23, 2013

9

10
    some <- loadCybert "tested.txt"some<-loadCybert"tested.txt"    putStrLn (show some)putStrLn(showsome)





