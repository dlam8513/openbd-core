/* 
 *  Copyright (C) 2000 - 2012 TagServlet Ltd
 *
 *  This file is part of Open BlueDragon (OpenBD) CFML Server Engine.
 *  
 *  OpenBD is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  Free Software Foundation,version 3.
 *  
 *  OpenBD is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with OpenBD.  If not, see http://www.gnu.org/licenses/
 *  
 *  Additional permission under GNU GPL version 3 section 7
 *  
 *  If you modify this Program, or any covered work, by linking or combining 
 *  it with any of the JARS listed in the README.txt (or a modified version of 
 *  (that library), containing parts covered by the terms of that JAR, the 
 *  licensors of this Program grant you additional permission to convey the 
 *  resulting work. 
 *  README.txt @ http://www.openbluedragon.org/license/README.txt
 *  
 *  http://www.openbluedragon.org/
 *  $Id: InspectProfileSession.java 2122 2012-06-22 10:59:34Z alan $
 */
package com.bluedragon.vision.functions;

import java.util.Iterator;
import java.util.List;

import com.bluedragon.vision.engine.CoreServer;
import com.bluedragon.vision.engine.VisionLiveSession;
import com.naryx.tagfusion.cfm.engine.cfArrayData;
import com.naryx.tagfusion.cfm.engine.cfData;
import com.naryx.tagfusion.cfm.engine.cfSession;
import com.naryx.tagfusion.cfm.engine.cfStringData;
import com.naryx.tagfusion.cfm.engine.cfStructData;
import com.naryx.tagfusion.cfm.engine.cfmRunTimeException;
import com.naryx.tagfusion.cfm.http.cfHttpData;
import com.naryx.tagfusion.cfm.sql.cfSQLQueryData;
import com.naryx.tagfusion.cfm.sql.preparedData;
import com.naryx.tagfusion.expression.function.functionBase;

public class InspectProfileSession extends functionBase {
	private static final long serialVersionUID = 1L;

	public InspectProfileSession(){
		min = max = 1;
	}
	
	public cfData execute( cfSession _session, List<cfData> parameters )throws cfmRunTimeException {
	  int sessionid = parameters.get(0).getInt();
	  VisionLiveSession	ds = CoreServer.thisInst.getActiveSession( sessionid );
	  
	  cfStructData	s	= new cfStructData();
	  
	  if ( ds != null ){
	  	cfData data = ds.getProfileData();
	  	if ( data != null && data instanceof cfSQLQueryData ){
	  		cfSQLQueryData sqldata = ((cfSQLQueryData)data);
	  		
	  		s.setData( "type", "sql" );
	  		s.setData( "sql", sqldata.queryString );
	  		s.setData( "time", System.currentTimeMillis()-sqldata.executeTime );
	  	
	  		List<preparedData> params = sqldata.getParams();
	  		if ( params != null ){
	  			cfArrayData	arr	= cfArrayData.createArray(1);
	  			
	  			Iterator<preparedData>	it	= params.iterator();
	  			while ( it.hasNext() ){
	  				preparedData	pData	= it.next();
	  				arr.addElement( new cfStringData(pData.getDataAsString()) );
	  			}
	  			
	  			s.setData("params", arr );
	  		}
	  	}else if ( data != null && data instanceof cfHttpData ){
	  		
	  		//cfHttpConnection hc	= ((cfHttpData)data).getConnection();
	  		
	  		//s.setData( "type", "http" );
	  		
	  	}
	  }
	  
	  return s;
	}	
}