package com.ozgen.miro;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.ozgen.miro.domain.Database;
import com.ozgen.miro.domain.WidgetAlreadyExistsException;
import com.ozgen.miro.domain.WidgetEntity;
import com.ozgen.miro.utils.LinkUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MiroWidgetHATEOASLinklIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Database database;
    private WidgetEntity[] testWidgets;

    private void createTestWidgets(int count) throws WidgetAlreadyExistsException {
        database.clear();
        testWidgets = new WidgetEntity[count];
        for (int i = 0; i < count; i++) {
            testWidgets[i] = new WidgetEntity.Builder().withModifiedAt(System.currentTimeMillis())
                    .withId(UUID.randomUUID().toString()).withHeight(10).withWidth(20).withXCoordinate(100)
                    .withYCoordinate(150).withzIndex(count - i).build();
            database.insert(testWidgets[i]);
        }
    }

    @Test
    public void givenWidgets_whenGetAllWidgets_page0_thenStatus200() throws Exception {
        int widgetsSize = 24;
        int page = 0;
        int pageSize = 10;
        createTestWidgets(widgetsSize);
        MvcResult result = mockMvc.perform(
                get("/widgets?page={page}&pageSize={pageSize}", page, pageSize).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String link = result.getResponse().getHeader("link");
        assertNotNull(link);
        int totalPages = widgetsSize / pageSize;
        if (page < totalPages) {
            assertTrue(link.contains(LinkUtil.REL_NEXT));
            assertTrue(link.contains(LinkUtil.REL_LAST));
        }
        if (page > 0) {
            assertTrue(link.contains(LinkUtil.REL_PREV));
            assertTrue(link.contains(LinkUtil.REL_FIRST));
        }
    }

    @Test
    public void givenWidgets_whenGetAllWidgets_page1_thenStatus200() throws Exception {
        int widgetsSize = 24;
        int page = 1;
        int pageSize = 10;
        createTestWidgets(widgetsSize);
        MvcResult result = mockMvc.perform(
                get("/widgets?page={page}&pageSize={pageSize}", page, pageSize).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String link = result.getResponse().getHeader("link");
        assertNotNull(link);
        int totalPages = widgetsSize / pageSize;
        if (page < totalPages) {
            assertTrue(link.contains(LinkUtil.REL_NEXT));
            assertTrue(link.contains(LinkUtil.REL_LAST));
        }
        if (page > 0) {
            assertTrue(link.contains(LinkUtil.REL_PREV));
            assertTrue(link.contains(LinkUtil.REL_FIRST));
        }
    }

    @Test
    public void givenWidgets_whenGetAllWidgets_page2_thenStatus200() throws Exception {
        int widgetsSize = 24;
        int page = 2;
        int pageSize = 10;
        createTestWidgets(widgetsSize);
        MvcResult result = mockMvc.perform(
                get("/widgets?page={page}&pageSize={pageSize}", page, pageSize).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String link = result.getResponse().getHeader("link");
        assertNotNull(link);
        int totalPages = widgetsSize / pageSize;
        if (page < totalPages) {
            assertTrue(link.contains(LinkUtil.REL_NEXT));
            assertTrue(link.contains(LinkUtil.REL_LAST));
        }
        if (page > 0) {
            assertTrue(link.contains(LinkUtil.REL_PREV));
            assertTrue(link.contains(LinkUtil.REL_FIRST));
        }
    }

    @Test
    public void givenWidgets_whenGetAllWidgets_invalidPage_thenStatus400() throws Exception {
        int widgetsSize = 24;
        int page = 3;
        int pageSize = 10;
        createTestWidgets(widgetsSize);
        mockMvc.perform(
                get("/widgets?page={page}&pageSize={pageSize}", page, pageSize).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError()).andReturn();
    }
}
