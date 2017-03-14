package ailk.app;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Timer;

import ailk.app.db.DBConnection;
import ailk.app.export.ExcelHander;
import ailk.app.util.DateUtil;
import ailk.app.thread.Slave;
public class MainAPP{
	public static void main(String[] args){
//		System.out.println("---------------come in----------------");
		try {
			ResultSet result = null;
		    PreparedStatement statement = null;
			Timer timer = new Timer();
			timer.schedule(new MasterTask(), 1000, 5000);//在1秒后执行此任务,每次间隔5秒,如果传递一个Data参数,就可以在某个固定的时间执行这个任务.
			while(true){//这个是用来停止此任务的,否则就一直循环执行此任务了
				try {
					int ch = System.in.read();
					if(ch-'c'==0){
						timer.cancel();//使用这个方法退出任务
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}			//String dateString = DateUtil.getDateString("yyyyMMddhhmmssSSS");
}
