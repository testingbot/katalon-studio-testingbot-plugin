# katalon-studio-testingbot-plugin

Katalon Studio TestingBot Plugin is a Custom Keyword Plugin that provides integration with TestingBot. Compatible with Katalon Studio from 6.2.0 onward.

## Katalon Studio Report Plugin provides:
- Automatically generate Desired Capabilities which allow the remote driver to connect to TestingBot.
- Automatically update TestingBot Tests with the correct name and status (pass/failed).

## Limitation:
- Katalon Studio TestingBot Plugin is for Test Case only, not Test Suite or Test Suite Collection.

## Build project
1. Run:
```sh
./gradlew clean katalonPluginPackage
```
2. Copy *build/libs/katalon-studio-report-plugin.jar* and paste into Plugins folder of your Katalon Studio project to start using the keyword or upload to Katalon Store

## For developers

The essential components and logic:

* **Keywords/katalon-plugin.json**: It lets you define the UI of a setting page for your own plug-in. This setting page will be available under Project > Settings > Plugins. You can define text inputs, checkboxes and buttons. For a button, you can define what it does when clicked by specifying an implementation class path.

* **Test Listeners/TestingBotTestListener.groovy**: A test listener before/after a test case that retrieves the current running configuration's name. You can use the name to condition your logic. In this case the after test case listener will update the tests's information with test case name and status if the current running configuration's name contains a *testingbot_ prefix*.

* **Keywords/com/kms/katalon/keyword/testingbot/TestingBotButtonSelectionEventHandler.groovy**: The implementation class for a button that contains the logic of what will occur when the button is clicked. 

    * The implementation class for a button must implement the interface *IControlSelectionEventHandler* which has a *handle* method. 

    * In the *handle* method, plug-in can retrieve information that were input in the corresponding plug-in setting page through the second argument. The first argument *IActionProvider* is an interface that provides some operations that can be applied on the information retrieved. In this case a CustomProfile is created using the information retrieved in the setting page and is saved using method *saveCustomProfile*. The third argument is an interface *IContext* that can be used to retrieve additional informatiom.
    
    * After *saveCustomProfile* is called, the custom profile is saved and is available under Custom Capabilities when executing a test case.
