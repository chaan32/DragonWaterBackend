package com.dragonwater.backend.Config.Exception.New.Exception.specific.Failed;

import com.dragonwater.backend.Config.Exception.New.ErrorCode;
import com.dragonwater.backend.Config.Exception.New.Exception.type.ServiceException;

public class ImageUploadFailedException extends ServiceException {
    public ImageUploadFailedException() {
        super(ErrorCode.FAILED_UPLOAD_IMAGE);
    }
}
