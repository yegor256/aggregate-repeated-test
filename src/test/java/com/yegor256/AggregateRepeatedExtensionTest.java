/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.yegor256;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;

/**
 * Test case for {@link AggregateRepeatedExtension}.
 * @since 0.1.0
 */
final class AggregateRepeatedExtensionTest {

    @Test
    void repeatsAnnotatedMethodAsManyTimesAsRequested() {
        LauncherFactory.create().execute(
            LauncherDiscoveryRequestBuilder.request()
                .selectors(DiscoverySelectors.selectClass(RepeatedFake.class))
                .build()
        );
        MatcherAssert.assertThat(
            "the annotated method dont run three extra times",
            RepeatedFake.CALLS.get(),
            Matchers.equalTo(4)
        );
    }
}
