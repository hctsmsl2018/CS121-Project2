



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

39f0fc7b4174ff426be332835534494ab3ab0338

















39f0fc7b4174ff426be332835534494ab3ab0338


Switch branch/tag










haskell


test.hs



Find file
Normal viewHistoryPermalink






test.hs



3.56 KB









Newer










Older









initial stuff



 


darlliu
committed
Feb 19, 2013






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




158




159




160




161




162




163




import Data.List (isPrefixOf)
inc :: Int -> Int
inc 0 = 0
inc x = (inc (x-1))+x


--filter2::Ord a=>(a-> Bool) ->[a]->[a]
filter2 p = foldr (\x acc -> if p x then x :acc else acc ) 1


fib :: Int -> Int
fib 0=0
fib 1=1
fib x=fib(x-1)+fib(x-2)

term :: Int -> Double
term 0=0 
term 1=1
term 2=2
term (-1)=1
term (-2)=2
term x=if x<(-2) then term(x+1)* term (x+2) 
    else if (x>2) then term(x-1)*term(x-2) 
    else 0
termfib x = (term . fib)

dlts= foldr step [] . lines
    where step l ds
            |"#define" `isPrefixOf` l = secondWord l:ds
            |otherwise                = ds
          secondWord= head. tail. words

myfun 0=0
myfun 1=1
myfun 2=2
myfun x=if x>0 then myfun(x-1)^myfun(x-2)
    else 0
mylist []=[]
mylist (x:xs)=not(head xs):mylist(tail xs)

mylist3 Nope=Nope
mylist3 (Add x xs)=xs

myreduc Nope = 0
myreduc (Add x y)=x + myreduc (y)
mylist2 []=[]
mylist2 xs=d++mylist2(tail xs)
    where d=mylist xs

mymerge xs =foldl step [] xs
    where step x ys = ys:x

mymap f xs= foldr step [] xs
    where step x ys = x `seq` f x:ys
mymap2 f xs= foldr step [] xs
    where step x ys = f x:ys
mycomp = (odd `mymap`) . mymerge 
mycomp2 = mymerge . (odd `mymap`)  
mycomp3 = length.mycomp2
--note : composite operator is right evaluated.
myfoldadd xs=foldl (\s t->s `seq` t `seq` s*t) 1 xs
myfoldadd2 xs=foldl (\s t-> s*t) 1 xs

class MyClass a where
    eql::a->a->Bool
    add::a->a->a
    fibz::a->a
instance MyClass Bool where
    eql True True = True
    eql False False = True
    eql _ _ = False
    add True False = True
    add False True = True
    add True True = True
    add _ _ = False
    fibz True = True
    fibz False = False

instance MyClass Int where
    eql x y = x==y
    add x y = x+y
    fibz x = fib x
        --deriving (Eq, Ord, Show)

data Tryouts = Try1 {
    tries1::String,
    tries2::String}
    |Try2 Int String
    |Try3 Int Int3
    deriving (Show)

instance MyClass Tryouts where
    eql x y = tries1 x==tries1 y
    add x y = Try1 (tries1 x) (tries2 y)
    fibz x = Try1 (tries1 x++tries2 x) (tries2 x ++ tries1 x)

data Flags=Flag1 Int Float | Flag2 Float Int
           deriving (Show)


data Marks=Mark1 Int [Int] 
    | Mark2 [Int] Int
    | Mark3 [Int] [Int]
    | Mark4 [Float] [Int]
           deriving (Show)

-- closure
--foo :: Num -> Num -> (Num -> Num)
--foo x y = let r = x / y
--          in (\z -> z + r)
--let binds r to the lambda which contains another argument z 
--f :: Num -> Num
--f = foo 1 0
--here invokes with x y
--foo is the closure, f is the function
--main = print (f 123)
--here invokes with z


data MyMaybe a = MyJust a
    |Nope2
    deriving(Show)
data Templated a b c=Templated a b c
    |Templated2 {
        geta::a, 
        getb::b, 
        getc::c}
    |Templated3 a String c
    deriving (Show)
data Mylist a =Add a (Mylist a)
    | Nope
    deriving (Show)
type Banners=(Flags,Marks)
type Int3=Int
myadd 0 _ = 1
myadd _ 0 = 1
myadd a b 
 | a<0||b<0   = if b==0 then 0 else 0
 | otherwise  = a+b+ (myadd (a-1) (b-1))
derp=herp
    where {
        herp=kerp
            where {
                kerp=1;
                herp=2
            };
        derp=1;
        derp2=case derp of{
            1 -> 1;
            2 -> 0}
    }

mysum xs
    | xs==[]    =1
mysum xs = length xs + mysum(tail(xs))

ran1 :: Int->Int->Int
ran1 times seed
    |times==1    =seed
    |times>1     =(1122345223*(ran1 newtimes seed)+12345) `mod` 54321
    where newtimes=times-1
main=print (term 35 , fib 35)













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

39f0fc7b4174ff426be332835534494ab3ab0338

















39f0fc7b4174ff426be332835534494ab3ab0338


Switch branch/tag










haskell


test.hs



Find file
Normal viewHistoryPermalink






test.hs



3.56 KB









Newer










Older









initial stuff



 


darlliu
committed
Feb 19, 2013






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




158




159




160




161




162




163




import Data.List (isPrefixOf)
inc :: Int -> Int
inc 0 = 0
inc x = (inc (x-1))+x


--filter2::Ord a=>(a-> Bool) ->[a]->[a]
filter2 p = foldr (\x acc -> if p x then x :acc else acc ) 1


fib :: Int -> Int
fib 0=0
fib 1=1
fib x=fib(x-1)+fib(x-2)

term :: Int -> Double
term 0=0 
term 1=1
term 2=2
term (-1)=1
term (-2)=2
term x=if x<(-2) then term(x+1)* term (x+2) 
    else if (x>2) then term(x-1)*term(x-2) 
    else 0
termfib x = (term . fib)

dlts= foldr step [] . lines
    where step l ds
            |"#define" `isPrefixOf` l = secondWord l:ds
            |otherwise                = ds
          secondWord= head. tail. words

myfun 0=0
myfun 1=1
myfun 2=2
myfun x=if x>0 then myfun(x-1)^myfun(x-2)
    else 0
mylist []=[]
mylist (x:xs)=not(head xs):mylist(tail xs)

mylist3 Nope=Nope
mylist3 (Add x xs)=xs

myreduc Nope = 0
myreduc (Add x y)=x + myreduc (y)
mylist2 []=[]
mylist2 xs=d++mylist2(tail xs)
    where d=mylist xs

mymerge xs =foldl step [] xs
    where step x ys = ys:x

mymap f xs= foldr step [] xs
    where step x ys = x `seq` f x:ys
mymap2 f xs= foldr step [] xs
    where step x ys = f x:ys
mycomp = (odd `mymap`) . mymerge 
mycomp2 = mymerge . (odd `mymap`)  
mycomp3 = length.mycomp2
--note : composite operator is right evaluated.
myfoldadd xs=foldl (\s t->s `seq` t `seq` s*t) 1 xs
myfoldadd2 xs=foldl (\s t-> s*t) 1 xs

class MyClass a where
    eql::a->a->Bool
    add::a->a->a
    fibz::a->a
instance MyClass Bool where
    eql True True = True
    eql False False = True
    eql _ _ = False
    add True False = True
    add False True = True
    add True True = True
    add _ _ = False
    fibz True = True
    fibz False = False

instance MyClass Int where
    eql x y = x==y
    add x y = x+y
    fibz x = fib x
        --deriving (Eq, Ord, Show)

data Tryouts = Try1 {
    tries1::String,
    tries2::String}
    |Try2 Int String
    |Try3 Int Int3
    deriving (Show)

instance MyClass Tryouts where
    eql x y = tries1 x==tries1 y
    add x y = Try1 (tries1 x) (tries2 y)
    fibz x = Try1 (tries1 x++tries2 x) (tries2 x ++ tries1 x)

data Flags=Flag1 Int Float | Flag2 Float Int
           deriving (Show)


data Marks=Mark1 Int [Int] 
    | Mark2 [Int] Int
    | Mark3 [Int] [Int]
    | Mark4 [Float] [Int]
           deriving (Show)

-- closure
--foo :: Num -> Num -> (Num -> Num)
--foo x y = let r = x / y
--          in (\z -> z + r)
--let binds r to the lambda which contains another argument z 
--f :: Num -> Num
--f = foo 1 0
--here invokes with x y
--foo is the closure, f is the function
--main = print (f 123)
--here invokes with z


data MyMaybe a = MyJust a
    |Nope2
    deriving(Show)
data Templated a b c=Templated a b c
    |Templated2 {
        geta::a, 
        getb::b, 
        getc::c}
    |Templated3 a String c
    deriving (Show)
data Mylist a =Add a (Mylist a)
    | Nope
    deriving (Show)
type Banners=(Flags,Marks)
type Int3=Int
myadd 0 _ = 1
myadd _ 0 = 1
myadd a b 
 | a<0||b<0   = if b==0 then 0 else 0
 | otherwise  = a+b+ (myadd (a-1) (b-1))
derp=herp
    where {
        herp=kerp
            where {
                kerp=1;
                herp=2
            };
        derp=1;
        derp2=case derp of{
            1 -> 1;
            2 -> 0}
    }

mysum xs
    | xs==[]    =1
mysum xs = length xs + mysum(tail(xs))

ran1 :: Int->Int->Int
ran1 times seed
    |times==1    =seed
    |times>1     =(1122345223*(ran1 newtimes seed)+12345) `mod` 54321
    where newtimes=times-1
main=print (term 35 , fib 35)












Open sidebar



Yu Liu haskell

39f0fc7b4174ff426be332835534494ab3ab0338







Open sidebar



Yu Liu haskell

39f0fc7b4174ff426be332835534494ab3ab0338




Open sidebar

Yu Liu haskell

39f0fc7b4174ff426be332835534494ab3ab0338


Yu Liuhaskellhaskell
39f0fc7b4174ff426be332835534494ab3ab0338










39f0fc7b4174ff426be332835534494ab3ab0338


Switch branch/tag










haskell


test.hs



Find file
Normal viewHistoryPermalink






test.hs



3.56 KB









Newer










Older









initial stuff



 


darlliu
committed
Feb 19, 2013






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




158




159




160




161




162




163




import Data.List (isPrefixOf)
inc :: Int -> Int
inc 0 = 0
inc x = (inc (x-1))+x


--filter2::Ord a=>(a-> Bool) ->[a]->[a]
filter2 p = foldr (\x acc -> if p x then x :acc else acc ) 1


fib :: Int -> Int
fib 0=0
fib 1=1
fib x=fib(x-1)+fib(x-2)

term :: Int -> Double
term 0=0 
term 1=1
term 2=2
term (-1)=1
term (-2)=2
term x=if x<(-2) then term(x+1)* term (x+2) 
    else if (x>2) then term(x-1)*term(x-2) 
    else 0
termfib x = (term . fib)

dlts= foldr step [] . lines
    where step l ds
            |"#define" `isPrefixOf` l = secondWord l:ds
            |otherwise                = ds
          secondWord= head. tail. words

myfun 0=0
myfun 1=1
myfun 2=2
myfun x=if x>0 then myfun(x-1)^myfun(x-2)
    else 0
mylist []=[]
mylist (x:xs)=not(head xs):mylist(tail xs)

mylist3 Nope=Nope
mylist3 (Add x xs)=xs

myreduc Nope = 0
myreduc (Add x y)=x + myreduc (y)
mylist2 []=[]
mylist2 xs=d++mylist2(tail xs)
    where d=mylist xs

mymerge xs =foldl step [] xs
    where step x ys = ys:x

mymap f xs= foldr step [] xs
    where step x ys = x `seq` f x:ys
mymap2 f xs= foldr step [] xs
    where step x ys = f x:ys
mycomp = (odd `mymap`) . mymerge 
mycomp2 = mymerge . (odd `mymap`)  
mycomp3 = length.mycomp2
--note : composite operator is right evaluated.
myfoldadd xs=foldl (\s t->s `seq` t `seq` s*t) 1 xs
myfoldadd2 xs=foldl (\s t-> s*t) 1 xs

class MyClass a where
    eql::a->a->Bool
    add::a->a->a
    fibz::a->a
instance MyClass Bool where
    eql True True = True
    eql False False = True
    eql _ _ = False
    add True False = True
    add False True = True
    add True True = True
    add _ _ = False
    fibz True = True
    fibz False = False

instance MyClass Int where
    eql x y = x==y
    add x y = x+y
    fibz x = fib x
        --deriving (Eq, Ord, Show)

data Tryouts = Try1 {
    tries1::String,
    tries2::String}
    |Try2 Int String
    |Try3 Int Int3
    deriving (Show)

instance MyClass Tryouts where
    eql x y = tries1 x==tries1 y
    add x y = Try1 (tries1 x) (tries2 y)
    fibz x = Try1 (tries1 x++tries2 x) (tries2 x ++ tries1 x)

data Flags=Flag1 Int Float | Flag2 Float Int
           deriving (Show)


data Marks=Mark1 Int [Int] 
    | Mark2 [Int] Int
    | Mark3 [Int] [Int]
    | Mark4 [Float] [Int]
           deriving (Show)

-- closure
--foo :: Num -> Num -> (Num -> Num)
--foo x y = let r = x / y
--          in (\z -> z + r)
--let binds r to the lambda which contains another argument z 
--f :: Num -> Num
--f = foo 1 0
--here invokes with x y
--foo is the closure, f is the function
--main = print (f 123)
--here invokes with z


data MyMaybe a = MyJust a
    |Nope2
    deriving(Show)
data Templated a b c=Templated a b c
    |Templated2 {
        geta::a, 
        getb::b, 
        getc::c}
    |Templated3 a String c
    deriving (Show)
data Mylist a =Add a (Mylist a)
    | Nope
    deriving (Show)
type Banners=(Flags,Marks)
type Int3=Int
myadd 0 _ = 1
myadd _ 0 = 1
myadd a b 
 | a<0||b<0   = if b==0 then 0 else 0
 | otherwise  = a+b+ (myadd (a-1) (b-1))
derp=herp
    where {
        herp=kerp
            where {
                kerp=1;
                herp=2
            };
        derp=1;
        derp2=case derp of{
            1 -> 1;
            2 -> 0}
    }

mysum xs
    | xs==[]    =1
mysum xs = length xs + mysum(tail(xs))

ran1 :: Int->Int->Int
ran1 times seed
    |times==1    =seed
    |times>1     =(1122345223*(ran1 newtimes seed)+12345) `mod` 54321
    where newtimes=times-1
main=print (term 35 , fib 35)















39f0fc7b4174ff426be332835534494ab3ab0338


Switch branch/tag










haskell


test.hs



Find file
Normal viewHistoryPermalink






test.hs



3.56 KB









Newer










Older









initial stuff



 


darlliu
committed
Feb 19, 2013






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




158




159




160




161




162




163




import Data.List (isPrefixOf)
inc :: Int -> Int
inc 0 = 0
inc x = (inc (x-1))+x


--filter2::Ord a=>(a-> Bool) ->[a]->[a]
filter2 p = foldr (\x acc -> if p x then x :acc else acc ) 1


fib :: Int -> Int
fib 0=0
fib 1=1
fib x=fib(x-1)+fib(x-2)

term :: Int -> Double
term 0=0 
term 1=1
term 2=2
term (-1)=1
term (-2)=2
term x=if x<(-2) then term(x+1)* term (x+2) 
    else if (x>2) then term(x-1)*term(x-2) 
    else 0
termfib x = (term . fib)

dlts= foldr step [] . lines
    where step l ds
            |"#define" `isPrefixOf` l = secondWord l:ds
            |otherwise                = ds
          secondWord= head. tail. words

myfun 0=0
myfun 1=1
myfun 2=2
myfun x=if x>0 then myfun(x-1)^myfun(x-2)
    else 0
mylist []=[]
mylist (x:xs)=not(head xs):mylist(tail xs)

mylist3 Nope=Nope
mylist3 (Add x xs)=xs

myreduc Nope = 0
myreduc (Add x y)=x + myreduc (y)
mylist2 []=[]
mylist2 xs=d++mylist2(tail xs)
    where d=mylist xs

mymerge xs =foldl step [] xs
    where step x ys = ys:x

mymap f xs= foldr step [] xs
    where step x ys = x `seq` f x:ys
mymap2 f xs= foldr step [] xs
    where step x ys = f x:ys
mycomp = (odd `mymap`) . mymerge 
mycomp2 = mymerge . (odd `mymap`)  
mycomp3 = length.mycomp2
--note : composite operator is right evaluated.
myfoldadd xs=foldl (\s t->s `seq` t `seq` s*t) 1 xs
myfoldadd2 xs=foldl (\s t-> s*t) 1 xs

class MyClass a where
    eql::a->a->Bool
    add::a->a->a
    fibz::a->a
instance MyClass Bool where
    eql True True = True
    eql False False = True
    eql _ _ = False
    add True False = True
    add False True = True
    add True True = True
    add _ _ = False
    fibz True = True
    fibz False = False

instance MyClass Int where
    eql x y = x==y
    add x y = x+y
    fibz x = fib x
        --deriving (Eq, Ord, Show)

data Tryouts = Try1 {
    tries1::String,
    tries2::String}
    |Try2 Int String
    |Try3 Int Int3
    deriving (Show)

instance MyClass Tryouts where
    eql x y = tries1 x==tries1 y
    add x y = Try1 (tries1 x) (tries2 y)
    fibz x = Try1 (tries1 x++tries2 x) (tries2 x ++ tries1 x)

data Flags=Flag1 Int Float | Flag2 Float Int
           deriving (Show)


data Marks=Mark1 Int [Int] 
    | Mark2 [Int] Int
    | Mark3 [Int] [Int]
    | Mark4 [Float] [Int]
           deriving (Show)

-- closure
--foo :: Num -> Num -> (Num -> Num)
--foo x y = let r = x / y
--          in (\z -> z + r)
--let binds r to the lambda which contains another argument z 
--f :: Num -> Num
--f = foo 1 0
--here invokes with x y
--foo is the closure, f is the function
--main = print (f 123)
--here invokes with z


data MyMaybe a = MyJust a
    |Nope2
    deriving(Show)
data Templated a b c=Templated a b c
    |Templated2 {
        geta::a, 
        getb::b, 
        getc::c}
    |Templated3 a String c
    deriving (Show)
data Mylist a =Add a (Mylist a)
    | Nope
    deriving (Show)
type Banners=(Flags,Marks)
type Int3=Int
myadd 0 _ = 1
myadd _ 0 = 1
myadd a b 
 | a<0||b<0   = if b==0 then 0 else 0
 | otherwise  = a+b+ (myadd (a-1) (b-1))
derp=herp
    where {
        herp=kerp
            where {
                kerp=1;
                herp=2
            };
        derp=1;
        derp2=case derp of{
            1 -> 1;
            2 -> 0}
    }

mysum xs
    | xs==[]    =1
mysum xs = length xs + mysum(tail(xs))

ran1 :: Int->Int->Int
ran1 times seed
    |times==1    =seed
    |times>1     =(1122345223*(ran1 newtimes seed)+12345) `mod` 54321
    where newtimes=times-1
main=print (term 35 , fib 35)











39f0fc7b4174ff426be332835534494ab3ab0338


Switch branch/tag










haskell


test.hs



Find file
Normal viewHistoryPermalink




39f0fc7b4174ff426be332835534494ab3ab0338


Switch branch/tag










haskell


test.hs





39f0fc7b4174ff426be332835534494ab3ab0338


Switch branch/tag








39f0fc7b4174ff426be332835534494ab3ab0338


Switch branch/tag





39f0fc7b4174ff426be332835534494ab3ab0338

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
haskell

test.hs
Find file
Normal viewHistoryPermalink




test.hs



3.56 KB









Newer










Older









initial stuff



 


darlliu
committed
Feb 19, 2013






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




158




159




160




161




162




163




import Data.List (isPrefixOf)
inc :: Int -> Int
inc 0 = 0
inc x = (inc (x-1))+x


--filter2::Ord a=>(a-> Bool) ->[a]->[a]
filter2 p = foldr (\x acc -> if p x then x :acc else acc ) 1


fib :: Int -> Int
fib 0=0
fib 1=1
fib x=fib(x-1)+fib(x-2)

term :: Int -> Double
term 0=0 
term 1=1
term 2=2
term (-1)=1
term (-2)=2
term x=if x<(-2) then term(x+1)* term (x+2) 
    else if (x>2) then term(x-1)*term(x-2) 
    else 0
termfib x = (term . fib)

dlts= foldr step [] . lines
    where step l ds
            |"#define" `isPrefixOf` l = secondWord l:ds
            |otherwise                = ds
          secondWord= head. tail. words

myfun 0=0
myfun 1=1
myfun 2=2
myfun x=if x>0 then myfun(x-1)^myfun(x-2)
    else 0
mylist []=[]
mylist (x:xs)=not(head xs):mylist(tail xs)

mylist3 Nope=Nope
mylist3 (Add x xs)=xs

myreduc Nope = 0
myreduc (Add x y)=x + myreduc (y)
mylist2 []=[]
mylist2 xs=d++mylist2(tail xs)
    where d=mylist xs

mymerge xs =foldl step [] xs
    where step x ys = ys:x

mymap f xs= foldr step [] xs
    where step x ys = x `seq` f x:ys
mymap2 f xs= foldr step [] xs
    where step x ys = f x:ys
mycomp = (odd `mymap`) . mymerge 
mycomp2 = mymerge . (odd `mymap`)  
mycomp3 = length.mycomp2
--note : composite operator is right evaluated.
myfoldadd xs=foldl (\s t->s `seq` t `seq` s*t) 1 xs
myfoldadd2 xs=foldl (\s t-> s*t) 1 xs

class MyClass a where
    eql::a->a->Bool
    add::a->a->a
    fibz::a->a
instance MyClass Bool where
    eql True True = True
    eql False False = True
    eql _ _ = False
    add True False = True
    add False True = True
    add True True = True
    add _ _ = False
    fibz True = True
    fibz False = False

instance MyClass Int where
    eql x y = x==y
    add x y = x+y
    fibz x = fib x
        --deriving (Eq, Ord, Show)

data Tryouts = Try1 {
    tries1::String,
    tries2::String}
    |Try2 Int String
    |Try3 Int Int3
    deriving (Show)

instance MyClass Tryouts where
    eql x y = tries1 x==tries1 y
    add x y = Try1 (tries1 x) (tries2 y)
    fibz x = Try1 (tries1 x++tries2 x) (tries2 x ++ tries1 x)

data Flags=Flag1 Int Float | Flag2 Float Int
           deriving (Show)


data Marks=Mark1 Int [Int] 
    | Mark2 [Int] Int
    | Mark3 [Int] [Int]
    | Mark4 [Float] [Int]
           deriving (Show)

-- closure
--foo :: Num -> Num -> (Num -> Num)
--foo x y = let r = x / y
--          in (\z -> z + r)
--let binds r to the lambda which contains another argument z 
--f :: Num -> Num
--f = foo 1 0
--here invokes with x y
--foo is the closure, f is the function
--main = print (f 123)
--here invokes with z


data MyMaybe a = MyJust a
    |Nope2
    deriving(Show)
data Templated a b c=Templated a b c
    |Templated2 {
        geta::a, 
        getb::b, 
        getc::c}
    |Templated3 a String c
    deriving (Show)
data Mylist a =Add a (Mylist a)
    | Nope
    deriving (Show)
type Banners=(Flags,Marks)
type Int3=Int
myadd 0 _ = 1
myadd _ 0 = 1
myadd a b 
 | a<0||b<0   = if b==0 then 0 else 0
 | otherwise  = a+b+ (myadd (a-1) (b-1))
derp=herp
    where {
        herp=kerp
            where {
                kerp=1;
                herp=2
            };
        derp=1;
        derp2=case derp of{
            1 -> 1;
            2 -> 0}
    }

mysum xs
    | xs==[]    =1
mysum xs = length xs + mysum(tail(xs))

ran1 :: Int->Int->Int
ran1 times seed
    |times==1    =seed
    |times>1     =(1122345223*(ran1 newtimes seed)+12345) `mod` 54321
    where newtimes=times-1
main=print (term 35 , fib 35)









test.hs



3.56 KB










test.hs



3.56 KB









Newer










Older
NewerOlder







initial stuff



 


darlliu
committed
Feb 19, 2013






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




158




159




160




161




162




163




import Data.List (isPrefixOf)
inc :: Int -> Int
inc 0 = 0
inc x = (inc (x-1))+x


--filter2::Ord a=>(a-> Bool) ->[a]->[a]
filter2 p = foldr (\x acc -> if p x then x :acc else acc ) 1


fib :: Int -> Int
fib 0=0
fib 1=1
fib x=fib(x-1)+fib(x-2)

term :: Int -> Double
term 0=0 
term 1=1
term 2=2
term (-1)=1
term (-2)=2
term x=if x<(-2) then term(x+1)* term (x+2) 
    else if (x>2) then term(x-1)*term(x-2) 
    else 0
termfib x = (term . fib)

dlts= foldr step [] . lines
    where step l ds
            |"#define" `isPrefixOf` l = secondWord l:ds
            |otherwise                = ds
          secondWord= head. tail. words

myfun 0=0
myfun 1=1
myfun 2=2
myfun x=if x>0 then myfun(x-1)^myfun(x-2)
    else 0
mylist []=[]
mylist (x:xs)=not(head xs):mylist(tail xs)

mylist3 Nope=Nope
mylist3 (Add x xs)=xs

myreduc Nope = 0
myreduc (Add x y)=x + myreduc (y)
mylist2 []=[]
mylist2 xs=d++mylist2(tail xs)
    where d=mylist xs

mymerge xs =foldl step [] xs
    where step x ys = ys:x

mymap f xs= foldr step [] xs
    where step x ys = x `seq` f x:ys
mymap2 f xs= foldr step [] xs
    where step x ys = f x:ys
mycomp = (odd `mymap`) . mymerge 
mycomp2 = mymerge . (odd `mymap`)  
mycomp3 = length.mycomp2
--note : composite operator is right evaluated.
myfoldadd xs=foldl (\s t->s `seq` t `seq` s*t) 1 xs
myfoldadd2 xs=foldl (\s t-> s*t) 1 xs

class MyClass a where
    eql::a->a->Bool
    add::a->a->a
    fibz::a->a
instance MyClass Bool where
    eql True True = True
    eql False False = True
    eql _ _ = False
    add True False = True
    add False True = True
    add True True = True
    add _ _ = False
    fibz True = True
    fibz False = False

instance MyClass Int where
    eql x y = x==y
    add x y = x+y
    fibz x = fib x
        --deriving (Eq, Ord, Show)

data Tryouts = Try1 {
    tries1::String,
    tries2::String}
    |Try2 Int String
    |Try3 Int Int3
    deriving (Show)

instance MyClass Tryouts where
    eql x y = tries1 x==tries1 y
    add x y = Try1 (tries1 x) (tries2 y)
    fibz x = Try1 (tries1 x++tries2 x) (tries2 x ++ tries1 x)

data Flags=Flag1 Int Float | Flag2 Float Int
           deriving (Show)


data Marks=Mark1 Int [Int] 
    | Mark2 [Int] Int
    | Mark3 [Int] [Int]
    | Mark4 [Float] [Int]
           deriving (Show)

-- closure
--foo :: Num -> Num -> (Num -> Num)
--foo x y = let r = x / y
--          in (\z -> z + r)
--let binds r to the lambda which contains another argument z 
--f :: Num -> Num
--f = foo 1 0
--here invokes with x y
--foo is the closure, f is the function
--main = print (f 123)
--here invokes with z


data MyMaybe a = MyJust a
    |Nope2
    deriving(Show)
data Templated a b c=Templated a b c
    |Templated2 {
        geta::a, 
        getb::b, 
        getc::c}
    |Templated3 a String c
    deriving (Show)
data Mylist a =Add a (Mylist a)
    | Nope
    deriving (Show)
type Banners=(Flags,Marks)
type Int3=Int
myadd 0 _ = 1
myadd _ 0 = 1
myadd a b 
 | a<0||b<0   = if b==0 then 0 else 0
 | otherwise  = a+b+ (myadd (a-1) (b-1))
derp=herp
    where {
        herp=kerp
            where {
                kerp=1;
                herp=2
            };
        derp=1;
        derp2=case derp of{
            1 -> 1;
            2 -> 0}
    }

mysum xs
    | xs==[]    =1
mysum xs = length xs + mysum(tail(xs))

ran1 :: Int->Int->Int
ran1 times seed
    |times==1    =seed
    |times>1     =(1122345223*(ran1 newtimes seed)+12345) `mod` 54321
    where newtimes=times-1
main=print (term 35 , fib 35)








initial stuff



 


darlliu
committed
Feb 19, 2013



initial stuff



 

initial stuff


darlliu
committed
Feb 19, 2013

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

158

159

160

161

162

163
import Data.List (isPrefixOf)importData.List(isPrefixOf)inc :: Int -> Intinc::Int->Intinc 0 = 0inc0=0inc x = (inc (x-1))+xincx=(inc(x-1))+x--filter2::Ord a=>(a-> Bool) ->[a]->[a]--filter2::Ord a=>(a-> Bool) ->[a]->[a]filter2 p = foldr (\x acc -> if p x then x :acc else acc ) 1filter2p=foldr(\xacc->ifpxthenx:accelseacc)1fib :: Int -> Intfib::Int->Intfib 0=0fib0=0fib 1=1fib1=1fib x=fib(x-1)+fib(x-2)fibx=fib(x-1)+fib(x-2)term :: Int -> Doubleterm::Int->Doubleterm 0=0 term0=0term 1=1term1=1term 2=2term2=2term (-1)=1term(-1)=1term (-2)=2term(-2)=2term x=if x<(-2) then term(x+1)* term (x+2) termx=ifx<(-2)thenterm(x+1)*term(x+2)    else if (x>2) then term(x-1)*term(x-2) elseif(x>2)thenterm(x-1)*term(x-2)    else 0else0termfib x = (term . fib)termfibx=(term.fib)dlts= foldr step [] . linesdlts=foldrstep[].lines    where step l dswheresteplds            |"#define" `isPrefixOf` l = secondWord l:ds|"#define"`isPrefixOf`l=secondWordl:ds            |otherwise                = ds|otherwise=ds          secondWord= head. tail. wordssecondWord=head.tail.wordsmyfun 0=0myfun0=0myfun 1=1myfun1=1myfun 2=2myfun2=2myfun x=if x>0 then myfun(x-1)^myfun(x-2)myfunx=ifx>0thenmyfun(x-1)^myfun(x-2)    else 0else0mylist []=[]mylist[]=[]mylist (x:xs)=not(head xs):mylist(tail xs)mylist(x:xs)=not(headxs):mylist(tailxs)mylist3 Nope=Nopemylist3Nope=Nopemylist3 (Add x xs)=xsmylist3(Addxxs)=xsmyreduc Nope = 0myreducNope=0myreduc (Add x y)=x + myreduc (y)myreduc(Addxy)=x+myreduc(y)mylist2 []=[]mylist2[]=[]mylist2 xs=d++mylist2(tail xs)mylist2xs=d++mylist2(tailxs)    where d=mylist xswhered=mylistxsmymerge xs =foldl step [] xsmymergexs=foldlstep[]xs    where step x ys = ys:xwherestepxys=ys:xmymap f xs= foldr step [] xsmymapfxs=foldrstep[]xs    where step x ys = x `seq` f x:yswherestepxys=x`seq`fx:ysmymap2 f xs= foldr step [] xsmymap2fxs=foldrstep[]xs    where step x ys = f x:yswherestepxys=fx:ysmycomp = (odd `mymap`) . mymerge mycomp=(odd`mymap`).mymergemycomp2 = mymerge . (odd `mymap`)  mycomp2=mymerge.(odd`mymap`)mycomp3 = length.mycomp2mycomp3=length.mycomp2--note : composite operator is right evaluated.--note : composite operator is right evaluated.myfoldadd xs=foldl (\s t->s `seq` t `seq` s*t) 1 xsmyfoldaddxs=foldl(\st->s`seq`t`seq`s*t)1xsmyfoldadd2 xs=foldl (\s t-> s*t) 1 xsmyfoldadd2xs=foldl(\st->s*t)1xsclass MyClass a whereclassMyClassawhere    eql::a->a->Booleql::a->a->Bool    add::a->a->aadd::a->a->a    fibz::a->afibz::a->ainstance MyClass Bool whereinstanceMyClassBoolwhere    eql True True = TrueeqlTrueTrue=True    eql False False = TrueeqlFalseFalse=True    eql _ _ = Falseeql__=False    add True False = TrueaddTrueFalse=True    add False True = TrueaddFalseTrue=True    add True True = TrueaddTrueTrue=True    add _ _ = Falseadd__=False    fibz True = TruefibzTrue=True    fibz False = FalsefibzFalse=Falseinstance MyClass Int whereinstanceMyClassIntwhere    eql x y = x==yeqlxy=x==y    add x y = x+yaddxy=x+y    fibz x = fib xfibzx=fibx        --deriving (Eq, Ord, Show)--deriving (Eq, Ord, Show)data Tryouts = Try1 {dataTryouts=Try1{    tries1::String,tries1::String,    tries2::String}tries2::String}    |Try2 Int String|Try2IntString    |Try3 Int Int3|Try3IntInt3    deriving (Show)deriving(Show)instance MyClass Tryouts whereinstanceMyClassTryoutswhere    eql x y = tries1 x==tries1 yeqlxy=tries1x==tries1y    add x y = Try1 (tries1 x) (tries2 y)addxy=Try1(tries1x)(tries2y)    fibz x = Try1 (tries1 x++tries2 x) (tries2 x ++ tries1 x)fibzx=Try1(tries1x++tries2x)(tries2x++tries1x)data Flags=Flag1 Int Float | Flag2 Float IntdataFlags=Flag1IntFloat|Flag2FloatInt           deriving (Show)deriving(Show)data Marks=Mark1 Int [Int] dataMarks=Mark1Int[Int]    | Mark2 [Int] Int|Mark2[Int]Int    | Mark3 [Int] [Int]|Mark3[Int][Int]    | Mark4 [Float] [Int]|Mark4[Float][Int]           deriving (Show)deriving(Show)-- closure-- closure--foo :: Num -> Num -> (Num -> Num)--foo :: Num -> Num -> (Num -> Num)--foo x y = let r = x / y--foo x y = let r = x / y--          in (\z -> z + r)--          in (\z -> z + r)--let binds r to the lambda which contains another argument z --let binds r to the lambda which contains another argument z --f :: Num -> Num--f :: Num -> Num--f = foo 1 0--f = foo 1 0--here invokes with x y--here invokes with x y--foo is the closure, f is the function--foo is the closure, f is the function--main = print (f 123)--main = print (f 123)--here invokes with z--here invokes with zdata MyMaybe a = MyJust adataMyMaybea=MyJusta    |Nope2|Nope2    deriving(Show)deriving(Show)data Templated a b c=Templated a b cdataTemplatedabc=Templatedabc    |Templated2 {|Templated2{        geta::a, geta::a,        getb::b, getb::b,        getc::c}getc::c}    |Templated3 a String c|Templated3aStringc    deriving (Show)deriving(Show)data Mylist a =Add a (Mylist a)dataMylista=Adda(Mylista)    | Nope|Nope    deriving (Show)deriving(Show)type Banners=(Flags,Marks)typeBanners=(Flags,Marks)type Int3=InttypeInt3=Intmyadd 0 _ = 1myadd0_=1myadd _ 0 = 1myadd_0=1myadd a b myaddab | a<0||b<0   = if b==0 then 0 else 0|a<0||b<0=ifb==0then0else0 | otherwise  = a+b+ (myadd (a-1) (b-1))|otherwise=a+b+(myadd(a-1)(b-1))derp=herpderp=herp    where {where{        herp=kerpherp=kerp            where {where{                kerp=1;kerp=1;                herp=2herp=2            };};        derp=1;derp=1;        derp2=case derp of{derp2=casederpof{            1 -> 1;1->1;            2 -> 0}2->0}    }}mysum xsmysumxs    | xs==[]    =1|xs==[]=1mysum xs = length xs + mysum(tail(xs))mysumxs=lengthxs+mysum(tail(xs))ran1 :: Int->Int->Intran1::Int->Int->Intran1 times seedran1timesseed    |times==1    =seed|times==1=seed    |times>1     =(1122345223*(ran1 newtimes seed)+12345) `mod` 54321|times>1=(1122345223*(ran1newtimesseed)+12345)`mod`54321    where newtimes=times-1wherenewtimes=times-1main=print (term 35 , fib 35)main=print(term35,fib35)





