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

package org.trendafilov.odesk.notifier.email;

import org.apache.commons.mail.EmailException;
import org.pmw.tinylog.Logger;
import org.springframework.stereotype.Component;
import org.trendafilov.confucius.Configuration;
import org.trendafilov.confucius.core.ConfigurationException;
import org.trendafilov.odesk.notifier.model.Alert;
import org.trendafilov.odesk.notifier.model.FixedJob;
import org.trendafilov.odesk.notifier.model.HourlyJob;
import org.trendafilov.odesk.notifier.model.Job;
import org.trendafilov.odesk.notifier.parser.OdeskUtils;

@Component
public class EmailComposer {
	
	public boolean composeAndSend(Job job, Alert alert) {
		StringBuilder message = new StringBuilder();
		if (job instanceof HourlyJob) 
			composeHourly(message, (HourlyJob) job, alert);
		if (job instanceof FixedJob)
			composeFixed(message, (FixedJob) job, alert);
		composeRemaining(message, job);
		return send(message, job, alert);
	}

    private void composeHourly(StringBuilder message, HourlyJob job, Alert alert) {
    	message.append("Duration: ")
    		.append(OdeskUtils.getHumanDuration(job.getDuration()))
    		.append("\n")
    		.append("Hours: ")
    		.append(job.getHoursPerWeek() + " hours per week")
    		.append("\n");
    }

	private void composeFixed(StringBuilder message, FixedJob job, Alert alert) {
    	message.append("Budget: $")
    		.append(job.getAmount())
    		.append("\n");
    }
    
    private void composeRemaining(StringBuilder message, Job job) {
    	try {
    		message.append("Link: ")
	    		.append("http://")
	    		.append(Configuration.getInstance().getStringValue("api.host"))
	    		.append(Configuration.getInstance().getStringValue("api.job.path"))
	    		.append(job.getCiphertext())
	    		.append("\n")
	    		.append("Description: ")
	    		.append(job.getDescription())
	    		.append("\n");    
    	} catch (ConfigurationException e) {
    		throw new RuntimeException(e);
    	}
    }
    
    private boolean send(StringBuilder message, Job job, Alert alert) {
    	for (String recepient : alert.getRecepients()) {
        	try {
				EmailSender.send(recepient, job.getTitle(), message.toString());
        		Thread.sleep(3000);
			} catch (ConfigurationException | EmailException | InterruptedException e) {
				Logger.error(e, "Unable to send email");
				return false;
			}
    	}
    	return true;
    }
}
