package io.github.gustacodes.msclientes.application;

import io.github.gustacodes.msclientes.application.representation.ClienteSaveRequest;
import io.github.gustacodes.msclientes.domain.Cliente;
import io.github.gustacodes.msclientes.services.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody ClienteSaveRequest saveRequest) {
        var cliente = saveRequest.toModel();
        clienteService.save(cliente);
        URI headerLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .query("cpf={cpf}")
                .buildAndExpand(cliente.getCpf())
                .toUri();
        return ResponseEntity.status(HttpStatus.CREATED).body(headerLocation);
    }

    @GetMapping
    public ResponseEntity<?> dadosDoCliente(@RequestParam("cpf") String cpf) {
        var cliente = clienteService.getByCpf(cpf);
        if (cliente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(cliente);
    }
}
