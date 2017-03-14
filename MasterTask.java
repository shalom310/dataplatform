package ailk.app;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import ailk.app.db.DBConnection;
import ailk.app.db.DBOperate;
import ailk.app.thread.Slave;
import ailk.app.util.*;

public class MasterTask extends java.util.TimerTask{
	@Override
	public void run() {
//		System.out.println("-------run------");
		ResultSet result = null;
	    PreparedStatement statement = null;
		try{
			DBOperate dbOper = new DBOperate();
			dbOper.createConnection();
			result = dbOper.read("select * from EXPORT_FLOW where state = 'S0A'");
			if (null != result)
		     {
		    	 while (result.next()) 
		    	 {
		    		 String file_path = LatnUtil.LatnID2FilePath(result.getInt("LATN_ID"));
		    		 Slave thread = new Slave(result.getInt("FLOW_ID"),
		    				 file_path,
		    				 result.getString("URL_NAME")+"-"+result.getInt("FLOW_ID")+".csv",
		    				 result.getString("URL_NAME"),
		    				 ClobUtil.ClobToString(result.getClob("EXECUTE_SQL")),
    						 result.getInt("LATN_ID")
		    				 );
		    		 thread.start();
		    	 }
		     }
			dbOper.closeDBConnection();
		}
		catch(Exception e){
			e.printStackTrace();		
		}
		//System.out.println("________");
	}
}
