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
package pl.softech.stockexchange.dao.ds;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;


/**
 *
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 */
public class RemoteBossaDataSource {

	protected final Logger logger = Logger.getLogger(RemoteBossaDataSource.class.getName());
	
	private final static String DEFAULT_HOST = "bossa.pl";
	
	private URL url;
	private String host;

	public RemoteBossaDataSource() throws MalformedURLException {
		this(DEFAULT_HOST);
	}
	
	private RemoteBossaDataSource(String url) throws MalformedURLException {
		this.host = DEFAULT_HOST;
		this.url = new URL("http://" + host + "/" + url);
	}
	
	public URL getUrl() {
		return url;
	}

	public String getHost() {
		return host;
	}
	
	public static RemoteBossaDataSource createRemoteRealTimeDataSource() throws MalformedURLException {
		return new RemoteBossaDataSource("pub/ciagle/omega/cgl/ndohlcv.txt");
	}

	public static RemoteBossaDataSource createRemoteDbDataSource() throws MalformedURLException {
		return new RemoteBossaDataSource("pub/ciagle/mstock/mstcgl.zip");
	}
	
}
