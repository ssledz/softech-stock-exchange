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
package pl.softech.stockexchange.model;

import java.util.Collection;

/**
 *
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 */
public class FIHelper {

    public static double[] toLow(Collection<FInstrument> fis) {
        return toLow(fis.toArray(new FInstrument[0]));
    }

    public static double[] toLow(FInstrument[] fis) {
        int size = fis.length;
        double[] ret = new double[size];

        for (int i = 0; i < size; i++) {
            ret[i] = fis[i].getLow();
        }

        return ret;
    }

    public static double[] toHigh(FInstrument[] fis) {
        int size = fis.length;
        double[] ret = new double[size];

        for (int i = 0; i < size; i++) {
            ret[i] = fis[i].getHigh();
        }

        return ret;
    }

    public static double[] toHigh(Collection<FInstrument> fis) {
        return toHigh(fis.toArray(new FInstrument[0]));
    }

    public static double[] toClose(Collection<FInstrument> fis) {
        return toClose(fis.toArray(new FInstrument[0]));
    }

    public static double[] toClose(FInstrument[] fis) {
        int size = fis.length;
        double[] ret = new double[size];

        for (int i = 0; i < size; i++) {
            ret[i] = fis[i].getClose();
        }

        return ret;
    }
    
    public static FInstrument add(FInstrument fin, double add) {
        return add(fin, (float) add);
    }
    
    public static FInstrument add(FInstrument fin, float add) {
        fin.setHigh(fin.getHigh() + add);
        fin.setLow(fin.getLow() + add);
        fin.setOpen(fin.getOpen() + add);
        fin.setClose(fin.getClose() + add);
        return fin;
    }

}
