#!/bin/bash
export APP_BIN=/location/to/jars
export APP_CFG=/location/to/config
java -Dconf.context=Main -Dconf.properties=$APP_CFG/notifier-app.cfg -cp $APP_BIN/*.jar org.trendafilov.odesk.notifier.Main
