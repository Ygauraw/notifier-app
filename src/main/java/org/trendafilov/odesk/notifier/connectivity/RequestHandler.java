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

package org.trendafilov.odesk.notifier.connectivity;

import java.io.IOException;

import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.pmw.tinylog.Logger;
import org.springframework.stereotype.Component;
import org.trendafilov.confucius.Configurable;
import org.trendafilov.confucius.Configuration;
import org.trendafilov.confucius.core.ConfigurationException;
import org.trendafilov.odesk.notifier.model.SearchCriteria;

@Component
public class RequestHandler {
	
	private String buildUrl(SearchCriteria criteria) throws ConfigurationException {
	    URIBuilder builder = new URIBuilder();
	    Configurable config = Configuration.getInstance();
	    builder.setScheme(config.getStringValue("api.scheme", "https")).setHost(config.getStringValue("api.host", "www.odesk.com")).setPath(config.getStringValue("api.query.path"))
	    .setParameter("page", "0;" + config.getIntValue("api.item.count", 20))
	    .setParameter("q", criteria.getQuery())
	    .setParameter("fb", criteria.getClientScore())
	    .setParameter("min", criteria.getMinBudget())
	    .setParameter("t", criteria.getJobType())
	    .setParameter("wl", criteria.getWorkHours())
	    .setParameter("dur", criteria.getDuration());
	    return builder.toString();
	}
	
	public String get(SearchCriteria criteria) {
		String result = "";
		try {
			result = Request.Get(buildUrl(criteria))
			.userAgent(Configuration.getInstance().getStringValue("profile.agent"))
			.connectTimeout(5000)
			.socketTimeout(5000)
			.execute().returnContent().asString();
		} catch (IOException e) {
			Logger.error(e, "Connection error");
		} catch (ConfigurationException e) {
			Logger.error(e, "Configuration error");
		}
		return result;
	}
}
