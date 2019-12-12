package com.kms.katalon.keyword.testingbot

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.keyword.CustomProfile
import com.kms.katalon.core.keyword.IActionProvider
import com.kms.katalon.core.keyword.IContext
import com.kms.katalon.core.keyword.IControlSelectionEventHandler
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.setting.BundleSettingStore
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.driver.WebUIDriverType
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

public class TestingBotButtonSelectionEventHandler implements IControlSelectionEventHandler {

	void handle(IActionProvider actionProvider, Map<String, Object> dataFields, IContext context) {
		CustomProfile profile = new CustomProfile();
		def customCapabilities = [:];
		customCapabilities['key'] = dataFields.getOrDefault("key", "");
		customCapabilities['secret'] = dataFields.getOrDefault("secret", "");
		customCapabilities['browserName'] = dataFields.getOrDefault("browserName", "");
		customCapabilities['platform'] = dataFields.getOrDefault("platform", "");
		customCapabilities['version'] = dataFields.getOrDefault("version", "");
		customCapabilities['name'] = dataFields.getOrDefault("name", "");
		customCapabilities['remoteWebDriverType'] = 'Selenium';
		customCapabilities['remoteWebDriverUrl'] = 'https://hub.testingbot.com/wd/hub';

		profile.setName(TestingBotUtils.TESTINGBOT_RUN_CONFIG_NAME + "default");
		profile.setDriverType("Remote");
		profile.setDesiredCapabilities(customCapabilities);

		actionProvider.saveCustomProfile(profile);
	}
}
