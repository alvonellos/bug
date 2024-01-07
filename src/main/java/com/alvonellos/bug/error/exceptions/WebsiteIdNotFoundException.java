package com.alvonellos.bug.error.exceptions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = "id", callSuper = true)
public class WebsiteIdNotFoundException extends WebsiteAPIException {
    private final Long id;

    public WebsiteIdNotFoundException(final Long id) {
        super("Interview with id " + id + " not found");
        this.id = id;
    }
}
