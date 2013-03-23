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
package pl.softech.stockexchange.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.LinkedList;
import pl.softech.stockexchange.model.FInstrument;

/**
 *
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 */
public class BossaParser {
	
	private DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd"); 
	
	private final BufferedReader getBufferedReader(InputStream in) throws IOException {
		return new BufferedReader(new InputStreamReader(in));
	}
	
	private boolean isHeadLine(String line) {
		String[] columns = line.split(",");
		return "<TICKER>".equalsIgnoreCase(columns[0]);
	}
	
	private final FInstrument parseLine(String line) throws ParseException {
		String[] columns = line.split(",");
		FInstrument ret = new FInstrument();
		try {
			ret.setName(columns[0]);
			ret.setDate(DATE_FORMAT.parse(columns[1]));
			ret.setOpen(Float.parseFloat(columns[2]));
			ret.setHigh(Float.parseFloat(columns[3]));
			ret.setLow(Float.parseFloat(columns[4]));
			ret.setClose(Float.parseFloat(columns[5]));
			ret.setVolume((int) Float.parseFloat(columns[6]));
		} catch (Exception e) {
			throw new ParseException(ret.toString(), e);
		}
		return ret;
	}
	
	public Collection<FInstrument> getInstruments(InputStream in) throws ParseException, IOException {
		BufferedReader bin = null;
		Collection<FInstrument> ret = new LinkedList<FInstrument>();
		try {
			
			bin = getBufferedReader(in);
			String line;
			while((line = bin.readLine()) != null) {
				if(isHeadLine(line)) {
					continue;
				}
				ret.add(parseLine(line));
			}
			
			return ret;
			
		} finally {
			
			in.close();
			
		}
	}
	
}
