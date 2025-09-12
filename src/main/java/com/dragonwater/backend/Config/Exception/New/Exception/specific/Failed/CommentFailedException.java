package com.dragonwater.backend.Config.Exception.New.Exception.specific.Failed;

import com.dragonwater.backend.Config.Exception.New.ErrorCode;
import com.dragonwater.backend.Config.Exception.New.Exception.type.ServiceException;

public class CommentFailedException extends ServiceException {
  public CommentFailedException() {
    super(ErrorCode.COMMENT_FAILED, "댓글 작성에 실패했습니다.");
  }
}
