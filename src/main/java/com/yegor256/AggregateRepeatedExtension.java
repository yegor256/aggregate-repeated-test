/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.yegor256;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;

/**
 *
 * @since 0.1.0
 */
public final class AggregateRepeatedExtension implements TestTemplateInvocationContextProvider {

    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        return context.getTestMethod().isPresent();
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(
        ExtensionContext context) {
        AggregateRepeatedTest annotation =
            context.getTestMethod().get().getAnnotation(AggregateRepeatedTest.class);
        int repeats = annotation.value();
        return Stream.of(
            new TestTemplateInvocationContext() {
                @Override
                public String getDisplayName(int invocationIndex) {
                    return String.format(
                        "%s#%s",
                        context.getRequiredTestClass().getName(),
                        context.getDisplayName()
                    );
                }
                @Override
                public List<Extension> getAdditionalExtensions() {
                    List<Extension> extensionList = TestTemplateInvocationContext.super.getAdditionalExtensions();
                    final List<Extension> extensions = new ArrayList<>();
                    extensions.addAll(extensionList);
                    extensions.add(
                        (BeforeTestExecutionCallback) ctx -> {
                            ctx.getStore(ExtensionContext.Namespace.GLOBAL).put("start", System.nanoTime());
                        }
                    );
                    extensions.add(
                        (AfterTestExecutionCallback) ctx -> {
                            long start = ctx.getStore(ExtensionContext.Namespace.GLOBAL).remove("start", long.class);
                            long elapsed = 0;
                            for (int i = 0; i < repeats; i++) {
                                try {
                                    ctx.getRequiredTestMethod().invoke(
                                        ctx.getRequiredTestInstance()
                                    );
                                } catch (Exception e) {
                                    // ignore it
                                }
                            }
                            Files.write(
                                Paths.get("/tmp/aggregate-repeated-test.txt"),
                                Collections.singletonList(
                                    String.format(
                                        "%s %d",
                                        ctx.getDisplayName(), System.nanoTime() - start
                                    )
                                ),
                                StandardOpenOption.CREATE,
                                StandardOpenOption.APPEND
                            );
                        }
                    );
                    return extensions;
                }
            }
        );
    }
}
