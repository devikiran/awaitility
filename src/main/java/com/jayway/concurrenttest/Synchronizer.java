/*
 * Copyright 2010 the original author or authors.
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
package com.jayway.concurrenttest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.TimeUnit;

import com.jayway.concurrenttest.synchronizer.AwaitConditionImpl;
import com.jayway.concurrenttest.synchronizer.Condition;
import com.jayway.concurrenttest.synchronizer.ConditionEvaluator;
import com.jayway.concurrenttest.synchronizer.ConditionOptions;
import com.jayway.concurrenttest.synchronizer.Duration;
import com.jayway.concurrenttest.synchronizer.Supplier;

public class Synchronizer extends ConditionOptions {
    private static volatile Duration defaultPollInterval = Duration.FIVE_HUNDRED_MILLISECONDS;

    private static volatile Duration defaultTimeout = Duration.FOREVER;

    private static volatile boolean defaultCatchUncaughtExceptions = false;

    public static void catchUncaughtExceptions() {
        defaultCatchUncaughtExceptions = true;
    }

    public static void reset() {
        defaultPollInterval = Duration.FIVE_HUNDRED_MILLISECONDS;
        defaultTimeout = Duration.FOREVER;
        defaultCatchUncaughtExceptions = false;
        Thread.setDefaultUncaughtExceptionHandler(null);
    }

    public static void await(ConditionEvaluator condition) throws Exception {
        await(defaultTimeout, condition);
    }

    public static void await(long timeout, TimeUnit unit, ConditionEvaluator condition) throws Exception {
        await(duration(timeout, unit), condition);
    }

    public static void await(Duration duration, ConditionEvaluator condition) throws Exception {
        await(duration, condition, null);
    }

    public static void await(ConditionEvaluator condition, Duration pollInterval) throws Exception {
        await(defaultTimeout, condition, pollInterval);
    }

    public static void await(long timeout, TimeUnit unit, ConditionEvaluator conditionEvaluator, Duration pollInterval)
            throws Exception {
        await(duration(timeout, unit), conditionEvaluator, pollInterval);
    }

    public static void await(Duration duration, ConditionEvaluator conditionEvaluator, Duration pollInterval)
            throws Exception {
        await(condition(duration, conditionEvaluator, pollInterval));
    }

    public static void await(Condition condition) throws Exception {
        if (condition == null) {
            throw new IllegalArgumentException("Condition cannot be null");
        }
        if (defaultCatchUncaughtExceptions) {
            condition.andCatchAllUncaughtExceptions();
        }
        condition.await();
    }

    public static Condition condition(long timeout, TimeUnit unit, ConditionEvaluator condition) {
        return condition(duration(timeout, unit), condition);
    }

    public static Condition condition(ConditionEvaluator condition) {
        return condition(defaultTimeout, condition);
    }

    public static Condition condition(Duration duration, ConditionEvaluator conditionEvaluator) {
        return condition(duration, conditionEvaluator, null);
    }

    public static Condition condition(long timeout, TimeUnit unit, ConditionEvaluator conditionEvaluator,
            Duration pollInterval) {
        return condition(duration(timeout, unit), conditionEvaluator, pollInterval);
    }

    public static Condition condition(ConditionEvaluator conditionEvaluator, Duration pollInterval) {
        return condition(defaultTimeout, conditionEvaluator, pollInterval);
    }

    public static Condition condition(Duration duration, ConditionEvaluator conditionEvaluator, Duration pollInterval) {
        if (pollInterval == null) {
            pollInterval = defaultPollInterval;
        }
        if (duration == null) {
            duration = defaultTimeout;
        }
        return new AwaitConditionImpl(duration, conditionEvaluator, pollInterval);
    }

    public static void setDefaultPollInterval(long pollInterval, TimeUnit unit) {
        defaultPollInterval = new Duration(pollInterval, unit);
    }

    public static void setDefaultTimeout(long timeout, TimeUnit unit) {
        defaultTimeout = duration(timeout, unit);
    }

    public static void setDefaultPollInterval(Duration pollInterval) {
        if (pollInterval == null) {
            throw new IllegalArgumentException("You must specify a poll interval (was null).");
        }
        defaultPollInterval = pollInterval;
    }

    public static void setDefaultTimeout(Duration defaultTimeout) {
        if (defaultTimeout == null) {
            throw new IllegalArgumentException("You must specify a default timeout (was null).");
        }
        Synchronizer.defaultTimeout = defaultTimeout;
    }
}