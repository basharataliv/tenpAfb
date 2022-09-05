package com.afba.imageplus.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsRes {
    private String policy;
    private String ssn;
    private String firstName;
    private String lastName;
}
