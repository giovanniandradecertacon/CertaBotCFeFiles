package br.com.certacon.certabotcfefiles.pages;

import br.com.certacon.certabotcfefiles.helpers.SeleniumHelperComponent;
import br.com.certacon.certabotcfefiles.utils.ProcessStatus;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.Optional;

@Service
public class CertaconHomePage {
    private final SeleniumHelperComponent helper;

    public CertaconHomePage(SeleniumHelperComponent helper) {
        this.helper = helper;
    }

    public ProcessStatus closeAndNavigate(RemoteWebDriver remoteWebDriver) throws MalformedURLException {
        ProcessStatus homePageStatus = null;

        try {
            Optional<WebElement> tutorialElement = helper.findElementByXpathAndSendKeys(1000L, 30L, "/html/body/div[3]/div/div/button", Keys.ESCAPE, remoteWebDriver);
            if (tutorialElement.isPresent()) {
                Optional<WebElement> frameElement = helper.switchToFrameByClassName(1000L, 30L, "geo-smart-iframe", remoteWebDriver);
                if (frameElement.isPresent()) {
                    Optional<WebElement> filterElement = helper.findElementNotButtonByXpathAndClick(1000L, 30L, "/html/body/div[17]/div[1]/div/div/div/div/div[2]/div[4]/div/div", remoteWebDriver);
                    if (filterElement.isPresent()) {
                        Optional<WebElement> efdElement = helper.findElementNotButtonByXpathAndClick(1000L, 30L, "//*[@id=\"geoMenu_wrap\"]/li[2]/a", remoteWebDriver);
                        if (efdElement.isPresent()) {
                            Optional<WebElement> uploadFilesElement = helper.findElementNotButtonByXpathAndClick(1000L, 30L, "/html/body/div[6]/div[2]/div/div/div/div/div/div[2]/div/div/div/div[1]/div/div[2]/div[1]/div/div/div/div/div/div/div/div/div[2]/div/div/div/ul/li/a", remoteWebDriver);
                            if (uploadFilesElement.isPresent()) {
                                homePageStatus = ProcessStatus.CHANGED;
                            }
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            homePageStatus = ProcessStatus.ERROR;
            throw new RuntimeException();
        } finally {
            return homePageStatus;
        }
    }
}
