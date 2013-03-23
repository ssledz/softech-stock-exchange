/*
 * Copyright 2013 Sławomir Śledź <slawomir.sledz@sof-tech.pl>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.softech.stockexchange.candle.editor.action;

import java.awt.Point;
import java.awt.Rectangle;
import pl.softech.stockexchange.candle.editor.CandlestickScene;
import pl.softech.stockexchange.candle.editor.CandlestickWidget;

/**
 *
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 */
public class CWidgetMoveHandler {

    private final CandlestickWidget widget;

    private final CandlestickScene scene;

    private Point start;

    public CWidgetMoveHandler(CandlestickWidget widget, CandlestickScene scene) {
        this.widget = widget;
        this.scene = scene;
    }

    public boolean shouldMove(Point click) {

        CWidgetResizeHandler h = widget.getResizeHandler();

        if (h == null) {
            return true;
        }

        if (h.isResizeAction(click)) {
            return false;
        }

        return true;

    }

    public void setStart(Point start) {
        this.start = start;
    }

    public void reset() {
        this.start = null;
    }

    public void move(Point currentLocation) {
        Point[] dispVector = move(start, currentLocation);

        if (scene != null) {
            scene.fireCWidgetMove(new CWidgetMoveEvent(dispVector, widget));
        }
    }

    private double[] includeConstraints(double xdiff, double ydiff) {

        if (scene == null) {
            return new double[] { xdiff, ydiff };
        }

        Rectangle sbounds = scene.getScene().getVisibleRect();

        Rectangle wbounds = widget.getWidget().getBounds();
        
        if(wbounds.x + xdiff < 0 || wbounds.getMaxX() + xdiff > sbounds.getMaxX()) {
            
            xdiff = 0;
        }
        
        if(wbounds.y + ydiff < 0 || wbounds.getMaxY() + ydiff > sbounds.getMaxY()) {
            ydiff = 0;
        }
        
        return new double[] { xdiff, ydiff };

    }

    private Point[] move(Point start, Point end) {

        double[] diff = includeConstraints(end.getX() - start.getX(), end.getY() - start.getY());

        double xdiff = diff[0];
        double ydiff = diff[1];

        Point loc = widget.getWidget().getLocation();
        loc.x += xdiff;
        loc.y += ydiff;
        widget.setLocation(loc);

        this.start = end;

        end.setLocation(start.getX() + xdiff, start.getY() + ydiff);
        
        return new Point[] { start, end };
    }

}
