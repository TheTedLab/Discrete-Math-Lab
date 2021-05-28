package main.utils;

public class Utils {

    public static void printMatrixUtils(int[][] matrix, String name) {
        System.out.println(name + ": ");
        for (int[] rows : matrix) {
            for (int cols : rows) {
                if (cols < 10) {
                    System.out.print("  " + cols + " ");
                } else if (cols == 100) {
                    System.out.print("inf ");
                } else {
                    System.out.print(" " + cols + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
