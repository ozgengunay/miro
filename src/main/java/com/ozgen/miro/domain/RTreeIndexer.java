package com.ozgen.miro.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.Rectangle;

import rx.Observable;

@Component
public class RTreeIndexer {
    private RTree<WidgetEntity, Geometry> rTreeIndex;

    public RTreeIndexer() {
        this.rTreeIndex = RTree.create();
    }

    // rtree library is immutable and gives a new index upon modification, that's why I have to synchronize the
    // modification
    public synchronized void addToRTreeIndex(WidgetEntity widgetEntity) {
        rTreeIndex = rTreeIndex.add(widgetEntity,
                Geometries.rectangle(widgetEntity.getxCoordinate() - widgetEntity.getWidth() / 2,
                        widgetEntity.getyCoordinate() - widgetEntity.getHeight() / 2,
                        widgetEntity.getxCoordinate() + widgetEntity.getWidth() / 2,
                        widgetEntity.getyCoordinate() + widgetEntity.getHeight() / 2));
    }

    // rtree library is immutable and gives a new index upon modification, that's why I have to synchronize the
    // modification
    public synchronized void removeFromRTreeIndex(WidgetEntity widgetEntity) {
        rTreeIndex = rTreeIndex.delete(widgetEntity,
                Geometries.rectangle(widgetEntity.getxCoordinate() - widgetEntity.getWidth() / 2,
                        widgetEntity.getyCoordinate() - widgetEntity.getHeight() / 2,
                        widgetEntity.getxCoordinate() + widgetEntity.getWidth() / 2,
                        widgetEntity.getyCoordinate() + widgetEntity.getHeight() / 2));
    }

    public synchronized Observable<Entry<WidgetEntity, Geometry>> search(Rectangle rectangle) {
        return rTreeIndex.search(rectangle);
    }

    public static WidgetEntity[] rTreeIndexerSearch(RTreeIndexer rTreeIndexer, Integer x1, Integer y1, Integer x2,
            Integer y2) {
        WidgetEntity[] widgetEntities;
        List<WidgetEntity> rTreeWidgetEntities = new ArrayList<WidgetEntity>();
        Rectangle searchRectangle = Geometries.rectangle(x1, y1, x2, y2);
        Iterable<Entry<WidgetEntity, Geometry>> iter = rTreeIndexer.search(searchRectangle).toBlocking().toIterable();
        for (Entry<WidgetEntity, Geometry> entry : iter) {
            if (rect1ContainsRect2(searchRectangle, (Rectangle) entry.geometry())) {
                rTreeWidgetEntities.add(entry.value());
            }
        }
        widgetEntities = rTreeWidgetEntities.toArray(new WidgetEntity[0]);
        return widgetEntities;
    }

    private static boolean rect1ContainsRect2(Rectangle r1, Rectangle r2) {
        return r1.x1() <= r2.x1() && r1.x2() >= r2.x2() && r1.y1() <= r2.y1() && r1.y2() >= r2.y2();
    }
}
