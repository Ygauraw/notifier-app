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

package org.trendafilov.odesk.notifier.model;

import java.util.Collection;
import java.util.List;


public class Alert {
	private final SearchCriteria criteria;
	private final RestrictedKeywords keywords;
	private final List<String> recepients;
	
	public Alert(SearchCriteria criteria, RestrictedKeywords keywords, List<String> recepients) {
		this.criteria = criteria;
		this.keywords = keywords;
		this.recepients = recepients;
	}
	
	public List<String> getRecepients() {
		return recepients;
	}
	
	public SearchCriteria getSearchCriteria() {
		return criteria;
	}
	
	public Collection<String> getRestrictedKeywords() {
		return keywords.getKeywords();
	}

}
