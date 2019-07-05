package com.example.ADSDemoProject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.SQLException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdsDemoProjectApplicationTests {

	@Autowired
	DataSource dataSource;

	@Test
	public void shouldUsingH2DbForTesting() throws SQLException {
		Assert.assertTrue(dataSource.getConnection().toString().contains("url=jdbc:h2:mem:db"));
	}
}
