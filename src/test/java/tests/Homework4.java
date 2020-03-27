package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Homework4 {
    protected static WebDriver driver;

    @BeforeMethod
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @AfterMethod
    public void teardown() {
        driver.quit();
    }

    //    DAYS
//1. go to http://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCheckBox
//2. Randomly select a checkbox. As soon as you check any day, print the name of the day
//    and uncheck immediately.
//    After you check and uncheck Friday for the third time, exit the program.
//  NOTE: Remember some checkboxes are not selectable. You need to find a way to ignore them
//    when they are randomly selected. It has to be dynamic. Do not hard code Saturday and Sunday.
//    Use values of certain attributes.
    @Test
    public void days() {
        driver.get("http://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCheckBox");
        List<WebElement> checkBoxes = driver.findElements(By.xpath("//input"));
        List<WebElement> days = driver.findElements(By.cssSelector(".gwt-CheckBox>label"));
        Random randomDays = new Random();
        int count = 0;
        while (count < 3) {
            //this method will return any value 0 and 7
            int index = randomDays.nextInt(days.size());
            //if we choose Enaled we can do it actions

            if (checkBoxes.get(index).isEnabled()) {
                days.get(index).click();
                if (days.get(index).getText().equals("Friday")) {
                    count++;
                }
                System.out.println(days.get(index).getText());
                days.get(index).click();
            }
        }

    }

    //TODAYS DATE
//1. go to http://practice.cybertekschool.com/dropdown
//2. verify that dropdowns under
// Select your date of birth display current year, month, day
    @Test
    public void todaysDate() {
        driver.get("http://practice.cybertekschool.com/dropdown");
        //  WebElement year =driver.findElement(By.id("year"));
        //   WebElement month =driver.findElement(By.id("month"));
        //  WebElement day  =driver.findElement(By.id("day"));

        Select year = new Select(driver.findElement(By.id("year")));
        Select month = new Select(driver.findElement(By.id("month")));
        Select day = new Select(driver.findElement(By.id("day")));

        String yearValue = year.getFirstSelectedOption().getText();
        String monthValue = month.getFirstSelectedOption().getText();
        String dayValue = day.getFirstSelectedOption().getText();

        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMMMdd");
        Assert.assertEquals(yearValue + monthValue + dayValue, sf.format(new Date()));

    }

//    YEARS, MONTHS, DAYS
//1. go to http://practice.cybertekschool.com/dropdown
//2. select a random year under Select your date of birth
//3. select month January
//4. verify that days dropdown has current number of days
//5. repeat steps 3, 4 for all the months
//NOTE: if you randomly select a leap year, verify February has 29 days

    @Test
    public void yearsMonthsDays() {
        driver.get("http://practice.cybertekschool.com/dropdown");

        Select year = new Select(driver.findElement(By.id("year")));
        Select month = new Select(driver.findElement(By.id("month")));
        Select day = new Select(driver.findElement(By.id("day")));

        Random r = new Random();
        int randomIndex = r.nextInt(year.getOptions().size());
        year.selectByIndex(randomIndex);
        //or
        // year.selectByIndex(new Random().nextInt(year.getOptions().size());

        List<String> months31 = new ArrayList<>(Arrays.asList(new String[]{"January", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"}));
        int febDays;
        int yearValue = Integer.parseInt(year.getFirstSelectedOption().getText());
        if (yearValue % 400 == 0 || yearValue % 4 == 0 && yearValue % 100 != 0) {
            febDays = 29;
        } else {
            febDays = 28;
        }
        for (int i = 0; i < 12; i++) {
            month.selectByIndex(i);
            if (months31.contains(month.getFirstSelectedOption().getText())) {
                Assert.assertEquals(day.getOptions().size(), 31);
            } else if (month.getFirstSelectedOption().getText().equals("February")) {
                Assert.assertEquals(day.getOptions().size(), febDays);
            } else {
                Assert.assertEquals(day.getOptions().size(), 30);
            }
        }
    }

    //    DEPARTMENTS SORT
//1. go to https://www.amazon.com
//2. verify that default value of the All departments dropdown is All
//3. verify that options in the All departments dropdown are not sorted alphabetically
    @Test
    public void departSort() {
        driver.get("https://www.amazon.com");
        Assert.assertEquals(driver.findElement(By.className("nav-search-label")).getText(), "All");
        List<WebElement> options = new Select(driver.findElement(By.id("searchDropdownBox"))).getOptions();
        boolean notAlphOrder = false;
        for (int i = 0; i < options.size() - 1; i++) {
            if (options.get(i).getText().compareTo(options.get(i + 1).getText()) > 0) {
                notAlphOrder = true;
                break;
            }
        }
        Assert.assertTrue(notAlphOrder);
    }

    //MAIN DEPARTMENTS
//1. go to https://www.amazon.com/gp/site-directory
//2. verify that every main department name
// (indicated by blue rectangles in the picture) is
//present in the All departments dropdown
// (indicated by green rectangle in the picture)
    @Test
    public void main_departments() {
        driver.get(" https://www.amazon.com/gp/site-directory");
        List<WebElement> mainDep = driver.findElements(By.tagName("h2"));
        List<WebElement> allDep = new Select(driver.findElement(By.id("searchDropdownBox"))).getOptions();

        Set<String> mainDeps = new HashSet<>();
        Set<String> allDeps = new HashSet<>();
        for (WebElement each : mainDep) {
            mainDeps.add(each.getText());
        }
        for (WebElement each : allDep) {
            allDeps.add(each.getText());
        }
        for (String each : mainDeps) {
            if (!allDeps.contains(each)) {
                System.out.println(each);
                System.out.println("This main dep is not in All dep list");
            }
        }
    }

    //    LINKS
//1. go to https://www.w3schools.com/
//2. find all the elements in the page with the tag a
//3. for each of those elements, if it is displayed
// on the page, print the text and the href of that
    @Test
    public void links() {
        driver.get("https://www.w3schools.com/");
        List<WebElement> elements = driver.findElements(By.tagName("a"));
        for (WebElement each : elements) {
            if (each.isDisplayed()) {
                System.out.println(each.getText());
                System.out.println(each.getAttribute("href"));
            }
        }
    }


//    VALID LINKS
//1. go to https://www.selenium.dev/documentation/en/
//2. find all the elements in the page with the tag a
//3. verify that all the links are valid

    @Test
    public void validLinks() throws MalformedURLException {
        driver.get("https://www.selenium.dev/documentation/en/");
        List<WebElement> links = driver.findElements(By.tagName("a"));
        for (int i = 0; i < links.size(); i++) {
            String href = links.get(i).getAttribute("href");
            try {
                URL url = new URL(href);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(3000);
                httpURLConnection.connect();
                Assert.assertTrue(httpURLConnection.getResponseCode() == 200);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
//    CART
//1. go to https://amazon.com
//2. search for "wooden spoon"
//3. click search
//4. remember the name and the price of a random result
//5. click on that random result
//6. verify default quantity of items is 1
//7. verify that the name and the price is the same as the one from step 5
//8. verify button "Add to Cart" is visible

    @Test
    public void cart(){
        driver.get("https://amazon.com");
        driver.findElement(By.id("twotabsearchtextbox")).sendKeys("wooden spoon");
        driver.findElement(By.xpath("//span[@id='nav-search-submit-text']/following-sibling::input")).click();
        List<WebElement> price = driver.findElements(By.xpath("//span[@class='a-price']/span[@class='a-offscreen']"));
        int x = new Random().nextInt(price.size());
        x = x==0?1:x;
        String originName = driver.findElement(By.xpath("(//span[@class='a-size-base-plus a-color-base a-text-normal'])["+x+"]")).getText();
        String originPrice = "$" +
                driver.findElement(By.xpath("(//span[@class='a-price']/span[2]/span[2])["+x+"]")).getText() +"."+
                driver.findElement(By.xpath("(//span[@class='a-price']/span[2]/span[3])["+x+"]")).getText();
        driver.findElement(By.xpath("(//span[@class='a-price-fraction'])["+x+"]")).click();
        Assert.assertEquals(driver.findElement(By.xpath("//span[text()='Qty:']/following-sibling::span")).getText(), "1");
        Assert.assertEquals(driver.findElement(By.id("productTitle")).getText(), originName);
        Assert.assertEquals(driver.findElement(By.id("price_inside_buybox")).getText(), originPrice);
        Assert.assertTrue(driver.findElement(By.id("add-to-cart-button")).isDisplayed());
    }

//    PRIME
//1. go to https://amazon.com
//2. search for "wooden spoon"
//3. click search
//4. remember name first result that has prime label
//5. select Prime checkbox on the left
//6. verify that name first result that has prime label is same as step 4
//7. check the last checkbox under Brand on the left
//8. verify that name first result that has prime label is different

    @Test
    public void prime(){
        driver.get("https://amazon.com");
        driver.findElement(By.id("twotabsearchtextbox")).sendKeys("wooden spoon");
        driver.findElement(By.xpath("//span[@id='nav-search-submit-text']/following-sibling::input")).click();
        WebElement firstPrimeName =driver.findElement(By.xpath("(//i[@aria-label='Amazon Prime']/../../../../../..//h2)[1]"));
        String name1 = firstPrimeName.getText();
        driver.findElement(By.xpath("//i[@class='a-icon a-icon-prime a-icon-medium']/../div/label/i")).click();
        String name2 = driver.findElement(By.xpath("(//i[@aria-label='Amazon Prime']/../../../../../..//h2)[1]")).getText();
        Assert.assertEquals(name2, name1);
        driver.findElement(By.xpath("//div[@id='brandsRefinements']//ul/li[last()]//i")).click();
        String name3 = driver.findElement(By.xpath("(//i[@aria-label='Amazon Prime']/../../../../../..//h2)[1]")).getText();
        Assert.assertNotEquals(name1, name3);
    }
//    MORE SPOONS
//1. go to https://amazon.com
//2. search for "wooden spoon"
//3. remember all Brand names on the left
//4. select Prime checkbox on the left
//5. verify that same Brand names are still displayed



    }


//CHEAP SPOONS
//1. go to https://amazon.com
//2. search for "wooden spoon"
//3. click on Price option Under $25 on the left
//4. verify that all results are cheaper than $25
