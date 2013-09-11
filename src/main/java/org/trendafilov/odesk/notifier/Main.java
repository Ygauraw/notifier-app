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

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class Main {
	private final AlertDeamon alertDeamon;
	private final NewJobPooler newJobPooler;
	
	public static void main(String[] args) {
		@SuppressWarnings("resource") // context resource is closed via a shutdown hook
		AbstractApplicationContext context = new ClassPathXmlApplicationContext("notifier-app.xml");
		context.registerShutdownHook();
		Main app = context.getBean(Main.class);
		app.startup();
	}
	
	@Autowired
	public Main(AlertDeamon alertDeamon, NewJobPooler newJobPooler) {
		this.alertDeamon = alertDeamon;
		this.newJobPooler = newJobPooler;
	}
	
	public void startup() {
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);  
		executor.scheduleAtFixedRate(alertDeamon, 1, 1, TimeUnit.MINUTES);
		
		while (true) {
			executor.submit(newJobPooler);
			int randomDelay = 3 + (int) (Math.random() * 5);
			Logger.info(String.format("Sleeping for: [%s]", randomDelay));
			try {
				Thread.sleep(randomDelay * 1000 * 60);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
