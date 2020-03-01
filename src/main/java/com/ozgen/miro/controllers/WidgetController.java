package com.ozgen.miro.controllers;

import static com.google.common.base.Preconditions.checkArgument;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_NO_CONTENT;
import static javax.servlet.http.HttpServletResponse.SC_OK;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import com.ozgen.miro.controllers.dto.WidgetDTO;
import com.ozgen.miro.controllers.exceptionhandling.ApiError;
import com.ozgen.miro.controllers.exceptionhandling.ErrorCode;
import com.ozgen.miro.controllers.exceptionhandling.MiroRestException;
import com.ozgen.miro.controllers.ratelimiting.RateLimit;
import com.ozgen.miro.controllers.ratelimiting.RateLimitExceededException;
import com.ozgen.miro.controllers.ratelimiting.RateLimiter;
import com.ozgen.miro.converters.WidgetFromDomain;
import com.ozgen.miro.domain.PageData;
import com.ozgen.miro.domain.PageNotFoundException;
import com.ozgen.miro.domain.WidgetAlreadyExistsException;
import com.ozgen.miro.domain.WidgetEntity;
import com.ozgen.miro.domain.WidgetNotFoundException;
import com.ozgen.miro.services.WidgetService;
import com.ozgen.miro.utils.LinkUtil;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Controller for basic widget operations
 *
 */
@RestController
@RequestMapping(value = { "/widgets" })
@CrossOrigin(origins = "*")
@SuppressWarnings("PMD.TooManyStaticImports")
public class WidgetController {
    private final WidgetService widgetService;
    private final ConversionService conversionService;
    private final RateLimiter getWidgetRateLimiter;
    private final RateLimiter createWidgetRateLimiter;
    private final RateLimiter updateWidgetRateLimiter;
    private final RateLimiter deleteWidgetRateLimiter;
    private final RateLimiter getAllWidgetsRateLimiter;
    private final RateLimiter globalWidgetRateLimiter;
    private final String maxPageSize;

    /**
     * Constructs {@link WidgetController}
     * 
     * @param widgetService            {@link WidgetService}
     * @param conversionService        {@link ConversionService}
     * @param getWidgetRateLimiter     {@link RateLimiter}
     * @param createWidgetRateLimiter  {@link RateLimiter}
     * @param updateWidgetRateLimiter  {@link RateLimiter}
     * @param deleteWidgetRateLimiter  {@link RateLimiter}
     * @param getAllWidgetsRateLimiter {@link RateLimiter}
     * @param globalWidgetRateLimiter  {@link RateLimiter}
     * @param maxPageSize              Maximum page size for getAll endpoint
     */
    @Autowired
    public WidgetController(WidgetService widgetService, ConversionService conversionService,
            RateLimiter getWidgetRateLimiter, RateLimiter createWidgetRateLimiter, RateLimiter updateWidgetRateLimiter,
            RateLimiter deleteWidgetRateLimiter, RateLimiter getAllWidgetsRateLimiter,
            RateLimiter globalWidgetRateLimiter, @Value("${pagination.maxPageSize}") String maxPageSize) {
        this.widgetService = widgetService;
        this.conversionService = conversionService;
        this.getWidgetRateLimiter = getWidgetRateLimiter;
        this.createWidgetRateLimiter = createWidgetRateLimiter;
        this.updateWidgetRateLimiter = updateWidgetRateLimiter;
        this.deleteWidgetRateLimiter = deleteWidgetRateLimiter;
        this.getAllWidgetsRateLimiter = getAllWidgetsRateLimiter;
        this.globalWidgetRateLimiter = globalWidgetRateLimiter;
        this.maxPageSize = maxPageSize;
    }

    /**
     * Gets a single widget created
     * 
     * @param widgetId Unique identifier of the widget
     * @param response {@link HttpServletResponse}
     * @return {@link ResponseEntity}
     * @throws MiroRestException          If widget not found
     * @throws RateLimitExceededException If request rate limit is exceeded
     */
    @RequestMapping(value = { "/{widgetId}" }, method = { RequestMethod.GET })
    @ApiOperation(value = "Gets a single widget created", notes = "Provide widgetId in the path")
    @ApiResponses({ @ApiResponse(code = SC_OK, response = WidgetDTO.class, message = "Widget returned successfully"),
            @ApiResponse(code = SC_BAD_REQUEST, response = ApiError.class, message = "Widget not found"),
            @ApiResponse(code = SC_INTERNAL_SERVER_ERROR, message = "Internal server error occurred while processing your request") })
    public ResponseEntity<WidgetDTO> getWidget(
            @ApiParam("Unique id of the widget") @PathVariable("widgetId") String widgetId,
            HttpServletResponse response) throws MiroRestException, RateLimitExceededException {
        try {
            addRateLimitHeaders(response, getWidgetRateLimiter.acquire());
            addRateLimitHeaders(response, globalWidgetRateLimiter.acquire());
            WidgetEntity widgetEntity = widgetService.getWidget(widgetId);
            return new ResponseEntity<WidgetDTO>(conversionService.convert(widgetEntity, WidgetDTO.class),
                    HttpStatus.OK);
        } catch (WidgetNotFoundException e) {
            throw new MiroRestException(ErrorCode.WIDGET_NOT_FOUND);
        }
    }

    /**
     * Creates a new widget
     * 
     * @param widgetDTO DTO object of the widget
     * @param response  {@link HttpServletResponse}
     * @return {@link ResponseEntity}
     * @throws MiroRestException          If widget verification fails
     * @throws RateLimitExceededException If request rate limit is exceeded
     */
    @RequestMapping(method = { RequestMethod.POST })
    @ApiOperation(value = "Creates a new widget", notes = "Provide WidgetDTO object in the request")
    @ApiResponses({ @ApiResponse(code = SC_CREATED, response = WidgetDTO.class, message = "Created widget"),
            @ApiResponse(code = SC_BAD_REQUEST, response = ApiError.class, message = "Widget verification fails"),
            @ApiResponse(code = SC_INTERNAL_SERVER_ERROR, message = "Internal server error occurred while processing your request") })
    public ResponseEntity<WidgetDTO> createWidget(
            @ApiParam("Data transfer object of the widget") @RequestBody WidgetDTO widgetDTO,
            HttpServletResponse response) throws MiroRestException, RateLimitExceededException {
        try {
            addRateLimitHeaders(response, createWidgetRateLimiter.acquire());
            addRateLimitHeaders(response, globalWidgetRateLimiter.acquire());
            WidgetEntity widgetEntity = widgetService
                    .createWidget(conversionService.convert(widgetDTO, WidgetEntity.class));
            return new ResponseEntity<WidgetDTO>(conversionService.convert(widgetEntity, WidgetDTO.class),
                    HttpStatus.CREATED);
        } catch (WidgetAlreadyExistsException e) {
            throw new MiroRestException(ErrorCode.WIDGET_ALREADY_EXISTS);
        }
    }

    /**
     * Updates an existing widget
     * 
     * @param widgetId  Unique identifier of the widget
     * @param widgetDTO DTO object of the widget
     * @param response  {@link HttpServletResponse}
     * @return {@link ResponseEntity}
     * @throws MiroRestException          If widget not found or widget verification fails
     * @throws RateLimitExceededException If request rate limit is exceeded
     */
    @RequestMapping(value = { "/{widgetId}" }, method = { RequestMethod.PUT })
    @ApiOperation(value = "Updates an existing widget", notes = "Provide WidgetDTO object in the request and widgetId in the path")
    @ApiResponses({ @ApiResponse(code = SC_OK, response = WidgetDTO.class, message = "Updated widget"),
            @ApiResponse(code = SC_BAD_REQUEST, response = ApiError.class, message = "Widget not found or widget verification fails"),
            @ApiResponse(code = SC_INTERNAL_SERVER_ERROR, message = "Internal server error occurred while processing your request") })
    public ResponseEntity<WidgetDTO> updateWidget(
            @ApiParam("Unique id of the widget") @PathVariable("widgetId") String widgetId,
            @ApiParam("Data transfer object of the widget") @RequestBody WidgetDTO widgetDTO,
            HttpServletResponse response) throws MiroRestException, RateLimitExceededException {
        try {
            addRateLimitHeaders(response, updateWidgetRateLimiter.acquire());
            addRateLimitHeaders(response, globalWidgetRateLimiter.acquire());
            WidgetEntity widgetEntity = widgetService.updateWidget(
                    conversionService.convert(widgetDTO.toBuilder().withId(widgetId).build(), WidgetEntity.class));
            return new ResponseEntity<WidgetDTO>(conversionService.convert(widgetEntity, WidgetDTO.class),
                    HttpStatus.OK);
        } catch (WidgetNotFoundException e) {
            throw new MiroRestException(ErrorCode.WIDGET_NOT_FOUND);
        }
    }

    /**
     * Deletes an existing widget
     * 
     * @param widgetId Unique identifier of the widget
     * @param response {@link HttpServletResponse}
     * @return {@link ResponseEntity}
     * @throws MiroRestException          If widget not found or paging parameter verification fails
     * @throws RateLimitExceededException If request rate limit is exceeded
     */
    @RequestMapping(value = { "/{widgetId}" }, method = { RequestMethod.DELETE })
    @ApiOperation(value = "Deletes an existing widget", notes = "Provide widgetId in the path")
    @ApiResponses({ @ApiResponse(code = SC_NO_CONTENT, response = WidgetDTO.class, message = "Deleted widget"),
            @ApiResponse(code = SC_BAD_REQUEST, response = ApiError.class, message = "Widget not found or paging parameter verification fails"),
            @ApiResponse(code = SC_INTERNAL_SERVER_ERROR, message = "Internal server error occurred while processing your request") })
    public ResponseEntity<WidgetDTO> deleteWidget(
            @ApiParam("Unique id of the widget") @PathVariable("widgetId") String widgetId,
            HttpServletResponse response) throws MiroRestException, RateLimitExceededException {
        try {
            addRateLimitHeaders(response, deleteWidgetRateLimiter.acquire());
            addRateLimitHeaders(response, globalWidgetRateLimiter.acquire());
            widgetService.deleteWidget(widgetId);
            return new ResponseEntity<WidgetDTO>(HttpStatus.NO_CONTENT);
        } catch (WidgetNotFoundException e) {
            throw new MiroRestException(ErrorCode.WIDGET_NOT_FOUND);
        }
    }

    /**
     * Gets all the widgets created sorted by z-index.
     * 
     * @param page       Page number for pagination
     * @param pageSize   Maximum number of items in a page
     * @param uriBuilder {@link UriBuilder}
     * @param response   {@link HttpServletResponse}
     * @param x1         Spatial search x1 param
     * @param y1         Spatial search y1 param
     * @param x2         Spatial search x2 param
     * @param y2         Spatial search y2 param
     * @return {@link ResponseEntity}
     * @throws MiroRestException          If page not found
     * @throws RateLimitExceededException If request rate limit is exceeded
     */
    @RequestMapping(method = { RequestMethod.GET })
    @ApiOperation(value = "Gets all the widgets created sorted by z-index", notes = "No parameter required")
    @ApiResponses({ @ApiResponse(code = SC_OK, response = WidgetDTO.class, message = "Widgets returned successfully"),
            @ApiResponse(code = SC_BAD_REQUEST, response = ApiError.class, message = "Page not found"),
            @ApiResponse(code = SC_INTERNAL_SERVER_ERROR, message = "Internal server error occurred while processing your request") })
    public ResponseEntity<WidgetDTO[]> getAllWidgets(@RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, UriComponentsBuilder uriBuilder,
            HttpServletResponse response,
            @ApiParam("Spatial search x1 param") @RequestParam(value = "x1", required = false) Integer x1,
            @ApiParam("Spatial search y1 param") @RequestParam(value = "y1", required = false) Integer y1,
            @ApiParam("Spatial search x2 param") @RequestParam(value = "x2", required = false) Integer x2,
            @ApiParam("Spatial search y2 param") @RequestParam(value = "y2", required = false) Integer y2)
            throws MiroRestException, RateLimitExceededException {
        try {
            addRateLimitHeaders(response, getAllWidgetsRateLimiter.acquire());
            addRateLimitHeaders(response, globalWidgetRateLimiter.acquire());
            checkArgument(pageSize <= Integer.parseInt(maxPageSize) && pageSize > 0,
                    "Page size should be between 1 and " + maxPageSize);
            PageData<WidgetEntity> pagedData = null;
            if (x1 == null || x2 == null || y1 == null || y2 == null) {
                // normal query
                pagedData = widgetService.getAllWidgets(page, pageSize);
            } else {
                // spatial query
                pagedData = widgetService.getAllWidgets(page, pageSize, x1, y1, x2, y2);
            }
            LinkUtil.addLinkHeaderOnPagedResourceRetrieval(uriBuilder, response, "widgets", pagedData.getPage(),
                    pagedData.getMaxPages(), pagedData.getPageSize());
            return new ResponseEntity<WidgetDTO[]>(
                    WidgetFromDomain.convertWidgets(pagedData.getWidgetEntities(), conversionService), HttpStatus.OK);
        } catch (PageNotFoundException e) {
            throw new MiroRestException(ErrorCode.PAGE_NOT_FOUND);
        }
    }

    private void addRateLimitHeaders(HttpServletResponse response, RateLimit rateLimit) {
        response.addHeader(rateLimit.getLimiterId() + ".nextResetInMilliSeconds",
                rateLimit.getNextResetInMilliSeconds().toString());
        response.addHeader(rateLimit.getLimiterId() + ".apiremainingRequests",
                rateLimit.getRemainingRequests().toString());
        response.addHeader(rateLimit.getLimiterId() + ".maxRequestsInPeriod",
                rateLimit.getMaxRequestsInPeriod().toString());
        response.addHeader(rateLimit.getLimiterId() + ".periodInSeconds", rateLimit.getPeriodInSeconds().toString());
    }
}
