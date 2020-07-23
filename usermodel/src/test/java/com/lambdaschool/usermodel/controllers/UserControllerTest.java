package com.lambdaschool.usermodel.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import com.lambdaschool.usermodel.services.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private List<User> usersList;


    @Before
    public void setUp() throws Exception {
        usersList = new ArrayList<>();

        Role r1 = new Role("admin");
        r1.setRoleid(1);
        Role r2 = new Role("user");
        r1.setRoleid(2);
        Role r3 = new Role("data");
        r1.setRoleid(3);


        // admin, data, user
        User u1 = new User("admin",
                "password",
                "admin@lambdaschool.local");
        u1.getRoles().add(new UserRoles(u1, r1));
        u1.getRoles().add(new UserRoles(u1, r2));
        u1.getRoles().add(new UserRoles(u1, r3));
        u1.getUseremails()
                .add(new Useremail(u1,
                        "admin@email.local"));
        u1.getUseremails().get(0).setUseremailid(21);
        u1.getUseremails()
                .add(new Useremail(u1,
                        "admin@mymail.local"));
        u1.getUseremails().get(1).setUseremailid(22);

        u1.setUserid(9);
        usersList.add(u1);

        // data, user
        User u2 = new User("cinnamon",
                "1234567",
                "cinnamon@lambdaschool.local");
        u2.getRoles().add(new UserRoles(u2, r2));
        u2.getRoles().add(new UserRoles(u2, r3));
        u2.getUseremails()
                .add(new Useremail(u2,
                        "cinnamon@mymail.local"));
        u2.getUseremails().get(0).setUseremailid(10);
        u2.getUseremails()
                .add(new Useremail(u2,
                        "hops@mymail.local"));
        u2.getUseremails().get(1).setUseremailid(11);
        u2.getUseremails()
                .add(new Useremail(u2,
                        "bunny@email.local"));
        u2.getUseremails().get(2).setUseremailid(12);

        u2.setUserid(10);
        usersList.add(u2);

        // user
        User u3 = new User("barnbarn",
                "ILuvM4th!",
                "barnbarn@lambdaschool.local");
        u3.getRoles().add(new UserRoles(u3, r2));
        u3.getUseremails()
                .add(new Useremail(u3,
                        "barnbarn@email.local"));
        u3.getUseremails().get(0).setUseremailid(32);
        u3.setUserid(12);
        usersList.add(u3);

        User u4 = new User("puttat",
                "password",
                "puttat@school.lambda");
        u4.getRoles().add(new UserRoles(u4, r2));
        u4.setUserid(13);
        usersList.add(u4);

        User u5 = new User("misskitty",
                "password",
                "misskitty@school.lambda");
        u5.getRoles().add(new UserRoles(u5, r2));
        u5.setUserid(13);
        usersList.add(u5);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void a_listAllUsers() throws Exception {
        String apiUrl = "/users/users";
        Mockito.when(userService.findAll()).thenReturn(usersList);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(usersList);

        assertEquals(er, tr);
    }

    @Test
    public void b_getUserById() throws Exception{
        String apiUrl = "/users/user/12";
        Mockito.when(userService.findUserById(12)).thenReturn(usersList.get(0));

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(usersList.get(0));

        assertEquals(er, tr);
    }

    @Test
    public void c_getUserByIdNotFound() throws Exception{
        String apiUrl = "/users/user/777";
        Mockito.when(userService.findUserById(777)).thenReturn(null);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();


        String er = "";

        assertEquals(er, tr);
    }

    @Test
    public void d_getUserByName() throws Exception {
        String apiUrl = "/users/user/name/barnbarn";
        Mockito.when(userService.findByName("barnbarn")).thenReturn(usersList.get(0));

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();


        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(usersList.get(0));

        assertEquals(er, tr);

    }

    @Test
    public void e_getUserLikeName() throws Exception{
        String apiUrl = "/users/user/name/like/barn";
        Mockito.when(userService.findByNameContaining("barn")).thenReturn(usersList);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();


        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(usersList);

        assertEquals(er, tr);
    }

    @Test
    public void f_addNewUser() throws Exception{
        String apiUrl="/users/user";

        String troy = "troy";
        User u7 = new User(troy,
                "password",
                "puttat@school.lambda");

        u7.getUseremails()
                .add(new Useremail(u7,
                        "barnbarn@email.local"));
        u7.setUserid(100);

        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(u7);

        Mockito.when(userService.save(any(User.class))).thenReturn(u7);
        RequestBuilder rb = MockMvcRequestBuilders.post(apiUrl)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userString);
        mockMvc.perform(rb).andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void g_updateFullUser() throws Exception {
        String apiUrl="/users/user";

        String troyt = "troyt";
        User u7 = new User(troyt,
                "password",
                "puttnnt@school.lambda");

        u7.getUseremails()
                .add(new Useremail(u7,
                        "barnbann@email.local"));
        u7.setUserid(100);

        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(u7);

        Mockito.when(userService.update(any(User.class), 13)).thenReturn(u7);
        RequestBuilder rb = MockMvcRequestBuilders.post(apiUrl)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userString);
        mockMvc.perform(rb).andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void h_updateUser() throws Exception{
        String apiUrl="/users/user";

        String troyt = "troyt";
        User u7 = new User(troyt,
                "password",
                "puttnnt@school.lambda");

        u7.getUseremails()
                .add(new Useremail(u7,
                        "barnbann@email.local"));
        u7.setUserid(100);

        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(u7);

        Mockito.when(userService.update(any(User.class), 13)).thenReturn(u7);
        RequestBuilder rb = MockMvcRequestBuilders.post(apiUrl)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userString);
        mockMvc.perform(rb).andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void i_deleteUserById() throws Exception{

    }
}