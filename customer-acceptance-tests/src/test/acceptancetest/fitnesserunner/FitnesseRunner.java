package acceptancetest.fitnesserunner;

import fitnesse.junit.FitNesseRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(FitNesseRunner.class)
@FitNesseRunner.Suite("GradleDemoSuite")
@FitNesseRunner.FitnesseDir(".")
@FitNesseRunner.OutputDir("./build/fitnesse-results")
class FitnesseRunner {
    @Test
    public void umm(){
        System.out.println("running");
    }
}