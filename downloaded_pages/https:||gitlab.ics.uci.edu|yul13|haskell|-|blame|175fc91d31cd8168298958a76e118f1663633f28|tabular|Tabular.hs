



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

175fc91d31cd8168298958a76e118f1663633f28

















175fc91d31cd8168298958a76e118f1663633f28


Switch branch/tag










haskell


tabular


Tabular.hs



Find file
Normal viewHistoryPermalink






Tabular.hs



7.77 KB









Newer










Older









update



 


darlliu
committed
Feb 26, 2015






1




2




3




4




5




6




7




8




--Implementation of R like table object with IO to .tsv in Haskell

import Data.CSV.Conduit
import Data.ByteString.Char8 (ByteString, unpack, pack)
import qualified Data.Vector as V
import Control.Monad
import Control.Exception as E
import Data.List









update


 

 


darlliu
committed
Feb 27, 2015






9




10




import Data.Ord (comparing)
import System.IO









update



 


darlliu
committed
Feb 26, 2015






11




12




13




14




type Text = ByteString -- Change this to change the underlying datatype
type Tabular = V.Vector(V.Vector Text)
type TabularRow = V.Vector Text










update


 

 


darlliu
committed
Feb 27, 2015






15




-- File accessor









update



 


darlliu
committed
Feb 26, 2015






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




fromFileWith' sep quote fp= do
    let myOption = CSVSettings{csvSep=sep, csvQuoteChar=quote}
    csvData <- readCSVFile myOption fp :: IO ( V.Vector([Text]) )
    return (Just ( V.map V.fromList csvData))

fromFileWith sep quote fp = E.catch
    (fromFileWith' sep quote fp)
    (\e -> do 
     let err = show (e::IOException)
     putStrLn $ "Unexpected Error at opening file: "++ (show err)
     return Nothing
    )

fromFile = fromFileWith '\t' Nothing










update


 

 


darlliu
committed
Feb 27, 2015






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




-- Write to file as tsv

showTabular :: Tabular -> String
showTabular t = intercalate "\n" $ V.toList $ rows where
   rows = V.map (\x -> intercalate "\t" (V.toList (toString x))) t

showTabularM :: Maybe Tabular -> String
showTabularM Nothing = ""
showTabularM (Just t) = showTabular t

toFile fname t = do
    withFile fname WriteMode (\handle -> do
        hPutStr handle (showTabular t))

toFile' fname t = case t of 
    Nothing -> return ()
    Just tt -> toFile fname tt
-- Constructor with an ordering

reorder :: V.Vector Int -> Tabular -> Tabular
reorder os t = (header' t) `V.cons` (V.map (\x -> t V.! (x+1)) os)

reorderM = liftM2 reorder









update



 


darlliu
committed
Feb 26, 2015






54




55




56




-- Row accessor, get Nothing in case of error

header' t = t V.! 0









update


 

 


darlliu
committed
Feb 27, 2015






57




header t = V.map pack $ t V.! 0









update



 


darlliu
committed
Feb 26, 2015






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





(#!) :: Tabular -> Int -> Maybe TabularRow
t #! i = if i<0 || i> V.length t 
    then Nothing
    else Just (t V.! (i+1))
     --compensate for the header which we assume always is there
(#) :: (Maybe Tabular) -> Int -> Maybe TabularRow
t # i = t >>= (#! i)

-- Column accessor, would like it to get nothing in case of error

(%!) :: Tabular -> Int ->Maybe TabularRow
t %! i = if i < 0 || i > (V.length $ t V.! 0 ) 
    then Nothing
    else let vv = V.map (\x -> (x::TabularRow) V.! i) t in
        if V.length vv > 1 
            then Just (V.tail vv)
            else Nothing

(%) :: (Maybe Tabular) -> Int ->Maybe TabularRow
t % i = t >>= (%! i)


(%%!):: Tabular -> String -> Maybe TabularRow









update


 

 


darlliu
committed
Feb 27, 2015






82




83




84




85




t %%! s = let h = header' t in 
            let key = (pack s) `V.elemIndex` h in case key of
                Nothing -> Nothing
                Just idx -> t %! idx









update



 


darlliu
committed
Feb 26, 2015






86




87




88




89




90




91




92




93





(%%):: Maybe(Tabular) -> String -> Maybe TabularRow
t %% s = t >>= (%%! s)

--type coercer, would like it to crash in case of error

toFloat :: TabularRow ->(V.Vector Float)
toFloat xs = V.map (\x -> (read $ unpack x) :: Float) xs









update


 

 


darlliu
committed
Feb 27, 2015






94




95




toFloatM ::(Monad m )=> m TabularRow -> m (V.Vector Float)
toFloatM = liftM toFloat









update



 


darlliu
committed
Feb 26, 2015






96




97




98





toString :: TabularRow ->(V.Vector String)
toString xs = V.map (\x -> unpack x) xs









update


 

 


darlliu
committed
Feb 27, 2015






99




100




toStringM ::(Monad m)=> m TabularRow -> m (V.Vector String)
toStringM = liftM toString









update



 


darlliu
committed
Feb 26, 2015






101




102




103





toBool :: TabularRow ->(V.Vector Bool)
toBool xs = V.map (\x -> (read $ unpack x) :: Bool) xs









update


 

 


darlliu
committed
Feb 27, 2015






104




105




toBoolM :: (Monad m)=> m TabularRow -> m (V.Vector Bool)
toBoolM = liftM toBool









update



 


darlliu
committed
Feb 26, 2015






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




133




134




135




136




137




138




139




140




141




142




143




144





--modifiers 

setRow' :: Int -> TabularRow -> Tabular ->  Maybe Tabular
setRow' i r t = if i >= V.length t -1 || i < 0 || (V.length r) /= (V.length $ header' t)
            then Nothing
            else Just (t V.// [(i + 1, r)])
setRow :: Int -> Maybe TabularRow -> Tabular ->  Maybe Tabular
setRow _ Nothing _ = Nothing
setRow i (Just r) t = if i >= V.length t -1 || i < 0 || (V.length r) /= (V.length $ header' t)
            then Nothing
            else Just (t V.// [(i + 1, r)])

setCol' :: Int -> TabularRow -> Tabular -> Maybe Tabular
setCol' i c t = if i >= V.length (header' t) || i < 0 || (V.length c) /= V.length t -1
            then Nothing
            else Just ( V.zipWith (\x y -> x V.// [(i,y)]) t ( n `V.cons` c) ) where
                n = header' t V.! i
setCol :: Int ->Maybe TabularRow -> Tabular -> Maybe Tabular
setCol _ Nothing _= Nothing
setCol i (Just c) t = if i >= V.length (header' t) || i < 0 || (V.length c) /= V.length t -1
            then Nothing
            else Just ( V.zipWith (\x y -> x V.// [(i,y)]) t ( n  `V.cons` c) ) where
                n = header' t V.! i

setColN' :: String -> TabularRow -> Tabular -> Maybe Tabular
setColN' n c t = let nn = pack n in case nn `V.elemIndex` (header' t) of 
            Nothing -> Nothing
            Just i -> Just ( V.zipWith (\x y -> x V.// [(i,y)]) t ( nn `V.cons` c) )

setColN :: String ->Maybe TabularRow -> Tabular -> Maybe Tabular
setColN _ Nothing _= Nothing
setColN n (Just c) t = let nn = pack n in case nn `V.elemIndex` (header' t) of 
            Nothing -> Nothing
            Just i -> Just ( V.zipWith (\x y -> x V.// [(i,y)]) t ( nn `V.cons` c) )

--use >>= to modify

--populators









update


 

 


darlliu
committed
Feb 27, 2015






145




146




147




148




149




150




151




152




153




154




155




156




157




--addRow 

rowAdd' :: TabularRow -> Tabular -> Maybe Tabular

r `rowAdd'` t = if (V.length r) /= (V.length $ header' t)
    then Nothing
    else Just (t `V.snoc` r)

rowAdd :: Maybe TabularRow -> Maybe Tabular -> Maybe Tabular
Nothing `rowAdd` _ = Nothing
_ `rowAdd` Nothing = Nothing
(Just r) `rowAdd` (Just t) = r `rowAdd'` t
t `addRow` r = r `rowAdd` t









update



 


darlliu
committed
Feb 26, 2015






158




159




--addCol (Name separate) <<










update


 

 


darlliu
committed
Feb 27, 2015






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




172




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




191




192




193




194




195




196




197




198




199




200




201




202




203




204




205




206




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




colAdd' :: (String, TabularRow) -> Tabular -> Maybe Tabular

(n, c) `colAdd'` t = let nn = pack n in if V.length c /= V.length t - 1
    then Nothing
    else Just ( V.zipWith (\x y -> x `V.snoc` y) t ( nn `V.cons` c) )

colAdd :: (String, Maybe TabularRow) -> Maybe Tabular -> Maybe Tabular
(_, Nothing) `colAdd` _ = Nothing
(_, _ ) `colAdd` Nothing = Nothing
(n , Just c) `colAdd` (Just t) = (n,c) `colAdd'` t
t `addCol` c = c `colAdd` t


--Sort and Filter

filter' :: (Text -> Bool) -> Int -> Tabular -> V.Vector Int
filter' f i t =  let c = t %! i in case c of 
        Just cc -> V.findIndices f cc
        Nothing -> V.fromList []

filterM' :: (Monad m)=>(Text -> Bool) -> Int -> m Tabular -> m (V.Vector Int)
filterM' f i = liftM ( filter' f i ) 

filterN :: (Text -> Bool) -> String -> Tabular -> V.Vector Int
filterN f s t = let i = (pack s) `V.elemIndex` (header' t) in case i of 
    Nothing -> filter' f (-1) t
    Just ii -> filter' f ii t

filterNM :: (Monad m)=>(Text -> Bool) -> String -> m Tabular -> m (V.Vector Int)
filterNM f s = liftM (filterN f s)


sort' :: (Ord a) => (Text  -> a) -> Int -> Tabular -> V.Vector Int
sort' f i t = let c = (t %! i) in case c of 
    Nothing -> V.fromList $ take (V.length t) [0 ..]
    (Just cc) -> V.fromList $ map fst $ sortBy (comparing snd) (V.toList $ V.indexed $ V.map f cc)

sortM' :: (Ord a, Monad m) => (Text  -> a) -> Int -> m Tabular -> m (V.Vector Int)
sortM' f i = liftM (sort' f i)

sortN :: (Ord a) => (Text  -> a) -> String -> Tabular -> V.Vector Int
sortN f s t = let i = (pack s) `V.elemIndex` (header' t) in case i of 
    Nothing -> sort' f (-1) t
    Just ii -> sort' f ii t

sortNM :: (Ord a, Monad m) => (Text  -> a) -> String -> m Tabular -> m (V.Vector Int)
sortNM f s = liftM (sortN f s)

-- Utility

text2Float x = (read $ unpack x ):: Float

text2String x = unpack x 

text2Bool x = (read $ unpack x ):: Bool











update



 


darlliu
committed
Feb 26, 2015






217




218




219




220




221




222




223




224




225




226




227




228




main = do
    t <- fromFile "./txts/test_csv.txt"
    let r = t # 0
    let c = t % 0
    let cc = t %% "KEGG"
    putStrLn $ show r 
    putStrLn $ show c
    putStrLn $ show cc
    let tt = t >>= (setRow 3 r)
    putStrLn $ show $ tt#3 
    let ttt = t >>= (setColN "KEGG" c)
    putStrLn $ show $ ttt%%"KEGG"









update


 

 


darlliu
committed
Feb 27, 2015






229




230




231




232




233




234




235




236




237




238




239




    let ttt2 = ttt `addRow` r
    let ttt3 = ttt2 `addCol` ( "Added Column", ttt2 % 2)
    putStrLn "Testing Adding Column"
    putStrLn $ show $ ttt3 %% "Added Column"
    putStrLn "Testing sorting and filtering"
    putStrLn $ show (ttt % 16)
    putStrLn $ show $ sortM' text2Float 16 ttt
    let os =  filterM' (\x -> text2Float x  < 1) 16 ttt
    putStrLn $ show os
    putStrLn $ showTabularM $ (reorderM os ttt)
    toFile' "./txts/test_csv_out.txt" (reorderM os ttt)









update



 


darlliu
committed
Feb 26, 2015






240




    return ()












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

175fc91d31cd8168298958a76e118f1663633f28

















175fc91d31cd8168298958a76e118f1663633f28


Switch branch/tag










haskell


tabular


Tabular.hs



Find file
Normal viewHistoryPermalink






Tabular.hs



7.77 KB









Newer










Older









update



 


darlliu
committed
Feb 26, 2015






1




2




3




4




5




6




7




8




--Implementation of R like table object with IO to .tsv in Haskell

import Data.CSV.Conduit
import Data.ByteString.Char8 (ByteString, unpack, pack)
import qualified Data.Vector as V
import Control.Monad
import Control.Exception as E
import Data.List









update


 

 


darlliu
committed
Feb 27, 2015






9




10




import Data.Ord (comparing)
import System.IO









update



 


darlliu
committed
Feb 26, 2015






11




12




13




14




type Text = ByteString -- Change this to change the underlying datatype
type Tabular = V.Vector(V.Vector Text)
type TabularRow = V.Vector Text










update


 

 


darlliu
committed
Feb 27, 2015






15




-- File accessor









update



 


darlliu
committed
Feb 26, 2015






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




fromFileWith' sep quote fp= do
    let myOption = CSVSettings{csvSep=sep, csvQuoteChar=quote}
    csvData <- readCSVFile myOption fp :: IO ( V.Vector([Text]) )
    return (Just ( V.map V.fromList csvData))

fromFileWith sep quote fp = E.catch
    (fromFileWith' sep quote fp)
    (\e -> do 
     let err = show (e::IOException)
     putStrLn $ "Unexpected Error at opening file: "++ (show err)
     return Nothing
    )

fromFile = fromFileWith '\t' Nothing










update


 

 


darlliu
committed
Feb 27, 2015






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




-- Write to file as tsv

showTabular :: Tabular -> String
showTabular t = intercalate "\n" $ V.toList $ rows where
   rows = V.map (\x -> intercalate "\t" (V.toList (toString x))) t

showTabularM :: Maybe Tabular -> String
showTabularM Nothing = ""
showTabularM (Just t) = showTabular t

toFile fname t = do
    withFile fname WriteMode (\handle -> do
        hPutStr handle (showTabular t))

toFile' fname t = case t of 
    Nothing -> return ()
    Just tt -> toFile fname tt
-- Constructor with an ordering

reorder :: V.Vector Int -> Tabular -> Tabular
reorder os t = (header' t) `V.cons` (V.map (\x -> t V.! (x+1)) os)

reorderM = liftM2 reorder









update



 


darlliu
committed
Feb 26, 2015






54




55




56




-- Row accessor, get Nothing in case of error

header' t = t V.! 0









update


 

 


darlliu
committed
Feb 27, 2015






57




header t = V.map pack $ t V.! 0









update



 


darlliu
committed
Feb 26, 2015






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





(#!) :: Tabular -> Int -> Maybe TabularRow
t #! i = if i<0 || i> V.length t 
    then Nothing
    else Just (t V.! (i+1))
     --compensate for the header which we assume always is there
(#) :: (Maybe Tabular) -> Int -> Maybe TabularRow
t # i = t >>= (#! i)

-- Column accessor, would like it to get nothing in case of error

(%!) :: Tabular -> Int ->Maybe TabularRow
t %! i = if i < 0 || i > (V.length $ t V.! 0 ) 
    then Nothing
    else let vv = V.map (\x -> (x::TabularRow) V.! i) t in
        if V.length vv > 1 
            then Just (V.tail vv)
            else Nothing

(%) :: (Maybe Tabular) -> Int ->Maybe TabularRow
t % i = t >>= (%! i)


(%%!):: Tabular -> String -> Maybe TabularRow









update


 

 


darlliu
committed
Feb 27, 2015






82




83




84




85




t %%! s = let h = header' t in 
            let key = (pack s) `V.elemIndex` h in case key of
                Nothing -> Nothing
                Just idx -> t %! idx









update



 


darlliu
committed
Feb 26, 2015






86




87




88




89




90




91




92




93





(%%):: Maybe(Tabular) -> String -> Maybe TabularRow
t %% s = t >>= (%%! s)

--type coercer, would like it to crash in case of error

toFloat :: TabularRow ->(V.Vector Float)
toFloat xs = V.map (\x -> (read $ unpack x) :: Float) xs









update


 

 


darlliu
committed
Feb 27, 2015






94




95




toFloatM ::(Monad m )=> m TabularRow -> m (V.Vector Float)
toFloatM = liftM toFloat









update



 


darlliu
committed
Feb 26, 2015






96




97




98





toString :: TabularRow ->(V.Vector String)
toString xs = V.map (\x -> unpack x) xs









update


 

 


darlliu
committed
Feb 27, 2015






99




100




toStringM ::(Monad m)=> m TabularRow -> m (V.Vector String)
toStringM = liftM toString









update



 


darlliu
committed
Feb 26, 2015






101




102




103





toBool :: TabularRow ->(V.Vector Bool)
toBool xs = V.map (\x -> (read $ unpack x) :: Bool) xs









update


 

 


darlliu
committed
Feb 27, 2015






104




105




toBoolM :: (Monad m)=> m TabularRow -> m (V.Vector Bool)
toBoolM = liftM toBool









update



 


darlliu
committed
Feb 26, 2015






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




133




134




135




136




137




138




139




140




141




142




143




144





--modifiers 

setRow' :: Int -> TabularRow -> Tabular ->  Maybe Tabular
setRow' i r t = if i >= V.length t -1 || i < 0 || (V.length r) /= (V.length $ header' t)
            then Nothing
            else Just (t V.// [(i + 1, r)])
setRow :: Int -> Maybe TabularRow -> Tabular ->  Maybe Tabular
setRow _ Nothing _ = Nothing
setRow i (Just r) t = if i >= V.length t -1 || i < 0 || (V.length r) /= (V.length $ header' t)
            then Nothing
            else Just (t V.// [(i + 1, r)])

setCol' :: Int -> TabularRow -> Tabular -> Maybe Tabular
setCol' i c t = if i >= V.length (header' t) || i < 0 || (V.length c) /= V.length t -1
            then Nothing
            else Just ( V.zipWith (\x y -> x V.// [(i,y)]) t ( n `V.cons` c) ) where
                n = header' t V.! i
setCol :: Int ->Maybe TabularRow -> Tabular -> Maybe Tabular
setCol _ Nothing _= Nothing
setCol i (Just c) t = if i >= V.length (header' t) || i < 0 || (V.length c) /= V.length t -1
            then Nothing
            else Just ( V.zipWith (\x y -> x V.// [(i,y)]) t ( n  `V.cons` c) ) where
                n = header' t V.! i

setColN' :: String -> TabularRow -> Tabular -> Maybe Tabular
setColN' n c t = let nn = pack n in case nn `V.elemIndex` (header' t) of 
            Nothing -> Nothing
            Just i -> Just ( V.zipWith (\x y -> x V.// [(i,y)]) t ( nn `V.cons` c) )

setColN :: String ->Maybe TabularRow -> Tabular -> Maybe Tabular
setColN _ Nothing _= Nothing
setColN n (Just c) t = let nn = pack n in case nn `V.elemIndex` (header' t) of 
            Nothing -> Nothing
            Just i -> Just ( V.zipWith (\x y -> x V.// [(i,y)]) t ( nn `V.cons` c) )

--use >>= to modify

--populators









update


 

 


darlliu
committed
Feb 27, 2015






145




146




147




148




149




150




151




152




153




154




155




156




157




--addRow 

rowAdd' :: TabularRow -> Tabular -> Maybe Tabular

r `rowAdd'` t = if (V.length r) /= (V.length $ header' t)
    then Nothing
    else Just (t `V.snoc` r)

rowAdd :: Maybe TabularRow -> Maybe Tabular -> Maybe Tabular
Nothing `rowAdd` _ = Nothing
_ `rowAdd` Nothing = Nothing
(Just r) `rowAdd` (Just t) = r `rowAdd'` t
t `addRow` r = r `rowAdd` t









update



 


darlliu
committed
Feb 26, 2015






158




159




--addCol (Name separate) <<










update


 

 


darlliu
committed
Feb 27, 2015






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




172




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




191




192




193




194




195




196




197




198




199




200




201




202




203




204




205




206




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




colAdd' :: (String, TabularRow) -> Tabular -> Maybe Tabular

(n, c) `colAdd'` t = let nn = pack n in if V.length c /= V.length t - 1
    then Nothing
    else Just ( V.zipWith (\x y -> x `V.snoc` y) t ( nn `V.cons` c) )

colAdd :: (String, Maybe TabularRow) -> Maybe Tabular -> Maybe Tabular
(_, Nothing) `colAdd` _ = Nothing
(_, _ ) `colAdd` Nothing = Nothing
(n , Just c) `colAdd` (Just t) = (n,c) `colAdd'` t
t `addCol` c = c `colAdd` t


--Sort and Filter

filter' :: (Text -> Bool) -> Int -> Tabular -> V.Vector Int
filter' f i t =  let c = t %! i in case c of 
        Just cc -> V.findIndices f cc
        Nothing -> V.fromList []

filterM' :: (Monad m)=>(Text -> Bool) -> Int -> m Tabular -> m (V.Vector Int)
filterM' f i = liftM ( filter' f i ) 

filterN :: (Text -> Bool) -> String -> Tabular -> V.Vector Int
filterN f s t = let i = (pack s) `V.elemIndex` (header' t) in case i of 
    Nothing -> filter' f (-1) t
    Just ii -> filter' f ii t

filterNM :: (Monad m)=>(Text -> Bool) -> String -> m Tabular -> m (V.Vector Int)
filterNM f s = liftM (filterN f s)


sort' :: (Ord a) => (Text  -> a) -> Int -> Tabular -> V.Vector Int
sort' f i t = let c = (t %! i) in case c of 
    Nothing -> V.fromList $ take (V.length t) [0 ..]
    (Just cc) -> V.fromList $ map fst $ sortBy (comparing snd) (V.toList $ V.indexed $ V.map f cc)

sortM' :: (Ord a, Monad m) => (Text  -> a) -> Int -> m Tabular -> m (V.Vector Int)
sortM' f i = liftM (sort' f i)

sortN :: (Ord a) => (Text  -> a) -> String -> Tabular -> V.Vector Int
sortN f s t = let i = (pack s) `V.elemIndex` (header' t) in case i of 
    Nothing -> sort' f (-1) t
    Just ii -> sort' f ii t

sortNM :: (Ord a, Monad m) => (Text  -> a) -> String -> m Tabular -> m (V.Vector Int)
sortNM f s = liftM (sortN f s)

-- Utility

text2Float x = (read $ unpack x ):: Float

text2String x = unpack x 

text2Bool x = (read $ unpack x ):: Bool











update



 


darlliu
committed
Feb 26, 2015






217




218




219




220




221




222




223




224




225




226




227




228




main = do
    t <- fromFile "./txts/test_csv.txt"
    let r = t # 0
    let c = t % 0
    let cc = t %% "KEGG"
    putStrLn $ show r 
    putStrLn $ show c
    putStrLn $ show cc
    let tt = t >>= (setRow 3 r)
    putStrLn $ show $ tt#3 
    let ttt = t >>= (setColN "KEGG" c)
    putStrLn $ show $ ttt%%"KEGG"









update


 

 


darlliu
committed
Feb 27, 2015






229




230




231




232




233




234




235




236




237




238




239




    let ttt2 = ttt `addRow` r
    let ttt3 = ttt2 `addCol` ( "Added Column", ttt2 % 2)
    putStrLn "Testing Adding Column"
    putStrLn $ show $ ttt3 %% "Added Column"
    putStrLn "Testing sorting and filtering"
    putStrLn $ show (ttt % 16)
    putStrLn $ show $ sortM' text2Float 16 ttt
    let os =  filterM' (\x -> text2Float x  < 1) 16 ttt
    putStrLn $ show os
    putStrLn $ showTabularM $ (reorderM os ttt)
    toFile' "./txts/test_csv_out.txt" (reorderM os ttt)









update



 


darlliu
committed
Feb 26, 2015






240




    return ()











Open sidebar



Yu Liu haskell

175fc91d31cd8168298958a76e118f1663633f28







Open sidebar



Yu Liu haskell

175fc91d31cd8168298958a76e118f1663633f28




Open sidebar

Yu Liu haskell

175fc91d31cd8168298958a76e118f1663633f28


Yu Liuhaskellhaskell
175fc91d31cd8168298958a76e118f1663633f28










175fc91d31cd8168298958a76e118f1663633f28


Switch branch/tag










haskell


tabular


Tabular.hs



Find file
Normal viewHistoryPermalink






Tabular.hs



7.77 KB









Newer










Older









update



 


darlliu
committed
Feb 26, 2015






1




2




3




4




5




6




7




8




--Implementation of R like table object with IO to .tsv in Haskell

import Data.CSV.Conduit
import Data.ByteString.Char8 (ByteString, unpack, pack)
import qualified Data.Vector as V
import Control.Monad
import Control.Exception as E
import Data.List









update


 

 


darlliu
committed
Feb 27, 2015






9




10




import Data.Ord (comparing)
import System.IO









update



 


darlliu
committed
Feb 26, 2015






11




12




13




14




type Text = ByteString -- Change this to change the underlying datatype
type Tabular = V.Vector(V.Vector Text)
type TabularRow = V.Vector Text










update


 

 


darlliu
committed
Feb 27, 2015






15




-- File accessor









update



 


darlliu
committed
Feb 26, 2015






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




fromFileWith' sep quote fp= do
    let myOption = CSVSettings{csvSep=sep, csvQuoteChar=quote}
    csvData <- readCSVFile myOption fp :: IO ( V.Vector([Text]) )
    return (Just ( V.map V.fromList csvData))

fromFileWith sep quote fp = E.catch
    (fromFileWith' sep quote fp)
    (\e -> do 
     let err = show (e::IOException)
     putStrLn $ "Unexpected Error at opening file: "++ (show err)
     return Nothing
    )

fromFile = fromFileWith '\t' Nothing










update


 

 


darlliu
committed
Feb 27, 2015






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




-- Write to file as tsv

showTabular :: Tabular -> String
showTabular t = intercalate "\n" $ V.toList $ rows where
   rows = V.map (\x -> intercalate "\t" (V.toList (toString x))) t

showTabularM :: Maybe Tabular -> String
showTabularM Nothing = ""
showTabularM (Just t) = showTabular t

toFile fname t = do
    withFile fname WriteMode (\handle -> do
        hPutStr handle (showTabular t))

toFile' fname t = case t of 
    Nothing -> return ()
    Just tt -> toFile fname tt
-- Constructor with an ordering

reorder :: V.Vector Int -> Tabular -> Tabular
reorder os t = (header' t) `V.cons` (V.map (\x -> t V.! (x+1)) os)

reorderM = liftM2 reorder









update



 


darlliu
committed
Feb 26, 2015






54




55




56




-- Row accessor, get Nothing in case of error

header' t = t V.! 0









update


 

 


darlliu
committed
Feb 27, 2015






57




header t = V.map pack $ t V.! 0









update



 


darlliu
committed
Feb 26, 2015






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





(#!) :: Tabular -> Int -> Maybe TabularRow
t #! i = if i<0 || i> V.length t 
    then Nothing
    else Just (t V.! (i+1))
     --compensate for the header which we assume always is there
(#) :: (Maybe Tabular) -> Int -> Maybe TabularRow
t # i = t >>= (#! i)

-- Column accessor, would like it to get nothing in case of error

(%!) :: Tabular -> Int ->Maybe TabularRow
t %! i = if i < 0 || i > (V.length $ t V.! 0 ) 
    then Nothing
    else let vv = V.map (\x -> (x::TabularRow) V.! i) t in
        if V.length vv > 1 
            then Just (V.tail vv)
            else Nothing

(%) :: (Maybe Tabular) -> Int ->Maybe TabularRow
t % i = t >>= (%! i)


(%%!):: Tabular -> String -> Maybe TabularRow









update


 

 


darlliu
committed
Feb 27, 2015






82




83




84




85




t %%! s = let h = header' t in 
            let key = (pack s) `V.elemIndex` h in case key of
                Nothing -> Nothing
                Just idx -> t %! idx









update



 


darlliu
committed
Feb 26, 2015






86




87




88




89




90




91




92




93





(%%):: Maybe(Tabular) -> String -> Maybe TabularRow
t %% s = t >>= (%%! s)

--type coercer, would like it to crash in case of error

toFloat :: TabularRow ->(V.Vector Float)
toFloat xs = V.map (\x -> (read $ unpack x) :: Float) xs









update


 

 


darlliu
committed
Feb 27, 2015






94




95




toFloatM ::(Monad m )=> m TabularRow -> m (V.Vector Float)
toFloatM = liftM toFloat









update



 


darlliu
committed
Feb 26, 2015






96




97




98





toString :: TabularRow ->(V.Vector String)
toString xs = V.map (\x -> unpack x) xs









update


 

 


darlliu
committed
Feb 27, 2015






99




100




toStringM ::(Monad m)=> m TabularRow -> m (V.Vector String)
toStringM = liftM toString









update



 


darlliu
committed
Feb 26, 2015






101




102




103





toBool :: TabularRow ->(V.Vector Bool)
toBool xs = V.map (\x -> (read $ unpack x) :: Bool) xs









update


 

 


darlliu
committed
Feb 27, 2015






104




105




toBoolM :: (Monad m)=> m TabularRow -> m (V.Vector Bool)
toBoolM = liftM toBool









update



 


darlliu
committed
Feb 26, 2015






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




133




134




135




136




137




138




139




140




141




142




143




144





--modifiers 

setRow' :: Int -> TabularRow -> Tabular ->  Maybe Tabular
setRow' i r t = if i >= V.length t -1 || i < 0 || (V.length r) /= (V.length $ header' t)
            then Nothing
            else Just (t V.// [(i + 1, r)])
setRow :: Int -> Maybe TabularRow -> Tabular ->  Maybe Tabular
setRow _ Nothing _ = Nothing
setRow i (Just r) t = if i >= V.length t -1 || i < 0 || (V.length r) /= (V.length $ header' t)
            then Nothing
            else Just (t V.// [(i + 1, r)])

setCol' :: Int -> TabularRow -> Tabular -> Maybe Tabular
setCol' i c t = if i >= V.length (header' t) || i < 0 || (V.length c) /= V.length t -1
            then Nothing
            else Just ( V.zipWith (\x y -> x V.// [(i,y)]) t ( n `V.cons` c) ) where
                n = header' t V.! i
setCol :: Int ->Maybe TabularRow -> Tabular -> Maybe Tabular
setCol _ Nothing _= Nothing
setCol i (Just c) t = if i >= V.length (header' t) || i < 0 || (V.length c) /= V.length t -1
            then Nothing
            else Just ( V.zipWith (\x y -> x V.// [(i,y)]) t ( n  `V.cons` c) ) where
                n = header' t V.! i

setColN' :: String -> TabularRow -> Tabular -> Maybe Tabular
setColN' n c t = let nn = pack n in case nn `V.elemIndex` (header' t) of 
            Nothing -> Nothing
            Just i -> Just ( V.zipWith (\x y -> x V.// [(i,y)]) t ( nn `V.cons` c) )

setColN :: String ->Maybe TabularRow -> Tabular -> Maybe Tabular
setColN _ Nothing _= Nothing
setColN n (Just c) t = let nn = pack n in case nn `V.elemIndex` (header' t) of 
            Nothing -> Nothing
            Just i -> Just ( V.zipWith (\x y -> x V.// [(i,y)]) t ( nn `V.cons` c) )

--use >>= to modify

--populators









update


 

 


darlliu
committed
Feb 27, 2015






145




146




147




148




149




150




151




152




153




154




155




156




157




--addRow 

rowAdd' :: TabularRow -> Tabular -> Maybe Tabular

r `rowAdd'` t = if (V.length r) /= (V.length $ header' t)
    then Nothing
    else Just (t `V.snoc` r)

rowAdd :: Maybe TabularRow -> Maybe Tabular -> Maybe Tabular
Nothing `rowAdd` _ = Nothing
_ `rowAdd` Nothing = Nothing
(Just r) `rowAdd` (Just t) = r `rowAdd'` t
t `addRow` r = r `rowAdd` t









update



 


darlliu
committed
Feb 26, 2015






158




159




--addCol (Name separate) <<










update


 

 


darlliu
committed
Feb 27, 2015






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




172




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




191




192




193




194




195




196




197




198




199




200




201




202




203




204




205




206




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




colAdd' :: (String, TabularRow) -> Tabular -> Maybe Tabular

(n, c) `colAdd'` t = let nn = pack n in if V.length c /= V.length t - 1
    then Nothing
    else Just ( V.zipWith (\x y -> x `V.snoc` y) t ( nn `V.cons` c) )

colAdd :: (String, Maybe TabularRow) -> Maybe Tabular -> Maybe Tabular
(_, Nothing) `colAdd` _ = Nothing
(_, _ ) `colAdd` Nothing = Nothing
(n , Just c) `colAdd` (Just t) = (n,c) `colAdd'` t
t `addCol` c = c `colAdd` t


--Sort and Filter

filter' :: (Text -> Bool) -> Int -> Tabular -> V.Vector Int
filter' f i t =  let c = t %! i in case c of 
        Just cc -> V.findIndices f cc
        Nothing -> V.fromList []

filterM' :: (Monad m)=>(Text -> Bool) -> Int -> m Tabular -> m (V.Vector Int)
filterM' f i = liftM ( filter' f i ) 

filterN :: (Text -> Bool) -> String -> Tabular -> V.Vector Int
filterN f s t = let i = (pack s) `V.elemIndex` (header' t) in case i of 
    Nothing -> filter' f (-1) t
    Just ii -> filter' f ii t

filterNM :: (Monad m)=>(Text -> Bool) -> String -> m Tabular -> m (V.Vector Int)
filterNM f s = liftM (filterN f s)


sort' :: (Ord a) => (Text  -> a) -> Int -> Tabular -> V.Vector Int
sort' f i t = let c = (t %! i) in case c of 
    Nothing -> V.fromList $ take (V.length t) [0 ..]
    (Just cc) -> V.fromList $ map fst $ sortBy (comparing snd) (V.toList $ V.indexed $ V.map f cc)

sortM' :: (Ord a, Monad m) => (Text  -> a) -> Int -> m Tabular -> m (V.Vector Int)
sortM' f i = liftM (sort' f i)

sortN :: (Ord a) => (Text  -> a) -> String -> Tabular -> V.Vector Int
sortN f s t = let i = (pack s) `V.elemIndex` (header' t) in case i of 
    Nothing -> sort' f (-1) t
    Just ii -> sort' f ii t

sortNM :: (Ord a, Monad m) => (Text  -> a) -> String -> m Tabular -> m (V.Vector Int)
sortNM f s = liftM (sortN f s)

-- Utility

text2Float x = (read $ unpack x ):: Float

text2String x = unpack x 

text2Bool x = (read $ unpack x ):: Bool











update



 


darlliu
committed
Feb 26, 2015






217




218




219




220




221




222




223




224




225




226




227




228




main = do
    t <- fromFile "./txts/test_csv.txt"
    let r = t # 0
    let c = t % 0
    let cc = t %% "KEGG"
    putStrLn $ show r 
    putStrLn $ show c
    putStrLn $ show cc
    let tt = t >>= (setRow 3 r)
    putStrLn $ show $ tt#3 
    let ttt = t >>= (setColN "KEGG" c)
    putStrLn $ show $ ttt%%"KEGG"









update


 

 


darlliu
committed
Feb 27, 2015






229




230




231




232




233




234




235




236




237




238




239




    let ttt2 = ttt `addRow` r
    let ttt3 = ttt2 `addCol` ( "Added Column", ttt2 % 2)
    putStrLn "Testing Adding Column"
    putStrLn $ show $ ttt3 %% "Added Column"
    putStrLn "Testing sorting and filtering"
    putStrLn $ show (ttt % 16)
    putStrLn $ show $ sortM' text2Float 16 ttt
    let os =  filterM' (\x -> text2Float x  < 1) 16 ttt
    putStrLn $ show os
    putStrLn $ showTabularM $ (reorderM os ttt)
    toFile' "./txts/test_csv_out.txt" (reorderM os ttt)









update



 


darlliu
committed
Feb 26, 2015






240




    return ()














175fc91d31cd8168298958a76e118f1663633f28


Switch branch/tag










haskell


tabular


Tabular.hs



Find file
Normal viewHistoryPermalink






Tabular.hs



7.77 KB









Newer










Older









update



 


darlliu
committed
Feb 26, 2015






1




2




3




4




5




6




7




8




--Implementation of R like table object with IO to .tsv in Haskell

import Data.CSV.Conduit
import Data.ByteString.Char8 (ByteString, unpack, pack)
import qualified Data.Vector as V
import Control.Monad
import Control.Exception as E
import Data.List









update


 

 


darlliu
committed
Feb 27, 2015






9




10




import Data.Ord (comparing)
import System.IO









update



 


darlliu
committed
Feb 26, 2015






11




12




13




14




type Text = ByteString -- Change this to change the underlying datatype
type Tabular = V.Vector(V.Vector Text)
type TabularRow = V.Vector Text










update


 

 


darlliu
committed
Feb 27, 2015






15




-- File accessor









update



 


darlliu
committed
Feb 26, 2015






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




fromFileWith' sep quote fp= do
    let myOption = CSVSettings{csvSep=sep, csvQuoteChar=quote}
    csvData <- readCSVFile myOption fp :: IO ( V.Vector([Text]) )
    return (Just ( V.map V.fromList csvData))

fromFileWith sep quote fp = E.catch
    (fromFileWith' sep quote fp)
    (\e -> do 
     let err = show (e::IOException)
     putStrLn $ "Unexpected Error at opening file: "++ (show err)
     return Nothing
    )

fromFile = fromFileWith '\t' Nothing










update


 

 


darlliu
committed
Feb 27, 2015






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




-- Write to file as tsv

showTabular :: Tabular -> String
showTabular t = intercalate "\n" $ V.toList $ rows where
   rows = V.map (\x -> intercalate "\t" (V.toList (toString x))) t

showTabularM :: Maybe Tabular -> String
showTabularM Nothing = ""
showTabularM (Just t) = showTabular t

toFile fname t = do
    withFile fname WriteMode (\handle -> do
        hPutStr handle (showTabular t))

toFile' fname t = case t of 
    Nothing -> return ()
    Just tt -> toFile fname tt
-- Constructor with an ordering

reorder :: V.Vector Int -> Tabular -> Tabular
reorder os t = (header' t) `V.cons` (V.map (\x -> t V.! (x+1)) os)

reorderM = liftM2 reorder









update



 


darlliu
committed
Feb 26, 2015






54




55




56




-- Row accessor, get Nothing in case of error

header' t = t V.! 0









update


 

 


darlliu
committed
Feb 27, 2015






57




header t = V.map pack $ t V.! 0









update



 


darlliu
committed
Feb 26, 2015






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





(#!) :: Tabular -> Int -> Maybe TabularRow
t #! i = if i<0 || i> V.length t 
    then Nothing
    else Just (t V.! (i+1))
     --compensate for the header which we assume always is there
(#) :: (Maybe Tabular) -> Int -> Maybe TabularRow
t # i = t >>= (#! i)

-- Column accessor, would like it to get nothing in case of error

(%!) :: Tabular -> Int ->Maybe TabularRow
t %! i = if i < 0 || i > (V.length $ t V.! 0 ) 
    then Nothing
    else let vv = V.map (\x -> (x::TabularRow) V.! i) t in
        if V.length vv > 1 
            then Just (V.tail vv)
            else Nothing

(%) :: (Maybe Tabular) -> Int ->Maybe TabularRow
t % i = t >>= (%! i)


(%%!):: Tabular -> String -> Maybe TabularRow









update


 

 


darlliu
committed
Feb 27, 2015






82




83




84




85




t %%! s = let h = header' t in 
            let key = (pack s) `V.elemIndex` h in case key of
                Nothing -> Nothing
                Just idx -> t %! idx









update



 


darlliu
committed
Feb 26, 2015






86




87




88




89




90




91




92




93





(%%):: Maybe(Tabular) -> String -> Maybe TabularRow
t %% s = t >>= (%%! s)

--type coercer, would like it to crash in case of error

toFloat :: TabularRow ->(V.Vector Float)
toFloat xs = V.map (\x -> (read $ unpack x) :: Float) xs









update


 

 


darlliu
committed
Feb 27, 2015






94




95




toFloatM ::(Monad m )=> m TabularRow -> m (V.Vector Float)
toFloatM = liftM toFloat









update



 


darlliu
committed
Feb 26, 2015






96




97




98





toString :: TabularRow ->(V.Vector String)
toString xs = V.map (\x -> unpack x) xs









update


 

 


darlliu
committed
Feb 27, 2015






99




100




toStringM ::(Monad m)=> m TabularRow -> m (V.Vector String)
toStringM = liftM toString









update



 


darlliu
committed
Feb 26, 2015






101




102




103





toBool :: TabularRow ->(V.Vector Bool)
toBool xs = V.map (\x -> (read $ unpack x) :: Bool) xs









update


 

 


darlliu
committed
Feb 27, 2015






104




105




toBoolM :: (Monad m)=> m TabularRow -> m (V.Vector Bool)
toBoolM = liftM toBool









update



 


darlliu
committed
Feb 26, 2015






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




133




134




135




136




137




138




139




140




141




142




143




144





--modifiers 

setRow' :: Int -> TabularRow -> Tabular ->  Maybe Tabular
setRow' i r t = if i >= V.length t -1 || i < 0 || (V.length r) /= (V.length $ header' t)
            then Nothing
            else Just (t V.// [(i + 1, r)])
setRow :: Int -> Maybe TabularRow -> Tabular ->  Maybe Tabular
setRow _ Nothing _ = Nothing
setRow i (Just r) t = if i >= V.length t -1 || i < 0 || (V.length r) /= (V.length $ header' t)
            then Nothing
            else Just (t V.// [(i + 1, r)])

setCol' :: Int -> TabularRow -> Tabular -> Maybe Tabular
setCol' i c t = if i >= V.length (header' t) || i < 0 || (V.length c) /= V.length t -1
            then Nothing
            else Just ( V.zipWith (\x y -> x V.// [(i,y)]) t ( n `V.cons` c) ) where
                n = header' t V.! i
setCol :: Int ->Maybe TabularRow -> Tabular -> Maybe Tabular
setCol _ Nothing _= Nothing
setCol i (Just c) t = if i >= V.length (header' t) || i < 0 || (V.length c) /= V.length t -1
            then Nothing
            else Just ( V.zipWith (\x y -> x V.// [(i,y)]) t ( n  `V.cons` c) ) where
                n = header' t V.! i

setColN' :: String -> TabularRow -> Tabular -> Maybe Tabular
setColN' n c t = let nn = pack n in case nn `V.elemIndex` (header' t) of 
            Nothing -> Nothing
            Just i -> Just ( V.zipWith (\x y -> x V.// [(i,y)]) t ( nn `V.cons` c) )

setColN :: String ->Maybe TabularRow -> Tabular -> Maybe Tabular
setColN _ Nothing _= Nothing
setColN n (Just c) t = let nn = pack n in case nn `V.elemIndex` (header' t) of 
            Nothing -> Nothing
            Just i -> Just ( V.zipWith (\x y -> x V.// [(i,y)]) t ( nn `V.cons` c) )

--use >>= to modify

--populators









update


 

 


darlliu
committed
Feb 27, 2015






145




146




147




148




149




150




151




152




153




154




155




156




157




--addRow 

rowAdd' :: TabularRow -> Tabular -> Maybe Tabular

r `rowAdd'` t = if (V.length r) /= (V.length $ header' t)
    then Nothing
    else Just (t `V.snoc` r)

rowAdd :: Maybe TabularRow -> Maybe Tabular -> Maybe Tabular
Nothing `rowAdd` _ = Nothing
_ `rowAdd` Nothing = Nothing
(Just r) `rowAdd` (Just t) = r `rowAdd'` t
t `addRow` r = r `rowAdd` t









update



 


darlliu
committed
Feb 26, 2015






158




159




--addCol (Name separate) <<










update


 

 


darlliu
committed
Feb 27, 2015






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




172




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




191




192




193




194




195




196




197




198




199




200




201




202




203




204




205




206




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




colAdd' :: (String, TabularRow) -> Tabular -> Maybe Tabular

(n, c) `colAdd'` t = let nn = pack n in if V.length c /= V.length t - 1
    then Nothing
    else Just ( V.zipWith (\x y -> x `V.snoc` y) t ( nn `V.cons` c) )

colAdd :: (String, Maybe TabularRow) -> Maybe Tabular -> Maybe Tabular
(_, Nothing) `colAdd` _ = Nothing
(_, _ ) `colAdd` Nothing = Nothing
(n , Just c) `colAdd` (Just t) = (n,c) `colAdd'` t
t `addCol` c = c `colAdd` t


--Sort and Filter

filter' :: (Text -> Bool) -> Int -> Tabular -> V.Vector Int
filter' f i t =  let c = t %! i in case c of 
        Just cc -> V.findIndices f cc
        Nothing -> V.fromList []

filterM' :: (Monad m)=>(Text -> Bool) -> Int -> m Tabular -> m (V.Vector Int)
filterM' f i = liftM ( filter' f i ) 

filterN :: (Text -> Bool) -> String -> Tabular -> V.Vector Int
filterN f s t = let i = (pack s) `V.elemIndex` (header' t) in case i of 
    Nothing -> filter' f (-1) t
    Just ii -> filter' f ii t

filterNM :: (Monad m)=>(Text -> Bool) -> String -> m Tabular -> m (V.Vector Int)
filterNM f s = liftM (filterN f s)


sort' :: (Ord a) => (Text  -> a) -> Int -> Tabular -> V.Vector Int
sort' f i t = let c = (t %! i) in case c of 
    Nothing -> V.fromList $ take (V.length t) [0 ..]
    (Just cc) -> V.fromList $ map fst $ sortBy (comparing snd) (V.toList $ V.indexed $ V.map f cc)

sortM' :: (Ord a, Monad m) => (Text  -> a) -> Int -> m Tabular -> m (V.Vector Int)
sortM' f i = liftM (sort' f i)

sortN :: (Ord a) => (Text  -> a) -> String -> Tabular -> V.Vector Int
sortN f s t = let i = (pack s) `V.elemIndex` (header' t) in case i of 
    Nothing -> sort' f (-1) t
    Just ii -> sort' f ii t

sortNM :: (Ord a, Monad m) => (Text  -> a) -> String -> m Tabular -> m (V.Vector Int)
sortNM f s = liftM (sortN f s)

-- Utility

text2Float x = (read $ unpack x ):: Float

text2String x = unpack x 

text2Bool x = (read $ unpack x ):: Bool











update



 


darlliu
committed
Feb 26, 2015






217




218




219




220




221




222




223




224




225




226




227




228




main = do
    t <- fromFile "./txts/test_csv.txt"
    let r = t # 0
    let c = t % 0
    let cc = t %% "KEGG"
    putStrLn $ show r 
    putStrLn $ show c
    putStrLn $ show cc
    let tt = t >>= (setRow 3 r)
    putStrLn $ show $ tt#3 
    let ttt = t >>= (setColN "KEGG" c)
    putStrLn $ show $ ttt%%"KEGG"









update


 

 


darlliu
committed
Feb 27, 2015






229




230




231




232




233




234




235




236




237




238




239




    let ttt2 = ttt `addRow` r
    let ttt3 = ttt2 `addCol` ( "Added Column", ttt2 % 2)
    putStrLn "Testing Adding Column"
    putStrLn $ show $ ttt3 %% "Added Column"
    putStrLn "Testing sorting and filtering"
    putStrLn $ show (ttt % 16)
    putStrLn $ show $ sortM' text2Float 16 ttt
    let os =  filterM' (\x -> text2Float x  < 1) 16 ttt
    putStrLn $ show os
    putStrLn $ showTabularM $ (reorderM os ttt)
    toFile' "./txts/test_csv_out.txt" (reorderM os ttt)









update



 


darlliu
committed
Feb 26, 2015






240




    return ()










175fc91d31cd8168298958a76e118f1663633f28


Switch branch/tag










haskell


tabular


Tabular.hs



Find file
Normal viewHistoryPermalink




175fc91d31cd8168298958a76e118f1663633f28


Switch branch/tag










haskell


tabular


Tabular.hs





175fc91d31cd8168298958a76e118f1663633f28


Switch branch/tag








175fc91d31cd8168298958a76e118f1663633f28


Switch branch/tag





175fc91d31cd8168298958a76e118f1663633f28

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
haskell

tabular

Tabular.hs
Find file
Normal viewHistoryPermalink




Tabular.hs



7.77 KB









Newer










Older









update



 


darlliu
committed
Feb 26, 2015






1




2




3




4




5




6




7




8




--Implementation of R like table object with IO to .tsv in Haskell

import Data.CSV.Conduit
import Data.ByteString.Char8 (ByteString, unpack, pack)
import qualified Data.Vector as V
import Control.Monad
import Control.Exception as E
import Data.List









update


 

 


darlliu
committed
Feb 27, 2015






9




10




import Data.Ord (comparing)
import System.IO









update



 


darlliu
committed
Feb 26, 2015






11




12




13




14




type Text = ByteString -- Change this to change the underlying datatype
type Tabular = V.Vector(V.Vector Text)
type TabularRow = V.Vector Text










update


 

 


darlliu
committed
Feb 27, 2015






15




-- File accessor









update



 


darlliu
committed
Feb 26, 2015






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




fromFileWith' sep quote fp= do
    let myOption = CSVSettings{csvSep=sep, csvQuoteChar=quote}
    csvData <- readCSVFile myOption fp :: IO ( V.Vector([Text]) )
    return (Just ( V.map V.fromList csvData))

fromFileWith sep quote fp = E.catch
    (fromFileWith' sep quote fp)
    (\e -> do 
     let err = show (e::IOException)
     putStrLn $ "Unexpected Error at opening file: "++ (show err)
     return Nothing
    )

fromFile = fromFileWith '\t' Nothing










update


 

 


darlliu
committed
Feb 27, 2015






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




-- Write to file as tsv

showTabular :: Tabular -> String
showTabular t = intercalate "\n" $ V.toList $ rows where
   rows = V.map (\x -> intercalate "\t" (V.toList (toString x))) t

showTabularM :: Maybe Tabular -> String
showTabularM Nothing = ""
showTabularM (Just t) = showTabular t

toFile fname t = do
    withFile fname WriteMode (\handle -> do
        hPutStr handle (showTabular t))

toFile' fname t = case t of 
    Nothing -> return ()
    Just tt -> toFile fname tt
-- Constructor with an ordering

reorder :: V.Vector Int -> Tabular -> Tabular
reorder os t = (header' t) `V.cons` (V.map (\x -> t V.! (x+1)) os)

reorderM = liftM2 reorder









update



 


darlliu
committed
Feb 26, 2015






54




55




56




-- Row accessor, get Nothing in case of error

header' t = t V.! 0









update


 

 


darlliu
committed
Feb 27, 2015






57




header t = V.map pack $ t V.! 0









update



 


darlliu
committed
Feb 26, 2015






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





(#!) :: Tabular -> Int -> Maybe TabularRow
t #! i = if i<0 || i> V.length t 
    then Nothing
    else Just (t V.! (i+1))
     --compensate for the header which we assume always is there
(#) :: (Maybe Tabular) -> Int -> Maybe TabularRow
t # i = t >>= (#! i)

-- Column accessor, would like it to get nothing in case of error

(%!) :: Tabular -> Int ->Maybe TabularRow
t %! i = if i < 0 || i > (V.length $ t V.! 0 ) 
    then Nothing
    else let vv = V.map (\x -> (x::TabularRow) V.! i) t in
        if V.length vv > 1 
            then Just (V.tail vv)
            else Nothing

(%) :: (Maybe Tabular) -> Int ->Maybe TabularRow
t % i = t >>= (%! i)


(%%!):: Tabular -> String -> Maybe TabularRow









update


 

 


darlliu
committed
Feb 27, 2015






82




83




84




85




t %%! s = let h = header' t in 
            let key = (pack s) `V.elemIndex` h in case key of
                Nothing -> Nothing
                Just idx -> t %! idx









update



 


darlliu
committed
Feb 26, 2015






86




87




88




89




90




91




92




93





(%%):: Maybe(Tabular) -> String -> Maybe TabularRow
t %% s = t >>= (%%! s)

--type coercer, would like it to crash in case of error

toFloat :: TabularRow ->(V.Vector Float)
toFloat xs = V.map (\x -> (read $ unpack x) :: Float) xs









update


 

 


darlliu
committed
Feb 27, 2015






94




95




toFloatM ::(Monad m )=> m TabularRow -> m (V.Vector Float)
toFloatM = liftM toFloat









update



 


darlliu
committed
Feb 26, 2015






96




97




98





toString :: TabularRow ->(V.Vector String)
toString xs = V.map (\x -> unpack x) xs









update


 

 


darlliu
committed
Feb 27, 2015






99




100




toStringM ::(Monad m)=> m TabularRow -> m (V.Vector String)
toStringM = liftM toString









update



 


darlliu
committed
Feb 26, 2015






101




102




103





toBool :: TabularRow ->(V.Vector Bool)
toBool xs = V.map (\x -> (read $ unpack x) :: Bool) xs









update


 

 


darlliu
committed
Feb 27, 2015






104




105




toBoolM :: (Monad m)=> m TabularRow -> m (V.Vector Bool)
toBoolM = liftM toBool









update



 


darlliu
committed
Feb 26, 2015






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




133




134




135




136




137




138




139




140




141




142




143




144





--modifiers 

setRow' :: Int -> TabularRow -> Tabular ->  Maybe Tabular
setRow' i r t = if i >= V.length t -1 || i < 0 || (V.length r) /= (V.length $ header' t)
            then Nothing
            else Just (t V.// [(i + 1, r)])
setRow :: Int -> Maybe TabularRow -> Tabular ->  Maybe Tabular
setRow _ Nothing _ = Nothing
setRow i (Just r) t = if i >= V.length t -1 || i < 0 || (V.length r) /= (V.length $ header' t)
            then Nothing
            else Just (t V.// [(i + 1, r)])

setCol' :: Int -> TabularRow -> Tabular -> Maybe Tabular
setCol' i c t = if i >= V.length (header' t) || i < 0 || (V.length c) /= V.length t -1
            then Nothing
            else Just ( V.zipWith (\x y -> x V.// [(i,y)]) t ( n `V.cons` c) ) where
                n = header' t V.! i
setCol :: Int ->Maybe TabularRow -> Tabular -> Maybe Tabular
setCol _ Nothing _= Nothing
setCol i (Just c) t = if i >= V.length (header' t) || i < 0 || (V.length c) /= V.length t -1
            then Nothing
            else Just ( V.zipWith (\x y -> x V.// [(i,y)]) t ( n  `V.cons` c) ) where
                n = header' t V.! i

setColN' :: String -> TabularRow -> Tabular -> Maybe Tabular
setColN' n c t = let nn = pack n in case nn `V.elemIndex` (header' t) of 
            Nothing -> Nothing
            Just i -> Just ( V.zipWith (\x y -> x V.// [(i,y)]) t ( nn `V.cons` c) )

setColN :: String ->Maybe TabularRow -> Tabular -> Maybe Tabular
setColN _ Nothing _= Nothing
setColN n (Just c) t = let nn = pack n in case nn `V.elemIndex` (header' t) of 
            Nothing -> Nothing
            Just i -> Just ( V.zipWith (\x y -> x V.// [(i,y)]) t ( nn `V.cons` c) )

--use >>= to modify

--populators









update


 

 


darlliu
committed
Feb 27, 2015






145




146




147




148




149




150




151




152




153




154




155




156




157




--addRow 

rowAdd' :: TabularRow -> Tabular -> Maybe Tabular

r `rowAdd'` t = if (V.length r) /= (V.length $ header' t)
    then Nothing
    else Just (t `V.snoc` r)

rowAdd :: Maybe TabularRow -> Maybe Tabular -> Maybe Tabular
Nothing `rowAdd` _ = Nothing
_ `rowAdd` Nothing = Nothing
(Just r) `rowAdd` (Just t) = r `rowAdd'` t
t `addRow` r = r `rowAdd` t









update



 


darlliu
committed
Feb 26, 2015






158




159




--addCol (Name separate) <<










update


 

 


darlliu
committed
Feb 27, 2015






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




172




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




191




192




193




194




195




196




197




198




199




200




201




202




203




204




205




206




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




colAdd' :: (String, TabularRow) -> Tabular -> Maybe Tabular

(n, c) `colAdd'` t = let nn = pack n in if V.length c /= V.length t - 1
    then Nothing
    else Just ( V.zipWith (\x y -> x `V.snoc` y) t ( nn `V.cons` c) )

colAdd :: (String, Maybe TabularRow) -> Maybe Tabular -> Maybe Tabular
(_, Nothing) `colAdd` _ = Nothing
(_, _ ) `colAdd` Nothing = Nothing
(n , Just c) `colAdd` (Just t) = (n,c) `colAdd'` t
t `addCol` c = c `colAdd` t


--Sort and Filter

filter' :: (Text -> Bool) -> Int -> Tabular -> V.Vector Int
filter' f i t =  let c = t %! i in case c of 
        Just cc -> V.findIndices f cc
        Nothing -> V.fromList []

filterM' :: (Monad m)=>(Text -> Bool) -> Int -> m Tabular -> m (V.Vector Int)
filterM' f i = liftM ( filter' f i ) 

filterN :: (Text -> Bool) -> String -> Tabular -> V.Vector Int
filterN f s t = let i = (pack s) `V.elemIndex` (header' t) in case i of 
    Nothing -> filter' f (-1) t
    Just ii -> filter' f ii t

filterNM :: (Monad m)=>(Text -> Bool) -> String -> m Tabular -> m (V.Vector Int)
filterNM f s = liftM (filterN f s)


sort' :: (Ord a) => (Text  -> a) -> Int -> Tabular -> V.Vector Int
sort' f i t = let c = (t %! i) in case c of 
    Nothing -> V.fromList $ take (V.length t) [0 ..]
    (Just cc) -> V.fromList $ map fst $ sortBy (comparing snd) (V.toList $ V.indexed $ V.map f cc)

sortM' :: (Ord a, Monad m) => (Text  -> a) -> Int -> m Tabular -> m (V.Vector Int)
sortM' f i = liftM (sort' f i)

sortN :: (Ord a) => (Text  -> a) -> String -> Tabular -> V.Vector Int
sortN f s t = let i = (pack s) `V.elemIndex` (header' t) in case i of 
    Nothing -> sort' f (-1) t
    Just ii -> sort' f ii t

sortNM :: (Ord a, Monad m) => (Text  -> a) -> String -> m Tabular -> m (V.Vector Int)
sortNM f s = liftM (sortN f s)

-- Utility

text2Float x = (read $ unpack x ):: Float

text2String x = unpack x 

text2Bool x = (read $ unpack x ):: Bool











update



 


darlliu
committed
Feb 26, 2015






217




218




219




220




221




222




223




224




225




226




227




228




main = do
    t <- fromFile "./txts/test_csv.txt"
    let r = t # 0
    let c = t % 0
    let cc = t %% "KEGG"
    putStrLn $ show r 
    putStrLn $ show c
    putStrLn $ show cc
    let tt = t >>= (setRow 3 r)
    putStrLn $ show $ tt#3 
    let ttt = t >>= (setColN "KEGG" c)
    putStrLn $ show $ ttt%%"KEGG"









update


 

 


darlliu
committed
Feb 27, 2015






229




230




231




232




233




234




235




236




237




238




239




    let ttt2 = ttt `addRow` r
    let ttt3 = ttt2 `addCol` ( "Added Column", ttt2 % 2)
    putStrLn "Testing Adding Column"
    putStrLn $ show $ ttt3 %% "Added Column"
    putStrLn "Testing sorting and filtering"
    putStrLn $ show (ttt % 16)
    putStrLn $ show $ sortM' text2Float 16 ttt
    let os =  filterM' (\x -> text2Float x  < 1) 16 ttt
    putStrLn $ show os
    putStrLn $ showTabularM $ (reorderM os ttt)
    toFile' "./txts/test_csv_out.txt" (reorderM os ttt)









update



 


darlliu
committed
Feb 26, 2015






240




    return ()








Tabular.hs



7.77 KB










Tabular.hs



7.77 KB









Newer










Older
NewerOlder







update



 


darlliu
committed
Feb 26, 2015






1




2




3




4




5




6




7




8




--Implementation of R like table object with IO to .tsv in Haskell

import Data.CSV.Conduit
import Data.ByteString.Char8 (ByteString, unpack, pack)
import qualified Data.Vector as V
import Control.Monad
import Control.Exception as E
import Data.List









update


 

 


darlliu
committed
Feb 27, 2015






9




10




import Data.Ord (comparing)
import System.IO









update



 


darlliu
committed
Feb 26, 2015






11




12




13




14




type Text = ByteString -- Change this to change the underlying datatype
type Tabular = V.Vector(V.Vector Text)
type TabularRow = V.Vector Text










update


 

 


darlliu
committed
Feb 27, 2015






15




-- File accessor









update



 


darlliu
committed
Feb 26, 2015






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




fromFileWith' sep quote fp= do
    let myOption = CSVSettings{csvSep=sep, csvQuoteChar=quote}
    csvData <- readCSVFile myOption fp :: IO ( V.Vector([Text]) )
    return (Just ( V.map V.fromList csvData))

fromFileWith sep quote fp = E.catch
    (fromFileWith' sep quote fp)
    (\e -> do 
     let err = show (e::IOException)
     putStrLn $ "Unexpected Error at opening file: "++ (show err)
     return Nothing
    )

fromFile = fromFileWith '\t' Nothing










update


 

 


darlliu
committed
Feb 27, 2015






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




-- Write to file as tsv

showTabular :: Tabular -> String
showTabular t = intercalate "\n" $ V.toList $ rows where
   rows = V.map (\x -> intercalate "\t" (V.toList (toString x))) t

showTabularM :: Maybe Tabular -> String
showTabularM Nothing = ""
showTabularM (Just t) = showTabular t

toFile fname t = do
    withFile fname WriteMode (\handle -> do
        hPutStr handle (showTabular t))

toFile' fname t = case t of 
    Nothing -> return ()
    Just tt -> toFile fname tt
-- Constructor with an ordering

reorder :: V.Vector Int -> Tabular -> Tabular
reorder os t = (header' t) `V.cons` (V.map (\x -> t V.! (x+1)) os)

reorderM = liftM2 reorder









update



 


darlliu
committed
Feb 26, 2015






54




55




56




-- Row accessor, get Nothing in case of error

header' t = t V.! 0









update


 

 


darlliu
committed
Feb 27, 2015






57




header t = V.map pack $ t V.! 0









update



 


darlliu
committed
Feb 26, 2015






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





(#!) :: Tabular -> Int -> Maybe TabularRow
t #! i = if i<0 || i> V.length t 
    then Nothing
    else Just (t V.! (i+1))
     --compensate for the header which we assume always is there
(#) :: (Maybe Tabular) -> Int -> Maybe TabularRow
t # i = t >>= (#! i)

-- Column accessor, would like it to get nothing in case of error

(%!) :: Tabular -> Int ->Maybe TabularRow
t %! i = if i < 0 || i > (V.length $ t V.! 0 ) 
    then Nothing
    else let vv = V.map (\x -> (x::TabularRow) V.! i) t in
        if V.length vv > 1 
            then Just (V.tail vv)
            else Nothing

(%) :: (Maybe Tabular) -> Int ->Maybe TabularRow
t % i = t >>= (%! i)


(%%!):: Tabular -> String -> Maybe TabularRow









update


 

 


darlliu
committed
Feb 27, 2015






82




83




84




85




t %%! s = let h = header' t in 
            let key = (pack s) `V.elemIndex` h in case key of
                Nothing -> Nothing
                Just idx -> t %! idx









update



 


darlliu
committed
Feb 26, 2015






86




87




88




89




90




91




92




93





(%%):: Maybe(Tabular) -> String -> Maybe TabularRow
t %% s = t >>= (%%! s)

--type coercer, would like it to crash in case of error

toFloat :: TabularRow ->(V.Vector Float)
toFloat xs = V.map (\x -> (read $ unpack x) :: Float) xs









update


 

 


darlliu
committed
Feb 27, 2015






94




95




toFloatM ::(Monad m )=> m TabularRow -> m (V.Vector Float)
toFloatM = liftM toFloat









update



 


darlliu
committed
Feb 26, 2015






96




97




98





toString :: TabularRow ->(V.Vector String)
toString xs = V.map (\x -> unpack x) xs









update


 

 


darlliu
committed
Feb 27, 2015






99




100




toStringM ::(Monad m)=> m TabularRow -> m (V.Vector String)
toStringM = liftM toString









update



 


darlliu
committed
Feb 26, 2015






101




102




103





toBool :: TabularRow ->(V.Vector Bool)
toBool xs = V.map (\x -> (read $ unpack x) :: Bool) xs









update


 

 


darlliu
committed
Feb 27, 2015






104




105




toBoolM :: (Monad m)=> m TabularRow -> m (V.Vector Bool)
toBoolM = liftM toBool









update



 


darlliu
committed
Feb 26, 2015






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




133




134




135




136




137




138




139




140




141




142




143




144





--modifiers 

setRow' :: Int -> TabularRow -> Tabular ->  Maybe Tabular
setRow' i r t = if i >= V.length t -1 || i < 0 || (V.length r) /= (V.length $ header' t)
            then Nothing
            else Just (t V.// [(i + 1, r)])
setRow :: Int -> Maybe TabularRow -> Tabular ->  Maybe Tabular
setRow _ Nothing _ = Nothing
setRow i (Just r) t = if i >= V.length t -1 || i < 0 || (V.length r) /= (V.length $ header' t)
            then Nothing
            else Just (t V.// [(i + 1, r)])

setCol' :: Int -> TabularRow -> Tabular -> Maybe Tabular
setCol' i c t = if i >= V.length (header' t) || i < 0 || (V.length c) /= V.length t -1
            then Nothing
            else Just ( V.zipWith (\x y -> x V.// [(i,y)]) t ( n `V.cons` c) ) where
                n = header' t V.! i
setCol :: Int ->Maybe TabularRow -> Tabular -> Maybe Tabular
setCol _ Nothing _= Nothing
setCol i (Just c) t = if i >= V.length (header' t) || i < 0 || (V.length c) /= V.length t -1
            then Nothing
            else Just ( V.zipWith (\x y -> x V.// [(i,y)]) t ( n  `V.cons` c) ) where
                n = header' t V.! i

setColN' :: String -> TabularRow -> Tabular -> Maybe Tabular
setColN' n c t = let nn = pack n in case nn `V.elemIndex` (header' t) of 
            Nothing -> Nothing
            Just i -> Just ( V.zipWith (\x y -> x V.// [(i,y)]) t ( nn `V.cons` c) )

setColN :: String ->Maybe TabularRow -> Tabular -> Maybe Tabular
setColN _ Nothing _= Nothing
setColN n (Just c) t = let nn = pack n in case nn `V.elemIndex` (header' t) of 
            Nothing -> Nothing
            Just i -> Just ( V.zipWith (\x y -> x V.// [(i,y)]) t ( nn `V.cons` c) )

--use >>= to modify

--populators









update


 

 


darlliu
committed
Feb 27, 2015






145




146




147




148




149




150




151




152




153




154




155




156




157




--addRow 

rowAdd' :: TabularRow -> Tabular -> Maybe Tabular

r `rowAdd'` t = if (V.length r) /= (V.length $ header' t)
    then Nothing
    else Just (t `V.snoc` r)

rowAdd :: Maybe TabularRow -> Maybe Tabular -> Maybe Tabular
Nothing `rowAdd` _ = Nothing
_ `rowAdd` Nothing = Nothing
(Just r) `rowAdd` (Just t) = r `rowAdd'` t
t `addRow` r = r `rowAdd` t









update



 


darlliu
committed
Feb 26, 2015






158




159




--addCol (Name separate) <<










update


 

 


darlliu
committed
Feb 27, 2015






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




172




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




191




192




193




194




195




196




197




198




199




200




201




202




203




204




205




206




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




colAdd' :: (String, TabularRow) -> Tabular -> Maybe Tabular

(n, c) `colAdd'` t = let nn = pack n in if V.length c /= V.length t - 1
    then Nothing
    else Just ( V.zipWith (\x y -> x `V.snoc` y) t ( nn `V.cons` c) )

colAdd :: (String, Maybe TabularRow) -> Maybe Tabular -> Maybe Tabular
(_, Nothing) `colAdd` _ = Nothing
(_, _ ) `colAdd` Nothing = Nothing
(n , Just c) `colAdd` (Just t) = (n,c) `colAdd'` t
t `addCol` c = c `colAdd` t


--Sort and Filter

filter' :: (Text -> Bool) -> Int -> Tabular -> V.Vector Int
filter' f i t =  let c = t %! i in case c of 
        Just cc -> V.findIndices f cc
        Nothing -> V.fromList []

filterM' :: (Monad m)=>(Text -> Bool) -> Int -> m Tabular -> m (V.Vector Int)
filterM' f i = liftM ( filter' f i ) 

filterN :: (Text -> Bool) -> String -> Tabular -> V.Vector Int
filterN f s t = let i = (pack s) `V.elemIndex` (header' t) in case i of 
    Nothing -> filter' f (-1) t
    Just ii -> filter' f ii t

filterNM :: (Monad m)=>(Text -> Bool) -> String -> m Tabular -> m (V.Vector Int)
filterNM f s = liftM (filterN f s)


sort' :: (Ord a) => (Text  -> a) -> Int -> Tabular -> V.Vector Int
sort' f i t = let c = (t %! i) in case c of 
    Nothing -> V.fromList $ take (V.length t) [0 ..]
    (Just cc) -> V.fromList $ map fst $ sortBy (comparing snd) (V.toList $ V.indexed $ V.map f cc)

sortM' :: (Ord a, Monad m) => (Text  -> a) -> Int -> m Tabular -> m (V.Vector Int)
sortM' f i = liftM (sort' f i)

sortN :: (Ord a) => (Text  -> a) -> String -> Tabular -> V.Vector Int
sortN f s t = let i = (pack s) `V.elemIndex` (header' t) in case i of 
    Nothing -> sort' f (-1) t
    Just ii -> sort' f ii t

sortNM :: (Ord a, Monad m) => (Text  -> a) -> String -> m Tabular -> m (V.Vector Int)
sortNM f s = liftM (sortN f s)

-- Utility

text2Float x = (read $ unpack x ):: Float

text2String x = unpack x 

text2Bool x = (read $ unpack x ):: Bool











update



 


darlliu
committed
Feb 26, 2015






217




218




219




220




221




222




223




224




225




226




227




228




main = do
    t <- fromFile "./txts/test_csv.txt"
    let r = t # 0
    let c = t % 0
    let cc = t %% "KEGG"
    putStrLn $ show r 
    putStrLn $ show c
    putStrLn $ show cc
    let tt = t >>= (setRow 3 r)
    putStrLn $ show $ tt#3 
    let ttt = t >>= (setColN "KEGG" c)
    putStrLn $ show $ ttt%%"KEGG"









update


 

 


darlliu
committed
Feb 27, 2015






229




230




231




232




233




234




235




236




237




238




239




    let ttt2 = ttt `addRow` r
    let ttt3 = ttt2 `addCol` ( "Added Column", ttt2 % 2)
    putStrLn "Testing Adding Column"
    putStrLn $ show $ ttt3 %% "Added Column"
    putStrLn "Testing sorting and filtering"
    putStrLn $ show (ttt % 16)
    putStrLn $ show $ sortM' text2Float 16 ttt
    let os =  filterM' (\x -> text2Float x  < 1) 16 ttt
    putStrLn $ show os
    putStrLn $ showTabularM $ (reorderM os ttt)
    toFile' "./txts/test_csv_out.txt" (reorderM os ttt)









update



 


darlliu
committed
Feb 26, 2015






240




    return ()







update



 


darlliu
committed
Feb 26, 2015



update



 

update


darlliu
committed
Feb 26, 2015

1

2

3

4

5

6

7

8
--Implementation of R like table object with IO to .tsv in Haskell--Implementation of R like table object with IO to .tsv in Haskellimport Data.CSV.ConduitimportData.CSV.Conduitimport Data.ByteString.Char8 (ByteString, unpack, pack)importData.ByteString.Char8(ByteString,unpack,pack)import qualified Data.Vector as VimportqualifiedData.VectorasVimport Control.MonadimportControl.Monadimport Control.Exception as EimportControl.ExceptionasEimport Data.ListimportData.List



update


 

 


darlliu
committed
Feb 27, 2015



update


 

 

update

 

darlliu
committed
Feb 27, 2015

9

10
import Data.Ord (comparing)importData.Ord(comparing)import System.IOimportSystem.IO



update



 


darlliu
committed
Feb 26, 2015



update



 

update


darlliu
committed
Feb 26, 2015

11

12

13

14
type Text = ByteString -- Change this to change the underlying datatypetypeText=ByteString-- Change this to change the underlying datatypetype Tabular = V.Vector(V.Vector Text)typeTabular=V.Vector(V.VectorText)type TabularRow = V.Vector TexttypeTabularRow=V.VectorText



update


 

 


darlliu
committed
Feb 27, 2015



update


 

 

update

 

darlliu
committed
Feb 27, 2015

15
-- File accessor-- File accessor



update



 


darlliu
committed
Feb 26, 2015



update



 

update


darlliu
committed
Feb 26, 2015

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
fromFileWith' sep quote fp= dofromFileWith'sepquotefp=do    let myOption = CSVSettings{csvSep=sep, csvQuoteChar=quote}letmyOption=CSVSettings{csvSep=sep,csvQuoteChar=quote}    csvData <- readCSVFile myOption fp :: IO ( V.Vector([Text]) )csvData<-readCSVFilemyOptionfp::IO(V.Vector([Text]))    return (Just ( V.map V.fromList csvData))return(Just(V.mapV.fromListcsvData))fromFileWith sep quote fp = E.catchfromFileWithsepquotefp=E.catch    (fromFileWith' sep quote fp)(fromFileWith'sepquotefp)    (\e -> do (\e->do     let err = show (e::IOException)leterr=show(e::IOException)     putStrLn $ "Unexpected Error at opening file: "++ (show err)putStrLn$"Unexpected Error at opening file: "++(showerr)     return NothingreturnNothing    ))fromFile = fromFileWith '\t' NothingfromFile=fromFileWith'\t'Nothing



update


 

 


darlliu
committed
Feb 27, 2015



update


 

 

update

 

darlliu
committed
Feb 27, 2015

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
-- Write to file as tsv-- Write to file as tsvshowTabular :: Tabular -> StringshowTabular::Tabular->StringshowTabular t = intercalate "\n" $ V.toList $ rows whereshowTabulart=intercalate"\n"$V.toList$rowswhere   rows = V.map (\x -> intercalate "\t" (V.toList (toString x))) trows=V.map(\x->intercalate"\t"(V.toList(toStringx)))tshowTabularM :: Maybe Tabular -> StringshowTabularM::MaybeTabular->StringshowTabularM Nothing = ""showTabularMNothing=""showTabularM (Just t) = showTabular tshowTabularM(Justt)=showTabularttoFile fname t = dotoFilefnamet=do    withFile fname WriteMode (\handle -> dowithFilefnameWriteMode(\handle->do        hPutStr handle (showTabular t))hPutStrhandle(showTabulart))toFile' fname t = case t of toFile'fnamet=casetof    Nothing -> return ()Nothing->return()    Just tt -> toFile fname ttJusttt->toFilefnamett-- Constructor with an ordering-- Constructor with an orderingreorder :: V.Vector Int -> Tabular -> Tabularreorder::V.VectorInt->Tabular->Tabularreorder os t = (header' t) `V.cons` (V.map (\x -> t V.! (x+1)) os)reorderost=(header't)`V.cons`(V.map(\x->tV.!(x+1))os)reorderM = liftM2 reorderreorderM=liftM2reorder



update



 


darlliu
committed
Feb 26, 2015



update



 

update


darlliu
committed
Feb 26, 2015

54

55

56
-- Row accessor, get Nothing in case of error-- Row accessor, get Nothing in case of errorheader' t = t V.! 0header't=tV.!0



update


 

 


darlliu
committed
Feb 27, 2015



update


 

 

update

 

darlliu
committed
Feb 27, 2015

57
header t = V.map pack $ t V.! 0headert=V.mappack$tV.!0



update



 


darlliu
committed
Feb 26, 2015



update



 

update


darlliu
committed
Feb 26, 2015

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
(#!) :: Tabular -> Int -> Maybe TabularRow(#!)::Tabular->Int->MaybeTabularRowt #! i = if i<0 || i> V.length t t#!i=ifi<0||i>V.lengtht    then NothingthenNothing    else Just (t V.! (i+1))elseJust(tV.!(i+1))     --compensate for the header which we assume always is there--compensate for the header which we assume always is there(#) :: (Maybe Tabular) -> Int -> Maybe TabularRow(#)::(MaybeTabular)->Int->MaybeTabularRowt # i = t >>= (#! i)t#i=t>>=(#!i)-- Column accessor, would like it to get nothing in case of error-- Column accessor, would like it to get nothing in case of error(%!) :: Tabular -> Int ->Maybe TabularRow(%!)::Tabular->Int->MaybeTabularRowt %! i = if i < 0 || i > (V.length $ t V.! 0 ) t%!i=ifi<0||i>(V.length$tV.!0)    then NothingthenNothing    else let vv = V.map (\x -> (x::TabularRow) V.! i) t inelseletvv=V.map(\x->(x::TabularRow)V.!i)tin        if V.length vv > 1 ifV.lengthvv>1            then Just (V.tail vv)thenJust(V.tailvv)            else NothingelseNothing(%) :: (Maybe Tabular) -> Int ->Maybe TabularRow(%)::(MaybeTabular)->Int->MaybeTabularRowt % i = t >>= (%! i)t%i=t>>=(%!i)(%%!):: Tabular -> String -> Maybe TabularRow(%%!)::Tabular->String->MaybeTabularRow



update


 

 


darlliu
committed
Feb 27, 2015



update


 

 

update

 

darlliu
committed
Feb 27, 2015

82

83

84

85
t %%! s = let h = header' t in t%%!s=leth=header'tin            let key = (pack s) `V.elemIndex` h in case key ofletkey=(packs)`V.elemIndex`hincasekeyof                Nothing -> NothingNothing->Nothing                Just idx -> t %! idxJustidx->t%!idx



update



 


darlliu
committed
Feb 26, 2015



update



 

update


darlliu
committed
Feb 26, 2015

86

87

88

89

90

91

92

93
(%%):: Maybe(Tabular) -> String -> Maybe TabularRow(%%)::Maybe(Tabular)->String->MaybeTabularRowt %% s = t >>= (%%! s)t%%s=t>>=(%%!s)--type coercer, would like it to crash in case of error--type coercer, would like it to crash in case of errortoFloat :: TabularRow ->(V.Vector Float)toFloat::TabularRow->(V.VectorFloat)toFloat xs = V.map (\x -> (read $ unpack x) :: Float) xstoFloatxs=V.map(\x->(read$unpackx)::Float)xs



update


 

 


darlliu
committed
Feb 27, 2015



update


 

 

update

 

darlliu
committed
Feb 27, 2015

94

95
toFloatM ::(Monad m )=> m TabularRow -> m (V.Vector Float)toFloatM::(Monadm)=>mTabularRow->m(V.VectorFloat)toFloatM = liftM toFloattoFloatM=liftMtoFloat



update



 


darlliu
committed
Feb 26, 2015



update



 

update


darlliu
committed
Feb 26, 2015

96

97

98
toString :: TabularRow ->(V.Vector String)toString::TabularRow->(V.VectorString)toString xs = V.map (\x -> unpack x) xstoStringxs=V.map(\x->unpackx)xs



update


 

 


darlliu
committed
Feb 27, 2015



update


 

 

update

 

darlliu
committed
Feb 27, 2015

99

100
toStringM ::(Monad m)=> m TabularRow -> m (V.Vector String)toStringM::(Monadm)=>mTabularRow->m(V.VectorString)toStringM = liftM toStringtoStringM=liftMtoString



update



 


darlliu
committed
Feb 26, 2015



update



 

update


darlliu
committed
Feb 26, 2015

101

102

103
toBool :: TabularRow ->(V.Vector Bool)toBool::TabularRow->(V.VectorBool)toBool xs = V.map (\x -> (read $ unpack x) :: Bool) xstoBoolxs=V.map(\x->(read$unpackx)::Bool)xs



update


 

 


darlliu
committed
Feb 27, 2015



update


 

 

update

 

darlliu
committed
Feb 27, 2015

104

105
toBoolM :: (Monad m)=> m TabularRow -> m (V.Vector Bool)toBoolM::(Monadm)=>mTabularRow->m(V.VectorBool)toBoolM = liftM toBooltoBoolM=liftMtoBool



update



 


darlliu
committed
Feb 26, 2015



update



 

update


darlliu
committed
Feb 26, 2015

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

133

134

135

136

137

138

139

140

141

142

143

144
--modifiers --modifiers setRow' :: Int -> TabularRow -> Tabular ->  Maybe TabularsetRow'::Int->TabularRow->Tabular->MaybeTabularsetRow' i r t = if i >= V.length t -1 || i < 0 || (V.length r) /= (V.length $ header' t)setRow'irt=ifi>=V.lengtht-1||i<0||(V.lengthr)/=(V.length$header't)            then NothingthenNothing            else Just (t V.// [(i + 1, r)])elseJust(tV.//[(i+1,r)])setRow :: Int -> Maybe TabularRow -> Tabular ->  Maybe TabularsetRow::Int->MaybeTabularRow->Tabular->MaybeTabularsetRow _ Nothing _ = NothingsetRow_Nothing_=NothingsetRow i (Just r) t = if i >= V.length t -1 || i < 0 || (V.length r) /= (V.length $ header' t)setRowi(Justr)t=ifi>=V.lengtht-1||i<0||(V.lengthr)/=(V.length$header't)            then NothingthenNothing            else Just (t V.// [(i + 1, r)])elseJust(tV.//[(i+1,r)])setCol' :: Int -> TabularRow -> Tabular -> Maybe TabularsetCol'::Int->TabularRow->Tabular->MaybeTabularsetCol' i c t = if i >= V.length (header' t) || i < 0 || (V.length c) /= V.length t -1setCol'ict=ifi>=V.length(header't)||i<0||(V.lengthc)/=V.lengtht-1            then NothingthenNothing            else Just ( V.zipWith (\x y -> x V.// [(i,y)]) t ( n `V.cons` c) ) whereelseJust(V.zipWith(\xy->xV.//[(i,y)])t(n`V.cons`c))where                n = header' t V.! in=header'tV.!isetCol :: Int ->Maybe TabularRow -> Tabular -> Maybe TabularsetCol::Int->MaybeTabularRow->Tabular->MaybeTabularsetCol _ Nothing _= NothingsetCol_Nothing_=NothingsetCol i (Just c) t = if i >= V.length (header' t) || i < 0 || (V.length c) /= V.length t -1setColi(Justc)t=ifi>=V.length(header't)||i<0||(V.lengthc)/=V.lengtht-1            then NothingthenNothing            else Just ( V.zipWith (\x y -> x V.// [(i,y)]) t ( n  `V.cons` c) ) whereelseJust(V.zipWith(\xy->xV.//[(i,y)])t(n`V.cons`c))where                n = header' t V.! in=header'tV.!isetColN' :: String -> TabularRow -> Tabular -> Maybe TabularsetColN'::String->TabularRow->Tabular->MaybeTabularsetColN' n c t = let nn = pack n in case nn `V.elemIndex` (header' t) of setColN'nct=letnn=packnincasenn`V.elemIndex`(header't)of            Nothing -> NothingNothing->Nothing            Just i -> Just ( V.zipWith (\x y -> x V.// [(i,y)]) t ( nn `V.cons` c) )Justi->Just(V.zipWith(\xy->xV.//[(i,y)])t(nn`V.cons`c))setColN :: String ->Maybe TabularRow -> Tabular -> Maybe TabularsetColN::String->MaybeTabularRow->Tabular->MaybeTabularsetColN _ Nothing _= NothingsetColN_Nothing_=NothingsetColN n (Just c) t = let nn = pack n in case nn `V.elemIndex` (header' t) of setColNn(Justc)t=letnn=packnincasenn`V.elemIndex`(header't)of            Nothing -> NothingNothing->Nothing            Just i -> Just ( V.zipWith (\x y -> x V.// [(i,y)]) t ( nn `V.cons` c) )Justi->Just(V.zipWith(\xy->xV.//[(i,y)])t(nn`V.cons`c))--use >>= to modify--use >>= to modify--populators--populators



update


 

 


darlliu
committed
Feb 27, 2015



update


 

 

update

 

darlliu
committed
Feb 27, 2015

145

146

147

148

149

150

151

152

153

154

155

156

157
--addRow --addRow rowAdd' :: TabularRow -> Tabular -> Maybe TabularrowAdd'::TabularRow->Tabular->MaybeTabularr `rowAdd'` t = if (V.length r) /= (V.length $ header' t)r`rowAdd'`t=if(V.lengthr)/=(V.length$header't)    then NothingthenNothing    else Just (t `V.snoc` r)elseJust(t`V.snoc`r)rowAdd :: Maybe TabularRow -> Maybe Tabular -> Maybe TabularrowAdd::MaybeTabularRow->MaybeTabular->MaybeTabularNothing `rowAdd` _ = NothingNothing`rowAdd`_=Nothing_ `rowAdd` Nothing = Nothing_`rowAdd`Nothing=Nothing(Just r) `rowAdd` (Just t) = r `rowAdd'` t(Justr)`rowAdd`(Justt)=r`rowAdd'`tt `addRow` r = r `rowAdd` tt`addRow`r=r`rowAdd`t



update



 


darlliu
committed
Feb 26, 2015



update



 

update


darlliu
committed
Feb 26, 2015

158

159
--addCol (Name separate) <<--addCol (Name separate) <<



update


 

 


darlliu
committed
Feb 27, 2015



update


 

 

update

 

darlliu
committed
Feb 27, 2015

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

172

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

191

192

193

194

195

196

197

198

199

200

201

202

203

204

205

206

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
colAdd' :: (String, TabularRow) -> Tabular -> Maybe TabularcolAdd'::(String,TabularRow)->Tabular->MaybeTabular(n, c) `colAdd'` t = let nn = pack n in if V.length c /= V.length t - 1(n,c)`colAdd'`t=letnn=packninifV.lengthc/=V.lengtht-1    then NothingthenNothing    else Just ( V.zipWith (\x y -> x `V.snoc` y) t ( nn `V.cons` c) )elseJust(V.zipWith(\xy->x`V.snoc`y)t(nn`V.cons`c))colAdd :: (String, Maybe TabularRow) -> Maybe Tabular -> Maybe TabularcolAdd::(String,MaybeTabularRow)->MaybeTabular->MaybeTabular(_, Nothing) `colAdd` _ = Nothing(_,Nothing)`colAdd`_=Nothing(_, _ ) `colAdd` Nothing = Nothing(_,_)`colAdd`Nothing=Nothing(n , Just c) `colAdd` (Just t) = (n,c) `colAdd'` t(n,Justc)`colAdd`(Justt)=(n,c)`colAdd'`tt `addCol` c = c `colAdd` tt`addCol`c=c`colAdd`t--Sort and Filter--Sort and Filterfilter' :: (Text -> Bool) -> Int -> Tabular -> V.Vector Intfilter'::(Text->Bool)->Int->Tabular->V.VectorIntfilter' f i t =  let c = t %! i in case c of filter'fit=letc=t%!iincasecof        Just cc -> V.findIndices f ccJustcc->V.findIndicesfcc        Nothing -> V.fromList []Nothing->V.fromList[]filterM' :: (Monad m)=>(Text -> Bool) -> Int -> m Tabular -> m (V.Vector Int)filterM'::(Monadm)=>(Text->Bool)->Int->mTabular->m(V.VectorInt)filterM' f i = liftM ( filter' f i ) filterM'fi=liftM(filter'fi)filterN :: (Text -> Bool) -> String -> Tabular -> V.Vector IntfilterN::(Text->Bool)->String->Tabular->V.VectorIntfilterN f s t = let i = (pack s) `V.elemIndex` (header' t) in case i of filterNfst=leti=(packs)`V.elemIndex`(header't)incaseiof    Nothing -> filter' f (-1) tNothing->filter'f(-1)t    Just ii -> filter' f ii tJustii->filter'fiitfilterNM :: (Monad m)=>(Text -> Bool) -> String -> m Tabular -> m (V.Vector Int)filterNM::(Monadm)=>(Text->Bool)->String->mTabular->m(V.VectorInt)filterNM f s = liftM (filterN f s)filterNMfs=liftM(filterNfs)sort' :: (Ord a) => (Text  -> a) -> Int -> Tabular -> V.Vector Intsort'::(Orda)=>(Text->a)->Int->Tabular->V.VectorIntsort' f i t = let c = (t %! i) in case c of sort'fit=letc=(t%!i)incasecof    Nothing -> V.fromList $ take (V.length t) [0 ..]Nothing->V.fromList$take(V.lengtht)[0..]    (Just cc) -> V.fromList $ map fst $ sortBy (comparing snd) (V.toList $ V.indexed $ V.map f cc)(Justcc)->V.fromList$mapfst$sortBy(comparingsnd)(V.toList$V.indexed$V.mapfcc)sortM' :: (Ord a, Monad m) => (Text  -> a) -> Int -> m Tabular -> m (V.Vector Int)sortM'::(Orda,Monadm)=>(Text->a)->Int->mTabular->m(V.VectorInt)sortM' f i = liftM (sort' f i)sortM'fi=liftM(sort'fi)sortN :: (Ord a) => (Text  -> a) -> String -> Tabular -> V.Vector IntsortN::(Orda)=>(Text->a)->String->Tabular->V.VectorIntsortN f s t = let i = (pack s) `V.elemIndex` (header' t) in case i of sortNfst=leti=(packs)`V.elemIndex`(header't)incaseiof    Nothing -> sort' f (-1) tNothing->sort'f(-1)t    Just ii -> sort' f ii tJustii->sort'fiitsortNM :: (Ord a, Monad m) => (Text  -> a) -> String -> m Tabular -> m (V.Vector Int)sortNM::(Orda,Monadm)=>(Text->a)->String->mTabular->m(V.VectorInt)sortNM f s = liftM (sortN f s)sortNMfs=liftM(sortNfs)-- Utility-- Utilitytext2Float x = (read $ unpack x ):: Floattext2Floatx=(read$unpackx)::Floattext2String x = unpack x text2Stringx=unpackxtext2Bool x = (read $ unpack x ):: Booltext2Boolx=(read$unpackx)::Bool



update



 


darlliu
committed
Feb 26, 2015



update



 

update


darlliu
committed
Feb 26, 2015

217

218

219

220

221

222

223

224

225

226

227

228
main = domain=do    t <- fromFile "./txts/test_csv.txt"t<-fromFile"./txts/test_csv.txt"    let r = t # 0letr=t#0    let c = t % 0letc=t%0    let cc = t %% "KEGG"letcc=t%%"KEGG"    putStrLn $ show r putStrLn$showr    putStrLn $ show cputStrLn$showc    putStrLn $ show ccputStrLn$showcc    let tt = t >>= (setRow 3 r)lettt=t>>=(setRow3r)    putStrLn $ show $ tt#3 putStrLn$show$tt#3    let ttt = t >>= (setColN "KEGG" c)letttt=t>>=(setColN"KEGG"c)    putStrLn $ show $ ttt%%"KEGG"putStrLn$show$ttt%%"KEGG"



update


 

 


darlliu
committed
Feb 27, 2015



update


 

 

update

 

darlliu
committed
Feb 27, 2015

229

230

231

232

233

234

235

236

237

238

239
    let ttt2 = ttt `addRow` rletttt2=ttt`addRow`r    let ttt3 = ttt2 `addCol` ( "Added Column", ttt2 % 2)letttt3=ttt2`addCol`("Added Column",ttt2%2)    putStrLn "Testing Adding Column"putStrLn"Testing Adding Column"    putStrLn $ show $ ttt3 %% "Added Column"putStrLn$show$ttt3%%"Added Column"    putStrLn "Testing sorting and filtering"putStrLn"Testing sorting and filtering"    putStrLn $ show (ttt % 16)putStrLn$show(ttt%16)    putStrLn $ show $ sortM' text2Float 16 tttputStrLn$show$sortM'text2Float16ttt    let os =  filterM' (\x -> text2Float x  < 1) 16 tttletos=filterM'(\x->text2Floatx<1)16ttt    putStrLn $ show osputStrLn$showos    putStrLn $ showTabularM $ (reorderM os ttt)putStrLn$showTabularM$(reorderMosttt)    toFile' "./txts/test_csv_out.txt" (reorderM os ttt)toFile'"./txts/test_csv_out.txt"(reorderMosttt)



update



 


darlliu
committed
Feb 26, 2015



update



 

update


darlliu
committed
Feb 26, 2015

240
    return ()return()





