package com.realtime.statistics.dto;

import lombok.Data;

@Data
public class StatisticsSummary {

    private Double sum;
    private Double max;
    private Double min;
    private Long count;
    private Double avg;

    public StatisticsSummary(Statistics statistics) {
        this.sum = statistics.getSum();
        this.max = statistics.getMax();
        this.min = statistics.getMin();
        this.count = statistics.getCount();
    }

    public StatisticsSummary() {
        this.sum = 0.00;
        this.max = Double.MIN_VALUE;
        this.min = Double.MAX_VALUE;
        this.count = 0L;
        this.avg = 0.00;
    }
}
