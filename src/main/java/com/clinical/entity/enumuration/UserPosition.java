package com.clinical.entity.enumuration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum UserPosition {
    PROFESSOR("PROFESSOR", "교수"),
    ADMIN("ADMIN", "관리자");

    private final String value;
    private final String description;

    @JsonValue
    @Override
    public String toString() { return this.value; }

    @JsonCreator
    public static UserPosition fromValue(String value) {
        return Stream.of(UserPosition.values())
                .filter(p -> p.value.equalsIgnoreCase(value))
                .findFirst()
                .orElse(null);
    }
}