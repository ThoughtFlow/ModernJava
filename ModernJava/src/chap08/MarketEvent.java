package chap08;

/**
 * This immutable class encapsulates the message originating from the stock market. It contains the stock symbol,
 * the type of trade (buy/sell), and the price.
 */
public class MarketEvent {

	private final String tickerSymbol;
	private final TradeType tradeType;
	private final double price;
	
	public static MarketEvent makeBuyEvent(String tickerSymbol, double price) {
		return new MarketEvent(tickerSymbol, TradeType.BUY, price);
	}
	
	public static MarketEvent makeSellEvent(String tickerSymbol, double price) {
		return new MarketEvent(tickerSymbol, TradeType.SELL, price);
	}
	
	private MarketEvent(String tickerSymbol, TradeType tradeType, double price) {
		this.tickerSymbol = tickerSymbol;
		this.tradeType = tradeType;
		this.price = price;
	}

	public String getTickerSymbol() {
		return tickerSymbol;
	}

	public TradeType getTradeType() {
		return tradeType;
	}

	public double getPrice() {
		return price;
	}
	
	public String toString() {
		return tradeType + ": " + tickerSymbol + "@" + price;
	}

	public enum TradeType {
		BUY,
		SELL;
	}
}