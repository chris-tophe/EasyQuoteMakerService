package com.christophe.quoteService.model;


import com.christophe.quoteService.models.Role;
import com.christophe.quoteService.models.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserTest {

    @Test
    void whenCreateNewUserThenReadData(){
        User u = new User("Christophe","LOYER","christophe.loyer@gmail.com","pass","ChrisLoyer",true,true,true,true);
        u.getRoles().add(Role.USER);
        assertEquals(u.getEmail(),"christophe.loyer@gmail.com");
        assertEquals(u.getFirstName(),"Christophe");
        assertEquals(u.getLastName(),"LOYER");

    }

}
