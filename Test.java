import java.io.File;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) throws Exception {
	String dirName = "TestData";
	String expected1 =
	    file2String(dirName + File.separator + "expected1.txt");

	test(dirName + File.separator + "data1.txt", expected1);
	test(dirName + File.separator + "data2.txt", null);
	test(dirName + File.separator + "data3.txt", null);
    }

    private static String file2String(String fileName) throws Exception {
	StringBuffer fileContents = new StringBuffer();
	Scanner fileInput = new Scanner(new File(fileName));
	while (fileInput.hasNextLine()) {
	    fileContents.append(fileInput.nextLine() + "\n");
	}
	return fileContents.toString();
    }
    
    private static boolean test(String dataFileName, String expectedResult) {
	System.out.println("\nTesting file " + dataFileName);
	boolean passed;
	String actualResult = null;
	try {
	    actualResult = RecordProcessor.processFile(dataFileName);
	}
	catch (Exception e) {
	    System.out.println("FAILED. Caught exception: " + e.toString());
	    passed = false;
	    return passed;
	}
	
	if (expectedResult != null) {
	    passed = expectedResult.equals(actualResult);
	}
	else {
	    passed = actualResult == null;
	}
	if (passed) {
	    System.out.println("PASSED");
	}
	else {
	    System.out.println("FAILED");
	    System.out.println("Expected Result: " + expectedResult);
	    System.out.println("Actual Result: " + actualResult);
	}
	return passed;
    }
			       
}
