package ru.practicum.shareit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class ShareItTests {
    @Autowired
    private ApplicationContext context;

   /* @Test
    void testContextLoads() {
        assertThat(context).isNotNull();
    }

    @Test
    void testCheckBeanLoading() {
        assertThat(context.containsBean("shareItApp")).isTrue();
    }*/
}