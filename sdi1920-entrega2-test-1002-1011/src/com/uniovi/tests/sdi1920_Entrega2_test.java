package com.uniovi.tests;

import com.uniovi.tests.pageobjects.*;
import com.uniovi.tests.util.SeleniumUtils;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.uniovi.tests.pageobjects.PO_LoginView;
import com.uniovi.tests.pageobjects.PO_NavView;
import com.uniovi.tests.pageobjects.PO_RegisterView;
import com.uniovi.tests.pageobjects.PO_SearchView;
import com.uniovi.tests.pageobjects.PO_View;
import com.uniovi.tests.util.SeleniumUtils;

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

	@Before
	public void setUp() {
		driver.navigate().to(URLReset);
		driver.navigate().to(URL);
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

	@BeforeClass
	static public void begin() {
		PO_View.setTimeout(3);
	}

    /**
     * PR01
     * Registro de Usuario con datos válidos
     */
    @Test
    public void PR01() {
        // Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        // Rellenamos el formulario con datos v�lidos
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
     * Registro de Usuario con datos invalidos (repeticion de contrase�a invalida)
     */
    @Test
    public void PR03() {
        // Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class",
                "btn btn-primary");
        // Rellenamos el formulario con contraseña que no coinciden.
        PO_RegisterView.fillForm(driver, "josefo@prueba.es", "Josefo",
                "Perez", "7777777", "7777778");
        PO_RegisterView.checkElement(driver, "text", "Las contrase�as deben ser iguales");
    }

    /**
     * PR04
     * Registro de Usuario con datos inválidos (email existente)
     */
    @Test
    public void PR04() {
        // Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class",
                "btn btn-primary");
        // Rellenamos el formulario con contraseña que no coinciden.
        PO_RegisterView.fillForm(driver, "prueba@uniovi.es", "Josefo",
                "Perez", "7777777", "7777777");
        PO_RegisterView.checkElement(driver, "text", "El email ya existe en el sistema");
    }

    /**
     * PR05
     * Inicio de sesion con datos válidos (usuario estándar)
     */
    @Test
    public void PR05() {
        PO_HomeView.clickOption(driver, "login", "class",
                "btn btn-primary");
        PO_LoginView.fillForm(driver, "pedro@uniovi.es", "123456");
        PO_LoginView.checkElement(driver, "text", "P�gina principal");
        PO_HomeView.clickOption(driver, "logout", "class",
                "btn btn-primary");
    }

    /**
     * PR06
     * Inicio de sesion con datos invalidos (usuario estandar, campo email y contrase�a vacios)
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
	 * PR07 Inicio de sesión con datos inválidos (usuario estándar, email
	 * existente, pero contraseña incorrecta)
	 */
	@Test
	public void PR07() {
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "pedro@uniovi.es", "123455");
		PO_LoginView.checkElement(driver, "text", "Email o password incorrecto");
	}

	/**
	 * PR08 Inicio de sesión con datos inválidos (usuario estándar, email no
	 * existente y contraseña no vacía)
	 */
	@Test
	public void PR08() {
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
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
        PO_LoginView.checkElement(driver, "text", "Iniciar sesi�n");
    }

    /**
     * PR10
     * Comprobar que el boton cerrar sesión no está visible si el usuario no está autenticado
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
        PO_HomeView.checkElement(driver, "text", "P�gina principal");
        PO_HomeView.clickOption(driver, "listUsers", "id", "listUsers");

        List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
        assertTrue(elementos.size() == 5);
    }

    /**
     * PR12
     * Hacer una busqueda con el campo vacio y comprobar que se muestra la pagina que
     * corresponde con el listado usuarios existentes en el sistema
     */
    @Test
    public void PR12() {
        PO_HomeView.clickOption(driver, "login", "class","btn btn-primary");
        PO_LoginView.fillForm(driver, "pedro@uniovi.es", "123456");
        PO_HomeView.checkElement(driver, "text", "P�gina principal");
        List<WebElement> elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/listUsers')]");
        elementos.get(0).click();
        PO_SearchView.fillForm(driver, "");
        elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
        assertTrue(elementos.size() == 5);
    }

    /**
     * PR13
     * Hacer una busqueda escribiendo en el campo un texto que no exista y comprobar que se
     * muestra la página que corresponde, con la lista de usuarios vacia
     */
    @Test
    public void PR13() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "pedro@uniovi.es", "123456");
        PO_HomeView.checkElement(driver, "text", "pedro@uniovi.es");
        List<WebElement> elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/listUsers')]");
        elementos.get(0).click();
        PO_SearchView.fillForm(driver, "asdfg");

        // Compruebo que no hay nada mas que la cabecera de la tabla de ofertas
        assertTrue(elementos.size() < 2);
    }

    /**
     * PR14
     * Hacer una busqueda con un texto especifico y comprobar que se muestra la pagina que
     * corresponde, con la lista de usuarios en los que el texto especificados sea parte de su nombre, apellidos o
     * de su email
     */
    @Test
    public void PR14() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "pedro@uniovi.es", "123456");
        PO_HomeView.checkElement(driver, "text", "P�gina principal");
        List<WebElement> elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/listUsers')]");
        elementos.get(0).click();
        PO_SearchView.fillForm(driver, "Pedr");

        // Compruebo que hay algo mas que la cabecera de la tabla de usuarios
        assertTrue(checkNumRows("datos") > 1);
    }

    /**
     * Metodo que comprueba el numero de filas de la tabla pasada como parametro
     * Utilizada unicamente para la PR14
     * @param table, la tabla a mirar
     * @return numero de filas
     */
    private int checkNumRows(String table){
        WebElement baseTable2 = driver.findElement(By.id(table));
        List<WebElement> tableRows2 = baseTable2.findElements(By.tagName("tr"));
        return tableRows2.size();
    }

    /**
     * PR15
     * Desde el listado de usuarios de la aplicaci�n, enviar una invitaci�n de amistad a un usuario.
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

		// ----------Segundo se entra como usuario prueba@uniovi.es para mandar peticion
		// a anton
		PO_LoginView.fillForm(driver, "prueba@uniovi.es", "123456");

		// Vamos a mirar la lista de usuarios
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/listUsers')]");

        // Vamos a la opci�n de  ver peticiones
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
     * Desde el listado de usuarios de la aplicaci�n, enviar una invitaci�n de amistad a un usuario al
     * que ya le hab�amos enviado la invitaci�n previamente. No deber�a dejarnos enviar la invitaci�n, se podr�a
     * ocultar el bot�n de enviar invitaci�n o notificar que ya hab�a sido enviada previamente
     */
    @Test
    public void PR16() {
        //Vamos a utilizar lo de antes y mandarla una petici�n a anton, y luego ver que no podemos enviarla.
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
        PO_View.checkElement(driver, "text", "No se puede mandar petici�n a este usuario");
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
     * Sobre el listado de invitaciones recibidas. Hacer click en el bot�n/enlace de una de ellas y
     * comprobar que dicha solicitud desaparece del listado de invitaciones.
     */
    @Test
    public void PR18() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //prueba@uniovi.es tiene 5 peticiones, si aceptamos una, deber�a tener 4
        PO_LoginView.fillForm(driver, "prueba@uniovi.es", "123456");
        List<WebElement> elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/peticiones')]");
        elementos.get(0).click();
        elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
        assertTrue(elementos.size() == 5);
        elementos = PO_View.checkElement(driver, "free",
                "//td[contains(text(), 'maria@uniovi.es')]/following-sibling::*/a[contains(@name, 'mandarPeticion')]");
        elementos.get(0).click();
        //Le acept� la peticion a Maria, por lo que ahora habr�a 4 peticiones
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
        //Le acept� la peticion a Maria, por lo que ahora habr�a 5 peticiones de las que quedan
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
     * Intentar acceder sin estar autenticado a la opci�n de listado de usuarios. Se deber� volver al
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
     * Intentar acceder sin estar autenticado a la opci�n de listado de invitaciones de amistad recibida
     * de un usuario est�ndar. Se deber� volver al formulario de login.
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
     * usuario. Se deber� mostrar un mensaje de acci�n indebida
     */
    @Test
    public void PR22() {
        // Vamos a resetear
        driver.navigate().to("https://localhost:8081/reset");
        // Vamos a listado de usuarios
        driver.navigate().to("https://localhost:8081/amigos");
        PO_View.checkElement(driver, "text", "No puedes ver la lista de amigos sin estar identificado.");
    }

	/**
	 * PR20 Intentar acceder sin estar autenticado a la opci�n de listado de
	 * usuarios. Se deber� volver al formulario de login.
	 */
	@Test
	public void PR20() {
		// Vamos a listado de usuarios
		driver.navigate().to("https://localhost:8081/listUsers");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//h2[contains(text(), 'Log in')]");
		assertTrue(elementos.size() == 1);
	}

	/**
	 * PR21 Intentar acceder sin estar autenticado a la opci�n de listado de
	 * invitaciones de amistad recibida de un usuario est�ndar. Se deber� volver al
	 * formulario de login.
	 */
	@Test
	public void PR21() {
		// Vamos a listado de peticiones
		driver.navigate().to("https://localhost:8081/peticiones");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//h2[contains(text(), 'Log in')]");
		assertTrue(elementos.size() == 1);
	}

	/**
	 * PR22 Intentar acceder estando autenticado como usuario standard a la lista de
	 * amigos de otro usuario. Se deber� mostrar un mensaje de acci�n indebida
	 */
	@Test
	public void PR22() {
		// Vamos a resetear
		driver.navigate().to("https://localhost:8081/reset");
		// Vamos a listado de usuarios
		driver.navigate().to("https://localhost:8081/amigos");
		PO_View.checkElement(driver, "text", "No puedes ver la lista de amigos sin estar identificado.");
	}

	/**
	 * PR23 Inicio de sesi�n con datos v�lidos.
	 */
	@Test
	public void PR23() {
		// Vamos al login
		driver.navigate().to("https://localhost:8081/cliente.html");
		// Rellenamos el formulario bien
		PO_LoginView.fillForm(driver, "prueba@uniovi.es", "123456");

		// Vemos que se va al listado de amigos
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//h2[contains(text(), 'Lista de amigos')]");
		assertTrue(elementos.size() == 1);
	}

	/**
	 * PR24 Inicio de sesi�n con datos inv�lidos (usuario no existente en la
	 * aplicaci�n).
	 */
	@Test
	public void PR24() {
		// Vamos al login
		driver.navigate().to("https://localhost:8081/cliente.html");
		// Rellenamos el formulario bien
		PO_LoginView.fillForm(driver, "prueba@uniovi.es", "12222223456");
		PO_View.checkElement(driver, "text", "Usuario no encontrado");

	}

	/**
	 * PR25 Acceder a la lista de amigos de un usuario, que al menos tenga tres
	 * amigos.
	 */
	@Test
	public void PR25() {
		// Como el usuario prueba@uniovi.es tiene varios amigos, vamos a probar con ese
		driver.navigate().to("https://localhost:8081/cliente.html");
		// Rellenamos el formulario bien
		PO_LoginView.fillForm(driver, "prueba@uniovi.es", "123456");

		// Se va directamente al listado de amigos, deberia haber 5
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementos.size() == 5);

	}

	/**
	 * PR26 Acceder a la lista de amigos de un usuario, y realizar un filtrado para
	 * encontrar a un amigo concreto, el nombre a buscar debe coincidir con el de un
	 * amigo.
	 */
	@Test
	public void PR26() {
		// Como el usuario prueba@uniovi.es tiene varios amigos, vamos a probar con ese
		driver.navigate().to("https://localhost:8081/cliente.html");
		// Rellenamos el formulario bien
		PO_LoginView.fillForm(driver, "prueba@uniovi.es", "123456");

		// Se va directamente al listado de amigos, deberia haber 5
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementos.size() == 5);

		// Ahora hacemos el filtrado por nombre, vamos a buscar a sdi
		PO_SearchView.fillForm(driver, "sdi");
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 1);

	}

	/**
	 * PR27 Acceder a la lista de mensajes de un amigo �chat�, la lista debe
	 * contener al menos tres mensajes.
	 */
	@Test
	public void PR27() {
		// En el reset a�adimos una conversacion de prueba e ines de 5 mensajes
		driver.navigate().to("https://localhost:8081/cliente.html");
		// Rellenamos el formulario bien
		PO_LoginView.fillForm(driver, "prueba@uniovi.es", "123456");

		// Vamos a dar click en ines
		List<WebElement> elementos = PO_View.checkElement(driver, "free",
				"//td[contains(text(), 'ines@uniovi.es')]/following-sibling::*/a[contains(@id, 'mensajes')]");
		elementos.get(0).click();

		// Ahora se va a abrir el chat con ines

		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 5);
	}

	/**
	 * PR29 Identificarse en la aplicaci�n y enviar un mensaje a un amigo, validar
	 * que el mensaje enviado aparece en el chat. Identificarse despu�s con el
	 * usuario que recibido el mensaje y validar que tiene un mensaje sin leer,
	 * entrar en el chat y comprobar que el mensaje pasa a tener el estado le�do.
	 */
	@Test
	public void PR29() {
		//Lo que se va a hacer es entrar como sdi, ver que no hay ningun mensaje sin leer
		//en la conversación con prueba, luego entrar como prueba, enviarle un mensaje.
		driver.navigate().to("https://localhost:8081/cliente.html");
		PO_LoginView.fillForm(driver, "sdi@uniovi.es", "123456");

		//Esperamos a que lo calcule
		SeleniumUtils.esperarSegundos(driver, 4);
		
		
		// Vemos lo de prueba
		List<WebElement> elementos = PO_View.checkElement(driver, "free",
				"//td[contains(text(), 'prueba@uniovi.es')]/following-sibling::*/a[contains(@name, 'numLeido')]");
		
		assertTrue(elementos.get(0).getText().equals("0"));
		

		// Ahora se va a prueba para enviarle un mensaje a sdi
		driver.manage().deleteAllCookies();
		driver.navigate().to("https://localhost:8081/cliente.html");
		PO_LoginView.fillForm(driver, "prueba@uniovi.es", "123456");
		elementos = PO_View.checkElement(driver, "free",
				"//td[contains(text(), 'sdi@uniovi.es')]/following-sibling::*/a[contains(@id, 'mensajes')]");
		
		elementos.get(0).click();
		
		
		//Ahora vamos a a�adir el mensaje
		
		WebElement search = driver.findElement(By.name("message"));
		search.click();
		search.clear();
		search.sendKeys("Mensaje de prueba");
		WebElement elemento = driver.findElement(By.id("boton-enviar"));
		elemento.click();
		
		//Ahora se encuentra como sdi
		driver.manage().deleteAllCookies();
		driver.navigate().to("https://localhost:8081/cliente.html");
		PO_LoginView.fillForm(driver, "sdi@uniovi.es", "123456");
		
		//Esperamos a que lo calcule
		SeleniumUtils.esperarSegundos(driver, 4);
		
		elementos = PO_View.checkElement(driver, "free",
				"//td[contains(text(), 'prueba@uniovi.es')]/following-sibling::*/a[contains(@name, 'numLeido')]");
		assertTrue(elementos.get(0).getText().equals("1"));
		elementos = PO_View.checkElement(driver, "free",
				"//td[contains(text(), 'prueba@uniovi.es')]/following-sibling::*/a[contains(@id, 'mensajes')]");
		elementos.get(0).click();
		//Primero no leido
		elementos = PO_View.checkElement(driver, "free", "//td[contains(text(), 'No leido')]");
		assertTrue(elementos.size() == 1);
		//Al segundo cambia a leido
		elementos = PO_View.checkElement(driver, "free", "//td[contains(text(), 'Leido')]");
		assertTrue(elementos.size() == 1);
		
		driver.navigate().to("https://localhost:8081/cliente.html");
		//Esperamos a que lo calcule y ahora habria 0
		SeleniumUtils.esperarSegundos(driver, 4);
		
		elementos = PO_View.checkElement(driver, "free",
				"//td[contains(text(), 'prueba@uniovi.es')]/following-sibling::*/a[contains(@name, 'numLeido')]");
		assertTrue(elementos.get(0).getText().equals("0"));
		
		
	}

	/**
	 * PR30 Identificarse en la aplicaci�n y enviar tres mensajes a un amigo,
	 * validar que los mensajes enviados aparecen en el chat. Identificarse despu�s
	 * con el usuario que recibido el mensaje y validar que el n�mero de mensajes
	 * sin leer aparece en la propia lista de amigos.
	 */
	@Test
	public void PR30() {
		//Lo que se va a hacer es entrar como sdi, ver que no hay ningun mensaje sin leer
				//en la conversación con prueba, luego entrar como prueba, enviarle un mensaje.
				driver.navigate().to("https://localhost:8081/cliente.html");
				PO_LoginView.fillForm(driver, "sdi@uniovi.es", "123456");

				//Esperamos a que lo calcule
				SeleniumUtils.esperarSegundos(driver, 4);
				
				
				// Vemos lo de prueba
				List<WebElement> elementos = PO_View.checkElement(driver, "free",
						"//td[contains(text(), 'prueba@uniovi.es')]/following-sibling::*/a[contains(@name, 'numLeido')]");
				
				assertTrue(elementos.get(0).getText().equals("0"));
				

				// Ahora se va a prueba para enviarle un mensaje a sdi
				driver.manage().deleteAllCookies();
				driver.navigate().to("https://localhost:8081/cliente.html");
				PO_LoginView.fillForm(driver, "prueba@uniovi.es", "123456");
				elementos = PO_View.checkElement(driver, "free",
						"//td[contains(text(), 'sdi@uniovi.es')]/following-sibling::*/a[contains(@id, 'mensajes')]");
				
				elementos.get(0).click();
				
				
				//Ahora vamos a añadir los mensajes
				
				WebElement search = driver.findElement(By.name("message"));
				search.click();
				search.clear();
				search.sendKeys("Mensaje de prueba");
				WebElement elemento = driver.findElement(By.id("boton-enviar"));
				elemento.click();
				SeleniumUtils.esperarSegundos(driver, 1);
				search.sendKeys("Mensaje de prueba2");
				elemento.click();
				SeleniumUtils.esperarSegundos(driver, 1);
				search.sendKeys("Mensaje de prueba3");
				elemento.click();
				
				//Ahora se encuentra como sdi
				driver.manage().deleteAllCookies();
				driver.navigate().to("https://localhost:8081/cliente.html");
				PO_LoginView.fillForm(driver, "sdi@uniovi.es", "123456");
				
				//Esperamos a que lo calcule
				SeleniumUtils.esperarSegundos(driver, 4);
				
				elementos = PO_View.checkElement(driver, "free",
						"//td[contains(text(), 'prueba@uniovi.es')]/following-sibling::*/a[contains(@name, 'numLeido')]");
				assertTrue(elementos.get(0).getText().equals("3"));
				
				
	}

	// PR031. Sin hacer /
	@Test
	public void PR31() {
		assertTrue("PR31 sin hacer", false);
	}

}
