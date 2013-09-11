/*
 *  Copyright 2013 Ivan Trendafilov
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.trendafilov.odesk.notifier;

import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trendafilov.odesk.notifier.configuration.AlertConfiguration;
import org.trendafilov.odesk.notifier.connectivity.RequestHandler;
import org.trendafilov.odesk.notifier.model.Alert;

@Component
public class NewJobPooler implements Runnable {
	
	private final RequestHandler requestHandler;
	private final JobCache jobCache;
	
	@Autowired
	public NewJobPooler(RequestHandler requestHandler, JobCache jobCache) {
		this.requestHandler = requestHandler;
		this.jobCache = jobCache;
	}

	@Override
	public void run() {
		try {
			for (Alert alert : AlertConfiguration.getAllAlerts()) {
				Logger.info("Fetching new data...");
				jobCache.put(requestHandler.get(alert.getSearchCriteria()), alert);
				Thread.sleep(1000 * 3); // don't flood with requests
			}
		} catch (RuntimeException | InterruptedException e) {
			Logger.error(e);
		}
	}
}
