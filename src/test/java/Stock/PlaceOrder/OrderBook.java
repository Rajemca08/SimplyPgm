package Stock.PlaceOrder;

import FiboMailTrigger.FiboCSVReport;
public class OrderBook {
	   String  Symbol             =  null;
	   String  ModeOfOrder        =  null;
	   String  OrderID            =  null;
	   String  OrderTime          =  null;
	   String  OrderType          =  null;
	   String  pendingQty         =  null;
	   String  OrderStatus        =  null;
	  
	OrderBook(){
		
		    Symbol             =  null;
		    ModeOfOrder        =  null;
		    OrderID            =  null;
		    OrderTime          =  null;
		    OrderType          =  null;
		    pendingQty         =  null;
		    OrderStatus        =  null;
		
	}

	public static void main(String[] args) throws Exception {
		BuySellOrder order = null;
		int exIter = 1;
		long iter = 1;
		FiboCSVReport fibo = FiboCSVReport.getInstance();
		while (true) {
			try {
				order = new BuySellOrder();
				order.logIntoApp();
				order.readInputFile();
				order.navigateToOrderBook();
				order.navigateToNSE();
				order.navigateToOrderBook();
				order.getOrderStatus("1000000006247258");
				order.CancelOrder("1000000006390634");								
				

			} catch (Exception e) {
				
		System.out.println(e);
				
			}
		}
	}

}
