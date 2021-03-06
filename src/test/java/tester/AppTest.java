package tester;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 *
 */
public class AppTest 
{
    /**
     *
     */
    @Test
    public void basicTest()
    {
        // get the rows
        ArrayList<ArrayList<Integer>> rows = getData();

        // Expected results
        ArrayList<Integer> expectedResult = new ArrayList<Integer>(Arrays.asList(4,3,5));
        ArrayList<Integer> actualResult = new ArrayList<>();

        // get index results
        for (ArrayList<Integer> row : rows) {
            actualResult.add(getIndex(row));
        }

        // Compare
        Assert.assertEquals(expectedResult, actualResult);
    }

     private static Integer getIndex(ArrayList<Integer> inputRow) {

        // For each potential index values
        for (int index=1; index<(inputRow.size()-1); ++index) {

            // Get either side sums and compare
            List<Integer> left = inputRow.subList( 0, index);
            Integer leftSum = left.stream().collect(Collectors.summingInt(Integer::intValue));
            List<Integer> right = inputRow.subList( index+1, inputRow.size());
            Integer rightSum = right.stream().collect(Collectors.summingInt(Integer::intValue));
            if (leftSum.compareTo(rightSum)==0) {
                return index;
            }
        }
        return null;
    }


    private static ArrayList<ArrayList<Integer>> getData() {
        // get page
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("http://localhost:3000/");

        // render challenge
        WebElement button = driver.findElement(By.xpath("//*[contains(@data-test-id,'render-challenge')]"));
        button.click();

        // get data
        ArrayList<ArrayList<Integer>> data = new  ArrayList<ArrayList<Integer>>();
        List<WebElement> rows = driver.findElements(By.tagName("tr"));
        // get rows
        for (WebElement row:rows) {
            // get field data
            ArrayList<Integer> fieldData =  new ArrayList<Integer>();
            List<WebElement> fields = row.findElements(By.tagName("td"));
            for (WebElement field:fields) {
                fieldData.add(Integer.parseInt(field.getText()));
            }
            data.add(fieldData);
        }
        driver.close();
        return data;
    }
}
