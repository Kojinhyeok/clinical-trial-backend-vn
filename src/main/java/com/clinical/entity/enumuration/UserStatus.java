package com.clinical.entity.enumuration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum UserStatus {
    ACTIVE("ACTIVE", "활성"),
    INACTIVE("INACTIVE", "비활성"),
    SUSPENDED("SUSPENDED", "정지");

    private final String value;
    private final String description;

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }

    @JsonCreator
    public static UserStatus fromValue(String value) {
        if (value == null) return null;

        return Stream.of(UserStatus.values())
                .filter(status -> status.value.equalsIgnoreCase(value))
                .findFirst()
                .orElse(null);
    }
}