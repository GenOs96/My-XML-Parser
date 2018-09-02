import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import jxl.CellView;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


public class PrintAll {

	public static void XMLtoEXCEL(ArrayList<CosmosBean> bean) throws IOException, RowsExceededException, WriteException {
		
		File output = new File("/Users/sanjana/Desktop/myTest.xls");
		WritableWorkbook myExcel = Workbook.createWorkbook(output);
		
		WritableSheet mySheet = myExcel.createSheet("mySheet", 0);
		
		ArrayList<String> col = new ArrayList<String>();
		col.add("Field Name");
		col.add("From");
		col.add("mid");
		col.add("to");
		col.add("Logic");
		
		for(int i = 0; i < 5; i++ ) {
			
			// Auto - Size the cell 
			CellView view = new CellView();
			view.setAutosize(true);
			mySheet.setColumnView(i, view);
			
			WritableCellFormat format = new WritableCellFormat();
			
			//Configure cell background
			format.setBackground(Colour.GRAY_25);
			
			// Configure font 
			WritableFont font = new WritableFont(WritableFont.createFont("Arial"), 14, WritableFont.BOLD);
			format.setFont(font);
			
			mySheet.addCell(new Label(i, 0, col.get(i), format));
			
		}
		
		for(int i = 0; i < bean.size(); i++ ) {
			
			// Configure font 
			WritableCellFormat format = new WritableCellFormat();
			WritableFont font = new WritableFont(WritableFont.createFont("Arial"), 12);
			format.setFont(font);
			
			mySheet.addCell(new Label(0, i+1, bean.get(i).getField_name(), format)); 
			mySheet.addCell(new Label(1, i+1, bean.get(i).getFrom(), format)); 
			mySheet.addCell(new Label(2, i+1, bean.get(i).getMid(), format)); 
			mySheet.addCell(new Label(3, i+1, bean.get(i).getTo(), format)); 
			mySheet.addCell(new Label(4, i+1, bean.get(i).getLogic(), format)); 
			
		}
		
		myExcel.write();
		myExcel.close();
		System.out.println("finished");

	}

}
