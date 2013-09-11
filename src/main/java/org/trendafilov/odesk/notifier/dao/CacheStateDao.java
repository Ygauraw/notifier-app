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

package org.trendafilov.odesk.notifier.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.trendafilov.odesk.notifier.model.Job;

@Repository
public class CacheStateDao {
	private final static String SELECT_ALL = "select ID from JOBS";
	private final static String INSERT = "insert into JOBS (ID, TIMESTAMP) values (?, ?)";
	private final static String DELETE = "delete from JOBS where ID = (?)";
	
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public CacheStateDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public Collection<Job> getAll() {
		return jdbcTemplate.query(SELECT_ALL, new RowMapper<Job>() {
			@Override
			public Job mapRow(ResultSet rs, int arg1) throws SQLException {
				return new Job("", "", rs.getString("ID")) { };
			}
		});
	}
	
	public void add(Job job) {
		jdbcTemplate.update(INSERT, new Object[] { job.getCiphertext(), job.getCreatedTime() });
	}
	
	public void remove(Job job) {
		jdbcTemplate.update(DELETE, new Object[] { job.getCiphertext() });
	}
}
