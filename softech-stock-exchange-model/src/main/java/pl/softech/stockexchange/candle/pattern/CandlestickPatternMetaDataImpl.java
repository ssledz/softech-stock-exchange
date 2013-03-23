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

import pl.softech.stockexchange.candle.pattern.ICandlePatternMetaData.Direction;
import pl.softech.stockexchange.candle.pattern.ICandlePatternMetaData.Reliability;
import pl.softech.stockexchange.candle.pattern.ICandlePatternMetaData.Type;


/**
 *
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 */
public class CandlestickPatternMetaDataImpl implements ICandlePatternMetaData {

    private Direction direction = Direction.BEARICH;
    private Type type = Type.CONTINUATION;
    private Reliability reliability = Reliability.UNKNOWN;
    private int barCount = 0;

    @Override
    public Type getType() {
        return type;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setReliability(Reliability reliability) {
        this.reliability = reliability;
    }

    @Override
    public int getBarCount() {
        return barCount;
    }

    public void setBarCount(int barCount) {
        this.barCount = barCount;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public Reliability getReliability() {
        return reliability;
    }

    public void derive(ICandlePatternMetaData m) {
        direction = m.getDirection();
        type = m.getType();
        reliability = m.getReliability();
    }

    @Override
    public String toString() {
        return "CandlestickPatternMetaDataImpl [direction=" + direction + ", type=" + type + ", reliability="
                + reliability + ", barCount=" + barCount + "]";
    }

}
