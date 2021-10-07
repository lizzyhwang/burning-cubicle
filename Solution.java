import java.io.*;
import java.util.*;

public class Solution {

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    // get n from system.in
    int n = 0;
    if (sc.hasNextInt()) {
      n = sc.nextInt();
    }

    // initialize array with correct amount of rows
    ArrayList<ArrayList<Integer>> array = new ArrayList();
    for (int k = 0; k < n; k++) {
      array.add(new ArrayList<Integer>());
    }

    // p is an integer that keeps track of the index of the current row we're filling.
    int p = 0;
    // while scanner has next int and the array is not full,
    // scan through args and add them to n x n array
    while (sc.hasNextInt() && array.get(n - 1).size() < n) {
      ArrayList<Integer> curRow = array.get(p);
      if (curRow.size() < n) {
        curRow.add(sc.nextInt());
      } else {
        p += 1;
      }
    }

    //StdOut.println(findBurningIndex(array, n, n));
    System.out.print(findBurningIndex(array, n, n));
  }

  public static String findBurningIndex(ArrayList<ArrayList<Integer>> arr, int h, int w) {
    if (h == 0 || w == 0) {
      throw new IllegalArgumentException("dimensions of array can't be zero");
    }

    int mh = (h / 2); // produces the index of the middle row/column
    int mw = (w / 2); //

    // all the rows/columns we care about in the array.
    ArrayList<Integer> fRow = arr.get(0);
    ArrayList<Integer> mRow = arr.get(mh);
    ArrayList<Integer> lRow = arr.get(h - 1);
    ArrayList<Integer> fCol = getCol(arr, 0);
    ArrayList<Integer> mCol = getCol(arr, mw);
    ArrayList<Integer> lCol = getCol(arr, w - 1);

    // the max of each row/column
    int maxFRow = findMax(fRow);
    int maxMRow = findMax(mRow);
    int maxLRow = findMax(lRow);
    int maxFCol = findMax(fCol);
    int maxMCol = findMax(mCol);
    int maxLCol = findMax(lCol);

    // all the possible maximums
    ArrayList<Integer> possibleMaxes =
        new ArrayList<Integer>(Arrays.asList(maxFRow, maxMRow, maxLRow, maxFCol, maxMCol, maxLCol));
    // for convenience: maximums of rows
    ArrayList<Integer> rowMaxes = new ArrayList<Integer>(Arrays.asList(maxFRow, maxMRow, maxLRow));
    // for convenience: maximums of columns
    ArrayList<Integer> colMaxes = new ArrayList<Integer>(Arrays.asList(maxFCol, maxMCol, maxLCol));

    int max = findMax(possibleMaxes);
    // get the index (row, col) of the maximum
    int row;
    int col;
    if (max == maxFRow) {
      row = 0;
      col = fRow.indexOf(max);
    } else if (max == maxMRow) {
      row = mh;
      col = mRow.indexOf(max);
    } else if (max == maxLRow) {
      row = h - 1;
      col = lRow.indexOf(max);
    } else if (max == maxFCol) {
      row = fCol.indexOf(max);
      col = 0;
    } else if (max == maxMCol) {
      row = mCol.indexOf(max);
      col = mw;
    } else { // last case scenario; we chose from  6 possible maxes so we dont need an else-if here
      row = lCol.indexOf(max);
      col = w - 1;
    }

    // check to see if this is a burning cubicle.
    if (rowMaxes.contains(max) && (row < h - 1) && max < arr.get(row + 1)
        .get(col)) { // item is from a row (not last) and item below is bigger
      chop(arr, row + 1, col, mh, mw, w, h);
      findBurningIndex(arr, arr.size(), arr.get(0).size());
    } else if (rowMaxes.contains(max) && (row > 0) && max < arr.get(row - 1)
        .get(col)) { // if item is from a row, not at top and item above is bigger
      chop(arr, row - 1, col, mh, mw, w, h);
      findBurningIndex(arr, arr.size(), arr.get(0).size());
    } else if (colMaxes.contains(max) && (col < w - 1) && (max < arr.get(row).get(
        col + 1))) { // if item is from column, not on right border, and item to right is bigger
      chop(arr, row, col + 1, mh, mw, w, h);
      findBurningIndex(arr, arr.size(), arr.get(0).size());
    } else if (colMaxes.contains(max) && (col > 0) && (max < arr.get(row)
        .get(col - 1))) { // if item is from column, not on left border and item to left is bigger
      chop(arr, row, col - 1, mh, mw, w, h);
      findBurningIndex(arr, arr.size(), arr.get(0).size());
    } else {
      return print2Ints(row + 1, col + 1);
    }

    return "";

  }

  // chops of the parts of the array that we don't care about.
  // (row,col) is (i,j) of the neighbor that is higher than the max we found in the window.
  // mh/mw are halfway indices for width and height that we previously divided on.
  // w/h are the numerical quantities for the width/height of the array (not highest index)
  // it's not possible for the row/col to be on a middle width or height index because if it were,
  // it would have been returned in the main function already.
  public static void chop(ArrayList<ArrayList<Integer>> arr,
      int row, int col, int mh, int mw, int w, int h) {

    // discard irrelevant rows and columns
    if (row < mh) {
      for (int i = h - 1; i > mh + 1; i--) {
        arr.remove(i);
      }
    } else if (row > mh) {
      for (int i = mh - 1; i >= 0; i--) {
        arr.remove(i);
      }
    }

    if (col < mw) {
      for (ArrayList<Integer> r : arr) {
        for (int j = w - 1; j > mw + 1; j--) {
          r.remove(j);
        }
      }
    } else if (col > mw) {
      for (ArrayList<Integer> r : arr) {
        for (int j = mw - 1; j >= 0; j--) {
          r.remove(j);
        }
      }
    }

  }


  public static String print2Ints(int i, int j) {
    return Integer.toString(i) + " " + Integer.toString(j);
  }

  public static ArrayList<Integer> getCol(ArrayList<ArrayList<Integer>> arr, int index) {
    ArrayList<Integer> result = new ArrayList<Integer>();
    for (ArrayList<Integer> a : arr) {
      result.add(a.get(index));
    }
    return result;
  }

  public static int findMax(ArrayList<Integer> arr) {
    int i = 0;
    for (int a : arr) {
      if (a > i) {
        i = a;
      }
    }
    return i;
  }


}
