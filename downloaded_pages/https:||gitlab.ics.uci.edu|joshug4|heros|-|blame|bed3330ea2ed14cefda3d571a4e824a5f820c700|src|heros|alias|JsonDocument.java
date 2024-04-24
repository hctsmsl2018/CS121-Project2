



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

bed3330ea2ed14cefda3d571a4e824a5f820c700

















bed3330ea2ed14cefda3d571a4e824a5f820c700


Switch branch/tag










heros


src


heros


alias


JsonDocument.java



Find file
Normal viewHistoryPermalink






JsonDocument.java



2.63 KB









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




17




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

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






18




public class JsonDocument {









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






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




	
	private CacheMap<String, JsonDocument> documents = new CacheMap<String, JsonDocument>() {
		@Override
		protected JsonDocument createItem(String key) {
			return new JsonDocument();
		}
	};
	private CacheMap<String, JsonArray> arrays = new CacheMap<String, JsonArray>() {
		@Override
		protected JsonArray createItem(String key) {
			return new JsonArray();
		}
	};
	private Map<String, String> keyValuePairs = Maps.newHashMap();
	
	public JsonDocument doc(String key) {
		return documents.getOrCreate(key);
	}
	









removing needless/wrong commas at the end


 

 


Johannes Lerch
committed
Apr 23, 2015






38




39




40




41




42




43




44




	public JsonDocument doc(String key, JsonDocument doc) {
		if(documents.containsKey(key))
			throw new IllegalArgumentException("There is already a document registered for key: "+key);
		documents.put(key, doc);
		return doc;
	}
	









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






45




46




47




48




49




50




51




52




	public JsonArray array(String key) {
		return arrays.getOrCreate(key);
	}
	
	public void keyValue(String key, String value) {
		keyValuePairs.put(key, value);
	}
	









removing needless/wrong commas at the end


 

 


Johannes Lerch
committed
Apr 23, 2015






53




54




55




56




57




58




59




	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		write(builder, 0);
		return builder.toString();
	}
	









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






60




61




62




63




	public void write(StringBuilder builder, int tabs) {
		builder.append("{\n");
		
		for(Entry<String, String> entry : keyValuePairs.entrySet()) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






64




			tabs(tabs+1, builder); builder.append("\""+entry.getKey()+"\": \""+entry.getValue()+"\",\n");









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






65




66




67




		}
		
		for(Entry<String, JsonArray> entry : arrays.entrySet()) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






68




			tabs(tabs+1, builder); builder.append("\""+entry.getKey()+"\": ");









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






69




70




71




72




73




			entry.getValue().write(builder, tabs+1);
			builder.append(",\n");
		}
		
		for(Entry<String, JsonDocument> entry : documents.entrySet()) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






74




			tabs(tabs+1, builder); builder.append("\""+entry.getKey()+"\": ");









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






75




76




77




78




			entry.getValue().write(builder, tabs+1);
			builder.append(",\n");
		}
		









removing needless/wrong commas at the end


 

 


Johannes Lerch
committed
Apr 23, 2015






79




80




81




		if(!keyValuePairs.isEmpty() || !arrays.isEmpty() || !documents.isEmpty())
			builder.delete(builder.length()-2, builder.length()-1); 
		









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






82




		tabs(tabs, builder); builder.append("}");









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






83




84




	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






85




86




87




88




	static void tabs(int tabs, StringBuilder builder) {
		for(int i=0; i<tabs; i++)
			builder.append("\t");
	}









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






89




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

bed3330ea2ed14cefda3d571a4e824a5f820c700

















bed3330ea2ed14cefda3d571a4e824a5f820c700


Switch branch/tag










heros


src


heros


alias


JsonDocument.java



Find file
Normal viewHistoryPermalink






JsonDocument.java



2.63 KB









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




17




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

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






18




public class JsonDocument {









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






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




	
	private CacheMap<String, JsonDocument> documents = new CacheMap<String, JsonDocument>() {
		@Override
		protected JsonDocument createItem(String key) {
			return new JsonDocument();
		}
	};
	private CacheMap<String, JsonArray> arrays = new CacheMap<String, JsonArray>() {
		@Override
		protected JsonArray createItem(String key) {
			return new JsonArray();
		}
	};
	private Map<String, String> keyValuePairs = Maps.newHashMap();
	
	public JsonDocument doc(String key) {
		return documents.getOrCreate(key);
	}
	









removing needless/wrong commas at the end


 

 


Johannes Lerch
committed
Apr 23, 2015






38




39




40




41




42




43




44




	public JsonDocument doc(String key, JsonDocument doc) {
		if(documents.containsKey(key))
			throw new IllegalArgumentException("There is already a document registered for key: "+key);
		documents.put(key, doc);
		return doc;
	}
	









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






45




46




47




48




49




50




51




52




	public JsonArray array(String key) {
		return arrays.getOrCreate(key);
	}
	
	public void keyValue(String key, String value) {
		keyValuePairs.put(key, value);
	}
	









removing needless/wrong commas at the end


 

 


Johannes Lerch
committed
Apr 23, 2015






53




54




55




56




57




58




59




	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		write(builder, 0);
		return builder.toString();
	}
	









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






60




61




62




63




	public void write(StringBuilder builder, int tabs) {
		builder.append("{\n");
		
		for(Entry<String, String> entry : keyValuePairs.entrySet()) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






64




			tabs(tabs+1, builder); builder.append("\""+entry.getKey()+"\": \""+entry.getValue()+"\",\n");









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






65




66




67




		}
		
		for(Entry<String, JsonArray> entry : arrays.entrySet()) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






68




			tabs(tabs+1, builder); builder.append("\""+entry.getKey()+"\": ");









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






69




70




71




72




73




			entry.getValue().write(builder, tabs+1);
			builder.append(",\n");
		}
		
		for(Entry<String, JsonDocument> entry : documents.entrySet()) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






74




			tabs(tabs+1, builder); builder.append("\""+entry.getKey()+"\": ");









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






75




76




77




78




			entry.getValue().write(builder, tabs+1);
			builder.append(",\n");
		}
		









removing needless/wrong commas at the end


 

 


Johannes Lerch
committed
Apr 23, 2015






79




80




81




		if(!keyValuePairs.isEmpty() || !arrays.isEmpty() || !documents.isEmpty())
			builder.delete(builder.length()-2, builder.length()-1); 
		









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






82




		tabs(tabs, builder); builder.append("}");









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






83




84




	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






85




86




87




88




	static void tabs(int tabs, StringBuilder builder) {
		for(int i=0; i<tabs; i++)
			builder.append("\t");
	}









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






89




}











Open sidebar



Joshua Garcia heros

bed3330ea2ed14cefda3d571a4e824a5f820c700







Open sidebar



Joshua Garcia heros

bed3330ea2ed14cefda3d571a4e824a5f820c700




Open sidebar

Joshua Garcia heros

bed3330ea2ed14cefda3d571a4e824a5f820c700


Joshua Garciaherosheros
bed3330ea2ed14cefda3d571a4e824a5f820c700










bed3330ea2ed14cefda3d571a4e824a5f820c700


Switch branch/tag










heros


src


heros


alias


JsonDocument.java



Find file
Normal viewHistoryPermalink






JsonDocument.java



2.63 KB









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




17




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

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






18




public class JsonDocument {









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






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




	
	private CacheMap<String, JsonDocument> documents = new CacheMap<String, JsonDocument>() {
		@Override
		protected JsonDocument createItem(String key) {
			return new JsonDocument();
		}
	};
	private CacheMap<String, JsonArray> arrays = new CacheMap<String, JsonArray>() {
		@Override
		protected JsonArray createItem(String key) {
			return new JsonArray();
		}
	};
	private Map<String, String> keyValuePairs = Maps.newHashMap();
	
	public JsonDocument doc(String key) {
		return documents.getOrCreate(key);
	}
	









removing needless/wrong commas at the end


 

 


Johannes Lerch
committed
Apr 23, 2015






38




39




40




41




42




43




44




	public JsonDocument doc(String key, JsonDocument doc) {
		if(documents.containsKey(key))
			throw new IllegalArgumentException("There is already a document registered for key: "+key);
		documents.put(key, doc);
		return doc;
	}
	









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






45




46




47




48




49




50




51




52




	public JsonArray array(String key) {
		return arrays.getOrCreate(key);
	}
	
	public void keyValue(String key, String value) {
		keyValuePairs.put(key, value);
	}
	









removing needless/wrong commas at the end


 

 


Johannes Lerch
committed
Apr 23, 2015






53




54




55




56




57




58




59




	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		write(builder, 0);
		return builder.toString();
	}
	









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






60




61




62




63




	public void write(StringBuilder builder, int tabs) {
		builder.append("{\n");
		
		for(Entry<String, String> entry : keyValuePairs.entrySet()) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






64




			tabs(tabs+1, builder); builder.append("\""+entry.getKey()+"\": \""+entry.getValue()+"\",\n");









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






65




66




67




		}
		
		for(Entry<String, JsonArray> entry : arrays.entrySet()) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






68




			tabs(tabs+1, builder); builder.append("\""+entry.getKey()+"\": ");









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






69




70




71




72




73




			entry.getValue().write(builder, tabs+1);
			builder.append(",\n");
		}
		
		for(Entry<String, JsonDocument> entry : documents.entrySet()) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






74




			tabs(tabs+1, builder); builder.append("\""+entry.getKey()+"\": ");









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






75




76




77




78




			entry.getValue().write(builder, tabs+1);
			builder.append(",\n");
		}
		









removing needless/wrong commas at the end


 

 


Johannes Lerch
committed
Apr 23, 2015






79




80




81




		if(!keyValuePairs.isEmpty() || !arrays.isEmpty() || !documents.isEmpty())
			builder.delete(builder.length()-2, builder.length()-1); 
		









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






82




		tabs(tabs, builder); builder.append("}");









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






83




84




	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






85




86




87




88




	static void tabs(int tabs, StringBuilder builder) {
		for(int i=0; i<tabs; i++)
			builder.append("\t");
	}









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






89




}














bed3330ea2ed14cefda3d571a4e824a5f820c700


Switch branch/tag










heros


src


heros


alias


JsonDocument.java



Find file
Normal viewHistoryPermalink






JsonDocument.java



2.63 KB









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




17




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

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






18




public class JsonDocument {









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






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




	
	private CacheMap<String, JsonDocument> documents = new CacheMap<String, JsonDocument>() {
		@Override
		protected JsonDocument createItem(String key) {
			return new JsonDocument();
		}
	};
	private CacheMap<String, JsonArray> arrays = new CacheMap<String, JsonArray>() {
		@Override
		protected JsonArray createItem(String key) {
			return new JsonArray();
		}
	};
	private Map<String, String> keyValuePairs = Maps.newHashMap();
	
	public JsonDocument doc(String key) {
		return documents.getOrCreate(key);
	}
	









removing needless/wrong commas at the end


 

 


Johannes Lerch
committed
Apr 23, 2015






38




39




40




41




42




43




44




	public JsonDocument doc(String key, JsonDocument doc) {
		if(documents.containsKey(key))
			throw new IllegalArgumentException("There is already a document registered for key: "+key);
		documents.put(key, doc);
		return doc;
	}
	









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






45




46




47




48




49




50




51




52




	public JsonArray array(String key) {
		return arrays.getOrCreate(key);
	}
	
	public void keyValue(String key, String value) {
		keyValuePairs.put(key, value);
	}
	









removing needless/wrong commas at the end


 

 


Johannes Lerch
committed
Apr 23, 2015






53




54




55




56




57




58




59




	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		write(builder, 0);
		return builder.toString();
	}
	









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






60




61




62




63




	public void write(StringBuilder builder, int tabs) {
		builder.append("{\n");
		
		for(Entry<String, String> entry : keyValuePairs.entrySet()) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






64




			tabs(tabs+1, builder); builder.append("\""+entry.getKey()+"\": \""+entry.getValue()+"\",\n");









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






65




66




67




		}
		
		for(Entry<String, JsonArray> entry : arrays.entrySet()) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






68




			tabs(tabs+1, builder); builder.append("\""+entry.getKey()+"\": ");









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






69




70




71




72




73




			entry.getValue().write(builder, tabs+1);
			builder.append(",\n");
		}
		
		for(Entry<String, JsonDocument> entry : documents.entrySet()) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






74




			tabs(tabs+1, builder); builder.append("\""+entry.getKey()+"\": ");









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






75




76




77




78




			entry.getValue().write(builder, tabs+1);
			builder.append(",\n");
		}
		









removing needless/wrong commas at the end


 

 


Johannes Lerch
committed
Apr 23, 2015






79




80




81




		if(!keyValuePairs.isEmpty() || !arrays.isEmpty() || !documents.isEmpty())
			builder.delete(builder.length()-2, builder.length()-1); 
		









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






82




		tabs(tabs, builder); builder.append("}");









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






83




84




	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






85




86




87




88




	static void tabs(int tabs, StringBuilder builder) {
		for(int i=0; i<tabs; i++)
			builder.append("\t");
	}









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






89




}










bed3330ea2ed14cefda3d571a4e824a5f820c700


Switch branch/tag










heros


src


heros


alias


JsonDocument.java



Find file
Normal viewHistoryPermalink




bed3330ea2ed14cefda3d571a4e824a5f820c700


Switch branch/tag










heros


src


heros


alias


JsonDocument.java





bed3330ea2ed14cefda3d571a4e824a5f820c700


Switch branch/tag








bed3330ea2ed14cefda3d571a4e824a5f820c700


Switch branch/tag





bed3330ea2ed14cefda3d571a4e824a5f820c700

Switch branch/tag





Switch branch/tag



Switch branch/tagSwitch branch/tag
heros

src

heros

alias

JsonDocument.java
Find file
Normal viewHistoryPermalink




JsonDocument.java



2.63 KB









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




17




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

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






18




public class JsonDocument {









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






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




	
	private CacheMap<String, JsonDocument> documents = new CacheMap<String, JsonDocument>() {
		@Override
		protected JsonDocument createItem(String key) {
			return new JsonDocument();
		}
	};
	private CacheMap<String, JsonArray> arrays = new CacheMap<String, JsonArray>() {
		@Override
		protected JsonArray createItem(String key) {
			return new JsonArray();
		}
	};
	private Map<String, String> keyValuePairs = Maps.newHashMap();
	
	public JsonDocument doc(String key) {
		return documents.getOrCreate(key);
	}
	









removing needless/wrong commas at the end


 

 


Johannes Lerch
committed
Apr 23, 2015






38




39




40




41




42




43




44




	public JsonDocument doc(String key, JsonDocument doc) {
		if(documents.containsKey(key))
			throw new IllegalArgumentException("There is already a document registered for key: "+key);
		documents.put(key, doc);
		return doc;
	}
	









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






45




46




47




48




49




50




51




52




	public JsonArray array(String key) {
		return arrays.getOrCreate(key);
	}
	
	public void keyValue(String key, String value) {
		keyValuePairs.put(key, value);
	}
	









removing needless/wrong commas at the end


 

 


Johannes Lerch
committed
Apr 23, 2015






53




54




55




56




57




58




59




	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		write(builder, 0);
		return builder.toString();
	}
	









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






60




61




62




63




	public void write(StringBuilder builder, int tabs) {
		builder.append("{\n");
		
		for(Entry<String, String> entry : keyValuePairs.entrySet()) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






64




			tabs(tabs+1, builder); builder.append("\""+entry.getKey()+"\": \""+entry.getValue()+"\",\n");









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






65




66




67




		}
		
		for(Entry<String, JsonArray> entry : arrays.entrySet()) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






68




			tabs(tabs+1, builder); builder.append("\""+entry.getKey()+"\": ");









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






69




70




71




72




73




			entry.getValue().write(builder, tabs+1);
			builder.append(",\n");
		}
		
		for(Entry<String, JsonDocument> entry : documents.entrySet()) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






74




			tabs(tabs+1, builder); builder.append("\""+entry.getKey()+"\": ");









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






75




76




77




78




			entry.getValue().write(builder, tabs+1);
			builder.append(",\n");
		}
		









removing needless/wrong commas at the end


 

 


Johannes Lerch
committed
Apr 23, 2015






79




80




81




		if(!keyValuePairs.isEmpty() || !arrays.isEmpty() || !documents.isEmpty())
			builder.delete(builder.length()-2, builder.length()-1); 
		









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






82




		tabs(tabs, builder); builder.append("}");









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






83




84




	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






85




86




87




88




	static void tabs(int tabs, StringBuilder builder) {
		for(int i=0; i<tabs; i++)
			builder.append("\t");
	}









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






89




}








JsonDocument.java



2.63 KB










JsonDocument.java



2.63 KB









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




17




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

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






18




public class JsonDocument {









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






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




	
	private CacheMap<String, JsonDocument> documents = new CacheMap<String, JsonDocument>() {
		@Override
		protected JsonDocument createItem(String key) {
			return new JsonDocument();
		}
	};
	private CacheMap<String, JsonArray> arrays = new CacheMap<String, JsonArray>() {
		@Override
		protected JsonArray createItem(String key) {
			return new JsonArray();
		}
	};
	private Map<String, String> keyValuePairs = Maps.newHashMap();
	
	public JsonDocument doc(String key) {
		return documents.getOrCreate(key);
	}
	









removing needless/wrong commas at the end


 

 


Johannes Lerch
committed
Apr 23, 2015






38




39




40




41




42




43




44




	public JsonDocument doc(String key, JsonDocument doc) {
		if(documents.containsKey(key))
			throw new IllegalArgumentException("There is already a document registered for key: "+key);
		documents.put(key, doc);
		return doc;
	}
	









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






45




46




47




48




49




50




51




52




	public JsonArray array(String key) {
		return arrays.getOrCreate(key);
	}
	
	public void keyValue(String key, String value) {
		keyValuePairs.put(key, value);
	}
	









removing needless/wrong commas at the end


 

 


Johannes Lerch
committed
Apr 23, 2015






53




54




55




56




57




58




59




	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		write(builder, 0);
		return builder.toString();
	}
	









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






60




61




62




63




	public void write(StringBuilder builder, int tabs) {
		builder.append("{\n");
		
		for(Entry<String, String> entry : keyValuePairs.entrySet()) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






64




			tabs(tabs+1, builder); builder.append("\""+entry.getKey()+"\": \""+entry.getValue()+"\",\n");









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






65




66




67




		}
		
		for(Entry<String, JsonArray> entry : arrays.entrySet()) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






68




			tabs(tabs+1, builder); builder.append("\""+entry.getKey()+"\": ");









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






69




70




71




72




73




			entry.getValue().write(builder, tabs+1);
			builder.append(",\n");
		}
		
		for(Entry<String, JsonDocument> entry : documents.entrySet()) {









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






74




			tabs(tabs+1, builder); builder.append("\""+entry.getKey()+"\": ");









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






75




76




77




78




			entry.getValue().write(builder, tabs+1);
			builder.append(",\n");
		}
		









removing needless/wrong commas at the end


 

 


Johannes Lerch
committed
Apr 23, 2015






79




80




81




		if(!keyValuePairs.isEmpty() || !arrays.isEmpty() || !documents.isEmpty())
			builder.delete(builder.length()-2, builder.length()-1); 
		









restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






82




		tabs(tabs, builder); builder.append("}");









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






83




84




	}










restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015






85




86




87




88




	static void tabs(int tabs, StringBuilder builder) {
		for(int i=0; i<tabs; i++)
			builder.append("\t");
	}









rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015






89




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

17
/*******************************************************************************/******************************************************************************* * Copyright (c) 2015 Johannes Lerch. * Copyright (c) 2015 Johannes Lerch. * All rights reserved. This program and the accompanying materials * All rights reserved. This program and the accompanying materials * are made available under the terms of the GNU Lesser Public License v2.1 * are made available under the terms of the GNU Lesser Public License v2.1 * which accompanies this distribution, and is available at * which accompanies this distribution, and is available at * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html *  *  * Contributors: * Contributors: *     Johannes Lerch - initial API and implementation *     Johannes Lerch - initial API and implementation ******************************************************************************/ ******************************************************************************/package heros.alias;packageheros.alias;import java.util.Map;importjava.util.Map;import java.util.Map.Entry;importjava.util.Map.Entry;import com.google.common.collect.Maps;importcom.google.common.collect.Maps;



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

18
public class JsonDocument {publicclassJsonDocument{



rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver



 

rewrite of ifds solver


Johannes Lerch
committed
Mar 19, 2015

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
		private CacheMap<String, JsonDocument> documents = new CacheMap<String, JsonDocument>() {privateCacheMap<String,JsonDocument>documents=newCacheMap<String,JsonDocument>(){		@Override@Override		protected JsonDocument createItem(String key) {protectedJsonDocumentcreateItem(Stringkey){			return new JsonDocument();returnnewJsonDocument();		}}	};};	private CacheMap<String, JsonArray> arrays = new CacheMap<String, JsonArray>() {privateCacheMap<String,JsonArray>arrays=newCacheMap<String,JsonArray>(){		@Override@Override		protected JsonArray createItem(String key) {protectedJsonArraycreateItem(Stringkey){			return new JsonArray();returnnewJsonArray();		}}	};};	private Map<String, String> keyValuePairs = Maps.newHashMap();privateMap<String,String>keyValuePairs=Maps.newHashMap();		public JsonDocument doc(String key) {publicJsonDocumentdoc(Stringkey){		return documents.getOrCreate(key);returndocuments.getOrCreate(key);	}}	



removing needless/wrong commas at the end


 

 


Johannes Lerch
committed
Apr 23, 2015



removing needless/wrong commas at the end


 

 

removing needless/wrong commas at the end

 

Johannes Lerch
committed
Apr 23, 2015

38

39

40

41

42

43

44
	public JsonDocument doc(String key, JsonDocument doc) {publicJsonDocumentdoc(Stringkey,JsonDocumentdoc){		if(documents.containsKey(key))if(documents.containsKey(key))			throw new IllegalArgumentException("There is already a document registered for key: "+key);thrownewIllegalArgumentException("There is already a document registered for key: "+key);		documents.put(key, doc);documents.put(key,doc);		return doc;returndoc;	}}	



rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver



 

rewrite of ifds solver


Johannes Lerch
committed
Mar 19, 2015

45

46

47

48

49

50

51

52
	public JsonArray array(String key) {publicJsonArrayarray(Stringkey){		return arrays.getOrCreate(key);returnarrays.getOrCreate(key);	}}		public void keyValue(String key, String value) {publicvoidkeyValue(Stringkey,Stringvalue){		keyValuePairs.put(key, value);keyValuePairs.put(key,value);	}}	



removing needless/wrong commas at the end


 

 


Johannes Lerch
committed
Apr 23, 2015



removing needless/wrong commas at the end


 

 

removing needless/wrong commas at the end

 

Johannes Lerch
committed
Apr 23, 2015

53

54

55

56

57

58

59
	@Override@Override	public String toString() {publicStringtoString(){		StringBuilder builder = new StringBuilder();StringBuilderbuilder=newStringBuilder();		write(builder, 0);write(builder,0);		return builder.toString();returnbuilder.toString();	}}	



rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver



 

rewrite of ifds solver


Johannes Lerch
committed
Mar 19, 2015

60

61

62

63
	public void write(StringBuilder builder, int tabs) {publicvoidwrite(StringBuilderbuilder,inttabs){		builder.append("{\n");builder.append("{\n");				for(Entry<String, String> entry : keyValuePairs.entrySet()) {for(Entry<String,String>entry:keyValuePairs.entrySet()){



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

64
			tabs(tabs+1, builder); builder.append("\""+entry.getKey()+"\": \""+entry.getValue()+"\",\n");tabs(tabs+1,builder);builder.append("\""+entry.getKey()+"\": \""+entry.getValue()+"\",\n");



rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver



 

rewrite of ifds solver


Johannes Lerch
committed
Mar 19, 2015

65

66

67
		}}				for(Entry<String, JsonArray> entry : arrays.entrySet()) {for(Entry<String,JsonArray>entry:arrays.entrySet()){



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

68
			tabs(tabs+1, builder); builder.append("\""+entry.getKey()+"\": ");tabs(tabs+1,builder);builder.append("\""+entry.getKey()+"\": ");



rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver



 

rewrite of ifds solver


Johannes Lerch
committed
Mar 19, 2015

69

70

71

72

73
			entry.getValue().write(builder, tabs+1);entry.getValue().write(builder,tabs+1);			builder.append(",\n");builder.append(",\n");		}}				for(Entry<String, JsonDocument> entry : documents.entrySet()) {for(Entry<String,JsonDocument>entry:documents.entrySet()){



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

74
			tabs(tabs+1, builder); builder.append("\""+entry.getKey()+"\": ");tabs(tabs+1,builder);builder.append("\""+entry.getKey()+"\": ");



rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver



 

rewrite of ifds solver


Johannes Lerch
committed
Mar 19, 2015

75

76

77

78
			entry.getValue().write(builder, tabs+1);entry.getValue().write(builder,tabs+1);			builder.append(",\n");builder.append(",\n");		}}		



removing needless/wrong commas at the end


 

 


Johannes Lerch
committed
Apr 23, 2015



removing needless/wrong commas at the end


 

 

removing needless/wrong commas at the end

 

Johannes Lerch
committed
Apr 23, 2015

79

80

81
		if(!keyValuePairs.isEmpty() || !arrays.isEmpty() || !documents.isEmpty())if(!keyValuePairs.isEmpty()||!arrays.isEmpty()||!documents.isEmpty())			builder.delete(builder.length()-2, builder.length()-1); builder.delete(builder.length()-2,builder.length()-1);		



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

82
		tabs(tabs, builder); builder.append("}");tabs(tabs,builder);builder.append("}");



rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver



 

rewrite of ifds solver


Johannes Lerch
committed
Mar 19, 2015

83

84
	}}



restructuring


 

 


Johannes Lerch
committed
Mar 26, 2015



restructuring


 

 

restructuring

 

Johannes Lerch
committed
Mar 26, 2015

85

86

87

88
	static void tabs(int tabs, StringBuilder builder) {staticvoidtabs(inttabs,StringBuilderbuilder){		for(int i=0; i<tabs; i++)for(inti=0;i<tabs;i++)			builder.append("\t");builder.append("\t");	}}



rewrite of ifds solver



 


Johannes Lerch
committed
Mar 19, 2015



rewrite of ifds solver



 

rewrite of ifds solver


Johannes Lerch
committed
Mar 19, 2015

89
}}





