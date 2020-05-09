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

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class sdi1920_Entrega2_test {
    static String PathFirefox65 = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
    static String Geckdriver024 = "src\\com\\uniovi\\tests\\tools\\geckodriver024win64.exe";

    static WebDriver driver = getDriver(PathFirefox65, Geckdriver024);
    static String URLReset = "https://localhost:8081/reset";
    static String URL = "https://localhost:8081";


    public static WebDriver getDriver(String PathFirefox, String Geckdriver) {
        System.setProperty("webdriver.firefox.bin", PathFirefox);
        System.setProperty("webdriver.gecko.driver", Geckdriver);
        WebDriver driver = new FirefoxDriver();
        return driver;
    }

    @BeforeClass
    static public void begin() {
        PO_View.setTimeout(3);
    }

    @AfterClass
    static public void end() {
        driver.quit();
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

    /**
     * PR01
     * Registro de Usuario con datos vÃ¡lidos
     */
    @Test
    public void PR01() {
        // Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        // Rellenamos el formulario con datos válidos
        PO_RegisterView.fillForm(driver, "josefo@uniovi.es", "Josefo", "Perez", "77777",
                "77777");
    }

    /**
     * PR02
     * Registro de Usuario con datos invalidos (email vacio, nombre vacio, apellidos vacios)
     */
    @Test
    public void PR02() {
        // Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class",
                "btn btn-primary");
        // Rellenamos el formulario con campo email vacio.
        PO_RegisterView.fillForm(driver, "", "Josefo", "Martinez", "7777777", "7777777");
        PO_RegisterView.checkElement(driver, "text", "Email vacio");

        // Caso de las nombre vacio
        PO_RegisterView.fillForm(driver, "prueba@uniovi.es", "", "Martinez", "7777777", "7777777");
        PO_RegisterView.checkElement(driver, "text", "Campos vacios");

        // Caso de las apellidos vacio
        PO_RegisterView.fillForm(driver, "prueba@uniovi.es", "Josefo", "", "7777777", "7777777");
        PO_RegisterView.checkElement(driver, "text", "Campos vacios");
    }

    /**
     * PR03
     * Registro de Usuario con datos invalidos (repeticion de contraseña invalida)
     */
    @Test
    public void PR03() {
        // Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class",
                "btn btn-primary");
        // Rellenamos el formulario con contraseÃ±a que no coinciden.
        PO_RegisterView.fillForm(driver, "josefo@prueba.es", "Josefo",
                "Perez", "7777777", "7777778");
        PO_RegisterView.checkElement(driver, "text", "Las contraseñas deben ser iguales");
    }

    /**
     * PR04
     * Registro de Usuario con datos invÃ¡lidos (email existente)
     */
    @Test
    public void PR04() {
        // Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class",
                "btn btn-primary");
        // Rellenamos el formulario con contraseÃ±a que no coinciden.
        PO_RegisterView.fillForm(driver, "prueba@uniovi.es", "Josefo",
                "Perez", "7777777", "7777777");
        PO_RegisterView.checkElement(driver, "text", "El email ya existe en el sistema");
    }

    /**
     * PR05
     * Inicio de sesion con datos vÃ¡lidos (usuario estÃ¡ndar)
     */
    @Test
    public void PR05() {
        PO_HomeView.clickOption(driver, "login", "class",
                "btn btn-primary");
        PO_LoginView.fillForm(driver, "pedro@uniovi.es", "123456");
        PO_LoginView.checkElement(driver, "text", "Página principal");
        PO_HomeView.clickOption(driver, "logout", "class",
                "btn btn-primary");
    }

    /**
     * PR06
     * Inicio de sesion con datos invalidos (usuario estandar, campo email y contraseña vacios)
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
     * Inicio de sesiÃ³n con datos invÃ¡lidos (usuario estÃ¡ndar, email existente, pero contraseÃ±a
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
     * Inicio de sesiÃ³n con datos invÃ¡lidos (usuario estÃ¡ndar, email no existente y contraseÃ±a no vacÃ­a)
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
     * Hacer click en la opcion de salir de sesion y comprobar que se redirige a la pagina de inicio de
     * sesion (Login)
     */
    @Test
    public void PR09() {
        PO_HomeView.clickOption(driver, "login", "class",
                "btn btn-primary");
        PO_LoginView.fillForm(driver, "pedro@uniovi.es", "123456");
        PO_HomeView.clickOption(driver, "logout", "class",
                "btn btn-primary");
        PO_LoginView.checkElement(driver, "text", "Iniciar sesión");
    }

    /**
     * PR10
     * Comprobar que el boton cerrar sesiÃ³n no estÃ¡ visible si el usuario no estÃ¡ autenticado
     */
    @Test
    public void PR10() {
        SeleniumUtils.elementoNoPresentePagina(driver, "logout");
    }

    /**
     * PR11
     * Mostrar el listado de usuarios y comprobar que se muestran todos los que existen en el sistema
     */
    @Test
    public void PR11() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "prueba@uniovi.es", "123456");
        PO_HomeView.checkElement(driver, "text", "Página principal");
        PO_HomeView.clickOption(driver, "listUsers", "id", "listUsers");

        List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
        assertTrue(elementos.size() == 5);
    }

    /**
     * PR12
     * Hacer una bÃºsqueda con el campo vacÃ­o y comprobar que se muestra la pÃ¡gina que
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
     * Hacer una bÃºsqueda escribiendo en el campo un texto que no exista y comprobar que se
     * muestra la pÃ¡gina que corresponde, con la lista de usuarios vacÃ­a
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

        // Compruebo que no hay nada mÃ¡s que la cabecera de la tabla de ofertas
        assertTrue(elementos.size() < 2);
    }

    /**
     * PR14
     * Hacer una bÃºsqueda con un texto especÃ­fico y comprobar que se muestra la pÃ¡gina que
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

        // Compruebo que no hay nada mÃ¡s que la cabecera de la tabla de ofertas
        //assertTrue(checkNumRows("tableBids") > 0);
    }

    /**
     * PR15
     * Desde el listado de usuarios de la aplicación, enviar una invitación de amistad a un usuario.
     * Comprobar que la solicitud de amistad aparece en el listado de invitaciones (punto siguiente).
     */
    @Test
    public void PR15() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Se entra como usuario prueba@uniovi.es
        PO_LoginView.fillForm(driver, "prueba@uniovi.es", "123456");
        PO_View.getP();

        // Vamos a mirar la lista de usuarios
        List<WebElement> elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/listUsers')]");

        // Pinchamos en ver usuarios
        elementos.get(0).click();

        //Vamos a la paginacion 3, que es donde se encuentra anton@uniovi.es

        elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/listUsers?pg=3')]");

        elementos.get(0).click();

        // Pinchamos en enviar peticion de amistad
        elementos = PO_View.checkElement(driver, "free",
                "//td[contains(text(), 'anton@uniovi.es')]/following-sibling::*/a[contains(@name, 'peticion')]");

        // Se envia peticion
        elementos.get(0).click();

        // Se sale del usuario de prueba para entrar en el de anton
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");

        PO_LoginView.fillForm(driver, "anton@uniovi.es", "123456");

        // Vamos a la opción de  ver peticiones
        elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'peticiones')]");

        elementos.get(0).click();

        //Tenia 5 peticiones antes, por lo que ahora deberia tener 6, y tener paginacion.
        elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
        assertTrue(elementos.size() == 5); //5 EN LA PRIMERA PAGINACION

        //Ahora se pasa a la siguiente paginacion y esta solo 1, que es el nuevo
        elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/peticiones?pg=2')]");
        elementos.get(0).click();
        assertTrue(elementos.size() == 1); //Se encuentra la peticion

        //Prueba de que esa peticion es del usuario prueba
        elementos = PO_View.checkElement(driver, "free",
                "//td[contains(text(), 'prueba@uniovi.es')]");
        assertTrue(elementos.size() == 1);

        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
    }

    /**
     * PR16
     * Desde el listado de usuarios de la aplicación, enviar una invitación de amistad a un usuario al
     * que ya le habíamos enviado la invitación previamente. No debería dejarnos enviar la invitación, se podría
     * ocultar el botón de enviar invitación o notificar que ya había sido enviada previamente
     */
    @Test
    public void PR16() {
        //Vamos a utilizar lo de antes y mandarla una petición a anton, y luego ver que no podemos enviarla.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Se entra como usuario prueba@uniovi.es
        PO_LoginView.fillForm(driver, "prueba@uniovi.es", "123456");
        PO_View.getP();

        // Vamos a mirar la lista de usuarios
        List<WebElement> elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/listUsers')]");

        // Pinchamos en ver usuarios
        elementos.get(0).click();

        //Vamos a la paginacion 3, que es donde se encuentra anton@uniovi.es

        elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/listUsers?pg=3')]");

        elementos.get(0).click();

        // Pinchamos en enviar peticion de amistad
        elementos = PO_View.checkElement(driver, "free",
                "//td[contains(text(), 'anton@uniovi.es')]/following-sibling::*/a[contains(@name, 'peticion')]");

        // Se envia peticion
        elementos.get(0).click();

        //ANTON YA TIENE LA PETICION, AHORA VAMOS A MIRAR QUE NO PODEMOS ENVIARSELA
        elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/listUsers')]");
        elementos.get(0).click();
        elementos = PO_View.checkElement(driver, "free", "//ul[contains(@class, 'pagination')]/li");
        System.out.println(elementos.size());
        //elementos.get(0).click();
        elementos = PO_View.checkElement(driver, "free",
                "//td[contains(text(), 'anton@uniovi.es')]/following-sibling::*/a[contains(@name, 'peticion')]");
        elementos.get(0).click();
        PO_View.checkElement(driver, "text", "No se puede mandar petición a este usuario");
    }

    /**
     * PR17
     * Mostrar el listado de invitaciones de amistad recibidas. Comprobar con un listado que
     * contenga varias invitaciones recibidas.
     */
    @Test
    public void PR17() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Primero comprobamos que anton tiene 5 invitaciones y luego va a tener 6 y la sexta es de prueba
        PO_LoginView.fillForm(driver, "anton@uniovi.es", "123456");
        List<WebElement> elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'peticiones')]");
        elementos.get(0).click();
        elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
        assertTrue(elementos.size() == 5); //5 EN LA PRIMERA PAGINACION
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");

        //Ahora prueba@uniovi.es le va a enviar una peticion
        PO_LoginView.fillForm(driver, "prueba@uniovi.es", "123456");
        elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/listUsers')]");
        elementos.get(0).click();
        elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/listUsers?pg=3')]");
        elementos.get(0).click();
        elementos = PO_View.checkElement(driver, "free",
                "//td[contains(text(), 'anton@uniovi.es')]/following-sibling::*/a[contains(@name, 'peticion')]");
        elementos.get(0).click();
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");

        //Ahora anton va a tener las 5 y una nueva en la segunda paginacion
        PO_LoginView.fillForm(driver, "anton@uniovi.es", "123456");
        elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'peticiones')]");
        elementos.get(0).click();
        elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
        assertTrue(elementos.size() == 5); //5 EN LA PRIMERA PAGINACION

        elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/peticiones?pg=2')]");
        elementos.get(0).click();
        assertTrue(elementos.size() == 1);
        elementos = PO_View.checkElement(driver, "free",
                "//td[contains(text(), 'prueba@uniovi.es')]");
        assertTrue(elementos.size() == 1);
    }

    /**
     * PR18
     * Sobre el listado de invitaciones recibidas. Hacer click en el botón/enlace de una de ellas y
     * comprobar que dicha solicitud desaparece del listado de invitaciones.
     */
    @Test
    public void PR18() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //prueba@uniovi.es tiene 5 peticiones, si aceptamos una, debería tener 4
        PO_LoginView.fillForm(driver, "prueba@uniovi.es", "123456");
        List<WebElement> elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/peticiones')]");
        elementos.get(0).click();
        elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
        assertTrue(elementos.size() == 5);
        elementos = PO_View.checkElement(driver, "free",
                "//td[contains(text(), 'maria@uniovi.es')]/following-sibling::*/a[contains(@name, 'mandarPeticion')]");
        elementos.get(0).click();
        //Le aceptó la peticion a Maria, por lo que ahora habría 4 peticiones
        elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
        assertTrue(elementos.size() == 4);
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
    }

    /**
     * PR19
     * Mostrar el listado de amigos de un usuario. Comprobar que el listado contiene los amigos que
     * deben ser.
     */
    @Test
    public void PR19() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Anton le envia peticion
        PO_LoginView.fillForm(driver, "anton@uniovi.es", "123456");
        List<WebElement> elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/listUsers')]");
        elementos.get(0).click();
        elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/listUsers?pg=2')]");
        elementos.get(0).click();
        elementos = PO_View.checkElement(driver, "free",
                "//td[contains(text(), 'prueba@uniovi.es')]/following-sibling::*/a[contains(@name, 'peticion')]");
        elementos.get(0).click();
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");

        //Ahora entramos en el de prueba@uniovi.es
        //prueba@uniovi.es tiene 6 peticiones
        PO_LoginView.fillForm(driver, "prueba@uniovi.es", "123456");
        elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/amigos')]");
        elementos.get(0).click();
        //Tiene 5 amigos
        elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
        assertTrue(elementos.size() == 5);

        //Ahora va a aceptar la peticion de anton
        elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/peticiones')]");
        elementos.get(0).click();
        elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/peticiones?pg=2')]");
        elementos.get(0).click();
        elementos = PO_View.checkElement(driver, "free",
                "//td[contains(text(), 'anton@uniovi.es')]/following-sibling::*/a[contains(@name, 'mandarPeticion')]");
        elementos.get(0).click();
        //Le aceptó la peticion a Maria, por lo que ahora habría 5 peticiones de las que quedan
        elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
        assertTrue(elementos.size() == 5);

        //Y ademas ahora va a tener 6 amigos
        elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/amigos')]");
        elementos.get(0).click();
        //Tiene 5 amigos en la primera pag
        elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
        assertTrue(elementos.size() == 5);
        //Y en la segunda paginacion tendra uno y se encuentra anton
        elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/amigos?pg=2')]");
        elementos.get(0).click();
        elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());

        assertTrue(elementos.size() == 1);
        elementos = PO_View.checkElement(driver, "free",
                "//td[contains(text(), 'anton@uniovi.es')]");
        assertTrue(elementos.size() == 1);

        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");

    }

    /**
     * PR20
     * Intentar acceder sin estar autenticado a la opción de listado de usuarios. Se deberá volver al
     * formulario de login.
     */
    @Test
    public void PR20() {
        // Vamos a listado de usuarios
        driver.navigate().to("https://localhost:8081/listUsers");
        List<WebElement> elementos = PO_View.checkElement(driver, "free",
                "//h2[contains(text(), 'Log in')]");
        assertTrue(elementos.size() == 1);
    }

    /**
     * PR21
     * Intentar acceder sin estar autenticado a la opción de listado de invitaciones de amistad recibida
     * de un usuario estándar. Se deberá volver al formulario de login.
     */
    @Test
    public void PR21() {
        // Vamos a listado de peticiones
        driver.navigate().to("https://localhost:8081/peticiones");
        List<WebElement> elementos = PO_View.checkElement(driver, "free",
                "//h2[contains(text(), 'Log in')]");
        assertTrue(elementos.size() == 1);
    }

    /**
     * PR22
     * Intentar acceder estando autenticado como usuario standard a la lista de amigos de otro
     * usuario. Se deberá mostrar un mensaje de acción indebida
     */
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

