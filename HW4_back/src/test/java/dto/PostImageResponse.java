
package dto;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Generated;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "data",
    "success",
    "status"
})
@Generated("jsonschema2pojo")
public class PostImageResponse implements Serializable
{

    @JsonProperty("data")
    private Data data;
    @JsonProperty("success")
    private Boolean success;
    @JsonProperty("status")
    private Integer status;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -5585864587621314453L;



}
