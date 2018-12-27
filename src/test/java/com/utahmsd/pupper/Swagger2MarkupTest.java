package com.utahmsd.pupper;

import io.github.swagger2markup.Swagger2MarkupConverter;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import java.io.BufferedWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = {PupperApplication.class, SwaggerConfig.class})
@SpringBootTest
@AutoConfigureRestDocs(outputDir = "build/asciidoc/snippets")
@WebAppConfiguration
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
public class Swagger2MarkupTest {

    private final String TEST_URL = System.getProperty("TEST_URL", "http://localhost:5000/");

    @Inject
    private MockMvc mockMvc;

    @Ignore
    @Test
    public void createSpringfoxSwaggerJson() throws Exception {
        //String designFirstSwaggerLocation = Swagger2MarkupTest.class.getResource("/swagger.json").getPath();

        String outputDir = System.getProperty("io.springfox.staticdocs.outputDir");
        MvcResult mvcResult = this.mockMvc.perform(get("/v2/api-docs")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        String swaggerJson = response.getContentAsString();
        Files.createDirectories(Paths.get(outputDir));
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputDir, "swagger.json"), StandardCharsets.UTF_8)) {
            writer.write(swaggerJson);
        }
    }

    @Ignore
    @Test
    public void convertRemoteSwaggerToAsciiDoc() throws MalformedURLException {
//        Path path = Paths.get("src/docs/swagger/swagger.json");
        Swagger2MarkupConverter.from(new URL(TEST_URL + "v2/api-docs")).build()
                .toFolder(Paths.get("src/docs/asciidoc"));
    }
//
//    private String createUser() throws JsonProcessingException {
//        UserAccount account = new UserAccount();
//        account.setUsername("thisisatest@test.com");
//        account.setPassword("password");
//        return new ObjectMapper().writeValueAsString(account);
//    }
}