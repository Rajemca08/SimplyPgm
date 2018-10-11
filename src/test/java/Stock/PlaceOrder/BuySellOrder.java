package Stock.PlaceOrder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import BaseFunc.Base;
import CSVReport.CSVReport;
import ExceptionPack.MyException;
import FiboMailTrigger.FiboDetails;
import Mailer.EmailUtil;

public class BuySellOrder extends Exception {

	public HashMap<String, Integer> hdrMap = new HashMap<String, Integer>();
	public Properties MailProp = new Properties();
	public Properties InputProp = new Properties();
	public Properties FiboProp = new Properties();
	public Properties IntraInputProp = new Properties();
	public WebDriver driver = null;
	public HashMap<String, String> MailHistory = new HashMap<String, String>();
	public String OrderDetails = null;
	public String MobileNumber = null;
	public static File FiboInput = null;

	/*** Column Values ***/
	public String Stockname = null;
	public Float open = 0.00f;
	public Float ltp = 0.0f;
	public Float profit = 0.0f;
	public long totbuyqnty = 0;
	public long totsellqnty = 0;
	public Float high = 0.0f;
	public Float low = 0.0f;
	public Float close = 0.0f;
	public Float sellprice = 0.0f;
	public Float buyprice = 0.0f;
	public Float changePer = 0.0f;

	public Float Ylow = 0.0f;
	public Float YHigh = 0.0f;
	public String Stockkey = null;

	/*** InputFrom Sheet and Manipulation ***/
	public int Qnty = 0;
	public Float SLPrice = 0.0f;
	public Float SLTPrice = 0.0f;
	public Float SL = 0.00f;
	public Float TSL = 0.00f;
	public Float limit = 0.0f;
	public int SLQnty = 0;
	public int volume = 0;
	public int Orderlimit = 0;
	public int ExtraProfit = 0;
	public String TableName = null;
	public String OrderType = null;
	public String OrderStatus = null;
	public long OdrID = 0;
	public long SLOdrID = 0;
	public String SLOdrIDStatus = null;
	public String ActionMsg = null;
	public CSVReport Report = CSVReport.getInstance();

	// public FiboCSVReport Report1 =FiboCSVReport.getInstance();
	public void clearFileds() {
		System.out.println("\n*** Clearing Fileds ***\n");

		ActionMsg = "Clearing Fields";

		Stockname = null;
		open = 0.0f;
		ltp = 0.0f;
		high = 0.0f;
		low = 0.0f;
		close = 0.0f;
		sellprice = 0.0f;
		buyprice = 0.0f;
		changePer = 0.00f;
		profit = 0.0f;
		totbuyqnty = 0;
		totsellqnty = 0;
		volume = 0;
		SLPrice = 0.0f;
		SLTPrice = 0.0f;
		ExtraProfit = 0;
		SLQnty = 0;

		OrderType = null;
		OrderStatus = null;
		OdrID = 0;

		SLOdrID = 0;
		SLOdrIDStatus = null;
	}

	public BuySellOrder() throws Exception {
		try {
			ActionMsg = "Constructor => Intializing Properties file";
			MailProp.load(new FileInputStream("C://PlaceOrderBot//Credentials.properties"));
			System.setProperty("webdriver.chrome.driver", MailProp.getProperty("chromeExe"));
			FiboInput = new File(MailProp.getProperty("FiboInputFile"));
			FiboProp.load(new FileInputStream(FiboInput.getPath()));
			ChromeOptions options = new ChromeOptions();
			options.addArguments("disable-infobars");
			driver = new ChromeDriver(options);

		} catch (Exception e) {
			throw e;
		}
	}

	public BuySellOrder(String Values) throws Exception {
		try {
			ActionMsg = "Constructor => Intializing Properties file";

			MailProp.load(new FileInputStream("C://PlaceOrderBot//Credentials.properties"));
			DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
			Date dateobj = new Date();
			String currentDate = df.format(dateobj);
			currentDate = currentDate.split(" ")[0].replaceAll("/", "_");
			// FiboInput = new
			// File("C://PlaceOrderBot//FiboInput//FiboInput"+currentDate+".properties");
			FiboInput = new File(MailProp.getProperty("FiboInputFile"));
			if (!FiboInput.exists())
				FiboInput.createNewFile();

			FiboProp.load(new FileInputStream(FiboInput.getPath()));
			System.setProperty("webdriver.chrome.driver", MailProp.getProperty("chromeExe"));
			ChromeOptions options = new ChromeOptions();
			options.addArguments("disable-infobars");
			driver = new ChromeDriver(options);

		} catch (Exception e) {
			throw e;
		}
	}

	public void profitBooking() throws Exception {
		try {

			ActionMsg = "Profit Booking " + Stockname;

			if (Orders.getStockOrderdList().contains(Stockname) && ltp > (open + limit + profit)
					&& buyprice > (open + (limit + profit))) {

				// SellOrder();
				OrderDetails = Stockname + ",P-Sold," + Qnty + "," + ltp + "," + "" + "," + low + "," + open + ","
						+ high + "," + close + "," + getCurrentTime() + ",";
				Report.addOrderDetailsInCSV(OrderDetails);
				sendAlertMail(Stockname + "=>P-Sold @" + buyprice + " SL @" + SLPrice + ":Ltp@" + ltp + ":L@" + low
						+ ":O@" + open + ":H@" + high);

			}
			if (Orders.getStockOrderdList().contains(Stockname) && ltp < (open - (limit + profit))
					&& sellprice < (open - (limit + profit))) {

				// buyOrder();
				OrderDetails = Stockname + ",P-Bought," + Qnty + "," + ltp + "," + "" + "," + low + "," + open + ","
						+ high + "," + close + "," + getCurrentTime() + ",";
				Report.addOrderDetailsInCSV(OrderDetails);
				sendAlertMail(Stockname + "=>P-Bought @" + sellprice + " SL @" + SLPrice + ":Ltp@" + ltp + ":L@" + low
						+ ":O@" + open + ":H@" + high);

			}
		} catch (Exception e) {
			throw e;
		}
	}

	public void getRowValue(int i) throws Exception {
		try {
			ActionMsg = "Get  Value of Row Nbr => " + String.valueOf(i);
			System.out.println("\n*** Reading the values from site Row Number: " + String.valueOf(i) + "\n");

			getElement(driver, By.xpath("(//table[@id='tbl_" + TableName + "']/tbody/tr)[" + i + "]")).click();
			String rowValues = getElement(driver,
					By.xpath("(//table[@id='tbl_" + TableName + "']/tbody/tr)[" + i + "]")).getText();
			Stockname = rowValues.split("\\n")[0].trim();
			if (Stockname.length() > 25) {
				Stockname = Stockname.substring(0, 23);

			}
			Stockkey = Stockname.replace(" ", "").trim();
			String[] priceDetails = (rowValues.split("\\n")[1].replaceAll(",", "")).trim().split(" ");
			ltp = Float.valueOf(priceDetails[hdrMap.get("LTP") - 3]);
			buyprice = Float.valueOf(priceDetails[hdrMap.get("BuyPrice") - 3]);
			sellprice = Float.valueOf(priceDetails[hdrMap.get("SellPrice") - 3]);
			low = Float.valueOf(priceDetails[hdrMap.get("Low") - 3]);
			open = Float.valueOf(priceDetails[hdrMap.get("Open") - 3]);
			high = Float.valueOf(priceDetails[hdrMap.get("High") - 3]);
			close = Float.valueOf(priceDetails[hdrMap.get("Close") - 3]);
			volume = Integer.valueOf(priceDetails[hdrMap.get("Volume") - 3]);
			changePer = Float.valueOf(priceDetails[hdrMap.get("%Change") - 3]);
			totbuyqnty = Integer.valueOf(priceDetails[hdrMap.get("TotalBuyQty") - 3]);// needtoupdate
			totsellqnty = Integer.valueOf(priceDetails[hdrMap.get("TotalSellQty") - 3]);

		} catch (Exception e) {
			throw e;
		}

	}

	public void getRowValueByStockname(String strStockname) throws Exception {
		try {
			ActionMsg = " Get Value of Row Nbr => " + String.valueOf(strStockname);
			System.out.println("\n*** Reading the values from site Row Number: " + String.valueOf(strStockname) + "\n");
			String StockRow = "//table[@id='tbl_" + TableName
					+ "']/tbody/descendant::td[contains(@class,'companyName')and contains(text(),'" + strStockname
					+ "')]/..";

			getElement(driver, By.xpath(StockRow)).click();
			String rowValues = getElement(driver, By.xpath(StockRow)).getText();

			String[] priceDetails = (rowValues.split("\\n")[1].replaceAll(",", "")).trim().split(" ");
			ltp = Float.valueOf(priceDetails[hdrMap.get("LTP") - 3]);
			buyprice = Float.valueOf(priceDetails[hdrMap.get("BuyPrice") - 3]);
			sellprice = Float.valueOf(priceDetails[hdrMap.get("SellPrice") - 3]);
			low = Float.valueOf(priceDetails[hdrMap.get("Low") - 3]);
			open = Float.valueOf(priceDetails[hdrMap.get("Open") - 3]);
			high = Float.valueOf(priceDetails[hdrMap.get("High") - 3]);
			close = Float.valueOf(priceDetails[hdrMap.get("Close") - 3]);
			volume = Integer.valueOf(priceDetails[hdrMap.get("Volume") - 3]);
			changePer = Float.valueOf(priceDetails[hdrMap.get("%Change") - 3]);
			totbuyqnty = Integer.valueOf(priceDetails[hdrMap.get("TotalBuyQty") - 3]);// needtoupdate
			totsellqnty = Integer.valueOf(priceDetails[hdrMap.get("TotalSellQty") - 3]);

		} catch (Exception e) {
			throw e;
		}

	}

	public int getRowSize() throws Exception {
		try {
			ActionMsg = "Get Row Size of the Watch List => " + TableName;
			List<WebElement> elementList = driver
					.findElements(By.xpath("//table[@id='tbl_" + TableName + "']/tbody/tr"));
			System.out.println("Row Size :" + elementList.size());
			if (elementList.size() == 0) {

				throw new MyException("Zero Row Watch List Exception");
			}
			return elementList.size();
		} catch (MyException e) {
			throw e;
		}
	}

	public void readInputFile() throws Exception {
		try {
			ActionMsg = "Reading Input.Properties file";
			ActionMsg = "Reading IntraOrderInputs.Properties file";
			InputProp.clear();
			InputProp.load(new FileInputStream("C://PlaceOrderBot//Inputs.properties"));
			Qnty = Integer.valueOf(InputProp.get("Qnty").toString());
			SLQnty = Integer.valueOf(InputProp.get("SLQnty").toString());
			SL = Float.valueOf(InputProp.get("SL").toString());
			TSL = Float.valueOf(InputProp.get("TriggerSL").toString());
			Orderlimit = Integer.valueOf(InputProp.get("OrderLimit").toString());
			ExtraProfit = Integer.valueOf(InputProp.get("ExtraProfit").toString());

			IntraInputProp.clear();
			IntraInputProp.load(new FileInputStream("C://PlaceOrderBot//IntraOrderInputs.properties"));

		} catch (Exception e) {
			throw e;
		}
	}

	public boolean BuySellPlaceOrder() throws Exception {
		
		//SMSAPIJAVA.sendMsg("TestMsg", MobileNumber);
		
		/** Buy & Sell the product based on Fibo AVG **/
		FiboDetails fibo = FiboDetails.FiboMap.get(Stockkey);
		if (!Orders.MapOrders.containsKey("BuyOrder"+Stockkey)) {
			if ((ltp >= fibo.BuyAbove && sellprice >= fibo.BuyAbove && ltp<fibo.BuyAbove+limit && open < fibo.BuyAbove && open > fibo.SellBelow)) {

				OrderDetails = Stockname + ",BuyOrder," + Qnty + "," + sellprice + "," + SLPrice + "," + low + ","
						+ open + "," + high + "," + close + "," + getCurrentTime() + ",";
				Report.addFiboOrderDetailsInCSV(OrderDetails);
				OrderType = "BuyOrder";
				Orders.MapOrders.put("BuyOrder"+Stockkey, new Orders(this));
				
			}
		} else if (Orders.MapOrders.containsKey("BuyOrder" + Stockkey)) {			
			
			Orders ordr =Orders.MapOrders.get("BuyOrder" + Stockkey);
			if(!(ordr.OrderStatus.equals("Closed"))){
					if (ordr.StockName == Stockname && ltp < fibo.SellBelow && ordr.OrderType.contains("BuyOrder")) {
	
						// Need to add code for verify the order status in the order book using order id.
						String OrderDetails = Stockname + ",BuySLTriggered," + Qnty + "," + ltp + "," + "" + "," + low + ","
								+ open + "," + high + "," + close + "," + getCurrentTime() + ",";
						Report.addOrderDetailsInCSV(OrderDetails);
						ordr.OrderStatus = "Closed";
					}	
			} else {
				if (ordr.StockName == Stockname && ltp > fibo.BuyTGT1 && ordr.OrderType.contains("BuyOrder")) {

					// Need to add code for verify the order status in the order book using order id.
					String OrderDetails = Stockname + ",BuyTargetAchieved," + Qnty + "," + ltp + "," + "" + "," + low
							+ "," + open + "," + high + "," + close + "," + getCurrentTime() + ",";
					Report.addOrderDetailsInCSV(OrderDetails);
					ordr.OrderStatus = "Closed";

				}
			}
				

		}
		/**Sell the product based on Fibo AVG **/
		if (!Orders.MapOrders.containsKey("SellOrder" + Stockkey)) {
			if (ltp <= fibo.SellBelow && buyprice <= fibo.SellBelow && open > fibo.SellBelow && open < fibo.BuyAbove
					&& ltp > fibo.SellBelow - limit) {

				String OrderDetails = Stockname + ",SellOrder," + Qnty + "," + buyprice + "," + SLPrice + "," + low
						+ "," + open + "," + high + "," + close + "," + getCurrentTime() + ",";
				Report.addFiboOrderDetailsInCSV(OrderDetails);
				OrderType = "SellOrder";
				Orders.MapOrders.put("SellOrder" + Stockkey, new Orders(this));

			}
		} else if (Orders.MapOrders.containsKey("SellOrder" + Stockkey)) {

			Orders ordr = Orders.MapOrders.get("BuyOrder" + Stockkey);
			/*** Checking Stop Loss Conditions and Profit Booking Condition ***/
			if (!ordr.OrderStatus.equals("Closed")) {
				if (ordr.StockName == Stockname && ltp > fibo.BuyAbove && ordr.OrderType.contains("SellOrder")) {

					// Need to add code for verify the order status in the order book using order id.
					OrderDetails = Stockname + ",SellSLTriggered," + Qnty + "," + ltp + "," + "" + "," + low + ","
							+ open + "," + high + "," + close + "," + getCurrentTime() + ",";
					Report.addOrderDetailsInCSV(OrderDetails);
					ordr.OrderStatus = "Closed";

				} else {
					if (ordr.StockName == Stockname && ltp < fibo.SellTGT1 && ordr.OrderType.contains("SellOrder")) {

						// Need to add code for verify the order status in the order book using order id.
						OrderDetails = Stockname + ",SellTargetAchieved," + Qnty + "," + ltp + "," + "" + "," + low
								+ "," + open + "," + high + "," + close + "," + getCurrentTime() + ",";
						Report.addOrderDetailsInCSV(OrderDetails);
						ordr.OrderStatus = "Closed";

					}

				}
			}
		}
		return true;	
	}

	public boolean logIntoApp() throws Exception {
		try {
			MobileNumber = MailProp.getProperty("MobileNumber");
			ActionMsg = "log Into Application";
			TableName = MailProp.getProperty("TableName");
			driver.get("https://trade.angelbroking.com");
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			driver.manage().timeouts().pageLoadTimeout(10000, TimeUnit.MILLISECONDS);
			driver.manage().window().fullscreen();
			// driver.manage().window().maximize();
			getElement(driver, By.id("txtUserID")).sendKeys(MailProp.getProperty("TradeUserID"));
			getElement(driver, By.id("txtTradingPassword")).sendKeys(MailProp.getProperty("TradePassword"));
			getElement(driver, By.id("toggleBox")).click();
			getElement(driver, By.xpath("//span[text()='Trading']")).click();
			getElement(driver, By.id("loginBtn")).click();
			getElement(driver,
					By.xpath(
							"//table[@id='tbl_" + TableName + "']/tbody/tr[1]/descendant::td[contains(@id,'ltrate_')]"))
									.isDisplayed();

			Thread.sleep(3000);
			List<WebElement> ele = driver
					.findElements(By.xpath("//*[@class='dataTables_scrollHeadInner']/descendant::th/span"));
			int j = 1;
			for (WebElement e : ele) {
				Actions actions = new Actions(driver);
				actions.moveToElement(e);
				actions.perform();
				hdrMap.put(e.getText().replace(" ", "").trim(), j);
				j++;
			}
			System.out.println(hdrMap);
		} catch (Exception e) {
			throw e;
		}
		return true;

	}

	public void buyOrder() throws Exception {

		try {
			ActionMsg = "buyOrder Script Name " + Stockname;

			getElement(driver, By.xpath("//a[@class='fR blue_btn' and text()='Buy']")).click();// Click
			getElement(driver, By.xpath("//*[@id='divProductType']/div[2]/span")).click();// Click
			getElement(driver, By.xpath("//*[@id='ddlProductType']/li[2]/a")).click();// Click
			WebElement myLink = driver.findElement(By.xpath("//*[@id='btnOk_Alert']"));
			if (myLink.isDisplayed()) {
				myLink.click();
			}
			getElement(driver, By.xpath("//*[@id='txtQuantity']")).sendKeys(String.valueOf(Qnty));
			getElement(driver, By.xpath("//*[@id='txtPrice']")).sendKeys(String.valueOf(sellprice));
			getElement(driver, By.xpath("//*[@id='btnSubmitOrder']")).click();
			getElement(driver, By.xpath("//*[@id='btnConfirm']")).click();
			getElement(driver, By.id("btnOk_Confirm")).click();
			getElement(driver, By.xpath("//a[@title='" + TableName + "']"));
			getElement(driver, By.xpath("//a[@title='" + TableName + "']")).click();

			OdrID = 0;
			OrderStatus = null;
		} catch (Exception e) {
			throw e;
		}
	}

	public void SellOrder() throws Exception {
		try {
			ActionMsg = "SellOrder Script Name " + Stockname;
			getElement(driver, By.xpath("//a[@class='fR red_btn' and text()='sell']")).click();// ClickBuyButton
			getElement(driver, By.xpath("//*[@id='divProductType']/div[2]/span")).click();// Click
			getElement(driver, By.xpath("//*[@id='ddlProductType']/li[2]/a")).click();// Click
			WebElement myLink = driver.findElement(By.xpath("//*[@id='btnOk_Alert']"));
			if (myLink.isDisplayed()) {
				myLink.click();
			}
			getElement(driver, By.xpath("//*[@id='txtQuantity']")).sendKeys(String.valueOf(Qnty));
			getElement(driver, By.xpath("//*[@id='txtPrice']")).sendKeys(String.valueOf(buyprice));
			getElement(driver, By.xpath("//*[@id='btnSubmitOrder']")).click();
			getElement(driver, By.xpath("//*[@id='btnConfirm']")).click();
			getElement(driver, By.id("btnOk_Confirm")).click();
			getElement(driver, By.xpath("//a[@title='" + TableName + "']"));
			getElement(driver, By.xpath("//a[@title='" + TableName + "']")).click();
		} catch (Exception e) {
			throw e;
		}
	}

	public void SLForBuyOrder() throws Exception {
		try {

			ActionMsg = "SLForBuyOrder Script Name " + Stockname;
			getElement(driver, By.xpath("//a[@class='fR red_btn' and text()='sell']")).click();
			getElement(driver, By.xpath("//*[@id='divddlMainPriceType']/div/span")).click();// ClickSLList
			getElement(driver, By.xpath("//*[@id='ddlMainPriceType']/li[2]/a")).click();// Click
			getElement(driver, By.xpath("//*[@id='divProductType']/div[2]/span")).click();// ClickProductTypelist
			getElement(driver, By.xpath("//*[@id='ddlProductType']/li[2]/a")).click();// ClickProductType
			WebElement myLink = driver.findElement(By.xpath("//*[@id='btnOk_Alert']"));
			if (myLink.isDisplayed()) {
				myLink.click();
			}
			getElement(driver, By.xpath("//*[@id='txtQuantity']")).sendKeys(String.valueOf(SLQnty));
			getElement(driver, By.xpath("//*[@id='txtPrice']")).sendKeys(String.valueOf(SLPrice));
			getElement(driver, By.xpath("//*[@id='txtTrigPrice']")).sendKeys(String.valueOf(SLTPrice));
			getElement(driver, By.xpath("//*[@id='btnSubmitOrder']")).click();
			getElement(driver, By.xpath("//*[@id='btnConfirm']")).click();
			getElement(driver, By.id("btnOk_Confirm")).click();
			getElement(driver, By.xpath("//a[@title='" + TableName + "']"));
			getElement(driver, By.xpath("//a[@title='" + TableName + "']")).click();
			SLOdrID = 0;
			SLOdrIDStatus = null;

		} catch (Exception e) {
			throw e;
		}
	}

	public void SLForSellOrder() throws Exception {

		try {

			getElement(driver, By.xpath("//a[@class='fR blue_btn' and text()='Buy']")).click();// ClickBuyButton
			getElement(driver, By.xpath("//*[@id='divddlMainPriceType']/div/span")).click();// ClickSLList
			getElement(driver, By.xpath("//*[@id='ddlMainPriceType']/li[2]/a")).click();// ClickSL
			getElement(driver, By.xpath("//*[@id='divProductType']/div[2]/span")).click();// ClickProductTypelist
			getElement(driver, By.xpath("//*[@id='ddlProductType']/li[2]/a")).click();// ClickProductType
			WebElement myLink = driver.findElement(By.xpath("//*[@id='btnOk_Alert']"));
			if (myLink.isDisplayed()) {
				myLink.click();
			}
			getElement(driver, By.xpath("//*[@id='txtQuantity']")).sendKeys(String.valueOf(SLQnty));
			getElement(driver, By.xpath("//*[@id='txtPrice']")).sendKeys(String.valueOf(SLPrice));
			getElement(driver, By.xpath("//*[@id='txtTrigPrice']")).sendKeys(String.valueOf(SLTPrice));
			getElement(driver, By.xpath("//*[@id='btnSubmitOrder']")).click();
			getElement(driver, By.xpath("//*[@id='btnConfirm']")).click();
			getElement(driver, By.id("btnOk_Confirm")).click();
			getElement(driver, By.xpath("//a[@title='" + TableName + "']"));
			getElement(driver, By.xpath("//a[@title='" + TableName + "']")).click();
			SLOdrID = 0;
			SLOdrIDStatus = null;

		} catch (Exception e) {
			throw e;
		}
	}

	public WebElement getElement(WebDriver driver, By Locators) throws Exception {

		WebElement element = null;
		try {
			WebDriverWait wait = new WebDriverWait(driver, 15);
			for (int i = 0; i <= 3; i++)
				try {
					element = wait.until(ExpectedConditions.elementToBeClickable(Locators));
				} catch (Exception e) {
					try {
						element = wait.until(ExpectedConditions.elementToBeClickable(Locators));
					} catch (Exception e1) {

					}
				}
		} catch (Exception e) {
			throw e;
		}
		return element;
	}

	public void sendAlertMail(String strSubjectMessage) throws FileNotFoundException, IOException {

		Properties MailProp = new Properties();
		MailProp.load(new FileInputStream("C://PlaceOrderBot//Credentials.properties"));
		final String fromEmail = MailProp.getProperty("fromEmail");
		final String password = MailProp.getProperty("password");
		final String toEmail = MailProp.getProperty("toEmail");

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Authenticator auth = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};
		Session session = Session.getDefaultInstance(props, auth);
		System.out.println(" Session created for message " + strSubjectMessage);
		EmailUtil.sendEmail(session, toEmail, strSubjectMessage, strSubjectMessage);

	}

	public String getCurrentTime() {

		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Date dateobj = new Date();
		String currentDate = df.format(dateobj);
		return currentDate;

	}

	public boolean getTimeDifferenceInMins(String OldTimeStamp) throws ParseException {

		try {
			DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
			Date dateobj = new Date();
			String NewTimeStamp = df.format(dateobj);
			Date OldTime = df.parse(OldTimeStamp);
			Date NewTime = df.parse(NewTimeStamp);
			long difference = (NewTime.getTime() - OldTime.getTime()) / 60000;
			System.out.println("Time Diff  " + difference);
			if (difference >= 15)
				return true;
			else
				return false;

		} catch (Exception e) {
			return false;
		}
	}

	public boolean cutoffTime(String OldTimeStamp) throws ParseException {
		try {
			DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
			Date dateobj = new Date();
			String NewTimeStamp = df.format(dateobj);
			Date OldTime = df.parse(OldTimeStamp);
			Date NewTime = df.parse(NewTimeStamp);
			long difference = (NewTime.getTime() - OldTime.getTime());
			if (difference > 0)
				difference = difference / 60000;
		} catch (Exception e) {

		}
		return true;
	}

	public boolean checkMarketTime() throws ParseException {
		try {

			String MarketStartTime = "09:10:00";
			String MarketEndTime = "15:30:36";
			DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
			Date dateobj = new Date();
			String NewTimeStamp = df.format(dateobj);
			MarketStartTime = NewTimeStamp.split(" ")[0].trim() + " " + MarketStartTime;
			MarketEndTime = NewTimeStamp.split(" ")[0].trim() + " " + MarketEndTime;
			Date MktStartTime = df.parse(MarketStartTime);
			Date CurrentTime = df.parse(NewTimeStamp);
			Date MktEndTime = df.parse(MarketEndTime);

			long difference1 = (CurrentTime.getTime() - MktStartTime.getTime());
			long difference2 = (CurrentTime.getTime() - MktEndTime.getTime());
			if (difference1 >= 0 && difference2 <= 0) {
				System.out.println("Market Time Started");
				return true;
			} else {
				System.out.println("Market Time Ended");
				return false;
			}

		} catch (Exception e) {
			return false;
		}
	}

	public float getLimitPrice() {

		if (open <= 175)
			limit = 0.50f;
		else if (open > 175 && open <= 300)
			limit = 0.90f;
		else if (open > 301 && open <= 450)
			limit = 1.50f;
		else if (open > 451 && open <= 750)
			limit = 2.50f;
		else if (open > 751 && open <= 1000)
			limit = 2.80f;
		else if (open > 1001 && open <= 1500)
			limit = 3.50f;
		else if (open > 1501 && open <= 2000)
			limit = 3.50f;
		else if (open > 2001 && open <= 2750)
			limit = 4.10f;
		else if (open > 2751 && open <= 3500)
			limit = 4.10f;
		else if (open > 3501)
			limit = 4.80f;

		// System.out.println("Limit " + limit);
		return limit;

	}

	public float getProfitPrice() {

		if (open <= 175)
			profit = 1.50f;
		else if (open > 176 && open <= 300)
			profit = 1.90f;
		else if (open > 301 && open <= 450)
			profit = 3.20f;
		else if (open > 451 && open <= 750)
			profit = 3.80f;
		else if (open > 751 && open <= 1000)
			profit = 4.20f;
		else if (open > 1001 && open <= 1500)
			profit = 4.50f;
		else if (open > 1501 && open <= 2000)
			profit = 5.50f;
		else if (open > 2001 && open <= 2750)
			profit = 6.40f;
		else if (open > 2751 && open <= 3500)
			profit = 6.40f;
		else if (open > 3501)
			profit = 8.20f;

		profit = profit + ExtraProfit;

		return profit;

	}

	public LinkedHashMap<String, Float> getFiboOutput() throws Exception {
		LinkedHashMap<String, Float> FiboOut = new LinkedHashMap<String, Float>();
		// open = ltp;
		Float Upb = Base.TruncateFloatValue((open + (open * 0.01f)));
		Float Upa = Base.TruncateFloatValue(open);
		Float Upc = Base.TruncateFloatValue((Upb - ((Upb - Upa) * 0.618f)));
		Float UpR1382 = Base.TruncateFloatValue((Upb - ((Upb - Upa) * 1.382f)));

		Float Dna = Base.TruncateFloatValue(open);
		Float Dnb = Base.TruncateFloatValue((open - (open * 0.01f)));
		Float Dnc = Base.TruncateFloatValue((Dnb + ((Dna - Dnb) * 0.618f)));
		Float DnR1382 = Base.TruncateFloatValue((Dnb + ((Dna - Dnb) * 1.382f)));

		Float UpExtn1382 = Base.TruncateFloatValue((Upc + ((Upb - Upa) * 1.382f)));
		Float UpExtn100 = Base.TruncateFloatValue((Upc + ((Upb - Upa) * 1f))); // Up100
																				// UpExtn100
		Float UpExtn382 = Base.TruncateFloatValue((Upc + ((Upb - Upa) * 0.382f)));
		Float UpExtn236 = Base.TruncateFloatValue((Upc + ((Upb - Upa) * 0.236f))); // Up100
																					// UpExt236
		Float UpRExtnavg = Base.TruncateFloatValue(((UpR1382 + UpExtn236) / 2)); // UpRExtnavg
		Float BuyTrig = Base.TruncateFloatValue(((open + UpExtn236) / 2));

		Float DnExtn236 = Base.TruncateFloatValue((Dnc - ((Dna - Dnb) * 0.236f))); // DnExtn236
		Float DnExtn382 = Base.TruncateFloatValue((Dnc - ((Dna - Dnb) * 0.382f)));
		Float DnExtn100 = Base.TruncateFloatValue((Dnc - ((Dna - Dnb) * 1))); // down
																				// DnExtn100
		Float DnExtn1382 = Base.TruncateFloatValue((Dnc - ((Dna - Dnb) * 1.382f)));
		Float DnRExtnavg = Base.TruncateFloatValue(((DnR1382 + DnExtn236) / 2)); // DnRExtnavg
		Float SellTrig = Base.TruncateFloatValue(((open + DnExtn236) / 2));

		FiboOut.put("SellBelow", DnExtn236);
		FiboOut.put("SellTGT1", DnExtn100);
		FiboOut.put("SellSL1", DnRExtnavg);
		FiboOut.put("SellSL2", UpExtn236);

		FiboOut.put("BuySL2", DnExtn236);
		FiboOut.put("BuySL1", UpRExtnavg);
		FiboOut.put("BuyAbove", UpExtn236);
		FiboOut.put("BuyTGT1", UpExtn100);

		return FiboOut;

	}

	public void gettotQntyOutput() throws Exception {

		if (((totbuyqnty * 5) <= totsellqnty) && totbuyqnty != 0 && totsellqnty != 0) {

			sendAlertMail(Stockname + "=> ( " + changePer + ") TotalBuyQnty @" + totbuyqnty + " TotalSellQnty @"
					+ totsellqnty);
		}

		if ((totbuyqnty >= (totsellqnty * 5)) && totbuyqnty != 0 && totsellqnty != 0) {

			sendAlertMail(Stockname + "=> ( " + changePer + ") TotalBuyQnty @" + totbuyqnty + " TotalSellQnty @"
					+ totsellqnty);
		}
	}

	public void getChangePerOutput() throws Exception {

		if ((changePer > 1.8 && changePer < 1.85) || (changePer > 2.8 && changePer < 2.9)
				|| (changePer > 5.0 && changePer < 5.1) || (changePer > 6.8 && changePer < 7)
				|| (changePer > 9 && changePer < 9.2) || (changePer > 13 && changePer < 13.2)
				|| (changePer > 15 && changePer < 15.2) || (changePer > 30 && changePer < 34)
				|| (changePer > 40 && changePer < 44)) {

			sendAlertMail(Stockname + "=> (" + changePer + "%) LTP @" + ltp + " low @" + low + " Open @" + open
					+ " High @" + high);
		}

		if ((changePer < -1.8 && changePer > -1.85) || (changePer < -2.8 && changePer > -2.9)
				|| (changePer < -5.0 && changePer > -5.1) || (changePer < -6.8 && changePer > -7)
				|| (changePer < -9 && changePer > -9.2) || (changePer < -13 && changePer > -13.2)
				|| (changePer < -15 && changePer > -15.2) || (changePer < -30 && changePer > -34)
				|| (changePer < -40 && changePer > -44)) {

			sendAlertMail(Stockname + "=> (" + changePer + "%) LTP @" + ltp + " low @" + low + " Open @" + open
					+ " High @" + high);
		}
	}

	public Float getRoudOfFloat(Float fl) {
		String valw = String.valueOf(fl);
		String values[] = valw.split("\\.");
		String price = values[0] + ".95";// +values[1].toCharArray()[0]+"5";

		return Float.valueOf(price);

	}

	public Float getRoudOfToFive(Float fl) {
		String valw = String.valueOf(fl);
		String values[] = valw.split("\\.");
		String price = values[0] + "." + values[1].toCharArray()[0] + "0";

		return Float.valueOf(price);

	}

	public void writelog() {

		Logger logger = Logger.getLogger("MyLog");
		FileHandler fh;

		try {

			// This block configure the logger with handler and formatter
			fh = new FileHandler("C://PlaceOrderBot//MyLogFile.log");
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);

			// the following statement is used to log any messages
			logger.info("My first log");

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void AddFiboDetailsIntoProp(BuySellOrder obj) throws IOException {

		String value = String.valueOf(obj.low) + "," + String.valueOf(obj.high);
		obj.FiboProp.put(obj.Stockkey, value);
		OutputStream out = new FileOutputStream(FiboInput);
		obj.FiboProp.store(out, null);
	}

	public LinkedHashMap<String, Float> getAccurateFibo() throws Exception {

		Float FiboAvgPrice = 0.0f;

		LinkedHashMap<String, Float> FiboAccurate = new LinkedHashMap<String, Float>();
		Ylow = Float.valueOf(FiboProp.get(Stockkey).toString().split(",")[0]);
		YHigh = Float.valueOf(FiboProp.get(Stockkey).toString().split(",")[1]);

		FiboAvgPrice = (open + close + Ylow + YHigh) / 4;

		Float Upb = Base.TruncateFloatValue((open + (open * 0.01f)));
		Float Upa = Base.TruncateFloatValue(open);
		Float Upc = Base.TruncateFloatValue((Upb - ((Upb - Upa) * 0.618f)));
		Float UpR1382 = Base.TruncateFloatValue((Upb - ((Upb - Upa) * 1.382f)));

		Float Dna = Base.TruncateFloatValue(open);
		Float Dnb = Base.TruncateFloatValue((open - (open * 0.01f)));
		Float Dnc = Base.TruncateFloatValue((Dnb + ((Dna - Dnb) * 0.618f)));
		Float DnR1382 = Base.TruncateFloatValue((Dnb + ((Dna - Dnb) * 1.382f)));

		Float UpExtn1382 = Base.TruncateFloatValue((Upc + ((Upb - Upa) * 1.382f)));
		Float UpExtn100 = Base.TruncateFloatValue((Upc + ((Upb - Upa) * 1f))); // Up100
																				// UpExtn100
		Float UpExtn382 = Base.TruncateFloatValue((Upc + ((Upb - Upa) * 0.382f)));
		Float UpExtn236 = Base.TruncateFloatValue((Upc + ((Upb - Upa) * 0.236f))); // Up100
																					// UpExt236
		Float UpRExtnavg = Base.TruncateFloatValue(((UpR1382 + UpExtn236) / 2)); // UpRExtnavg
		Float BuyTrig = Base.TruncateFloatValue(((open + UpExtn236) / 2));

		Float DnExtn236 = Base.TruncateFloatValue((Dnc - ((Dna - Dnb) * 0.236f))); // DnExtn236
		Float DnExtn382 = Base.TruncateFloatValue((Dnc - ((Dna - Dnb) * 0.382f)));
		Float DnExtn100 = Base.TruncateFloatValue((Dnc - ((Dna - Dnb) * 1))); // down
																				// DnExtn100
		Float DnExtn1382 = Base.TruncateFloatValue((Dnc - ((Dna - Dnb) * 1.382f)));
		Float DnRExtnavg = Base.TruncateFloatValue(((DnR1382 + DnExtn236) / 2)); // DnRExtnavg
		Float SellTrig = Base.TruncateFloatValue(((open + DnExtn236) / 2));

		FiboAccurate.put("SellBelow", DnExtn236);
		FiboAccurate.put("SellTGT1", DnExtn100);
		FiboAccurate.put("SellSL1", DnRExtnavg);
		FiboAccurate.put("SellSL2", UpExtn236);

		FiboAccurate.put("BuySL2", DnExtn236);
		FiboAccurate.put("BuySL1", UpRExtnavg);
		FiboAccurate.put("BuyAbove", UpExtn236);
		FiboAccurate.put("BuyTGT1", UpExtn100);

		return FiboAccurate;

	}

}
