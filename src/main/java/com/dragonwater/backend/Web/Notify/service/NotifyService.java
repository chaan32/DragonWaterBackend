package com.dragonwater.backend.Web.Notify.service;

import com.dragonwater.backend.Web.User.Member.domain.Members;
import com.dragonwater.backend.Web.User.Member.domain.Role;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Member;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface NotifyService {

    void notifySuccessOrderToCustomerBySMS(String phone, String productName) throws JsonProcessingException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException;

    void notifyOrderToAdminBySMS(Role role) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException;

    void notifySuccessRegistration(Members member);
}
