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

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.trendafilov.confucius.Configurable;
import org.trendafilov.confucius.Configuration;
import org.trendafilov.confucius.core.ConfigurationException;

public class EmailSender {
	
	public static void send(String recepient, String subject, String message) throws EmailException, ConfigurationException {
		SimpleEmail email = new SimpleEmail();
		Configurable config = Configuration.getInstance();
		email.setHostName(config.getStringValue("profile.smtp.host"));
		email.setSmtpPort(config.getIntValue("profile.smtp.port"));
		email.setAuthenticator(new DefaultAuthenticator(config.getStringValue("profile.username"), config.getStringValue("profile.password")));
		email.setSSLOnConnect(true);
		email.setFrom(config.getStringValue("profile.email"), config.getStringValue("profile.name"));
		email.addTo(recepient);
		email.setSubject(subject);
		email.setMsg(message);
		email.send();
	}
}
