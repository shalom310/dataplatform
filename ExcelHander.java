package ailk.app.export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import ailk.app.db.DBOperate;
import ailk.app.db.Parameter;
import ailk.app.util.LatnUtil;
import ailk.app.util.ZipUtil;

public class ExcelHander {

	public static void sqlToExcle(int id, String filepath, String sql,
			String filename, String sheetName, int latnID) {
		DBOperate dbOper = new DBOperate();
		try {
			dbOper.createConnection();
			resultSetToCSV(id, dbOper.read(sql), filepath, filename,
					sheetName, latnID);
			dbOper.closeDBConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOper.closeDBConnection();
		} catch (Exception e) {
			e.printStackTrace();
			dbOper.closeDBConnection();
		}

	}

	public static void resultSetToCSV(int id, ResultSet rs, String filepath,
			String filename, String sheetName, int latnID) throws Exception {
		// HSSFWorkbook workbook = new HSSFWorkbook();
		// HSSFSheet sheet = workbook.createSheet();
		// workbook.setSheetName(0, sheetName);
		// HSSFRow row = sheet.createRow((short) 0);
		// HSSFCell cell;
		ResultSetMetaData md = rs.getMetaData();
		int nColumn = md.getColumnCount();

		/*
		 * // 写入各个字段的名称 for (int i = 1; i <= nColumn; i++) { cell =
		 * row.createCell((short) (i - 1));
		 * cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		 * cell.setCellValue(md.getColumnLabel(i)); } int iRow = 1; //
		 * 写入各条记录，每条记录对应Excel中的一行 while (rs.next()) { row =
		 * sheet.createRow((short) iRow); for (int j = 1; j <= nColumn; j++) {
		 * cell = row.createCell((short) (j - 1));
		 * cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		 * cell.setCellValue(rs.getObject(j) ==
		 * null?"":rs.getObject(j).toString()); } iRow++; }
		 */

		File file = new File(LatnUtil.LatnID2FilePath(latnID) + id+".csv");
		FileOutputStream out = new FileOutputStream(file);
		OutputStreamWriter osw = new OutputStreamWriter(out);
		BufferedWriter bw = new BufferedWriter(osw);
		// 写入各个字段的名称
		for (int i = 1; i <= nColumn - 1; i++) {
			bw.write(md.getColumnLabel(i));
			bw.write(",");
		}
		bw.write(md.getColumnLabel(nColumn));
		bw.write("\n");
		int iRow = 1;
		Object value;
		while (rs.next()) {
			for (int j = 1; j <= nColumn - 1; j++) {
				value = rs.getObject(j);
				bw.write(value == null ? "" : value.toString());
				// bw.write(rs.getObject(j)+"");
				bw.write(",");
			}
			value = rs.getObject(nColumn);
			bw.write(value == null ? "" : value.toString());
			// bw.write(rs.getObject(nColumn)+"");
			bw.write("\n");
			iRow++;
		}

		bw.close();
		osw.close();
		out.close();
		
		ZipUtil.zip(filepath+filename+".zip", "", filepath+id+".csv");
		file.delete();
		
		DBOperate dbOper = new DBOperate();
		dbOper.beginTransaction();
		ArrayList paraList = new ArrayList();
		paraList.add(new Parameter(filepath));
		paraList.add(new Parameter(filename+".zip"));
		paraList.add(new Parameter(id));
		dbOper.execute(
				"update EXPORT_FLOW set state = 'S0E', file_path= ?,file_name=?, end_date= sysdate where flow_id = ?",
				paraList);
		dbOper.commitTransaction();
		//System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
	}
	

}
