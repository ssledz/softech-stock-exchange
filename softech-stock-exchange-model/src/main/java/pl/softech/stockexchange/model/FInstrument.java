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

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 */
public class FInstrument implements Cloneable, Serializable {

    private static final long serialVersionUID = 1L;

//    public class RowView {
//		
//		@TableColumn(name="Date", order=0)   public Date    getDate() { return date; }
//		@TableColumn(name="Name", order=1)   public String  getName() { return name; }
//		@TableColumn(name="Open", order=2)   public Float   getOpen() { return open; }
//		@TableColumn(name="Low", order=3)    public Float   getLow() { return low; }
//		@TableColumn(name="High", order=4)   public Float   getHigh() { return high; }
//		@TableColumn(name="Close", order=5)  public Float   getClose() { return close; }
//		@TableColumn(name="Volume", order=6) public Integer getVolume() { return volume; }
//	}
	
	private String name;
	private Date date;
	private float open;
	private float high;
	private float low;
	private float close;
	private int volume;
	
//	private transient RowView rowView;
	
//	public RowView getRowView() {
//		if(rowView == null)
//			rowView = new RowView();
//		return rowView;
//	}

	@Override
	public String toString() {
		return "FInstrument [name=" + name + ", date=" + date + ", open="
				+ open + ", high=" + high + ", low=" + low + ", close=" + close
				+ ", volume=" + volume + "]";
	}
	
	/* SETERS AND GETERS */
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	@PropertyName(name="open")
	public float getOpen() {
		return open;
	}
	
	public void setOpen(float open) {
		this.open = open;
	}
	
	@PropertyName(name="high")
	public float getHigh() {
		return high;
	}
	public void setHigh(float high) {
		this.high = high;
	}
	
	@PropertyName(name="low")
	public float getLow() {
		return low;
	}
	
	public void setLow(float low) {
		this.low = low;
	}
	
	@PropertyName(name="close")
	public float getClose() {
		return close;
	}
	
	public void setClose(float close) {
		this.close = close;
	}
	public int getVolume() {
		return volume;
	}
	public void setVolume(int volume) {
		this.volume = volume;
	}

    @Override
    public FInstrument clone() {
        FInstrument clone;
        try {
            clone = (FInstrument) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        } 
        
//        clone.rowView = null;
        
        return clone;
    }
	
	
	
}
