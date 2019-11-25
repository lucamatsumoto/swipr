package com.swipr.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.swipr.matcher.SellQuery;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Interest {

    @JsonProperty("buyerId")
    private Integer buyerId;

    @JsonProperty("sellQuery")
    private SellQuery sellquery;

    @JsonCreator
    public Interest(Integer buyerId, SellQuery sellQuery) {
        this.buyerId = buyerId;
        this.sellquery = sellQuery;
    }

}