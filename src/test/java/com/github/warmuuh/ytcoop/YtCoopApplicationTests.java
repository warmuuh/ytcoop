package com.github.warmuuh.ytcoop;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = YtCoopApplication.class)
@WebAppConfiguration
public class YtCoopApplicationTests {

	//need to figure out how to start spring social stuff under test..
	@Test @Ignore
	public void contextLoads() {
	}

}
