package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.security.jwt.JwtUtils;
import com.example.demo.service.MapStructMappper;
import com.example.demo.service.RoleRepository;
import com.example.demo.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@EnableScheduling
@RestController   // מאפר לתת כתובת לפונקציות
@RequestMapping("/api/user")
@CrossOrigin(value = "http://localhost:5173", allowCredentials = "true")
// מסיר הבטחה משום שהשרת שלנו לא מאובטח וכך כשמסירים את ההבטחה ניתן לגשת אליו
public class UserController {

    public class WeekCounter {
        ////////גרף
        public static int getCurrentWeekOfMonth(LocalDate currentDate) {
            return currentDate.get(WeekFields.of(Locale.getDefault()).weekOfMonth());
        }
    }

    private UserRepository userRepository;

    private MapStructMappper mapper;

    private JwtUtils jwtUtils;
    // מנהל האישורים
    AuthenticationManager authenticationManager;
    private RoleRepository roleRepository;

    private static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "\\images\\";


    @Autowired
    public UserController(UserRepository userRepository, MapStructMappper mapper, JwtUtils jwtUtils, RoleRepository roleRepository, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.jwtUtils = jwtUtils;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
    }

    // הוספת משתמש חדש
    @PostMapping("/signUp")
    public ResponseEntity<User> createUser(@RequestBody User u) {
        User user = userRepository.findByEmail(u.getEmail());
        if (user == null) {
            String pass = u.getPassword();
            u.setPassword(new BCryptPasswordEncoder(8).encode(pass));
            u.getRoles().add(roleRepository.findById((long) 2).get());
            User newUser = userRepository.save(u);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);

        } else if (user != null) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<User> updateSharing(@PathVariable long id, @RequestBody User user) {
        User u = userRepository.findById(id).orElse(null);
        if (user != null) {
            u.setEmail(user.getEmail());
            u.setFirstName(user.getFirstName());
            u.setPassword(user.getPassword());
            u.setLastName(user.getLastName());
            u.setId(user.getId());
            u.setSharings(user.getSharings());
            u.setComments(user.getComments());

            userRepository.save(u);
            return new ResponseEntity<>(u, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }

    @GetMapping("/dugma")
    public String getDugma() {
        return "hello";
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity deleteUser(@PathVariable long id) {
        try {
            userRepository.deleteById(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getUser")
    public ResponseEntity<List<User>> getUser() {
        try {
            List<User> user = new ArrayList<>();
            userRepository.findAll().forEach(u -> user.add(u));
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getUser/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else { //יחזיר שגיאה 404
            return new ResponseEntity<>(user, HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/uploadUser")
    public ResponseEntity uploadUserWithImage(@RequestPart("image") MultipartFile file, @RequestPart("user") User u) {
        try {
            String filePath = UPLOAD_DIRECTORY + file.getOriginalFilename();
            Path fileName = Paths.get(filePath); // הנתיב בו נשמור את התמונה
            Files.write(fileName, file.getBytes());
            u.setImage(filePath);
            User newSharing = userRepository.save(u);
            return new ResponseEntity(newSharing, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity(u, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody User u) {

        //מייצר טוקן
        //מבצע בדיקה האם האימות בוצע בהצלחה
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(u.getEmail(), u.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //מחזיר את היוזר כאובייקט עטוף
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        //מחזיר עוגיה של jwt לפי פרטי המשתמש
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(userDetails.getUsername());
    }
//    public ResponseEntity<User> check(@RequestBody User u) throws IOException {
//        User user = userRepository.findByEmail(u.getEmail());
////        System.out.println(user);
//        if (user != null && user.getPassword().equals(u.getPassword())) {//mail & pass correct
//            return new ResponseEntity<>(user, HttpStatus.OK);//200
//        } else if (user != null) {//mail correct & pass uncorrect
//            return new ResponseEntity<>(user, HttpStatus.CREATED);//201- יחזור סטטוס לא שגיאה אבל גם לא אוקי
//        } else//mail&password uncorrect- send to Sign UP
//            return new ResponseEntity<>(HttpStatus.ACCEPTED);//202
//    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOut() {
        ResponseCookie responseCookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body("you've been signed out");
    }

    ////////גרף
//    @Scheduled(cron = "0 */2 * * * *")
    @Scheduled(cron = "0 0 0 1 * *")
    public void res() {
        System.out.println("Function res() executed at: " + LocalDateTime.now());
        for (User user : userRepository.findAll()) {
            user.setCounts(new int[4]);
            userRepository.save(user);
        }
    }

    @PutMapping("/viewers/{id}")
    public ResponseEntity<User> updateV(@PathVariable long id) {
        User user = userRepository.findById(id).orElse(null);
        try {
            if (user != null) {
                LocalDate currentDate = LocalDate.now();
                System.out.println("day: " + currentDate.getDayOfMonth());
                int currentWeek = WeekCounter.getCurrentWeekOfMonth(currentDate);
                System.out.println("week: " + currentWeek);
                user.getCounts()[currentWeek - 1] += 1;
                userRepository.save(user);

            }
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(user, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


}
