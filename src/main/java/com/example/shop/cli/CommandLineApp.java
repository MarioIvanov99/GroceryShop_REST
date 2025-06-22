package com.example.shop.cli;

import com.example.shop.model.*;
import com.example.shop.service.OrderService;
import com.example.shop.service.ProductService;
import com.example.shop.service.RouteService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class CommandLineApp {

    private final ProductService productService;
    private final OrderService orderService;
    private final RouteService routeService;

    public CommandLineApp(ProductService productService, OrderService orderService, RouteService routeService) {
        this.productService = productService;
        this.orderService = orderService;
        this.routeService = routeService;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Our Store!");

        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Create product");
            System.out.println("2. List products");
            System.out.println("3. Update product");
            System.out.println("4. Delete product");
            System.out.println("5. Place new order");
            System.out.println("6. Exit");
            System.out.print("> ");

            String input = scanner.nextLine();

            switch (input) {
                case "1" -> createProduct(scanner);
                case "2" -> listProducts();
                case "3" -> updateProduct(scanner);
                case "4" -> deleteProduct(scanner);
                case "5" -> placeOrder(scanner);
                case "6" -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void createProduct(Scanner scanner) {
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();

        System.out.print("Enter quantity: ");
        int quantity = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter price: ");
        double price = Double.parseDouble(scanner.nextLine());

        System.out.print("Enter warehouse location X: ");
        int x = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter warehouse location Y: ");
        int y = Integer.parseInt(scanner.nextLine());

        Product product = new Product();
        product.setName(name);
        product.setQuantity(quantity);
        product.setPrice(price);
        product.setLocation(new Location(x, y));

        productService.create(product);
        System.out.println("Product created successfully.");
    }

    private void listProducts() {
        List<Product> products = productService.list();
        if (products.isEmpty()) {
            System.out.println("No products found.");
        } else {
            System.out.println("Available Products:");
            int i = 1;
            for (Product p : products) {
                System.out.printf("%d. %s | Qty: %d | Price: %.2f | Location: (%d,%d)%n",
                        i++, p.getName(), p.getQuantity(), p.getPrice(), p.getLocation().getX(), p.getLocation().getY());
            }
        }
    }

    private void updateProduct(Scanner scanner) {
        System.out.print("Enter product name to update: ");
        String name = scanner.nextLine();

        Product product = productService.getByName(name);
        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        System.out.print("Enter new quantity: ");
        int quantity = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter new price: ");
        double price = Double.parseDouble(scanner.nextLine());

        product.setQuantity(quantity);
        product.setPrice(price);
        productService.update(product);

        System.out.println("Product updated successfully.");
    }

    private void deleteProduct(Scanner scanner) {
        System.out.print("Enter product name to delete: ");
        String name = scanner.nextLine();

        Product product = productService.getByName(name);
        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        productService.delete(product.getId());
        System.out.println("Product deleted successfully.");
    }

    private void placeOrder(Scanner scanner) {
        System.out.println("Placing a new order...");
        List<OrderItem> items = new ArrayList<>();

        while (true) {
            System.out.print("Enter product name (or 'done' to finish): ");
            String productName = scanner.nextLine();
            if ("done".equalsIgnoreCase(productName)) break;

            System.out.print("Enter quantity: ");
            int qty = Integer.parseInt(scanner.nextLine());

            OrderItem item = new OrderItem();
            item.setProductName(productName);
            item.setQuantity(qty);
            items.add(item);
        }

        System.out.println("Processing your order...");
        Order order = orderService.placeOrder(items);
        System.out.println("Order status: " + order.getStatus());

        if ("SUCCESS".equalsIgnoreCase(order.getStatus())) {
            RouteResponse route = routeService.calculateRoute(order.getId());
            System.out.print("Visited locations:");
            for (Location loc : route.getVisitedLocations()) {
                System.out.printf("[%d,%d] ", loc.getX(), loc.getY());
            }
            System.out.println("\nMessage: Your order is ready! Please collect it at the desk.");
        } else {
            System.out.println("Message: Not enough stock to fulfill your order.");
        }
    }
}
