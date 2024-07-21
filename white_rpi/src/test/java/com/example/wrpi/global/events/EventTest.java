package com.example.wrpi.global.events;

import com.example.wrpi.domain.Events.entity.Event;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
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

    @Test
    @Parameters
    public void testFree(int basePrice,int maxPrice,boolean isFree){

        //Given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();

        //When
        event.update();

        //Then
        assertThat(event.isFree()).isFalse();

    }

    private Object[] parametersForTestFree(){
      return new Object[]{
        new Object[]{0,0,true},
              new Object[]{100,0,false},
              new Object[]{0,100,false},
              new Object[]{100,200,false}

      };
    }

    @Test
    @Parameters
    public void testofline(String location,boolean isOffline){
        //Given
        Event event = Event.builder()
                .location(location)
                .build();
        //when
            event.update();

        //then
        assertThat(event.isOffline()).isEqualTo(isOffline);
    }

    private Object[] parametersForTestOffline(){
        return new Object[]{
                new Object[]{"강남",true},
                new Object[]{null,false},
                new Object[]{"       ",false}
        };
    }

}