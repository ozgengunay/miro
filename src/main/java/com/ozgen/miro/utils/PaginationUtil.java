package com.ozgen.miro.utils;

import java.util.Arrays;

import com.ozgen.miro.domain.PageData;
import com.ozgen.miro.domain.PageNotFoundException;
import com.ozgen.miro.domain.WidgetEntity;

public final class PaginationUtil {
    private PaginationUtil() {
        throw new AssertionError();
    }

    public static PageData<WidgetEntity> paginate(int page, int pageSize, WidgetEntity[] widgetEntities)
            throws PageNotFoundException {
        // For size=10: 0,10,20
        int totalPages = widgetEntities.length / pageSize;
        if (page > totalPages)
            throw new PageNotFoundException("Page " + page + " doesn't exist");
        int beginIndex = page * pageSize > (widgetEntities.length - 1) ? 0 : page * pageSize;
        // For size=10 : 9,19,29
        int endIndex = beginIndex + pageSize > widgetEntities.length ? widgetEntities.length : beginIndex + pageSize;
        // sorts by z-index
        Arrays.sort(widgetEntities);
        WidgetEntity[] pagedEntities = Arrays.copyOfRange(widgetEntities, beginIndex, endIndex);
        return new PageData<WidgetEntity>(pagedEntities, page, totalPages, pageSize);
    }
}
