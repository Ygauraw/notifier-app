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

public class SearchCriteria {
	private String query;
	private String clientScore; 
	private String minBudget;
	private String jobType;
	private String workHours;
	private String duration;
	
	public void setQuery(String query) {
		this.query = query;
	}
	
	public String getQuery() {
		return query;
	}
	
	public void setClientScore(ClientScore score) {
		this.clientScore = score.toString();
	}
	
	public String getClientScore() {
		return clientScore;
	}
	
	public void setMinBudget(int minBudget) {
		this.minBudget = Integer.toString(minBudget);
	}
	
	public String getMinBudget() {
		return minBudget;
	}
	
	public void setJobType(JobType type) {
		this.jobType = type.toString();
	}
	
	public String getJobType() {
		return jobType;
	}
	
	public void setWorkHours(WorkHours hours) {
		this.workHours = hours.toString();
	}
	
	public String getWorkHours() {
		return workHours;
	}
	
	public void setDuration(Duration time) {
		this.duration = time.toString();
	}
	
	public String getDuration() {
		return duration;
	}
	
	public enum ClientScore {
		NONE("0"), ONE_FOUR("10"), FOUR("40"), FOUR_FIVE("45"), FIVE("50");
		
		private final String text;
		
		private ClientScore(final String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			return text;
		}
	}
	
	public enum JobType {
		HOURLY("Hourly"), FIXED("Fixed");
		
		private final String text;
		
		private JobType(final String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			return text;
		}
	}
	
	public enum WorkHours {
		AS_NEEDED("0"), PART_TIME("20"), FULL_TIME("40");
		
		private final String text;
		
		private WorkHours(final String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			return text;
		} 
	}
	
	public enum Duration {
		LESS_THAN_ONE_WEEK("0"), LESS_THAN_ONE_MONTH("1"), ONE_TO_THREE_MONTHS("4"), THREE_TO_SIX_MONTHS("13"), ONGOING("26");
		
		private final String text;
		
		private Duration(final String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			return text;
		} 
	}
}
