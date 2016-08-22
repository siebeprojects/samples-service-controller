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

import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;

/**
 * This class is used to send responses back to the singleton ServiceController once
 * the service is done processing the request.
 */
class ServiceResultReceiver extends ResultReceiver {

    /**
     * Create a new ServiceResultReceiver
     * All callbacks from the service will be executed in 
     * the main looper thread.
     */
    ServiceResultReceiver() {
        super(new Handler(Looper.getMainLooper()));
    }
}
