package com.csc340.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
@RestController


public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	public static class Pagination {
		private int limit;
		private int offset;
		private int count;
		private int total;

		// Getters and setters
		public int getLimit() {
			return limit;
		}

		public void setLimit(int limit) {
			this.limit = limit;
		}

		public int getOffset() {
			return offset;
		}

		public void setOffset(int offset) {
			this.offset = offset;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public int getTotal() {
			return total;
		}

		public void setTotal(int total) {
			this.total = total;
		}
	}
	public static class StockData {
		private double open;
		private double high;
		private double low;
		private double close;
		private double volume;
		private String symbol;
		private String exchange;
		private String date;

		// Getters and setters
		public double getOpen() {
			return open;
		}

		public void setOpen(double open) {
			this.open = open;
		}

		public double getHigh() {
			return high;
		}

		public void setHigh(double high) {
			this.high = high;
		}

		public double getLow() {
			return low;
		}

		public void setLow(double low) {
			this.low = low;
		}

		public double getClose() {
			return close;
		}

		public void setClose(double close) {
			this.close = close;
		}

		public double getVolume() {
			return volume;
		}

		public void setVolume(double volume) {
			this.volume = volume;
		}

		public String getSymbol() {
			return symbol;
		}

		public void setSymbol(String symbol) {
			this.symbol = symbol;
		}

		public String getExchange() {
			return exchange;
		}

		public void setExchange(String exchange) {
			this.exchange = exchange;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}
	}


	@RestController
	public class StockController {

		@GetMapping("/stocks")
		public List<StockData> getStockData() throws Exception {
			String apiUrl = "http://api.marketstack.com/v1/eod?access_key=51f84434dc8a93835a6a24c1855822b1&symbols=AAPL";

			// Make the API call
			RestTemplate restTemplate = new RestTemplate();
			String response = restTemplate.getForObject(apiUrl, String.class);

			// Parse the JSON response
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(response);

			// Parse pagination info (if needed)
			JsonNode paginationNode = root.path("pagination");
			Pagination pagination = mapper.treeToValue(paginationNode, Pagination.class);

			// Extract stock data from the "data" array
			List<StockData> stockList = new ArrayList<>();
			JsonNode dataNode = root.path("data");

			for (JsonNode stockNode : dataNode) {
				StockData stockData = new StockData();
				stockData.setOpen(stockNode.path("open").asDouble());
				stockData.setHigh(stockNode.path("high").asDouble());
				stockData.setLow(stockNode.path("low").asDouble());
				stockData.setClose(stockNode.path("close").asDouble());
				stockData.setVolume(stockNode.path("volume").asDouble());
				stockData.setSymbol(stockNode.path("symbol").asText());
				stockData.setExchange(stockNode.path("exchange").asText());
				stockData.setDate(stockNode.path("date").asText());

				stockList.add(stockData);
			}

			// Return the list of stock data
			return stockList;
		}
	}


}
