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
			timer.schedule(new MasterTask(), 1000, 5000);//��1���ִ�д�����,ÿ�μ��5��,�������һ��Data����,�Ϳ�����ĳ���̶���ʱ��ִ���������.
			while(true){//���������ֹͣ�������,�����һֱѭ��ִ�д�������
				try {
					int ch = System.in.read();
					if(ch-'c'==0){
						timer.cancel();//ʹ����������˳�����
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
