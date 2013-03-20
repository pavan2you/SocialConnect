/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.example.socialconnect.platform.db;

import android.text.TextUtils;

/**
 * Represents a low-level contacts RawContact - or at least
 * the fields of the RawContact that we care about.
 */
final public class RawContact {

    /** The tag used to log to adb console. **/
    private static final String TAG = "RawContact";

    private final String mUserName;

    private final String mFullName;

    private final String mFirstName;

    private final String mLastName;

    private final String mCellPhone;

    private final String mOfficePhone;

    private final String mHomePhone;

    private final String mEmail;

    private final String mStatus;

    private final String mAvatarUrl;

    private final boolean mDeleted;

    private final boolean mDirty;

    private final String mServerContactId;

    private final long mRawContactId;

    private final long mSyncState;

    public String getServerContactId() {
        return mServerContactId;
    }

    public long getRawContactId() {
        return mRawContactId;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getFullName() {
        return mFullName;
    }

    public String getCellPhone() {
        return mCellPhone;
    }

    public String getOfficePhone() {
        return mOfficePhone;
    }

    public String getHomePhone() {
        return mHomePhone;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getStatus() {
        return mStatus;
    }

    public String getAvatarUrl() {
        return mAvatarUrl;
    }

    public boolean isDeleted() {
        return mDeleted;
    }

    public boolean isDirty() {
        return mDirty;
    }

    public long getSyncState() {
        return mSyncState;
    }

    public String getBestName() {
        if (!TextUtils.isEmpty(mFullName)) {
            return mFullName;
        } else if (TextUtils.isEmpty(mFirstName)) {
            return mLastName;
        } else {
            return mFirstName;
        }
    }

    public RawContact(String name, String fullName, String firstName, String lastName,
            String cellPhone, String officePhone, String homePhone, String email,
            String status, String avatarUrl, boolean deleted, String serverContactId,
            long rawContactId, long syncState, boolean dirty) {
        mUserName = name;
        mFullName = fullName;
        mFirstName = firstName;
        mLastName = lastName;
        mCellPhone = cellPhone;
        mOfficePhone = officePhone;
        mHomePhone = homePhone;
        mEmail = email;
        mStatus = status;
        mAvatarUrl = avatarUrl;
        mDeleted = deleted;
        mServerContactId = serverContactId;
        mRawContactId = rawContactId;
        mSyncState = syncState;
        mDirty = dirty;
    }

    /**
     * Creates and returns RawContact instance from all the supplied parameters.
     */
    public static RawContact create(String fullName, String firstName, String lastName,
            String cellPhone, String officePhone, String homePhone,
            String email, String status, boolean deleted, long rawContactId,
            String serverContactId) {
        return new RawContact(null, fullName, firstName, lastName, cellPhone, officePhone,
                homePhone, email, status, null, deleted, serverContactId, rawContactId,
                -1, true);
    }

    /**
     * Creates and returns a User instance that represents a deleted user.
     * Since the user is deleted, all we need are the client/server IDs.
     * @param clientUserId The client-side ID for the contact
     * @param serverUserId The server-side ID for the contact
     * @return a minimal User object representing the deleted contact.
     */
    public static RawContact createDeletedContact(long rawContactId, String serverContactId) {
        return new RawContact(null, null, null, null, null, null, null,
                null, null, null, true, serverContactId, rawContactId, -1, true);
    }
}
