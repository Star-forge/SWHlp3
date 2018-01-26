package sw_client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import java.util.stream.IntStream;

public class UtilsTest {
    private static final Logger log = LogManager.getLogger("com.adbmanager.log4j2");

    @Before
    public void before(){
        Utils.resultList.clear();
    }

    @org.junit.Test
    public void addResultToList() {

        IntStream.range(0, 50).forEachOrdered(n -> {
            Utils.addResultToList("aaa");
            Assert.assertEquals("addResultToList 50 failed", n, Utils.resultList.size()-1);
        });

        log.info("addResultToList size = {}", Utils.resultList.size());
        Utils.addResultToList("aaa");
        Assert.assertEquals("addResultToList 51 failed", 50, Utils.resultList.size());

        Utils.resultList.clear();
        Utils.addResultToList("bbb");
        IntStream.range(0,49).forEachOrdered(n -> Utils.addResultToList("aaa"));

        Assert.assertTrue("", Utils.resultList.get(0).equalsIgnoreCase("bbb"));
        Utils.addResultToList("aaa");
        Assert.assertTrue("", Utils.resultList.get(0).equalsIgnoreCase("aaa"));


    }

    @org.junit.Test
    public void isSameResultsLastNtimes() {
        Utils.addResultToList("bbb");
        IntStream.range(0,9).forEachOrdered(n -> Utils.addResultToList("aaa"));
        Assert.assertFalse("isSameResultsLastNtimes 9 fail", Utils.isSameResultsLastNTimes(10));
        Utils.addResultToList("aaa");
        Assert.assertTrue("isSameResultsLastNtimes 10 fail", Utils.isSameResultsLastNTimes(10));
    }
}