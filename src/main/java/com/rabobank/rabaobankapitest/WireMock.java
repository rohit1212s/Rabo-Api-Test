package com.rabobank.rabaobankapitest;



import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

public class WireMock {

    private WireMockServer wireMockServer;

    public WireMock() {
    	
    	String testResource =System.getProperty("user.dir")+"\\mockserver";
    	//System.out.println(testResource);
       
        wireMockServer = new WireMockServer(WireMockConfiguration.options().
                port(8089).
                usingFilesUnderDirectory(testResource).
                disableRequestJournal());
    }

    public void start() {
        wireMockServer.start();
        System.out.println("wiremock started");
    }

    public void stop() {
        wireMockServer.stop();
        System.out.println("wiremock stopped");
    }
}