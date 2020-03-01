package com.ozgen.miro;
import java.util.LinkedList;

class Solution {
    public static void main(String[] args) {
        LinkedList<Integer> number1 = new LinkedList<>();
        number1.add(2);
        number1.add(4);
        number1.add(3);
        LinkedList<Integer> number2 = new LinkedList<>();
        number2.add(5);
        number2.add(6);
        number2.add(4);
        sum(number1, number2).stream().forEach(System.out::println);
    }

//    private Integer sumRecursive(LinkedList<Integer> sum, int index1, int index2) {
//        number1.get(index1);
//        number2.get(index2);
//        sum.push(sumRecursive(sum, ++index1, ++index2));
//        return sum;
//    }

    private static LinkedList<Integer> sum(LinkedList<Integer> number1, LinkedList<Integer> number2) {
        LinkedList<Integer> sum = new LinkedList<>();
        int maxDigits = number1.size() >= number2.size() ? number1.size() : number2.size();
        int carry = 0;
        for (int i = 0; i < maxDigits; i++) {
            Integer digit1 = number1.pollFirst();
            if (digit1 == null) {
                digit1 = 0;
            }
            Integer digit2 = number2.pollFirst();
            if (digit2 == null) {
                digit2 = 0;
            }
            int digitTotal = digit1 + digit2 + carry;
            if (digitTotal >= 10) {
                carry = 1;
                digitTotal -= 10;
            } else {
                carry = 0;
            }
            sum.push(digitTotal);
        }
        if (carry == 1) {
            sum.push(1);
        }
        return sum;
    }
}