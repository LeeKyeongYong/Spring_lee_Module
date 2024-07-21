package com.example.wrpi.global.events;

import com.example.wrpi.domain.Events.entity.Event;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class EventTest {

    @Test
    public void builder(){
        Event event =  Event.builder()
                .name("Spring rest api")
                .description("Rest API development with Spring")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean(){

        //Given
        String name="Event";
        String description="Spring";

        //when
        Event event = new Event();
        event.setName(name);
        event.setDescription(description);

        //Then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);

    }
}