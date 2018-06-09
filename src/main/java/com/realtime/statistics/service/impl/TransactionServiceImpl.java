package com.realtime.statistics.service.impl;

import com.realtime.statistics.config.Constants;
import com.realtime.statistics.dto.Transaction;
import com.realtime.statistics.exception.ErrorCode;
import com.realtime.statistics.exception.ValidationException;
import com.realtime.statistics.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public TransactionServiceImpl(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public Boolean add(Transaction transaction) {

        validateTransaction(transaction);
        if (isTransactionTimestampUnderInterestInterval(transaction.getTimestamp())) {
            log.info("[SaM is Happy] Transaction is valid: {}", transaction);
            applicationEventPublisher.publishEvent(transaction);
            return Boolean.TRUE;
        } else {
            log.info("[SaM is Sad] Transaction timestamp is old: {}", transaction);
            return Boolean.FALSE;
        }
    }

    private void validateTransaction(Transaction transaction) {
        if (Objects.isNull(transaction)) {
            throw new ValidationException(ErrorCode.VALIDATION_EMPTY_REQUEST_BODY);
        }
        if (Objects.isNull(transaction.getAmount())) {
            throw new ValidationException(ErrorCode.VALIDATION_MISSING_AMOUNT);
        }
        if (Objects.isNull(transaction.getTimestamp())) {
            throw new ValidationException(ErrorCode.VALIDATION_MISSING_TIMESTAMP);
        }
    }

    private Boolean isTransactionTimestampUnderInterestInterval(Long timestamp) {
        return timestamp > (System.currentTimeMillis() - Constants.INTERESTED_INTERVAL_IN_MILLISECONDS);
    }

}
