



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

312ea5fb80ee00f190b74af0b7f9846a1a869a0d

















312ea5fb80ee00f190b74af0b7f9846a1a869a0d


Switch branch/tag










haskell


cybert-stack


app


huanganalysis.hs



Find file
Normal viewHistoryPermalink






huanganalysis.hs



4.53 KB









Newer










Older









stack implementaion of the cybert project



yul13
committed
Apr 18, 2016




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


17


18


19


20


21


22


23


24


25


26


27


28


29


30


31


32


33


34


35


36


37


38


39


40


41


42


43


44


45


46


47


48


49


50


51


52


53


54


55


56


57


58


59


60


61


62


63


64


65


66


67


68


69


70


71


72


73


74


75


76


77


78


79


80


81


82


83


84


85


86


87


88


89


90


91


92


93


94


95


96


97


98


99


100


101


102


103


104


105


106


107


108


109


110


111


112


113


114


115



import Cybert
import Data.Maybe
import Data.List
import qualified Data.Map as M
import Control.Monad
import qualified Control.Exception as E
import System.Directory
import System.IO
import qualified Control.Monad.Parallel as P
{-import Data.Set(fromList, toList, union, empty)-}

cheader' = M.fromList [("Secondary_Data_FDR","fdr"), ("Secondary_Data_FPR","fpr"),("Secondary_Data_Pval1","pval1")]
cheader  = M.union (cheader') (M.adjust (\x->"accession") "probe" cybert_header)
loadCybert' = loadCybert cheader
targetDirs= ["NatData_runs","XlinkData_runs"]

{-utility fctns for writing to file -}
stringify sep xs = foldl1 (\x y ->x++sep++ y) xs

writeString fname content = do
    withFile fname WriteMode (\handle -> do
        hPutStr handle content)

mvroot :: FilePath -> FilePath -> IO Bool
mvroot root cur = do
   E.catch
    (
        do
         createDirectoryIfMissing True (cur ++"/ontargets")
         createDirectoryIfMissing True (cur ++"/scatters")
         createDirectoryIfMissing True (cur ++"/avgs")
         setCurrentDirectory  (cur++"/runs")
         return True
    )
    (\e-> do
        let err = show (e::E.IOException)
        putStr $ stringify "\n" ["Error Chaging to directory\n" , cur,
           "Err:", err]
        return False
    );

{-extractTargets :: [Cybert_entry] -> IO()-}

exportScatter fname xs = do
    let ss = map (\x-> let d = secondaryData x
                        in stringify "\t" [probe x, show (d M.! "pval1"),show (d M.! "fdr")]) xs
    (writeString ("../scatters/"++ fname)) (stringify "\n" ss)

exportTargets fname xs = do
    exportProbes xs ("../ontargets/" ++ fname)
    let ss = map (\x-> let d = secondaryData x
                        in stringify "\t"
                            [probe x, showEitherF (mean x), show (d M.! "pval1"),
                             show (d M.! "fpr"), show (d M.! "fdr")]) xs
    writeString ("../targetdetails/"++ fname) $ 
        stringify "\n" (["accession\tmean\tpvalue(one-tail)\tFPR\tFDR"]++ss)
exportAVG fn xs = do
    let xxs = map fn xs
    let avg = if length xs >=2 then (foldl1 (+) xxs) / (fromIntegral (length xxs))
               else 1.0
    let gname = if length xs == 0 then "Unknown"
                else fromMaybe "Unknown" (collection $ (xs !! 0))
    let ss = gname ++ "\t" ++ (show avg) ++ "\n"
    return ss
subroutine:: (Maybe [Cybert_entry]) -> IO ([String])
subroutine xs = do
    let xxs'= fromMaybe [] xs
    let fname = if length xxs' == 0 then "Nill.dummy"
                else fromMaybe "Nill.dummy" (collection $ xxs' !! 0)
    ; exportScatter fname xxs'
    {-; exportAVG "../avgs/Total_AVG_mean.tsv" mean xxs'-}
    let xxs = filter (\x -> let d = (secondaryData x)
                              in if (d M.! "fdr") < 0.0 then (pval x) < 0.05
                             {-in case where beta fitting fails-}
                                 else (d M.! "pval1") <0.05 
                                 {-&& (d M.! "fdr") < 0.1-}
                     ) (entriesByUpDown True xxs')
    ; putStrLn  (fname ++" , "++(show $ length xxs))
    ; exportTargets fname xxs
    ; tot_p <- exportAVG pval  xxs'
    ; tot_f <- exportAVG (\x->let d = secondaryData x in d M.! "fdr") xxs'
    ; tar_p <- exportAVG pval xxs
    ; tar_f <- exportAVG (\x->let d = secondaryData x in d M.! "fdr") xxs
    return [tot_p,tot_f,tar_p,tar_f]
    {-; exportAVG "../avgs/Targets_AVG_mean.tsv"  xxs'-}

routine :: FilePath -> FilePath -> IO()
routine root cur = do
    flag<-mvroot root cur;
    if flag then do
        filenames' <- getDirectoryContents "."
        let filenames = filter (\x -> "_cybert_result.tsv" `isInfixOf` x) filenames'
        collections <- mapM loadCybert' filenames
        putStrLn $ show filenames
        {-acquire cybert entries -}
        avgs <- P.mapM subroutine collections
        writeString "../avgs/Total_AVG_pval.tsv" (stringify "\n" (map (\x->x!!0) avgs))
        writeString "../avgs/Total_AVG_fdr.tsv" (stringify "\n" (map (\x->x!!1) avgs))
        writeString "../avgs/Targets_AVG_pval.tsv" (stringify "\n" (map (\x->x!!2) avgs))
        writeString "../avgs/Targets_AVG_fdr.tsv" (stringify "\n" (map (\x->x!!3) avgs))
        {-E.evaluate $ runEval $ do-}
            {-parMap (rpar.subroutine) collections-}
            {-parMap rseq parTasks-}
        {-use pval of 0.05 and fpr of 0.1 to filter-}
        putStr $ "\nHandled" ++ cur ++"\n"
        setCurrentDirectory root
    else do
        putStr "\nNothing is done\n"
        setCurrentDirectory root

main = do
{-first get stuff from the "runs" folder-}
     curdir <- getCurrentDirectory;
     mapM (routine curdir) targetDirs














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

312ea5fb80ee00f190b74af0b7f9846a1a869a0d

















312ea5fb80ee00f190b74af0b7f9846a1a869a0d


Switch branch/tag










haskell


cybert-stack


app


huanganalysis.hs



Find file
Normal viewHistoryPermalink






huanganalysis.hs



4.53 KB









Newer










Older









stack implementaion of the cybert project



yul13
committed
Apr 18, 2016




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


17


18


19


20


21


22


23


24


25


26


27


28


29


30


31


32


33


34


35


36


37


38


39


40


41


42


43


44


45


46


47


48


49


50


51


52


53


54


55


56


57


58


59


60


61


62


63


64


65


66


67


68


69


70


71


72


73


74


75


76


77


78


79


80


81


82


83


84


85


86


87


88


89


90


91


92


93


94


95


96


97


98


99


100


101


102


103


104


105


106


107


108


109


110


111


112


113


114


115



import Cybert
import Data.Maybe
import Data.List
import qualified Data.Map as M
import Control.Monad
import qualified Control.Exception as E
import System.Directory
import System.IO
import qualified Control.Monad.Parallel as P
{-import Data.Set(fromList, toList, union, empty)-}

cheader' = M.fromList [("Secondary_Data_FDR","fdr"), ("Secondary_Data_FPR","fpr"),("Secondary_Data_Pval1","pval1")]
cheader  = M.union (cheader') (M.adjust (\x->"accession") "probe" cybert_header)
loadCybert' = loadCybert cheader
targetDirs= ["NatData_runs","XlinkData_runs"]

{-utility fctns for writing to file -}
stringify sep xs = foldl1 (\x y ->x++sep++ y) xs

writeString fname content = do
    withFile fname WriteMode (\handle -> do
        hPutStr handle content)

mvroot :: FilePath -> FilePath -> IO Bool
mvroot root cur = do
   E.catch
    (
        do
         createDirectoryIfMissing True (cur ++"/ontargets")
         createDirectoryIfMissing True (cur ++"/scatters")
         createDirectoryIfMissing True (cur ++"/avgs")
         setCurrentDirectory  (cur++"/runs")
         return True
    )
    (\e-> do
        let err = show (e::E.IOException)
        putStr $ stringify "\n" ["Error Chaging to directory\n" , cur,
           "Err:", err]
        return False
    );

{-extractTargets :: [Cybert_entry] -> IO()-}

exportScatter fname xs = do
    let ss = map (\x-> let d = secondaryData x
                        in stringify "\t" [probe x, show (d M.! "pval1"),show (d M.! "fdr")]) xs
    (writeString ("../scatters/"++ fname)) (stringify "\n" ss)

exportTargets fname xs = do
    exportProbes xs ("../ontargets/" ++ fname)
    let ss = map (\x-> let d = secondaryData x
                        in stringify "\t"
                            [probe x, showEitherF (mean x), show (d M.! "pval1"),
                             show (d M.! "fpr"), show (d M.! "fdr")]) xs
    writeString ("../targetdetails/"++ fname) $ 
        stringify "\n" (["accession\tmean\tpvalue(one-tail)\tFPR\tFDR"]++ss)
exportAVG fn xs = do
    let xxs = map fn xs
    let avg = if length xs >=2 then (foldl1 (+) xxs) / (fromIntegral (length xxs))
               else 1.0
    let gname = if length xs == 0 then "Unknown"
                else fromMaybe "Unknown" (collection $ (xs !! 0))
    let ss = gname ++ "\t" ++ (show avg) ++ "\n"
    return ss
subroutine:: (Maybe [Cybert_entry]) -> IO ([String])
subroutine xs = do
    let xxs'= fromMaybe [] xs
    let fname = if length xxs' == 0 then "Nill.dummy"
                else fromMaybe "Nill.dummy" (collection $ xxs' !! 0)
    ; exportScatter fname xxs'
    {-; exportAVG "../avgs/Total_AVG_mean.tsv" mean xxs'-}
    let xxs = filter (\x -> let d = (secondaryData x)
                              in if (d M.! "fdr") < 0.0 then (pval x) < 0.05
                             {-in case where beta fitting fails-}
                                 else (d M.! "pval1") <0.05 
                                 {-&& (d M.! "fdr") < 0.1-}
                     ) (entriesByUpDown True xxs')
    ; putStrLn  (fname ++" , "++(show $ length xxs))
    ; exportTargets fname xxs
    ; tot_p <- exportAVG pval  xxs'
    ; tot_f <- exportAVG (\x->let d = secondaryData x in d M.! "fdr") xxs'
    ; tar_p <- exportAVG pval xxs
    ; tar_f <- exportAVG (\x->let d = secondaryData x in d M.! "fdr") xxs
    return [tot_p,tot_f,tar_p,tar_f]
    {-; exportAVG "../avgs/Targets_AVG_mean.tsv"  xxs'-}

routine :: FilePath -> FilePath -> IO()
routine root cur = do
    flag<-mvroot root cur;
    if flag then do
        filenames' <- getDirectoryContents "."
        let filenames = filter (\x -> "_cybert_result.tsv" `isInfixOf` x) filenames'
        collections <- mapM loadCybert' filenames
        putStrLn $ show filenames
        {-acquire cybert entries -}
        avgs <- P.mapM subroutine collections
        writeString "../avgs/Total_AVG_pval.tsv" (stringify "\n" (map (\x->x!!0) avgs))
        writeString "../avgs/Total_AVG_fdr.tsv" (stringify "\n" (map (\x->x!!1) avgs))
        writeString "../avgs/Targets_AVG_pval.tsv" (stringify "\n" (map (\x->x!!2) avgs))
        writeString "../avgs/Targets_AVG_fdr.tsv" (stringify "\n" (map (\x->x!!3) avgs))
        {-E.evaluate $ runEval $ do-}
            {-parMap (rpar.subroutine) collections-}
            {-parMap rseq parTasks-}
        {-use pval of 0.05 and fpr of 0.1 to filter-}
        putStr $ "\nHandled" ++ cur ++"\n"
        setCurrentDirectory root
    else do
        putStr "\nNothing is done\n"
        setCurrentDirectory root

main = do
{-first get stuff from the "runs" folder-}
     curdir <- getCurrentDirectory;
     mapM (routine curdir) targetDirs













Open sidebar



Yu Liu haskell

312ea5fb80ee00f190b74af0b7f9846a1a869a0d







Open sidebar



Yu Liu haskell

312ea5fb80ee00f190b74af0b7f9846a1a869a0d




Open sidebar

Yu Liu haskell

312ea5fb80ee00f190b74af0b7f9846a1a869a0d


Yu Liuhaskellhaskell
312ea5fb80ee00f190b74af0b7f9846a1a869a0d










312ea5fb80ee00f190b74af0b7f9846a1a869a0d


Switch branch/tag










haskell


cybert-stack


app


huanganalysis.hs



Find file
Normal viewHistoryPermalink






huanganalysis.hs



4.53 KB









Newer










Older









stack implementaion of the cybert project



yul13
committed
Apr 18, 2016




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


17


18


19


20


21


22


23


24


25


26


27


28


29


30


31


32


33


34


35


36


37


38


39


40


41


42


43


44


45


46


47


48


49


50


51


52


53


54


55


56


57


58


59


60


61


62


63


64


65


66


67


68


69


70


71


72


73


74


75


76


77


78


79


80


81


82


83


84


85


86


87


88


89


90


91


92


93


94


95


96


97


98


99


100


101


102


103


104


105


106


107


108


109


110


111


112


113


114


115



import Cybert
import Data.Maybe
import Data.List
import qualified Data.Map as M
import Control.Monad
import qualified Control.Exception as E
import System.Directory
import System.IO
import qualified Control.Monad.Parallel as P
{-import Data.Set(fromList, toList, union, empty)-}

cheader' = M.fromList [("Secondary_Data_FDR","fdr"), ("Secondary_Data_FPR","fpr"),("Secondary_Data_Pval1","pval1")]
cheader  = M.union (cheader') (M.adjust (\x->"accession") "probe" cybert_header)
loadCybert' = loadCybert cheader
targetDirs= ["NatData_runs","XlinkData_runs"]

{-utility fctns for writing to file -}
stringify sep xs = foldl1 (\x y ->x++sep++ y) xs

writeString fname content = do
    withFile fname WriteMode (\handle -> do
        hPutStr handle content)

mvroot :: FilePath -> FilePath -> IO Bool
mvroot root cur = do
   E.catch
    (
        do
         createDirectoryIfMissing True (cur ++"/ontargets")
         createDirectoryIfMissing True (cur ++"/scatters")
         createDirectoryIfMissing True (cur ++"/avgs")
         setCurrentDirectory  (cur++"/runs")
         return True
    )
    (\e-> do
        let err = show (e::E.IOException)
        putStr $ stringify "\n" ["Error Chaging to directory\n" , cur,
           "Err:", err]
        return False
    );

{-extractTargets :: [Cybert_entry] -> IO()-}

exportScatter fname xs = do
    let ss = map (\x-> let d = secondaryData x
                        in stringify "\t" [probe x, show (d M.! "pval1"),show (d M.! "fdr")]) xs
    (writeString ("../scatters/"++ fname)) (stringify "\n" ss)

exportTargets fname xs = do
    exportProbes xs ("../ontargets/" ++ fname)
    let ss = map (\x-> let d = secondaryData x
                        in stringify "\t"
                            [probe x, showEitherF (mean x), show (d M.! "pval1"),
                             show (d M.! "fpr"), show (d M.! "fdr")]) xs
    writeString ("../targetdetails/"++ fname) $ 
        stringify "\n" (["accession\tmean\tpvalue(one-tail)\tFPR\tFDR"]++ss)
exportAVG fn xs = do
    let xxs = map fn xs
    let avg = if length xs >=2 then (foldl1 (+) xxs) / (fromIntegral (length xxs))
               else 1.0
    let gname = if length xs == 0 then "Unknown"
                else fromMaybe "Unknown" (collection $ (xs !! 0))
    let ss = gname ++ "\t" ++ (show avg) ++ "\n"
    return ss
subroutine:: (Maybe [Cybert_entry]) -> IO ([String])
subroutine xs = do
    let xxs'= fromMaybe [] xs
    let fname = if length xxs' == 0 then "Nill.dummy"
                else fromMaybe "Nill.dummy" (collection $ xxs' !! 0)
    ; exportScatter fname xxs'
    {-; exportAVG "../avgs/Total_AVG_mean.tsv" mean xxs'-}
    let xxs = filter (\x -> let d = (secondaryData x)
                              in if (d M.! "fdr") < 0.0 then (pval x) < 0.05
                             {-in case where beta fitting fails-}
                                 else (d M.! "pval1") <0.05 
                                 {-&& (d M.! "fdr") < 0.1-}
                     ) (entriesByUpDown True xxs')
    ; putStrLn  (fname ++" , "++(show $ length xxs))
    ; exportTargets fname xxs
    ; tot_p <- exportAVG pval  xxs'
    ; tot_f <- exportAVG (\x->let d = secondaryData x in d M.! "fdr") xxs'
    ; tar_p <- exportAVG pval xxs
    ; tar_f <- exportAVG (\x->let d = secondaryData x in d M.! "fdr") xxs
    return [tot_p,tot_f,tar_p,tar_f]
    {-; exportAVG "../avgs/Targets_AVG_mean.tsv"  xxs'-}

routine :: FilePath -> FilePath -> IO()
routine root cur = do
    flag<-mvroot root cur;
    if flag then do
        filenames' <- getDirectoryContents "."
        let filenames = filter (\x -> "_cybert_result.tsv" `isInfixOf` x) filenames'
        collections <- mapM loadCybert' filenames
        putStrLn $ show filenames
        {-acquire cybert entries -}
        avgs <- P.mapM subroutine collections
        writeString "../avgs/Total_AVG_pval.tsv" (stringify "\n" (map (\x->x!!0) avgs))
        writeString "../avgs/Total_AVG_fdr.tsv" (stringify "\n" (map (\x->x!!1) avgs))
        writeString "../avgs/Targets_AVG_pval.tsv" (stringify "\n" (map (\x->x!!2) avgs))
        writeString "../avgs/Targets_AVG_fdr.tsv" (stringify "\n" (map (\x->x!!3) avgs))
        {-E.evaluate $ runEval $ do-}
            {-parMap (rpar.subroutine) collections-}
            {-parMap rseq parTasks-}
        {-use pval of 0.05 and fpr of 0.1 to filter-}
        putStr $ "\nHandled" ++ cur ++"\n"
        setCurrentDirectory root
    else do
        putStr "\nNothing is done\n"
        setCurrentDirectory root

main = do
{-first get stuff from the "runs" folder-}
     curdir <- getCurrentDirectory;
     mapM (routine curdir) targetDirs
















312ea5fb80ee00f190b74af0b7f9846a1a869a0d


Switch branch/tag










haskell


cybert-stack


app


huanganalysis.hs



Find file
Normal viewHistoryPermalink






huanganalysis.hs



4.53 KB









Newer










Older









stack implementaion of the cybert project



yul13
committed
Apr 18, 2016




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


17


18


19


20


21


22


23


24


25


26


27


28


29


30


31


32


33


34


35


36


37


38


39


40


41


42


43


44


45


46


47


48


49


50


51


52


53


54


55


56


57


58


59


60


61


62


63


64


65


66


67


68


69


70


71


72


73


74


75


76


77


78


79


80


81


82


83


84


85


86


87


88


89


90


91


92


93


94


95


96


97


98


99


100


101


102


103


104


105


106


107


108


109


110


111


112


113


114


115



import Cybert
import Data.Maybe
import Data.List
import qualified Data.Map as M
import Control.Monad
import qualified Control.Exception as E
import System.Directory
import System.IO
import qualified Control.Monad.Parallel as P
{-import Data.Set(fromList, toList, union, empty)-}

cheader' = M.fromList [("Secondary_Data_FDR","fdr"), ("Secondary_Data_FPR","fpr"),("Secondary_Data_Pval1","pval1")]
cheader  = M.union (cheader') (M.adjust (\x->"accession") "probe" cybert_header)
loadCybert' = loadCybert cheader
targetDirs= ["NatData_runs","XlinkData_runs"]

{-utility fctns for writing to file -}
stringify sep xs = foldl1 (\x y ->x++sep++ y) xs

writeString fname content = do
    withFile fname WriteMode (\handle -> do
        hPutStr handle content)

mvroot :: FilePath -> FilePath -> IO Bool
mvroot root cur = do
   E.catch
    (
        do
         createDirectoryIfMissing True (cur ++"/ontargets")
         createDirectoryIfMissing True (cur ++"/scatters")
         createDirectoryIfMissing True (cur ++"/avgs")
         setCurrentDirectory  (cur++"/runs")
         return True
    )
    (\e-> do
        let err = show (e::E.IOException)
        putStr $ stringify "\n" ["Error Chaging to directory\n" , cur,
           "Err:", err]
        return False
    );

{-extractTargets :: [Cybert_entry] -> IO()-}

exportScatter fname xs = do
    let ss = map (\x-> let d = secondaryData x
                        in stringify "\t" [probe x, show (d M.! "pval1"),show (d M.! "fdr")]) xs
    (writeString ("../scatters/"++ fname)) (stringify "\n" ss)

exportTargets fname xs = do
    exportProbes xs ("../ontargets/" ++ fname)
    let ss = map (\x-> let d = secondaryData x
                        in stringify "\t"
                            [probe x, showEitherF (mean x), show (d M.! "pval1"),
                             show (d M.! "fpr"), show (d M.! "fdr")]) xs
    writeString ("../targetdetails/"++ fname) $ 
        stringify "\n" (["accession\tmean\tpvalue(one-tail)\tFPR\tFDR"]++ss)
exportAVG fn xs = do
    let xxs = map fn xs
    let avg = if length xs >=2 then (foldl1 (+) xxs) / (fromIntegral (length xxs))
               else 1.0
    let gname = if length xs == 0 then "Unknown"
                else fromMaybe "Unknown" (collection $ (xs !! 0))
    let ss = gname ++ "\t" ++ (show avg) ++ "\n"
    return ss
subroutine:: (Maybe [Cybert_entry]) -> IO ([String])
subroutine xs = do
    let xxs'= fromMaybe [] xs
    let fname = if length xxs' == 0 then "Nill.dummy"
                else fromMaybe "Nill.dummy" (collection $ xxs' !! 0)
    ; exportScatter fname xxs'
    {-; exportAVG "../avgs/Total_AVG_mean.tsv" mean xxs'-}
    let xxs = filter (\x -> let d = (secondaryData x)
                              in if (d M.! "fdr") < 0.0 then (pval x) < 0.05
                             {-in case where beta fitting fails-}
                                 else (d M.! "pval1") <0.05 
                                 {-&& (d M.! "fdr") < 0.1-}
                     ) (entriesByUpDown True xxs')
    ; putStrLn  (fname ++" , "++(show $ length xxs))
    ; exportTargets fname xxs
    ; tot_p <- exportAVG pval  xxs'
    ; tot_f <- exportAVG (\x->let d = secondaryData x in d M.! "fdr") xxs'
    ; tar_p <- exportAVG pval xxs
    ; tar_f <- exportAVG (\x->let d = secondaryData x in d M.! "fdr") xxs
    return [tot_p,tot_f,tar_p,tar_f]
    {-; exportAVG "../avgs/Targets_AVG_mean.tsv"  xxs'-}

routine :: FilePath -> FilePath -> IO()
routine root cur = do
    flag<-mvroot root cur;
    if flag then do
        filenames' <- getDirectoryContents "."
        let filenames = filter (\x -> "_cybert_result.tsv" `isInfixOf` x) filenames'
        collections <- mapM loadCybert' filenames
        putStrLn $ show filenames
        {-acquire cybert entries -}
        avgs <- P.mapM subroutine collections
        writeString "../avgs/Total_AVG_pval.tsv" (stringify "\n" (map (\x->x!!0) avgs))
        writeString "../avgs/Total_AVG_fdr.tsv" (stringify "\n" (map (\x->x!!1) avgs))
        writeString "../avgs/Targets_AVG_pval.tsv" (stringify "\n" (map (\x->x!!2) avgs))
        writeString "../avgs/Targets_AVG_fdr.tsv" (stringify "\n" (map (\x->x!!3) avgs))
        {-E.evaluate $ runEval $ do-}
            {-parMap (rpar.subroutine) collections-}
            {-parMap rseq parTasks-}
        {-use pval of 0.05 and fpr of 0.1 to filter-}
        putStr $ "\nHandled" ++ cur ++"\n"
        setCurrentDirectory root
    else do
        putStr "\nNothing is done\n"
        setCurrentDirectory root

main = do
{-first get stuff from the "runs" folder-}
     curdir <- getCurrentDirectory;
     mapM (routine curdir) targetDirs












312ea5fb80ee00f190b74af0b7f9846a1a869a0d


Switch branch/tag










haskell


cybert-stack


app


huanganalysis.hs



Find file
Normal viewHistoryPermalink




312ea5fb80ee00f190b74af0b7f9846a1a869a0d


Switch branch/tag










haskell


cybert-stack


app


huanganalysis.hs





312ea5fb80ee00f190b74af0b7f9846a1a869a0d


Switch branch/tag








312ea5fb80ee00f190b74af0b7f9846a1a869a0d


Switch branch/tag





312ea5fb80ee00f190b74af0b7f9846a1a869a0d

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
haskell

cybert-stack

app

huanganalysis.hs
Find file
Normal viewHistoryPermalink




huanganalysis.hs



4.53 KB









Newer










Older









stack implementaion of the cybert project



yul13
committed
Apr 18, 2016




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


17


18


19


20


21


22


23


24


25


26


27


28


29


30


31


32


33


34


35


36


37


38


39


40


41


42


43


44


45


46


47


48


49


50


51


52


53


54


55


56


57


58


59


60


61


62


63


64


65


66


67


68


69


70


71


72


73


74


75


76


77


78


79


80


81


82


83


84


85


86


87


88


89


90


91


92


93


94


95


96


97


98


99


100


101


102


103


104


105


106


107


108


109


110


111


112


113


114


115



import Cybert
import Data.Maybe
import Data.List
import qualified Data.Map as M
import Control.Monad
import qualified Control.Exception as E
import System.Directory
import System.IO
import qualified Control.Monad.Parallel as P
{-import Data.Set(fromList, toList, union, empty)-}

cheader' = M.fromList [("Secondary_Data_FDR","fdr"), ("Secondary_Data_FPR","fpr"),("Secondary_Data_Pval1","pval1")]
cheader  = M.union (cheader') (M.adjust (\x->"accession") "probe" cybert_header)
loadCybert' = loadCybert cheader
targetDirs= ["NatData_runs","XlinkData_runs"]

{-utility fctns for writing to file -}
stringify sep xs = foldl1 (\x y ->x++sep++ y) xs

writeString fname content = do
    withFile fname WriteMode (\handle -> do
        hPutStr handle content)

mvroot :: FilePath -> FilePath -> IO Bool
mvroot root cur = do
   E.catch
    (
        do
         createDirectoryIfMissing True (cur ++"/ontargets")
         createDirectoryIfMissing True (cur ++"/scatters")
         createDirectoryIfMissing True (cur ++"/avgs")
         setCurrentDirectory  (cur++"/runs")
         return True
    )
    (\e-> do
        let err = show (e::E.IOException)
        putStr $ stringify "\n" ["Error Chaging to directory\n" , cur,
           "Err:", err]
        return False
    );

{-extractTargets :: [Cybert_entry] -> IO()-}

exportScatter fname xs = do
    let ss = map (\x-> let d = secondaryData x
                        in stringify "\t" [probe x, show (d M.! "pval1"),show (d M.! "fdr")]) xs
    (writeString ("../scatters/"++ fname)) (stringify "\n" ss)

exportTargets fname xs = do
    exportProbes xs ("../ontargets/" ++ fname)
    let ss = map (\x-> let d = secondaryData x
                        in stringify "\t"
                            [probe x, showEitherF (mean x), show (d M.! "pval1"),
                             show (d M.! "fpr"), show (d M.! "fdr")]) xs
    writeString ("../targetdetails/"++ fname) $ 
        stringify "\n" (["accession\tmean\tpvalue(one-tail)\tFPR\tFDR"]++ss)
exportAVG fn xs = do
    let xxs = map fn xs
    let avg = if length xs >=2 then (foldl1 (+) xxs) / (fromIntegral (length xxs))
               else 1.0
    let gname = if length xs == 0 then "Unknown"
                else fromMaybe "Unknown" (collection $ (xs !! 0))
    let ss = gname ++ "\t" ++ (show avg) ++ "\n"
    return ss
subroutine:: (Maybe [Cybert_entry]) -> IO ([String])
subroutine xs = do
    let xxs'= fromMaybe [] xs
    let fname = if length xxs' == 0 then "Nill.dummy"
                else fromMaybe "Nill.dummy" (collection $ xxs' !! 0)
    ; exportScatter fname xxs'
    {-; exportAVG "../avgs/Total_AVG_mean.tsv" mean xxs'-}
    let xxs = filter (\x -> let d = (secondaryData x)
                              in if (d M.! "fdr") < 0.0 then (pval x) < 0.05
                             {-in case where beta fitting fails-}
                                 else (d M.! "pval1") <0.05 
                                 {-&& (d M.! "fdr") < 0.1-}
                     ) (entriesByUpDown True xxs')
    ; putStrLn  (fname ++" , "++(show $ length xxs))
    ; exportTargets fname xxs
    ; tot_p <- exportAVG pval  xxs'
    ; tot_f <- exportAVG (\x->let d = secondaryData x in d M.! "fdr") xxs'
    ; tar_p <- exportAVG pval xxs
    ; tar_f <- exportAVG (\x->let d = secondaryData x in d M.! "fdr") xxs
    return [tot_p,tot_f,tar_p,tar_f]
    {-; exportAVG "../avgs/Targets_AVG_mean.tsv"  xxs'-}

routine :: FilePath -> FilePath -> IO()
routine root cur = do
    flag<-mvroot root cur;
    if flag then do
        filenames' <- getDirectoryContents "."
        let filenames = filter (\x -> "_cybert_result.tsv" `isInfixOf` x) filenames'
        collections <- mapM loadCybert' filenames
        putStrLn $ show filenames
        {-acquire cybert entries -}
        avgs <- P.mapM subroutine collections
        writeString "../avgs/Total_AVG_pval.tsv" (stringify "\n" (map (\x->x!!0) avgs))
        writeString "../avgs/Total_AVG_fdr.tsv" (stringify "\n" (map (\x->x!!1) avgs))
        writeString "../avgs/Targets_AVG_pval.tsv" (stringify "\n" (map (\x->x!!2) avgs))
        writeString "../avgs/Targets_AVG_fdr.tsv" (stringify "\n" (map (\x->x!!3) avgs))
        {-E.evaluate $ runEval $ do-}
            {-parMap (rpar.subroutine) collections-}
            {-parMap rseq parTasks-}
        {-use pval of 0.05 and fpr of 0.1 to filter-}
        putStr $ "\nHandled" ++ cur ++"\n"
        setCurrentDirectory root
    else do
        putStr "\nNothing is done\n"
        setCurrentDirectory root

main = do
{-first get stuff from the "runs" folder-}
     curdir <- getCurrentDirectory;
     mapM (routine curdir) targetDirs










huanganalysis.hs



4.53 KB










huanganalysis.hs



4.53 KB









Newer










Older
NewerOlder







stack implementaion of the cybert project



yul13
committed
Apr 18, 2016




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


17


18


19


20


21


22


23


24


25


26


27


28


29


30


31


32


33


34


35


36


37


38


39


40


41


42


43


44


45


46


47


48


49


50


51


52


53


54


55


56


57


58


59


60


61


62


63


64


65


66


67


68


69


70


71


72


73


74


75


76


77


78


79


80


81


82


83


84


85


86


87


88


89


90


91


92


93


94


95


96


97


98


99


100


101


102


103


104


105


106


107


108


109


110


111


112


113


114


115



import Cybert
import Data.Maybe
import Data.List
import qualified Data.Map as M
import Control.Monad
import qualified Control.Exception as E
import System.Directory
import System.IO
import qualified Control.Monad.Parallel as P
{-import Data.Set(fromList, toList, union, empty)-}

cheader' = M.fromList [("Secondary_Data_FDR","fdr"), ("Secondary_Data_FPR","fpr"),("Secondary_Data_Pval1","pval1")]
cheader  = M.union (cheader') (M.adjust (\x->"accession") "probe" cybert_header)
loadCybert' = loadCybert cheader
targetDirs= ["NatData_runs","XlinkData_runs"]

{-utility fctns for writing to file -}
stringify sep xs = foldl1 (\x y ->x++sep++ y) xs

writeString fname content = do
    withFile fname WriteMode (\handle -> do
        hPutStr handle content)

mvroot :: FilePath -> FilePath -> IO Bool
mvroot root cur = do
   E.catch
    (
        do
         createDirectoryIfMissing True (cur ++"/ontargets")
         createDirectoryIfMissing True (cur ++"/scatters")
         createDirectoryIfMissing True (cur ++"/avgs")
         setCurrentDirectory  (cur++"/runs")
         return True
    )
    (\e-> do
        let err = show (e::E.IOException)
        putStr $ stringify "\n" ["Error Chaging to directory\n" , cur,
           "Err:", err]
        return False
    );

{-extractTargets :: [Cybert_entry] -> IO()-}

exportScatter fname xs = do
    let ss = map (\x-> let d = secondaryData x
                        in stringify "\t" [probe x, show (d M.! "pval1"),show (d M.! "fdr")]) xs
    (writeString ("../scatters/"++ fname)) (stringify "\n" ss)

exportTargets fname xs = do
    exportProbes xs ("../ontargets/" ++ fname)
    let ss = map (\x-> let d = secondaryData x
                        in stringify "\t"
                            [probe x, showEitherF (mean x), show (d M.! "pval1"),
                             show (d M.! "fpr"), show (d M.! "fdr")]) xs
    writeString ("../targetdetails/"++ fname) $ 
        stringify "\n" (["accession\tmean\tpvalue(one-tail)\tFPR\tFDR"]++ss)
exportAVG fn xs = do
    let xxs = map fn xs
    let avg = if length xs >=2 then (foldl1 (+) xxs) / (fromIntegral (length xxs))
               else 1.0
    let gname = if length xs == 0 then "Unknown"
                else fromMaybe "Unknown" (collection $ (xs !! 0))
    let ss = gname ++ "\t" ++ (show avg) ++ "\n"
    return ss
subroutine:: (Maybe [Cybert_entry]) -> IO ([String])
subroutine xs = do
    let xxs'= fromMaybe [] xs
    let fname = if length xxs' == 0 then "Nill.dummy"
                else fromMaybe "Nill.dummy" (collection $ xxs' !! 0)
    ; exportScatter fname xxs'
    {-; exportAVG "../avgs/Total_AVG_mean.tsv" mean xxs'-}
    let xxs = filter (\x -> let d = (secondaryData x)
                              in if (d M.! "fdr") < 0.0 then (pval x) < 0.05
                             {-in case where beta fitting fails-}
                                 else (d M.! "pval1") <0.05 
                                 {-&& (d M.! "fdr") < 0.1-}
                     ) (entriesByUpDown True xxs')
    ; putStrLn  (fname ++" , "++(show $ length xxs))
    ; exportTargets fname xxs
    ; tot_p <- exportAVG pval  xxs'
    ; tot_f <- exportAVG (\x->let d = secondaryData x in d M.! "fdr") xxs'
    ; tar_p <- exportAVG pval xxs
    ; tar_f <- exportAVG (\x->let d = secondaryData x in d M.! "fdr") xxs
    return [tot_p,tot_f,tar_p,tar_f]
    {-; exportAVG "../avgs/Targets_AVG_mean.tsv"  xxs'-}

routine :: FilePath -> FilePath -> IO()
routine root cur = do
    flag<-mvroot root cur;
    if flag then do
        filenames' <- getDirectoryContents "."
        let filenames = filter (\x -> "_cybert_result.tsv" `isInfixOf` x) filenames'
        collections <- mapM loadCybert' filenames
        putStrLn $ show filenames
        {-acquire cybert entries -}
        avgs <- P.mapM subroutine collections
        writeString "../avgs/Total_AVG_pval.tsv" (stringify "\n" (map (\x->x!!0) avgs))
        writeString "../avgs/Total_AVG_fdr.tsv" (stringify "\n" (map (\x->x!!1) avgs))
        writeString "../avgs/Targets_AVG_pval.tsv" (stringify "\n" (map (\x->x!!2) avgs))
        writeString "../avgs/Targets_AVG_fdr.tsv" (stringify "\n" (map (\x->x!!3) avgs))
        {-E.evaluate $ runEval $ do-}
            {-parMap (rpar.subroutine) collections-}
            {-parMap rseq parTasks-}
        {-use pval of 0.05 and fpr of 0.1 to filter-}
        putStr $ "\nHandled" ++ cur ++"\n"
        setCurrentDirectory root
    else do
        putStr "\nNothing is done\n"
        setCurrentDirectory root

main = do
{-first get stuff from the "runs" folder-}
     curdir <- getCurrentDirectory;
     mapM (routine curdir) targetDirs












stack implementaion of the cybert project



yul13
committed
Apr 18, 2016




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


17


18


19


20


21


22


23


24


25


26


27


28


29


30


31


32


33


34


35


36


37


38


39


40


41


42


43


44


45


46


47


48


49


50


51


52


53


54


55


56


57


58


59


60


61


62


63


64


65


66


67


68


69


70


71


72


73


74


75


76


77


78


79


80


81


82


83


84


85


86


87


88


89


90


91


92


93


94


95


96


97


98


99


100


101


102


103


104


105


106


107


108


109


110


111


112


113


114


115



import Cybert
import Data.Maybe
import Data.List
import qualified Data.Map as M
import Control.Monad
import qualified Control.Exception as E
import System.Directory
import System.IO
import qualified Control.Monad.Parallel as P
{-import Data.Set(fromList, toList, union, empty)-}

cheader' = M.fromList [("Secondary_Data_FDR","fdr"), ("Secondary_Data_FPR","fpr"),("Secondary_Data_Pval1","pval1")]
cheader  = M.union (cheader') (M.adjust (\x->"accession") "probe" cybert_header)
loadCybert' = loadCybert cheader
targetDirs= ["NatData_runs","XlinkData_runs"]

{-utility fctns for writing to file -}
stringify sep xs = foldl1 (\x y ->x++sep++ y) xs

writeString fname content = do
    withFile fname WriteMode (\handle -> do
        hPutStr handle content)

mvroot :: FilePath -> FilePath -> IO Bool
mvroot root cur = do
   E.catch
    (
        do
         createDirectoryIfMissing True (cur ++"/ontargets")
         createDirectoryIfMissing True (cur ++"/scatters")
         createDirectoryIfMissing True (cur ++"/avgs")
         setCurrentDirectory  (cur++"/runs")
         return True
    )
    (\e-> do
        let err = show (e::E.IOException)
        putStr $ stringify "\n" ["Error Chaging to directory\n" , cur,
           "Err:", err]
        return False
    );

{-extractTargets :: [Cybert_entry] -> IO()-}

exportScatter fname xs = do
    let ss = map (\x-> let d = secondaryData x
                        in stringify "\t" [probe x, show (d M.! "pval1"),show (d M.! "fdr")]) xs
    (writeString ("../scatters/"++ fname)) (stringify "\n" ss)

exportTargets fname xs = do
    exportProbes xs ("../ontargets/" ++ fname)
    let ss = map (\x-> let d = secondaryData x
                        in stringify "\t"
                            [probe x, showEitherF (mean x), show (d M.! "pval1"),
                             show (d M.! "fpr"), show (d M.! "fdr")]) xs
    writeString ("../targetdetails/"++ fname) $ 
        stringify "\n" (["accession\tmean\tpvalue(one-tail)\tFPR\tFDR"]++ss)
exportAVG fn xs = do
    let xxs = map fn xs
    let avg = if length xs >=2 then (foldl1 (+) xxs) / (fromIntegral (length xxs))
               else 1.0
    let gname = if length xs == 0 then "Unknown"
                else fromMaybe "Unknown" (collection $ (xs !! 0))
    let ss = gname ++ "\t" ++ (show avg) ++ "\n"
    return ss
subroutine:: (Maybe [Cybert_entry]) -> IO ([String])
subroutine xs = do
    let xxs'= fromMaybe [] xs
    let fname = if length xxs' == 0 then "Nill.dummy"
                else fromMaybe "Nill.dummy" (collection $ xxs' !! 0)
    ; exportScatter fname xxs'
    {-; exportAVG "../avgs/Total_AVG_mean.tsv" mean xxs'-}
    let xxs = filter (\x -> let d = (secondaryData x)
                              in if (d M.! "fdr") < 0.0 then (pval x) < 0.05
                             {-in case where beta fitting fails-}
                                 else (d M.! "pval1") <0.05 
                                 {-&& (d M.! "fdr") < 0.1-}
                     ) (entriesByUpDown True xxs')
    ; putStrLn  (fname ++" , "++(show $ length xxs))
    ; exportTargets fname xxs
    ; tot_p <- exportAVG pval  xxs'
    ; tot_f <- exportAVG (\x->let d = secondaryData x in d M.! "fdr") xxs'
    ; tar_p <- exportAVG pval xxs
    ; tar_f <- exportAVG (\x->let d = secondaryData x in d M.! "fdr") xxs
    return [tot_p,tot_f,tar_p,tar_f]
    {-; exportAVG "../avgs/Targets_AVG_mean.tsv"  xxs'-}

routine :: FilePath -> FilePath -> IO()
routine root cur = do
    flag<-mvroot root cur;
    if flag then do
        filenames' <- getDirectoryContents "."
        let filenames = filter (\x -> "_cybert_result.tsv" `isInfixOf` x) filenames'
        collections <- mapM loadCybert' filenames
        putStrLn $ show filenames
        {-acquire cybert entries -}
        avgs <- P.mapM subroutine collections
        writeString "../avgs/Total_AVG_pval.tsv" (stringify "\n" (map (\x->x!!0) avgs))
        writeString "../avgs/Total_AVG_fdr.tsv" (stringify "\n" (map (\x->x!!1) avgs))
        writeString "../avgs/Targets_AVG_pval.tsv" (stringify "\n" (map (\x->x!!2) avgs))
        writeString "../avgs/Targets_AVG_fdr.tsv" (stringify "\n" (map (\x->x!!3) avgs))
        {-E.evaluate $ runEval $ do-}
            {-parMap (rpar.subroutine) collections-}
            {-parMap rseq parTasks-}
        {-use pval of 0.05 and fpr of 0.1 to filter-}
        putStr $ "\nHandled" ++ cur ++"\n"
        setCurrentDirectory root
    else do
        putStr "\nNothing is done\n"
        setCurrentDirectory root

main = do
{-first get stuff from the "runs" folder-}
     curdir <- getCurrentDirectory;
     mapM (routine curdir) targetDirs










stack implementaion of the cybert project



yul13
committed
Apr 18, 2016




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


17


18


19


20


21


22


23


24


25


26


27


28


29


30


31


32


33


34


35


36


37


38


39


40


41


42


43


44


45


46


47


48


49


50


51


52


53


54


55


56


57


58


59


60


61


62


63


64


65


66


67


68


69


70


71


72


73


74


75


76


77


78


79


80


81


82


83


84


85


86


87


88


89


90


91


92


93


94


95


96


97


98


99


100


101


102


103


104


105


106


107


108


109


110


111


112


113


114


115



import Cybert
import Data.Maybe
import Data.List
import qualified Data.Map as M
import Control.Monad
import qualified Control.Exception as E
import System.Directory
import System.IO
import qualified Control.Monad.Parallel as P
{-import Data.Set(fromList, toList, union, empty)-}

cheader' = M.fromList [("Secondary_Data_FDR","fdr"), ("Secondary_Data_FPR","fpr"),("Secondary_Data_Pval1","pval1")]
cheader  = M.union (cheader') (M.adjust (\x->"accession") "probe" cybert_header)
loadCybert' = loadCybert cheader
targetDirs= ["NatData_runs","XlinkData_runs"]

{-utility fctns for writing to file -}
stringify sep xs = foldl1 (\x y ->x++sep++ y) xs

writeString fname content = do
    withFile fname WriteMode (\handle -> do
        hPutStr handle content)

mvroot :: FilePath -> FilePath -> IO Bool
mvroot root cur = do
   E.catch
    (
        do
         createDirectoryIfMissing True (cur ++"/ontargets")
         createDirectoryIfMissing True (cur ++"/scatters")
         createDirectoryIfMissing True (cur ++"/avgs")
         setCurrentDirectory  (cur++"/runs")
         return True
    )
    (\e-> do
        let err = show (e::E.IOException)
        putStr $ stringify "\n" ["Error Chaging to directory\n" , cur,
           "Err:", err]
        return False
    );

{-extractTargets :: [Cybert_entry] -> IO()-}

exportScatter fname xs = do
    let ss = map (\x-> let d = secondaryData x
                        in stringify "\t" [probe x, show (d M.! "pval1"),show (d M.! "fdr")]) xs
    (writeString ("../scatters/"++ fname)) (stringify "\n" ss)

exportTargets fname xs = do
    exportProbes xs ("../ontargets/" ++ fname)
    let ss = map (\x-> let d = secondaryData x
                        in stringify "\t"
                            [probe x, showEitherF (mean x), show (d M.! "pval1"),
                             show (d M.! "fpr"), show (d M.! "fdr")]) xs
    writeString ("../targetdetails/"++ fname) $ 
        stringify "\n" (["accession\tmean\tpvalue(one-tail)\tFPR\tFDR"]++ss)
exportAVG fn xs = do
    let xxs = map fn xs
    let avg = if length xs >=2 then (foldl1 (+) xxs) / (fromIntegral (length xxs))
               else 1.0
    let gname = if length xs == 0 then "Unknown"
                else fromMaybe "Unknown" (collection $ (xs !! 0))
    let ss = gname ++ "\t" ++ (show avg) ++ "\n"
    return ss
subroutine:: (Maybe [Cybert_entry]) -> IO ([String])
subroutine xs = do
    let xxs'= fromMaybe [] xs
    let fname = if length xxs' == 0 then "Nill.dummy"
                else fromMaybe "Nill.dummy" (collection $ xxs' !! 0)
    ; exportScatter fname xxs'
    {-; exportAVG "../avgs/Total_AVG_mean.tsv" mean xxs'-}
    let xxs = filter (\x -> let d = (secondaryData x)
                              in if (d M.! "fdr") < 0.0 then (pval x) < 0.05
                             {-in case where beta fitting fails-}
                                 else (d M.! "pval1") <0.05 
                                 {-&& (d M.! "fdr") < 0.1-}
                     ) (entriesByUpDown True xxs')
    ; putStrLn  (fname ++" , "++(show $ length xxs))
    ; exportTargets fname xxs
    ; tot_p <- exportAVG pval  xxs'
    ; tot_f <- exportAVG (\x->let d = secondaryData x in d M.! "fdr") xxs'
    ; tar_p <- exportAVG pval xxs
    ; tar_f <- exportAVG (\x->let d = secondaryData x in d M.! "fdr") xxs
    return [tot_p,tot_f,tar_p,tar_f]
    {-; exportAVG "../avgs/Targets_AVG_mean.tsv"  xxs'-}

routine :: FilePath -> FilePath -> IO()
routine root cur = do
    flag<-mvroot root cur;
    if flag then do
        filenames' <- getDirectoryContents "."
        let filenames = filter (\x -> "_cybert_result.tsv" `isInfixOf` x) filenames'
        collections <- mapM loadCybert' filenames
        putStrLn $ show filenames
        {-acquire cybert entries -}
        avgs <- P.mapM subroutine collections
        writeString "../avgs/Total_AVG_pval.tsv" (stringify "\n" (map (\x->x!!0) avgs))
        writeString "../avgs/Total_AVG_fdr.tsv" (stringify "\n" (map (\x->x!!1) avgs))
        writeString "../avgs/Targets_AVG_pval.tsv" (stringify "\n" (map (\x->x!!2) avgs))
        writeString "../avgs/Targets_AVG_fdr.tsv" (stringify "\n" (map (\x->x!!3) avgs))
        {-E.evaluate $ runEval $ do-}
            {-parMap (rpar.subroutine) collections-}
            {-parMap rseq parTasks-}
        {-use pval of 0.05 and fpr of 0.1 to filter-}
        putStr $ "\nHandled" ++ cur ++"\n"
        setCurrentDirectory root
    else do
        putStr "\nNothing is done\n"
        setCurrentDirectory root

main = do
{-first get stuff from the "runs" folder-}
     curdir <- getCurrentDirectory;
     mapM (routine curdir) targetDirs








stack implementaion of the cybert project



yul13
committed
Apr 18, 2016




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


17


18


19


20


21


22


23


24


25


26


27


28


29


30


31


32


33


34


35


36


37


38


39


40


41


42


43


44


45


46


47


48


49


50


51


52


53


54


55


56


57


58


59


60


61


62


63


64


65


66


67


68


69


70


71


72


73


74


75


76


77


78


79


80


81


82


83


84


85


86


87


88


89


90


91


92


93


94


95


96


97


98


99


100


101


102


103


104


105


106


107


108


109


110


111


112


113


114


115



import Cybert
import Data.Maybe
import Data.List
import qualified Data.Map as M
import Control.Monad
import qualified Control.Exception as E
import System.Directory
import System.IO
import qualified Control.Monad.Parallel as P
{-import Data.Set(fromList, toList, union, empty)-}

cheader' = M.fromList [("Secondary_Data_FDR","fdr"), ("Secondary_Data_FPR","fpr"),("Secondary_Data_Pval1","pval1")]
cheader  = M.union (cheader') (M.adjust (\x->"accession") "probe" cybert_header)
loadCybert' = loadCybert cheader
targetDirs= ["NatData_runs","XlinkData_runs"]

{-utility fctns for writing to file -}
stringify sep xs = foldl1 (\x y ->x++sep++ y) xs

writeString fname content = do
    withFile fname WriteMode (\handle -> do
        hPutStr handle content)

mvroot :: FilePath -> FilePath -> IO Bool
mvroot root cur = do
   E.catch
    (
        do
         createDirectoryIfMissing True (cur ++"/ontargets")
         createDirectoryIfMissing True (cur ++"/scatters")
         createDirectoryIfMissing True (cur ++"/avgs")
         setCurrentDirectory  (cur++"/runs")
         return True
    )
    (\e-> do
        let err = show (e::E.IOException)
        putStr $ stringify "\n" ["Error Chaging to directory\n" , cur,
           "Err:", err]
        return False
    );

{-extractTargets :: [Cybert_entry] -> IO()-}

exportScatter fname xs = do
    let ss = map (\x-> let d = secondaryData x
                        in stringify "\t" [probe x, show (d M.! "pval1"),show (d M.! "fdr")]) xs
    (writeString ("../scatters/"++ fname)) (stringify "\n" ss)

exportTargets fname xs = do
    exportProbes xs ("../ontargets/" ++ fname)
    let ss = map (\x-> let d = secondaryData x
                        in stringify "\t"
                            [probe x, showEitherF (mean x), show (d M.! "pval1"),
                             show (d M.! "fpr"), show (d M.! "fdr")]) xs
    writeString ("../targetdetails/"++ fname) $ 
        stringify "\n" (["accession\tmean\tpvalue(one-tail)\tFPR\tFDR"]++ss)
exportAVG fn xs = do
    let xxs = map fn xs
    let avg = if length xs >=2 then (foldl1 (+) xxs) / (fromIntegral (length xxs))
               else 1.0
    let gname = if length xs == 0 then "Unknown"
                else fromMaybe "Unknown" (collection $ (xs !! 0))
    let ss = gname ++ "\t" ++ (show avg) ++ "\n"
    return ss
subroutine:: (Maybe [Cybert_entry]) -> IO ([String])
subroutine xs = do
    let xxs'= fromMaybe [] xs
    let fname = if length xxs' == 0 then "Nill.dummy"
                else fromMaybe "Nill.dummy" (collection $ xxs' !! 0)
    ; exportScatter fname xxs'
    {-; exportAVG "../avgs/Total_AVG_mean.tsv" mean xxs'-}
    let xxs = filter (\x -> let d = (secondaryData x)
                              in if (d M.! "fdr") < 0.0 then (pval x) < 0.05
                             {-in case where beta fitting fails-}
                                 else (d M.! "pval1") <0.05 
                                 {-&& (d M.! "fdr") < 0.1-}
                     ) (entriesByUpDown True xxs')
    ; putStrLn  (fname ++" , "++(show $ length xxs))
    ; exportTargets fname xxs
    ; tot_p <- exportAVG pval  xxs'
    ; tot_f <- exportAVG (\x->let d = secondaryData x in d M.! "fdr") xxs'
    ; tar_p <- exportAVG pval xxs
    ; tar_f <- exportAVG (\x->let d = secondaryData x in d M.! "fdr") xxs
    return [tot_p,tot_f,tar_p,tar_f]
    {-; exportAVG "../avgs/Targets_AVG_mean.tsv"  xxs'-}

routine :: FilePath -> FilePath -> IO()
routine root cur = do
    flag<-mvroot root cur;
    if flag then do
        filenames' <- getDirectoryContents "."
        let filenames = filter (\x -> "_cybert_result.tsv" `isInfixOf` x) filenames'
        collections <- mapM loadCybert' filenames
        putStrLn $ show filenames
        {-acquire cybert entries -}
        avgs <- P.mapM subroutine collections
        writeString "../avgs/Total_AVG_pval.tsv" (stringify "\n" (map (\x->x!!0) avgs))
        writeString "../avgs/Total_AVG_fdr.tsv" (stringify "\n" (map (\x->x!!1) avgs))
        writeString "../avgs/Targets_AVG_pval.tsv" (stringify "\n" (map (\x->x!!2) avgs))
        writeString "../avgs/Targets_AVG_fdr.tsv" (stringify "\n" (map (\x->x!!3) avgs))
        {-E.evaluate $ runEval $ do-}
            {-parMap (rpar.subroutine) collections-}
            {-parMap rseq parTasks-}
        {-use pval of 0.05 and fpr of 0.1 to filter-}
        putStr $ "\nHandled" ++ cur ++"\n"
        setCurrentDirectory root
    else do
        putStr "\nNothing is done\n"
        setCurrentDirectory root

main = do
{-first get stuff from the "runs" folder-}
     curdir <- getCurrentDirectory;
     mapM (routine curdir) targetDirs






stack implementaion of the cybert project



yul13
committed
Apr 18, 2016



stack implementaion of the cybert project


stack implementaion of the cybert project

yul13
committed
Apr 18, 2016


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


17


18


19


20


21


22


23


24


25


26


27


28


29


30


31


32


33


34


35


36


37


38


39


40


41


42


43


44


45


46


47


48


49


50


51


52


53


54


55


56


57


58


59


60


61


62


63


64


65


66


67


68


69


70


71


72


73


74


75


76


77


78


79


80


81


82


83


84


85


86


87


88


89


90


91


92


93


94


95


96


97


98


99


100


101


102


103


104


105


106


107


108


109


110


111


112


113


114


115


import Cybert
import Data.Maybe
import Data.List
import qualified Data.Map as M
import Control.Monad
import qualified Control.Exception as E
import System.Directory
import System.IO
import qualified Control.Monad.Parallel as P
{-import Data.Set(fromList, toList, union, empty)-}

cheader' = M.fromList [("Secondary_Data_FDR","fdr"), ("Secondary_Data_FPR","fpr"),("Secondary_Data_Pval1","pval1")]
cheader  = M.union (cheader') (M.adjust (\x->"accession") "probe" cybert_header)
loadCybert' = loadCybert cheader
targetDirs= ["NatData_runs","XlinkData_runs"]

{-utility fctns for writing to file -}
stringify sep xs = foldl1 (\x y ->x++sep++ y) xs

writeString fname content = do
    withFile fname WriteMode (\handle -> do
        hPutStr handle content)

mvroot :: FilePath -> FilePath -> IO Bool
mvroot root cur = do
   E.catch
    (
        do
         createDirectoryIfMissing True (cur ++"/ontargets")
         createDirectoryIfMissing True (cur ++"/scatters")
         createDirectoryIfMissing True (cur ++"/avgs")
         setCurrentDirectory  (cur++"/runs")
         return True
    )
    (\e-> do
        let err = show (e::E.IOException)
        putStr $ stringify "\n" ["Error Chaging to directory\n" , cur,
           "Err:", err]
        return False
    );

{-extractTargets :: [Cybert_entry] -> IO()-}

exportScatter fname xs = do
    let ss = map (\x-> let d = secondaryData x
                        in stringify "\t" [probe x, show (d M.! "pval1"),show (d M.! "fdr")]) xs
    (writeString ("../scatters/"++ fname)) (stringify "\n" ss)

exportTargets fname xs = do
    exportProbes xs ("../ontargets/" ++ fname)
    let ss = map (\x-> let d = secondaryData x
                        in stringify "\t"
                            [probe x, showEitherF (mean x), show (d M.! "pval1"),
                             show (d M.! "fpr"), show (d M.! "fdr")]) xs
    writeString ("../targetdetails/"++ fname) $ 
        stringify "\n" (["accession\tmean\tpvalue(one-tail)\tFPR\tFDR"]++ss)
exportAVG fn xs = do
    let xxs = map fn xs
    let avg = if length xs >=2 then (foldl1 (+) xxs) / (fromIntegral (length xxs))
               else 1.0
    let gname = if length xs == 0 then "Unknown"
                else fromMaybe "Unknown" (collection $ (xs !! 0))
    let ss = gname ++ "\t" ++ (show avg) ++ "\n"
    return ss
subroutine:: (Maybe [Cybert_entry]) -> IO ([String])
subroutine xs = do
    let xxs'= fromMaybe [] xs
    let fname = if length xxs' == 0 then "Nill.dummy"
                else fromMaybe "Nill.dummy" (collection $ xxs' !! 0)
    ; exportScatter fname xxs'
    {-; exportAVG "../avgs/Total_AVG_mean.tsv" mean xxs'-}
    let xxs = filter (\x -> let d = (secondaryData x)
                              in if (d M.! "fdr") < 0.0 then (pval x) < 0.05
                             {-in case where beta fitting fails-}
                                 else (d M.! "pval1") <0.05 
                                 {-&& (d M.! "fdr") < 0.1-}
                     ) (entriesByUpDown True xxs')
    ; putStrLn  (fname ++" , "++(show $ length xxs))
    ; exportTargets fname xxs
    ; tot_p <- exportAVG pval  xxs'
    ; tot_f <- exportAVG (\x->let d = secondaryData x in d M.! "fdr") xxs'
    ; tar_p <- exportAVG pval xxs
    ; tar_f <- exportAVG (\x->let d = secondaryData x in d M.! "fdr") xxs
    return [tot_p,tot_f,tar_p,tar_f]
    {-; exportAVG "../avgs/Targets_AVG_mean.tsv"  xxs'-}

routine :: FilePath -> FilePath -> IO()
routine root cur = do
    flag<-mvroot root cur;
    if flag then do
        filenames' <- getDirectoryContents "."
        let filenames = filter (\x -> "_cybert_result.tsv" `isInfixOf` x) filenames'
        collections <- mapM loadCybert' filenames
        putStrLn $ show filenames
        {-acquire cybert entries -}
        avgs <- P.mapM subroutine collections
        writeString "../avgs/Total_AVG_pval.tsv" (stringify "\n" (map (\x->x!!0) avgs))
        writeString "../avgs/Total_AVG_fdr.tsv" (stringify "\n" (map (\x->x!!1) avgs))
        writeString "../avgs/Targets_AVG_pval.tsv" (stringify "\n" (map (\x->x!!2) avgs))
        writeString "../avgs/Targets_AVG_fdr.tsv" (stringify "\n" (map (\x->x!!3) avgs))
        {-E.evaluate $ runEval $ do-}
            {-parMap (rpar.subroutine) collections-}
            {-parMap rseq parTasks-}
        {-use pval of 0.05 and fpr of 0.1 to filter-}
        putStr $ "\nHandled" ++ cur ++"\n"
        setCurrentDirectory root
    else do
        putStr "\nNothing is done\n"
        setCurrentDirectory root

main = do
{-first get stuff from the "runs" folder-}
     curdir <- getCurrentDirectory;
     mapM (routine curdir) targetDirs

import CybertimportCybertimport Data.MaybeimportData.Maybeimport Data.ListimportData.Listimport qualified Data.Map as MimportqualifiedData.MapasMimport Control.MonadimportControl.Monadimport qualified Control.Exception as EimportqualifiedControl.ExceptionasEimport System.DirectoryimportSystem.Directoryimport System.IOimportSystem.IOimport qualified Control.Monad.Parallel as PimportqualifiedControl.Monad.ParallelasP{-import Data.Set(fromList, toList, union, empty)-}{-import Data.Set(fromList, toList, union, empty)-}cheader' = M.fromList [("Secondary_Data_FDR","fdr"), ("Secondary_Data_FPR","fpr"),("Secondary_Data_Pval1","pval1")]cheader'=M.fromList[("Secondary_Data_FDR","fdr"),("Secondary_Data_FPR","fpr"),("Secondary_Data_Pval1","pval1")]cheader  = M.union (cheader') (M.adjust (\x->"accession") "probe" cybert_header)cheader=M.union(cheader')(M.adjust(\x->"accession")"probe"cybert_header)loadCybert' = loadCybert cheaderloadCybert'=loadCybertcheadertargetDirs= ["NatData_runs","XlinkData_runs"]targetDirs=["NatData_runs","XlinkData_runs"]{-utility fctns for writing to file -}{-utility fctns for writing to file -}stringify sep xs = foldl1 (\x y ->x++sep++ y) xsstringifysepxs=foldl1(\xy->x++sep++y)xswriteString fname content = dowriteStringfnamecontent=do    withFile fname WriteMode (\handle -> dowithFilefnameWriteMode(\handle->do        hPutStr handle content)hPutStrhandlecontent)mvroot :: FilePath -> FilePath -> IO Boolmvroot::FilePath->FilePath->IOBoolmvroot root cur = domvrootrootcur=do   E.catchE.catch    ((        dodo         createDirectoryIfMissing True (cur ++"/ontargets")createDirectoryIfMissingTrue(cur++"/ontargets")         createDirectoryIfMissing True (cur ++"/scatters")createDirectoryIfMissingTrue(cur++"/scatters")         createDirectoryIfMissing True (cur ++"/avgs")createDirectoryIfMissingTrue(cur++"/avgs")         setCurrentDirectory  (cur++"/runs")setCurrentDirectory(cur++"/runs")         return TruereturnTrue    ))    (\e-> do(\e->do        let err = show (e::E.IOException)leterr=show(e::E.IOException)        putStr $ stringify "\n" ["Error Chaging to directory\n" , cur,putStr$stringify"\n"["Error Chaging to directory\n",cur,           "Err:", err]"Err:",err]        return FalsereturnFalse    ););{-extractTargets :: [Cybert_entry] -> IO()-}{-extractTargets :: [Cybert_entry] -> IO()-}exportScatter fname xs = doexportScatterfnamexs=do    let ss = map (\x-> let d = secondaryData xletss=map(\x->letd=secondaryDatax                        in stringify "\t" [probe x, show (d M.! "pval1"),show (d M.! "fdr")]) xsinstringify"\t"[probex,show(dM.!"pval1"),show(dM.!"fdr")])xs    (writeString ("../scatters/"++ fname)) (stringify "\n" ss)(writeString("../scatters/"++fname))(stringify"\n"ss)exportTargets fname xs = doexportTargetsfnamexs=do    exportProbes xs ("../ontargets/" ++ fname)exportProbesxs("../ontargets/"++fname)    let ss = map (\x-> let d = secondaryData xletss=map(\x->letd=secondaryDatax                        in stringify "\t"instringify"\t"                            [probe x, showEitherF (mean x), show (d M.! "pval1"),[probex,showEitherF(meanx),show(dM.!"pval1"),                             show (d M.! "fpr"), show (d M.! "fdr")]) xsshow(dM.!"fpr"),show(dM.!"fdr")])xs    writeString ("../targetdetails/"++ fname) $ writeString("../targetdetails/"++fname)$        stringify "\n" (["accession\tmean\tpvalue(one-tail)\tFPR\tFDR"]++ss)stringify"\n"(["accession\tmean\tpvalue(one-tail)\tFPR\tFDR"]++ss)exportAVG fn xs = doexportAVGfnxs=do    let xxs = map fn xsletxxs=mapfnxs    let avg = if length xs >=2 then (foldl1 (+) xxs) / (fromIntegral (length xxs))letavg=iflengthxs>=2then(foldl1(+)xxs)/(fromIntegral(lengthxxs))               else 1.0else1.0    let gname = if length xs == 0 then "Unknown"letgname=iflengthxs==0then"Unknown"                else fromMaybe "Unknown" (collection $ (xs !! 0))elsefromMaybe"Unknown"(collection$(xs!!0))    let ss = gname ++ "\t" ++ (show avg) ++ "\n"letss=gname++"\t"++(showavg)++"\n"    return ssreturnsssubroutine:: (Maybe [Cybert_entry]) -> IO ([String])subroutine::(Maybe[Cybert_entry])->IO([String])subroutine xs = dosubroutinexs=do    let xxs'= fromMaybe [] xsletxxs'=fromMaybe[]xs    let fname = if length xxs' == 0 then "Nill.dummy"letfname=iflengthxxs'==0then"Nill.dummy"                else fromMaybe "Nill.dummy" (collection $ xxs' !! 0)elsefromMaybe"Nill.dummy"(collection$xxs'!!0)    ; exportScatter fname xxs';exportScatterfnamexxs'    {-; exportAVG "../avgs/Total_AVG_mean.tsv" mean xxs'-}{-; exportAVG "../avgs/Total_AVG_mean.tsv" mean xxs'-}    let xxs = filter (\x -> let d = (secondaryData x)letxxs=filter(\x->letd=(secondaryDatax)                              in if (d M.! "fdr") < 0.0 then (pval x) < 0.05inif(dM.!"fdr")<0.0then(pvalx)<0.05                             {-in case where beta fitting fails-}{-in case where beta fitting fails-}                                 else (d M.! "pval1") <0.05 else(dM.!"pval1")<0.05                                 {-&& (d M.! "fdr") < 0.1-}{-&& (d M.! "fdr") < 0.1-}                     ) (entriesByUpDown True xxs'))(entriesByUpDownTruexxs')    ; putStrLn  (fname ++" , "++(show $ length xxs));putStrLn(fname++" , "++(show$lengthxxs))    ; exportTargets fname xxs;exportTargetsfnamexxs    ; tot_p <- exportAVG pval  xxs';tot_p<-exportAVGpvalxxs'    ; tot_f <- exportAVG (\x->let d = secondaryData x in d M.! "fdr") xxs';tot_f<-exportAVG(\x->letd=secondaryDataxindM.!"fdr")xxs'    ; tar_p <- exportAVG pval xxs;tar_p<-exportAVGpvalxxs    ; tar_f <- exportAVG (\x->let d = secondaryData x in d M.! "fdr") xxs;tar_f<-exportAVG(\x->letd=secondaryDataxindM.!"fdr")xxs    return [tot_p,tot_f,tar_p,tar_f]return[tot_p,tot_f,tar_p,tar_f]    {-; exportAVG "../avgs/Targets_AVG_mean.tsv"  xxs'-}{-; exportAVG "../avgs/Targets_AVG_mean.tsv"  xxs'-}routine :: FilePath -> FilePath -> IO()routine::FilePath->FilePath->IO()routine root cur = doroutinerootcur=do    flag<-mvroot root cur;flag<-mvrootrootcur;    if flag then doifflagthendo        filenames' <- getDirectoryContents "."filenames'<-getDirectoryContents"."        let filenames = filter (\x -> "_cybert_result.tsv" `isInfixOf` x) filenames'letfilenames=filter(\x->"_cybert_result.tsv"`isInfixOf`x)filenames'        collections <- mapM loadCybert' filenamescollections<-mapMloadCybert'filenames        putStrLn $ show filenamesputStrLn$showfilenames        {-acquire cybert entries -}{-acquire cybert entries -}        avgs <- P.mapM subroutine collectionsavgs<-P.mapMsubroutinecollections        writeString "../avgs/Total_AVG_pval.tsv" (stringify "\n" (map (\x->x!!0) avgs))writeString"../avgs/Total_AVG_pval.tsv"(stringify"\n"(map(\x->x!!0)avgs))        writeString "../avgs/Total_AVG_fdr.tsv" (stringify "\n" (map (\x->x!!1) avgs))writeString"../avgs/Total_AVG_fdr.tsv"(stringify"\n"(map(\x->x!!1)avgs))        writeString "../avgs/Targets_AVG_pval.tsv" (stringify "\n" (map (\x->x!!2) avgs))writeString"../avgs/Targets_AVG_pval.tsv"(stringify"\n"(map(\x->x!!2)avgs))        writeString "../avgs/Targets_AVG_fdr.tsv" (stringify "\n" (map (\x->x!!3) avgs))writeString"../avgs/Targets_AVG_fdr.tsv"(stringify"\n"(map(\x->x!!3)avgs))        {-E.evaluate $ runEval $ do-}{-E.evaluate $ runEval $ do-}            {-parMap (rpar.subroutine) collections-}{-parMap (rpar.subroutine) collections-}            {-parMap rseq parTasks-}{-parMap rseq parTasks-}        {-use pval of 0.05 and fpr of 0.1 to filter-}{-use pval of 0.05 and fpr of 0.1 to filter-}        putStr $ "\nHandled" ++ cur ++"\n"putStr$"\nHandled"++cur++"\n"        setCurrentDirectory rootsetCurrentDirectoryroot    else doelsedo        putStr "\nNothing is done\n"putStr"\nNothing is done\n"        setCurrentDirectory rootsetCurrentDirectoryrootmain = domain=do{-first get stuff from the "runs" folder-}{-first get stuff from the "runs" folder-}     curdir <- getCurrentDirectory;curdir<-getCurrentDirectory;     mapM (routine curdir) targetDirsmapM(routinecurdir)targetDirs





