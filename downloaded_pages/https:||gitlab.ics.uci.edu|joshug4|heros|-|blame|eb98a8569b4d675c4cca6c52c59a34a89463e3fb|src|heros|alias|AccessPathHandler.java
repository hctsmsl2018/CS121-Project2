



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

eb98a8569b4d675c4cca6c52c59a34a89463e3fb

















eb98a8569b4d675c4cca6c52c59a34a89463e3fb


Switch branch/tag










heros


src


heros


alias


AccessPathHandler.java



Find file
Normal viewHistoryPermalink






AccessPathHandler.java



3.85 KB









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




13




14




15




16




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

import heros.alias.FlowFunction.ConstrainedFact;
import heros.alias.FlowFunction.ReadFieldConstraint;
import heros.alias.FlowFunction.WriteFieldConstraint;










removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






17




public class AccessPathHandler<Field, Fact, Stmt, Method> {









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






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





	private AccessPath<Field> accessPath;
	private Resolver<Field, Fact, Stmt, Method> resolver;

	public AccessPathHandler(AccessPath<Field> accessPath, Resolver<Field, Fact, Stmt, Method> resolver) {
		this.accessPath = accessPath;
		this.resolver = resolver;
	}

	public boolean canRead(Field field) {
		return accessPath.canRead(field);
	}
	
	public boolean mayCanRead(Field field) {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






32




		return accessPath.canRead(field) || (accessPath.hasEmptyAccessPath() && !accessPath.isAccessInExclusions(field));









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






33




34




35




	}
	
	public boolean mayBeEmpty() {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






36




		return accessPath.hasEmptyAccessPath();









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






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




	}

	public ConstrainedFact<Field, Fact, Stmt, Method> generate(Fact fact) {
		return new ConstrainedFact<>(new WrappedFact<Field, Fact, Stmt, Method>(fact, accessPath, resolver));
	}
	
	public ConstrainedFact<Field, Fact, Stmt, Method> generateWithEmptyAccessPath(Fact fact, ZeroHandler<Field> zeroHandler) {
		return new ConstrainedFact<>(new WrappedFact<>(fact, new AccessPath<Field>(), new ZeroCallEdgeResolver<>(resolver.analyzer, zeroHandler)));
	}
	
	public ResultBuilder<Field, Fact, Stmt, Method> prepend(final Field field) {
		return new ResultBuilder<Field, Fact, Stmt, Method>() {
			@Override
			public ConstrainedFact<Field, Fact, Stmt, Method> generate(Fact fact) {
				return new ConstrainedFact<>(new WrappedFact<>(fact, accessPath.prepend(field), resolver));
			}
		};
	}
	
	public ResultBuilder<Field, Fact, Stmt, Method> read(final Field field) {
		if(mayCanRead(field)) {
			return new ResultBuilder<Field, Fact, Stmt, Method>() {
				@Override
				public ConstrainedFact<Field, Fact, Stmt, Method> generate(Fact fact) {
					if(canRead(field))









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






62




						return new ConstrainedFact<>(new WrappedFact<>(fact, accessPath.removeFirst(), resolver));









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






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




					else
						return new ConstrainedFact<>(new WrappedFact<>(fact, new AccessPath<Field>(), resolver), new ReadFieldConstraint<>(field));
				}
			};
		}
		else
			throw new IllegalArgumentException("Cannot read field "+field);
	}
	
	public ResultBuilder<Field, Fact, Stmt, Method> overwrite(final Field field) {









bugfix


 

 


Johannes Lerch
committed
Mar 26, 2015






73




		if(mayBeEmpty())









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






74




75




76




77




			return new ResultBuilder<Field, Fact, Stmt, Method>() {
				@Override
				public ConstrainedFact<Field, Fact, Stmt, Method> generate(Fact fact) {
					if(accessPath.canRead(field)) {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






78




						AccessPath<Field> tempAccPath = accessPath.removeFirst();









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






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




						if(tempAccPath.hasEmptyAccessPath())
							return new ConstrainedFact<>(new WrappedFact<>(fact, tempAccPath.appendExcludedFieldReference(field), resolver));
						else
							return new ConstrainedFact<>(new WrappedFact<>(fact, tempAccPath, resolver));
					} else if(accessPath.isAccessInExclusions(field))
						return new ConstrainedFact<>(new WrappedFact<>(fact, accessPath, resolver));
					else
						return new ConstrainedFact<>(new WrappedFact<>(fact, accessPath.appendExcludedFieldReference(field), resolver), new WriteFieldConstraint<>(field));
				}
			};
		else
			throw new IllegalArgumentException("Cannot write field "+field);
	}
	









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






93




	public static interface ResultBuilder<FieldRef, FactAbstraction, Stmt, Method> {









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






94




95




96




97




		public ConstrainedFact<FieldRef, FactAbstraction, Stmt, Method> generate(FactAbstraction fact);
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

eb98a8569b4d675c4cca6c52c59a34a89463e3fb

















eb98a8569b4d675c4cca6c52c59a34a89463e3fb


Switch branch/tag










heros


src


heros


alias


AccessPathHandler.java



Find file
Normal viewHistoryPermalink






AccessPathHandler.java



3.85 KB









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




13




14




15




16




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

import heros.alias.FlowFunction.ConstrainedFact;
import heros.alias.FlowFunction.ReadFieldConstraint;
import heros.alias.FlowFunction.WriteFieldConstraint;










removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






17




public class AccessPathHandler<Field, Fact, Stmt, Method> {









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






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





	private AccessPath<Field> accessPath;
	private Resolver<Field, Fact, Stmt, Method> resolver;

	public AccessPathHandler(AccessPath<Field> accessPath, Resolver<Field, Fact, Stmt, Method> resolver) {
		this.accessPath = accessPath;
		this.resolver = resolver;
	}

	public boolean canRead(Field field) {
		return accessPath.canRead(field);
	}
	
	public boolean mayCanRead(Field field) {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






32




		return accessPath.canRead(field) || (accessPath.hasEmptyAccessPath() && !accessPath.isAccessInExclusions(field));









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






33




34




35




	}
	
	public boolean mayBeEmpty() {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






36




		return accessPath.hasEmptyAccessPath();









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






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




	}

	public ConstrainedFact<Field, Fact, Stmt, Method> generate(Fact fact) {
		return new ConstrainedFact<>(new WrappedFact<Field, Fact, Stmt, Method>(fact, accessPath, resolver));
	}
	
	public ConstrainedFact<Field, Fact, Stmt, Method> generateWithEmptyAccessPath(Fact fact, ZeroHandler<Field> zeroHandler) {
		return new ConstrainedFact<>(new WrappedFact<>(fact, new AccessPath<Field>(), new ZeroCallEdgeResolver<>(resolver.analyzer, zeroHandler)));
	}
	
	public ResultBuilder<Field, Fact, Stmt, Method> prepend(final Field field) {
		return new ResultBuilder<Field, Fact, Stmt, Method>() {
			@Override
			public ConstrainedFact<Field, Fact, Stmt, Method> generate(Fact fact) {
				return new ConstrainedFact<>(new WrappedFact<>(fact, accessPath.prepend(field), resolver));
			}
		};
	}
	
	public ResultBuilder<Field, Fact, Stmt, Method> read(final Field field) {
		if(mayCanRead(field)) {
			return new ResultBuilder<Field, Fact, Stmt, Method>() {
				@Override
				public ConstrainedFact<Field, Fact, Stmt, Method> generate(Fact fact) {
					if(canRead(field))









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






62




						return new ConstrainedFact<>(new WrappedFact<>(fact, accessPath.removeFirst(), resolver));









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






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




					else
						return new ConstrainedFact<>(new WrappedFact<>(fact, new AccessPath<Field>(), resolver), new ReadFieldConstraint<>(field));
				}
			};
		}
		else
			throw new IllegalArgumentException("Cannot read field "+field);
	}
	
	public ResultBuilder<Field, Fact, Stmt, Method> overwrite(final Field field) {









bugfix


 

 


Johannes Lerch
committed
Mar 26, 2015






73




		if(mayBeEmpty())









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






74




75




76




77




			return new ResultBuilder<Field, Fact, Stmt, Method>() {
				@Override
				public ConstrainedFact<Field, Fact, Stmt, Method> generate(Fact fact) {
					if(accessPath.canRead(field)) {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






78




						AccessPath<Field> tempAccPath = accessPath.removeFirst();









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






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




						if(tempAccPath.hasEmptyAccessPath())
							return new ConstrainedFact<>(new WrappedFact<>(fact, tempAccPath.appendExcludedFieldReference(field), resolver));
						else
							return new ConstrainedFact<>(new WrappedFact<>(fact, tempAccPath, resolver));
					} else if(accessPath.isAccessInExclusions(field))
						return new ConstrainedFact<>(new WrappedFact<>(fact, accessPath, resolver));
					else
						return new ConstrainedFact<>(new WrappedFact<>(fact, accessPath.appendExcludedFieldReference(field), resolver), new WriteFieldConstraint<>(field));
				}
			};
		else
			throw new IllegalArgumentException("Cannot write field "+field);
	}
	









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






93




	public static interface ResultBuilder<FieldRef, FactAbstraction, Stmt, Method> {









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






94




95




96




97




		public ConstrainedFact<FieldRef, FactAbstraction, Stmt, Method> generate(FactAbstraction fact);
	}

}











Open sidebar



Joshua Garcia heros

eb98a8569b4d675c4cca6c52c59a34a89463e3fb







Open sidebar



Joshua Garcia heros

eb98a8569b4d675c4cca6c52c59a34a89463e3fb




Open sidebar

Joshua Garcia heros

eb98a8569b4d675c4cca6c52c59a34a89463e3fb


Joshua Garciaherosheros
eb98a8569b4d675c4cca6c52c59a34a89463e3fb










eb98a8569b4d675c4cca6c52c59a34a89463e3fb


Switch branch/tag










heros


src


heros


alias


AccessPathHandler.java



Find file
Normal viewHistoryPermalink






AccessPathHandler.java



3.85 KB









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




13




14




15




16




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

import heros.alias.FlowFunction.ConstrainedFact;
import heros.alias.FlowFunction.ReadFieldConstraint;
import heros.alias.FlowFunction.WriteFieldConstraint;










removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






17




public class AccessPathHandler<Field, Fact, Stmt, Method> {









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






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





	private AccessPath<Field> accessPath;
	private Resolver<Field, Fact, Stmt, Method> resolver;

	public AccessPathHandler(AccessPath<Field> accessPath, Resolver<Field, Fact, Stmt, Method> resolver) {
		this.accessPath = accessPath;
		this.resolver = resolver;
	}

	public boolean canRead(Field field) {
		return accessPath.canRead(field);
	}
	
	public boolean mayCanRead(Field field) {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






32




		return accessPath.canRead(field) || (accessPath.hasEmptyAccessPath() && !accessPath.isAccessInExclusions(field));









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






33




34




35




	}
	
	public boolean mayBeEmpty() {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






36




		return accessPath.hasEmptyAccessPath();









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






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




	}

	public ConstrainedFact<Field, Fact, Stmt, Method> generate(Fact fact) {
		return new ConstrainedFact<>(new WrappedFact<Field, Fact, Stmt, Method>(fact, accessPath, resolver));
	}
	
	public ConstrainedFact<Field, Fact, Stmt, Method> generateWithEmptyAccessPath(Fact fact, ZeroHandler<Field> zeroHandler) {
		return new ConstrainedFact<>(new WrappedFact<>(fact, new AccessPath<Field>(), new ZeroCallEdgeResolver<>(resolver.analyzer, zeroHandler)));
	}
	
	public ResultBuilder<Field, Fact, Stmt, Method> prepend(final Field field) {
		return new ResultBuilder<Field, Fact, Stmt, Method>() {
			@Override
			public ConstrainedFact<Field, Fact, Stmt, Method> generate(Fact fact) {
				return new ConstrainedFact<>(new WrappedFact<>(fact, accessPath.prepend(field), resolver));
			}
		};
	}
	
	public ResultBuilder<Field, Fact, Stmt, Method> read(final Field field) {
		if(mayCanRead(field)) {
			return new ResultBuilder<Field, Fact, Stmt, Method>() {
				@Override
				public ConstrainedFact<Field, Fact, Stmt, Method> generate(Fact fact) {
					if(canRead(field))









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






62




						return new ConstrainedFact<>(new WrappedFact<>(fact, accessPath.removeFirst(), resolver));









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






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




					else
						return new ConstrainedFact<>(new WrappedFact<>(fact, new AccessPath<Field>(), resolver), new ReadFieldConstraint<>(field));
				}
			};
		}
		else
			throw new IllegalArgumentException("Cannot read field "+field);
	}
	
	public ResultBuilder<Field, Fact, Stmt, Method> overwrite(final Field field) {









bugfix


 

 


Johannes Lerch
committed
Mar 26, 2015






73




		if(mayBeEmpty())









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






74




75




76




77




			return new ResultBuilder<Field, Fact, Stmt, Method>() {
				@Override
				public ConstrainedFact<Field, Fact, Stmt, Method> generate(Fact fact) {
					if(accessPath.canRead(field)) {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






78




						AccessPath<Field> tempAccPath = accessPath.removeFirst();









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






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




						if(tempAccPath.hasEmptyAccessPath())
							return new ConstrainedFact<>(new WrappedFact<>(fact, tempAccPath.appendExcludedFieldReference(field), resolver));
						else
							return new ConstrainedFact<>(new WrappedFact<>(fact, tempAccPath, resolver));
					} else if(accessPath.isAccessInExclusions(field))
						return new ConstrainedFact<>(new WrappedFact<>(fact, accessPath, resolver));
					else
						return new ConstrainedFact<>(new WrappedFact<>(fact, accessPath.appendExcludedFieldReference(field), resolver), new WriteFieldConstraint<>(field));
				}
			};
		else
			throw new IllegalArgumentException("Cannot write field "+field);
	}
	









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






93




	public static interface ResultBuilder<FieldRef, FactAbstraction, Stmt, Method> {









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






94




95




96




97




		public ConstrainedFact<FieldRef, FactAbstraction, Stmt, Method> generate(FactAbstraction fact);
	}

}














eb98a8569b4d675c4cca6c52c59a34a89463e3fb


Switch branch/tag










heros


src


heros


alias


AccessPathHandler.java



Find file
Normal viewHistoryPermalink






AccessPathHandler.java



3.85 KB









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




13




14




15




16




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

import heros.alias.FlowFunction.ConstrainedFact;
import heros.alias.FlowFunction.ReadFieldConstraint;
import heros.alias.FlowFunction.WriteFieldConstraint;










removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






17




public class AccessPathHandler<Field, Fact, Stmt, Method> {









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






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





	private AccessPath<Field> accessPath;
	private Resolver<Field, Fact, Stmt, Method> resolver;

	public AccessPathHandler(AccessPath<Field> accessPath, Resolver<Field, Fact, Stmt, Method> resolver) {
		this.accessPath = accessPath;
		this.resolver = resolver;
	}

	public boolean canRead(Field field) {
		return accessPath.canRead(field);
	}
	
	public boolean mayCanRead(Field field) {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






32




		return accessPath.canRead(field) || (accessPath.hasEmptyAccessPath() && !accessPath.isAccessInExclusions(field));









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






33




34




35




	}
	
	public boolean mayBeEmpty() {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






36




		return accessPath.hasEmptyAccessPath();









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






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




	}

	public ConstrainedFact<Field, Fact, Stmt, Method> generate(Fact fact) {
		return new ConstrainedFact<>(new WrappedFact<Field, Fact, Stmt, Method>(fact, accessPath, resolver));
	}
	
	public ConstrainedFact<Field, Fact, Stmt, Method> generateWithEmptyAccessPath(Fact fact, ZeroHandler<Field> zeroHandler) {
		return new ConstrainedFact<>(new WrappedFact<>(fact, new AccessPath<Field>(), new ZeroCallEdgeResolver<>(resolver.analyzer, zeroHandler)));
	}
	
	public ResultBuilder<Field, Fact, Stmt, Method> prepend(final Field field) {
		return new ResultBuilder<Field, Fact, Stmt, Method>() {
			@Override
			public ConstrainedFact<Field, Fact, Stmt, Method> generate(Fact fact) {
				return new ConstrainedFact<>(new WrappedFact<>(fact, accessPath.prepend(field), resolver));
			}
		};
	}
	
	public ResultBuilder<Field, Fact, Stmt, Method> read(final Field field) {
		if(mayCanRead(field)) {
			return new ResultBuilder<Field, Fact, Stmt, Method>() {
				@Override
				public ConstrainedFact<Field, Fact, Stmt, Method> generate(Fact fact) {
					if(canRead(field))









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






62




						return new ConstrainedFact<>(new WrappedFact<>(fact, accessPath.removeFirst(), resolver));









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






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




					else
						return new ConstrainedFact<>(new WrappedFact<>(fact, new AccessPath<Field>(), resolver), new ReadFieldConstraint<>(field));
				}
			};
		}
		else
			throw new IllegalArgumentException("Cannot read field "+field);
	}
	
	public ResultBuilder<Field, Fact, Stmt, Method> overwrite(final Field field) {









bugfix


 

 


Johannes Lerch
committed
Mar 26, 2015






73




		if(mayBeEmpty())









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






74




75




76




77




			return new ResultBuilder<Field, Fact, Stmt, Method>() {
				@Override
				public ConstrainedFact<Field, Fact, Stmt, Method> generate(Fact fact) {
					if(accessPath.canRead(field)) {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






78




						AccessPath<Field> tempAccPath = accessPath.removeFirst();









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






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




						if(tempAccPath.hasEmptyAccessPath())
							return new ConstrainedFact<>(new WrappedFact<>(fact, tempAccPath.appendExcludedFieldReference(field), resolver));
						else
							return new ConstrainedFact<>(new WrappedFact<>(fact, tempAccPath, resolver));
					} else if(accessPath.isAccessInExclusions(field))
						return new ConstrainedFact<>(new WrappedFact<>(fact, accessPath, resolver));
					else
						return new ConstrainedFact<>(new WrappedFact<>(fact, accessPath.appendExcludedFieldReference(field), resolver), new WriteFieldConstraint<>(field));
				}
			};
		else
			throw new IllegalArgumentException("Cannot write field "+field);
	}
	









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






93




	public static interface ResultBuilder<FieldRef, FactAbstraction, Stmt, Method> {









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






94




95




96




97




		public ConstrainedFact<FieldRef, FactAbstraction, Stmt, Method> generate(FactAbstraction fact);
	}

}










eb98a8569b4d675c4cca6c52c59a34a89463e3fb


Switch branch/tag










heros


src


heros


alias


AccessPathHandler.java



Find file
Normal viewHistoryPermalink




eb98a8569b4d675c4cca6c52c59a34a89463e3fb


Switch branch/tag










heros


src


heros


alias


AccessPathHandler.java





eb98a8569b4d675c4cca6c52c59a34a89463e3fb


Switch branch/tag








eb98a8569b4d675c4cca6c52c59a34a89463e3fb


Switch branch/tag





eb98a8569b4d675c4cca6c52c59a34a89463e3fb

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src

heros

alias

AccessPathHandler.java
Find file
Normal viewHistoryPermalink




AccessPathHandler.java



3.85 KB









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




13




14




15




16




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

import heros.alias.FlowFunction.ConstrainedFact;
import heros.alias.FlowFunction.ReadFieldConstraint;
import heros.alias.FlowFunction.WriteFieldConstraint;










removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






17




public class AccessPathHandler<Field, Fact, Stmt, Method> {









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






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





	private AccessPath<Field> accessPath;
	private Resolver<Field, Fact, Stmt, Method> resolver;

	public AccessPathHandler(AccessPath<Field> accessPath, Resolver<Field, Fact, Stmt, Method> resolver) {
		this.accessPath = accessPath;
		this.resolver = resolver;
	}

	public boolean canRead(Field field) {
		return accessPath.canRead(field);
	}
	
	public boolean mayCanRead(Field field) {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






32




		return accessPath.canRead(field) || (accessPath.hasEmptyAccessPath() && !accessPath.isAccessInExclusions(field));









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






33




34




35




	}
	
	public boolean mayBeEmpty() {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






36




		return accessPath.hasEmptyAccessPath();









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






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




	}

	public ConstrainedFact<Field, Fact, Stmt, Method> generate(Fact fact) {
		return new ConstrainedFact<>(new WrappedFact<Field, Fact, Stmt, Method>(fact, accessPath, resolver));
	}
	
	public ConstrainedFact<Field, Fact, Stmt, Method> generateWithEmptyAccessPath(Fact fact, ZeroHandler<Field> zeroHandler) {
		return new ConstrainedFact<>(new WrappedFact<>(fact, new AccessPath<Field>(), new ZeroCallEdgeResolver<>(resolver.analyzer, zeroHandler)));
	}
	
	public ResultBuilder<Field, Fact, Stmt, Method> prepend(final Field field) {
		return new ResultBuilder<Field, Fact, Stmt, Method>() {
			@Override
			public ConstrainedFact<Field, Fact, Stmt, Method> generate(Fact fact) {
				return new ConstrainedFact<>(new WrappedFact<>(fact, accessPath.prepend(field), resolver));
			}
		};
	}
	
	public ResultBuilder<Field, Fact, Stmt, Method> read(final Field field) {
		if(mayCanRead(field)) {
			return new ResultBuilder<Field, Fact, Stmt, Method>() {
				@Override
				public ConstrainedFact<Field, Fact, Stmt, Method> generate(Fact fact) {
					if(canRead(field))









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






62




						return new ConstrainedFact<>(new WrappedFact<>(fact, accessPath.removeFirst(), resolver));









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






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




					else
						return new ConstrainedFact<>(new WrappedFact<>(fact, new AccessPath<Field>(), resolver), new ReadFieldConstraint<>(field));
				}
			};
		}
		else
			throw new IllegalArgumentException("Cannot read field "+field);
	}
	
	public ResultBuilder<Field, Fact, Stmt, Method> overwrite(final Field field) {









bugfix


 

 


Johannes Lerch
committed
Mar 26, 2015






73




		if(mayBeEmpty())









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






74




75




76




77




			return new ResultBuilder<Field, Fact, Stmt, Method>() {
				@Override
				public ConstrainedFact<Field, Fact, Stmt, Method> generate(Fact fact) {
					if(accessPath.canRead(field)) {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






78




						AccessPath<Field> tempAccPath = accessPath.removeFirst();









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






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




						if(tempAccPath.hasEmptyAccessPath())
							return new ConstrainedFact<>(new WrappedFact<>(fact, tempAccPath.appendExcludedFieldReference(field), resolver));
						else
							return new ConstrainedFact<>(new WrappedFact<>(fact, tempAccPath, resolver));
					} else if(accessPath.isAccessInExclusions(field))
						return new ConstrainedFact<>(new WrappedFact<>(fact, accessPath, resolver));
					else
						return new ConstrainedFact<>(new WrappedFact<>(fact, accessPath.appendExcludedFieldReference(field), resolver), new WriteFieldConstraint<>(field));
				}
			};
		else
			throw new IllegalArgumentException("Cannot write field "+field);
	}
	









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






93




	public static interface ResultBuilder<FieldRef, FactAbstraction, Stmt, Method> {









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






94




95




96




97




		public ConstrainedFact<FieldRef, FactAbstraction, Stmt, Method> generate(FactAbstraction fact);
	}

}








AccessPathHandler.java



3.85 KB










AccessPathHandler.java



3.85 KB









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




13




14




15




16




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

import heros.alias.FlowFunction.ConstrainedFact;
import heros.alias.FlowFunction.ReadFieldConstraint;
import heros.alias.FlowFunction.WriteFieldConstraint;










removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






17




public class AccessPathHandler<Field, Fact, Stmt, Method> {









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






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





	private AccessPath<Field> accessPath;
	private Resolver<Field, Fact, Stmt, Method> resolver;

	public AccessPathHandler(AccessPath<Field> accessPath, Resolver<Field, Fact, Stmt, Method> resolver) {
		this.accessPath = accessPath;
		this.resolver = resolver;
	}

	public boolean canRead(Field field) {
		return accessPath.canRead(field);
	}
	
	public boolean mayCanRead(Field field) {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






32




		return accessPath.canRead(field) || (accessPath.hasEmptyAccessPath() && !accessPath.isAccessInExclusions(field));









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






33




34




35




	}
	
	public boolean mayBeEmpty() {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






36




		return accessPath.hasEmptyAccessPath();









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






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




	}

	public ConstrainedFact<Field, Fact, Stmt, Method> generate(Fact fact) {
		return new ConstrainedFact<>(new WrappedFact<Field, Fact, Stmt, Method>(fact, accessPath, resolver));
	}
	
	public ConstrainedFact<Field, Fact, Stmt, Method> generateWithEmptyAccessPath(Fact fact, ZeroHandler<Field> zeroHandler) {
		return new ConstrainedFact<>(new WrappedFact<>(fact, new AccessPath<Field>(), new ZeroCallEdgeResolver<>(resolver.analyzer, zeroHandler)));
	}
	
	public ResultBuilder<Field, Fact, Stmt, Method> prepend(final Field field) {
		return new ResultBuilder<Field, Fact, Stmt, Method>() {
			@Override
			public ConstrainedFact<Field, Fact, Stmt, Method> generate(Fact fact) {
				return new ConstrainedFact<>(new WrappedFact<>(fact, accessPath.prepend(field), resolver));
			}
		};
	}
	
	public ResultBuilder<Field, Fact, Stmt, Method> read(final Field field) {
		if(mayCanRead(field)) {
			return new ResultBuilder<Field, Fact, Stmt, Method>() {
				@Override
				public ConstrainedFact<Field, Fact, Stmt, Method> generate(Fact fact) {
					if(canRead(field))









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






62




						return new ConstrainedFact<>(new WrappedFact<>(fact, accessPath.removeFirst(), resolver));









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






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




					else
						return new ConstrainedFact<>(new WrappedFact<>(fact, new AccessPath<Field>(), resolver), new ReadFieldConstraint<>(field));
				}
			};
		}
		else
			throw new IllegalArgumentException("Cannot read field "+field);
	}
	
	public ResultBuilder<Field, Fact, Stmt, Method> overwrite(final Field field) {









bugfix


 

 


Johannes Lerch
committed
Mar 26, 2015






73




		if(mayBeEmpty())









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






74




75




76




77




			return new ResultBuilder<Field, Fact, Stmt, Method>() {
				@Override
				public ConstrainedFact<Field, Fact, Stmt, Method> generate(Fact fact) {
					if(accessPath.canRead(field)) {









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






78




						AccessPath<Field> tempAccPath = accessPath.removeFirst();









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






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




						if(tempAccPath.hasEmptyAccessPath())
							return new ConstrainedFact<>(new WrappedFact<>(fact, tempAccPath.appendExcludedFieldReference(field), resolver));
						else
							return new ConstrainedFact<>(new WrappedFact<>(fact, tempAccPath, resolver));
					} else if(accessPath.isAccessInExclusions(field))
						return new ConstrainedFact<>(new WrappedFact<>(fact, accessPath, resolver));
					else
						return new ConstrainedFact<>(new WrappedFact<>(fact, accessPath.appendExcludedFieldReference(field), resolver), new WriteFieldConstraint<>(field));
				}
			};
		else
			throw new IllegalArgumentException("Cannot write field "+field);
	}
	









removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015






93




	public static interface ResultBuilder<FieldRef, FactAbstraction, Stmt, Method> {









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






94




95




96




97




		public ConstrainedFact<FieldRef, FactAbstraction, Stmt, Method> generate(FactAbstraction fact);
	}

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
/*******************************************************************************/******************************************************************************* * Copyright (c) 2015 Johannes Lerch. * Copyright (c) 2015 Johannes Lerch. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Johannes Lerch - initial API and implementation *     Johannes Lerch - initial API and implementation ******************************************************************************/ ******************************************************************************/package heros.alias;packageheros.alias;import heros.alias.FlowFunction.ConstrainedFact;importheros.alias.FlowFunction.ConstrainedFact;import heros.alias.FlowFunction.ReadFieldConstraint;importheros.alias.FlowFunction.ReadFieldConstraint;import heros.alias.FlowFunction.WriteFieldConstraint;importheros.alias.FlowFunction.WriteFieldConstraint;



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
public class AccessPathHandler<Field, Fact, Stmt, Method> {publicclassAccessPathHandler<Field,Fact,Stmt,Method>{



rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver



 

rewrite of ifds solver


Johannes Lerch
committed
Mar 19, 2015

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
	private AccessPath<Field> accessPath;privateAccessPath<Field>accessPath;	private Resolver<Field, Fact, Stmt, Method> resolver;privateResolver<Field,Fact,Stmt,Method>resolver;	public AccessPathHandler(AccessPath<Field> accessPath, Resolver<Field, Fact, Stmt, Method> resolver) {publicAccessPathHandler(AccessPath<Field>accessPath,Resolver<Field,Fact,Stmt,Method>resolver){		this.accessPath = accessPath;this.accessPath=accessPath;		this.resolver = resolver;this.resolver=resolver;	}}	public boolean canRead(Field field) {publicbooleancanRead(Fieldfield){		return accessPath.canRead(field);returnaccessPath.canRead(field);	}}		public boolean mayCanRead(Field field) {publicbooleanmayCanRead(Fieldfield){



removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path


 

 

removed unnecessary merge code in access path

 

Johannes Lerch
committed
Mar 25, 2015

32
		return accessPath.canRead(field) || (accessPath.hasEmptyAccessPath() && !accessPath.isAccessInExclusions(field));returnaccessPath.canRead(field)||(accessPath.hasEmptyAccessPath()&&!accessPath.isAccessInExclusions(field));



rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver



 

rewrite of ifds solver


Johannes Lerch
committed
Mar 19, 2015

33

34

35
	}}		public boolean mayBeEmpty() {publicbooleanmayBeEmpty(){



removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path


 

 

removed unnecessary merge code in access path

 

Johannes Lerch
committed
Mar 25, 2015

36
		return accessPath.hasEmptyAccessPath();returnaccessPath.hasEmptyAccessPath();



rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver



 

rewrite of ifds solver


Johannes Lerch
committed
Mar 19, 2015

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
	}}	public ConstrainedFact<Field, Fact, Stmt, Method> generate(Fact fact) {publicConstrainedFact<Field,Fact,Stmt,Method>generate(Factfact){		return new ConstrainedFact<>(new WrappedFact<Field, Fact, Stmt, Method>(fact, accessPath, resolver));returnnewConstrainedFact<>(newWrappedFact<Field,Fact,Stmt,Method>(fact,accessPath,resolver));	}}		public ConstrainedFact<Field, Fact, Stmt, Method> generateWithEmptyAccessPath(Fact fact, ZeroHandler<Field> zeroHandler) {publicConstrainedFact<Field,Fact,Stmt,Method>generateWithEmptyAccessPath(Factfact,ZeroHandler<Field>zeroHandler){		return new ConstrainedFact<>(new WrappedFact<>(fact, new AccessPath<Field>(), new ZeroCallEdgeResolver<>(resolver.analyzer, zeroHandler)));returnnewConstrainedFact<>(newWrappedFact<>(fact,newAccessPath<Field>(),newZeroCallEdgeResolver<>(resolver.analyzer,zeroHandler)));	}}		public ResultBuilder<Field, Fact, Stmt, Method> prepend(final Field field) {publicResultBuilder<Field,Fact,Stmt,Method>prepend(finalFieldfield){		return new ResultBuilder<Field, Fact, Stmt, Method>() {returnnewResultBuilder<Field,Fact,Stmt,Method>(){			@Override@Override			public ConstrainedFact<Field, Fact, Stmt, Method> generate(Fact fact) {publicConstrainedFact<Field,Fact,Stmt,Method>generate(Factfact){				return new ConstrainedFact<>(new WrappedFact<>(fact, accessPath.prepend(field), resolver));returnnewConstrainedFact<>(newWrappedFact<>(fact,accessPath.prepend(field),resolver));			}}		};};	}}		public ResultBuilder<Field, Fact, Stmt, Method> read(final Field field) {publicResultBuilder<Field,Fact,Stmt,Method>read(finalFieldfield){		if(mayCanRead(field)) {if(mayCanRead(field)){			return new ResultBuilder<Field, Fact, Stmt, Method>() {returnnewResultBuilder<Field,Fact,Stmt,Method>(){				@Override@Override				public ConstrainedFact<Field, Fact, Stmt, Method> generate(Fact fact) {publicConstrainedFact<Field,Fact,Stmt,Method>generate(Factfact){					if(canRead(field))if(canRead(field))



removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path


 

 

removed unnecessary merge code in access path

 

Johannes Lerch
committed
Mar 25, 2015

62
						return new ConstrainedFact<>(new WrappedFact<>(fact, accessPath.removeFirst(), resolver));returnnewConstrainedFact<>(newWrappedFact<>(fact,accessPath.removeFirst(),resolver));



rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver



 

rewrite of ifds solver


Johannes Lerch
committed
Mar 19, 2015

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
					elseelse						return new ConstrainedFact<>(new WrappedFact<>(fact, new AccessPath<Field>(), resolver), new ReadFieldConstraint<>(field));returnnewConstrainedFact<>(newWrappedFact<>(fact,newAccessPath<Field>(),resolver),newReadFieldConstraint<>(field));				}}			};};		}}		elseelse			throw new IllegalArgumentException("Cannot read field "+field);thrownewIllegalArgumentException("Cannot read field "+field);	}}		public ResultBuilder<Field, Fact, Stmt, Method> overwrite(final Field field) {publicResultBuilder<Field,Fact,Stmt,Method>overwrite(finalFieldfield){



bugfix


 

 


Johannes Lerch
committed
Mar 26, 2015



bugfix


 

 

bugfix

 

Johannes Lerch
committed
Mar 26, 2015

73
		if(mayBeEmpty())if(mayBeEmpty())



rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver



 

rewrite of ifds solver


Johannes Lerch
committed
Mar 19, 2015

74

75

76

77
			return new ResultBuilder<Field, Fact, Stmt, Method>() {returnnewResultBuilder<Field,Fact,Stmt,Method>(){				@Override@Override				public ConstrainedFact<Field, Fact, Stmt, Method> generate(Fact fact) {publicConstrainedFact<Field,Fact,Stmt,Method>generate(Factfact){					if(accessPath.canRead(field)) {if(accessPath.canRead(field)){



removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path


 

 

removed unnecessary merge code in access path

 

Johannes Lerch
committed
Mar 25, 2015

78
						AccessPath<Field> tempAccPath = accessPath.removeFirst();AccessPath<Field>tempAccPath=accessPath.removeFirst();



rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver



 

rewrite of ifds solver


Johannes Lerch
committed
Mar 19, 2015

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
						if(tempAccPath.hasEmptyAccessPath())if(tempAccPath.hasEmptyAccessPath())							return new ConstrainedFact<>(new WrappedFact<>(fact, tempAccPath.appendExcludedFieldReference(field), resolver));returnnewConstrainedFact<>(newWrappedFact<>(fact,tempAccPath.appendExcludedFieldReference(field),resolver));						elseelse							return new ConstrainedFact<>(new WrappedFact<>(fact, tempAccPath, resolver));returnnewConstrainedFact<>(newWrappedFact<>(fact,tempAccPath,resolver));					} else if(accessPath.isAccessInExclusions(field))}elseif(accessPath.isAccessInExclusions(field))						return new ConstrainedFact<>(new WrappedFact<>(fact, accessPath, resolver));returnnewConstrainedFact<>(newWrappedFact<>(fact,accessPath,resolver));					elseelse						return new ConstrainedFact<>(new WrappedFact<>(fact, accessPath.appendExcludedFieldReference(field), resolver), new WriteFieldConstraint<>(field));returnnewConstrainedFact<>(newWrappedFact<>(fact,accessPath.appendExcludedFieldReference(field),resolver),newWriteFieldConstraint<>(field));				}}			};};		elseelse			throw new IllegalArgumentException("Cannot write field "+field);thrownewIllegalArgumentException("Cannot write field "+field);	}}	



removed unnecessary merge code in access path


 

 


Johannes Lerch
committed
Mar 25, 2015



removed unnecessary merge code in access path


 

 

removed unnecessary merge code in access path

 

Johannes Lerch
committed
Mar 25, 2015

93
	public static interface ResultBuilder<FieldRef, FactAbstraction, Stmt, Method> {publicstaticinterfaceResultBuilder<FieldRef,FactAbstraction,Stmt,Method>{



rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver



 

rewrite of ifds solver


Johannes Lerch
committed
Mar 19, 2015

94

95

96

97
		public ConstrainedFact<FieldRef, FactAbstraction, Stmt, Method> generate(FactAbstraction fact);publicConstrainedFact<FieldRef,FactAbstraction,Stmt,Method>generate(FactAbstractionfact);	}}}}





