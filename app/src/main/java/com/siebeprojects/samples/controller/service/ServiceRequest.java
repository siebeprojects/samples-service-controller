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
 * Class for sending a request with data to the SampleService
 */
public class ServiceRequest implements Parcelable {

    /** The app specific requests*/
    public final static int REQ_REVERSE_TEXT = 0x01;

    /** The key values for storing info in the ServiceRequest */
    public final static String KEY_DATA     = "data";

    /** the sequence id for generating unique request ids */
    private static int nextId;

    /** The type of request */
    public int type;

    /** The bundle holding custom request data */
    public Bundle data;

    /** The result receiver */
    public ResultReceiver receiver;

    /** The id of this request */
    public int id;

    /** 
     * Create a new ServiceRequest, provide the type of the request.
     * The type will be used by the Service to determine what action should be 
     * performed.
     *
     * @param type The type of this service request 
     */    
    public ServiceRequest(int type) {
        this.type = type;
        this.id   = getNextId();
        this.data = new Bundle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ServiceRequest[id: ");
        sb.append(id);
        sb.append(",type: ");
        sb.append(Integer.toHexString(type));
        sb.append("]");
        return sb.toString();
    }

    /**
     * Return the next unique request id.
     *
     * @return  The next request id 
     */
    private static synchronized int getNextId() {
        return ++nextId;
    }

    /**
     * Describes the contents of this request.
     *
     * @return The type of this request.
     */
    public int describeContents() {
        return 0;
    }

    /**
     * Writes the request to the out buffer.
     */
    public void writeToParcel(Parcel out, int flags) {
        out.writeValue(receiver);
        out.writeInt(id);
        out.writeInt(type);
        out.writeBundle(data);
    }

    /** 
     * Constructor for Parceling
     * 
     * @param in The parcel to read from
     */
     private ServiceRequest(Parcel in) {
         receiver = (ResultReceiver)in.readValue(ServiceResultReceiver.class.getClassLoader());
         type     = in.readInt();
         id       = in.readInt();
         data     = in.readBundle(ServiceRequest.class.getClassLoader());
     }

    /**
     *
     */
    public static final Parcelable.Creator<ServiceRequest> CREATOR = new Parcelable.Creator<ServiceRequest>() {

         /**
          *
          */
         public ServiceRequest createFromParcel(Parcel in) {
             return new ServiceRequest(in);
         }

         /**
          *
          */
         public ServiceRequest[] newArray(int size) {
             return new ServiceRequest[size];
         }
     };
}
