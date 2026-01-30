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

    // Concrete methods (inherited by all subclasses)
    void sleep() {
        System.out.println(name + " is sleeping.");
    }

    void displayInfo() {
        System.out.println("Name: " + name + ", Age: " + age);
    }
}

class Dog extends Animal {
    String breed;

    Dog(String name, int age, String breed) {
        super(name, age);
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
        String movement = canFly == true ? "flies in the sky" : "walks on the ground";
        System.out.println(name + " " + movement);
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
        System.out.println(name + " makes bubbles!");
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
