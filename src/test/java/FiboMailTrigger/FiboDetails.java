package FiboMailTrigger;

import java.util.HashMap;
import java.util.LinkedHashMap;
import Stock.PlaceOrder.BuySellOrder;


public class FiboDetails {

	public static HashMap<String, FiboDetails> FiboMap = new HashMap<String, FiboDetails>();
	public String Stockkey = null;
	public String StockName = null;
	public Float SellTGT1 = 0.0f;
	public Float SellBelow = 0.0f;
	public Float SellSL1 = 0.0f;
	public Float SellSL2 = 0.0f;
	public Float BuySL2 = 0.0f;
	public Float BuySL1 = 0.0f;
	public Float BuyAbove = 0.0f;
	public Float BuyTGT1 = 0.0f;

	public FiboDetails(BuySellOrder details, LinkedHashMap<String, Float> target) {
		
		Stockkey = details.Stockkey;
		SellTGT1 = target.get("SellTGT1");
		SellBelow = target.get("SellBelow");
		SellSL1 = target.get("SellSL1");
		SellSL2 = target.get("SellSL2");
		BuySL2 = target.get("BuySL2");
		BuySL1 = target.get("BuySL1");
		BuyAbove = target.get("BuyAbove");
		BuyTGT1 = target.get("BuyTGT1");

	}
	public static void main(String[] args) throws Exception {
		BuySellOrder order = null;
		int exIter = 1;
		long iter = 1;
		while (true) {
			try {
				order = new BuySellOrder("FiboInputCreation");
				order.readInputFile();
				if (order.logIntoApp()) {
					while (true) {
						order.readInputFile();
						if (iter == 1 || (iter % 10) == 0) {
							for (int i = 1; i <= order.getRowSize(); i++) {
								order.clearFileds();
								order.getRowValue(i);
								if (iter == 1)
									order.AddFiboDetailsIntoProp(order);
								else if (iter > 1) {
									order.driver.quit();
									System.exit(0);
								}
							
							}
						}
						iter++;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				order.driver.quit();
				String message = order.ActionMsg + " -> Failed  Iterations :" + exIter + " is failed "
						+ e.getMessage();
				order.sendAlertMail(message);
				exIter = exIter + 1;
				if (exIter > 7) {
					System.exit(0);
				}
			}
		}
	}

}
