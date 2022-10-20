package com.d138.wheere.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Controller
public class PaymentController {

    @RequestMapping("/kakaopay.cls")
    public String kakaopay() {
        try {
            URL address = new URL("https://kapi.kakao.com/v1/payment/ready");
            HttpURLConnection serverConnection = (HttpURLConnection) address.openConnection();
            serverConnection.setRequestMethod("POST");
            serverConnection.setRequestProperty("Authorization", "KakaoAK ${bb5fd892ed6435b11eda1610cad90611}");
            serverConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            serverConnection.setDoOutput(true);

            String parameter = "cid=TC0ONETIME&partner_order_id=partner_order_id&partner_user_id=partner_user_id&item_name=bus_pay&quantity=1&total_amount=500&tax_free_amount=0&approval_url=http://localhost/reservation/pay&cancel_url=http://localhost/cancel&fail_url=http://localhost/fail";

            OutputStream giver = serverConnection.getOutputStream();
            DataOutputStream dataGiver = new DataOutputStream(giver);
            dataGiver.writeBytes(parameter);
            dataGiver.close();

            int resultCode = serverConnection.getResponseCode();

            InputStream receiver;

            if (resultCode == 200) {
                receiver = serverConnection.getInputStream();
            } else {
                receiver = serverConnection.getErrorStream();
            }

            InputStreamReader reader = new InputStreamReader(receiver);

            BufferedReader transformer = new BufferedReader(reader);

            System.out.println("testAPI");
            System.out.println(transformer.readLine());

            return "%";

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
