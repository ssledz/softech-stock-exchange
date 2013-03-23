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


import java.util.logging.Logger;
import pl.softech.stockexchange.dao.FileDownloadMonitor.FileDownloadEvent;
import pl.softech.stockexchange.dao.FileDownloadMonitor.FileDownloadEvent.Type;
import pl.softech.stockexchange.dao.FileDownloadMonitor.FileDownloadListener;

/**
 *
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 */
public class SimeFileDownloadListener implements FileDownloadListener {

	private static final long SIZE = 1024*1024;
	
	protected final Logger logger = Logger
			.getLogger(SimeFileDownloadListener.class.getName());

	private long bytesRecieved = 0;

	private long last = 0;
	
	private long size;

	@Override
	public void actionPerformed(FileDownloadEvent event) {

		if (event.type == Type.START) {
			bytesRecieved = 0;
			size = event.size;
		}

		if (event.type == Type.MESSAGE) {
			logger.info(event.message);
		}

		if (event.type == Type.BYTES_RECIEVED) {
			bytesRecieved += event.bytes;

			if (bytesRecieved / SIZE  > last) {
				last = bytesRecieved / SIZE;
				logger.info("Downloaded: " + bytesRecieved + "/" + size + " [" + ((float)bytesRecieved/size * 100) + "%]");

			}

		}

	}

	public static FileDownloadListener create() {
		return new SimeFileDownloadListener();
	}

}
