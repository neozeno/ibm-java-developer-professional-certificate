interface Drawable {
    // Abstract method
    void draw();
}

interface Resizable {
    void resize(int width, int height);
}

// Class implementing multiple interfaces
class Circle implements Drawable, Resizable {
    int radius;
    private int x, y;

    Circle(int x, int y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    @Override
    public void draw() {
        System.out.printf("Drawing Circle at (%d, %d) with radius %d%n", x, y, radius);
    }

    @Override
    public void resize(int width, int height) {
        this.radius = width / 2;
        System.out.printf("Circle resized with radius %d%n", radius);
    }
}

class Rectangle implements Drawable,  Resizable {
    private int x, y, width, height;

    Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw() {
        System.out.printf("Drawing rectangle at (%d, %d) with size %d x %d%n", x, y, width, height);
    }

    @Override
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        System.out.printf("Rectangle resized with size %d x %d%n", width, height);
    }
}

public class BasicInterface {
    public static void main(String[] args) {
       Circle circle = new Circle(50, 100, 100);
       Rectangle rectangle = new Rectangle(200, 100, 50, 50);

       // Polymorphism with interfaces
       Drawable[] shapes = {circle, rectangle};
       Resizable[] resizables = {circle, rectangle};

       for (Drawable shape : shapes) {
           shape.draw();
       }

       for (Resizable resizable : resizables) {
           resizable.resize(150, 150);
       }
    }
}
