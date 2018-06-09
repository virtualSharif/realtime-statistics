package com.realtime.statistics.service.impl;

import com.realtime.statistics.dto.Transaction;
import com.realtime.statistics.exception.ValidationException;
import com.realtime.statistics.service.TransactionService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationEventPublisher;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class TransactionServiceImplTest {

    private TransactionService classUnderTest;

    private ApplicationEventPublisher applicationEventPublisher;

    @Before
    public void setUp() {
        applicationEventPublisher = mock(ApplicationEventPublisher.class);
        classUnderTest = new TransactionServiceImpl(applicationEventPublisher);
    }

    @Test(expected = ValidationException.class)
    public void add_shouldThrowValidationExceptionWhenTransactionIsNull() {

        //given
        Transaction transaction = null;

        //when
        classUnderTest.add(transaction);

    }

    @Test(expected = ValidationException.class)
    public void add_shouldThrowValidationExceptionWhenTransactionTimestampIsNull() {
        //given
        Transaction transaction = new Transaction(100d, null);

        //when
        classUnderTest.add(transaction);

    }

    @Test(expected = ValidationException.class)
    public void add_shouldThrowValidationExceptionWhenTransactionAmountIsNull() {

        //given
        Transaction transaction = new Transaction(null, System.currentTimeMillis());

        //when
        classUnderTest.add(transaction);

    }

    @Test
    public void add_shouldReturnTrueWhenTransactionTimestampIsCurrentTimestamp() {

        //given
        doNothing().when(applicationEventPublisher).publishEvent(any());
        Transaction transaction = new Transaction(100d, System.currentTimeMillis());

        //when
        Boolean result = classUnderTest.add(transaction);

        //then
        assertTrue(result);
    }

    @Test
    public void add_shouldReturnFalseWhenTransactionTimestampIsOutdated() {

        //given
        Long outdatedTimestamp = System.currentTimeMillis() - 61000L;
        Transaction transaction = new Transaction(100d, outdatedTimestamp);

        //when
        Boolean result = classUnderTest.add(transaction);

        //then
        assertFalse(result);

    }

    @Test
    public void add_shouldCallApplicationEventPublisherOnce() {

        //given
        doNothing().when(applicationEventPublisher).publishEvent(any());
        Transaction transaction = new Transaction(200d, System.currentTimeMillis());

        //when
        classUnderTest.add(transaction);

        //then
        verify(applicationEventPublisher, times(1))
                .publishEvent(transaction);
    }

}