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

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.ResultReceiver;

/**
 * The ServiceResponse holding response data from the service.
 * This class can also be used to include errors while
 * processing the request in the service.
 */
public class ServiceResponse implements Parcelable {

    /** The key to obtain result data from the internal bundle */
    public final static String KEY_DATA    = "data";

    /** The id of the request this response belongs to */
    public int requestId;

    /** The bundle containing result data */
    public Bundle data;

    /**
     * Create a new ServiceResponse object
     * 
     * @param requestId     The request id this response belongs to.
     */
    public ServiceResponse(int requestId) {
        this.requestId = requestId;
        data = new Bundle();
    }

    /** 
     * Check if this service response is bound to a request with 
     * the given id.
     * 
     * @param requestId The requestId to check for 
     * 
     * @return true when request ids matches, false otherwise
     */
    public boolean isRequest(int requestId) {
        return this.requestId == requestId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ServiceResponse[requestId: ");
        sb.append(requestId);
        sb.append("]");
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(requestId);
        out.writeBundle(data);
    }

    /**
     *  
     */
     private ServiceResponse(Parcel in) {
         requestId = in.readInt();
         data = in.readBundle(ServiceRequest.class.getClassLoader());
     }

    /**
     *
     */
    public static final Parcelable.Creator<ServiceResponse> CREATOR = new Parcelable.Creator<ServiceResponse>() {

         /**
          *
          */
         public ServiceResponse createFromParcel(Parcel in) {
             return new ServiceResponse(in);
         }

         /**
          *
          */
         public ServiceResponse[] newArray(int size) {
             return new ServiceResponse[size];
         }
     };
}
