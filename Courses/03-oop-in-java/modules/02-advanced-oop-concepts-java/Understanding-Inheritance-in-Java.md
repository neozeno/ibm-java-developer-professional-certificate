## Understading Inheritance in Java

Inheritance is a fundamental Object-Oriented Programming (OOP) concept
that allows a class (called a subclass or child class) to inherit
properties and methods from another class (called a superclass or parent class).
It establishes an "is-a" relationship between classes.

```java
class Subclass extends Superclass {
    // subclass body
}
```

### Code Reusability

Inheritance promotes code reusability by allowing you to:

- Define common attributes and behaviors once in a parent class
- Reuse them across multiple child classes without duplicating code
- Reduce maintenance effort—changes to common functionality only need to be made in one place

For example:

```java
// Parent class with common functionality
class Animal {
    String name;
    int age;
    
    void eat() {
        System.out.println(name + " is eating");
    }
    
    void sleep() {
        System.out.println(name + " is sleeping");
    }
}

// Dog reuses Animal's properties and methods
class Dog extends Animal {
    void bark() {
        System.out.println(name + " is barking");
    }
}

// Cat reuses Animal's properties and methods
class Cat extends Animal {
    void meow() {
        System.out.println(name + " is meowing");
    }
}

// Usage
public class Main {
    public static void main(String[] args) {
        Dog dog = new Dog();
        dog.name = "Buddy";
        dog.eat();    // Inherited from Animal
        dog.sleep();  // Inherited from Animal
        dog.bark();   // Dog's own method
        
        Cat cat = new Cat();
        cat.name = "Whiskers";
        cat.eat();    // Inherited from Animal
        cat.meow();   // Cat's own method
    }
}
```

### Method Overriding

Method overriding allows a subclass to provide a specific implementation
of a method that is already defined in its superclass.

#### Rules for Method Overriding:

- Method signature (name and parameters) must be identical
- Access modifier cannot be more restrictive than the parent's method
- Return type must be the same or a covariant type
- Use `@Override` annotation for clarity and compile-time checking

**Example:**

```java
class Vehicle {
    void start() {
        System.out.println("Vehicle is starting");
    }
    
    void stop() {
        System.out.println("Vehicle is stopping");
    }
}

class Car extends Vehicle {
    @Override
    void start() {
        System.out.println("Car engine is starting with a key");
    }
    
    // stop() is inherited without modification
}

class ElectricCar extends Vehicle {
    @Override
    void start() {
        System.out.println("Electric car is starting silently");
    }
    
    @Override
    void stop() {
        System.out.println("Electric car is regenerating energy while stopping");
    }
}

// Usage
public class Main {
    public static void main(String[] args) {
        Vehicle v = new Vehicle();
        v.start();  // Output: Vehicle is starting
        
        Car c = new Car();
        c.start();  // Output: Car engine is starting with a key
        c.stop();   // Output: Vehicle is stopping (inherited)
        
        ElectricCar e = new ElectricCar();
        e.start();  // Output: Electric car is starting silently
        e.stop();   // Output: Electric car is regenerating energy while stopping
    }
}
```

### Code Organization

Inheritance improves code organization by:

- Creating logical class hierarchies that mirror real-world relationships
- Grouping related classes together
- Making code more maintainable and easier to understand
- Enabling polymorphism for flexible code design

For example:

```java
// Organized hierarchy for an employee management system
class Employee {
    String name;
    int id;
    double baseSalary;
    
    void work() {
        System.out.println(name + " is working");
    }
    
    double calculateSalary() {
        return baseSalary;
    }
}

class Manager extends Employee {
    double bonus;
    
    @Override
    double calculateSalary() {
        return baseSalary + bonus;
    }
    
    void conductMeeting() {
        System.out.println(name + " is conducting a meeting");
    }
}

class Developer extends Employee {
    String programmingLanguage;
    
    void writeCode() {
        System.out.println(name + " is writing " + programmingLanguage + " code");
    }
}

class Intern extends Employee {
    String mentor;
    
    void learn() {
        System.out.println(name + " is learning from " + mentor);
    }
}
```

### What Is Inherited and What Is Not

**✅ What IS Inherited:**

- Public members (fields and methods)
- Protected members (fields and methods)
- Default (package-private) members - only if the subclass is in the same package

**❌ What is NOT Inherited:**

- Private members - not directly accessible, but can be accessed through public/protected getters/setters
- Constructors - not inherited, but the parent constructor is called via super()
- Static members - not inherited in the traditional sense, but can be accessed via the parent class name

Example:

```java
class Parent {
    public String publicField = "Public";
    protected String protectedField = "Protected";
    String defaultField = "Default";  // package-private
    private String privateField = "Private";
    
    public Parent() {
        System.out.println("Parent constructor");
    }
    
    public void publicMethod() {
        System.out.println("Public method");
    }
    
    protected void protectedMethod() {
        System.out.println("Protected method");
    }
    
    void defaultMethod() {
        System.out.println("Default method");
    }
    
    private void privateMethod() {
        System.out.println("Private method");
    }
    
    public String getPrivateField() {
        return privateField;
    }
    
    static void staticMethod() {
        System.out.println("Static method");
    }
}

class Child extends Parent {
    public Child() {
        super();  // Calls parent constructor
        System.out.println("Child constructor");
    }
    
    void testInheritance() {
        System.out.println(publicField);      // ✅ Accessible
        System.out.println(protectedField);   // ✅ Accessible
        System.out.println(defaultField);     // ✅ Accessible (same package)
        // System.out.println(privateField);  // ❌ Not accessible
        System.out.println(getPrivateField()); // ✅ Accessible via public method
        
        publicMethod();     // ✅ Accessible
        protectedMethod();  // ✅ Accessible
        defaultMethod();    // ✅ Accessible (same package)
        // privateMethod(); // ❌ Not accessible
        
        Parent.staticMethod(); // Static members accessed via class name
    }
}
```

### Types of Inheritance in Java

1. **Single Inheritance:** One class inherits from one superclass.

```java
class Animal {
    void eat() {
        System.out.println("Eating...");
    }
}

class Dog extends Animal {
    void bark() {
        System.out.println("Barking...");
    }
}

// Dog inherits from Animal
```

2. **Multilevel Inheritance**: A chain of inheritance where a class inherits
from a subclass.

```java
class Animal {
    void eat() {
        System.out.println("Eating...");
    }
}

class Mammal extends Animal {
    void breathe() {
        System.out.println("Breathing...");
    }
}

class Dog extends Mammal {
    void bark() {
        System.out.println("Barking...");
    }
}

// Dog -> Mammal -> Animal
// Dog has access to eat(), breathe(), and bark()

public class Main {
    public static void main(String[] args) {
        Dog dog = new Dog();
        dog.eat();     // From Animal
        dog.breathe(); // From Mammal
        dog.bark();    // From Dog
    }
}
```

3. **Hierarchical Inheritance:** Multiple classes inherit from the same superclass.

```java
class Vehicle {
    String brand;
    
    void start() {
        System.out.println("Vehicle starting");
    }
}

class Car extends Vehicle {
    void drive() {
        System.out.println("Car is driving");
    }
}

class Motorcycle extends Vehicle {
    void ride() {
        System.out.println("Motorcycle is riding");
    }
}

class Truck extends Vehicle {
    void haul() {
        System.out.println("Truck is hauling");
    }
}

// Car, Motorcycle, and Truck all inherit from Vehicle
```

4. **Multiple Inheritance (NOT Supported Directly in Java):** A class
inheriting from multiple classes. **Java does not support this with classes**
to avoid the "Diamond Problem."

```java
// ❌ This is NOT allowed in Java
class A {
    void method() { }
}

class B {
    void method() { }
}

// class C extends A, B { }  // Compilation error!
```

**However**, Java achieves multiple inheritance through **interfaces**:

```java
interface Flyable {
    void fly();
}

interface Swimmable {
    void swim();
}

// ✅ A class can implement multiple interfaces
class Duck implements Flyable, Swimmable {
    @Override
    public void fly() {
        System.out.println("Duck is flying");
    }
    
    @Override
    public void swim() {
        System.out.println("Duck is swimming");
    }
}

public class Main {
    public static void main(String[] args) {
        Duck duck = new Duck();
        duck.fly();
        duck.swim();
    }
}
```

#### The Diamond Problem (Why Multiple Inheritance is Not Allowed)

```java
// If Java allowed multiple inheritance:
class A {
    void display() {
        System.out.println("A");
    }
}

class B extends A {
    void display() {
        System.out.println("B");
    }
}

class C extends A {
    void display() {
        System.out.println("C");
    }
}

// If this were allowed:
// class D extends B, C { }

// Which display() method would D inherit? B's or C's?
// This ambiguity is the diamond problem.
```

### Summary Table

|--------------|----------------------------------------------|-------------------------------|
| Type         | Description                                  | Supported in Java             |
|--------------|----------------------------------------------|-------------------------------|
| Single       | Class extends one superclass                 | ✅ Yes                        |
| Multilevel   | Chain of inheritance (A→B→C)                 | ✅ Yes                        |
| Hierarchical | Multiple classes inherit from one superclass | ✅ Yes                        |
| Multiple     | Class inherits from multiple classes         | ❌ No (use interfaces)        |
| Hybrid       | Combination of multiple types                | ⚠️ Partially (via interfaces) |
|--------------|----------------------------------------------|-------------------------------|

### Best Practices

- Use inheritance for "is-a" relationships (e.g., Dog is an Animal)
- Favor composition over inheritance when appropriate
- Keep inheritance hierarchies shallow (avoid deep chains)
- Override methods with `@Override` annotation
- Call `super()` explicitly when needed in constructors
- Don't break the Liskov Substitution Principle - subclasses should be substitutable for their parent classes

## Polymorphism
