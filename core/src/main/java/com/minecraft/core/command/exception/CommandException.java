/*
 * Copyright (C) Pedrudo (persona), All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.minecraft.core.command.exception;

import com.minecraft.core.command.message.MessageType;
import lombok.NoArgsConstructor;

/**
 * The CommandException is the default exception thrown
 * by the framework if any errors are thrown during the
 * execution of a command.
 */
@NoArgsConstructor
public class CommandException extends RuntimeException {

    private MessageType messageType;

    public CommandException(final MessageType messageType, final String message) {
        super(message);
        this.messageType = messageType;
    }

    public CommandException(final Throwable cause) {
        super(cause);
    }

    public CommandException(final String message) {
        super(message);
    }

    public MessageType getMessageType() {
        return messageType;
    }

}