package net.sunxu.study.c2;

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
    public void index_anonymous_access() throws Exception {
        expectAccess(anonymous(), "/", "index");
    }

    @Test
    public void index_authenticated_access() throws Exception {
        expectAccess(authenticated("someone"), "/", "index");
    }

    @Test
    public void anonymousAccess_anonymous_access() throws Exception {
        expectAccess(anonymous(), "/anonymous-access", "anonymousAccess");
    }

    @Test
    public void anonymousAccess_authenticated_forbid() throws Exception {
        expectForbid(authenticated("someone"), "/anonymous-access");
    }

    @Test
    public void authenticatedAccess_anonymous_forbid() throws Exception {
        expectForbid(anonymous(), "/authenticated-access");
    }

    @Test
    public void authenticatedAccess_authenticated_access() throws Exception {
        expectAccess(authenticated("someone"), "/authenticated-access", "authenticatedAccess");
    }

    @Test
    public void roleAdminAccess_annonymous_forbid() throws Exception {
        expectForbid(anonymous(), "/role-admin-access");
    }

    @Test
    public void roleAdminAccess_authenticatedWithoutRoleAdmin_forbid() throws Exception {
        expectForbid(authenticated("someone"), "/role-admin-access");
    }

    @Test
    public void roleAdminAccess_authenticatedWithRoleAdmin_access() throws Exception {
        expectAccess(authenticated("admin"), "/role-admin-access", "roleAdminAccess");
    }
}
