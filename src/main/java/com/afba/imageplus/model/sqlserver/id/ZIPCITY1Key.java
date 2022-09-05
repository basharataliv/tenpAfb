package com.afba.imageplus.model.sqlserver.id;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZIPCITY1Key implements Serializable {

    private Long zip1;

    private String city1;
}
