package pom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pom.elements.StudentTableRow;

import javax.naming.Name;
import java.util.List;

public class MainPage {

    private final WebDriverWait wait;

    @FindBy(css = "nav li.mdc-menu-surface--anchor a")
    private WebElement usernameLinkInNavBar;

    @FindBy(id = "create-btn")
    private WebElement createButton;
    @FindBy(css = "form div.submit button")
    private WebElement submitButton;
    @FindBy(xpath = "//form//span[contains(text(), 'Fist Name')]/following-sibling::input")
    private WebElement firstName;
    @FindBy(xpath = "//form//span[contains(text(), 'Login')]/following-sibling::input")
    private WebElement login;
    @FindBy(xpath = "//table[@aria-label='Dummies list']/tbody/tr")
    private List<WebElement> rowsInStudentTable;

    public MainPage(WebDriver driver, WebDriverWait wait) {
        PageFactory.initElements(driver, this);
        this.wait = wait;
    }

    public WebElement waitAndGetStudentTitleByText(String title) {
        String xpath = String.format("//table[@aria-label='Dummies list']/tbody//td[text()='%s']", title);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
    }

    public void createStudent(String firstNameDummie, String loginDummie) {
        wait.until(ExpectedConditions.visibilityOf(createButton)).click();
        wait.until(ExpectedConditions.visibilityOf(firstName)).sendKeys(firstNameDummie);
        wait.until(ExpectedConditions.visibilityOf(login)).sendKeys(loginDummie);
        submitButton.click();
        waitAndGetStudentTitleByText(firstNameDummie);
    }

    public String getUsernameLabelText() {
        return wait.until(ExpectedConditions.visibilityOf(usernameLinkInNavBar))
                .getText().replace("\n", " ");
    }

    public StudentTableRow getStudentRowByName(String Name) {
        wait.until(ExpectedConditions.visibilityOfAllElements(rowsInStudentTable));
        return rowsInStudentTable.stream()
                .map(StudentTableRow::new)
                .filter(row -> row.getName().equals(Name))
                .findFirst().orElseThrow();
    }

    public String getFirstGeneratedStudentName() {
        wait.until(ExpectedConditions.visibilityOfAllElements(rowsInStudentTable));
        return rowsInStudentTable.stream()
                .map(StudentTableRow::new)
                .findFirst().orElseThrow().getName();
    }

    public void clickTrashIconOnStudentWithName(String name) {
        getStudentRowByName(name).clickTrashIcon();
    }

    public void clickRestoreFromTrashIconOnStudentWithName(String name) {
        getStudentRowByName(name).clickRestoreFromTrashIcon();
    }

    public String getStatusOfStudentWithName(String name) {
        return getStudentRowByName(name).getStatus();
    }

}
