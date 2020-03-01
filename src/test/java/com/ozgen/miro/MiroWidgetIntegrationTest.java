package com.ozgen.miro;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ozgen.miro.controllers.dto.WidgetDTO;
import com.ozgen.miro.domain.Database;
import com.ozgen.miro.domain.WidgetAlreadyExistsException;
import com.ozgen.miro.domain.WidgetEntity;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MiroWidgetIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Database database;
    @Autowired
    private ObjectMapper objectMapper;
    private WidgetEntity[] testWidgets;

    @Test
    public void givenWidgets_whenGetWidget_thenStatus200() throws Exception {
        createTestWidgets(2);
        mockMvc.perform(get("/widgets/{widgetId}", testWidgets[0].getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id", is(testWidgets[0].getId()))).andReturn();
    }

    @Test
    public void givenWidgets_whenGetAllWidgets_thenStatus200_zIndexSorting() throws Exception {
        createTestWidgets(2);
        mockMvc.perform(get("/widgets").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$[1].id", is(testWidgets[0].getId())))
                .andExpect(jsonPath("$[0].id", is(testWidgets[1].getId()))).andReturn();
    }

    @Test
    public void givenNoWidgets_whenCreateWidgets_thenStatus201_widgetsReturnedHasSize1() throws Exception {
        WidgetDTO widgetDTO = new WidgetDTO.Builder().withHeight(10).withWidth(20).withXCoordinate(100)
                .withYCoordinate(150).withzIndex(1).build();
        mockMvc.perform(post("/widgets").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(widgetDTO))).andExpect(status().isCreated()).andReturn();
        mockMvc.perform(get("/widgets").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1))).andReturn();
    }

    @Test
    public void givenWidgets_whenUpdateWidget_thenStatus200() throws Exception {
        createTestWidgets(2);
        WidgetDTO widgetDTO = new WidgetDTO.Builder().withHeight(10).withWidth(20).withXCoordinate(100)
                .withYCoordinate(500).withzIndex(1).build();
        mockMvc.perform(put("/widgets/{widgetId}", testWidgets[0].getId()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(widgetDTO))).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testWidgets[0].getId()))).andExpect(jsonPath("$.yCoordinate", is(500)))
                .andExpect(jsonPath("$.xCoordinate", is(100))).andReturn();
    }

    @Test
    public void givenWidgets_whenUpdateWidgetWithNonExistentId_thenStatus400() throws Exception {
        createTestWidgets(2);
        WidgetDTO widgetDTO = new WidgetDTO.Builder().withHeight(10).withWidth(20).withXCoordinate(100)
                .withYCoordinate(500).withzIndex(1).build();
        mockMvc.perform(put("/widgets/{widgetId}", 0).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(widgetDTO))).andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    public void givenWidgets_whenDeleteWidgetWithNonExistentId_thenStatus400() throws Exception {
        createTestWidgets(2);
        mockMvc.perform(delete("/widgets/{widgetId}", 0).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError()).andReturn();
    }

    @Test
    public void givenWidgets_whenDeleteWidget_thenStatus200_widgetsReturnedHasSize1() throws Exception {
        createTestWidgets(2);
        mockMvc.perform(delete("/widgets/{widgetId}", testWidgets[0].getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()).andReturn();
        mockMvc.perform(get("/widgets").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].id", is(testWidgets[1].getId())))
                .andReturn();
    }

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
}
