# Polymorphism in Java

## Definition

Polymorphism means "many forms" - it's the ability of an object to take on
many forms. In Java, polymorphism allows you to perform a single action in
different ways. It enables you to write flexible and reusable code by
allowing objects of different types to be accessed through the same
interface.

**Key Principle**: "One interface, multiple implementations"

## Types of Polymorphism

Java supports two types of polymorphism:
1. **Compile-time Polymorphism** (Static Polymorphism)
2. **Runtime Polymorphism** (Dynamic Polymorphism)

---

## 1. Compile-Time Polymorphism (Method Overloading)

### Definition

Compile-time polymorphism is achieved through **method overloading** - 
having multiple methods with the same name but different parameters in the
same class. The compiler determines which method to call based on the method
signature at compile time.

### Rules for Method Overloading

- Methods must have the **same name**
- Methods must have **different parameter lists** (number, type, or order)
- Return type alone is NOT sufficient to overload a method
- Access modifiers can be different

### Example 1: Basic Method Overloading

```java
class Calculator {
    // Method with 2 int parameters
    int add(int a, int b) {
        return a + b;
    }
    
    // Method with 3 int parameters
    int add(int a, int b, int c) {
        return a + b + c;
    }
    
    // Method with 2 double parameters
    double add(double a, double b) {
        return a + b;
    }
    
    // Method with different parameter order
    String add(String a, int b) {
        return a + b;
    }
    
    String add(int a, String b) {
        return a + b;
    }
}

public class Main {
    public static void main(String[] args) {
        Calculator calc = new Calculator();
        
        System.out.println(calc.add(5, 10));           // Calls add(int, int)
        System.out.println(calc.add(5, 10, 15));       // Calls add(int, int, int)
        System.out.println(calc.add(5.5, 10.5));       // Calls add(double, double)
        System.out.println(calc.add("Result: ", 42));  // Calls add(String, int)
        System.out.println(calc.add(42, " items"));    // Calls add(int, String)
    }
}
```

**Output:**
```
15
30
16.0
Result: 42
42 items
```

### Example 2: Constructor Overloading

```java
class Student {
    String name;
    int age;
    String major;
    
    // Constructor with no parameters
    Student() {
        this.name = "Unknown";
        this.age = 0;
        this.major = "Undeclared";
    }
    
    // Constructor with name only
    Student(String name) {
        this.name = name;
        this.age = 0;
        this.major = "Undeclared";
    }
    
    // Constructor with name and age
    Student(String name, int age) {
        this.name = name;
        this.age = age;
        this.major = "Undeclared";
    }
    
    // Constructor with all parameters
    Student(String name, int age, String major) {
        this.name = name;
        this.age = age;
        this.major = major;
    }
    
    void display() {
        System.out.println("Name: " + name + ", Age: " + age + 
                          ", Major: " + major);
    }
}

public class Main {
    public static void main(String[] args) {
        Student s1 = new Student();
        Student s2 = new Student("Alice");
        Student s3 = new Student("Bob", 20);
        Student s4 = new Student("Charlie", 22, "Computer Science");
        
        s1.display();
        s2.display();
        s3.display();
        s4.display();
    }
}
```

**Output:**
```
Name: Unknown, Age: 0, Major: Undeclared
Name: Alice, Age: 0, Major: Undeclared
Name: Bob, Age: 20, Major: Undeclared
Name: Charlie, Age: 22, Major: Computer Science
```

---

## 2. Runtime Polymorphism (Method Overriding)

### Definition

Runtime polymorphism is achieved through **method overriding** - when a
subclass provides a specific implementation of a method already defined in
its superclass. The JVM determines which method to call at runtime based on
the actual object type, not the reference type.

### Rules for Method Overriding

- Method signature must be **identical** to the parent class method
- Access modifier must be **same or less restrictive**
- Return type must be **same or covariant** (subtype)
- Cannot override **static, final, or private** methods
- Use `@Override` annotation for clarity

### Example 1: Basic Method Overriding

```java
class Animal {
    void makeSound() {
        System.out.println("Animal makes a sound");
    }
    
    void sleep() {
        System.out.println("Animal is sleeping");
    }
}

class Dog extends Animal {
    @Override
    void makeSound() {
        System.out.println("Dog barks: Woof! Woof!");
    }
    
    // sleep() is inherited without modification
}

class Cat extends Animal {
    @Override
    void makeSound() {
        System.out.println("Cat meows: Meow! Meow!");
    }
    
    @Override
    void sleep() {
        System.out.println("Cat sleeps 16 hours a day");
    }
}

class Cow extends Animal {
    @Override
    void makeSound() {
        System.out.println("Cow moos: Moo! Moo!");
    }
}

public class Main {
    public static void main(String[] args) {
        // Reference type: Animal, Object type: Dog
        Animal myAnimal = new Animal();
        Animal myDog = new Dog();
        Animal myCat = new Cat();
        Animal myCow = new Cow();
        
        myAnimal.makeSound();  // Animal makes a sound
        myDog.makeSound();     // Dog barks: Woof! Woof!
        myCat.makeSound();     // Cat meows: Meow! Meow!
        myCow.makeSound();     // Cow moos: Moo! Moo!
        
        System.out.println();
        
        myAnimal.sleep();      // Animal is sleeping
        myDog.sleep();         // Animal is sleeping (inherited)
        myCat.sleep();         // Cat sleeps 16 hours a day (overridden)
    }
}
```

**Output:**
```
Animal makes a sound
Dog barks: Woof! Woof!
Cat meows: Meow! Meow!
Cow moos: Moo! Moo!

Animal is sleeping
Animal is sleeping
Cat sleeps 16 hours a day
```

### Example 2: Polymorphism with Arrays

```java
class Shape {
    void draw() {
        System.out.println("Drawing a shape");
    }
    
    double getArea() {
        return 0.0;
    }
}

class Circle extends Shape {
    double radius;
    
    Circle(double radius) {
        this.radius = radius;
    }
    
    @Override
    void draw() {
        System.out.println("Drawing a circle");
    }
    
    @Override
    double getArea() {
        return Math.PI * radius * radius;
    }
}

class Rectangle extends Shape {
    double width, height;
    
    Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    void draw() {
        System.out.println("Drawing a rectangle");
    }
    
    @Override
    double getArea() {
        return width * height;
    }
}

class Triangle extends Shape {
    double base, height;
    
    Triangle(double base, double height) {
        this.base = base;
        this.height = height;
    }
    
    @Override
    void draw() {
        System.out.println("Drawing a triangle");
    }
    
    @Override
    double getArea() {
        return 0.5 * base * height;
    }
}

public class Main {
    public static void main(String[] args) {
        // Array of Shape references holding different object types
        Shape[] shapes = new Shape[3];
        shapes[0] = new Circle(5.0);
        shapes[1] = new Rectangle(4.0, 6.0);
        shapes[2] = new Triangle(3.0, 8.0);
        
        // Polymorphism in action
        for (Shape shape : shapes) {
            shape.draw();
            System.out.println("Area: " + shape.getArea());
            System.out.println();
        }
    }
}
```

**Output:**
```
Drawing a circle
Area: 78.53981633974483

Drawing a rectangle
Area: 24.0

Drawing a triangle
Area: 12.0
```

---

## Compile-Time vs Runtime Polymorphism Comparison

| Feature | Compile-Time | Runtime |
|---------|-------------|---------|
| **Also Known As** | Static Polymorphism | Dynamic Polymorphism |
| **Achieved By** | Method Overloading | Method Overriding |
| **Binding Time** | Compile time | Runtime |
| **Performance** | Faster (resolved early) | Slower (resolved at runtime) |
| **Method Name** | Same name, different params | Same signature |
| **Inheritance** | Not required | Required |
| **Flexibility** | Less flexible | More flexible |
| **Example** | add(int, int) vs add(double, double) | Animal.makeSound() overridden in Dog |

### Example Demonstrating Both Types

```java
class MathOperations {
    // Compile-time polymorphism (Method Overloading)
    int multiply(int a, int b) {
        return a * b;
    }
    
    double multiply(double a, double b) {
        return a * b;
    }
    
    int multiply(int a, int b, int c) {
        return a * b * c;
    }
    
    // Method to demonstrate runtime polymorphism
    void displayResult(int result) {
        System.out.println("Result: " + result);
    }
}

class AdvancedMath extends MathOperations {
    // Runtime polymorphism (Method Overriding)
    @Override
    void displayResult(int result) {
        System.out.println("Advanced Result: " + result + " (calculated)");
    }
}

public class Main {
    public static void main(String[] args) {
        MathOperations math = new MathOperations();
        
        // Compile-time polymorphism - compiler decides which multiply()
        System.out.println(math.multiply(5, 3));        // Calls multiply(int, int)
        System.out.println(math.multiply(5.5, 2.2));    // Calls multiply(double, double)
        System.out.println(math.multiply(2, 3, 4));     // Calls multiply(int, int, int)
        
        System.out.println();
        
        // Runtime polymorphism - JVM decides at runtime
        MathOperations basicMath = new MathOperations();
        MathOperations advMath = new AdvancedMath();
        
        basicMath.displayResult(100);  // Calls MathOperations version
        advMath.displayResult(100);    // Calls AdvancedMath version (runtime decision)
    }
}
```

**Output:**
```
15
12.1
24

Result: 100
Advanced Result: 100 (calculated)
```

---

## Use Cases of Polymorphism

### 1. Code Reusability and Maintainability

```java
// Without polymorphism - BAD APPROACH
class PaymentProcessor {
    void processCreditCardPayment(CreditCard card) {
        // process credit card
    }
    
    void processPayPalPayment(PayPal paypal) {
        // process PayPal
    }
    
    void processBitcoinPayment(Bitcoin bitcoin) {
        // process Bitcoin
    }
}

// With polymorphism - GOOD APPROACH
interface Payment {
    void processPayment(double amount);
}

class CreditCard implements Payment {
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing credit card payment: $" + amount);
    }
}

class PayPal implements Payment {
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing PayPal payment: $" + amount);
    }
}

class Bitcoin implements Payment {
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing Bitcoin payment: $" + amount);
    }
}

class PaymentProcessor {
    // One method handles all payment types!
    void processPayment(Payment payment, double amount) {
        payment.processPayment(amount);
    }
}

public class Main {
    public static void main(String[] args) {
        PaymentProcessor processor = new PaymentProcessor();
        
        processor.processPayment(new CreditCard(), 100.0);
        processor.processPayment(new PayPal(), 50.0);
        processor.processPayment(new Bitcoin(), 200.0);
    }
}
```

### 2. Framework and Library Design

```java
// Collections Framework example
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // List is the interface (parent type)
        List<String> arrayList = new ArrayList<>();
        List<String> linkedList = new LinkedList<>();
        
        // Both use the same interface methods
        arrayList.add("Java");
        linkedList.add("Python");
        
        // Can be passed to same method
        printList(arrayList);
        printList(linkedList);
    }
    
    // Single method works with any List implementation
    static void printList(List<String> list) {
        for (String item : list) {
            System.out.println(item);
        }
    }
}
```

### 3. Plugin Architecture

```java
interface Plugin {
    void execute();
    String getName();
}

class Logger implements Plugin {
    @Override
    public void execute() {
        System.out.println("Logging data...");
    }
    
    @Override
    public String getName() {
        return "Logger Plugin";
    }
}

class Database implements Plugin {
    @Override
    public void execute() {
        System.out.println("Connecting to database...");
    }
    
    @Override
    public String getName() {
        return "Database Plugin";
    }
}

class EmailSender implements Plugin {
    @Override
    public void execute() {
        System.out.println("Sending email...");
    }
    
    @Override
    public String getName() {
        return "Email Sender Plugin";
    }
}

class Application {
    private List<Plugin> plugins = new ArrayList<>();
    
    void registerPlugin(Plugin plugin) {
        plugins.add(plugin);
        System.out.println("Registered: " + plugin.getName());
    }
    
    void runAllPlugins() {
        System.out.println("\nExecuting all plugins:");
        for (Plugin plugin : plugins) {
            plugin.execute();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Application app = new Application();
        
        // Dynamically add different plugins
        app.registerPlugin(new Logger());
        app.registerPlugin(new Database());
        app.registerPlugin(new EmailSender());
        
        app.runAllPlugins();
    }
}
```

### 4. Sorting Custom Objects

```java
class Employee implements Comparable<Employee> {
    String name;
    int age;
    double salary;
    
    Employee(String name, int age, double salary) {
        this.name = name;
        this.age = age;
        this.salary = salary;
    }
    
    @Override
    public int compareTo(Employee other) {
        return this.name.compareTo(other.name);  // Sort by name
    }
    
    @Override
    public String toString() {
        return name + " (" + age + ") - $" + salary;
    }
}

public class Main {
    public static void main(String[] args) {
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee("Charlie", 35, 75000));
        employees.add(new Employee("Alice", 28, 65000));
        employees.add(new Employee("Bob", 42, 85000));
        
        // Polymorphism allows Collections.sort() to work with any Comparable
        Collections.sort(employees);
        
        System.out.println("Sorted by name:");
        for (Employee emp : employees) {
            System.out.println(emp);
        }
        
        // Custom comparator for different sorting
        Collections.sort(employees, new Comparator<Employee>() {
            @Override
            public int compare(Employee e1, Employee e2) {
                return Double.compare(e2.salary, e1.salary);  // Descending
            }
        });
        
        System.out.println("\nSorted by salary (descending):");
        for (Employee emp : employees) {
            System.out.println(emp);
        }
    }
}
```

### 5. Strategy Pattern (Design Pattern)

```java
interface SortingStrategy {
    void sort(int[] array);
}

class BubbleSort implements SortingStrategy {
    @Override
    public void sort(int[] array) {
        System.out.println("Sorting using Bubble Sort");
        // bubble sort implementation
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }
}

class QuickSort implements SortingStrategy {
    @Override
    public void sort(int[] array) {
        System.out.println("Sorting using Quick Sort");
        quickSort(array, 0, array.length - 1);
    }
    
    private void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }
    
    private int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        return i + 1;
    }
}

class MergeSort implements SortingStrategy {
    @Override
    public void sort(int[] array) {
        System.out.println("Sorting using Merge Sort");
        mergeSort(array, 0, array.length - 1);
    }
    
    private void mergeSort(int[] arr, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);
            merge(arr, left, mid, right);
        }
    }
    
    private void merge(int[] arr, int left, int mid, int right) {
        // merge implementation
        int n1 = mid - left + 1;
        int n2 = right - mid;
        int[] L = new int[n1];
        int[] R = new int[n2];
        
        System.arraycopy(arr, left, L, 0, n1);
        System.arraycopy(arr, mid + 1, R, 0, n2);
        
        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k++] = L[i++];
            } else {
                arr[k++] = R[j++];
            }
        }
        
        while (i < n1) arr[k++] = L[i++];
        while (j < n2) arr[k++] = R[j++];
    }
}

class Sorter {
    private SortingStrategy strategy;
    
    void setStrategy(SortingStrategy strategy) {
        this.strategy = strategy;
    }
    
    void performSort(int[] array) {
        strategy.sort(array);
    }
}

public class Main {
    public static void main(String[] args) {
        int[] data1 = {64, 34, 25, 12, 22, 11, 90};
        int[] data2 = {64, 34, 25, 12, 22, 11, 90};
        int[] data3 = {64, 34, 25, 12, 22, 11, 90};
        
        Sorter sorter = new Sorter();
        
        // Change strategy at runtime using polymorphism
        sorter.setStrategy(new BubbleSort());
        sorter.performSort(data1);
        System.out.println(Arrays.toString(data1));
        
        sorter.setStrategy(new QuickSort());
        sorter.performSort(data2);
        System.out.println(Arrays.toString(data2));
        
        sorter.setStrategy(new MergeSort());
        sorter.performSort(data3);
        System.out.println(Arrays.toString(data3));
    }
}
```

---

## Benefits of Polymorphism

1. **Code Reusability** - Write once, use with multiple types
2. **Flexibility** - Easy to extend with new implementations
3. **Maintainability** - Changes in one place affect all implementations
4. **Loose Coupling** - Reduces dependencies between classes
5. **Extensibility** - Add new functionality without modifying existing code
6. **Clean Code** - More readable and organized codebase

## Summary

- **Compile-time polymorphism** (overloading) = Same name, different
  parameters, resolved at compile time
- **Runtime polymorphism** (overriding) = Same signature, different
  implementations, resolved at runtime
- Polymorphism enables writing flexible, maintainable, and extensible code
- Essential for design patterns, frameworks, and large-scale applications
- Fundamental to achieving loose coupling and high cohesion in OOP design
