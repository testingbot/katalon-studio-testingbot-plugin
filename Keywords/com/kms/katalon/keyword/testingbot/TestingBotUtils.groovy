package com.kms.katalon.keyword.testingbot

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import org.json.JSONArray
import org.json.JSONObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.setting.BundleSettingStore
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.testobject.UrlEncodedBodyParameter
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.TestObjectProperty
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import groovy.json.JsonOutput

import com.kms.katalon.core.testobject.RestRequestObjectBuilder

import internal.GlobalVariable

public class TestingBotUtils {
	public static String TESTINGBOT_RUN_CONFIG_NAME = "testingbot_";
	private static KeywordLogger logger = KeywordLogger.getInstance(TestingBotUtils.class);

	public static JSONArray getAllJobs() {
		String projectLocation = RunConfiguration.getProjectDir();
		BundleSettingStore store = new BundleSettingStore(projectLocation, "com.kms.katalon.keyword.Testingbot-keywords", true);
		String key = store.getString("key", "");
		String secret = store.getString("secret", "");
		String auth = "${key}:${secret}";
		String endpoint = "https://api.testingbot.com/v1/tests";

		String requestMethod = "GET";
		byte[] authEncBytes = Base64.getEncoder().encode(auth.getBytes());
		String authStringEnc = new String(authEncBytes);

		TestObjectProperty header1 = new TestObjectProperty("Authorization", ConditionType.EQUALS, "Basic " + authStringEnc);
		TestObjectProperty header2 = new TestObjectProperty("Accept", ConditionType.EQUALS, "application/json");
		ArrayList defaultHeaders = Arrays.asList(header1, header2);
		
		def builder = new RestRequestObjectBuilder();
		def requestObject = builder
				.withRestRequestMethod(requestMethod)
				.withRestUrl(endpoint)
				.withHttpHeaders(defaultHeaders)
				.build();

		ResponseObject respObj = WS.sendRequest(requestObject);
		JSONObject jsonObj = new JSONObject(respObj.getResponseText());
		return jsonObj.getJSONArray("data");
	}

	public static String getLatestJobId() {
		JSONArray jsonArray = TestingBotUtils.getAllJobs();
		return jsonArray.getJSONObject(0).getString("session_id");
	}

	public static String updateJob(String jobId, Map argsMap) {
		String projectLocation = RunConfiguration.getProjectDir();
		BundleSettingStore store = new BundleSettingStore(projectLocation, "com.kms.katalon.keyword.Testingbot-keywords", true);
		String key = store.getString("key", "");
		String secret = store.getString("secret", "");

		String auth = "${key}:${secret}";
		String endpoint = "https://api.testingbot.com/v1/tests/" + jobId;
		String requestMethod = "PUT";
		byte[] authEncBytes = Base64.getEncoder().encode(auth.getBytes());
		String authStringEnc = new String(authEncBytes);
		TestObjectProperty header1 = new TestObjectProperty("Authorization", ConditionType.EQUALS, "Basic " + authStringEnc);
		TestObjectProperty header2 = new TestObjectProperty("Content-Type", ConditionType.EQUALS, "application/x-www-form-urlencoded");
		TestObjectProperty header3 = new TestObjectProperty("Accept", ConditionType.EQUALS, "application/json");
		ArrayList defaultHeaders = Arrays.asList(header1, header2, header3);

		List<UrlEncodedBodyParameter> params = new ArrayList<UrlEncodedBodyParameter>(2);
        for (Map.Entry<String, Object> entry : argsMap.entrySet()) {
            if (entry.getValue() != null) {
                params.add(new UrlEncodedBodyParameter("test[" + entry.getKey() + "]", entry.getValue().toString()));
            }
        }

		def builder = new RestRequestObjectBuilder();
		def requestObject = builder
				.withRestRequestMethod(requestMethod)
				.withRestUrl(endpoint)
				.withHttpHeaders(defaultHeaders)
				.withUrlEncodedBodyContent(params)
				.build();

		ResponseObject respObj = WS.sendRequest(requestObject);
		return respObj.getResponseText();
	}
}
