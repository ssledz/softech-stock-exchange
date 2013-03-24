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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import pl.softech.stockexchange.candle.Candle;
import pl.softech.stockexchange.candle.pattern.CandlePattern;
import pl.softech.stockexchange.candle.pattern.ICandlePattern;
import pl.softech.stockexchange.candle.pattern.ICandlePatternMetaData;
import pl.softech.stockexchange.candle.xml.XmlDefinitionLoader;
import pl.softech.stockexchange.model.FInstrument;

/**
 *
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 */
public class XmlCandlePattern implements ICandlePattern {

    public static final String ROOT_ELEMENT_NAME = "candlestick-pattern";
    public static final String CANDLESTICK_ELEMENT_NAME = "candlestick";
    
    private final ICandlePatternMetaData metaData;
    private final Candle[] pattern;

    private static PatternRecognizer patternRecognizer = new PatternRecognizer();

    private XmlCandlePattern(ICandlePatternMetaData metaData, Candle[] pattern) {
        this.metaData = metaData;
        this.pattern = pattern;
    }

    @Override
    public Type getType() {
        return metaData.getType();
    }

    @Override
    public int getBarCount() {
        return pattern.length;
    }

    @Override
    public Direction getDirection() {
        return metaData.getDirection();
    }

    @Override
    public Reliability getReliability() {
        return metaData.getReliability();
    }

    @Override
    public boolean test(FInstrument[] finstruments) {

        return patternRecognizer.test(pattern, finstruments);

    }

    public static XmlCandlePattern create(String fileName) {

        XmlCandlePattern pattern = null;

        try {
            XmlDefinitionLoader loader = new XmlDefinitionLoader();
            loader.load(XmlCandlePattern.class.getResourceAsStream(fileName));
            pattern = new XmlCandlePattern(loader.getMetaData(), loader.getCandles());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pattern;
    }

    public static void main(String[] args) {

        XmlCandlePattern pattern = XmlCandlePattern.create("bullish-engulfing-pattern.xml");

        FInstrument[] instruments = new FInstrument[pattern.pattern.length];
        for (int i = 0; i < instruments.length; i++) {
            instruments[i] = pattern.pattern[i].instrument();
        }

        System.out.println((CandlePattern.BULLISH_ENGULFING.test(instruments) ? "OK" : "ERROR") + "\tIS PATTERN BULLISH_ENGULFING");
        System.out.println((CandlePattern.BULLISH_ENGULFING_XML.test(instruments) ? "OK" : "ERROR") + "\tIS PATTERN BULLISH_ENGULFING_XML");

        List<FInstrument> tmp = new LinkedList<FInstrument>(Arrays.asList(instruments));
        tmp.addAll(Arrays.asList(instruments));
        tmp.addAll(Arrays.asList(instruments));

        instruments = tmp.toArray(new FInstrument[0]);
        System.out.println((CandlePattern.BULLISH_RISING_TREE_METHODS_XML.test(instruments) ? "ERROR" : "OK") + "\tIS NOT BULLISH_RISING_TREE_METHODS_XML");
        System.out.println((CandlePattern.BULLISH_RISING_TREE_METHODS.test(instruments) ? "ERROR" : "OK") + "\tIS NOT BULLISH_RISING_TREE_METHODS");

        pattern = XmlCandlePattern.create("bullish-rising-three-methods-pattern.xml");

        instruments = new FInstrument[pattern.pattern.length];
        for (int i = 0; i < instruments.length; i++) {
            instruments[i] = pattern.pattern[i].instrument();
        }

        System.out.println((CandlePattern.BULLISH_RISING_TREE_METHODS.test(instruments) ? "OK" : "ERROR") + "\tIS BULLISH_RISING_TREE_METHODS");
        System.out.println((CandlePattern.BULLISH_RISING_TREE_METHODS_XML.test(instruments) ? "OK" : "ERROR") + "\tIS BULLISH_RISING_TREE_METHODS_XML");
    }

}
