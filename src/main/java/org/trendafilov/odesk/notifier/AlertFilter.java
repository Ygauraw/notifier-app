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

import org.trendafilov.odesk.notifier.model.Alert;
import org.trendafilov.odesk.notifier.model.Job;

public class AlertFilter {
	public static boolean isAllowed(Job job, Alert alert) {
		for (String item : alert.getRestrictedKeywords()) {
			if (job.getDescription().toLowerCase().contains(item.toLowerCase()) || job.getTitle().toLowerCase().contains(item.toLowerCase())) {
				return false;
			}
		}
		return true;
	}
}
