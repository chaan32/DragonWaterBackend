package com.dragonwater.backend.Config.Exception.New.Exception.specific.ETC;

import com.dragonwater.backend.Config.Exception.New.ErrorCode;
import com.dragonwater.backend.Config.Exception.New.Exception.type.ServiceException;

public class DuplicateCommentException extends ServiceException {
    public DuplicateCommentException() {
        super(ErrorCode.DUPLICATE_COMMENT, ErrorCode.DUPLICATE_COMMENT.getMessage());
    }

    public DuplicateCommentException(String message) {
        super(ErrorCode.DUPLICATE_COMMENT, message);
    }
}
