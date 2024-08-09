package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view;

import java.net.URI;
import java.util.Objects;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.ValidationError;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ApiErrorResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-08-09T13:32:51.493460+02:00[Europe/Paris]", comments = "Generator version: 7.7.0")
public class ApiErrorResponse {

  private String timestamp;

  private Integer code;

  private String status;

  private Boolean success;

  private String reason;

  private String developerMessage;

  private String path;

  private UUID trackId;

  @Valid
  private List<@Valid ValidationError> error = new ArrayList<>();

  public ApiErrorResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ApiErrorResponse(String timestamp, Integer code, String status, Boolean success, String reason, String path, UUID trackId) {
    this.timestamp = timestamp;
    this.code = code;
    this.status = status;
    this.success = success;
    this.reason = reason;
    this.path = path;
    this.trackId = trackId;
  }

  public ApiErrorResponse timestamp(String timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @NotNull 
  @Schema(name = "timestamp", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("timestamp")
  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public ApiErrorResponse code(Integer code) {
    this.code = code;
    return this;
  }

  /**
   * Get code
   * @return code
   */
  @NotNull 
  @Schema(name = "code", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("code")
  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public ApiErrorResponse status(String status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public ApiErrorResponse success(Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  @NotNull 
  @Schema(name = "success", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("success")
  public Boolean getSuccess() {
    return success;
  }

  public void setSuccess(Boolean success) {
    this.success = success;
  }

  public ApiErrorResponse reason(String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  @NotNull 
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reason")
  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public ApiErrorResponse developerMessage(String developerMessage) {
    this.developerMessage = developerMessage;
    return this;
  }

  /**
   * Get developerMessage
   * @return developerMessage
   */
  
  @Schema(name = "developerMessage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("developerMessage")
  public String getDeveloperMessage() {
    return developerMessage;
  }

  public void setDeveloperMessage(String developerMessage) {
    this.developerMessage = developerMessage;
  }

  public ApiErrorResponse path(String path) {
    this.path = path;
    return this;
  }

  /**
   * Get path
   * @return path
   */
  @NotNull 
  @Schema(name = "path", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("path")
  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public ApiErrorResponse trackId(UUID trackId) {
    this.trackId = trackId;
    return this;
  }

  /**
   * Get trackId
   * @return trackId
   */
  @NotNull @Valid 
  @Schema(name = "trackId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("trackId")
  public UUID getTrackId() {
    return trackId;
  }

  public void setTrackId(UUID trackId) {
    this.trackId = trackId;
  }

  public ApiErrorResponse error(List<@Valid ValidationError> error) {
    this.error = error;
    return this;
  }

  public ApiErrorResponse addErrorItem(ValidationError errorItem) {
    if (this.error == null) {
      this.error = new ArrayList<>();
    }
    this.error.add(errorItem);
    return this;
  }

  /**
   * Get error
   * @return error
   */
  @Valid 
  @Schema(name = "error", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("error")
  public List<@Valid ValidationError> getError() {
    return error;
  }

  public void setError(List<@Valid ValidationError> error) {
    this.error = error;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApiErrorResponse apiErrorResponse = (ApiErrorResponse) o;
    return Objects.equals(this.timestamp, apiErrorResponse.timestamp) &&
        Objects.equals(this.code, apiErrorResponse.code) &&
        Objects.equals(this.status, apiErrorResponse.status) &&
        Objects.equals(this.success, apiErrorResponse.success) &&
        Objects.equals(this.reason, apiErrorResponse.reason) &&
        Objects.equals(this.developerMessage, apiErrorResponse.developerMessage) &&
        Objects.equals(this.path, apiErrorResponse.path) &&
        Objects.equals(this.trackId, apiErrorResponse.trackId) &&
        Objects.equals(this.error, apiErrorResponse.error);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, code, status, success, reason, developerMessage, path, trackId, error);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApiErrorResponse {\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    developerMessage: ").append(toIndentedString(developerMessage)).append("\n");
    sb.append("    path: ").append(toIndentedString(path)).append("\n");
    sb.append("    trackId: ").append(toIndentedString(trackId)).append("\n");
    sb.append("    error: ").append(toIndentedString(error)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

