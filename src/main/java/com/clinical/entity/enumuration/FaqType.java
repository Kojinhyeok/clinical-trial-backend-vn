package com.clinical.entity.enumuration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum FaqType {
    PARTICIPATION("PARTICIPATION", "시험참여"),
    REQUEST("REQUEST", "시험의뢰");

    private final String value;
    private final String description;

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }

    @JsonCreator
    public static FaqType fromValue(String value) {
        return Stream.of(FaqType.values())
                .filter(type -> type.value.equalsIgnoreCase(value))
                .findFirst()
                .orElse(null);
    }
}