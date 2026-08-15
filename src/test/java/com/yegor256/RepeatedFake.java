/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.yegor256;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Fake test case that counts how many times it was called by JUnit
 * together with {@link AggregateRepeatedExtension}.
 * @since 0.1.0
 */
final class RepeatedFake {

    /**
     * How many times the method below was called, for the test to see.
     */
    static final AtomicInteger CALLS = new AtomicInteger();

    /**
     * How many times this very instance was called.
     */
    private final AtomicInteger own = new AtomicInteger();

    @AggregateRepeatedTest(3)
    void countsItsOwnInvocations() {
        RepeatedFake.CALLS.set(this.own.incrementAndGet());
    }
}
