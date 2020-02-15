import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;

abstract class Sensor {

    private TreeMap<Instant, Float> lastValues;

    Sensor() {
        lastValues = new TreeMap<>();
    }

    Float getCurrentValue() {
        return lastValues.lastEntry().getValue();
    }

    float getAverage() {
        AtomicReference<Float> sum = new AtomicReference<>((float) 0);

        lastValues.forEach((instant, aFloat) -> sum.updateAndGet(v -> v + aFloat));

        return sum.get() / lastValues.size();
    }

    float getMaxValue() {
        return lastValues.values().stream().max(Float::compareTo).get();
    }

    float getMinValue() {
        return lastValues.values().stream().min(Float::compareTo).get();
    }

    void addValue(Float value) {
        if (isValueToBeAdded()) {
            lastValues.put(Instant.now(), value);

            if (lastValues.size() > 24)
                lastValues.pollFirstEntry();
        }
    }

    boolean isValueToBeAdded() {
        if (lastValues.size() == 0)
            return true;

        Instant instantToBeCompared = lastValues.lastKey().plus(1, ChronoUnit.SECONDS);

        return Instant.now().compareTo(instantToBeCompared) >= 0;
    }
}
