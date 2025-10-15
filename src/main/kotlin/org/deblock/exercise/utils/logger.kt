package org.deblock.exercise.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory

inline fun <reified T> T.shortLogger(): Logger = LoggerFactory.getLogger(T::class.java.simpleName)