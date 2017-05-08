/*
 * Copyright 2016, The Android Open Source Project
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

package com.cerezaconsulting.compendio.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CompedioDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "Compendio.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String BOOLEAN_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ACTIVITIES =
            "CREATE TABLE " + CompendioPersistenceContract.ActivityEntity.TABLE_NAME + " (" +
                    CompendioPersistenceContract.ActivityEntity._ID + BOOLEAN_TYPE + " PRIMARY KEY AUTOINCREMENT," +
                    CompendioPersistenceContract.ActivityEntity.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    CompendioPersistenceContract.ActivityEntity.INTELLECT + TEXT_TYPE + COMMA_SEP +
                    CompendioPersistenceContract.ActivityEntity.CORRECT + TEXT_TYPE + COMMA_SEP +
                    CompendioPersistenceContract.ActivityEntity.INCORRECT + TEXT_TYPE + COMMA_SEP +
                    CompendioPersistenceContract.ActivityEntity.POORLY + TEXT_TYPE + COMMA_SEP +
                    CompendioPersistenceContract.ActivityEntity.TRAINING + TEXT_TYPE + COMMA_SEP +
                    CompendioPersistenceContract.ActivityEntity.CHAPTER + TEXT_TYPE + COMMA_SEP +
                    CompendioPersistenceContract.ActivityEntity.SYNC + BOOLEAN_TYPE +
            " )";

    private static final String SQL_CREATE_REVIEWS =
            "CREATE TABLE " + CompendioPersistenceContract.ReviewEntity.TABLE_NAME + " (" +
                    CompendioPersistenceContract.ReviewEntity._ID + BOOLEAN_TYPE + " PRIMARY KEY AUTOINCREMENT," +
                    CompendioPersistenceContract.ReviewEntity.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    CompendioPersistenceContract.ReviewEntity.DATE + TEXT_TYPE + COMMA_SEP +
                    CompendioPersistenceContract.ReviewEntity.COUNTDOWN + TEXT_TYPE + COMMA_SEP +
                    CompendioPersistenceContract.ReviewEntity.COMPLETED + BOOLEAN_TYPE + COMMA_SEP +
                    CompendioPersistenceContract.ReviewEntity.TRAINING + TEXT_TYPE + COMMA_SEP +
                    CompendioPersistenceContract.ReviewEntity.SYNC + BOOLEAN_TYPE +
                    " )";


    private static final String SQL_CREATE_FRAGMENTS =
            "CREATE TABLE " + CompendioPersistenceContract.FragmentEntity.TABLE_NAME + " (" +
                    CompendioPersistenceContract.FragmentEntity._ID + BOOLEAN_TYPE + " PRIMARY KEY," +
                    CompendioPersistenceContract.FragmentEntity.TITLE + TEXT_TYPE + COMMA_SEP +
                    CompendioPersistenceContract.FragmentEntity.CONTENT + TEXT_TYPE + COMMA_SEP +
                    CompendioPersistenceContract.FragmentEntity.CHAPTER + TEXT_TYPE + COMMA_SEP +
                    CompendioPersistenceContract.FragmentEntity.TRAINING + TEXT_TYPE  +
                    " )";

    public CompedioDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ACTIVITIES);
        db.execSQL(SQL_CREATE_REVIEWS);
        db.execSQL(SQL_CREATE_FRAGMENTS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }
}
