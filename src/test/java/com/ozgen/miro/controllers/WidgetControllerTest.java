package com.ozgen.miro.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ozgen.miro.controllers.dto.WidgetDTO;
import com.ozgen.miro.controllers.ratelimiting.RateLimit;
import com.ozgen.miro.controllers.ratelimiting.RateLimitExceededException;
import com.ozgen.miro.controllers.ratelimiting.RateLimiter;
import com.ozgen.miro.converters.WidgetToDomain;
import com.ozgen.miro.domain.PageData;
import com.ozgen.miro.domain.WidgetEntity;
import com.ozgen.miro.services.WidgetService;

@RunWith(SpringRunner.class)
@WebMvcTest(WidgetController.class)
public class WidgetControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private WidgetService widgetService;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean(name = "createWidgetRateLimiter")
    private RateLimiter createWidgetRateLimiter;

    @Test
    public void createWidget() throws Exception {
        String widgetId = UUID.randomUUID().toString();
        WidgetDTO widgetDTO = new WidgetDTO.Builder().withHeight(10).withWidth(20).withXCoordinate(100)
                .withYCoordinate(150).withzIndex(1).build();
        WidgetEntity widgetEntity = new WidgetToDomain().convert(widgetDTO);
        WidgetEntity widgetReturned = widgetEntity.toBuilder().withId(widgetId).build();
        when(widgetService.createWidget(widgetEntity)).thenReturn(widgetReturned);
        when(createWidgetRateLimiter.acquire())
                .thenReturn(new RateLimit.Builder().withLimiterId("create").withMaxRequestsInPeriod(4)
                        .withNextResetInMilliSeconds(10000l).withPeriodInSeconds(60).withRemainingRequests(3).build());
        mockMvc.perform(post("/widgets").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(widgetDTO))).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(widgetId))).andReturn();
    }

    @Test
    public void createWidget_rateLimitexceeded() throws Exception {
        String widgetId = UUID.randomUUID().toString();
        WidgetDTO widgetDTO = new WidgetDTO.Builder().withHeight(10).withWidth(20).withXCoordinate(100)
                .withYCoordinate(150).withzIndex(1).build();
        WidgetEntity widgetEntity = new WidgetToDomain().convert(widgetDTO);
        WidgetEntity widgetReturned = widgetEntity.toBuilder().withId(widgetId).build();
        when(widgetService.createWidget(widgetEntity)).thenReturn(widgetReturned);
        when(createWidgetRateLimiter.acquire()).thenThrow(RateLimitExceededException.class);
        mockMvc.perform(post("/widgets").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(widgetDTO))).andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    public void updateWidget() throws Exception {
        String widgetId = UUID.randomUUID().toString();
        WidgetDTO widgetDTO = new WidgetDTO.Builder().withHeight(10).withWidth(20).withXCoordinate(100)
                .withYCoordinate(150).withzIndex(1).build();
        WidgetEntity widgetEntity = new WidgetToDomain().convert(widgetDTO.toBuilder().withId(widgetId).build());
        WidgetEntity widgetReturned = widgetEntity.toBuilder().build();
        when(widgetService.updateWidget(widgetEntity)).thenReturn(widgetReturned);
        mockMvc.perform(put("/widgets/{widgetId}", widgetId).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(widgetDTO))).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(widgetId))).andReturn();
    }

    @Test
    public void deleteWidget() throws Exception {
        String widgetId = UUID.randomUUID().toString();
        mockMvc.perform(delete("/widgets/{widgetId}", widgetId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()).andReturn();
    }

    @Test
    public void getWidget() throws Exception {
        String widgetId = UUID.randomUUID().toString();
        WidgetEntity widgetEntity = new WidgetEntity.Builder().withId(widgetId).withHeight(10).withWidth(20)
                .withXCoordinate(100).withYCoordinate(150).withzIndex(1).build();
        when(widgetService.getWidget(widgetId)).thenReturn(widgetEntity);
        mockMvc.perform(get("/widgets/{widgetId}", widgetId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id", is(widgetId))).andReturn();
    }

    @Test
    public void getAllWidgets() throws Exception {
        String widgetId1 = UUID.randomUUID().toString();
        String widgetId2 = UUID.randomUUID().toString();
        WidgetEntity[] widgetEntities = new WidgetEntity[2];
        widgetEntities[0] = new WidgetEntity.Builder().withModifiedAt(System.currentTimeMillis()).withId(widgetId1)
                .withHeight(10).withWidth(20).withXCoordinate(100).withYCoordinate(150).withzIndex(1).build();
        widgetEntities[1] = new WidgetEntity.Builder().withModifiedAt(System.currentTimeMillis()).withId(widgetId2)
                .withHeight(11).withWidth(21).withXCoordinate(101).withYCoordinate(151).withzIndex(2).build();
        when(widgetService.getAllWidgets(0, 10)).thenReturn(new PageData<WidgetEntity>(widgetEntities, 0, 1, 2));
        mockMvc.perform(get("/widgets").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(widgetId1))).andExpect(jsonPath("$[1].id", is(widgetId2)))
                .andReturn();
    }

    @Test
    public void getAllWidgets_pagination_1() throws Exception {
        String widgetId1 = UUID.randomUUID().toString();
        WidgetEntity[] widgetEntities = new WidgetEntity[1];
        widgetEntities[0] = new WidgetEntity.Builder().withModifiedAt(System.currentTimeMillis()).withId(widgetId1)
                .withHeight(10).withWidth(20).withXCoordinate(100).withYCoordinate(150).withzIndex(1).build();
        when(widgetService.getAllWidgets(1, 1)).thenReturn(new PageData<WidgetEntity>(widgetEntities, 0, 1, 1));
        mockMvc.perform(get("/widgets?page=1&pageSize=1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void getAllWidgets_pagination_2() throws Exception {
        String widgetId1 = UUID.randomUUID().toString();
        String widgetId2 = UUID.randomUUID().toString();
        WidgetEntity[] widgetEntities = new WidgetEntity[2];
        widgetEntities[0] = new WidgetEntity.Builder().withModifiedAt(System.currentTimeMillis()).withId(widgetId1)
                .withHeight(10).withWidth(20).withXCoordinate(100).withYCoordinate(150).withzIndex(1).build();
        widgetEntities[1] = new WidgetEntity.Builder().withModifiedAt(System.currentTimeMillis()).withId(widgetId2)
                .withHeight(11).withWidth(21).withXCoordinate(101).withYCoordinate(151).withzIndex(2).build();
        when(widgetService.getAllWidgets(0, 2)).thenReturn(new PageData<WidgetEntity>(widgetEntities, 0, 1, 2));
        mockMvc.perform(get("/widgets?page=0&pageSize=2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)));
    }
}
