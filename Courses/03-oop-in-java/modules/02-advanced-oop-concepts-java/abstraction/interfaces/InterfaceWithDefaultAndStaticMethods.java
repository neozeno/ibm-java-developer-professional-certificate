interface Vehicle {
    // Constants
    String type = "motorized";
    int MAX_SPEED = 200;

    // Abstract methods
    void start();
    void stop();
    void accelerate(int speed);

    // Default methods (provide default implementations)
    default void honk() {
        System.out.println("Beep! Beep!");
    }

    default void displayInfo() {
        System.out.printf("This is a vehicle with a maximum speed of %d km/h\n", MAX_SPEED);
    }

    // Static method (belongs to the interface not to its instances)
    static void vehicleType() {
        System.out.printf("This is a %s vehicle.", type);
    }
}

class Car implements Vehicle {
    private final String make;
    private final String model;
    private int currentSpeed = 0;

    public Car(String make, String model) {
        this.make = make;
        this.model = model;
    }

    @Override
    public void start() {
        System.out.printf("%s is starting...\n", model);
    }

    @Override
    public void stop() {
        currentSpeed = 0;
        System.out.printf("%s has stopped.\n", model);
    }

    @Override
    public void accelerate(int speed) {
        currentSpeed += speed;

        if (currentSpeed > MAX_SPEED) {
            currentSpeed = MAX_SPEED;
        }

        System.out.printf("%s accelerating to %d km/h\n", model, currentSpeed);
    }

    // Default methods can be overridden if needed
    @Override
    public void displayInfo() {
        System.out.printf("This is a %s %s with a maximum speed of %d km/h\n", make, model, MAX_SPEED);
    }
}

class Motorcycle implements Vehicle {
    private final String brand;
    private final String model;
    private int currentSpeed = 0;

    public Motorcycle(String brand, String model) {
        this.brand = brand;
        this.model = model;
    }

    @Override
    public void start() {
        System.out.printf("Vroom! %s is starting...\n", model);
    }

    @Override
    public void stop() {
        System.out.printf("Vroom! %s has stopped.\n", model);
    }

    @Override
    public void accelerate(int speed) {
        currentSpeed += speed;
        System.out.printf("%s accelerating to %d km/h\n", model, currentSpeed);
    }

    @Override
    public void displayInfo() {
        System.out.printf("This is a %s %s motorcycle with a maximum speed of %d km/h\n", brand, model, MAX_SPEED);
    }
}

public class InterfaceWithDefaultAndStaticMethods {
    public static void main(String[] args) {
        Car car = new Car("Tesla", "Model 3");
        Motorcycle motorcycle = new Motorcycle("BMW", "S 1000 RR");

        System.out.println("=== Car ===");
        car.displayInfo();
        car.start();
        car.accelerate(60);
        car.honk();
        car.stop();

        System.out.println("=== Motorcycle ===");
        motorcycle.displayInfo();
        motorcycle.start();
        motorcycle.accelerate(60);
        motorcycle.honk();
        motorcycle.stop();

        System.out.println();
        Vehicle.vehicleType();  // Static method called via interface
    }
}