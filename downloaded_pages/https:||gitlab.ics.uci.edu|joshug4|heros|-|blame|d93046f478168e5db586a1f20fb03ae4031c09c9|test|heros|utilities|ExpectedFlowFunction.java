


GitLab











Explore




Sign in




GitLab








GitLab

Explore

Sign in











Joshua Garcia heros

d93046f478168e5db586a1f20fb03ae4031c09c9




















heros


test


heros


utilities


ExpectedFlowFunction.java





Find file




Normal view



History



Permalink









ExpectedFlowFunction.java




1.3 KiB









Newer










Older









restructuring



Johannes Lerch
committed
Mar 26, 2015




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
package heros.utilities;









renaming package

 


Johannes Lerch
committed
Jun 01, 2015




13


14



import heros.fieldsens.AccessPathHandler;
import heros.fieldsens.FlowFunction;








restructuring



Johannes Lerch
committed
Mar 26, 2015




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




import com.google.common.base.Joiner;

public abstract class ExpectedFlowFunction<Fact> {

	public final Fact source;
	public final Fact[] targets;
	public Edge edge;
	int times;

	public ExpectedFlowFunction(int times, Fact source, Fact... targets) {
		this.times = times;
		this.source = source;
		this.targets = targets;
	}

	@Override
	public String toString() {
		return String.format("%s: %s -> {%s}", edge, source, Joiner.on(",").join(targets));
	}
	
	public abstract String transformerString();

	public abstract FlowFunction.ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler);
}



















Joshua Garcia heros

d93046f478168e5db586a1f20fb03ae4031c09c9




















heros


test


heros


utilities


ExpectedFlowFunction.java





Find file




Normal view



History



Permalink









ExpectedFlowFunction.java




1.3 KiB









Newer










Older









restructuring



Johannes Lerch
committed
Mar 26, 2015




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
package heros.utilities;









renaming package

 


Johannes Lerch
committed
Jun 01, 2015




13


14



import heros.fieldsens.AccessPathHandler;
import heros.fieldsens.FlowFunction;








restructuring



Johannes Lerch
committed
Mar 26, 2015




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




import com.google.common.base.Joiner;

public abstract class ExpectedFlowFunction<Fact> {

	public final Fact source;
	public final Fact[] targets;
	public Edge edge;
	int times;

	public ExpectedFlowFunction(int times, Fact source, Fact... targets) {
		this.times = times;
		this.source = source;
		this.targets = targets;
	}

	@Override
	public String toString() {
		return String.format("%s: %s -> {%s}", edge, source, Joiner.on(",").join(targets));
	}
	
	public abstract String transformerString();

	public abstract FlowFunction.ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler);
}
















Joshua Garcia heros

d93046f478168e5db586a1f20fb03ae4031c09c9












Joshua Garcia heros

d93046f478168e5db586a1f20fb03ae4031c09c9










Joshua Garcia heros

d93046f478168e5db586a1f20fb03ae4031c09c9




Joshua Garciaherosheros
d93046f478168e5db586a1f20fb03ae4031c09c9













heros


test


heros


utilities


ExpectedFlowFunction.java





Find file




Normal view



History



Permalink









ExpectedFlowFunction.java




1.3 KiB









Newer










Older









restructuring



Johannes Lerch
committed
Mar 26, 2015




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
package heros.utilities;









renaming package

 


Johannes Lerch
committed
Jun 01, 2015




13


14



import heros.fieldsens.AccessPathHandler;
import heros.fieldsens.FlowFunction;








restructuring



Johannes Lerch
committed
Mar 26, 2015




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




import com.google.common.base.Joiner;

public abstract class ExpectedFlowFunction<Fact> {

	public final Fact source;
	public final Fact[] targets;
	public Edge edge;
	int times;

	public ExpectedFlowFunction(int times, Fact source, Fact... targets) {
		this.times = times;
		this.source = source;
		this.targets = targets;
	}

	@Override
	public String toString() {
		return String.format("%s: %s -> {%s}", edge, source, Joiner.on(",").join(targets));
	}
	
	public abstract String transformerString();

	public abstract FlowFunction.ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler);
}


















heros


test


heros


utilities


ExpectedFlowFunction.java





Find file




Normal view



History



Permalink









ExpectedFlowFunction.java




1.3 KiB









Newer










Older









restructuring



Johannes Lerch
committed
Mar 26, 2015




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
package heros.utilities;









renaming package

 


Johannes Lerch
committed
Jun 01, 2015




13


14



import heros.fieldsens.AccessPathHandler;
import heros.fieldsens.FlowFunction;








restructuring



Johannes Lerch
committed
Mar 26, 2015




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




import com.google.common.base.Joiner;

public abstract class ExpectedFlowFunction<Fact> {

	public final Fact source;
	public final Fact[] targets;
	public Edge edge;
	int times;

	public ExpectedFlowFunction(int times, Fact source, Fact... targets) {
		this.times = times;
		this.source = source;
		this.targets = targets;
	}

	@Override
	public String toString() {
		return String.format("%s: %s -> {%s}", edge, source, Joiner.on(",").join(targets));
	}
	
	public abstract String transformerString();

	public abstract FlowFunction.ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler);
}













heros


test


heros


utilities


ExpectedFlowFunction.java





Find file




Normal view



History



Permalink









heros


test


heros


utilities


ExpectedFlowFunction.java





heros

test

heros

utilities

ExpectedFlowFunction.java


Find file




Normal view



History



Permalink



Find file


Normal view

History

Permalink





ExpectedFlowFunction.java




1.3 KiB









Newer










Older









restructuring



Johannes Lerch
committed
Mar 26, 2015




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
package heros.utilities;









renaming package

 


Johannes Lerch
committed
Jun 01, 2015




13


14



import heros.fieldsens.AccessPathHandler;
import heros.fieldsens.FlowFunction;








restructuring



Johannes Lerch
committed
Mar 26, 2015




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




import com.google.common.base.Joiner;

public abstract class ExpectedFlowFunction<Fact> {

	public final Fact source;
	public final Fact[] targets;
	public Edge edge;
	int times;

	public ExpectedFlowFunction(int times, Fact source, Fact... targets) {
		this.times = times;
		this.source = source;
		this.targets = targets;
	}

	@Override
	public String toString() {
		return String.format("%s: %s -> {%s}", edge, source, Joiner.on(",").join(targets));
	}
	
	public abstract String transformerString();

	public abstract FlowFunction.ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler);
}









ExpectedFlowFunction.java




1.3 KiB










ExpectedFlowFunction.java




1.3 KiB









Newer










Older
NewerOlder







restructuring



Johannes Lerch
committed
Mar 26, 2015




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
package heros.utilities;









renaming package

 


Johannes Lerch
committed
Jun 01, 2015




13


14



import heros.fieldsens.AccessPathHandler;
import heros.fieldsens.FlowFunction;








restructuring



Johannes Lerch
committed
Mar 26, 2015




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




import com.google.common.base.Joiner;

public abstract class ExpectedFlowFunction<Fact> {

	public final Fact source;
	public final Fact[] targets;
	public Edge edge;
	int times;

	public ExpectedFlowFunction(int times, Fact source, Fact... targets) {
		this.times = times;
		this.source = source;
		this.targets = targets;
	}

	@Override
	public String toString() {
		return String.format("%s: %s -> {%s}", edge, source, Joiner.on(",").join(targets));
	}
	
	public abstract String transformerString();

	public abstract FlowFunction.ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler);
}











restructuring



Johannes Lerch
committed
Mar 26, 2015




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
package heros.utilities;









renaming package

 


Johannes Lerch
committed
Jun 01, 2015




13


14



import heros.fieldsens.AccessPathHandler;
import heros.fieldsens.FlowFunction;








restructuring



Johannes Lerch
committed
Mar 26, 2015




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




import com.google.common.base.Joiner;

public abstract class ExpectedFlowFunction<Fact> {

	public final Fact source;
	public final Fact[] targets;
	public Edge edge;
	int times;

	public ExpectedFlowFunction(int times, Fact source, Fact... targets) {
		this.times = times;
		this.source = source;
		this.targets = targets;
	}

	@Override
	public String toString() {
		return String.format("%s: %s -> {%s}", edge, source, Joiner.on(",").join(targets));
	}
	
	public abstract String transformerString();

	public abstract FlowFunction.ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler);
}









restructuring



Johannes Lerch
committed
Mar 26, 2015




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
package heros.utilities;









renaming package

 


Johannes Lerch
committed
Jun 01, 2015




13


14



import heros.fieldsens.AccessPathHandler;
import heros.fieldsens.FlowFunction;








restructuring



Johannes Lerch
committed
Mar 26, 2015




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




import com.google.common.base.Joiner;

public abstract class ExpectedFlowFunction<Fact> {

	public final Fact source;
	public final Fact[] targets;
	public Edge edge;
	int times;

	public ExpectedFlowFunction(int times, Fact source, Fact... targets) {
		this.times = times;
		this.source = source;
		this.targets = targets;
	}

	@Override
	public String toString() {
		return String.format("%s: %s -> {%s}", edge, source, Joiner.on(",").join(targets));
	}
	
	public abstract String transformerString();

	public abstract FlowFunction.ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler);
}







restructuring



Johannes Lerch
committed
Mar 26, 2015




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
package heros.utilities;







restructuring



Johannes Lerch
committed
Mar 26, 2015



restructuring


restructuring

Johannes Lerch
committed
Mar 26, 2015


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
package heros.utilities;


/*******************************************************************************/******************************************************************************* * Copyright (c) 2015 Johannes Lerch. * Copyright (c) 2015 Johannes Lerch. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Johannes Lerch - initial API and implementation *     Johannes Lerch - initial API and implementation ******************************************************************************/ ******************************************************************************/package heros.utilities;packageheros.utilities;




renaming package

 


Johannes Lerch
committed
Jun 01, 2015




13


14



import heros.fieldsens.AccessPathHandler;
import heros.fieldsens.FlowFunction;






renaming package

 


Johannes Lerch
committed
Jun 01, 2015



renaming package

 

renaming package

Johannes Lerch
committed
Jun 01, 2015


13


14


import heros.fieldsens.AccessPathHandler;
import heros.fieldsens.FlowFunction;

import heros.fieldsens.AccessPathHandler;importheros.fieldsens.AccessPathHandler;import heros.fieldsens.FlowFunction;importheros.fieldsens.FlowFunction;




restructuring



Johannes Lerch
committed
Mar 26, 2015




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




import com.google.common.base.Joiner;

public abstract class ExpectedFlowFunction<Fact> {

	public final Fact source;
	public final Fact[] targets;
	public Edge edge;
	int times;

	public ExpectedFlowFunction(int times, Fact source, Fact... targets) {
		this.times = times;
		this.source = source;
		this.targets = targets;
	}

	@Override
	public String toString() {
		return String.format("%s: %s -> {%s}", edge, source, Joiner.on(",").join(targets));
	}
	
	public abstract String transformerString();

	public abstract FlowFunction.ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler);
}





restructuring



Johannes Lerch
committed
Mar 26, 2015



restructuring


restructuring

Johannes Lerch
committed
Mar 26, 2015


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



import com.google.common.base.Joiner;

public abstract class ExpectedFlowFunction<Fact> {

	public final Fact source;
	public final Fact[] targets;
	public Edge edge;
	int times;

	public ExpectedFlowFunction(int times, Fact source, Fact... targets) {
		this.times = times;
		this.source = source;
		this.targets = targets;
	}

	@Override
	public String toString() {
		return String.format("%s: %s -> {%s}", edge, source, Joiner.on(",").join(targets));
	}
	
	public abstract String transformerString();

	public abstract FlowFunction.ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler);
}
import com.google.common.base.Joiner;importcom.google.common.base.Joiner;public abstract class ExpectedFlowFunction<Fact> {publicabstractclassExpectedFlowFunction<Fact>{	public final Fact source;publicfinalFactsource;	public final Fact[] targets;publicfinalFact[]targets;	public Edge edge;publicEdgeedge;	int times;inttimes;	public ExpectedFlowFunction(int times, Fact source, Fact... targets) {publicExpectedFlowFunction(inttimes,Factsource,Fact...targets){		this.times = times;this.times=times;		this.source = source;this.source=source;		this.targets = targets;this.targets=targets;	}}	@Override@Override	public String toString() {publicStringtoString(){		return String.format("%s: %s -> {%s}", edge, source, Joiner.on(",").join(targets));returnString.format("%s: %s -> {%s}",edge,source,Joiner.on(",").join(targets));	}}		public abstract String transformerString();publicabstractStringtransformerString();	public abstract FlowFunction.ConstrainedFact<String, TestFact, Statement, TestMethod> apply(TestFact target, AccessPathHandler<String, TestFact, Statement, TestMethod> accPathHandler);publicabstractFlowFunction.ConstrainedFact<String,TestFact,Statement,TestMethod>apply(TestFacttarget,AccessPathHandler<String,TestFact,Statement,TestMethod>accPathHandler);}}