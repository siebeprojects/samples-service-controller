/*
 * This file is part of Siebe Projects samples.
 *
 * Siebe Projects samples is free software: you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Siebe Projects samples is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Lesser GNU General Public License for more details.
 *
 * You should have received a copy of the Lesser GNU General Public License
 * along with Siebe Projects samples.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.siebeprojects.samples.controller.service;

/**
 * The service request observer, this observer will be notified
 * when a request has been processed.
 */
public class ServiceObserver {

    /** 
     * This method will be called when the service has completed a request. 
     * Use the requestId in the response object to identify to which request 
     * this response belongs.
     * 
     * @param response The response containing response data. 
     */    
    public void onServiceRequestCompleted(ServiceResponse response) {
    }
}
