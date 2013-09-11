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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trendafilov.confucius.Configuration;
import org.trendafilov.odesk.notifier.dao.CacheStateDao;
import org.trendafilov.odesk.notifier.model.Alert;
import org.trendafilov.odesk.notifier.model.Job;
import org.trendafilov.odesk.notifier.parser.OdeskUtils;

@Component
public class JobCache {
    private List<Job> unprocessed = Collections.synchronizedList(new ArrayList<Job>()); 
    private List<Job> processed = Collections.synchronizedList(new ArrayList<Job>()); 
    private Map<Job, Alert> alertPair = new ConcurrentHashMap<>();
    private final CacheStateDao dao;
    
    @Autowired
    public JobCache(CacheStateDao dao) {
    	this.dao = dao;
    	processed.addAll(this.dao.getAll());
    }

    public void put(String xmlInput, Alert alert) {
    	Collection<Job> jobs = OdeskUtils.parseJobs(xmlInput);
    	for (Job job : jobs) {
    		if (!processed.contains(job) && !unprocessed.contains(job)) {
    			alertPair.put(job, alert);
    			unprocessed.add(job);
    		}
    	}
    	Logger.info(String.format("Unprocessed items: [%s]", unprocessed.size()));
    }
    
    public synchronized void remove(Job job) {
    	unprocessed.remove(job);
    	alertPair.remove(job);
    	addProcessed(job);
    	Logger.info(String.format("Unprocessed items: [%s]", unprocessed.size()));
    }
    
    public Collection<Job> getUnprocessed() {
    	return unprocessed;
    }
    
    public Alert getAlertByJob(Job job) {
    	return alertPair.get(job);
    }
    
    private void addProcessed(Job job) {
    	if (processed.size() > Configuration.getInstance().getIntValue("profile.job.cache", 1000));
    		dao.remove(processed.remove(0));
    	dao.add(job);
    	processed.add(job);
    }
}
