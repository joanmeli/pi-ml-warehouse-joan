package br.com.group9.pimlwarehouse.controller;

import br.com.group9.pimlwarehouse.dto.AgentDTO;
import br.com.group9.pimlwarehouse.dto.NewAgentDTO;
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
     * POST method to create agent.
     * @param agentDTO send agent data.
     * @return returns agent payload created and status "201-Created".
     */
    @PostMapping(BASE_PATH)
    public ResponseEntity<NewAgentDTO> createAgent(@Valid @RequestBody NewAgentDTO newAgentDTO) {
        AgentDTO agent = this.authenticationAPIService.createAgent(newAgentDTO.convert());
        return new ResponseEntity<>(agent.convert(), HttpStatus.CREATED);
    }
}
