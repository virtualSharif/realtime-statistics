package com.realtime.statistics.cache;

import com.realtime.statistics.config.Constants;
import com.realtime.statistics.dto.Statistics;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StatisticsCache {

    @Getter
    private final Map<Integer, Statistics> data
            = new ConcurrentHashMap<>(Constants.LAST_INTERESTED_INTERVAL_IN_SECONDS);

}
