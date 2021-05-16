package ru.bvb.waves.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StateTest {

    @Test
    void next() {
        State state = State.TO_DO;
        assertEquals(State.IN_PROGRESS, state.next());
        state = State.CODE_REVIEW;
        assertEquals(State.DEV_TEST, state.next());
    }
}