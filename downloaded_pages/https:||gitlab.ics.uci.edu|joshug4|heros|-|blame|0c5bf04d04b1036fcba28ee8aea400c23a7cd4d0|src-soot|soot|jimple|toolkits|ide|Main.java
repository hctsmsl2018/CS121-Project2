



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

0c5bf04d04b1036fcba28ee8aea400c23a7cd4d0

















0c5bf04d04b1036fcba28ee8aea400c23a7cd4d0


Switch branch/tag










heros


src-soot


soot


jimple


toolkits


ide


Main.java



Find file
Normal viewHistoryPermalink






Main.java



1.28 KB









Newer










Older









moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






1




package soot.jimple.toolkits.ide;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






2




3




4





import java.util.Map;










moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






5




6




import de.bodden.ide.IFDSTabulationProblem;
import de.bodden.ide.InterproceduralCFG;









renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






7




8




import de.bodden.ide.solver.IFDSSolver;










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






9




10




11




12




13




14




15




import soot.Local;
import soot.PackManager;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootMethod;
import soot.Transform;
import soot.Unit;









moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






16




17




import soot.jimple.toolkits.ide.exampleproblems.IFDSLocalInfoFlow;
import soot.jimple.toolkits.ide.icfg.JimpleBasedInterproceduralCFG;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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





public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		PackManager.v().getPack("wjtp").add(new Transform("wjtp.ifds", new SceneTransformer() {
			protected void internalTransform(String phaseName, @SuppressWarnings("rawtypes") Map options) {

				IFDSTabulationProblem<Unit,Local,SootMethod,InterproceduralCFG<Unit,SootMethod>> problem = new IFDSLocalInfoFlow(new JimpleBasedInterproceduralCFG());
				
				IFDSSolver<Unit,Local,SootMethod,InterproceduralCFG<Unit,SootMethod>> solver = new IFDSSolver<Unit,Local,SootMethod,InterproceduralCFG<Unit,SootMethod>>(problem);	
				solver.solve();
				Unit ret = Scene.v().getMainMethod().getActiveBody().getUnits().getLast();
				for(Local l: solver.ifdsResultsAt(ret)) {
					System.err.println(l);
				}
			}
		}));
		
		soot.Main.main(args);
	}

}












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

0c5bf04d04b1036fcba28ee8aea400c23a7cd4d0

















0c5bf04d04b1036fcba28ee8aea400c23a7cd4d0


Switch branch/tag










heros


src-soot


soot


jimple


toolkits


ide


Main.java



Find file
Normal viewHistoryPermalink






Main.java



1.28 KB









Newer










Older









moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






1




package soot.jimple.toolkits.ide;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






2




3




4





import java.util.Map;










moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






5




6




import de.bodden.ide.IFDSTabulationProblem;
import de.bodden.ide.InterproceduralCFG;









renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






7




8




import de.bodden.ide.solver.IFDSSolver;










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






9




10




11




12




13




14




15




import soot.Local;
import soot.PackManager;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootMethod;
import soot.Transform;
import soot.Unit;









moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






16




17




import soot.jimple.toolkits.ide.exampleproblems.IFDSLocalInfoFlow;
import soot.jimple.toolkits.ide.icfg.JimpleBasedInterproceduralCFG;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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





public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		PackManager.v().getPack("wjtp").add(new Transform("wjtp.ifds", new SceneTransformer() {
			protected void internalTransform(String phaseName, @SuppressWarnings("rawtypes") Map options) {

				IFDSTabulationProblem<Unit,Local,SootMethod,InterproceduralCFG<Unit,SootMethod>> problem = new IFDSLocalInfoFlow(new JimpleBasedInterproceduralCFG());
				
				IFDSSolver<Unit,Local,SootMethod,InterproceduralCFG<Unit,SootMethod>> solver = new IFDSSolver<Unit,Local,SootMethod,InterproceduralCFG<Unit,SootMethod>>(problem);	
				solver.solve();
				Unit ret = Scene.v().getMainMethod().getActiveBody().getUnits().getLast();
				for(Local l: solver.ifdsResultsAt(ret)) {
					System.err.println(l);
				}
			}
		}));
		
		soot.Main.main(args);
	}

}











Open sidebar



Joshua Garcia heros

0c5bf04d04b1036fcba28ee8aea400c23a7cd4d0







Open sidebar



Joshua Garcia heros

0c5bf04d04b1036fcba28ee8aea400c23a7cd4d0




Open sidebar

Joshua Garcia heros

0c5bf04d04b1036fcba28ee8aea400c23a7cd4d0


Joshua Garciaherosheros
0c5bf04d04b1036fcba28ee8aea400c23a7cd4d0










0c5bf04d04b1036fcba28ee8aea400c23a7cd4d0


Switch branch/tag










heros


src-soot


soot


jimple


toolkits


ide


Main.java



Find file
Normal viewHistoryPermalink






Main.java



1.28 KB









Newer










Older









moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






1




package soot.jimple.toolkits.ide;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






2




3




4





import java.util.Map;










moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






5




6




import de.bodden.ide.IFDSTabulationProblem;
import de.bodden.ide.InterproceduralCFG;









renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






7




8




import de.bodden.ide.solver.IFDSSolver;










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






9




10




11




12




13




14




15




import soot.Local;
import soot.PackManager;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootMethod;
import soot.Transform;
import soot.Unit;









moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






16




17




import soot.jimple.toolkits.ide.exampleproblems.IFDSLocalInfoFlow;
import soot.jimple.toolkits.ide.icfg.JimpleBasedInterproceduralCFG;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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





public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		PackManager.v().getPack("wjtp").add(new Transform("wjtp.ifds", new SceneTransformer() {
			protected void internalTransform(String phaseName, @SuppressWarnings("rawtypes") Map options) {

				IFDSTabulationProblem<Unit,Local,SootMethod,InterproceduralCFG<Unit,SootMethod>> problem = new IFDSLocalInfoFlow(new JimpleBasedInterproceduralCFG());
				
				IFDSSolver<Unit,Local,SootMethod,InterproceduralCFG<Unit,SootMethod>> solver = new IFDSSolver<Unit,Local,SootMethod,InterproceduralCFG<Unit,SootMethod>>(problem);	
				solver.solve();
				Unit ret = Scene.v().getMainMethod().getActiveBody().getUnits().getLast();
				for(Local l: solver.ifdsResultsAt(ret)) {
					System.err.println(l);
				}
			}
		}));
		
		soot.Main.main(args);
	}

}














0c5bf04d04b1036fcba28ee8aea400c23a7cd4d0


Switch branch/tag










heros


src-soot


soot


jimple


toolkits


ide


Main.java



Find file
Normal viewHistoryPermalink






Main.java



1.28 KB









Newer










Older









moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






1




package soot.jimple.toolkits.ide;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






2




3




4





import java.util.Map;










moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






5




6




import de.bodden.ide.IFDSTabulationProblem;
import de.bodden.ide.InterproceduralCFG;









renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






7




8




import de.bodden.ide.solver.IFDSSolver;










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






9




10




11




12




13




14




15




import soot.Local;
import soot.PackManager;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootMethod;
import soot.Transform;
import soot.Unit;









moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






16




17




import soot.jimple.toolkits.ide.exampleproblems.IFDSLocalInfoFlow;
import soot.jimple.toolkits.ide.icfg.JimpleBasedInterproceduralCFG;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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





public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		PackManager.v().getPack("wjtp").add(new Transform("wjtp.ifds", new SceneTransformer() {
			protected void internalTransform(String phaseName, @SuppressWarnings("rawtypes") Map options) {

				IFDSTabulationProblem<Unit,Local,SootMethod,InterproceduralCFG<Unit,SootMethod>> problem = new IFDSLocalInfoFlow(new JimpleBasedInterproceduralCFG());
				
				IFDSSolver<Unit,Local,SootMethod,InterproceduralCFG<Unit,SootMethod>> solver = new IFDSSolver<Unit,Local,SootMethod,InterproceduralCFG<Unit,SootMethod>>(problem);	
				solver.solve();
				Unit ret = Scene.v().getMainMethod().getActiveBody().getUnits().getLast();
				for(Local l: solver.ifdsResultsAt(ret)) {
					System.err.println(l);
				}
			}
		}));
		
		soot.Main.main(args);
	}

}










0c5bf04d04b1036fcba28ee8aea400c23a7cd4d0


Switch branch/tag










heros


src-soot


soot


jimple


toolkits


ide


Main.java



Find file
Normal viewHistoryPermalink




0c5bf04d04b1036fcba28ee8aea400c23a7cd4d0


Switch branch/tag










heros


src-soot


soot


jimple


toolkits


ide


Main.java





0c5bf04d04b1036fcba28ee8aea400c23a7cd4d0


Switch branch/tag








0c5bf04d04b1036fcba28ee8aea400c23a7cd4d0


Switch branch/tag





0c5bf04d04b1036fcba28ee8aea400c23a7cd4d0

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src-soot

soot

jimple

toolkits

ide

Main.java
Find file
Normal viewHistoryPermalink




Main.java



1.28 KB









Newer










Older









moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






1




package soot.jimple.toolkits.ide;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






2




3




4





import java.util.Map;










moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






5




6




import de.bodden.ide.IFDSTabulationProblem;
import de.bodden.ide.InterproceduralCFG;









renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






7




8




import de.bodden.ide.solver.IFDSSolver;










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






9




10




11




12




13




14




15




import soot.Local;
import soot.PackManager;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootMethod;
import soot.Transform;
import soot.Unit;









moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






16




17




import soot.jimple.toolkits.ide.exampleproblems.IFDSLocalInfoFlow;
import soot.jimple.toolkits.ide.icfg.JimpleBasedInterproceduralCFG;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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





public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		PackManager.v().getPack("wjtp").add(new Transform("wjtp.ifds", new SceneTransformer() {
			protected void internalTransform(String phaseName, @SuppressWarnings("rawtypes") Map options) {

				IFDSTabulationProblem<Unit,Local,SootMethod,InterproceduralCFG<Unit,SootMethod>> problem = new IFDSLocalInfoFlow(new JimpleBasedInterproceduralCFG());
				
				IFDSSolver<Unit,Local,SootMethod,InterproceduralCFG<Unit,SootMethod>> solver = new IFDSSolver<Unit,Local,SootMethod,InterproceduralCFG<Unit,SootMethod>>(problem);	
				solver.solve();
				Unit ret = Scene.v().getMainMethod().getActiveBody().getUnits().getLast();
				for(Local l: solver.ifdsResultsAt(ret)) {
					System.err.println(l);
				}
			}
		}));
		
		soot.Main.main(args);
	}

}








Main.java



1.28 KB










Main.java



1.28 KB









Newer










Older
NewerOlder







moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






1




package soot.jimple.toolkits.ide;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






2




3




4





import java.util.Map;










moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






5




6




import de.bodden.ide.IFDSTabulationProblem;
import de.bodden.ide.InterproceduralCFG;









renamed package


 

 


Eric Bodden
committed
Nov 28, 2012






7




8




import de.bodden.ide.solver.IFDSSolver;










initial checkin



 


Eric Bodden
committed
Nov 14, 2012






9




10




11




12




13




14




15




import soot.Local;
import soot.PackManager;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootMethod;
import soot.Transform;
import soot.Unit;









moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012






16




17




import soot.jimple.toolkits.ide.exampleproblems.IFDSLocalInfoFlow;
import soot.jimple.toolkits.ide.icfg.JimpleBasedInterproceduralCFG;









initial checkin



 


Eric Bodden
committed
Nov 14, 2012






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





public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		PackManager.v().getPack("wjtp").add(new Transform("wjtp.ifds", new SceneTransformer() {
			protected void internalTransform(String phaseName, @SuppressWarnings("rawtypes") Map options) {

				IFDSTabulationProblem<Unit,Local,SootMethod,InterproceduralCFG<Unit,SootMethod>> problem = new IFDSLocalInfoFlow(new JimpleBasedInterproceduralCFG());
				
				IFDSSolver<Unit,Local,SootMethod,InterproceduralCFG<Unit,SootMethod>> solver = new IFDSSolver<Unit,Local,SootMethod,InterproceduralCFG<Unit,SootMethod>>(problem);	
				solver.solve();
				Unit ret = Scene.v().getMainMethod().getActiveBody().getUnits().getLast();
				for(Local l: solver.ifdsResultsAt(ret)) {
					System.err.println(l);
				}
			}
		}));
		
		soot.Main.main(args);
	}

}







moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012



moved dependencies on soot into separate package


 

 

moved dependencies on soot into separate package

 

Eric Bodden
committed
Nov 28, 2012

1
package soot.jimple.toolkits.ide;packagesoot.jimple.toolkits.ide;



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

2

3

4
import java.util.Map;importjava.util.Map;



moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012



moved dependencies on soot into separate package


 

 

moved dependencies on soot into separate package

 

Eric Bodden
committed
Nov 28, 2012

5

6
import de.bodden.ide.IFDSTabulationProblem;importde.bodden.ide.IFDSTabulationProblem;import de.bodden.ide.InterproceduralCFG;importde.bodden.ide.InterproceduralCFG;



renamed package


 

 


Eric Bodden
committed
Nov 28, 2012



renamed package


 

 

renamed package

 

Eric Bodden
committed
Nov 28, 2012

7

8
import de.bodden.ide.solver.IFDSSolver;importde.bodden.ide.solver.IFDSSolver;



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

9

10

11

12

13

14

15
import soot.Local;importsoot.Local;import soot.PackManager;importsoot.PackManager;import soot.Scene;importsoot.Scene;import soot.SceneTransformer;importsoot.SceneTransformer;import soot.SootMethod;importsoot.SootMethod;import soot.Transform;importsoot.Transform;import soot.Unit;importsoot.Unit;



moved dependencies on soot into separate package


 

 


Eric Bodden
committed
Nov 28, 2012



moved dependencies on soot into separate package


 

 

moved dependencies on soot into separate package

 

Eric Bodden
committed
Nov 28, 2012

16

17
import soot.jimple.toolkits.ide.exampleproblems.IFDSLocalInfoFlow;importsoot.jimple.toolkits.ide.exampleproblems.IFDSLocalInfoFlow;import soot.jimple.toolkits.ide.icfg.JimpleBasedInterproceduralCFG;importsoot.jimple.toolkits.ide.icfg.JimpleBasedInterproceduralCFG;



initial checkin



 


Eric Bodden
committed
Nov 14, 2012



initial checkin



 

initial checkin


Eric Bodden
committed
Nov 14, 2012

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
public class Main {publicclassMain{	/**/**	 * @param args	 * @param args	 */	 */	public static void main(String[] args) {publicstaticvoidmain(String[]args){						PackManager.v().getPack("wjtp").add(new Transform("wjtp.ifds", new SceneTransformer() {PackManager.v().getPack("wjtp").add(newTransform("wjtp.ifds",newSceneTransformer(){			protected void internalTransform(String phaseName, @SuppressWarnings("rawtypes") Map options) {protectedvoidinternalTransform(StringphaseName,@SuppressWarnings("rawtypes")Mapoptions){				IFDSTabulationProblem<Unit,Local,SootMethod,InterproceduralCFG<Unit,SootMethod>> problem = new IFDSLocalInfoFlow(new JimpleBasedInterproceduralCFG());IFDSTabulationProblem<Unit,Local,SootMethod,InterproceduralCFG<Unit,SootMethod>>problem=newIFDSLocalInfoFlow(newJimpleBasedInterproceduralCFG());								IFDSSolver<Unit,Local,SootMethod,InterproceduralCFG<Unit,SootMethod>> solver = new IFDSSolver<Unit,Local,SootMethod,InterproceduralCFG<Unit,SootMethod>>(problem);	IFDSSolver<Unit,Local,SootMethod,InterproceduralCFG<Unit,SootMethod>>solver=newIFDSSolver<Unit,Local,SootMethod,InterproceduralCFG<Unit,SootMethod>>(problem);				solver.solve();solver.solve();				Unit ret = Scene.v().getMainMethod().getActiveBody().getUnits().getLast();Unitret=Scene.v().getMainMethod().getActiveBody().getUnits().getLast();				for(Local l: solver.ifdsResultsAt(ret)) {for(Locall:solver.ifdsResultsAt(ret)){					System.err.println(l);System.err.println(l);				}}			}}		}));}));				soot.Main.main(args);soot.Main.main(args);	}}}}





