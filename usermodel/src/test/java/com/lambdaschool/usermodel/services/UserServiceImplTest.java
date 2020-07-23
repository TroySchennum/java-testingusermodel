package com.lambdaschool.usermodel.services;

import com.lambdaschool.usermodel.UserModelApplication;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserModelApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        List<User> myList = userService.findAll();
        for (User u: myList)
        {
            System.out.println(u.getUserid() + " " + u.getUsername());
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void a_findUserById() {
        assertEquals("cinnamon", userService.findUserById(7).getUsername());
    }

    @Test (expected = EntityNotFoundException.class)
    public void b_findUserByIdFails() {
        assertEquals("cinnamon", userService.findUserById(777).getUsername());
    }

    @Test
    public void c_findByNameContaining() {
        assertEquals(1, userService.findByNameContaining("barn").size());
    }

    @Test
    public void d_findAll() {
        assertEquals(5, userService.findAll().size());
    }

    @Test
    public void e_delete() {
        userService.delete(7);
        assertEquals(4, userService.findAll().size());
    }

    @Test
    public void f_findByName() {
        assertEquals("barnbarn", userService.findByName("barnbarn").getUsername());

    }

    @Test
    public void g_save() {
        String troy = "troy";
        User u7 = new User(troy,
                "password",
                "putt@school.lambda");

        u7.getUseremails()
                .add(new Useremail(u7,
                        "barn@email.local"));



        User addUser = userService.save(u7);
        assertNotNull(addUser);
        assertEquals(troy, addUser.getUsername());
    }

    @Test
    public void h_update() {
        String troys = "troys";
        User u7 = new User(troys,
                "password",
                "putter@school.lambda");

        u7.getUseremails()
                .add(new Useremail(u7,
                        "barner@email.local"));



        User addUser = userService.update(u7, 13);
        assertNotNull(addUser);
        assertEquals(troys, addUser.getUsername());
    }

    @Test
    public void i_deleteAll() {
        userService.deleteAll();
        assertEquals(0, userService.findAll().size());
    }
}