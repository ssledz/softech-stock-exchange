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


import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 */
public class FileDownloadMonitor {

	public interface FileDownloadListener {
		
		public void actionPerformed(FileDownloadEvent event);
		
	}
	
	public static class FileDownloadEvent {
		
		public enum Type { MESSAGE, BYTES_RECIEVED, START};
		
		public String message;
		public int bytes;
		public long size;
		public Type type;
		public FileDownloadEvent(String message) {
			this.message = message;
			this.type = Type.MESSAGE;
		}
		public FileDownloadEvent(int bytes) {
			this.bytes = bytes;
			this.type = Type.BYTES_RECIEVED;
		}
		public FileDownloadEvent(String message, Type type, long size) {
			this.message = message;
			this.type = type;
			this.size = size;
		}
	}
	
	
	private final List<FileDownloadListener> listeners;

	public FileDownloadMonitor() {
		this.listeners = new LinkedList<FileDownloadListener>();
	}
	
	public void fireEvent(FileDownloadEvent event) {
		
		for(FileDownloadListener l : listeners) {
			l.actionPerformed(event);
		}
		
	}
	
	public void add(FileDownloadListener l) {
		listeners.add(l);
	}
	
	public void remove(FileDownloadListener l) {
		listeners.remove(l);
	}
	
}
