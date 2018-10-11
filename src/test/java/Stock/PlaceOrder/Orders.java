package Stock.PlaceOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Orders {

	public String OrderType = null;
	public String StockName = null;
	public String StockKey = null;
	public String Symbol = null;
	public String OrderStatus = null;
	public Float BuyPrice = 0.0f;
	public Float SellPrice = 0.0f;
	public Float SLPrice = 0.0f;
	public Float SLTPrice = 0.0f;
	public String TimeStamp = null;
	public int Qnty = 0;
	public long OdrID = 0;
	public long SLOdrID = 0;
	public String SLOdrIDStatus = null;
	
	//public static List<Orders> odrList = new ArrayList<Orders>();
	public static List<String> StockList = new ArrayList<String>();
	public static HashMap<String,Orders> MapOrders = new HashMap<String,Orders>();


	public Orders(BuySellOrder order) {

		OrderType = order.OrderType;
		OdrID = order.OdrID;		
		StockName = order.Stockname;
		StockKey  = order.Stockkey;
		Symbol = order.Stockname;
		OrderStatus = order.OrderStatus;
		BuyPrice = order.buyprice;
		SellPrice = order.sellprice;		
		Qnty = order.Qnty;
		
		SLOdrID = order.SLOdrID;
		SLOdrIDStatus = order.SLOdrIDStatus;
		SLPrice = order.SLPrice;
		SLTPrice = order.SLTPrice;
		StockList.add(OrderType + order.Stockname);		
	}

	public static List<String> getStockOrderdList() {
		return Orders.StockList;

	}
	


}
