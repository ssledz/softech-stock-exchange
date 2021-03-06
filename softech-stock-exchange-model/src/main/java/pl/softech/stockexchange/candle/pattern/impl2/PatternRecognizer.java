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
package pl.softech.stockexchange.candle.pattern.impl2;

import pl.softech.stockexchange.candle.Candle;
import pl.softech.stockexchange.model.FInstrument;

/**
 *
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 */
public class PatternRecognizer {

    public boolean test(Candle[] pattern, FInstrument[] finstruments) {
        
        Candle[] candles = Candle.create(finstruments, pattern.length);
        
        for(IRule rule : CandleRules.values()) {
            if(!rule.match(candles, candles)) {
                return false;
            }
        }
        
        return true;
    }
    
}
