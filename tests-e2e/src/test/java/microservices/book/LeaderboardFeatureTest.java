package microservices.book;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:target/cucumber",
                "junit:target/junit-report.xml"})
public class LeaderboardFeatureTest {
}
