package com.bit2016.gugudanfighter.test;

import java.util.HashSet;
import java.util.Set;

public class SetTest {
    private static Set<Multi> set;

    public static void main(String[] args) {
        game();
        System.out.println(set);
    }

    private static void game() {
        set = new HashSet<Multi>();
        while (set.size() <= 9) {
            set.add(new Multi());
        }
        int answer = randomNumber(0, 9);

    }

    // 랜덤 숫자 생성기
    private static int randomNumber(int from, int to) {
        return (int)(Math.random() * to) + from;
    }

    // 문제 생성기
    private static class Multi {
        private int left;
        private int right;

        private Multi() {
            left = randomNumber(2, 8);
            right = randomNumber(1, 9);
        }

        @Override
        public String toString() {
            return "[" + left + ":" + right + "]";
        }

        // 해시코드 생성
        @Override
        public int hashCode() {
            return 31 * left * right;
        }

        // 결과값이 같을 때 중복이 되지 않도록 하기 위해서
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Multi that = (Multi) obj;
            return right * left == that.right * that.left;
        }
    }
}
