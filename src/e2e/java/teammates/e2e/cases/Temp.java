package teammates.e2e.cases;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.CSVDataSet;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.OnceOnlyController;
import org.apache.jmeter.control.gui.LoopControlPanel;
import org.apache.jmeter.control.gui.OnceOnlyControllerGui;
import org.apache.jmeter.control.gui.TestPlanGui;
import org.apache.jmeter.protocol.http.config.gui.HttpDefaultsGui;
import org.apache.jmeter.protocol.http.control.CookieManager;
import org.apache.jmeter.protocol.http.control.gui.HttpTestSampleGui;
import org.apache.jmeter.protocol.http.gui.CookiePanel;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testbeans.gui.TestBeanGUI;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jmeter.testelement.property.TestElementProperty;
import org.apache.jmeter.threads.SetupThreadGroup;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.threads.gui.ThreadGroupGui;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.collections.ListedHashTree;

/**
 * temp.
 */
public class Temp {

    /**
     * Student Profile test plan.
     */
    protected HashTree getTestPlan(String jmxFile) throws IOException {
        // Note: These three properties are only for generating a .jmx from the HashTree (first 2 are a must)
        // 1. element.setProperty(TestElement.TEST_CLASS, Element.class.getName());
        // 2. element.setProperty(TestElement.GUI_CLASS, ElementGui.clas.getName());
        // 3. element.setName("Element title displayed in GUI")

        // TestPlan
        TestPlan testPlan = new TestPlan();
        testPlan.setName("Student Profile Test Plan");
        testPlan.setEnabled(true);
        testPlan.setUserDefinedVariables(new Arguments());
        testPlan.setProperty(TestElement.TEST_CLASS, TestPlan.class.getName());
        testPlan.setProperty(TestElement.GUI_CLASS, TestPlanGui.class.getName());

        // Thread Group controller
        LoopController loopController = new LoopController();
        loopController.setEnabled(true);
        loopController.setLoops(1);
        loopController.setProperty(TestElement.TEST_CLASS, LoopController.class.getName());
        loopController.setProperty(TestElement.GUI_CLASS, LoopControlPanel.class.getName());

        // Thread Group
        ThreadGroup threadGroup = new SetupThreadGroup();
        threadGroup.setName("Thread Group");
        threadGroup.setNumThreads(100);
        threadGroup.setRampUp(2);
        threadGroup.setProperty(new StringProperty(ThreadGroup.ON_SAMPLE_ERROR, ThreadGroup.ON_SAMPLE_ERROR_CONTINUE));
        threadGroup.setSamplerController(loopController);
        threadGroup.setProperty(TestElement.TEST_CLASS, ThreadGroup.class.getName());
        threadGroup.setProperty(TestElement.GUI_CLASS, ThreadGroupGui.class.getName());

        // CSVConfig
        CSVDataSet csvDataSet = new CSVDataSet();
        csvDataSet.setName("CSV Data Config: User Details");
        csvDataSet.setProperty(new StringProperty("filename", "src/e2e/resources/data/studentProfileConfig.csv"));
        csvDataSet.setProperty(new StringProperty("delimiter", "|"));
        csvDataSet.setProperty(new StringProperty("shareMode", "shareMode.all"));
        csvDataSet.setProperty("ignoreFirstLine", true);
        csvDataSet.setProperty("quoted", true);
        csvDataSet.setProperty("recycle", true);
        csvDataSet.setProperty("stopThread", false);
        csvDataSet.setProperty(TestElement.TEST_CLASS, CSVDataSet.class.getName());
        csvDataSet.setProperty(TestElement.GUI_CLASS, TestBeanGUI.class.getName());

        // CookieManager
        CookieManager cookieManager = new CookieManager();
        cookieManager.setName("HTTP Cookie Manager");
        cookieManager.setClearEachIteration(false);
        cookieManager.setCookiePolicy("standard");
        cookieManager.setProperty(TestElement.TEST_CLASS, CookieManager.class.getName());
        cookieManager.setProperty(TestElement.GUI_CLASS, CookiePanel.class.getName());

        // HTTP Default Sampler
        ConfigTestElement defaultSampler = new ConfigTestElement();
        defaultSampler.setEnabled(true);
        defaultSampler.setProperty(new TestElementProperty(HTTPSampler.ARGUMENTS, new Arguments()));
        defaultSampler.setProperty(HTTPSampler.DOMAIN, "localhost");
        defaultSampler.setProperty(HTTPSampler.PORT, "8080");
        defaultSampler.setName("HTTP Request Defaults");
        defaultSampler.setProperty(TestElement.TEST_CLASS, ConfigTestElement.class.getName());
        defaultSampler.setProperty(TestElement.GUI_CLASS, HttpDefaultsGui.class.getName());

        // Login HTTP Request
        HTTPSamplerProxy loginSampler = new HTTPSamplerProxy();
        loginSampler.setName("Login");
        loginSampler.setPath("_ah/login?action=Log+In&email=${email}&isAdmin=${isAdmin}&continue=http://localhost:8080/webapi/auth?frontendUrl=http://localhost:4200");
        loginSampler.setMethod("POST");
        loginSampler.setFollowRedirects(true);
        loginSampler.setUseKeepAlive(true);
        loginSampler.setProperty(TestElement.TEST_CLASS, HTTPSamplerProxy.class.getName());
        loginSampler.setProperty(TestElement.GUI_CLASS, HttpTestSampleGui.class.getName());

        // Controller for Login request
        OnceOnlyController onceOnlyController = new OnceOnlyController();
        onceOnlyController.setName("Only once controller");
        onceOnlyController.setProperty(TestElement.TEST_CLASS, OnceOnlyController.class.getName());
        onceOnlyController.setProperty(TestElement.GUI_CLASS, OnceOnlyControllerGui.class.getName());

        // Student Profile HTTP Request
        HTTPSamplerProxy studentProfileSampler = new HTTPSamplerProxy();
        studentProfileSampler.setName("Student Profile");
        studentProfileSampler.setPath("webapi/student/profile?googleid=${googleid}");
        studentProfileSampler.setMethod("GET");
        studentProfileSampler.addArgument("googleid", "${googleid}");
        studentProfileSampler.setEnabled(true);
        studentProfileSampler.setProperty(TestElement.TEST_CLASS, HTTPSamplerProxy.class.getName());
        studentProfileSampler.setProperty(TestElement.GUI_CLASS, HttpTestSampleGui.class.getName());

        // Create Test plan
        HashTree testPlanHashTree = new ListedHashTree();
        HashTree threadGroupHashTree = testPlanHashTree.add(testPlan, threadGroup);
        threadGroupHashTree.add(csvDataSet);
        threadGroupHashTree.add(cookieManager);
        threadGroupHashTree.add(defaultSampler);
        threadGroupHashTree.add(onceOnlyController, loginSampler);
        threadGroupHashTree.add(studentProfileSampler);

        // Save to jmx file
        SaveService.saveTree(testPlanHashTree, new FileOutputStream("studentProfileGenerated.jmx"));

        return testPlanHashTree;
    }

}
