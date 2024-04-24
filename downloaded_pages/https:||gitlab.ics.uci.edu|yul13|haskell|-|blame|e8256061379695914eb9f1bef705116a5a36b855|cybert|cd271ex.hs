



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

e8256061379695914eb9f1bef705116a5a36b855

















e8256061379695914eb9f1bef705116a5a36b855


Switch branch/tag










haskell


cybert


cd271ex.hs



Find file
Normal viewHistoryPermalink






cd271ex.hs



1.69 KB









Newer










Older









tests



 


darlliu
committed
May 25, 2013






1




-- reproduce the analysis for CD271 pvals, functional stype









wot now


 

 


darlliu
committed
May 25, 2013






2




3




4




5




--
import Cybert
import Control.Monad
import Data.Maybe









can now reproduce results, phew


 

 


darlliu
committed
May 25, 2013






6




import Data.Set (fromList, toList, union, empty)









wot now


 

 


darlliu
committed
May 25, 2013






7




8




9




extract:: Maybe [a] -> [a]
extract Nothing = []
extract (Just x) = x









can now reproduce results, phew


 

 


darlliu
committed
May 25, 2013






10




11




12




extractS:: Maybe String -> String
extractS Nothing = ""
extractS (Just x) = x









hmm


 

 


darlliu
committed
May 25, 2013






13




14




15




16




pfind x = map (filter (\y -> probe y == probe x))
    -- find entries with same probeid
countln = foldr (\z acc -> acc + (length z)) 0
    -- count cross lists num









wot now


 

 


darlliu
committed
May 25, 2013






17




18




19




20




main = do
    all_combined <- loadCybert "./CyberT_Output/unpaired/CD271_all_together0.txt"
    let all_up = (entriesByPval 0.05) $ (entriesByUpDown True)  (extract all_combined)
        all_down = (entriesByPval 0.05) $ (entriesByUpDown False)  (extract all_combined)









hmm


 

 


darlliu
committed
May 25, 2013






21




22




    ; exportCybert all_up "all_up_refs.txt"
    ; exportCybert all_down "all_down_refs.txt"









wot now


 

 


darlliu
committed
May 25, 2013






23




24




25




26




27




28




    ; onesM <- mapM loadCybert $ map ("./CyberT_Output/unpaired/CD271_one_one" ++ ) ["0.txt","1.txt","2.txt","3.txt"]
    ; pairsM <- mapM loadCybert ["./CyberT_Output/unpaired/CD271_two_two_" ++i ++ j ++ ".txt" | i <- ["1","2","3"], j<-["0","1"] ]
    let ones = map extract (all_combined:onesM)
        pairs = map extract (all_combined:pairsM)
    let up = map  (entriesByPval 0.05)  (map (entriesByUpDown True) pairs)
        down = map  (entriesByPval 0.05)  (map (entriesByUpDown False) pairs)









hmm


 

 


darlliu
committed
May 25, 2013






29




30




        five_up = map (filter (\x ->  countln (pfind x up) >= 5 )) up
        five_down =map (filter (\x -> countln (pfind x down) >= 5 )) down









can now reproduce results, phew


 

 


darlliu
committed
May 25, 2013






31




32




        five_ups = foldl union empty (map cybertToSet five_up)
        five_downs = foldl union empty (map cybertToSet five_down)









hmm


 

 


darlliu
committed
May 25, 2013






33




34




    ;  exportCybert (toList five_ups) "five_up_refs.txt" 
    ;  exportCybert (toList five_downs) "five_down_refs.txt" 












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

e8256061379695914eb9f1bef705116a5a36b855

















e8256061379695914eb9f1bef705116a5a36b855


Switch branch/tag










haskell


cybert


cd271ex.hs



Find file
Normal viewHistoryPermalink






cd271ex.hs



1.69 KB









Newer










Older









tests



 


darlliu
committed
May 25, 2013






1




-- reproduce the analysis for CD271 pvals, functional stype









wot now


 

 


darlliu
committed
May 25, 2013






2




3




4




5




--
import Cybert
import Control.Monad
import Data.Maybe









can now reproduce results, phew


 

 


darlliu
committed
May 25, 2013






6




import Data.Set (fromList, toList, union, empty)









wot now


 

 


darlliu
committed
May 25, 2013






7




8




9




extract:: Maybe [a] -> [a]
extract Nothing = []
extract (Just x) = x









can now reproduce results, phew


 

 


darlliu
committed
May 25, 2013






10




11




12




extractS:: Maybe String -> String
extractS Nothing = ""
extractS (Just x) = x









hmm


 

 


darlliu
committed
May 25, 2013






13




14




15




16




pfind x = map (filter (\y -> probe y == probe x))
    -- find entries with same probeid
countln = foldr (\z acc -> acc + (length z)) 0
    -- count cross lists num









wot now


 

 


darlliu
committed
May 25, 2013






17




18




19




20




main = do
    all_combined <- loadCybert "./CyberT_Output/unpaired/CD271_all_together0.txt"
    let all_up = (entriesByPval 0.05) $ (entriesByUpDown True)  (extract all_combined)
        all_down = (entriesByPval 0.05) $ (entriesByUpDown False)  (extract all_combined)









hmm


 

 


darlliu
committed
May 25, 2013






21




22




    ; exportCybert all_up "all_up_refs.txt"
    ; exportCybert all_down "all_down_refs.txt"









wot now


 

 


darlliu
committed
May 25, 2013






23




24




25




26




27




28




    ; onesM <- mapM loadCybert $ map ("./CyberT_Output/unpaired/CD271_one_one" ++ ) ["0.txt","1.txt","2.txt","3.txt"]
    ; pairsM <- mapM loadCybert ["./CyberT_Output/unpaired/CD271_two_two_" ++i ++ j ++ ".txt" | i <- ["1","2","3"], j<-["0","1"] ]
    let ones = map extract (all_combined:onesM)
        pairs = map extract (all_combined:pairsM)
    let up = map  (entriesByPval 0.05)  (map (entriesByUpDown True) pairs)
        down = map  (entriesByPval 0.05)  (map (entriesByUpDown False) pairs)









hmm


 

 


darlliu
committed
May 25, 2013






29




30




        five_up = map (filter (\x ->  countln (pfind x up) >= 5 )) up
        five_down =map (filter (\x -> countln (pfind x down) >= 5 )) down









can now reproduce results, phew


 

 


darlliu
committed
May 25, 2013






31




32




        five_ups = foldl union empty (map cybertToSet five_up)
        five_downs = foldl union empty (map cybertToSet five_down)









hmm


 

 


darlliu
committed
May 25, 2013






33




34




    ;  exportCybert (toList five_ups) "five_up_refs.txt" 
    ;  exportCybert (toList five_downs) "five_down_refs.txt" 











Open sidebar



Yu Liu haskell

e8256061379695914eb9f1bef705116a5a36b855







Open sidebar



Yu Liu haskell

e8256061379695914eb9f1bef705116a5a36b855




Open sidebar

Yu Liu haskell

e8256061379695914eb9f1bef705116a5a36b855


Yu Liuhaskellhaskell
e8256061379695914eb9f1bef705116a5a36b855










e8256061379695914eb9f1bef705116a5a36b855


Switch branch/tag










haskell


cybert


cd271ex.hs



Find file
Normal viewHistoryPermalink






cd271ex.hs



1.69 KB









Newer










Older









tests



 


darlliu
committed
May 25, 2013






1




-- reproduce the analysis for CD271 pvals, functional stype









wot now


 

 


darlliu
committed
May 25, 2013






2




3




4




5




--
import Cybert
import Control.Monad
import Data.Maybe









can now reproduce results, phew


 

 


darlliu
committed
May 25, 2013






6




import Data.Set (fromList, toList, union, empty)









wot now


 

 


darlliu
committed
May 25, 2013






7




8




9




extract:: Maybe [a] -> [a]
extract Nothing = []
extract (Just x) = x









can now reproduce results, phew


 

 


darlliu
committed
May 25, 2013






10




11




12




extractS:: Maybe String -> String
extractS Nothing = ""
extractS (Just x) = x









hmm


 

 


darlliu
committed
May 25, 2013






13




14




15




16




pfind x = map (filter (\y -> probe y == probe x))
    -- find entries with same probeid
countln = foldr (\z acc -> acc + (length z)) 0
    -- count cross lists num









wot now


 

 


darlliu
committed
May 25, 2013






17




18




19




20




main = do
    all_combined <- loadCybert "./CyberT_Output/unpaired/CD271_all_together0.txt"
    let all_up = (entriesByPval 0.05) $ (entriesByUpDown True)  (extract all_combined)
        all_down = (entriesByPval 0.05) $ (entriesByUpDown False)  (extract all_combined)









hmm


 

 


darlliu
committed
May 25, 2013






21




22




    ; exportCybert all_up "all_up_refs.txt"
    ; exportCybert all_down "all_down_refs.txt"









wot now


 

 


darlliu
committed
May 25, 2013






23




24




25




26




27




28




    ; onesM <- mapM loadCybert $ map ("./CyberT_Output/unpaired/CD271_one_one" ++ ) ["0.txt","1.txt","2.txt","3.txt"]
    ; pairsM <- mapM loadCybert ["./CyberT_Output/unpaired/CD271_two_two_" ++i ++ j ++ ".txt" | i <- ["1","2","3"], j<-["0","1"] ]
    let ones = map extract (all_combined:onesM)
        pairs = map extract (all_combined:pairsM)
    let up = map  (entriesByPval 0.05)  (map (entriesByUpDown True) pairs)
        down = map  (entriesByPval 0.05)  (map (entriesByUpDown False) pairs)









hmm


 

 


darlliu
committed
May 25, 2013






29




30




        five_up = map (filter (\x ->  countln (pfind x up) >= 5 )) up
        five_down =map (filter (\x -> countln (pfind x down) >= 5 )) down









can now reproduce results, phew


 

 


darlliu
committed
May 25, 2013






31




32




        five_ups = foldl union empty (map cybertToSet five_up)
        five_downs = foldl union empty (map cybertToSet five_down)









hmm


 

 


darlliu
committed
May 25, 2013






33




34




    ;  exportCybert (toList five_ups) "five_up_refs.txt" 
    ;  exportCybert (toList five_downs) "five_down_refs.txt" 














e8256061379695914eb9f1bef705116a5a36b855


Switch branch/tag










haskell


cybert


cd271ex.hs



Find file
Normal viewHistoryPermalink






cd271ex.hs



1.69 KB









Newer










Older









tests



 


darlliu
committed
May 25, 2013






1




-- reproduce the analysis for CD271 pvals, functional stype









wot now


 

 


darlliu
committed
May 25, 2013






2




3




4




5




--
import Cybert
import Control.Monad
import Data.Maybe









can now reproduce results, phew


 

 


darlliu
committed
May 25, 2013






6




import Data.Set (fromList, toList, union, empty)









wot now


 

 


darlliu
committed
May 25, 2013






7




8




9




extract:: Maybe [a] -> [a]
extract Nothing = []
extract (Just x) = x









can now reproduce results, phew


 

 


darlliu
committed
May 25, 2013






10




11




12




extractS:: Maybe String -> String
extractS Nothing = ""
extractS (Just x) = x









hmm


 

 


darlliu
committed
May 25, 2013






13




14




15




16




pfind x = map (filter (\y -> probe y == probe x))
    -- find entries with same probeid
countln = foldr (\z acc -> acc + (length z)) 0
    -- count cross lists num









wot now


 

 


darlliu
committed
May 25, 2013






17




18




19




20




main = do
    all_combined <- loadCybert "./CyberT_Output/unpaired/CD271_all_together0.txt"
    let all_up = (entriesByPval 0.05) $ (entriesByUpDown True)  (extract all_combined)
        all_down = (entriesByPval 0.05) $ (entriesByUpDown False)  (extract all_combined)









hmm


 

 


darlliu
committed
May 25, 2013






21




22




    ; exportCybert all_up "all_up_refs.txt"
    ; exportCybert all_down "all_down_refs.txt"









wot now


 

 


darlliu
committed
May 25, 2013






23




24




25




26




27




28




    ; onesM <- mapM loadCybert $ map ("./CyberT_Output/unpaired/CD271_one_one" ++ ) ["0.txt","1.txt","2.txt","3.txt"]
    ; pairsM <- mapM loadCybert ["./CyberT_Output/unpaired/CD271_two_two_" ++i ++ j ++ ".txt" | i <- ["1","2","3"], j<-["0","1"] ]
    let ones = map extract (all_combined:onesM)
        pairs = map extract (all_combined:pairsM)
    let up = map  (entriesByPval 0.05)  (map (entriesByUpDown True) pairs)
        down = map  (entriesByPval 0.05)  (map (entriesByUpDown False) pairs)









hmm


 

 


darlliu
committed
May 25, 2013






29




30




        five_up = map (filter (\x ->  countln (pfind x up) >= 5 )) up
        five_down =map (filter (\x -> countln (pfind x down) >= 5 )) down









can now reproduce results, phew


 

 


darlliu
committed
May 25, 2013






31




32




        five_ups = foldl union empty (map cybertToSet five_up)
        five_downs = foldl union empty (map cybertToSet five_down)









hmm


 

 


darlliu
committed
May 25, 2013






33




34




    ;  exportCybert (toList five_ups) "five_up_refs.txt" 
    ;  exportCybert (toList five_downs) "five_down_refs.txt" 










e8256061379695914eb9f1bef705116a5a36b855


Switch branch/tag










haskell


cybert


cd271ex.hs



Find file
Normal viewHistoryPermalink




e8256061379695914eb9f1bef705116a5a36b855


Switch branch/tag










haskell


cybert


cd271ex.hs





e8256061379695914eb9f1bef705116a5a36b855


Switch branch/tag








e8256061379695914eb9f1bef705116a5a36b855


Switch branch/tag





e8256061379695914eb9f1bef705116a5a36b855

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
haskell

cybert

cd271ex.hs
Find file
Normal viewHistoryPermalink




cd271ex.hs



1.69 KB









Newer










Older









tests



 


darlliu
committed
May 25, 2013






1




-- reproduce the analysis for CD271 pvals, functional stype









wot now


 

 


darlliu
committed
May 25, 2013






2




3




4




5




--
import Cybert
import Control.Monad
import Data.Maybe









can now reproduce results, phew


 

 


darlliu
committed
May 25, 2013






6




import Data.Set (fromList, toList, union, empty)









wot now


 

 


darlliu
committed
May 25, 2013






7




8




9




extract:: Maybe [a] -> [a]
extract Nothing = []
extract (Just x) = x









can now reproduce results, phew


 

 


darlliu
committed
May 25, 2013






10




11




12




extractS:: Maybe String -> String
extractS Nothing = ""
extractS (Just x) = x









hmm


 

 


darlliu
committed
May 25, 2013






13




14




15




16




pfind x = map (filter (\y -> probe y == probe x))
    -- find entries with same probeid
countln = foldr (\z acc -> acc + (length z)) 0
    -- count cross lists num









wot now


 

 


darlliu
committed
May 25, 2013






17




18




19




20




main = do
    all_combined <- loadCybert "./CyberT_Output/unpaired/CD271_all_together0.txt"
    let all_up = (entriesByPval 0.05) $ (entriesByUpDown True)  (extract all_combined)
        all_down = (entriesByPval 0.05) $ (entriesByUpDown False)  (extract all_combined)









hmm


 

 


darlliu
committed
May 25, 2013






21




22




    ; exportCybert all_up "all_up_refs.txt"
    ; exportCybert all_down "all_down_refs.txt"









wot now


 

 


darlliu
committed
May 25, 2013






23




24




25




26




27




28




    ; onesM <- mapM loadCybert $ map ("./CyberT_Output/unpaired/CD271_one_one" ++ ) ["0.txt","1.txt","2.txt","3.txt"]
    ; pairsM <- mapM loadCybert ["./CyberT_Output/unpaired/CD271_two_two_" ++i ++ j ++ ".txt" | i <- ["1","2","3"], j<-["0","1"] ]
    let ones = map extract (all_combined:onesM)
        pairs = map extract (all_combined:pairsM)
    let up = map  (entriesByPval 0.05)  (map (entriesByUpDown True) pairs)
        down = map  (entriesByPval 0.05)  (map (entriesByUpDown False) pairs)









hmm


 

 


darlliu
committed
May 25, 2013






29




30




        five_up = map (filter (\x ->  countln (pfind x up) >= 5 )) up
        five_down =map (filter (\x -> countln (pfind x down) >= 5 )) down









can now reproduce results, phew


 

 


darlliu
committed
May 25, 2013






31




32




        five_ups = foldl union empty (map cybertToSet five_up)
        five_downs = foldl union empty (map cybertToSet five_down)









hmm


 

 


darlliu
committed
May 25, 2013






33




34




    ;  exportCybert (toList five_ups) "five_up_refs.txt" 
    ;  exportCybert (toList five_downs) "five_down_refs.txt" 








cd271ex.hs



1.69 KB










cd271ex.hs



1.69 KB









Newer










Older
NewerOlder







tests



 


darlliu
committed
May 25, 2013






1




-- reproduce the analysis for CD271 pvals, functional stype









wot now


 

 


darlliu
committed
May 25, 2013






2




3




4




5




--
import Cybert
import Control.Monad
import Data.Maybe









can now reproduce results, phew


 

 


darlliu
committed
May 25, 2013






6




import Data.Set (fromList, toList, union, empty)









wot now


 

 


darlliu
committed
May 25, 2013






7




8




9




extract:: Maybe [a] -> [a]
extract Nothing = []
extract (Just x) = x









can now reproduce results, phew


 

 


darlliu
committed
May 25, 2013






10




11




12




extractS:: Maybe String -> String
extractS Nothing = ""
extractS (Just x) = x









hmm


 

 


darlliu
committed
May 25, 2013






13




14




15




16




pfind x = map (filter (\y -> probe y == probe x))
    -- find entries with same probeid
countln = foldr (\z acc -> acc + (length z)) 0
    -- count cross lists num









wot now


 

 


darlliu
committed
May 25, 2013






17




18




19




20




main = do
    all_combined <- loadCybert "./CyberT_Output/unpaired/CD271_all_together0.txt"
    let all_up = (entriesByPval 0.05) $ (entriesByUpDown True)  (extract all_combined)
        all_down = (entriesByPval 0.05) $ (entriesByUpDown False)  (extract all_combined)









hmm


 

 


darlliu
committed
May 25, 2013






21




22




    ; exportCybert all_up "all_up_refs.txt"
    ; exportCybert all_down "all_down_refs.txt"









wot now


 

 


darlliu
committed
May 25, 2013






23




24




25




26




27




28




    ; onesM <- mapM loadCybert $ map ("./CyberT_Output/unpaired/CD271_one_one" ++ ) ["0.txt","1.txt","2.txt","3.txt"]
    ; pairsM <- mapM loadCybert ["./CyberT_Output/unpaired/CD271_two_two_" ++i ++ j ++ ".txt" | i <- ["1","2","3"], j<-["0","1"] ]
    let ones = map extract (all_combined:onesM)
        pairs = map extract (all_combined:pairsM)
    let up = map  (entriesByPval 0.05)  (map (entriesByUpDown True) pairs)
        down = map  (entriesByPval 0.05)  (map (entriesByUpDown False) pairs)









hmm


 

 


darlliu
committed
May 25, 2013






29




30




        five_up = map (filter (\x ->  countln (pfind x up) >= 5 )) up
        five_down =map (filter (\x -> countln (pfind x down) >= 5 )) down









can now reproduce results, phew


 

 


darlliu
committed
May 25, 2013






31




32




        five_ups = foldl union empty (map cybertToSet five_up)
        five_downs = foldl union empty (map cybertToSet five_down)









hmm


 

 


darlliu
committed
May 25, 2013






33




34




    ;  exportCybert (toList five_ups) "five_up_refs.txt" 
    ;  exportCybert (toList five_downs) "five_down_refs.txt" 







tests



 


darlliu
committed
May 25, 2013



tests



 

tests


darlliu
committed
May 25, 2013

1
-- reproduce the analysis for CD271 pvals, functional stype-- reproduce the analysis for CD271 pvals, functional stype



wot now


 

 


darlliu
committed
May 25, 2013



wot now


 

 

wot now

 

darlliu
committed
May 25, 2013

2

3

4

5
----import CybertimportCybertimport Control.MonadimportControl.Monadimport Data.MaybeimportData.Maybe



can now reproduce results, phew


 

 


darlliu
committed
May 25, 2013



can now reproduce results, phew


 

 

can now reproduce results, phew

 

darlliu
committed
May 25, 2013

6
import Data.Set (fromList, toList, union, empty)importData.Set(fromList,toList,union,empty)



wot now


 

 


darlliu
committed
May 25, 2013



wot now


 

 

wot now

 

darlliu
committed
May 25, 2013

7

8

9
extract:: Maybe [a] -> [a]extract::Maybe[a]->[a]extract Nothing = []extractNothing=[]extract (Just x) = xextract(Justx)=x



can now reproduce results, phew


 

 


darlliu
committed
May 25, 2013



can now reproduce results, phew


 

 

can now reproduce results, phew

 

darlliu
committed
May 25, 2013

10

11

12
extractS:: Maybe String -> StringextractS::MaybeString->StringextractS Nothing = ""extractSNothing=""extractS (Just x) = xextractS(Justx)=x



hmm


 

 


darlliu
committed
May 25, 2013



hmm


 

 

hmm

 

darlliu
committed
May 25, 2013

13

14

15

16
pfind x = map (filter (\y -> probe y == probe x))pfindx=map(filter(\y->probey==probex))    -- find entries with same probeid-- find entries with same probeidcountln = foldr (\z acc -> acc + (length z)) 0countln=foldr(\zacc->acc+(lengthz))0    -- count cross lists num-- count cross lists num



wot now


 

 


darlliu
committed
May 25, 2013



wot now


 

 

wot now

 

darlliu
committed
May 25, 2013

17

18

19

20
main = domain=do    all_combined <- loadCybert "./CyberT_Output/unpaired/CD271_all_together0.txt"all_combined<-loadCybert"./CyberT_Output/unpaired/CD271_all_together0.txt"    let all_up = (entriesByPval 0.05) $ (entriesByUpDown True)  (extract all_combined)letall_up=(entriesByPval0.05)$(entriesByUpDownTrue)(extractall_combined)        all_down = (entriesByPval 0.05) $ (entriesByUpDown False)  (extract all_combined)all_down=(entriesByPval0.05)$(entriesByUpDownFalse)(extractall_combined)



hmm


 

 


darlliu
committed
May 25, 2013



hmm


 

 

hmm

 

darlliu
committed
May 25, 2013

21

22
    ; exportCybert all_up "all_up_refs.txt";exportCybertall_up"all_up_refs.txt"    ; exportCybert all_down "all_down_refs.txt";exportCybertall_down"all_down_refs.txt"



wot now


 

 


darlliu
committed
May 25, 2013



wot now


 

 

wot now

 

darlliu
committed
May 25, 2013

23

24

25

26

27

28
    ; onesM <- mapM loadCybert $ map ("./CyberT_Output/unpaired/CD271_one_one" ++ ) ["0.txt","1.txt","2.txt","3.txt"];onesM<-mapMloadCybert$map("./CyberT_Output/unpaired/CD271_one_one"++)["0.txt","1.txt","2.txt","3.txt"]    ; pairsM <- mapM loadCybert ["./CyberT_Output/unpaired/CD271_two_two_" ++i ++ j ++ ".txt" | i <- ["1","2","3"], j<-["0","1"] ];pairsM<-mapMloadCybert["./CyberT_Output/unpaired/CD271_two_two_"++i++j++".txt"|i<-["1","2","3"],j<-["0","1"]]    let ones = map extract (all_combined:onesM)letones=mapextract(all_combined:onesM)        pairs = map extract (all_combined:pairsM)pairs=mapextract(all_combined:pairsM)    let up = map  (entriesByPval 0.05)  (map (entriesByUpDown True) pairs)letup=map(entriesByPval0.05)(map(entriesByUpDownTrue)pairs)        down = map  (entriesByPval 0.05)  (map (entriesByUpDown False) pairs)down=map(entriesByPval0.05)(map(entriesByUpDownFalse)pairs)



hmm


 

 


darlliu
committed
May 25, 2013



hmm


 

 

hmm

 

darlliu
committed
May 25, 2013

29

30
        five_up = map (filter (\x ->  countln (pfind x up) >= 5 )) upfive_up=map(filter(\x->countln(pfindxup)>=5))up        five_down =map (filter (\x -> countln (pfind x down) >= 5 )) downfive_down=map(filter(\x->countln(pfindxdown)>=5))down



can now reproduce results, phew


 

 


darlliu
committed
May 25, 2013



can now reproduce results, phew


 

 

can now reproduce results, phew

 

darlliu
committed
May 25, 2013

31

32
        five_ups = foldl union empty (map cybertToSet five_up)five_ups=foldlunionempty(mapcybertToSetfive_up)        five_downs = foldl union empty (map cybertToSet five_down)five_downs=foldlunionempty(mapcybertToSetfive_down)



hmm


 

 


darlliu
committed
May 25, 2013



hmm


 

 

hmm

 

darlliu
committed
May 25, 2013

33

34
    ;  exportCybert (toList five_ups) "five_up_refs.txt" ;exportCybert(toListfive_ups)"five_up_refs.txt"    ;  exportCybert (toList five_downs) "five_down_refs.txt" ;exportCybert(toListfive_downs)"five_down_refs.txt"





