package net.sunxu.study;

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

    private MockMvc authenticated(String userName) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(userName, "123456");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return mockMvc;
    }

    private void logout() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/logout").with(csrf()))
                .andExpect(status().is3xxRedirection());
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
        int status = mockMvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getStatus();
        assertTrue(status == 403 || status >= 300 && status < 400);
    }

    @Test
    public void noResourceMethod_access() throws Exception {
        expectAccess(anonymous(), "/no-resource-method", "noResourceMethod");
    }

    @Test
    public void resourceForAnonymous_anonymous_access() throws Exception {
        expectAccess(anonymous(), "/resource-for-anonymous", "resourceForAnonymous");
    }

    @Test
    public void resourceForAnonymous_authenticated_denied() throws Exception {
        expectForbid(authenticated("admin"), "/resource-for-anonymous");
        logout();
    }

    @Test
    public void resourceForAuthenticated_anonymous_denied() throws Exception {
        expectForbid(anonymous(), "/resource-for-authenticated");
    }

    @Test
    public void resourceForAuthenticated_authenticated_access() throws Exception {
        expectAccess(authenticated("admin"), "/resource-for-authenticated", "resourceForAuthenticated");
        logout();
    }

    @Test
    public void dualResources_access() throws Exception {
        expectAccess(anonymous(), "/dual-resource", "dualResources");
    }

    @Test
    public void dualResourcesWithResourcesAnnotation_access() throws Exception {
        expectAccess(anonymous(), "/dual-resource-with-resources-annotation", "dualResourcesWithResourcesAnnotation");
    }

    @Test
    public void callServiceWithResourceInInterface_access() throws Exception {
        expectAccess(anonymous(), "/call-service-with-resource-in-interface", "");
    }

    @Test
    public void callServiceWithResourceInImplement_denied() throws Exception {
        expectForbid(anonymous(), "/call-service-with-resource-in-implement");
    }
}
