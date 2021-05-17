import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by IntelliJ IDEA
 * Date: 16.05.2021
 * Time: 6:49 AM
 *
 * @author lordvidex
 * Name: Овамойо Олувадамилола Эванс
 * <p>
 * Desc:
 */
public class Main {
    private static void generateTC() {
        // generate the test cases
        try {
            TCGenerator.generate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // generate the test case
        //generateTC();

        // read testcases
        int[] array = readTestCases();
        Set<Integer> set = new HashSet<>();
        for (int k : array) {
            set.add(k);
        }

        // insert all the array items into the BRTree
        BRTree tree = new BRTree();
        insertArrayItemsIntoTree(array, tree);

        // find randomItems in the tree
        int[] randomItems = pickRandomItemsFrom(array, 100);
        findItemsInTree(randomItems, tree);

        randomItems = pickRandomItemsFrom(array, 1000);
        deleteItemsInTree(randomItems, tree);
        System.out.println(tree.printTree());
    }

    private static void deleteItemsInTree(int[] randomItems, BRTree tree) {
        for (int j : randomItems) {
            tree.deleteItem(j);
        }
    }

    private static void findItemsInTree(int[] randomItems, BRTree tree) {
        SpeedAnalysis analysis;
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("res/analysis_find.csv"));
            bw.write("item,item_found,item_count,iterations_count,time_in_nanoseconds");
            bw.write("\n");
            for (int j : randomItems) {
                analysis = tree.findItemWithAnalysis(j);
                String line = String.format("%d,%s,%d,%d,%d",
                        analysis.item,
                        analysis.isActionSuccess,
                        analysis.itemCount,
                        analysis.iterationsCount,
                        analysis.timeInNano);
                bw.write(line);
                bw.write("\n");

            }
            bw.close();
        } catch (IOException e) {
            System.out.println("Failed to add to analysis_insert file");
        }
    }

    private static int[] pickRandomItemsFrom(int[] array, int itemCount) {
        int[] result = new int[itemCount];
        Set<Integer> pickedIndices = new HashSet<>();
        // create a random instance and generate itemCount items
        Random random = new Random();
        for (int i = 0; i < itemCount; i++) {
            int pos;
            do {
                pos = random.nextInt(array.length);
            } while (pickedIndices.contains(pos));
            result[i] = array[pos];
            pickedIndices.add(pos);
        }
        return result;
    }

    private static void insertArrayItemsIntoTree(int[] array, BRTree tree) {
        SpeedAnalysis analysis;
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("res/analysis_insert.csv"));
            bw.write("item,item_count,iterations_count,time_in_nanoseconds");
            bw.write("\n");
            for (int j : array) {
                analysis = tree.insertItemWithAnalysis(j);
                if (analysis.isActionSuccess) {
                    String line = String.format("%d,%d,%d,%d",
                            analysis.item,
                            analysis.itemCount,
                            analysis.iterationsCount,
                            analysis.timeInNano);
                    bw.write(line);
                    bw.write("\n");
                }
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("Failed to add to analysis_insert file");
        }
    }

    private static int[] readTestCases() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("res/test_case.txt"));

            // read test case count
            br.readLine();

            // read test case data
            return Arrays.stream(br.readLine().trim().split(" "))
                    .mapToInt(Integer::parseInt).toArray();
        } catch (IOException e) {
            System.out.println("Error reading test cases");
            System.exit(-1);
        }
        return null;
    }
}
