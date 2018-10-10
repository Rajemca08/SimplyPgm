package FiboMailTrigger;

import java.util.HashMap;
import java.util.LinkedHashMap;

import Stock.PlaceOrder.BuySellOrder;

public class FiboMailAlert {
	public static void main(String[] args) throws Exception {
		LinkedHashMap<String, Float> FiboOut = new LinkedHashMap<String, Float> ();	
		BuySellOrder order = null;
		try {
			order = new BuySellOrder();
			if (order.logIntoApp()) {
			   for (int i = 1; i <= order.getRowSize(); i++) {
				   order.getRowValue(i);
				   FiboOut = order.getFiboOutput();
				   order.sendAlertMail("symbol :" + order.Stockname+" "+ FiboOut.toString());
			   }
			}

		} catch (Exception e) {
			
		}
	}

}
