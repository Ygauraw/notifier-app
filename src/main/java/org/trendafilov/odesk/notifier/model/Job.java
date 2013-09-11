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

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Job {
	private final String title;
	private final String description;
	private final String ciphertext;
	private final String createdTime;

	public Job(String title, String description, String ciphertext) {
		this.title = title;
		this.description = description;
		this.ciphertext = ciphertext;
		this.createdTime = (new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date(System.currentTimeMillis())));
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getCiphertext() {
		return ciphertext;
	}
	
	public String getCreatedTime() {
		return createdTime;
	}

	@Override
	public int hashCode() {
		return ciphertext.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!(obj instanceof Job))
			return false;

		Job rhs = (Job) obj;
		return ciphertext.equals(rhs.getCiphertext());
	}
}
