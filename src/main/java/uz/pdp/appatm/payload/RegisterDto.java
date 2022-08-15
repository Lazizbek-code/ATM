package uz.pdp.appatm.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class RegisterDto {
    @Size(min = 3, max = 50)
    @NotNull
    private String firstName;

    @Size(min = 3, max = 50)
    @NotNull
    private String lastName;

    @Email
    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private Set<Integer> roleId;
}
