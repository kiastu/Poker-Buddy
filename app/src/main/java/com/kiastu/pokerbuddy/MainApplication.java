package com.kiastu.pokerbuddy;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by dakong on 8/17/15.
 */
public class MainApplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "LL1RyZwG6OlU8ghWLsKqHgfmi3YSGdNC9SUVj82r", "zEsjk0X9O0BCUlpYFWvxFDPhq5PgAVnYJCG7m7RI");
    }
}
