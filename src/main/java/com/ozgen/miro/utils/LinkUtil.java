package com.ozgen.miro.utils;

import java.util.StringJoiner;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.util.UriComponentsBuilder;

import com.google.common.net.HttpHeaders;

/**
 * Provides some constants and utility methods to build a Link Header to be stored in the {@link HttpServletResponse}
 * object
 */
public final class LinkUtil {
    public static final String REL_NEXT = "next";
    public static final String REL_PREV = "prev";
    public static final String REL_FIRST = "first";
    public static final String REL_LAST = "last";
    private static final String PAGE = "page";

    private LinkUtil() {
        throw new AssertionError();
    }

    /**
     * @param uriBuilder   {@link UriComponentsBuilder}
     * @param response     {@link UriComponentsBuilder}
     * @param resourceName REST API resource name
     * @param page         current page
     * @param totalPages   total number of pages
     * @param pageSize     number of items in a page
     */
    public static void addLinkHeaderOnPagedResourceRetrieval(UriComponentsBuilder uriBuilder,
            HttpServletResponse response, String resourceName, int page, int totalPages, int pageSize) {
        uriBuilder.path("/" + resourceName);
        StringJoiner linkHeader = new StringJoiner(", ");
        if (hasNextPage(page, totalPages)) {
            String uriForNextPage = constructNextPageUri(uriBuilder, page, pageSize);
            linkHeader.add(LinkUtil.createLinkHeader(uriForNextPage, LinkUtil.REL_NEXT));
        }
        if (hasPreviousPage(page)) {
            String uriForPrevPage = constructPrevPageUri(uriBuilder, page, pageSize);
            linkHeader.add(LinkUtil.createLinkHeader(uriForPrevPage, LinkUtil.REL_PREV));
        }
        if (hasFirstPage(page)) {
            String uriForFirstPage = constructFirstPageUri(uriBuilder, pageSize);
            linkHeader.add(LinkUtil.createLinkHeader(uriForFirstPage, LinkUtil.REL_FIRST));
        }
        if (hasLastPage(page, totalPages)) {
            String uriForLastPage = constructLastPageUri(uriBuilder, totalPages, pageSize);
            linkHeader.add(LinkUtil.createLinkHeader(uriForLastPage, LinkUtil.REL_LAST));
        }
        if (linkHeader.length() > 0) {
            response.addHeader(HttpHeaders.LINK, linkHeader.toString());
        }
    }

    private static String constructNextPageUri(UriComponentsBuilder uriBuilder, int page, int size) {
        return uriBuilder.replaceQueryParam(PAGE, page + 1).replaceQueryParam("size", size).build().encode()
                .toUriString();
    }

    private static String constructPrevPageUri(UriComponentsBuilder uriBuilder, int page, int size) {
        return uriBuilder.replaceQueryParam(PAGE, page - 1).replaceQueryParam("size", size).build().encode()
                .toUriString();
    }

    private static String constructFirstPageUri(UriComponentsBuilder uriBuilder, int size) {
        return uriBuilder.replaceQueryParam(PAGE, 0).replaceQueryParam("size", size).build().encode().toUriString();
    }

    private static String constructLastPageUri(UriComponentsBuilder uriBuilder, int totalPages, int size) {
        return uriBuilder.replaceQueryParam(PAGE, totalPages).replaceQueryParam("size", size).build().encode()
                .toUriString();
    }

    private static boolean hasNextPage(int page, int totalPages) {
        return page < totalPages;
    }

    private static boolean hasPreviousPage(int page) {
        return page > 0;
    }

    private static boolean hasFirstPage(int page) {
        return hasPreviousPage(page);
    }

    private static boolean hasLastPage(int page, int totalPages) {
        return hasNextPage(page, totalPages);
    }

    /**
     * Creates a Link Header to be stored in the {@link HttpServletResponse}
     * 
     * @param uri the base uri
     * @param rel the relative path
     * 
     * @return the complete url
     */
    private static String createLinkHeader(String uri, String rel) {
        return "<" + uri + ">; rel=\"" + rel + "\"";
    }
}
