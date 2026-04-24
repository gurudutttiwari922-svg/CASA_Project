package com.casa.core;

import com.casa.model.ReallocationLog;
import java.util.ArrayList;
import java.util.List;

public class ReallocationLogService {

    private List<ReallocationLog> logs = new ArrayList<>();

    public void addLog(ReallocationLog log) {
        logs.add(log);
    }

    public int getLogCount() {
        return logs.size();
    }

    public void printLogs() {
        System.out.println("\n================ REALLOCATION LOGS ================");
        if (logs.isEmpty()) {
            System.out.println("No reallocation events recorded.");
            return;
        }

        for (ReallocationLog log : logs) {
            System.out.println(log);
        }
    }
}
