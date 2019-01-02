import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class physics extends JComponent implements ActionListener, KeyListener {

    private static double delay = 10.0, dt = delay / 1000.0;                                        // Millis between steps, period of integration
    private double cf = 1;
    Vector ff = new Vector();
    private double Scale = 100.0; // Pixels in 1 meter
    private Random rnd = new Random(System.currentTimeMillis());
    static Collision col = new Collision();
    private static ArrayList <Actor> Actors = new ArrayList<>();


    Vector tmp = new Vector();

    public static void main(String[] args) {
        JFrame window = new JFrame("Window");
        physics world = new physics();
        window.add(world);
        window.pack();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        Timer t = new Timer ((int)delay, world);
        t.start();
        window.addKeyListener(world);

        Actor New_actor = new Actor(), n = new Actor();


        New_actor.setLocation(200, 300);
        n.setLocation(500, 450);
        New_actor.addPoint(100, 50);
        New_actor.addPoint(100, -50);
        New_actor.addPoint(-100, -50);
        New_actor.addPoint(-100, 50);
        New_actor.setMass(10);
        New_actor.Rotate(1);
        Actors.add(New_actor);
        n.addPoint(100, 50);
        n.addPoint(100, -50);
        n.addPoint(-100, -50);
        n.addPoint(-100, 50);
        n.Rotate(0.468);
        n.setMass(10);
        Actors.add(n);
        New_actor = new Actor();
        New_actor.setLocation(600, 200);
        New_actor.addPoint(100, 50);
        New_actor.addPoint(100, -50);
        New_actor.addPoint(-100, -50);
        New_actor.addPoint(-100, 50);
        New_actor.setMass(10);
        New_actor.Rotate(-1);
       // Actors.add(New_actor);
        New_actor = new Actor();
        New_actor.setLocation(300, 100);
        New_actor.addPoint(100, 50);
        New_actor.addPoint(100, -50);
        New_actor.addPoint(-100, -50);
        New_actor.addPoint(-100, 50);
        New_actor.setMass(10);
        New_actor.Rotate(3);
      //  Actors.add(New_actor);

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(new Color(63, 171, 126));
        g.fillRect(0,0, 800, 600);
        g.setColor(new Color(0, 84, 134));

        for (Actor aActor : Actors) {
            g.fillPolygon(aActor.getPolShape());
        }
        g.setColor(new Color(0,0,0));
        for (Actor aActor : Actors) {
            for (int i = 0; i < aActor.ShapeSize() - 1; i++) {
                g.drawLine((int)(aActor.getLocation().x + aActor.Shape(i).x),(int)(aActor.getLocation().y + aActor.Shape(i).y),(int)(aActor.getLocation().x + aActor.Shape(i + 1).x),(int)(aActor.getLocation().y + aActor.Shape(i + 1).y));
            }
            g.drawLine((int)(aActor.getLocation().x + aActor.Shape(0).x),(int)(aActor.getLocation().y + aActor.Shape(0).y),(int)(aActor.getLocation().x + aActor.Shape(aActor.ShapeSize() - 1).x),(int)(aActor.getLocation().y + aActor.Shape(aActor.ShapeSize() - 1).y));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Vector linAcceleration;
        double angAcceleration;
        Actor firstActor, secondActor;
       /* for (int i = 0; i < Actors.size(); i++) {
            for (int q = i + 1; q < Actors.size(); q++) {
                if (Actors.get(q).getMass() != 0) {
                    secondActor = Actors.get(q);
                    firstActor = Actors.get(i);
                }
                else {
                    secondActor = Actors.get(i);
                    firstActor = Actors.get(q);
                }
                if (firstActor.getMass() == 0 && secondActor.getMass() == 0) {
                    continue;
                }
                col.detectCollision(firstActor, secondActor);
                if (col.MTV().mod() < 1.0 || col.intersect) {
                    Vector R = col.bPoint().minus(secondActor.getLocation()), Norm = col.MTV().normal();
                    if (col.intersect) {
                        Norm = Norm.negate();
                    }
                    else {
                        Vector relVel = secondActor.linVelocity.plus(new Vector(-secondActor.angVelocity * col.bPoint().minus(secondActor.getLocation()).y, secondActor.angVelocity * col.bPoint().minus(secondActor.getLocation()).x));
                        if (relVel.dotProduct(col.MTV()) >= 0) {
                            continue;
                        }
                    }
                    double lm = Norm.x * secondActor.getInvMass() * Norm.x + Norm.y * secondActor.getInvMass() * Norm.y + R.crossProduct(Norm) * 12.0 * secondActor.getInvMass() / 50000.0 * R.crossProduct(Norm);
                    lm = -1.0 * (Norm.x * secondActor.linVelocity.x + Norm.y * secondActor.linVelocity.y + R.crossProduct(Norm) * secondActor.angVelocity) / lm;
                    secondActor.linVelocity.x += lm * Norm.x * secondActor.getInvMass();
                    secondActor.linVelocity.y += lm * Norm.y * secondActor.getInvMass();
                    secondActor.angVelocity += lm * R.crossProduct(Norm) * 12.0 * secondActor.getInvMass() / 50000.0;
                }
            }
        }*/
       for (Actor aActor: Actors) {
           Vector Norm = new Vector(), Point = null;
           for (int i = 0; i < aActor.ShapeSize(); ++i) {
               if (aActor.Shape(i).plus(aActor.getLocation()).x < 0) {
                    Point = aActor.Shape(i);
                    Norm = new Vector(1,0);
               }
               if (aActor.Shape(i).plus(aActor.getLocation()).x > 800) {
                   Point = aActor.Shape(i);
                   Norm = new Vector(-1,0);
               }
               if (aActor.Shape(i).plus(aActor.getLocation()).y < 0) {
                   Point = aActor.Shape(i);
                   Norm = new Vector(0,1);
               }
               if (aActor.Shape(i).plus(aActor.getLocation()).y > 600) {
                   Point = aActor.Shape(i);
                   Norm = new Vector(0,-1);
               }
               if (Point != null) {
                  /* Vector relVel = new Vector (-aActor.angVelocity * Point.y, aActor.angVelocity * Point.x);
                   if (relVel.dotProduct(Norm) >= 0.0) {
                       continue;
                   }*/
                   double lm = Norm.x * aActor.getInvMass() * Norm.x + Norm.y * aActor.getInvMass() * Norm.y + Point.crossProduct(Norm) * 12.0 * aActor.getInvMass() / 50000.0 * Point.crossProduct(Norm);
                   lm = -2.0 * (Norm.x * aActor.linVelocity.x + Norm.y * aActor.linVelocity.y + Point.crossProduct(Norm) * aActor.angVelocity) / lm;
                   aActor.linVelocity.x += lm * Norm.x * aActor.getInvMass();
                   aActor.linVelocity.y += lm * Norm.y * aActor.getInvMass();
                   aActor.angVelocity += lm * Point.crossProduct(Norm) * 12.0 * aActor.getInvMass() / 50000.0;
                   Point = null;
               }
           }
       }
        for (Actor aActor: Actors) {
           // aActor.ComputeForces(new Vector(0, 5000), aActor.getLocation());
            aActor.ComputeForces(ff, aActor.getLocation());
            linAcceleration = aActor.Force.multiply(aActor.getInvMass());
            aActor.linVelocity.x += linAcceleration.x * dt;
            aActor.linVelocity.y += linAcceleration.y * dt;
            aActor.Translate(aActor.linVelocity.x * dt, aActor.linVelocity.y * dt);
            angAcceleration = aActor.Torque * 12.0 * aActor.getInvMass() / 50000;
            aActor.angVelocity += angAcceleration * dt;
            aActor.Rotate(aActor.angVelocity * dt);
            aActor.Force.reset();
            aActor.Torque = 0.0;
        }
       // firstActor = Actors.get(4);
       // Vector v = new Vector(firstActor.Shape(3).multiply(-firstActor.angVelocity).y + firstActor.linVelocity.x, firstActor.Shape(3).multiply(firstActor.angVelocity).x + firstActor.linVelocity.y);
        //System.out.printf("%f %f\n", v.x, v.y);
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == 'ц') {
            ff.y = -15000;
        }
        if (e.getKeyChar() == 'ф') {
            ff.x = -15000;
        }
        if (e.getKeyChar() == 'ы') {
            ff.y = 15000;
        }
        if (e.getKeyChar() == 'в') {
            ff.x = 15000;
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            /*Actor New_actor = new Actor(), n = new Actor();
            //New_actor.setLocation(rnd.nextDouble() * (800.0 - 100), rnd.nextDouble() * (600.0 - 100));
            double cfx, cfy;
            if (rnd.nextDouble() <= 0.5) {
                cfx = 1.0;
            }
            else {
                cfx = -1.0;
            }
            if (rnd.nextDouble() <= 0.5) {
                cfy = 1.0;
            }
            else {
                cfy = -1.0;
            }
           // New_actor.linVelocity = new Vector(rnd.nextDouble() * 300.0 * cfx, rnd.nextDouble() * 300.0 * cfy);
*/
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyChar() == 'ц') {
            ff.reset();
        }
        if (e.getKeyChar() == 'ф') {
            ff.reset();
        }
        if (e.getKeyChar() == 'ы') {
            ff.reset();
        }
        if (e.getKeyChar() == 'в') {
            ff.reset();
        }
    }
}
