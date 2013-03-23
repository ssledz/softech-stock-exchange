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
package pl.softech.stockexchange.transaction;

import java.util.Date;
import pl.softech.math.stat.Average;
import pl.softech.stockexchange.candle.pattern.CandlePattern;
import pl.softech.stockexchange.candle.pattern.ICandlePattern;
import pl.softech.stockexchange.candle.pattern.ICandlePatternMetaData.Reliability;
import pl.softech.stockexchange.candle.pattern.ICandlePatternMetaData.Type;
import pl.softech.stockexchange.index.Adx;
import pl.softech.stockexchange.model.FIHelper;
import pl.softech.stockexchange.model.FInstrument;

/**
 *
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 */
public class SimpleTransactionSystem implements ITestable {

    private FInstrument[] instruments;

    private int nAdx;
    private int nSlowSma;
    private int nFastSma;

    private double[] low;
    private double[] high;
    private double[] close;

    private double[] adx;
    private double[] smaSlow;
    private double[] smaFast;

    private int cursor;

    private SimpleTransactionSystem(FInstrument[] instruments, int nAdx, int nSlowSma, int nFastSma) {
        this.instruments = instruments;
        this.nAdx = nAdx;
        this.nSlowSma = nSlowSma;
        this.nFastSma = nFastSma;
        init();
    }

    public SimpleTransactionSystem(FInstrument[] instruments) {
        this(instruments, 45, 100, 15);

    }

    @Override
    public String toString() {
        return instruments[0].getName();
    }

    private void init() {
        low = FIHelper.toLow(instruments);
        high = FIHelper.toHigh(instruments);
        close = FIHelper.toClose(instruments);

        adx = new Adx().count(low, high, close, nAdx);
        smaSlow = Average.sma(close, nSlowSma);
        smaFast = Average.sma(close, nFastSma);

        cursor = instruments.length - 1;

    }

    @Override
    public boolean start() {
        cursor = Math.max(nAdx, Math.max(nSlowSma, nFastSma));
        cursor = Math.min(cursor, instruments.length - 1);
        return cursor < instruments.length;
    }

    @Override
    public Date getDate() {
        return instruments[cursor].getDate();
    }

    public boolean next() {

        if (cursor < instruments.length - 1) {

            cursor++;
            return true;

        }

        return false;
    }

    @Override
    public Signal emit() {

        if (adx[cursor] >= 20) {

            if (close[cursor] > smaSlow[cursor]) {

                for (ICandlePattern p : CandlePattern.bullish()) {
                    if (p.getType() == Type.CONTINUATION && p.test(instruments)) {
                            return Signal.BUY;
                    }
                }

            }
        }

        if (smaFast[cursor] > close[cursor] && smaFast[cursor - 1] < close[cursor - 1]) {

            for (ICandlePattern p : CandlePattern.bullish()) {
                if (p.getType() == Type.REVERSAL && p.test(instruments)) {
                    if (p.getReliability() == Reliability.STRONG || p.getReliability() == Reliability.MODERATE) {
//                        return Signal.BUY;
                    }
                }
            }

            return Signal.SELL;
        }

        return Signal.NONE;
    }

    @Override
    public double getOpen() {
        return instruments[cursor].getOpen();
    }

    @Override
    public double getClose() {
        return instruments[cursor].getClose();
    }

}
