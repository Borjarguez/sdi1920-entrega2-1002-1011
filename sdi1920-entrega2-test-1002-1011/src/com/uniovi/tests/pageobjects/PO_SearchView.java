package com.uniovi.tests.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PO_SearchView extends PO_View {

	public static void fillForm(WebDriver driver, String searchp) {
		WebElement search = driver.findElement(By.name("searchText"));
		search.click();
		search.clear();
		search.sendKeys(searchp);
		
	}

}
