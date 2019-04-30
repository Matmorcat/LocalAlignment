package com.matmorcat;

import java.util.Arrays;

/**
 * A demonstrative Java implementation of local alignment based on the 
 * Smith-Waterman algorithm.
 * @author Matmorcat
 */
public class ExtraCredit1 {
    
    // Input Strings
    private static final String INPUT_A = "AGGTTG";
    private static final String INPUT_B = "TCAGTTGCC";
    
    // Varying weights for this dataset
    private static final int OFFSET_MATCH = 1;
    private static final int OFFSET_MISMATCH = -2;
    private static final int OFFSET_GAP = -2;
    private static final int LOCAL_MIN = 0;
    
    // Toggle showing of matrix for debugging
    private static final boolean SHOW_MATRIX = true;
    
    public static void main(String[] args) {

        String lcs = longestCommonSequence(INPUT_A, INPUT_B);
        System.out.println("\nBest Local Alignment: \"" + lcs + "\"");
    }

    private static String longestCommonSequence(String str1, String str2) {
        int[][] arr = new int[str1.length() + 1][str2.length() + 1];
        String resultStr = "";
        
        // The highest value in the matrix
        int max_value = 0;
        
        // The location (i, j) of the highest value (local)
        int max_i = 0;
        int max_j = 0;

        if (SHOW_MATRIX)
            // Iterate through each element of the matrix starting at top left
            for (int i = 1; i < str1.length() + 1; i++) {
                for (int j = 1; j < str2.length() + 1; j++) {

                    // If the characters match
                    if (str1.charAt(i - 1) == str2.charAt(j - 1))
                        arr[i][j] = arr[i - 1][j - 1] + OFFSET_MATCH;
                    else
                        // Choose the highest value of the 4 possible paths (local)
                        arr[i][j] = Math.max(
                                        Math.max(arr[i - 1][j - 1] + OFFSET_MISMATCH, LOCAL_MIN),
                                        Math.max(arr[i - 1][j] + OFFSET_GAP, arr[i][j - 1] + OFFSET_GAP)
                                    );

                    // Keep track of the location of the highest value for backtracking
                    if (arr[i][j] > max_value) {
                        max_value = arr[i][j];
                        max_i = i;
                        max_j = j;
                    }
                }
            }
        
        // Format the output matrix with a title and char for each column
        System.out.println("Alignment Matrix:\n");
        System.out.println("  " + Arrays.toString(("_" + str2).toCharArray()).replaceAll("[\\[\\],]", " "));
        
        // Print out each line of the matrix with the row character on the left
        for (int i = 0; i < arr.length; i++)
            System.out.println(("_" + str1).charAt(i) + " " + Arrays.toString(arr[i]));

        // Backtrack through the matrix, starting at the highest value
        int i = max_i, j = max_j;

        // Continue backtracking while values are positive
        while (arr[i][j] > 0) {
            
            // Backtrack through diagonal (match)
            if (arr[i - 1][j - 1] + OFFSET_MATCH == arr[i][j]) {
                
                resultStr = str1.charAt(i - 1) + resultStr;
                i--;
                j--;
                
            // Backtrack through j (gap)
            } else if (arr[i][j - 1] + OFFSET_GAP == arr[i][j]) {
                    resultStr = "_" + resultStr;
                    j--;
                    
            // Backtrack through i (gap)
            } else if (arr[i - 1][j] + OFFSET_GAP == arr[i][j]) {
                resultStr = "_" + resultStr;
                i--;
            }
        }
        return resultStr;

    }
}