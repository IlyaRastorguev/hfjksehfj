package utm.transport.app.core.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public abstract class BaseEntityDto {

    @ApiModelProperty(value = "Id")
    protected String id;

}
