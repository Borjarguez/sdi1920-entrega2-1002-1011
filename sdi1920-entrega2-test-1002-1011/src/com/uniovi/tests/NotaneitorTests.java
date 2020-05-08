package com.uniovi.tests;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.uniovi.tests.pageobjects.PO_LoginView;
import com.uniovi.tests.pageobjects.PO_NavView;
import com.uniovi.tests.pageobjects.PO_RegisterView;
import com.uniovi.tests.pageobjects.PO_SearchView;
import com.uniovi.tests.pageobjects.PO_View;
import com.uniovi.tests.util.SeleniumUtils;

//Paquetes JUnit
//Paquetes Selenium
//Paquetes Utilidades de Testing Propias
//Paquetes con los Page Object

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NotaneitorTests {
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

	static public int getCuantasPaginas() {
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//a[contains(@name, 'paginacion')]");
		return elementos.size();
	}

	/**
	 * PR01 Registro de Usuario con datos v√°lidos
	 */
	@Test
	public void PR01() {
		// Vamos al formulario de registro
		PO_NavView.clickOption(driver, "signup", "class", "btn btn-primary");
		// Rellenamos el formulario con email vac√≠o.
		PO_RegisterView.fillForm(driver, "josefo@uniovi.es", "Josefo", "Perez", "77777", "77777");
	}

	/**
	 * PR02 Registro de Usuario con datos inv√°lidos (email vac√≠o, nombre vac√≠o,
	 * apellidos vac√≠os)
	 */
	@Test
	public void PR02() {
		// TODO Esto est√° para comprobar la repetici√≥n de la contrase√±a (cambiar a
		// email, nombre, etc)
		// Vamos al formulario de registro
		PO_NavView.clickOption(driver, "signup", "class", "btn btn-primary");
		// Rellenamos el formulario con contrase√±a que no coinciden.
		PO_RegisterView.fillForm(driver, "josefo@prueba.es", "Josefo", "Perez", "7777777", "7777778");
		PO_RegisterView.checkElement(driver, "text", "Las contrase√±as no coinciden");
	}

	/**
	 * PR03 Registro de Usuario con datos inv√°lidos (repetici√≥n de contrase√±a
	 * inv√°lida)
	 */
	@Test
	public void PR03() {
		// Vamos al formulario de registro
		PO_NavView.clickOption(driver, "signup", "class", "btn btn-primary");
		// Rellenamos el formulario con contrase√±a que no coinciden.
		PO_RegisterView.fillForm(driver, "josefo@prueba.es", "Josefo", "Perez", "7777777", "7777778");
		PO_RegisterView.checkElement(driver, "text", "Las contrase√±as no coinciden");
	}

	/**
	 * PR04 Registro de Usuario con datos inv√°lidos (email existente)
	 */
	@Test
	public void PR04() {
		// TODO Comprobar el mensaje del email existente ya en la base de datos
		// Vamos al formulario de registro
		PO_NavView.clickOption(driver, "signup", "class", "btn btn-primary");
		// Rellenamos el formulario con contrase√±a que no coinciden.
		PO_RegisterView.fillForm(driver, "josefo@prueba.es", "Josefo", "Perez", "7777777", "7777778");
		PO_RegisterView.checkElement(driver, "text", "Las contrase√±as no coinciden");
	}

	/**
	 * PR05 Inicio de sesi√≥n con datos v√°lidos (usuario est√°ndar)
	 */
	@Test
	public void PR05() {
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "pedro@uniovi.es", "123456");
		PO_LoginView.checkElement(driver, "text", "pedro@uniovi.es");
		PO_LoginView.checkElement(driver, "text", "Pedro Alonso");
		PO_NavView.clickOption(driver, "logout", "class", "btn btn-primary");
	}

	/**
	 * PR06 Inicio de sesi√≥n con datos inv√°lidos (usuario est√°ndar, campo email y
	 * contrase√±a vac√≠os)
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
	 * PR07 Inicio de sesi√≥n con datos inv√°lidos (usuario est√°ndar, email
	 * existente, pero contrase√±a incorrecta)
	 */
	@Test
	public void PR07() {
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "pedro@uniovi.es", "123455");
		PO_LoginView.checkElement(driver, "text", "Email o password incorrecto");
	}

	/**
	 * PR08 Inicio de sesi√≥n con datos inv√°lidos (usuario est√°ndar, email no
	 * existente y contrase√±a no vac√≠a)
	 */
	@Test
	public void PR08() {
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "antunaalejandro@uniovi.es", "123456");
		PO_LoginView.checkElement(driver, "text", "Email o password incorrecto");
	}

	/**
	 * PR09 Hacer click en la opci√≥n de salir de sesi√≥n y comprobar que se
	 * redirige a la p√°gina de inicio de sesi√≥n (Login)
	 */
	@Test
	public void PR09() {
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "pedro@uniovi.es", "123456");
		PO_NavView.clickOption(driver, "logout", "class", "btn btn-primary");
		PO_LoginView.checkElement(driver, "text", "Identificaci√≥n de usuario");
	}

	/**
	 * PR10 Comprobar que el bot√≥n cerrar sesi√≥n no est√° visible si el usuario no
	 * est√° autenticado
	 */
	@Test
	public void PR10() {
		// SeleniumUtils.elementoNoPresentePagina(driver, "btnUser");
	}

	/**
	 * PR11 Mostrar el listado de usuarios y comprobar que se muestran todos los que
	 * existen en el sistema
	 */
	@Test
	public void PR11() {
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "admin@email.com", "admin");
		PO_NavView.checkElement(driver, "text", "admin@email.com");
		PO_NavView.clickOption(driver, "/user/list", "class", "btn btn-primary");

		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());

		assertTrue(elementos.size() == 4);
	}

	/**
	 * PR12 Hacer una b√∫squeda con el campo vac√≠o y comprobar que se muestra la
	 * p√°gina que corresponde con el listado usuarios existentes en el sistema
	 */
	@Test
	public void PR12() {
		// TODO Adaptar a usuarios
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "pedro@uniovi.es", "123456");
		PO_NavView.checkElement(driver, "text", "pedro@uniovi.es");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/bid/search')]");
		elementos.get(0).click();
		// PO_SearchBidView.fillForm(driver, "");
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());

		assertTrue(elementos.size() == 5);
	}

	/**
	 * PR13 Hacer una b√∫squeda escribiendo en el campo un texto que no exista y
	 * comprobar que se muestra la p√°gina que corresponde, con la lista de usuarios
	 * vac√≠a
	 */
	@Test
	public void PR13() {
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "pedro@uniovi.es", "123456");
		PO_NavView.checkElement(driver, "text", "pedro@uniovi.es");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/bid/search')]");
		elementos.get(0).click();
		// PO_SearchBidView.fillForm(driver, "asdfg");
		System.out.println(elementos.size());

		// Compruebo que no hay nada m√°s que la cabecera de la tabla de ofertas
		assertTrue(elementos.size() < 2);
	}

	/**
	 * PR14 Hacer una b√∫squeda con un texto espec√≠fico y comprobar que se muestra
	 * la p√°gina que corresponde, con la lista de usuarios en los que el texto
	 * especificados sea parte de su nombre, apellidos o de su email
	 */
	@Test
	public void PR14() {
		// TODO Adaptar a usuarios
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "pedro@uniovi.es", "123456");
		PO_NavView.checkElement(driver, "text", "pedro@uniovi.es");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/bid/search')]");
		elementos.get(0).click();
		// PO_SearchBidView.fillForm(driver, "OFERTA");

		// Compruebo que no hay nada m√°s que la cabecera de la tabla de ofertas
		// assertTrue(checkNumRows("tableBids") > 0);
	}

	/**
	 * PR15 Desde el listado de usuarios de la aplicaciÛn, enviar una invitaciÛn de
	 * amistad a un usuario. Comprobar que la solicitud de amistad aparece en el
	 * listado de invitaciones (punto siguiente).
	 */
	@Test
	public void PR15() {
		PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
		// ----------Primero vemos que anton tiene 5 peticiones y solo una paginacion en
		// peticiones
		PO_LoginView.fillForm(driver, "anton@uniovi.es", "123456");

		// Vamos a la opciÛn de ver peticiones
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

		// Vamos a la opciÛn de ver peticiones
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
	 * PR16 Desde el listado de usuarios de la aplicaciÛn, enviar una invitaciÛn de
	 * amistad a un usuario al que ya le habÌamos enviado la invitaciÛn previamente.
	 * No deberÌa dejarnos enviar la invitaciÛn, se podrÌa ocultar el botÛn de
	 * enviar invitaciÛn o notificar que ya habÌa sido enviada previamente
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

		// ----------Segundo se intenta enviar otra vez la peticion y se ve que no se puede
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/listUsers')]");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/listUsers?pg=3')]");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free",
				"//td[contains(text(), 'anton@uniovi.es')]/following-sibling::*/a[contains(@name, 'peticion')]");
		elementos.get(0).click();
		PO_View.checkElement(driver, "text", "No se puede mandar peticiÛn a este usuario");
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
		PO_NavView.clickOption(driver,"logout", "class", "btn btn-primary");

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
	 * botÛn/enlace de una de ellas y comprobar que dicha solicitud desaparece del
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

		// ----------Le aceptÛ la peticion a anton, por lo que ahora van a tener 5
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
	 * PR20 Intentar acceder sin estar autenticado a la opciÛn de listado de
	 * usuarios. Se deber· volver al formulario de login.
	 */
	@Test
	public void PR20() {
		// Vamos a listado de usuarios
		driver.navigate().to("https://localhost:8081/listUsers");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//h2[contains(text(), 'Log in')]");
		assertTrue(elementos.size() == 1);
	}

	/**
	 * PR21 Intentar acceder sin estar autenticado a la opciÛn de listado de
	 * invitaciones de amistad recibida de un usuario est·ndar. Se deber· volver al
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
	 * amigos de otro usuario. Se deber· mostrar un mensaje de acciÛn indebida
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
	 * PR23
	 * Inicio de sesiÛn con datos v·lidos.
	 * */
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
	 * PR24
	 * Inicio de sesiÛn con datos inv·lidos (usuario no existente en la aplicaciÛn).
	 * */
	@Test
	public void PR24() {
		// Vamos al login
		driver.navigate().to("https://localhost:8081/cliente.html");
		// Rellenamos el formulario bien
		PO_LoginView.fillForm(driver, "prueba@uniovi.es", "12222223456");
		PO_View.checkElement(driver, "text", "Usuario no encontrado");

	}

	/**
	 * PR25
	 * Acceder a la lista de amigos de un usuario, que al menos tenga tres amigos.
	 * */
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
	 * PR26
	 * Acceder a la lista de amigos de un usuario, y realizar un filtrado para encontrar a un amigo
	 * concreto, el nombre a buscar debe coincidir con el de un amigo.
	 * */
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
		
		//Ahora hacemos el filtrado por nombre, vamos a buscar a sdi
		PO_SearchView.fillForm(driver, "sdi");
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementos.size() == 1);
		
	}

	/**
	 * PR27
	 * Acceder a la lista de mensajes de un amigo ìchatî, la lista debe contener al menos tres
	 * mensajes.
	 * */
	@Test
	public void PR27() {
		assertTrue("PR27 sin hacer", false);
	}

	/**
	 * PR29
	 * Identificarse en la aplicaciÛn y enviar un mensaje a un amigo, validar que el mensaje enviado
	 * aparece en el chat. Identificarse despuÈs con el usuario que recibido el mensaje y validar que tiene un
	 * mensaje sin leer, entrar en el chat y comprobar que el mensaje pasa a tener el estado leÌdo.
	 * */
	@Test
	public void PR29() {
		assertTrue("PR29 sin hacer", false);
	}

	/**
	 * PR30
	 * Identificarse en la aplicaciÛn y enviar tres mensajes a un amigo, validar que los mensajes
	 * enviados aparecen en el chat. Identificarse despuÈs con el usuario que recibido el mensaje y validar que el
	 * n˙mero de mensajes sin leer aparece en la propia lista de amigos.
	 * */
	@Test
	public void PR30() {
		assertTrue("PR30 sin hacer", false);
	}

	// PR031. Sin hacer /
	@Test
	public void PR31() {
		assertTrue("PR31 sin hacer", false);
	}

}
