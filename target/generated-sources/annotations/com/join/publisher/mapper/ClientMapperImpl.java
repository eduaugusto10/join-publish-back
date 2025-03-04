package com.join.publisher.mapper;

import com.join.publisher.models.Client;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-04T20:42:02-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.6 (Amazon.com Inc.)"
)
@Component
public class ClientMapperImpl implements ClientMapper {

    @Override
    public void toUpdate(Client existingClient, Client newClient) {
        if ( newClient == null ) {
            return;
        }

        if ( newClient.getName() != null ) {
            existingClient.setName( newClient.getName() );
        }
        if ( newClient.getEmail() != null ) {
            existingClient.setEmail( newClient.getEmail() );
        }
        if ( newClient.getCpf() != null ) {
            existingClient.setCpf( newClient.getCpf() );
        }
        if ( newClient.getPhone() != null ) {
            existingClient.setPhone( newClient.getPhone() );
        }
    }
}
