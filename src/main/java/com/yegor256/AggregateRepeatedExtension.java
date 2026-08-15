/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.yegor256;

import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;

/**
 * JUnit5 extension that runs a test method a few times in a row and
 * reports how long all of them took together.
 * @since 0.1.0
 */
public final class AggregateRepeatedExtension implements TestTemplateInvocationContextProvider {

    @Override
    public boolean supportsTestTemplate(final ExtensionContext context) {
        return context.getTestMethod().isPresent();
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(
        final ExtensionContext context) {
        return Stream.of(
            new Aggregated(
                String.format(
                    "%s#%s",
                    context.getRequiredTestClass().getName(),
                    context.getDisplayName()
                ),
                context.getRequiredTestMethod()
                    .getAnnotation(AggregateRepeatedTest.class)
                    .value()
            )
        );
    }
}
