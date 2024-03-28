import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("a = ");
        double a = in.nextDouble();
        System.out.print("b = ");
        double b = in.nextDouble();
        in.close();
        division(a, b);
        subtraction(a, b);
        multiplication(a, b);
        addition(a, b);
    }
    static void division(double a, double b){
        System.out.println("a / b =" + (a / b));
    }
    static void subtraction(double a, double b){
        System.out.println("a - b =" + (a-b));
    }
    static void multiplication(double a, double b){
        System.out.println("a * b =" + (a*b));
    }
    static void addition(double a, double b){
        System.out.println("a + b =" + (a+b));
    }
}