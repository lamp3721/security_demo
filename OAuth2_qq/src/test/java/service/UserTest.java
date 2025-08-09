package service;

import org.example.ApplicationMain;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ApplicationMain.class)
public class UserTest {

    @Autowired
    UserService userService;

    @Test
    public void test() {
        userService.list().forEach(System.out::println);
    }
}
