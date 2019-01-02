import static java.lang.Math.*;

class Vector {
    double x, y;
    Vector() {
        x = 0;
        y = 0;
    };
    Vector(double X, double Y) {
        x = X;
        y = Y;
    }
    Vector(Vector V) {
        this.x = V.x;
        this.y = V.y;
    }

    Vector copy() {
        return new Vector(this);
    }

    void reset() {
        x = 0;
        y = 0;
    }

    double mod() {
        return sqrt(x * x + y * y);
    }
    double dotProduct(Vector V) {
        return this.x * V.x + this.y * V.y;
    }
    double crossProduct(Vector V) {
        return this.x * V.y - this.y * V.x;
    }
    Vector negate() {
        return new Vector(-this.x, -this.y);
    }
    Vector minus(Vector V) {
        return new Vector(this.x - V.x, this.y - V.y);
    }
    Vector plus(Vector V) {
        return new Vector(this.x + V.x, this.y + V.y);
    }
    Vector divide(double Divider) {
        return new Vector(this.x / Divider, this.y / Divider);
    }
    Vector multiply(double Multiplier) {
        return new Vector(this.x * Multiplier, this.y * Multiplier);
    }
    double degree(Vector V) {
        if (this.equals(V)) {
            return  0;
        }
        return acos((this.x * V.x + this.y * V.y) / this.mod() / V.mod());
    }

    boolean equals(Vector V) {
        return abs(this.x - V.x) < 0.01 && abs(this.y - V.y) < 0.01;
    }
    void normalize() {
        double sum = sqrt(x * x + y * y);
        x = x / sum;
        y = y / sum;
    }
    Vector normal() {
        double sum = sqrt(x * x + y * y);
        return new Vector(x / sum, y / sum);
    }

    void print() {
        System.out.printf("%f %f\n", x, y);
    }
}
