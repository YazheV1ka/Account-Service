package account.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class NewPasswordRequest {
    @JsonProperty(value = "new_password")
    @Size(min = 12, message = "Password length must be 12 chars minimum!")
    @NotNull
    private String newPassword;
}