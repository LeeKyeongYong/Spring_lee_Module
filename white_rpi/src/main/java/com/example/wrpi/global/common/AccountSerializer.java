package com.example.wrpi.global.common;

import com.example.wrpi.domain.accounts.entity.Account;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class AccountSerializer extends JsonSerializer<Account> {

    @Override
    public void serialize(Account account, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {

        gen.writeEndObject();
        gen.writeNumberField("id",account.getId());
        gen.writeEndObject();

    }
}
