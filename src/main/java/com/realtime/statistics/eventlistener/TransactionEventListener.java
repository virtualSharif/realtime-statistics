package com.realtime.statistics.eventlistener;

import com.realtime.statistics.dto.Transaction;
import com.realtime.statistics.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TransactionEventListener {

    private final StatisticsService statisticsService;

    @Autowired
    public TransactionEventListener(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @Async
    @EventListener
    public void handleEvent(Transaction transaction) {
        log.debug("[SaM's TRANSACTION-EVENT-LISTENER] Transaction event triggered with the data: {}", transaction);
        statisticsService.compute(transaction, System.currentTimeMillis());
    }

}