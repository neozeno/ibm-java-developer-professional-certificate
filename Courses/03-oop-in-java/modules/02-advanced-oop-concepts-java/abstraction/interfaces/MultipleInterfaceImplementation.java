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

// Ducks can fly, swim, and walk
class Duck implements Flyable, Swimmable, Walkable {
    private final String name;

    Duck(String name) {
        this.name = name;
    }

    @Override
    public void fly() {
        System.out.printf("%s is flying\n", name);
    }

    @Override
    public void swim() {
        System.out.printf("%s is swimming\n", name);
    }

    @Override
    public void walk() {
        System.out.printf("%s is waddling\n", name);
    }
}

// Fishes can only swim
class Fish implements Swimmable {
    private final String name;

    Fish(String name) {
        this.name = name;
    }

    @Override
    public void swim() {
        System.out.printf("%s is swimming gracefully\n", name);
    }
}

// Airplanes can fly
class Airplane implements Flyable {
    private final String model;

    Airplane(String model) {
        this.model = model;
    }

    @Override
    public void fly() {
        System.out.printf("%s is flying at cruise altitude", model);
    }

     @Override
     public void takeOff() {
         System.out.printf("%s is taking off from the runway\n", model);
     }
}

public class MultipleInterfaceImplementation {
    public static void main(String[] args) {
        Duck duck = new Duck("Donald");
        Fish fish = new Fish("Nemo");
        Airplane plane = new Airplane("Boeing 747");

        System.out.println("=== Duck ===");
        duck.walk();
        duck.takeOff();
        duck.fly();
        duck.swim();
        System.out.println();

        System.out.println("=== Fish ===");
        fish.dive();
        fish.swim();
        System.out.println();

        System.out.println("=== Airplane ===");
        plane.takeOff();
        plane.fly();
    }
}
