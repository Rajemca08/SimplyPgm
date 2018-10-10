package FiboMailTrigger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import com.opencsv.CSVWriter;

import Stock.PlaceOrder.BuySellOrder;

public class FiboCSVReport {

	public File file = null;
    public static FiboCSVReport csvReport = null; 
	

	public static FiboCSVReport getInstance() throws Exception {
		if (csvReport == null) {
			csvReport = new FiboCSVReport();
		}
		return csvReport;

	}

	public FiboCSVReport() throws Exception {
		try {
			DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
			Date dateobj = new Date();
			String currentDate = df.format(dateobj);
			currentDate = currentDate.split(" ")[0].replaceAll("/", "_");
			file = new File("C://PlaceOrderBot//Reports//Report_Fibo_" + currentDate + ".csv");
			if (!file.exists()) {
				file.createNewFile();
			}
			addHeaderForFiboCSV();
			
			

		} catch (Exception e) {
			throw e;
		}
	}
	
	/* Add Headers for Newly Created CSV File */
	public void addHeaderForFiboCSV() throws Exception {

		boolean value = false;
		try {
			FileWriter outputfile = new FileWriter(file);
			CSVWriter writer = new CSVWriter(outputfile, ',', CSVWriter.NO_QUOTE_CHARACTER,
					CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);

			List<String[]> data = new ArrayList<String[]>();
			data.add(new String[] { "StockName", "TGT1","SellBelow","SellSL1","SellSL2","BuySL2","BuySL1","BuyAbove","TGT1","LOW","OPEN","HIGH","CLOSE","LTP"});
			writer.writeAll(data);
			
			writer.close();

		} catch (Exception e) {
			throw e;

		}
	}
	public void addFiboDetailsInCSV( BuySellOrder obj ,LinkedHashMap<String, Float> FiboOut) throws Exception {
		FileWriter pw = new FileWriter(file,true);
		try {
			String data = obj.Stockname+"," +FiboOut.get("SellTGT1")+","+FiboOut.get("SellBelow")+","+FiboOut.get("SellSL1")+","+FiboOut.get("SellSL2")+
					","+FiboOut.get("BuySL2")+","+FiboOut.get("BuySL1")+","+FiboOut.get("BuyAbove")+","+FiboOut.get("BuyTGT1")+","+obj.low+","+obj.open +","+obj.high+","+obj.close+","+obj.ltp;
			BufferedWriter bw = new BufferedWriter(pw);
			PrintWriter pw1 = new PrintWriter(bw);
			pw1.println();
			pw1.print(data);
			pw1.flush();
			pw1.close();
            bw.close();
            
      } catch (Exception e) {
			throw e;

		}
	}
	/*Add BuyOrSellOrders in the Report Sheet */
	
	public void addOrderDetailsInCSV( String data) throws Exception {
		FileWriter pw = new FileWriter(file,true);
		try {
			BufferedWriter bw = new BufferedWriter(pw);
			PrintWriter pw1 = new PrintWriter(bw);
			pw1.println();
			pw1.println(data);
			pw1.flush();
			pw1.close();
            bw.close();
            
		} catch (Exception e) {
			throw e;

		}
	}
	
	/*Add BuyOrSellOrders in the Report Sheet */
	
	public void updateBuyOrderInCSV(File file , String data) throws Exception {
		FileWriter pw = new FileWriter(file,true);
		try {

			BufferedWriter bw = new BufferedWriter(pw);
			PrintWriter pw1 = new PrintWriter(bw);
			pw1.println();
			pw1.println(data);
			pw1.flush();
			pw1.close();
            bw.close();
            
		} catch (Exception e) {
			throw e;

		}
	}
	
	
	/* Get current date and Time */
	public static String getCurrentDate() throws Exception {

		String currentDate = null;
		try {

			DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
			Date dateobj = new Date();
			currentDate = df.format(dateobj);

		} catch (Exception e) {
			throw e;
		}
		return currentDate;
	}

	/* Get current Time */
	public static String getCurrentTime() throws Exception {
		String currentTime = null;
		
		try {

			DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
			Date dateobj = new Date();
			currentTime = df.format(dateobj);
			
		} catch (Exception e) {
			throw e;
		}
		return currentTime.split(" ")[1];
	}
}
