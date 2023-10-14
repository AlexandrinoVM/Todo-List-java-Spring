package br.com.viniciuasAM.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUuserRepository uuserRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel){
        var user =this.uuserRepository.findByUsername(userModel.getUsername());
        if(user != null){
            System.out.println("usurio ja existe ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("USUARIO JA EXISTE");
        }

       var passwordHashad = BCrypt.withDefaults()
       .hashToString(12,userModel.getPassword().toCharArray());

        userModel.setPassword(passwordHashad);

       var usercreated = this.uuserRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.OK).body(usercreated);
    }
}
