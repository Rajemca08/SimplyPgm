package Stock.PlaceOrder;

import FiboMailTrigger.FiboCSVReport;
import FiboMailTrigger.FiboDetails;


public class Executor extends Exception {

	public static void main(String[] args) throws Exception {
		BuySellOrder order = null;
		int exIter = 1;
		long iter = 1;
		FiboCSVReport fibo = FiboCSVReport.getInstance();
		while (true) {
			try {
				order = new BuySellOrder();
				order.readInputFile();
				if (order.logIntoApp()) {
					while (true) {
						order.readInputFile();
						if (iter == 1 || (iter % 10) == 0) {
							for (int i = 1; i <= order.getRowSize(); i++) {
								order.clearFileds();
								order.getRowValue(i);
								if (iter == 1) {
									FiboDetails.FiboMap.put(order.Stockkey,	new FiboDetails(order, order.getAccurateFibo()));
									fibo.addFiboDetailsInCSV(order, order.getAccurateFibo());
								}
								 order.gettotQntyOutput();
								 order.getChangePerOutput();
								 order.getLimitPrice();
								 order.BuySellPlaceOrder();
							}
						} /*else if (Orders.getStockOrderdList().size() > 0) {
							try {
								for (Object str : Orders.StockList) {
									order.clearFileds();
									order.getRowValueByStockname((String) str);
									order.getLimitPrice();
									order.getProfitPrice();
									order.profitBooking();
								}
							} catch (Exception e) {

							}
						}*/
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
					order.sendAlertMail("Terminated the job since it is throwing exception 7 times.");
					System.exit(0);
				}
			}
		}
	}
}
