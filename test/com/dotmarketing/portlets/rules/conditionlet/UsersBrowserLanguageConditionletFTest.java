package com.dotmarketing.portlets.rules.conditionlet;

import com.dotcms.repackage.com.google.common.collect.Lists;
import com.dotcms.repackage.org.junit.After;
import com.dotcms.repackage.org.junit.Before;
import com.dotcms.repackage.org.junit.Test;
import com.dotmarketing.portlets.rules.RuleDataGen;
import com.dotmarketing.portlets.rules.model.Condition;
import com.dotmarketing.portlets.rules.model.Rule;
import com.dotmarketing.servlets.test.ServletTestRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.dotcms.repackage.org.junit.Assert.assertEquals;
import static com.dotcms.repackage.org.junit.Assert.assertNull;
import static com.dotmarketing.portlets.rules.parameter.comparison.Comparison.IS;
import static com.dotmarketing.portlets.rules.parameter.comparison.Comparison.IS_NOT;

/**
 * @author Jonathan Gamba
 *         Date: 1/12/16
 */
public class UsersBrowserLanguageConditionletFTest {

    private Random random = new Random();
    private HttpServletRequest request;
    private ConditionDataGen conditionDataGen = new ConditionDataGen();
    private ConditionletTestUtil conditionletTestUtil = new ConditionletTestUtil();

    @Before
    public void init () {
        request = ServletTestRunner.localRequest.get();
        HttpSession session = request.getSession(false);
        if ( session != null ) {
            session.invalidate();
        }
    }

    @After
    public void tearDown () throws Exception {
        conditionletTestUtil.clear();
    }

    @Test
    public void testIsComparison () throws IOException {

        String randomKey = "test-" + random.nextInt();
        String value = randomKey + "-value";

        //Creating the Conditionlet for the Browser language
        Condition condition = conditionDataGen.next();
        condition.setConditionletId(UsersBrowserLanguageConditionlet.class.getSimpleName());
        condition.setName("Visitor's Browser Language");
        condition.addValue(Conditionlet.COMPARISON_KEY, IS.getId());
        condition.addValue(UsersBrowserLanguageConditionlet.LANGUAGE_INPUT_KEY, "en");

        //Persist the Conditionlet
        conditionletTestUtil.createRandomSetResponseHeaderRule(condition, randomKey, value);

        //Execute some requests and validate the responses
        ApiRequest apiRequest = new ApiRequest(request, UsersBrowserLanguageConditionlet.BROWSER_LANGUAGE_HEADER);

        URLConnection conn = apiRequest.makeRequest("about-us/index", "nso,xh;q=0.8,es-CR;q=0.5,es;q=0.3");
        assertNull("Specified response header should be NOT present in the Response.", conn.getHeaderField(randomKey));
        conn = apiRequest.makeRequest("products/", "nso,xh;q=0.8,en-US;q=0.5,en;q=0.3");
        assertEquals("Specified response header should be present in the Response.", value, conn.getHeaderField(randomKey));
        conn = apiRequest.makeRequest("contact-us/", "nso,xh;q=0.8,en-US;q=0.5,en;q=0.3");
        assertEquals("Specified response header should be present in the Response.", value, conn.getHeaderField(randomKey));
    }

    @Test
    public void testIsNotComparison () throws IOException {

        String randomKey = "test-" + random.nextInt();
        String value = randomKey + "-value";

        //Creating the Conditionlet for the Browser language
        Condition condition = conditionDataGen.next();
        condition.setConditionletId(UsersBrowserLanguageConditionlet.class.getSimpleName());
        condition.setName("Visitor's Browser Language");
        condition.addValue(Conditionlet.COMPARISON_KEY, IS_NOT.getId());
        condition.addValue(UsersBrowserLanguageConditionlet.LANGUAGE_INPUT_KEY, "en");

        //Persist the Conditionlet
        conditionletTestUtil.createRandomSetResponseHeaderRule(condition, randomKey, value);

        //Execute some requests and validate the responses
        ApiRequest apiRequest = new ApiRequest(request);

        URLConnection conn = apiRequest.makeRequest("about-us/index", "nso,xh;q=0.8,es-CR;q=0.5,es;q=0.3");
        assertEquals("Specified response header should be present in the Response.", value, conn.getHeaderField(randomKey));
        conn = apiRequest.makeRequest("products/", "nso,xh;q=0.8,en-US;q=0.5,en;q=0.3");
        assertNull("Specified response header should be NOT present in the Response.", conn.getHeaderField(randomKey));
        conn = apiRequest.makeRequest("contact-us/", "nso,xh;q=0.8,en-US;q=0.5,en;q=0.3");
        assertNull("Specified response header should be NOT present in the Response.", conn.getHeaderField(randomKey));
    }



}