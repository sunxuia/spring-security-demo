package net.sunxu.study;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
public class ResourceControllerTests {

    @Autowired
    private MockMvc mockMvc;

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
    public void noMethodResource_denied() throws Exception {
        expectForbid(mockMvc, "/class-resource/no-method-resource");
    }

    @Test
    public void withMethodResource_access() throws Exception {
        expectAccess(mockMvc, "/class-resource/with-method-resource", "withMethodResource");
    }
}
