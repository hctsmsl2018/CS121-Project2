


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


SourceStmtAnnotatedMethodAnalyzer.java





Find file




Normal view



History



Permalink









SourceStmtAnnotatedMethodAnalyzer.java




3.39 KiB









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








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




11



package heros.alias;








bidi solver



Johannes Lerch
committed
Mar 20, 2015




12


13













removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




14



public class SourceStmtAnnotatedMethodAnalyzer<Field, Fact, Stmt, Method>








bidi solver



Johannes Lerch
committed
Mar 20, 2015




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



		implements MethodAnalyzer<Field, Fact, Stmt, Method> {

	private Method method;
	private CacheMap<Key<Fact, Stmt>, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>> perSourceAnalyzer = 
			new CacheMap<Key<Fact, Stmt>, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>>() {
		@Override
		protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> createItem(Key<Fact, Stmt> key) {
			return new PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>(method, key.fact, context);
		}
	};
	private Context<Field, Fact, Stmt, Method> context;
	private Synchronizer<Stmt> synchronizer;
	
	public SourceStmtAnnotatedMethodAnalyzer(Method method, Context<Field, Fact, Stmt, Method> context, Synchronizer<Stmt> synchronizer) {
		this.method = method;
		this.context = context;
		this.synchronizer = synchronizer;
	}
	
	@Override








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




35



	public void addIncomingEdge(CallEdge<Field, Fact, Stmt, Method> incEdge) {








bidi solver



Johannes Lerch
committed
Mar 20, 2015




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



		WrappedFact<Field, Fact, Stmt, Method> calleeSourceFact = incEdge.getCalleeSourceFact();
		Key<Fact, Stmt> key = new Key<Fact, Stmt>(calleeSourceFact.getFact(), null);
		PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer = perSourceAnalyzer.getOrCreate(key);
		analyzer.addIncomingEdge(incEdge);		
	}

	@Override
	public void addInitialSeed(Stmt startPoint, Fact val) {
		Key<Fact, Stmt> key = new Key<Fact, Stmt>(val, startPoint);
		perSourceAnalyzer.getOrCreate(key).addInitialSeed(startPoint);
	}

	@Override
	public void addUnbalancedReturnFlow(final WrappedFactAtStatement<Field, Fact, Stmt, Method> target, final Stmt callSite) {
		synchronizer.synchronizeOnStmt(callSite, new Runnable() {
			@Override
			public void run() {
				Key<Fact, Stmt> key = new Key<Fact, Stmt>(context.zeroValue, callSite);
				perSourceAnalyzer.getOrCreate(key).scheduleUnbalancedReturnEdgeTo(target);				
			}
		});
	}
	
	public static interface Synchronizer<Stmt> {
		void synchronizeOnStmt(Stmt stmt, Runnable job);
	}
	
	private static class Key<Fact, Stmt> {
		private Fact fact;
		private Stmt stmt;

		private Key(Fact fact, Stmt stmt) {
			this.fact = fact;
			this.stmt = stmt;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((fact == null) ? 0 : fact.hashCode());
			result = prime * result + ((stmt == null) ? 0 : stmt.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Key other = (Key) obj;
			if (fact == null) {
				if (other.fact != null)
					return false;
			} else if (!fact.equals(other.fact))
				return false;
			if (stmt == null) {
				if (other.stmt != null)
					return false;
			} else if (!stmt.equals(other.stmt))
				return false;
			return true;
		}
	}
}



















Joshua Garcia heros

90319070378f4cb7ea0f858ad47d6b5108a3b59d




















heros


src


heros


alias


SourceStmtAnnotatedMethodAnalyzer.java





Find file




Normal view



History



Permalink









SourceStmtAnnotatedMethodAnalyzer.java




3.39 KiB









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








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




11



package heros.alias;








bidi solver



Johannes Lerch
committed
Mar 20, 2015




12


13













removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




14



public class SourceStmtAnnotatedMethodAnalyzer<Field, Fact, Stmt, Method>








bidi solver



Johannes Lerch
committed
Mar 20, 2015




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



		implements MethodAnalyzer<Field, Fact, Stmt, Method> {

	private Method method;
	private CacheMap<Key<Fact, Stmt>, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>> perSourceAnalyzer = 
			new CacheMap<Key<Fact, Stmt>, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>>() {
		@Override
		protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> createItem(Key<Fact, Stmt> key) {
			return new PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>(method, key.fact, context);
		}
	};
	private Context<Field, Fact, Stmt, Method> context;
	private Synchronizer<Stmt> synchronizer;
	
	public SourceStmtAnnotatedMethodAnalyzer(Method method, Context<Field, Fact, Stmt, Method> context, Synchronizer<Stmt> synchronizer) {
		this.method = method;
		this.context = context;
		this.synchronizer = synchronizer;
	}
	
	@Override








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




35



	public void addIncomingEdge(CallEdge<Field, Fact, Stmt, Method> incEdge) {








bidi solver



Johannes Lerch
committed
Mar 20, 2015




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



		WrappedFact<Field, Fact, Stmt, Method> calleeSourceFact = incEdge.getCalleeSourceFact();
		Key<Fact, Stmt> key = new Key<Fact, Stmt>(calleeSourceFact.getFact(), null);
		PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer = perSourceAnalyzer.getOrCreate(key);
		analyzer.addIncomingEdge(incEdge);		
	}

	@Override
	public void addInitialSeed(Stmt startPoint, Fact val) {
		Key<Fact, Stmt> key = new Key<Fact, Stmt>(val, startPoint);
		perSourceAnalyzer.getOrCreate(key).addInitialSeed(startPoint);
	}

	@Override
	public void addUnbalancedReturnFlow(final WrappedFactAtStatement<Field, Fact, Stmt, Method> target, final Stmt callSite) {
		synchronizer.synchronizeOnStmt(callSite, new Runnable() {
			@Override
			public void run() {
				Key<Fact, Stmt> key = new Key<Fact, Stmt>(context.zeroValue, callSite);
				perSourceAnalyzer.getOrCreate(key).scheduleUnbalancedReturnEdgeTo(target);				
			}
		});
	}
	
	public static interface Synchronizer<Stmt> {
		void synchronizeOnStmt(Stmt stmt, Runnable job);
	}
	
	private static class Key<Fact, Stmt> {
		private Fact fact;
		private Stmt stmt;

		private Key(Fact fact, Stmt stmt) {
			this.fact = fact;
			this.stmt = stmt;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((fact == null) ? 0 : fact.hashCode());
			result = prime * result + ((stmt == null) ? 0 : stmt.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Key other = (Key) obj;
			if (fact == null) {
				if (other.fact != null)
					return false;
			} else if (!fact.equals(other.fact))
				return false;
			if (stmt == null) {
				if (other.stmt != null)
					return false;
			} else if (!stmt.equals(other.stmt))
				return false;
			return true;
		}
	}
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


SourceStmtAnnotatedMethodAnalyzer.java





Find file




Normal view



History



Permalink









SourceStmtAnnotatedMethodAnalyzer.java




3.39 KiB









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








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




11



package heros.alias;








bidi solver



Johannes Lerch
committed
Mar 20, 2015




12


13













removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




14



public class SourceStmtAnnotatedMethodAnalyzer<Field, Fact, Stmt, Method>








bidi solver



Johannes Lerch
committed
Mar 20, 2015




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



		implements MethodAnalyzer<Field, Fact, Stmt, Method> {

	private Method method;
	private CacheMap<Key<Fact, Stmt>, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>> perSourceAnalyzer = 
			new CacheMap<Key<Fact, Stmt>, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>>() {
		@Override
		protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> createItem(Key<Fact, Stmt> key) {
			return new PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>(method, key.fact, context);
		}
	};
	private Context<Field, Fact, Stmt, Method> context;
	private Synchronizer<Stmt> synchronizer;
	
	public SourceStmtAnnotatedMethodAnalyzer(Method method, Context<Field, Fact, Stmt, Method> context, Synchronizer<Stmt> synchronizer) {
		this.method = method;
		this.context = context;
		this.synchronizer = synchronizer;
	}
	
	@Override








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




35



	public void addIncomingEdge(CallEdge<Field, Fact, Stmt, Method> incEdge) {








bidi solver



Johannes Lerch
committed
Mar 20, 2015




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



		WrappedFact<Field, Fact, Stmt, Method> calleeSourceFact = incEdge.getCalleeSourceFact();
		Key<Fact, Stmt> key = new Key<Fact, Stmt>(calleeSourceFact.getFact(), null);
		PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer = perSourceAnalyzer.getOrCreate(key);
		analyzer.addIncomingEdge(incEdge);		
	}

	@Override
	public void addInitialSeed(Stmt startPoint, Fact val) {
		Key<Fact, Stmt> key = new Key<Fact, Stmt>(val, startPoint);
		perSourceAnalyzer.getOrCreate(key).addInitialSeed(startPoint);
	}

	@Override
	public void addUnbalancedReturnFlow(final WrappedFactAtStatement<Field, Fact, Stmt, Method> target, final Stmt callSite) {
		synchronizer.synchronizeOnStmt(callSite, new Runnable() {
			@Override
			public void run() {
				Key<Fact, Stmt> key = new Key<Fact, Stmt>(context.zeroValue, callSite);
				perSourceAnalyzer.getOrCreate(key).scheduleUnbalancedReturnEdgeTo(target);				
			}
		});
	}
	
	public static interface Synchronizer<Stmt> {
		void synchronizeOnStmt(Stmt stmt, Runnable job);
	}
	
	private static class Key<Fact, Stmt> {
		private Fact fact;
		private Stmt stmt;

		private Key(Fact fact, Stmt stmt) {
			this.fact = fact;
			this.stmt = stmt;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((fact == null) ? 0 : fact.hashCode());
			result = prime * result + ((stmt == null) ? 0 : stmt.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Key other = (Key) obj;
			if (fact == null) {
				if (other.fact != null)
					return false;
			} else if (!fact.equals(other.fact))
				return false;
			if (stmt == null) {
				if (other.stmt != null)
					return false;
			} else if (!stmt.equals(other.stmt))
				return false;
			return true;
		}
	}
}


















heros


src


heros


alias


SourceStmtAnnotatedMethodAnalyzer.java





Find file




Normal view



History



Permalink









SourceStmtAnnotatedMethodAnalyzer.java




3.39 KiB









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








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




11



package heros.alias;








bidi solver



Johannes Lerch
committed
Mar 20, 2015




12


13













removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




14



public class SourceStmtAnnotatedMethodAnalyzer<Field, Fact, Stmt, Method>








bidi solver



Johannes Lerch
committed
Mar 20, 2015




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



		implements MethodAnalyzer<Field, Fact, Stmt, Method> {

	private Method method;
	private CacheMap<Key<Fact, Stmt>, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>> perSourceAnalyzer = 
			new CacheMap<Key<Fact, Stmt>, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>>() {
		@Override
		protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> createItem(Key<Fact, Stmt> key) {
			return new PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>(method, key.fact, context);
		}
	};
	private Context<Field, Fact, Stmt, Method> context;
	private Synchronizer<Stmt> synchronizer;
	
	public SourceStmtAnnotatedMethodAnalyzer(Method method, Context<Field, Fact, Stmt, Method> context, Synchronizer<Stmt> synchronizer) {
		this.method = method;
		this.context = context;
		this.synchronizer = synchronizer;
	}
	
	@Override








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




35



	public void addIncomingEdge(CallEdge<Field, Fact, Stmt, Method> incEdge) {








bidi solver



Johannes Lerch
committed
Mar 20, 2015




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



		WrappedFact<Field, Fact, Stmt, Method> calleeSourceFact = incEdge.getCalleeSourceFact();
		Key<Fact, Stmt> key = new Key<Fact, Stmt>(calleeSourceFact.getFact(), null);
		PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer = perSourceAnalyzer.getOrCreate(key);
		analyzer.addIncomingEdge(incEdge);		
	}

	@Override
	public void addInitialSeed(Stmt startPoint, Fact val) {
		Key<Fact, Stmt> key = new Key<Fact, Stmt>(val, startPoint);
		perSourceAnalyzer.getOrCreate(key).addInitialSeed(startPoint);
	}

	@Override
	public void addUnbalancedReturnFlow(final WrappedFactAtStatement<Field, Fact, Stmt, Method> target, final Stmt callSite) {
		synchronizer.synchronizeOnStmt(callSite, new Runnable() {
			@Override
			public void run() {
				Key<Fact, Stmt> key = new Key<Fact, Stmt>(context.zeroValue, callSite);
				perSourceAnalyzer.getOrCreate(key).scheduleUnbalancedReturnEdgeTo(target);				
			}
		});
	}
	
	public static interface Synchronizer<Stmt> {
		void synchronizeOnStmt(Stmt stmt, Runnable job);
	}
	
	private static class Key<Fact, Stmt> {
		private Fact fact;
		private Stmt stmt;

		private Key(Fact fact, Stmt stmt) {
			this.fact = fact;
			this.stmt = stmt;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((fact == null) ? 0 : fact.hashCode());
			result = prime * result + ((stmt == null) ? 0 : stmt.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Key other = (Key) obj;
			if (fact == null) {
				if (other.fact != null)
					return false;
			} else if (!fact.equals(other.fact))
				return false;
			if (stmt == null) {
				if (other.stmt != null)
					return false;
			} else if (!stmt.equals(other.stmt))
				return false;
			return true;
		}
	}
}













heros


src


heros


alias


SourceStmtAnnotatedMethodAnalyzer.java





Find file




Normal view



History



Permalink









heros


src


heros


alias


SourceStmtAnnotatedMethodAnalyzer.java





heros

src

heros

alias

SourceStmtAnnotatedMethodAnalyzer.java


Find file




Normal view



History



Permalink



Find file


Normal view

History

Permalink





SourceStmtAnnotatedMethodAnalyzer.java




3.39 KiB









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








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




11



package heros.alias;








bidi solver



Johannes Lerch
committed
Mar 20, 2015




12


13













removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




14



public class SourceStmtAnnotatedMethodAnalyzer<Field, Fact, Stmt, Method>








bidi solver



Johannes Lerch
committed
Mar 20, 2015




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



		implements MethodAnalyzer<Field, Fact, Stmt, Method> {

	private Method method;
	private CacheMap<Key<Fact, Stmt>, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>> perSourceAnalyzer = 
			new CacheMap<Key<Fact, Stmt>, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>>() {
		@Override
		protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> createItem(Key<Fact, Stmt> key) {
			return new PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>(method, key.fact, context);
		}
	};
	private Context<Field, Fact, Stmt, Method> context;
	private Synchronizer<Stmt> synchronizer;
	
	public SourceStmtAnnotatedMethodAnalyzer(Method method, Context<Field, Fact, Stmt, Method> context, Synchronizer<Stmt> synchronizer) {
		this.method = method;
		this.context = context;
		this.synchronizer = synchronizer;
	}
	
	@Override








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




35



	public void addIncomingEdge(CallEdge<Field, Fact, Stmt, Method> incEdge) {








bidi solver



Johannes Lerch
committed
Mar 20, 2015




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



		WrappedFact<Field, Fact, Stmt, Method> calleeSourceFact = incEdge.getCalleeSourceFact();
		Key<Fact, Stmt> key = new Key<Fact, Stmt>(calleeSourceFact.getFact(), null);
		PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer = perSourceAnalyzer.getOrCreate(key);
		analyzer.addIncomingEdge(incEdge);		
	}

	@Override
	public void addInitialSeed(Stmt startPoint, Fact val) {
		Key<Fact, Stmt> key = new Key<Fact, Stmt>(val, startPoint);
		perSourceAnalyzer.getOrCreate(key).addInitialSeed(startPoint);
	}

	@Override
	public void addUnbalancedReturnFlow(final WrappedFactAtStatement<Field, Fact, Stmt, Method> target, final Stmt callSite) {
		synchronizer.synchronizeOnStmt(callSite, new Runnable() {
			@Override
			public void run() {
				Key<Fact, Stmt> key = new Key<Fact, Stmt>(context.zeroValue, callSite);
				perSourceAnalyzer.getOrCreate(key).scheduleUnbalancedReturnEdgeTo(target);				
			}
		});
	}
	
	public static interface Synchronizer<Stmt> {
		void synchronizeOnStmt(Stmt stmt, Runnable job);
	}
	
	private static class Key<Fact, Stmt> {
		private Fact fact;
		private Stmt stmt;

		private Key(Fact fact, Stmt stmt) {
			this.fact = fact;
			this.stmt = stmt;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((fact == null) ? 0 : fact.hashCode());
			result = prime * result + ((stmt == null) ? 0 : stmt.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Key other = (Key) obj;
			if (fact == null) {
				if (other.fact != null)
					return false;
			} else if (!fact.equals(other.fact))
				return false;
			if (stmt == null) {
				if (other.stmt != null)
					return false;
			} else if (!stmt.equals(other.stmt))
				return false;
			return true;
		}
	}
}









SourceStmtAnnotatedMethodAnalyzer.java




3.39 KiB










SourceStmtAnnotatedMethodAnalyzer.java




3.39 KiB









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








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




11



package heros.alias;








bidi solver



Johannes Lerch
committed
Mar 20, 2015




12


13













removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




14



public class SourceStmtAnnotatedMethodAnalyzer<Field, Fact, Stmt, Method>








bidi solver



Johannes Lerch
committed
Mar 20, 2015




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



		implements MethodAnalyzer<Field, Fact, Stmt, Method> {

	private Method method;
	private CacheMap<Key<Fact, Stmt>, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>> perSourceAnalyzer = 
			new CacheMap<Key<Fact, Stmt>, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>>() {
		@Override
		protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> createItem(Key<Fact, Stmt> key) {
			return new PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>(method, key.fact, context);
		}
	};
	private Context<Field, Fact, Stmt, Method> context;
	private Synchronizer<Stmt> synchronizer;
	
	public SourceStmtAnnotatedMethodAnalyzer(Method method, Context<Field, Fact, Stmt, Method> context, Synchronizer<Stmt> synchronizer) {
		this.method = method;
		this.context = context;
		this.synchronizer = synchronizer;
	}
	
	@Override








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




35



	public void addIncomingEdge(CallEdge<Field, Fact, Stmt, Method> incEdge) {








bidi solver



Johannes Lerch
committed
Mar 20, 2015




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



		WrappedFact<Field, Fact, Stmt, Method> calleeSourceFact = incEdge.getCalleeSourceFact();
		Key<Fact, Stmt> key = new Key<Fact, Stmt>(calleeSourceFact.getFact(), null);
		PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer = perSourceAnalyzer.getOrCreate(key);
		analyzer.addIncomingEdge(incEdge);		
	}

	@Override
	public void addInitialSeed(Stmt startPoint, Fact val) {
		Key<Fact, Stmt> key = new Key<Fact, Stmt>(val, startPoint);
		perSourceAnalyzer.getOrCreate(key).addInitialSeed(startPoint);
	}

	@Override
	public void addUnbalancedReturnFlow(final WrappedFactAtStatement<Field, Fact, Stmt, Method> target, final Stmt callSite) {
		synchronizer.synchronizeOnStmt(callSite, new Runnable() {
			@Override
			public void run() {
				Key<Fact, Stmt> key = new Key<Fact, Stmt>(context.zeroValue, callSite);
				perSourceAnalyzer.getOrCreate(key).scheduleUnbalancedReturnEdgeTo(target);				
			}
		});
	}
	
	public static interface Synchronizer<Stmt> {
		void synchronizeOnStmt(Stmt stmt, Runnable job);
	}
	
	private static class Key<Fact, Stmt> {
		private Fact fact;
		private Stmt stmt;

		private Key(Fact fact, Stmt stmt) {
			this.fact = fact;
			this.stmt = stmt;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((fact == null) ? 0 : fact.hashCode());
			result = prime * result + ((stmt == null) ? 0 : stmt.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Key other = (Key) obj;
			if (fact == null) {
				if (other.fact != null)
					return false;
			} else if (!fact.equals(other.fact))
				return false;
			if (stmt == null) {
				if (other.stmt != null)
					return false;
			} else if (!stmt.equals(other.stmt))
				return false;
			return true;
		}
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








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




11



package heros.alias;








bidi solver



Johannes Lerch
committed
Mar 20, 2015




12


13













removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




14



public class SourceStmtAnnotatedMethodAnalyzer<Field, Fact, Stmt, Method>








bidi solver



Johannes Lerch
committed
Mar 20, 2015




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



		implements MethodAnalyzer<Field, Fact, Stmt, Method> {

	private Method method;
	private CacheMap<Key<Fact, Stmt>, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>> perSourceAnalyzer = 
			new CacheMap<Key<Fact, Stmt>, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>>() {
		@Override
		protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> createItem(Key<Fact, Stmt> key) {
			return new PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>(method, key.fact, context);
		}
	};
	private Context<Field, Fact, Stmt, Method> context;
	private Synchronizer<Stmt> synchronizer;
	
	public SourceStmtAnnotatedMethodAnalyzer(Method method, Context<Field, Fact, Stmt, Method> context, Synchronizer<Stmt> synchronizer) {
		this.method = method;
		this.context = context;
		this.synchronizer = synchronizer;
	}
	
	@Override








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




35



	public void addIncomingEdge(CallEdge<Field, Fact, Stmt, Method> incEdge) {








bidi solver



Johannes Lerch
committed
Mar 20, 2015




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



		WrappedFact<Field, Fact, Stmt, Method> calleeSourceFact = incEdge.getCalleeSourceFact();
		Key<Fact, Stmt> key = new Key<Fact, Stmt>(calleeSourceFact.getFact(), null);
		PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer = perSourceAnalyzer.getOrCreate(key);
		analyzer.addIncomingEdge(incEdge);		
	}

	@Override
	public void addInitialSeed(Stmt startPoint, Fact val) {
		Key<Fact, Stmt> key = new Key<Fact, Stmt>(val, startPoint);
		perSourceAnalyzer.getOrCreate(key).addInitialSeed(startPoint);
	}

	@Override
	public void addUnbalancedReturnFlow(final WrappedFactAtStatement<Field, Fact, Stmt, Method> target, final Stmt callSite) {
		synchronizer.synchronizeOnStmt(callSite, new Runnable() {
			@Override
			public void run() {
				Key<Fact, Stmt> key = new Key<Fact, Stmt>(context.zeroValue, callSite);
				perSourceAnalyzer.getOrCreate(key).scheduleUnbalancedReturnEdgeTo(target);				
			}
		});
	}
	
	public static interface Synchronizer<Stmt> {
		void synchronizeOnStmt(Stmt stmt, Runnable job);
	}
	
	private static class Key<Fact, Stmt> {
		private Fact fact;
		private Stmt stmt;

		private Key(Fact fact, Stmt stmt) {
			this.fact = fact;
			this.stmt = stmt;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((fact == null) ? 0 : fact.hashCode());
			result = prime * result + ((stmt == null) ? 0 : stmt.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Key other = (Key) obj;
			if (fact == null) {
				if (other.fact != null)
					return false;
			} else if (!fact.equals(other.fact))
				return false;
			if (stmt == null) {
				if (other.stmt != null)
					return false;
			} else if (!stmt.equals(other.stmt))
				return false;
			return true;
		}
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








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




11



package heros.alias;








bidi solver



Johannes Lerch
committed
Mar 20, 2015




12


13













removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




14



public class SourceStmtAnnotatedMethodAnalyzer<Field, Fact, Stmt, Method>








bidi solver



Johannes Lerch
committed
Mar 20, 2015




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



		implements MethodAnalyzer<Field, Fact, Stmt, Method> {

	private Method method;
	private CacheMap<Key<Fact, Stmt>, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>> perSourceAnalyzer = 
			new CacheMap<Key<Fact, Stmt>, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>>() {
		@Override
		protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> createItem(Key<Fact, Stmt> key) {
			return new PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>(method, key.fact, context);
		}
	};
	private Context<Field, Fact, Stmt, Method> context;
	private Synchronizer<Stmt> synchronizer;
	
	public SourceStmtAnnotatedMethodAnalyzer(Method method, Context<Field, Fact, Stmt, Method> context, Synchronizer<Stmt> synchronizer) {
		this.method = method;
		this.context = context;
		this.synchronizer = synchronizer;
	}
	
	@Override








refactoring

 


Johannes Lerch
committed
Apr 01, 2015




35



	public void addIncomingEdge(CallEdge<Field, Fact, Stmt, Method> incEdge) {








bidi solver



Johannes Lerch
committed
Mar 20, 2015




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



		WrappedFact<Field, Fact, Stmt, Method> calleeSourceFact = incEdge.getCalleeSourceFact();
		Key<Fact, Stmt> key = new Key<Fact, Stmt>(calleeSourceFact.getFact(), null);
		PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer = perSourceAnalyzer.getOrCreate(key);
		analyzer.addIncomingEdge(incEdge);		
	}

	@Override
	public void addInitialSeed(Stmt startPoint, Fact val) {
		Key<Fact, Stmt> key = new Key<Fact, Stmt>(val, startPoint);
		perSourceAnalyzer.getOrCreate(key).addInitialSeed(startPoint);
	}

	@Override
	public void addUnbalancedReturnFlow(final WrappedFactAtStatement<Field, Fact, Stmt, Method> target, final Stmt callSite) {
		synchronizer.synchronizeOnStmt(callSite, new Runnable() {
			@Override
			public void run() {
				Key<Fact, Stmt> key = new Key<Fact, Stmt>(context.zeroValue, callSite);
				perSourceAnalyzer.getOrCreate(key).scheduleUnbalancedReturnEdgeTo(target);				
			}
		});
	}
	
	public static interface Synchronizer<Stmt> {
		void synchronizeOnStmt(Stmt stmt, Runnable job);
	}
	
	private static class Key<Fact, Stmt> {
		private Fact fact;
		private Stmt stmt;

		private Key(Fact fact, Stmt stmt) {
			this.fact = fact;
			this.stmt = stmt;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((fact == null) ? 0 : fact.hashCode());
			result = prime * result + ((stmt == null) ? 0 : stmt.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Key other = (Key) obj;
			if (fact == null) {
				if (other.fact != null)
					return false;
			} else if (!fact.equals(other.fact))
				return false;
			if (stmt == null) {
				if (other.stmt != null)
					return false;
			} else if (!stmt.equals(other.stmt))
				return false;
			return true;
		}
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




refactoring

 


Johannes Lerch
committed
Apr 01, 2015




11



package heros.alias;






refactoring

 


Johannes Lerch
committed
Apr 01, 2015



refactoring

 

refactoring

Johannes Lerch
committed
Apr 01, 2015


11


package heros.alias;

package heros.alias;packageheros.alias;




bidi solver



Johannes Lerch
committed
Mar 20, 2015




12


13











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


13










removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015




14



public class SourceStmtAnnotatedMethodAnalyzer<Field, Fact, Stmt, Method>






removed unnecessary merge code in access path

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path

 

removed unnecessary merge code in access path

Johannes Lerch
committed
Mar 25, 2015


14


public class SourceStmtAnnotatedMethodAnalyzer<Field, Fact, Stmt, Method>

public class SourceStmtAnnotatedMethodAnalyzer<Field, Fact, Stmt, Method>publicclassSourceStmtAnnotatedMethodAnalyzer<Field,Fact,Stmt,Method>




bidi solver



Johannes Lerch
committed
Mar 20, 2015




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



		implements MethodAnalyzer<Field, Fact, Stmt, Method> {

	private Method method;
	private CacheMap<Key<Fact, Stmt>, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>> perSourceAnalyzer = 
			new CacheMap<Key<Fact, Stmt>, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>>() {
		@Override
		protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> createItem(Key<Fact, Stmt> key) {
			return new PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>(method, key.fact, context);
		}
	};
	private Context<Field, Fact, Stmt, Method> context;
	private Synchronizer<Stmt> synchronizer;
	
	public SourceStmtAnnotatedMethodAnalyzer(Method method, Context<Field, Fact, Stmt, Method> context, Synchronizer<Stmt> synchronizer) {
		this.method = method;
		this.context = context;
		this.synchronizer = synchronizer;
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


		implements MethodAnalyzer<Field, Fact, Stmt, Method> {

	private Method method;
	private CacheMap<Key<Fact, Stmt>, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>> perSourceAnalyzer = 
			new CacheMap<Key<Fact, Stmt>, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>>() {
		@Override
		protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> createItem(Key<Fact, Stmt> key) {
			return new PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>(method, key.fact, context);
		}
	};
	private Context<Field, Fact, Stmt, Method> context;
	private Synchronizer<Stmt> synchronizer;
	
	public SourceStmtAnnotatedMethodAnalyzer(Method method, Context<Field, Fact, Stmt, Method> context, Synchronizer<Stmt> synchronizer) {
		this.method = method;
		this.context = context;
		this.synchronizer = synchronizer;
	}
	
	@Override

		implements MethodAnalyzer<Field, Fact, Stmt, Method> {implementsMethodAnalyzer<Field,Fact,Stmt,Method>{	private Method method;privateMethodmethod;	private CacheMap<Key<Fact, Stmt>, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>> perSourceAnalyzer = privateCacheMap<Key<Fact,Stmt>,PerAccessPathMethodAnalyzer<Field,Fact,Stmt,Method>>perSourceAnalyzer=			new CacheMap<Key<Fact, Stmt>, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>>() {newCacheMap<Key<Fact,Stmt>,PerAccessPathMethodAnalyzer<Field,Fact,Stmt,Method>>(){		@Override@Override		protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> createItem(Key<Fact, Stmt> key) {protectedPerAccessPathMethodAnalyzer<Field,Fact,Stmt,Method>createItem(Key<Fact,Stmt>key){			return new PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>(method, key.fact, context);returnnewPerAccessPathMethodAnalyzer<Field,Fact,Stmt,Method>(method,key.fact,context);		}}	};};	private Context<Field, Fact, Stmt, Method> context;privateContext<Field,Fact,Stmt,Method>context;	private Synchronizer<Stmt> synchronizer;privateSynchronizer<Stmt>synchronizer;		public SourceStmtAnnotatedMethodAnalyzer(Method method, Context<Field, Fact, Stmt, Method> context, Synchronizer<Stmt> synchronizer) {publicSourceStmtAnnotatedMethodAnalyzer(Methodmethod,Context<Field,Fact,Stmt,Method>context,Synchronizer<Stmt>synchronizer){		this.method = method;this.method=method;		this.context = context;this.context=context;		this.synchronizer = synchronizer;this.synchronizer=synchronizer;	}}		@Override@Override




refactoring

 


Johannes Lerch
committed
Apr 01, 2015




35



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


35


	public void addIncomingEdge(CallEdge<Field, Fact, Stmt, Method> incEdge) {

	public void addIncomingEdge(CallEdge<Field, Fact, Stmt, Method> incEdge) {publicvoidaddIncomingEdge(CallEdge<Field,Fact,Stmt,Method>incEdge){




bidi solver



Johannes Lerch
committed
Mar 20, 2015




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



		WrappedFact<Field, Fact, Stmt, Method> calleeSourceFact = incEdge.getCalleeSourceFact();
		Key<Fact, Stmt> key = new Key<Fact, Stmt>(calleeSourceFact.getFact(), null);
		PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer = perSourceAnalyzer.getOrCreate(key);
		analyzer.addIncomingEdge(incEdge);		
	}

	@Override
	public void addInitialSeed(Stmt startPoint, Fact val) {
		Key<Fact, Stmt> key = new Key<Fact, Stmt>(val, startPoint);
		perSourceAnalyzer.getOrCreate(key).addInitialSeed(startPoint);
	}

	@Override
	public void addUnbalancedReturnFlow(final WrappedFactAtStatement<Field, Fact, Stmt, Method> target, final Stmt callSite) {
		synchronizer.synchronizeOnStmt(callSite, new Runnable() {
			@Override
			public void run() {
				Key<Fact, Stmt> key = new Key<Fact, Stmt>(context.zeroValue, callSite);
				perSourceAnalyzer.getOrCreate(key).scheduleUnbalancedReturnEdgeTo(target);				
			}
		});
	}
	
	public static interface Synchronizer<Stmt> {
		void synchronizeOnStmt(Stmt stmt, Runnable job);
	}
	
	private static class Key<Fact, Stmt> {
		private Fact fact;
		private Stmt stmt;

		private Key(Fact fact, Stmt stmt) {
			this.fact = fact;
			this.stmt = stmt;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((fact == null) ? 0 : fact.hashCode());
			result = prime * result + ((stmt == null) ? 0 : stmt.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Key other = (Key) obj;
			if (fact == null) {
				if (other.fact != null)
					return false;
			} else if (!fact.equals(other.fact))
				return false;
			if (stmt == null) {
				if (other.stmt != null)
					return false;
			} else if (!stmt.equals(other.stmt))
				return false;
			return true;
		}
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


		WrappedFact<Field, Fact, Stmt, Method> calleeSourceFact = incEdge.getCalleeSourceFact();
		Key<Fact, Stmt> key = new Key<Fact, Stmt>(calleeSourceFact.getFact(), null);
		PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer = perSourceAnalyzer.getOrCreate(key);
		analyzer.addIncomingEdge(incEdge);		
	}

	@Override
	public void addInitialSeed(Stmt startPoint, Fact val) {
		Key<Fact, Stmt> key = new Key<Fact, Stmt>(val, startPoint);
		perSourceAnalyzer.getOrCreate(key).addInitialSeed(startPoint);
	}

	@Override
	public void addUnbalancedReturnFlow(final WrappedFactAtStatement<Field, Fact, Stmt, Method> target, final Stmt callSite) {
		synchronizer.synchronizeOnStmt(callSite, new Runnable() {
			@Override
			public void run() {
				Key<Fact, Stmt> key = new Key<Fact, Stmt>(context.zeroValue, callSite);
				perSourceAnalyzer.getOrCreate(key).scheduleUnbalancedReturnEdgeTo(target);				
			}
		});
	}
	
	public static interface Synchronizer<Stmt> {
		void synchronizeOnStmt(Stmt stmt, Runnable job);
	}
	
	private static class Key<Fact, Stmt> {
		private Fact fact;
		private Stmt stmt;

		private Key(Fact fact, Stmt stmt) {
			this.fact = fact;
			this.stmt = stmt;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((fact == null) ? 0 : fact.hashCode());
			result = prime * result + ((stmt == null) ? 0 : stmt.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Key other = (Key) obj;
			if (fact == null) {
				if (other.fact != null)
					return false;
			} else if (!fact.equals(other.fact))
				return false;
			if (stmt == null) {
				if (other.stmt != null)
					return false;
			} else if (!stmt.equals(other.stmt))
				return false;
			return true;
		}
	}
}
		WrappedFact<Field, Fact, Stmt, Method> calleeSourceFact = incEdge.getCalleeSourceFact();WrappedFact<Field,Fact,Stmt,Method>calleeSourceFact=incEdge.getCalleeSourceFact();		Key<Fact, Stmt> key = new Key<Fact, Stmt>(calleeSourceFact.getFact(), null);Key<Fact,Stmt>key=newKey<Fact,Stmt>(calleeSourceFact.getFact(),null);		PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer = perSourceAnalyzer.getOrCreate(key);PerAccessPathMethodAnalyzer<Field,Fact,Stmt,Method>analyzer=perSourceAnalyzer.getOrCreate(key);		analyzer.addIncomingEdge(incEdge);		analyzer.addIncomingEdge(incEdge);	}}	@Override@Override	public void addInitialSeed(Stmt startPoint, Fact val) {publicvoidaddInitialSeed(StmtstartPoint,Factval){		Key<Fact, Stmt> key = new Key<Fact, Stmt>(val, startPoint);Key<Fact,Stmt>key=newKey<Fact,Stmt>(val,startPoint);		perSourceAnalyzer.getOrCreate(key).addInitialSeed(startPoint);perSourceAnalyzer.getOrCreate(key).addInitialSeed(startPoint);	}}	@Override@Override	public void addUnbalancedReturnFlow(final WrappedFactAtStatement<Field, Fact, Stmt, Method> target, final Stmt callSite) {publicvoidaddUnbalancedReturnFlow(finalWrappedFactAtStatement<Field,Fact,Stmt,Method>target,finalStmtcallSite){		synchronizer.synchronizeOnStmt(callSite, new Runnable() {synchronizer.synchronizeOnStmt(callSite,newRunnable(){			@Override@Override			public void run() {publicvoidrun(){				Key<Fact, Stmt> key = new Key<Fact, Stmt>(context.zeroValue, callSite);Key<Fact,Stmt>key=newKey<Fact,Stmt>(context.zeroValue,callSite);				perSourceAnalyzer.getOrCreate(key).scheduleUnbalancedReturnEdgeTo(target);				perSourceAnalyzer.getOrCreate(key).scheduleUnbalancedReturnEdgeTo(target);			}}		});});	}}		public static interface Synchronizer<Stmt> {publicstaticinterfaceSynchronizer<Stmt>{		void synchronizeOnStmt(Stmt stmt, Runnable job);voidsynchronizeOnStmt(Stmtstmt,Runnablejob);	}}		private static class Key<Fact, Stmt> {privatestaticclassKey<Fact,Stmt>{		private Fact fact;privateFactfact;		private Stmt stmt;privateStmtstmt;		private Key(Fact fact, Stmt stmt) {privateKey(Factfact,Stmtstmt){			this.fact = fact;this.fact=fact;			this.stmt = stmt;this.stmt=stmt;		}}		@Override@Override		public int hashCode() {publicinthashCode(){			final int prime = 31;finalintprime=31;			int result = 1;intresult=1;			result = prime * result + ((fact == null) ? 0 : fact.hashCode());result=prime*result+((fact==null)?0:fact.hashCode());			result = prime * result + ((stmt == null) ? 0 : stmt.hashCode());result=prime*result+((stmt==null)?0:stmt.hashCode());			return result;returnresult;		}}		@Override@Override		public boolean equals(Object obj) {publicbooleanequals(Objectobj){			if (this == obj)if(this==obj)				return true;returntrue;			if (obj == null)if(obj==null)				return false;returnfalse;			if (getClass() != obj.getClass())if(getClass()!=obj.getClass())				return false;returnfalse;			Key other = (Key) obj;Keyother=(Key)obj;			if (fact == null) {if(fact==null){				if (other.fact != null)if(other.fact!=null)					return false;returnfalse;			} else if (!fact.equals(other.fact))}elseif(!fact.equals(other.fact))				return false;returnfalse;			if (stmt == null) {if(stmt==null){				if (other.stmt != null)if(other.stmt!=null)					return false;returnfalse;			} else if (!stmt.equals(other.stmt))}elseif(!stmt.equals(other.stmt))				return false;returnfalse;			return true;returntrue;		}}	}}}}