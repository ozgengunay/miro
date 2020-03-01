package com.ozgen.miro.domain;

public class PageData<T> {
    private final WidgetEntity[] widgetEntities;
    private final int maxPages;
    private final int pageSize;
    private final int page;

    public PageData(WidgetEntity[] widgetEntities, int page, int maxPages, int pageSize) {
        this.widgetEntities = widgetEntities;
        this.page = page;
        this.maxPages = maxPages;
        this.pageSize = pageSize;
    }

    public WidgetEntity[] getWidgetEntities() {
        return widgetEntities;
    }

    public int getMaxPages() {
        return maxPages;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPage() {
        return page;
    }
}
