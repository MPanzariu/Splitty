/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;

import client.scenes.AddQuoteCtrl;
import client.scenes.MainCtrl;
import client.scenes.QuoteOverviewCtrl;
import com.google.inject.name.Names;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class MyModule implements Module {
    private static final String CONFIG_NAME = "splitty.properties";
    private static final String DEFAULT_PROPS_SERVER_URL = "http://localhost:8080/";

    @Override
    public void configure(Binder binder) {
        Properties properties = loadProperties();
        Names.bindProperties(binder, properties);

        binder.bind(MainCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AddQuoteCtrl.class).in(Scopes.SINGLETON);
        binder.bind(QuoteOverviewCtrl.class).in(Scopes.SINGLETON);
    }

    private Properties loadProperties(){
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(CONFIG_NAME));
        } catch (IOException ex) {
            loadDefaults(properties);
        }
        return properties;
    }

    private void loadDefaults(Properties properties){
        properties.setProperty("serverURL", DEFAULT_PROPS_SERVER_URL);
        try {
            properties.store(new FileWriter(CONFIG_NAME), null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}