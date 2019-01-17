package net.sunxu.study;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.HttpServletResponse;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class IndexControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Value("${jwt.tokenHeaderName}")
    private String tokenHeaderName;

    @Value("${jwt.tokenPrefix}")
    private String tokenPrefix;

    @Test
    public void auth_notAuthorized_getJwtString() throws Exception {
        String res = getJwtString();
        String userName = jwtTokenUtils.getUserNameFromToken(res);
        assertEquals("admin", userName);
    }

    private String getJwtString() throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get("/auth").param("name", "admin"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    public void index_authorized_getUserName() throws Exception {
        String token = getJwtString();
        mockMvc.perform(MockMvcRequestBuilders.get("/")
                .header(tokenHeaderName, tokenPrefix + token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("admin"));
    }

    @Test
    public void index_notAuthorized_getNothing() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    public void requireAuth_notAuthorized_forbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/require-auth").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpServletResponse.SC_FORBIDDEN));
    }

    @Test
    public void requireAuth_authorized_getName() throws Exception {
        String token = getJwtString();
        mockMvc.perform(MockMvcRequestBuilders.get("/require-auth")
                .header(tokenHeaderName, tokenPrefix + token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }
}
