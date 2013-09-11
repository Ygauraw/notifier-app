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
import org.trendafilov.odesk.notifier.email.EmailComposer;
import org.trendafilov.odesk.notifier.model.Alert;
import org.trendafilov.odesk.notifier.model.Job;

@Component
public class AlertDeamon implements Runnable {

	private final EmailComposer emailComposer;
	private final JobCache jobCache;

	@Autowired
	public AlertDeamon(EmailComposer emailComposer, JobCache jobCache) {
		this.emailComposer = emailComposer;
		this.jobCache = jobCache;
	}

	@Override
	public void run() {
		try {
			while (!jobCache.getUnprocessed().isEmpty()) {
				Job job = jobCache.getUnprocessed().iterator().next();
				Alert alert = jobCache.getAlertByJob(job);
				if (AlertFilter.isAllowed(job, alert)) {
					if (emailComposer.composeAndSend(job, alert)) {
						jobCache.remove(job);
					}
				} else {
					jobCache.remove(job);
				}
			}
		} catch (RuntimeException e) {
			Logger.error(e);
		}
	}
}
