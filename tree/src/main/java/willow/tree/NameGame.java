package willow.tree;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;



public class NameGame 
{
	public static WebDriver driver;
	public static WebDriver userinfodrive;
	public static String url = "http://www.ericrochester.com/name-game/";

	
	public static void main( String[] args ) throws InterruptedException
	{	
		//DRIVER SETUP --- DRIVER OPENS CORRECT WEBPAGE -- DRIVER LOADS PAGE
		System.setProperty("webdriver.chrome.driver","C:/chromedriver.exe" ); //path of a driver
		driver= new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		driver.navigate().to(url); 		
				
		//TEST CASE 1) Page Title is displayed
		validateTitleIsPresent();
		
		//Time out to get page elements loaded
		Thread.sleep(6000);

		//TEST CASE 2) Attempts counter increment
		validateClickingFirstPhotoIncreasesTriesCounter();
		
		//TEST CASE 3) Streak counter increment
		validateStreakCounter();
		
		//TEST CASE 4) Multiple Streak counter resets
		multipleStreakReset();
				
		//TEST CASE 5) Attempt and Correct counter validation
		randomTenCounters();
		
		//TEST CASE 6) Name and photos change after correct selection
		nameAndPhotoChange();
	
		//TEST CASE 7) Bonus: Failed selection appears more frequent than correct selection
		frequencyTest();
		
		driver.quit();
}

	//TEST CASE 1) Page Title is displayed
	//Verifies Name Game title is shown (Was Provided)
	public static void validateTitleIsPresent(){
		//Step 1) Get website's title
		WebElement title = driver.findElement(By.xpath("/html/body/div/div[1]/h1"));
		String headerTitle = title.getText().toString();

		//Step 2) Displays Actual Result
		if (title != null) {System.out.println( "Success:  --- Title is Shown ---" + "\n" + headerTitle );}
		else {System.out.println( "Failure:  --- Title is not Shown ---" + "\n" + headerTitle );}
	}
	
	//TEST CASE 2) Attempts counter increment
	//Verifies attempt counter increment (Was Provided)
	public static void validateClickingFirstPhotoIncreasesTriesCounter() {
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		//Step 1) Script gets "Attempt" counters before it clicks on the picture and then gets updated attempt counters
		int count = Integer.parseInt(driver.findElement(By.className("attempts")).getText().toString());
		driver.findElement(By.className("photo")).click();
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		int countAfter = Integer.parseInt(driver.findElement(By.className("attempts")).getText().toString());
		
		//Step 2) Script Compares "Attempt" counters before and after clicking
		//Script Displays Actual Result:
		if (countAfter < count) { 
			System.out.println( "Failure: --- Attempt counter does not incremente properly ---");
			return;}
		else {
			System.out.println( "Success: --- Attempt counter is correctly incremented ---");
		}
	}

	//TEST CASE 3) Streak counter increment
	public static void validateStreakCounter() throws InterruptedException {
		
		//Time out to get page elements loaded
		Thread.sleep(9000);
		
		//Step 1) Script gets initial "Streak" counters
		int oldStreakCount = Integer.parseInt(driver.findElement(By.className("streak")).getText().toString());
		String textName = driver.findElement(By.xpath("//*[@id=\"name\"]")).getText().toString();        	
				
		//Step 2) Script clicks on the correct picture
		WebElement picture = driver.findElement(By.xpath("//div[contains(text(), \""+ textName +"\")]//preceding-sibling::div"));
		picture.click();
		
		//Step 3) Script gets updated attempt counters
		int countAfterCorrect = Integer.parseInt(driver.findElement(By.className("streak")).getText().toString());
		
		//Step 4) Script Compares "Streak" counters before and after clicking
		//Script Displays Actual Result:
		if (countAfterCorrect < oldStreakCount) { 
			System.out.println( "Failure: --- Streak counter does not incremente properly ---");
		}
		else {
			System.out.println( "Success: --- Streak counter is correctly incremented ---");
		}
	}
	
	//TEST CASE 4) Multiple Streak counter resets
	public static void multipleStreakReset() throws InterruptedException {
		
		//Time out to get page elements loaded
		Thread.sleep(6000);
		
		//Step 1) Script gets initial "Streak" counters
		int initialStreakCount = Integer.parseInt(driver.findElement(By.className("streak")).getText().toString());
		
		//Step 2) Script uses validateStreakCounter() to create a STREAK
		for(int i = 0; i<5; i++)
		{
			validateStreakCounter();
		}    
		
		//Step 3) Script gets and clicks on incorrect picture
		List<WebElement> allNames =  driver.findElements(By.className("name"));
		String textName =  driver.findElement(By.xpath("//*[@id=\"name\"]")).getText().toString();        	
		for(WebElement x : allNames) 
		{
			if(!x.getText().toString().equals(textName)){	
				WebElement picture = driver.findElement(By.xpath("//div[contains(text(), \""+ x.getText().toString() +"\")]//preceding-sibling::div"));
				picture.click();
				break;
			}
		}
		
		//Step 4) Script gets updated "Streak" counters
		int finalStreakCount = Integer.parseInt(driver.findElement(By.className("streak")).getText().toString());
		
		//Step 5) Script Compares "Streak" counters before and after reset
		//Script Displays Actual Result:
		if (finalStreakCount >= initialStreakCount) { 
			System.out.println( "Success: --- Streak Counter was reset properly ---");
		}
		else {
			System.out.println( "Failure: --- Streak Counter was not reset properly ---"); 
		}
	}    
	
	//TEST CASE 5) Attempt and Correct counter validation
	private static void randomTenCounters() throws InterruptedException {

		//GET INNITIAL VALUE OF COUNTERS BEFORE RUNNING TEST CASE
		//COUNTER FOR TOTAL  INITIAL SHOULD EQUAL TO NEW - 10
		//AND CORRECT (NEED ADDITIONAL COUNTER TO COMPARE IF INITIAL AND FINAL DIFF. SAME AS ADD. COUNTER
	
		//Time out to get page elements loaded
		Thread.sleep(9000);


		//Step 1) Script gets number for Total and Correct counters
		WebElement initialCorrect = driver.findElement(By.xpath("//span[@class = 'correct']"));
		WebElement initialTotal = driver.findElement(By.xpath("//span[@class = 'attempts']"));
		System.out.println("The initial number of Total Tries:  " + initialTotal.getText());
		System.out.println("The initial number of Correct Answers:  " + initialCorrect.getText());
		int initialT = Integer.parseInt(initialTotal.getText());
		int initialC = Integer.parseInt(initialCorrect.getText());


		//Step 2) Script will make sure that there are 10 different picture selections
		for (int t = 0; t < 10; t++)
		{
			Thread.sleep(6000);
	
			//Get a random values into array so there is no duplicate value until correct answers
			Random rand = new Random(); 
			int value = rand.nextInt(99999);
			value = (value % 5) + 1; 

			//Random xPath with no repeated to it so xPath wont break as photo changes to wrong photo class if bad pair
			try { 	
				WebElement randomSelection = driver.findElement(By.xpath("//div[@class='photo']//div[text() = '" + value + "']"));
				randomSelection.click();        	
			} 
			catch (NoSuchElementException e){ t--; }
			Thread.sleep(6000);	
	}
	
		WebElement lastTotal = driver.findElement(By.xpath("//span[@class = 'attempts']"));
		WebElement lastCorrect = driver.findElement(By.xpath("//span[@class = 'correct']"));
		int finalT = Integer.parseInt(lastTotal.getText());
		
		//Step 3) Script Compares Total Counter Values
		//Script Displays Actual Result:
		if( (finalT - initialT) == 10 ) {
			System.out.println( "Success: --- After 10 random selections attempt counter is working as it schould ---");
			System.out.println( "Initial Attempts Counter is:  " + initialT + " vs. Final Attepts Counter is:  " + finalT);
		}
		else {
			System.out.println( "Failure: --- After 10 random selections attempt counter is invalid ---");
			System.out.println( "Initial Attempts Counter is:  " + initialT + " vs. Final Attepts Counter is:  " + finalT);
		}
		
		//Step 4) Script Compares Correct Counter Values
		//Script Displays Actual Result:
		int finalC = Integer.parseInt(lastCorrect.getText());
		if(initialC < finalC) {
			System.out.println( "Success: --- Corret Answer counter is working as intended ---");
			System.out.println( "Initial Correct Counter is:  " + initialC + " vs. Final Correct Counter is:  " + finalC);
		}
		else {
			System.out.println( "Failure: --- Corret Answer counter is NOT working ---");
			System.out.println( "Initial Correct Counter is:  " + initialC + " vs. Final Correct Counter is:  " + finalC);
		}	
}		
	
	//TEST CASE 6) Name and photos change after correct selection
	private static void nameAndPhotoChange() throws InterruptedException {
		
		//Time out to get page elements loaded
		Thread.sleep(8000);

		//Step 1) Script gets List of names
		List<WebElement> names =  driver.findElements(By.className("name"));
//		Iterator<WebElement> itr = names.iterator();
//		while(itr.hasNext()) {
//			System.out.println(itr.next().getText());
//		}		
		
		//Step 2) Script puts set of picture URL into an Array
		String[] pics = new String[5];
		for(int i = 0; i < 5; i++) {
			pics[i] = driver.findElement(By.xpath("//*[@id=\"gallery\"]/div/div[" + ++i +"]/img")).getAttribute("src");
//			System.out.println(pics[--i]);
		}

		//Step 3) Script uses validateStreakCounter() to select correct picture and name
		validateStreakCounter();
		Thread.sleep(8000);
		
		//Step 4) Script gets new List of names
		List<WebElement> names2 =  driver.findElements(By.className("name"));
//		Iterator<WebElement> itr2 = names2.iterator();
//		while(itr2.hasNext()) {
//			System.out.println(itr2.next().getText());
//		}	
		
		//Step 5) Script puts new set of picture URL into an Array
		String[] pics2 = new String[5];
		for(int i = 0; i < 5; i++) {
			pics2[i] = driver.findElement(By.xpath("//*[@id=\"gallery\"]/div/div[" + ++i +"]/img")).getAttribute("src");
//			System.out.println(pics2[--i]);
		}

		//Step 6) Script Compares List of names before and after correct selection
		//Script Displays Actual Result:
		if (Collections.disjoint(names, names2)) {
			System.out.println( "Success: --- Name do not repeat after successful selection ---");
		}
		else {
			System.out.println( "Failure: --- Name do repeat after successful selection ---"); 
		}

		//Step 7) Script Compares List of URLs before and after correct selection
		//Script Displays Actual Result:
		if (Arrays.deepEquals(pics, pics2)) {
			System.out.println( "Failure: --- Pictures repeated after successful selection ---"); 
		}
		else {
			System.out.println( "Success: --- Pictures did not repeat after successful selection ---");
		}		
	}

	//TEST CASE 7) Bonus: Failed selection appears more frequent than correct selection
	public static void frequencyTest() throws InterruptedException {
		
		//Time out to get page elements loaded
		Thread.sleep(6000);

		//Step 1) Script gets List WebElements by names		
		//GET ALL NAMES FOR NEW PAGE
		List<WebElement> allNames =  driver.findElements(By.className("name"));

		//Step 2) Script identifies correct choice/name		
		WebElement nameCorrect = driver.findElement(By.xpath("//*[@id=\"name\"]"));        	
		String textName = nameCorrect.getText().toString();
//		System.out.println("\n" + "The Correct Choices is:   " + textName +"\n");

		//Initialization of map and variables	
		Map< String,Integer> nameMap =  new HashMap< String,Integer>(); 	
		String getWrongName = null;
		WebElement picture;
		//Create A MAP FOR CORRECT USER AND ONE INCCORECT WITH COUNTERS

		//Step 3) Script will loop 100 time(s) to compare incorrect and correct selection frequency
		for(int f = 0; f<100; f++) {
			//INITIAL WRONG CHOICE
			if (getWrongName == null) {
				//LOOKS FOR ALL NAME WEBELEMENTS IN THE LIST 
				for(WebElement x : allNames) 
				{
					getWrongName = x.getText().toString();
					if(!getWrongName.equals(textName)){	
						picture = driver.findElement(By.xpath("//div[contains(text(), \""+ getWrongName +"\")]//preceding-sibling::div"));
						picture.click();
						nameMap.put(getWrongName, 1);
						System.out.println("\n" + "Wrong Name:   " + getWrongName +"\n");
						break;
					}
					getWrongName = null;		
				}
			}
			else{
				//IF WE HAVE WRONG_NAME THIS  IS EXECUTED
				allNames =  driver.findElements(By.className("name"));
				Iterator<WebElement> iter = allNames.iterator();
				while(iter.hasNext()) {
					WebElement we = iter.next();
					if (we.getText().toString().equals(getWrongName)) {
						//Click on it add it into map
						picture = driver.findElement(By.xpath("//div[@class='photo']//div[text()='"+ getWrongName +"']//preceding-sibling::div"));
						picture.click();
						//Script Updates Map 
						if (nameMap.containsKey(getWrongName)) {
							nameMap.put(getWrongName, nameMap.get(getWrongName)+1);
						}
						else {
							nameMap.put(getWrongName, 1);
						}					
						break;
					}
				}
			}
			nameCorrect = driver.findElement(By.xpath("//*[@id=\"name\"]"));        	
			textName = nameCorrect.getText().toString();
			//Script Updates Map
			if (nameMap.containsKey(textName)) {
				nameMap.put(textName, nameMap.get(textName)+1);
			}
			else {
				nameMap.put(textName, 1);
			}		
			allNames =  driver.findElements(By.className("name"));
			Iterator<WebElement> iter = allNames.iterator();
			while(iter.hasNext()) {
				WebElement we = iter.next();
				if (!we.getText().toString().equals(textName) && !we.getText().toString().equals(getWrongName)) {
					//Update Map 
					if (nameMap.containsKey(we.getText().toString())) {
						nameMap.put(we.getText().toString(), nameMap.get(we.getText().toString())+1);
						//						System.out.println("Key = " + we.getText().toString() );
					}				
				}
			}
			//Script Clicks Correct Name
			picture = driver.findElement(By.xpath("//div[contains(text(), \""+ textName +"\")]//preceding-sibling::div"));
			picture.click();
			Thread.sleep(6000);
			allNames =  driver.findElements(By.className("name"));
		}	
		int count = 0;
		
		//Step 4) Script Compares Frequencies of appearance for all correctly selected choices and one choice that was set ass incorrect
		//Script Displays Actual Result:
		for (Map.Entry<String,Integer> entry : nameMap.entrySet()) {
			if (!entry.getKey().equals(getWrongName) && nameMap.get(getWrongName) < entry.getValue()) {
				int timesApp = entry.getValue() - nameMap.get(getWrongName);	
				System.out.println("Correct Selection:  " + entry.getKey() + " Appears " + timesApp + " More Time(s) Than " + getWrongName);
				count++;
			}
		}
		System.out.println(getWrongName + " appears less often than " + count + " correctly selected name(s) from " + (nameMap.size() - 1));
		if ((count/(nameMap.size()-1)) >= .85) {
			System.out.println("Failure:  Correctly selected names appear 85% more freqently than selected inccorectly --- " + getWrongName + " ---");
		}
		else {
			System.out.println("Success:  Inccorectly selected --- " + getWrongName + " --- appear more freqently than other " + (nameMap.size() - 1) + " correctly selected name(s) ");
		}
	}

}
