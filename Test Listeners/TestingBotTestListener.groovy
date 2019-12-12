import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import org.json.JSONArray
import org.json.JSONObject

import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.setting.BundleSettingStore
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.keyword.testingbot.TestingBotUtils
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile

import internal.GlobalVariable as GlobalVariable

import com.kms.katalon.core.annotation.BeforeTestCase
import com.kms.katalon.core.annotation.BeforeTestSuite
import com.kms.katalon.core.annotation.AfterTestCase
import com.kms.katalon.core.annotation.AfterTestSuite
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.context.TestSuiteContext
import com.kms.katalon.core.logging.KeywordLogger

public class TestingBotTestListener {

	@BeforeTestCase
	def enableTestingBot(TestCaseContext testCaseContext) {
		String runConfigName = (String) RunConfiguration.getProperty("Name")
		KeywordUtil.logInfo("[TESTINGBOT] Current run configuration: " + runConfigName)
		if(runConfigName.toLowerCase().startsWith(TestingBotUtils.TESTINGBOT_RUN_CONFIG_NAME)){ 
			KeywordUtil.logInfo("[TESTINGBOT] TestingBot Plugin will auto update test status and information!")
		} else {
			KeywordUtil.logInfo("[TESTINGBOT] TestingBot Plugin will not auto update test status and information!");
		}
	}

	@AfterTestCase
	def autoUpdateJobStatus(TestCaseContext testCaseContext) {
		String runConfigName = (String) RunConfiguration.getProperty("Name");
		KeywordUtil.logInfo("[TESTINGBOT] Current run configuration: " + runConfigName)
		if(runConfigName.toLowerCase().startsWith(TestingBotUtils.TESTINGBOT_RUN_CONFIG_NAME)){
			KeywordUtil.logInfo("[TESTINGBOT] Auto updating test status and information ...")
			String latestJobId = TestingBotUtils.getLatestJobId()
			String status = testCaseContext.getTestCaseStatus()
			TestingBotUtils.updateJob(latestJobId,
					, ["name":testCaseContext.getTestCaseId(), "success": status.equals("PASSED") ? true: false])
		}
	}
}