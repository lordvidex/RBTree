import java.io.*;
import java.util.Random;

/**
 * Created by IntelliJ IDEA
 * Date: 16.05.2021
 * Time: 6:41 AM
 *
 * @author lordvidex
 * Name: Овамойо Олувадамилола Эванс
 * <p>
 * Desc:
 */
public class TCGenerator {
    static final int TEST_CASE_COUNT = 10000;

    /**
     * Generates TEST_CASE_COUNT random numbers
     * to the <b>res/test_case.txt</b> file
     */
    public static void generate() throws IOException {
        File f = new File("res/test_case.txt");

        if(!f.exists() && !f.getParentFile().mkdir()){
            System.out.println("There was an error creating res folder and generating test cases");
        }

        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
        Random random = new Random();

        // number of test cases
        bw.write(TEST_CASE_COUNT + "\n");


        for (int i = 1; i <= TEST_CASE_COUNT; i++) {

            // generate a random number
            bw.write(random.nextInt(TEST_CASE_COUNT * 100) + " ");
        }
        bw.close();
    }
}
