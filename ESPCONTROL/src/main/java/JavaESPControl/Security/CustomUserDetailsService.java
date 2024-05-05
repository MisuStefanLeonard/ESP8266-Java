/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JavaESPControl.Security;

import JavaESPControl.Models.Accounts;
import JavaESPControl.Models.Roles;
import JavaESPControl.Models.UserRoles;
import JavaESPControl.Repository.AccountsRepository;
import JavaESPControl.Repository.RolesRepository;
import JavaESPControl.Repository.UserRolesRepository;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author misustefan
 */
@Service
public class CustomUserDetailsService implements UserDetailsService{
    
    private final AccountsRepository accRepo;
    private final RolesRepository rolesRepository;
    private final UserRolesRepository userRolesRepository;
    
    @Autowired
    public CustomUserDetailsService(AccountsRepository repo,
                                    RolesRepository rolesRepository,
                                    UserRolesRepository userRolesRepository
                                    ){
        accRepo = repo;
        this.rolesRepository = rolesRepository;
        this.userRolesRepository= userRolesRepository;
        
    }
    
  
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Accounts account = accRepo.findByemail(email);
        if(account != null) {
            Set<GrantedAuthority> auth = new HashSet<>();
            List<UserRoles> currentUserRoles = userRolesRepository.findAllByaccount(account);

            for(UserRoles us : currentUserRoles) {

                auth.add(new SimpleGrantedAuthority(us.getID_ROLE_FK().getRoleName()));
            }

            return new org.springframework.security.core.userdetails.User(
                    account.getUsername(), 
                    account.getPassword(),
                    auth);
        } else {
            throw new UsernameNotFoundException("Invalid username or password");
        }
    }

}
