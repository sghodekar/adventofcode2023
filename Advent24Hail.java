package com.example;

import lombok.Data;

import java.util.*;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;
import com.microsoft.z3.*;

@Data
class Point{
    double x;
    double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}

/*
class Hailstone {
    double x, y, z, vx, vy, vz;
    boolean pathCrossed;

    public Hailstone(double x, double y, double z, double vx, double vy, double vz) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hailstone hailstone = (Hailstone) o;
        return Double.compare(hailstone.x, x) == 0 && Double.compare(hailstone.y, y) == 0 && Double.compare(hailstone.z, z) == 0 && Double.compare(hailstone.vx, vx) == 0 && Double.compare(hailstone.vy, vy) == 0 && Double.compare(hailstone.vz, vz) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, vx, vy, vz);
    }

    @Override
    public String toString() {
        return "Hailstone{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", vx=" + vx +
                ", vy=" + vy +
                ", vz=" + vz +
                ", pathCrossed=" + pathCrossed +
                '}';
    }
}*/

public class Advent24Hail extends FileProcessor {

    List<Hailstone> hlist = new ArrayList<>();
    double testlow = 200000000000000d;
    double testhigh = 400000000000000d;

    public static void main(String[] args) {
        Advent24Hail h = new Advent24Hail();
        h.readFile("advent24_input.txt");

        System.out.println(h.hlist);

        Point A = new Point(19, 13);
        Point B = new Point(17, 14);
        Point C = new Point(18, 19);
        Point D = new Point(17, 18);

        Point intersection = lineLineIntersection(A, B, C, D);

        if (intersection.x == Double.MAX_VALUE &&
                intersection.y == Double.MAX_VALUE)
        {
            System.out.println("The given lines AB and CD are parallel.");
        }

        else
        {
            // NOTE: Further check can be applied in case
            // of line segments. Here, we have considered AB
            // and CD as lines
            System.out.print("The intersection of the given lines AB " +
                    "and CD is: ");
            System.out.println(intersection.toString());
        }
        h.part1();
        h.part2v2();
    }

    Set<Hailstone> hsset = new HashSet<>();
    void part1(){
        int count = 0;
        for(int i=0; i < hlist.size(); i++) {
            for(int j=i+1; j < hlist.size(); j++) {
                Hailstone h1 = hlist.get(i);
                Point A = new Point(h1.x(), h1.y());
                Point B = new Point(h1.x() + h1.vx(), h1.y()+ h1.vy());

                Hailstone h2 = hlist.get(j);
                Point C = new Point(h2.x(), h2.y());
                Point D = new Point(h2.x() + h2.vx(), h2.y()+ h2.vy());

                Point intersection = lineLineIntersection(A, B, C, D);

                if (intersection.x == Double.MAX_VALUE &&
                        intersection.y == Double.MAX_VALUE)
                {
                    System.out.println("The given lines AB and CD are parallel.");
                }
                else
                {
                    if(intersection.x >=testlow && intersection.x <= testhigh
                       && intersection.y >= testlow && intersection.y <= testhigh) {

                        //check if its future
                        if ((intersection.x - h1.x())*h1.vx() >=0 &&
                            (intersection.y - h1.y())*h1.vy() >=0 &&
                            (intersection.x - h2.x())*h2.vx() >=0 &&
                            (intersection.y - h2.y())*h2.vy() >=0) {
                                count++;
                        }
                    }
                }
            }
        }
        System.out.println("Count = "+count);
    }

    void part2() {
        // Create an instance of the expression evaluator
        ExprEvaluator evaluator = new ExprEvaluator();

        // Define the symbolic variables
        IExpr x = evaluator.parse("x");
        IExpr y = evaluator.parse("y");
        IExpr z = evaluator.parse("z");
        IExpr a = evaluator.parse("a");
        IExpr b = evaluator.parse("b");
        IExpr c = evaluator.parse("c");

        // Define the system of equations
        StringBuilder sb = new StringBuilder();
        for(int i=0; i < 10; i++) {
            Hailstone hs = hlist.get(i);
            //IExpr equation1 = evaluator.parse("(x - "+hs.x+") * ("+hs.vy +" - b) - (y - "+hs.y+") * ("+hs.vx+" - a) = 0");
            IExpr equation1 = evaluator.parse("x * "+hs.vy()+" - x*b - "+ (hs.x()*hs.vy())+"- b*"+hs.x() +" - y *"+hs.vx()+ " +y*a - "+(hs.y()*hs.vx()) +" + a*"+ hs.y() +"=0");
           // IExpr equation2 = evaluator.parse("(y -"+hs.y+") * ("+hs.vz+" - vz) - (z -"+hs.z+") * ("+hs.vy+" - vy) = 0");
            IExpr equation2 = evaluator.parse("y * "+hs.vz()+" - x*c - "+ (hs.y()*hs.vz())+"- c*"+hs.y() +" - z *"+hs.vy()+ " +z*b - "+(hs.z()*hs.vy()) +" + b*"+ hs.z()+"=0");
            sb.append(equation1).append(", ").append(equation2).append(",");
        }


        // Solve the system of equations
        IExpr system = evaluator.parse("{x, y,z,a,b,c} /. Solve({" + sb.substring(0, sb.length()-1).toString() + "}, {x, y,z,a,b,c})");

        // Evaluate the system and print the result
        IExpr solution = evaluator.eval(system);
        System.out.println("Solution: " + solution);
    }

    void part2v2() {
        Context ctx = new Context();
        Solver s = ctx.mkSolver();
        RealExpr x = ctx.mkRealConst("x");
        RealExpr y = ctx.mkRealConst("y");
        RealExpr z = ctx.mkRealConst("z");
        RealExpr vx = ctx.mkRealConst("vx");
        RealExpr vy = ctx.mkRealConst("vy");
        RealExpr vz = ctx.mkRealConst("vz");

        for(int i=0; i < hlist.size(); i++) {
            RealExpr ti = ctx.mkRealConst("t_"+i);
            Hailstone hs = hlist.get(i);
            RatNum hsx = ctx.mkReal((long)hs.x());
            RatNum hsy = ctx.mkReal((long)hs.y());
            RatNum hsz = ctx.mkReal((long)hs.z());
            RatNum hsvx = ctx.mkReal((long)hs.vx());
            RatNum hsvy = ctx.mkReal((long)hs.vy());
            RatNum hsvz = ctx.mkReal((long)hs.vz());
            BoolExpr e1 = ctx.mkEq(ctx.mkAdd(x, ctx.mkMul(vx, ti)), ctx.mkAdd(hsx, ctx.mkMul(hsvx, ti)));
            BoolExpr e2 = ctx.mkEq(ctx.mkAdd(y, ctx.mkMul(vy, ti)), ctx.mkAdd(hsy, ctx.mkMul(hsvy, ti)));
            BoolExpr e3 = ctx.mkEq(ctx.mkAdd(z, ctx.mkMul(vz, ti)), ctx.mkAdd(hsz, ctx.mkMul(hsvz, ti)));

            s.add(e1);
            s.add(e2);
            s.add(e3);
        }
        s.check();
        Model model = s.getModel();
        System.out.println(model.eval(x, true));
        System.out.println(model.eval(y, true));
        System.out.println(model.eval(z, true));
        //long sum = model.eval(x, true) + model.eval(y, true) + model.eval(y, true)


    }

    static Point lineLineIntersection(Point A, Point B, Point C, Point D)
    {
        // Line AB represented as a1x + b1y = c1
        double a1 = B.y - A.y;
        double b1 = A.x - B.x;
        double c1 = a1*(A.x) + b1*(A.y);

        // Line CD represented as a2x + b2y = c2
        double a2 = D.y - C.y;
        double b2 = C.x - D.x;
        double c2 = a2*(C.x)+ b2*(C.y);

        double determinant = a1*b2 - a2*b1;

        if (determinant == 0)
        {
            // The lines are parallel. This is simplified
            // by returning a pair of FLT_MAX
            return new Point(Double.MAX_VALUE, Double.MAX_VALUE);
        }
        else
        {
            double x = (b2*c1 - b1*c2)/determinant;
            double y = (a1*c2 - a2*c1)/determinant;
            return new Point(x, y);
        }
    }


    public Optional<Point> calculateIntersectionPoint(
            double m1,
            double b1,
            double m2,
            double b2) {

        if (m1 == m2) {
            return Optional.empty();
        }

        double x = (b2 - b1) / (m1 - m2);
        double y = m1 * x + b1;

        Point point = new Point(x,y);
        //point.setLocation(x, y);
        return Optional.of(point);
    }

    @Override
    public String processLine(String s) {
        String hline = s.replace('@', ',' );
        String[] elements = hline.split(",");
        Hailstone hs = new Hailstone(Double.parseDouble(elements[0].trim()),
                                    Double.parseDouble(elements[1].trim()),
                                    Double.parseDouble(elements[2].trim()),
                                    Double.parseDouble(elements[3].trim()),
                                    Double.parseDouble(elements[4].trim()),
                                    Double.parseDouble(elements[5].trim()));
        hlist.add(hs);
        return null;
    }
}


