/*
 * Copyright 2016 gdeignacio.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.caib.sgtsic.jooreports;

import java.io.Serializable;

/**
 *
 * @author gdeignacio
 */
class OOServerSettings implements Serializable {
    
    private String host;
    private String port;

    public OOServerSettings(String host, String port) {
        this.host = host;
        this.port = port;
    }
    
    public OOServerSettings() {
        this.host = "localhost";
        this.port = "8100";
    }
    
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
    
    
}
