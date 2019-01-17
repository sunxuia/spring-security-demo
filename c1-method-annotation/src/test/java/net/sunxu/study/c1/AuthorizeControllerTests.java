package net.sunxu.study.c1;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthorizeControllerTests {
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

    private MockMvc rememberMe() {
        Authentication authentication = new RememberMeAuthenticationToken("admin",
                "remember-me-as-admin", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return mockMvc;
    }

    private void logout() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/logout").with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    private void expectAccess(MockMvc mockMvc, String uri, String expectContent) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/authorize" + uri).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(expectContent)));
    }

    private void expectForbid(MockMvc mockMvc, String uri) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/authorize" + uri).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void noAuthority_anonymous_access() throws Exception {
        expectAccess(anonymous(), "/", "noAuthority");
    }

    @Test
    public void noAuthority_authenticated_access() throws Exception {
        expectAccess(authenticated(), "/", "noAuthority");
    }

    @Test
    public void hasRoleAnonymous_anonymous_access() throws Exception {
        expectAccess(anonymous(), "/has-role-anonymous", "hasRoleAnonymous");
    }

    @Test
    public void hasRoleAnonymous_authenticated_forbid() throws Exception {
        expectForbid(authenticated(), "/has-role-anonymous");
    }

    @Test
    public void hasRoleAdmin_anonymous_forbid() throws Exception {
        expectForbid(anonymous(), "/has-role-admin");
    }

    @Test
    public void hasRoleAdmin_authenticated_access() throws Exception {
        expectAccess(authenticated(), "/has-role-admin", "hasRoleAdmin");
    }

    @Test
    public void hasRoleRoot_anonymous_forbid() throws Exception {
        expectForbid(anonymous(), "/has-role-root");
    }

    @Test
    public void hasRoleRoot_authenticated_forbid() throws Exception {
        expectForbid(authenticated(), "/has-role-root");
    }

    @Test
    public void hasAuthorityAdmin_anonymous_forbid() throws Exception {
        expectForbid(anonymous(), "/has-authority-admin");
    }

    @Test
    public void hasAuthorityAdmin_authenticated_access() throws Exception {
        expectAccess(authenticated(), "/has-authority-admin", "hasAuthorityAdmin");
    }

    @Test
    public void hasAuthorityAnonymous_anonymous_access() throws Exception {
        expectAccess(anonymous(), "/has-authority-anonymous", "hasAuthorityAnonymous");
    }

    @Test
    public void hasAuthorityAnonymous_authenticated_forbid() throws Exception {
        expectForbid(authenticated(), "/has-authority-anonymous");
    }

    @Test
    public void isAnonymous_anonymous_access() throws Exception {
        expectAccess(anonymous(), "/is-anonymous", "isAnonymous");
    }

    @Test
    public void isAnonymous_authenticated_forbid() throws Exception {
        expectForbid(authenticated(), "/is-anonymous");
    }

    @Test
    public void isAuthenticated_anonymous_forbid() throws Exception {
        expectForbid(anonymous(), "/is-authenticated");
    }

    @Test
    public void isAuthenticated_authenticated_access() throws Exception {
        expectAccess(authenticated(), "/is-authenticated", "isAuthenticated");
    }

    @Test
    public void isFullyAuthenticated_anonymous_forbid() throws Exception {
        expectForbid(anonymous(), "/is-fully-authenticated");
    }

    @Test
    public void isFullyAuthenticated_authenticatedWithNoRememberMe_access() throws Exception {
        expectAccess(authenticated(), "/is-fully-authenticated", "isFullyAuthenticated");
    }

    @Test
    public void isFullyAuthenticated_authenticatedWithRememberMe_forbid() throws Exception {
        expectForbid(rememberMe(), "/is-fully-authenticated");
    }

    @Test
    public void isRememberMe_anonymous_forbid() throws Exception {
        expectForbid(anonymous(), "/is-remember-me");
    }

    @Test
    public void isRememberMe_authenticatedWithNoRememberMe_forbid() throws Exception {
        expectForbid(authenticated(), "/is-remember-me");
    }

    @Test
    public void isRememberMe_authenticatedWithRememberMe_access() throws Exception {
        expectAccess(rememberMe(), "/is-remember-me", "isRememberMe");
    }

    @Test
    public void principalIsAnonymousUser_anonymous_access() throws Exception {
        expectAccess(anonymous(), "/principal-is-anonymous-user", "principalIsAnonymousUser");
    }

    @Test
    public void principalIsAnonymousUser_authenticated_forbid() throws Exception {
        expectForbid(authenticated(), "/principal-is-anonymous-user");
    }

    @Test
    public void principalIsCustomUserDetails_anonymous_forbid() throws Exception {
        expectForbid(anonymous(), "/principal-is-custom-user-details");
    }

    @Test
    public void principalIsCustomUserDetails_authenticated_access() throws Exception {
        expectAccess(authenticated(), "/principal-is-custom-user-details", "principalIsCustomUserDetails");
    }

    @Test
    public void userNameIsAdmin_anonymous_forbid() throws Exception {
        expectForbid(anonymous(), "/user-name-is-admin");
    }

    @Test
    public void userNameIsAdmin_authenticated_access() throws Exception {
        expectAccess(authenticated(), "/user-name-is-admin", "userNameIsAdmin");
    }

    @Test
    public void userNameIsAdmin_authenticatedWithNameA_forbid() throws Exception {
        Authentication authentication = new UsernamePasswordAuthenticationToken("A", "123456");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        expectForbid(mockMvc, "/user-name-is-admin");
    }

    @Test
    public void authenticatedIsAuthenticated_anonymous_access() throws Exception {
        expectAccess(anonymous(), "/authentication-is-authenticated", "authenticationIsAuthenticated");
    }

    @Test
    public void authenticatedIsAuthenticated_authenticated_access() throws Exception {
        expectAccess(authenticated(), "/authentication-is-authenticated", "authenticationIsAuthenticated");
    }

    @Test
    public void authenticationDetailsIsFrom127001_localTest_access() throws Exception {
        expectForbid(authenticated(), "/authentication-details-is-from-127001");
        logout();
        expectAccess(anonymous(), "/authentication-details-is-from-127001", "authenticationDetailsIsFrom127001");
    }

    @Test
    public void hasPermissionW2Args_anonymous_access() throws Exception {
        expectAccess(anonymous(), "/has-permission-w-2-args", "hasPermissionW2Args");
    }

    @Test
    public void hasPermissionW2Args_authenticated_forbid() throws Exception {
        expectForbid(authenticated(), "/has-permission-w-2-args");
    }

    @Test
    public void hasPermissionW3Args_anonymous_access() throws Exception {
        expectAccess(anonymous(), "/has-permission-w-3-args", "hasPermissionW3Args");
    }

    @Test
    public void hasPermissionW3Args_authenticated_forbid() throws Exception {
        expectForbid(authenticated(), "/has-permission-w-3-args");
    }

    @Test
    public void postAuthorize_annoymousAndAuthorized_2VisitCount() throws Exception {
        authenticated().perform(MockMvcRequestBuilders.get("/authorize/post-authorize"))
                .andExpect(status().isForbidden());
        logout();
        anonymous().perform(MockMvcRequestBuilders.get("/authorize/post-authorize"))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
    }
}
