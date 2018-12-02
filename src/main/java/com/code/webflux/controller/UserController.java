package com.code.webflux.controller;

import com.code.webflux.domain.User;
import com.code.webflux.repository.UserRepository;
import com.code.webflux.utils.CheckUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.print.attribute.standard.Media;
import javax.validation.Valid;

/**
 * Created by yankefei on 2018/12/2.
 */

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //一次性返回数据
    @GetMapping("/")
    public Flux<User> getAll() {
        return this.userRepository.findAll();
    }

    //以SSE形式多次返回数据
    @GetMapping(value = "/stream/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> streamGetAll() {
        return this.userRepository.findAll();
    }

    @PostMapping("/")
    public Mono<User> createUser(@Valid @RequestBody User user) {
        //spring data jpa中新增和修改都是用save，主要根据id是否为null判断
        user.setId(null);
        CheckUtil.checkName(user.getName());
        return this.userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable("id") String id) {
        //deleteById没有返回值，不能判断数据是否存在
        //当需要操作数据，并返回一个Mono时，使用flatMap
        return this.userRepository.findById(id).flatMap(user -> this.userRepository.delete(user)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                .defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND)));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<User>> updateUser(@PathVariable("id") String id, @Valid @RequestBody User user) {
        return this.userRepository.findById(id)
                .flatMap(u -> {
                    u.setName(user.getName());
                    u.setAge(user.getAge());
                    return this.userRepository.save(u);
                }).map(user1 -> new ResponseEntity<User>(user1, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<User>> getUser(@PathVariable("id") String id) {
        return this.userRepository.findById(id)
                .map(u -> new ResponseEntity<User>(u, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/age/{start}/{end}")
    public Flux<User> getUserByAge(@PathVariable("start") int start, @PathVariable("end") int end) {
        return this.userRepository.findByAgeBetween(start, end);
    }

    @GetMapping(value = "/stream/age/{start}/{end}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> streamGetUserByAge(@PathVariable("start") int start, @PathVariable("end") int end) {
        return this.userRepository.findByAgeBetween(start, end);
    }

    @GetMapping("/age/old")
    public Flux<User> getOldUser() {
        return this.userRepository.findOldUser();
    }
}
