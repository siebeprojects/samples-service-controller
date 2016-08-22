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

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageManager;
import android.content.SharedPreferences;

import android.util.Log;
import android.os.Bundle;
import android.text.TextUtils;
import java.util.HashMap;
import java.util.HashSet;

/**
 * This class serves as a bridge between the application and service.
 * It holds an internal hashmap of pending requests. The method isPending() can
 * be used to determine if requests are pending. 
 * Use the addObserver and removeObserver methods to start and stop listening for responses.
 * These methods can be used i.e. in the onPause and onResume methods of an Activity.
 */
public final class ServiceController {

    private final static String TAG     = "sample_ServiceController";

    /** the singleton service controller */
    private static ServiceController singleton;

    /** the context of the application */
    private Context context;

    /** Is the ServiceController initialized */
    private boolean init;

    /** 
     * the internal map of service requests, this map is used 
     * to determine if requests are pending  
     */  
    private HashMap<Integer, ServiceRequest> requests;

    /** the service observers */
    private HashSet<ServiceObserver> observers;

    /**
     * Create the singleton instance of this ServiceController
     */
    private ServiceController() {
        requests = new HashMap<Integer, ServiceRequest>();
        observers = new HashSet<ServiceObserver>();
    }

    /**
     * Get the singleton instance of this ServiceController.
     *
     * @return the singleton ServiceController
     */
    public final static ServiceController getSingleton() {

        if (singleton != null) {
            return singleton;
        }
        synchronized (ServiceController.class) {
            if (singleton == null) {
                singleton = new ServiceController();
            }
        }
        return singleton;
    }

    /** 
     * Initialize the service controller, the ServiceController will hold a 
     * reference to the Application Context until 
     * the ServiceController is stopped.
     * 
     * @param context       The context.
     */
    public synchronized void init(Context context) {

        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        if (!init) {
            this.context = context.getApplicationContext();
            init = true;
        }
    }

    /** 
     * Add an observer to this service controller. Only non null observer can
     * be added. 
     * 
     * @param observer      The observer to be added to this ServiceController
     */
    public synchronized void addObserver(ServiceObserver observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }

    /** 
     * Remove an observer from this ServiceController. Only non-null 
     * observers can be removed from this ServiceController. 
     * 
     * @param observer      The observer to be removed.
     */
    public synchronized void removeObserver(ServiceObserver observer) {
        if (observer != null) {
            observers.remove(observer);
        }
    }

    /** 
     * Check if there is a pending request with the given id
     * 
     * @param requestId     The id of the request to check for
     * 
     * @return true when the request is pending, false otherwise
     */
    public synchronized boolean isPending(int requestId) {
        return requests.containsKey(requestId);
    }

    /** 
     * Get the Context object stored in this ServiceController.
     * 
     * @return The Context or null if this ServiceController was not initialized 
     *      properly. 
     */
    public Context getContext() {
        return context;
    }

    /** 
     * Stop this ServiceController, this will clear the request and observer 
     * buffers.
     */
    public synchronized void stop() {

        if (init) {
            requests.clear();
            observers.clear();

            Intent intent = new Intent(context, ServiceController.class);
            context.stopService(intent);

            this.context = null;
            this.init = false;
        }
    }

    /** 
     * Shout the text to the service and wait for the service to respond.
     * 
     * @param text The text to send to the service
     * 
     * @return The unique request id, this id can be used for the isPending method
     * to check if the request has already been handled.
     */
    public int reverse(String text) {

        if (TextUtils.isEmpty(text)) {
            throw new IllegalArgumentException("Text cannot be null or empty");
        }   
        ServiceRequest request = new ServiceRequest(ServiceRequest.REQ_REVERSE_TEXT);
        request.data.putString(ServiceResponse.KEY_DATA, text);
        return sendToService(request);
    } 

    /** 
     * Send the request to the service, store the request 
     * into the internal request buffer.
     * 
     * @param request The request to be send to the service.
     */
    private synchronized int sendToService(ServiceRequest request) {

        if (context == null) {
            throw new IllegalStateException("ServiceController is missing context, either stopped or not initialised");
        }

        // add teh result receiver to the request object
        request.receiver = new ServiceResultReceiver() {
                protected void onReceiveResult(int resultCode, Bundle resultData) {
                    notifyListeners(resultCode, resultData);
                }
            };

        requests.put(request.id, request);
        Intent intent = SampleService.createSampleServiceIntent(context, request);
        context.startService(intent);
        return request.id;
    }

    /** 
     * Notify all observers that are registered to this 
     * ServiceController
     * 
     * @param requestId The id of the request that is completed 
     * @param bundle The bundle containing the response data
     */
    private synchronized void notifyListeners(int requestId, Bundle bundle) {
        
        ServiceRequest req = requests.remove(requestId);
        if (req == null) {
            return;
        }
        ServiceResponse resp = bundle.getParcelable(SampleService.KEY_SERVICERESPONSE);
        for (ServiceObserver observer : observers) {
            observer.onServiceRequestCompleted(resp);
        }
    }
}

