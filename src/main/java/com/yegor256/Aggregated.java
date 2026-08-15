/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.yegor256;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;

/**
 * Single invocation of a test that must be repeated a few times, while
 * the time of all repetitions is measured together.
 * @since 0.1.0
 */
final class Aggregated implements TestTemplateInvocationContext {

    /**
     * The key of the starting moment in the store of JUnit.
     */
    private static final String START = "start";

    /**
     * The name to show for this invocation.
     */
    private final String label;

    /**
     * How many extra times the test method must be called.
     */
    private final int repeats;

    Aggregated(final String name, final int times) {
        this.label = name;
        this.repeats = times;
    }

    @Override
    public String getDisplayName(final int index) {
        return this.label;
    }

    @Override
    public List<Extension> getAdditionalExtensions() {
        return Arrays.asList(
            (BeforeTestExecutionCallback) ctx -> ctx.getStore(
                ExtensionContext.Namespace.GLOBAL
            ).put(Aggregated.START, System.nanoTime()),
            (AfterTestExecutionCallback) this::report
        );
    }

    /**
     * Repeat the test method and save the total time to the report.
     * @param ctx The context of the test that has just finished
     * @throws Exception If the test method fails
     */
    private void report(final ExtensionContext ctx) throws Exception {
        for (int idx = 0; idx < this.repeats; idx += 1) {
            ctx.getRequiredTestMethod().invoke(ctx.getRequiredTestInstance());
        }
        Files.write(
            Paths.get("/tmp/aggregate-repeated-test.txt"),
            Collections.singletonList(
                String.format(
                    "%s %d",
                    ctx.getDisplayName(),
                    System.nanoTime() - ctx.getStore(ExtensionContext.Namespace.GLOBAL)
                        .remove(Aggregated.START, Long.class)
                )
            ),
            StandardOpenOption.CREATE,
            StandardOpenOption.APPEND
        );
    }
}
