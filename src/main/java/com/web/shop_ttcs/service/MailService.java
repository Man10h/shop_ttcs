package com.web.shop_ttcs.service;

public interface MailService {
    public void send(String to, String subject, String html);
}
