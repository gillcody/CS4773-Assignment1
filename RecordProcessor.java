import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

public class RecordProcessor {
	private static String [] employeeFirstNames;
	private static String [] employeeLastNames;
	private static int [] employeeAges;
	private static String [] employeeType;
	private static double [] employeePay;
	
	public static String processFile(String file) {
		StringBuffer stringBuffer = new StringBuffer();
		
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(file));
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
			return null;
		}
		
		int employeeCount = 0; //was 'c', may need to be changed
		while(scanner.hasNextLine()) {
			String employeeInfo = scanner.nextLine();
			if(employeeInfo.length() > 0)
				employeeCount++;
		}

		employeeFirstNames = new String[employeeCount];
		employeeLastNames = new String[employeeCount];
		employeeAges = new int[employeeCount];
		employeeType = new String[employeeCount];
		employeePay = new double[employeeCount];

		scanner.close();
		try {
			scanner = new Scanner(new File(file));
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
			return null;
		}

		employeeCount = 0;
		while(scanner.hasNextLine()) {
			String employeeInfo = scanner.nextLine();
			if(employeeInfo.length() > 0) {
				
				String [] words = employeeInfo.split(",");

				int c2 = 0; 
				for(; c2 < employeeLastNames.length; c2++) {
					if(employeeLastNames[c2] == null)
						break;
					
					if(employeeLastNames[c2].compareTo(words[1]) > 0) {
						for(int i = employeeCount; i > c2; i--) {
							employeeFirstNames[i] = employeeFirstNames[i - 1];
							employeeLastNames[i] = employeeLastNames[i - 1];
							employeeAges[i] = employeeAges[i - 1];
							employeeType[i] = employeeType[i - 1];
							employeePay[i] = employeePay[i - 1];
						}
						break;
					}
				}
				
				employeeFirstNames[c2] = words[0];
				employeeLastNames[c2] = words[1];
				employeeType[c2] = words[3];

				try {
					employeeAges[c2] = Integer.parseInt(words[2]);
					employeePay[c2] = Double.parseDouble(words[4]);
				} catch(Exception e) {
					System.err.println(e.getMessage());
					scanner.close();
					return null;
				}
				
				employeeCount++;
			}
		}
		
		if(employeeCount == 0) {
			System.err.println("No records found in data file");
			scanner.close();
			return null;
		}
		
		//print the rows
		stringBuffer.append(String.format("# of people imported: %d\n", employeeFirstNames.length));
		
		stringBuffer.append(String.format("\n%-30s %s  %-12s %12s\n", "Person Name", "Age", "Emp. Type", "Pay"));
		for(int i = 0; i < 30; i++)
			stringBuffer.append(String.format("-"));
		stringBuffer.append(String.format(" ---  "));
		for(int i = 0; i < 12; i++)
			stringBuffer.append(String.format("-"));
		stringBuffer.append(String.format(" "));
		for(int i = 0; i < 12; i++)
			stringBuffer.append(String.format("-"));
		stringBuffer.append(String.format("\n"));
		
		for(int i = 0; i < employeeFirstNames.length; i++) {
			stringBuffer.append(String.format("%-30s %-3d  %-12s $%12.2f\n", employeeFirstNames[i] + " " + employeeLastNames[i], employeeAges[i]
				, employeeType[i], employeePay[i]));
		}
		
		int sumAges = 0;
		float avgAge = 0f;
		int numCommissionEmployees = 0;
		double sumCommissionPay = 0;
		double avgCommissionPay = 0;
		int numHourlyEmployees = 0;
		double sumHourlyPay = 0;
		double avgHourlyPay = 0;
		int numSalaryEmployees = 0;
		double sumSalaryPay = 0;
		double avgSalaryPay = 0;
		for(int i = 0; i < employeeFirstNames.length; i++) {
			sumAges += employeeAges[i];
			if(employeeType[i].equals("Commission")) {
				sumCommissionPay += employeePay[i];
				numCommissionEmployees++;
			} else if(employeeType[i].equals("Hourly")) {
				sumHourlyPay += employeePay[i];
				numHourlyEmployees++;
			} else if(employeeType[i].equals("Salary")) {
				sumSalaryPay += employeePay[i];
				numSalaryEmployees++;
			}
		}
		avgAge = (float) sumAges / employeeFirstNames.length;
		stringBuffer.append(String.format("\nAverage age:         %12.1f\n", avgAge));
		avgCommissionPay = sumCommissionPay / numCommissionEmployees;
		stringBuffer.append(String.format("Average commission:  $%12.2f\n", avgCommissionPay));
		avgHourlyPay = sumHourlyPay / numHourlyEmployees;
		stringBuffer.append(String.format("Average hourly wage: $%12.2f\n", avgHourlyPay));
		avgSalaryPay = sumSalaryPay / numSalaryEmployees;
		stringBuffer.append(String.format("Average salary:      $%12.2f\n", avgSalaryPay));
		
		HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
		int numSharedFirstName = 0;
		for(int i = 0; i < employeeFirstNames.length; i++) {
			if(hashMap.containsKey(employeeFirstNames[i])) {
				hashMap.put(employeeFirstNames[i], hashMap.get(employeeFirstNames[i]) + 1);
				numSharedFirstName++;
			} else {
				hashMap.put(employeeFirstNames[i], 1);
			}
		}

		stringBuffer.append(String.format("\nFirst names with more than one person sharing it:\n"));
		if(numSharedFirstName > 0) {
			Set<String> set = hashMap.keySet();
			for(String str : set) {
				if(hashMap.get(str) > 1) {
					stringBuffer.append(String.format("%s, # people with this name: %d\n", str, hashMap.get(str)));
				}
			}
		} else { 
			stringBuffer.append(String.format("All first names are unique"));
		}

		HashMap<String, Integer> hashMap2 = new HashMap<String, Integer>();
		int numSharedLastName = 0;
		for(int i = 0; i < employeeLastNames.length; i++) {
			if(hashMap2.containsKey(employeeLastNames[i])) {
				hashMap2.put(employeeLastNames[i], hashMap2.get(employeeLastNames[i]) + 1);
				numSharedLastName++;
			} else {
				hashMap2.put(employeeLastNames[i], 1);
			}
		}

		stringBuffer.append(String.format("\nLast names with more than one person sharing it:\n"));
		if(numSharedLastName > 0) {
			Set<String> set = hashMap2.keySet();
			for(String str : set) {
				if(hashMap2.get(str) > 1) {
					stringBuffer.append(String.format("%s, # people with this name: %d\n", str, hashMap2.get(str)));
				}
			}
		} else { 
			stringBuffer.append(String.format("All last names are unique"));
		}
		
		//close the file
		scanner.close();
		
		return stringBuffer.toString();
	}
	
}
