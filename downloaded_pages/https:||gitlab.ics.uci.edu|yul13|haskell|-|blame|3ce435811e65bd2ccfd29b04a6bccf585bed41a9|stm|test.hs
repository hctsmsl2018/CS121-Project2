



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

3ce435811e65bd2ccfd29b04a6bccf585bed41a9

















3ce435811e65bd2ccfd29b04a6bccf585bed41a9


Switch branch/tag










haskell


stm


test.hs



Find file
Normal viewHistoryPermalink






test.hs



498 Bytes









Newer










Older









stm test stub



 


darlliu
committed
Jul 03, 2013






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




import Control.Monad
import Control.Concurrent
import Control.Concurrent.STM

arrayRed::Int-> Int -> STM ()
arrayRed t th = do
    --allocate t arrays
    ; arr <- atomically $ newTVar (take t [1 ..])
    --parallel divide of 1 over
    ; forkIO $ th `timesDo` (appV (\x-> 1/x ) arr)
    milliSleep 800
    ; after <- atomically . readTVar arr
    --reduction(atomic)
  where timesDo = replicateM_
        milliSleep = theadDelay . (*) 1000
appV fn x = atomically $ readTVar x >>= writeTVar x . fn












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

3ce435811e65bd2ccfd29b04a6bccf585bed41a9

















3ce435811e65bd2ccfd29b04a6bccf585bed41a9


Switch branch/tag










haskell


stm


test.hs



Find file
Normal viewHistoryPermalink






test.hs



498 Bytes









Newer










Older









stm test stub



 


darlliu
committed
Jul 03, 2013






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




import Control.Monad
import Control.Concurrent
import Control.Concurrent.STM

arrayRed::Int-> Int -> STM ()
arrayRed t th = do
    --allocate t arrays
    ; arr <- atomically $ newTVar (take t [1 ..])
    --parallel divide of 1 over
    ; forkIO $ th `timesDo` (appV (\x-> 1/x ) arr)
    milliSleep 800
    ; after <- atomically . readTVar arr
    --reduction(atomic)
  where timesDo = replicateM_
        milliSleep = theadDelay . (*) 1000
appV fn x = atomically $ readTVar x >>= writeTVar x . fn











Open sidebar



Yu Liu haskell

3ce435811e65bd2ccfd29b04a6bccf585bed41a9







Open sidebar



Yu Liu haskell

3ce435811e65bd2ccfd29b04a6bccf585bed41a9




Open sidebar

Yu Liu haskell

3ce435811e65bd2ccfd29b04a6bccf585bed41a9


Yu Liuhaskellhaskell
3ce435811e65bd2ccfd29b04a6bccf585bed41a9










3ce435811e65bd2ccfd29b04a6bccf585bed41a9


Switch branch/tag










haskell


stm


test.hs



Find file
Normal viewHistoryPermalink






test.hs



498 Bytes









Newer










Older









stm test stub



 


darlliu
committed
Jul 03, 2013






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




import Control.Monad
import Control.Concurrent
import Control.Concurrent.STM

arrayRed::Int-> Int -> STM ()
arrayRed t th = do
    --allocate t arrays
    ; arr <- atomically $ newTVar (take t [1 ..])
    --parallel divide of 1 over
    ; forkIO $ th `timesDo` (appV (\x-> 1/x ) arr)
    milliSleep 800
    ; after <- atomically . readTVar arr
    --reduction(atomic)
  where timesDo = replicateM_
        milliSleep = theadDelay . (*) 1000
appV fn x = atomically $ readTVar x >>= writeTVar x . fn














3ce435811e65bd2ccfd29b04a6bccf585bed41a9


Switch branch/tag










haskell


stm


test.hs



Find file
Normal viewHistoryPermalink






test.hs



498 Bytes









Newer










Older









stm test stub



 


darlliu
committed
Jul 03, 2013






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




import Control.Monad
import Control.Concurrent
import Control.Concurrent.STM

arrayRed::Int-> Int -> STM ()
arrayRed t th = do
    --allocate t arrays
    ; arr <- atomically $ newTVar (take t [1 ..])
    --parallel divide of 1 over
    ; forkIO $ th `timesDo` (appV (\x-> 1/x ) arr)
    milliSleep 800
    ; after <- atomically . readTVar arr
    --reduction(atomic)
  where timesDo = replicateM_
        milliSleep = theadDelay . (*) 1000
appV fn x = atomically $ readTVar x >>= writeTVar x . fn










3ce435811e65bd2ccfd29b04a6bccf585bed41a9


Switch branch/tag










haskell


stm


test.hs



Find file
Normal viewHistoryPermalink




3ce435811e65bd2ccfd29b04a6bccf585bed41a9


Switch branch/tag










haskell


stm


test.hs





3ce435811e65bd2ccfd29b04a6bccf585bed41a9


Switch branch/tag








3ce435811e65bd2ccfd29b04a6bccf585bed41a9


Switch branch/tag





3ce435811e65bd2ccfd29b04a6bccf585bed41a9

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
haskell

stm

test.hs
Find file
Normal viewHistoryPermalink




test.hs



498 Bytes









Newer










Older









stm test stub



 


darlliu
committed
Jul 03, 2013






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




import Control.Monad
import Control.Concurrent
import Control.Concurrent.STM

arrayRed::Int-> Int -> STM ()
arrayRed t th = do
    --allocate t arrays
    ; arr <- atomically $ newTVar (take t [1 ..])
    --parallel divide of 1 over
    ; forkIO $ th `timesDo` (appV (\x-> 1/x ) arr)
    milliSleep 800
    ; after <- atomically . readTVar arr
    --reduction(atomic)
  where timesDo = replicateM_
        milliSleep = theadDelay . (*) 1000
appV fn x = atomically $ readTVar x >>= writeTVar x . fn








test.hs



498 Bytes










test.hs



498 Bytes









Newer










Older
NewerOlder







stm test stub



 


darlliu
committed
Jul 03, 2013






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




import Control.Monad
import Control.Concurrent
import Control.Concurrent.STM

arrayRed::Int-> Int -> STM ()
arrayRed t th = do
    --allocate t arrays
    ; arr <- atomically $ newTVar (take t [1 ..])
    --parallel divide of 1 over
    ; forkIO $ th `timesDo` (appV (\x-> 1/x ) arr)
    milliSleep 800
    ; after <- atomically . readTVar arr
    --reduction(atomic)
  where timesDo = replicateM_
        milliSleep = theadDelay . (*) 1000
appV fn x = atomically $ readTVar x >>= writeTVar x . fn







stm test stub



 


darlliu
committed
Jul 03, 2013



stm test stub



 

stm test stub


darlliu
committed
Jul 03, 2013

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
import Control.MonadimportControl.Monadimport Control.ConcurrentimportControl.Concurrentimport Control.Concurrent.STMimportControl.Concurrent.STMarrayRed::Int-> Int -> STM ()arrayRed::Int->Int->STM()arrayRed t th = doarrayRedtth=do    --allocate t arrays--allocate t arrays    ; arr <- atomically $ newTVar (take t [1 ..]);arr<-atomically$newTVar(taket[1..])    --parallel divide of 1 over--parallel divide of 1 over    ; forkIO $ th `timesDo` (appV (\x-> 1/x ) arr);forkIO$th`timesDo`(appV(\x->1/x)arr)    milliSleep 800milliSleep800    ; after <- atomically . readTVar arr;after<-atomically.readTVararr    --reduction(atomic)--reduction(atomic)  where timesDo = replicateM_wheretimesDo=replicateM_        milliSleep = theadDelay . (*) 1000milliSleep=theadDelay.(*)1000appV fn x = atomically $ readTVar x >>= writeTVar x . fnappVfnx=atomically$readTVarx>>=writeTVarx.fn





