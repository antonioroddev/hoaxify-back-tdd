package com.hoaxfyu.hoaxify;

import com.hoaxfyu.hoaxify.User.User;
import com.hoaxfyu.hoaxify.User.UserRepository;
import com.hoaxfyu.hoaxify.shared.GenericResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    TestRestTemplate testRestTemplate;
    @Autowired
    UserRepository userRepository;
    private String PATH = "/api/1.0/users";


    // Name Scheme = methodName_condition_expectedBehaviour
    @Test
    public void postUser_whenUserIsValid_receiveOk(){
        User user = createValidUser();
        ResponseEntity<Object> response = testRestTemplate.postForEntity(PATH, user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void postUser_whenUserIsValid_userSavedToDataBase(){
        User user = createValidUser();
        testRestTemplate.postForEntity(PATH, user, Object.class);
        assertThat(userRepository.count()).isEqualTo(1);

    }

    @Test
    public void postUser_whenUserIsValid_receiveSuccess(){
        User user = createValidUser();
        ResponseEntity<GenericResponse> response = testRestTemplate.postForEntity(PATH, user, GenericResponse.class);
        assertThat(response.getBody().getMessage()).isNotNull();
    }

    private User createValidUser(){
        User user = new User();
        user.setUsername("test-Username");
        user.setDisplayName("test-displayName");
        user.setPassword("test-password");
        return user;
    }

    @Before
    public void cleanup(){
        userRepository.deleteAll();
    }

}
