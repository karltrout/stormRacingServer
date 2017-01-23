package com.stormracing;

import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import com.stormracing.entities.UserAccount;
import com.stormracing.entities.UserAccountRepository;

import java.util.Arrays;


import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class ApplicationTests {
	
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;
    private HttpMessageConverter<Object> mappingJackson2HttpMessageConverter;
    
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserAccountRepository accountRepository;

    @SuppressWarnings("unchecked")
	@Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter =  (HttpMessageConverter<Object>) Arrays.asList(converters).stream()
            .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
            .findAny()
            .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }
    
    @Before
    public void setup() throws Exception {
       
    	this.mockMvc = webAppContextSetup(webApplicationContext).build();
        this.accountRepository.deleteAll();
        //this will be userId 2 user id 1 was used and deleted
        this.accountRepository.save(new UserAccount("Karl", "Trout", "karl.trout@gmail.com"));
   
    }
    
    @After
    public void teardown() throws Exception {
        this.accountRepository.deleteAll();
        this.accountRepository.flush();
    }
    
    @Test
    public void userNotFound() throws Exception {
        
    	mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void postNewUser() throws Exception {
    	  
    	mockMvc.perform(post("/users")
           .content(this.json(new UserAccount("test","test","test@gmail.com")))
           .contentType(contentType))
           .andExpect(status().is(200));
    }
    
	@Test
	public void contextLoads() {
	}
	
    protected String json(Object o) throws IOException {
        
    	MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
        
    }

}
