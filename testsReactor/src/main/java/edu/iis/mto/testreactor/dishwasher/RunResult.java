package edu.iis.mto.testreactor.dishwasher;

import java.util.Objects;

public class RunResult {

    private final int runMinutes;
    private final Status status;

    private RunResult(Builder builder) {
        this.runMinutes = builder.runMinutes;
        this.status = builder.status;
    }

    public Status getStatus() {
        return status;
    }

    public int getRunMinutes() {
        return runMinutes;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RunResult result = (RunResult) o;
        return runMinutes == result.runMinutes &&
                status == result.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(runMinutes, status);
    }

    public static final class Builder {

        private int runMinutes;
        private Status status;

        private Builder() {}

        public Builder withRunMinutes(int runMinutes) {
            this.runMinutes = runMinutes;
            return this;
        }

        public Builder withStatus(Status status) {
            this.status = status;
            return this;
        }

        public RunResult build() {
            return new RunResult(this);
        }
    }
}
