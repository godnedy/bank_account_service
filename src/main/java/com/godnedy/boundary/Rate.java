package com.godnedy.boundary;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Rate {
    String no;
    String effectiveDate;
    Double bid;
    Double ask;
}
