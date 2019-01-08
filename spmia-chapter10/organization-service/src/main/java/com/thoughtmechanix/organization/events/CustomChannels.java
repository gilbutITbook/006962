package com.thoughtmechanix.organization.events;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.SubscribableChannel;

public interface CustomChannels {
    @Output("outboundOrgChanges")
    SubscribableChannel outboundOrgChanges();
}
