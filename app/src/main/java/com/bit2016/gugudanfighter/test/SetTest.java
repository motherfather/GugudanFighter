package com.bit2016.gugudanfighter.test;

import java.util.HashSet;
import java.util.Set;

public class SetTest {
    public static void main(String[] args) {
        // 보기를 중복없이 넣기 위해서 hashset 사용
        Set<Multiplication> set = new HashSet<Multiplication>();
        while (set.size() != 9) { // 보기 9개 선택
            int left = randomize(2, 8); // 앞쪽 피연산자
            int right = randomize(1, 9); // 뒷쪽 피연산자

            set.add(new Multiplication(left, right));
        }

        int indexRandom = randomize(0, 9); // hashset의 9개의 보기에서 문제 선택

        int index = 0;
        for (Multiplication mul : set) {
            if (index == indexRandom) {
                int left_answer = mul.left;
                int right_answer = mul.right;
            } else {
                System.out.println(mul);
            }
            index++;
        }
    }
    // 랜덤수 구하기
    private static int randomize(int from, int to) {
        return (int)(Math.random() * to) + from;
    }
    // 곱셈 생성기
    private static class Multiplication {
        private  int left;
        private  int right;

        public Multiplication(int left, int right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return "Multiplication{" +
                    "left=" + left +
                    ", right=" + right +
                    ", product=" + right*left +
                    '}';
        }
        // equals 문제해결용
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Multiplication that = (Multiplication) o;

            return right * left == that.right * that.left;

        }
        // 해시코드 생성
        @Override
        public int hashCode() {
            return 31 * (left * right);
        }
    }
}
