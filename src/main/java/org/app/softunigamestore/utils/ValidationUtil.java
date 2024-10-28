package org.app.softunigamestore.utils;

import jakarta.validation.ConstraintViolation;

import java.util.Set;

public interface ValidationUtil {

    <T> boolean isValid(T entity);

    <T> Set<ConstraintViolation<T>> getViolations(T entity);
}
