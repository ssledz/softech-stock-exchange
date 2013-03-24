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
package pl.softech.stockexchange.candle.xml;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import pl.softech.stockexchange.candle.Candle;
import pl.softech.stockexchange.candle.pattern.CandlestickPatternMetaDataImpl;
import pl.softech.stockexchange.candle.pattern.ICandlePatternMetaData;
import pl.softech.stockexchange.candle.pattern.impl2.XmlCandlePattern;
import pl.softech.stockexchange.model.FInstrument;
import pl.softech.stockexchange.model.PropertyName;

/**
 *
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 */
public class XmlDefinitionLoader {

    private ICandlePatternMetaData metaData;
    private Candle[] candles;

    public XmlDefinitionLoader load(File file) throws Exception {

        BufferedInputStream in = null;

        try {
            in = new BufferedInputStream(new FileInputStream(file));
            loadXml(in);
        } finally {
            if(in != null) {
                in.close();
            }
        }

        return this;

    }
    
    public XmlDefinitionLoader load(InputStream in) throws Exception {
        loadXml(in);
        return this;
    }

    public Candle[] getCandles() {
        return candles;
    }
    

    protected void loadXml(InputStream in) throws Exception {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(in);

        Element element = document.getDocumentElement();

        if (!XmlCandlePattern.ROOT_ELEMENT_NAME.equals(element.getNodeName())) {
            throw new Exception("No" + XmlCandlePattern.ROOT_ELEMENT_NAME + " root element");
        }

        metaData = readMetaData(element);

        candles = new Candle[metaData.getBarCount()];

        int index = 0;
        for (Node node = element.getFirstChild(); node != null; node = node.getNextSibling()) {
            if (node.hasAttributes()) {
                Candle c = readCandlestick(node);
                candles[index++] = c;
            }
        }

    }


    public ICandlePatternMetaData getMetaData() {
        return metaData;
    }

    protected Method getSetterForGetter(Class<?> clazz, Method getter) throws Exception {

        String name = "set".concat(getter.getName().substring("get".length()));

        return clazz.getMethod(name, getter.getReturnType());

    }

    protected Candle readCandlestick(Node node) throws Exception {

        FInstrument fin = new FInstrument();

        NamedNodeMap nnm = node.getAttributes();

        for (Method m : FInstrument.class.getMethods()) {

            if (m.getParameterTypes().length > 0) {
                continue;
            }

            PropertyName meta = m.getAnnotation(PropertyName.class);

            if (meta == null) {
                continue;
            }

            String att = nnm.getNamedItem(meta.name()).getNodeValue();

            if (m.getReturnType() == float.class) {
                float value = Float.parseFloat(att);
                Method setter = getSetterForGetter(FInstrument.class, m);
                setter.invoke(fin, value);
            }

        }

        return new Candle(fin);
        
        
    }
    
    
    private ICandlePatternMetaData readMetaData(Element element) throws Exception {
        CandlestickPatternMetaDataImpl ret = new CandlestickPatternMetaDataImpl();

        for (Method m : ICandlePatternMetaData.class.getMethods()) {

            if (m.getParameterTypes().length > 0) {
                continue;
            }

            PropertyName meta = m.getAnnotation(PropertyName.class);
            if (meta == null) {
                continue;
            }

            String att = element.getAttribute(meta.name());
            if (m.getReturnType().isEnum()) {

                for (Object value : m.getReturnType().getEnumConstants()) {

                    if (att.equals(value.toString())) {
                        Method setter = getSetterForGetter(CandlestickPatternMetaDataImpl.class, m);
                        setter.invoke(ret, value);
                    }

                }

            } else if (m.getReturnType() == int.class) {

                int num = Integer.parseInt(att);
                Method setter = getSetterForGetter(CandlestickPatternMetaDataImpl.class, m);
                setter.invoke(ret, num);

            }

        }

        return ret;
    }

}
