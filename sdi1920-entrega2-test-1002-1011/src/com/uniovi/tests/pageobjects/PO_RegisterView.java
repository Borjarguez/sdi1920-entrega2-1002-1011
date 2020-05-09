package com.uniovi.tests.pageobjects;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PO_RegisterView extends PO_NavView {	
	
	static public void fillForm(WebDriver driver, String emailp, String namep, String surnamep, String passwordp, String passwordCheckp) {
		WebElement email = driver.findElement(By.name("email"));
		email.click();
		email.clear();
		email.sendKeys(emailp);
		WebElement name = driver.findElement(By.name("name"));
		name.click();
		name.clear();
		name.sendKeys(namep);
		WebElement surname = driver.findElement(By.name("surname"));
		surname.click();
		surname.clear();
		surname.sendKeys(surnamep);
		WebElement password = driver.findElement(By.name("password"));
		password.click();
		password.clear();
		password.sendKeys(passwordp);
		WebElement passwordCheck = driver.findElement(By.name("passwordCheck"));
		passwordCheck.click();
		passwordCheck.clear();
		passwordCheck.sendKeys(passwordCheckp);
		//Pulsar el boton de Alta.
		By boton = By.className("btn");
		driver.findElement(boton).click();	
	}
	
}
