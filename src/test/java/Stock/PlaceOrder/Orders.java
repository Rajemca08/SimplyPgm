package Stock.PlaceOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Orders {

	public String odrStatus = null;
	public String OrderType = null;
	public String StockName = null;
	public String StockKey = null;
	public String Symbol = null;
	public Float BuyPrice = 0.0f;
	public Float SellPrice = 0.0f;
	public Float SLPrice = 0.0f;
	public Float SLTPrice = 0.0f;
	public String TimeStamp = null;
	public int Qnty = 0;
	public String BuyOdrID = null;
	public String SellOdrID = null;	
	public String SLOdrID = null;
	public String SLOdrStatus = null;	
	public String BuyOdrStatus = null;
	public String SellOdrStatus = null;
	HashMap<String,OrderBook> Book = null;
	
	//public static List<Orders> odrList = new ArrayList<Orders>();
	public static List<String> StockList = new ArrayList<String>();
	public static HashMap<String,Orders> MapOrders = new HashMap<String,Orders>();
	


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
		StockList.add(OrderType + order.Stockname);	
		Book = new HashMap<String,OrderBook>();
		
	}

	public static List<String> getStockOrderdList() {
		return Orders.StockList;

	}
	


}
