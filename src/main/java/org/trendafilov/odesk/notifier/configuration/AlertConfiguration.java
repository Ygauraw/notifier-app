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

package org.trendafilov.odesk.notifier.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.pmw.tinylog.Logger;
import org.trendafilov.confucius.Configurable;
import org.trendafilov.confucius.Configuration;
import org.trendafilov.confucius.core.ConfigurationException;
import org.trendafilov.odesk.notifier.model.Alert;
import org.trendafilov.odesk.notifier.model.RestrictedKeywords;
import org.trendafilov.odesk.notifier.model.SearchCriteria;
import org.trendafilov.odesk.notifier.model.SearchCriteria.JobType;

/**
 * Configuration is driven by creation of the appropriate Alert objects.
 */
public class AlertConfiguration {

	public static Collection<Alert> getAllAlerts() {
		List<Alert> alerts = new ArrayList<>();
		Configurable config = Configuration.getInstance();
		try {
			for (String key : config.getStringList("profile.alerts")) {
				SearchCriteria criteria = new SearchCriteria();
				criteria.setQuery(config.getStringValue(key + ".query"));
				criteria.setJobType(JobType.valueOf(config.getStringValue(key + ".type").toUpperCase()));
				alerts.add(new Alert(criteria, new RestrictedKeywords(config.getStringList(key + ".restriction")), config.getStringList(key + ".email")));
			}
		} catch (ConfigurationException e) {
			Logger.error(e, "Configuration error");
			throw new RuntimeException(e);
		}
		return alerts;
	}
}
