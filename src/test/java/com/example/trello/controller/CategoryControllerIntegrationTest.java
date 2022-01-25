package com.example.trello.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.example.trello.dto.CategoryDTO;
import com.example.trello.entity.Category;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Base64;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"security.basic.enabled=false", "management.security.enabled=false"})
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
@AutoConfigureMockMvc
public class CategoryControllerIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();


    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }


    @WithMockUser("/admin")
    @Test
    public void testCreateCategory() throws Exception {

        Category person = new Category(115, "Category 5");
        String jsonRequest = objectMapper.writeValueAsString(person);

        MvcResult result = mockMvc
                .perform(post("/api/categories/category").content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        assertEquals(200, result.getResponse().getStatus());

    }

    @Test
    public void testGetCategories() {
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/categories"), HttpMethod.GET, entity, String.class);
//        ResponseEntity<?> response = restTemplate.withBasicAuth("admin", "123456").getForEntity("/api/categories",
//                ArrayList.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetCategoriesWithMockMvc() throws Exception {

        MvcResult result = mockMvc.perform(get("/api/categories").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        assertEquals(200, result.getResponse().getStatus());

    }

    @Test
    public void testGetCategoryById() throws Exception {
//        List<Category> categoryDTOList = restTemplate.getForObject("http://localhost:" +port+ "/categories", List.class);
//        Assertions.assertNotNull(categoryDTOList);

//        ResponseEntity<CategoryDTO> response = restTemplate.getForEntity("/categories/category/111", CategoryDTO.class);
//
//        assertEquals(111, response.getBody().getCategory_id());

       // mvc.perform(get("/categories/category/1")).andExpect(status().isOk());

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/categories/category/1"), HttpMethod.GET, entity, String.class);

        String expected = "{\"category_id\":1,\"categoryName\":\"Category 1\"}";
       JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    @WithMockUser("/admin")
    @Test
    public void testUpdateCategory() throws Exception {

        Category person = new Category(3, "Cate 5");
        String jsonRequest = objectMapper.writeValueAsString(person);

        MvcResult result = mockMvc
                .perform(put("/api/categories/category/3").content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        assertEquals(200, result.getResponse().getStatus());

    }

    @Test
    public void shouldReturn200WhenGetCateById() throws Exception{
        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturn200WhenGetAllCate() throws Exception{
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk());
    }



//    @Test
//    public void testCreateCategory() throws Exception {
//
//        ObjectNode loginRequest = objectMapper.createObjectNode();
//        loginRequest.put("username","admin");
//        loginRequest.put("password","123456");
//        JsonNode loginResponse = restTemplate.postForObject("/api/signin", loginRequest.toString(), JsonNode.class);
//
//        //HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//        headers.add("X-Authorization", "Bearer " + loginResponse.get("token").textValue());
//        headers.add("Content-Type", "application/json");
//
////        return new HttpEntity<>(null, headers);
////
////        HttpEntity request = getRequestEntity();
////        ResponseEntity response = template.exchange("/get",
////                HttpMethod.GET,
////                request,
////                new ParameterizedTypeReference<List<Foo>>() {});
//        //assert stuff
//
//        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
//
//        ResponseEntity<String> response = restTemplate.withBasicAuth("admin", "123456").exchange(
//                createURLWithPort("/api/categories/category"), HttpMethod.POST, entity, String.class);
//
////        ResponseEntity<String> result = restTemplate.withBasicAuth("admin", "123456")
////                .postForEntity("/api/categories/category", entity, String.class);
//
//        //String actual = response.getHeaders().get(HttpHeaders.LOCATION).get(0);
//        int actual = response.getStatusCode().value();
//
//        assertEquals(201, actual);
//
//    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
