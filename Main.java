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
            System.out.println("Stocks loaded from file successfully.");
        } catch (IOException e) {
            System.out.println("Error loading stocks from file.");
        }
    }

    public List<Stock> getStocks() {
        return stocks;
    }
}

class StockTickerGUI extends JFrame {
    private StockTicker stockTicker;
    private JTextArea outputTextArea;
    private JTextField symbolTextField;

    public StockTickerGUI() {
        stockTicker = new StockTicker();
        setupUI();
    }

    private void setupUI() {
        setTitle("Stock Ticker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(61, 90, 128));
        headerPanel.setPreferredSize(new Dimension(600, 50));

        JLabel titleLabel = new JLabel("Stock Ticker");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Stock List Panel
        JPanel stockListPanel = new JPanel();
        stockListPanel.setLayout(new BoxLayout(stockListPanel, BoxLayout.Y_AXIS));
        stockListPanel.setBackground(new Color(243, 243, 243));

        outputTextArea = new JTextArea();
        outputTextArea.setEditable(false);
        outputTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        stockListPanel.add(scrollPane);

        add(stockListPanel, BorderLayout.CENTER);

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(243, 243, 243));
        formPanel.setPreferredSize(new Dimension(600, 100));
        formPanel.setLayout(new FlowLayout());

        JLabel symbolLabel = new JLabel("Symbol:");
        symbolLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(symbolLabel);

        symbolTextField = new JTextField(10);
        symbolTextField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(symbolTextField);

        JButton addButton = new JButton("Add Stock");
        addButton.setFont(new Font("Arial", Font.PLAIN, 14));
        addButton.setBackground(new Color(91, 155, 213));
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String symbol = symbolTextField.getText().toUpperCase();
                Stock stock = new Stock(symbol, "", 0.0);
                stockTicker.addStock(stock);
                updateOutput();
                clearFields();
            }
        });
        formPanel.add(addButton);

        JButton removeButton = new JButton("Remove Stock");
        removeButton.setFont(new Font("Arial", Font.PLAIN, 14));
        removeButton.setBackground(new Color(91, 155, 213));
        removeButton.setForeground(Color.WHITE);
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String symbol = symbolTextField.getText().toUpperCase();
                List<Stock> stocks = stockTicker.getStocks();
                for (Stock stock : stocks) {
                    if (stock.getSymbol().equals(symbol)) {
                        stockTicker.removeStock(stock);
                        break;
                    }
                }
                updateOutput();
                clearFields();
            }
        });
        formPanel.add(removeButton);

        JButton updateButton = new JButton("Update Stock Prices");
        updateButton.setFont(new Font("Arial", Font.PLAIN, 14));
        updateButton.setBackground(new Color(91, 155, 213));
        updateButton.setForeground(Color.WHITE);
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stockTicker.updateStockPrice();
                updateOutput();
            }
        });
        formPanel.add(updateButton);

        JButton sortButton = new JButton("Sort by Price");
        sortButton.setFont(new Font("Arial", Font.PLAIN, 14));
        sortButton.setBackground(new Color(91, 155, 213));
        sortButton.setForeground(Color.WHITE);
        sortButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stockTicker.sortByPrice();
                updateOutput();
            }
        });
        formPanel.add(sortButton);

        JButton saveButton = new JButton("Save to File");
        saveButton.setFont(new Font("Arial", Font.PLAIN, 14));
        saveButton.setBackground(new Color(91, 155, 213));
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Save Stocks");
                fileChooser.setFileFilter(new FileNameExtensionFilter("Text files (*.txt)", "txt"));
                int userSelection = fileChooser.showSaveDialog(StockTickerGUI.this);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    String fileName = fileChooser.getSelectedFile().getAbsolutePath();
                    stockTicker.saveStocksToFile(fileName);
                }
            }
        });
        formPanel.add(saveButton);

        JButton loadButton = new JButton("Load from File");
        loadButton.setFont(new Font("Arial", Font.PLAIN, 14));
        loadButton.setBackground(new Color(91, 155, 213));
        loadButton.setForeground(Color.WHITE);
        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Load Stocks");
                fileChooser.setFileFilter(new FileNameExtensionFilter("Text files (*.txt)", "txt"));
                int userSelection = fileChooser.showOpenDialog(StockTickerGUI.this);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    String fileName = fileChooser.getSelectedFile().getAbsolutePath();
                    stockTicker.loadStocksFromFile(fileName);
                    updateOutput();
                }
            }
        });
        formPanel.add(loadButton);

        add(formPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateOutput() {
        outputTextArea.setText("");
        List<Stock> stocks = stockTicker.getStocks();
        for (Stock stock : stocks) {
            outputTextArea.append(stock + "\n");
        }
    }

    private void clearFields() {
        symbolTextField.setText("");
    }
}

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new StockTickerGUI();
            }
        });
    }
}