package io.github.crawlerbot.cucumber.stepdefs;

import io.github.crawlerbot.CrawlerWorkerApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = CrawlerWorkerApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
