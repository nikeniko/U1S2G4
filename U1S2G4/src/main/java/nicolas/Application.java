package nicolas;

import nicolas.entitie.Customer;
import nicolas.entitie.Order;
import nicolas.entitie.Product;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Application {
    private static final List<Product> warehouse = new ArrayList<>();
    private static final List<Customer> customers = new ArrayList<>();
    private static final List<Order> orders = new ArrayList<>();

    public static void main(String[] args) {
        initializeWarehouse();
        createCustomers();
        placeOrders();

        printList(orders);

        System.out.println("************* 1 *****************");
        getOrdersByCustomer().forEach((customer, orders) -> {
            System.out.println("Customer: " + customer + " has made " + orders.size() + " orders.");
            System.out.println("Orders: " + orders);
        });

        System.out.println("************* 2 *****************");
        getTotalSpentByCustomer().forEach((customer, total) ->
                System.out.println("Customer: " + customer + " has spent " + total + " €")
        );

        System.out.println("************* 3 *****************");
        getTopThreeExpensiveProducts().forEach(System.out::println);

        System.out.println("************* 4 *****************");
        System.out.println("Average Order Value: " + getAverageOrderValue());

        System.out.println("************* 5 *****************");
        System.out.println("Categories and Totals: " + getCategoryTotals());

        System.out.println("************* 6 *****************");
        try {
            saveProductsToDisk();
        } catch (IOException e) {
            System.err.println("Error saving to disk: " + e.getMessage());
        }

        System.out.println("************* 7 *****************");
        try {
            loadProductsFromDisk().forEach(System.out::println);
        } catch (IOException e) {
            System.err.println("Error loading from disk: " + e.getMessage());
        }
    }
    public static Map<Customer, List<Order>> getOrdersByCustomer() {
        return orders.stream().collect(Collectors.groupingBy(Order::getCustomer));
    }
    public static Map<Customer, Double> getTotalSpentByCustomer() {
        return orders.stream()
                .collect(Collectors.groupingBy(Order::getCustomer, Collectors.summingDouble(Order::getTotal)));
    }
    public static List<Product> getTopThreeExpensiveProducts() {
        return warehouse.stream()
                .sorted(Comparator.comparing(Product::getPrice).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }
    public static double getAverageOrderValue() {
        return orders.stream()
                .mapToDouble(Order::getTotal)
                .average()
                .orElse(0.0);
    }
    public static Map<String, Double> getCategoryTotals() {
        return warehouse.stream()
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.summingDouble(Product::getPrice)));
    }
    public static void saveProductsToDisk() throws IOException {
        StringBuilder data = new StringBuilder();

        for (Product product : warehouse) {
            data.append(String.format("%s@%s@%.2f#", product.getName(), product.getCategory(), product.getPrice()));
        }
        Files.write(Paths.get("products.txt"), data.toString().getBytes(StandardCharsets.UTF_8));
    }
    public static List<Product> loadProductsFromDisk() throws IOException {
        String fileContent = new String(Files.readAllBytes(Paths.get("products.txt")), StandardCharsets.UTF_8);
        return Arrays.stream(fileContent.split("#"))
                .filter(str -> !str.isEmpty())
                .map(line -> {
                    String[] parts = line.split("@");
                    String priceWithDot = parts[2].replace(",", ".");
                    return new Product(parts[0], parts[1], Double.parseDouble(priceWithDot));
                })
                .collect(Collectors.toList());
    }

    public static void printList(List<?> list) {
        list.forEach(System.out::println);
    }

    public static void initializeWarehouse() {
        warehouse.addAll(Arrays.asList(
                new Product("Samsung Galaxy S22", "Smartphones", 1200.0),
                new Product("Moby Dick", "Books", 25),
                new Product("Harry Potter", "Books", 40),
                new Product("The Catcher in the Rye", "Books", 30),
                new Product("Huggies Diapers", "Baby", 20),
                new Product("Toy Truck", "Toys", 35),
                new Product("Drone", "Toys", 150),
                new Product("Lego City", "Toys", 80)
        ));
    }

    public static void createCustomers() {
        customers.addAll(Arrays.asList(
                new Customer("John Smith", 1),
                new Customer("Emily Johnson", 2),
                new Customer("Michael Brown", 3),
                new Customer("Sarah Davis", 4)
        ));
    }

    public static void placeOrders() {
        orders.addAll(Arrays.asList(
                createOrder(customers.get(0), "Samsung Galaxy S22", "Moby Dick", "Huggies Diapers"),
                createOrder(customers.get(1), "Harry Potter", "The Catcher in the Rye", "Samsung Galaxy S22"),
                createOrder(customers.get(2), "Moby Dick", "Huggies Diapers"),
                createOrder(customers.get(3), "Huggies Diapers"),
                createOrder(customers.get(2), "Samsung Galaxy S22")
        ));
    }

    private static Order createOrder(Customer customer, String... productNames) {
        Order order = new Order(customer);
        Arrays.stream(productNames).map(name -> warehouse.stream()
                .filter(product -> product.getName().equals(name))
                .findFirst().orElseThrow()).forEach(order::addProduct);
        return order;
    }
}
