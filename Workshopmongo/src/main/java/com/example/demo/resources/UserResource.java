package com.example.demo.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.domain.Post;
import com.example.demo.domain.User;
import com.example.demo.dto.UserDTO;
import com.example.demo.services.UserService;

// Define que esta classe é um controlador REST e mapeia todas as requisições para "/users" para este controlador
@RequestMapping(value = "/users")
@RestController
public class UserResource {

	// Injeção de dependência do serviço UserService
	@Autowired
	private UserService service;

	// Mapeia requisições HTTP GET para o método findAll
	@GetMapping
	public ResponseEntity<List<UserDTO>> findAll() {
		// Chama o método findAll do serviço para obter uma lista de usuários
		List<User> list = service.findAll();

		// Converte a lista de entidades User para uma lista de DTOs (UserDTO)
		List<UserDTO> listDto = list.stream().map(x -> new UserDTO(x)).collect(Collectors.toList());

		// Retorna uma resposta HTTP OK (200) contendo a lista de DTOs no corpo da
		// resposta
		return ResponseEntity.ok().body(listDto);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<UserDTO> findById(@PathVariable String id) {
		User obj = service.findById(id);
		return ResponseEntity.ok().body(new UserDTO(obj));
	}

	@PostMapping
	public ResponseEntity<Void> insert(@RequestBody UserDTO objDto) {
		// Converte o objeto DTO (UserDTO) em uma entidade User usando o método fromDTO
		// do serviço
		User obj = service.fromDTO(objDto);

		// Chama o serviço para inserir o novo usuário no banco de dados
		obj = service.insert(obj);

		// Cria a URI (Uniform Resource Identifier) para a resposta, incluindo o ID do
		// novo usuário
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

		// Retorna uma resposta HTTP Created (código 201) com a URI do recurso
		// recém-criado no cabeçalho da resposta
		return ResponseEntity.created(uri).build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<UserDTO> delete(@PathVariable String id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> update(@RequestBody UserDTO objDto, @PathVariable String id) {
		User obj = service.fromDTO(objDto);
		obj.setId(id);
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping(value = "/{id}/posts")
	public ResponseEntity<List<Post>> findPosts(@PathVariable String id) {
		User obj = service.findById(id);
		return ResponseEntity.ok().body(obj.getPosts());
	}


}
