



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

c8207c2fbefda6fb415c1ad6d6ad01eaca5d8d1b

















c8207c2fbefda6fb415c1ad6d6ad01eaca5d8d1b


Switch branch/tag










haskell


stm


test.hs



Find file
Normal viewHistoryPermalink






test.hs



730 Bytes









Newer










Older









clean up


 

 


darlliu
committed
Feb 26, 2015






1




module Main where









stm test stub



 


darlliu
committed
Jul 03, 2013






2




3




4




import Control.Monad
import Control.Concurrent
import Control.Concurrent.STM









clean up


 

 


darlliu
committed
Feb 26, 2015






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




17




18




19




 
main = do shared <- atomically $ newTVar 0
          before <- atomRead shared
          putStrLn $ "Before: " ++ show before
          forkIO $ 25 `timesDo` (dispVar shared >> milliSleep 20)
          forkIO $ 10 `timesDo` (appV ((+) 2) shared >> milliSleep 50)
          forkIO $ 20 `timesDo` (appV pred shared >> milliSleep 25)
          milliSleep 800
          after <- atomRead shared
          putStrLn $ "After: " ++ show after
 where timesDo = replicateM_
       milliSleep = threadDelay . (*) 1000
 
atomRead = atomically . readTVar
dispVar x = atomRead x >>= print









stm test stub



 


darlliu
committed
Jul 03, 2013






20




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

c8207c2fbefda6fb415c1ad6d6ad01eaca5d8d1b

















c8207c2fbefda6fb415c1ad6d6ad01eaca5d8d1b


Switch branch/tag










haskell


stm


test.hs



Find file
Normal viewHistoryPermalink






test.hs



730 Bytes









Newer










Older









clean up


 

 


darlliu
committed
Feb 26, 2015






1




module Main where









stm test stub



 


darlliu
committed
Jul 03, 2013






2




3




4




import Control.Monad
import Control.Concurrent
import Control.Concurrent.STM









clean up


 

 


darlliu
committed
Feb 26, 2015






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




17




18




19




 
main = do shared <- atomically $ newTVar 0
          before <- atomRead shared
          putStrLn $ "Before: " ++ show before
          forkIO $ 25 `timesDo` (dispVar shared >> milliSleep 20)
          forkIO $ 10 `timesDo` (appV ((+) 2) shared >> milliSleep 50)
          forkIO $ 20 `timesDo` (appV pred shared >> milliSleep 25)
          milliSleep 800
          after <- atomRead shared
          putStrLn $ "After: " ++ show after
 where timesDo = replicateM_
       milliSleep = threadDelay . (*) 1000
 
atomRead = atomically . readTVar
dispVar x = atomRead x >>= print









stm test stub



 


darlliu
committed
Jul 03, 2013






20




appV fn x = atomically $ readTVar x >>= writeTVar x . fn











Open sidebar



Yu Liu haskell

c8207c2fbefda6fb415c1ad6d6ad01eaca5d8d1b







Open sidebar



Yu Liu haskell

c8207c2fbefda6fb415c1ad6d6ad01eaca5d8d1b




Open sidebar

Yu Liu haskell

c8207c2fbefda6fb415c1ad6d6ad01eaca5d8d1b


Yu Liuhaskellhaskell
c8207c2fbefda6fb415c1ad6d6ad01eaca5d8d1b










c8207c2fbefda6fb415c1ad6d6ad01eaca5d8d1b


Switch branch/tag










haskell


stm


test.hs



Find file
Normal viewHistoryPermalink






test.hs



730 Bytes









Newer










Older









clean up


 

 


darlliu
committed
Feb 26, 2015






1




module Main where









stm test stub



 


darlliu
committed
Jul 03, 2013






2




3




4




import Control.Monad
import Control.Concurrent
import Control.Concurrent.STM









clean up


 

 


darlliu
committed
Feb 26, 2015






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




17




18




19




 
main = do shared <- atomically $ newTVar 0
          before <- atomRead shared
          putStrLn $ "Before: " ++ show before
          forkIO $ 25 `timesDo` (dispVar shared >> milliSleep 20)
          forkIO $ 10 `timesDo` (appV ((+) 2) shared >> milliSleep 50)
          forkIO $ 20 `timesDo` (appV pred shared >> milliSleep 25)
          milliSleep 800
          after <- atomRead shared
          putStrLn $ "After: " ++ show after
 where timesDo = replicateM_
       milliSleep = threadDelay . (*) 1000
 
atomRead = atomically . readTVar
dispVar x = atomRead x >>= print









stm test stub



 


darlliu
committed
Jul 03, 2013






20




appV fn x = atomically $ readTVar x >>= writeTVar x . fn














c8207c2fbefda6fb415c1ad6d6ad01eaca5d8d1b


Switch branch/tag










haskell


stm


test.hs



Find file
Normal viewHistoryPermalink






test.hs



730 Bytes









Newer










Older









clean up


 

 


darlliu
committed
Feb 26, 2015






1




module Main where









stm test stub



 


darlliu
committed
Jul 03, 2013






2




3




4




import Control.Monad
import Control.Concurrent
import Control.Concurrent.STM









clean up


 

 


darlliu
committed
Feb 26, 2015






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




17




18




19




 
main = do shared <- atomically $ newTVar 0
          before <- atomRead shared
          putStrLn $ "Before: " ++ show before
          forkIO $ 25 `timesDo` (dispVar shared >> milliSleep 20)
          forkIO $ 10 `timesDo` (appV ((+) 2) shared >> milliSleep 50)
          forkIO $ 20 `timesDo` (appV pred shared >> milliSleep 25)
          milliSleep 800
          after <- atomRead shared
          putStrLn $ "After: " ++ show after
 where timesDo = replicateM_
       milliSleep = threadDelay . (*) 1000
 
atomRead = atomically . readTVar
dispVar x = atomRead x >>= print









stm test stub



 


darlliu
committed
Jul 03, 2013






20




appV fn x = atomically $ readTVar x >>= writeTVar x . fn










c8207c2fbefda6fb415c1ad6d6ad01eaca5d8d1b


Switch branch/tag










haskell


stm


test.hs



Find file
Normal viewHistoryPermalink




c8207c2fbefda6fb415c1ad6d6ad01eaca5d8d1b


Switch branch/tag










haskell


stm


test.hs





c8207c2fbefda6fb415c1ad6d6ad01eaca5d8d1b


Switch branch/tag








c8207c2fbefda6fb415c1ad6d6ad01eaca5d8d1b


Switch branch/tag





c8207c2fbefda6fb415c1ad6d6ad01eaca5d8d1b

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
haskell

stm

test.hs
Find file
Normal viewHistoryPermalink




test.hs



730 Bytes









Newer










Older









clean up


 

 


darlliu
committed
Feb 26, 2015






1




module Main where









stm test stub



 


darlliu
committed
Jul 03, 2013






2




3




4




import Control.Monad
import Control.Concurrent
import Control.Concurrent.STM









clean up


 

 


darlliu
committed
Feb 26, 2015






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




17




18




19




 
main = do shared <- atomically $ newTVar 0
          before <- atomRead shared
          putStrLn $ "Before: " ++ show before
          forkIO $ 25 `timesDo` (dispVar shared >> milliSleep 20)
          forkIO $ 10 `timesDo` (appV ((+) 2) shared >> milliSleep 50)
          forkIO $ 20 `timesDo` (appV pred shared >> milliSleep 25)
          milliSleep 800
          after <- atomRead shared
          putStrLn $ "After: " ++ show after
 where timesDo = replicateM_
       milliSleep = threadDelay . (*) 1000
 
atomRead = atomically . readTVar
dispVar x = atomRead x >>= print









stm test stub



 


darlliu
committed
Jul 03, 2013






20




appV fn x = atomically $ readTVar x >>= writeTVar x . fn








test.hs



730 Bytes










test.hs



730 Bytes









Newer










Older
NewerOlder







clean up


 

 


darlliu
committed
Feb 26, 2015






1




module Main where









stm test stub



 


darlliu
committed
Jul 03, 2013






2




3




4




import Control.Monad
import Control.Concurrent
import Control.Concurrent.STM









clean up


 

 


darlliu
committed
Feb 26, 2015






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




17




18




19




 
main = do shared <- atomically $ newTVar 0
          before <- atomRead shared
          putStrLn $ "Before: " ++ show before
          forkIO $ 25 `timesDo` (dispVar shared >> milliSleep 20)
          forkIO $ 10 `timesDo` (appV ((+) 2) shared >> milliSleep 50)
          forkIO $ 20 `timesDo` (appV pred shared >> milliSleep 25)
          milliSleep 800
          after <- atomRead shared
          putStrLn $ "After: " ++ show after
 where timesDo = replicateM_
       milliSleep = threadDelay . (*) 1000
 
atomRead = atomically . readTVar
dispVar x = atomRead x >>= print









stm test stub



 


darlliu
committed
Jul 03, 2013






20




appV fn x = atomically $ readTVar x >>= writeTVar x . fn







clean up


 

 


darlliu
committed
Feb 26, 2015



clean up


 

 

clean up

 

darlliu
committed
Feb 26, 2015

1
module Main wheremoduleMainwhere



stm test stub



 


darlliu
committed
Jul 03, 2013



stm test stub



 

stm test stub


darlliu
committed
Jul 03, 2013

2

3

4
import Control.MonadimportControl.Monadimport Control.ConcurrentimportControl.Concurrentimport Control.Concurrent.STMimportControl.Concurrent.STM



clean up


 

 


darlliu
committed
Feb 26, 2015



clean up


 

 

clean up

 

darlliu
committed
Feb 26, 2015

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

17

18

19
 main = do shared <- atomically $ newTVar 0main=doshared<-atomically$newTVar0          before <- atomRead sharedbefore<-atomReadshared          putStrLn $ "Before: " ++ show beforeputStrLn$"Before: "++showbefore          forkIO $ 25 `timesDo` (dispVar shared >> milliSleep 20)forkIO$25`timesDo`(dispVarshared>>milliSleep20)          forkIO $ 10 `timesDo` (appV ((+) 2) shared >> milliSleep 50)forkIO$10`timesDo`(appV((+)2)shared>>milliSleep50)          forkIO $ 20 `timesDo` (appV pred shared >> milliSleep 25)forkIO$20`timesDo`(appVpredshared>>milliSleep25)          milliSleep 800milliSleep800          after <- atomRead sharedafter<-atomReadshared          putStrLn $ "After: " ++ show afterputStrLn$"After: "++showafter where timesDo = replicateM_wheretimesDo=replicateM_       milliSleep = threadDelay . (*) 1000milliSleep=threadDelay.(*)1000 atomRead = atomically . readTVaratomRead=atomically.readTVardispVar x = atomRead x >>= printdispVarx=atomReadx>>=print



stm test stub



 


darlliu
committed
Jul 03, 2013



stm test stub



 

stm test stub


darlliu
committed
Jul 03, 2013

20
appV fn x = atomically $ readTVar x >>= writeTVar x . fnappVfnx=atomically$readTVarx>>=writeTVarx.fn





