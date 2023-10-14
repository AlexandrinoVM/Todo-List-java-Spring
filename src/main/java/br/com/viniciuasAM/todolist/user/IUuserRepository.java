package br.com.viniciuasAM.todolist.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;



public interface IUuserRepository extends JpaRepository<UserModel, UUID>{
   UserModel findByUsername(String username);
}
