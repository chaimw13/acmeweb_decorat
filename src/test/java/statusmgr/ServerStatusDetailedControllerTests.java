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
package statusmgr;

import org.assertj.core.api.AssertJProxySetup;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;


import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ServerStatusDetailedControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturn_Basic_Ext() throws Exception {

        this.mockMvc.perform(get("/server/status/detailed?details=Extensions")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.statusDesc").value("Server is up, and is using these extensions - [Hypervisor, Kubernetes, RAID-6]"));
    }

    @Test
    public void shouldReturn_Basic_Mem() throws Exception {

        this.mockMvc.perform(get("/server/status/detailed?details=Memory")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.statusDesc").value("Server is up, and its memory is Running low"));
    }

    @Test
    public void shouldReturn_Basic_Op() throws Exception {

        this.mockMvc.perform(get("/server/status/detailed?details=Operations")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.statusDesc").value("Server is up, and is operating normally"));
    }

    @Test
    public void shouldReturn_Basic_Mem_Op() throws Exception {

        this.mockMvc.perform(get("/server/status/detailed?details=Memory,Operations")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.statusDesc").value("Server is up, and its memory is Running low, and is operating normally"));
    }

    @Test
    public void shouldReturn_Basic_MemExt() throws Exception {

        this.mockMvc.perform(get("/server/status/detailed?details=Memory,Extensions")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.statusDesc").value("Server is up, and its memory is Running low, and is using these extensions - [Hypervisor, Kubernetes, RAID-6]"));
    }

    @Test
    public void shouldReturn_Basic_Op_Ext_Mem() throws Exception {

        this.mockMvc.perform(get("/server/status/detailed?details=Operations,Extensions,Memory")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.statusDesc").value("Server is up, and is operating normally, and is using these extensions - [Hypervisor, Kubernetes, RAID-6], and its memory is Running low"));
    }


    @Test
    public void withParamShouldReturnTailored_Basic_Op_Ext_Mem() throws Exception {

        this.mockMvc.perform(get("/server/status/detailed?details=Operations,Extensions,Memory").param("name", "RebYid"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.contentHeader").value("Server Status requested by RebYid"))
                .andExpect(jsonPath("$.statusDesc").value("Server is up, and is operating normally, and is using these extensions - [Hypervisor, Kubernetes, RAID-6], and its memory is Running low"));
    }


    @Test
    public void withoutDetailsShouldReturnErrorBadRequest() throws Exception {

        this.mockMvc.perform(get("/server/status/detailed")).andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusDesc").doesNotExist())
                .andExpect(status().reason("Required List parameter 'details' is not present"));
    }

    @Test
    public void withInvalidDetailsShouldReturnErrorBadRequest() throws Exception {



        MvcResult result = this.mockMvc.perform(get("/server/status/detailed?details=InvalidDetailOption1")).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusDesc").doesNotExist())
                .andReturn();


        Assert.assertEquals(result.getResolvedException().getLocalizedMessage(), "Invalid details option: InvalidDetailOption1");

    }

}
