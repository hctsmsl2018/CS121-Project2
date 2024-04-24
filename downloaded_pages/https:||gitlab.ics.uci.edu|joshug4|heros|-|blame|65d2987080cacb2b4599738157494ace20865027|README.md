



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

65d2987080cacb2b4599738157494ace20865027

















65d2987080cacb2b4599738157494ace20865027


Switch branch/tag










heros


README.md



Find file
Normal viewHistoryPermalink







README.md



4.26 KB









Newer










Older









docs


 

 


Eric Bodden
committed
Nov 29, 2012






1




2




3




Heros IFDS/IDE Solver
=====================
Heros is a generic implementation of an IFDS/IDE Solver that can be plugged into existing, Java-based program analysis frameworks. A reference connector exists for the [Soot](http://www.sable.mcgill.ca/soot/) framework.









.


 

 


Eric Bodden
committed
Nov 14, 2012






4














.


 

 


Eric Bodden
committed
Nov 29, 2012






5




6




7




8




9




10




Heros...
* supports solving both IFDS and IDE problems,
* is multi-threaded and thus highly scalable,
* provides a simple programming interface, and
* is fully generic, i.e., can be used to formulate program analysis problems for different programming languages.










docs


 

 


Eric Bodden
committed
Nov 29, 2012






11




12




13




14




15




16




Who are the developers of Heros?
--------------------------------
Heros was developed and is maintained by [Eric Bodden](http://bodden.de/).

Why is Heros called Heros?
--------------------------









.


 

 


Eric Bodden
committed
Nov 29, 2012






17




The name contains (in a different order) the first characters of the last names Reps, Horwitz and Sagiv, the original inventors of the IFDS/IDE frameworks. Heros is pronounced like the Greek word, not like heroes in English.









docs


 

 


Eric Bodden
committed
Nov 29, 2012






18




19




20





What is IFDS/IDE in the first place?
------------------------------------









.


 

 


Eric Bodden
committed
Nov 29, 2012






21




22




[IFDS](http://dx.doi.org/10.1145/199448.199462) is a general framework for solving inter-procedural, finite, distributive subset problems in a flow-sensitive, fully context-sensitive manner. From a user's perspective, IFDS allows static program analysis in a template-driven manner. Users simply define flow functions for an analysis problem but don't need to worry about solving the analysis problem. The latter is automatically taken care of by the solver, in this case by Heros.
[IDE](http://dx.doi.org/10.1016/0304-3975(96)00072-2) is an extension of IFDS that allows more expressive computations. Heros implements an IDE solver and supports IFDS problems as special cases of IDE.









docs


 

 


Eric Bodden
committed
Nov 29, 2012






23




24




25





What are the unique features of Heros over other IFDS/IDE solvers?
------------------------------------------------------------------









.


 

 


Eric Bodden
committed
Nov 29, 2012






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




To the best of our knowledge there exist at least two other similar solvers implemented in Java. [Wala](http://wala.sf.net/) implements a solver that supports IFDS but not IDE. The solver is highly scalable but in our eyes requires more verbose definitions of client analyses. Heros is fully multi-threaded, while Wala's solver is not. There also exists a Scala-based solver by [Nomair A. Naeem, Ondrej Lhotak, and Jonathan Rodriguez](http://dx.doi.org/10.1007/978-3-642-11970-5_8). This implementation does support IDE, and there exists a multi-threaded version of it, but as of yet the implementation is not publicly available.

Why did you create Heros?
-------------------------
One reason is that we found that no existing IFDS/IDE solver satisfied all our needs. The solver in Wala was available but only supported IFDs. Moreover we desired a more simple client interface. The solver by Naeem, Lhotak and Rodriguez was written in Scala. Further we wanted a solver that could be used with multiple programming languages.

The second, and probably better reason is that we found that IFDS/IDE is really useful and that probably there should be a solver that the community can build on, extend and improve over the years. But this requires clean code and documentation. When designing Heros we took special care to provide just that.

What is this all about support for multiple programming languages?
------------------------------------------------------------------
Solving an IFDS/IDE analysis problem basically requires three things:
1. An IFDS/IDE solver.
2. An implementation of an inter-procedural control-flow graph (ICFG).
3. The definition of an IFDS/IDE analysis problem in the form of flow functions.

The solver in heros is fully generic. It can be combined with any form of ICFG. Through Java's generic type variables, Heros abstracts from any concrete types such as statements and methods. To connect Heros to a program-analysis framework for a particular language, all one needs to do is to implement a special version of the ICFG. We provide a reference implementation for Soot. Also the IFDS/IDE analysis problems need to be defined with respect to the actual programming language's constructs and semantics. They are not generic. The entire solver, however, can be reused as is. We are currently working on connecting Heros to a C/C++ compiler.









docs


 

 


Eric Bodden
committed
Nov 29, 2012






42




43




44




45





Under what License can I use Heros?
-----------------------------------
Heros is released under LGPL - see [LICENSE.txt](LICENSE.txt) for details.












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

65d2987080cacb2b4599738157494ace20865027

















65d2987080cacb2b4599738157494ace20865027


Switch branch/tag










heros


README.md



Find file
Normal viewHistoryPermalink







README.md



4.26 KB









Newer










Older









docs


 

 


Eric Bodden
committed
Nov 29, 2012






1




2




3




Heros IFDS/IDE Solver
=====================
Heros is a generic implementation of an IFDS/IDE Solver that can be plugged into existing, Java-based program analysis frameworks. A reference connector exists for the [Soot](http://www.sable.mcgill.ca/soot/) framework.









.


 

 


Eric Bodden
committed
Nov 14, 2012






4














.


 

 


Eric Bodden
committed
Nov 29, 2012






5




6




7




8




9




10




Heros...
* supports solving both IFDS and IDE problems,
* is multi-threaded and thus highly scalable,
* provides a simple programming interface, and
* is fully generic, i.e., can be used to formulate program analysis problems for different programming languages.










docs


 

 


Eric Bodden
committed
Nov 29, 2012






11




12




13




14




15




16




Who are the developers of Heros?
--------------------------------
Heros was developed and is maintained by [Eric Bodden](http://bodden.de/).

Why is Heros called Heros?
--------------------------









.


 

 


Eric Bodden
committed
Nov 29, 2012






17




The name contains (in a different order) the first characters of the last names Reps, Horwitz and Sagiv, the original inventors of the IFDS/IDE frameworks. Heros is pronounced like the Greek word, not like heroes in English.









docs


 

 


Eric Bodden
committed
Nov 29, 2012






18




19




20





What is IFDS/IDE in the first place?
------------------------------------









.


 

 


Eric Bodden
committed
Nov 29, 2012






21




22




[IFDS](http://dx.doi.org/10.1145/199448.199462) is a general framework for solving inter-procedural, finite, distributive subset problems in a flow-sensitive, fully context-sensitive manner. From a user's perspective, IFDS allows static program analysis in a template-driven manner. Users simply define flow functions for an analysis problem but don't need to worry about solving the analysis problem. The latter is automatically taken care of by the solver, in this case by Heros.
[IDE](http://dx.doi.org/10.1016/0304-3975(96)00072-2) is an extension of IFDS that allows more expressive computations. Heros implements an IDE solver and supports IFDS problems as special cases of IDE.









docs


 

 


Eric Bodden
committed
Nov 29, 2012






23




24




25





What are the unique features of Heros over other IFDS/IDE solvers?
------------------------------------------------------------------









.


 

 


Eric Bodden
committed
Nov 29, 2012






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




To the best of our knowledge there exist at least two other similar solvers implemented in Java. [Wala](http://wala.sf.net/) implements a solver that supports IFDS but not IDE. The solver is highly scalable but in our eyes requires more verbose definitions of client analyses. Heros is fully multi-threaded, while Wala's solver is not. There also exists a Scala-based solver by [Nomair A. Naeem, Ondrej Lhotak, and Jonathan Rodriguez](http://dx.doi.org/10.1007/978-3-642-11970-5_8). This implementation does support IDE, and there exists a multi-threaded version of it, but as of yet the implementation is not publicly available.

Why did you create Heros?
-------------------------
One reason is that we found that no existing IFDS/IDE solver satisfied all our needs. The solver in Wala was available but only supported IFDs. Moreover we desired a more simple client interface. The solver by Naeem, Lhotak and Rodriguez was written in Scala. Further we wanted a solver that could be used with multiple programming languages.

The second, and probably better reason is that we found that IFDS/IDE is really useful and that probably there should be a solver that the community can build on, extend and improve over the years. But this requires clean code and documentation. When designing Heros we took special care to provide just that.

What is this all about support for multiple programming languages?
------------------------------------------------------------------
Solving an IFDS/IDE analysis problem basically requires three things:
1. An IFDS/IDE solver.
2. An implementation of an inter-procedural control-flow graph (ICFG).
3. The definition of an IFDS/IDE analysis problem in the form of flow functions.

The solver in heros is fully generic. It can be combined with any form of ICFG. Through Java's generic type variables, Heros abstracts from any concrete types such as statements and methods. To connect Heros to a program-analysis framework for a particular language, all one needs to do is to implement a special version of the ICFG. We provide a reference implementation for Soot. Also the IFDS/IDE analysis problems need to be defined with respect to the actual programming language's constructs and semantics. They are not generic. The entire solver, however, can be reused as is. We are currently working on connecting Heros to a C/C++ compiler.









docs


 

 


Eric Bodden
committed
Nov 29, 2012






42




43




44




45





Under what License can I use Heros?
-----------------------------------
Heros is released under LGPL - see [LICENSE.txt](LICENSE.txt) for details.











Open sidebar



Joshua Garcia heros

65d2987080cacb2b4599738157494ace20865027







Open sidebar



Joshua Garcia heros

65d2987080cacb2b4599738157494ace20865027




Open sidebar

Joshua Garcia heros

65d2987080cacb2b4599738157494ace20865027


Joshua Garciaherosheros
65d2987080cacb2b4599738157494ace20865027










65d2987080cacb2b4599738157494ace20865027


Switch branch/tag










heros


README.md



Find file
Normal viewHistoryPermalink







README.md



4.26 KB









Newer










Older









docs


 

 


Eric Bodden
committed
Nov 29, 2012






1




2




3




Heros IFDS/IDE Solver
=====================
Heros is a generic implementation of an IFDS/IDE Solver that can be plugged into existing, Java-based program analysis frameworks. A reference connector exists for the [Soot](http://www.sable.mcgill.ca/soot/) framework.









.


 

 


Eric Bodden
committed
Nov 14, 2012






4














.


 

 


Eric Bodden
committed
Nov 29, 2012






5




6




7




8




9




10




Heros...
* supports solving both IFDS and IDE problems,
* is multi-threaded and thus highly scalable,
* provides a simple programming interface, and
* is fully generic, i.e., can be used to formulate program analysis problems for different programming languages.










docs


 

 


Eric Bodden
committed
Nov 29, 2012






11




12




13




14




15




16




Who are the developers of Heros?
--------------------------------
Heros was developed and is maintained by [Eric Bodden](http://bodden.de/).

Why is Heros called Heros?
--------------------------









.


 

 


Eric Bodden
committed
Nov 29, 2012






17




The name contains (in a different order) the first characters of the last names Reps, Horwitz and Sagiv, the original inventors of the IFDS/IDE frameworks. Heros is pronounced like the Greek word, not like heroes in English.









docs


 

 


Eric Bodden
committed
Nov 29, 2012






18




19




20





What is IFDS/IDE in the first place?
------------------------------------









.


 

 


Eric Bodden
committed
Nov 29, 2012






21




22




[IFDS](http://dx.doi.org/10.1145/199448.199462) is a general framework for solving inter-procedural, finite, distributive subset problems in a flow-sensitive, fully context-sensitive manner. From a user's perspective, IFDS allows static program analysis in a template-driven manner. Users simply define flow functions for an analysis problem but don't need to worry about solving the analysis problem. The latter is automatically taken care of by the solver, in this case by Heros.
[IDE](http://dx.doi.org/10.1016/0304-3975(96)00072-2) is an extension of IFDS that allows more expressive computations. Heros implements an IDE solver and supports IFDS problems as special cases of IDE.









docs


 

 


Eric Bodden
committed
Nov 29, 2012






23




24




25





What are the unique features of Heros over other IFDS/IDE solvers?
------------------------------------------------------------------









.


 

 


Eric Bodden
committed
Nov 29, 2012






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




To the best of our knowledge there exist at least two other similar solvers implemented in Java. [Wala](http://wala.sf.net/) implements a solver that supports IFDS but not IDE. The solver is highly scalable but in our eyes requires more verbose definitions of client analyses. Heros is fully multi-threaded, while Wala's solver is not. There also exists a Scala-based solver by [Nomair A. Naeem, Ondrej Lhotak, and Jonathan Rodriguez](http://dx.doi.org/10.1007/978-3-642-11970-5_8). This implementation does support IDE, and there exists a multi-threaded version of it, but as of yet the implementation is not publicly available.

Why did you create Heros?
-------------------------
One reason is that we found that no existing IFDS/IDE solver satisfied all our needs. The solver in Wala was available but only supported IFDs. Moreover we desired a more simple client interface. The solver by Naeem, Lhotak and Rodriguez was written in Scala. Further we wanted a solver that could be used with multiple programming languages.

The second, and probably better reason is that we found that IFDS/IDE is really useful and that probably there should be a solver that the community can build on, extend and improve over the years. But this requires clean code and documentation. When designing Heros we took special care to provide just that.

What is this all about support for multiple programming languages?
------------------------------------------------------------------
Solving an IFDS/IDE analysis problem basically requires three things:
1. An IFDS/IDE solver.
2. An implementation of an inter-procedural control-flow graph (ICFG).
3. The definition of an IFDS/IDE analysis problem in the form of flow functions.

The solver in heros is fully generic. It can be combined with any form of ICFG. Through Java's generic type variables, Heros abstracts from any concrete types such as statements and methods. To connect Heros to a program-analysis framework for a particular language, all one needs to do is to implement a special version of the ICFG. We provide a reference implementation for Soot. Also the IFDS/IDE analysis problems need to be defined with respect to the actual programming language's constructs and semantics. They are not generic. The entire solver, however, can be reused as is. We are currently working on connecting Heros to a C/C++ compiler.









docs


 

 


Eric Bodden
committed
Nov 29, 2012






42




43




44




45





Under what License can I use Heros?
-----------------------------------
Heros is released under LGPL - see [LICENSE.txt](LICENSE.txt) for details.














65d2987080cacb2b4599738157494ace20865027


Switch branch/tag










heros


README.md



Find file
Normal viewHistoryPermalink







README.md



4.26 KB









Newer










Older









docs


 

 


Eric Bodden
committed
Nov 29, 2012






1




2




3




Heros IFDS/IDE Solver
=====================
Heros is a generic implementation of an IFDS/IDE Solver that can be plugged into existing, Java-based program analysis frameworks. A reference connector exists for the [Soot](http://www.sable.mcgill.ca/soot/) framework.









.


 

 


Eric Bodden
committed
Nov 14, 2012






4














.


 

 


Eric Bodden
committed
Nov 29, 2012






5




6




7




8




9




10




Heros...
* supports solving both IFDS and IDE problems,
* is multi-threaded and thus highly scalable,
* provides a simple programming interface, and
* is fully generic, i.e., can be used to formulate program analysis problems for different programming languages.










docs


 

 


Eric Bodden
committed
Nov 29, 2012






11




12




13




14




15




16




Who are the developers of Heros?
--------------------------------
Heros was developed and is maintained by [Eric Bodden](http://bodden.de/).

Why is Heros called Heros?
--------------------------









.


 

 


Eric Bodden
committed
Nov 29, 2012






17




The name contains (in a different order) the first characters of the last names Reps, Horwitz and Sagiv, the original inventors of the IFDS/IDE frameworks. Heros is pronounced like the Greek word, not like heroes in English.









docs


 

 


Eric Bodden
committed
Nov 29, 2012






18




19




20





What is IFDS/IDE in the first place?
------------------------------------









.


 

 


Eric Bodden
committed
Nov 29, 2012






21




22




[IFDS](http://dx.doi.org/10.1145/199448.199462) is a general framework for solving inter-procedural, finite, distributive subset problems in a flow-sensitive, fully context-sensitive manner. From a user's perspective, IFDS allows static program analysis in a template-driven manner. Users simply define flow functions for an analysis problem but don't need to worry about solving the analysis problem. The latter is automatically taken care of by the solver, in this case by Heros.
[IDE](http://dx.doi.org/10.1016/0304-3975(96)00072-2) is an extension of IFDS that allows more expressive computations. Heros implements an IDE solver and supports IFDS problems as special cases of IDE.









docs


 

 


Eric Bodden
committed
Nov 29, 2012






23




24




25





What are the unique features of Heros over other IFDS/IDE solvers?
------------------------------------------------------------------









.


 

 


Eric Bodden
committed
Nov 29, 2012






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




To the best of our knowledge there exist at least two other similar solvers implemented in Java. [Wala](http://wala.sf.net/) implements a solver that supports IFDS but not IDE. The solver is highly scalable but in our eyes requires more verbose definitions of client analyses. Heros is fully multi-threaded, while Wala's solver is not. There also exists a Scala-based solver by [Nomair A. Naeem, Ondrej Lhotak, and Jonathan Rodriguez](http://dx.doi.org/10.1007/978-3-642-11970-5_8). This implementation does support IDE, and there exists a multi-threaded version of it, but as of yet the implementation is not publicly available.

Why did you create Heros?
-------------------------
One reason is that we found that no existing IFDS/IDE solver satisfied all our needs. The solver in Wala was available but only supported IFDs. Moreover we desired a more simple client interface. The solver by Naeem, Lhotak and Rodriguez was written in Scala. Further we wanted a solver that could be used with multiple programming languages.

The second, and probably better reason is that we found that IFDS/IDE is really useful and that probably there should be a solver that the community can build on, extend and improve over the years. But this requires clean code and documentation. When designing Heros we took special care to provide just that.

What is this all about support for multiple programming languages?
------------------------------------------------------------------
Solving an IFDS/IDE analysis problem basically requires three things:
1. An IFDS/IDE solver.
2. An implementation of an inter-procedural control-flow graph (ICFG).
3. The definition of an IFDS/IDE analysis problem in the form of flow functions.

The solver in heros is fully generic. It can be combined with any form of ICFG. Through Java's generic type variables, Heros abstracts from any concrete types such as statements and methods. To connect Heros to a program-analysis framework for a particular language, all one needs to do is to implement a special version of the ICFG. We provide a reference implementation for Soot. Also the IFDS/IDE analysis problems need to be defined with respect to the actual programming language's constructs and semantics. They are not generic. The entire solver, however, can be reused as is. We are currently working on connecting Heros to a C/C++ compiler.









docs


 

 


Eric Bodden
committed
Nov 29, 2012






42




43




44




45





Under what License can I use Heros?
-----------------------------------
Heros is released under LGPL - see [LICENSE.txt](LICENSE.txt) for details.










65d2987080cacb2b4599738157494ace20865027


Switch branch/tag










heros


README.md



Find file
Normal viewHistoryPermalink




65d2987080cacb2b4599738157494ace20865027


Switch branch/tag










heros


README.md





65d2987080cacb2b4599738157494ace20865027


Switch branch/tag








65d2987080cacb2b4599738157494ace20865027


Switch branch/tag





65d2987080cacb2b4599738157494ace20865027

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

README.md
Find file
Normal viewHistoryPermalink





README.md



4.26 KB









Newer










Older









docs


 

 


Eric Bodden
committed
Nov 29, 2012






1




2




3




Heros IFDS/IDE Solver
=====================
Heros is a generic implementation of an IFDS/IDE Solver that can be plugged into existing, Java-based program analysis frameworks. A reference connector exists for the [Soot](http://www.sable.mcgill.ca/soot/) framework.









.


 

 


Eric Bodden
committed
Nov 14, 2012






4














.


 

 


Eric Bodden
committed
Nov 29, 2012






5




6




7




8




9




10




Heros...
* supports solving both IFDS and IDE problems,
* is multi-threaded and thus highly scalable,
* provides a simple programming interface, and
* is fully generic, i.e., can be used to formulate program analysis problems for different programming languages.










docs


 

 


Eric Bodden
committed
Nov 29, 2012






11




12




13




14




15




16




Who are the developers of Heros?
--------------------------------
Heros was developed and is maintained by [Eric Bodden](http://bodden.de/).

Why is Heros called Heros?
--------------------------









.


 

 


Eric Bodden
committed
Nov 29, 2012






17




The name contains (in a different order) the first characters of the last names Reps, Horwitz and Sagiv, the original inventors of the IFDS/IDE frameworks. Heros is pronounced like the Greek word, not like heroes in English.









docs


 

 


Eric Bodden
committed
Nov 29, 2012






18




19




20





What is IFDS/IDE in the first place?
------------------------------------









.


 

 


Eric Bodden
committed
Nov 29, 2012






21




22




[IFDS](http://dx.doi.org/10.1145/199448.199462) is a general framework for solving inter-procedural, finite, distributive subset problems in a flow-sensitive, fully context-sensitive manner. From a user's perspective, IFDS allows static program analysis in a template-driven manner. Users simply define flow functions for an analysis problem but don't need to worry about solving the analysis problem. The latter is automatically taken care of by the solver, in this case by Heros.
[IDE](http://dx.doi.org/10.1016/0304-3975(96)00072-2) is an extension of IFDS that allows more expressive computations. Heros implements an IDE solver and supports IFDS problems as special cases of IDE.









docs


 

 


Eric Bodden
committed
Nov 29, 2012






23




24




25





What are the unique features of Heros over other IFDS/IDE solvers?
------------------------------------------------------------------









.


 

 


Eric Bodden
committed
Nov 29, 2012






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




To the best of our knowledge there exist at least two other similar solvers implemented in Java. [Wala](http://wala.sf.net/) implements a solver that supports IFDS but not IDE. The solver is highly scalable but in our eyes requires more verbose definitions of client analyses. Heros is fully multi-threaded, while Wala's solver is not. There also exists a Scala-based solver by [Nomair A. Naeem, Ondrej Lhotak, and Jonathan Rodriguez](http://dx.doi.org/10.1007/978-3-642-11970-5_8). This implementation does support IDE, and there exists a multi-threaded version of it, but as of yet the implementation is not publicly available.

Why did you create Heros?
-------------------------
One reason is that we found that no existing IFDS/IDE solver satisfied all our needs. The solver in Wala was available but only supported IFDs. Moreover we desired a more simple client interface. The solver by Naeem, Lhotak and Rodriguez was written in Scala. Further we wanted a solver that could be used with multiple programming languages.

The second, and probably better reason is that we found that IFDS/IDE is really useful and that probably there should be a solver that the community can build on, extend and improve over the years. But this requires clean code and documentation. When designing Heros we took special care to provide just that.

What is this all about support for multiple programming languages?
------------------------------------------------------------------
Solving an IFDS/IDE analysis problem basically requires three things:
1. An IFDS/IDE solver.
2. An implementation of an inter-procedural control-flow graph (ICFG).
3. The definition of an IFDS/IDE analysis problem in the form of flow functions.

The solver in heros is fully generic. It can be combined with any form of ICFG. Through Java's generic type variables, Heros abstracts from any concrete types such as statements and methods. To connect Heros to a program-analysis framework for a particular language, all one needs to do is to implement a special version of the ICFG. We provide a reference implementation for Soot. Also the IFDS/IDE analysis problems need to be defined with respect to the actual programming language's constructs and semantics. They are not generic. The entire solver, however, can be reused as is. We are currently working on connecting Heros to a C/C++ compiler.









docs


 

 


Eric Bodden
committed
Nov 29, 2012






42




43




44




45





Under what License can I use Heros?
-----------------------------------
Heros is released under LGPL - see [LICENSE.txt](LICENSE.txt) for details.









README.md



4.26 KB











README.md



4.26 KB









Newer










Older
NewerOlder







docs


 

 


Eric Bodden
committed
Nov 29, 2012






1




2




3




Heros IFDS/IDE Solver
=====================
Heros is a generic implementation of an IFDS/IDE Solver that can be plugged into existing, Java-based program analysis frameworks. A reference connector exists for the [Soot](http://www.sable.mcgill.ca/soot/) framework.









.


 

 


Eric Bodden
committed
Nov 14, 2012






4














.


 

 


Eric Bodden
committed
Nov 29, 2012






5




6




7




8




9




10




Heros...
* supports solving both IFDS and IDE problems,
* is multi-threaded and thus highly scalable,
* provides a simple programming interface, and
* is fully generic, i.e., can be used to formulate program analysis problems for different programming languages.










docs


 

 


Eric Bodden
committed
Nov 29, 2012






11




12




13




14




15




16




Who are the developers of Heros?
--------------------------------
Heros was developed and is maintained by [Eric Bodden](http://bodden.de/).

Why is Heros called Heros?
--------------------------









.


 

 


Eric Bodden
committed
Nov 29, 2012






17




The name contains (in a different order) the first characters of the last names Reps, Horwitz and Sagiv, the original inventors of the IFDS/IDE frameworks. Heros is pronounced like the Greek word, not like heroes in English.









docs


 

 


Eric Bodden
committed
Nov 29, 2012






18




19




20





What is IFDS/IDE in the first place?
------------------------------------









.


 

 


Eric Bodden
committed
Nov 29, 2012






21




22




[IFDS](http://dx.doi.org/10.1145/199448.199462) is a general framework for solving inter-procedural, finite, distributive subset problems in a flow-sensitive, fully context-sensitive manner. From a user's perspective, IFDS allows static program analysis in a template-driven manner. Users simply define flow functions for an analysis problem but don't need to worry about solving the analysis problem. The latter is automatically taken care of by the solver, in this case by Heros.
[IDE](http://dx.doi.org/10.1016/0304-3975(96)00072-2) is an extension of IFDS that allows more expressive computations. Heros implements an IDE solver and supports IFDS problems as special cases of IDE.









docs


 

 


Eric Bodden
committed
Nov 29, 2012






23




24




25





What are the unique features of Heros over other IFDS/IDE solvers?
------------------------------------------------------------------









.


 

 


Eric Bodden
committed
Nov 29, 2012






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




To the best of our knowledge there exist at least two other similar solvers implemented in Java. [Wala](http://wala.sf.net/) implements a solver that supports IFDS but not IDE. The solver is highly scalable but in our eyes requires more verbose definitions of client analyses. Heros is fully multi-threaded, while Wala's solver is not. There also exists a Scala-based solver by [Nomair A. Naeem, Ondrej Lhotak, and Jonathan Rodriguez](http://dx.doi.org/10.1007/978-3-642-11970-5_8). This implementation does support IDE, and there exists a multi-threaded version of it, but as of yet the implementation is not publicly available.

Why did you create Heros?
-------------------------
One reason is that we found that no existing IFDS/IDE solver satisfied all our needs. The solver in Wala was available but only supported IFDs. Moreover we desired a more simple client interface. The solver by Naeem, Lhotak and Rodriguez was written in Scala. Further we wanted a solver that could be used with multiple programming languages.

The second, and probably better reason is that we found that IFDS/IDE is really useful and that probably there should be a solver that the community can build on, extend and improve over the years. But this requires clean code and documentation. When designing Heros we took special care to provide just that.

What is this all about support for multiple programming languages?
------------------------------------------------------------------
Solving an IFDS/IDE analysis problem basically requires three things:
1. An IFDS/IDE solver.
2. An implementation of an inter-procedural control-flow graph (ICFG).
3. The definition of an IFDS/IDE analysis problem in the form of flow functions.

The solver in heros is fully generic. It can be combined with any form of ICFG. Through Java's generic type variables, Heros abstracts from any concrete types such as statements and methods. To connect Heros to a program-analysis framework for a particular language, all one needs to do is to implement a special version of the ICFG. We provide a reference implementation for Soot. Also the IFDS/IDE analysis problems need to be defined with respect to the actual programming language's constructs and semantics. They are not generic. The entire solver, however, can be reused as is. We are currently working on connecting Heros to a C/C++ compiler.









docs


 

 


Eric Bodden
committed
Nov 29, 2012






42




43




44




45





Under what License can I use Heros?
-----------------------------------
Heros is released under LGPL - see [LICENSE.txt](LICENSE.txt) for details.







docs


 

 


Eric Bodden
committed
Nov 29, 2012



docs


 

 

docs

 

Eric Bodden
committed
Nov 29, 2012

1

2

3
Heros IFDS/IDE SolverHeros IFDS/IDE Solver==========================================Heros is a generic implementation of an IFDS/IDE Solver that can be plugged into existing, Java-based program analysis frameworks. A reference connector exists for the [Soot](http://www.sable.mcgill.ca/soot/) framework.[Soot](http://www.sable.mcgill.ca/soot/)



.


 

 


Eric Bodden
committed
Nov 14, 2012



.


 

 

.

 

Eric Bodden
committed
Nov 14, 2012

4




.


 

 


Eric Bodden
committed
Nov 29, 2012



.


 

 

.

 

Eric Bodden
committed
Nov 29, 2012

5

6

7

8

9

10
Heros...* supports solving both IFDS and IDE problems,** is multi-threaded and thus highly scalable,** provides a simple programming interface, and** is fully generic, i.e., can be used to formulate program analysis problems for different programming languages.*



docs


 

 


Eric Bodden
committed
Nov 29, 2012



docs


 

 

docs

 

Eric Bodden
committed
Nov 29, 2012

11

12

13

14

15

16
Who are the developers of Heros?Who are the developers of Heros?----------------------------------------------------------------Heros was developed and is maintained by [Eric Bodden](http://bodden.de/).[Eric Bodden](http://bodden.de/)Why is Heros called Heros?Why is Heros called Heros?----------------------------------------------------



.


 

 


Eric Bodden
committed
Nov 29, 2012



.


 

 

.

 

Eric Bodden
committed
Nov 29, 2012

17
The name contains (in a different order) the first characters of the last names Reps, Horwitz and Sagiv, the original inventors of the IFDS/IDE frameworks. Heros is pronounced like the Greek word, not like heroes in English.



docs


 

 


Eric Bodden
committed
Nov 29, 2012



docs


 

 

docs

 

Eric Bodden
committed
Nov 29, 2012

18

19

20
What is IFDS/IDE in the first place?What is IFDS/IDE in the first place?------------------------------------------------------------------------



.


 

 


Eric Bodden
committed
Nov 29, 2012



.


 

 

.

 

Eric Bodden
committed
Nov 29, 2012

21

22
[IFDS](http://dx.doi.org/10.1145/199448.199462) is a general framework for solving inter-procedural, finite, distributive subset problems in a flow-sensitive, fully context-sensitive manner. From a user's perspective, IFDS allows static program analysis in a template-driven manner. Users simply define flow functions for an analysis problem but don't need to worry about solving the analysis problem. The latter is automatically taken care of by the solver, in this case by Heros.[IFDS](http://dx.doi.org/10.1145/199448.199462)[IDE](http://dx.doi.org/10.1016/0304-3975(96)00072-2) is an extension of IFDS that allows more expressive computations. Heros implements an IDE solver and supports IFDS problems as special cases of IDE.[IDE](http://dx.doi.org/10.1016/0304-3975(96)



docs


 

 


Eric Bodden
committed
Nov 29, 2012



docs


 

 

docs

 

Eric Bodden
committed
Nov 29, 2012

23

24

25
What are the unique features of Heros over other IFDS/IDE solvers?What are the unique features of Heros over other IFDS/IDE solvers?------------------------------------------------------------------------------------------------------------------------------------



.


 

 


Eric Bodden
committed
Nov 29, 2012



.


 

 

.

 

Eric Bodden
committed
Nov 29, 2012

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
To the best of our knowledge there exist at least two other similar solvers implemented in Java. [Wala](http://wala.sf.net/) implements a solver that supports IFDS but not IDE. The solver is highly scalable but in our eyes requires more verbose definitions of client analyses. Heros is fully multi-threaded, while Wala's solver is not. There also exists a Scala-based solver by [Nomair A. Naeem, Ondrej Lhotak, and Jonathan Rodriguez](http://dx.doi.org/10.1007/978-3-642-11970-5_8). This implementation does support IDE, and there exists a multi-threaded version of it, but as of yet the implementation is not publicly available.[Wala](http://wala.sf.net/)[Nomair A. Naeem, Ondrej Lhotak, and Jonathan Rodriguez](http://dx.doi.org/10.1007/978-3-642-11970-5_8)Why did you create Heros?Why did you create Heros?--------------------------------------------------One reason is that we found that no existing IFDS/IDE solver satisfied all our needs. The solver in Wala was available but only supported IFDs. Moreover we desired a more simple client interface. The solver by Naeem, Lhotak and Rodriguez was written in Scala. Further we wanted a solver that could be used with multiple programming languages.The second, and probably better reason is that we found that IFDS/IDE is really useful and that probably there should be a solver that the community can build on, extend and improve over the years. But this requires clean code and documentation. When designing Heros we took special care to provide just that.What is this all about support for multiple programming languages?What is this all about support for multiple programming languages?------------------------------------------------------------------------------------------------------------------------------------Solving an IFDS/IDE analysis problem basically requires three things:1. An IFDS/IDE solver.1.2. An implementation of an inter-procedural control-flow graph (ICFG).2.3. The definition of an IFDS/IDE analysis problem in the form of flow functions.3.The solver in heros is fully generic. It can be combined with any form of ICFG. Through Java's generic type variables, Heros abstracts from any concrete types such as statements and methods. To connect Heros to a program-analysis framework for a particular language, all one needs to do is to implement a special version of the ICFG. We provide a reference implementation for Soot. Also the IFDS/IDE analysis problems need to be defined with respect to the actual programming language's constructs and semantics. They are not generic. The entire solver, however, can be reused as is. We are currently working on connecting Heros to a C/C++ compiler.



docs


 

 


Eric Bodden
committed
Nov 29, 2012



docs


 

 

docs

 

Eric Bodden
committed
Nov 29, 2012

42

43

44

45
Under what License can I use Heros?Under what License can I use Heros?----------------------------------------------------------------------Heros is released under LGPL - see [LICENSE.txt](LICENSE.txt) for details.[LICENSE.txt](LICENSE.txt)





