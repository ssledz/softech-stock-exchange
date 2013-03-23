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
 * http://www.fxwords.com/b/bullish-belt-hold.html 
 * 
 * http://www.investopedia.com/terms/b/bullishbelthold.asp
 * 
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 */
public class BullishBeltHold implements ICandlePattern {

    @Override
    public Type getType() {
        return Type.REVERSAL;
    }

    @Override
    public int getBarCount() {
        return 1;
    }

    @Override
    public Direction getDirection() {
        return Direction.BULLISH;
    }

    @Override
    public Reliability getReliability() {
        return Reliability.WEAK;
    }

    @Override
    public boolean test(FInstrument[] finstruments) {
        
        Candle[] candlesticks = Candle.create(finstruments, 3);
        
        if(candlesticks[2].isWhite() && !candlesticks[2].hasLowShadow() && candlesticks[2].hasHighShadow()) {
            
            Candle last = candlesticks[0]; 
            for(int i = 1; i < candlesticks.length - 1; i++) {
                if(!candlesticks[i].isAbove(last)) {
                    return false;
                }
                last = candlesticks[i];
            }
            return true;
            
        }
        
        return false;
    }

}
