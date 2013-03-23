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
import pl.softech.stockexchange.candle.editor.PaletteCandelstickWidgets;
import pl.softech.stockexchange.candle.editor.dad.CWidgetDropEvent;
import pl.softech.stockexchange.candle.editor.dad.CWidgetDropListener;
import pl.softech.stockexchange.model.FIHelper;

/**
 *
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 */
public class CWidgetMotionListenerImpl implements CWidgetMoveListener, CWidgetDropListener {

    private final CandlestickScene scene;
    private double point2pixel;
    
    public CWidgetMotionListenerImpl(CandlestickScene scene) {
        this.scene = scene;
        this.point2pixel = PaletteCandelstickWidgets.point2pixel();
    }

    @Override
    public void movePerformed(CWidgetMoveEvent event) {
        
        CandlestickWidget widget = event.getWidget();
        Point[] disp = event.getDisp();
        double len = (disp[1].getY() - disp[0].getY()) * point2pixel;
        FIHelper.add(widget.getCandlestick().instrument(), -len);
    }

    @Override
    public void dropPerformed(CWidgetDropEvent event) {
        
        CandlestickWidget widget = event.getWidget();
        Point loc = widget.getWidget().getLocation();
        Rectangle sbounds = scene.getScene().getBounds();
        Rectangle wbounds = widget.getWidget().getBounds();
        
        double len = (sbounds.getHeight() - loc.y - wbounds.getHeight()) * point2pixel;
        FIHelper.add(widget.getCandlestick().instrument(), len);
    }

}
