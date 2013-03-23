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
package pl.softech.stockexchange.candle.pattern.impl;

import pl.softech.stockexchange.candle.Candle;
import pl.softech.stockexchange.candle.pattern.ICandlePattern;
import pl.softech.stockexchange.model.FInstrument;

/**
 * This pattern is a two bar pattern where the first bar is black and the second
 * bar is white. The second white bar engulfs the black bar. This means it is a
 * bigger bar from top to bottom. On the first day, the sellers have control of
 * the stock. As the first candle is only small, this shows that the sellers
 * only have a loose grip on the stock. The second bar shows a significant white
 * candle showing that the buyers have taken control of the stock. This can
 * often be a strong reversal signal.
 * 
 * http://www.howtotradestocks.org/candlestick-patterns.html
 * http://www.hotcandlestick.com/candles.htm
 * http://www.fxwords.com/b/bullish-engulfing-candlestick.html
 * 
 * Objecie Hossy
 * 
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 * 
 */
public class BullishEngulfing implements ICandlePattern {

    @Override
    public int getBarCount() {
        return 2;
    }

    @Override
    public Type getType() {
        return Type.REVERSAL;
    }

    @Override
    public Direction getDirection() {
        return Direction.BULLISH;
    }

    @Override
    public boolean test(FInstrument[] finstruments) {

        Candle current = new Candle(finstruments[finstruments.length - 1]);
        Candle last = new Candle(finstruments[finstruments.length - 2]);

        if (current.isWhite() && current.hasBothShadows() && current.engulfsBody(last)) {

            if (last.isBlack() && last.hasBothShadows()) {

                FInstrument fcurr = current.instrument();
                FInstrument flast = last.instrument();

                if (flast.getLow() > fcurr.getLow() && flast.getHigh() < fcurr.getHigh()) {

                    return true;

                }

            }

        }

        return false;
    }

    @Override
    public Reliability getReliability() {
        return Reliability.MODERATE;
    }

}
