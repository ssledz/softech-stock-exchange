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
import pl.softech.stockexchange.candle.editor.CandlestickWidget;

/**
 *
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 */
public class CWidgetMoveEvent {

    private final Point[] disp;
    private final CandlestickWidget widget;

    public CWidgetMoveEvent(Point[] dispVector, CandlestickWidget widget) {
        this.disp = dispVector;
        this.widget = widget;
    }

    public Point[] getDisp() {
        return disp;
    }

    public CandlestickWidget getWidget() {
        return widget;
    }

}
