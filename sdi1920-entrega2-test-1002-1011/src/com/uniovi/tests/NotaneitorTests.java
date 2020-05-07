package com.uniovi.tests;

import com.uniovi.tests.pageobjects.PO_HomeView;
import com.uniovi.tests.pageobjects.PO_LoginView;
import com.uniovi.tests.pageobjects.PO_RegisterView;
import com.uniovi.tests.pageobjects.PO_View;
import com.uniovi.tests.util.SeleniumUtils;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.List;

import static org.junit.Assert.assertTrue;

//Paquetes JUnit
//Paquetes Selenium
//Paquetes Utilidades de Testing Propias
//Paquetes con los Page Object


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NotaneitorTests {
	static String PathFirefox65 = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
    static String Geckdriver024 = "tools\\geckodriver024win64.exe";

	static WebDriver driver = getDriver(PathFirefox65, Geckdriver024);
    static String URLReset = "https://localhost:8081/reset";
    static String URL = "https://localhost:8081";


    public static WebDriver getDriver(String PathFirefox, String Geckdriver) {
        System.setProperty("webdriver.firefox.bin", PathFirefox);
        System.setProperty("webdriver.gecko.driver", Geckdriver);
        WebDriver driver = new FirefoxDriver();
        return driver;
    }

    @Before
    public void setUp() {
    	driver.navigate().to(URLReset);
        driver.navigate().to(URL);
    }

    @After
    public void tearDown() {
        driver.manage().deleteAllCookies();
    }

    @BeforeClass
    static public void begin() {
        PO_View.setTimeout(3);
    }

    @AfterClass
    static public void end() {
        driver.quit();
    }

    /**
     * PR01
     * Registro de Usuario con datos válidos
     */
    @Test
    public void PR01() {
        // Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        // Rellenamos el formulario con email vacío.
        PO_RegisterView.fillForm(driver, "josefo@uniovi.es", "Josefo", "Perez", "77777",
                "77777");
    }

    /**
     * PR02
     * Registro de Usuario con datos inválidos (email vacío, nombre vacío, apellidos vacíos)
     */
    @Test
    public void PR02() {
        // TODO Esto está para comprobar la repetición de la contraseña (cambiar a email, nombre, etc)
        // Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class",
                "btn btn-primary");
        // Rellenamos el formulario con contraseña que no coinciden.
        PO_RegisterView.fillForm(driver, "josefo@prueba.es", "Josefo",
                "Perez", "7777777", "7777778");
        PO_RegisterView.checkElement(driver, "text", "Las contraseñas no coinciden");
    }

    /**
     * PR03
     * Registro de Usuario con datos inválidos (repetición de contraseña inválida)
     */
    @Test
    public void PR03() {
        // Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class",
                "btn btn-primary");
        // Rellenamos el formulario con contraseña que no coinciden.
        PO_RegisterView.fillForm(driver, "josefo@prueba.es", "Josefo",
                "Perez", "7777777", "7777778");
        PO_RegisterView.checkElement(driver, "text", "Las contraseñas no coinciden");
    }

    /**
     * PR04
     * Registro de Usuario con datos inválidos (email existente)
     */
    @Test
    public void PR04() {
        // TODO Comprobar el mensaje del email existente ya en la base de datos
        // Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class",
                "btn btn-primary");
        // Rellenamos el formulario con contraseña que no coinciden.
        PO_RegisterView.fillForm(driver, "josefo@prueba.es", "Josefo",
                "Perez", "7777777", "7777778");
        PO_RegisterView.checkElement(driver, "text", "Las contraseñas no coinciden");
    }

    /**
     * PR05
     * Inicio de sesión con datos válidos (usuario estándar)
     */
    @Test
    public void PR05() {
        PO_HomeView.clickOption(driver, "login", "class",
                "btn btn-primary");
        PO_LoginView.fillForm(driver, "pedro@uniovi.es", "123456");
        PO_LoginView.checkElement(driver, "text", "pedro@uniovi.es");
        PO_LoginView.checkElement(driver, "text", "Pedro Alonso");
        PO_HomeView.clickOption(driver, "logout", "class",
                "btn btn-primary");
    }

    /**
     * PR06
     * Inicio de sesión con datos inválidos (usuario estándar, campo email y contraseña vacíos)
     */
    @Test
    public void PR06() {
        PO_HomeView.clickOption(driver, "login", "class",
                "btn btn-primary");
        PO_LoginView.fillForm(driver, "", "123455");
        PO_LoginView.checkElement(driver, "text", "Email o password incorrecto");
        PO_LoginView.fillForm(driver, "pedro@uniovi.es", "");
        PO_LoginView.checkElement(driver, "text", "Email o password incorrecto");
    }

    /**
     * PR07
     * Inicio de sesión con datos inválidos (usuario estándar, email existente, pero contraseña
     * incorrecta)
     */
    @Test
    public void PR07() {
        PO_HomeView.clickOption(driver, "login", "class",
                "btn btn-primary");
        PO_LoginView.fillForm(driver, "pedro@uniovi.es", "123455");
        PO_LoginView.checkElement(driver, "text", "Email o password incorrecto");
    }

    /**
     * PR08
     * Inicio de sesión con datos inválidos (usuario estándar, email no existente y contraseña no vacía)
     */
    @Test
    public void PR08() {
        PO_HomeView.clickOption(driver, "login", "class",
                "btn btn-primary");
        PO_LoginView.fillForm(driver, "antunaalejandro@uniovi.es", "123456");
        PO_LoginView.checkElement(driver, "text", "Email o password incorrecto");
    }

    /**
     * PR09
     * Hacer click en la opción de salir de sesión y comprobar que se redirige a la página de inicio de
     * sesión (Login)
     */
    @Test
    public void PR09() {
        PO_HomeView.clickOption(driver, "login", "class",
                "btn btn-primary");
        PO_LoginView.fillForm(driver, "pedro@uniovi.es", "123456");
        PO_HomeView.clickOption(driver, "logout", "class",
                "btn btn-primary");
        PO_LoginView.checkElement(driver, "text", "Identificación de usuario");
    }

    /**
     * PR10
     * Comprobar que el botón cerrar sesión no está visible si el usuario no está autenticado
     */
    @Test
    public void PR10() {
        //SeleniumUtils.elementoNoPresentePagina(driver, "btnUser");
    }

    /**
     * PR11
     * Mostrar el listado de usuarios y comprobar que se muestran todos los que existen en el sistema
     */
    @Test
    public void PR11() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "admin@email.com", "admin");
        PO_HomeView.checkElement(driver, "text", "admin@email.com");
        PO_HomeView.clickOption(driver, "/user/list", "class",
                "btn btn-primary");

        List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());

        assertTrue(elementos.size() == 4);
    }

    /**
     * PR12
     * Hacer una búsqueda con el campo vacío y comprobar que se muestra la página que
     * corresponde con el listado usuarios existentes en el sistema
     */
    @Test
    public void PR12() {
        // TODO Adaptar a usuarios
        PO_HomeView.clickOption(driver, "login", "class",
                "btn btn-primary");
        PO_LoginView.fillForm(driver, "pedro@uniovi.es", "123456");
        PO_HomeView.checkElement(driver, "text", "pedro@uniovi.es");
        List<WebElement> elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/bid/search')]");
        elementos.get(0).click();
        //PO_SearchBidView.fillForm(driver, "");
        elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());

        assertTrue(elementos.size() == 5);
    }

    /**
     * PR13
     * Hacer una búsqueda escribiendo en el campo un texto que no exista y comprobar que se
     * muestra la página que corresponde, con la lista de usuarios vacía
     */
    @Test
    public void PR13() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "pedro@uniovi.es", "123456");
        PO_HomeView.checkElement(driver, "text", "pedro@uniovi.es");
        List<WebElement> elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/bid/search')]");
        elementos.get(0).click();
        //PO_SearchBidView.fillForm(driver, "asdfg");
        System.out.println(elementos.size());

        // Compruebo que no hay nada más que la cabecera de la tabla de ofertas
        assertTrue(elementos.size() < 2);
    }

    /**
     * PR14
     * Hacer una búsqueda con un texto específico y comprobar que se muestra la página que
     * corresponde, con la lista de usuarios en los que el texto especificados sea parte de su nombre, apellidos o
     * de su email
     */
    @Test
    public void PR14() {
        // TODO Adaptar a usuarios
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "pedro@uniovi.es", "123456");
        PO_HomeView.checkElement(driver, "text", "pedro@uniovi.es");
        List<WebElement> elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/bid/search')]");
        elementos.get(0).click();
        //PO_SearchBidView.fillForm(driver, "OFERTA");

        // Compruebo que no hay nada más que la cabecera de la tabla de ofertas
        //assertTrue(checkNumRows("tableBids") > 0);
    }

    //PR15. Sin hacer /
    @Test
    public void PR15() {
    	//Se entra como usuario prueba@uniovi.es
        PO_LoginView.fillForm(driver, "prueba@uniovi.es", "123456");
        PO_View.getP();
        
   		// Vamos a mirar la lista de usuarios
   		List<WebElement> elementos  = PO_View.checkElement(driver, "free", "//a[contains(@href, '/listUsers')]");

   			// Pinchamos en ver usuarios
   		elementos.get(0).click();
   		
        //Vamos a la paginacion 3, que es donde se encuentra anton@uniovi.es

   		elementos  = PO_View.checkElement(driver, "free", "//a[contains(@href, '/listUsers?pg=3')]");

   		elementos.get(0).click();

   		// Pinchamos en enviar peticion de amistad
        elementos = PO_View.checkElement(driver, "free",
        				"//td[contains(text(), 'anton@uniovi.es')]/following-sibling::*/a[contains(@name, 'peticion')]");
        
        // Se envia peticion
        elementos.get(0).click();

        // Se sale del usuario de prueba para entrar en el de anton
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");

        PO_LoginView.fillForm(driver, "anton@uniovi.es", "123456");

        // Vamos a la opci�n de  ver peticiones
        elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'peticiones')]");
        		
        elementos.get(0).click();

        //Tenia 5 peticiones antes, por lo que ahora deberia tener 6, y tener paginacion.
        elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
        assertTrue(elementos.size() == 5); //5 EN LA PRIMERA PAGINACION
        
        //Ahora se pasa a la siguiente paginacion y esta solo 1, que es el nuevo
        elementos  = PO_View.checkElement(driver, "free", "//a[contains(@href, '/peticiones?pg=2')]");
        elementos.get(0).click();
        assertTrue(elementos.size() == 1); //Se encuentra la peticion
           		
        	//Prueba de que esa peticion es del usuario prueba
        elementos = PO_View.checkElement(driver, "free",
        				"//td[contains(text(), 'prueba@uniovi.es')]");
        assertTrue(elementos.size() == 1);
        
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
    }

    //PR16. Sin hacer /
    @Test
    public void PR16() {
        assertTrue("PR16 sin hacer", false);
    }

    //PR017. Sin hacer /
    @Test
    public void PR17() {
        assertTrue("PR17 sin hacer", false);
    }

    //PR18. Sin hacer /
    @Test
    public void PR18() {
        assertTrue("PR18 sin hacer", false);
    }

    //PR19. Sin hacer /
    @Test
    public void PR19() {
        assertTrue("PR19 sin hacer", false);
    }

    //P20. Sin hacer /
    @Test
    public void PR20() {
        // Vamos a resetear
        driver.navigate().to("https://localhost:8081/reset");
        // Vamos a listado de usuarios
        driver.navigate().to("https://localhost:8081/listUsers");
        PO_View.checkElement(driver, "text", "Log in");
    }

    //PR21. Sin hacer /
    @Test
    public void PR21() {
        // Vamos a resetear
        driver.navigate().to("https://localhost:8081/reset");
        // Vamos a listado de usuarios
        driver.navigate().to("https://localhost:8081/peticiones");
        PO_View.checkElement(driver, "text", "Log in");
    }

    //PR22. Sin hacer /
    @Test
    public void PR22() {
        // Vamos a resetear
        driver.navigate().to("https://localhost:8081/reset");
        // Vamos a listado de usuarios
        driver.navigate().to("https://localhost:8081/amigos");
        PO_View.checkElement(driver, "text", "No puedes ver la lista de amigos sin estar identificado.");
    }

    //PR23. Sin hacer /
    @Test
    public void PR23() {
        assertTrue("PR23 sin hacer", false);
    }

    //PR24. Sin hacer /
    @Test
    public void PR24() {
        assertTrue("PR24 sin hacer", false);
    }

    //PR25. Sin hacer /
    @Test
    public void PR25() {
        assertTrue("PR25 sin hacer", false);
    }

    //PR26. Sin hacer /
    @Test
    public void PR26() {
        assertTrue("PR26 sin hacer", false);
    }

    //PR27. Sin hacer /
    @Test
    public void PR27() {
        assertTrue("PR27 sin hacer", false);
    }

    //PR029. Sin hacer /
    @Test
    public void PR29() {
        assertTrue("PR29 sin hacer", false);
    }

    //PR030. Sin hacer /
    @Test
    public void PR30() {
        assertTrue("PR30 sin hacer", false);
    }

    //PR031. Sin hacer /
    @Test
    public void PR31() {
        assertTrue("PR31 sin hacer", false);
    }


}

