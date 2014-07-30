/*
 * Copyright (c) 2013, JoshuaTree Software. All rights reserved.
 */
package com.mycompany;

import java.lang.String;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.junit.*;
import static org.junit.Assert.*;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

/**
 * This class uses apache selenium firefox driver to drive commander web ui
 */
public class FortressDemo2SeleniumITCase
{
    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();
    private static final Logger LOG = Logger.getLogger( FortressDemo2SeleniumITCase.class.getName() );

    @Before
    public void setUp() throws Exception
    {
        FirefoxProfile ffProfile = new FirefoxProfile();
        ffProfile.setPreference( "browser.safebrowsing.malware.enabled", false );
        driver = new FirefoxDriver( ffProfile );
        driver.manage().window().maximize();

        // Use test local default:
        baseUrl = "http://localhost:8080";
        driver.manage().timeouts().implicitlyWait( 5, TimeUnit.SECONDS );
    }

    private void info(String msg)
    {
        ( ( JavascriptExecutor ) driver ).executeScript( "$(document.getElementById('infoField')).val('" + msg + "');" );
    }

    @Test
    public void testCase1() throws Exception
    {
        LOG.info( "Begin FortressDemo2SeleniumITCase" );
        driver.get( baseUrl + "/fortressdemo2" );
        login( GlobalUtils.USER_1, "password");

        TUtils.sleep( 1 );

        // User 1:
        doPositiveButtonTests( GlobalUtils.PAGE_1, GlobalUtils.USER_1, GlobalUtils.BTN_PAGE_1 );
        doNegativeButtonTests( GlobalUtils.PAGE_2, GlobalUtils.USER_1, GlobalUtils.BTN_PAGE_2 );
        doNegativeButtonTests( GlobalUtils.PAGE_3, GlobalUtils.USER_1, GlobalUtils.BTN_PAGE_3 );
        logout( GlobalUtils.USER_1 );

        // User 2:
        login( GlobalUtils.USER_2, "password" );
        TUtils.sleep( 1 );
        doPositiveButtonTests( GlobalUtils.PAGE_2, GlobalUtils.USER_2, GlobalUtils.BTN_PAGE_2 );
        doNegativeLinkTest( GlobalUtils.PAGE_1, GlobalUtils.USER_2 );
        doNegativeLinkTest( GlobalUtils.PAGE_3, GlobalUtils.USER_2 );
        logout( GlobalUtils.USER_2 );

        // User 3:
        login( GlobalUtils.USER_3, "password" );
        TUtils.sleep( 1 );
        doPositiveButtonTests( GlobalUtils.PAGE_3, GlobalUtils.USER_3, GlobalUtils.BTN_PAGE_3 );
        doNegativeLinkTest( GlobalUtils.PAGE_1, GlobalUtils.USER_3 );
        doNegativeLinkTest( GlobalUtils.PAGE_2, GlobalUtils.USER_3 );
        logout( GlobalUtils.USER_3 );

        // Back to User1 for Activation tests:
        login( GlobalUtils.USER_1, "password");
        TUtils.sleep( 1 );
        doActivateTest( );
        logout( GlobalUtils.USER_1 );
    }

    private void doActivateTest( )
    {
        info("Do Role Activation Test");
        // Go to Page 2
        driver.findElement( By.linkText( GlobalUtils.PAGE_2 ) ).click();
        doNegativeButtonTest( GlobalUtils.USER_1, GlobalUtils.BTN_PAGE_2, GlobalUtils.BUTTON1 );

        // Now activate ROLE_TEST2:
        activateRole(GlobalUtils.ROLE_TEST2);
        //TUtils.sleep( 1 );
        // Make sure the pop-up is correct:
        if(!processPopup("Role selection ROLE_TEST2 activation failed because of Dynamic SoD rule violation"))
            fail("doActivate Button Test 2 Failed: " + GlobalUtils.BTN_PAGE_2 + "." + GlobalUtils.BUTTON1);
        doNegativeButtonTest( GlobalUtils.USER_1, GlobalUtils.BTN_PAGE_2, GlobalUtils.BUTTON1 );

        // Go back to Page 1
        driver.findElement( By.linkText( GlobalUtils.PAGE_1 ) ).click();

        // Now deactivate ROLE_TEST1:
        ( ( JavascriptExecutor ) driver ).executeScript( "$(document.getElementById('" + GlobalUtils.ACTIVE_ROLES + "')).val('" + GlobalUtils.ROLE_TEST1 + "');" );
        driver.findElement( By.name( GlobalUtils.ROLES_DEACTIVATE ) ).click();
        TUtils.sleep( 1 );
        doNegativeButtonTest( GlobalUtils.USER_1, GlobalUtils.BTN_PAGE_1, GlobalUtils.BUTTON1 );

        // Now go back to Page 2 again
        driver.findElement( By.linkText( GlobalUtils.PAGE_2 ) ).click();
        doNegativeButtonTest( GlobalUtils.USER_1, GlobalUtils.BTN_PAGE_2, GlobalUtils.BUTTON1 );

        // Now active ROLE_TEST2:
        activateRole(GlobalUtils.ROLE_TEST2);

        // Click the buttons on page 2
        doPositiveButtonTests( null, GlobalUtils.USER_1, GlobalUtils.BTN_PAGE_2 );

        // Now go to Page 3 and do negative tests on buttons:
        doNegativeButtonTests( GlobalUtils.PAGE_3, GlobalUtils.USER_1, GlobalUtils.BTN_PAGE_3 );

        // Now activate ROLE_TEST3:
        activateRole(GlobalUtils.ROLE_TEST3);
        TUtils.sleep( 3 );
        // Make sure the pop-up is correct:
        if(!processPopup("Role selection ROLE_TEST3 activation failed because of Dynamic SoD rule violation"))
            fail("doActivate Button Test 3 Failed: " + GlobalUtils.BTN_PAGE_2 + "." + GlobalUtils.BUTTON1);
        doNegativeButtonTest( GlobalUtils.USER_1, GlobalUtils.BTN_PAGE_3, GlobalUtils.BUTTON1 );

        // Go back to Page 2
        driver.findElement( By.linkText( GlobalUtils.PAGE_2 ) ).click();

        // Now deactivate ROLE_TEST2:
        ( ( JavascriptExecutor ) driver ).executeScript( "$(document.getElementById('" + GlobalUtils.ACTIVE_ROLES + "')).val('" + GlobalUtils.ROLE_TEST2 + "');" );
        driver.findElement( By.name( GlobalUtils.ROLES_DEACTIVATE ) ).click();
        TUtils.sleep( 1 );
        doNegativeButtonTest( GlobalUtils.USER_1, GlobalUtils.BTN_PAGE_2, GlobalUtils.BUTTON1 );

        // Now go back to Page 3 again
        driver.findElement( By.linkText( GlobalUtils.PAGE_3 ) ).click();
        doNegativeButtonTest( GlobalUtils.USER_1, GlobalUtils.BTN_PAGE_2, GlobalUtils.BUTTON1 );

        // Now active ROLE_TEST3:
        activateRole(GlobalUtils.ROLE_TEST3);
        //TUtils.sleep( 3 );

        // Now click the buttons on page 3:
        doPositiveButtonTests( null, GlobalUtils.USER_1, GlobalUtils.BTN_PAGE_3 );
    }

    private void doPositiveButtonTests( String linkName, String userId, String pageId )
    {
        if(linkName != null)
            driver.findElement( By.linkText( linkName ) ).click();
        // Click the buttons on the page
        doPositiveButtonTest(pageId, GlobalUtils.BUTTON1, pageId + "." + GlobalUtils.BUTTON1);
        doPositiveButtonTest(pageId, GlobalUtils.BUTTON2, pageId + "." + GlobalUtils.BUTTON2);
        doPositiveButtonTest(pageId, GlobalUtils.BUTTON3, pageId + "." + GlobalUtils.BUTTON3);
    }

    private void doNegativeButtonTests( String linkName, String userId, String pageId )
    {
        if(linkName != null)
            driver.findElement( By.linkText( linkName ) ).click();
        doNegativeButtonTest( userId, pageId, GlobalUtils.BUTTON1 );
/*
        doNegativeButtonTest( userId, pageId, GlobalUtils.BUTTON2 );
        doNegativeButtonTest( userId, pageId, GlobalUtils.BUTTON3 );
*/
    }

    private boolean processPopup(String text)
    {
        boolean textFound = false;
        try
        {
            Alert alert = driver.switchTo ().alert ();
            //alert is present
            LOG.info( "Button Pressed:" + alert.getText() );
            if(alert.getText().equals( text ))
                textFound = true;

            alert.accept();
        }
        catch ( NoAlertPresentException n)
        {
            //Alert isn't present
        }
        return textFound;
    }

    private void activateRole(String roleName)
    {
        info("Active test for " + roleName);
        ( ( JavascriptExecutor ) driver ).executeScript( "$(document.getElementById('" + GlobalUtils.INACTIVE_ROLES + "')).val('" + roleName + "');" );
        driver.findElement( By.name( GlobalUtils.ROLES_ACTIVATE ) ).click();
    }

    private void doPositiveButtonTest(String pageId, String buttonId, String alertText)
    {
        info("Positive button test for " + pageId + ", " + buttonId);
        driver.findElement( By.name( pageId + "." + buttonId ) ).click();
        TUtils.sleep( 1 );
        if(!processPopup(alertText))
            fail("Button Test Failed: " + pageId + "." + buttonId);
    }

    private void doNegativeButtonTest( String userId, String pageId, String buttonId )
    {
        info("Negative button test for " + pageId + ", " + buttonId + ", and " + userId);
        try
        {
            driver.findElement( By.name( pageId + "." + buttonId ) ).click();
            fail("Negative Button Test Failed: " + pageId + "." + GlobalUtils.BUTTON1);
        }
        catch (org.openqa.selenium.NoSuchElementException e)
        {
            // pass
        }
    }

    private void doNegativeLinkTest( String linkName, String userId  )
    {
        info("Negative link:" + linkName + " test for " + userId);
        try
        {
            if(driver.findElement( By.linkText( linkName ) ).isEnabled())
            {
                fail("Negative Link Test Failed UserId: " + userId + " Link: " + linkName);
            }
            fail("Negative Button Test Failed UserId: " + userId + " Link: " + linkName);
        }
        catch (org.openqa.selenium.NoSuchElementException e)
        {
            // pass
        }
    }

    private void login(String userId, String password)
    {
        driver.findElement( By.id( GlobalUtils.USER_ID ) ).clear();
        driver.findElement( By.id( GlobalUtils.USER_ID ) ).sendKeys( userId );
        driver.findElement( By.id( GlobalUtils.PSWD_FIELD ) ).clear();
        driver.findElement( By.id( GlobalUtils.PSWD_FIELD ) ).sendKeys( password );
        driver.findElement( By.name( GlobalUtils.LOGIN ) ).click();
        LOG.info( "User: " + userId + " has logged ON" );
        info("Login User: " + userId);
    }

    private void logout(String userId)
    {
        info("Logout " + userId);
        driver.findElement( By.linkText( "LOGOUT" ) ).click();
        LOG.info( "User: " + userId + " has logged OFF" );
    }

    private void nextPage(WebElement table, String szTableName)
    {
        table = driver.findElement(By.id( szTableName));
        List<WebElement> allRows = table.findElements(By.tagName("a"));
        for (WebElement row : allRows)
        {
            String szText = row.getText();
            if(szText.equals( "Go to the next page" ))
                row.click();
            LOG.debug( "row text=" + row.getText() );
        }
    }

    @After
    public void tearDown() throws Exception
    {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if ( !"".equals( verificationErrorString ) )
        {
            fail( verificationErrorString );
        }
    }

    private boolean isElementPresent( By by )
    {
        try
        {
            driver.findElement( by );
            return true;
        }
        catch ( NoSuchElementException e )
        {
            return false;
        }
    }

    private boolean isAlertPresent()
    {
        try
        {
            driver.switchTo().alert();
            return true;
        }
        catch ( NoAlertPresentException e )
        {
            return false;
        }
    }

    private String closeAlertAndGetItsText()
    {
        try
        {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if ( acceptNextAlert )
            {
                alert.accept();
            }
            else
            {
                alert.dismiss();
            }
            return alertText;
        }
        finally
        {
            acceptNextAlert = true;
        }
    }
}
