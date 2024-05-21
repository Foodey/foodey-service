package com.foodey.server.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoggingService {

  private ObjectMapper mapper = new ObjectMapper();

  public void logRequestInfo(HttpServletRequest httpServletRequest) {
    StringBuilder data = createRequestInfo(httpServletRequest);

    log.info(data.toString());
  }

  public void logRequest(HttpServletRequest httpServletRequest, Object body) {
    try {
      if (httpServletRequest.getRequestURI().contains("medias")) {
        return;
      }
      StringBuilder data = createRequestInfo(httpServletRequest);

      if (body == null) data.append("[BODY REQUEST]: ").append("<empty>");
      else
        data.append("[BODY REQUEST]: ")
            .append("\n\n")
            .append(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(body));

      log.info(data.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private StringBuilder createRequestInfo(HttpServletRequest httpServletRequest) {
    StringBuilder data = new StringBuilder();
    data.append("\n\n--------------LOGGING REQUEST----------------\n")
        .append("[REQUEST-ID]: ")
        .append(httpServletRequest.getRequestId())
        .append("\n")
        .append("[PATH]: ")
        .append(httpServletRequest.getRequestURI())
        .append("\n")
        .append("[QUERIES]: ")
        .append(httpServletRequest.getQueryString())
        .append("\n")
        .append("[HEADERS]: ")
        .append("\n");

    httpServletRequest
        .getHeaderNames()
        .asIterator()
        .forEachRemaining(
            headerName -> {
              String key = headerName;
              String value = httpServletRequest.getHeader(key);
              data.append("---").append(key).append(" : ").append(value).append("\n");
            });

    return data;
  }

  public void logResponse(HttpServletRequest request, HttpServletResponse response, Object body) {
    try {
      if (request.getRequestURI().contains("medias")) {
        return;
      }

      StringBuilder data = new StringBuilder();
      data.append("\n\n---------------LOGGING RESPONSE----------------\n")
          .append("[REQUEST-ID]: ")
          .append(request.getRequestId())
          .append("\n")
          .append("[STATUS]: ")
          .append(response.getStatus())
          .append("\n");

      if (body != null) {
        data.append("[BODY RESPONSE]: ")
            .append("\n\n")
            .append(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(body));
      } else {
        data.append("[BODY RESPONSE]: <empty>");
      }

      data.append("\n---------------END LOGGING RESPONSE----------------\n");

      log.info(data.toString());
    } catch (Exception e) {
      log.error("Error while logging response", e);
    }
  }
}
