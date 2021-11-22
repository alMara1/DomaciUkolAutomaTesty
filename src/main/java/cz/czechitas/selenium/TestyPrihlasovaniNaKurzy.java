package cz.czechitas.selenium;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestyPrihlasovaniNaKurzy {

    public static final String UZIVATELSKY_EMAIL = "mara.vesely@gmail.com";
    public static final String HESLO = "123myJsmeBratri";
    public static final String URL = "https://cz-test-jedna.herokuapp.com/";
    public static final String TLACITKO_PRIHLASIT = "//button[@type='submit']";
    public static final String JMENO_ZAKA = "Albert Jáchym";
    public static final String PRIJMENI_ZAKA = "Veselý";
    public static final String DATUM_NAROZENI = "1.11.2011";
    public static final String TLACITKO_VYTVORIT_PRIHLASKU = "//button[@type='submit']";
    public static final String VYTVORIT_NOVOU_PRIHLASKU = "(//div[@class='card-header text-right'])/a[contains(text(),'Vytvořit')]";
    public static final String VYBRAT_TRETI_TYP_KURZU = "(//div[@class='card-body text-center'])[3]/a[contains(text(),'inform')]";
    public static final String VYTVORIT_PRIHLASKU_KURZU = "//a[contains(@class, 'btn') and contains(.,'Vytvořit')]";
    public static final String TEXT_PRIHLASEN_V_HORNI_LISTE = "//*[contains(@class, 'nav-item') and contains(.,'Přihlášen')]";
    public static final String BUTTON_TERMIN = "//button[@class='btn dropdown-toggle btn-light']";
    public static final String POLE_DATUM = "//input[@type='search']";

    WebDriver prohlizec;
    WebDriverWait cekani;


    @BeforeEach
    public void setUp() {
      System.setProperty("webdriver.gecko.driver", System.getProperty("user.home") + "/Java-Training/Selenium/geckodriver");
//        System.setProperty("webdriver.gecko.driver", "C:\\Java-Training\\Selenium\\geckodriver.exe");
        prohlizec = new FirefoxDriver();
        cekani = new WebDriverWait(prohlizec,5);
        prohlizec.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

    }

    @Disabled
    @Test // Ukol 1: Test přihlášení existujícího uživatele.
    public void poZadaniSpravnychUdajuJeUzivatelPrihlasen() {

        prohlizec.navigate().to(URL+"prihlaseni");
        najdiPodleIdAVlozText("email",UZIVATELSKY_EMAIL);
        najdiPodleIdAVlozText("password",HESLO);
        prohlizec.findElement(By.xpath(TLACITKO_PRIHLASIT)).click();
        Assertions.assertNotNull((cekani.until(ExpectedConditions.visibilityOfElementLocated(By.xpath
                        ("//*[contains(@class, 'nav-item') and contains(.,'Přihlášen')]")))),
                "Uživatel není prihlasen");
    }

    @Test // Ukol 3: Test Přihlásit do systemu -> Vybrat kurz -> Přihlásit dítě
    public void poPrihlaseniVybraniKurzuaPrihlaseniDiteteJePrihlaskaVSeznamu() {
        prihlasitUzivateleMarekVesely();
        najdiPodleXPathAKlikni(VYTVORIT_NOVOU_PRIHLASKU);
        najdiPodleXPathAKlikni(VYBRAT_TRETI_TYP_KURZU);
        najdiPodleXPathAKlikni(VYTVORIT_PRIHLASKU_KURZU);
        vyplnAPosliPrihlasku();
    }

    private void vyplnAPosliPrihlasku() {
        najdiPodleXPathAKlikni(BUTTON_TERMIN);
        najdiPodleXPathAVlozText(POLE_DATUM, "05\n");
        najdiPodleIdAVlozText("forename", JMENO_ZAKA);
        najdiPodleIdAVlozText("surname", PRIJMENI_ZAKA);
        najdiPodleIdAVlozText("birthday", DATUM_NAROZENI);
        najdiPodleIdAKlikni("payment_fksp");
        najdiPodleIdAVlozText("note","prihlaska Test 3");
        najdiPodleIdAKlikni("terms_conditions");
        prohlizec.findElement(By.xpath(TLACITKO_VYTVORIT_PRIHLASKU)).click();
    }


    public void prihlasitUzivateleMarekVesely() {
        prohlizec.navigate().to(URL + "prihlaseni");
        najdiPodleIdAVlozText("email", UZIVATELSKY_EMAIL);
        najdiPodleIdAVlozText("password", HESLO);
        prohlizec.findElement(By.xpath(TLACITKO_PRIHLASIT)).click();
        Assertions.assertNotNull((cekani.until(ExpectedConditions.visibilityOfElementLocated(By.xpath
                        (TEXT_PRIHLASEN_V_HORNI_LISTE)))),
                "Uživatel není prihlasen");
    }


    public void najdiPodleIdAVlozText(String idElementu, String posliText) {
        cekani.until(ExpectedConditions.visibilityOfElementLocated(By.id(idElementu))).sendKeys(posliText);
    }

    public void najdiPodleIdAKlikni(String idElementu) {
        cekani.until(ExpectedConditions.visibilityOfElementLocated(By.id(idElementu))).click();
    }

    public void najdiPodleXPathAVlozText(String xPathElementu, String posliText) {
        cekani.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPathElementu))).sendKeys(posliText);
    }

    public void najdiPodleXPathAKlikni(String xPathElementu) {
        cekani.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPathElementu))).click();
    }

    @Disabled
    @Test
    public void zviratVTabulceMusiByt7() {
        prohlizec.navigate().to("https://automation-playground.czechitas.repl.co/");
        WebElement zalozkaTabulka = prohlizec.findElement(By.id("table"));
        zalozkaTabulka.click();

        List<WebElement> seznamZvirat = prohlizec.findElements(By.xpath("//table/tbody/tr/td[1]"));
        Assertions.assertEquals(7, seznamZvirat.size());
    }
    @Disabled
    @Test
    public void prvniZvireVTabulceMusiBytKocka() {
        prohlizec.navigate().to("https://automation-playground.czechitas.repl.co/");
        WebElement zalozkaTabulka = prohlizec.findElement(By.id("table"));
        zalozkaTabulka.click();

        List<WebElement> seznamZvirat = prohlizec.findElements(By.xpath("//table/tbody/tr/td[1]"));
        WebElement elementPrvnihoZvirete = seznamZvirat.get(0);
        String prvniZvire = elementPrvnihoZvirete.getText();
        Assertions.assertEquals("Kočka", prvniZvire);
    }

    @AfterEach
    public void tearDown() {
        prohlizec.quit();
    }
}
