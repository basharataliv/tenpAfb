package com.afba.imageplus.dto.res.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SSN {
    String ssn;
    String lastName;
    String firstName;
    List<Policy> policies;
}
