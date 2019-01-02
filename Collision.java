import java.util.ArrayList;

import static java.lang.Math.*;

class Collision {
    private Vector MTV, aPoint, bPoint;
    boolean intersect;

    Collision() {
        MTV = new Vector();
        aPoint = new Vector();
        bPoint = new Vector();
        intersect = false;
    }

    private Vector support (Actor A, Vector D) {
        return support(A, D, false);
    }

    private Vector support(Actor A, Vector D, boolean ignoring_first) {
        double max = D.dotProduct(A.Shape(0));
        Vector result = new Vector(A.Shape(0));
        for (int i = 1; i < A.ShapeSize(); i++) {
            if (abs(D.dotProduct(A.Shape(i)) - max) < 0.01) {
                if (ignoring_first) {
                    result = A.Shape(i).copy();
                }
                continue;
            }
            if (D.dotProduct(A.Shape(i)) > max) {
                max = D.dotProduct(A.Shape(i));
                result = A.Shape(i).copy();
            }
        }
        return result.plus(A.getLocation());
    }

    void detectCollision(Actor A, Actor B) {
        polytop_polytop(A, B);
    }

    private Vector closest_point(Vector A, Vector B) {
        if (A.equals(B)) {
            return A;
        }
        Vector direct = A.minus(B);
        double lambda1, lambda2;
        lambda1 = (-B.x * direct.x - B.y * direct.y) / (direct.x * direct.x + direct.y * direct.y);
        if (lambda1 < 0 || lambda1 > 1) {
            if (A.mod() > B.mod()) {
                return B;
            }
            else {
                return A;
            }
        }
        lambda2 = 1 - lambda1;
        return A.multiply(lambda1).plus(B.multiply(lambda2));
    }

    private boolean contains_origin(ArrayList<Vector> verts) {
        switch (verts.size()) {
            case 1:
                if (abs(verts.get(0).x) < 0.001 && abs(verts.get(0).y) <= 0.001) {
                    return true;
                }
                break;
            case 2:
                if (abs(verts.get(0).degree(verts.get(1)) - PI) < 0.001) {
                    return true;
                }
                break;
            case 3:
                if (abs(verts.get(0).degree(verts.get(1)) + verts.get(1).degree(verts.get(2)) + verts.get(2).degree(verts.get(0)) - 2.0 * PI) < 0.001) {
                    return true;
                }
                break;
        }
        return false;
    }

    private void polytop_polytop(Actor A, Actor B) {
        Vector d = new Vector(1, 1), prev_d = new Vector(0,0);
        ArrayList<Vector> verts = new ArrayList<>();
        while (true) {
            d = support(A, d.negate()).minus(support(B, d));
            verts.add(d);
            if (contains_origin(verts)) {
                intersect = true;
                Vector dist;
                prev_d.reset();
                int pos;
                while (true) {
                    d = closest_point(verts.get(verts.size() - 1), verts.get(0));
                    pos = verts.size() - 1;
                   // System.out.printf("%f %f ", verts.get(verts.size() - 1).x, verts.get(verts.size() - 1).y);
                   // System.out.printf("   %f\n", d.y);
                    for (int i = 0; i < verts.size() - 1; i++) {
                       // System.out.printf("%f %f ", verts.get(i).x, verts.get(i).y);
                        dist = closest_point(verts.get(i), verts.get(i + 1));
                       // System.out.printf("   %f\n", dist.y);
                        if (dist.mod() < d.mod()) {
                            d = dist;
                            pos = i;
                        }
                    }
                  //  System.out.printf("%f\n", d.y);
                   // System.out.println();
                    verts.add(pos + 1, support(A, d).minus(support(B, d.negate())));
                    if (abs(d.x - prev_d.x) < 0.01 && abs(d.y - prev_d.y) < 0.01) {
                        MTV = d.negate();
                        aPoint = support(A, MTV.negate());
                        bPoint = support(B, MTV);
                        Vector p_1, p_2, tmp_a = support(A, MTV.negate(), true), tmp_b = support(B, MTV, true);
                        if (aPoint.minus(bPoint).equals(tmp_a.minus(tmp_b))) {
                            p_1 = aPoint.minus(tmp_b);
                            p_2 = tmp_a.minus(bPoint);
                            Vector tmp = bPoint;
                            bPoint = tmp_b;
                            tmp_b = tmp;
                        }
                        else {
                            p_1 = aPoint.minus(bPoint);
                            p_2 = tmp_a.minus(tmp_b);
                        }
                        Vector L = p_2.minus(p_1);
                        if (L.mod() == 0) {
                            break;
                        }
                        double lambda_2 = -L.dotProduct(p_1) / L.dotProduct(L), lambda_1 = 1 - lambda_2;
                        if (lambda_1 < 0) {
                            aPoint = tmp_a;
                            bPoint = tmp_b;
                        }
                        else if (lambda_2 >= 0) {
                            aPoint = aPoint.multiply(lambda_1).plus(tmp_a.multiply(lambda_2));
                            bPoint = bPoint.multiply(lambda_1).plus(tmp_b.multiply(lambda_2));
                        }
                        break;
                    }
                    prev_d = d;
                }
                //System.out.printf("\n");
                break;
            }
            else {
                intersect = false;
                switch (verts.size()) {
                    case 1:
                        break;
                    case 2:
                        d = closest_point(verts.get(0), verts.get(1));
                        if (d == verts.get(0)) {
                            verts.remove(1);
                        }
                        else if (d == verts.get(1)) {
                            verts.remove(0);
                        }
                        break;
                    case 3:
                        d = closest_point(verts.get(0), verts.get(1));
                        Vector dist = closest_point(verts.get(1), verts.get(2));
                        int cnt = 0;
                        if (dist.mod() < d.mod()) {
                            d = dist;
                            cnt++;
                        }
                        dist = closest_point(verts.get(2), verts.get(0));
                        if (dist.mod() < d.mod()) {
                            d = dist;
                            cnt++;
                        }
                        switch (cnt) {
                            case 0:
                                verts.remove(2);
                                if (d == verts.get(0)) {
                                    verts.remove(1);
                                }
                                if (d == verts.get(1)) {
                                    verts.remove(0);
                                }
                                break;
                            case 1:
                                verts.remove(0);
                                if (d == verts.get(0)) {
                                    verts.remove(1);
                                }
                                if (d == verts.get(1)) {
                                    verts.remove(0);
                                }
                                break;
                            case 2:
                                verts.remove(1);
                                if (d == verts.get(1)) {
                                    verts.remove(0);
                                }
                                if (d == verts.get(0)) {
                                    verts.remove(1);
                                }
                                break;
                        }
                        break;
                }
            }
            if (abs(d.x - prev_d.x) < 0.01 && abs(d.y - prev_d.y) < 0.01) {
                MTV = d.negate();
                aPoint = support(A, MTV);
                bPoint = support(B, MTV.negate());
                Vector p_1, p_2, tmp_a = support(A, MTV, true), tmp_b = support(B, MTV.negate(), true);
                if (aPoint.minus(bPoint).equals(tmp_a.minus(tmp_b))) {
                    p_1 = aPoint.minus(tmp_b);
                    p_2 = tmp_a.minus(bPoint);
                    Vector tmp = bPoint;
                    bPoint = tmp_b;
                    tmp_b = tmp;
                }
                else {
                    p_1 = aPoint.minus(bPoint);
                    p_2 = tmp_a.minus(tmp_b);
                }
                Vector L = p_2.minus(p_1);
                if (L.mod() == 0) {
                    break;
                }
                double lambda_2 = -L.dotProduct(p_1) / L.dotProduct(L), lambda_1 = 1 - lambda_2;
                if (lambda_1 < 0) {
                    aPoint = tmp_a;
                    bPoint = tmp_b;
                }
                else if (lambda_2 >= 0) {
                    aPoint = aPoint.multiply(lambda_1).plus(tmp_a.multiply(lambda_2));
                    bPoint = bPoint.multiply(lambda_1).plus(tmp_b.multiply(lambda_2));
                }
                break;
            }
            prev_d = d;
        }
    }

    Vector MTV() {
        return new Vector(MTV.x, MTV.y);
    }
    Vector aPoint() {
        return new Vector(aPoint.x, aPoint.y);
    }
    Vector bPoint() {
        return new Vector(bPoint.x, bPoint.y);
    }
}