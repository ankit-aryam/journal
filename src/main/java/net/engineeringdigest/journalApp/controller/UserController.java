package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.UserServices;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServices userServices;

    @GetMapping
    public List<User> getAll(){
        return userServices.getAll();
    }

    @PostMapping
    public void createUser(@RequestBody User user){
        userServices.saveUser(user);
    }

    @PutMapping("/{userName}")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable String userName){
        User userInDb = userServices.findByUserName(userName);
        if(userInDb!=null){
            userInDb.setUserName(user.getUserName());
            userInDb.setPassword(user.getPassword());
            userServices.saveUser(userInDb);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
