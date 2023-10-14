package br.com.viniciuasAM.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.viniciuasAM.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ItaskRespository ItaskRespository;
    

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskmodel,HttpServletRequest request){
        System.out.println("chegou no controller");
        var idUser = request.getAttribute("idUser");
        taskmodel.setIdUser((UUID) idUser );

        var currentDate = LocalDateTime.now();
        if(currentDate.isAfter(taskmodel.getStartAT())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("data de inicio/termino deve ser maior do que a atual ");
        }


        var task = this.ItaskRespository.save(taskmodel);
        return  ResponseEntity.status(HttpStatus.OK).body(task);
    }
    @GetMapping("/")
    public List<TaskModel> list(HttpServletRequest request){
        var idUser = request.getAttribute("idUser");
         var tasks  =this.ItaskRespository.findByIdUser((UUID )idUser);
        return tasks;
    }

    @PutMapping("/{id}")
     public ResponseEntity update(@RequestBody TaskModel taskModel,@PathVariable UUID id,HttpServletRequest request){
        var task = this.ItaskRespository.findById(id).orElse(null);

        if(task == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("tarefa não encontrada");
        }

        var idUser = request.getAttribute("idUser");
        if(!task.getIdUser().equals(idUser)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("usuario não tem autorização para acessar isso");
        }

        Utils.copyNULpLProerties(taskModel,task);
        
        var tasks = this.ItaskRespository.save(taskModel);
        return ResponseEntity.ok(tasks);
       
     }
     
}
