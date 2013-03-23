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
package pl.softech.stockexchange.index;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import pl.softech.stockexchange.model.FIHelper;
import pl.softech.stockexchange.model.FInstrument;

/**
 * 
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 */
public class AdxTest {

    private static class Data {

        public ArrayList<FInstrument> instruments;
        public ArrayList<Double> tr1;
        public ArrayList<Double> plusDM1;
        public ArrayList<Double> minusDM1;
        public ArrayList<Double> tr14;
        public ArrayList<Double> plusDM14;
        public ArrayList<Double> minusDM14;

        public ArrayList<Double> plusDI14;
        public ArrayList<Double> minusDI14;

        public ArrayList<Double> adx;

        public Data() {

            instruments = new ArrayList<FInstrument>();
            tr1 = new ArrayList<Double>();
            plusDM1 = new ArrayList<Double>();
            minusDM1 = new ArrayList<Double>();
            tr14 = new ArrayList<Double>();
            plusDM14 = new ArrayList<Double>();
            minusDM14 = new ArrayList<Double>();
            plusDI14 = new ArrayList<Double>();
            minusDI14 = new ArrayList<Double>();
            adx = new ArrayList<Double>();

        }
        
        public Double[] getPlusDI14() {
            return plusDI14.toArray(new Double[0]);
        }
        
        public Double[] getMinusDI14() {
            return minusDI14.toArray(new Double[0]);
        }
        
        public Double[] getTr14() {
            return tr14.toArray(new Double[0]);
        }
        
        public Double[] getAdx() {
            return adx.toArray(new Double[0]);
        }

        public void add(String high, String low, String close) {
            FInstrument instrument = new FInstrument();
            instrument.setHigh(Float.parseFloat(high));
            instrument.setLow(Float.parseFloat(low));
            instrument.setClose(Float.parseFloat(close));
            instruments.add(instrument);
        }

        private Double parseDouble(String arg) {
            if (arg.length() == 0) {
                return Double.NaN;
            }
            return Double.parseDouble(arg);
        }

        public void add(String[] args, int from) {

            @SuppressWarnings("unchecked")
            List<Double>[] tab = new List[] { tr1, plusDM1, minusDM1, tr14, plusDM14, minusDM14, plusDI14, minusDI14 };

            int j = 0;
            for (int i = from; i < args.length && j < tab.length; i++, j++) {

                tab[j].add(parseDouble(args[i]));

            }

            for (int i = j; i < tab.length; i++) {
                tab[i].add(parseDouble(""));
            }

        }

        public void add(String[] args) {
            if (args.length < 17) {
                this.adx.add(parseDouble(""));
            } else {

                this.adx.add(parseDouble(args[args.length - 1]));
            }
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < instruments.size(); i++) {
                builder.append(instruments.get(i).getHigh()).append("#");
                builder.append(instruments.get(i).getLow()).append("#");
                builder.append(instruments.get(i).getClose()).append("#");
                builder.append(adx.get(i)).append("#");
                builder.append("\n");
            }
            return builder.toString();
        }

    }

    private static Data parseFile() throws Exception {

        BufferedReader in = new BufferedReader(new InputStreamReader(
                AdxTest.class.getResourceAsStream("adx-sample.txt")));

        String line;
        Data dataa = new Data();
        try {
            in.readLine();
            in.readLine();
            while ((line = in.readLine()) != null) {

                String[] colls = line.split("#");
                int i = 1;
                dataa.add(colls[++i], colls[++i], colls[++i]);
                dataa.add(colls, ++i);
                dataa.add(colls);

            }
        } finally {
            in.close();
        }

        return dataa;
    }

    private static Data data;
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        data = parseFile();
    }
    
    @Test
    public void testCount() throws Exception {
        
        double[] low = FIHelper.toLow(data.instruments);
        double[] high = FIHelper.toHigh(data.instruments);
        double[] close = FIHelper.toClose(data.instruments);
        
        Adx adx = new Adx();
        double[] _adx = adx.count(low, high, close, 14);
        
        Double[] adxExpected = data.getAdx();
        Double[] plusDI14Expected = data.getPlusDI14();
        Double[] minusDI14Expected = data.getMinusDI14();
        Double[] tr14Expected = data.getTr14();
        
        for(int i = 0; i < adxExpected.length; i++) {
            
            double myAdx = _adx[i];
            double myplusDI14 = adx.getPlusDI()[i];
            double myminusDI14 = adx.getMinusDI()[i];
            double myTr14 = adx.getTr()[i];
            
            double expectedAdx = adxExpected[i].doubleValue();
            double expectedPlusDI14 = plusDI14Expected[i].doubleValue(); 
            double expectedMinusDI14 = minusDI14Expected[i].doubleValue();
            double expectedTr14 = tr14Expected[i].doubleValue();
            
            double delta = 0.001;
            Assert.assertEquals(expectedAdx, myAdx, delta);
            Assert.assertEquals(expectedPlusDI14, myplusDI14, delta);
            Assert.assertEquals(expectedMinusDI14, myminusDI14, delta);
            Assert.assertEquals(expectedTr14, myTr14, delta);
        }
        
        
    }

}
