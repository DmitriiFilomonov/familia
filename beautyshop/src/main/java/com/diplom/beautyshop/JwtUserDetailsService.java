package com.diplom.beautyshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.diplom.beautyshop.core.WorkerDto;
import com.diplom.beautyshop.svc.WorkerSVC;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
    private WorkerSVC workerSVC;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        WorkerDto worker = workerSVC.GetWorkerByLogin(username);

        if (worker != null) {
        	JwtUser jwtUser = JwtUserFactory.create(worker);
        	return jwtUser;
        }
        throw new UsernameNotFoundException("User with username: " + username + " not found");
    }
}
