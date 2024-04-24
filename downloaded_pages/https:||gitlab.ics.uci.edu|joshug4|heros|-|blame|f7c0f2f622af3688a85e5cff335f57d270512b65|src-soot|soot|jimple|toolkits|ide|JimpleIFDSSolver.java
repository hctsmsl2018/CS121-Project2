



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

f7c0f2f622af3688a85e5cff335f57d270512b65

















f7c0f2f622af3688a85e5cff335f57d270512b65


Switch branch/tag










heros


src-soot


soot


jimple


toolkits


ide


JimpleIFDSSolver.java



Find file
Normal viewHistoryPermalink






JimpleIFDSSolver.java



1.76 KB









Newer










Older









added dumping code again for Soot/Jimple versions



 


Eric Bodden
committed
Nov 29, 2012






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




package soot.jimple.toolkits.ide;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import soot.PatchingChain;
import soot.SootMethod;
import soot.Unit;

import com.google.common.collect.Table.Cell;

import de.bodden.ide.InterproceduralCFG;
import de.bodden.ide.solver.IFDSSolver;

public class JimpleIFDSSolver<D, V> extends IFDSSolver<Unit, D, SootMethod, InterproceduralCFG<Unit,SootMethod>> {

	private final boolean DUMP_RESULTS;

	public JimpleIFDSSolver(DefaultJimpleIFDSTabulationProblem<D, InterproceduralCFG<Unit,SootMethod>> problem) {
		this(problem,false);
	}
	
	public JimpleIFDSSolver(DefaultJimpleIFDSTabulationProblem<D, InterproceduralCFG<Unit,SootMethod>> problem, boolean dumpResults) {
		super(problem);
		this.DUMP_RESULTS = dumpResults;
	}
	
	@Override
	public void solve(int numThreads) {
		super.solve(numThreads);
		if(DUMP_RESULTS)
			dumpResults();
	}
	
	public void dumpResults() {
		try {
			PrintWriter out = new PrintWriter(new FileOutputStream("ideSolverDump"+System.currentTimeMillis()+".csv"));
			List<String> res = new ArrayList<String>();
			for(Cell<Unit, D, ?> entry: val.cellSet()) {
				SootMethod methodOf = (SootMethod) icfg.getMethodOf(entry.getRowKey());
				PatchingChain<Unit> units = methodOf.getActiveBody().getUnits();
				int i=0;
				for (Unit unit : units) {
					if(unit==entry.getRowKey())
						break;
					i++;
				}

				res.add(methodOf+";"+entry.getRowKey()+"@"+i+";"+entry.getColumnKey()+";"+entry.getValue());
			}
			Collections.sort(res);
			for (String string : res) {
				out.println(string);
			}
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
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

f7c0f2f622af3688a85e5cff335f57d270512b65

















f7c0f2f622af3688a85e5cff335f57d270512b65


Switch branch/tag










heros


src-soot


soot


jimple


toolkits


ide


JimpleIFDSSolver.java



Find file
Normal viewHistoryPermalink






JimpleIFDSSolver.java



1.76 KB









Newer










Older









added dumping code again for Soot/Jimple versions



 


Eric Bodden
committed
Nov 29, 2012






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




package soot.jimple.toolkits.ide;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import soot.PatchingChain;
import soot.SootMethod;
import soot.Unit;

import com.google.common.collect.Table.Cell;

import de.bodden.ide.InterproceduralCFG;
import de.bodden.ide.solver.IFDSSolver;

public class JimpleIFDSSolver<D, V> extends IFDSSolver<Unit, D, SootMethod, InterproceduralCFG<Unit,SootMethod>> {

	private final boolean DUMP_RESULTS;

	public JimpleIFDSSolver(DefaultJimpleIFDSTabulationProblem<D, InterproceduralCFG<Unit,SootMethod>> problem) {
		this(problem,false);
	}
	
	public JimpleIFDSSolver(DefaultJimpleIFDSTabulationProblem<D, InterproceduralCFG<Unit,SootMethod>> problem, boolean dumpResults) {
		super(problem);
		this.DUMP_RESULTS = dumpResults;
	}
	
	@Override
	public void solve(int numThreads) {
		super.solve(numThreads);
		if(DUMP_RESULTS)
			dumpResults();
	}
	
	public void dumpResults() {
		try {
			PrintWriter out = new PrintWriter(new FileOutputStream("ideSolverDump"+System.currentTimeMillis()+".csv"));
			List<String> res = new ArrayList<String>();
			for(Cell<Unit, D, ?> entry: val.cellSet()) {
				SootMethod methodOf = (SootMethod) icfg.getMethodOf(entry.getRowKey());
				PatchingChain<Unit> units = methodOf.getActiveBody().getUnits();
				int i=0;
				for (Unit unit : units) {
					if(unit==entry.getRowKey())
						break;
					i++;
				}

				res.add(methodOf+";"+entry.getRowKey()+"@"+i+";"+entry.getColumnKey()+";"+entry.getValue());
			}
			Collections.sort(res);
			for (String string : res) {
				out.println(string);
			}
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}











Open sidebar



Joshua Garcia heros

f7c0f2f622af3688a85e5cff335f57d270512b65







Open sidebar



Joshua Garcia heros

f7c0f2f622af3688a85e5cff335f57d270512b65




Open sidebar

Joshua Garcia heros

f7c0f2f622af3688a85e5cff335f57d270512b65


Joshua Garciaherosheros
f7c0f2f622af3688a85e5cff335f57d270512b65










f7c0f2f622af3688a85e5cff335f57d270512b65


Switch branch/tag










heros


src-soot


soot


jimple


toolkits


ide


JimpleIFDSSolver.java



Find file
Normal viewHistoryPermalink






JimpleIFDSSolver.java



1.76 KB









Newer










Older









added dumping code again for Soot/Jimple versions



 


Eric Bodden
committed
Nov 29, 2012






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




package soot.jimple.toolkits.ide;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import soot.PatchingChain;
import soot.SootMethod;
import soot.Unit;

import com.google.common.collect.Table.Cell;

import de.bodden.ide.InterproceduralCFG;
import de.bodden.ide.solver.IFDSSolver;

public class JimpleIFDSSolver<D, V> extends IFDSSolver<Unit, D, SootMethod, InterproceduralCFG<Unit,SootMethod>> {

	private final boolean DUMP_RESULTS;

	public JimpleIFDSSolver(DefaultJimpleIFDSTabulationProblem<D, InterproceduralCFG<Unit,SootMethod>> problem) {
		this(problem,false);
	}
	
	public JimpleIFDSSolver(DefaultJimpleIFDSTabulationProblem<D, InterproceduralCFG<Unit,SootMethod>> problem, boolean dumpResults) {
		super(problem);
		this.DUMP_RESULTS = dumpResults;
	}
	
	@Override
	public void solve(int numThreads) {
		super.solve(numThreads);
		if(DUMP_RESULTS)
			dumpResults();
	}
	
	public void dumpResults() {
		try {
			PrintWriter out = new PrintWriter(new FileOutputStream("ideSolverDump"+System.currentTimeMillis()+".csv"));
			List<String> res = new ArrayList<String>();
			for(Cell<Unit, D, ?> entry: val.cellSet()) {
				SootMethod methodOf = (SootMethod) icfg.getMethodOf(entry.getRowKey());
				PatchingChain<Unit> units = methodOf.getActiveBody().getUnits();
				int i=0;
				for (Unit unit : units) {
					if(unit==entry.getRowKey())
						break;
					i++;
				}

				res.add(methodOf+";"+entry.getRowKey()+"@"+i+";"+entry.getColumnKey()+";"+entry.getValue());
			}
			Collections.sort(res);
			for (String string : res) {
				out.println(string);
			}
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}














f7c0f2f622af3688a85e5cff335f57d270512b65


Switch branch/tag










heros


src-soot


soot


jimple


toolkits


ide


JimpleIFDSSolver.java



Find file
Normal viewHistoryPermalink






JimpleIFDSSolver.java



1.76 KB









Newer










Older









added dumping code again for Soot/Jimple versions



 


Eric Bodden
committed
Nov 29, 2012






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




package soot.jimple.toolkits.ide;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import soot.PatchingChain;
import soot.SootMethod;
import soot.Unit;

import com.google.common.collect.Table.Cell;

import de.bodden.ide.InterproceduralCFG;
import de.bodden.ide.solver.IFDSSolver;

public class JimpleIFDSSolver<D, V> extends IFDSSolver<Unit, D, SootMethod, InterproceduralCFG<Unit,SootMethod>> {

	private final boolean DUMP_RESULTS;

	public JimpleIFDSSolver(DefaultJimpleIFDSTabulationProblem<D, InterproceduralCFG<Unit,SootMethod>> problem) {
		this(problem,false);
	}
	
	public JimpleIFDSSolver(DefaultJimpleIFDSTabulationProblem<D, InterproceduralCFG<Unit,SootMethod>> problem, boolean dumpResults) {
		super(problem);
		this.DUMP_RESULTS = dumpResults;
	}
	
	@Override
	public void solve(int numThreads) {
		super.solve(numThreads);
		if(DUMP_RESULTS)
			dumpResults();
	}
	
	public void dumpResults() {
		try {
			PrintWriter out = new PrintWriter(new FileOutputStream("ideSolverDump"+System.currentTimeMillis()+".csv"));
			List<String> res = new ArrayList<String>();
			for(Cell<Unit, D, ?> entry: val.cellSet()) {
				SootMethod methodOf = (SootMethod) icfg.getMethodOf(entry.getRowKey());
				PatchingChain<Unit> units = methodOf.getActiveBody().getUnits();
				int i=0;
				for (Unit unit : units) {
					if(unit==entry.getRowKey())
						break;
					i++;
				}

				res.add(methodOf+";"+entry.getRowKey()+"@"+i+";"+entry.getColumnKey()+";"+entry.getValue());
			}
			Collections.sort(res);
			for (String string : res) {
				out.println(string);
			}
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}










f7c0f2f622af3688a85e5cff335f57d270512b65


Switch branch/tag










heros


src-soot


soot


jimple


toolkits


ide


JimpleIFDSSolver.java



Find file
Normal viewHistoryPermalink




f7c0f2f622af3688a85e5cff335f57d270512b65


Switch branch/tag










heros


src-soot


soot


jimple


toolkits


ide


JimpleIFDSSolver.java





f7c0f2f622af3688a85e5cff335f57d270512b65


Switch branch/tag








f7c0f2f622af3688a85e5cff335f57d270512b65


Switch branch/tag





f7c0f2f622af3688a85e5cff335f57d270512b65

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src-soot

soot

jimple

toolkits

ide

JimpleIFDSSolver.java
Find file
Normal viewHistoryPermalink




JimpleIFDSSolver.java



1.76 KB









Newer










Older









added dumping code again for Soot/Jimple versions



 


Eric Bodden
committed
Nov 29, 2012






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




package soot.jimple.toolkits.ide;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import soot.PatchingChain;
import soot.SootMethod;
import soot.Unit;

import com.google.common.collect.Table.Cell;

import de.bodden.ide.InterproceduralCFG;
import de.bodden.ide.solver.IFDSSolver;

public class JimpleIFDSSolver<D, V> extends IFDSSolver<Unit, D, SootMethod, InterproceduralCFG<Unit,SootMethod>> {

	private final boolean DUMP_RESULTS;

	public JimpleIFDSSolver(DefaultJimpleIFDSTabulationProblem<D, InterproceduralCFG<Unit,SootMethod>> problem) {
		this(problem,false);
	}
	
	public JimpleIFDSSolver(DefaultJimpleIFDSTabulationProblem<D, InterproceduralCFG<Unit,SootMethod>> problem, boolean dumpResults) {
		super(problem);
		this.DUMP_RESULTS = dumpResults;
	}
	
	@Override
	public void solve(int numThreads) {
		super.solve(numThreads);
		if(DUMP_RESULTS)
			dumpResults();
	}
	
	public void dumpResults() {
		try {
			PrintWriter out = new PrintWriter(new FileOutputStream("ideSolverDump"+System.currentTimeMillis()+".csv"));
			List<String> res = new ArrayList<String>();
			for(Cell<Unit, D, ?> entry: val.cellSet()) {
				SootMethod methodOf = (SootMethod) icfg.getMethodOf(entry.getRowKey());
				PatchingChain<Unit> units = methodOf.getActiveBody().getUnits();
				int i=0;
				for (Unit unit : units) {
					if(unit==entry.getRowKey())
						break;
					i++;
				}

				res.add(methodOf+";"+entry.getRowKey()+"@"+i+";"+entry.getColumnKey()+";"+entry.getValue());
			}
			Collections.sort(res);
			for (String string : res) {
				out.println(string);
			}
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}








JimpleIFDSSolver.java



1.76 KB










JimpleIFDSSolver.java



1.76 KB









Newer










Older
NewerOlder







added dumping code again for Soot/Jimple versions



 


Eric Bodden
committed
Nov 29, 2012






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




package soot.jimple.toolkits.ide;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import soot.PatchingChain;
import soot.SootMethod;
import soot.Unit;

import com.google.common.collect.Table.Cell;

import de.bodden.ide.InterproceduralCFG;
import de.bodden.ide.solver.IFDSSolver;

public class JimpleIFDSSolver<D, V> extends IFDSSolver<Unit, D, SootMethod, InterproceduralCFG<Unit,SootMethod>> {

	private final boolean DUMP_RESULTS;

	public JimpleIFDSSolver(DefaultJimpleIFDSTabulationProblem<D, InterproceduralCFG<Unit,SootMethod>> problem) {
		this(problem,false);
	}
	
	public JimpleIFDSSolver(DefaultJimpleIFDSTabulationProblem<D, InterproceduralCFG<Unit,SootMethod>> problem, boolean dumpResults) {
		super(problem);
		this.DUMP_RESULTS = dumpResults;
	}
	
	@Override
	public void solve(int numThreads) {
		super.solve(numThreads);
		if(DUMP_RESULTS)
			dumpResults();
	}
	
	public void dumpResults() {
		try {
			PrintWriter out = new PrintWriter(new FileOutputStream("ideSolverDump"+System.currentTimeMillis()+".csv"));
			List<String> res = new ArrayList<String>();
			for(Cell<Unit, D, ?> entry: val.cellSet()) {
				SootMethod methodOf = (SootMethod) icfg.getMethodOf(entry.getRowKey());
				PatchingChain<Unit> units = methodOf.getActiveBody().getUnits();
				int i=0;
				for (Unit unit : units) {
					if(unit==entry.getRowKey())
						break;
					i++;
				}

				res.add(methodOf+";"+entry.getRowKey()+"@"+i+";"+entry.getColumnKey()+";"+entry.getValue());
			}
			Collections.sort(res);
			for (String string : res) {
				out.println(string);
			}
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}







added dumping code again for Soot/Jimple versions



 


Eric Bodden
committed
Nov 29, 2012



added dumping code again for Soot/Jimple versions



 

added dumping code again for Soot/Jimple versions


Eric Bodden
committed
Nov 29, 2012

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
package soot.jimple.toolkits.ide;packagesoot.jimple.toolkits.ide;import java.io.FileNotFoundException;importjava.io.FileNotFoundException;import java.io.FileOutputStream;importjava.io.FileOutputStream;import java.io.PrintWriter;importjava.io.PrintWriter;import java.util.ArrayList;importjava.util.ArrayList;import java.util.Collections;importjava.util.Collections;import java.util.List;importjava.util.List;import soot.PatchingChain;importsoot.PatchingChain;import soot.SootMethod;importsoot.SootMethod;import soot.Unit;importsoot.Unit;import com.google.common.collect.Table.Cell;importcom.google.common.collect.Table.Cell;import de.bodden.ide.InterproceduralCFG;importde.bodden.ide.InterproceduralCFG;import de.bodden.ide.solver.IFDSSolver;importde.bodden.ide.solver.IFDSSolver;public class JimpleIFDSSolver<D, V> extends IFDSSolver<Unit, D, SootMethod, InterproceduralCFG<Unit,SootMethod>> {publicclassJimpleIFDSSolver<D,V>extendsIFDSSolver<Unit,D,SootMethod,InterproceduralCFG<Unit,SootMethod>>{	private final boolean DUMP_RESULTS;privatefinalbooleanDUMP_RESULTS;	public JimpleIFDSSolver(DefaultJimpleIFDSTabulationProblem<D, InterproceduralCFG<Unit,SootMethod>> problem) {publicJimpleIFDSSolver(DefaultJimpleIFDSTabulationProblem<D,InterproceduralCFG<Unit,SootMethod>>problem){		this(problem,false);this(problem,false);	}}		public JimpleIFDSSolver(DefaultJimpleIFDSTabulationProblem<D, InterproceduralCFG<Unit,SootMethod>> problem, boolean dumpResults) {publicJimpleIFDSSolver(DefaultJimpleIFDSTabulationProblem<D,InterproceduralCFG<Unit,SootMethod>>problem,booleandumpResults){		super(problem);super(problem);		this.DUMP_RESULTS = dumpResults;this.DUMP_RESULTS=dumpResults;	}}		@Override@Override	public void solve(int numThreads) {publicvoidsolve(intnumThreads){		super.solve(numThreads);super.solve(numThreads);		if(DUMP_RESULTS)if(DUMP_RESULTS)			dumpResults();dumpResults();	}}		public void dumpResults() {publicvoiddumpResults(){		try {try{			PrintWriter out = new PrintWriter(new FileOutputStream("ideSolverDump"+System.currentTimeMillis()+".csv"));PrintWriterout=newPrintWriter(newFileOutputStream("ideSolverDump"+System.currentTimeMillis()+".csv"));			List<String> res = new ArrayList<String>();List<String>res=newArrayList<String>();			for(Cell<Unit, D, ?> entry: val.cellSet()) {for(Cell<Unit,D,?>entry:val.cellSet()){				SootMethod methodOf = (SootMethod) icfg.getMethodOf(entry.getRowKey());SootMethodmethodOf=(SootMethod)icfg.getMethodOf(entry.getRowKey());				PatchingChain<Unit> units = methodOf.getActiveBody().getUnits();PatchingChain<Unit>units=methodOf.getActiveBody().getUnits();				int i=0;inti=0;				for (Unit unit : units) {for(Unitunit:units){					if(unit==entry.getRowKey())if(unit==entry.getRowKey())						break;break;					i++;i++;				}}				res.add(methodOf+";"+entry.getRowKey()+"@"+i+";"+entry.getColumnKey()+";"+entry.getValue());res.add(methodOf+";"+entry.getRowKey()+"@"+i+";"+entry.getColumnKey()+";"+entry.getValue());			}}			Collections.sort(res);Collections.sort(res);			for (String string : res) {for(Stringstring:res){				out.println(string);out.println(string);			}}			out.flush();out.flush();			out.close();out.close();		} catch (FileNotFoundException e) {}catch(FileNotFoundExceptione){			e.printStackTrace();e.printStackTrace();		}}	}}}}





