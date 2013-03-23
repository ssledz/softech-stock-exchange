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


/**
 *
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 */
public enum CandleRules implements IRule {
    
    COLOR_RULE(new CandleColorRule()),
    BOTH_SHADOWS_RULE(new CandleBothShadowsRule()),
    LOW_SHADOW_RULE(new CandleLowShadowRule()),
    HIGH_SHADOW_RULE(new CandleHighShadowRule()),
    SEQ_ENGFULS_BODY_RULE(new CandleSeqEngfulsBodyRule()),
    GREEDY_ENGFULS_BODY_RULE(new CandleGreedyEngfulsBodyRule()),
    IS_DOJI_RULE(new CandleIsDojiRule()),
    LONG_BODY_RULE(new CandleLongBodyRule()),
    RELATIVE_LONG_BODY_RULE(new CandleRelativeLongBodyRule()),
    RELATVE_SHORT_HIGH_SHADOW_RULE(new CandleRelativeShortHighShadowRule()),
    RELATVE_SHORT_LOW_SHADOW_RULE(new CandleRelativeShortLowShadowRule()),
    RELATVE_SHORT_BOTH_SHADOWS_RULE(new CandleRelativeShortBothShadowsRule()),
    SEQ_BLOW_RULE(new CandleSeqBelowRule()),
    SEQ_ABOVE_RULE(new CandleSeqAboveRule());
    
    
    private final IRule rule;
    
    private CandleRules(IRule rule) {
        this.rule = rule;
    }

    @Override
    public boolean match(Candle[] pattern, Candle[] candles) {
        return rule.match(pattern, candles);
    }

}
