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
import android.app.IntentService;

import android.content.Intent;

import android.util.Log;

import android.os.Bundle;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;

import android.text.TextUtils;

/**
 * The Service to handle all App requests and communication with 
 * the backend. 
 */
public final class SampleService extends IntentService {

    private final static String TAG     = "sample_SampleService";

    /** The keys to identify values in the bundles */
    public final static String KEY_REQUESTDATA       = "requestdata";
    public final static String KEY_SERVICEREQUEST    = "servicerequest";
    public final static String KEY_SERVICERESPONSE   = "serviceresponse";

    /** 
     * Construct a new SampleService
     */
    public SampleService() {
        super("SampleService");
    }

    /** 
     * Create a sample service launch intent
     * 
     * @param context The context used to create the intent
     * @param request The request that should be added to the intent 
     * 
     * @return The launch Intent
     */
    public final static Intent createSampleServiceIntent(Context context, ServiceRequest request) {

        Intent intent = new Intent(context, SampleService.class);
        Bundle data = new Bundle();
        data.putParcelable(KEY_SERVICEREQUEST, request);
        intent.putExtra(KEY_REQUESTDATA, data);
        return intent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent == null || !intent.hasExtra(KEY_REQUESTDATA)) {
            return;
        }
        Bundle data = intent.getBundleExtra(KEY_REQUESTDATA);
        ServiceRequest request = (ServiceRequest)data.getParcelable(KEY_SERVICEREQUEST);

        if (request != null) {
            handleServiceRequest(data, request);
        }
    }

    /** 
     * Handle the service request from the application
     * 
     * @param data      The data bundle 
     * @param request   The request to handle.
     */
    private void handleServiceRequest(Bundle data, ServiceRequest request) {

        ServiceResponse response = new ServiceResponse(request.id);
        switch (request.type) {
        case ServiceRequest.REQ_REVERSE_TEXT:
            String text = request.data.getString(ServiceRequest.KEY_DATA);
            text = new StringBuilder(text).reverse().toString();
            response.data.putString(ServiceResponse.KEY_DATA, text);
            break;
        }

        // send the response back to the ServiceController
        data.putParcelable(KEY_SERVICERESPONSE, response);
        request.receiver.send(response.requestId, data);
    }
}
