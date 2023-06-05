package com.diplom.beautyshop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.diplom.beautyshop.core.WorkerDto;
import com.diplom.beautyshop.svc.WorkerSVC;

@Controller
@RequestMapping(value="/auth")
public class AuthControl {

	private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final WorkerSVC workerSVC;

    @Autowired
    public AuthControl(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, WorkerSVC workerSVC) {
    	super();
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.workerSVC = workerSVC;
    }
	
	@RequestMapping(method=RequestMethod.GET, value = "/login")
    public ResponseEntity login(@RequestParam(name="login", required=true) String login, @RequestParam(name="pass", required=true) String pass) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, pass));
            WorkerDto user = workerSVC.GetWorkerByLogin(login);

            if (user == null) {
                throw new UsernameNotFoundException("User with username: " + login + " not found");
            }

            List<String> roles = new ArrayList<String>();
            roles.add("ADMIN");
            String token = jwtTokenProvider.createToken(login, roles);

            Map<Object, Object> response = new HashMap<>();
            response.put("username", login);
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
	
}
