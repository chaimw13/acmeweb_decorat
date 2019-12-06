/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.acme.statusmgr;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Concurrency/performance test that runs 6 tests repeatedly and concurrently, and measures
 * and prints out throughput in terms of requests handled per second.
  */
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Execution(CONCURRENT)
public class ServerStatusConcurrentControllerTests {

    protected static final Logger LOGGER = LoggerFactory.getLogger("ACMEWEB_PERFORMANCE");

    // How many times to repeat each test
    final static int numRepeats = 20;

    @Autowired
    private MockMvc mockMvc;

    // counter for total number of requests processes.
    static final AtomicLong numRequests = new AtomicLong();

    // Remember start time for response time calculation
    static final AtomicLong startTime = new AtomicLong();


    /**
     * Set up for transaction timing
     */
    @BeforeAll     // NOT BeforeEach
    static void setUpAll() {
        numRequests.set(0);
        startTime.set(System.currentTimeMillis());
    }

    /**
     * Compute and display transaction timing
     */
    @AfterAll
    static void tearDownAll() {
        long execTime = System.currentTimeMillis() - startTime.get();

        LOGGER.info("Executed {} requests in {} milliseconds, request rate of {} reqs per millisec",
                numRequests, execTime, (numRequests.get()  * 1000.0) / execTime);
    }


    @RepeatedTest(numRepeats)
    public void MIKEshouldReturn_Basic_Ext() throws Exception {

        this.mockMvc.perform(get("/server/status/detailed?details=extensions")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.statusDesc").value("Server is up, and is using these extensions - [Hypervisor, Kubernetes, RAID-6]"));

        numRequests.incrementAndGet();
    }

    @RepeatedTest(numRepeats)
    public void MIKEshouldReturn_Basic_Mem() throws Exception {

        this.mockMvc.perform(get("/server/status/detailed?details=memory")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.statusDesc").value("Server is up, and its memory is Running low"));

        numRequests.incrementAndGet();
    }

    @RepeatedTest(numRepeats)
    public void MIKEshouldReturn_Basic_Op() throws Exception {

        this.mockMvc.perform(get("/server/status/detailed?details=operations")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.statusDesc").value("Server is up, and is operating normally"));

        numRequests.incrementAndGet();
    }

    @RepeatedTest(numRepeats)
    public void MIKEshouldReturn_Basic_Mem_Op() throws Exception {

        this.mockMvc.perform(get("/server/status/detailed?details=memory,operations")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.statusDesc").value("Server is up, and its memory is Running low, and is operating normally"));

        numRequests.incrementAndGet();
    }

    @RepeatedTest(numRepeats)
    public void MIKEshouldReturn_Basic_MemExt() throws Exception {

        this.mockMvc.perform(get("/server/status/detailed?details=memory,extensions")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.statusDesc").value("Server is up, and its memory is Running low, and is using these extensions - [Hypervisor, Kubernetes, RAID-6]"));

        numRequests.incrementAndGet();
    }

    @RepeatedTest(numRepeats)
    public void MIKEshouldReturn_Basic_Op_Ext_Mem() throws Exception {

        this.mockMvc.perform(get("/server/status/detailed?details=operations,extensions,memory")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.statusDesc").value("Server is up, and is operating normally, and is using these extensions - [Hypervisor, Kubernetes, RAID-6], and its memory is Running low"));

        numRequests.incrementAndGet();
    }




}
