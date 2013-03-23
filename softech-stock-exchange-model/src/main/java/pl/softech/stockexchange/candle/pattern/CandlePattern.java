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
package pl.softech.stockexchange.candle.pattern;

import java.util.ArrayList;
import java.util.List;
import pl.softech.stockexchange.candle.pattern.impl.BullishAbandonedBaby;
import pl.softech.stockexchange.candle.pattern.impl.BullishBeltHold;
import pl.softech.stockexchange.candle.pattern.impl.BullishBreakaway;
import pl.softech.stockexchange.candle.pattern.impl.BullishDojiStar;
import pl.softech.stockexchange.candle.pattern.impl.BullishEngulfing;
import pl.softech.stockexchange.candle.pattern.impl.BullishRisingThreeMethods;
import pl.softech.stockexchange.candle.pattern.impl.BullishThreeLineStrike;
import pl.softech.stockexchange.candle.pattern.impl2.XmlCandlePattern;
import pl.softech.stockexchange.model.FInstrument;

/**
 *
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 */
public enum CandlePattern implements ICandlePattern {

    BULLISH_ENGULFING(new BullishEngulfing()), //
    BULLISH_RISING_TREE_METHODS(new BullishRisingThreeMethods()), //
    BULLISH_RISING_TREE_METHODS_XML(XmlCandlePattern.create("bullish-rising-three-methods-pattern.xml")), //
    BULLISH_THREE_LINE_STRIKE(new BullishThreeLineStrike()),//
    BULLISH_ABANDONED_BABY(new BullishAbandonedBaby()),//
    BULLISH_BELT_HOLD(new BullishBeltHold()),//
    BULLISH_BREAKAWAY(new BullishBreakaway()),//
    BULLISH_DOJI_STAR(new BullishDojiStar()),
    
    BULLISH_ENGULFING_XML(XmlCandlePattern.create("bullish-engulfing-pattern.xml"));

    private final ICandlePattern pattern;

    private static ICandlePattern[] bullish;
    private static ICandlePattern[] bearish;

    private CandlePattern(ICandlePattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean test(FInstrument[] finstruments) {
        return pattern.test(finstruments);
    }

    @Override
    public Type getType() {
        return pattern.getType();
    }

    @Override
    public int getBarCount() {
        return pattern.getBarCount();
    }

    @Override
    public Direction getDirection() {
        return pattern.getDirection();
    }

    @Override
    public Reliability getReliability() {
        return pattern.getReliability();
    }

    private static void init() {
        List<ICandlePattern> bullishList = new ArrayList<ICandlePattern>();
        List<ICandlePattern> bearishList = new ArrayList<ICandlePattern>();
        for (CandlePattern cp : values()) {
            if (cp.getDirection() == Direction.BULLISH) {

                bullishList.add(cp);

            } else {

                bearishList.add(cp);

            }
        }

        bullish = bullishList.toArray(new ICandlePattern[bullishList.size()]);
        bearish = bearishList.toArray(new ICandlePattern[bearishList.size()]);
    }

    public static ICandlePattern[] bullish() {

        if (bullish != null) {
            return bullish;
        }

        init();

        return bullish;

    }

    public static ICandlePattern[] bearish() {

        if (bearish != null) {
            return bearish;
        }

        init();

        return bearish;

    }

}
