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
 * http://www.fxwords.com/b/bullish-abandoned-baby-candlestick.html 
 * 
 * • Day-one is a red day continuing an established bear trend.
 * • Day-two is a doji whose shadows trades below day-ones close.
 * • Day-three is a blue day that opens and trades above with little or no overlapping shadows
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 */
public class BullishAbandonedBaby implements ICandlePattern {

    @Override
    public Type getType() {
        return Type.REVERSAL;
    }

    @Override
    public int getBarCount() {
        return 3;
    }

    @Override
    public Direction getDirection() {
        return Direction.BULLISH;
    }

    @Override
    public Reliability getReliability() {
        return Reliability.STRONG;
    }

    @Override
    public boolean test(FInstrument[] finstruments) {
        
        Candle[] candlesticks = Candle.create(finstruments, getBarCount());
        
        if(candlesticks[0].isBlack()) {
            
            if(candlesticks[1].getBodyLen() == 0) {
                
                if(candlesticks[0].instrument().getClose() > candlesticks[1].instrument().getLow()) {
                    
                    if(candlesticks[2].isWhite() && candlesticks[2].isAbove(candlesticks[1])) {
                        return true;
                    } 
                    
                }
                
            }
            
        }
        
        return false;
    }

}
