package Stock.PlaceOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Orders {

	public String StockName = null;
	public String StockKey = null;
	public String Symbol = null;
	public Float BuyPrice = 0.0f;
	public Float SellPrice = 0.0f;
	public String TimeStamp = null;
	public int Qnty = 0;
	public String BuyOdrID = null;
	public String SellOdrID = null;	
	public String SLOdrID = null;
	HashMap<String,OrderBook> Book = null;
	public statusType OrderStaus = null;
	public orderType OrderType  = null;
	//public static List<Orders> odrList = new ArrayList<Orders>();
	public static List<String> StockList = new ArrayList<String>();
	public static HashMap<String,Orders> MapOrders = new HashMap<String,Orders>();
	public enum statusType {Open ,OpenWithSL,Closed};
	public enum orderType {BuyOrder ,SellOrder};
	
	

	public Orders(BuySellOrder order) {

		OrderType = order.OrderType;
		BuyOdrID  = null;	
		SLOdrID   = null;
		SellOdrID = null;
		StockName = order.Stockname;
		StockKey  = order.Stockkey;
		BuyPrice  = order.buyprice;
		SellPrice = order.sellprice;		
		Qnty      = order.Qnty;
		StockList.add(OrderType.toString() + order.Stockname);	
		Book = new HashMap<String,OrderBook>();
		OrderStaus = null;
	}

	public static List<String> getStockOrderdList() {
		return Orders.StockList;

	}
	


}
