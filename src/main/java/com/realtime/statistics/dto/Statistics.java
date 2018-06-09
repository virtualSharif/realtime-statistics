package com.realtime.statistics.dto;

import lombok.Data;

@Data
public class Statistics {

    private Long timestamp;

    private Double sum;

    private Double max;

    private Double min;

    private Long count;
}
