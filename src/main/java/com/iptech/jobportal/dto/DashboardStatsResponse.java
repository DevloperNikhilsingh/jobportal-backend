package com.iptech.jobportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsResponse {
    private Long totalJobs;
    private Long totalApplications;
    private Long totalRequests;
    private Long totalSubscribers;
}