package cz.czechitas.selenium;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestyPrihlasovaniNaKurzy {

    public static final String UZIVATELSKY_EMAIL = "mara.vesely@gmail.com";
    public static final String HESLO = "123myJsmeBratri";
    public static final String URL = "https://cz-test-jedna.herokuapp.com/";
    public static final String TLACITKO_PRIHLASIT = "//button[@type='submit']";
    public static final String JMENO_ZAKA = "Petr";
    public static final String PRIJMENI_ZAKA = "Mírný";
    public static final String DATUM_NAROZENI = "1.11.2011";
    public static final String TLACITKO_VYTVORIT_PRIHLASKU = "//input[@type='submit']";
    public static final String VYTVORIT_NOVOU_PRIHLASKU = "(//div[@class='card-header text-right'])/a[contains(text(),'Vytvořit')]";
    public static final String VYBRAT_TRETI_TYP_KURZU = "(//div[@class='card-body text-center'])[3]/a[contains(text(),'inform')]";
    public static final String VYTVORIT_PRIHLASKU_KURZU = "//a[contains(@class, 'btn') and contains(.,'Vytvořit')]";
    public static final String TEXT_PRIHLASEN_V_HORNI_LISTE = "//*[contains(@class, 'nav-item') and contains(.,'Přihlášen')]";
    public static final String BUTTON_TERMIN = "//button[contains(@class,'btn dropdown-toggle btn-light')]";
    public static final String POLE_DATUM = "//input[@type='search']";
    public static final String SOUHLAS_S_PODMINKAMI = "//label[@class='custom-control-label' and @for='terms_conditions']";
    public static final String PLATBA_FKSP = "//label[@class='custom-control-label' and @for='payment_fksp']";
    public static final String ODKAZ_NA_PRIHLASENI = "//*[contains(@class, 'nav-item') and contains(.,'Přihlásit')]";
    public static final String ODKAZ_NA_PRIHLASKY = "//*[contains(@class, 'nav-item') and contains(.,'Přihlášky')]";
    public static final String TABULKA_PRIHLASENYCH_KURZU = "//table/tbody/tr/td[2]";
    public static final String DROP_DOWN_MENU_ODHLASENI = "//a[@class='dropdown-toggle']";
    public static final String TLACITKO_ODHLASIT = "logout-link";
    public static final String ODKAZ_DOMŮ = "//*[contains(@class, 'nav-item') and contains(.,'Domů')]";
    public static final String DRUHY_TYP_KURZU = "(//div[@class='card-body text-center'])[2]/a[contains(text(),'inform')]";

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


    @Test // Ukol 1: Test přihlášení existujícího uživatele.
    public void poZadaniSpravnychUdajuJeUzivatelPrihlasen() {
        prohlizec.navigate().to(URL);
        najdiPodleXPathAKlikni(ODKAZ_NA_PRIHLASENI);
        prihlasitUzivateleMarekVesely();
        Assertions.assertNotNull((cekani.until(ExpectedConditions.visibilityOfElementLocated(By.xpath
                        (TEXT_PRIHLASEN_V_HORNI_LISTE)))), "Uživatel není prihlasen");
    }


    @Test // Ukol 2: Test Vybrat kurz -> Přihlásit do systemu -> Přihlásit dítě
    public void poVybraniKurzuPrihlaseniaPrihlaseniDiteteJePrihlaskaVSeznamu () {
        prohlizec.navigate().to(URL);
        najdiPodleXPathAKlikni(ODKAZ_NA_PRIHLASENI);
        prihlasitUzivateleMarekVesely();
        int pocetKurzuPredPrihlaskou = kontrolaPoctuPrihlasenychKurzu();
        najdiPodleXPathAKlikni(DROP_DOWN_MENU_ODHLASENI);
        najdiPodleIdAKlikni(TLACITKO_ODHLASIT);
        najdiPodleXPathAKlikni(VYBRAT_TRETI_TYP_KURZU);
        najdiPodleXPathAKlikni(VYTVORIT_PRIHLASKU_KURZU);
        prihlasitUzivateleMarekVesely();
        vyplnAPosliPrihlasku();
        najdiPodleXPathAKlikni(ODKAZ_NA_PRIHLASKY);
        int pocetKurzuPoPrihlasce = kontrolaPoctuPrihlasenychKurzu();
        Assertions.assertEquals(pocetKurzuPredPrihlaskou,pocetKurzuPoPrihlasce-1,
                "Kurz nebyl registrovan");
    }

    @Test // Ukol 3: Test Přihlásit do systemu -> Vybrat kurz -> Přihlásit dítě
    public void poPrihlaseniVybraniKurzuaPrihlaseniDiteteJePrihlaskaVSeznamu() {
        prohlizec.navigate().to(URL);
        najdiPodleXPathAKlikni(ODKAZ_NA_PRIHLASENI);
        prihlasitUzivateleMarekVesely();
        int pocetKurzuPredPrihlaskou = kontrolaPoctuPrihlasenychKurzu();
        najdiPodleXPathAKlikni(VYTVORIT_NOVOU_PRIHLASKU);
        najdiPodleXPathAKlikni(VYBRAT_TRETI_TYP_KURZU);
        najdiPodleXPathAKlikni(VYTVORIT_PRIHLASKU_KURZU);
        vyplnAPosliPrihlasku();
        najdiPodleXPathAKlikni(ODKAZ_NA_PRIHLASKY);
        int pocetKurzuPoPrihlasce = kontrolaPoctuPrihlasenychKurzu();
        Assertions.assertEquals(pocetKurzuPredPrihlaskou,pocetKurzuPoPrihlasce-1,
                "Kurz nebyl registrovan");
    }

    @Test // Ukol 4: Test ověření počtu vypsaných termínů kurzu HTML - musi byt 4.
    public void pocetKurzuHTMLMusiByt4 () {
        prohlizec.navigate().to(URL);
        najdiPodleXPathAKlikni(ODKAZ_NA_PRIHLASENI);
        prihlasitUzivateleMarekVesely();
        najdiPodleXPathAKlikni(ODKAZ_DOMŮ);
        najdiPodleXPathAKlikni(DRUHY_TYP_KURZU);
        najdiPodleXPathAKlikni(VYTVORIT_PRIHLASKU_KURZU);
        najdiPodleXPathAKlikni(BUTTON_TERMIN);
        List<WebElement> pocetTerminu = prohlizec.findElements(By.xpath("//li/a[@class='dropdown-item']"));
        Assertions.assertEquals(pocetTerminu.size(),4,"Pocet terminu nesedi.");
    }

    private int kontrolaPoctuPrihlasenychKurzu() {
        List<WebElement> seznamKurzu = prohlizec.findElements(By.xpath(TABULKA_PRIHLASENYCH_KURZU));
        int pocetKurzu = seznamKurzu.size();
        return pocetKurzu;
    }

    private void vyplnAPosliPrihlasku() {
        najdiPodleXPathAKlikni(BUTTON_TERMIN);
        najdiPodleXPathAVlozText(POLE_DATUM, "03\n");
        najdiPodleIdAVlozText("forename", JMENO_ZAKA);
        najdiPodleIdAVlozText("surname", PRIJMENI_ZAKA);
        najdiPodleIdAVlozText("birthday", DATUM_NAROZENI);
        najdiPodleXPathAKlikni(PLATBA_FKSP);
        najdiPodleIdAVlozText("note","prihlaska Test 3");
        najdiPodleXPathAKlikni(SOUHLAS_S_PODMINKAMI);
        prohlizec.findElement(By.xpath(TLACITKO_VYTVORIT_PRIHLASKU)).click();
    }


    public void prihlasitUzivateleMarekVesely() {
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

    @AfterEach
    public void tearDown() {
        prohlizec.quit();
    }
}
