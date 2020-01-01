package com.bricklink.fulfillment.api.shipstation;

import com.bricklink.fulfillment.shipstation.model.Tag;
import feign.RequestLine;

import java.util.List;

public interface Accounts {
    @RequestLine("GET /accounts/listtags")
    List<Tag> listTags();
}
