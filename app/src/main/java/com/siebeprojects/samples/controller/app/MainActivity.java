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

package com.siebeprojects.samples.controller.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.siebeprojects.samples.controller.service.ServiceController;
import com.siebeprojects.samples.controller.service.ServiceObserver;
import com.siebeprojects.samples.controller.service.ServiceResponse;

import com.siebeprojects.samples.controller.R;

/**
 * MainActivity that initialises the ServiceController, 
 * sends one request and receives a response.
 */
public final class MainActivity extends AppCompatActivity {

    /** Tag for logging */
    private final static String TAG = "sample_MainActivity";

    /** The request id that is pending */
    private int requestId;

    /** The service observer */
    private ServiceObserver observer;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialise the service observer
        ServiceController.getSingleton().init(this);
        initServiceObserver();

        setContentView(R.layout.main_activity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPause() {
        super.onPause();
        ServiceController.getSingleton().removeObserver(observer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResume() {
        super.onResume();
        ServiceController controller = ServiceController.getSingleton();
        controller.addObserver(observer);
        requestId = controller.reverse("reverse this text");
    }

    /** 
     * Initialise the service observer.
     */
    private void initServiceObserver() {

        observer = new ServiceObserver() {

                @Override
                public void onServiceRequestCompleted(ServiceResponse response) {
                    handleOnServiceRequestCompleted(response);
                }
            };
    }

    /** 
     * Handle the service request completed event
     * 
     * @param response The service response
     */
    private void handleOnServiceRequestCompleted(ServiceResponse response) {

        Log.i(TAG, "onServiceRequestCompleted: " + response);
        if (response.isRequest(this.requestId)) {
            this.requestId = 0;
            String data = response.data.getString(ServiceResponse.KEY_DATA);
            Log.i(TAG, "ServiceController response: " + data);
        }
    }
}
