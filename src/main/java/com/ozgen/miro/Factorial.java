package com.ozgen.miro;

public class Factorial {
    public static void main(String[] args) {
        System.out.println(fibonacciIT(5));
    }

    public static int fact(int i) {
        if (i == 0)
            return 1;
        return i * fact(i - 1);
    }

    public static int fact_it(int i) {
        int fact = 1;
        for (int x = i; x > 0; x--)
            fact *= x;
        return fact;
    }

//1 1 2 3 5 8
    public static int fibo(int i) {
        if (i == 1)
            return 1;
        if (i == 2)
            return 1;
        return fibo(i - 1) + fibo(i - 2);
    }

    public static int fibonacciRecursion(int n) {
        if (n == 0) {
            return 0;
        }
        if (n == 1 || n == 2) {
            return 1;
        }
        return fibonacciRecursion(n - 2) + fibonacciRecursion(n - 1);
    }

    public static int fibonacciIT(int n) {
        int value = 0;
        int valuePrev = 1;
        int valuePrevPrev = 1;
        for (int i = 3; i <= n; i++) {
            value = valuePrev + valuePrevPrev;
            valuePrevPrev = valuePrev;
            valuePrev = value;
        }
        return value;
    }

    public static int fibo_it(int i) {
        int fact = 1;
        for (int x = i; x > 0; x--)
            fact *= x;
        return fact;
    }
}