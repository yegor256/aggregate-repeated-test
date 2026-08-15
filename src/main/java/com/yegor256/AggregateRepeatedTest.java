/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.yegor256;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Mark a test method that must be repeated a few times, with the total
 * time of all repetitions reported as one number.
 *
 * @since 0.1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@TestTemplate
@ExtendWith(AggregateRepeatedExtension.class)
public @interface AggregateRepeatedTest {
    int value() default 100;
}
