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

import android.provider.BaseColumns;

/**
 * The contract used for the db to save the tasks locally.
 */
public final class CompendioPersistenceContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private CompendioPersistenceContract() {
    }

    /* Inner class that defines the table contents */
    public static abstract class TrainingEntry implements BaseColumns {
        public static final String TABLE_NAME = "training";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_COMPLETED = "completed";
    }

    public static abstract class ActivityEntity implements BaseColumns {
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String TABLE_NAME = "activity";
        public static final String INTELLECT = "intellect";
        public static final String CORRECT = "correct";
        public static final String INCORRECT = "incorrect";
        public static final String POORLY = "poorly";
        public static final String TRAINING = "training";
        public static final String CHAPTER = "chapter";
        public static final String SYNC = "sync";
    }

    public static abstract class ReviewEntity implements BaseColumns {
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String TABLE_NAME = "review";
        public static final String DATE = "date";
        public static final String COUNTDOWN = "countdown";
        public static final String COMPLETED = "completed";
        public static final String TRAINING = "training";
        public static final String SYNC = "sync";
    }
}
