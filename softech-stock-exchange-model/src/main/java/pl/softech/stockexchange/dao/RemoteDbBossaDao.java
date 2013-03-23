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


import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import pl.softech.stockexchange.dao.ds.RemoteBossaDataSource;
import pl.softech.stockexchange.dao.ds.Utils;
import pl.softech.stockexchange.model.FInstrument;

/**
 *
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 */
public class RemoteDbBossaDao {

	private static final String DEFAULT_DB_FILE_NAME = "db-stock.zip";
	private static final String DEFAULT_DB_DIR_NAME = "remote-db-cache";
	private static final String FILE_EXT = ".mst";

	private final RemoteBossaDataSource dataSource;
	private final BossaParser parser;
	private final FileDownloadMonitor fileDownloadMonitor;

	private final File dbZipFile;
	private final File dbDirName;

	private DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd"); 
	
	public RemoteDbBossaDao() throws RemoteBossaDaoException {

		try {

			dataSource = RemoteBossaDataSource.createRemoteDbDataSource();

			parser = new BossaParser();
			fileDownloadMonitor = new FileDownloadMonitor();
			fileDownloadMonitor.add(SimeFileDownloadListener.create());

			dbZipFile = new File(new File(DEFAULT_DB_DIR_NAME),
					DEFAULT_DB_FILE_NAME);
			dbDirName = new File(DEFAULT_DB_DIR_NAME);

			init();

		} catch (Exception e) {

			throw new RemoteBossaDaoException(e);

		}
	}
	
	private boolean isUpToDate(File dbZipFile) {
	    Calendar calc = Calendar.getInstance();
	    int currDay = calc.get(Calendar.DAY_OF_YEAR);
	    calc.setTime(new Date(dbZipFile.lastModified()));
	    return currDay == calc.get(Calendar.DAY_OF_YEAR);
	}

	private void init() throws RemoteBossaDaoException {

		if (!dbZipFile.exists() || !isUpToDate(dbZipFile)) {
			downloadDb();
		}

	}

	private void downloadDb() throws RemoteBossaDaoException {

		if (!dbDirName.exists()) {
			if (!dbDirName.mkdir()) {
				throw new RemoteBossaDaoException("Can't create dir: "
						+ dbDirName.getAbsolutePath());
			}
		}

		try {

			Utils.downloadFile(dataSource, dbDirName.getAbsolutePath(),
					fileDownloadMonitor).renameTo(dbZipFile);

		} catch (IOException e) {
			throw new RemoteBossaDaoException("Can't dowload db file from: "
					+ dataSource.getUrl(), e);
		}

	}

	@SuppressWarnings("unchecked")
	public Collection<String> findeAllInstrumentsName() throws Exception {

		Collection<String> ret = new LinkedList<String>();


		ZipFile zipFile = new ZipFile(dbZipFile);

		for (Enumeration<ZipEntry> e = (Enumeration<ZipEntry>) zipFile
				.entries(); e.hasMoreElements();) {
			ZipEntry entry = e.nextElement();
			ret.add(entry.getName().replace(FILE_EXT, ""));
		}

		return ret;

	}

	public Collection<FInstrument> findeInstrumentByName(String name) throws Exception {

		ZipFile zipFile = new ZipFile(dbZipFile);
		ZipEntry entry = zipFile.getEntry(name.toUpperCase() + FILE_EXT);
		return parser.getInstruments(zipFile.getInputStream(entry));

	}
	
	public Collection<FInstrument> findeInstrumentByName(String name, String from) throws Exception {
		return findeInstrumentByName(name, DATE_FORMAT.parse(from));
	}
	
	public Collection<FInstrument> findeInstrumentByName(String name, Date from) throws Exception {
		return findeInstrumentByName(name, from, new Date());
	}
	
	public Collection<FInstrument> findeInstrumentByName(String name, Date from, Date to) throws Exception {
		Collection<FInstrument> ret = findeInstrumentByName(name);
		Iterator<FInstrument> it = ret.iterator();
		while(it.hasNext()) {
			
			FInstrument finst = it.next();
			if(from.before(finst.getDate()) && finst.getDate().before(to)) {
				continue;
			}
			
			if(finst.getDate().equals(from) || finst.getDate().equals(to)) {
				continue;
			}
			it.remove();
		}
		return ret;
	}

	public static void main(String[] args) throws Exception {
		
		
		RemoteDbBossaDao dao = new RemoteDbBossaDao();
//		for (String name : dao.findeAllInstrumentsName()) {
//			System.out.println(name);
//		}
		
		for (FInstrument i : dao.findeInstrumentByName("KGHM")) {
			System.out.println(i);
		}
	}

}
