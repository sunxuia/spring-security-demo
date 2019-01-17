package net.sunxu.study.c1;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FilterControllerTests {
    @Autowired
    private MockMvc mockMvc;

    private void assertGetList(String uri, List<?> list) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/filter" + uri)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        new ObjectMapper().writeValueAsString(list)
                ));
    }

    @Test
    public void testNoFilter() throws Exception {
        assertGetList("", Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
    }


    @Test
    public void testGetEven() throws Exception {
        assertGetList("/get-even", Arrays.asList(1, 3, 5, 7, 9));
    }

    @Test
    public void testFilterEven() throws Exception {
        assertGetList("/filter-even?list=1,2,3,4", Arrays.asList(1, 3));
    }
}
