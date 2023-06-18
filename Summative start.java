import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

class Stock {
    private String symbol;
    private String name;
    private double price;

    public Stock(String symbol, String name, double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#.##");
        return "Symbol: " + symbol + ", Name: " + name + ", Price: $" + df.format(price);
    }
}

class StockTicker {
    private List<Stock> stocks;

    public StockTicker() {
        stocks = new ArrayList<>();
    }

    public void addStock(Stock stock) {
        stocks.add(stock);
        System.out.println("Stock added successfully.");
    }

    public void removeStock(Stock stock) {
        stocks.remove(stock);
        System.out.println("Stock removed successfully.");
    }

    public void updateStockPrice() {
        // Simulating random price updates
        for (Stock stock : stocks) {
            double randomChange = Math.random() * 10 - 5; // Random change between -5 and 5
            double newPrice = stock.getPrice() + randomChange;
            stock.setPrice(newPrice);
        }
        System.out.println("Stock prices updated successfully.");
    }

    public void sortByPrice() {
        stocks.sort(Comparator.comparingDouble(Stock::getPrice).thenComparing(Stock::getSymbol));
    }

    public void saveStocksToFile(String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (Stock stock : stocks) {
                writer.write(stock.getSymbol() + "," + stock.getName() + "," + stock.getPrice() + "\n");
            }
            System.out.println("Stocks saved to file successfully.");
        } catch (IOException e) {
            System.out.println("Error saving stocks to file.");
        }
    }

    public void loadStocksFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            stocks.clear();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String symbol = parts[0];
                    String name = parts[1];
                    double price = Double.parseDouble(parts[2]);
                    Stock stock = new Stock(symbol, name, price);
                    stocks.add(stock);
                }
            }