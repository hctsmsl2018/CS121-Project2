


GitLab











Explore




Sign in




GitLab








GitLab

Explore

Sign in











Joshua Garcia heros

7b66dd07048cf95ba76faff3cd2ad10458da7e1a




















heros


src


heros


fieldsens


MethodAnalyzerImpl.java





Find file




Normal view



History



Permalink









MethodAnalyzerImpl.java




2.22 KiB









Newer










Older









bidi solver



Johannes Lerch
committed
Mar 20, 2015




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



/*******************************************************************************
 * Copyright (c) 2015 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




11



package heros.fieldsens;








bidi solver



Johannes Lerch
committed
Mar 20, 2015




12












restructuring

 


Johannes Lerch
committed
Jun 01, 2015




13


14


15


16



import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;
import heros.utilities.DefaultValueMap;









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




17



public class MethodAnalyzerImpl<Field,Fact, Stmt, Method> 








bidi solver



Johannes Lerch
committed
Mar 20, 2015




18


19


20



		implements MethodAnalyzer<Field, Fact, Stmt, Method> {

	private Method method;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




21


22



	private DefaultValueMap<Fact, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>> perSourceAnalyzer = 
			new DefaultValueMap<Fact, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>>() {








bidi solver



Johannes Lerch
committed
Mar 20, 2015




23


24



		@Override
		protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> createItem(Fact key) {








debugger

 


Johannes Lerch
committed
Jul 08, 2015




25



			return new PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>(method, key, context, debugger);








bidi solver



Johannes Lerch
committed
Mar 20, 2015




26


27


28



		}
	};
	private Context<Field, Fact, Stmt, Method> context;








debugger

 


Johannes Lerch
committed
Jul 08, 2015




29



	private Debugger<Field, Fact, Stmt, Method> debugger;








bidi solver



Johannes Lerch
committed
Mar 20, 2015




30












debugger

 


Johannes Lerch
committed
Jul 08, 2015




31



	MethodAnalyzerImpl(Method method, Context<Field, Fact, Stmt, Method> context, Debugger<Field, Fact, Stmt, Method> debugger) {








bidi solver



Johannes Lerch
committed
Mar 20, 2015




32


33



		this.method = method;
		this.context = context;








debugger

 


Johannes Lerch
committed
Jul 08, 2015




34



		this.debugger = debugger;








bidi solver



Johannes Lerch
committed
Mar 20, 2015




35


36


37



	}
	
	@Override








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




38



	public void addIncomingEdge(CallEdge<Field, Fact, Stmt, Method> incEdge) {








bidi solver



Johannes Lerch
committed
Mar 20, 2015




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



		WrappedFact<Field, Fact, Stmt, Method> calleeSourceFact = incEdge.getCalleeSourceFact();
		PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer = perSourceAnalyzer.getOrCreate(calleeSourceFact.getFact());
		analyzer.addIncomingEdge(incEdge);
	}

	@Override
	public void addInitialSeed(Stmt startPoint, Fact val) {
		perSourceAnalyzer.getOrCreate(val).addInitialSeed(startPoint);
	}
	
	@Override
	public void addUnbalancedReturnFlow(WrappedFactAtStatement<Field, Fact, Stmt, Method> target, Stmt callSite) {
		perSourceAnalyzer.getOrCreate(context.zeroValue).scheduleUnbalancedReturnEdgeTo(target);
	}
}



















Joshua Garcia heros

7b66dd07048cf95ba76faff3cd2ad10458da7e1a




















heros


src


heros


fieldsens


MethodAnalyzerImpl.java





Find file




Normal view



History



Permalink









MethodAnalyzerImpl.java




2.22 KiB









Newer










Older









bidi solver



Johannes Lerch
committed
Mar 20, 2015




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



/*******************************************************************************
 * Copyright (c) 2015 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




11



package heros.fieldsens;








bidi solver



Johannes Lerch
committed
Mar 20, 2015




12












restructuring

 


Johannes Lerch
committed
Jun 01, 2015




13


14


15


16



import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;
import heros.utilities.DefaultValueMap;









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




17



public class MethodAnalyzerImpl<Field,Fact, Stmt, Method> 








bidi solver



Johannes Lerch
committed
Mar 20, 2015




18


19


20



		implements MethodAnalyzer<Field, Fact, Stmt, Method> {

	private Method method;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




21


22



	private DefaultValueMap<Fact, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>> perSourceAnalyzer = 
			new DefaultValueMap<Fact, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>>() {








bidi solver



Johannes Lerch
committed
Mar 20, 2015




23


24



		@Override
		protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> createItem(Fact key) {








debugger

 


Johannes Lerch
committed
Jul 08, 2015




25



			return new PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>(method, key, context, debugger);








bidi solver



Johannes Lerch
committed
Mar 20, 2015




26


27


28



		}
	};
	private Context<Field, Fact, Stmt, Method> context;








debugger

 


Johannes Lerch
committed
Jul 08, 2015




29



	private Debugger<Field, Fact, Stmt, Method> debugger;








bidi solver



Johannes Lerch
committed
Mar 20, 2015




30












debugger

 


Johannes Lerch
committed
Jul 08, 2015




31



	MethodAnalyzerImpl(Method method, Context<Field, Fact, Stmt, Method> context, Debugger<Field, Fact, Stmt, Method> debugger) {








bidi solver



Johannes Lerch
committed
Mar 20, 2015




32


33



		this.method = method;
		this.context = context;








debugger

 


Johannes Lerch
committed
Jul 08, 2015




34



		this.debugger = debugger;








bidi solver



Johannes Lerch
committed
Mar 20, 2015




35


36


37



	}
	
	@Override








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




38



	public void addIncomingEdge(CallEdge<Field, Fact, Stmt, Method> incEdge) {








bidi solver



Johannes Lerch
committed
Mar 20, 2015




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



		WrappedFact<Field, Fact, Stmt, Method> calleeSourceFact = incEdge.getCalleeSourceFact();
		PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer = perSourceAnalyzer.getOrCreate(calleeSourceFact.getFact());
		analyzer.addIncomingEdge(incEdge);
	}

	@Override
	public void addInitialSeed(Stmt startPoint, Fact val) {
		perSourceAnalyzer.getOrCreate(val).addInitialSeed(startPoint);
	}
	
	@Override
	public void addUnbalancedReturnFlow(WrappedFactAtStatement<Field, Fact, Stmt, Method> target, Stmt callSite) {
		perSourceAnalyzer.getOrCreate(context.zeroValue).scheduleUnbalancedReturnEdgeTo(target);
	}
}
















Joshua Garcia heros

7b66dd07048cf95ba76faff3cd2ad10458da7e1a












Joshua Garcia heros

7b66dd07048cf95ba76faff3cd2ad10458da7e1a










Joshua Garcia heros

7b66dd07048cf95ba76faff3cd2ad10458da7e1a




Joshua Garciaherosheros
7b66dd07048cf95ba76faff3cd2ad10458da7e1a













heros


src


heros


fieldsens


MethodAnalyzerImpl.java





Find file




Normal view



History



Permalink









MethodAnalyzerImpl.java




2.22 KiB









Newer










Older









bidi solver



Johannes Lerch
committed
Mar 20, 2015




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



/*******************************************************************************
 * Copyright (c) 2015 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




11



package heros.fieldsens;








bidi solver



Johannes Lerch
committed
Mar 20, 2015




12












restructuring

 


Johannes Lerch
committed
Jun 01, 2015




13


14


15


16



import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;
import heros.utilities.DefaultValueMap;









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




17



public class MethodAnalyzerImpl<Field,Fact, Stmt, Method> 








bidi solver



Johannes Lerch
committed
Mar 20, 2015




18


19


20



		implements MethodAnalyzer<Field, Fact, Stmt, Method> {

	private Method method;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




21


22



	private DefaultValueMap<Fact, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>> perSourceAnalyzer = 
			new DefaultValueMap<Fact, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>>() {








bidi solver



Johannes Lerch
committed
Mar 20, 2015




23


24



		@Override
		protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> createItem(Fact key) {








debugger

 


Johannes Lerch
committed
Jul 08, 2015




25



			return new PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>(method, key, context, debugger);








bidi solver



Johannes Lerch
committed
Mar 20, 2015




26


27


28



		}
	};
	private Context<Field, Fact, Stmt, Method> context;








debugger

 


Johannes Lerch
committed
Jul 08, 2015




29



	private Debugger<Field, Fact, Stmt, Method> debugger;








bidi solver



Johannes Lerch
committed
Mar 20, 2015




30












debugger

 


Johannes Lerch
committed
Jul 08, 2015




31



	MethodAnalyzerImpl(Method method, Context<Field, Fact, Stmt, Method> context, Debugger<Field, Fact, Stmt, Method> debugger) {








bidi solver



Johannes Lerch
committed
Mar 20, 2015




32


33



		this.method = method;
		this.context = context;








debugger

 


Johannes Lerch
committed
Jul 08, 2015




34



		this.debugger = debugger;








bidi solver



Johannes Lerch
committed
Mar 20, 2015




35


36


37



	}
	
	@Override








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




38



	public void addIncomingEdge(CallEdge<Field, Fact, Stmt, Method> incEdge) {








bidi solver



Johannes Lerch
committed
Mar 20, 2015




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



		WrappedFact<Field, Fact, Stmt, Method> calleeSourceFact = incEdge.getCalleeSourceFact();
		PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer = perSourceAnalyzer.getOrCreate(calleeSourceFact.getFact());
		analyzer.addIncomingEdge(incEdge);
	}

	@Override
	public void addInitialSeed(Stmt startPoint, Fact val) {
		perSourceAnalyzer.getOrCreate(val).addInitialSeed(startPoint);
	}
	
	@Override
	public void addUnbalancedReturnFlow(WrappedFactAtStatement<Field, Fact, Stmt, Method> target, Stmt callSite) {
		perSourceAnalyzer.getOrCreate(context.zeroValue).scheduleUnbalancedReturnEdgeTo(target);
	}
}


















heros


src


heros


fieldsens


MethodAnalyzerImpl.java





Find file




Normal view



History



Permalink









MethodAnalyzerImpl.java




2.22 KiB









Newer










Older









bidi solver



Johannes Lerch
committed
Mar 20, 2015




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



/*******************************************************************************
 * Copyright (c) 2015 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




11



package heros.fieldsens;








bidi solver



Johannes Lerch
committed
Mar 20, 2015




12












restructuring

 


Johannes Lerch
committed
Jun 01, 2015




13


14


15


16



import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;
import heros.utilities.DefaultValueMap;









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




17



public class MethodAnalyzerImpl<Field,Fact, Stmt, Method> 








bidi solver



Johannes Lerch
committed
Mar 20, 2015




18


19


20



		implements MethodAnalyzer<Field, Fact, Stmt, Method> {

	private Method method;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




21


22



	private DefaultValueMap<Fact, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>> perSourceAnalyzer = 
			new DefaultValueMap<Fact, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>>() {








bidi solver



Johannes Lerch
committed
Mar 20, 2015




23


24



		@Override
		protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> createItem(Fact key) {








debugger

 


Johannes Lerch
committed
Jul 08, 2015




25



			return new PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>(method, key, context, debugger);








bidi solver



Johannes Lerch
committed
Mar 20, 2015




26


27


28



		}
	};
	private Context<Field, Fact, Stmt, Method> context;








debugger

 


Johannes Lerch
committed
Jul 08, 2015




29



	private Debugger<Field, Fact, Stmt, Method> debugger;








bidi solver



Johannes Lerch
committed
Mar 20, 2015




30












debugger

 


Johannes Lerch
committed
Jul 08, 2015




31



	MethodAnalyzerImpl(Method method, Context<Field, Fact, Stmt, Method> context, Debugger<Field, Fact, Stmt, Method> debugger) {








bidi solver



Johannes Lerch
committed
Mar 20, 2015




32


33



		this.method = method;
		this.context = context;








debugger

 


Johannes Lerch
committed
Jul 08, 2015




34



		this.debugger = debugger;








bidi solver



Johannes Lerch
committed
Mar 20, 2015




35


36


37



	}
	
	@Override








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




38



	public void addIncomingEdge(CallEdge<Field, Fact, Stmt, Method> incEdge) {








bidi solver



Johannes Lerch
committed
Mar 20, 2015




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



		WrappedFact<Field, Fact, Stmt, Method> calleeSourceFact = incEdge.getCalleeSourceFact();
		PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer = perSourceAnalyzer.getOrCreate(calleeSourceFact.getFact());
		analyzer.addIncomingEdge(incEdge);
	}

	@Override
	public void addInitialSeed(Stmt startPoint, Fact val) {
		perSourceAnalyzer.getOrCreate(val).addInitialSeed(startPoint);
	}
	
	@Override
	public void addUnbalancedReturnFlow(WrappedFactAtStatement<Field, Fact, Stmt, Method> target, Stmt callSite) {
		perSourceAnalyzer.getOrCreate(context.zeroValue).scheduleUnbalancedReturnEdgeTo(target);
	}
}













heros


src


heros


fieldsens


MethodAnalyzerImpl.java





Find file




Normal view



History



Permalink









heros


src


heros


fieldsens


MethodAnalyzerImpl.java





heros

src

heros

fieldsens

MethodAnalyzerImpl.java


Find file




Normal view



History



Permalink



Find file


Normal view

History

Permalink





MethodAnalyzerImpl.java




2.22 KiB









Newer










Older









bidi solver



Johannes Lerch
committed
Mar 20, 2015




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



/*******************************************************************************
 * Copyright (c) 2015 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




11



package heros.fieldsens;








bidi solver



Johannes Lerch
committed
Mar 20, 2015




12












restructuring

 


Johannes Lerch
committed
Jun 01, 2015




13


14


15


16



import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;
import heros.utilities.DefaultValueMap;









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




17



public class MethodAnalyzerImpl<Field,Fact, Stmt, Method> 








bidi solver



Johannes Lerch
committed
Mar 20, 2015




18


19


20



		implements MethodAnalyzer<Field, Fact, Stmt, Method> {

	private Method method;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




21


22



	private DefaultValueMap<Fact, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>> perSourceAnalyzer = 
			new DefaultValueMap<Fact, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>>() {








bidi solver



Johannes Lerch
committed
Mar 20, 2015




23


24



		@Override
		protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> createItem(Fact key) {








debugger

 


Johannes Lerch
committed
Jul 08, 2015




25



			return new PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>(method, key, context, debugger);








bidi solver



Johannes Lerch
committed
Mar 20, 2015




26


27


28



		}
	};
	private Context<Field, Fact, Stmt, Method> context;








debugger

 


Johannes Lerch
committed
Jul 08, 2015




29



	private Debugger<Field, Fact, Stmt, Method> debugger;








bidi solver



Johannes Lerch
committed
Mar 20, 2015




30












debugger

 


Johannes Lerch
committed
Jul 08, 2015




31



	MethodAnalyzerImpl(Method method, Context<Field, Fact, Stmt, Method> context, Debugger<Field, Fact, Stmt, Method> debugger) {








bidi solver



Johannes Lerch
committed
Mar 20, 2015




32


33



		this.method = method;
		this.context = context;








debugger

 


Johannes Lerch
committed
Jul 08, 2015




34



		this.debugger = debugger;








bidi solver



Johannes Lerch
committed
Mar 20, 2015




35


36


37



	}
	
	@Override








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




38



	public void addIncomingEdge(CallEdge<Field, Fact, Stmt, Method> incEdge) {








bidi solver



Johannes Lerch
committed
Mar 20, 2015




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



		WrappedFact<Field, Fact, Stmt, Method> calleeSourceFact = incEdge.getCalleeSourceFact();
		PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer = perSourceAnalyzer.getOrCreate(calleeSourceFact.getFact());
		analyzer.addIncomingEdge(incEdge);
	}

	@Override
	public void addInitialSeed(Stmt startPoint, Fact val) {
		perSourceAnalyzer.getOrCreate(val).addInitialSeed(startPoint);
	}
	
	@Override
	public void addUnbalancedReturnFlow(WrappedFactAtStatement<Field, Fact, Stmt, Method> target, Stmt callSite) {
		perSourceAnalyzer.getOrCreate(context.zeroValue).scheduleUnbalancedReturnEdgeTo(target);
	}
}









MethodAnalyzerImpl.java




2.22 KiB










MethodAnalyzerImpl.java




2.22 KiB









Newer










Older
NewerOlder







bidi solver



Johannes Lerch
committed
Mar 20, 2015




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



/*******************************************************************************
 * Copyright (c) 2015 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




11



package heros.fieldsens;








bidi solver



Johannes Lerch
committed
Mar 20, 2015




12












restructuring

 


Johannes Lerch
committed
Jun 01, 2015




13


14


15


16



import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;
import heros.utilities.DefaultValueMap;









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




17



public class MethodAnalyzerImpl<Field,Fact, Stmt, Method> 








bidi solver



Johannes Lerch
committed
Mar 20, 2015




18


19


20



		implements MethodAnalyzer<Field, Fact, Stmt, Method> {

	private Method method;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




21


22



	private DefaultValueMap<Fact, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>> perSourceAnalyzer = 
			new DefaultValueMap<Fact, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>>() {








bidi solver



Johannes Lerch
committed
Mar 20, 2015




23


24



		@Override
		protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> createItem(Fact key) {








debugger

 


Johannes Lerch
committed
Jul 08, 2015




25



			return new PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>(method, key, context, debugger);








bidi solver



Johannes Lerch
committed
Mar 20, 2015




26


27


28



		}
	};
	private Context<Field, Fact, Stmt, Method> context;








debugger

 


Johannes Lerch
committed
Jul 08, 2015




29



	private Debugger<Field, Fact, Stmt, Method> debugger;








bidi solver



Johannes Lerch
committed
Mar 20, 2015




30












debugger

 


Johannes Lerch
committed
Jul 08, 2015




31



	MethodAnalyzerImpl(Method method, Context<Field, Fact, Stmt, Method> context, Debugger<Field, Fact, Stmt, Method> debugger) {








bidi solver



Johannes Lerch
committed
Mar 20, 2015




32


33



		this.method = method;
		this.context = context;








debugger

 


Johannes Lerch
committed
Jul 08, 2015




34



		this.debugger = debugger;








bidi solver



Johannes Lerch
committed
Mar 20, 2015




35


36


37



	}
	
	@Override








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




38



	public void addIncomingEdge(CallEdge<Field, Fact, Stmt, Method> incEdge) {








bidi solver



Johannes Lerch
committed
Mar 20, 2015




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



		WrappedFact<Field, Fact, Stmt, Method> calleeSourceFact = incEdge.getCalleeSourceFact();
		PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer = perSourceAnalyzer.getOrCreate(calleeSourceFact.getFact());
		analyzer.addIncomingEdge(incEdge);
	}

	@Override
	public void addInitialSeed(Stmt startPoint, Fact val) {
		perSourceAnalyzer.getOrCreate(val).addInitialSeed(startPoint);
	}
	
	@Override
	public void addUnbalancedReturnFlow(WrappedFactAtStatement<Field, Fact, Stmt, Method> target, Stmt callSite) {
		perSourceAnalyzer.getOrCreate(context.zeroValue).scheduleUnbalancedReturnEdgeTo(target);
	}
}











bidi solver



Johannes Lerch
committed
Mar 20, 2015




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



/*******************************************************************************
 * Copyright (c) 2015 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




11



package heros.fieldsens;








bidi solver



Johannes Lerch
committed
Mar 20, 2015




12












restructuring

 


Johannes Lerch
committed
Jun 01, 2015




13


14


15


16



import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;
import heros.utilities.DefaultValueMap;









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




17



public class MethodAnalyzerImpl<Field,Fact, Stmt, Method> 








bidi solver



Johannes Lerch
committed
Mar 20, 2015




18


19


20



		implements MethodAnalyzer<Field, Fact, Stmt, Method> {

	private Method method;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




21


22



	private DefaultValueMap<Fact, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>> perSourceAnalyzer = 
			new DefaultValueMap<Fact, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>>() {








bidi solver



Johannes Lerch
committed
Mar 20, 2015




23


24



		@Override
		protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> createItem(Fact key) {








debugger

 


Johannes Lerch
committed
Jul 08, 2015




25



			return new PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>(method, key, context, debugger);








bidi solver



Johannes Lerch
committed
Mar 20, 2015




26


27


28



		}
	};
	private Context<Field, Fact, Stmt, Method> context;








debugger

 


Johannes Lerch
committed
Jul 08, 2015




29



	private Debugger<Field, Fact, Stmt, Method> debugger;








bidi solver



Johannes Lerch
committed
Mar 20, 2015




30












debugger

 


Johannes Lerch
committed
Jul 08, 2015




31



	MethodAnalyzerImpl(Method method, Context<Field, Fact, Stmt, Method> context, Debugger<Field, Fact, Stmt, Method> debugger) {








bidi solver



Johannes Lerch
committed
Mar 20, 2015




32


33



		this.method = method;
		this.context = context;








debugger

 


Johannes Lerch
committed
Jul 08, 2015




34



		this.debugger = debugger;








bidi solver



Johannes Lerch
committed
Mar 20, 2015




35


36


37



	}
	
	@Override








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




38



	public void addIncomingEdge(CallEdge<Field, Fact, Stmt, Method> incEdge) {








bidi solver



Johannes Lerch
committed
Mar 20, 2015




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



		WrappedFact<Field, Fact, Stmt, Method> calleeSourceFact = incEdge.getCalleeSourceFact();
		PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer = perSourceAnalyzer.getOrCreate(calleeSourceFact.getFact());
		analyzer.addIncomingEdge(incEdge);
	}

	@Override
	public void addInitialSeed(Stmt startPoint, Fact val) {
		perSourceAnalyzer.getOrCreate(val).addInitialSeed(startPoint);
	}
	
	@Override
	public void addUnbalancedReturnFlow(WrappedFactAtStatement<Field, Fact, Stmt, Method> target, Stmt callSite) {
		perSourceAnalyzer.getOrCreate(context.zeroValue).scheduleUnbalancedReturnEdgeTo(target);
	}
}









bidi solver



Johannes Lerch
committed
Mar 20, 2015




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



/*******************************************************************************
 * Copyright (c) 2015 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/








renaming package

 


Johannes Lerch
committed
Jun 01, 2015




11



package heros.fieldsens;








bidi solver



Johannes Lerch
committed
Mar 20, 2015




12












restructuring

 


Johannes Lerch
committed
Jun 01, 2015




13


14


15


16



import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;
import heros.utilities.DefaultValueMap;









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




17



public class MethodAnalyzerImpl<Field,Fact, Stmt, Method> 








bidi solver



Johannes Lerch
committed
Mar 20, 2015




18


19


20



		implements MethodAnalyzer<Field, Fact, Stmt, Method> {

	private Method method;








restructuring

 


Johannes Lerch
committed
Jun 01, 2015




21


22



	private DefaultValueMap<Fact, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>> perSourceAnalyzer = 
			new DefaultValueMap<Fact, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>>() {








bidi solver



Johannes Lerch
committed
Mar 20, 2015




23


24



		@Override
		protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> createItem(Fact key) {








debugger

 


Johannes Lerch
committed
Jul 08, 2015




25



			return new PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>(method, key, context, debugger);








bidi solver



Johannes Lerch
committed
Mar 20, 2015




26


27


28



		}
	};
	private Context<Field, Fact, Stmt, Method> context;








debugger

 


Johannes Lerch
committed
Jul 08, 2015




29



	private Debugger<Field, Fact, Stmt, Method> debugger;








bidi solver



Johannes Lerch
committed
Mar 20, 2015




30












debugger

 


Johannes Lerch
committed
Jul 08, 2015




31



	MethodAnalyzerImpl(Method method, Context<Field, Fact, Stmt, Method> context, Debugger<Field, Fact, Stmt, Method> debugger) {








bidi solver



Johannes Lerch
committed
Mar 20, 2015




32


33



		this.method = method;
		this.context = context;








debugger

 


Johannes Lerch
committed
Jul 08, 2015




34



		this.debugger = debugger;








bidi solver



Johannes Lerch
committed
Mar 20, 2015




35


36


37



	}
	
	@Override








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




38



	public void addIncomingEdge(CallEdge<Field, Fact, Stmt, Method> incEdge) {








bidi solver



Johannes Lerch
committed
Mar 20, 2015




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



		WrappedFact<Field, Fact, Stmt, Method> calleeSourceFact = incEdge.getCalleeSourceFact();
		PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer = perSourceAnalyzer.getOrCreate(calleeSourceFact.getFact());
		analyzer.addIncomingEdge(incEdge);
	}

	@Override
	public void addInitialSeed(Stmt startPoint, Fact val) {
		perSourceAnalyzer.getOrCreate(val).addInitialSeed(startPoint);
	}
	
	@Override
	public void addUnbalancedReturnFlow(WrappedFactAtStatement<Field, Fact, Stmt, Method> target, Stmt callSite) {
		perSourceAnalyzer.getOrCreate(context.zeroValue).scheduleUnbalancedReturnEdgeTo(target);
	}
}







bidi solver



Johannes Lerch
committed
Mar 20, 2015




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



/*******************************************************************************
 * Copyright (c) 2015 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/






bidi solver



Johannes Lerch
committed
Mar 20, 2015



bidi solver


bidi solver

Johannes Lerch
committed
Mar 20, 2015


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


/*******************************************************************************
 * Copyright (c) 2015 Johannes Lerch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Johannes Lerch - initial API and implementation
 ******************************************************************************/

/*******************************************************************************/******************************************************************************* * Copyright (c) 2015 Johannes Lerch. * Copyright (c) 2015 Johannes Lerch. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Johannes Lerch - initial API and implementation *     Johannes Lerch - initial API and implementation ******************************************************************************/ ******************************************************************************/




renaming package

 


Johannes Lerch
committed
Jun 01, 2015




11



package heros.fieldsens;






renaming package

 


Johannes Lerch
committed
Jun 01, 2015



renaming package

 

renaming package

Johannes Lerch
committed
Jun 01, 2015


11


package heros.fieldsens;

package heros.fieldsens;packageheros.fieldsens;




bidi solver



Johannes Lerch
committed
Mar 20, 2015




12










bidi solver



Johannes Lerch
committed
Mar 20, 2015



bidi solver


bidi solver

Johannes Lerch
committed
Mar 20, 2015


12









restructuring

 


Johannes Lerch
committed
Jun 01, 2015




13


14


15


16



import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;
import heros.utilities.DefaultValueMap;







restructuring

 


Johannes Lerch
committed
Jun 01, 2015



restructuring

 

restructuring

Johannes Lerch
committed
Jun 01, 2015


13


14


15


16


import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;
import heros.utilities.DefaultValueMap;


import heros.fieldsens.structs.WrappedFact;importheros.fieldsens.structs.WrappedFact;import heros.fieldsens.structs.WrappedFactAtStatement;importheros.fieldsens.structs.WrappedFactAtStatement;import heros.utilities.DefaultValueMap;importheros.utilities.DefaultValueMap;




removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




17



public class MethodAnalyzerImpl<Field,Fact, Stmt, Method> 






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


17


public class MethodAnalyzerImpl<Field,Fact, Stmt, Method> 

public class MethodAnalyzerImpl<Field,Fact, Stmt, Method> publicclassMethodAnalyzerImpl<Field,Fact,Stmt,Method>




bidi solver



Johannes Lerch
committed
Mar 20, 2015




18


19


20



		implements MethodAnalyzer<Field, Fact, Stmt, Method> {

	private Method method;






bidi solver



Johannes Lerch
committed
Mar 20, 2015



bidi solver


bidi solver

Johannes Lerch
committed
Mar 20, 2015


18


19


20


		implements MethodAnalyzer<Field, Fact, Stmt, Method> {

	private Method method;

		implements MethodAnalyzer<Field, Fact, Stmt, Method> {implementsMethodAnalyzer<Field,Fact,Stmt,Method>{	private Method method;privateMethodmethod;




restructuring

 


Johannes Lerch
committed
Jun 01, 2015




21


22



	private DefaultValueMap<Fact, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>> perSourceAnalyzer = 
			new DefaultValueMap<Fact, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>>() {






restructuring

 


Johannes Lerch
committed
Jun 01, 2015



restructuring

 

restructuring

Johannes Lerch
committed
Jun 01, 2015


21


22


	private DefaultValueMap<Fact, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>> perSourceAnalyzer = 
			new DefaultValueMap<Fact, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>>() {

	private DefaultValueMap<Fact, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>> perSourceAnalyzer = privateDefaultValueMap<Fact,PerAccessPathMethodAnalyzer<Field,Fact,Stmt,Method>>perSourceAnalyzer=			new DefaultValueMap<Fact, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>>() {newDefaultValueMap<Fact,PerAccessPathMethodAnalyzer<Field,Fact,Stmt,Method>>(){




bidi solver



Johannes Lerch
committed
Mar 20, 2015




23


24



		@Override
		protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> createItem(Fact key) {






bidi solver



Johannes Lerch
committed
Mar 20, 2015



bidi solver


bidi solver

Johannes Lerch
committed
Mar 20, 2015


23


24


		@Override
		protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> createItem(Fact key) {

		@Override@Override		protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> createItem(Fact key) {protectedPerAccessPathMethodAnalyzer<Field,Fact,Stmt,Method>createItem(Factkey){




debugger

 


Johannes Lerch
committed
Jul 08, 2015




25



			return new PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>(method, key, context, debugger);






debugger

 


Johannes Lerch
committed
Jul 08, 2015



debugger

 

debugger

Johannes Lerch
committed
Jul 08, 2015


25


			return new PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>(method, key, context, debugger);

			return new PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>(method, key, context, debugger);returnnewPerAccessPathMethodAnalyzer<Field,Fact,Stmt,Method>(method,key,context,debugger);




bidi solver



Johannes Lerch
committed
Mar 20, 2015




26


27


28



		}
	};
	private Context<Field, Fact, Stmt, Method> context;






bidi solver



Johannes Lerch
committed
Mar 20, 2015



bidi solver


bidi solver

Johannes Lerch
committed
Mar 20, 2015


26


27


28


		}
	};
	private Context<Field, Fact, Stmt, Method> context;

		}}	};};	private Context<Field, Fact, Stmt, Method> context;privateContext<Field,Fact,Stmt,Method>context;




debugger

 


Johannes Lerch
committed
Jul 08, 2015




29



	private Debugger<Field, Fact, Stmt, Method> debugger;






debugger

 


Johannes Lerch
committed
Jul 08, 2015



debugger

 

debugger

Johannes Lerch
committed
Jul 08, 2015


29


	private Debugger<Field, Fact, Stmt, Method> debugger;

	private Debugger<Field, Fact, Stmt, Method> debugger;privateDebugger<Field,Fact,Stmt,Method>debugger;




bidi solver



Johannes Lerch
committed
Mar 20, 2015




30










bidi solver



Johannes Lerch
committed
Mar 20, 2015



bidi solver


bidi solver

Johannes Lerch
committed
Mar 20, 2015


30









debugger

 


Johannes Lerch
committed
Jul 08, 2015




31



	MethodAnalyzerImpl(Method method, Context<Field, Fact, Stmt, Method> context, Debugger<Field, Fact, Stmt, Method> debugger) {






debugger

 


Johannes Lerch
committed
Jul 08, 2015



debugger

 

debugger

Johannes Lerch
committed
Jul 08, 2015


31


	MethodAnalyzerImpl(Method method, Context<Field, Fact, Stmt, Method> context, Debugger<Field, Fact, Stmt, Method> debugger) {

	MethodAnalyzerImpl(Method method, Context<Field, Fact, Stmt, Method> context, Debugger<Field, Fact, Stmt, Method> debugger) {MethodAnalyzerImpl(Methodmethod,Context<Field,Fact,Stmt,Method>context,Debugger<Field,Fact,Stmt,Method>debugger){




bidi solver



Johannes Lerch
committed
Mar 20, 2015




32


33



		this.method = method;
		this.context = context;






bidi solver



Johannes Lerch
committed
Mar 20, 2015



bidi solver


bidi solver

Johannes Lerch
committed
Mar 20, 2015


32


33


		this.method = method;
		this.context = context;

		this.method = method;this.method=method;		this.context = context;this.context=context;




debugger

 


Johannes Lerch
committed
Jul 08, 2015




34



		this.debugger = debugger;






debugger

 


Johannes Lerch
committed
Jul 08, 2015



debugger

 

debugger

Johannes Lerch
committed
Jul 08, 2015


34


		this.debugger = debugger;

		this.debugger = debugger;this.debugger=debugger;




bidi solver



Johannes Lerch
committed
Mar 20, 2015




35


36


37



	}
	
	@Override






bidi solver



Johannes Lerch
committed
Mar 20, 2015



bidi solver


bidi solver

Johannes Lerch
committed
Mar 20, 2015


35


36


37


	}
	
	@Override

	}}		@Override@Override




refactoring

 


Johannes Lerch
committed
Apr 01, 2015




38



	public void addIncomingEdge(CallEdge<Field, Fact, Stmt, Method> incEdge) {






refactoring

 


Johannes Lerch
committed
Apr 01, 2015



refactoring

 

refactoring

Johannes Lerch
committed
Apr 01, 2015


38


	public void addIncomingEdge(CallEdge<Field, Fact, Stmt, Method> incEdge) {

	public void addIncomingEdge(CallEdge<Field, Fact, Stmt, Method> incEdge) {publicvoidaddIncomingEdge(CallEdge<Field,Fact,Stmt,Method>incEdge){




bidi solver



Johannes Lerch
committed
Mar 20, 2015




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



		WrappedFact<Field, Fact, Stmt, Method> calleeSourceFact = incEdge.getCalleeSourceFact();
		PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer = perSourceAnalyzer.getOrCreate(calleeSourceFact.getFact());
		analyzer.addIncomingEdge(incEdge);
	}

	@Override
	public void addInitialSeed(Stmt startPoint, Fact val) {
		perSourceAnalyzer.getOrCreate(val).addInitialSeed(startPoint);
	}
	
	@Override
	public void addUnbalancedReturnFlow(WrappedFactAtStatement<Field, Fact, Stmt, Method> target, Stmt callSite) {
		perSourceAnalyzer.getOrCreate(context.zeroValue).scheduleUnbalancedReturnEdgeTo(target);
	}
}





bidi solver



Johannes Lerch
committed
Mar 20, 2015



bidi solver


bidi solver

Johannes Lerch
committed
Mar 20, 2015


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


		WrappedFact<Field, Fact, Stmt, Method> calleeSourceFact = incEdge.getCalleeSourceFact();
		PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer = perSourceAnalyzer.getOrCreate(calleeSourceFact.getFact());
		analyzer.addIncomingEdge(incEdge);
	}

	@Override
	public void addInitialSeed(Stmt startPoint, Fact val) {
		perSourceAnalyzer.getOrCreate(val).addInitialSeed(startPoint);
	}
	
	@Override
	public void addUnbalancedReturnFlow(WrappedFactAtStatement<Field, Fact, Stmt, Method> target, Stmt callSite) {
		perSourceAnalyzer.getOrCreate(context.zeroValue).scheduleUnbalancedReturnEdgeTo(target);
	}
}
		WrappedFact<Field, Fact, Stmt, Method> calleeSourceFact = incEdge.getCalleeSourceFact();WrappedFact<Field,Fact,Stmt,Method>calleeSourceFact=incEdge.getCalleeSourceFact();		PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer = perSourceAnalyzer.getOrCreate(calleeSourceFact.getFact());PerAccessPathMethodAnalyzer<Field,Fact,Stmt,Method>analyzer=perSourceAnalyzer.getOrCreate(calleeSourceFact.getFact());		analyzer.addIncomingEdge(incEdge);analyzer.addIncomingEdge(incEdge);	}}	@Override@Override	public void addInitialSeed(Stmt startPoint, Fact val) {publicvoidaddInitialSeed(StmtstartPoint,Factval){		perSourceAnalyzer.getOrCreate(val).addInitialSeed(startPoint);perSourceAnalyzer.getOrCreate(val).addInitialSeed(startPoint);	}}		@Override@Override	public void addUnbalancedReturnFlow(WrappedFactAtStatement<Field, Fact, Stmt, Method> target, Stmt callSite) {publicvoidaddUnbalancedReturnFlow(WrappedFactAtStatement<Field,Fact,Stmt,Method>target,StmtcallSite){		perSourceAnalyzer.getOrCreate(context.zeroValue).scheduleUnbalancedReturnEdgeTo(target);perSourceAnalyzer.getOrCreate(context.zeroValue).scheduleUnbalancedReturnEdgeTo(target);	}}}}