package com.dragonwater.backend.Config.Exception.New.Exception.specific.NotFound;

import com.dragonwater.backend.Config.Exception.New.ErrorCode;
import com.dragonwater.backend.Config.Exception.New.Exception.type.EntityException;

public class ImageNotFoundException extends EntityException {
    public ImageNotFoundException() {
        super(ErrorCode.IMAGE_NOT_FOUND, "이미지를 찾을 수 없습니다.");
    }
}