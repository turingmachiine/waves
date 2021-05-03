package ru.bvb.waves.models;

public enum State {
    TO_DO, IN_PROGRESS, CODE_REVIEW, DEV_TEST, TESTING, DONE, WONTFIX;

    public static final State[] values = values();

    public State next() {
        return values[(ordinal() + 1) % values.length];
    }
}
