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
 * http://www.fxwords.com/b/bullish-breakaway-candlestick.html
 * 
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 */
public class BullishBreakaway implements ICandlePattern {

    @Override
    public Type getType() {
        return Type.REVERSAL;
    }

    @Override
    public int getBarCount() {
        return 5;
    }

    @Override
    public Direction getDirection() {
        return Direction.BULLISH;
    }

    @Override
    public Reliability getReliability() {
        return Reliability.MODERATE;
    }

    @Override
    public boolean test(FInstrument[] finstruments) {
        
        Candle[] candlesticks = Candle.create(finstruments, getBarCount());
        
        if(candlesticks[0].isBlack() && candlesticks[0].hasRelativeLongBody(candlesticks)) {
            
            Candle last = candlesticks[0];
            for(int i = 1; i < candlesticks.length - 1; i++) {
                
                if(!candlesticks[i].isBlack()) {
                    return false;
                }
                
                if(last.instrument().getClose() <= candlesticks[i].instrument().getClose()) {
                    return false;
                }
                
                if(candlesticks[0].getBodyLen() < candlesticks[i].getBodyLen()) {
                    return false;
                }
                
                last = candlesticks[i];
            }
            
            last = candlesticks[candlesticks.length - 1];
            
            if(last.isWhite() && last.hasRelativeLongBody(candlesticks)) {
                
                double close = last.instrument().getClose();
                
                if(candlesticks[0].intersectBody(close) || candlesticks[1].intersectBody(close)) {
                    return true;
                }
            }
        }
        
        return false;
    }

}
