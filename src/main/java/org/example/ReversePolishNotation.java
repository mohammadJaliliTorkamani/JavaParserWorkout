package org.example;

import java.util.Stack;
import java.util.stream.Stream;

/**
 * Comment 1
 */
public class ReversePolishNotation {

    //comment 2
    public static int ONE_BILLION = 1000000000;
    public static int TWO_BILLION = 2_000_000_000;

    private double memory = 0;


    /**
     * comment 3
     *
     * @param input
     * @return
     */
    public Double calc(String input) {
        String[] tokens = input.split(" ");
        Stack<Double> numbers = new Stack<>();
        Stream.of(tokens).forEach(t -> {
            double a, b;
            switch (t) {
                case "+":
                    a = numbers.pop();
                    b = numbers.pop();
                    numbers.push(a + b);
                    break;
                case "-":
                    a = numbers.pop();
                    b = numbers.pop();
                    numbers.push(a - b);
                    break;
                case "*":
                    a = numbers.pop();
                    b = numbers.pop();
                    numbers.push(a * b);
                    break;
                case "/":
                    a = numbers.pop();
                    b = numbers.pop();
                    numbers.push(a / b);
                    break;
            }
        });
        return numbers.pop();
    }

    /**
     * Comment 4
     */
    public void memoryClear() {
        memory = 0;
    }

    public double memoryRecall() {
        return memory;
    }

    public void memoryStore(double value) {
        memory = value;
    }
}

/* EOF */