package com.sofa.pilot.service;

public final class SimpleCalculator {

    private SimpleCalculator(){
        throw new UnsupportedOperationException("Class cannot be initialized");
    }

    public static int add(int... addends) {
        int sum = 0;
        for (int addend: addends) {
            sum += addend;
        }
        return sum;
    }
}
