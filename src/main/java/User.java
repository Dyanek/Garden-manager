class User {
    private String userName;

    private int executedCommandsCount;
    private int executedWateringsCount;
    private int executedLightingsCount;

    String getUserName() {
        return userName;
    }

    int getExecutedCommandsCount() {
        return executedCommandsCount;
    }

    int getExecutedWateringsCount() {
        return executedWateringsCount;
    }

    int getExecutedLightingsCount() {
        return executedLightingsCount;
    }

    void increaseExecutedCommqndsCount() {
        executedCommandsCount++;
    }

    void increaseExecutedWateringsCount() {
        increaseExecutedCommqndsCount();
        executedWateringsCount++;
    }

    void increaseExecutedLightingsCount() {
        increaseExecutedCommqndsCount();
        executedLightingsCount++;
    }

    User(String userName) {
        this.userName = userName;
        this.executedCommandsCount = 0;
        this.executedWateringsCount = 0;
        this.executedLightingsCount = 0;
    }
}
