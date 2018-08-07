package io.github.crawlerbot.service;

import com.google.common.base.Function;
import io.github.crawlerbot.enumerations.Action;
import io.github.crawlerbot.enumerations.BrowserOS;
import io.github.crawlerbot.enumerations.SeleniumActionGetContent;
import io.github.crawlerbot.exceptions.NotSupportBrowserException;
import io.github.crawlerbot.models.*;
import org.joda.time.DateTimeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;



public class Crawler implements CrawlerEngine {

    private static final Logger logger = LoggerFactory.getLogger(Crawler.class);
    private CrawlLine input;
    private Link link;
    private Channel channel;
    private BrowserName browserName;
    private BrowserHost browserHost;
    private BrowserOS browserOS;
    private CrawlerResult crawlerResult;

    private CommandLiner commandLiner;

    public Crawler(Channel channel, CommandLiner commandLiner) {
        this.channel = channel;

        Link link = new Link();
        link.setUrl(channel.getUrl());
        link.setCurrentLevel(0);

        CrawlLine crawlLine = new CrawlLine();
        crawlLine.setChannel(channel);
        crawlLine.setLink(link);

        this.link = link;
        this.input = crawlLine;
        this.commandLiner = commandLiner;

        logger.info("[] config crawler: {}", this.toString());

    }

    public Crawler(CrawlLine crawlLine, CommandLiner commandLiner) {
        this.input = crawlLine;
        this.channel = crawlLine.getChannel();
        this.link = crawlLine.getLink();
        this.commandLiner = commandLiner;
        logger.info("[] config crawler: {}", this.toString());

    }

    public Crawler config(BrowserName browserName, BrowserHost browserHost, BrowserOS browserOS) throws NotSupportBrowserException, MalformedURLException {
        this.browserName = browserName;
        this.browserHost = browserHost;
        this.browserOS = browserOS;
        WebDriver browser;
        Capabilities chromeCapabilities;
        if (browserName.equals(BrowserName.CHROME)) {
            chromeCapabilities = DesiredCapabilities.chrome();
        } else if (browserName.equals(BrowserName.FIREFOX)) {
            chromeCapabilities = DesiredCapabilities.firefox();
        } else {
            throw new NotSupportBrowserException("The input browser is not valid, please input chrome or firefox!");
        }

        if (browserHost == null) browserHost = BrowserHost.LOCAL;
        if (browserHost.equals(BrowserHost.LOCAL)) {
            String os = "mac";
            if (browserOS.equals(BrowserOS.UBUNTU)) {
                os = "linux";
            }
            if (browserOS.equals(BrowserOS.WINDOWS)) {
                os = "window";
            }
            if (browserName.equals(BrowserName.CHROME)) {
                System.setProperty("webdriver.chrome.driver", "selenium/" + os + "/chromedriver");
                browser = new ChromeDriver();
            } else {
                System.setProperty("webdriver.firefox.driver", "selenium/" + os + "/gekodriver");
                browser = new FirefoxDriver();
            }
        } else {
            browser = new RemoteWebDriver(new URL(CrawlerConfiguration.REMOTE_BROWSER_URL), chromeCapabilities);
        }
        this.browser = browser;

        logger.info("[] config browser: {}", this.toString());
        return this;
    }

    public CrawlerResult getCrawlerResult() {
        return crawlerResult;
    }

    public void setCrawlerResult(CrawlerResult crawlerResult) {
        this.crawlerResult = crawlerResult;
    }

    public CrawlLine getCrawline() {
        return input;
    }

    public void setCrawline(CrawlLine input) {
        this.input = input;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }


    @Override
    public String toString() {
        return "Crawler{" +
            "input=" + input +
            ", link=" + link +
            ", channel=" + channel +
            ", browserName=" + browserName +
            ", browserHost=" + browserHost +
            ", crawlerResult=" + crawlerResult +
            ", commandLiner=" + commandLiner +
            ", browser=" + browser +
            '}';
    }

    public class CrawlerDocument {
        private Document document;

        public CrawlerDocument(Document document) {
            this.document = document;
        }

        public Document getDocument() {
            return document;
        }

        public void setDocument(Document document) {
            this.document = document;
        }
    }

    public enum BrowserName {
        CHROME, FIREFOX
    }

    public enum BrowserHost {
        REMOTE, LOCAL
    }

    private WebDriver browser;

    private boolean quitBroweser() {
        if (this.browser == null) return true;
        if (this.browser != null) {
            this.browser.quit();
            return true;
        }
        return true;
    }

    public Crawler() {

    }

    private List<SiteAction> getSiteActions(Channel channel, Integer level) {
        if (channel == null || level == null) return null;
        Set<SiteAction> actions = channel.getActions();
        if (actions == null) return null;
        List<SiteAction> result = new ArrayList<>();
        for (SiteAction siteAction : actions) {
            if (siteAction.getLevel() == level) {
                result.add(siteAction);
            }
        }
        return result;
    }

    private Crawler bindingFetchResult(String url) {
        String currentUrl = this.browser.getCurrentUrl();
        String html = this.browser.getPageSource();
        Document document = Jsoup.parse(html);
        document.setBaseUri(currentUrl);

        CrawlerResult result = new CrawlerResult();
        result.setRequestUrl(url);
        result.setCurrentUrl(currentUrl);
        result.setDocument(document);
        this.setCrawlerResult(result);
        return this;

    }

    @Override
    public Crawler fetch() {
        CrawlLine crawlLine = this.getCrawline();
        String url = crawlLine.getLink().getUrl();
        Channel channel = crawlLine.getChannel();
        logger.info("[] fetch : {}", url);
        Integer currentLevel = crawlLine.getLink().getCurrentLevel();

        this.browser.get(url);
        sleep(1000L);
        bindingFetchResult(url);
        List<SiteAction> actions = getSiteActions(channel, currentLevel);

        logger.info("[] site actions at level: {}, {}", currentLevel, actions);

        if (actions == null || actions.size() == 0) {
            quitBroweser();
            this.parse(false).index().nextLinks().report();
            return this;
        }

        for (SiteAction siteAction : actions) {

            if (siteAction.getAction() != null && siteAction.getAction().equals(Action.SCROLL)) {
                // waiting until done action
                Integer totalActions = siteAction.getTotalActions();
                for (Integer from = 0; from < totalActions; from++) {
                    logger.info("[] do scroll at step: {}", from);
                    doScroll(url, totalActions);
                }
                bindingFetchResult(url);
                logger.info("[] call parse, then index, then next link, then report");
                this.parse(false).index().nextLinks().report();
            }
            else if (siteAction.getAction() != null && siteAction.getAction().equals(Action.CLICK)) {
                if (siteAction.getGetContent().equals(SeleniumActionGetContent.DONE_ACTION)) {
                    logger.info("[] do click when DONE_ACTION");
                    Integer totalActions = siteAction.getTotalActions();
                    for (Integer from = 0; from < totalActions; from++) {
                        doClick(url, totalActions, siteAction);
                    }
                    bindingFetchResult(url);
                    boolean splitData = false;
                    this.parse(splitData).index().nextLinks().report();
                    logger.info("[] done click when DONE_ACTION");

                } else if (siteAction.getGetContent().equals(SeleniumActionGetContent.EACH_ACTION)) {
                    logger.info("[] do click for EACH_ACTION");
                    Integer totalActions = siteAction.getTotalActions();
                    if (totalActions != -1) {
                        logger.info("[] do click for EACH_ACTION with limit from: {}", totalActions);
                        for (Integer from = 0; from < totalActions; from++) {
                            WebElement loadMoreButton = getLoadMoreButton(siteAction);
                            logger.info("[] do click at step: {}", from);
                            if (loadMoreButton != null && loadMoreButton.isDisplayed()) {
                                doClick(url, totalActions, siteAction);
                                bindingFetchResult(url);
                                boolean splitData = true;
                                this.parse(splitData).index().nextLinks().report();
                            }
                        }
                    } else {
                        while (true) {
                            WebElement loadMoreButton = getLoadMoreButton(siteAction);
                            if (loadMoreButton != null && loadMoreButton.isDisplayed()) {
                                doClick(url, totalActions, siteAction);
                                bindingFetchResult(url);
                                boolean splitData = true;
                                this.parse(splitData).index().nextLinks().report();
                            } else {
                                break;
                            }
                        }
                    }

                }
            }
        }
        quitBroweser();
        return this;
    }

    private CrawlLine getScrapeData(CrawlLine currentCrawlerInput, List<ConfigMapping> mappingList, Document htmlDoc, Link link, Link parentLink, Channel channel, ConfigGroup configGroup, boolean splitData) {
        if (mappingList == null || mappingList.size() == 0 || htmlDoc == null) return null;
        List<HashMap<String, List<String>>> data = new ArrayList<>();
        HashMap<String, List<String>> resultData = parserData(mappingList, htmlDoc, link, parentLink);
        data.add(resultData);
        if (splitData) {
            currentCrawlerInput.setData(data);
        } else {
            if (currentCrawlerInput.getData().size() == 0) {
                currentCrawlerInput.setData(data);
            } else {
                currentCrawlerInput.updateData(data, configGroup);
            }
        }

        return currentCrawlerInput;
    }

    private List<ConfigGroup> getConfigGroupByLevel(Integer currentLevel, Set<ConfigGroup> configGroups) {
        if (configGroups == null || configGroups.size() == 0) return null;
        List<ConfigGroup> configGroupList = new ArrayList<>();
        for (ConfigGroup configGroup : configGroups) {
            if (configGroup.getCurrentLevel() == currentLevel) {
                configGroupList.add(configGroup);
            }
        }
        return configGroupList;
    }

    private HashMap<String, List<String>> parserData(List<ConfigMapping> mappingList, Document htmlDoc, Link link, Link parentLink) {
        HashMap<String, List<String>> result = new HashMap<>();
        for (ConfigMapping configMapping : mappingList) {
            String selector = configMapping.getSelector();
            String name = configMapping.getName();
            String attr = configMapping.getAttr();
            Elements fieldElements = htmlDoc.select(selector);
            if (fieldElements != null && fieldElements.size() > 0) {
                List<String> values = new ArrayList<>();
                for (Element element : fieldElements) {
                    String data = "";
                    if (attr.equals("text")) {
                        data = element.text();
                    } else if (attr.equals("html")) {
                        data = element.html();
                    } else {
                        data = element.attr(attr);
                    }
                    values.add(data);
                }
                result.put(name, values);
            }
        }
        if (result.get("url") == null || result.get("url").get(0) == "") {
            List<String> urls = new ArrayList<>();
            urls.add(link.getUrl());
            result.put("url", urls);
        }
        return result;
    }

    @Override
    public Crawler parse(boolean splitData) {
        logger.info("[] call Crawler parse");
        CrawlLine crawlLine = this.getCrawline();
        Channel channel = this.getChannel();
        Integer currentLevel = crawlLine.getLink().getCurrentLevel();
        CrawlerResult crawlerResult = this.getCrawlerResult();
        Document document = crawlerResult.getDocument();
        Link link = crawlLine.getLink();
        link.setUrl(crawlerResult.getCurrentUrl());
        Set<ConfigGroup> configGroups = channel.getConfigGroups();
        List<ConfigGroup> listConfigGroups = getConfigGroupByLevel(currentLevel, configGroups);
        for (ConfigGroup configGroup : listConfigGroups) {
            List<ConfigMapping> mappingList = new ArrayList<>(configGroup.getMappings());
            crawlLine = getScrapeData(crawlLine, mappingList, document, link, link, channel, configGroup, splitData);
            logger.info("[] call Crawler parse crawlLine: {}", crawlLine);
        }
        this.setCrawline(crawlLine);

        return this;
    }

    @Override
    public Crawler index() {
        logger.info("[] Crawler index");
        CrawlLine crawlLine = this.getCrawline();
        String id = crawlLine.getLink().getId();
        if (id == null) id = DateTimeUtils.currentTimeMillis() + "";
        String fileName = "result_level_" + crawlLine.getLink().getCurrentLevel() + "_link_" + id;
        FileStoreService.writeLocalFile(fileName, crawlLine);
        Integer currentLevel = crawlLine.getLink().getCurrentLevel();
        if (currentLevel + 1 == crawlLine.getChannel().getArchiveLevel()) {
            // send to queue index data
        }
        return this;
    }

    @Override
    public Crawler upload() {
        return null;
    }

    private Integer getMaxItemInResultData(HashMap<String, List<String>> resultData) {
        try {
            if (resultData == null || resultData.isEmpty()) return 0;
            Set<String> fields = resultData.keySet();
            Integer max = 0;
            for (String field : fields) {
                List<String> data = resultData.get(field);
                if (data != null && data.size() > max) {
                    max = data.size();
                }
            }
            return max;
        } catch (Exception ex) {
            ex.printStackTrace();
            //log.info("getMaxItemInResultData ex: {}", ex.toString());
            return 0;
        }
    }

    private List<CrawlLine> addNextLink(List<CrawlLine> nextInputs, CrawlLine currentCrawlerInput, Integer currentLevel, String url, HashMap<String, List<String>> data) {
        try {
            if (currentCrawlerInput.getChannel().getUrl() == url) return nextInputs;
            Link link = new Link();
            link.setCurrentLevel(currentLevel);
            link.setUrl(url);
            CrawlLine nextInput = new CrawlLine();
            nextInput.setChannel(currentCrawlerInput.getChannel());
            nextInput.setLink(link);
            List<HashMap<String, List<String>>> splitData = new ArrayList<>();
            splitData.add(data);
            nextInput.setData(splitData);
            nextInputs.add(nextInput);
            return nextInputs;
        } catch (Exception ex) {
            return nextInputs;
        }

    }

//    public static void main(String args[]) {
//
//        Crawler crawler = new Crawler();
//
//        CrawlLine crawlLine = new FileStoreService().readLocalCrawline("sample_crawline.json");
//        List<CrawlLine> crawlLineList = crawler.getNextLinks(crawlLine);
//        logger.info("crawlLineList: {}", crawlLineList.size());
//
//    }

    private List<CrawlLine> getNextLinks(CrawlLine currentCrawlerInput) {

        List<HashMap<String, List<String>>> listObjectdata = currentCrawlerInput.getData();
        if (listObjectdata == null || listObjectdata.size() == 0) {
            return null;
        }
        /* get fields set */
        List<String> fields = new ArrayList<>();
        for (HashMap<String, List<String>> objectData : listObjectdata) {
            Set<String> fieldsSet = objectData.keySet();
            for (String field : fieldsSet) {
                fields.add(field);
            }
        }
        /* get max item in set */
        int maxItem = 0;
        for (HashMap<String, List<String>> objectData : listObjectdata) {
            int currentLength = getMaxItemInResultData(objectData);
            if (maxItem < currentLength) {
                maxItem = currentLength;
            }
        }
        List<HashMap<String, List<String>>> splitData = new ArrayList<>();
        for (HashMap<String, List<String>> objectData : listObjectdata) {
            for (int i = 0; i < maxItem; i++) {
                HashMap<String, List<String>> dataItem = new HashMap<>();
                for (String field : fields) {
                    String data = "";
                    try {
                        data = objectData.get(field).get(i);
                    } catch (Exception ex) {
                        //log.info("error array index object data: {}", ex.toString());
                    }
                    List<String> datas = new ArrayList<>();
                    datas.add(data);
                    dataItem.put(field, datas);
                }
                splitData.add(dataItem);
            }
        }
        List<CrawlLine> nextInputs = new ArrayList<>();
        Integer nextLevel = currentCrawlerInput.getLink().getCurrentLevel() + 1;

        for (HashMap<String, List<String>> objectData : splitData) {

            String url = objectData.get("url").get(0);
            if (currentCrawlerInput.getChannel().isAllowExternalUrl() == null || !currentCrawlerInput.getChannel().isAllowExternalUrl()) {
                String domain = CrawlerSiteUtils.getDomainName(url);
                if (currentCrawlerInput.getChannel().getUrl().contains(domain)) {
                    addNextLink(nextInputs, currentCrawlerInput, nextLevel, url, objectData);
                }
            } else {
                addNextLink(nextInputs, currentCrawlerInput, nextLevel, url, objectData);
            }

        }
        return nextInputs;
    }

    @Override
    public Crawler nextLinks() {
        CrawlLine crawlLine = this.getCrawline();
        Channel channel = crawlLine.getChannel();
        logger.info("[] nextlinks current level: {}, archive level: {}", crawlLine.getLink().getCurrentLevel(), channel.getArchiveLevel());
        if (crawlLine.getLink().getCurrentLevel() + 1 >= channel.getTotalLevel()) return this;
        List<CrawlLine> nextLinks = getNextLinks(crawlLine);
        int from = 0;
        for (CrawlLine nextInput : nextLinks) {
            String id = nextInput.getLink().getId();
            if (id == null) {
                id = DateTimeUtils.currentTimeMillis() + "";
            }
            String resultFileName = "next_link_" + id;
            FileStoreService.writeLocalFile(resultFileName, nextInput);
            if (commandLiner != null) {
                logger.info("[] send  next link to queue from: {}", from);
                commandLiner.sendNextCrawlLine(nextInput);
                from++;
            }
        }

        return this;
    }

    @Override
    public Crawler report() {
        return null;
    }

    @Override
    public Crawler start() {
        this.fetch();
        return this;
    }

    private void sleep(Long miliseconds) {
        try {
            Thread.sleep(miliseconds);
        } catch (Exception ex) {

        }
    }

    private void doScroll(String url, Integer totalActions) {
        sleep(2000L);
        int total = 0;
        do {
            sleep(3000L);
            ((JavascriptExecutor) this.browser)
                .executeScript("window.scrollTo(0, 80000)");
            total++;
        }
        while (total < totalActions);
    }

    private WebElement getLoadMoreButton(SiteAction fetchAction) {
        final String cssSelector = fetchAction.getSelector();
        Wait<WebDriver> stubbornWait = new FluentWait<>(this.browser)
            .withTimeout(10, TimeUnit.SECONDS)
            .pollingEvery(5, TimeUnit.SECONDS)
            .ignoring(NoSuchElementException.class)
            .ignoring(StaleElementReferenceException.class);

        WebElement loadMoreButton = stubbornWait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                return driver.findElement(By.cssSelector(cssSelector));
            }
        });
        return loadMoreButton;
    }

    private void doClick(String url, Integer totalActions, SiteAction fetchAction) {
        try {
            logger.info("[] start do click next");
            WebElement loadMoreButton = getLoadMoreButton(fetchAction);
            ((JavascriptExecutor) this.browser).executeScript("arguments[0].scrollIntoView(true);", loadMoreButton);
            sleep(500L);
            if (loadMoreButton != null) {
                loadMoreButton.click();
                sleep(3000L);
            }
        } catch (Exception ex) {
            logger.info("[] click event exception: {}", ex.toString());
        }
    }

    private void doClicks(String url, Integer totalActions, SiteAction fetchAction) {

        final String cssSelector = fetchAction.getSelector();
        sleep(3000L);
        while (true) {
            Wait<WebDriver> stubbornWait = new FluentWait<>(this.browser)
                .withTimeout(10, TimeUnit.SECONDS)
                .pollingEvery(5, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);

            WebElement loadMoreButton = stubbornWait.until(new Function<WebDriver, WebElement>() {
                public WebElement apply(WebDriver driver) {
                    return driver.findElement(By.cssSelector(cssSelector));
                }
            });
            if (loadMoreButton != null) {
                loadMoreButton.click();
                sleep(3000L);
            } else {
                break;
            }
        }

    }
}
