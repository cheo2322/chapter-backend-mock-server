package com.pichincha.mock.server.webclient;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;

import static org.junit.jupiter.api.Assertions.*;

class DemoWebClientTest {

    private ClientAndServer mockServer;

    @BeforeEach
    public void setupMockServer() {
        mockServer = ClientAndServer.startClientAndServer(2001);
    }

    @AfterEach
    public void tearDownServer() {
        mockServer.stop();
    }

    @Test
    void getTransactions() {
    }
}