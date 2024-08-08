package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * CreateUomViewResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-08-08T23:46:14.765854+02:00[Europe/Paris]", comments = "Generator version: 7.7.0")
public class CreateUomViewResponse {

  private UUID id;

  private String name;

  private UUID uomCategoryId;

  /**
   * Gets or Sets uomType
   */
  public enum UomTypeEnum {
    LOWER("LOWER"),
    
    REFERENCE("REFERENCE"),
    
    GREATER("GREATER");

    private String value;

    UomTypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static UomTypeEnum fromValue(String value) {
      for (UomTypeEnum b : UomTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private UomTypeEnum uomType;

  private Double ratio;

  private Boolean active;

  public CreateUomViewResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreateUomViewResponse(UUID id, String name, UUID uomCategoryId, UomTypeEnum uomType, Double ratio, Boolean active) {
    this.id = id;
    this.name = name;
    this.uomCategoryId = uomCategoryId;
    this.uomType = uomType;
    this.ratio = ratio;
    this.active = active;
  }

  public CreateUomViewResponse id(UUID id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  @NotNull @Valid 
  @Schema(name = "id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public CreateUomViewResponse name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull 
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public CreateUomViewResponse uomCategoryId(UUID uomCategoryId) {
    this.uomCategoryId = uomCategoryId;
    return this;
  }

  /**
   * Get uomCategoryId
   * @return uomCategoryId
   */
  @NotNull @Valid 
  @Schema(name = "uomCategoryId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("uomCategoryId")
  public UUID getUomCategoryId() {
    return uomCategoryId;
  }

  public void setUomCategoryId(UUID uomCategoryId) {
    this.uomCategoryId = uomCategoryId;
  }

  public CreateUomViewResponse uomType(UomTypeEnum uomType) {
    this.uomType = uomType;
    return this;
  }

  /**
   * Get uomType
   * @return uomType
   */
  @NotNull 
  @Schema(name = "uomType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("uomType")
  public UomTypeEnum getUomType() {
    return uomType;
  }

  public void setUomType(UomTypeEnum uomType) {
    this.uomType = uomType;
  }

  public CreateUomViewResponse ratio(Double ratio) {
    this.ratio = ratio;
    return this;
  }

  /**
   * Get ratio
   * @return ratio
   */
  @NotNull 
  @Schema(name = "ratio", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("ratio")
  public Double getRatio() {
    return ratio;
  }

  public void setRatio(Double ratio) {
    this.ratio = ratio;
  }

  public CreateUomViewResponse active(Boolean active) {
    this.active = active;
    return this;
  }

  /**
   * Get active
   * @return active
   */
  @NotNull 
  @Schema(name = "active", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("active")
  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateUomViewResponse createUomViewResponse = (CreateUomViewResponse) o;
    return Objects.equals(this.id, createUomViewResponse.id) &&
        Objects.equals(this.name, createUomViewResponse.name) &&
        Objects.equals(this.uomCategoryId, createUomViewResponse.uomCategoryId) &&
        Objects.equals(this.uomType, createUomViewResponse.uomType) &&
        Objects.equals(this.ratio, createUomViewResponse.ratio) &&
        Objects.equals(this.active, createUomViewResponse.active);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, uomCategoryId, uomType, ratio, active);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateUomViewResponse {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    uomCategoryId: ").append(toIndentedString(uomCategoryId)).append("\n");
    sb.append("    uomType: ").append(toIndentedString(uomType)).append("\n");
    sb.append("    ratio: ").append(toIndentedString(ratio)).append("\n");
    sb.append("    active: ").append(toIndentedString(active)).append("\n");
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

