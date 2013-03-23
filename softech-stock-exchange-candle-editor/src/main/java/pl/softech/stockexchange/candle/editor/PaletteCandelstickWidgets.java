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
package pl.softech.stockexchange.candle.editor;

import java.awt.Rectangle;
import pl.softech.stockexchange.candle.Candle;
import pl.softech.stockexchange.model.FInstrument;

/**
 *
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 */
public enum PaletteCandelstickWidgets {
    
    BLACK_BODY(createBlackBody()),
    WHITE_BODY(createWhiteBody()),
    DOJI(createDoji());

    private final CandlestickWidget widget;

    private PaletteCandelstickWidgets(CandlestickWidget widget) {
        this.widget = widget;
    }
    
    public static double point2pixel() {
        CandlestickWidget widget = PaletteCandelstickWidgets.WHITE_BODY.widget;
        Rectangle wbounds =  widget.getWidget().getBounds();
        return widget.getCandlestick().getLen() / wbounds.getHeight();
    }
    
    public CandlestickWidget widget() {
        return widget.clone();
    }
    
    private static CandlestickWidget createBlackBody() {
        FInstrument fInstrument = new FInstrument();
        fInstrument = new FInstrument();
        fInstrument.setLow(10);
        fInstrument.setOpen(50);
        fInstrument.setClose(30);
        fInstrument.setHigh(70);
        Candle widget = new Candle(fInstrument);
        return new CandlestickWidget(widget, null);
    }
    
    private static CandlestickWidget createWhiteBody() {
        FInstrument fInstrument = new FInstrument();
        fInstrument = new FInstrument();
        fInstrument.setLow(10);
        fInstrument.setOpen(30);
        fInstrument.setClose(50);
        fInstrument.setHigh(70);
        Candle widget = new Candle(fInstrument);
        return new CandlestickWidget(widget, null);
    }
    
    private static CandlestickWidget createDoji() {
        FInstrument fInstrument = new FInstrument();
        fInstrument = new FInstrument();
        fInstrument.setLow(10);
        fInstrument.setOpen(40);
        fInstrument.setClose(40);
        fInstrument.setHigh(70);
        Candle widget = new Candle(fInstrument);
        return new CandlestickWidget(widget, null);
    }

}
