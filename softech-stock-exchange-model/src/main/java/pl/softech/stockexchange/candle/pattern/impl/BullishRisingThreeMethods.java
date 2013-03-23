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
 * http://www.fxwords.com/b/bullish-rising-three-methods-candlestick.html
 * 
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 */
public class BullishRisingThreeMethods implements ICandlePattern {

    @Override
    public Type getType() {
        return Type.CONTINUATION;
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
        return Reliability.STRONG;
    }

    @Override
    public boolean test(FInstrument[] finstruments) {
        
        Candle[] candlesticks = Candle.create(finstruments, getBarCount());
        
        if(candlesticks[0].isWhite()) {
            
            if(candlesticks[1].isBlack() ) {
                
                
                
                
                for(int i = 2; i < candlesticks.length - 2; i++) {
                    
                    
                    
                    if(!candlesticks[i].isBlack() || !candlesticks[i].isBelow(candlesticks[i - 1])) {
                        
                        return false;
                        
                        
                    }
                    
                }
                
                if(candlesticks[candlesticks.length - 2].instrument().getClose() > candlesticks[0].instrument().getOpen()) {
                    
                    if(candlesticks[candlesticks.length - 1].isWhite()) {
                        
                        double minBody = candlesticks[candlesticks.length - 2].instrument().getClose();
                        double maxBody = candlesticks[1].instrument().getOpen();
                        
                        if(candlesticks[candlesticks.length - 1].getBodyLen() / (maxBody - minBody) > 0.5) {
                            return true;
                        }
                        
                    }
                    
                }
                
            }
        }
        
        
        return false;
    }

}
