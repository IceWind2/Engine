import java.awt.*;
import java.util.ArrayList;
import static java.lang.Math.*;

class Actor {
    public Vector min = new Vector(), max = new Vector();
    private Polygon polShape = new Polygon();
    Polygon getPolShape() {
        return polShape;
    }
    private void updateAABB() {
        polShape.reset();
        for (Vector aShape : Shape) {
            polShape.addPoint((int) (aShape.x + Location.x), (int) (aShape.y + Location.y));
        }
        Rectangle R = polShape.getBounds();
        min.x = R.x;
        min.y = R.y;
        max.x = R.x + R.width;
        max.y = R.y + R.height;
    }

    private Vector Location = new Vector();
    Vector getLocation() {
        return Location;
    }
    void setLocation(double X, double Y) {
        Translate(X - Location.x, Y - Location.y);
    }
    void setLocation(Vector V) {
        Translate(V);
    }
    void Translate(double X, double Y) {
        Location.x += X;
        Location.y += Y;
        updateAABB();
    }
    void Translate(Vector V) {
        Translate(V.x, V.y);
    }

    private double Rotation = 0.0;
    double getRotation() {
        return Rotation;
    }
    void setRotation(double Angle) {
        Rotate(Angle - Rotation);
    }
    void Rotate(double Angle) {
        Rotation += Angle;
        for (int i = 0; i < Shape.size(); i++) {
            Shape.set(i, new Vector(Shape.get(i).x * cos(Angle) + Shape.get(i).y * -sin(Angle), Shape.get(i).x * sin(Angle) + Shape.get(i).y * cos(Angle)));
        }
        updateAABB();
     }

    private ArrayList<Vector> Shape = new ArrayList<>();
    void addPoint(double X, double Y) {
        Shape.add(new Vector(X, Y));
        updateAABB();
    }
    void addPoint(Vector V) {
        addPoint(V.x, V.y);
    }
    Vector Shape (int I) {
        return Shape.get(I);
    }
    int ShapeSize() {
        return Shape.size();
    }

    Vector linVelocity = new Vector();
    double angVelocity = 0.0;

    private double Mass = 0, invMass = 0;
    void setMass(double M) {
        Mass = M;
        if (M == 0) {
            invMass = 0;
        }
        else {
            invMass = 1 / M;
        }
    }
    double getMass() {
        return Mass;
    }
    double getInvMass() {
        return invMass;
    }

    Vector Force = new Vector();
    double Torque = 0.0;

    public void ComputeForces(Vector Force, Vector Point) {
        this.Force = this.Force.plus(Force);
        this.Torque += Point.minus(this.getLocation()).x * Force.y - Point.minus(this.getLocation()).y * Force.x;
    }

    Actor() {}
    Actor(double X, double Y) {
        setLocation(X, Y);
    }
    Actor (double X, double Y, double M) {
        setLocation(X, Y);
        setMass(M);
    }
}
