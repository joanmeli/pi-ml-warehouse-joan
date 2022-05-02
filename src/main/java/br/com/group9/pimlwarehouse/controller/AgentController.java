package br.com.group9.pimlwarehouse.controller;

import br.com.group9.pimlwarehouse.dto.AgentDTO;
import br.com.group9.pimlwarehouse.service.AuthenticationAPIService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AgentController extends APIController {
    private static final String BASE_PATH = "agent";
    private AuthenticationAPIService authenticationAPIService;

    public AgentController(AuthenticationAPIService authenticationAPIService) {
        this.authenticationAPIService = authenticationAPIService;
    }

    /**
     * POST method to create agent
     * @param agentDTO returns the registered data of the agent
     * @return returns agent payload created and status "201-Created"
     */
    @PostMapping(BASE_PATH)
    public ResponseEntity<AgentDTO> createAgent(@Valid @RequestBody AgentDTO agentDTO) {
        AgentDTO agent = this.authenticationAPIService.createAgent(agentDTO);
        return new ResponseEntity<>(agent, HttpStatus.CREATED);
    }
}
