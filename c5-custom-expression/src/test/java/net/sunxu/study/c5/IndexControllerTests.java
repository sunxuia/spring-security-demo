package net.sunxu.study.c5;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class IndexControllerTests {
    @Autowired
    private MockMvc mockMvc;

    private MockMvc authenticated() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("admin", "123456");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return mockMvc;
    }

    private MockMvc anonymous() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return mockMvc;
    }

    private void expectAccess(MockMvc mockMvc, String uri, String expectContent) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(expectContent)));
    }

    private void expectForbid(MockMvc mockMvc, String uri) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void test_authenticated_access() throws Exception {
        expectAccess(authenticated(), "/test", "test");
    }

    @Test
    public void test_anonymous_forbidden() throws Exception {
        expectForbid(anonymous(), "/test");
    }

    @Test
    public void testArray_anonymous_access() throws Exception {
        expectAccess(anonymous(), "/test-array", "[\"a\",\"b\"]");
    }
}
