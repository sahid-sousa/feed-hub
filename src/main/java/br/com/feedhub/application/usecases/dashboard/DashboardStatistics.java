package br.com.feedhub.application.usecases.dashboard;

import br.com.feedhub.interfaces.dto.response.DashboardResponse;
import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.servlet.http.HttpServletRequest;

public interface DashboardStatistics {
    DashboardResponse execute(Integer startMonth, Integer endMonth, HttpServletRequest request);
}
