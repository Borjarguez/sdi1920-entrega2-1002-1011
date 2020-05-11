package com.uniovi.tests;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
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

	static public int getCuantasPaginas() {
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//a[contains(@name, 'paginacion')]");
		return elementos.size();
	}

	/**
	 * PR01 Registro de Usuario con datos vÃƒÂ¡lidos
	 */
	@Test
	public void PR01() {
		// Vamos al formulario de registro
		PO_NavView.clickOption(driver, "signup", "class", "btn btn-primary");
		// Rellenamos el formulario con datos vÃ¯Â¿Â½lidos
		PO_RegisterView.fillForm(driver, "josefo@uniovi.es", "Josefo", "Perez", "77777", "77777");
	}

	/**
	 * PR02 Registro de Usuario con datos invalidos (email vacio, nombre vacio,
	 * apellidos vacios)
	 */
	@Test
	public void PR02() {
		// Vamos al formulario de registro
		PO_NavView.clickOption(driver, "signup", "class", "btn btn-primary");
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
	 * PR03 Registro de Usuario con datos invalidos (repeticion de contraseña
	 * invalida)
	 */
	@Test
	public void PR03() {
		// Vamos al formulario de registro
		PO_NavView.clickOption(driver, "signup", "class", "btn btn-primary");
		// Rellenamos el formulario con contraseña que no coinciden.
		PO_RegisterView.fillForm(driver, "josefo@prueba.es", "Josefo", "Perez", "7777777", "7777778");
		PO_RegisterView.checkElement(driver, "text", "Las contraseñas deben ser iguales");
	}

	/**
	 * PR04 Registro de Usuario con datos invalidos (email existente)
	 */
	@Test
	public void PR04() {
		// Vamos al formulario de registro
		PO_NavView.clickOption(driver, "signup", "class", "btn btn-primary");
		// Rellenamos el formulario con contraseña que no coinciden.
		PO_RegisterView.fillForm(driver, "prueba@uniovi.es", "Josefo", "Perez", "7777777", "7777777");
		PO_RegisterView.checkElement(driver, "text", "El email ya existe en el sistema");
	}

	/**
	 * PR05 Inicio de sesion con datos validos (usuario estandar)
	 */
	@Test
	public void PR05() {
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "pedro@uniovi.es", "123456");
		PO_LoginView.checkElement(driver, "text", "Página principal");
		PO_NavView.clickOption(driver, "logout", "class", "btn btn-primary");
	}

	/**
	 * PR06 Inicio de sesion con datos invalidos (usuario estandar, campo email y
	 * contraseña vacios)
	 */
	@Test
	public void PR06() {
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "", "123455");
		PO_LoginView.checkElement(driver, "text", "Email o password incorrecto");
		PO_LoginView.fillForm(driver, "pedro@uniovi.es", "");
		PO_LoginView.checkElement(driver, "text", "Email o password incorrecto");
	}

	/**
	 * PR07 Inicio de sesion con datos invalidos (usuario estandar, email existente,
	 * pero contraseña incorrecta)
	 */
	@Test
	public void PR07() {
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "pedro@uniovi.es", "123455");
		PO_LoginView.checkElement(driver, "text", "Email o password incorrecto");
	}

	/**
	 * PR08 Inicio de sesion con datos invalidos (usuario estandar, email no
	 * existente y contraseña no vacia)
	 */
	@Test
	public void PR08() {
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "antunaalejandro@uniovi.es", "123456");
		PO_LoginView.checkElement(driver, "text", "Email o password incorrecto");
	}

	/**
	 * PR09 Hacer click en la opcion de salir de sesion y comprobar que se redirige
	 * a la pagina de inicio de sesion (Login)
	 */
	@Test
	public void PR09() {
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "pedro@uniovi.es", "123456");
		PO_NavView.clickOption(driver, "logout", "class", "btn btn-primary");
		PO_LoginView.checkElement(driver, "text", "Iniciar sesión");
	}

	/**
	 * PR10 Comprobar que el boton cerrar sesion no esta visible si el usuario no
	 * esta autenticado
	 */
	@Test
	public void PR10() {
		SeleniumUtils.elementoNoPresentePagina(driver, "logout");
	}

	/**
	 * PR11 Mostrar el listado de usuarios y comprobar que se muestran todos los que
	 * existen en el sistema
	 */
	@Test
	public void PR11() {
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "prueba@uniovi.es", "123456");
		PO_NavView.checkElement(driver, "text", "Página principal");
		PO_NavView.clickOption(driver, "listUsers", "id", "listUsers");

		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementos.size() == 5);
	}

	/**
	 * PR12 Hacer una busqueda con el campo vacio y comprobar que se muestra la
	 * pagina que corresponde con el listado usuarios existentes en el sistema
	 */
	@Test
	public void PR12() {
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "pedro@uniovi.es", "123456");
		PO_NavView.checkElement(driver, "text", "Página principal");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/listUsers')]");
		elementos.get(0).click();
		PO_SearchView.fillForm(driver, "");
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 5);
	}

	/**
	 * PR13 Hacer una busqueda escribiendo en el campo un texto que no exista y
	 * comprobar que se muestra la pagina que corresponde, con la lista de usuarios
	 * vacia
	 */
	@Test
	public void PR13() {
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "pedro@uniovi.es", "123456");
		PO_NavView.checkElement(driver, "text", "pedro@uniovi.es");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/listUsers')]");
		elementos.get(0).click();
		PO_SearchView.fillForm(driver, "asdfg");

		// Compruebo que no hay nada mas que la cabecera de la tabla de ofertas
		assertTrue(elementos.size() < 2);
	}

	/**
	 * PR14 Hacer una busqueda con un texto especifico y comprobar que se muestra la
	 * pagina que corresponde, con la lista de usuarios en los que el texto
	 * especificados sea parte de su nombre, apellidos o de su email
	 */
	@Test
	public void PR14() {
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "pedro@uniovi.es", "123456");
		PO_NavView.checkElement(driver, "text", "Página principal");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/listUsers')]");
		elementos.get(0).click();
		PO_SearchView.fillForm(driver, "Pedr");

		// Compruebo que hay algo mas que la cabecera de la tabla de usuarios
		assertTrue(checkNumRows("datos") > 1);
	}

	/**
	 * Metodo que comprueba el numero de filas de la tabla pasada como parametro
	 * Utilizada unicamente para la PR14
	 * 
	 * @param table, la tabla a mirar
	 * @return numero de filas
	 */
	private int checkNumRows(String table) {
		WebElement baseTable2 = driver.findElement(By.id(table));
		List<WebElement> tableRows2 = baseTable2.findElements(By.tagName("tr"));
		return tableRows2.size();
	}

	/**
	 * PR15 Desde el listado de usuarios de la aplicacion, enviar una invitacion de
	 * amistad a un usuario. Comprobar que la solicitud de amistad aparece en el
	 * listado de invitaciones (punto siguiente).
	 */
	@Test
	public void PR15() {
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
		// ----------Primero vemos que anton tiene 5 peticiones y solo una paginacion en
		// peticiones
		PO_LoginView.fillForm(driver, "anton@uniovi.es", "123456");

		// Vamos a la opciï¿½n de ver peticiones
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'peticiones')]");
		elementos.get(0).click();

		// Tenia 5 peticiones antes, por lo que ahora deberia tener 6, y tener
		// paginacion.
		// Uno de paginacion y 5 en la primera paginacion
		assertTrue(getCuantasPaginas() == 1);

		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 5); // 5 EN LA PRIMERA PAGINACION

		PO_NavView.clickOption(driver, "logout", "class", "btn btn-primary");

		// ----------Segundo se entra como usuario prueba@uniovi.es para mandar peticion
		// a anton
		PO_LoginView.fillForm(driver, "prueba@uniovi.es", "123456");

		// Vamos a mirar la lista de usuarios
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/listUsers')]");

		// Pinchamos en ver usuarios
		elementos.get(0).click();

		// Vamos a la paginacion 3, que es donde se encuentra anton@uniovi.es
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/listUsers?pg=3')]");

		elementos.get(0).click();

		// Pinchamos en enviar peticion de amistad
		elementos = PO_View.checkElement(driver, "free",
				"//td[contains(text(), 'anton@uniovi.es')]/following-sibling::*/a[contains(@name, 'peticion')]");
		// Se envia peticion
		elementos.get(0).click();
		// Se sale del usuario de prueba para entrar en el de anton
		PO_NavView.clickOption(driver, "logout", "class", "btn btn-primary");

		// ----------Tercero se entra como usuario anton@uniovi.es para ver si se tiene
		// la peticion de prueba@uniovi.es
		PO_LoginView.fillForm(driver, "anton@uniovi.es", "123456");

		// Vamos a la opciï¿½n de ver peticiones
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'peticiones')]");

		elementos.get(0).click();

		// Tenia 5 peticiones antes, por lo que ahora deberia tener 6, y tener 2
		// paginacion.
		assertTrue(getCuantasPaginas() == 2);
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 5); // 5 EN LA PRIMERA PAGINACION

		// Ahora se pasa a la siguiente paginacion y esta solo 1, que es el nuevo
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/peticiones?pg=2')]");
		elementos.get(0).click();
		assertTrue(elementos.size() == 1); // Se encuentra la peticion

		// Prueba de que esa peticion es del usuario prueba
		elementos = PO_View.checkElement(driver, "free", "//td[contains(text(), 'prueba@uniovi.es')]");
		assertTrue(elementos.size() == 1);

		PO_NavView.clickOption(driver, "logout", "class", "btn btn-primary");
	}

	/**
	 * PR16 Desde el listado de usuarios de la aplicacion, enviar una invitacion de
	 * amistad a un usuario al que ya le habiamos enviado la invitacion previamente.
	 * No deberia dejarnos enviar la invitacion, se podria ocultar el boton de
	 * enviar invitacion o notificar que ya habia sido enviada previamente
	 */
	@Test
	public void PR16() {
		// ----------Primero vamos a mandarle una peticion a anton
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
		// Se entra como usuario prueba@uniovi.es
		PO_LoginView.fillForm(driver, "prueba@uniovi.es", "123456");

		// Vamos a mirar la lista de usuarios
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/listUsers')]");

		// Pinchamos en ver usuarios
		elementos.get(0).click();

		// Vamos a la paginacion 3, que es donde se encuentra anton@uniovi.es

		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/listUsers?pg=3')]");

		elementos.get(0).click();

		// Pinchamos en enviar peticion de amistad
		elementos = PO_View.checkElement(driver, "free",
				"//td[contains(text(), 'anton@uniovi.es')]/following-sibling::*/a[contains(@name, 'peticion')]");

		// Se envia peticion
		elementos.get(0).click();

		// ----------Segundo se intenta enviar otra vez la peticion y se ve que no se
		// puede
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/listUsers')]");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/listUsers?pg=3')]");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free",
				"//td[contains(text(), 'anton@uniovi.es')]/following-sibling::*/a[contains(@name, 'peticion')]");
		elementos.get(0).click();
		PO_View.checkElement(driver, "text", "No se puede mandar petición a este usuario");
	}

	/**
	 * PR17 Mostrar el listado de invitaciones de amistad recibidas. Comprobar con
	 * un listado que contenga varias invitaciones recibidas.
	 */
	@Test
	public void PR17() {
		// ----------Primero comprobamos que anton tiene 5 invitaciones y una de
		// paginacion
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");

		PO_LoginView.fillForm(driver, "anton@uniovi.es", "123456");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'peticiones')]");
		elementos.get(0).click();
		// 1 paginacion solo
		assertTrue(getCuantasPaginas() == 1);
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 5); // 5 EN LA PRIMERA PAGINACION
		PO_NavView.clickOption(driver, "logout", "class", "btn btn-primary");

		// ----------Segundo se entra como usuario prueba@uniovi.es para mandar peticion
		// a anton
		PO_LoginView.fillForm(driver, "prueba@uniovi.es", "123456");
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/listUsers')]");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/listUsers?pg=3')]");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free",
				"//td[contains(text(), 'anton@uniovi.es')]/following-sibling::*/a[contains(@name, 'peticion')]");
		elementos.get(0).click();
		PO_NavView.clickOption(driver, "logout", "class", "btn btn-primary");

		// ----------Tercero se entra como anton@uniovi.es para ver que tiene una
		// peticion mas y paginacion
		PO_LoginView.fillForm(driver, "anton@uniovi.es", "123456");
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'peticiones')]");
		elementos.get(0).click();
		// 2 paginacion
		assertTrue(getCuantasPaginas() == 2);

		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 5); // 5 EN LA PRIMERA PAGINACION

		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/peticiones?pg=2')]");
		elementos.get(0).click();
		// Una invitacion en la segunda paginacion
		assertTrue(elementos.size() == 1);
		// Esa invitacion es la de prueba
		elementos = PO_View.checkElement(driver, "free", "//td[contains(text(), 'prueba@uniovi.es')]");
		assertTrue(elementos.size() == 1);
	}

	/**
	 * PR18 Sobre el listado de invitaciones recibidas. Hacer click en el
	 * boton/enlace de una de ellas y comprobar que dicha solicitud desaparece del
	 * listado de invitaciones.
	 */
	@Test
	public void PR18() {
		// ----------Para probar esto con paginacion, primero anton envia peticion para
		// asi que haya 6 peticiones y haya paginacion
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "anton@uniovi.es", "123456");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/listUsers')]");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/listUsers?pg=2')]");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free",
				"//td[contains(text(), 'prueba@uniovi.es')]/following-sibling::*/a[contains(@name, 'peticion')]");
		elementos.get(0).click();
		PO_NavView.clickOption(driver, "logout", "class", "btn btn-primary");

		// ----------Se entra como prueba@uniovi.es
		PO_LoginView.fillForm(driver, "prueba@uniovi.es", "123456");
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/peticiones')]");
		elementos.get(0).click();
		// Hay dos de paginacion
		assertTrue(getCuantasPaginas() == 2);
		// Cinco en la primera paginacion
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 5);
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/peticiones?pg=2')]");
		elementos.get(0).click();
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 1);

		// Se acepta el de anton
		elementos = PO_View.checkElement(driver, "free",
				"//td[contains(text(), 'anton@uniovi.es')]/following-sibling::*/a[contains(@name, 'mandarPeticion')]");
		elementos.get(0).click();

		// ----------Le aceptï¿½ la peticion a anton, por lo que ahora van a tener 5
		// peticiones y 1 de paginacion
		// 1 de paginacion
		assertTrue(getCuantasPaginas() == 1);

		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 5);
		PO_NavView.clickOption(driver, "logout", "class", "btn btn-primary");
	}

	/**
	 * PR19 Mostrar el listado de amigos de un usuario. Comprobar que el listado
	 * contiene los amigos que deben ser.
	 */
	@Test
	public void PR19() {
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");

		// ----------Primero se entra como anton y se manda peticion a prueba
		PO_LoginView.fillForm(driver, "anton@uniovi.es", "123456");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/listUsers')]");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/listUsers?pg=2')]");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free",
				"//td[contains(text(), 'prueba@uniovi.es')]/following-sibling::*/a[contains(@name, 'peticion')]");
		elementos.get(0).click();
		PO_NavView.clickOption(driver, "logout", "class", "btn btn-primary");

		// ----------Segundo se entra como prueba y miramos que tiene 5 amigos y uno de
		// paginacion
		PO_LoginView.fillForm(driver, "prueba@uniovi.es", "123456");
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/amigos')]");
		elementos.get(0).click();

		// 1 de paginacion
		assertTrue(getCuantasPaginas() == 1);
		// Tiene 5 amigos
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 5);

		// ----------Tercero hay 6 peticiones, dos paginacion y se acepta la peticion de
		// anton
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/peticiones')]");
		elementos.get(0).click();
		// 2 de paginacion
		assertTrue(getCuantasPaginas() == 2);
		// 5 peticiones en la primera paginacion
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 5);
		// se va la segunda paginacion para aceptar la peticion de anton
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/peticiones?pg=2')]");
		elementos.get(0).click();
		// Hay una peticion en la segunda paginacion
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 1);
		elementos = PO_View.checkElement(driver, "free",
				"//td[contains(text(), 'anton@uniovi.es')]/following-sibling::*/a[contains(@name, 'mandarPeticion')]");
		elementos.get(0).click();

		// ----------Cuarto se mira que ahora hay 5 peticiones y uno de paginacion
		// 1 de paginacion
		assertTrue(getCuantasPaginas() == 1);
		// 5 de peticiones solo
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 5);

		// ----------Quinto se mira que ahora hay dos paginacion en amigos y 6 amigos en
		// total y el ultimo amigo es anton
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/amigos')]");
		elementos.get(0).click();

		// 2 de paginacion
		assertTrue(getCuantasPaginas() == 2);

		// Tiene 5 amigos en la primera pag
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 5);

		// Tiene un amigo en la segunda paginacion y es anton
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/amigos?pg=2')]");
		elementos.get(0).click();

		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 1);

		elementos = PO_View.checkElement(driver, "free", "//td[contains(text(), 'anton@uniovi.es')]");
		assertTrue(elementos.size() == 1);

		PO_NavView.clickOption(driver, "logout", "class", "btn btn-primary");

	}

	/**
	 * PR20 Intentar acceder sin estar autenticado a la opcion de listado de
	 * usuarios. Se deberi volver al formulario de login.
	 */
	@Test
	public void PR20() {
		// Vamos a listado de usuarios
		driver.navigate().to("https://localhost:8081/listUsers");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//h2[contains(text(), 'Iniciar sesión')]");
		assertTrue(elementos.size() == 1);
	}

	/**
	 * PR21 Intentar acceder sin estar autenticado a la opcion de listado de
	 * invitaciones de amistad recibida de un usuario estandar. Se deberi volver al
	 * formulario de login.
	 */
	@Test
	public void PR21() {
		// Vamos a listado de peticiones
		driver.navigate().to("https://localhost:8081/peticiones");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//h2[contains(text(), 'Iniciar sesión')]");
		assertTrue(elementos.size() == 1);
	}

	/**
	 * PR22 Intentar acceder estando autenticado como usuario standard a la lista de
	 * amigos de otro usuario. Se deberi mostrar un mensaje de accion indebida
	 */
	@Test
	public void PR22() {
		// Vamos a listado de peticiones
		driver.navigate().to("https://localhost:8081/peticiones");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//h2[contains(text(), 'Iniciar sesión')]");
		assertTrue(elementos.size() == 1);
	}

	/**
	 * PR23 Inicio de sesion con datos validos.
	 */
	@Test
	public void PR23() {
		driver.manage().deleteAllCookies();
		// Vamos al login
		driver.navigate().to("https://localhost:8081/cliente.html");
		// Rellenamos el formulario bien
		PO_LoginView.fillForm(driver, "prueba@uniovi.es", "123456");

		// Vemos que se va al listado de amigos
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//h2[contains(text(), 'Lista de amigos')]");
		assertTrue(elementos.size() == 1);
		driver.navigate().to("https://localhost:8081/");
	}

	/**
	 * PR24 Inicio de sesion con datos invalidos (usuario no existente en la
	 * aplicacio).
	 */
	@Test
	public void PR24() {
		driver.manage().deleteAllCookies();
		// Vamos al login
		driver.navigate().to("https://localhost:8081/cliente.html");
		// Rellenamos el formulario bien
		PO_LoginView.fillForm(driver, "prueba@uniovi.es", "12222223456");
		PO_View.checkElement(driver, "text", "Usuario no encontrado");
		driver.navigate().to("https://localhost:8081/");
	}

	/**
	 * PR25 Acceder a la lista de amigos de un usuario, que al menos tenga tres
	 * amigos.
	 */
	@Test
	public void PR25() {
		driver.manage().deleteAllCookies();
		// Como el usuario prueba@uniovi.es tiene varios amigos, vamos a probar con ese
		driver.navigate().to("https://localhost:8081/cliente.html");
		// Rellenamos el formulario bien
		PO_LoginView.fillForm(driver, "prueba@uniovi.es", "123456");

		// Se va directamente al listado de amigos, deberia haber 5
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementos.size() == 5);
		driver.navigate().to("https://localhost:8081/");

	}

	/**
	 * PR26 Acceder a la lista de amigos de un usuario, y realizar un filtrado para
	 * encontrar a un amigo concreto, el nombre a buscar debe coincidir con el de un
	 * amigo.
	 */
	@Test
	public void PR26() {
		driver.manage().deleteAllCookies();
		// Como el usuario prueba@uniovi.es tiene varios amigos, vamos a probar con ese
		driver.navigate().to("https://localhost:8081/cliente.html");
		// Rellenamos el formulario bien
		PO_LoginView.fillForm(driver, "prueba@uniovi.es", "123456");

		// Se va directamente al listado de amigos, deberia haber 5
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementos.size() == 5);

		// Ahora hacemos el filtrado por nombre, vamos a buscar a sdi
		WebElement search = driver.findElement(By.name("searchText"));
		search.click();
		search.clear();
		search.sendKeys("sdi");
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 1);
		driver.navigate().to("https://localhost:8081/");

	}

	/**
	 * PR27 Acceder a la lista de mensajes de un amigo chat, la lista debe contener
	 * al menos tres mensajes.
	 */
	@Test
	public void PR27() {
		driver.manage().deleteAllCookies();
		// En el reset aï¿½adimos una conversacion de prueba e ines de 5 mensajes
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
		driver.navigate().to("https://localhost:8081/");
	}
	
	/**
	 * PR28 Acceder a la lista de mensajes de un amigo “chat” y crear un nuevo mensaje, validar que el
	 * mensaje aparece en la lista de mensajes..
	 */
	@Test
	public void PR28() {
		driver.manage().deleteAllCookies();
		// En el reset a�adimos una conversacion de prueba e ines de 5 mensajes
		driver.navigate().to("https://localhost:8081/cliente.html");
		// Rellenamos el formulario bien
		PO_LoginView.fillForm(driver, "prueba@uniovi.es", "123456");

		// Vamos a dar click en ines
		List<WebElement> elementos = PO_View.checkElement(driver, "free",
				"//td[contains(text(), 'ines@uniovi.es')]/following-sibling::*/a[contains(@id, 'mensajes')]");
		elementos.get(0).click();

		// Ahora se va a abrir el chat con ines
		SeleniumUtils.esperarSegundos(driver, 4);
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 5);
		
		//Creamos el mensaje
		WebElement search = driver.findElement(By.name("message"));
		search.click();
		search.clear();
		search.sendKeys("Mensaje de prueba");
		WebElement elemento = driver.findElement(By.id("boton-enviar"));
		elemento.click();
		
		//Esperamos a que se cargue
		SeleniumUtils.esperarSegundos(driver, 4);
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 6);
		driver.navigate().to("https://localhost:8081/");
	}

	/**
	 * PR29 Identificarse en la aplicacion y enviar un mensaje a un amigo, validar
	 * que el mensaje enviado aparece en el chat. Identificarse despues con el
	 * usuario que recibido el mensaje y validar que tiene un mensaje sin leer,
	 * entrar en el chat y comprobar que el mensaje pasa a tener el estado leido.
	 */
	@Test
	public void PR29() {
		driver.manage().deleteAllCookies();
		// Lo que se va a hacer es entrar como sdi, ver que no hay ningun mensaje sin
		// leer
		// en la conversaciÃ³n con prueba, luego entrar como prueba, enviarle un
		// mensaje.
		driver.navigate().to("https://localhost:8081/cliente.html");
		PO_LoginView.fillForm(driver, "sdi@uniovi.es", "123456");

		// Esperamos a que lo calcule
		SeleniumUtils.esperarSegundos(driver, 4);

		// Vemos lo de prueba
		List<WebElement> elementos = PO_View.checkElement(driver, "free",
				"//td[contains(text(), 'prueba@uniovi.es')]/following-sibling::*/a[contains(@name, 'numLeido')]");

		assertTrue(elementos.get(0).getText().equals("0"));

		// Ahora se va a prueba para enviarle un mensaje a sdi
		driver.navigate().to("https://localhost:8081/");
		driver.manage().deleteAllCookies();
		driver.navigate().to("https://localhost:8081/cliente.html");
		PO_LoginView.fillForm(driver, "prueba@uniovi.es", "123456");
		elementos = PO_View.checkElement(driver, "free",
				"//td[contains(text(), 'sdi@uniovi.es')]/following-sibling::*/a[contains(@id, 'mensajes')]");

		elementos.get(0).click();

		// Ahora vamos a aï¿½adir el mensaje

		WebElement search = driver.findElement(By.name("message"));
		search.click();
		search.clear();
		search.sendKeys("Mensaje de prueba");
		WebElement elemento = driver.findElement(By.id("boton-enviar"));
		elemento.click();

		// Ahora se encuentra como sdi
		driver.navigate().to("https://localhost:8081/");
		driver.manage().deleteAllCookies();
		driver.navigate().to("https://localhost:8081/cliente.html");
		PO_LoginView.fillForm(driver, "sdi@uniovi.es", "123456");

		// Esperamos a que lo calcule
		SeleniumUtils.esperarSegundos(driver, 4);

		elementos = PO_View.checkElement(driver, "free",
				"//td[contains(text(), 'prueba@uniovi.es')]/following-sibling::*/a[contains(@name, 'numLeido')]");
		assertTrue(elementos.get(0).getText().equals("1"));
		elementos = PO_View.checkElement(driver, "free",
				"//td[contains(text(), 'prueba@uniovi.es')]/following-sibling::*/a[contains(@id, 'mensajes')]");
		elementos.get(0).click();
		// Primero no leido
		elementos = PO_View.checkElement(driver, "free", "//td[contains(text(), 'No leido')]");
		assertTrue(elementos.size() == 1);
		// Al segundo cambia a leido
		elementos = PO_View.checkElement(driver, "free", "//td[contains(text(), 'Leido')]");
		assertTrue(elementos.size() == 1);

		driver.navigate().to("https://localhost:8081/cliente.html");
		// Esperamos a que lo calcule y ahora habria 0
		SeleniumUtils.esperarSegundos(driver, 4);

		elementos = PO_View.checkElement(driver, "free",
				"//td[contains(text(), 'prueba@uniovi.es')]/following-sibling::*/a[contains(@name, 'numLeido')]");
		assertTrue(elementos.get(0).getText().equals("0"));
		driver.navigate().to("https://localhost:8081/");

	}

	/**
	 * PR30 Identificarse en la aplicacion y enviar tres mensajes a un amigo,
	 * validar que los mensajes enviados aparecen en el chat. Identificarse despues
	 * con el usuario que recibido el mensaje y validar que el numero de mensajes
	 * sin leer aparece en la propia lista de amigos.
	 */
	@Test
	public void PR30() {
		driver.manage().deleteAllCookies();
		// Lo que se va a hacer es entrar como sdi, ver que no hay ningun mensaje sin
		// leer
		// en la conversacion con prueba, luego entrar como prueba, enviarle un
		// mensaje.
		driver.navigate().to("https://localhost:8081/cliente.html");
		PO_LoginView.fillForm(driver, "sdi@uniovi.es", "123456");

		// Esperamos a que lo calcule
		SeleniumUtils.esperarSegundos(driver, 4);

		// Vemos lo de prueba
		List<WebElement> elementos = PO_View.checkElement(driver, "free",
				"//td[contains(text(), 'prueba@uniovi.es')]/following-sibling::*/a[contains(@name, 'numLeido')]");

		assertTrue(elementos.get(0).getText().equals("0"));

		// Ahora se va a prueba para enviarle un mensaje a sdi
		driver.navigate().to("https://localhost:8081/");
		driver.manage().deleteAllCookies();
		driver.navigate().to("https://localhost:8081/cliente.html");
		PO_LoginView.fillForm(driver, "prueba@uniovi.es", "123456");
		elementos = PO_View.checkElement(driver, "free",
				"//td[contains(text(), 'sdi@uniovi.es')]/following-sibling::*/a[contains(@id, 'mensajes')]");

		elementos.get(0).click();

		// Ahora vamos a aÃ±adir los mensajes

		WebElement search = driver.findElement(By.name("message"));
		search.click();
		search.clear();
		search.sendKeys("Mensaje de prueba");
		WebElement elemento = driver.findElement(By.id("boton-enviar"));
		elemento.click();
		search.clear();
		SeleniumUtils.esperarSegundos(driver, 1);
		search.sendKeys("Mensaje de prueba2");
		elemento.click();
		search.clear();
		SeleniumUtils.esperarSegundos(driver, 1);
		search.sendKeys("Mensaje de prueba3");
		elemento.click();

		// Ahora se encuentra como sdi
		driver.navigate().to("https://localhost:8081/");
		driver.manage().deleteAllCookies();
		driver.navigate().to("https://localhost:8081/cliente.html");
		PO_LoginView.fillForm(driver, "sdi@uniovi.es", "123456");

		// Esperamos a que lo calcule
		SeleniumUtils.esperarSegundos(driver, 4);

		elementos = PO_View.checkElement(driver, "free",
				"//td[contains(text(), 'prueba@uniovi.es')]/following-sibling::*/a[contains(@name, 'numLeido')]");
		assertTrue(elementos.get(0).getText().equals("3"));
		driver.navigate().to("https://localhost:8081/");

	}

	

}
