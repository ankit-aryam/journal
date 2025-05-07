package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.JournalEntryServices;
import net.engineeringdigest.journalApp.service.UserServices;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryServices journalEntryServices;

    @Autowired
    private UserServices userServices;

    @GetMapping("/{userName}")
    public ResponseEntity getAllJournalEntriesOfUser(@PathVariable String userName){

        User user = userServices.findByUserName(userName);
        List all = user.getJournalEntries();
        if(all!=null && !all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{userName}")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry, @PathVariable String userName){

        try{
            myEntry.setDate(LocalDateTime.now());
            journalEntryServices.saveEntry(myEntry, userName);
            return new ResponseEntity<>(myEntry, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myId){

//        return journalEntryServices.findById(myId).orElse(null);
        Optional<JournalEntry> journalEntry = journalEntryServices.findById(myId);
        if(journalEntry.isPresent()){
            return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{userName}/{id}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId id, @PathVariable String userName){
        try {
            journalEntryServices.deleteById(id,userName);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

//    @PutMapping("id/{id}")
//    public JournalEntry updateJournalEntryById(@PathVariable ObjectId id, @RequestBody JournalEntry myEntry){
//        JournalEntry old = journalEntryServices.findById(id).orElse(null);
//
//        while(old!=null){
//            old.setTitle(myEntry.getTitle() != null && !myEntry.getTitle().equals("") ? myEntry.getTitle() : old.getTitle());
//            old.setContent(myEntry.getContent() != null && !myEntry.getContent().equals("") ? myEntry.getContent() : old.getContent());
//        }
//        journalEntryServices.saveEntry(old);
//        return old;
//    }
}
