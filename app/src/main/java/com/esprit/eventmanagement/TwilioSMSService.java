package com.esprit.eventmanagement;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.FormBody;
import okhttp3.Credentials;
import java.io.IOException;

public class TwilioSMSService {

    private static final String ACCOUNT_SID = "AC6f8458a4c5a9390aa02fb7188102e896";
    private static final String AUTH_TOKEN = "a09cd80f795a0329ab99747cf60dc952";
    private static final String TWILIO_PHONE_NUMBER = "+18602724405";

    private OkHttpClient client = new OkHttpClient();

    public void sendSms(String toPhoneNumber, String messageBody) {
        String url = "https://api.twilio.com/2010-04-01/Accounts/" + ACCOUNT_SID + "/Messages.json";

        FormBody formBody = new FormBody.Builder()
                .add("From", TWILIO_PHONE_NUMBER) // Assurez-vous que c'est un numéro valide
                .add("To", toPhoneNumber) // Assurez-vous que c'est un numéro valide
                .add("Body", messageBody) // Assurez-vous que le message est correct
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .addHeader("Authorization", Credentials.basic(ACCOUNT_SID, AUTH_TOKEN))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            System.out.println("Message sent successfully: " + response.body().string());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
