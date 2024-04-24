



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

53255057c9a07fb753863d2001f09ebe37ff8742

















53255057c9a07fb753863d2001f09ebe37ff8742


Switch branch/tag










haskell


cybert


Cybert.hs



Find file
Normal viewHistoryPermalink






Cybert.hs



8.98 KB









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




7




8




9




10




11




12




13




14




15




--a simple loader for cybert data
--generates cybert reports and tables
--
--has the following:
--1, a polymorphic and flexible data structure indexed by multiple
--keys.
--2, ability to do set operation, selection filtering and mapping 
--on entries
--3, IO for both human readable format and message passing to other python
--code
--
{-module definitions -}
module Cybert
(
    Cybert_entry(NA, Cybert,probe,genesym,









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






16




17




        sample, collection, mean, bf, bh, sds, 
        pval,ratio,secondaryRefs,secondaryData),









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






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




    cybert_entry,
    showCybertEntries,
    entriesByFold,
    entriesByPval,
    entriesBySym,
    entriesByUpDown,
    entriesBySecondaryRef,
    cybertToSet,
    loadCybert,
    exportCybert,
    exportGeneSyms,
    exportProbes
) where
{-end module definitions -}
import Data.Bits
import Data.List









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






34




35




import Data.Maybe
import Data.Char









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






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




import System.IO
import System.IO.Error
import qualified Data.Set as S
import qualified Data.Map as M
import qualified Data.ByteString.Char8 as B

{-basic data types-}
data Cybert_entry = NA | Cybert {
    probe :: String ,
    genesym :: Maybe String ,
    sample :: [String] ,
    collection :: Maybe String , --which dataset
    mean :: Either Float [Float] , --multiple means









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






49




    sds :: Either Float [Float],









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






50




    pval :: Float, --the pairwise or ANOVA pval









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






51




52




    bf :: Float,
    bh :: Float,









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






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




    ratio :: Either Float [Float], --pairwise ratio or numerous ratios
    secondaryRefs :: M.Map String String , --optional secondary refs
    secondaryData :: M.Map String Float , --optional secondary data
    raw :: B.ByteString
}
cybert_entry = Cybert{
    -- default constructor
    probe = "NONE",
    genesym = Nothing,
    sample = [],
    collection = Nothing,
    mean = Right [],









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






65




    sds = Right [],









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






66




    pval = -1,









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






67




68




    bf = -1,
    bh = -1,









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






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




    ratio = Left (-1),
    secondaryRefs = M.fromList [("","")],
    secondaryData = M.fromList [("",0)],
    raw = B.empty
}

hasher :: String -> Int
hasher = foldl (\h c -> 33*h `xor` fromEnum c) 5381
cybertHash :: Cybert_entry -> Int
cybertHash NA = 0
cybertHash Cybert{probe=p, collection=(Nothing)} = hasher $ "NONE"++p
cybertHash Cybert{probe=p, collection=(Just s)} = hasher $ s++p

instance Eq Cybert_entry where
    a == b = (cybertHash a) == (cybertHash b)
instance Ord Cybert_entry where
    compare a b = compare  (cybertHash a)  (cybertHash b)
--hashing
{-end basic data types-}
{-format and show routines-}
showJustS::Maybe String -> String
showJustS Nothing = "N/A"
showJustS (Just x) = x

showEitherF::(Either Float [Float]) -> String
showEitherF (Left x) = show x
showEitherF (Right xxs@(x:[])) = show x
showEitherF (Right xxs@(x:xs)) = (show x) ++"\t"++ (showEitherF (Right xs))

showCybertEntries::[Cybert_entry]->String
showCybertEntries (x:[])= show x
showCybertEntries (x:xs)= (show x) ++ "\n" ++ (showCybertEntries xs)

instance Show Cybert_entry where
    showsPrec _ a s = show a ++ s
    show NA = "NA"
    show Cybert {probe=p, genesym=sym, sample=ss, collection=col, mean=m,









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






106




107




    pval=pv, ratio=r, secondaryRefs=_, secondaryData=_,raw=_} = p++"\t"++
        (showJustS sym)++"\t"++(showJustS col) ++"\t" ++(showEitherF m)++"\t"++(show pv)









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






108




109




110




111




112




113




114




115




116




117




118




119




120




121




122




123




124




125




126




127




128




129




130




131




132




        -- for each sample
{-end format and show routines-}
{-set operation and filtering routines-}
entriesBySym :: [Cybert_entry] -> String -> [Cybert_entry]
entriesBySym xs sym = filter (\x -> genesym x == (Just sym)) xs

entryByProbe :: [Cybert_entry] -> String -> Cybert_entry
entryByProbe xs p = head $ filter (\x -> probe x == p) xs

entriesBySecondaryRef :: [Cybert_entry] -> String -> String -> [Cybert_entry]
entriesBySecondaryRef xs tref ref = filter (\x -> (secondaryRefs x) M.! tref == ref) xs
--lookup
entriesByFold :: [Cybert_entry] -> Float -> [Cybert_entry]
entriesByFold xs threshold = filter (\x ->pred $ mean x) xs where
                            pred (Left a) = False
                            pred (Right b)= if length b < 2 then False
                                            else b!!1-b!!0 > threshold

entriesByPval :: [Cybert_entry] -> Float -> [Cybert_entry]
entriesByPval xs threshold = filter (\x -> pval x < threshold) xs

entriesByUpDown :: [Cybert_entry] -> Bool -> [Cybert_entry]
entriesByUpDown xs val = filter (\x -> pred $ mean x) xs where
                          pred (Left a) = False
                          pred (Right b) = if length b < 2 then False









hmm


 

 


darlliu
committed
May 24, 2013






133




134




                                           else let bigger = (b!!1-b!!0>0)
                                                in bigger==val









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






135




136




137




138




139




140




141




--filtering

cybertToSet :: [Cybert_entry] -> S.Set Cybert_entry
cybertToSet xs = S.fromList xs
--set operations
{-end set operation and filtering routines-}
{-IO routines-}









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






142




143




144




145




146




buildHeaderPrec :: B.ByteString -> [(String , B.ByteString)]
buildHeaderPrec s = let ss = B.split '\t' s
                in map (\x -> (stripQuote $ B.unpack  x, x)) ss where
                   stripQuote ('\"':xs) = map toLower $ take ((length xs) - 1) xs
                   stripQuote xs = map toLower xs









hmm


 

 


darlliu
committed
May 24, 2013






147














should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






148




149




150




151




152




153




154




155




buildHeader :: B.ByteString -> (M.Map String (Maybe Int) )
buildHeader s = let ss = buildHeaderPrec s; sl = B.split '\t' s
                in M.fromList $ map  (\x -> (fst x , (snd x) `elemIndex` sl)) ss
maybeGet :: [B.ByteString] -> Maybe Int -> B.ByteString
[] `maybeGet` _ = B.pack ""
x `maybeGet` Nothing = B.pack ""
x `maybeGet` (Just s)= if length x > s then x !! s
                        else B.pack ""









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






156














should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






157




158




159




160




161




162




163




164




165




166




167




168




169




170




171




getText :: (M.Map String (Maybe Int))-> [B.ByteString] -> String -> Maybe String
getText header ss id = if id `M.notMember` header
                         then Nothing
                         else let idx = header M.! id
                              in Just (B.unpack $ ss `maybeGet` idx)
getNum :: (M.Map String (Maybe Int))-> [B.ByteString] -> String -> Float
getNum header ss id = if id `M.notMember` header
                        then -1 :: Float
                        else let idx = header M.! id
                             in read (B.unpack $ ss `maybeGet` idx) :: Float
getNums :: (M.Map String (Maybe Int))-> [B.ByteString] -> [String] -> Either Float [Float]
getNums header ss ids = let nums = map (getNum header ss) ids
                         in if length nums == 1 then Left (nums !! 0)
                              else Right $ filter (not . (== -1)) nums
lineToCybert :: (M.Map String (Maybe Int))-> [Cybert_entry] -> B.ByteString -> [Cybert_entry]









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






172




--take a header and an accumulator, then read the line and append the cybert entry









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






173




174




175




176




177




178




179




180




181




182




183




184




185




186




187




188




189




190




lineToCybert header xs line = xs ++ readLine line where
    readLine s = let ss = B.split '\t' s 
                  in if length ss /= M.size header then []
                     else let cybt= cybert_entry {
                         probe = B.unpack $ ss `maybeGet` (header M.! "probe_id"),
                         --this is a must
                         genesym = getText header ss "gene_sym",
                         --this is of maybe type
                         pval = getNum header ss "pval",
                         bf = getNum header ss "bonferroni",
                         bh = getNum header ss "bh",
                         --these are -1 defaulted
                         mean = getNums header ss $ map ( "mean" ++ ) ["c","e","1","2","3","4","5","6","7"],
                         sds = getNums header ss $ map ( "std" ++ ) ["c","e","1","2","3","4","5","6","7"]
                         --these are one or many
                         , raw = s
                         --raw info
                         } in [cybt]









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






191




192




193




194




195




196




197





loadCybert :: String -> IO (Maybe [Cybert_entry])
loadCybert fname = catch
    (withFile fname ReadMode (\handle -> do
        contents <- B.hGetContents handle
        let mylines =  B.split '\n' contents
        if length mylines <= 1 then return Nothing









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






198




199




200




201




202




203




        else let header = buildHeader (head mylines);
                 output = (Just (foldl (lineToCybert header) [] (drop 1 mylines)))
             in if output == (Just []) then return Nothing
                  else do 
                    putStrLn "Done parsing"
                    return output









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






204




205




    ))
    (\err -> do









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






206




            if isEOFError err









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






207




208




209




210




211




212




213




214




215




216




217




218




219




220




221




222




223




224




            then do
                 putStrLn "File is empty or truncated."
                 return Nothing
            else do
                 putStrLn $ "Unexpected Error at opening file: "++ (show err)
                 return Nothing
    )
-- Loads a cybert table from fname
exportCybert :: [Cybert_entry] -> String -> IO()
exportCybert xs fname = do
    withFile fname WriteMode (\handle -> do
            let contents = showCybertEntries xs
            hPutStr handle contents
            )

exportGeneSyms :: [Cybert_entry] -> String -> IO()
exportGeneSyms xs fname = do
    withFile fname WriteMode (\handle -> do









hmm


 

 


darlliu
committed
May 23, 2013






225




            let contents = foldl1 (\acc x -> acc++"\n"++x) (map showJustS (map genesym xs))









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






226




227




228




229




230




231




            hPutStr handle contents
            )

exportProbes :: [Cybert_entry] -> String -> IO()
exportProbes xs fname = do
    withFile fname WriteMode (\handle -> do









hmm


 

 


darlliu
committed
May 23, 2013






232




            let contents = foldl1 (\acc x -> acc++"\n"++x) (map probe xs)









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






233




234




235




236




237




            hPutStr handle contents
            )

{-exportRef :: [Cybert_entry] -> String -> String -> IO()-}
{-end IO routines-}












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

53255057c9a07fb753863d2001f09ebe37ff8742

















53255057c9a07fb753863d2001f09ebe37ff8742


Switch branch/tag










haskell


cybert


Cybert.hs



Find file
Normal viewHistoryPermalink






Cybert.hs



8.98 KB









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




7




8




9




10




11




12




13




14




15




--a simple loader for cybert data
--generates cybert reports and tables
--
--has the following:
--1, a polymorphic and flexible data structure indexed by multiple
--keys.
--2, ability to do set operation, selection filtering and mapping 
--on entries
--3, IO for both human readable format and message passing to other python
--code
--
{-module definitions -}
module Cybert
(
    Cybert_entry(NA, Cybert,probe,genesym,









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






16




17




        sample, collection, mean, bf, bh, sds, 
        pval,ratio,secondaryRefs,secondaryData),









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






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




    cybert_entry,
    showCybertEntries,
    entriesByFold,
    entriesByPval,
    entriesBySym,
    entriesByUpDown,
    entriesBySecondaryRef,
    cybertToSet,
    loadCybert,
    exportCybert,
    exportGeneSyms,
    exportProbes
) where
{-end module definitions -}
import Data.Bits
import Data.List









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






34




35




import Data.Maybe
import Data.Char









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






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




import System.IO
import System.IO.Error
import qualified Data.Set as S
import qualified Data.Map as M
import qualified Data.ByteString.Char8 as B

{-basic data types-}
data Cybert_entry = NA | Cybert {
    probe :: String ,
    genesym :: Maybe String ,
    sample :: [String] ,
    collection :: Maybe String , --which dataset
    mean :: Either Float [Float] , --multiple means









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






49




    sds :: Either Float [Float],









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






50




    pval :: Float, --the pairwise or ANOVA pval









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






51




52




    bf :: Float,
    bh :: Float,









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






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




    ratio :: Either Float [Float], --pairwise ratio or numerous ratios
    secondaryRefs :: M.Map String String , --optional secondary refs
    secondaryData :: M.Map String Float , --optional secondary data
    raw :: B.ByteString
}
cybert_entry = Cybert{
    -- default constructor
    probe = "NONE",
    genesym = Nothing,
    sample = [],
    collection = Nothing,
    mean = Right [],









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






65




    sds = Right [],









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






66




    pval = -1,









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






67




68




    bf = -1,
    bh = -1,









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






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




    ratio = Left (-1),
    secondaryRefs = M.fromList [("","")],
    secondaryData = M.fromList [("",0)],
    raw = B.empty
}

hasher :: String -> Int
hasher = foldl (\h c -> 33*h `xor` fromEnum c) 5381
cybertHash :: Cybert_entry -> Int
cybertHash NA = 0
cybertHash Cybert{probe=p, collection=(Nothing)} = hasher $ "NONE"++p
cybertHash Cybert{probe=p, collection=(Just s)} = hasher $ s++p

instance Eq Cybert_entry where
    a == b = (cybertHash a) == (cybertHash b)
instance Ord Cybert_entry where
    compare a b = compare  (cybertHash a)  (cybertHash b)
--hashing
{-end basic data types-}
{-format and show routines-}
showJustS::Maybe String -> String
showJustS Nothing = "N/A"
showJustS (Just x) = x

showEitherF::(Either Float [Float]) -> String
showEitherF (Left x) = show x
showEitherF (Right xxs@(x:[])) = show x
showEitherF (Right xxs@(x:xs)) = (show x) ++"\t"++ (showEitherF (Right xs))

showCybertEntries::[Cybert_entry]->String
showCybertEntries (x:[])= show x
showCybertEntries (x:xs)= (show x) ++ "\n" ++ (showCybertEntries xs)

instance Show Cybert_entry where
    showsPrec _ a s = show a ++ s
    show NA = "NA"
    show Cybert {probe=p, genesym=sym, sample=ss, collection=col, mean=m,









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






106




107




    pval=pv, ratio=r, secondaryRefs=_, secondaryData=_,raw=_} = p++"\t"++
        (showJustS sym)++"\t"++(showJustS col) ++"\t" ++(showEitherF m)++"\t"++(show pv)









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






108




109




110




111




112




113




114




115




116




117




118




119




120




121




122




123




124




125




126




127




128




129




130




131




132




        -- for each sample
{-end format and show routines-}
{-set operation and filtering routines-}
entriesBySym :: [Cybert_entry] -> String -> [Cybert_entry]
entriesBySym xs sym = filter (\x -> genesym x == (Just sym)) xs

entryByProbe :: [Cybert_entry] -> String -> Cybert_entry
entryByProbe xs p = head $ filter (\x -> probe x == p) xs

entriesBySecondaryRef :: [Cybert_entry] -> String -> String -> [Cybert_entry]
entriesBySecondaryRef xs tref ref = filter (\x -> (secondaryRefs x) M.! tref == ref) xs
--lookup
entriesByFold :: [Cybert_entry] -> Float -> [Cybert_entry]
entriesByFold xs threshold = filter (\x ->pred $ mean x) xs where
                            pred (Left a) = False
                            pred (Right b)= if length b < 2 then False
                                            else b!!1-b!!0 > threshold

entriesByPval :: [Cybert_entry] -> Float -> [Cybert_entry]
entriesByPval xs threshold = filter (\x -> pval x < threshold) xs

entriesByUpDown :: [Cybert_entry] -> Bool -> [Cybert_entry]
entriesByUpDown xs val = filter (\x -> pred $ mean x) xs where
                          pred (Left a) = False
                          pred (Right b) = if length b < 2 then False









hmm


 

 


darlliu
committed
May 24, 2013






133




134




                                           else let bigger = (b!!1-b!!0>0)
                                                in bigger==val









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






135




136




137




138




139




140




141




--filtering

cybertToSet :: [Cybert_entry] -> S.Set Cybert_entry
cybertToSet xs = S.fromList xs
--set operations
{-end set operation and filtering routines-}
{-IO routines-}









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






142




143




144




145




146




buildHeaderPrec :: B.ByteString -> [(String , B.ByteString)]
buildHeaderPrec s = let ss = B.split '\t' s
                in map (\x -> (stripQuote $ B.unpack  x, x)) ss where
                   stripQuote ('\"':xs) = map toLower $ take ((length xs) - 1) xs
                   stripQuote xs = map toLower xs









hmm


 

 


darlliu
committed
May 24, 2013






147














should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






148




149




150




151




152




153




154




155




buildHeader :: B.ByteString -> (M.Map String (Maybe Int) )
buildHeader s = let ss = buildHeaderPrec s; sl = B.split '\t' s
                in M.fromList $ map  (\x -> (fst x , (snd x) `elemIndex` sl)) ss
maybeGet :: [B.ByteString] -> Maybe Int -> B.ByteString
[] `maybeGet` _ = B.pack ""
x `maybeGet` Nothing = B.pack ""
x `maybeGet` (Just s)= if length x > s then x !! s
                        else B.pack ""









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






156














should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






157




158




159




160




161




162




163




164




165




166




167




168




169




170




171




getText :: (M.Map String (Maybe Int))-> [B.ByteString] -> String -> Maybe String
getText header ss id = if id `M.notMember` header
                         then Nothing
                         else let idx = header M.! id
                              in Just (B.unpack $ ss `maybeGet` idx)
getNum :: (M.Map String (Maybe Int))-> [B.ByteString] -> String -> Float
getNum header ss id = if id `M.notMember` header
                        then -1 :: Float
                        else let idx = header M.! id
                             in read (B.unpack $ ss `maybeGet` idx) :: Float
getNums :: (M.Map String (Maybe Int))-> [B.ByteString] -> [String] -> Either Float [Float]
getNums header ss ids = let nums = map (getNum header ss) ids
                         in if length nums == 1 then Left (nums !! 0)
                              else Right $ filter (not . (== -1)) nums
lineToCybert :: (M.Map String (Maybe Int))-> [Cybert_entry] -> B.ByteString -> [Cybert_entry]









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






172




--take a header and an accumulator, then read the line and append the cybert entry









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






173




174




175




176




177




178




179




180




181




182




183




184




185




186




187




188




189




190




lineToCybert header xs line = xs ++ readLine line where
    readLine s = let ss = B.split '\t' s 
                  in if length ss /= M.size header then []
                     else let cybt= cybert_entry {
                         probe = B.unpack $ ss `maybeGet` (header M.! "probe_id"),
                         --this is a must
                         genesym = getText header ss "gene_sym",
                         --this is of maybe type
                         pval = getNum header ss "pval",
                         bf = getNum header ss "bonferroni",
                         bh = getNum header ss "bh",
                         --these are -1 defaulted
                         mean = getNums header ss $ map ( "mean" ++ ) ["c","e","1","2","3","4","5","6","7"],
                         sds = getNums header ss $ map ( "std" ++ ) ["c","e","1","2","3","4","5","6","7"]
                         --these are one or many
                         , raw = s
                         --raw info
                         } in [cybt]









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






191




192




193




194




195




196




197





loadCybert :: String -> IO (Maybe [Cybert_entry])
loadCybert fname = catch
    (withFile fname ReadMode (\handle -> do
        contents <- B.hGetContents handle
        let mylines =  B.split '\n' contents
        if length mylines <= 1 then return Nothing









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






198




199




200




201




202




203




        else let header = buildHeader (head mylines);
                 output = (Just (foldl (lineToCybert header) [] (drop 1 mylines)))
             in if output == (Just []) then return Nothing
                  else do 
                    putStrLn "Done parsing"
                    return output









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






204




205




    ))
    (\err -> do









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






206




            if isEOFError err









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






207




208




209




210




211




212




213




214




215




216




217




218




219




220




221




222




223




224




            then do
                 putStrLn "File is empty or truncated."
                 return Nothing
            else do
                 putStrLn $ "Unexpected Error at opening file: "++ (show err)
                 return Nothing
    )
-- Loads a cybert table from fname
exportCybert :: [Cybert_entry] -> String -> IO()
exportCybert xs fname = do
    withFile fname WriteMode (\handle -> do
            let contents = showCybertEntries xs
            hPutStr handle contents
            )

exportGeneSyms :: [Cybert_entry] -> String -> IO()
exportGeneSyms xs fname = do
    withFile fname WriteMode (\handle -> do









hmm


 

 


darlliu
committed
May 23, 2013






225




            let contents = foldl1 (\acc x -> acc++"\n"++x) (map showJustS (map genesym xs))









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






226




227




228




229




230




231




            hPutStr handle contents
            )

exportProbes :: [Cybert_entry] -> String -> IO()
exportProbes xs fname = do
    withFile fname WriteMode (\handle -> do









hmm


 

 


darlliu
committed
May 23, 2013






232




            let contents = foldl1 (\acc x -> acc++"\n"++x) (map probe xs)









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






233




234




235




236




237




            hPutStr handle contents
            )

{-exportRef :: [Cybert_entry] -> String -> String -> IO()-}
{-end IO routines-}











Open sidebar



Yu Liu haskell

53255057c9a07fb753863d2001f09ebe37ff8742







Open sidebar



Yu Liu haskell

53255057c9a07fb753863d2001f09ebe37ff8742




Open sidebar

Yu Liu haskell

53255057c9a07fb753863d2001f09ebe37ff8742


Yu Liuhaskellhaskell
53255057c9a07fb753863d2001f09ebe37ff8742










53255057c9a07fb753863d2001f09ebe37ff8742


Switch branch/tag










haskell


cybert


Cybert.hs



Find file
Normal viewHistoryPermalink






Cybert.hs



8.98 KB









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




7




8




9




10




11




12




13




14




15




--a simple loader for cybert data
--generates cybert reports and tables
--
--has the following:
--1, a polymorphic and flexible data structure indexed by multiple
--keys.
--2, ability to do set operation, selection filtering and mapping 
--on entries
--3, IO for both human readable format and message passing to other python
--code
--
{-module definitions -}
module Cybert
(
    Cybert_entry(NA, Cybert,probe,genesym,









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






16




17




        sample, collection, mean, bf, bh, sds, 
        pval,ratio,secondaryRefs,secondaryData),









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






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




    cybert_entry,
    showCybertEntries,
    entriesByFold,
    entriesByPval,
    entriesBySym,
    entriesByUpDown,
    entriesBySecondaryRef,
    cybertToSet,
    loadCybert,
    exportCybert,
    exportGeneSyms,
    exportProbes
) where
{-end module definitions -}
import Data.Bits
import Data.List









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






34




35




import Data.Maybe
import Data.Char









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






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




import System.IO
import System.IO.Error
import qualified Data.Set as S
import qualified Data.Map as M
import qualified Data.ByteString.Char8 as B

{-basic data types-}
data Cybert_entry = NA | Cybert {
    probe :: String ,
    genesym :: Maybe String ,
    sample :: [String] ,
    collection :: Maybe String , --which dataset
    mean :: Either Float [Float] , --multiple means









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






49




    sds :: Either Float [Float],









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






50




    pval :: Float, --the pairwise or ANOVA pval









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






51




52




    bf :: Float,
    bh :: Float,









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






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




    ratio :: Either Float [Float], --pairwise ratio or numerous ratios
    secondaryRefs :: M.Map String String , --optional secondary refs
    secondaryData :: M.Map String Float , --optional secondary data
    raw :: B.ByteString
}
cybert_entry = Cybert{
    -- default constructor
    probe = "NONE",
    genesym = Nothing,
    sample = [],
    collection = Nothing,
    mean = Right [],









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






65




    sds = Right [],









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






66




    pval = -1,









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






67




68




    bf = -1,
    bh = -1,









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






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




    ratio = Left (-1),
    secondaryRefs = M.fromList [("","")],
    secondaryData = M.fromList [("",0)],
    raw = B.empty
}

hasher :: String -> Int
hasher = foldl (\h c -> 33*h `xor` fromEnum c) 5381
cybertHash :: Cybert_entry -> Int
cybertHash NA = 0
cybertHash Cybert{probe=p, collection=(Nothing)} = hasher $ "NONE"++p
cybertHash Cybert{probe=p, collection=(Just s)} = hasher $ s++p

instance Eq Cybert_entry where
    a == b = (cybertHash a) == (cybertHash b)
instance Ord Cybert_entry where
    compare a b = compare  (cybertHash a)  (cybertHash b)
--hashing
{-end basic data types-}
{-format and show routines-}
showJustS::Maybe String -> String
showJustS Nothing = "N/A"
showJustS (Just x) = x

showEitherF::(Either Float [Float]) -> String
showEitherF (Left x) = show x
showEitherF (Right xxs@(x:[])) = show x
showEitherF (Right xxs@(x:xs)) = (show x) ++"\t"++ (showEitherF (Right xs))

showCybertEntries::[Cybert_entry]->String
showCybertEntries (x:[])= show x
showCybertEntries (x:xs)= (show x) ++ "\n" ++ (showCybertEntries xs)

instance Show Cybert_entry where
    showsPrec _ a s = show a ++ s
    show NA = "NA"
    show Cybert {probe=p, genesym=sym, sample=ss, collection=col, mean=m,









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






106




107




    pval=pv, ratio=r, secondaryRefs=_, secondaryData=_,raw=_} = p++"\t"++
        (showJustS sym)++"\t"++(showJustS col) ++"\t" ++(showEitherF m)++"\t"++(show pv)









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






108




109




110




111




112




113




114




115




116




117




118




119




120




121




122




123




124




125




126




127




128




129




130




131




132




        -- for each sample
{-end format and show routines-}
{-set operation and filtering routines-}
entriesBySym :: [Cybert_entry] -> String -> [Cybert_entry]
entriesBySym xs sym = filter (\x -> genesym x == (Just sym)) xs

entryByProbe :: [Cybert_entry] -> String -> Cybert_entry
entryByProbe xs p = head $ filter (\x -> probe x == p) xs

entriesBySecondaryRef :: [Cybert_entry] -> String -> String -> [Cybert_entry]
entriesBySecondaryRef xs tref ref = filter (\x -> (secondaryRefs x) M.! tref == ref) xs
--lookup
entriesByFold :: [Cybert_entry] -> Float -> [Cybert_entry]
entriesByFold xs threshold = filter (\x ->pred $ mean x) xs where
                            pred (Left a) = False
                            pred (Right b)= if length b < 2 then False
                                            else b!!1-b!!0 > threshold

entriesByPval :: [Cybert_entry] -> Float -> [Cybert_entry]
entriesByPval xs threshold = filter (\x -> pval x < threshold) xs

entriesByUpDown :: [Cybert_entry] -> Bool -> [Cybert_entry]
entriesByUpDown xs val = filter (\x -> pred $ mean x) xs where
                          pred (Left a) = False
                          pred (Right b) = if length b < 2 then False









hmm


 

 


darlliu
committed
May 24, 2013






133




134




                                           else let bigger = (b!!1-b!!0>0)
                                                in bigger==val









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






135




136




137




138




139




140




141




--filtering

cybertToSet :: [Cybert_entry] -> S.Set Cybert_entry
cybertToSet xs = S.fromList xs
--set operations
{-end set operation and filtering routines-}
{-IO routines-}









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






142




143




144




145




146




buildHeaderPrec :: B.ByteString -> [(String , B.ByteString)]
buildHeaderPrec s = let ss = B.split '\t' s
                in map (\x -> (stripQuote $ B.unpack  x, x)) ss where
                   stripQuote ('\"':xs) = map toLower $ take ((length xs) - 1) xs
                   stripQuote xs = map toLower xs









hmm


 

 


darlliu
committed
May 24, 2013






147














should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






148




149




150




151




152




153




154




155




buildHeader :: B.ByteString -> (M.Map String (Maybe Int) )
buildHeader s = let ss = buildHeaderPrec s; sl = B.split '\t' s
                in M.fromList $ map  (\x -> (fst x , (snd x) `elemIndex` sl)) ss
maybeGet :: [B.ByteString] -> Maybe Int -> B.ByteString
[] `maybeGet` _ = B.pack ""
x `maybeGet` Nothing = B.pack ""
x `maybeGet` (Just s)= if length x > s then x !! s
                        else B.pack ""









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






156














should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






157




158




159




160




161




162




163




164




165




166




167




168




169




170




171




getText :: (M.Map String (Maybe Int))-> [B.ByteString] -> String -> Maybe String
getText header ss id = if id `M.notMember` header
                         then Nothing
                         else let idx = header M.! id
                              in Just (B.unpack $ ss `maybeGet` idx)
getNum :: (M.Map String (Maybe Int))-> [B.ByteString] -> String -> Float
getNum header ss id = if id `M.notMember` header
                        then -1 :: Float
                        else let idx = header M.! id
                             in read (B.unpack $ ss `maybeGet` idx) :: Float
getNums :: (M.Map String (Maybe Int))-> [B.ByteString] -> [String] -> Either Float [Float]
getNums header ss ids = let nums = map (getNum header ss) ids
                         in if length nums == 1 then Left (nums !! 0)
                              else Right $ filter (not . (== -1)) nums
lineToCybert :: (M.Map String (Maybe Int))-> [Cybert_entry] -> B.ByteString -> [Cybert_entry]









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






172




--take a header and an accumulator, then read the line and append the cybert entry









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






173




174




175




176




177




178




179




180




181




182




183




184




185




186




187




188




189




190




lineToCybert header xs line = xs ++ readLine line where
    readLine s = let ss = B.split '\t' s 
                  in if length ss /= M.size header then []
                     else let cybt= cybert_entry {
                         probe = B.unpack $ ss `maybeGet` (header M.! "probe_id"),
                         --this is a must
                         genesym = getText header ss "gene_sym",
                         --this is of maybe type
                         pval = getNum header ss "pval",
                         bf = getNum header ss "bonferroni",
                         bh = getNum header ss "bh",
                         --these are -1 defaulted
                         mean = getNums header ss $ map ( "mean" ++ ) ["c","e","1","2","3","4","5","6","7"],
                         sds = getNums header ss $ map ( "std" ++ ) ["c","e","1","2","3","4","5","6","7"]
                         --these are one or many
                         , raw = s
                         --raw info
                         } in [cybt]









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






191




192




193




194




195




196




197





loadCybert :: String -> IO (Maybe [Cybert_entry])
loadCybert fname = catch
    (withFile fname ReadMode (\handle -> do
        contents <- B.hGetContents handle
        let mylines =  B.split '\n' contents
        if length mylines <= 1 then return Nothing









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






198




199




200




201




202




203




        else let header = buildHeader (head mylines);
                 output = (Just (foldl (lineToCybert header) [] (drop 1 mylines)))
             in if output == (Just []) then return Nothing
                  else do 
                    putStrLn "Done parsing"
                    return output









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






204




205




    ))
    (\err -> do









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






206




            if isEOFError err









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






207




208




209




210




211




212




213




214




215




216




217




218




219




220




221




222




223




224




            then do
                 putStrLn "File is empty or truncated."
                 return Nothing
            else do
                 putStrLn $ "Unexpected Error at opening file: "++ (show err)
                 return Nothing
    )
-- Loads a cybert table from fname
exportCybert :: [Cybert_entry] -> String -> IO()
exportCybert xs fname = do
    withFile fname WriteMode (\handle -> do
            let contents = showCybertEntries xs
            hPutStr handle contents
            )

exportGeneSyms :: [Cybert_entry] -> String -> IO()
exportGeneSyms xs fname = do
    withFile fname WriteMode (\handle -> do









hmm


 

 


darlliu
committed
May 23, 2013






225




            let contents = foldl1 (\acc x -> acc++"\n"++x) (map showJustS (map genesym xs))









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






226




227




228




229




230




231




            hPutStr handle contents
            )

exportProbes :: [Cybert_entry] -> String -> IO()
exportProbes xs fname = do
    withFile fname WriteMode (\handle -> do









hmm


 

 


darlliu
committed
May 23, 2013






232




            let contents = foldl1 (\acc x -> acc++"\n"++x) (map probe xs)









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






233




234




235




236




237




            hPutStr handle contents
            )

{-exportRef :: [Cybert_entry] -> String -> String -> IO()-}
{-end IO routines-}














53255057c9a07fb753863d2001f09ebe37ff8742


Switch branch/tag










haskell


cybert


Cybert.hs



Find file
Normal viewHistoryPermalink






Cybert.hs



8.98 KB









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




7




8




9




10




11




12




13




14




15




--a simple loader for cybert data
--generates cybert reports and tables
--
--has the following:
--1, a polymorphic and flexible data structure indexed by multiple
--keys.
--2, ability to do set operation, selection filtering and mapping 
--on entries
--3, IO for both human readable format and message passing to other python
--code
--
{-module definitions -}
module Cybert
(
    Cybert_entry(NA, Cybert,probe,genesym,









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






16




17




        sample, collection, mean, bf, bh, sds, 
        pval,ratio,secondaryRefs,secondaryData),









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






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




    cybert_entry,
    showCybertEntries,
    entriesByFold,
    entriesByPval,
    entriesBySym,
    entriesByUpDown,
    entriesBySecondaryRef,
    cybertToSet,
    loadCybert,
    exportCybert,
    exportGeneSyms,
    exportProbes
) where
{-end module definitions -}
import Data.Bits
import Data.List









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






34




35




import Data.Maybe
import Data.Char









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






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




import System.IO
import System.IO.Error
import qualified Data.Set as S
import qualified Data.Map as M
import qualified Data.ByteString.Char8 as B

{-basic data types-}
data Cybert_entry = NA | Cybert {
    probe :: String ,
    genesym :: Maybe String ,
    sample :: [String] ,
    collection :: Maybe String , --which dataset
    mean :: Either Float [Float] , --multiple means









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






49




    sds :: Either Float [Float],









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






50




    pval :: Float, --the pairwise or ANOVA pval









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






51




52




    bf :: Float,
    bh :: Float,









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






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




    ratio :: Either Float [Float], --pairwise ratio or numerous ratios
    secondaryRefs :: M.Map String String , --optional secondary refs
    secondaryData :: M.Map String Float , --optional secondary data
    raw :: B.ByteString
}
cybert_entry = Cybert{
    -- default constructor
    probe = "NONE",
    genesym = Nothing,
    sample = [],
    collection = Nothing,
    mean = Right [],









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






65




    sds = Right [],









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






66




    pval = -1,









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






67




68




    bf = -1,
    bh = -1,









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






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




    ratio = Left (-1),
    secondaryRefs = M.fromList [("","")],
    secondaryData = M.fromList [("",0)],
    raw = B.empty
}

hasher :: String -> Int
hasher = foldl (\h c -> 33*h `xor` fromEnum c) 5381
cybertHash :: Cybert_entry -> Int
cybertHash NA = 0
cybertHash Cybert{probe=p, collection=(Nothing)} = hasher $ "NONE"++p
cybertHash Cybert{probe=p, collection=(Just s)} = hasher $ s++p

instance Eq Cybert_entry where
    a == b = (cybertHash a) == (cybertHash b)
instance Ord Cybert_entry where
    compare a b = compare  (cybertHash a)  (cybertHash b)
--hashing
{-end basic data types-}
{-format and show routines-}
showJustS::Maybe String -> String
showJustS Nothing = "N/A"
showJustS (Just x) = x

showEitherF::(Either Float [Float]) -> String
showEitherF (Left x) = show x
showEitherF (Right xxs@(x:[])) = show x
showEitherF (Right xxs@(x:xs)) = (show x) ++"\t"++ (showEitherF (Right xs))

showCybertEntries::[Cybert_entry]->String
showCybertEntries (x:[])= show x
showCybertEntries (x:xs)= (show x) ++ "\n" ++ (showCybertEntries xs)

instance Show Cybert_entry where
    showsPrec _ a s = show a ++ s
    show NA = "NA"
    show Cybert {probe=p, genesym=sym, sample=ss, collection=col, mean=m,









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






106




107




    pval=pv, ratio=r, secondaryRefs=_, secondaryData=_,raw=_} = p++"\t"++
        (showJustS sym)++"\t"++(showJustS col) ++"\t" ++(showEitherF m)++"\t"++(show pv)









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






108




109




110




111




112




113




114




115




116




117




118




119




120




121




122




123




124




125




126




127




128




129




130




131




132




        -- for each sample
{-end format and show routines-}
{-set operation and filtering routines-}
entriesBySym :: [Cybert_entry] -> String -> [Cybert_entry]
entriesBySym xs sym = filter (\x -> genesym x == (Just sym)) xs

entryByProbe :: [Cybert_entry] -> String -> Cybert_entry
entryByProbe xs p = head $ filter (\x -> probe x == p) xs

entriesBySecondaryRef :: [Cybert_entry] -> String -> String -> [Cybert_entry]
entriesBySecondaryRef xs tref ref = filter (\x -> (secondaryRefs x) M.! tref == ref) xs
--lookup
entriesByFold :: [Cybert_entry] -> Float -> [Cybert_entry]
entriesByFold xs threshold = filter (\x ->pred $ mean x) xs where
                            pred (Left a) = False
                            pred (Right b)= if length b < 2 then False
                                            else b!!1-b!!0 > threshold

entriesByPval :: [Cybert_entry] -> Float -> [Cybert_entry]
entriesByPval xs threshold = filter (\x -> pval x < threshold) xs

entriesByUpDown :: [Cybert_entry] -> Bool -> [Cybert_entry]
entriesByUpDown xs val = filter (\x -> pred $ mean x) xs where
                          pred (Left a) = False
                          pred (Right b) = if length b < 2 then False









hmm


 

 


darlliu
committed
May 24, 2013






133




134




                                           else let bigger = (b!!1-b!!0>0)
                                                in bigger==val









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






135




136




137




138




139




140




141




--filtering

cybertToSet :: [Cybert_entry] -> S.Set Cybert_entry
cybertToSet xs = S.fromList xs
--set operations
{-end set operation and filtering routines-}
{-IO routines-}









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






142




143




144




145




146




buildHeaderPrec :: B.ByteString -> [(String , B.ByteString)]
buildHeaderPrec s = let ss = B.split '\t' s
                in map (\x -> (stripQuote $ B.unpack  x, x)) ss where
                   stripQuote ('\"':xs) = map toLower $ take ((length xs) - 1) xs
                   stripQuote xs = map toLower xs









hmm


 

 


darlliu
committed
May 24, 2013






147














should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






148




149




150




151




152




153




154




155




buildHeader :: B.ByteString -> (M.Map String (Maybe Int) )
buildHeader s = let ss = buildHeaderPrec s; sl = B.split '\t' s
                in M.fromList $ map  (\x -> (fst x , (snd x) `elemIndex` sl)) ss
maybeGet :: [B.ByteString] -> Maybe Int -> B.ByteString
[] `maybeGet` _ = B.pack ""
x `maybeGet` Nothing = B.pack ""
x `maybeGet` (Just s)= if length x > s then x !! s
                        else B.pack ""









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






156














should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






157




158




159




160




161




162




163




164




165




166




167




168




169




170




171




getText :: (M.Map String (Maybe Int))-> [B.ByteString] -> String -> Maybe String
getText header ss id = if id `M.notMember` header
                         then Nothing
                         else let idx = header M.! id
                              in Just (B.unpack $ ss `maybeGet` idx)
getNum :: (M.Map String (Maybe Int))-> [B.ByteString] -> String -> Float
getNum header ss id = if id `M.notMember` header
                        then -1 :: Float
                        else let idx = header M.! id
                             in read (B.unpack $ ss `maybeGet` idx) :: Float
getNums :: (M.Map String (Maybe Int))-> [B.ByteString] -> [String] -> Either Float [Float]
getNums header ss ids = let nums = map (getNum header ss) ids
                         in if length nums == 1 then Left (nums !! 0)
                              else Right $ filter (not . (== -1)) nums
lineToCybert :: (M.Map String (Maybe Int))-> [Cybert_entry] -> B.ByteString -> [Cybert_entry]









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






172




--take a header and an accumulator, then read the line and append the cybert entry









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






173




174




175




176




177




178




179




180




181




182




183




184




185




186




187




188




189




190




lineToCybert header xs line = xs ++ readLine line where
    readLine s = let ss = B.split '\t' s 
                  in if length ss /= M.size header then []
                     else let cybt= cybert_entry {
                         probe = B.unpack $ ss `maybeGet` (header M.! "probe_id"),
                         --this is a must
                         genesym = getText header ss "gene_sym",
                         --this is of maybe type
                         pval = getNum header ss "pval",
                         bf = getNum header ss "bonferroni",
                         bh = getNum header ss "bh",
                         --these are -1 defaulted
                         mean = getNums header ss $ map ( "mean" ++ ) ["c","e","1","2","3","4","5","6","7"],
                         sds = getNums header ss $ map ( "std" ++ ) ["c","e","1","2","3","4","5","6","7"]
                         --these are one or many
                         , raw = s
                         --raw info
                         } in [cybt]









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






191




192




193




194




195




196




197





loadCybert :: String -> IO (Maybe [Cybert_entry])
loadCybert fname = catch
    (withFile fname ReadMode (\handle -> do
        contents <- B.hGetContents handle
        let mylines =  B.split '\n' contents
        if length mylines <= 1 then return Nothing









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






198




199




200




201




202




203




        else let header = buildHeader (head mylines);
                 output = (Just (foldl (lineToCybert header) [] (drop 1 mylines)))
             in if output == (Just []) then return Nothing
                  else do 
                    putStrLn "Done parsing"
                    return output









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






204




205




    ))
    (\err -> do









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






206




            if isEOFError err









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






207




208




209




210




211




212




213




214




215




216




217




218




219




220




221




222




223




224




            then do
                 putStrLn "File is empty or truncated."
                 return Nothing
            else do
                 putStrLn $ "Unexpected Error at opening file: "++ (show err)
                 return Nothing
    )
-- Loads a cybert table from fname
exportCybert :: [Cybert_entry] -> String -> IO()
exportCybert xs fname = do
    withFile fname WriteMode (\handle -> do
            let contents = showCybertEntries xs
            hPutStr handle contents
            )

exportGeneSyms :: [Cybert_entry] -> String -> IO()
exportGeneSyms xs fname = do
    withFile fname WriteMode (\handle -> do









hmm


 

 


darlliu
committed
May 23, 2013






225




            let contents = foldl1 (\acc x -> acc++"\n"++x) (map showJustS (map genesym xs))









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






226




227




228




229




230




231




            hPutStr handle contents
            )

exportProbes :: [Cybert_entry] -> String -> IO()
exportProbes xs fname = do
    withFile fname WriteMode (\handle -> do









hmm


 

 


darlliu
committed
May 23, 2013






232




            let contents = foldl1 (\acc x -> acc++"\n"++x) (map probe xs)









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






233




234




235




236




237




            hPutStr handle contents
            )

{-exportRef :: [Cybert_entry] -> String -> String -> IO()-}
{-end IO routines-}










53255057c9a07fb753863d2001f09ebe37ff8742


Switch branch/tag










haskell


cybert


Cybert.hs



Find file
Normal viewHistoryPermalink




53255057c9a07fb753863d2001f09ebe37ff8742


Switch branch/tag










haskell


cybert


Cybert.hs





53255057c9a07fb753863d2001f09ebe37ff8742


Switch branch/tag








53255057c9a07fb753863d2001f09ebe37ff8742


Switch branch/tag





53255057c9a07fb753863d2001f09ebe37ff8742

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
haskell

cybert

Cybert.hs
Find file
Normal viewHistoryPermalink




Cybert.hs



8.98 KB









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




7




8




9




10




11




12




13




14




15




--a simple loader for cybert data
--generates cybert reports and tables
--
--has the following:
--1, a polymorphic and flexible data structure indexed by multiple
--keys.
--2, ability to do set operation, selection filtering and mapping 
--on entries
--3, IO for both human readable format and message passing to other python
--code
--
{-module definitions -}
module Cybert
(
    Cybert_entry(NA, Cybert,probe,genesym,









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






16




17




        sample, collection, mean, bf, bh, sds, 
        pval,ratio,secondaryRefs,secondaryData),









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






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




    cybert_entry,
    showCybertEntries,
    entriesByFold,
    entriesByPval,
    entriesBySym,
    entriesByUpDown,
    entriesBySecondaryRef,
    cybertToSet,
    loadCybert,
    exportCybert,
    exportGeneSyms,
    exportProbes
) where
{-end module definitions -}
import Data.Bits
import Data.List









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






34




35




import Data.Maybe
import Data.Char









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






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




import System.IO
import System.IO.Error
import qualified Data.Set as S
import qualified Data.Map as M
import qualified Data.ByteString.Char8 as B

{-basic data types-}
data Cybert_entry = NA | Cybert {
    probe :: String ,
    genesym :: Maybe String ,
    sample :: [String] ,
    collection :: Maybe String , --which dataset
    mean :: Either Float [Float] , --multiple means









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






49




    sds :: Either Float [Float],









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






50




    pval :: Float, --the pairwise or ANOVA pval









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






51




52




    bf :: Float,
    bh :: Float,









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






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




    ratio :: Either Float [Float], --pairwise ratio or numerous ratios
    secondaryRefs :: M.Map String String , --optional secondary refs
    secondaryData :: M.Map String Float , --optional secondary data
    raw :: B.ByteString
}
cybert_entry = Cybert{
    -- default constructor
    probe = "NONE",
    genesym = Nothing,
    sample = [],
    collection = Nothing,
    mean = Right [],









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






65




    sds = Right [],









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






66




    pval = -1,









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






67




68




    bf = -1,
    bh = -1,









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






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




    ratio = Left (-1),
    secondaryRefs = M.fromList [("","")],
    secondaryData = M.fromList [("",0)],
    raw = B.empty
}

hasher :: String -> Int
hasher = foldl (\h c -> 33*h `xor` fromEnum c) 5381
cybertHash :: Cybert_entry -> Int
cybertHash NA = 0
cybertHash Cybert{probe=p, collection=(Nothing)} = hasher $ "NONE"++p
cybertHash Cybert{probe=p, collection=(Just s)} = hasher $ s++p

instance Eq Cybert_entry where
    a == b = (cybertHash a) == (cybertHash b)
instance Ord Cybert_entry where
    compare a b = compare  (cybertHash a)  (cybertHash b)
--hashing
{-end basic data types-}
{-format and show routines-}
showJustS::Maybe String -> String
showJustS Nothing = "N/A"
showJustS (Just x) = x

showEitherF::(Either Float [Float]) -> String
showEitherF (Left x) = show x
showEitherF (Right xxs@(x:[])) = show x
showEitherF (Right xxs@(x:xs)) = (show x) ++"\t"++ (showEitherF (Right xs))

showCybertEntries::[Cybert_entry]->String
showCybertEntries (x:[])= show x
showCybertEntries (x:xs)= (show x) ++ "\n" ++ (showCybertEntries xs)

instance Show Cybert_entry where
    showsPrec _ a s = show a ++ s
    show NA = "NA"
    show Cybert {probe=p, genesym=sym, sample=ss, collection=col, mean=m,









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






106




107




    pval=pv, ratio=r, secondaryRefs=_, secondaryData=_,raw=_} = p++"\t"++
        (showJustS sym)++"\t"++(showJustS col) ++"\t" ++(showEitherF m)++"\t"++(show pv)









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






108




109




110




111




112




113




114




115




116




117




118




119




120




121




122




123




124




125




126




127




128




129




130




131




132




        -- for each sample
{-end format and show routines-}
{-set operation and filtering routines-}
entriesBySym :: [Cybert_entry] -> String -> [Cybert_entry]
entriesBySym xs sym = filter (\x -> genesym x == (Just sym)) xs

entryByProbe :: [Cybert_entry] -> String -> Cybert_entry
entryByProbe xs p = head $ filter (\x -> probe x == p) xs

entriesBySecondaryRef :: [Cybert_entry] -> String -> String -> [Cybert_entry]
entriesBySecondaryRef xs tref ref = filter (\x -> (secondaryRefs x) M.! tref == ref) xs
--lookup
entriesByFold :: [Cybert_entry] -> Float -> [Cybert_entry]
entriesByFold xs threshold = filter (\x ->pred $ mean x) xs where
                            pred (Left a) = False
                            pred (Right b)= if length b < 2 then False
                                            else b!!1-b!!0 > threshold

entriesByPval :: [Cybert_entry] -> Float -> [Cybert_entry]
entriesByPval xs threshold = filter (\x -> pval x < threshold) xs

entriesByUpDown :: [Cybert_entry] -> Bool -> [Cybert_entry]
entriesByUpDown xs val = filter (\x -> pred $ mean x) xs where
                          pred (Left a) = False
                          pred (Right b) = if length b < 2 then False









hmm


 

 


darlliu
committed
May 24, 2013






133




134




                                           else let bigger = (b!!1-b!!0>0)
                                                in bigger==val









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






135




136




137




138




139




140




141




--filtering

cybertToSet :: [Cybert_entry] -> S.Set Cybert_entry
cybertToSet xs = S.fromList xs
--set operations
{-end set operation and filtering routines-}
{-IO routines-}









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






142




143




144




145




146




buildHeaderPrec :: B.ByteString -> [(String , B.ByteString)]
buildHeaderPrec s = let ss = B.split '\t' s
                in map (\x -> (stripQuote $ B.unpack  x, x)) ss where
                   stripQuote ('\"':xs) = map toLower $ take ((length xs) - 1) xs
                   stripQuote xs = map toLower xs









hmm


 

 


darlliu
committed
May 24, 2013






147














should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






148




149




150




151




152




153




154




155




buildHeader :: B.ByteString -> (M.Map String (Maybe Int) )
buildHeader s = let ss = buildHeaderPrec s; sl = B.split '\t' s
                in M.fromList $ map  (\x -> (fst x , (snd x) `elemIndex` sl)) ss
maybeGet :: [B.ByteString] -> Maybe Int -> B.ByteString
[] `maybeGet` _ = B.pack ""
x `maybeGet` Nothing = B.pack ""
x `maybeGet` (Just s)= if length x > s then x !! s
                        else B.pack ""









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






156














should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






157




158




159




160




161




162




163




164




165




166




167




168




169




170




171




getText :: (M.Map String (Maybe Int))-> [B.ByteString] -> String -> Maybe String
getText header ss id = if id `M.notMember` header
                         then Nothing
                         else let idx = header M.! id
                              in Just (B.unpack $ ss `maybeGet` idx)
getNum :: (M.Map String (Maybe Int))-> [B.ByteString] -> String -> Float
getNum header ss id = if id `M.notMember` header
                        then -1 :: Float
                        else let idx = header M.! id
                             in read (B.unpack $ ss `maybeGet` idx) :: Float
getNums :: (M.Map String (Maybe Int))-> [B.ByteString] -> [String] -> Either Float [Float]
getNums header ss ids = let nums = map (getNum header ss) ids
                         in if length nums == 1 then Left (nums !! 0)
                              else Right $ filter (not . (== -1)) nums
lineToCybert :: (M.Map String (Maybe Int))-> [Cybert_entry] -> B.ByteString -> [Cybert_entry]









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






172




--take a header and an accumulator, then read the line and append the cybert entry









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






173




174




175




176




177




178




179




180




181




182




183




184




185




186




187




188




189




190




lineToCybert header xs line = xs ++ readLine line where
    readLine s = let ss = B.split '\t' s 
                  in if length ss /= M.size header then []
                     else let cybt= cybert_entry {
                         probe = B.unpack $ ss `maybeGet` (header M.! "probe_id"),
                         --this is a must
                         genesym = getText header ss "gene_sym",
                         --this is of maybe type
                         pval = getNum header ss "pval",
                         bf = getNum header ss "bonferroni",
                         bh = getNum header ss "bh",
                         --these are -1 defaulted
                         mean = getNums header ss $ map ( "mean" ++ ) ["c","e","1","2","3","4","5","6","7"],
                         sds = getNums header ss $ map ( "std" ++ ) ["c","e","1","2","3","4","5","6","7"]
                         --these are one or many
                         , raw = s
                         --raw info
                         } in [cybt]









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






191




192




193




194




195




196




197





loadCybert :: String -> IO (Maybe [Cybert_entry])
loadCybert fname = catch
    (withFile fname ReadMode (\handle -> do
        contents <- B.hGetContents handle
        let mylines =  B.split '\n' contents
        if length mylines <= 1 then return Nothing









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






198




199




200




201




202




203




        else let header = buildHeader (head mylines);
                 output = (Just (foldl (lineToCybert header) [] (drop 1 mylines)))
             in if output == (Just []) then return Nothing
                  else do 
                    putStrLn "Done parsing"
                    return output









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






204




205




    ))
    (\err -> do









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






206




            if isEOFError err









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






207




208




209




210




211




212




213




214




215




216




217




218




219




220




221




222




223




224




            then do
                 putStrLn "File is empty or truncated."
                 return Nothing
            else do
                 putStrLn $ "Unexpected Error at opening file: "++ (show err)
                 return Nothing
    )
-- Loads a cybert table from fname
exportCybert :: [Cybert_entry] -> String -> IO()
exportCybert xs fname = do
    withFile fname WriteMode (\handle -> do
            let contents = showCybertEntries xs
            hPutStr handle contents
            )

exportGeneSyms :: [Cybert_entry] -> String -> IO()
exportGeneSyms xs fname = do
    withFile fname WriteMode (\handle -> do









hmm


 

 


darlliu
committed
May 23, 2013






225




            let contents = foldl1 (\acc x -> acc++"\n"++x) (map showJustS (map genesym xs))









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






226




227




228




229




230




231




            hPutStr handle contents
            )

exportProbes :: [Cybert_entry] -> String -> IO()
exportProbes xs fname = do
    withFile fname WriteMode (\handle -> do









hmm


 

 


darlliu
committed
May 23, 2013






232




            let contents = foldl1 (\acc x -> acc++"\n"++x) (map probe xs)









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






233




234




235




236




237




            hPutStr handle contents
            )

{-exportRef :: [Cybert_entry] -> String -> String -> IO()-}
{-end IO routines-}








Cybert.hs



8.98 KB










Cybert.hs



8.98 KB









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




7




8




9




10




11




12




13




14




15




--a simple loader for cybert data
--generates cybert reports and tables
--
--has the following:
--1, a polymorphic and flexible data structure indexed by multiple
--keys.
--2, ability to do set operation, selection filtering and mapping 
--on entries
--3, IO for both human readable format and message passing to other python
--code
--
{-module definitions -}
module Cybert
(
    Cybert_entry(NA, Cybert,probe,genesym,









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






16




17




        sample, collection, mean, bf, bh, sds, 
        pval,ratio,secondaryRefs,secondaryData),









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






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




    cybert_entry,
    showCybertEntries,
    entriesByFold,
    entriesByPval,
    entriesBySym,
    entriesByUpDown,
    entriesBySecondaryRef,
    cybertToSet,
    loadCybert,
    exportCybert,
    exportGeneSyms,
    exportProbes
) where
{-end module definitions -}
import Data.Bits
import Data.List









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






34




35




import Data.Maybe
import Data.Char









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






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




import System.IO
import System.IO.Error
import qualified Data.Set as S
import qualified Data.Map as M
import qualified Data.ByteString.Char8 as B

{-basic data types-}
data Cybert_entry = NA | Cybert {
    probe :: String ,
    genesym :: Maybe String ,
    sample :: [String] ,
    collection :: Maybe String , --which dataset
    mean :: Either Float [Float] , --multiple means









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






49




    sds :: Either Float [Float],









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






50




    pval :: Float, --the pairwise or ANOVA pval









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






51




52




    bf :: Float,
    bh :: Float,









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






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




    ratio :: Either Float [Float], --pairwise ratio or numerous ratios
    secondaryRefs :: M.Map String String , --optional secondary refs
    secondaryData :: M.Map String Float , --optional secondary data
    raw :: B.ByteString
}
cybert_entry = Cybert{
    -- default constructor
    probe = "NONE",
    genesym = Nothing,
    sample = [],
    collection = Nothing,
    mean = Right [],









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






65




    sds = Right [],









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






66




    pval = -1,









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






67




68




    bf = -1,
    bh = -1,









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






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




    ratio = Left (-1),
    secondaryRefs = M.fromList [("","")],
    secondaryData = M.fromList [("",0)],
    raw = B.empty
}

hasher :: String -> Int
hasher = foldl (\h c -> 33*h `xor` fromEnum c) 5381
cybertHash :: Cybert_entry -> Int
cybertHash NA = 0
cybertHash Cybert{probe=p, collection=(Nothing)} = hasher $ "NONE"++p
cybertHash Cybert{probe=p, collection=(Just s)} = hasher $ s++p

instance Eq Cybert_entry where
    a == b = (cybertHash a) == (cybertHash b)
instance Ord Cybert_entry where
    compare a b = compare  (cybertHash a)  (cybertHash b)
--hashing
{-end basic data types-}
{-format and show routines-}
showJustS::Maybe String -> String
showJustS Nothing = "N/A"
showJustS (Just x) = x

showEitherF::(Either Float [Float]) -> String
showEitherF (Left x) = show x
showEitherF (Right xxs@(x:[])) = show x
showEitherF (Right xxs@(x:xs)) = (show x) ++"\t"++ (showEitherF (Right xs))

showCybertEntries::[Cybert_entry]->String
showCybertEntries (x:[])= show x
showCybertEntries (x:xs)= (show x) ++ "\n" ++ (showCybertEntries xs)

instance Show Cybert_entry where
    showsPrec _ a s = show a ++ s
    show NA = "NA"
    show Cybert {probe=p, genesym=sym, sample=ss, collection=col, mean=m,









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






106




107




    pval=pv, ratio=r, secondaryRefs=_, secondaryData=_,raw=_} = p++"\t"++
        (showJustS sym)++"\t"++(showJustS col) ++"\t" ++(showEitherF m)++"\t"++(show pv)









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






108




109




110




111




112




113




114




115




116




117




118




119




120




121




122




123




124




125




126




127




128




129




130




131




132




        -- for each sample
{-end format and show routines-}
{-set operation and filtering routines-}
entriesBySym :: [Cybert_entry] -> String -> [Cybert_entry]
entriesBySym xs sym = filter (\x -> genesym x == (Just sym)) xs

entryByProbe :: [Cybert_entry] -> String -> Cybert_entry
entryByProbe xs p = head $ filter (\x -> probe x == p) xs

entriesBySecondaryRef :: [Cybert_entry] -> String -> String -> [Cybert_entry]
entriesBySecondaryRef xs tref ref = filter (\x -> (secondaryRefs x) M.! tref == ref) xs
--lookup
entriesByFold :: [Cybert_entry] -> Float -> [Cybert_entry]
entriesByFold xs threshold = filter (\x ->pred $ mean x) xs where
                            pred (Left a) = False
                            pred (Right b)= if length b < 2 then False
                                            else b!!1-b!!0 > threshold

entriesByPval :: [Cybert_entry] -> Float -> [Cybert_entry]
entriesByPval xs threshold = filter (\x -> pval x < threshold) xs

entriesByUpDown :: [Cybert_entry] -> Bool -> [Cybert_entry]
entriesByUpDown xs val = filter (\x -> pred $ mean x) xs where
                          pred (Left a) = False
                          pred (Right b) = if length b < 2 then False









hmm


 

 


darlliu
committed
May 24, 2013






133




134




                                           else let bigger = (b!!1-b!!0>0)
                                                in bigger==val









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






135




136




137




138




139




140




141




--filtering

cybertToSet :: [Cybert_entry] -> S.Set Cybert_entry
cybertToSet xs = S.fromList xs
--set operations
{-end set operation and filtering routines-}
{-IO routines-}









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






142




143




144




145




146




buildHeaderPrec :: B.ByteString -> [(String , B.ByteString)]
buildHeaderPrec s = let ss = B.split '\t' s
                in map (\x -> (stripQuote $ B.unpack  x, x)) ss where
                   stripQuote ('\"':xs) = map toLower $ take ((length xs) - 1) xs
                   stripQuote xs = map toLower xs









hmm


 

 


darlliu
committed
May 24, 2013






147














should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






148




149




150




151




152




153




154




155




buildHeader :: B.ByteString -> (M.Map String (Maybe Int) )
buildHeader s = let ss = buildHeaderPrec s; sl = B.split '\t' s
                in M.fromList $ map  (\x -> (fst x , (snd x) `elemIndex` sl)) ss
maybeGet :: [B.ByteString] -> Maybe Int -> B.ByteString
[] `maybeGet` _ = B.pack ""
x `maybeGet` Nothing = B.pack ""
x `maybeGet` (Just s)= if length x > s then x !! s
                        else B.pack ""









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






156














should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






157




158




159




160




161




162




163




164




165




166




167




168




169




170




171




getText :: (M.Map String (Maybe Int))-> [B.ByteString] -> String -> Maybe String
getText header ss id = if id `M.notMember` header
                         then Nothing
                         else let idx = header M.! id
                              in Just (B.unpack $ ss `maybeGet` idx)
getNum :: (M.Map String (Maybe Int))-> [B.ByteString] -> String -> Float
getNum header ss id = if id `M.notMember` header
                        then -1 :: Float
                        else let idx = header M.! id
                             in read (B.unpack $ ss `maybeGet` idx) :: Float
getNums :: (M.Map String (Maybe Int))-> [B.ByteString] -> [String] -> Either Float [Float]
getNums header ss ids = let nums = map (getNum header ss) ids
                         in if length nums == 1 then Left (nums !! 0)
                              else Right $ filter (not . (== -1)) nums
lineToCybert :: (M.Map String (Maybe Int))-> [Cybert_entry] -> B.ByteString -> [Cybert_entry]









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






172




--take a header and an accumulator, then read the line and append the cybert entry









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






173




174




175




176




177




178




179




180




181




182




183




184




185




186




187




188




189




190




lineToCybert header xs line = xs ++ readLine line where
    readLine s = let ss = B.split '\t' s 
                  in if length ss /= M.size header then []
                     else let cybt= cybert_entry {
                         probe = B.unpack $ ss `maybeGet` (header M.! "probe_id"),
                         --this is a must
                         genesym = getText header ss "gene_sym",
                         --this is of maybe type
                         pval = getNum header ss "pval",
                         bf = getNum header ss "bonferroni",
                         bh = getNum header ss "bh",
                         --these are -1 defaulted
                         mean = getNums header ss $ map ( "mean" ++ ) ["c","e","1","2","3","4","5","6","7"],
                         sds = getNums header ss $ map ( "std" ++ ) ["c","e","1","2","3","4","5","6","7"]
                         --these are one or many
                         , raw = s
                         --raw info
                         } in [cybt]









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






191




192




193




194




195




196




197





loadCybert :: String -> IO (Maybe [Cybert_entry])
loadCybert fname = catch
    (withFile fname ReadMode (\handle -> do
        contents <- B.hGetContents handle
        let mylines =  B.split '\n' contents
        if length mylines <= 1 then return Nothing









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






198




199




200




201




202




203




        else let header = buildHeader (head mylines);
                 output = (Just (foldl (lineToCybert header) [] (drop 1 mylines)))
             in if output == (Just []) then return Nothing
                  else do 
                    putStrLn "Done parsing"
                    return output









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






204




205




    ))
    (\err -> do









should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013






206




            if isEOFError err









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






207




208




209




210




211




212




213




214




215




216




217




218




219




220




221




222




223




224




            then do
                 putStrLn "File is empty or truncated."
                 return Nothing
            else do
                 putStrLn $ "Unexpected Error at opening file: "++ (show err)
                 return Nothing
    )
-- Loads a cybert table from fname
exportCybert :: [Cybert_entry] -> String -> IO()
exportCybert xs fname = do
    withFile fname WriteMode (\handle -> do
            let contents = showCybertEntries xs
            hPutStr handle contents
            )

exportGeneSyms :: [Cybert_entry] -> String -> IO()
exportGeneSyms xs fname = do
    withFile fname WriteMode (\handle -> do









hmm


 

 


darlliu
committed
May 23, 2013






225




            let contents = foldl1 (\acc x -> acc++"\n"++x) (map showJustS (map genesym xs))









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






226




227




228




229




230




231




            hPutStr handle contents
            )

exportProbes :: [Cybert_entry] -> String -> IO()
exportProbes xs fname = do
    withFile fname WriteMode (\handle -> do









hmm


 

 


darlliu
committed
May 23, 2013






232




            let contents = foldl1 (\acc x -> acc++"\n"++x) (map probe xs)









cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013






233




234




235




236




237




            hPutStr handle contents
            )

{-exportRef :: [Cybert_entry] -> String -> String -> IO()-}
{-end IO routines-}







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

7

8

9

10

11

12

13

14

15
--a simple loader for cybert data--a simple loader for cybert data--generates cybert reports and tables--generates cybert reports and tables------has the following:--has the following:--1, a polymorphic and flexible data structure indexed by multiple--1, a polymorphic and flexible data structure indexed by multiple--keys.--keys.--2, ability to do set operation, selection filtering and mapping --2, ability to do set operation, selection filtering and mapping --on entries--on entries--3, IO for both human readable format and message passing to other python--3, IO for both human readable format and message passing to other python--code--code----{-module definitions -}{-module definitions -}module CybertmoduleCybert((    Cybert_entry(NA, Cybert,probe,genesym,Cybert_entry(NA,Cybert,probe,genesym,



should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013



should work now, exercise considered done?


 

 

should work now, exercise considered done?

 

darlliu
committed
May 25, 2013

16

17
        sample, collection, mean, bf, bh, sds, sample,collection,mean,bf,bh,sds,        pval,ratio,secondaryRefs,secondaryData),pval,ratio,secondaryRefs,secondaryData),



cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013



cybert exercise, now need to load line only



 

cybert exercise, now need to load line only


darlliu
committed
May 23, 2013

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
    cybert_entry,cybert_entry,    showCybertEntries,showCybertEntries,    entriesByFold,entriesByFold,    entriesByPval,entriesByPval,    entriesBySym,entriesBySym,    entriesByUpDown,entriesByUpDown,    entriesBySecondaryRef,entriesBySecondaryRef,    cybertToSet,cybertToSet,    loadCybert,loadCybert,    exportCybert,exportCybert,    exportGeneSyms,exportGeneSyms,    exportProbesexportProbes) where)where{-end module definitions -}{-end module definitions -}import Data.BitsimportData.Bitsimport Data.ListimportData.List



should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013



should work now, exercise considered done?


 

 

should work now, exercise considered done?

 

darlliu
committed
May 25, 2013

34

35
import Data.MaybeimportData.Maybeimport Data.CharimportData.Char



cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013



cybert exercise, now need to load line only



 

cybert exercise, now need to load line only


darlliu
committed
May 23, 2013

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
import System.IOimportSystem.IOimport System.IO.ErrorimportSystem.IO.Errorimport qualified Data.Set as SimportqualifiedData.SetasSimport qualified Data.Map as MimportqualifiedData.MapasMimport qualified Data.ByteString.Char8 as BimportqualifiedData.ByteString.Char8asB{-basic data types-}{-basic data types-}data Cybert_entry = NA | Cybert {dataCybert_entry=NA|Cybert{    probe :: String ,probe::String,    genesym :: Maybe String ,genesym::MaybeString,    sample :: [String] ,sample::[String],    collection :: Maybe String , --which datasetcollection::MaybeString,--which dataset    mean :: Either Float [Float] , --multiple meansmean::EitherFloat[Float],--multiple means



should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013



should work now, exercise considered done?


 

 

should work now, exercise considered done?

 

darlliu
committed
May 25, 2013

49
    sds :: Either Float [Float],sds::EitherFloat[Float],



cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013



cybert exercise, now need to load line only



 

cybert exercise, now need to load line only


darlliu
committed
May 23, 2013

50
    pval :: Float, --the pairwise or ANOVA pvalpval::Float,--the pairwise or ANOVA pval



should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013



should work now, exercise considered done?


 

 

should work now, exercise considered done?

 

darlliu
committed
May 25, 2013

51

52
    bf :: Float,bf::Float,    bh :: Float,bh::Float,



cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013



cybert exercise, now need to load line only



 

cybert exercise, now need to load line only


darlliu
committed
May 23, 2013

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
    ratio :: Either Float [Float], --pairwise ratio or numerous ratiosratio::EitherFloat[Float],--pairwise ratio or numerous ratios    secondaryRefs :: M.Map String String , --optional secondary refssecondaryRefs::M.MapStringString,--optional secondary refs    secondaryData :: M.Map String Float , --optional secondary datasecondaryData::M.MapStringFloat,--optional secondary data    raw :: B.ByteStringraw::B.ByteString}}cybert_entry = Cybert{cybert_entry=Cybert{    -- default constructor-- default constructor    probe = "NONE",probe="NONE",    genesym = Nothing,genesym=Nothing,    sample = [],sample=[],    collection = Nothing,collection=Nothing,    mean = Right [],mean=Right[],



should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013



should work now, exercise considered done?


 

 

should work now, exercise considered done?

 

darlliu
committed
May 25, 2013

65
    sds = Right [],sds=Right[],



cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013



cybert exercise, now need to load line only



 

cybert exercise, now need to load line only


darlliu
committed
May 23, 2013

66
    pval = -1,pval=-1,



should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013



should work now, exercise considered done?


 

 

should work now, exercise considered done?

 

darlliu
committed
May 25, 2013

67

68
    bf = -1,bf=-1,    bh = -1,bh=-1,



cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013



cybert exercise, now need to load line only



 

cybert exercise, now need to load line only


darlliu
committed
May 23, 2013

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
    ratio = Left (-1),ratio=Left(-1),    secondaryRefs = M.fromList [("","")],secondaryRefs=M.fromList[("","")],    secondaryData = M.fromList [("",0)],secondaryData=M.fromList[("",0)],    raw = B.emptyraw=B.empty}}hasher :: String -> Inthasher::String->Inthasher = foldl (\h c -> 33*h `xor` fromEnum c) 5381hasher=foldl(\hc->33*h`xor`fromEnumc)5381cybertHash :: Cybert_entry -> IntcybertHash::Cybert_entry->IntcybertHash NA = 0cybertHashNA=0cybertHash Cybert{probe=p, collection=(Nothing)} = hasher $ "NONE"++pcybertHashCybert{probe=p,collection=(Nothing)}=hasher$"NONE"++pcybertHash Cybert{probe=p, collection=(Just s)} = hasher $ s++pcybertHashCybert{probe=p,collection=(Justs)}=hasher$s++pinstance Eq Cybert_entry whereinstanceEqCybert_entrywhere    a == b = (cybertHash a) == (cybertHash b)a==b=(cybertHasha)==(cybertHashb)instance Ord Cybert_entry whereinstanceOrdCybert_entrywhere    compare a b = compare  (cybertHash a)  (cybertHash b)compareab=compare(cybertHasha)(cybertHashb)--hashing--hashing{-end basic data types-}{-end basic data types-}{-format and show routines-}{-format and show routines-}showJustS::Maybe String -> StringshowJustS::MaybeString->StringshowJustS Nothing = "N/A"showJustSNothing="N/A"showJustS (Just x) = xshowJustS(Justx)=xshowEitherF::(Either Float [Float]) -> StringshowEitherF::(EitherFloat[Float])->StringshowEitherF (Left x) = show xshowEitherF(Leftx)=showxshowEitherF (Right xxs@(x:[])) = show xshowEitherF(Rightxxs@(x:[]))=showxshowEitherF (Right xxs@(x:xs)) = (show x) ++"\t"++ (showEitherF (Right xs))showEitherF(Rightxxs@(x:xs))=(showx)++"\t"++(showEitherF(Rightxs))showCybertEntries::[Cybert_entry]->StringshowCybertEntries::[Cybert_entry]->StringshowCybertEntries (x:[])= show xshowCybertEntries(x:[])=showxshowCybertEntries (x:xs)= (show x) ++ "\n" ++ (showCybertEntries xs)showCybertEntries(x:xs)=(showx)++"\n"++(showCybertEntriesxs)instance Show Cybert_entry whereinstanceShowCybert_entrywhere    showsPrec _ a s = show a ++ sshowsPrec_as=showa++s    show NA = "NA"showNA="NA"    show Cybert {probe=p, genesym=sym, sample=ss, collection=col, mean=m,showCybert{probe=p,genesym=sym,sample=ss,collection=col,mean=m,



should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013



should work now, exercise considered done?


 

 

should work now, exercise considered done?

 

darlliu
committed
May 25, 2013

106

107
    pval=pv, ratio=r, secondaryRefs=_, secondaryData=_,raw=_} = p++"\t"++pval=pv,ratio=r,secondaryRefs=_,secondaryData=_,raw=_}=p++"\t"++        (showJustS sym)++"\t"++(showJustS col) ++"\t" ++(showEitherF m)++"\t"++(show pv)(showJustSsym)++"\t"++(showJustScol)++"\t"++(showEitherFm)++"\t"++(showpv)



cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013



cybert exercise, now need to load line only



 

cybert exercise, now need to load line only


darlliu
committed
May 23, 2013

108

109

110

111

112

113

114

115

116

117

118

119

120

121

122

123

124

125

126

127

128

129

130

131

132
        -- for each sample-- for each sample{-end format and show routines-}{-end format and show routines-}{-set operation and filtering routines-}{-set operation and filtering routines-}entriesBySym :: [Cybert_entry] -> String -> [Cybert_entry]entriesBySym::[Cybert_entry]->String->[Cybert_entry]entriesBySym xs sym = filter (\x -> genesym x == (Just sym)) xsentriesBySymxssym=filter(\x->genesymx==(Justsym))xsentryByProbe :: [Cybert_entry] -> String -> Cybert_entryentryByProbe::[Cybert_entry]->String->Cybert_entryentryByProbe xs p = head $ filter (\x -> probe x == p) xsentryByProbexsp=head$filter(\x->probex==p)xsentriesBySecondaryRef :: [Cybert_entry] -> String -> String -> [Cybert_entry]entriesBySecondaryRef::[Cybert_entry]->String->String->[Cybert_entry]entriesBySecondaryRef xs tref ref = filter (\x -> (secondaryRefs x) M.! tref == ref) xsentriesBySecondaryRefxstrefref=filter(\x->(secondaryRefsx)M.!tref==ref)xs--lookup--lookupentriesByFold :: [Cybert_entry] -> Float -> [Cybert_entry]entriesByFold::[Cybert_entry]->Float->[Cybert_entry]entriesByFold xs threshold = filter (\x ->pred $ mean x) xs whereentriesByFoldxsthreshold=filter(\x->pred$meanx)xswhere                            pred (Left a) = Falsepred(Lefta)=False                            pred (Right b)= if length b < 2 then Falsepred(Rightb)=iflengthb<2thenFalse                                            else b!!1-b!!0 > thresholdelseb!!1-b!!0>thresholdentriesByPval :: [Cybert_entry] -> Float -> [Cybert_entry]entriesByPval::[Cybert_entry]->Float->[Cybert_entry]entriesByPval xs threshold = filter (\x -> pval x < threshold) xsentriesByPvalxsthreshold=filter(\x->pvalx<threshold)xsentriesByUpDown :: [Cybert_entry] -> Bool -> [Cybert_entry]entriesByUpDown::[Cybert_entry]->Bool->[Cybert_entry]entriesByUpDown xs val = filter (\x -> pred $ mean x) xs whereentriesByUpDownxsval=filter(\x->pred$meanx)xswhere                          pred (Left a) = Falsepred(Lefta)=False                          pred (Right b) = if length b < 2 then Falsepred(Rightb)=iflengthb<2thenFalse



hmm


 

 


darlliu
committed
May 24, 2013



hmm


 

 

hmm

 

darlliu
committed
May 24, 2013

133

134
                                           else let bigger = (b!!1-b!!0>0)elseletbigger=(b!!1-b!!0>0)                                                in bigger==valinbigger==val



cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013



cybert exercise, now need to load line only



 

cybert exercise, now need to load line only


darlliu
committed
May 23, 2013

135

136

137

138

139

140

141
--filtering--filteringcybertToSet :: [Cybert_entry] -> S.Set Cybert_entrycybertToSet::[Cybert_entry]->S.SetCybert_entrycybertToSet xs = S.fromList xscybertToSetxs=S.fromListxs--set operations--set operations{-end set operation and filtering routines-}{-end set operation and filtering routines-}{-IO routines-}{-IO routines-}



should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013



should work now, exercise considered done?


 

 

should work now, exercise considered done?

 

darlliu
committed
May 25, 2013

142

143

144

145

146
buildHeaderPrec :: B.ByteString -> [(String , B.ByteString)]buildHeaderPrec::B.ByteString->[(String,B.ByteString)]buildHeaderPrec s = let ss = B.split '\t' sbuildHeaderPrecs=letss=B.split'\t's                in map (\x -> (stripQuote $ B.unpack  x, x)) ss whereinmap(\x->(stripQuote$B.unpackx,x))sswhere                   stripQuote ('\"':xs) = map toLower $ take ((length xs) - 1) xsstripQuote('\"':xs)=maptoLower$take((lengthxs)-1)xs                   stripQuote xs = map toLower xsstripQuotexs=maptoLowerxs



hmm


 

 


darlliu
committed
May 24, 2013



hmm


 

 

hmm

 

darlliu
committed
May 24, 2013

147




should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013



should work now, exercise considered done?


 

 

should work now, exercise considered done?

 

darlliu
committed
May 25, 2013

148

149

150

151

152

153

154

155
buildHeader :: B.ByteString -> (M.Map String (Maybe Int) )buildHeader::B.ByteString->(M.MapString(MaybeInt))buildHeader s = let ss = buildHeaderPrec s; sl = B.split '\t' sbuildHeaders=letss=buildHeaderPrecs;sl=B.split'\t's                in M.fromList $ map  (\x -> (fst x , (snd x) `elemIndex` sl)) ssinM.fromList$map(\x->(fstx,(sndx)`elemIndex`sl))ssmaybeGet :: [B.ByteString] -> Maybe Int -> B.ByteStringmaybeGet::[B.ByteString]->MaybeInt->B.ByteString[] `maybeGet` _ = B.pack ""[]`maybeGet`_=B.pack""x `maybeGet` Nothing = B.pack ""x`maybeGet`Nothing=B.pack""x `maybeGet` (Just s)= if length x > s then x !! sx`maybeGet`(Justs)=iflengthx>sthenx!!s                        else B.pack ""elseB.pack""



cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013



cybert exercise, now need to load line only



 

cybert exercise, now need to load line only


darlliu
committed
May 23, 2013

156




should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013



should work now, exercise considered done?


 

 

should work now, exercise considered done?

 

darlliu
committed
May 25, 2013

157

158

159

160

161

162

163

164

165

166

167

168

169

170

171
getText :: (M.Map String (Maybe Int))-> [B.ByteString] -> String -> Maybe StringgetText::(M.MapString(MaybeInt))->[B.ByteString]->String->MaybeStringgetText header ss id = if id `M.notMember` headergetTextheaderssid=ifid`M.notMember`header                         then NothingthenNothing                         else let idx = header M.! idelseletidx=headerM.!id                              in Just (B.unpack $ ss `maybeGet` idx)inJust(B.unpack$ss`maybeGet`idx)getNum :: (M.Map String (Maybe Int))-> [B.ByteString] -> String -> FloatgetNum::(M.MapString(MaybeInt))->[B.ByteString]->String->FloatgetNum header ss id = if id `M.notMember` headergetNumheaderssid=ifid`M.notMember`header                        then -1 :: Floatthen-1::Float                        else let idx = header M.! idelseletidx=headerM.!id                             in read (B.unpack $ ss `maybeGet` idx) :: Floatinread(B.unpack$ss`maybeGet`idx)::FloatgetNums :: (M.Map String (Maybe Int))-> [B.ByteString] -> [String] -> Either Float [Float]getNums::(M.MapString(MaybeInt))->[B.ByteString]->[String]->EitherFloat[Float]getNums header ss ids = let nums = map (getNum header ss) idsgetNumsheaderssids=letnums=map(getNumheaderss)ids                         in if length nums == 1 then Left (nums !! 0)iniflengthnums==1thenLeft(nums!!0)                              else Right $ filter (not . (== -1)) numselseRight$filter(not.(==-1))numslineToCybert :: (M.Map String (Maybe Int))-> [Cybert_entry] -> B.ByteString -> [Cybert_entry]lineToCybert::(M.MapString(MaybeInt))->[Cybert_entry]->B.ByteString->[Cybert_entry]



cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013



cybert exercise, now need to load line only



 

cybert exercise, now need to load line only


darlliu
committed
May 23, 2013

172
--take a header and an accumulator, then read the line and append the cybert entry--take a header and an accumulator, then read the line and append the cybert entry



should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013



should work now, exercise considered done?


 

 

should work now, exercise considered done?

 

darlliu
committed
May 25, 2013

173

174

175

176

177

178

179

180

181

182

183

184

185

186

187

188

189

190
lineToCybert header xs line = xs ++ readLine line wherelineToCybertheaderxsline=xs++readLinelinewhere    readLine s = let ss = B.split '\t' s readLines=letss=B.split'\t's                  in if length ss /= M.size header then []iniflengthss/=M.sizeheaderthen[]                     else let cybt= cybert_entry {elseletcybt=cybert_entry{                         probe = B.unpack $ ss `maybeGet` (header M.! "probe_id"),probe=B.unpack$ss`maybeGet`(headerM.!"probe_id"),                         --this is a must--this is a must                         genesym = getText header ss "gene_sym",genesym=getTextheaderss"gene_sym",                         --this is of maybe type--this is of maybe type                         pval = getNum header ss "pval",pval=getNumheaderss"pval",                         bf = getNum header ss "bonferroni",bf=getNumheaderss"bonferroni",                         bh = getNum header ss "bh",bh=getNumheaderss"bh",                         --these are -1 defaulted--these are -1 defaulted                         mean = getNums header ss $ map ( "mean" ++ ) ["c","e","1","2","3","4","5","6","7"],mean=getNumsheaderss$map("mean"++)["c","e","1","2","3","4","5","6","7"],                         sds = getNums header ss $ map ( "std" ++ ) ["c","e","1","2","3","4","5","6","7"]sds=getNumsheaderss$map("std"++)["c","e","1","2","3","4","5","6","7"]                         --these are one or many--these are one or many                         , raw = s,raw=s                         --raw info--raw info                         } in [cybt]}in[cybt]



cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013



cybert exercise, now need to load line only



 

cybert exercise, now need to load line only


darlliu
committed
May 23, 2013

191

192

193

194

195

196

197
loadCybert :: String -> IO (Maybe [Cybert_entry])loadCybert::String->IO(Maybe[Cybert_entry])loadCybert fname = catchloadCybertfname=catch    (withFile fname ReadMode (\handle -> do(withFilefnameReadMode(\handle->do        contents <- B.hGetContents handlecontents<-B.hGetContentshandle        let mylines =  B.split '\n' contentsletmylines=B.split'\n'contents        if length mylines <= 1 then return Nothingiflengthmylines<=1thenreturnNothing



should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013



should work now, exercise considered done?


 

 

should work now, exercise considered done?

 

darlliu
committed
May 25, 2013

198

199

200

201

202

203
        else let header = buildHeader (head mylines);elseletheader=buildHeader(headmylines);                 output = (Just (foldl (lineToCybert header) [] (drop 1 mylines)))output=(Just(foldl(lineToCybertheader)[](drop1mylines)))             in if output == (Just []) then return Nothinginifoutput==(Just[])thenreturnNothing                  else do elsedo                    putStrLn "Done parsing"putStrLn"Done parsing"                    return outputreturnoutput



cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013



cybert exercise, now need to load line only



 

cybert exercise, now need to load line only


darlliu
committed
May 23, 2013

204

205
    ))))    (\err -> do(\err->do



should work now, exercise considered done?


 

 


darlliu
committed
May 25, 2013



should work now, exercise considered done?


 

 

should work now, exercise considered done?

 

darlliu
committed
May 25, 2013

206
            if isEOFError errifisEOFErrorerr



cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013



cybert exercise, now need to load line only



 

cybert exercise, now need to load line only


darlliu
committed
May 23, 2013

207

208

209

210

211

212

213

214

215

216

217

218

219

220

221

222

223

224
            then dothendo                 putStrLn "File is empty or truncated."putStrLn"File is empty or truncated."                 return NothingreturnNothing            else doelsedo                 putStrLn $ "Unexpected Error at opening file: "++ (show err)putStrLn$"Unexpected Error at opening file: "++(showerr)                 return NothingreturnNothing    ))-- Loads a cybert table from fname-- Loads a cybert table from fnameexportCybert :: [Cybert_entry] -> String -> IO()exportCybert::[Cybert_entry]->String->IO()exportCybert xs fname = doexportCybertxsfname=do    withFile fname WriteMode (\handle -> dowithFilefnameWriteMode(\handle->do            let contents = showCybertEntries xsletcontents=showCybertEntriesxs            hPutStr handle contentshPutStrhandlecontents            ))exportGeneSyms :: [Cybert_entry] -> String -> IO()exportGeneSyms::[Cybert_entry]->String->IO()exportGeneSyms xs fname = doexportGeneSymsxsfname=do    withFile fname WriteMode (\handle -> dowithFilefnameWriteMode(\handle->do



hmm


 

 


darlliu
committed
May 23, 2013



hmm


 

 

hmm

 

darlliu
committed
May 23, 2013

225
            let contents = foldl1 (\acc x -> acc++"\n"++x) (map showJustS (map genesym xs))letcontents=foldl1(\accx->acc++"\n"++x)(mapshowJustS(mapgenesymxs))



cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013



cybert exercise, now need to load line only



 

cybert exercise, now need to load line only


darlliu
committed
May 23, 2013

226

227

228

229

230

231
            hPutStr handle contentshPutStrhandlecontents            ))exportProbes :: [Cybert_entry] -> String -> IO()exportProbes::[Cybert_entry]->String->IO()exportProbes xs fname = doexportProbesxsfname=do    withFile fname WriteMode (\handle -> dowithFilefnameWriteMode(\handle->do



hmm


 

 


darlliu
committed
May 23, 2013



hmm


 

 

hmm

 

darlliu
committed
May 23, 2013

232
            let contents = foldl1 (\acc x -> acc++"\n"++x) (map probe xs)letcontents=foldl1(\accx->acc++"\n"++x)(mapprobexs)



cybert exercise, now need to load line only



 


darlliu
committed
May 23, 2013



cybert exercise, now need to load line only



 

cybert exercise, now need to load line only


darlliu
committed
May 23, 2013

233

234

235

236

237
            hPutStr handle contentshPutStrhandlecontents            )){-exportRef :: [Cybert_entry] -> String -> String -> IO()-}{-exportRef :: [Cybert_entry] -> String -> String -> IO()-}{-end IO routines-}{-end IO routines-}





