


GitLab











Explore




Sign in




GitLab








GitLab

Explore

Sign in











Joshua Garcia heros

90319070378f4cb7ea0f858ad47d6b5108a3b59d




















heros


src


heros


alias


Resolver.java





Find file




Normal view



History



Permalink









Resolver.java




2.29 KiB









Newer










Older









rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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
package heros.alias;









refactoring

 


Johannes Lerch
committed
Apr 01, 2015




13


14



import heros.alias.FlowFunction.Constraint;









rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




15


16


17


18



import java.util.List;

import com.google.common.collect.Lists;









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




19



public abstract class Resolver<Field, Fact, Stmt, Method> {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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




	private boolean interest = false;
	private List<InterestCallback<Field, Fact, Stmt, Method>> interestCallbacks = Lists.newLinkedList();
	protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer;
	private boolean canBeResolvedEmpty = false;
	
	public Resolver(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer) {
		this.analyzer = analyzer;
	}

	public abstract void resolve(Constraint<Field> constraint, InterestCallback<Field, Fact, Stmt, Method> callback);
	
	public void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Resolver<Field, Fact, Stmt, Method> resolver) {
		if(interest)
			return;
		
		log("Interest given");
		interest = true;
		for(InterestCallback<Field, Fact, Stmt, Method> callback : Lists.newLinkedList(interestCallbacks)) {
			callback.interest(analyzer, resolver);
		}
		
		if(canBeResolvedEmpty)
			interestCallbacks = null;
	}
	
	protected void canBeResolvedEmpty() {
		if(canBeResolvedEmpty)
			return;
		
		canBeResolvedEmpty = true;
		for(InterestCallback<Field, Fact, Stmt, Method> callback : Lists.newLinkedList(interestCallbacks)) {
			callback.canBeResolvedEmpty();
		}
		
		if(interest)
			interestCallbacks = null;
	}

	public boolean isInterestGiven() {
		return interest;
	}

	protected void registerCallback(InterestCallback<Field, Fact, Stmt, Method> callback) {
		if(interest) {
			callback.interest(analyzer, this);
		}
		else {
			log("Callback registered");
			interestCallbacks.add(callback);
		}

		if(canBeResolvedEmpty)
			callback.canBeResolvedEmpty();
	}
	
	protected abstract void log(String message);
	
}



















Joshua Garcia heros

90319070378f4cb7ea0f858ad47d6b5108a3b59d




















heros


src


heros


alias


Resolver.java





Find file




Normal view



History



Permalink









Resolver.java




2.29 KiB









Newer










Older









rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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
package heros.alias;









refactoring

 


Johannes Lerch
committed
Apr 01, 2015




13


14



import heros.alias.FlowFunction.Constraint;









rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




15


16


17


18



import java.util.List;

import com.google.common.collect.Lists;









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




19



public abstract class Resolver<Field, Fact, Stmt, Method> {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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




	private boolean interest = false;
	private List<InterestCallback<Field, Fact, Stmt, Method>> interestCallbacks = Lists.newLinkedList();
	protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer;
	private boolean canBeResolvedEmpty = false;
	
	public Resolver(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer) {
		this.analyzer = analyzer;
	}

	public abstract void resolve(Constraint<Field> constraint, InterestCallback<Field, Fact, Stmt, Method> callback);
	
	public void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Resolver<Field, Fact, Stmt, Method> resolver) {
		if(interest)
			return;
		
		log("Interest given");
		interest = true;
		for(InterestCallback<Field, Fact, Stmt, Method> callback : Lists.newLinkedList(interestCallbacks)) {
			callback.interest(analyzer, resolver);
		}
		
		if(canBeResolvedEmpty)
			interestCallbacks = null;
	}
	
	protected void canBeResolvedEmpty() {
		if(canBeResolvedEmpty)
			return;
		
		canBeResolvedEmpty = true;
		for(InterestCallback<Field, Fact, Stmt, Method> callback : Lists.newLinkedList(interestCallbacks)) {
			callback.canBeResolvedEmpty();
		}
		
		if(interest)
			interestCallbacks = null;
	}

	public boolean isInterestGiven() {
		return interest;
	}

	protected void registerCallback(InterestCallback<Field, Fact, Stmt, Method> callback) {
		if(interest) {
			callback.interest(analyzer, this);
		}
		else {
			log("Callback registered");
			interestCallbacks.add(callback);
		}

		if(canBeResolvedEmpty)
			callback.canBeResolvedEmpty();
	}
	
	protected abstract void log(String message);
	
}
















Joshua Garcia heros

90319070378f4cb7ea0f858ad47d6b5108a3b59d












Joshua Garcia heros

90319070378f4cb7ea0f858ad47d6b5108a3b59d










Joshua Garcia heros

90319070378f4cb7ea0f858ad47d6b5108a3b59d




Joshua Garciaherosheros
90319070378f4cb7ea0f858ad47d6b5108a3b59d













heros


src


heros


alias


Resolver.java





Find file




Normal view



History



Permalink









Resolver.java




2.29 KiB









Newer










Older









rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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
package heros.alias;









refactoring

 


Johannes Lerch
committed
Apr 01, 2015




13


14



import heros.alias.FlowFunction.Constraint;









rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




15


16


17


18



import java.util.List;

import com.google.common.collect.Lists;









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




19



public abstract class Resolver<Field, Fact, Stmt, Method> {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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




	private boolean interest = false;
	private List<InterestCallback<Field, Fact, Stmt, Method>> interestCallbacks = Lists.newLinkedList();
	protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer;
	private boolean canBeResolvedEmpty = false;
	
	public Resolver(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer) {
		this.analyzer = analyzer;
	}

	public abstract void resolve(Constraint<Field> constraint, InterestCallback<Field, Fact, Stmt, Method> callback);
	
	public void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Resolver<Field, Fact, Stmt, Method> resolver) {
		if(interest)
			return;
		
		log("Interest given");
		interest = true;
		for(InterestCallback<Field, Fact, Stmt, Method> callback : Lists.newLinkedList(interestCallbacks)) {
			callback.interest(analyzer, resolver);
		}
		
		if(canBeResolvedEmpty)
			interestCallbacks = null;
	}
	
	protected void canBeResolvedEmpty() {
		if(canBeResolvedEmpty)
			return;
		
		canBeResolvedEmpty = true;
		for(InterestCallback<Field, Fact, Stmt, Method> callback : Lists.newLinkedList(interestCallbacks)) {
			callback.canBeResolvedEmpty();
		}
		
		if(interest)
			interestCallbacks = null;
	}

	public boolean isInterestGiven() {
		return interest;
	}

	protected void registerCallback(InterestCallback<Field, Fact, Stmt, Method> callback) {
		if(interest) {
			callback.interest(analyzer, this);
		}
		else {
			log("Callback registered");
			interestCallbacks.add(callback);
		}

		if(canBeResolvedEmpty)
			callback.canBeResolvedEmpty();
	}
	
	protected abstract void log(String message);
	
}


















heros


src


heros


alias


Resolver.java





Find file




Normal view



History



Permalink









Resolver.java




2.29 KiB









Newer










Older









rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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
package heros.alias;









refactoring

 


Johannes Lerch
committed
Apr 01, 2015




13


14



import heros.alias.FlowFunction.Constraint;









rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




15


16


17


18



import java.util.List;

import com.google.common.collect.Lists;









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




19



public abstract class Resolver<Field, Fact, Stmt, Method> {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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




	private boolean interest = false;
	private List<InterestCallback<Field, Fact, Stmt, Method>> interestCallbacks = Lists.newLinkedList();
	protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer;
	private boolean canBeResolvedEmpty = false;
	
	public Resolver(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer) {
		this.analyzer = analyzer;
	}

	public abstract void resolve(Constraint<Field> constraint, InterestCallback<Field, Fact, Stmt, Method> callback);
	
	public void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Resolver<Field, Fact, Stmt, Method> resolver) {
		if(interest)
			return;
		
		log("Interest given");
		interest = true;
		for(InterestCallback<Field, Fact, Stmt, Method> callback : Lists.newLinkedList(interestCallbacks)) {
			callback.interest(analyzer, resolver);
		}
		
		if(canBeResolvedEmpty)
			interestCallbacks = null;
	}
	
	protected void canBeResolvedEmpty() {
		if(canBeResolvedEmpty)
			return;
		
		canBeResolvedEmpty = true;
		for(InterestCallback<Field, Fact, Stmt, Method> callback : Lists.newLinkedList(interestCallbacks)) {
			callback.canBeResolvedEmpty();
		}
		
		if(interest)
			interestCallbacks = null;
	}

	public boolean isInterestGiven() {
		return interest;
	}

	protected void registerCallback(InterestCallback<Field, Fact, Stmt, Method> callback) {
		if(interest) {
			callback.interest(analyzer, this);
		}
		else {
			log("Callback registered");
			interestCallbacks.add(callback);
		}

		if(canBeResolvedEmpty)
			callback.canBeResolvedEmpty();
	}
	
	protected abstract void log(String message);
	
}













heros


src


heros


alias


Resolver.java





Find file




Normal view



History



Permalink









heros


src


heros


alias


Resolver.java





heros

src

heros

alias

Resolver.java


Find file




Normal view



History



Permalink



Find file


Normal view

History

Permalink





Resolver.java




2.29 KiB









Newer










Older









rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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
package heros.alias;









refactoring

 


Johannes Lerch
committed
Apr 01, 2015




13


14



import heros.alias.FlowFunction.Constraint;









rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




15


16


17


18



import java.util.List;

import com.google.common.collect.Lists;









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




19



public abstract class Resolver<Field, Fact, Stmt, Method> {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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




	private boolean interest = false;
	private List<InterestCallback<Field, Fact, Stmt, Method>> interestCallbacks = Lists.newLinkedList();
	protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer;
	private boolean canBeResolvedEmpty = false;
	
	public Resolver(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer) {
		this.analyzer = analyzer;
	}

	public abstract void resolve(Constraint<Field> constraint, InterestCallback<Field, Fact, Stmt, Method> callback);
	
	public void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Resolver<Field, Fact, Stmt, Method> resolver) {
		if(interest)
			return;
		
		log("Interest given");
		interest = true;
		for(InterestCallback<Field, Fact, Stmt, Method> callback : Lists.newLinkedList(interestCallbacks)) {
			callback.interest(analyzer, resolver);
		}
		
		if(canBeResolvedEmpty)
			interestCallbacks = null;
	}
	
	protected void canBeResolvedEmpty() {
		if(canBeResolvedEmpty)
			return;
		
		canBeResolvedEmpty = true;
		for(InterestCallback<Field, Fact, Stmt, Method> callback : Lists.newLinkedList(interestCallbacks)) {
			callback.canBeResolvedEmpty();
		}
		
		if(interest)
			interestCallbacks = null;
	}

	public boolean isInterestGiven() {
		return interest;
	}

	protected void registerCallback(InterestCallback<Field, Fact, Stmt, Method> callback) {
		if(interest) {
			callback.interest(analyzer, this);
		}
		else {
			log("Callback registered");
			interestCallbacks.add(callback);
		}

		if(canBeResolvedEmpty)
			callback.canBeResolvedEmpty();
	}
	
	protected abstract void log(String message);
	
}









Resolver.java




2.29 KiB










Resolver.java




2.29 KiB









Newer










Older
NewerOlder







rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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
package heros.alias;









refactoring

 


Johannes Lerch
committed
Apr 01, 2015




13


14



import heros.alias.FlowFunction.Constraint;









rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




15


16


17


18



import java.util.List;

import com.google.common.collect.Lists;









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




19



public abstract class Resolver<Field, Fact, Stmt, Method> {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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




	private boolean interest = false;
	private List<InterestCallback<Field, Fact, Stmt, Method>> interestCallbacks = Lists.newLinkedList();
	protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer;
	private boolean canBeResolvedEmpty = false;
	
	public Resolver(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer) {
		this.analyzer = analyzer;
	}

	public abstract void resolve(Constraint<Field> constraint, InterestCallback<Field, Fact, Stmt, Method> callback);
	
	public void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Resolver<Field, Fact, Stmt, Method> resolver) {
		if(interest)
			return;
		
		log("Interest given");
		interest = true;
		for(InterestCallback<Field, Fact, Stmt, Method> callback : Lists.newLinkedList(interestCallbacks)) {
			callback.interest(analyzer, resolver);
		}
		
		if(canBeResolvedEmpty)
			interestCallbacks = null;
	}
	
	protected void canBeResolvedEmpty() {
		if(canBeResolvedEmpty)
			return;
		
		canBeResolvedEmpty = true;
		for(InterestCallback<Field, Fact, Stmt, Method> callback : Lists.newLinkedList(interestCallbacks)) {
			callback.canBeResolvedEmpty();
		}
		
		if(interest)
			interestCallbacks = null;
	}

	public boolean isInterestGiven() {
		return interest;
	}

	protected void registerCallback(InterestCallback<Field, Fact, Stmt, Method> callback) {
		if(interest) {
			callback.interest(analyzer, this);
		}
		else {
			log("Callback registered");
			interestCallbacks.add(callback);
		}

		if(canBeResolvedEmpty)
			callback.canBeResolvedEmpty();
	}
	
	protected abstract void log(String message);
	
}











rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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
package heros.alias;









refactoring

 


Johannes Lerch
committed
Apr 01, 2015




13


14



import heros.alias.FlowFunction.Constraint;









rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




15


16


17


18



import java.util.List;

import com.google.common.collect.Lists;









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




19



public abstract class Resolver<Field, Fact, Stmt, Method> {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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




	private boolean interest = false;
	private List<InterestCallback<Field, Fact, Stmt, Method>> interestCallbacks = Lists.newLinkedList();
	protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer;
	private boolean canBeResolvedEmpty = false;
	
	public Resolver(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer) {
		this.analyzer = analyzer;
	}

	public abstract void resolve(Constraint<Field> constraint, InterestCallback<Field, Fact, Stmt, Method> callback);
	
	public void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Resolver<Field, Fact, Stmt, Method> resolver) {
		if(interest)
			return;
		
		log("Interest given");
		interest = true;
		for(InterestCallback<Field, Fact, Stmt, Method> callback : Lists.newLinkedList(interestCallbacks)) {
			callback.interest(analyzer, resolver);
		}
		
		if(canBeResolvedEmpty)
			interestCallbacks = null;
	}
	
	protected void canBeResolvedEmpty() {
		if(canBeResolvedEmpty)
			return;
		
		canBeResolvedEmpty = true;
		for(InterestCallback<Field, Fact, Stmt, Method> callback : Lists.newLinkedList(interestCallbacks)) {
			callback.canBeResolvedEmpty();
		}
		
		if(interest)
			interestCallbacks = null;
	}

	public boolean isInterestGiven() {
		return interest;
	}

	protected void registerCallback(InterestCallback<Field, Fact, Stmt, Method> callback) {
		if(interest) {
			callback.interest(analyzer, this);
		}
		else {
			log("Callback registered");
			interestCallbacks.add(callback);
		}

		if(canBeResolvedEmpty)
			callback.canBeResolvedEmpty();
	}
	
	protected abstract void log(String message);
	
}









rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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
package heros.alias;









refactoring

 


Johannes Lerch
committed
Apr 01, 2015




13


14



import heros.alias.FlowFunction.Constraint;









rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




15


16


17


18



import java.util.List;

import com.google.common.collect.Lists;









removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




19



public abstract class Resolver<Field, Fact, Stmt, Method> {








rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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




	private boolean interest = false;
	private List<InterestCallback<Field, Fact, Stmt, Method>> interestCallbacks = Lists.newLinkedList();
	protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer;
	private boolean canBeResolvedEmpty = false;
	
	public Resolver(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer) {
		this.analyzer = analyzer;
	}

	public abstract void resolve(Constraint<Field> constraint, InterestCallback<Field, Fact, Stmt, Method> callback);
	
	public void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Resolver<Field, Fact, Stmt, Method> resolver) {
		if(interest)
			return;
		
		log("Interest given");
		interest = true;
		for(InterestCallback<Field, Fact, Stmt, Method> callback : Lists.newLinkedList(interestCallbacks)) {
			callback.interest(analyzer, resolver);
		}
		
		if(canBeResolvedEmpty)
			interestCallbacks = null;
	}
	
	protected void canBeResolvedEmpty() {
		if(canBeResolvedEmpty)
			return;
		
		canBeResolvedEmpty = true;
		for(InterestCallback<Field, Fact, Stmt, Method> callback : Lists.newLinkedList(interestCallbacks)) {
			callback.canBeResolvedEmpty();
		}
		
		if(interest)
			interestCallbacks = null;
	}

	public boolean isInterestGiven() {
		return interest;
	}

	protected void registerCallback(InterestCallback<Field, Fact, Stmt, Method> callback) {
		if(interest) {
			callback.interest(analyzer, this);
		}
		else {
			log("Callback registered");
			interestCallbacks.add(callback);
		}

		if(canBeResolvedEmpty)
			callback.canBeResolvedEmpty();
	}
	
	protected abstract void log(String message);
	
}







rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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
package heros.alias;







rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


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
package heros.alias;


/*******************************************************************************/******************************************************************************* * Copyright (c) 2015 Johannes Lerch. * Copyright (c) 2015 Johannes Lerch. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Johannes Lerch - initial API and implementation *     Johannes Lerch - initial API and implementation ******************************************************************************/ ******************************************************************************/package heros.alias;packageheros.alias;




refactoring

 


Johannes Lerch
committed
Apr 01, 2015




13


14



import heros.alias.FlowFunction.Constraint;







refactoring

 


Johannes Lerch
committed
Apr 01, 2015



refactoring

 

refactoring

Johannes Lerch
committed
Apr 01, 2015


13


14


import heros.alias.FlowFunction.Constraint;


import heros.alias.FlowFunction.Constraint;importheros.alias.FlowFunction.Constraint;




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




15


16


17


18



import java.util.List;

import com.google.common.collect.Lists;







rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


15


16


17


18


import java.util.List;

import com.google.common.collect.Lists;


import java.util.List;importjava.util.List;import com.google.common.collect.Lists;importcom.google.common.collect.Lists;




removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




19



public abstract class Resolver<Field, Fact, Stmt, Method> {






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


19


public abstract class Resolver<Field, Fact, Stmt, Method> {

public abstract class Resolver<Field, Fact, Stmt, Method> {publicabstractclassResolver<Field,Fact,Stmt,Method>{




rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015




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




	private boolean interest = false;
	private List<InterestCallback<Field, Fact, Stmt, Method>> interestCallbacks = Lists.newLinkedList();
	protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer;
	private boolean canBeResolvedEmpty = false;
	
	public Resolver(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer) {
		this.analyzer = analyzer;
	}

	public abstract void resolve(Constraint<Field> constraint, InterestCallback<Field, Fact, Stmt, Method> callback);
	
	public void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Resolver<Field, Fact, Stmt, Method> resolver) {
		if(interest)
			return;
		
		log("Interest given");
		interest = true;
		for(InterestCallback<Field, Fact, Stmt, Method> callback : Lists.newLinkedList(interestCallbacks)) {
			callback.interest(analyzer, resolver);
		}
		
		if(canBeResolvedEmpty)
			interestCallbacks = null;
	}
	
	protected void canBeResolvedEmpty() {
		if(canBeResolvedEmpty)
			return;
		
		canBeResolvedEmpty = true;
		for(InterestCallback<Field, Fact, Stmt, Method> callback : Lists.newLinkedList(interestCallbacks)) {
			callback.canBeResolvedEmpty();
		}
		
		if(interest)
			interestCallbacks = null;
	}

	public boolean isInterestGiven() {
		return interest;
	}

	protected void registerCallback(InterestCallback<Field, Fact, Stmt, Method> callback) {
		if(interest) {
			callback.interest(analyzer, this);
		}
		else {
			log("Callback registered");
			interestCallbacks.add(callback);
		}

		if(canBeResolvedEmpty)
			callback.canBeResolvedEmpty();
	}
	
	protected abstract void log(String message);
	
}





rewrite of ifds solver



Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver


rewrite of ifds solver

Johannes Lerch
committed
Mar 19, 2015


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



	private boolean interest = false;
	private List<InterestCallback<Field, Fact, Stmt, Method>> interestCallbacks = Lists.newLinkedList();
	protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer;
	private boolean canBeResolvedEmpty = false;
	
	public Resolver(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer) {
		this.analyzer = analyzer;
	}

	public abstract void resolve(Constraint<Field> constraint, InterestCallback<Field, Fact, Stmt, Method> callback);
	
	public void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Resolver<Field, Fact, Stmt, Method> resolver) {
		if(interest)
			return;
		
		log("Interest given");
		interest = true;
		for(InterestCallback<Field, Fact, Stmt, Method> callback : Lists.newLinkedList(interestCallbacks)) {
			callback.interest(analyzer, resolver);
		}
		
		if(canBeResolvedEmpty)
			interestCallbacks = null;
	}
	
	protected void canBeResolvedEmpty() {
		if(canBeResolvedEmpty)
			return;
		
		canBeResolvedEmpty = true;
		for(InterestCallback<Field, Fact, Stmt, Method> callback : Lists.newLinkedList(interestCallbacks)) {
			callback.canBeResolvedEmpty();
		}
		
		if(interest)
			interestCallbacks = null;
	}

	public boolean isInterestGiven() {
		return interest;
	}

	protected void registerCallback(InterestCallback<Field, Fact, Stmt, Method> callback) {
		if(interest) {
			callback.interest(analyzer, this);
		}
		else {
			log("Callback registered");
			interestCallbacks.add(callback);
		}

		if(canBeResolvedEmpty)
			callback.canBeResolvedEmpty();
	}
	
	protected abstract void log(String message);
	
}
	private boolean interest = false;privatebooleaninterest=false;	private List<InterestCallback<Field, Fact, Stmt, Method>> interestCallbacks = Lists.newLinkedList();privateList<InterestCallback<Field,Fact,Stmt,Method>>interestCallbacks=Lists.newLinkedList();	protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer;protectedPerAccessPathMethodAnalyzer<Field,Fact,Stmt,Method>analyzer;	private boolean canBeResolvedEmpty = false;privatebooleancanBeResolvedEmpty=false;		public Resolver(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer) {publicResolver(PerAccessPathMethodAnalyzer<Field,Fact,Stmt,Method>analyzer){		this.analyzer = analyzer;this.analyzer=analyzer;	}}	public abstract void resolve(Constraint<Field> constraint, InterestCallback<Field, Fact, Stmt, Method> callback);publicabstractvoidresolve(Constraint<Field>constraint,InterestCallback<Field,Fact,Stmt,Method>callback);		public void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Resolver<Field, Fact, Stmt, Method> resolver) {publicvoidinterest(PerAccessPathMethodAnalyzer<Field,Fact,Stmt,Method>analyzer,Resolver<Field,Fact,Stmt,Method>resolver){		if(interest)if(interest)			return;return;				log("Interest given");log("Interest given");		interest = true;interest=true;		for(InterestCallback<Field, Fact, Stmt, Method> callback : Lists.newLinkedList(interestCallbacks)) {for(InterestCallback<Field,Fact,Stmt,Method>callback:Lists.newLinkedList(interestCallbacks)){			callback.interest(analyzer, resolver);callback.interest(analyzer,resolver);		}}				if(canBeResolvedEmpty)if(canBeResolvedEmpty)			interestCallbacks = null;interestCallbacks=null;	}}		protected void canBeResolvedEmpty() {protectedvoidcanBeResolvedEmpty(){		if(canBeResolvedEmpty)if(canBeResolvedEmpty)			return;return;				canBeResolvedEmpty = true;canBeResolvedEmpty=true;		for(InterestCallback<Field, Fact, Stmt, Method> callback : Lists.newLinkedList(interestCallbacks)) {for(InterestCallback<Field,Fact,Stmt,Method>callback:Lists.newLinkedList(interestCallbacks)){			callback.canBeResolvedEmpty();callback.canBeResolvedEmpty();		}}				if(interest)if(interest)			interestCallbacks = null;interestCallbacks=null;	}}	public boolean isInterestGiven() {publicbooleanisInterestGiven(){		return interest;returninterest;	}}	protected void registerCallback(InterestCallback<Field, Fact, Stmt, Method> callback) {protectedvoidregisterCallback(InterestCallback<Field,Fact,Stmt,Method>callback){		if(interest) {if(interest){			callback.interest(analyzer, this);callback.interest(analyzer,this);		}}		else {else{			log("Callback registered");log("Callback registered");			interestCallbacks.add(callback);interestCallbacks.add(callback);		}}		if(canBeResolvedEmpty)if(canBeResolvedEmpty)			callback.canBeResolvedEmpty();callback.canBeResolvedEmpty();	}}		protected abstract void log(String message);protectedabstractvoidlog(Stringmessage);	}}