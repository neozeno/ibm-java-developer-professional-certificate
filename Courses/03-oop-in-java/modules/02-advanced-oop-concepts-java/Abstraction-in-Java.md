# Abstraction in Java

## Definition

Abstraction is a fundamental OOP principle that focuses on hiding complex
implementation details and showing only the essential features of an object.
It allows you to focus on WHAT an object does rather than HOW it does it.

**Key Concept**: Abstraction provides a simplified view of an object by
exposing only relevant information while hiding unnecessary complexity.

**Real-world analogy**: When you drive a car, you use the steering wheel,
pedals, and gear shift without knowing the internal mechanics of the engine,
transmission, or braking system. The car's interface (controls) abstracts
away the complexity.

## Ways to Achieve Abstraction in Java

Java provides two mechanisms to achieve abstraction:
1. **Abstract Classes** (0-100% abstraction)
2. **Interfaces** (100% abstraction - until Java 8)

---

## Abstract Classes

### Definition

An abstract class is a class that cannot be instantiated and may contain
both abstract methods (without implementation) and concrete methods (with
implementation). It serves as a blueprint for other classes.

### Key Characteristics

- Declared using the `abstract` keyword
- Cannot be instantiated directly (no `new AbstractClass()`)
- Can have abstract methods (without body) and concrete methods (with body)
- Can have constructors (called by subclasses via `super()`)
- Can have instance variables (fields)
- Can have static methods and variables
- Supports single inheritance only
- Subclasses must implement all abstract methods or be abstract themselves

### Syntax

```java
abstract class ClassName {
    // Abstract method (no implementation)
    abstract returnType methodName(parameters);
    
    // Concrete method (with implementation)
    returnType concreteMethod() {
        // method body
    }
    
    // Constructor
    ClassName() {
        // constructor body
    }
    
    // Fields
    int field;
}
```

### Example 1: Basic Abstract Class

```java
abstract class Animal {
    // Instance variable
    String name;
    int age;
    
    // Constructor
    Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    // Abstract methods (must be implemented by subclasses)
    abstract void makeSound();
    abstract void move();
    
    // Concrete method (inherited by all subclasses)
    void sleep() {
        System.out.println(name + " is sleeping");
    }
    
    void displayInfo() {
        System.out.println("Name: " + name + ", Age: " + age);
    }
}

class Dog extends Animal {
    String breed;
    
    Dog(String name, int age, String breed) {
        super(name, age);  // Call parent constructor
        this.breed = breed;
    }
    
    @Override
    void makeSound() {
        System.out.println(name + " barks: Woof! Woof!");
    }
    
    @Override
    void move() {
        System.out.println(name + " runs on four legs");
    }
}

class Bird extends Animal {
    boolean canFly;
    
    Bird(String name, int age, boolean canFly) {
        super(name, age);
        this.canFly = canFly;
    }
    
    @Override
    void makeSound() {
        System.out.println(name + " chirps: Tweet! Tweet!");
    }
    
    @Override
    void move() {
        if (canFly) {
            System.out.println(name + " flies in the sky");
        } else {
            System.out.println(name + " walks on the ground");
        }
    }
}

class Fish extends Animal {
    String waterType;
    
    Fish(String name, int age, String waterType) {
        super(name, age);
        this.waterType = waterType;
    }
    
    @Override
    void makeSound() {
        System.out.println(name + " makes bubbles");
    }
    
    @Override
    void move() {
        System.out.println(name + " swims in " + waterType + " water");
    }
}

public class Main {
    public static void main(String[] args) {
        // Animal animal = new Animal("Generic", 5); // ERROR: Cannot instantiate
        
        Dog dog = new Dog("Buddy", 3, "Golden Retriever");
        Bird bird = new Bird("Tweety", 1, true);
        Fish fish = new Fish("Nemo", 2, "salt");
        
        dog.displayInfo();
        dog.makeSound();
        dog.move();
        dog.sleep();
        
        System.out.println();
        
        bird.displayInfo();
        bird.makeSound();
        bird.move();
        bird.sleep();
        
        System.out.println();
        
        fish.displayInfo();
        fish.makeSound();
        fish.move();
        fish.sleep();
    }
}
```

**Output:**
```
Name: Buddy, Age: 3
Buddy barks: Woof! Woof!
Buddy runs on four legs
Buddy is sleeping

Name: Tweety, Age: 1
Tweety chirps: Tweet! Tweet!
Tweety flies in the sky
Tweety is sleeping

Name: Nemo, Age: 2
Nemo makes bubbles
Nemo swims in salt water
Nemo is sleeping
```

### Example 2: Abstract Class for Banking System

```java
abstract class BankAccount {
    protected String accountNumber;
    protected String accountHolder;
    protected double balance;
    
    BankAccount(String accountNumber, String accountHolder, double balance) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = balance;
    }
    
    // Abstract methods - each account type implements differently
    abstract void calculateInterest();
    abstract double getMinimumBalance();
    
    // Concrete methods - common to all accounts
    void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: $" + amount);
            displayBalance();
        } else {
            System.out.println("Invalid deposit amount");
        }
    }
    
    void withdraw(double amount) {
        if (amount > 0 && balance - amount >= getMinimumBalance()) {
            balance -= amount;
            System.out.println("Withdrawn: $" + amount);
            displayBalance();
        } else {
            System.out.println("Insufficient balance or invalid amount");
        }
    }
    
    void displayBalance() {
        System.out.println("Current Balance: $" + balance);
    }
    
    void displayAccountInfo() {
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Account Holder: " + accountHolder);
        System.out.println("Balance: $" + balance);
    }
}

class SavingsAccount extends BankAccount {
    private double interestRate = 0.04; // 4% annual interest
    
    SavingsAccount(String accountNumber, String accountHolder, double balance) {
        super(accountNumber, accountHolder, balance);
    }
    
    @Override
    void calculateInterest() {
        double interest = balance * interestRate;
        balance += interest;
        System.out.println("Interest added: $" + interest);
        displayBalance();
    }
    
    @Override
    double getMinimumBalance() {
        return 1000.0; // Minimum $1000 balance
    }
}

class CurrentAccount extends BankAccount {
    private double overdraftLimit = 5000.0;
    
    CurrentAccount(String accountNumber, String accountHolder, double balance) {
        super(accountNumber, accountHolder, balance);
    }
    
    @Override
    void calculateInterest() {
        // No interest for current accounts
        System.out.println("Current accounts do not earn interest");
    }
    
    @Override
    double getMinimumBalance() {
        return -overdraftLimit; // Can go negative up to overdraft limit
    }
    
    @Override
    void withdraw(double amount) {
        if (amount > 0 && balance - amount >= getMinimumBalance()) {
            balance -= amount;
            System.out.println("Withdrawn: $" + amount);
            if (balance < 0) {
                System.out.println("WARNING: Overdraft used!");
            }
            displayBalance();
        } else {
            System.out.println("Exceeds overdraft limit or invalid amount");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        SavingsAccount savings = new SavingsAccount("SAV001", "Alice", 5000);
        CurrentAccount current = new CurrentAccount("CUR001", "Bob", 2000);
        
        System.out.println("=== Savings Account ===");
        savings.displayAccountInfo();
        savings.deposit(1000);
        savings.calculateInterest();
        savings.withdraw(500);
        
        System.out.println("\n=== Current Account ===");
        current.displayAccountInfo();
        current.deposit(500);
        current.calculateInterest();
        current.withdraw(3000);
    }
}
```

**Output**:

```
=== Savings Account ===
Account number: 123
Account holder: John Doe
Balance: $5000.0
Deposited: $1000.0
Current balance: $6000.0
Interest added: $240.0
Current balance: $6240.0
Withdrawn: $500.0
Current balance: $5740.0
=== Current Account ===
Account number: 700
Account holder: Jane Doe
Balance: $2000.0
Deposited: $1000.0
Current balance: $3000.0
Current accounts do not earn interests.
Withdrawn: $500.0
Current balance: $2500.0
```

---

## Interfaces

### Definition

An interface is a completely abstract type that defines a contract - a set
of method signatures that implementing classes must provide. It specifies
WHAT a class should do, but not HOW it should do it.

### Key Characteristics

- Declared using the `interface` keyword
- All methods are `public abstract` by default (before Java 8)
- All variables are `public static final` by default (constants)
- Cannot be instantiated
- A class can implement multiple interfaces (multiple inheritance)
- Since Java 8: can have default and static methods with implementation
- Since Java 9: can have private methods

### Syntax

```java
interface InterfaceName {
    // Constant (public static final by default)
    int CONSTANT = 100;
    
    // Abstract method (public abstract by default)
    void methodName();
    
    // Default method (Java 8+)
    default void defaultMethod() {
        // implementation
    }
    
    // Static method (Java 8+)
    static void staticMethod() {
        // implementation
    }
    
    // Private method (Java 9+)
    private void privateMethod() {
        // implementation
    }
}
```

### Example 1: Basic Interface

```java
interface Drawable {
    // Abstract method
    void draw();
}

interface Resizable {
    void resize(int width, int height);
}

// Class implementing multiple interfaces
class Circle implements Drawable, Resizable {
    private int radius;
    private int x, y;
    
    Circle(int radius, int x, int y) {
        this.radius = radius;
        this.x = x;
        this.y = y;
    }
    
    @Override
    public void draw() {
        System.out.println("Drawing circle at (" + x + "," + y + 
                          ") with radius " + radius);
    }
    
    @Override
    public void resize(int width, int height) {
        this.radius = width / 2;
        System.out.println("Circle resized to radius: " + radius);
    }
}

class Rectangle implements Drawable, Resizable {
    private int width, height;
    private int x, y;
    
    Rectangle(int width, int height, int x, int y) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }
    
    @Override
    public void draw() {
        System.out.println("Drawing rectangle at (" + x + "," + y + 
                          ") with size " + width + "x" + height);
    }
    
    @Override
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        System.out.println("Rectangle resized to: " + width + "x" + height);
    }
}

public class Main {
    public static void main(String[] args) {
        Circle circle = new Circle(50, 100, 100);
        Rectangle rectangle = new Rectangle(200, 100, 50, 50);
        
        // Polymorphism with interfaces
        Drawable[] shapes = {circle, rectangle};
        
        for (Drawable shape : shapes) {
            shape.draw();
        }
        
        System.out.println();
        
        // Using Resizable interface
        Resizable[] resizables = {circle, rectangle};
        for (Resizable r : resizables) {
            r.resize(150, 150);
        }
    }
}
```

**Output:**

```
Drawing Circle at (50, 100) with radius 100
Drawing rectangle at (200, 100) with size 50 x 50
Circle resized with radius 75
Rectangle resized with size 150 x 150
```

### Example 2: Interface with Default and Static Methods (Java 8+)

```java
interface Vehicle {
    // Constants
    int MAX_SPEED = 200;
    
    // Abstract methods
    void start();
    void stop();
    void accelerate(int speed);
    
    // Default method (provides default implementation)
    default void honk() {
        System.out.println("Beep! Beep!");
    }
    
    default void displayInfo() {
        System.out.println("This is a vehicle");
        System.out.println("Maximum speed: " + MAX_SPEED + " km/h");
    }
    
    // Static method (belongs to interface, not instances)
    static void vehicleType() {
        System.out.println("This is a motorized vehicle");
    }
}

class Car implements Vehicle {
    private String model;
    private int currentSpeed;
    
    Car(String model) {
        this.model = model;
        this.currentSpeed = 0;
    }
    
    @Override
    public void start() {
        System.out.println(model + " engine started");
    }
    
    @Override
    public void stop() {
        currentSpeed = 0;
        System.out.println(model + " stopped");
    }
    
    @Override
    public void accelerate(int speed) {
        currentSpeed += speed;
        if (currentSpeed > MAX_SPEED) {
            currentSpeed = MAX_SPEED;
        }
        System.out.println(model + " accelerating to " + currentSpeed + " km/h");
    }
    
    // Override default method if needed
    @Override
    public void honk() {
        System.out.println(model + " honks: HONK! HONK!");
    }
}

class Motorcycle implements Vehicle {
    private String brand;
    private int currentSpeed;
    
    Motorcycle(String brand) {
        this.brand = brand;
        this.currentSpeed = 0;
    }
    
    @Override
    public void start() {
        System.out.println(brand + " motorcycle started with a roar!");
    }
    
    @Override
    public void stop() {
        currentSpeed = 0;
        System.out.println(brand + " motorcycle stopped");
    }
    
    @Override
    public void accelerate(int speed) {
        currentSpeed += speed;
        System.out.println(brand + " accelerating to " + currentSpeed + " km/h");
    }
    
    // Uses default honk() method
}

public class Main {
    public static void main(String[] args) {
        Car car = new Car("Tesla Model 3");
        Motorcycle bike = new Motorcycle("Harley Davidson");
        
        System.out.println("=== Car ===");
        car.start();
        car.accelerate(60);
        car.honk();  // Overridden method
        car.displayInfo();  // Default method
        car.stop();
        
        System.out.println("\n=== Motorcycle ===");
        bike.start();
        bike.accelerate(80);
        bike.honk();  // Default method
        bike.displayInfo();  // Default method
        bike.stop();
        
        System.out.println();
        Vehicle.vehicleType();  // Static method called via interface
    }
}
```

### Example 3: Multiple Interface Implementation
```java
interface Flyable {
    void fly();
    default void takeOff() {
        System.out.println("Taking off...");
    }
}

interface Swimmable {
    void swim();
    default void dive() {
        System.out.println("Diving...");
    }
}

interface Walkable {
    void walk();
}

// Duck can fly, swim, and walk
class Duck implements Flyable, Swimmable, Walkable {
    private String name;
    
    Duck(String name) {
        this.name = name;
    }
    
    @Override
    public void fly() {
        System.out.println(name + " is flying");
    }
    
    @Override
    public void swim() {
        System.out.println(name + " is swimming");
    }
    
    @Override
    public void walk() {
        System.out.println(name + " is waddling");
    }
}

// Fish can only swim
class Fish implements Swimmable {
    private String name;
    
    Fish(String name) {
        this.name = name;
    }
    
    @Override
    public void swim() {
        System.out.println(name + " is swimming gracefully");
    }
}

// Airplane can fly
class Airplane implements Flyable {
    private String model;
    
    Airplane(String model) {
        this.model = model;
    }
    
    @Override
    public void fly() {
        System.out.println(model + " is flying at high altitude");
    }
    
    @Override
    public void takeOff() {
        System.out.println(model + " is taking off from the runway");
    }
}

public class Main {
    public static void main(String[] args) {
        Duck duck = new Duck("Donald");
        Fish fish = new Fish("Nemo");
        Airplane plane = new Airplane("Boeing 747");
        
        System.out.println("=== Duck ===");
        duck.takeOff();
        duck.fly();
        duck.swim();
        duck.walk();
        
        System.out.println("\n=== Fish ===");
        fish.dive();
        fish.swim();
        
        System.out.println("\n=== Airplane ===");
        plane.takeOff();
        plane.fly();
    }
}
```

---

## Abstract Class vs Interface: Detailed Comparison

| Feature | Abstract Class | Interface |
|---------|---------------|-----------|
| **Keyword** | `abstract class` | `interface` |
| **Instantiation** | Cannot be instantiated | Cannot be instantiated |
| **Methods** | Abstract + concrete methods | Abstract methods (default/static since Java 8) |
| **Variables** | Any type of variables | Only `public static final` (constants) |
| **Access Modifiers** | public, protected, private | Only public (methods are public by default) |
| **Constructor** | Can have constructors | Cannot have constructors |
| **Inheritance** | Single inheritance (`extends`) | Multiple inheritance (`implements`) |
| **Default Implementation** | Yes, can provide | Yes (since Java 8 with `default`) |
| **Use Case** | Common base with shared code | Contract/behavior specification |
| **Abstraction Level** | 0-100% abstraction | 100% abstraction (pure interface) |
| **Fields** | Instance and static fields | Only static final fields |
| **Speed** | Faster (single inheritance) | Slightly slower (multiple lookup) |
| **When to Use** | IS-A relationship with shared code | CAN-DO capability/behavior |

### When to Use Abstract Classes

Use abstract classes when:
- You want to share code among closely related classes
- Classes have common fields or require access modifiers other than public
- You need non-static or non-final fields
- You want to declare non-public members
- You have a clear parent-child "IS-A" relationship

**Example:**
```java
abstract class Employee {  // IS-A relationship
    protected String name;
    protected double salary;
    
    Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }
    
    abstract double calculateBonus();
    
    void displayInfo() {  // Common implementation
        System.out.println("Name: " + name + ", Salary: $" + salary);
    }
}

class Manager extends Employee {
    Manager(String name, double salary) {
        super(name, salary);
    }
    
    @Override
    double calculateBonus() {
        return salary * 0.20;  // 20% bonus
    }
}

class Developer extends Employee {
    Developer(String name, double salary) {
        super(name, salary);
    }
    
    @Override
    double calculateBonus() {
        return salary * 0.15;  // 15% bonus
    }
}
```

### When to Use Interfaces

Use interfaces when:
- You expect unrelated classes to implement your interface
- You want to specify behavior that can be implemented by any class
- You want to take advantage of multiple inheritance
- You want to define a contract without implementation details
- You have a "CAN-DO" capability rather than "IS-A" relationship

**Example:**
```java
interface Printable {  // CAN-DO capability
    void print();
}

interface Scannable {
    void scan();
}

interface Faxable {
    void fax();
}

// All-in-one printer implements multiple capabilities
class MultiFunctionPrinter implements Printable, Scannable, Faxable {
    @Override
    public void print() {
        System.out.println("Printing document...");
    }
    
    @Override
    public void scan() {
        System.out.println("Scanning document...");
    }
    
    @Override
    public void fax() {
        System.out.println("Faxing document...");
    }
}

// Simple printer only implements printing
class SimplePrinter implements Printable {
    @Override
    public void print() {
        System.out.println("Printing document...");
    }
}
```

---

## Combining Abstract Classes and Interfaces

You can combine both for maximum flexibility:

```java
// Interface defines the contraect
interface PaymentProcessor {
    void processPayment(double amount);
    void refund(double amount);
}

// Abstract class that provide common implementation
abstract class BasePaymentProcessor implements PaymentProcessor {
    protected String merchantId;
    protected double totalProcessed;

    BasePaymentProcessor(String merchantId) {
        this.merchantId = merchantId;
        this.totalProcessed = 0;
    }

    // Common implementation for all payment processors
    @Override
    public void refund(double amount) {
        if (amount > 0 && amount <= totalProcessed) {
            totalProcessed -= amount;
            System.out.printf("Refunded: $%.2f\n", amount);
            System.out.printf("Total Processed: $%.2f\n", totalProcessed);
        } else {
            System.out.println("Invalid refund amount.");
        }
    }

    // Abstract method—each processor implements it differently
    abstract void logTransaction(String type, double amount);

    protected void updateTotal(double amount) {
        totalProcessed += amount;
    }
}

class CreditCardProcessor extends BasePaymentProcessor {
    CreditCardProcessor(String merchantId) {
        super(merchantId);
    }

    @Override
    public void processPayment(double amount) {
        System.out.printf("Processing credit card payment: $%.2f\n", amount);
        logTransaction("CREDIT_CARD", amount);
        updateTotal(amount);
    }

    @Override
    void logTransaction(String type, double amount) {
        System.out.printf("[LOG] %s transaction: $%.2f (Merchant: %s)\n", type, amount, merchantId);
    }
}

class PayPalProcessor extends BasePaymentProcessor {
    PayPalProcessor(String merchantId) {
        super(merchantId);
    }

    @Override
    public void processPayment(double amount) {
        System.out.printf("Processing PayPal payment: $%.2f\n", amount);
        logTransaction("PAYPAL", amount);
        updateTotal(amount);
    }

    @Override
    void logTransaction(String type, double amount) {
        System.out.printf("[PAYPAL_LOG] %s transaction: $%.2f via %s\n", type, amount, merchantId);
    }
}

public class CombiningAbstractClassesAndInterfaces {
    public static void main(String[] args) {
        PaymentProcessor cc = new CreditCardProcessor("MERCHANT_001");
        PaymentProcessor pp = new PayPalProcessor("MERCHANT_002");

        cc.processPayment(100.0);
        cc.processPayment(50.0);
        cc.refund(30.0);

        System.out.println();

        pp.processPayment(200.0);
        pp.refund(50.0);
    }
}
```

**Output**:

```
Processing credit card payment: $100.00
[LOG] CREDIT_CARD transaction: $100.00 (Merchant: MERCHANT_001)
Processing credit card payment: $50.00
[LOG] CREDIT_CARD transaction: $50.00 (Merchant: MERCHANT_001)
Refunded: $30.00
Total Processed: $120.00

Processing PayPal payment: $200.00
[PAYPAL_LOG] PAYPAL transaction: $200.00 via MERCHANT_002
Refunded: $50.00
Total Processed: $150.00
```

---

## Benefits of Abstraction

### 1. Security and Data Hiding

Abstraction hides internal implementation details, exposing only necessary
functionality. This protects sensitive data and prevents misuse.

```java
abstract class DatabaseConnection {
    private String connectionString;  // Hidden from users
    private String password;          // Hidden from users
    
    DatabaseConnection(String connectionString, String password) {
        this.connectionString = connectionString;
        this.password = password;
    }
    
    // Public interface - users only see this
    abstract void connect();
    abstract void disconnect();
    abstract void executeQuery(String query);
    
    // Protected helper method - only subclasses can access
    protected boolean authenticate() {
        // Complex authentication logic hidden from users
        System.out.println("Authenticating...");
        return true;
    }
}

class MySQLConnection extends DatabaseConnection {
    MySQLConnection(String connectionString, String password) {
        super(connectionString, password);
    }
    
    @Override
    void connect() {
        if (authenticate()) {  // Uses protected method
            System.out.println("Connected to MySQL database");
        }
    }
    
    @Override
    void disconnect() {
        System.out.println("Disconnected from MySQL");
    }
    
    @Override
    void executeQuery(String query) {
        System.out.println("Executing MySQL query: " + query);
    }
}
```

### 2. Code Reusability

Common functionality is defined once and reused across multiple classes.

```java
abstract class Report {
    protected String title;
    protected String date;
    
    Report(String title, String date) {
        this.title = title;
        this.date = date;
    }
    
    // Common template method
    final void generateReport() {
        printHeader();
        printBody();
        printFooter();
    }
    
    // Common implementation
    void printHeader() {
        System.out.println("========================================");
        System.out.println("Report: " + title);
        System.out.println("Date: " + date);
        System.out.println("========================================");
    }
    
    // Abstract - each report type implements differently
    abstract void printBody();
    
    // Common implementation
    void printFooter() {
        System.out.println("========================================");
        System.out.println("End of Report");
    }
}

class SalesReport extends Report {
    private double totalSales;
    
    SalesReport(String date, double totalSales) {
        super("Sales Report", date);
        this.totalSales = totalSales;
    }
    
    @Override
    void printBody() {
        System.out.println("Total Sales: $" + totalSales);
    }
}

class InventoryReport extends Report {
    private int itemCount;
    
    InventoryReport(String date, int itemCount) {
        super("Inventory Report", date);
        this.itemCount = itemCount;
    }
    
    @Override
    void printBody() {
        System.out.println("Total Items in Stock: " + itemCount);
    }
}
```

### 3. Maintainability

Changes to implementation don't affect code that uses the abstraction.

```java
interface Logger {
    void log(String message);
}

class FileLogger implements Logger {
    @Override
    public void log(String message) {
        // Implementation can change without affecting users
        System.out.println("[FILE] " + message);
        // Could write to file instead
    }
}

class DatabaseLogger implements Logger {
    @Override
    public void log(String message) {
        // Different implementation, same interface
        System.out.println("[DATABASE] " + message);
        // Could write to database instead
    }
}

class Application {
    private Logger logger;
    
    Application(Logger logger) {
        this.logger = logger;  // Depends on abstraction, not implementation
    }
    
    void doSomething() {
        logger.log("Application started");
        // Application doesn't care how logging is implemented
        logger.log("Processing data...");
        logger.log("Application finished");
    }
}

public class Main {
    public static void main(String[] args) {
        // Can switch logger implementation easily
        Application app1 = new Application(new FileLogger());
        app1.doSomething();
        
        System.out.println();
        
        Application app2 = new Application(new DatabaseLogger());
        app2.doSomething();
    }
}
```

### 4. Loose Coupling

Classes depend on abstractions rather than concrete implementations,
reducing dependencies between components.

```java
// Bad: Tight coupling
class EmailService {
    void sendEmail(String message) {
        System.out.println("Sending email: " + message);
    }
}

class OrderProcessor {
    private EmailService emailService = new EmailService();  // Tightly coupled
    
    void processOrder() {
        // If we want to use SMS instead, we need to modify this class
        emailService.sendEmail("Order processed");
    }
}

// Good: Loose coupling with abstraction
interface NotificationService {
    void send(String message);
}

class EmailNotification implements NotificationService {
    @Override
    public void send(String message) {
        System.out.println("Sending email: " + message);
    }
}

class SMSNotification implements NotificationService {
    @Override
    public void send(String message) {
        System.out.println("Sending SMS: " + message);
    }
}

class OrderProcessorImproved {
    private NotificationService notificationService;
    
    OrderProcessorImproved(NotificationService notificationService) {
        this.notificationService = notificationService;  // Loosely coupled
    }
    
    void processOrder() {
        // Can use any notification service without modifying this class
        notificationService.send("Order processed");
    }
}

public class Main {
    public static void main(String[] args) {
        OrderProcessorImproved emailOrder = 
            new OrderProcessorImproved(new EmailNotification());
        emailOrder.processOrder();
        
        OrderProcessorImproved smsOrder = 
            new OrderProcessorImproved(new SMSNotification());
        smsOrder.processOrder();
    }
}
```

### 5. Flexibility and Extensibility

Easy to add new implementations without modifying existing code (Open/Closed
Principle).

```java
interface Shape {
    double calculateArea();
    double calculatePerimeter();
}

class Circle implements Shape {
    private double radius;
    
    Circle(double radius) {
        this.radius = radius;
    }
    
    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }
    
    @Override
    public double calculatePerimeter() {
        return 2 * Math.PI * radius;
    }
}

class Rectangle implements Shape {
    private double width, height;
    
    Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public double calculateArea() {
        return width * height;
    }
    
    @Override
    public double calculatePerimeter() {
        return 2 * (width + height);
    }
}

// Adding new shape - existing code unchanged
class Triangle implements Shape {
    private double a, b, c;
    
    Triangle(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
    
    @Override
    public double calculateArea() {
        double s = (a + b + c) / 2;
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }
